package org.xaware.ide.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xaware.ide.xadev.common.BundleUtils;
import org.xaware.shared.util.BuildXAwareHome;

/**
 * A utility class for extracting jar files containing xaware.home files and directories. This is designed to be used
 * during installation of designer or server and will create the home directory with the required files specified by an
 * input jar file.
 * 
 * @author dwieland
 * 
 */
public class BuildXAwareHomeDesigner extends BuildXAwareHome {

    /**
     * Extract the zip file containing the home files and directories to a specified location overwrite any existing
     * files and directories based on the overwrite parameter.
     * 
     * @param xawarehome
     * @param xawarehomezip
     * @param overwrite
     * @throws Exception
     */
    @Override
    public void buildHome(final String xawarehome, final InputStream xawarehomezip, final boolean overwrite) throws Exception {
        setOSFlag();
        final File dir = new File(xawarehome);
        dir.mkdir();
        extractZipStream(xawarehomezip, dir.getAbsolutePath(), overwrite);
        substituteHome(xawarehome);
    }

    /**
     * Use AntRunner to substitute xaware.home in script files
     */
    @Override
    protected void substituteHome(final String p_xawarehome) {
        updateXAwareHomeSubstitution(p_xawarehome);
        final File antBuildFile = new File(p_xawarehome + File.separator + "conf/SubstitutionFileList.xml");
        if (antBuildFile.exists()) {
            try {
                final IProgressMonitor monitor = new NullProgressMonitor();
                final AntRunner runner = new AntRunner();
                runner.setBuildFileLocation(antBuildFile.getAbsolutePath());
                final String[] executionTargets = new String[1];
                if (isWindows) {
                    executionTargets[0] = "substitute_windows";
                } else {
                    executionTargets[0] = "substitute_unix";
                }
                runner.setExecutionTargets(executionTargets);
                runner.run(monitor);
            } catch (final CoreException e) {
                System.out.println("Failed to run ANT substitution " + e.getMessage());
            }
        }
    }

    /**
     * extract jars from existing resource bundle using DesignerHomePluginJars.xml
     * 
     * @param xawareHome
     * @throws IOException
     */
    public void extractJarsFromResources(final String xawareHome) throws IOException {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("DesignerHomePluginJars.xml");
        final SAXBuilder builder = new SAXBuilder();
        try {
            final org.jdom.Document doc = builder.build(is);
            final Element rootElem = doc.getRootElement();
            final List sourcePluginElems = rootElem.getChildren("SourcePlugin");
            for (final Iterator i = sourcePluginElems.iterator(); i.hasNext(); ) {
                final Element sourcePluginElem = (Element) i.next();
                final String symbName = sourcePluginElem.getAttributeValue("symbolicName");
                final List jarNameElems = sourcePluginElem.getChildren("JarName");
                for (final Iterator i2 = jarNameElems.iterator(); i2.hasNext(); ) {
                    final Element jarNameElem = (Element) i2.next();
                    final String jarName = jarNameElem.getTextTrim();
                    final String sourcePath = jarNameElem.getAttributeValue("sourcePath");
                    final String destinationPath = jarNameElem.getAttributeValue("destinationPath");
                    String pathWithinBundle = sourcePath + "/" + jarName;
                    if (sourcePath.equals(".")) {
                        pathWithinBundle = jarName;
                    }
                    final File destinationDir = new File(xawareHome, destinationPath);
                    final File destinationFile = new File(destinationDir, jarName);
                    try {
                        BundleUtils.extractBundleEntry(symbName, pathWithinBundle, destinationFile);
                    } catch (final IOException e) {
                        System.out.println("Exception extracting " + jarName + ": " + e.getMessage());
                    }
                }
            }
        } catch (final JDOMException e) {
            System.out.println("FAILED TO BUILD DOCUMENT FROM DesignerHomePluginJars.xml: " + e);
        }
    }

    /**
     * Usage: org.xaware.shared.util.BuildXAwareHome xawarehome=c:\\xaware_home xawarehomezip=c:\\homeJarFile.jar
     * overwrite=yes
     * 
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java org.xaware.shared.util.BuildXAwareHome xawarehome=c:\\xaware_home xawarehomezip=c:\\homeJarFile.jar overwrite=yes");
            return;
        }
        try {
            final HashMap<String, String> parms = new HashMap<String, String>(10);
            for (int i = 0; i < args.length; i++) {
                final String[] parm = args[i].split("=");
                parms.put(parm[0], parm[1]);
            }
            boolean overwrite = false;
            final String overwriteStr = (String) parms.get("overwrite");
            if (overwriteStr != null && overwriteStr.equals("yes")) {
                overwrite = true;
            }
            final InputStream insZipFile = new FileInputStream((String) parms.get("xawarehomezip"));
            final BuildXAwareHomeDesigner xaHomeBuilder = new BuildXAwareHomeDesigner();
            xaHomeBuilder.buildHome((String) parms.get("xawarehome"), insZipFile, overwrite);
        } catch (final Exception e) {
            System.err.println("Unhandled exception:");
            e.printStackTrace();
            return;
        }
    }
}
