package com.manydesigns.portofino.base;

import com.manydesigns.portofino.base.workflow.MDWfState;
import com.manydesigns.portofino.base.workflow.MDWfTransition;
import com.manydesigns.portofino.util.Defs;
import com.manydesigns.portofino.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class MDWfAttribute extends MDAttribute {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private List<MDWfState> ownWfStates = new ArrayList<MDWfState>();

    private MDWfState initialWfState = null;

    private Collection<MDWfTransition> allWfTransitions = null;

    /**
     * Creates a new instance of MDWfAttribute
     */
    public MDWfAttribute(MDClass ownerCls, int id, String name, String prettyName, Integer order, String groupName, boolean inName, boolean inSummary, boolean inDetails, boolean required, String description, MDThreadLocals threadLocals, Locale locale) {
        super(ownerCls, id, name, prettyName, order, groupName, inName, inSummary, inDetails, required, description, threadLocals, locale);
    }

    public int getPhysicalJdbcType() {
        return java.sql.Types.INTEGER;
    }

    public void visit(MDConfigVisitor visitor) {
        visitor.doWfAttributePre(this);
        visitCommonAttribute(visitor);
        visitor.doWfAttributePost();
    }

    public String formatValue(MDWfState state) {
        if (state == null) {
            return Util.getLocalizedString(Defs.MDLIBI18N, locale, "Undefined_state");
        } else {
            return state.getName();
        }
    }

    public MDWfState getInitialWfState() {
        return initialWfState;
    }

    public Collection<MDWfState> getWfStates() {
        return ownWfStates;
    }

    public void registerWfState(MDWfState state, boolean initial) throws Exception {
        ownWfStates.add(state);
        if (initial) {
            if (initialWfState == null) initialWfState = state; else throw new Exception("Stato iniziale ridefinito");
        }
    }

    public synchronized Collection<MDWfTransition> getAllWfTransitions() {
        if (allWfTransitions == null) {
            allWfTransitions = new ArrayList<MDWfTransition>();
            for (MDWfState state : ownWfStates) {
                allWfTransitions.addAll(state.getAllWfTransitions());
            }
        }
        return allWfTransitions;
    }

    public void setAttributeCastValue(MDObject obj, Object value) throws Exception {
        if (value == null) {
            obj.setWfAttribute(this, null);
        } else if (value instanceof MDWfState) {
            obj.setWfAttribute(this, (MDWfState) value);
        } else if (value instanceof Integer) {
            MDWfState state = getOwnerClass().getConfig().getWfStateById((Integer) value);
            if (!ownWfStates.contains(state)) {
                throw new Exception("State does not belong to this workflow");
            }
            obj.setWfAttribute(this, state);
        } else {
            throw new Exception("Incompatible type");
        }
    }
}
