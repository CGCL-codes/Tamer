package syntelos.uel;

/**
 * <p> Interface to bound objects, fields, functions and methods.
 * </p>
 * 
 * @author jdp
 * @since 1.5
 */
public interface Subject extends Named {

    /**
     * This is an RValue that can be assigned from.
     */
    public interface Get extends Subject {

        /**
         * This is an LValue that can be assigned to.
         */
        public interface Set extends Get {

            public void subjectSet(java.lang.Object value);
        }

        /**
         * @return Value of Named
         */
        public java.lang.Object subjectGet();
    }

    /**
     * This is an object class with class fields and functions.  A
     * class is not an RValue and has no subject get.
     */
    public interface IClass extends Subject {

        public javax.xml.namespace.QName getName();

        public java.lang.Class getInstanceClass();

        /**
         * @param name Field, method or class function name
         * @return Null for not found. 
         */
        public Subject subjectResolve(java.lang.String name);

        /**
         * <p> Equivalent to {@link
         * subjectResolve(java.lang.String)}. </p>
         * 
         * @param qn Dotted child of this name
         * @return Null for not found.
         */
        public Subject subjectResolve(javax.xml.namespace.QName qn);
    }

    /**
     * This is an instance object with instance fields and methods.
     * An instance object is a valid RValue returned by subject get.
     */
    public interface Instance extends Subject.IClass, Subject.Get {
    }

    /**
     * This is a special value is a function or method.
     */
    public interface Callable extends Subject {

        /**
         * @return Value of Named
         */
        public java.lang.Object subjectCall(java.lang.Object[] argv);
    }
}
