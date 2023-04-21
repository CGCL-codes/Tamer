package com.philip.journal.core.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import com.philip.core.BusinessServiceProxy;
import com.philip.core.WarningType;
import com.philip.journal.core.bean.User;
import com.philip.journal.core.exception.JournalException;
import com.philip.journal.home.bean.Branch;
import com.philip.journal.home.bean.Category;
import com.philip.journal.home.bean.Entry;

/**
 * @author cry30
 */
public class ServiceProxy extends BusinessServiceProxy {

    @Override
    protected Object proxyMehodCall(final Class<?>[] paramType, final Object[] args) throws JournalException {
        try {
            return super.proxyMehodCall(paramType, args);
        } catch (final DataIntegrityViolationException div) {
            throw new JournalException(div.getCause().getCause().getMessage(), div);
        } catch (final Exception e) {
            throw new JournalException(e.getMessage(), e);
        }
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Imports Branch and Entry data from xml files. Existing data will be replaced by the contents of the xml
     * file.
     *
     * @param xmlData - xml data to import.
     * @throws JournalException wrapper application exception.
     */
    public void importCodeXML(final InputStream xmlData) throws JournalException {
        proxyMehodCall(new Class[] { InputStream.class }, new Object[] { xmlData });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Converts all entry data in the DB to xml.
     *
     * @param targetStream - stream to write the Xml output to.
     * @throws JournalException - when the export to xml fails for some reason.
     * @see com.philip.journal.core.service.ServiceFacade#exportToXML(Map, java.io.OutputStream)
     */
    public void exportToXML(final OutputStream targetStream) throws JournalException {
        proxyMehodCall(new Class[] { OutputStream.class }, new Object[] { targetStream });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Retrieves the direct sub branches of a given branchId.
     *
     * @param branchId Id of the Branch whose direct children are to be retrieved.
     * @return List&lt;Branch&gt; containing the direct children of the given branchId.
     *
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see {@link com.philip.journal.core.service.ServiceFacade#getChildren(Map, long)}
     */
    @SuppressWarnings("unchecked")
    public List<Branch> getChildren(final long branchId) throws JournalException {
        return (List<Branch>) proxyMehodCall(new Class[] { Long.class }, new Object[] { branchId });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Adds a new branch to the given branchId.
     *
     * @param parentId - id of the branch where the newly created branch will be attached to.
     * @param branchName - the name of the branch to be created.
     * @return the id of the created branch
     *
     * @throws JournalException thrown when operation has failed to add the new branch due to the following
     *             reasons: <li>branchName already exist for the given parent ID.</li> <li>supplied parentId
     *             does not exist</li>
     * @see {@link com.philip.journal.core.service.ServiceFacade#addBranch(Map, long, java.lang.String)}
     */
    public long addBranch(final long parentId, final String branchName) throws JournalException {
        return (Long) proxyMehodCall(new Class[] { Long.class, String.class }, new Object[] { Long.valueOf(parentId), branchName });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Removes the branch with the given branchId along with all the sub branches.
     *
     * @param branchId branchId of the Branch to delete.
     * @throws JournalException can be thrown due to delete concurrency issues. Currently not possible to
     *             happen.
     * @see {@link com.philip.journal.core.service.ServiceFacade#deleteBranch(Map, long)}
     */
    public void deleteBranch(final long branchId) throws JournalException {
        proxyMehodCall(new Class[] { Long.class }, new Object[] { Long.valueOf(branchId) });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Changes the name of the given branch.
     *
     * @param parentId Branch ID.
     * @param newBranchName new Branch name.
     * @throws JournalException when rename fails for some reason like when the new name is already existing.
     * @see {@link com.philip.journal.core.service.ServiceFacade#renameBranch(Map, long, java.lang.String)}
     */
    public void renameBranch(final long parentId, final String newBranchName) throws JournalException {
        proxyMehodCall(new Class[] { Long.class, String.class }, new Object[] { parentId, newBranchName });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Moves a folder to another folder.
     *
     * @param newParentId - id of the new parent id to put the branch into.
     * @param branchId - id of the branch to move
     * @throws JournalException - thrown when the move fails due to constraint.
     * @see {@link com.philip.journal.core.service.ServiceFacade#moveBranch(Map, long, long)}
     */
    public void moveBranch(final long newParentId, final long branchId) throws JournalException {
        proxyMehodCall(new Class[] { Long.class, Long.class }, new Object[] { newParentId, branchId });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Returns the Map of preferences. Root ConfigItem is mapped to null, while child configItem will be
     * mapped to parent ConfigItem matching the ID.
     *
     * @return List of Config Items in List of Map format.
     *
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#getPreferences(java.util.Map)
     */
    @SuppressWarnings(WarningType.UNCHECKED)
    public List<Map<String, Object>> getPreferences() throws JournalException {
        return (List<Map<String, Object>>) proxyMehodCall(new Class[0], new Object[0]);
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Retrieves node properties.
     *
     * @param nodeId - id of entry or branch.
     * @param isEntry - specifies if the node is a Branch or is an Entry.
     * @return Node properties in a format similar to windows file properties dialog.
     *
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#getNodeProperties(long, boolean)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getNodeProperties(final long nodeId, final boolean isEntry) throws JournalException {
        return (Map<String, Object>) proxyMehodCall(new Class[] { Long.class, Boolean.class }, new Object[] { nodeId, isEntry });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Searches entries that contain the specified string. The search is not case sensitive. Search string
     * must be at least 3 characters so the data source is not over burdened.
     *
     * @param searchParam - the matching string to look for.
     * @return All entries containing the specified string
     * @throws JournalException - when the search parameter is less than 3 characters.
     * @exception IllegalArgumentException when the searchParam is null or is less than 3 characters.
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#searchEntriesSimple(java.util.Map, java.lang.String)
     */
    @SuppressWarnings(WarningType.UNCHECKED)
    public List<Entry> searchEntriesSimple(final String searchParam) throws JournalException {
        return (List<Entry>) proxyMehodCall(new Class[] { String.class }, new Object[] { searchParam });
    }

    /**
     * (COPIED from interface.) <br/>
     *
     * Search with sorting.
     *
     * @param searchParam - the matching string to look for.
     * @param sortField Entry field to sort with.
     * @param direction either ASC or DESC.
     * @return Sorted entries containing the specified string.
     * @throws JournalException - when the search parameter is less than 3 characters.
     * @exception IllegalArgumentException when the searchParam is null or is less than 3 characters.
     */
    @SuppressWarnings(WarningType.UNCHECKED)
    public List<Entry> searchEntriesSimple(final String searchParam, final String sortField, final String direction) throws JournalException {
        return (List<Entry>) proxyMehodCall(new Class[] { String.class, String.class, String.class }, new Object[] { searchParam, sortField, direction });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Moves an entry to another folder.
     *
     * @param newParentId - id of the new parent id to put the branch into.
     * @param entryId - id of the entry to move
     * @throws JournalException - thrown when the move fails due to constraint.
     * @exception IllegalArgumentException when the newParentId or EntryId did not match objects in the data
     *                source.
     * @see com.philip.journal.core.service.ServiceFacade#moveEntry(java.util.Map, long, long)
     */
    public void moveEntry(final long newParentId, final long entryId) throws JournalException {
        proxyMehodCall(new Class[] { Long.class, Long.class }, new Object[] { newParentId, entryId });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Renames the title of the entry.
     *
     * @param entryId - id of the entry to move
     * @param newTitle - the new title to set
     * @throws JournalException - thrown when the rename fails due to constraint.
     * @exception IllegalArgumentException If any of the following is true:
     *                <ul>
     *                <li>when the EntryId did not match objects in the data source.
     *                <li>when the new title is null or empty String
     *                </ul>
     * @see com.philip.journal.core.service.ServiceFacade#renameEntry(java.util.Map, long, java.lang.String)
     */
    public void renameEntry(final long entryId, final String newTitle) throws JournalException {
        proxyMehodCall(new Class[] { Long.class, String.class }, new Object[] { entryId, newTitle });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Returns List of available categories.
     *
     * @deprecated Design change, no longer used.
     * @return List&lt;Categories&gt; the list of branch formatted for display. <br>
     *         <b>example:</b> <br>
     *         Category(0, 'Root' > 'parent name' > 'parent name' > 'branch name')
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#getCategories(java.util.Map, long)
     */
    @Deprecated
    @SuppressWarnings(WarningType.UNCHECKED)
    public List<Category> getCategories() throws JournalException {
        return (List<Category>) proxyMehodCall(new Class[] {}, new Object[] {});
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Returns List of Entry under the given Branch ID.
     *
     * @param branchId - branch id from where to retrieve a list of entries.
     * @return List of entries under the specified branch id.
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#getEntries(java.util.Map, long)
     */
    @SuppressWarnings(WarningType.UNCHECKED)
    public List<Entry> getEntries(final long branchId) throws JournalException {
        return (List<Entry>) proxyMehodCall(new Class[] { Long.class }, new Object[] { branchId });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Simply reads an Entry entity from the DAO service.
     *
     * @param entryId entry Id of Detail to fetch.
     * @return Entry entity.
     * @throws JournalException - SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#getEntryDetail(java.util.Map, long)
     */
    public Entry getEntryDetail(final long entryId) throws JournalException {
        return (Entry) proxyMehodCall(new Class[] { Long.class }, new Object[] { entryId });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Saves an Entry either to insert new record or update existing Entry.
     *
     * @param branchId - the branch id to where this entry will belong.
     * @param entryId - the entry id.
     * @param title - the title of this entry
     * @param description - the contents of this entry.
     * @return entry id of the object.
     * @throws JournalException is thrown when the save fails for some reason. Save will fail when same title
     *             exists for the same branch.
     * @exception IllegalArgumentException If any of the following is true:
     *                <ul>
     *                <li>when branchId is not found from the data source.
     *                <li>when title or description parameter is null.
     *                </ul>
     * @see com.philip.journal.core.service.ServiceFacade#saveEntry(java.util.Map, long, long, String, String)
     */
    public long saveEntry(final long branchId, final long entryId, final String title, final String description) throws JournalException {
        return (Long) proxyMehodCall(new Class[] { Long.class, Long.class, String.class, String.class }, new Object[] { branchId, entryId, title, description });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Deletes the specified entry.
     *
     * @param entryId Entry Id to delete.
     * @throws JournalException wrapped application exception.
     * @throws JournalException SYSTEM CRITICAL EXCEPTION ONLY.
     * @see com.philip.journal.core.service.ServiceFacade#deleteEntry(java.util.Map, long)
     */
    public void deleteEntry(final long entryId) throws JournalException {
        proxyMehodCall(new Class[] { Long.class }, new Object[] { entryId });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * Changes the password. Currently no control is made to ensure about the security of the new password.
     *
     * @param oldPassword - used to initially validate the user.
     * @param newPassword - new password to set
     * @throws JournalException IncorrectPasswordException - when the supplied old password does not match the
     *             current password
     * @see com.philip.journal.core.bean.User
     * @see com.philip.journal.core.service.ServiceFacade#changePassword(java.util.Map, java.lang.String,
     *      java.lang.String)
     */
    public void changePassword(final String oldPassword, final String newPassword) throws JournalException {
        proxyMehodCall(new Class[] { String.class, String.class }, new Object[] { oldPassword, newPassword });
    }

    /**
     * (EXTRACTED from interface). <br/>
     *
     * Authenticates the user against the system.
     *
     * @param username case sensitive username to authenticate.
     * @param password case sensitive password.
     * @return the authenticated User object. been supplied.
     *
     * @throws JournalException <ul>
     *             <li>UserNotFoundException when the username does not exist in the data source.
     *             <li/>
     *             <li>IncorrectPasswordException when the supplied username was found but an invalid password
     *             has</li>
     *             </ul>
     *
     * @see com.philip.journal.core.bean.User
     * @see com.philip.journal.core.service.ServiceFacade#authenticateUser(java.util.Map, java.lang.String,
     *      java.lang.String)
     */
    public User authenticateUser(final String username, final String password) throws JournalException {
        return (User) proxyMehodCall(new Class[] { String.class, String.class }, new Object[] { username, password });
    }

    /**
     * (COPIED from interface). <br/>
     *
     * RTFC.
     *
     * @return the current User object.
     * @see com.philip.journal.core.bean.User
     * @see com.philip.journal.core.service.ServiceFacade#getCurrentUser(java.util.Map)
     *
     * @throws JournalException SYSTEM CRITICAL EXCEPTION ONLY.
     */
    public User getCurrentUser() throws JournalException {
        return (User) proxyMehodCall(new Class[0], new Object[0]);
    }
}
