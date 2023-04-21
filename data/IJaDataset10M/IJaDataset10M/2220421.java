package org.eaasyst.eaa.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.data.OptionListSourceFactoryBase;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.persistent.Document;
import org.eaasyst.eaa.syst.data.transients.SearchFilter;
import org.eaasyst.eaa.syst.data.transients.SearchSpecification;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>This class implements the required methods for the
 * EaasyStreet FolderOptionListSource factory component.</p>
 *
 * @version 2.7
 * @author Jeff Chilton
 */
public class FolderOptionListSourceFactory extends OptionListSourceFactoryBase {

    /**
	 * <p>Constructs a new "FolderOptionListSourceFactory" object.</p>
	 *
	 * @since Eaasy Street 2.7
	 */
    public FolderOptionListSourceFactory() {
        setClassName(StringUtils.computeClassName(getClass()));
    }

    /**
	 * <p>Loads the optionListSource.</p>
	 * 
	 * @param version The version to which this access bean is associated
	 * @since Eaasy Street 2.7
	 */
    protected void loadOptionListSource(String version) {
        List options = new ArrayList();
        options.add("0:(Home)");
        processFolder(0, options, "", "");
        setOptionListSource(version, new DynaOptionListSource(StringUtils.join(options, ",")));
    }

    /**
	 * <p>Processes a single Folder.</p>
	 * 
	 * @param folderId the folder to process
	 * @param options the options list
	 * @param prefix the higher-level menu item labels
	 * @param separator the label separator
	 * @since Eaasy Street 2.7
	 */
    private void processFolder(int folderId, List options, String prefix, String separator) {
        String fullPrefix = prefix + separator;
        List folders = getChildFolders(folderId);
        if (folders != null) {
            Iterator i = folders.iterator();
            while (i.hasNext()) {
                Document thisFolder = (Document) i.next();
                String label = fullPrefix + thisFolder.getTitle();
                String thisEntry = thisFolder.getIdString() + ":" + label;
                options.add(thisEntry);
                processFolder(thisFolder.getId(), options, label, " -> ");
            }
        }
    }

    /**
	 * <p>Retrives all folders with parentFolderId equal to the folder id passed.</p>
	 * 
	 * @param folderId the id of the parent folder
	 * @return the list of child folders
	 * @since Eaasy Street 2.7
	 */
    private List getChildFolders(int folderId) {
        EaasyStreet.logTrace("[In] FolderOptionListSourceFactory.getFolderContents()");
        List folders = null;
        SearchFilter filter1 = new SearchFilter();
        filter1.setFieldName("folderId");
        filter1.setFilterType(SearchFilter.TYPE_EQUALS);
        filter1.setFieldValue(new String[] { folderId + "" });
        filter1.setNumericComparison(true);
        SearchFilter filter2 = new SearchFilter();
        filter2.setFieldName("type");
        filter2.setFilterType(SearchFilter.TYPE_EQUALS);
        filter2.setFieldValue(new String[] { Document.TYPE_FOLDER });
        SearchSpecification spec = new SearchSpecification();
        spec.setFilter(new SearchFilter[] { filter1, filter2 });
        spec.setSortField(new String[] { "title", "lastContentUpdate desc" });
        Map parameters = new HashMap();
        parameters.put("filter", spec);
        DataConnector dc = new DataConnector(new DocumentDabFactory().getAccessBean("list"));
        dc.setParameters(parameters);
        dc.setCommand(DataConnector.READ_COMMAND);
        dc.execute();
        if (dc.getResponseCode() == 0) {
            folders = (List) dc.getExecutionResults();
        } else {
            if (dc.getResponseCode() != 1) {
                EaasyStreet.logError("Exception returned while attempting to retrieve contents of folder " + folderId + ": response code=" + dc.getResponseCode() + ", response=" + dc.getResponseString());
            }
        }
        EaasyStreet.logTrace("[Out] FolderOptionListSourceFactory.getFolderContents()");
        return folders;
    }
}
