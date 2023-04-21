package bytecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import recoder.ParserException;
import recoder.bytecode.ByteCodeParser;
import recoder.bytecode.ClassFile;
import recoder.bytecode.ClassFileParser;
import recoder.bytecode.FieldInfo;
import recoder.bytecode.MemberInfo;
import recoder.bytecode.MethodInfo;

public class TestByteCode {

    private boolean equals(String s, String t) {
        if (s == null) return t == null; else return s.equals(t);
    }

    private boolean equals(String[] s, String[] t) {
        if (s == null) return t == null;
        if (t == null) return false;
        if (s.length != t.length) {
            return false;
        }
        for (int i = 0; i < s.length; i++) {
            if (!equals(s[i], t[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean equals(List<? extends MemberInfo> f, List<? extends MemberInfo> g) {
        if (f == null) return g == null;
        if (g == null) return false;
        if (f.size() != g.size()) return false;
        boolean res = true;
        for (MemberInfo fi : f) {
            boolean found = false;
            for (MemberInfo gi : g) {
                if (fastEquals(fi, gi)) {
                    found = true;
                    if (!equals(fi, gi)) res = false;
                }
            }
            res &= found;
        }
        return res;
    }

    private boolean fastEquals(MemberInfo f, MemberInfo g) {
        if (f instanceof FieldInfo && g instanceof FieldInfo) {
            return f.getName().equals(g.getName());
        } else if (f instanceof MethodInfo && g instanceof MethodInfo) {
            if (f.getName().equals(g.getName())) return equals(((MethodInfo) f).getParameterTypeNames(), ((MethodInfo) g).getParameterTypeNames()); else return false;
        } else return false;
    }

    private boolean equals(MemberInfo f, MemberInfo g) {
        if (f instanceof FieldInfo && g instanceof FieldInfo) return equals((FieldInfo) f, (FieldInfo) g); else if (f instanceof MethodInfo && g instanceof MethodInfo) return equals((MethodInfo) f, (MethodInfo) g); else return false;
    }

    private boolean equals(MethodInfo f, MethodInfo g) {
        return false;
    }

    private boolean equals(FieldInfo f, FieldInfo g) {
        boolean res = true;
        if (f.getAccessFlags() != g.getAccessFlags()) {
            System.err.println(f.getName() + " getFullName differs");
        }
        if (!equals(f.getName(), g.getName())) {
            System.err.println(f.getName() + " getName differs");
            res = false;
        }
        if (!equals(f.getTypeName(), g.getTypeName())) {
            System.err.println(f.getName() + " getTypeName differs");
            res = false;
        }
        if (!equals(f.getConstantValue(), g.getConstantValue())) {
            System.err.println(f.getName() + " getConstantValue differs");
            res = false;
        }
        return res;
    }

    private boolean equals(ClassFile c, ClassFile d) {
        boolean res = true;
        if (!equals(c.getFullName(), d.getFullName())) {
            System.err.println("getFullName differs");
            res = false;
        }
        if (!equals(c.getLocation(), d.getLocation())) {
            System.err.println("getLocation differs");
            res = false;
        }
        if (!equals(c.getPhysicalName(), d.getPhysicalName())) {
            System.err.println("getPhysicalName differs");
            res = false;
        }
        if (!equals(c.getTypeName(), d.getTypeName())) {
            System.err.println("getTypeName differs");
            res = false;
        }
        if (c.getAccessFlags() != d.getAccessFlags()) {
            System.err.println("getAccessFlags differs");
            res = false;
        }
        if (!equals(c.getSuperClassName(), d.getSuperClassName())) {
            System.err.println("getSuperClassName differs");
            res = false;
        }
        if (!equals(c.getInterfaceNames(), d.getInterfaceNames())) {
            System.err.println("getInterfaceNames differs");
            res = false;
        }
        if (!equals(c.getInnerClassNames(), d.getInnerClassNames())) {
            System.err.println("getInnerClassNames differs");
            res = false;
        }
        if (!equals(c.getFieldInfos(), d.getFieldInfos())) {
            System.err.println("getFieldInfos differs");
            res = false;
        }
        if (!equals(c.getMethodInfos(), d.getMethodInfos())) {
            System.err.println("getMethodInfos differs");
            res = false;
        }
        if (!equals(c.getConstructorInfos(), d.getConstructorInfos())) {
            System.err.println("getConstructorInfos differs");
            res = false;
        }
        return res;
    }

    public void run() throws Exception {
        File file = new File("/home/fran/Projects/msi/TestClass.class");
        ByteCodeParser bcp = new ByteCodeParser();
        bcp.readJava5Signatures = false;
        ClassFile c = bcp.parseClassFile(new FileInputStream(file));
        ClassFileParser cfp = new ClassFileParser();
        ClassFile d = cfp.parseClassFile(new FileInputStream(file));
        equals(c, d);
        System.out.println(c);
        System.out.println(d);
    }

    /**
	 * @param args
	 * @throws IOException
	 * @throws ParserException
	 * @throws FileNotFoundException
	 */
    public static void main(String[] args) throws Exception {
        TestByteCode tbc = new TestByteCode();
        tbc.run();
    }
}
