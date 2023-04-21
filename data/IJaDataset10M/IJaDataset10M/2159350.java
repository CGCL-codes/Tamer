package edu.ucdavis.genomics.metabolomics.ant.task.hibernate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author wohlgemuth
 * generates the minimal hibernate configfile
 */
public class GenerateConfig extends Task {

    String generateDir;

    String outputDir;

    public void execute() throws BuildException {
        super.execute();
        if (generateDir == null) {
            throw new BuildException("you must set a directory");
        }
        generateDir = generateDir.replace('/', File.separatorChar);
        if (outputDir == null) {
            throw new BuildException("you must set a directory");
        }
        File dir = new File(getProject().getBaseDir() + File.separator + generateDir);
        File file = new File(getProject().getBaseDir() + File.separator + outputDir + File.separator + "hibernate.cfg.xml");
        if (dir.exists() == false) {
            throw new BuildException("directory " + dir.getAbsolutePath() + " doesn't exist");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("<!DOCTYPE hibernate-configuration PUBLIC \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\" \"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\n");
            writer.write("<hibernate-configuration>\n");
            writer.write("<session-factory>\n");
            scan(dir, writer);
            writer.write("</session-factory>");
            writer.write("</hibernate-configuration>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    private void scan(File dir, BufferedWriter writer) throws IOException {
        if (dir.isDirectory()) {
            File files[] = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                scan(files[i], writer);
            }
        } else {
            if (dir.getName().endsWith("hbm.xml")) {
                String nameOfFile = dir.toString();
                System.out.println(nameOfFile);
                System.out.println(getProject().getBaseDir());
                System.out.println(generateDir);
                String begin = getProject().getBaseDir() + File.separator + generateDir;
                begin = begin.replaceAll("\\\\", "\\\\\\\\");
                String nameOfEntry = nameOfFile.replaceFirst(begin, "");
                String name = nameOfEntry.replace('\\', '/');
                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }
                System.out.println("name: " + name);
                writer.write("<mapping resource=\"" + name + "\"/>\n");
            }
        }
    }

    /**
	 * 
	 */
    public GenerateConfig() {
        super();
    }

    public String getOutputDir() {
        return this.outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getGenerateDir() {
        return this.generateDir;
    }

    public void setGenerateDir(String generateDir) {
        this.generateDir = generateDir;
    }
}
