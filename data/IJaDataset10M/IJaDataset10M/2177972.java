package au.edu.qut.yawl.enhancedworklist.cpn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.*;

public class RuntimeCompiler {

    private static final String USER_DIR = "user.dir";

    private static final String CLASS_PATH = "au.edu.qut.yawl.enhancedworklist.cpn";

    private static com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();

    private static File _classFile;

    private static String _className;

    private static String GetClassName(String fileName) {
        return fileName.substring(0, fileName.length() - 5);
    }

    public static synchronized void Compile(String classFileName, String code) {
        String path = "E:\\study\\thesis\\sourcecode\\yawl\\classes";
        String classPath = "E:\\study\\thesis\\sourcecode\\yawl\\classes\\au\\edu\\qut\\yawl\\enhancedworklist\\cpn";
        File fileTemp = new File(path);
        try {
            _classFile = File.createTempFile(classFileName, ".java", fileTemp);
        } catch (IOException e) {
            return;
        }
        _classFile.deleteOnExit();
        String fileName = _classFile.getName();
        _className = GetClassName(fileName);
        String classFileContent = "public class " + _className + "{" + code + "}";
        System.out.println(classFileContent);
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(_classFile));
        } catch (FileNotFoundException e) {
            return;
        }
        out.println("package au.edu.qut.yawl.enhancedworklist.cpn;\n" + "public class " + _className + "{");
        out.println(code);
        out.println("}");
        out.flush();
        out.close();
        String[] args = new String[] { "-d", path, path + "\\" + fileName };
        int status = javac.compile(args);
        System.out.println("compile result" + status);
        try {
            Class cls = Class.forName("au.edu.qut.yawl.enhancedworklist.cpn." + _className);
            Method m[] = cls.getDeclaredMethods();
            for (int i = 0; i < m.length; i++) System.out.println(m[i].toString());
        } catch (ClassNotFoundException e1) {
            return;
        } catch (SecurityException e) {
            return;
        }
    }

    public static synchronized Object run(String methodName, Class argClasses[], Object[] args) {
        new File(_classFile.getParent(), _className + ".class").deleteOnExit();
        try {
            String classPath = CLASS_PATH + "." + _className;
            Class cls = Class.forName(classPath);
            Class[] Classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                Classes[i] = args[i].getClass();
                System.out.print(Classes[i].toString() + "\n");
                System.out.print(argClasses[i].toString() + "\n");
            }
            Method method = cls.getMethod(methodName, argClasses);
            Object obj = cls.newInstance();
            return method.invoke(obj, args);
        } catch (InstantiationException e0) {
            System.out.println("run: instantiation");
            return null;
        } catch (ClassNotFoundException e1) {
            System.out.println("run: class not found");
            return null;
        } catch (NoSuchMethodException e2) {
            System.out.println("run: no such method");
            return null;
        } catch (IllegalAccessException e3) {
            System.out.println("run: illegal access");
            return null;
        } catch (InvocationTargetException e4) {
            System.out.println("run: invocation target");
            return null;
        } catch (SecurityException e) {
            System.out.println("run: security");
            return null;
        }
    }
}
