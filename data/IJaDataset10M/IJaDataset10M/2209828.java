package ontool.model;

/**
 * Place model.
 * @author AntonioSRGomes
 * @version $Id: PlaceModel.java,v 1.1 2003/10/22 03:06:41 asrgomes Exp $
 */
public class PlaceModel extends PortHolderModel {

    /** EVENT = the class type was set, ARG = new class type*/
    public static final String SET_CLASSTYPE = "SET_CLASSTYPE";

    private ClassModel classType = null;

    /**
	 * Creates a new place model.
	 * @param parent parent model
	 * @param name name of this class
	 */
    public PlaceModel(PageModel parent, String name, ClassModel classType) {
        super(parent, new ByClassSelector(KObjectModel.class), name);
        setClassType(classType);
    }

    /**
	 * Sets the class type of this place.
	 * @param classType class
	 * @see #SET_CLASSTYPE
	 */
    public void setClassType(ClassModel classType) {
        if (this.classType != null) this.classType.deleteObserver(this);
        if (classType != null) classType.addObserver(this);
        this.classType = classType;
        notifyEvent(new ModelEvent(this, SET_GENERICTYPE, this.classType));
        notifyEvent(new ModelEvent(this, SET_CLASSTYPE, this.classType));
    }

    /**
	 * Gets the current class type of this place.
	 * @return class
	 */
    public ClassModel getClassType() {
        return classType;
    }

    public Model getGenericType() {
        return classType;
    }

    public Class getPortRefClass(Class portClass) {
        if (portClass == PrivInPortModel.class) return SensorFieldModel.class;
        if (portClass == PrivOutPortModel.class) return EffectorFieldModel.class;
        return null;
    }

    public void update(ModelEvent me) {
        if (me.getSource() == classType && me.isType(BEING_DESTROYED)) setClassType(null); else super.update(me);
    }

    protected void beforeDestroy() {
        super.beforeDestroy();
        if (this.classType != null) this.classType.deleteObserver(this);
    }
}
