package de.grogra.pf.io;

import de.grogra.pf.registry.*;
import de.grogra.util.*;

/**
 * This factory class is used to represent a
 * {@link de.grogra.pf.io.FilterSource} in the registry.
 * 
 * @author Ole Kniemeyer
 */
public class FilterItem extends Item {

    /**
	 * The 
	 */
    String create;

    IOFlavor inputFlavor;

    String input;

    String inputClass;

    IOFlavor outputFlavor;

    String output;

    String outputClass;

    private transient boolean loaded = false;

    public static final NType $TYPE;

    public static final NType.Field create$FIELD;

    public static final NType.Field input$FIELD;

    public static final NType.Field inputClass$FIELD;

    public static final NType.Field output$FIELD;

    public static final NType.Field outputClass$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(FilterItem.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((FilterItem) o).create = (String) value;
                    return;
                case 1:
                    ((FilterItem) o).input = (String) value;
                    return;
                case 2:
                    ((FilterItem) o).inputClass = (String) value;
                    return;
                case 3:
                    ((FilterItem) o).output = (String) value;
                    return;
                case 4:
                    ((FilterItem) o).outputClass = (String) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((FilterItem) o).create;
                case 1:
                    return ((FilterItem) o).input;
                case 2:
                    return ((FilterItem) o).inputClass;
                case 3:
                    return ((FilterItem) o).output;
                case 4:
                    return ((FilterItem) o).outputClass;
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new FilterItem());
        $TYPE.addManagedField(create$FIELD = new _Field("create", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 0));
        $TYPE.addManagedField(input$FIELD = new _Field("input", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 1));
        $TYPE.addManagedField(inputClass$FIELD = new _Field("inputClass", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 2));
        $TYPE.addManagedField(output$FIELD = new _Field("output", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 3));
        $TYPE.addManagedField(outputClass$FIELD = new _Field("outputClass", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(String.class), null, 4));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new FilterItem();
    }

    public FilterItem() {
        super(null);
    }

    public FilterItem(String name, IOFlavor input, IOFlavor output, String create) {
        super(name);
        this.inputFlavor = input;
        this.outputFlavor = output;
        this.create = create;
    }

    public synchronized IOFlavor getInputFlavor() {
        if (inputFlavor == null) {
            inputFlavor = getInputFlavorImpl(((Item) getAxisParent()).getName());
        }
        return inputFlavor;
    }

    protected IOFlavor getInputFlavorImpl(String mimeType) {
        if (inputClass != null) {
            try {
                return IOFlavor.valueOf(classForName(inputClass, false));
            } catch (ClassNotFoundException e) {
                de.grogra.pf.boot.Main.logSevere(e);
                return IOFlavor.INVALID;
            }
        }
        return IOFlavor.valueOf(mimeType + ";io=" + input);
    }

    public synchronized IOFlavor getOutputFlavor() {
        if (outputFlavor == null) {
            outputFlavor = getOutputFlavorImpl();
        }
        return outputFlavor;
    }

    protected IOFlavor getOutputFlavorImpl() {
        if (outputClass != null) {
            try {
                return IOFlavor.valueOf(classForName(outputClass, false));
            } catch (ClassNotFoundException e) {
                de.grogra.pf.boot.Main.logSevere(e);
                return IOFlavor.INVALID;
            }
        }
        return (output != null) ? IOFlavor.valueOf(output) : null;
    }

    protected boolean useLazyFilter() {
        return (output != null) || (outputClass != null);
    }

    public FilterSource createFilter(FilterSource source) {
        if (loaded || !useLazyFilter()) {
            return createFilterImpl(source);
        }
        return new LazyFilter(this, source, getOutputFlavor()) {

            @Override
            protected FilterSource createFilterSource() {
                loaded = true;
                return createFilterImpl(source);
            }
        };
    }

    protected FilterSource createFilterImpl(FilterSource source) {
        Throwable t;
        try {
            return (FilterSource) Utils.invoke(create, new Object[] { this, source }, getClassLoader());
        } catch (ClassNotFoundException e) {
            t = e;
        } catch (NoSuchMethodException e) {
            t = e;
        } catch (InstantiationException e) {
            t = e;
        } catch (IllegalAccessException e) {
            t = e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            t = e.getCause();
            if (t instanceof Error) {
                throw (Error) t;
            }
        }
        if (!(t instanceof IllegalArgumentException)) {
            de.grogra.pf.boot.Main.logSevere(t);
        }
        return null;
    }

    @Override
    protected String paramString() {
        return super.paramString() + ',' + getInputFlavor() + '>' + getOutputFlavor() + ',' + getPluginDescriptor();
    }
}
