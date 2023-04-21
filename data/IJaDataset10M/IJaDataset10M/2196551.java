package gruntspud.filter;

import gruntspud.CVSSubstType;
import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.file.DirectoryStatus;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.netbeans.lib.cvsclient.file.FileStatus;

/**
 *  Description of the Class
 *
 *@author     magicthize
 *@created    26 May 2002
 */
public class CVSFileFilterModel extends AbstractListModel implements ComboBoxModel, Cloneable {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.filter.ResourceBundle");

    private Vector filters;

    private Object sel;

    private GruntspudContext context;

    private CVSFileFilterModel() {
    }

    /**
     * Creates a new CVSFileFilterModel object.
     *
     * @param context DOCUMENT ME!
     */
    public CVSFileFilterModel(GruntspudContext context) {
        this.context = context;
        filters = new Vector();
        CVSFileDirectoryFilter allDirectoriesFilter = new CVSFileDirectoryFilter();
        CVSFileDirectoryFilter needsAttentionFilter = new CVSFileDirectoryFilter();
        needsAttentionFilter.addStatusToIgnore(DirectoryStatus.PLAIN);
        needsAttentionFilter.addStatusToIgnore(DirectoryStatus.UP_TO_DATE);
        CVSFileSubstTypeFilter substFilter = new CVSFileSubstTypeFilter();
        substFilter.addSubstTypeToIgnore(CVSSubstType.CVS_SUBST_TYPE_IGNORED);
        CVSFileStatusFilter allStatusFilter = new CVSFileStatusFilter();
        CVSFileFilter allFilter = new CVSFileFilter(res.getString("allFilterName"), allStatusFilter, substFilter, allDirectoriesFilter);
        allFilter.setPreset(true);
        addFilter(allFilter);
        CVSFileStatusFilter commitableStatusFilter = new CVSFileStatusFilter();
        commitableStatusFilter.addStatusToIgnore(FileStatus.UNKNOWN);
        commitableStatusFilter.addStatusToIgnore(FileStatus.NEEDS_CHECKOUT);
        commitableStatusFilter.addStatusToIgnore(FileStatus.NEEDS_MERGE);
        commitableStatusFilter.addStatusToIgnore(FileStatus.NEEDS_PATCH);
        commitableStatusFilter.addStatusToIgnore(FileStatus.UP_TO_DATE);
        CVSFileFilter commitableFilter = new CVSFileFilter(res.getString("commitableFilterName"), commitableStatusFilter, substFilter, needsAttentionFilter);
        commitableFilter.setPreset(true);
        addFilter(commitableFilter);
        CVSFileStatusFilter unknownStatusFilter = new CVSFileStatusFilter();
        unknownStatusFilter.addStatusToIgnore(FileStatus.ADDED);
        unknownStatusFilter.addStatusToIgnore(FileStatus.HAS_CONFLICTS);
        unknownStatusFilter.addStatusToIgnore(FileStatus.MODIFIED);
        unknownStatusFilter.addStatusToIgnore(FileStatus.NEEDS_CHECKOUT);
        unknownStatusFilter.addStatusToIgnore(FileStatus.NEEDS_MERGE);
        unknownStatusFilter.addStatusToIgnore(FileStatus.NEEDS_PATCH);
        unknownStatusFilter.addStatusToIgnore(FileStatus.REMOVED);
        unknownStatusFilter.addStatusToIgnore(FileStatus.UP_TO_DATE);
        CVSFileFilter unknownFilter = new CVSFileFilter(res.getString("unknownFilterName"), unknownStatusFilter, substFilter, needsAttentionFilter);
        unknownFilter.setPreset(true);
        addFilter(unknownFilter);
        CVSFileStatusFilter inCVSStatusFilter = new CVSFileStatusFilter();
        inCVSStatusFilter.addStatusToIgnore(FileStatus.UNKNOWN);
        CVSFileFilter inCVSFilter = new CVSFileFilter(res.getString("inCVSFilterName"), inCVSStatusFilter, substFilter, allDirectoriesFilter);
        inCVSFilter.setPreset(true);
        addFilter(inCVSFilter);
        CVSFileStatusFilter conflictStatusFilter = new CVSFileStatusFilter();
        conflictStatusFilter.addStatusToIgnore(FileStatus.ADDED);
        conflictStatusFilter.addStatusToIgnore(FileStatus.MODIFIED);
        conflictStatusFilter.addStatusToIgnore(FileStatus.NEEDS_CHECKOUT);
        conflictStatusFilter.addStatusToIgnore(FileStatus.NEEDS_MERGE);
        conflictStatusFilter.addStatusToIgnore(FileStatus.NEEDS_PATCH);
        conflictStatusFilter.addStatusToIgnore(FileStatus.REMOVED);
        conflictStatusFilter.addStatusToIgnore(FileStatus.UP_TO_DATE);
        conflictStatusFilter.addStatusToIgnore(FileStatus.UNKNOWN);
        CVSFileFilter conflictFilter = new CVSFileFilter(res.getString("conflictsFileName"), conflictStatusFilter, substFilter, needsAttentionFilter);
        conflictFilter.setPreset(true);
        addFilter(conflictFilter);
        CVSFileStatusFilter addedAndRemovedStatusFilter = new CVSFileStatusFilter();
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.MODIFIED);
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.HAS_CONFLICTS);
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.NEEDS_CHECKOUT);
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.NEEDS_MERGE);
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.NEEDS_PATCH);
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.UP_TO_DATE);
        addedAndRemovedStatusFilter.addStatusToIgnore(FileStatus.UNKNOWN);
        CVSFileFilter addedAndRemovedFilter = new CVSFileFilter(res.getString("addedAndRemovedFilterName"), addedAndRemovedStatusFilter, substFilter, needsAttentionFilter);
        addedAndRemovedFilter.setPreset(true);
        addFilter(addedAndRemovedFilter);
        CVSFileStatusFilter remoteChanges = new CVSFileStatusFilter();
        remoteChanges.addStatusToIgnore(FileStatus.ADDED);
        remoteChanges.addStatusToIgnore(FileStatus.HAS_CONFLICTS);
        remoteChanges.addStatusToIgnore(FileStatus.MODIFIED);
        remoteChanges.addStatusToIgnore(FileStatus.REMOVED);
        remoteChanges.addStatusToIgnore(FileStatus.UNKNOWN);
        remoteChanges.addStatusToIgnore(FileStatus.UP_TO_DATE);
        CVSFileFilter remoteChangesFilter = new CVSFileFilter("Remote changes", remoteChanges, substFilter, needsAttentionFilter);
        remoteChangesFilter.setPreset(true);
        addFilter(remoteChangesFilter);
        StringTokenizer s = new StringTokenizer(context.getHost().getProperty(Constants.FILTER, res.getString("allFilterName")), "&");
        try {
            String sel = s.nextToken();
            Constants.UI_LOG.debug("Selected global filter is " + sel);
            while (s.hasMoreTokens()) {
                String f = s.nextToken();
                CVSFileFilter filter = new CVSFileFilter();
                filter.setFromPropertyString(f);
                addFilter(filter);
            }
            CVSFileFilter f = getFilter(sel);
            this.sel = f == null ? getFilterAt(0) : f;
        } catch (Exception e) {
        }
    }

    public Object clone() throws CloneNotSupportedException {
        CVSFileFilterModel model = new CVSFileFilterModel();
        model.filters = this.filters;
        model.context = this.context;
        return model;
    }

    /**
     * DOCUMENT ME!
     */
    public void cleanUp() {
        StringBuffer buf = new StringBuffer();
        String sel = (getSelectedFilter() == null) ? "" : getSelectedFilter().getName();
        Constants.UI_LOG.debug("Saving " + sel + " as currently selected global");
        buf.append(sel);
        for (int i = 0; i < getSize(); i++) {
            CVSFileFilter f = getFilterAt(i);
            if (!f.isPreset()) {
                buf.append("&");
                buf.append(f.getPropertyString());
            }
        }
        context.getHost().setProperty(Constants.FILTER, buf.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void removeFilter(CVSFileFilter filter) {
        int i = filters.indexOf(filter);
        if (i != -1) {
            filters.removeElementAt(i);
            fireIntervalRemoved(this, i, i);
            if (filter == getSelectedItem()) {
                setSelectedItem((getSize() > 0) ? getFilterAt(0) : null);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CVSFileFilter getFilter(String name) {
        for (int i = 0; i < getSize(); i++) {
            CVSFileFilter f = getFilterAt(i);
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CVSFileFilter getFilterAt(int i) {
        return (CVSFileFilter) filters.elementAt(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CVSFileFilter getSelectedFilter() {
        return (CVSFileFilter) getSelectedItem();
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void addFilter(CVSFileFilter filter) {
        int i = filters.size();
        filters.addElement(filter);
        fireIntervalAdded(this, i, i);
        if (sel == null) {
            sel = filter;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        return filters.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getElementAt(int i) {
        return filters.elementAt(i);
    }

    /**
	 * @param filter
	 * @param b
	 */
    public void setSelectedItem(Object sel) {
        Object oldSel = this.sel;
        this.sel = sel;
        if (oldSel != sel) {
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSelectedItem() {
        return sel;
    }
}
