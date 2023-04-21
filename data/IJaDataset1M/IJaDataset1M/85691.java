package org.kalypso.nofdpidss.geodata.wizard.map.addlayer;

import javax.xml.namespace.QName;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategories;
import org.kalypso.nofdpidss.core.ui.GeoDataTreeContentProvider;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class AMLTreeCheckStateListener implements ICheckStateListener {

    final QName[] m_groups = new QName[] { IGeodataCategories.QN_CATEGORY_TYPE, IGeodataCategories.QN_SUB_CATEGORY, IGeodataCategories.QN_ABSTRACT_CATEGORY, IGeodataCategories.QN_ABSTRACT_SUB_CATEGORY };

    GeoDataTreeContentProvider m_provider = new GeoDataTreeContentProvider();

    public void checkStateChanged(final CheckStateChangedEvent event) {
        final Object source = event.getElement();
        final CheckboxTreeViewer viewer = (CheckboxTreeViewer) event.getSource();
        if (source instanceof Feature) {
            final Feature feature = (Feature) source;
            final QName group = feature.getFeatureType().getSubstitutionGroupFT().getQName();
            if (ArrayUtils.contains(m_groups, group)) updateChildren(viewer, feature, event.getChecked());
            final Feature parent = feature.getParent();
            if (parent == null) return;
            final QName parentGroup = parent.getFeatureType().getSubstitutionGroupFT().getQName();
            if (ArrayUtils.contains(m_groups, parentGroup)) updateParent(viewer, feature.getParent(), event.getChecked());
            if (viewer.getGrayed(feature)) viewer.setGrayed(feature, false);
        }
    }

    private boolean hasActiveChildren(final CheckboxTreeViewer viewer, final Feature category) {
        final boolean checked = viewer.getChecked(category);
        if (checked) return true;
        if (m_provider.hasChildren(category)) {
            final Object[] children = m_provider.getChildren(category);
            for (final Object obj : children) {
                if (!(obj instanceof Feature)) continue;
                final Feature child = (Feature) obj;
                if (hasActiveChildren(viewer, child)) return true;
            }
        }
        return false;
    }

    private boolean hasInactiveChildren(final CheckboxTreeViewer viewer, final Feature category) {
        final boolean checked = viewer.getChecked(category);
        if (!checked) return true;
        if (m_provider.hasChildren(category)) {
            final Object[] children = m_provider.getChildren(category);
            for (final Object obj : children) {
                if (!(obj instanceof Feature)) continue;
                final Feature child = (Feature) obj;
                if (hasInactiveChildren(viewer, child)) return true;
            }
        }
        return false;
    }

    private void updateChildren(final CheckboxTreeViewer viewer, final Feature feature, final boolean checked) {
        if (m_provider.hasChildren(feature)) {
            final Object[] children = m_provider.getChildren(feature);
            for (final Object obj : children) {
                if (!(obj instanceof Feature)) continue;
                final Feature child = (Feature) obj;
                viewer.setChecked(child, checked);
                updateChildren(viewer, child, checked);
            }
        }
    }

    private void updateParent(final CheckboxTreeViewer viewer, final Feature feature, final boolean checked) {
        viewer.setChecked(feature, checked);
        if (checked) {
            if (hasInactiveChildren(viewer, feature)) viewer.setGrayChecked(feature, checked); else viewer.setGrayed(feature, false);
        } else if (hasActiveChildren(viewer, feature)) viewer.setGrayChecked(feature, true); else viewer.setGrayed(feature, false);
        final Feature parent = feature.getParent();
        if (parent != null) {
            final QName parentGroup = parent.getFeatureType().getSubstitutionGroupFT().getQName();
            if (ArrayUtils.contains(m_groups, parentGroup)) updateParent(viewer, parent, checked);
        }
    }
}
