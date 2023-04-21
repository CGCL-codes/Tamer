package org.ocl4java.ocl.JDTModelFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.ocl4java.Constraint;
import org.ocl4java.ocl.Messages;
import tudresden.ocl.check.OclTypeException;
import tudresden.ocl.check.types.Type;

/**
 * This is a factory used to create Model-Elements
 * representing Colletions.
 * It also tried to remember what member-types collections
 * where created with to work around a serious bug in the
 * Dresden OCL-compiler version 1.
 */
public final class CollectionFactory {

    /**
     * All business-methods are static so
     * the constructor is private.
     */
    private CollectionFactory() {
        super();
    }

    @Constraint(value = "post: simpleTypeName='java.util.Collection' implies result.getClass().getName()='tudresden.ocl.check.types.Collection'", value1 = "post: simpleTypeName='java.util.Set' implies result.getClass().getName()='tudresden.ocl.check.types.Set'", value2 = "post: simpleTypeName='java.util.List' implies result.getClass().getName()='tudresden.ocl.check.types.Sequence'")
    public static Type createCollection(final String simpleTypeName, final ParameterizedType paramType, final JDTClassAny containedIn, final JDTFacade rf, final String name) {
        if (simpleTypeName.equals("java.util.Collection")) try {
            String fullyQualifiedParamTypeName = containedIn.getParameterType(paramType);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.COLLECTION, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingCollection", name));
            ex.initCause(x);
            throw ex;
        }
        if (simpleTypeName.equals("java.util.Set") || simpleTypeName.equals("java.util.HashSet") || simpleTypeName.equals("java.util.LinkedHashSet") || simpleTypeName.equals("java.util.TreeSet")) try {
            String fullyQualifiedParamTypeName = containedIn.getParameterType(paramType);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SET, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingSet", name));
            ex.initCause(x);
            throw ex;
        }
        if (simpleTypeName.equals("java.util.Map") || simpleTypeName.equals("java.util.HashMap") || simpleTypeName.equals("java.util.WeakHashMap") || simpleTypeName.equals("java.util.TreeMap")) try {
            String fullyQualifiedParamTypeName = containedIn.getSecondParameterType(paramType);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SET, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingMap", name));
            ex.initCause(x);
            throw ex;
        }
        if (simpleTypeName.equals("java.util.List") || simpleTypeName.equals("java.util.LinkedList") || simpleTypeName.equals("java.util.ArrayList") || simpleTypeName.equals("java.util.Vector") || simpleTypeName.equals("java.util.Stack")) try {
            String fullyQualifiedParamTypeName = containedIn.getParameterType(paramType);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SEQUENCE, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingSequence", name));
            ex.initCause(x);
            throw ex;
        }
        return null;
    }

    /**
     * For a paramatrized type with 1 parameter, get the parameter-type.
     * @param t the parameter-types
     * @return the fully qualified Class-Name
     */
    private static String getParameterType(final Collection<String> t) {
        if (t == null) {
            throw new OclTypeException(Messages.getString("CollectionFactory.ProblemOnlyOneParamAllowed", "null"));
        }
        if (t.size() != 1) {
            throw new OclTypeException(Messages.getString("CollectionFactory.ProblemOnlyOneParamAllowed", t.size()));
        }
        return t.iterator().next();
    }

    /**
     * For a paramatrized type with 2 parameter, get the parameter-type.
     * @param t the parameter-types
     * @return the fully qualified Class-Name
     */
    private static String getSecondParameterType(final Collection<String> t) {
        if (t == null) {
            throw new OclTypeException(Messages.getString("CollectionFactory.ProblemOnlyTwoParamAllowed", "null"));
        }
        if (t.size() != 2) {
            throw new OclTypeException(Messages.getString("CollectionFactory.ProblemOnlyTwoParamAllowed", t.size()));
        }
        Iterator<String> iterator = t.iterator();
        iterator.next();
        return iterator.next();
    }

    public static Type createCollection(@Constraint("pre: simpleTypeName.size>0") final String simpleTypeName, final Collection<String> params, @Constraint("pre: rf.toString().size>0") final JDTFacade rf) {
        if (simpleTypeName.equals("java.util.Collection")) try {
            String fullyQualifiedParamTypeName = getParameterType(params);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.COLLECTION, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingCollectionOfType", simpleTypeName + "'<" + Arrays.toString(params.toArray()) + ">"));
            ex.initCause(x);
            throw ex;
        }
        if (simpleTypeName.equals("java.util.Set") || simpleTypeName.equals("java.util.HashSet") || simpleTypeName.equals("java.util.LinkedHashSet") || simpleTypeName.equals("java.util.TreeSet")) try {
            String fullyQualifiedParamTypeName = getParameterType(params);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SET, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingSetOfType", simpleTypeName + "'<" + Arrays.toString(params.toArray()) + ">"));
            ex.initCause(x);
            throw ex;
        }
        if (simpleTypeName.equals("java.util.Map") || simpleTypeName.equals("java.util.HashMap") || simpleTypeName.equals("java.util.WeakHashMap") || simpleTypeName.equals("java.util.TreeMap")) try {
            String fullyQualifiedParamTypeName = getSecondParameterType(params);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SET, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingMapOfType", simpleTypeName + "'<" + Arrays.toString(params.toArray()) + ">"));
            ex.initCause(x);
            throw ex;
        }
        if (simpleTypeName.equals("java.util.List") || simpleTypeName.equals("java.util.LinkedList") || simpleTypeName.equals("java.util.ArrayList") || simpleTypeName.equals("java.util.Vector") || simpleTypeName.equals("java.util.Stack")) try {
            String fullyQualifiedParamTypeName = getParameterType(params);
            Type collectionContentType = rf.getType(fullyQualifiedParamTypeName);
            tudresden.ocl.check.types.Collection retval = new tudresden.ocl.check.types.Collection(tudresden.ocl.check.types.Collection.SEQUENCE, collectionContentType);
            return retval;
        } catch (Exception x) {
            x.printStackTrace();
            OclTypeException ex = new OclTypeException(Messages.getString("CollectionFactory.ProblemHandlingSequenceOfType", simpleTypeName + "'<" + Arrays.toString(params.toArray()) + ">"));
            ex.initCause(x);
            throw ex;
        }
        return null;
    }
}
