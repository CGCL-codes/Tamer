package org.progeeks.meta;

import java.util.*;
import org.progeeks.meta.beans.*;
import org.progeeks.meta.functor.*;
import org.progeeks.meta.util.*;
import org.progeeks.util.log.*;

/**
 *  Temporary class used to test meta-class functors.  Ultimately
 *  this will be moved to somewhere like the examples module but while
 *  the code is experimental it will live here.
 *
 *  @version   $Revision: 1.5 $
 *  @author    Paul Speed
 */
public class Tester {

    static Log log = Log.getLog(Tester.class);

    public static void main(String[] args) throws Exception {
        Log.initialize();
        MetaClassRegistryX classes = new MetaClassRegistryX();
        MetaClassRegistry.setContextRegistry(classes);
        List props = BeanUtils.getBeanPropertyInfos(TestBean.class);
        String name = BeanUtils.getNameForClass(TestBean.class);
        System.out.println("Property infos:" + props);
        List argInfos;
        argInfos = new ArrayList();
        argInfos.add(new PropertyInfo("arg0", "First Integer", new ClassPropertyType(Integer.class)));
        argInfos.add(new PropertyInfo("arg1", "Second Integer", new ClassPropertyType(Integer.class)));
        MetaClass twoIntsClass = classes.createMetaClass("int,int", argInfos);
        argInfos = new ArrayList();
        argInfos.add(new PropertyInfo("arg0", "First Number", new ClassPropertyType(Number.class)));
        argInfos.add(new PropertyInfo("arg1", "Second Number", new ClassPropertyType(Number.class)));
        MetaClass twoNumsClass = classes.createMetaClass("java.lang.Number,java.lang.Number", argInfos);
        argInfos = new ArrayList();
        argInfos.add(new PropertyInfo("arg0", "First Integer", new ClassPropertyType(Integer.class)));
        argInfos.add(new PropertyInfo("arg1", "Second Integer", new ClassPropertyType(Integer.class)));
        MetaClass twoIntegersClass = classes.createMetaClass("java.lang.Integer,java.lang.Integer", argInfos, Collections.EMPTY_LIST, Collections.singletonList(twoNumsClass));
        List funcs = new ArrayList();
        funcs.add(new FunctorInfo("incrementAge", "Increment age", "Increments the value of age.", null, null));
        funcs.add(new FunctorInfo("add", "Add Integers", "Adds two integers together.", new ClassPropertyType(Integer.class), twoIntsClass));
        funcs.add(new FunctorInfo("add", "Add Numbers", "Adds two Numbers together.", new ClassPropertyType(Double.class), twoNumsClass));
        FunctorInfo multFunc = new FunctorInfo("multiply", "Multiply Integers", "Multiplies two integer values.", new ClassPropertyType(Integer.class), twoIntsClass);
        funcs.add(multFunc);
        funcs.add(new FunctorInfo("divide", "Divide Integers", "Divides the first integer by the second", new ClassPropertyType(Integer.class), twoIntsClass));
        MetaClassX testClass = (MetaClassX) classes.createMetaClass(name, props, funcs, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        System.out.println("TestClass:" + testClass);
        for (Iterator i = testClass.getFunctorInfos().iterator(); i.hasNext(); ) {
            System.out.println("  " + i.next());
        }
        testClass.setMetaClassAnnotation(Functor.FUNCTOR_ANNOTATION, multFunc, new Functor() {

            public Object execute(FunctorInfo info, MetaObject self, MetaObject parms) {
                Integer i1 = (Integer) parms.getProperty("arg0");
                Integer i2 = (Integer) parms.getProperty("arg1");
                return (new Integer(i1.intValue() * i2.intValue()));
            }
        });
        TestBean testBean = new TestBean();
        MetaObjectX testObject = new BeanMetaObjectX(testBean, testClass);
        System.out.println("TestObject:" + testObject);
        System.out.println("Testing age incrementer");
        testObject.execute("incrementAge", null);
        System.out.println("TestObject:" + testObject);
        testObject.execute("incrementAge", null);
        System.out.println("TestObject:" + testObject);
        testObject.execute("incrementAge", null);
        System.out.println("TestObject:" + testObject);
        MetaObject parms = new MapMetaObject(new HashMap(), twoIntsClass, null);
        parms.setProperty("arg0", new Integer(42));
        parms.setProperty("arg1", new Integer(1));
        MetaObject parms2 = new MapMetaObject(new HashMap(), twoNumsClass, null);
        parms2.setProperty("arg0", new Integer(42));
        parms2.setProperty("arg1", new Integer(1));
        MetaObject parms3 = new MapMetaObject(new HashMap(), twoIntegersClass, null);
        parms3.setProperty("arg0", new Integer(42));
        parms3.setProperty("arg1", new Integer(1));
        System.out.println("Trying method add( int,int ).");
        System.out.println("add(" + parms + ") = " + testObject.execute("add", parms));
        for (int i = 2; i < 5; i++) {
            parms.setProperty("arg1", new Integer(i));
            System.out.println("add(" + parms + ") = " + testObject.execute("add", parms));
        }
        System.out.println("Trying add method add( Number,Number ).");
        parms2.setProperty("arg1", new Double(1));
        System.out.println("add(" + parms2 + ") = " + testObject.execute("add", parms2));
        for (int i = 2; i < 5; i++) {
            parms2.setProperty("arg1", new Double(i));
            System.out.println("add(" + parms2 + ") = " + testObject.execute("add", parms2));
        }
        System.out.println("Trying add method add( Integer,Integer ).");
        parms3.setProperty("arg1", new Integer(1));
        System.out.println("add(" + parms3 + ") = " + testObject.execute("add", parms3));
        for (int i = 2; i < 5; i++) {
            parms3.setProperty("arg1", new Integer(i));
            System.out.println("add(" + parms3 + ") = " + testObject.execute("add", parms3));
        }
        parms.setProperty("arg1", new Integer(1));
        System.out.println("multiply(" + parms + ") = " + testObject.execute("multiply", parms));
        for (int i = 2; i < 5; i++) {
            parms.setProperty("arg1", new Integer(i));
            System.out.println("multiply(" + parms + ") = " + testObject.execute("multiply", parms));
        }
        try {
            parms.setProperty("arg1", new Integer(1));
            System.out.println("divide(" + parms + ") = " + testObject.execute("divide", parms));
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        parms.setProperty("arg1", new Integer(1));
        System.out.println("subtract(" + parms + ") = " + testObject.execute("subtract", parms));
    }

    public static class TestBean {

        private String name;

        private int age;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return (name);
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getAge() {
            return (age);
        }

        public void incrementAge() {
            age++;
        }

        public int add(int a, int b) {
            System.out.println("->TestBean.add(int,int)");
            return (a + b);
        }

        public double add(Number a, Number b) {
            System.out.println("->TestBean.add(Number,Number)");
            return (a.doubleValue() + b.doubleValue());
        }

        public String toString() {
            return ("TestBean[" + name + ", " + age + "]");
        }
    }
}
