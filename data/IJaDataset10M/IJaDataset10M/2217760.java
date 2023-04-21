package com.princetonsoftech.maven.psteclipse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import edu.ucdavis.genomics.metabolomics.binbase.util.PackageFinder;

/**
 * <ul>
 * <li>Title: ManifestGenerator</li>
 * <li>Description: The class <code>ManifestGenerator</code> is a Mojo helper
 * that generates an Eclipse plugin manifest, and deploys all dependent
 * artifacts, for a binary plugin.</li>
 * <li>Created: Aug 31, 2006 by: prippete01</li>
 * </ul>
 * 
 * @author $Author: prippete01 $
 * @version $Revision: 1.7 $
 */
public class ManifestGenerator extends AbstractMojoHelper implements ManifestConstants {

    /**
	 * Legal copyright notice.
	 */
    public static final String COPYRIGHT = "Copyright (c) 2006, Princeton Softech Inc. All rights reserved.";

    /**
	 * SCCS header.
	 */
    public static final String HEADER = "$Header: /users1/cvsroot/maven-pst/maven-psteclipse-plugin/src/main/java/com/princetonsoftech/maven/psteclipse/ManifestGenerator.java,v 1.7 2007/02/08 22:02:30 prippete01 Exp $";

    /**
	 * The project.
	 */
    private MavenProject project;

    /**
	 * The buddies.
	 */
    private ArrayList buddies;

    /**
	 * The destination directory.
	 */
    private File destinationDirectory;

    /**
	 * The baseDir/lib directory where downloaded jars will live
	 */
    private File libDirectory;

    /**
	 * Constructs a new <code>ManifestGeneratorHelper</code> instance.
	 * 
	 * @param log
	 * @param baseDirectory
	 * @param project
	 * @param buddies
	 * @param destinationDirectory
	 */
    public ManifestGenerator(Log log, File baseDirectory, MavenProject project, ArrayList buddies, File destinationDirectory) {
        super(log, baseDirectory);
        this.project = project;
        this.buddies = buddies;
        this.destinationDirectory = destinationDirectory;
    }

    protected void doExecute() throws MojoExecutionException, MojoFailureException {
        File manifestDirectory = new File(destinationDirectory, MANIFEST_DIRECTORY);
        getLog().debug("The manifestDir is " + manifestDirectory);
        if (!manifestDirectory.exists()) {
            if (!manifestDirectory.mkdir()) {
                throw new MojoExecutionException("Unable to create directory '" + manifestDirectory + "'");
            }
        }
        libDirectory = new File(destinationDirectory, LIB_DIRECTORY);
        getLog().debug("The libDir is " + libDirectory);
        if (libDirectory.exists()) {
            getLog().info("deleting existing librrary directory!");
            libDirectory.delete();
        }
        if (!libDirectory.exists()) {
            if (!libDirectory.mkdir()) {
                throw new MojoExecutionException("Unable to create directory '" + libDirectory + "'");
            }
        }
        File manifestFile = getManifestFile(manifestDirectory);
        Manifest manifest = new Manifest();
        Attributes mainAttributes = manifest.getMainAttributes();
        writeInitialManifestAttributes(mainAttributes);
        resolveBundleClasspathEntries(mainAttributes);
        writeManifestToFile(manifestFile, manifest);
    }

    /**
	 * Returns the manifest file.
	 * 
	 * @param manifestDirectory
	 *            the manifest directory.
	 * @return the manifest file.
	 * @throws MojoExecutionException
	 */
    private File getManifestFile(File manifestDirectory) throws MojoExecutionException {
        File manifestFile = new File(manifestDirectory, MANIFEST_FILE_NAME);
        if (manifestFile.exists()) {
            getLog().info("deleting existing manifest file!");
            manifestFile.delete();
        }
        if (!manifestFile.exists()) {
            try {
                manifestFile.createNewFile();
            } catch (IOException e) {
                throw new MojoExecutionException("Could not create Manifest File", e);
            }
        } else {
            getLog().warn("PST Mojo Overwriting existing Manifest File");
        }
        return manifestFile;
    }

