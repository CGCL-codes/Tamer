package org.ncgr.cmtv.services;

import org.ncgr.cmtv.services.ClassificationStrategy;
import org.ncgr.cmtv.services.Transformation;
import org.ncgr.cmtv.datamodel.MapModel;
import org.ncgr.cmtv.datamodel.DataWidgetModel;
import org.ncgr.cmtv.rendering.LinearMapWidgetFactory;
import org.ncgr.cmtv.rendering.LinearMapWidgetContainer;
import org.ncgr.cmtv.rendering.impl.FilteringDynamicallySizedMappedObjectContainer;
import org.ncgr.cmtv.layout.impl.TilingMapLayoutManager;
import java.util.*;

/**
 * An implementation of <code>Transformation</code> designed to take a 
 * given <code>ClassificationStrategy</code> and use it to construct
 * a widget containment hierarchy paralleling the classification hierarchy
 * determined by the given strategy.
 *
 * @author Andrew Farmer
 * @version $Revision: 1.1.1.1 $ 
 */
public class ClassificationStrategyContainerTransformationAdapter implements Transformation {

    ClassificationStrategy classifier;

    LinearMapWidgetContainer parentContainer;

    Map class2Container = new HashMap();

    private MapModel mapModel;

    private List factories;

    private DataWidgetModel dataWidgetModel;

    public ClassificationStrategyContainerTransformationAdapter(ClassificationStrategy classifier, LinearMapWidgetContainer parentContainer, MapModel mapModel, DataWidgetModel dataWidgetModel, List factories) {
        this.classifier = classifier;
        this.parentContainer = parentContainer;
        this.mapModel = mapModel;
        this.dataWidgetModel = dataWidgetModel;
        this.factories = factories;
    }

    public Collection transform(Collection input) {
        ArrayList retval = new ArrayList();
        MappedObjectInclusionRule[] classes = classifier.getClassesForObjects(input);
        for (int i = 0; i < classes.length; i++) {
            if (class2Container.get(classes[i]) == null) {
                javax.swing.JPanel p = new javax.swing.JPanel();
                String borderStyle = System.getProperty(org.ncgr.cmtv.CompMapViewer.PROP_NS + "group_borders");
                if (borderStyle != null) {
                    if (borderStyle.equals("line")) {
                        p.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black, 1));
                        p.setToolTipText(classes[i].getDescription());
                    } else if (borderStyle.equals("title")) {
                        p.setBorder(javax.swing.BorderFactory.createTitledBorder(classes[i].getDescription()));
                    }
                }
                p.setBackground(org.ncgr.cmtv.CompMapViewer.DEFAULT_BACKGROUND);
                FilteringDynamicallySizedMappedObjectContainer w = new FilteringDynamicallySizedMappedObjectContainer(parentContainer.getMap(), p, false, (LinearMapWidgetFactory[]) factories.toArray(new LinearMapWidgetFactory[0]), classes[i], mapModel, dataWidgetModel);
                retval.add(w);
                w.setLayout(new TilingMapLayoutManager(w));
                w.setSelectable(false);
                class2Container.put(classes[i], w);
            }
        }
        return retval;
    }

    public String getDescription() {
        return classifier.getDescription();
    }

    public ClassificationStrategy getStrategy() {
        return classifier;
    }

    public boolean equals(Object o) {
        if (o instanceof ClassificationStrategyContainerTransformationAdapter) {
            if (((ClassificationStrategyContainerTransformationAdapter) o).getStrategy().equals(getStrategy())) {
                return true;
            }
        }
        return false;
    }
}
