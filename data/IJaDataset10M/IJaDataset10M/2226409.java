package net.sourceforge.olympos.diagramimageexporter;

import java.util.ArrayList;

public class InfoXmlFigure {

    EnumFigureType typ;

    String id;

    String name;

    String alias;

    String objectStatus;

    private ArrayList<InfoXmlConnection> child = new ArrayList<InfoXmlConnection>();

    private ArrayList<InfoXmlFigure> childFig = new ArrayList<InfoXmlFigure>();

    private ArrayList<InfoXmlFigure> childFigActSet = new ArrayList<InfoXmlFigure>();

    private ArrayList<InfoXMLOptionValue> childOpt = new ArrayList<InfoXMLOptionValue>();

    private ArrayList<InfoXMLOptionValue> childVal = new ArrayList<InfoXMLOptionValue>();

    InfoXmlFigure(String id, String name, EnumFigureType type, String alias, String objectStatus) {
        setAll(id, name, type, alias, objectStatus);
    }

    public EnumFigureType getTyp() {
        return typ;
    }

    public void setTyp(EnumFigureType typ) {
        this.typ = typ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObject_status() {
        return objectStatus;
    }

    public void setObject_status(String objectStatus) {
        this.objectStatus = objectStatus;
    }

    public void addOperation(InfoXMLOptionValue childOpt) {
        this.childOpt.add(childOpt);
    }

    public ArrayList<InfoXMLOptionValue> getOperation() {
        return childOpt;
    }

    public void addAttribute(InfoXMLOptionValue childOpt) {
        this.childVal.add(childOpt);
    }

    public ArrayList<InfoXMLOptionValue> getAttribute() {
        return childVal;
    }

    public void addChild(InfoXmlConnection childe) {
        this.child.add(childe);
    }

    public ArrayList<InfoXmlConnection> getChild() {
        return child;
    }

    public void setAll(String id, String name, EnumFigureType type, String alias, String objectStatus) {
        this.id = id;
        this.name = name;
        this.typ = type;
        this.alias = alias;
        this.objectStatus = objectStatus;
    }

    public void addChildFig(InfoXmlFigure childFig) {
        this.childFig.add(childFig);
    }

    public ArrayList<InfoXmlFigure> getChildrenFig() {
        return childFig;
    }

    public void addChildFigActSet(InfoXmlFigure childFig) {
        this.childFigActSet.add(childFig);
    }

    public ArrayList<InfoXmlFigure> getChildrenFigActSet() {
        return childFigActSet;
    }
}