    /**
	 * Writes the manifest to file.
	 * 
	 * @param manifestFile
	 *            the manifest file.
	 * @param manifest
	 *            the manifest.
	 * @throws MojoExecutionException
	 */
    private void writeManifestToFile(File manifestFile, Manifest manifest) throws MojoExecutionException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(manifestFile);
            manifest.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new MojoExecutionException("Could not write out Manifest File");
        }
    }

    /**
	 * Resolves the bundle classpath entries.
	 * 
	 * @param mainAttributes
	 *            the main attributes.
	 * @throws MojoExecutionException
	 */
    private void resolveBundleClasspathEntries(Attributes mainAttributes) throws MojoExecutionException {
        Iterator dependecies = project.getCompileArtifacts().iterator();
        StringBuffer classpath = new StringBuffer();
        StringBuffer exportedPackages = new StringBuffer();
        StringBuffer importedPackages = new StringBuffer();
        while (dependecies.hasNext()) {
            Artifact artifact = (Artifact) dependecies.next();
            if (classpath.length() > 0) {
                classpath.append(",");
            }
            classpath.append(LIB_DIRECTORY);
            classpath.append(File.separator);
            File file = artifact.getFile();
            String fileName = file.getName();
            classpath.append(fileName);
            File localCopy = copyArtifact(file);
            getLog().info("working on: " + file);
            addExportedPackages(localCopy, exportedPackages);
            addImportPackage(localCopy, importedPackages);
        }
        mainAttributes.put(new Attributes.Name(BUNDLE_CLASSPATH), classpath.toString());
        mainAttributes.put(new Attributes.Name(EXPORT_PACKAGE), exportedPackages.toString());
        mainAttributes.put(new Attributes.Name(IMPORT_PACKAGE), importedPackages.toString());
    }

    /**
	 * Adds exported packages.
	 * 
	 * @param file
	 *            the jar file.
	 * @param exportedPackages
	 *            the buffer that holds the exported packages.
	 * @throws MojoExecutionException
	 */
    private void addExportedPackages(File file, StringBuffer exportedPackages) throws MojoExecutionException {
        ArrayList packageList = new ArrayList();
        try {
            JarFile jar = new JarFile(file);
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    try {
                        String packageName = getPackageForName(entryName);
                        if (!packageList.contains(packageName)) {
                            packageList.add(packageName);
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        getLog().warn("error! ignoring it: " + entryName + " - " + e.getMessage());
                    }
                }
            }
            jar.close();
        } catch (IOException e) {
            throw new MojoExecutionException("Could not introspect jar " + file.getAbsolutePath(), e);
        }
        if (packageList.size() > 0) {
            Object[] packages = packageList.toArray();
            for (int i = 0; i < packages.length; i++) {
                if (i > 0 || exportedPackages.length() > 0) {
                    exportedPackages.append(",");
                }
                exportedPackages.append(packages[i]);
            }
        }
    }

    /**
	 * adds an import package
	 * 
	 * @param file
	 * @param exportedPackages
	 * @throws MojoExecutionException
	 */
    private void addImportPackage(File file, StringBuffer exportedPackages) throws MojoExecutionException {
        ArrayList<String> packageList = new ArrayList<String>();
        try {
            JarFile jar = new JarFile(file);
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    try {
                        List<String> classes = PackageFinder.findFilterPackages(jar.getInputStream(jarEntry), "org.eclipse.", this.getLog());
                        for (String str : classes) {
                            if (packageList.contains(str) == false) {
                                packageList.add(str);
                            }
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                    }
                }
            }
            jar.close();
        } catch (IOException e) {
            throw new MojoExecutionException("Could not introspect jar " + file.getAbsolutePath(), e);
        }
        if (packageList.size() > 0) {
            Object[] packages = packageList.toArray();
            for (int i = 0; i < packages.length; i++) {
                if (i > 0 || exportedPackages.length() > 0) {
                    exportedPackages.append(",");
                }
                exportedPackages.append(packages[i]);
            }
        }
    }

    /**
	 * Gets the package for the specified entry name.
	 * 
	 * @param entryName
	 *            the jar entry's name.
	 * @return the package name for the entry.
	 */
    private String getPackageForName(String entryName) {
        getLog().debug("current: " + entryName);
        entryName = entryName.substring(0, entryName.lastIndexOf('/'));
        String packageName = entryName.replace('/', '.');
        return packageName;
    }

    /**
	 * Copies the artifact.
	 * 
	 * @param file
	 *            the artifact's file.
	 * @return the copy.
	 * @throws MojoExecutionException
	 */
    private File copyArtifact(File file) throws MojoExecutionException {
        String fileName = file.getName();
        File copy = new File(libDirectory, fileName);
        if (!copy.exists()) {
            try {
                copy.createNewFile();
            } catch (IOException e) {
                throw new MojoExecutionException("Could not create new File " + fileName, e);
            }
        }
        try {
            copyFile(file, copy);
        } catch (IOException e) {
            throw new MojoExecutionException("Error Copying file " + fileName, e);
        }
        return copy;
    }

    /**
	 * Writes the initial main attributes.
	 * 
	 * @param mainAttributes
	 *            the main attributes.
	 */
    private void writeInitialManifestAttributes(Attributes mainAttributes) {
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION_VALUE);
        mainAttributes.put(new Attributes.Name(BUNDLE_MANIFEST_VERSION), BUNDLE_MANIFEST_VERSION_VALUE);
        mainAttributes.put(new Attributes.Name(BUNDLE_NAME), project.getName());
        mainAttributes.put(new Attributes.Name(BUNDLE_SYMBOLIC_NAME), project.getArtifactId());
        String version = project.getVersion();
        int index = version.indexOf("-SNAPSHOT");
        if (index > 0) {
            version = version.substring(0, index);
        }
        mainAttributes.put(new Attributes.Name(BUNDLE_VERSION), version);
        mainAttributes.put(new Attributes.Name(BUNDLE_VENDOR), BUNDLE_VENDOR_VALUE);
        mainAttributes.put(new Attributes.Name(BUNDLE_LOCALIZATION), BUNDLE_LOCALIZATION_VALUE);
        mainAttributes.put(new Attributes.Name(ECLIPSE_BUDDY_POLICY), ECLIPSE_BUDDY_POLICY_VALUE);
        if (buddies != null && buddies.size() > 0) {
            StringBuffer buddyList = new StringBuffer();
            Object[] buddyArray = buddies.toArray();
            for (int i = 0; i < buddyArray.length; i++) {
                if (i > 0) {
                    buddyList.append(",");
                }
                buddyList.append(buddyArray[i]);
            }
            mainAttributes.put(new Attributes.Name(ECLIPSE_REGISTER_BUDDY), buddyList.toString());
        }
    }
}
