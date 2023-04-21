package ch.epfl.lbd.database.providers.mondrian.olap;

import ch.epfl.lbd.database.olap.Dimension;
import ch.epfl.lbd.database.olap.Hierarchy;
import ch.epfl.lbd.etl.XML;

public class MondrianDimension extends Dimension implements MondrianSchema {

    public static final long serialVersionUID = 0x98329342;

    protected XML dimDef;

    public MondrianDimension(String name, Hierarchy hierarchy) {
        super(name, hierarchy);
        if (hierarchy == null) return;
        String levelString = "<Dimension name =\"" + name + "\"/>";
        dimDef = new XML(levelString);
        if (hierarchy instanceof MondrianHierarchy) {
            dimDef.addToRootNode(((MondrianHierarchy) hierarchy).addToMondrianSchema());
        } else logger.error("only Mondrian Hierarchy can be combined with MondrianDimension");
    }

    protected void setHierarchy(Hierarchy hier) {
        if (this.hierarchy == null) {
            this.hierarchy = hier;
            String levelString = "<Dimension name =\"" + name + "\"/>";
            dimDef = new XML(levelString);
            if (hierarchy instanceof MondrianHierarchy) {
                dimDef.addToRootNode(((MondrianHierarchy) hierarchy).addToMondrianSchema());
            } else logger.error("only Mondrian Hierarchy can be combined with MondrianDimension");
        }
    }

    public XML addToMondrianSchema() {
        return dimDef;
    }
}
