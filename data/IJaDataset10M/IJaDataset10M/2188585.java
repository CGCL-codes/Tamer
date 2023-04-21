package org.deft.importer;

import org.deft.repository.fragment.HierarchyFragment;

public interface IImportManager {

    /**
	 * This methods imports all files returned by an IImporter into a given
	 * Project
	 * 
	 * @param fragment
	 *            The Project or Folder in which we import the files
	 * @param importer
	 *            The IImporter to use for importing
	 */
    public abstract void importInto(final HierarchyFragment fragment);
}
