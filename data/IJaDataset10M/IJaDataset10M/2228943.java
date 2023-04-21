package org.jbjf.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbjf.core.AbstractTask;
import org.jbjf.core.file.DelimitedDocumentAdapter;
import org.jbjf.core.file.FlatRow;
import org.jbjf.tasks.CopyFile;

/**
 * <h3>Description:</h3>
 * <hr>
 * <p>
 * The <code>CopyFilesInDocumentAdapter</code> task will copy files
 * from a source directory to a target directory for any files listed
 * in a delimited document object stored on the JBJF Job Stack.  The
 * filenames should not contain any directory path information, only
 * the filename.ext is needed.  There are two other &lt;resource&gt;
 * elements that provide the source and target directory paths.
 * </p>
 * <p>
 * The task will sub-contract out the actual copy file task, setting
 * up a sub-task (<code>org.jbjf.tasks.CopyFile</code>) and then 
 * pre-filling the sub-task resources prior to execution.  The 
 * <code>CopyFilesInDocumentAdapter</code> task has the following
 * limitations:<br>
 * <ul>
 * <li>Local Filesystems - While the task handles Windows UNC, the
 * general intent is that both the source and destination directories
 * are on the same machine.
 * </ul>
 * </p>
 * <p>
 * <h3>Dependencies:</h3>
 * <hr>
 * <ul>
 * <li>JBJF 1.3.0(+)</li>
 * <li>JRE/JDK 6(+)</li>
 * </ul>
 * </p>
 * <p>
 * <h3>Resources:</h3>
 * <hr>
 * The Task expects the following &lt;resource&gt; elements in the
 * JBJF Batch Definition &lt;task&gt; element to be defined:
 * <ul>
 * <li>directory-source - A directory path to the source directory
 * where the files reside.  The directory path can be relative or
 * absolute.
 * <li>directory-target - A directory path where you wish to copy
 * the files to.  The directory path can be relative or
 * absolute.
 * <li>job-stack-key - A Job Stack key to lookup the delimited 
 * document adapter (<code>org.jbjf.core.file.DelimitedDocumentAdapter</code>)
 * that contains the list of filenames to copy.  These filenames
 * should not contain any directory path information.
 * </ul>
 * <p>
 * <h3>Details</h3>
 * <hr>
 * <h4>Input Resources (&lt;resource&gt;) Elements</h4>
 * <table border='1' width='65%'>
 * <thead>
 *  <tr>
 *      <td width='15%'>Location</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td width='15%'>Id/Name</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td width='25%'>Type</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td width='10%'>Required</td>
 *      <td width='1%'>&nbsp;</td>
 *      <td>Description/Comments</td>
 *  </tr>
 *  </thead>
 *  <tr valign='top'>
 *      <td>&lt;task&gt;</td>
 *      <td>&nbsp;</td>
 *      <td>directory-source</td>
 *      <td>&nbsp;</td>
 *      <td>String</td>
 *      <td>&nbsp;</td>
 *      <td>True</td>
 *      <td>&nbsp;</td>
 *      <td>The source directory, relative or absolute, where the
 *      files reside.
 *      </td>
 *  </tr>
 *  <tr valign='top'>
 *      <td>&lt;task&gt;</td>
 *      <td>&nbsp;</td>
 *      <td>directory-target</td>
 *      <td>&nbsp;</td>
 *      <td>String</td>
 *      <td>&nbsp;</td>
 *      <td>True</td>
 *      <td>&nbsp;</td>
 *      <td>The destination directory, relative or absolute, where the
 *      files should be copied to.
 *      </td>
 *  </tr>
 *  <tr valign='top'>
 *      <td>&lt;task&gt;</td>
 *      <td>&nbsp;</td>
 *      <td>job-stack-key</td>
 *      <td>&nbsp;</td>
 *      <td>String</td>
 *      <td>&nbsp;</td>
 *      <td>True</td>
 *      <td>&nbsp;</td>
 *      <td>A key to the JBJF job stack that can lookup the delimited
 *      document adapter (<code>org.jbjf.core.file.DelimitedDocumentAdapter</code>) that 
 *      contains the list of filenames.
 *      </td>
 *  </tr>
 * </table>
 * </p>
 * <p>
 * The following is an example XML &lt;task&gt; element:
 * </p>
 * <p>
 * <pre>
 * &lt;jbjf-tasks>
 *     &lt;task name="t003" order="3" active="true"&gt;
 *         &lt;class&gt;org.jbjf.tasks.CopyFilesInDocumentAdapter&lt;/class&gt;
 *         &lt;resource type="directory-source"&gt;C:\My Documents\big-app\data-files&lt;/resource&gt;
 *         &lt;resource type="directory-target"&gt;E:\My Documents\big-app\persistent&lt;/resource&gt;
 *         &lt;resource type="job-stack-key"&gt;file-search-results&lt;/resource&gt;
 *     &lt;/task&gt;
 *     &lt;task name="two" order="2" active="false"&gt;
 * </pre>
 * </p>
 * <p>
 * @author Adym S. Lincoln<br>
 *         Copyright (C) 2007-2009. JBJF All rights reserved.
 * @version 1.3.2
 * @since   1.3.2, JBJF
 * </p>
 */
