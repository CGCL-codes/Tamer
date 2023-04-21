package org.vikamine.swing.subgroup.editors.zoomtable;

import java.util.Iterator;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.swing.subgroup.editors.zoomtable.update.TargetShareComputer;
import org.vikamine.swing.subgroup.editors.zoomtable.update.ValuesComputer;
import org.vikamine.swing.util.filters.AttributeFilter;

/**
 * @author Tobias Vogele
 */
public class AttributeNode extends FilteringNode {

    protected String id;

    protected ZoomAttribute zoomAttribute;

    private ValuesComputer valuesComputer;

    private TargetShareComputer targetShareComputer;

    private boolean userSelected = true;

    private ZoomAttribute zoomAttributeForSortedView = null;

    /**
     * This constructor is just for (quick and dirty) testing ...
     * 
     * @param att
     */
    AttributeNode(Attribute att) {
        setAttribute(att);
    }

    /**
     * This constructor is just for (quick and dirty) testing ...
     * 
     * @param id
     */
    AttributeNode(String id) {
        this.id = id;
    }

    public AttributeNode(String id, Attribute att) {
        this(id);
        setAttribute(att);
    }

    public boolean areChildrenCreated() {
        return children != null;
    }

    /**
     * @return
     */
    public Attribute getAttribute() {
        if (getZoomAttribute() == null) {
            return null;
        } else {
            return getZoomAttribute().getAttribute();
        }
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Returns the attributeValues.
     */
    public ZoomAttribute getZoomAttribute() {
        return zoomAttribute;
    }

    public void setAttribute(Attribute att) {
        if (att != null) {
            id = att.getId();
            zoomAttribute = new ZoomAttribute(att);
        } else {
            zoomAttribute = null;
        }
        computeZoomAttributeValues();
    }

    /**
     * Recalculates the targetshare for all values.
     */
    public void updateTargetShares() {
        if (getZoomAttribute() == null || getTargetShareComputer() == null) {
            return;
        }
        Attribute att = getZoomAttribute().getAttribute();
        for (Iterator iter = getZoomAttribute().getOverviewValues().iterator(); iter.hasNext(); ) {
            ZoomAttributeValue zoomvalue = (ZoomAttributeValue) iter.next();
            TargetShareComputer.ComputationResult rates = getTargetShareComputer().computeAttributeValueRatesRelativeToSubgroup(att, zoomvalue);
            zoomvalue.setTargetShare(rates.getTpRate());
            zoomvalue.setCurrentTargetShare(rates.getCurrentTpRate());
            zoomvalue.setFnRate(rates.getFnRate());
            zoomvalue.setSize(rates.getSize());
            zoomvalue.setAbsoluteFrequency(rates.getSize());
            zoomvalue.clearQuality();
        }
        for (Iterator iter = getZoomAttribute().getSortedValues().iterator(); iter.hasNext(); ) {
            ZoomAttributeValue zoomvalue = (ZoomAttributeValue) iter.next();
            TargetShareComputer.ComputationResult rates = getTargetShareComputer().computeAttributeValueRatesRelativeToSubgroup(att, zoomvalue);
            zoomvalue.setTargetShare(rates.getTpRate());
            zoomvalue.setCurrentTargetShare(rates.getCurrentTpRate());
            zoomvalue.setFnRate(rates.getFnRate());
            zoomvalue.setSize(rates.getSize());
            zoomvalue.setAbsoluteFrequency(rates.getSize());
            zoomvalue.clearQuality();
        }
    }

    /**
     * @param attributeValues
     *                The attributeValues to set.
     */
    public void setZoomAttribute(ZoomAttribute attributeValues) {
        this.zoomAttribute = attributeValues;
    }

    @Override
    protected boolean isAcceptedBy(AttributeFilter filter) {
        if (getAttribute() != null) {
            return filter.isAccepted(getAttribute());
        } else {
            return filter.isAccepted(getId());
        }
    }

    /**
     * @return Returns the valuesSomputer.
     */
    public ValuesComputer getValuesComputer() {
        return valuesComputer;
    }

    /**
     * @param valuesSomputer
     *                The valuesSomputer to set. The values are not computed
     *                immediately, i.e.,
     *                CommonZoomTablesController#updateZoomTables needs to be
     *                called, if the values need to be (re-)computed!
     */
    public void setValuesComputer(ValuesComputer valuesSomputer) {
        this.valuesComputer = valuesSomputer;
    }

    protected void computeZoomAttributeValues() {
        if (getValuesComputer() == null) {
            return;
        }
        if (getAttribute() != null) {
            ZoomAttribute oldAttribute = zoomAttribute;
            zoomAttribute = getValuesComputer().computeValues(getAttribute());
            if (oldAttribute instanceof NumericZoomAttribute) {
                ((NumericZoomAttribute) zoomAttribute).updateBounds((NumericZoomAttribute) oldAttribute);
            }
            updateTargetShares();
        } else {
            zoomAttribute = null;
        }
    }

    /**
     * @return Returns the targetShareComputer.
     */
    public TargetShareComputer getTargetShareComputer() {
        return targetShareComputer;
    }

    /**
     * @param targetShareComputer
     *                The targetShareComputer to set.
     */
    public void setTargetShareComputer(TargetShareComputer targetShareComputer) {
        this.targetShareComputer = targetShareComputer;
    }

    public boolean isUserSelected() {
        return userSelected;
    }

    public void setUserSelected(boolean userSelected) {
        this.userSelected = userSelected;
    }

    public ZoomAttribute getZoomAttributeForSortedView() {
        return zoomAttributeForSortedView;
    }

    public void setZoomAttributeForSortedView(ZoomAttribute zoomAttributeForSortedView) {
        this.zoomAttributeForSortedView = zoomAttributeForSortedView;
    }
}
