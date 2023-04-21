package classcreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Stefano
 */
public class Type {

    private static double outputFormatVersion = 1.0;

    public String name = "";

    public String packageName = "";

    public Type parameter = null;

    /**
     * Builds primitive Type
     * @param n
     */
    public Type(String n) {
        name = n;
    }

    /**
     * Builds Type
     * @param n
     */
    public Type(String n, String p) {
        name = n;
        packageName = p;
    }

    /**
     * Builds parametric primitive Type
     * @param n
     */
    public Type(String n, Type t) {
        name = n;
        parameter = t;
    }

    /**
     * Builds parametric Type
     * @param n
     */
    public Type(String n, String p, Type t) {
        name = n;
        packageName = p;
        parameter = t;
    }

    public boolean isPrimitive() {
        return packageName.isEmpty();
    }

    public boolean isString() {
        return isPrimitive() && name.equals("String");
    }

    public boolean isConstructor() {
        return equals(Naming.constructorType);
    }

    public boolean isParametric() {
        return parameter != null;
    }

    public boolean equals(Type t) {
        if (parameter == null || t.parameter == null) return name.equals(t.name) && packageName.equals(t.packageName); else return name.equals(t.name) && packageName.equals(t.packageName) && parameter.equals(t.parameter);
    }

    public String toString() {
        if (isConstructor()) return "";
        if (isPrimitive() && !isParametric()) return name;
        if (isPrimitive() && isParametric()) return name + "<" + parameter.toString() + ">";
        if (isParametric()) return packageName + "." + name + "<" + parameter.toString() + ">"; else return packageName + "." + name;
    }

    public static Type parse(BufferedReader br, TypeRepository tr) throws IOException {
        br.readLine();
        double version = Double.parseDouble(br.readLine());
        if (version <= 1.0) {
            String name = br.readLine();
            String pname = br.readLine();
            int tt = Integer.parseInt(br.readLine());
            if (tt == -1) {
                return new Type(name, pname, null);
            } else {
                return new Type(name, pname, tr.get(tt));
            }
        }
        String name = br.readLine();
        String pname = br.readLine();
        int tt = Integer.parseInt(br.readLine());
        if (tt == -1) {
            return new Type(name, pname, null);
        } else {
            return new Type(name, pname, tr.get(tt));
        }
    }

    public void permanentize(BufferedWriter br, TypeRepository tr) throws IOException {
        br.write("/***/ Type: " + name);
        br.newLine();
        br.write(String.valueOf(outputFormatVersion));
        br.newLine();
        br.write(name);
        br.newLine();
        br.write(packageName);
        br.newLine();
        if (parameter == null) {
            br.write(String.valueOf(-1));
            br.newLine();
            return;
        }
        for (int i = 0; i < tr.size(); i++) {
            if (tr.get(i).equals(parameter)) {
                br.write(String.valueOf(i));
                br.newLine();
                return;
            }
        }
        br.write(String.valueOf(-1));
        br.newLine();
    }
}
