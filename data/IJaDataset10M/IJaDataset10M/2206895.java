package com.manydesigns.portofino.base;

import com.manydesigns.portofino.base.calculations.MDCalc;
import com.manydesigns.portofino.base.calculations.MDRelatedAttrCalc;
import com.manydesigns.portofino.base.users.MDMetaUserGroup;
import com.manydesigns.portofino.util.Defs;
import com.manydesigns.portofino.util.Escape;
import com.manydesigns.portofino.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public abstract class MDAttribute {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    public static final String NULL_STRING = "";

    private final MDClass ownerCls;

    private final int id;

    private final String name;

    private final String escapedName;

    private final String prettyName;

    private final Integer order;

    private final String groupName;

    private final boolean inName;

    private final boolean inSummary;

    private final boolean inDetails;

    private final boolean required;

    private final String description;

    private final Collection<MDRelatedAttrCalc> ownRACs;

    private final Collection<MDMetaUserGroup> ownVisibility;

    protected final MDThreadLocals threadLocals;

    protected final Locale locale;

    /**
     * Creates a new instance of MDAttribute
     */
    public MDAttribute(MDClass ownerCls, int id, String name, String prettyName, Integer order, String groupName, boolean inName, boolean inSummary, boolean inDetails, boolean required, String description, MDThreadLocals threadLocals, Locale locale) {
        this.ownerCls = ownerCls;
        this.id = id;
        this.name = name;
        this.escapedName = Escape.dbSchemaEscape(name);
        this.prettyName = prettyName;
        this.order = order;
        this.groupName = groupName;
        this.inName = inName;
        this.inSummary = inSummary;
        this.inDetails = inDetails;
        this.required = required;
        this.description = description;
        this.threadLocals = threadLocals;
        this.locale = locale;
        ownRACs = new ArrayList<MDRelatedAttrCalc>();
        ownVisibility = new ArrayList<MDMetaUserGroup>();
    }

    public MDClass getOwnerClass() {
        return ownerCls;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEscapedName() {
        return escapedName;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public boolean isInDetails() {
        return inDetails;
    }

    public boolean isInSummary() {
        return inSummary;
    }

    public boolean isInName() {
        return inName;
    }

    public Integer getOrder() {
        return order;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public boolean isImmutable() {
        return false;
    }

    private MDCalc ownCalculation = null;

    public boolean isCalculated() throws Exception {
        return (ownCalculation != null);
    }

    public void registerAttributeCalculation(MDCalc calc) throws Exception {
        if (ownCalculation != null) throw new Exception(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Attribute_calculation_already_present"));
        ownCalculation = calc;
    }

    public void recalculate(MDObject obj) throws Exception {
        if (ownCalculation == null) return;
        Object value = ownCalculation.recalculate(obj);
        setAttributeCastValue(obj, value);
    }

    public abstract void setAttributeCastValue(MDObject obj, Object value) throws Exception;

    public abstract int getPhysicalJdbcType();

    public void registerRelatedAttrCalc(MDRelatedAttrCalc rac) {
        ownRACs.add(rac);
    }

    public MDCalc getOwnCalculation() {
        return ownCalculation;
    }

    public void registerVisibility(MDMetaUserGroup mug) {
        ownVisibility.add(mug);
    }

    public boolean isVisible() throws Exception {
        if (ownVisibility.isEmpty() || threadLocals.getCurrentUser() == null) {
            return true;
        }
        boolean result = false;
        for (MDMetaUserGroup mug : ownVisibility) {
            if (mug.check()) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return ownerCls.toString() + ".\"" + getName() + "\"";
    }

    public abstract void visit(MDConfigVisitor visitor);

    public void visitCommonAttribute(MDConfigVisitor visitor) {
        visitor.doAttributeVisibilityListPre();
        for (MDMetaUserGroup mug : ownVisibility) {
            visitor.doAttributeVisibility(mug);
        }
        visitor.doAttributeVisibilityListPost();
        if (ownCalculation != null) {
            visitor.doCalculatedAttributePre();
            ownCalculation.visit(visitor);
            visitor.doCalculatedAttributePost();
        }
    }
}
