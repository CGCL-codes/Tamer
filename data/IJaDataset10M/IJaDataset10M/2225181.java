package com.rapidminer.operator.repository;

import java.util.List;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeRepositoryLocation;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.repository.DataEntry;
import com.rapidminer.repository.Entry;
import com.rapidminer.repository.Folder;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryLocation;

/**
 * An Operator to rename repository entries. The user can select the entry to rename, a new name and if an already existing entry should be
 * overwritten or not. If overwriting is not allowed (default case) a user error is thrown if there already exists another element with the new name. 
 * 
 * @author Nils Woehler
 *
 */
public class RepositoryEntryRenameOperator extends AbstractRepositoryManagerOperator {

    public static final String ELEMENT_TO_RENAME = "entry_to_rename";

    public static final String NEW_ELEMENT_NAME = "new_name";

    public static final String OVERWRITE = "overwrite";

    public RepositoryEntryRenameOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        super.doWork();
        RepositoryLocation repoLoc = getParameterAsRepositoryLocation(ELEMENT_TO_RENAME);
        String newName = getParameterAsString(NEW_ELEMENT_NAME);
        boolean overwrite = getParameterAsBoolean(OVERWRITE);
        if (newName.length() == 0) {
            throw new UserError(this, "207", "", NEW_ELEMENT_NAME, "An empty new name is not allowed.");
        }
        Entry entry;
        try {
            entry = repoLoc.locateEntry();
            if (entry == null) {
                throw new UserError(this, "301", repoLoc);
            }
        } catch (RepositoryException e1) {
            throw new UserError(this, e1, "302", repoLoc, e1.getMessage());
        }
        try {
            Folder containingFolder = entry.getContainingFolder();
            if (containingFolder != null && containingFolder.containsEntry(newName)) {
                if (overwrite) {
                    List<DataEntry> dataEntries = containingFolder.getDataEntries();
                    boolean deleted = false;
                    for (DataEntry dataEntry : dataEntries) {
                        if (dataEntry.getName().equals(newName)) {
                            dataEntry.delete();
                            deleted = true;
                            break;
                        }
                    }
                    if (!deleted) {
                        List<Folder> subfolders = containingFolder.getSubfolders();
                        for (Folder subfolder : subfolders) {
                            if (subfolder.getName().equals(newName)) {
                                subfolder.delete();
                                deleted = true;
                                break;
                            }
                        }
                    }
                    if (!deleted) {
                        throw new RepositoryException("Could not delete already existing element " + newName + " at move destination " + containingFolder);
                    }
                } else {
                    throw new RepositoryException("Could not rename entry: Element with name " + newName + " already exists.");
                }
            }
        } catch (RepositoryException e) {
            throw new OperatorException("Renaming the repository entry " + entry + " is not possible: ", e);
        }
        try {
            entry.rename(newName);
        } catch (RepositoryException e) {
            throw new UserError(this, e, "repository_management.rename_repository_entry", repoLoc, newName, e.getMessage());
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeRepositoryLocation(ELEMENT_TO_RENAME, "Entry that should be renamed", true, true, false));
        types.add(new ParameterTypeString(NEW_ELEMENT_NAME, "New entry name", false, false));
        types.add(new ParameterTypeBoolean(OVERWRITE, "Overwrite already existing entry with same name?", false, false));
        return types;
    }
}
