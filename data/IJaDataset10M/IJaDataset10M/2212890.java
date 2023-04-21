package com.io_software.catools.search;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import com.io_software.catools.search.capability.Production;

/** Presents a directory as a searchable entity and tells the search methods
    applicable to it. Searching a directory means searching all files
    contained in it (and <em>not</em> the filenames listed in it). Searching the
    contained files is done by using instances of class {@link
    com.io_software.catools.search.SearchableFile}.

    @version $Id: SearchableDirectory.java,v 1.11 2001/04/01 19:57:34 aul Exp $
    @author Axel Uhl
  */
public class SearchableDirectory extends AbstractSearchable {

    /** An instance of this class is tied to a particular directory. No
        filename filter is set, so all contained files are considered for the
        search.

        @param d the directory which to present as a <tt>Searchable</tt> object
      */
    public SearchableDirectory(File d) {
        directory = d;
        filenameFilter = null;
    }

    /** Ties the searchable object to the given directory and sets the
    	specified filename filter, so that only those files matching the
    	filter will be searched.

    	@param d the directory to search the files in
    	@param filter the filename filter. Only those files in the directory
    		matched by the filter will be searched.
      */
    public SearchableDirectory(File d, FilenameFilter filter) {
        this(d);
        filenameFilter = filter;
    }

    /** A standard directory, without further knowledge about its type or content,
        is currently searchable with those query types that the contained files
        support.<p>
        
        Future extenions might be the support for directory attributes like
        number of files contained in it, number of subdirectories, contained
        filenames, etc.

	@return the search methods applicable to this source, namely
	        keyword search and regular expression search
      */
    public Production getSupportedQueryTypes() {
        return new TextFileSearchable(null).getSupportedQueryTypes();
    }

    /** searches the files in the directory represented by this object
        using the specified query. If a filename filter was defined at
        construction time, only those files matching the specified filter
        will be searched.<p>
        
        This means that the passed <tt>query</tt> is <em>not</em> executed
        immediately against this directory object by instead against all
        the contained files represented by {@link SearchableFile} objects
        instead.

	@param query the query, supposed to be either a
		<tt>FileKeywordQuery</tt> or a <tt>FileRegexQuery</tt>.
	@param requestor the principal / user / entity requesting this
		search
	@return a set containing those {@link TextSearchable} objects
	        matching the query or an empty set if no objects are
	        matched or if the query was not applicable to this source.
      */
    public Set search(Query query, Requestor requestor) throws Exception {
        Set result = new HashSet();
        if (understands(query)) {
            Set files = getSearchableFiles(false);
            for (Iterator e = files.iterator(); e.hasNext(); ) {
                Searchable s = (Searchable) e.next();
                result.addAll(s.search(query, requestor));
            }
        } else query.dispatch(this, requestor);
        return result;
    }

    /** retrieves those files from the directory represented by this object
	and wrap them by {@link TextSearchable} objects. If a filename
	filter was defined in the constructor, use it to filter down
	the list of files contained in the directory.
	
	@param recursive if <tt>true</tt>, directories contained in the
		specified directory will recursively be added to the search.
	@return the set of {@link TextSearchable} objects representing
		the (possibly filtered) set of files in this directory
		and the {@link SearchableDirectory} objects representing
		the subdirectories (only, if <tt>recursive</tt> was
		<tt>true</tt>).
      */
    private Set getSearchableFiles(boolean recursive) {
        Set result = new HashSet();
        String[] files;
        if (filenameFilter != null) files = directory.list(filenameFilter); else files = directory.list();
        if (files != null) for (int i = 0; i < files.length; i++) {
            File f = new File(directory, files[i]);
            if (f.isDirectory()) {
                if (recursive) result.add(new SearchableDirectory(f));
            } else result.add(new TextFileSearchable(f));
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            SearchableDirectory sd = new SearchableDirectory(new File(args[0]));
            RegexQuery q = new RegexQuery(args[1]);
            Set foundFiles = sd.search(q, null);
            System.out.println("The following files contain the search expression " + args[1] + ":");
            System.out.println(foundFiles);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /** the directory for which to search the contained files */
    private File directory;

    /** The filter specifying which files from the directory to search.
    	If <tt>null</tt>, no filter is used and all files contained in the
    	directory will be searched.
      */
    private FilenameFilter filenameFilter;
}
