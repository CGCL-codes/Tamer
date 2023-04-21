package org.snipsnap.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.List;

public class PropertyConstantBuilder extends Task {

    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("usage: " + PropertyConstantBuilder.class.getName() + " propertiesfile interface [prefix]");
            System.exit(-1);
        }
        String prefix = "";
        if (args.length > 2) {
            prefix = args[2];
        }
        try {
            new PropertyConstantBuilder().buildClass(args[0], new File(args[1]), prefix);
        } catch (BuildException e) {
            System.err.println("class build failed: " + e.getMessage());
            System.exit(-1);
        }
        System.exit(0);
    }

    private String propertiesFile = null;

    private String stubFile = null;

    private String prefix = "";

    public void execute() throws BuildException {
        buildClass(propertiesFile, new File(stubFile), prefix);
    }

    public void setFile(String file) {
        this.stubFile = file;
    }

    public void setProperties(String file) {
        this.propertiesFile = file;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void buildClass(String propertiesFile, File file, String prefix) throws BuildException {
        Properties defaults = new Properties();
        String[] files = propertiesFile.split(" *, *");
        for (int fileCount = 0; fileCount < files.length; fileCount++) {
            try {
                defaults.load(new FileInputStream(files[fileCount]));
            } catch (IOException e) {
                throw new BuildException("properties file '" + files[fileCount] + "' not found.");
            }
        }
        try {
            PrintWriter stubWriter = new PrintWriter(new FileWriter(stubFile));
            stubWriter.println();
            stubWriter.println("  // automatically created interface/constants stub from");
            stubWriter.println("  // " + propertiesFile);
            stubWriter.println("  // generated on " + new SimpleDateFormat().format(new Date()));
            createConstants(stubWriter, defaults, prefix);
            stubWriter.close();
        } catch (IOException e) {
            throw new BuildException("error writing class sources: " + e.getMessage());
        }
    }

    private void createConstants(PrintWriter stubWriter, Properties properties, String prefix) {
        TreeSet sorted = new TreeSet(properties.keySet());
        Iterator it = sorted.iterator();
        while (it.hasNext()) {
            String property = (String) it.next();
            stubWriter.println("  // constant/getter for '" + property + "'");
            stubWriter.print("  public final static String " + property.toUpperCase().replace('.', '_'));
            stubWriter.println(" = \"" + property + "\";");
            stubWriter.println("  public String get" + getCamlCase(property, prefix) + "();");
            stubWriter.println("  public String set" + getCamlCase(property, prefix) + "(String value);");
        }
    }

    private String getCamlCase(String name, String prefix) {
        if (name.startsWith(prefix)) {
            name = name.substring(prefix.length());
        }
        StringTokenizer tokenizer = new StringTokenizer(name, ".", false);
        StringBuffer result = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            result.append(token.substring(0, 1).toUpperCase());
            if (token.length() > 1) {
                result.append(token.substring(1));
            }
        }
        return result.toString();
    }
}
