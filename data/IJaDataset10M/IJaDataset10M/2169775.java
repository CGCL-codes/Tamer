package com.ixora.rms.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Node;
import com.ixora.rms.ResourceId;
import com.ixora.common.MessageRepository;
import com.ixora.common.utils.Utils;
import com.ixora.common.xml.exception.XMLException;
import com.ixora.rms.CounterDescriptor;
import com.ixora.rms.CounterId;
import com.ixora.rms.EntityConfiguration;
import com.ixora.rms.EntityDescriptor;
import com.ixora.rms.EntityId;
import com.ixora.rms.MonitoringLevel;

/** Entity data */
final class EntityInfoImpl extends ArtefactInfoContainerImpl implements EntityInfo {

    private static final long serialVersionUID = 6928978549721350639L;

    /** Entity descriptor */
    private EntityDescriptor entity;

    /** Current entity configuration */
    private EntityConfiguration configuration;

    /** Whether or not the entity is enabled */
    private boolean enabled;

    /** Translated path */
    private String translatedPath;

    /** Translated path */
    private String[] translatedPathFragments;

    /** Counters map */
    private Map<CounterId, CounterInfoImpl> counters;

    /** Translated description */
    private String translatedDescription;

    /** Translated name */
    private String translatedName;

    /** Dirty (user modified) flag */
    private boolean dirty;

    /** Parent entity or null if the parent is an agent */
    private EntityInfoImpl parent;

