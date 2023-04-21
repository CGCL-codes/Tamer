package org.jdiameter.client.impl.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.LocalAction;
import org.jdiameter.api.Realm;
import org.jdiameter.api.Statistic;
import org.jdiameter.client.api.IAnswer;
import org.jdiameter.client.api.IAssembler;
import org.jdiameter.client.api.IContainer;
import org.jdiameter.client.api.IMessage;
import org.jdiameter.client.api.IRequest;
import org.jdiameter.client.api.controller.IRealm;
import org.jdiameter.client.api.controller.IRealmTable;
import org.jdiameter.server.api.agent.IAgent;
import org.jdiameter.server.api.agent.IAgentConfiguration;
import org.jdiameter.server.api.agent.IProxy;
import org.jdiameter.server.api.agent.IRedirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RealmTableImpl implements IRealmTable {

    private static final Logger logger = LoggerFactory.getLogger(RealmTableImpl.class);

    protected Map<String, RealmSet> realmNameToRealmSet = new HashMap<String, RealmSet>();

    protected List<String> allRealmsSet = new ArrayList<String>();

    protected String localRealmName;

    protected String localHost;

    protected IAssembler assembler;

    public RealmTableImpl(IContainer con) {
        this.assembler = con.getAssemblerFacility();
    }

    public boolean realmExists(String realmName) {
        return (this.realmNameToRealmSet.containsKey(realmName) && this.realmNameToRealmSet.get(realmName).size() > 0);
    }

    public Realm addRealm(String realmName, ApplicationId applicationId, LocalAction action, String agentConfiguration, boolean dynamic, long expirationTime, String[] hosts) throws InternalException {
        logger.debug("Adding realm [{}] into network map", realmName);
        IAgentConfiguration agentConf = this.assembler.getComponentInstance(IAgentConfiguration.class);
        if (agentConf != null) {
            agentConf = agentConf.parse(agentConfiguration);
        }
        return addRealm(realmName, applicationId, action, agentConf, dynamic, expirationTime, hosts);
    }

    public Realm addRealm(String realmName, ApplicationId applicationId, LocalAction action, IAgentConfiguration agentConf, boolean dynamic, long expirationTime, String[] hosts) throws InternalException {
        IAgent agent = null;
        switch(action) {
            case LOCAL:
            case RELAY:
                break;
            case PROXY:
                agent = this.assembler.getComponentInstance(IProxy.class);
                break;
            case REDIRECT:
                agent = this.assembler.getComponentInstance(IRedirect.class);
                break;
        }
        RealmImpl realmImpl = new RealmImpl(realmName, applicationId, action, agent, agentConf, dynamic, expirationTime, hosts);
        addRealm(realmImpl);
        return realmImpl;
    }

    public Realm getRealm(String realmName, ApplicationId applicationId) {
        RealmSet rs = this.realmNameToRealmSet.get(realmName);
        return rs == null ? null : rs.getRealm(applicationId);
    }

    public Realm removeRealmApplicationId(String realmName, ApplicationId appId) {
        RealmSet set = this.realmNameToRealmSet.get(realmName);
        if (set != null) {
            Realm r = set.getRealm(appId);
            set.removeRealm(appId);
            if (set.size() == 0 && !realmName.equals(this.localRealmName)) {
                this.realmNameToRealmSet.remove(realmName);
                this.allRealmsSet.remove(realmName);
            }
            return r;
        }
        return null;
    }

    public Collection<Realm> removeRealm(String realmName) {
        RealmSet set = null;
        if (realmName.equals(this.localRealmName)) {
            set = this.realmNameToRealmSet.get(realmName);
        } else {
            set = this.realmNameToRealmSet.remove(realmName);
            if (set != null) {
                Collection<Realm> present = set.values();
                allRealmsSet.remove(realmName);
                return new ArrayList<Realm>(present);
            }
        }
        return null;
    }

    public Collection<Realm> getRealms(String realmName) {
        RealmSet set = this.realmNameToRealmSet.get(realmName);
        if (set != null) {
            Collection<Realm> present = set.values();
            return new ArrayList<Realm>(present);
        }
        return null;
    }

    public Collection<Realm> getRealms() {
        ArrayList<Realm> rss = new ArrayList<Realm>();
        Set<String> keys = new HashSet<String>(this.realmNameToRealmSet.keySet());
        for (String key : keys) {
            RealmSet rs = this.realmNameToRealmSet.get(key);
            rss.addAll(rs.values());
        }
        return rss;
    }

    public Realm matchRealm(IRequest request) {
        try {
            IMessage req = (IMessage) request;
            String destinationRealm = req.getAvps().getAvp(Avp.DESTINATION_REALM).getDiameterIdentity();
            return this.matchRealm(req, destinationRealm);
        } catch (Exception e) {
            logger.error("Unable to read Destination-Realm AVP to match realm to request", e);
        }
        return null;
    }

    public Realm matchRealm(IAnswer message, String destRealm) {
        return this.matchRealm((IMessage) message, destRealm);
    }

    public String getRealmForPeer(String fqdn) {
        Collection<Realm> realms = getRealms();
        for (Realm r : realms) {
            IRealm ir = (IRealm) r;
            if (ir.hasPeerName(fqdn)) {
                return ir.getName();
            }
        }
        return null;
    }

    /**
   * @param appId
   */
    public void addLocalApplicationId(ApplicationId appId) {
        RealmSet rs = getRealmSet(localRealmName, false);
        rs.addRealm(new RealmImpl(localRealmName, appId, LocalAction.LOCAL, null, null, true, -1, this.localHost) {

            public boolean isLocal() {
                return true;
            }
        });
    }

    /**
   * @param appId
   */
    public void removeLocalApplicationId(ApplicationId appId) {
        RealmSet rs = getRealmSet(localRealmName, false);
        Realm realm = rs.getRealm(appId);
        if (realm.isDynamic()) {
            rs.removeRealm(appId);
        }
    }

    /**
   * @param localRealm
   * @param fqdn
   */
    public void addLocalRealm(String localRealm, String fqdn) {
        this.localRealmName = localRealm;
        this.localHost = fqdn;
        getRealmSet(localRealm, true);
    }

    protected Realm matchRealm(IMessage message, String realm) {
        if (realmExists(realm)) {
            ApplicationId singleId = message.getSingleApplicationId();
            Realm r = getRealm(realm, singleId);
            if (r == null) {
                List<ApplicationId> appIds = message.getApplicationIdAvps();
                for (int index = 0; index < appIds.size(); index++) {
                    r = getRealm(realm, appIds.get(index));
                    if (r != null) {
                        break;
                    }
                }
            }
            return r;
        }
        return null;
    }

    protected void addRealm(Realm realm) throws InternalException {
        RealmSet rs = getRealmSet(realm.getName(), true);
        rs.addRealm(realm);
        allRealmsSet.add(realm.getName());
    }

    protected RealmSet getRealmSet(String pKey, boolean create) {
        RealmSet rs = realmNameToRealmSet.get(pKey);
        if (rs == null && create) {
            rs = new RealmSet();
            realmNameToRealmSet.put(pKey, rs);
        }
        return rs;
    }

    private class RealmSet {

        protected Map<ApplicationId, Realm> appIdToRealm = new HashMap<ApplicationId, Realm>();

        /**
     * @param realm
     * @throws InternalException
     */
        public void addRealm(Realm realm) {
            if (this.appIdToRealm.containsKey(realm.getApplicationId())) {
                Realm presentRealm = this.appIdToRealm.get(realm.getApplicationId());
                if (realm.getName().equals(localRealmName)) {
                    RealmImpl realmImpl = (RealmImpl) presentRealm;
                    realmImpl.dynamic = false;
                    for (String peerName : ((RealmImpl) realm).getPeerNames()) {
                        realmImpl.addPeerName(peerName);
                    }
                } else if (!presentRealm.isDynamic() && realm.isDynamic()) {
                } else if (presentRealm.isDynamic() && !realm.isDynamic()) {
                    RealmImpl realmImpl = (RealmImpl) presentRealm;
                    realmImpl.dynamic = false;
                    for (String peerName : ((RealmImpl) realm).getPeerNames()) {
                        realmImpl.addPeerName(peerName);
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Entry for realm '{}', already exists: {}", realm, this);
                    }
                }
            } else {
                this.appIdToRealm.put(realm.getApplicationId(), realm);
            }
        }

        /**
     * @return
     */
        public Collection<Realm> values() {
            return this.appIdToRealm.values();
        }

        /**
     * @return
     */
        public int size() {
            return this.appIdToRealm.size();
        }

        /**
     * @param appId
     * @return
     */
        public Realm getRealm(ApplicationId appId) {
            return this.appIdToRealm.get(appId);
        }

        /**
     * @param appId
     * @return
     */
        public Realm removeRealm(ApplicationId appId) {
            return this.appIdToRealm.remove(appId);
        }
    }

    @Override
    public Statistic getStatistic(String realmName) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws InternalException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws InternalException {
        return null;
    }
}
