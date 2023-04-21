package de.objectcode.openk.soa.connectors.sugarcrm;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.internal.soa.esb.message.format.serialized.SerializedMessagePlugin;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import com.sugarcrm.www.sugarcrm.Entry_value;
import com.sugarcrm.www.sugarcrm.Get_entry_list_result;
import com.sugarcrm.www.sugarcrm.Name_value;
import com.sugarcrm.www.sugarcrm.Set_entry_result;
import com.sugarcrm.www.sugarcrm.SugarsoapBindingStub;
import com.sugarcrm.www.sugarcrm.SugarsoapLocator;
import com.sugarcrm.www.sugarcrm.User_auth;
import de.objectcode.openk.soa.idmapper.api.IIdMapper;
import de.objectcode.openk.soa.model.v1.ApplicationObjectIdType;
import de.objectcode.openk.soa.model.v1.EventResponseMessage;
import de.objectcode.openk.soa.model.v1.EventType;
import de.objectcode.openk.soa.model.v1.ResponseBusinessObjectType;
import de.objectcode.openk.soa.model.v1.ResponseType;

public class HandleSugarEvent extends AbstractActionPipelineProcessor {

    private static final Log LOG = LogFactory.getLog(HandleSugarEvent.class);

    MessagePayloadProxy payloadProxy;

    IIdMapper idMapper;

    private SugarsoapLocator sugarService;

    private static final String applicationName = "sugarcrm";

    private String moduleName;

    private String sugarUserName;

    private String sugarPassword;

    private URL sugarURL;

    int sugarNotDeletedFlag = 0;

    int sugarMaxResults = 10;