public class CopyFilesInDocumentAdapter extends AbstractTask {

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = CopyFilesInDocumentAdapter.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "CopyFilesInDocumentAdapter()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /**
     * Default constructor.  Sets the required &lt;resource&gt;
     * elements.  
     */
    public CopyFilesInDocumentAdapter() {
        if (getRequiredResources() == null) {
            setRequiredResources(new ArrayList());
        }
        getRequiredResources().add("directory-source");
        getRequiredResources().add("directory-target");
        getRequiredResources().add("job-stack-key");
    }

    @Override
    public void runTask(HashMap jobStack) throws Exception {
        if (hasRequiredResources()) {
            CopyFile lsubTask = new CopyFile();
            HashMap lsubResources = new HashMap();
            String dirSource = resolveResource("directory-source");
            getLog().debug(SHORT_NAME + " Source Directory [" + dirSource + "]");
            String dirTarget = resolveResource("directory-target");
            getLog().debug(SHORT_NAME + " Target Directory [" + dirTarget + "]");
            String keyFileNames = resolveResource("job-stack-key");
            getLog().debug(SHORT_NAME + " File Names Key [" + keyFileNames + "]");
            DelimitedDocumentAdapter lfileNames = null;
            lfileNames = (DelimitedDocumentAdapter) getParameters().get(keyFileNames);
            if (lfileNames != null) {
                if (lfileNames.getRecordCount() > 0) {
                    for (int lngIdx = 0; lngIdx < lfileNames.getRecordCount(); lngIdx++) {
                        FlatRow vmRow = (FlatRow) lfileNames.getRecordContainer(lngIdx);
                        String lstrFilename = vmRow.getColumnValue(0);
                        getLog().debug("Copy [" + lstrFilename + "] in directory [" + dirSource + "] to directory [" + dirTarget + "]");
                        lsubResources.put("source", dirSource + File.separator + lstrFilename);
                        lsubResources.put("target", dirTarget + File.separator + lstrFilename);
                        lsubTask.setResources(lsubResources);
                        lsubTask.runTask(getParameters());
                    }
                }
            }
        }
    }

    /**
     * Utility method that takes in a <code>String</code> name
     * of a &lt;directory&gt; element and returns the XML value from
     * that element.
     * <p>
     * @param   keyResource String value of the resource to find.
     * @return              Returns a <code>String</code> value
     *                      of the &lt;directory&gt; element.
     */
    public String resolveResource(String keyResource) throws Exception {
        getLog().debug("resolveResource job stack key [" + keyResource + "]");
        String lstrResults = null;
        if (getResources().containsKey(keyResource)) {
            lstrResults = (String) getResources().get(keyResource);
            if ((lstrResults != null) && (lstrResults.length() > 0)) {
                getLog().debug("resolveDirectory found [" + keyResource + "] as [" + lstrResults + "]");
            } else {
                throw new Exception("Task [" + SHORT_NAME + "] found a resource [" + keyResource + "]," + " but no corresponding <resource> value.  Please" + " double-check the values and links in the" + " JBJF Batch Definition file.");
            }
        } else {
            throw new Exception("Task [" + SHORT_NAME + "] requires a resource" + " of type='" + keyResource + "'...<resource type='" + keyResource + "'>" + " Please make sure this resource is coded in the JBJF" + " Batch Definition file.");
        }
        getLog().debug("resolveResource found [" + lstrResults + "]");
        return lstrResults;
    }
}
