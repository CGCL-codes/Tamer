package org.jbjf.tasks;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jbjf.core.AbstractTask;
import org.jbjf.core.file.DelimitedDocument;
import org.jbjf.core.file.DelimitedDocumentAdapter;
import org.jbjf.core.file.FlatRow;
import org.jbjf.util.JBJFFileFilter;
import org.jbjf.xml.JBJFSQLParameter;

/**
 * <p>
 * The <code>SearchForFilePatterns</code> Task is designed to take
 * a previous read list of filename patterns and search for those
 * on a source directory.  The list of filename patterns should
 * be placed on the JBJF Job Stack as a text document adapter 
 * object, <code>org.jbjf.core.file.DelimitedDocumentAdapter</code>.  The 
 * <code>SearchForFilePatterns</code> task then iterates thru that
 * list and looks on the source directory (&lt;resource type="search-source"&gt;)
 * for any file(s) that match any of the filename patterns in the list.  The 
 * search results are then collected onto a second text document
 * adapter and placed onto the JBJF Job Stack, &lt;resource type="search-results"&gt;.
 * </p>
 * <p>
 * The Task expects the following &lt;resource&gt; elements in the
 * &lt;task&gt; element to be defined:
 * <ul>
 * <li>search-source - A directory path to the source directory that
 * will be searched.
 * <li>search-results - A Job Stack key that will be used to store 
 * the outcome of the search...The object stored with this key is
 * an <code>org.jbjf.core.file.DelimitedDocumentAdapter.</code>
 * <li>search-patterns - A Job Stack key that is the list of filename
 * patterns to search for...the object is a 
 * <code>org.jbjf.core.file.DelimitedDocumentAdapter.</code>
 * </ul>
 * </p>
 * <p>
 * USAGE:
 * <hr>
 * The intended usage of this task is automated searches of large 
 * drives and directories.  The task will ONLY place the results
 * on the job stack if there were any matches.  If all file name 
 * patterns are searched and NO matches are found, then nothing
 * will be placed on the job stack.  This means a sub-sequent task
 * that will consume these results should check the job stack for
 * a containsKey() prior to attempting a fetch of the results.
 * </p>
 * <p>
 * An example &lt;task&gt; definition:
 * <pre>
 * &lt;task name="t003" order="3" active="true"&gt;
 *     &lt;class&gt;org.jbjf.tasks.SearchForFilePatterns&lt;/class&gt;
 *     &lt;resource type="search-source"&gt;/usr/apps/myapp&lt;/resource&gt;
 *     &lt;resource type="search-results"&gt;file-search-results&lt;/resource&gt;
 *     &lt;resource type="search-patterns"&gt;file-search-patterns&lt;/resource&gt;
 * &lt;/task&gt;
 * </pre>
 * </p>
 * <p>
 * <h3>History</h3> 
 * <hr>
 * 10/09/2010 - ASL - Created the initial class.
 * </p>
 * <p>
 * @author Adym S. Lincoln<br>
 *         Copyright (C) 2007-2009. JBJF All rights reserved.
 * @version 1.3.2
 * @since   1.3.2
 * </p>
 */
public class SearchForFilePatterns extends AbstractTask {

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = SearchForFilePatterns.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "SearchForFilePatterns()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /**
     * Default constructor.
     */
    public SearchForFilePatterns() {
        if (getRequiredResources() == null) {
            setRequiredResources(new ArrayList());
        }
        getRequiredResources().add("search-source");
        getRequiredResources().add("search-results");
        getRequiredResources().add("search-patterns");
    }

    @Override
    public void runTask(HashMap jobStack) throws Exception {
        getLog().debug(this.SHORT_NAME + "...Starting...");
        String dirSource = resolveResource("search-source");
        getLog().debug(SHORT_NAME + " Source Directory [" + dirSource + "]");
        String keySearchResults = resolveResource("search-results");
        getLog().debug(SHORT_NAME + " Search Results Key [" + keySearchResults + "]");
        String keyFilePatterns = resolveResource("search-patterns");
        getLog().debug(SHORT_NAME + " Search Patterns Key [" + keyFilePatterns + "]");
        DelimitedDocumentAdapter lfilePatterns = null;
        lfilePatterns = (DelimitedDocumentAdapter) getParameters().get(keyFilePatterns);
        DelimitedDocumentAdapter ldocSearchResults = null;
        DelimitedDocument ldlmSearchResults = null;
        if (lfilePatterns != null) {
            if (lfilePatterns.getRecordCount() > 0) {
                for (int lngIdx = 0; lngIdx < lfilePatterns.getRecordCount(); lngIdx++) {
                    FlatRow vmRow = (FlatRow) lfilePatterns.getRecordContainer(lngIdx);
                    String[] lstrFilesFound = null;
                    getLog().debug("Filename pattern Search [" + vmRow.getColumnValue(0) + "] in directory [" + dirSource + "]");
                    lstrFilesFound = searchForFiles(dirSource, vmRow.getColumnValue(0));
                    if (lstrFilesFound != null) {
                        if (ldocSearchResults == null) {
                            ldlmSearchResults = new DelimitedDocument(1, '\t');
                            ldocSearchResults = new DelimitedDocumentAdapter(ldlmSearchResults);
                        }
                        for (int i = 0; i < lstrFilesFound.length; i++) {
                            getLog().debug("Found match [" + dirSource + File.separator + lstrFilesFound[i] + "]");
                            FlatRow lfileRow = ldlmSearchResults.createRow();
                            lfileRow.setColumnValue(0, lstrFilesFound[i]);
                            ldocSearchResults.addRecord(lfileRow);
                        }
                    }
                }
            }
        }
        if (ldocSearchResults != null) {
            getParameters().put(keySearchResults, ldocSearchResults);
        }
        getLog().debug(this.SHORT_NAME + "...Complete...");
    }

    /**
     * Utility method that takes in a HashMap collection of &lt;resource&gt;
     * tags and a partial key filter and returns a HashMap collection
     * of filtered entries.
     * <p>
     * @param   pstrDirectory   String value of the directory/folder
     *                          to search.
     * @param   pstrPattern     Filename pattern.
     * @return                  Returns a <code>String[]</code> of files.
     */
    public String[] searchForFiles(String pstrDirectory, String pstrPattern) {
        getLog().debug("File Search Directory [" + pstrDirectory + "] for Pattern [" + pstrPattern + "]");
        File lfileDirectory = new File(pstrDirectory);
        String[] lstrResults;
        FilenameFilter filter = new JBJFFileFilter(pstrPattern);
        lstrResults = lfileDirectory.list(filter);
        if (lstrResults != null) {
            getLog().info("File Search Complete [" + lstrResults.length + "] files found");
        } else {
            getLog().info("File Search Complete [0] files found...Hope that's OK...");
        }
        return lstrResults;
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
