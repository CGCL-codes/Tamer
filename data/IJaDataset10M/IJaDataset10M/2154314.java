package org.davic.mpeg.sections;

/**
 * This class is used by TableSectionFilter to report that a section has been
 * encountered which has a different version_number from earlier sections. It is
 * generated only once per filtering action. The section with a different
 * version_number is ignored.
 * @version new for DAVIC 1.3.1
 */
public class VersionChangeDetectedEvent extends SectionFilterEvent {

    /**
	 * This constructs a VersionChangeDetectedEvent event for the specified
	 * SectionFilter object.
	 * @param f the SectionFilter object which filtered the data.
	 * @param appData application data that was passed to the startFiltering
	 *            method
	 */
    public VersionChangeDetectedEvent(SectionFilter f, Object appData) {
        super(f, appData);
    }

    /**
	 * This returns the SectionFilter object which filtered the data.
	 */
    public Object getSource() {
        return null;
    }

    /**
	 * This returns the original version number of the table.
	 */
    public int getOriginalVersion() {
        return 0;
    }

    /**
	 * This returns the version number of the new table.
	 */
    public int getNewVersion() {
        return 0;
    }
}
