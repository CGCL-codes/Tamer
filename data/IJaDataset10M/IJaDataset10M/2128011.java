package org.jaffa.tools.build.ant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.jaffa.tools.common.SourceDecomposerUtils;

/** This task is used to list the customizations made to code generated classes.
 * Example usage:
 *   <listcustomizations outputFileName="aFileNameWithPath">
 *     <!-- Provide the list of files to scan via nested filesets -->
 *     <fileset .../>
 *   </listcustomizations>
 *
 * @author GautamJ
 */
public class ListCustomizationsTask extends Task {

    private String customizationFilter;

    private String outputFileName;

    private List filesets = new LinkedList();

    /** Getter for property customizationFilter.
     * @return Value of property customizationFilter.
     */
    public String getCustomizationFilter() {
        return this.customizationFilter;
    }

    /** Setter for property customizationFilter.
     * @param customizationFilter New value of property customizationFilter.
     */
    public void setCustomizationFilter(String customizationFilter) {
        this.customizationFilter = customizationFilter;
    }

    /** Getter for property outputFileName.
     * @return Value of property outputFileName.
     */
    public String getOutputFileName() {
        return this.outputFileName;
    }

    /** Setter for property outputFileName.
     * @param outputFileName New value of property outputFileName.
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    /** Adds a FileSet.
     * @param fs A FileSet.
     */
    public void addFileset(FileSet fs) {
        this.filesets.add(fs);
    }

    /** Scans the set of files, specified via the nested <fileset> elemet(s), for custom code.
     * @throws BuildException if any error occurs
     */
    public void execute() throws BuildException {
        Set filesToScan = new LinkedHashSet();
        for (Iterator itr = filesets.iterator(); itr.hasNext(); ) {
            FileSet fs = (FileSet) itr.next();
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            File basedir = ds.getBasedir();
            String[] files = ds.getIncludedFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) filesToScan.add(new File(basedir, files[i]));
            }
        }
        if (filesToScan.size() > 0) {
            BufferedWriter writer = null;
            File fileToScan = null;
            try {
                writer = getOutputFileName() != null ? new BufferedWriter(new FileWriter(getOutputFileName())) : new BufferedWriter(new OutputStreamWriter(System.out));
                for (Iterator itr = filesToScan.iterator(); itr.hasNext(); ) {
                    fileToScan = (File) itr.next();
                    SourceDecomposerUtils.listCustomizations(fileToScan, writer, getCustomizationFilter());
                    writer.flush();
                }
                if (getOutputFileName() != null) System.out.println("Customizations have been written to: " + getOutputFileName());
            } catch (Exception e) {
                throw new BuildException("Error in scanning the file for custom code: " + fileToScan, e);
            } finally {
                try {
                    if (getOutputFileName() != null && writer != null) writer.close();
                } catch (Exception e) {
                    throw new BuildException("Error in closing the file: " + getOutputFileName(), e);
                }
            }
        }
    }
}
