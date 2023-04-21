package org.vikamine.gui.subgroup.editors.zoomtable.update;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.vikamine.app.DMManager;
import org.vikamine.gui.subgroup.editors.zoomtable.AttributeNode;
import org.vikamine.gui.subgroup.editors.zoomtable.CommonZoomTreesModel;
import org.vikamine.gui.subgroup.editors.zoomtable.DiscretizedZoomAttributeValue;
import org.vikamine.gui.subgroup.editors.zoomtable.FilteringNode;
import org.vikamine.gui.subgroup.editors.zoomtable.NumericZoomAttribute;
import org.vikamine.gui.subgroup.editors.zoomtable.ZoomAttribute;
import org.vikamine.gui.subgroup.editors.zoomtable.ZoomAttributeValue;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.DerivedNominalAttribute;

/**
 * @author Tobias Vogele
 */
public class AnimationDataPreparer {

    private CommonZoomTreesModel treeModel;

    public AnimationDataPreparer() {
        super();
    }

    public AnimationDataPreparer(CommonZoomTreesModel model) {
        setTreeModel(model);
    }

    private AnimatedZoomAttributeValue createAnimatedZoomAttributeValue(ZoomAttributeValue oldValue, ZoomAttributeValue newValue) {
        AnimatedZoomAttributeValue animValue = new AnimatedZoomAttributeValue(oldValue);
        animValue.setNewRelFreq(newValue.getRelativeFrequency());
        animValue.setNewAbsFreq(newValue.getAbsoluteFrequency());
        return animValue;
    }

    public void dispose() {
        FilteringNode root = (FilteringNode) getTreeModel().getRoot();
        for (Iterator iter = root.getChildren().iterator(); iter.hasNext(); ) {
            AttributeNode child = (AttributeNode) iter.next();
            dispose(child);
        }
    }

    private void dispose(AttributeNode node) {
        if (node.getZoomAttribute() != null) {
            dispose(node.getZoomAttribute());
        }
        if (node.areChildrenCreated()) {
            for (Iterator iter = node.getChildren().iterator(); iter.hasNext(); ) {
                AttributeNode child = (AttributeNode) iter.next();
                dispose(child);
            }
        }
    }

    private void dispose(List zoomvalues) {
        for (ListIterator iter = zoomvalues.listIterator(); iter.hasNext(); ) {
            ZoomAttributeValue value = (ZoomAttributeValue) iter.next();
            if (value instanceof AnimatedZoomAttributeValue) {
                AnimatedZoomAttributeValue animVal = (AnimatedZoomAttributeValue) value;
                double freq = animVal.getNewRelFreq();
                double targetShare = animVal.getNewTargetShare();
                ZoomAttributeValue oldVal = animVal.getDelegate();
                oldVal.setRelativeFrequency(freq);
                oldVal.setAbsoluteFrequency(animVal.getNewAbsFreq());
                oldVal.setTargetShare(targetShare);
                oldVal.setCurrentTargetShare(animVal.getNewCurrentTargetShare());
                if (oldVal instanceof DiscretizedZoomAttributeValue) {
                    DiscretizedZoomAttributeValue discOld = (DiscretizedZoomAttributeValue) oldVal;
                    dispose(discOld.getUndiscretizedValues());
                }
                iter.set(oldVal);
            }
        }
    }

    private void dispose(ZoomAttribute attribute) {
        synchronized (attribute) {
            dispose(attribute.getOverviewValues());
            dispose(attribute.getSortedValues());
        }
    }

    public CommonZoomTreesModel getTreeModel() {
        return treeModel;
    }

    public void initialize() {
        FilteringNode root = (FilteringNode) getTreeModel().getRoot();
        for (Iterator iter = root.getChildren().iterator(); iter.hasNext(); ) {
            AttributeNode element = (AttributeNode) iter.next();
            initialize(element);
        }
    }

    private void initialize(AttributeNode node) {
        if (node.getAttribute() != null && !(node.getAttribute() instanceof DerivedNominalAttribute)) {
            Attribute att = DMManager.getInstance().getOntology().getAttribute(node.getId());
            Attribute old = node.getAttribute();
            if ((att == null && old != null) || (att != null && old == null)) {
                node.setAttribute(att);
            }
        }
        if (node.getZoomAttribute() != null) {
            initialize(node.getZoomAttribute(), node.getValuesComputer(), node);
        }
        if (node.areChildrenCreated()) {
            for (Iterator iter = node.getChildren().iterator(); iter.hasNext(); ) {
                AttributeNode child = (AttributeNode) iter.next();
                initialize(child);
            }
        }
        node.setZoomAttributeForSortedView(null);
    }

    private void initialize(ZoomAttribute attribute, ValuesComputer computer, AttributeNode node) {
        ZoomAttribute zoomAttributeWithNewValues = null;
        zoomAttributeWithNewValues = computer.computeValues(attribute.getAttribute());
        node.setZoomAttributeForSortedView(zoomAttributeWithNewValues);
        if (attribute instanceof NumericZoomAttribute) {
            ((NumericZoomAttribute) attribute).updateBounds((NumericZoomAttribute) zoomAttributeWithNewValues);
        }
        synchronized (attribute) {
            initialize(attribute.getOverviewValues(), zoomAttributeWithNewValues.getOverviewValues());
            initialize(attribute.getSortedValues(), zoomAttributeWithNewValues.getSortedValues());
        }
    }

