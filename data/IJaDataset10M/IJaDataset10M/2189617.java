package ontool.model;

import java.util.Iterator;

/**
 * External class model.
 * @author AntonioSRGomes
 * @version $Id: InternalClassModel.java,v 1.1 2003/10/22 03:06:41 asrgomes Exp $
 */
public class InternalClassModel extends ClassModel {

    private int sensorCount;

    private int stateCount;

    private int effectorCount;

    private int functionCount;

    /**
	 * Creates a new internal class model.
	 * @param parent parent model
	 * @param name model name
	 */
    public InternalClassModel(NetworkModel parent, String name) {
        super(parent, new ByClassSelector(FieldModel.class, FunctionModel.class), name, new Class[] { SensorFieldModel.class, StateFieldModel.class, EffectorFieldModel.class, FunctionModel.class });
        setInitImpl(getInitImpl() + "// (1) Set the maximum number of firings this object\n" + "// can have in a single iteration\n" + "setMaxFireCount(1);\n");
    }

    public void compile() {
        compileIndexes();
        super.compile();
    }

    public void compileIndexes() {
        int idx = 0;
        for (Iterator i = getChildren(SensorFieldModel.class).iterator(); i.hasNext(); ) ((Indexable) i.next()).setIndex(idx++);
        sensorCount = idx;
        idx = 0;
        for (Iterator i = getChildren(StateFieldModel.class).iterator(); i.hasNext(); ) ((Indexable) i.next()).setIndex(idx++);
        stateCount = idx;
        idx = 0;
        for (Iterator i = getChildren(EffectorFieldModel.class).iterator(); i.hasNext(); ) ((Indexable) i.next()).setIndex(idx++);
        effectorCount = idx;
        idx = 0;
        for (Iterator i = getChildren(FunctionModel.class).iterator(); i.hasNext(); ) ((Indexable) i.next()).setIndex(idx++);
        functionCount = idx;
    }

    /**
	 * Returns the effectorCount.
	 * @return int
	 */
    public int getEffectorCount() {
        return effectorCount;
    }

    /**
	 * Returns the functionCount.
	 * @return int
	 */
    public int getFunctionCount() {
        return functionCount;
    }

    /**
	 * Returns the sensorCount.
	 * @return int
	 */
    public int getSensorCount() {
        return sensorCount;
    }

    /**
	 * Returns the stateCount.
	 * @return int
	 */
    public int getStateCount() {
        return stateCount;
    }
}
