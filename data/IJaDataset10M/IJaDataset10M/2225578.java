package de.paragon.explorer.model;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Vector;
import de.paragon.explorer.figure.Figure;
import de.paragon.explorer.util.ExplorerColorManager;
import de.paragon.explorer.util.ObjectViewManager;
import de.paragon.explorer.util.StandardEnumeration;
import de.paragon.explorer.util.StandardEnumerator;

/**
 * Klassenbeschreibung: Ein ObjectModel repraesentiert ein Object. Es besteht
 * aus: 1. Einem ObjectHeaderModel - fuer den Klassennamen 2. Einer Liste von
 * AttributeModels, die die Attribute des eigenen Objects repraesentieren. 3.
 * Einer Liste von ConnectionModels, die entweder von anderen AttributeModels
 * kommen oder von einer der eigenen AttributeModels zu an- deren ObjectModels
 * gehen.
 * 
 * 
 * Instanzvariablen: headerModel: Referenz auf das Model, das fuer die
 * Beinhaltung des Objektnamens steht. attributeModels: Liste aller Modelle, die
 * den Inhalt der Attribute (Instanzvariablen etc.) halten. explorerModel:
 * Referenz auf das Gesamtmodel. connectionModels: Liste aller Modell, die die
 * Darstellung einer Referenz repraesentieren.
 * 
 * Das ObjectModel wird referenziert von: AttributeModel in der "normalen"
 * Hierarchieverknuepfung, dem ExplorerModel, zusammen mit allen anderen
 * ObjectModels in einer Liste. dem ObjectHeaderModel, das fuer den Inhalt des
 * Namens des Objectes zustaendig ist.
 */
public class ObjectModel extends Model {

    private ObjectHeaderModel headerModel;

    private StandardEnumeration attributeModels;

    private ExplorerModel explorerModel;

    private StandardEnumeration connectionModels;

    private ObjectViewManager objectViewManager;

    public ObjectModel() {
        super();
    }

    public void addAttributeModel(AttributeModel attrModl) {
        this.getAttributeModels().addElement(attrModl);
    }

    public void addConnectionModel(ConnectionModel conModl) {
        this.getConnectionModels().addElement(conModl);
    }

    public StandardEnumeration getAttributeModels() {
        if (this.attributeModels == null) {
            this.setAttributeModels(new StandardEnumerator());
        }
        return this.attributeModels;
    }

    public ExplorerColorManager getColorManager() {
        return this.getExplorerModel().getColorManager();
    }

    public StandardEnumeration getConnectionModels() {
        if (this.connectionModels == null) {
            this.setConnectionModels(new StandardEnumerator());
        }
        return this.connectionModels;
    }

    public Vector<Field> getDeclaredFields() {
        return this.getDeclaredFields(this.getObject().getClass());
    }

    public Vector<Field> getDeclaredFields(Class<?> aClass) {
        Field[] field;
        Vector<Field> vector;
        if (aClass.getSuperclass() != null) {
            vector = this.getDeclaredFields(aClass.getSuperclass());
        } else {
            vector = new Vector<Field>();
        }
        field = aClass.getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            vector.add(field[i]);
        }
        return vector;
    }

    public ExplorerModel getExplorerModel() {
        return this.explorerModel;
    }

    @Override
    public Figure getFigure() {
        return this.getFigureInModel();
    }

    public ObjectHeaderModel getHeaderModel() {
        return this.headerModel;
    }

    public int getNumberOfAttributes() {
        if (this.isArrayObject()) {
            return Array.getLength(this.getObject());
        }
        if (this.isStringObject() || this.isNullObject()) {
            return 0;
        }
        return this.getDeclaredFields().size();
    }

    public ObjectViewManager getObjectViewManager() {
        if (this.objectViewManager == null) {
            this.setObjectViewManager(new ObjectViewManager(this));
        }
        return this.objectViewManager;
    }

    public boolean isArrayObject() {
        return this.getObject().getClass().isArray();
    }

    public boolean isNullObject() {
        return NullObject.isNullObject(this.getObject());
    }

    @Override
    public boolean isObjectModel() {
        return true;
    }

    public boolean isStringObject() {
        return this.getObject().getClass() == String.class;
    }

    public void removeConnectionModel(ConnectionModel conModl) {
        this.getConnectionModels().removeElement(conModl);
    }

    public void setAttributeModels(StandardEnumeration models) {
        this.attributeModels = models;
    }

    private void setConnectionModels(StandardEnumeration conModls) {
        this.connectionModels = conModls;
    }

    public void setExplorerModel(ExplorerModel explModl) {
        this.explorerModel = explModl;
    }

    @Override
    public void setFigure(Figure newFigure) {
        this.setFigureInModel(newFigure);
    }

    public void setHeaderModel(ObjectHeaderModel model) {
        this.headerModel = model;
    }

    public void setObjectViewManager(ObjectViewManager newObjectViewManager) {
        this.objectViewManager = newObjectViewManager;
    }
}
