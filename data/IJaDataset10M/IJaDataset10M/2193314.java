package tools;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import function.io.IOFunction;
import function.io.LineReader;

public class AbstractImplementor {

    /**
		Retrieve Classnames to implement from 
		args and STD-IN
	*/
    static Object[] getClassName(String[] args) {
        final ArrayList list = new ArrayList();
        for (int i = 0; i != args.length; i++) {
            list.add(args[i]);
        }
        LineReader.readStream(System.in, new IOFunction() {

            public void apply(Object obj) throws IOException {
                list.add(obj);
            }
        });
        return (list.toArray());
    }

    static Object[] getAbstractMethods(Object[] classNames) {
        ArrayList list = new ArrayList();
        for (int i = 0; i != classNames.length; i++) {
            addAbstractMethods(classNames[i].toString(), list);
        }
        return list.toArray();
    }

    static void addAbstractMethods(String className, List list) {
        try {
            Class c = Class.forName(className);
            Method[] m = c.getDeclaredMethods();
            for (int i = 0; i != m.length; i++) {
                if (Modifier.isAbstract(m[i].getModifiers())) list.add(m[i]);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void writeMethods(Object[] m) {
        for (int i = 0; i != m.length; i++) {
            printMethod((Method) m[i]);
        }
    }

    static void printMethod(Method m) {
        System.out.print("\n\n\t");
        if (Modifier.isPublic(m.getModifiers())) System.out.print("public "); else if (Modifier.isProtected(m.getModifiers())) System.out.print("protected ");
        System.out.print(Tools.getDeclaration(m.getReturnType()));
        System.out.print(" ");
        System.out.print(m.getName());
        System.out.print(" (");
        Class[] cs = m.getParameterTypes();
        for (int i = 0; i != cs.length; i++) {
            if (i != 0) System.out.print(", ");
            System.out.print(Tools.getDeclaration(cs[i]));
            System.out.print(" arg" + i);
        }
        System.out.print(")");
        Class[] exc = m.getExceptionTypes();
        for (int i = 0; i != exc.length; i++) {
            if (i == 0) System.out.print(" throws "); else System.out.print(", ");
            System.out.print(Tools.getDeclaration(exc[i]));
        }
        System.out.print(" {\n\t}\n");
    }

    public static void main(String[] args) {
        Object[] classNames = getClassName(args);
        Object[] methods = getAbstractMethods(classNames);
        writeMethods(methods);
    }
}
