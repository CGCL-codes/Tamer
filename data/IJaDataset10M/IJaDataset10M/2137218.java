package org.vardb.util;

import java.util.ArrayList;
import java.util.List;
import org.vardb.CVardbException;

public class CJarHelper {

    private CJarHelper() {
    }

    public static Object forName(String s, String arg) {
        Object[][] args = new Object[][] { new Object[] {}, new Object[] { arg } };
        return forName(s, args);
    }

    @SuppressWarnings("unchecked")
    public static Object forName(String s, Object[][] args) {
        try {
            System.out.println("trying to create a new instance of class [" + s + "] with arguments");
            java.lang.reflect.Constructor c = Class.forName(s).getConstructors()[0];
            return c.newInstance(args[c.getParameterTypes().length]);
        } catch (Exception e) {
            throw new CVardbException(e);
        }
    }

    public static List<String> checkJarFiles(String libDir, String jarfile) {
        List<String> problems = new ArrayList<String>();
        String str = CFileHelper.readFile(jarfile);
        List<String> jars = CFileHelper.listFiles(libDir, ".jar");
        for (String line : CStringHelper.splitLines(str)) {
            if (line.startsWith("#")) continue;
            int index = line.indexOf(" ");
            String action = line.substring(0, index);
            String jar = line.substring(index + 1);
            if (action.equals("ADD")) {
                if (!jars.contains(jar)) problems.add("Missing jar: " + jar);
            } else if (action.equals("DELETE")) {
                if (jars.contains(jar)) problems.add("Jar should be removed: " + jar);
            } else if (action.equals("REPLACE")) {
                List<String> arr = CStringHelper.split(jar, " WITH ");
                String oldjar = arr.get(0);
                String newjar = arr.get(1);
                if (jars.contains(oldjar) && !jars.contains(newjar)) problems.add("Jar " + oldjar + " should be replaced with " + newjar); else if (jars.contains(oldjar) && jars.contains(newjar)) problems.add("Jar " + oldjar + " should be removed (superceded by " + newjar + ")"); else if (!jars.contains(oldjar) && !jars.contains(newjar)) problems.add("Missing jar: " + newjar);
            }
        }
        System.out.println("" + problems.size() + " problems");
        for (String problem : problems) {
            System.out.println(problem);
        }
        return problems;
    }
}