    /**
	 * Constructor.
     * @param parent
	 * @param msgRep
	 * @param entity
	 * @param enabled
	 * @param model
	 */
    EntityInfoImpl(EntityInfoImpl parent, String msgRep, EntityDescriptor ed, boolean enabled, SessionModel model) {
        super(model);
        this.parent = parent;
        this.messageRepository = msgRep;
        this.model = model;
        setUpFromDescriptor(ed);
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#hasChildren()
	 */
    public boolean hasChildren() {
        return this.entity.hasChildren();
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#getTranslatedDescription()
	 */
    public String getTranslatedDescription() {
        return this.translatedDescription;
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#getTranslatedName()
	 */
    public String getTranslatedName() {
        return this.translatedName;
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#getTranslatedPath()
	 */
    public String getTranslatedPath() {
        return this.translatedPath;
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#getTranslatedPathFragments()()
	 */
    public String[] getTranslatedPathFragments() {
        return translatedPathFragments;
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#getCounterInfo(com.ixora.rms.CounterId)
	 */
    public CounterInfo getCounterInfo(CounterId cid) {
        return this.counters.get(cid);
    }

    /**
	 * @return the info for the counters for the
	 * given level
	 */
    public Collection<CounterInfo> getCounterInfoForLevel(MonitoringLevel level) {
        Collection<CounterDescriptor> c = this.entity.getCounterDescriptorsForLevel(level);
        List<CounterInfo> ret = new ArrayList<CounterInfo>(c.size());
        CounterDescriptor cd;
        for (Iterator<CounterDescriptor> iter = c.iterator(); iter.hasNext(); ) {
            cd = iter.next();
            ret.add(counters.get(cd.getId()));
        }
        return ret;
    }

    /**
	 * @return the id of this entity
	 */
    public EntityId getId() {
        return this.entity.getId();
    }

    /**
	 * @return the entity configuration
	 */
    public EntityConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
	 * @return the supported levels of this entity
	 */
    public MonitoringLevel[] getSupportedLevels() {
        return this.entity.getSupportedLevels();
    }

    /**
	 * @return true if this entity supports sampling interval
	 */
    public boolean supportsSamplingInterval() {
        return this.entity.supportsSamplingInterval();
    }

    /**
	 * @return the name of this entity
	 */
    public String getName() {
        return this.entity.getName();
    }

    /**
	 * @see com.ixora.rms.MonitoringDescriptor#appliesToLevel(com.ixora.rms.MonitoringLevel)
	 */
    public boolean appliesToLevel(MonitoringLevel level) {
        return this.entity.appliesToLevel(level);
    }

    /**
	 * @return true if the entity is enabled
	 */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (model.getShowIdentifiers()) {
            return getName();
        }
        return getTranslatedName();
    }

    /**
	 * @return the entity description
	 */
    public String getDescription() {
        return this.entity.getDescription();
    }

    /**
	 * @return the current level
	 */
    public MonitoringLevel getLevel() {
        return this.entity.getLevel();
    }

    /**
	 * @return the alternate name of this entity
	 */
    public String getAlternateName() {
        return this.entity.getAlternateName();
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#getCounterInfo()
	 */
    public Collection<CounterInfo> getCounterInfo() {
        Collection<CounterInfo> retList = new LinkedList<CounterInfo>();
        retList.addAll(this.counters.values());
        return retList;
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#uncommittedVisibleCounters()
	 */
    public boolean uncommittedVisibleCounters() {
        Collection<CounterDescriptor> c = this.entity.getCounterDescriptors();
        for (CounterDescriptor cd : c) {
            CounterInfo ci = counters.get(cd.getId());
            if (!ci.isCommitted()) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @return the counter info impl
	 */
    private CounterInfoImpl getCounterInfoImpl(CounterId cid) {
        return this.counters.get(cid);
    }

    /**
	 * Sets the enabled flag by inspecting the current configuration.
	 * Must be invoked after changes to the monitored counters.
     * @return true if the flag was actually changed
	 */
    private boolean updateEnabledFlag() {
        Set<CounterId> c = this.configuration.getMonitoredCountersIds();
        boolean mustBeEnabled = true;
        if (c == null || c.size() == 0) {
            mustBeEnabled = false;
        }
        boolean oldValue = this.enabled;
        this.enabled = mustBeEnabled;
        return oldValue != this.enabled;
    }

    /**
     * @see com.ixora.common.xml.XMLExternalizable#toXML(org.w3c.dom.Node)
     */
    public void toXML(Node parent) throws XMLException {
    }

    /**
     * @see com.ixora.common.xml.XMLExternalizable#fromXML(org.w3c.dom.Node)
     */
    public void fromXML(Node node) throws XMLException {
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#isDirty()
	 */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @return the entity descriptor.
     */
    EntityDescriptor getEntityDescriptor() {
        return entity;
    }

    /**
	 * Sets the value of the given flag for the given counter.
	 * @param e
	 */
    void setCounterFlag(CounterId cid, int flag, boolean value) {
        CounterInfoImpl ci = getCounterInfoImpl(cid);
        if (ci != null) {
            ci.setFlag(flag, value);
        }
    }

    /**
	 * Updates entity info with info from the descriptor.
	 * @param desc
     * @return true if the entity was changed as the result of the update
	 */
    boolean update(EntityDescriptor desc) {
        return updateFromDescriptor(desc);
    }

    /**
	 * Rolls back the given counter.
	 * @param cid
	 */
    void rollbackCounter(CounterId cid) {
        CounterInfoImpl ci = getCounterInfoImpl(cid);
        if (ci != null) {
            ci.rollback();
        }
    }

    /**
	 * Sets the configuration.
	 * @param conf
	 */
    void setConfiguration(EntityConfiguration conf) {
        this.configuration = conf;
        for (CounterInfoImpl cinfo : this.counters.values()) {
            cinfo.setFlag(CounterInfo.ENABLED, false);
            cinfo.commit();
        }
        Set<CounterId> c = this.configuration.getMonitoredCountersIds();
        if (!Utils.isEmptyCollection(c)) {
            for (CounterId cid : c) {
                CounterInfoImpl cinfo = this.counters.get(cid);
                if (cinfo != null) {
                    cinfo.setFlag(CounterInfo.ENABLED, true);
                    cinfo.commit();
                }
            }
        }
        updateEnabledFlag();
    }

    /**
	 * Rolls back all queries.
	 */
    void rollbackAllCounters() {
        for (CounterInfoImpl cd : counters.values()) {
            cd.rollback();
        }
    }

    /**
	 * @param ed
	 */
    private void setUpFromDescriptor(EntityDescriptor ed) {
        this.counters = new HashMap<CounterId, CounterInfoImpl>();
        this.entity = ed;
        if (ResourceId.EMPTY_ENTITY.getName().equals(this.entity.getId().getName())) {
            throw new IllegalArgumentException("Invalid entity name: " + ResourceId.EMPTY_ENTITY.getName());
        }
        this.configuration = ed.getConfiguration();
        if (this.configuration == null) {
            this.configuration = new EntityConfiguration();
        }
        updateEnabledFlag();
        Collection<CounterDescriptor> c = entity.getCounterDescriptors();
        Set<CounterId> monitoredCounters = this.configuration.getMonitoredCountersIds();
        for (CounterDescriptor cd : c) {
            CounterInfoImpl counterImpl = new CounterInfoImpl(this.messageRepository, cd, model);
            if (monitoredCounters != null && monitoredCounters.contains(cd.getId())) {
                counterImpl.setFlag(CounterInfo.ENABLED, true);
            } else {
                counterImpl.setFlag(CounterInfo.ENABLED, false);
            }
            counterImpl.commit();
            this.counters.put(cd.getId(), counterImpl);
        }
        String alternateName = entity.getAlternateName();
        if (alternateName != null) {
            alternateName = alternateName.trim();
        }
        boolean useAlternateName = !Utils.isEmptyString(alternateName);
        if (useAlternateName) {
            this.translatedName = alternateName;
        } else {
            this.translatedName = MessageRepository.get(this.messageRepository, entity.getName());
        }
        String desc = entity.getDescription();
        if (desc == null || desc.length() == 0) {
            this.translatedDescription = "";
        } else {
            this.translatedDescription = MessageRepository.get(this.messageRepository, entity.getDescription());
        }
        if (this.parent == null) {
            String[] path = entity.getId().getPathComponents();
            StringBuffer buff = new StringBuffer();
            translatedPathFragments = new String[path.length];
            for (int i = 0; i < path.length; i++) {
                String t = MessageRepository.get(this.messageRepository, path[i]);
                translatedPathFragments[i] = t;
                buff.append(t);
                if (i != path.length - 1) {
                    buff.append(EntityId.DELIMITER);
                }
            }
            this.translatedPath = buff.toString();
        } else {
            List<String> parts = new LinkedList<String>();
            EntityInfoImpl anc = this;
            while (anc != null) {
                parts.add(anc.getTranslatedName());
                anc = anc.parent;
                if (anc == null) {
                    parts.add("root");
                }
            }
            Collections.reverse(parts);
            this.translatedPathFragments = parts.toArray(new String[parts.size()]);
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < translatedPathFragments.length; i++) {
                String t = translatedPathFragments[i];
                buff.append(t);
                if (i != translatedPathFragments.length - 1) {
                    buff.append(EntityId.DELIMITER);
                }
            }
            this.translatedPath = buff.toString();
        }
    }

    /**
     * @param ed
     * @return true if the entity was modified as the result of the update
     */
    private boolean updateFromDescriptor(EntityDescriptor ed) {
        if (this.entity.equals(ed)) {
            return false;
        }
        this.entity = ed;
        this.configuration = ed.getConfiguration();
        updateEnabledFlag();
        Collection<CounterDescriptor> c = entity.getCounterDescriptors();
        Set<CounterId> monitoredCounters = this.configuration.getMonitoredCountersIds();
        for (CounterDescriptor cd : c) {
            CounterInfoImpl counterImpl = new CounterInfoImpl(this.messageRepository, cd, model);
            if (monitoredCounters != null && monitoredCounters.contains(cd.getId())) {
                counterImpl.setFlag(CounterInfo.ENABLED, true);
            } else {
                counterImpl.setFlag(CounterInfo.ENABLED, false);
            }
            counterImpl.commit();
            this.counters.put(cd.getId(), counterImpl);
        }
        return true;
    }

    /**
	 * @param dirty the dirty to set.
	 */
    void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#canRefreshChildren()
	 */
    public boolean canRefreshChildren() {
        return this.entity.canRefreshChildren();
    }

    /**
	 * @see com.ixora.rms.client.model.EntityInfo#safeToRefreshRecursivelly()
	 */
    public boolean safeToRefreshRecursivelly() {
        return this.entity.safeToRefreshRecursivelly();
    }
}
