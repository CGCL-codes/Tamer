package sample.threadCheck.predicate;

import edu.rice.cs.cunit.threadCheck.PredicateLink;
import edu.rice.cs.cunit.threadCheck.Combine;
import java.util.Arrays;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

public class TCSample5 {

    public static class PrimitivePredicates {

        public static boolean checkByte(Object thisO, byte value) {
            System.out.println("thisO = " + thisO + ", value:byte = " + value);
            return false;
        }

        public static boolean checkChar(Object thisO, char value) {
            System.out.println("thisO = " + thisO + ", value:char = " + value);
            return false;
        }

        public static boolean checkDouble(Object thisO, double value) {
            System.out.println("thisO = " + thisO + ", value:double = " + value);
            return false;
        }

        public static boolean checkFloat(Object thisO, float value) {
            System.out.println("thisO = " + thisO + ", value:float = " + value);
            return false;
        }

        public static boolean checkInt(Object thisO, int value) {
            System.out.println("thisO = " + thisO + ", value:int = " + value);
            return false;
        }

        public static boolean checkLong(Object thisO, long value) {
            System.out.println("thisO = " + thisO + ", value:long = " + value);
            return false;
        }

        public static boolean checkShort(Object thisO, short value) {
            System.out.println("thisO = " + thisO + ", value:short = " + value);
            return false;
        }

        public static boolean checkBoolean(Object thisO, boolean value) {
            System.out.println("thisO = " + thisO + ", value:boolean = " + value);
            return false;
        }

