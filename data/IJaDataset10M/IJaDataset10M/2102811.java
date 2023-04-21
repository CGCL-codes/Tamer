package com.metanology.mde.core.metaModel.actiongraphs;

import com.metanology.mde.core.metaModel.*;

public class Partition extends ModelElement {

    private ActivityGraph activityGraph = null;

    private ModelElementCollection contents = null;

    /**
	 *  Get the activityGraph.  
	 *	
	 */
    public ActivityGraph getActivityGraph() {
        return this.activityGraph;
    }

    /**
	 *  Set the activityGraph.  
	 *	
	 */
    public void setActivityGraph(ActivityGraph newVal) {
        this.activityGraph = newVal;
    }

    /**
     * get the collection of ModelElement.

     */
    public ModelElementCollection getContents() {
        if (this.contents == null) {
            this.contents = new ModelElementCollection();
        }
        return this.contents;
    }

    public void setContents(ModelElementCollection newVal) {
        this.contents = newVal;
    }

    public Object cloneObject(java.util.Map parameters, Object clone) {
        java.util.Map objMap = (java.util.Map) parameters.get("model.element");
        if (objMap.get(this) != null) return objMap.get(this);
        if (clone == null) clone = new Partition();
        super.cloneObject(parameters, clone);
        if (getActivityGraph() != null) ((Partition) clone).setActivityGraph((ActivityGraph) getActivityGraph().cloneObject(parameters, null));
        for (java.util.Iterator iter = getContents().iterator(); iter.hasNext(); ) {
            ((Partition) clone).getContents().add(((ModelElement) iter.next()).cloneObject(parameters, null));
        }
        return clone;
    }
}
