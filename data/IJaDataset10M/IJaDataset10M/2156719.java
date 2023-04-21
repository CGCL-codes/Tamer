package bsh;

import java.lang.reflect.Array;

class BSHType extends SimpleNode implements BshClassManager.Listener {

    /**
		baseType is used during evaluation of full type and retained for the
		case where we are an array type.
		In the case where we are not an array this will be the same as type.
	*/
    private Class baseType;

    /** 
		If we are an array type this will be non zero and indicate the 
		dimensionality of the array.  e.g. 2 for String[][];
	*/
    private int arrayDims;

    /** 
		Internal cache of the type.  Cleared on classloader change.
	*/
    private Class type;

    String descriptor;

    BSHType(int id) {
        super(id);
    }

    /**
		Used by the grammar to indicate dimensions of array types 
		during parsing.
	*/
    public void addArrayDimension() {
        arrayDims++;
    }

    SimpleNode getTypeNode() {
        return (SimpleNode) jjtGetChild(0);
    }

    /**
		 Returns a class descriptor for this type.
		 If the type is an ambiguous name (object type) evaluation is 
		 attempted through the namespace in order to resolve imports.
		 If it is not found and the name is non-compound we assume the default
		 package for the name.
	*/
    public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
        if (descriptor != null) return descriptor;
        String descriptor;
        SimpleNode node = getTypeNode();
        if (node instanceof BSHPrimitiveType) descriptor = getTypeDescriptor(((BSHPrimitiveType) node).type); else {
            String clasName = ((BSHAmbiguousName) node).text;
            BshClassManager bcm = interpreter.getClassManager();
            String definingClass = bcm.getClassBeingDefined(clasName);
            Class clas = null;
            if (definingClass == null) {
                try {
                    clas = ((BSHAmbiguousName) node).toClass(callstack, interpreter);
                } catch (EvalError e) {
                }
            } else clasName = definingClass;
            if (clas != null) {
                descriptor = getTypeDescriptor(clas);
            } else {
                if (defaultPackage == null || Name.isCompound(clasName)) descriptor = "L" + clasName.replace('.', '/') + ";"; else descriptor = "L" + defaultPackage.replace('.', '/') + "/" + clasName + ";";
            }
        }
        for (int i = 0; i < arrayDims; i++) descriptor = "[" + descriptor;
        this.descriptor = descriptor;
        return descriptor;
    }

    public Class getType(CallStack callstack, Interpreter interpreter) throws EvalError {
        if (type != null) return type;
        SimpleNode node = getTypeNode();
        if (node instanceof BSHPrimitiveType) baseType = ((BSHPrimitiveType) node).getType(); else baseType = ((BSHAmbiguousName) node).toClass(callstack, interpreter);
        if (arrayDims > 0) {
            try {
                int[] dims = new int[arrayDims];
                Object obj = Array.newInstance(baseType, dims);
                type = obj.getClass();
            } catch (Exception e) {
                throw new EvalError("Couldn't construct array type", this, callstack);
            }
        } else type = baseType;
        interpreter.getClassManager().addListener(this);
        return type;
    }

    /**
		baseType is used during evaluation of full type and retained for the
		case where we are an array type.
		In the case where we are not an array this will be the same as type.
	*/
    public Class getBaseType() {
        return baseType;
    }

    /** 
		If we are an array type this will be non zero and indicate the 
		dimensionality of the array.  e.g. 2 for String[][];
	*/
    public int getArrayDims() {
        return arrayDims;
    }

    public void classLoaderChanged() {
        type = null;
        baseType = null;
    }

    public static String getTypeDescriptor(Class clas) {
        if (clas == Boolean.TYPE) return "Z";
        if (clas == Character.TYPE) return "C";
        if (clas == Byte.TYPE) return "B";
        if (clas == Short.TYPE) return "S";
        if (clas == Integer.TYPE) return "I";
        if (clas == Long.TYPE) return "J";
        if (clas == Float.TYPE) return "F";
        if (clas == Double.TYPE) return "D";
        if (clas == Void.TYPE) return "V";
        String name = clas.getName().replace('.', '/');
        if (name.startsWith("[") || name.endsWith(";")) return name; else return "L" + name.replace('.', '/') + ";";
    }
}
