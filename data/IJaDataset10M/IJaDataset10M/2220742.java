package br.gov.demoiselle.jasperreports.maven.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal compile-reports
 * @parameter expression="${project.build.directory}"
 */
public class CompileReports extends AbstractMojo {

    /**
	 * Location of the file.
	 *
	 * @parameter expression="${outputDirectory}"
	 * @required
	 */
    private File outputDirectory;

    /**
	 * Location of the file.
	 *
	 * @parameter expression="${inputDirectory}"
	 * @required
	 */
    private File inputDirectory;

    public void execute() throws MojoExecutionException {
        Long count = 0L;
        StringBuffer msg = new StringBuffer();
        msg.append("\n-------------------------------------------------------");
        msg.append("\nCompiling Reports");
        msg.append("\n-------------------------------------------------------");
        getLog().info(msg.toString());
        getLog().info("Input Directory....: " + inputDirectory.toString());
        getLog().info("Output Directory...: " + outputDirectory.toString());
        if (inputDirectory.isDirectory()) {
            for (File jrxmlFile : inputDirectory.listFiles()) {
                if (isCompile(jrxmlFile)) {
                    InputStream inputStream;
                    try {
                        inputStream = new BufferedInputStream(new FileInputStream(jrxmlFile));
                        JasperDesign design = JRXmlLoader.load(inputStream);
                        if (!outputDirectory.exists()) {
                            getLog().info("Created Directory: " + outputDirectory.getAbsolutePath());
                            outputDirectory.mkdirs();
                        }
                        getLog().info("Compiling: " + jrxmlFile.getName());
                        JasperCompileManager.compileReportToFile(design, getFileJasperName(jrxmlFile));
                        count++;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        throw new MojoExecutionException("File '" + jrxmlFile.getAbsolutePath() + "' not found", e);
                    } catch (JRException e) {
                        e.printStackTrace();
                        throw new MojoExecutionException("JRException", e);
                    }
                }
            }
        } else {
            getLog().warn("Directory " + inputDirectory.toString() + " not exist");
        }
        if (count > 0) {
            getLog().info(count + " reports compiled successfully.\n-------------------------------------------------------");
        } else {
            getLog().info("No report was compiled.\n-------------------------------------------------------");
        }
    }

    private boolean isCompile(File jrxmlFile) {
        String jasperFileName = getFileJasperName(jrxmlFile);
        if (!jrxmlFile.getName().endsWith("jrxml")) {
            return false;
        }
        File jasper = new File(jasperFileName);
        if (jasper.exists()) {
            if (jrxmlFile.lastModified() < jasper.lastModified()) {
                return false;
            }
        }
        return true;
    }

    private String getFileJasperName(File jrxmlFile) {
        StringBuffer fileNameOut = new StringBuffer();
        fileNameOut.append(outputDirectory.getAbsolutePath());
        fileNameOut.append(File.separatorChar);
        fileNameOut.append(jrxmlFile.getName().replace(".jrxml", ".jasper"));
        return fileNameOut.toString();
    }
}
