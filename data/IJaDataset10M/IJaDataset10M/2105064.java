package sywico.core.change;

import java.io.File;
import org.apache.log4j.Logger;
import sywico.core.change.model.Change;
import sywico.core.change.model.ChangeList;
import sywico.core.change.model.Conflict;
import sywico.core.change.model.CreateFileChange;
import sywico.core.change.model.MergeBean;
import sywico.core.change.model.MkDirChange;
import sywico.core.change.model.RmDirChange;

/**
 * This class is in charge of detecting the conflicts between two file trees
 * 
 * 
 * An instance of this class is used to perform one calculation
 * 1) instanciate 2) go() 3) collect results
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class ConflictDetector {

    public static Logger logger = Logger.getLogger(ConflictDetector.class);

    MergeBean mergeBean;

    protected ChangeList originalCompanyChanges;

    protected ChangeList originalDeveloperChanges;

    protected int idxDeveloper;

    protected int idxCompany;

    /**
     * 
     * 
     * @param companyChanges
     * @param developerChanges
     */
    public ConflictDetector(ChangeList companyChanges, ChangeList developerChanges) {
        this.originalCompanyChanges = companyChanges;
        this.originalDeveloperChanges = developerChanges;
        mergeBean = new MergeBean();
        idxDeveloper = 0;
        idxCompany = 0;
    }

    /**
     * pop the current company change and save it as it is
     *
     */
    protected void transferCompanyChange() {
        Change change = getCompanyChange();
        if (logger.isDebugEnabled()) logger.debug("transfer company change " + change);
        mergeBean.getChangesFromCompany().getChanges().add(change);
        idxCompany++;
    }

    /**
     * pop the current developer change and save it as it is
     *
     */
    protected void transferDeveloperChange() {
        Change change = getDeveloperChange();
        if (logger.isDebugEnabled()) logger.debug("transfer developer change " + change);
        mergeBean.getChangesFromDeveloper().getChanges().add(change);
        idxDeveloper++;
    }

    /**
     * @return true if changes to be processed can still be found in the developer list
     */
    protected boolean stillChangesInDeveloper() {
        return idxDeveloper < originalDeveloperChanges.getChanges().size();
    }

    /**
     * @return true if changes to be processed can still be found in the company list
     */
    protected boolean stillChangesInCompany() {
        return idxCompany < originalCompanyChanges.getChanges().size();
    }

    /**
     * @return the current developer change, or null
     */
    protected Change getDeveloperChange() {
        if (stillChangesInDeveloper()) return originalDeveloperChanges.getChanges().get(idxDeveloper);
        return null;
    }

    /**
     * @return the current company change, or null
     */
    protected Change getCompanyChange() {
        if (stillChangesInCompany()) return originalCompanyChanges.getChanges().get(idxCompany);
        return null;
    }

    /**
     * 
     * sub function used to extract a conflict between one company and many developer changes.
     * 
     * A conflict will be extracted only if there are some changes in the developer side at or after the current position
     * (idxDeveloper), that uses the specified root
     * 
     * 
     * 
     *   
     * @param usingRoot
     * @return true if a conflict was extracted
     */
    protected boolean transferMultipleChangesInDeveloperAgainstOneInCompany(String usingRoot) {
        Change companyChange = originalCompanyChanges.getChanges().get(idxCompany);
        int newIdxDeveloper = whileMatch(usingRoot, originalDeveloperChanges, idxDeveloper);
        if (newIdxDeveloper > idxDeveloper) {
            Conflict conflict = new Conflict(companyChange.getFileName(), new ChangeList(companyChange), new ChangeList(originalDeveloperChanges.getChanges().subList(idxDeveloper, newIdxDeveloper)));
            mergeBean.getAllConflicts().add(conflict);
            idxCompany++;
            idxDeveloper = newIdxDeveloper;
            if (logger.isDebugEnabled()) logger.debug("found conflict 1C-mD:" + conflict);
            return true;
        } else return false;
    }

    /**
     * 
     * sub function used to extract a conflict between one developer and many company changes. 
     * see its sister function transferMultipleChangesInDeveloperAgainstOneInCompany() for more comments
     *  
     * 
     * @param usingRoot
     * @return true if a conflict was extracted
     */
    protected boolean transferMultipleChangesInCompanyAgainstOneInDeveloper(String usingRoot) {
        Change developerChange = originalDeveloperChanges.getChanges().get(idxDeveloper);
        int newIdxCompany = whileMatch(usingRoot, originalCompanyChanges, idxCompany);
        if (newIdxCompany > idxCompany) {
            Conflict conflict = new Conflict(developerChange.getFileName(), new ChangeList(originalCompanyChanges.getChanges().subList(idxCompany, newIdxCompany)), new ChangeList(developerChange));
            mergeBean.getAllConflicts().add(conflict);
            idxCompany = newIdxCompany;
            idxDeveloper++;
            if (logger.isDebugEnabled()) logger.debug("found conflict mC-1D:" + conflict);
            return true;
        } else return false;
    }

    /**
      * 
      * extract from the companyChanges and developerChanges the conflicts
      * 
      */
    public ConflictDetector go() {
        if (logger.isInfoEnabled()) logger.info("detecting conflicts...");
        while (stillChangesInDeveloper() || stillChangesInCompany()) {
            if (logger.isDebugEnabled()) logger.debug(" C[" + idxCompany + "]=" + getCompanyChange() + " D[" + idxDeveloper + "]=" + getDeveloperChange());
            if (!stillChangesInCompany()) transferDeveloperChange(); else if (!stillChangesInDeveloper()) transferCompanyChange(); else {
                Change developerChange = getDeveloperChange();
                Change companyChange = getCompanyChange();
                boolean dealt = false;
                if (developerChange instanceof RmDirChange) {
                    if (transferMultipleChangesInCompanyAgainstOneInDeveloper(developerChange.getFileName() + File.separator)) dealt = true;
                }
                if (!dealt) if (developerChange instanceof MkDirChange && companyChange instanceof CreateFileChange) {
                    if (developerChange.getFileName().equals(companyChange.getFileName())) if (transferMultipleChangesInDeveloperAgainstOneInCompany(developerChange.getFileName())) dealt = true;
                }
                if (!dealt) if (companyChange instanceof RmDirChange) {
                    if (transferMultipleChangesInDeveloperAgainstOneInCompany(companyChange.getFileName() + File.separator)) dealt = true;
                }
                if (!dealt) if (companyChange instanceof MkDirChange && developerChange instanceof CreateFileChange) {
                    if (companyChange.getFileName().equals(developerChange.getFileName())) if (transferMultipleChangesInCompanyAgainstOneInDeveloper(companyChange.getFileName())) dealt = true;
                }
                if (!dealt) {
                    int cmp = developerChange.getFileName().compareTo(companyChange.getFileName());
                    if (cmp > 0) transferCompanyChange(); else if (cmp < 0) transferDeveloperChange(); else {
                        if (developerChange.equals(companyChange)) {
                            if (logger.isDebugEnabled()) logger.debug("developer and company change are same: " + developerChange);
                            mergeBean.getChangesFromBoth().getChanges().add(developerChange);
                        } else {
                            Conflict conflict = new Conflict(companyChange.getFileName(), new ChangeList(companyChange), new ChangeList(developerChange));
                            mergeBean.getAllConflicts().add(conflict);
                            if (logger.isDebugEnabled()) logger.debug("found conflict 1C-1D:" + conflict);
                        }
                        idxDeveloper++;
                        idxCompany++;
                    }
                }
            }
        }
        if (logger.isInfoEnabled()) logger.info("go() return. merge bean:" + getMergeBean());
        return this;
    }

    /**
      * Helper function.
      * 
      * returns the idx of the first element after startIdx (included) that does not start with root,
      * or the size of the list if all the elements after startIdx start with root,
      * 
      * 
      * @param root
      * @param changeList
      * @param start
      * @return the idx of the first element after startIdx that does not start with root.
      */
    public static int whileMatch(String root, ChangeList changeList, int startIdx) {
        int retVal = startIdx;
        while (retVal < changeList.getChanges().size() && changeList.getChanges().get(retVal).getFileName().startsWith(root)) retVal++;
        return retVal;
    }

    public MergeBean getMergeBean() {
        return mergeBean;
    }
}
