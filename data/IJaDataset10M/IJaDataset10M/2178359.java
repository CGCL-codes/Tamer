package net.sourceforge.calleridentific.calleridentification;

/**
 * Generates the message displayed next to the tray icon.
 * @author Christoph Neijenhuis
 */
public interface TrayMessageGenerator {

    /**
     * Generates a message with the information found in the contact.
     * @param newContact A contact with necessary information.
     * @return A message with information about the caller.
     */
    public String generateDisplayMessage(Contact newContact);

    /**
     * Generates a message including the phone number.
     * @param phoneNumber Phone number of the caller.
     * @return A message including the phone number.
     */
    public String generateDisplayMessage(String phoneNumber);
}
