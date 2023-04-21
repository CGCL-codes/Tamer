package de.erdesignerng.visual.editor.repository;

import de.erdesignerng.model.serializer.repository.RepositoryEntryDescriptor;

/**
 * Datamodel for the save to repository dialog.
 * 
 * @author mirkosertic
 */
public class SaveToRepositoryDataModel {

    private String nameForNewEntry;

    private RepositoryEntryDescriptor existingEntry;

    private String nameForExistingEntry;

    /**
	 * @return the nameForNewEntry
	 */
    public String getNameForNewEntry() {
        return nameForNewEntry;
    }

    /**
	 * @param nameForNewEntry
	 *			the nameForNewEntry to set
	 */
    public void setNameForNewEntry(String nameForNewEntry) {
        this.nameForNewEntry = nameForNewEntry;
    }

    /**
	 * @return the existingEntry
	 */
    public RepositoryEntryDescriptor getExistingEntry() {
        return existingEntry;
    }

    /**
	 * @param existingEntry
	 *			the existingEntry to set
	 */
    public void setExistingEntry(RepositoryEntryDescriptor existingEntry) {
        this.existingEntry = existingEntry;
    }

    /**
	 * @return the nameForExistingEntry
	 */
    public String getNameForExistingEntry() {
        return nameForExistingEntry;
    }

    /**
	 * @param nameForExistingEntry
	 *			the nameForExistingEntry to set
	 */
    public void setNameForExistingEntry(String nameForExistingEntry) {
        this.nameForExistingEntry = nameForExistingEntry;
    }
}
