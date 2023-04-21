package sample.threadCheck.subAnnot;

import edu.rice.cs.cunit.threadCheck.subAnnot.predicates.*;
import edu.rice.cs.cunit.threadCheck.subAnnot.PredicateLink;
import edu.rice.cs.cunit.threadCheck.subAnnot.InvariantAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

public class TCSample5 {

    public static class PrintPrimitive {
        public static boolean checkString(Object thisO, String value) {
            System.out.println("thisO = "+thisO+", value:String = "+value);
            return false;
        }
        public static boolean checkStringsEqual(Object thisO, String value, String other) {
            System.out.println("thisO = "+thisO+", value:String = "+value+", other:String = "+other);
            return value.equals(other);
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.TYPE})
    @PredicateLink(value=PrintPrimitive.class, method="checkString")
    @interface PrintStringAnnotation extends InvariantAnnotation {
        String value();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.TYPE})
    @PredicateLink(value=PrintPrimitive.class, method="checkStringsEqual")
    @interface PrintStringAnnotation2 extends PrintStringAnnotation {
        String other();
    }
    
    public static void main(String[] args) {
        TCSample5 o = new TCSample5();
        o.testEqual();
        o.testNotEqual();
    }

    @PrintStringAnnotation2(value="equal",other="equal")
    public void testEqual() {
        System.out.println("testEqual!");
    }

    @PrintStringAnnotation2(value="equal",other="notEqual")
    public void testNotEqual() {
        System.out.println("testNotEqual!");
    }
}