    public HandleSugarEvent(ConfigTree config) throws ConfigurationException {
        payloadProxy = new MessagePayloadProxy(config);
        try {
            InitialContext ctx = new InitialContext();
            idMapper = (IIdMapper) ctx.lookup(IIdMapper.JNDI_NAME);
        } catch (NamingException e) {
            throw new ConfigurationException(e);
        }
        String baseUrl = config.getRequiredAttribute("base-url");
        moduleName = config.getRequiredAttribute("module-name");
        sugarUserName = config.getRequiredAttribute("username");
        sugarPassword = config.getRequiredAttribute("password");
        try {
            sugarService = new SugarsoapLocator();
            sugarService.setEndpointAddress("sugarsoapPort", baseUrl);
            sugarURL = new URL(baseUrl);
        } catch (ServiceException e) {
            throw new ConfigurationException(e);
        } catch (MalformedURLException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    public Message process(Message message) throws ActionProcessingException {
        boolean persisted = false;
        EventResponseMessage response = new EventResponseMessage();
        Message esbResponse = new SerializedMessagePlugin().getMessage();
        SugarEvent sugarEvent = new SugarEvent(message);
        response.setEvent(sugarEvent.getEvent());
        LOG.warn("before " + sugarEvent.getResponse().getBusinessObjects().toString());
        SugarsoapBindingStub stub = null;
        String session = null;
        try {
            stub = new SugarsoapBindingStub(sugarURL, sugarService);
            User_auth user = new User_auth(sugarUserName, sugarPassword, "");
            Set_entry_result res = stub.login(user, "OpenCooperation");
            session = res.getId();
            if (sugarEvent.getEvent().getAction().equals(EventType.EventActionEnum.CREATE)) {
                String appUid = create(stub, session, sugarEvent.getModule());
                persisted = (null != appUid);
                if (persisted) {
                    for (ResponseBusinessObjectType rbot : sugarEvent.getResponse().getBusinessObjects()) {
                        if (null == rbot.getBusinessObject().getApplicationObjectIds()) {
                            ApplicationObjectIdType addappref = new ApplicationObjectIdType();
                            addappref.setApplicationId(applicationName);
                            addappref.setName(rbot.getBusinessObject().getObjectType());
                            addappref.setValue(appUid);
                            rbot.getBusinessObject().setApplicationObjectIds(new ArrayList<ApplicationObjectIdType>());
                            rbot.getBusinessObject().getApplicationObjectIds().add(addappref);
                        } else {
                            for (ApplicationObjectIdType appref : rbot.getBusinessObject().getApplicationObjectIds()) {
                                if (appref.getValue().equalsIgnoreCase("billing") || appref.getValue().equalsIgnoreCase("shipping")) {
                                    appref.setName(appref.getValue());
                                    appref.setValue(appUid);
                                } else {
                                    appref.setValue(appUid);
                                }
                            }
                        }
                    }
                    LOG.warn("after " + sugarEvent.getResponse().getBusinessObjects().toString());
                    response.setResponse(new ResponseType());
                    response.getResponse().setBusinessObjects(sugarEvent.getResponse().getBusinessObjects());
                }
            } else if (sugarEvent.getEvent().getAction().equals(EventType.EventActionEnum.DELETE)) {
                persisted = delete(stub, session, sugarEvent.getModule());
            } else if (sugarEvent.getEvent().getAction().equals(EventType.EventActionEnum.UPDATE)) {
                persisted = update(stub, session, sugarEvent.getModule());
            } else {
                throw new ActionProcessingException("Invalid action");
            }
            if (!persisted) {
                LOG.warn("SugarCRM: entry already exists.");
            }
            esbResponse.getBody().add(payloadProxy.getSetPayloadLocation(), response);
        } catch (Exception e) {
            throw new ActionProcessingException(e);
        } finally {
            try {
                if (null != stub) stub.logout(session);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return esbResponse;
    }

    private String create(SugarsoapBindingStub stub, String session, SugarModule module) throws ActionProcessingException {
        String resp = null;
        try {
            String whereClause = getWhereClause(module.getWhereFields(), module.getModuleNvl());
            String[] selectClause = getSelectFields(module.getModuleNvl());
            String logwarn = "";
            for (String s : selectClause) {
                logwarn += " " + s;
            }
            Get_entry_list_result elres = stub.get_entry_list(session, moduleName, whereClause, "id", 0, selectClause, sugarMaxResults, sugarNotDeletedFlag);
            if (0 == elres.getResult_count()) {
                Name_value[] nvlt = new Name_value[module.getModuleNvl().size()];
                Set_entry_result seres = stub.set_entry(session, moduleName, module.getModuleNvl().toArray(nvlt));
                resp = seres.getId();
                LOG.warn("created: " + resp);
            }
        } catch (RemoteException e) {
            throw new ActionProcessingException(e);
        }
        return resp;
    }

    private boolean delete(SugarsoapBindingStub stub, String session, SugarModule module) throws ActionProcessingException {
        boolean resp = true;
        try {
            Name_value deletedFlagIndex = getIndexOf(module.getModuleNvl(), "deleted");
            if (null != deletedFlagIndex) {
                deletedFlagIndex.setValue("1");
            } else {
                module.getModuleNvl().add(new Name_value("deleted", "1"));
            }
            String deletedId = null;
            String whereClause = getWhereClause(module.getWhereFields(), module.getModuleNvl());
            String[] selectClause = getSelectFields(module.getModuleNvl());
            Get_entry_list_result elres = stub.get_entry_list(session, moduleName, whereClause, "id", 0, selectClause, sugarMaxResults, sugarNotDeletedFlag);
            if (elres.getResult_count() > 0) {
                Name_value deletedIdIndex = getIndexOf(module.getModuleNvl(), "id");
                for (Entry_value ev : elres.getEntry_list()) {
                    LOG.warn("deleted: " + ev.getId());
                    deletedId = ev.getId();
                    if (null != deletedId) {
                        if (null != deletedIdIndex) {
                            deletedIdIndex.setValue(deletedId);
                        } else {
                            deletedIdIndex = new Name_value("id", deletedId);
                            module.getModuleNvl().add(deletedIdIndex);
                        }
                        Name_value[] nvlt = new Name_value[module.getModuleNvl().size()];
                        Set_entry_result seres = stub.set_entry(session, moduleName, module.getModuleNvl().toArray(nvlt));
                        resp = (seres.getError().getDescription().equalsIgnoreCase("No Error")) && resp;
                    }
                }
            }
        } catch (RemoteException e) {
            throw new ActionProcessingException(e);
        }
        return resp;
    }

    private boolean update(SugarsoapBindingStub stub, String session, SugarModule module) throws ActionProcessingException {
        boolean resp = true;
        try {
            String whereClause = getWhereClause(module.getWhereFields(), module.getModuleNvl());
            String[] selectClause = getSelectFields(module.getModuleNvl());
            Get_entry_list_result elres = stub.get_entry_list(session, moduleName, whereClause, "id", 0, selectClause, sugarMaxResults, sugarNotDeletedFlag);
            if (elres.getResult_count() > 0) {
                Name_value updateIdIndex = getIndexOf(module.getModuleNvl(), "id");
                if (elres.getResult_count() > 1) LOG.warn("update: found more than one entry");
                for (Entry_value ev : elres.getEntry_list()) {
                    LOG.warn("updated: " + ev.getId());
                    if (null != ev.getId()) {
                        if (null != updateIdIndex) {
                            updateIdIndex.setValue(ev.getId());
                        } else {
                            updateIdIndex = new Name_value("id", ev.getId());
                            module.getModuleNvl().add(updateIdIndex);
                        }
                        Name_value[] nvlt = new Name_value[module.getModuleNvl().size()];
                        Set_entry_result seres = stub.set_entry(session, moduleName, module.getModuleNvl().toArray(nvlt));
                        resp = (seres.getError().getDescription().equalsIgnoreCase("No Error")) && resp;
                    }
                }
            }
        } catch (RemoteException e) {
            throw new ActionProcessingException(e);
        }
        return resp;
    }

    private Name_value getIndexOf(List<Name_value> nvl, String key) {
        for (Name_value nv : nvl) {
            if (nv.getName().equalsIgnoreCase(key)) return nv;
        }
        return null;
    }

    private String[] getSelectFields(List<Name_value> nvl) {
        boolean idSelected = false;
        String[] selectFields;
        for (int i = 0; i < nvl.size(); i++) {
            idSelected = idSelected || (nvl.get(i).getName().equalsIgnoreCase("id"));
        }
        if (idSelected) {
            selectFields = new String[nvl.size()];
        } else {
            selectFields = new String[nvl.size() + 1];
            selectFields[nvl.size()] = "id";
        }
        for (int i = 0; i < nvl.size(); i++) {
            selectFields[i] = nvl.get(i).getName();
        }
        StringBuilder selects = new StringBuilder();
        for (String s : selectFields) {
            selects.append(s + " ");
        }
        return selectFields;
    }

    private String getWhereClause(String[] whereFields, List<Name_value> nvl) {
        String whereprefix = moduleName.toLowerCase() + ".";
        StringBuilder clause = new StringBuilder();
        if (null != whereFields && whereFields.length > 0) {
            for (int i = 0; i < whereFields.length; i++) {
                for (Name_value nv : nvl) {
                    if (null != nv.getValue() && whereFields[i].equalsIgnoreCase(nv.getName())) {
                        clause.append(whereprefix + whereFields[i] + "='" + nv.getValue());
                    }
                }
                if (i + 1 < whereFields.length) {
                    clause.append("' AND ");
                } else {
                    clause.append("' ");
                }
            }
        }
        return clause.toString();
    }
}
