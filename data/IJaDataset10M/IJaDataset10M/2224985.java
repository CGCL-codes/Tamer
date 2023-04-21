package fr.soleil.bensikin.datasources.tango;

import java.util.ArrayList;
import java.util.Vector;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;

/**
 * Defines operations on Tango attributes. Currently used only for searching
 * those attributes.
 * 
 * @author CLAISSE
 */
public interface ITangoManager {

    /**
	 * Loads Tango attributes into a Vector containing Domain objects.
	 * 
	 * @param searchCriterions
	 *            The search criterions
	 * @return A Vector containing Domain objects
	 */
    public Vector loadDomains(Criterions searchCriterions);

    /**
	 * Loads the Tango attributes of a given device.
	 * 
	 * @param device_name
	 *            The complete name of the device to load attributes for
	 * @return An ArrayList containing the names of the device's attributes
	 */
    public ArrayList dbGetAttributeList(String device_name);
}
