package org.vikamine.swing.subgroup.editors.zoomtable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.vikamine.app.DMManager;
import org.vikamine.app.Resources;
import org.vikamine.kernel.Describer;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.NominalAttribute;
import org.vikamine.kernel.data.SingleValue;
import org.vikamine.kernel.data.Value;
import org.vikamine.swing.subgroup.editors.zoomtable.update.TargetShareComputer;
import org.vikamine.swing.subgroup.editors.zoomtable.update.ValuesComputer;
import org.vikamine.swing.util.SGPluginUtil;
import org.vikamine.swing.util.filters.AttributeFilter;

public class SortedZoomValueNode extends AttributeNode {

    public class ZoomValueNodeValuesComputer implements ValuesComputer {

        public ZoomAttribute computeValues(Attribute attribute) {
            return computeZoomAttribute();
        }
    }

    public class ComplementaryValue extends SingleValue {

        private static final long serialVersionUID = 7326177941202636605L;

        private Attribute attribute;

        private double doubleValue;

        ComplementaryValue(Attribute att, double value, String id) {
            super(id);
            if (att == null) {
                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "ComplementaryValue", new IllegalArgumentException("Attribute is null!"));
            }
            this.attribute = att;
            this.doubleValue = value;
        }

        @Override
        public boolean isValueContainedInInstance(DataRecord instance) {
            return false;
        }

        @Override
        public Attribute getAttribute() {
            return attribute;
        }

        @Override
        public String getId() {
            return attribute.getId() + "aRest";
        }

        @Override
        public String getDescription() {
            return Resources.I18N.getString("not") + " [" + getValue().getDescription() + "]";
        }

        @Override
        public String getDescription(Describer d) {
            return d.createDescription(this);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null || obj.getClass() != getClass()) {
                return false;
            } else {
                ComplementaryValue that = (ComplementaryValue) obj;
                return this.attribute.equals(that.attribute);
            }
        }

        @Override
        public int computeCachedHashCode() {
            return attribute.hashCode();
        }

        @Override
        public String toString() {
            return getId();
        }

        @Override
        public boolean isMissingValue() {
            return false;
        }

        @Override
        public double doubleValue() {
            return doubleValue;
        }
    }

    private Value value;

    private AttributeNode parent;

    private ValuesComputer computer;

    public SortedZoomValueNode(Attribute attribute, AttributeNode parent, Value value) {
        super(attribute.getId(), attribute);
        this.value = value;
        this.parent = parent;
        computer = new ZoomValueNodeValuesComputer();
        computeZoomAttribute();
    }

    @Override
    public boolean areChildrenCreated() {
        return false;
    }

    public Value getValue() {
        return value;
    }

    /**
     * @return
     */
    @Override
    public Attribute getAttribute() {
        if (parent.getZoomAttribute() == null) {
            return null;
        } else {
            return parent.getZoomAttribute().getAttribute();
        }
    }

    /**
     * @return Returns the id.
     */
    @Override
    public String getId() {
        if (parent != null) return parent.getId(); else return "";
    }

    /**
     * @return Returns the attributeValues.
     */
    private ZoomAttribute computeZoomAttribute() {
        if (parent != null && parent.getAttribute() != null && parent.getZoomAttribute() != null) {
            ZoomAttribute zoomAtt = new ZoomAttribute(parent.getAttribute());
            ComplementaryValue cValue = null;
            int valuesCount = 0;
            int sgSize = SGPluginUtil.getCurrentSGInstancesCount();
            ZoomAttribute oldAtt = (parent.getZoomAttributeForSortedView() != null) ? parent.getZoomAttributeForSortedView() : parent.getZoomAttribute();
            Iterator<Value> itera;
            if (parent.getAttribute() instanceof NominalAttribute) {
                itera = ((NominalAttribute) parent.getAttribute()).allValuesIterator();
            } else {
                itera = parent.getAttribute().usedValuesIterator(DMManager.getInstance().getOntology().getPopulation().dataset());
            }
            while (itera.hasNext()) {
                itera.next();
                valuesCount++;
            }
            for (Iterator iter = oldAtt.getSortedValues().iterator(); iter.hasNext(); ) {
                ZoomAttributeValue zoomVal = (ZoomAttributeValue) iter.next();
                if (zoomVal.getValue().isMissingValue()) {
                    valuesCount++;
                    break;
                }
            }
            int rest = 0;
            List sortedValues = new ArrayList(oldAtt.getSortedValues());
            if (!sortedValues.isEmpty()) {
                List subList = new ArrayList(sortedValues.subList(0, valuesCount));
                sortedValues = sortedValues.subList(valuesCount, sortedValues.size());
                while (!sortedValues.isEmpty() || !subList.isEmpty()) {
                    ZoomAttributeValue missingValue = null;
                    for (Iterator iter = subList.iterator(); iter.hasNext(); ) {
                        ZoomAttributeValue val = (ZoomAttributeValue) iter.next();
                        if (val.getValue().equals(value)) {
                            zoomAtt.addSortedValue(val);
                        } else if (val.getValue().isMissingValue()) {
                            missingValue = val;
                        } else {
                            rest += val.getAbsoluteFrequency();
                        }
                    }
                    if (sgSize > 0) {
                        cValue = new ComplementaryValue(parent.getAttribute(), rest, null);
                    } else {
                        cValue = new ComplementaryValue(parent.getAttribute(), 0, null);
                    }
                    ZoomAttributeValue newValue = new ZoomAttributeValue(cValue);
                    newValue.setAbsoluteFrequency(rest);
                    newValue.setCurrentTargetShare(0);
                    if (sgSize > 0) {
                        newValue.setRelativeFrequency((double) rest / sgSize);
                    } else {
                        newValue.setRelativeFrequency(0);
                    }
                    zoomAtt.addSortedValue(newValue);
                    if (missingValue != null) {
                        zoomAtt.addSortedValue(missingValue);
                    }
                    rest = 0;
                    if (!sortedValues.isEmpty()) {
                        subList = new ArrayList(sortedValues.subList(0, valuesCount));
                        sortedValues = sortedValues.subList(valuesCount, sortedValues.size());
                    } else {
                        subList.clear();
                    }
                }
            }
            return zoomAtt;
        }
        if (parent == null) return null;
        return new ZoomAttribute(parent.getAttribute());
    }

    @Override
    public ZoomAttribute getZoomAttribute() {
        if (zoomAttribute == null) {
            zoomAttribute = computeZoomAttribute();
        } else if (zoomAttribute.getSortedValues().isEmpty()) {
            zoomAttribute = computeZoomAttribute();
        }
        return zoomAttribute;
    }

    @Override
    protected boolean isAcceptedBy(AttributeFilter filter) {
        if (getAttribute() != null) {
            return filter.isAccepted(getAttribute());
        } else {
            return filter.isAccepted(getId());
        }
    }

    @Override
    protected void computeZoomAttributeValues() {
    }

    @Override
    public void updateTargetShares() {
    }

    /**
     * @return Returns the valuesSomputer.
     */
    @Override
    public ValuesComputer getValuesComputer() {
        return computer;
    }

    /**
     * @return Returns the targetShareComputer.
     */
    @Override
    public TargetShareComputer getTargetShareComputer() {
        return null;
    }

    @Override
    public boolean isUserSelected() {
        if (parent != null) return parent.isUserSelected(); else return false;
    }

    public AttributeNode getParent() {
        return parent;
    }
}