        public static boolean wrong(Object thisO, PredicateLink value) {
            System.out.println("Wrong!");
            return false;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkByte")
    @interface ByteAnnotation {

        byte value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkChar")
    @interface CharAnnotation {

        char value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkDouble")
    @interface DoubleAnnotation {

        double value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkFloat")
    @interface FloatAnnotation {

        float value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkInt")
    @interface IntAnnotation {

        int value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkLong")
    @interface LongAnnotation {

        long value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkShort")
    @interface ShortAnnotation {

        short value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "checkBoolean")
    @interface BooleanAnnotation {

        boolean value();
    }

    public static class PrimitiveArrayPredicates {

        public static boolean checkByte(Object thisO, byte[] value) {
            System.out.println("thisO = " + thisO + ", value:byte[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkChar(Object thisO, char[] value) {
            System.out.println("thisO = " + thisO + ", value:char[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkDouble(Object thisO, double[] value) {
            System.out.println("thisO = " + thisO + ", value:double[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkFloat(Object thisO, float[] value) {
            System.out.println("thisO = " + thisO + ", value:float[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkInt(Object thisO, int[] value) {
            System.out.println("thisO = " + thisO + ", value:int[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkLong(Object thisO, long[] value) {
            System.out.println("thisO = " + thisO + ", value:long[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkShort(Object thisO, short[] value) {
            System.out.println("thisO = " + thisO + ", value:short[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkBoolean(Object thisO, boolean[] value) {
            System.out.println("thisO = " + thisO + ", value:boolean[] = " + Arrays.toString(value));
            return false;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkByte")
    @interface ByteArrayAnnotation {

        byte[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkChar")
    @interface CharArrayAnnotation {

        char[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkDouble")
    @interface DoubleArrayAnnotation {

        double[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkFloat")
    @interface FloatArrayAnnotation {

        float[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkInt")
    @interface IntArrayAnnotation {

        int[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkLong")
    @interface LongArrayAnnotation {

        long[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkShort")
    @interface ShortArrayAnnotation {

        short[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitiveArrayPredicates.class, method = "checkBoolean")
    @interface BooleanArrayAnnotation {

        boolean[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = PrimitivePredicates.class, method = "wrong")
    @interface WrongAnnotation {

        PredicateLink value();
    }

    public static class ClassPredicates {

        public static boolean checkString(Object thisO, String value) {
            System.out.println("thisO = " + thisO + ", value:String = " + value);
            return false;
        }

        public static boolean checkStringArray(Object thisO, String[] value) {
            System.out.println("thisO = " + thisO + ", value:String[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkClass(Object thisO, Class value) {
            System.out.println("thisO = " + thisO + ", value:Class = " + value);
            return false;
        }

        public static boolean checkClassArray(Object thisO, Class[] value) {
            System.out.println("thisO = " + thisO + ", value:Class[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkEnum(Object thisO, Combine.Mode value) {
            System.out.println("thisO = " + thisO + ", value:enum = " + value);
            return false;
        }

        public static boolean checkEnumArray(Object thisO, Combine.Mode[] value) {
            System.out.println("thisO = " + thisO + ", value:enum[] = " + Arrays.toString(value));
            return false;
        }

        public static boolean checkIntString(Object thisO, int i, String s) {
            System.out.println("thisO = " + thisO + ", i:int = " + i + ", s:String = " + s);
            return false;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkString")
    @interface StringAnnotation {

        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkStringArray")
    @interface StringArrayAnnotation {

        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkClass")
    @interface ClassAnnotation {

        Class value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkClassArray")
    @interface ClassArrayAnnotation {

        Class[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkEnum")
    @interface EnumAnnotation {

        Combine.Mode value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkEnumArray")
    @interface EnumArrayAnnotation {

        Combine.Mode[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkIntString")
    @interface IntStringAnnotation {

        int i();

        String s();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
    @PredicateLink(value = ClassPredicates.class, method = "checkIntString")
    @interface StringIntAnnotation {

        String s();

        int i();
    }

    @ByteAnnotation(123)
    @CharAnnotation('a')
    @DoubleAnnotation(3.14)
    @FloatAnnotation(2.72f)
    @IntAnnotation(42)
    @LongAnnotation(1024 * 1024 * 1024)
    @ShortAnnotation(32000)
    @BooleanAnnotation(true)
    @ByteArrayAnnotation({ 123, -128 })
    @CharArrayAnnotation({ 'a', 'b', 'c' })
    @DoubleArrayAnnotation({ 3.14 })
    @FloatArrayAnnotation({ 2.72f, 1.11f, 2.22f, 3.33f })
    @IntArrayAnnotation({ 42, 100, 200 })
    @LongArrayAnnotation(1024 * 1024 * 1024)
    @ShortArrayAnnotation({ 32000, -16000, 0 })
    @BooleanArrayAnnotation({ false, true, false, true, false })
    public static void main(String[] args) {
        System.out.println("Main!");
        TCSample5 o = new TCSample5();
        o.run();
        o.error();
        o.runObj();
        o.runObj2();
        o.runIntString();
    }

    @ByteAnnotation(123)
    @CharAnnotation('a')
    @DoubleAnnotation(3.14)
    @FloatAnnotation(2.72f)
    @IntAnnotation(42)
    @LongAnnotation(1024 * 1024 * 1024)
    @ShortAnnotation(32000)
    @BooleanAnnotation(true)
    @ByteArrayAnnotation({  })
    @CharArrayAnnotation({  })
    @DoubleArrayAnnotation({  })
    @FloatArrayAnnotation({  })
    @IntArrayAnnotation({  })
    @LongArrayAnnotation({  })
    @ShortArrayAnnotation({  })
    @BooleanArrayAnnotation({  })
    void run() {
        System.out.println("Run!");
    }

    @WrongAnnotation(@PredicateLink(Object.class))
    void error() {
        System.out.println("Error!");
    }

    @StringAnnotation("foo")
    @ClassAnnotation(String.class)
    @EnumAnnotation(Combine.Mode.NOT)
    @StringArrayAnnotation({ "xxx", "yyy", "zzz" })
    @ClassArrayAnnotation({ Object.class, Integer.class })
    @EnumArrayAnnotation({  })
    void runObj() {
        System.out.println("Run Obj!");
    }

    @StringArrayAnnotation({  })
    @ClassArrayAnnotation({  })
    @EnumArrayAnnotation({  })
    void runObj2() {
        System.out.println("Run Obj 2!");
    }

    @IntStringAnnotation(i = 123, s = "abc")
    @StringIntAnnotation(s = "xyz", i = 456)
    void runIntString() {
        System.out.println("Run Int String!");
    }
}