    private void initialize(List oldValues, List newValues) {
        if (oldValues.size() < newValues.size()) {
            initializeZoomOut(oldValues, newValues);
        } else {
            initializeZoomIn(oldValues, newValues);
        }
    }

    private void initializeZoomOut(List oldValues, List newValues) {
        assert (oldValues.size() < newValues.size());
        Iterator newIter = newValues.iterator();
        for (ListIterator oldIter = oldValues.listIterator(); oldIter.hasNext(); ) {
            ZoomAttributeValue oldValue = (ZoomAttributeValue) oldIter.next();
            ZoomAttributeValue newValue = null;
            do {
                if (!newIter.hasNext()) {
                    newValue = null;
                    break;
                }
                newValue = (ZoomAttributeValue) newIter.next();
                if (!newValue.equalsValue(oldValue)) {
                    AnimatedZoomAttributeValue value = new AnimatedZoomAttributeValue(newValue);
                    double newFreq = newValue.getRelativeFrequency();
                    value.setOldRelFreq(0);
                    value.setOldAbsFreq(0);
                    value.setNewRelFreq(newFreq);
                    value.setNewAbsFreq(newValue.getAbsoluteFrequency());
                    oldIter.previous();
                    oldIter.add(value);
                    oldIter.next();
                }
            } while (!newValue.equalsValue(oldValue));
            if ((newValue != null) && (newValue.equalsValue(oldValue))) {
                if (!(oldValue instanceof AnimatedZoomAttributeValue)) {
                    oldIter.set(createAnimatedZoomAttributeValue(oldValue, newValue));
                }
            }
        }
        while (newIter.hasNext()) {
            ZoomAttributeValue newValue = (ZoomAttributeValue) newIter.next();
            AnimatedZoomAttributeValue value = new AnimatedZoomAttributeValue(newValue);
            double newFreq = newValue.getRelativeFrequency();
            value.setOldRelFreq(0);
            value.setNewRelFreq(newFreq);
            oldValues.add(value);
        }
        for (Iterator iter = oldValues.iterator(); iter.hasNext(); ) {
            ZoomAttributeValue val = (ZoomAttributeValue) iter.next();
            if (!(val instanceof AnimatedZoomAttributeValue)) {
                iter.remove();
            }
        }
    }

    private void initializeZoomIn(List oldValues, List newValues) {
        assert (oldValues.size() >= newValues.size());
        ListIterator oldIter = oldValues.listIterator();
        for (Iterator newIter = newValues.iterator(); newIter.hasNext(); ) {
            ZoomAttributeValue newValue = (ZoomAttributeValue) newIter.next();
            ZoomAttributeValue oldValue = null;
            do {
                if (!oldIter.hasNext()) {
                    oldValue = null;
                    break;
                }
                oldValue = (ZoomAttributeValue) oldIter.next();
                if ((!newValue.equalsValue(oldValue)) && (!(oldValue instanceof AnimatedZoomAttributeValue))) {
                    AnimatedZoomAttributeValue value = new AnimatedZoomAttributeValue(oldValue);
                    value.setNewRelFreq(0);
                    value.setNewAbsFreq(0);
                    oldIter.set(value);
                }
            } while (!newValue.equalsValue(oldValue));
            if ((oldValue != null) && (newValue.equalsValue(oldValue))) {
                if (!(oldValue instanceof AnimatedZoomAttributeValue)) {
                    if (newValue instanceof AnimatedZoomAttributeValue) {
                        AnimatedZoomAttributeValue aniVal = new AnimatedZoomAttributeValue(oldValue);
                        aniVal.setNewAbsFreq(((AnimatedZoomAttributeValue) newValue).getNewAbsFreq());
                        aniVal.setNewRelFreq(((AnimatedZoomAttributeValue) newValue).getNewRelFreq());
                        oldIter.set(aniVal);
                    } else {
                        oldIter.set(createAnimatedZoomAttributeValue(oldValue, newValue));
                    }
                }
            }
        }
        while (oldIter.hasNext()) {
            ZoomAttributeValue oldValue = (ZoomAttributeValue) oldIter.next();
            if (!(oldValue instanceof AnimatedZoomAttributeValue)) {
                AnimatedZoomAttributeValue value = new AnimatedZoomAttributeValue(oldValue);
                value.setNewRelFreq(0);
                oldIter.set(value);
            }
        }
        for (Iterator iter = oldValues.iterator(); iter.hasNext(); ) {
            ZoomAttributeValue val = (ZoomAttributeValue) iter.next();
            if (!(val instanceof AnimatedZoomAttributeValue)) {
                throw new IllegalStateException("Illegal state!");
            }
        }
    }

    public void setTreeModel(CommonZoomTreesModel origModel) {
        this.treeModel = origModel;
    }
}
