package votebox.middle.driver;

import votebox.middle.Properties;

/**
 * This interface defines a general adapter that the
 * 
 * @author Kyle
 * 
 */
public interface IAdapter {

    /**
	 * Call this method to ask that a specific element be selected.
	 * 
	 * @param uid
	 *            Ask to select a card element that has this UID.
	 * @return This method returns true if the selection was a success (if it
	 *         was allowed), and false otherwise.
	 * @throws UnknownUIDException
	 *             This method throws when the UID that is given as a parameter
	 *             is not defined.
	 */
    public boolean select(String uid) throws UnknownUIDException, SelectionException;

    /**
	 * Call this method to ask that a specific element be deselected.
	 * 
	 * @return This method returns true if the deselection was a success (if it
	 *         was allowed), and false otherwise.
	 * @param uid
	 *            Ask to deselect a card element that has this UID.
	 * @throws UnknownUIDException
	 *             This method throws when the UID that is given as a parameter
	 *             is not defined.
	 */
    public boolean deselect(String uid) throws UnknownUIDException, DeselectionException;

    /**
	 * Call this method to get properties.
	 * 
	 * @return This method returns properties.
	 */
    public Properties getProperties();
}
