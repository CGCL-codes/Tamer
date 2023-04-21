package ejbdoclet;

import java.beans.Introspector;
import java.net.*;
import java.io.*;
import java.util.*;
import com.sun.javadoc.*;

public class HomeInterfaceDoclet extends EJBDoclet {

    private static String DEFAULT_TEMPLATE_FILE = "home.j";

    public void setPattern(String new_pattern) {
        homeClassPattern = new_pattern;
    }

    public void execute() throws Exception {
        System.out.println("HomeInterfaceDoclet.execute");
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; i++) {
            setCurrentClass(classes[i]);
            if (isDocletGenerated(getCurrentClass())) continue;
            if (isEntity(getCurrentClass()) || isSession(getCurrentClass())) {
                if (hasEjbName(getCurrentClass())) {
                    File file = new File(destDir.toString(), javaFile(getGeneratedClassName()));
                    File beanFile = beanFile();
                    if (file.exists()) {
                        if (file.lastModified() > beanFile.lastModified()) {
                            continue;
                        }
                    }
                    System.out.println("Create home for:" + getCurrentClass().toString());
                    file.getParentFile().mkdirs();
                    out = new IndentPrintWriter(new BufferedWriter(new FileWriter(file)));
                    File template_file = getTemplateFile();
                    java.io.InputStream is = template_file == null ? this.getClass().getResourceAsStream(DEFAULT_TEMPLATE_FILE) : new BufferedInputStream(new FileInputStream(template_file));
                    generate(is);
                    out.close();
                }
            }
        }
    }

    protected String getGeneratedClassName() {
        return homeClass();
    }

    public String parentHomeClass() {
        if (hasHomeExtends(getCurrentClass())) {
            return getParameterValue(getText(getCurrentClass(), "ejb:home-extends"), "base-class-name", 0);
        }
        return "javax.ejb.EJBHome";
    }
}
