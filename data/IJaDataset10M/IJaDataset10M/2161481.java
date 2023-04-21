package org.biomage.tools.generate_dtd;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.biomage.tools.generate_classes.CreateFile;
import org.biomage.tools.generate_classes.XMIParseHelpers;
import org.biomage.tools.helpers.StringOutputHelpers;
import org.w3c.dom.*;

/**
 * <b>Description:</b>
 *      Class that is resposible for generating a DTD file
 *      for the classes represented by the list of class nodes
 *      passed into the constructor.
 *
 */
public class WriteDTDFile {

    /**
     * <b>Description:</b>
     *      The top level class.  Even though it has no roles, must not
     *      be treated the same since it has no roles because it is the 
     *      top element
     */
    protected static Vector topElementNames = new Vector();

    protected void setTopElements(Element topElements) throws Exception {
        NodeList elements = topElements.getElementsByTagName("element");
        for (int i = 0; i < elements.getLength(); i++) {
            topElementNames.add(XMIParseHelpers.getElementText((Element) elements.item(i)));
        }
    }

    /**
     * <b>Description:</b>
     *      Utility method to verify whether class has roles or is top-level.
     *
     *<p>
     *  @param createFile: the class to check.
     *<p>
     *
     */
    protected static boolean isReferencedClass(CreateFile createFile) {
        return (null != createFile.getRoleNames() && 0 < createFile.getRoleNames().size()) || topElementNames.contains(createFile.getClassFileName());
    }

    /**
     * <b>Description:</b>
     *      An interface implemented by classes that take a class list and transform the
     *      members by either changing the relationships or adding or subtracting classes.
     */
    public static interface CreateClassTransformer {

        public void transform(Vector createFileList) throws Exception;
    }

    protected CreateClassTransformer transformer = null;

    /**
     * <b>Description:</b>
     *      Constructor for the DTD file generator.
     *
     *<p>
     *  @param classFiles: information on the DTD Elements to write.
     *  @param dtdFileName: what to name the output file.
     *  @param outputDir: where to write the file.
     *  @param header: templete file for the dtd header information.
     *  @param transformClassName: optional name of the class to transform the classFiles.
     *  @param packageOrder: order of the packages for the MAGE-ML elemnt and order of the lists
     *      for the packages.
     *  @param topElements: classes not referenced by any other class in the model itself.
     *<p>
     */
    public WriteDTDFile(Vector classFiles, String dtdFileName, String outputDir, File header, String transformClassName, Element topElements) throws Exception {
        if (null != transformClassName && 0 != transformClassName.length()) {
            Class transformClass = Class.forName(transformClassName);
            transformer = (CreateClassTransformer) transformClass.newInstance();
            StringOutputHelpers.writeOutput("Calling " + transformClassName + " to transform the class list.", 0);
            transformer.transform(classFiles);
        }
        setTopElements(topElements);
        classFiles = sortWriteFiles(classFiles);
        Vector writeFiles = CreateWriteFiles(classFiles);
        for (int i = 0; i < writeFiles.size(); i++) {
            ((WriteDTDElement) writeFiles.get(i)).createXMLStrings();
        }
        String path = "";
        if (null != outputDir) {
            path = outputDir + File.separatorChar;
        }
        FileWriter writer = new FileWriter(path + dtdFileName);
        writeHeader(writer, header);
        StringOutputHelpers.writeOutput("Writing Entities.", 0);
        for (int i = 0; i < writeFiles.size(); i++) {
            ((WriteDTDElement) writeFiles.get(i)).writeEntities(writer);
        }
        StringOutputHelpers.writeOutput("Writing Elements and Attributes.", 0);
        for (int i = 0; i < writeFiles.size(); i++) {
            ((WriteDTDElement) writeFiles.get(i)).writeBody(writer);
        }
        writer.close();
    }

    /**
     * <b>Description:</b>
     *      Create the write classes for each of the class files.
     *
     *<p>
     *  @param classFiles: information on the DTD Elements to write.
     *<p>
     */
    public Vector CreateWriteFiles(Vector classFiles) throws Exception {
        Vector writeFiles = new Vector(200);
        for (int i = 0; i < classFiles.size(); i++) {
            CreateFile createFile = (CreateFile) classFiles.elementAt(i);
            if (CreateFile.UML_CLASS == createFile.getFileType()) {
                writeFiles.add(new WriteDTDClassElement(createFile));
            } else if (CreateFile.UML_PACKAGE == createFile.getFileType()) {
                writeFiles.add(new WriteDTDPackageElement(createFile));
            } else if (CreateFile.UML_MODEL == createFile.getFileType()) {
                writeFiles.add(new WriteDTDMageElement(createFile));
            }
        }
        return writeFiles;
    }

    /**
     * <b>Description:</b>
     *      An inner class to hold the inter and intra package
     *      dependencies.
     */
    protected static class PackageDependencies {

        /**
        * <b>Description:</b>
        *      Names of packages this package is dependent on..
        */
        Set dependentOn = new HashSet();

        /**
        * <b>Description:</b>
        *      The classes that belong to this package.
        */
        Vector createFiles = new Vector();
    }

    public static Vector sortWriteFiles(Vector classFiles) throws Exception {
        StringOutputHelpers.writeOutput("Sorting CreateFiles by Inheritence Hierarchy and Package", 0);
        Map name2dependencies = new HashMap();
        for (int i = 0; i < classFiles.size(); i++) {
            CreateFile createFile = (CreateFile) classFiles.elementAt(i);
            String className = createFile.getClassFileName();
            String packageName = createFile.getPackageName();
            PackageDependencies dependencies = null;
            if (null == (dependencies = (PackageDependencies) name2dependencies.get(packageName))) {
                StringOutputHelpers.writeOutput("\tInitializing dependencies for " + packageName, 3);
                dependencies = new PackageDependencies();
                name2dependencies.put(packageName, dependencies);
            }
            int j = 0;
            for (; j < dependencies.createFiles.size(); j++) {
                CreateFile curCreateFile = (CreateFile) dependencies.createFiles.get(j);
                String curClassName = curCreateFile.getClassFileName();
                if (0 > className.compareTo(curClassName)) {
                    dependencies.createFiles.add(j, createFile);
                    break;
                }
            }
            if (j == dependencies.createFiles.size()) {
                dependencies.createFiles.add(createFile);
            }
        }
        Vector packageNames = new Vector(name2dependencies.keySet());
        for (int i = 0; i < packageNames.size(); i++) {
            StringOutputHelpers.writeOutput("\tOrdering files in package " + packageNames.get(i), 3);
            PackageDependencies dependencies = (PackageDependencies) name2dependencies.get(packageNames.get(i));
            Vector newOrder = new Vector(dependencies.createFiles.size());
            for (int j = dependencies.createFiles.size() - 1; j >= 0; j--) {
                CreateFile createFile = (CreateFile) dependencies.createFiles.get(j);
                CreateFile baseClass = createFile.getBaseClassCreateFile();
                if (null == baseClass) {
                    StringOutputHelpers.writeOutput("\t\tFinished with class " + createFile.getClassFileName(), 3);
                    newOrder.add(0, createFile);
                    continue;
                }
                String baseClassPackage = baseClass.getPackageName();
                if (baseClassPackage.equals(packageNames.get(i))) {
                    int k = newOrder.size() - 1;
                    done: for (; k >= 0; k--) {
                        baseClass = createFile.getBaseClassCreateFile();
                        String baseClassName = baseClass.getClassFileName();
                        CreateFile curCreateFile = (CreateFile) newOrder.get(k);
                        String curClassName = curCreateFile.getClassFileName();
                        while (true) {
                            if (baseClassName.equals(curClassName)) {
                                newOrder.add(k + 1, createFile);
                                break done;
                            }
                            CreateFile curBaseClass = baseClass.getBaseClassCreateFile();
                            if (null == curBaseClass) {
                                break;
                            }
                            String curBaseClassName = curBaseClass.getClassFileName();
                            String curBaseClassPackage = curBaseClass.getPackageName();
                            if (!curBaseClassPackage.equals(packageNames.get(i))) {
                                break;
                            } else {
                                baseClass = curBaseClass;
                                baseClassName = curBaseClassName;
                            }
                        }
                    }
                    if (-1 == k) {
                        newOrder.add(0, createFile);
                    }
                } else {
                    StringOutputHelpers.writeOutput("\tAdding dependency " + baseClass.getPackageName() + " to Package.", 3);
                    dependencies.dependentOn.add(baseClass.getPackageName());
                    newOrder.add(0, createFile);
                }
                StringOutputHelpers.writeOutput("\t\tFinished with class " + createFile.getClassFileName(), 3);
            }
            for (int j = 0; j < newOrder.size(); j++) {
                if (CreateFile.UML_PACKAGE == ((CreateFile) newOrder.get(j)).getFileType()) {
                    Object packageFile = newOrder.remove(j);
                    newOrder.add(0, packageFile);
                }
            }
            dependencies.createFiles = newOrder;
        }
        Vector newOrder = new Vector(packageNames.size());
        for (int i = 0; i < packageNames.size(); i++) {
            StringOutputHelpers.writeOutput("\tOrdering package " + packageNames.get(i), 3);
            PackageDependencies dependencies = (PackageDependencies) name2dependencies.get(packageNames.get(i));
            Vector dependentOn = new Vector(dependencies.dependentOn);
            int j = newOrder.size() - 1;
            for (; j >= 0; j--) {
                if (-1 < dependentOn.indexOf(newOrder.get(j))) {
                    newOrder.add(j + 1, packageNames.get(i));
                    break;
                }
            }
            if (-1 == j) {
                newOrder.add(0, packageNames.get(i));
            }
        }
        StringOutputHelpers.writeOutput("Original count of files: " + classFiles.size(), 3);
        classFiles.removeAllElements();
        for (int i = 0; i < newOrder.size(); i++) {
            StringOutputHelpers.writeOutput("\tAdding package " + newOrder.get(i), 3);
            PackageDependencies dependencies = (PackageDependencies) name2dependencies.get(newOrder.get(i));
            classFiles.addAll(dependencies.createFiles);
        }
        StringOutputHelpers.writeOutput("Rearranged count of files: " + classFiles.size(), 3);
        for (int i = 0; i < classFiles.size(); i++) {
            if (CreateFile.UML_MODEL == ((CreateFile) classFiles.get(i)).getFileType()) {
                Object mageFile = classFiles.remove(i);
                classFiles.add(0, mageFile);
            }
        }
        return classFiles;
    }

    /**
     * <b>Description:</b>
     *      Writes the header information out to the file.
     *
     *<p>
     *  @param writer: FileWriter to used to write to the file.
     *  @param header: File for the header information.
     *<p>
     *
     */
    protected void writeHeader(FileWriter writer, File header) throws Exception {
        FileReader reader = new FileReader(header);
        int curChar = -1;
        writer.write("<!--" + StringOutputHelpers.NEWLINE);
        while (-1 != (curChar = reader.read())) {
            writer.write(curChar);
        }
        writer.write(StringOutputHelpers.NEWLINE + "-->" + StringOutputHelpers.NEWLINE);
        reader.close();
    }
}
