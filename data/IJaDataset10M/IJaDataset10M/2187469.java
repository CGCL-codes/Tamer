package ports;

import java.util.Vector;

/**
 * Description class XD
 * @author FORDPREFECT
 *
 */
public class Description {

    private String ImageDate;

    private String OwnerName;

    private String Imagedescription;

    private Vector messages;

    private String prefix;

    /**
	 * Constructor
	 * @param imageDate : image Date
	 * @param ownerName : owner Name
	 * @param imagedescription : Image Description
	 */
    public Description(String imageDate, String ownerName, String imagedescription) {
        super();
        ImageDate = imageDate;
        Imagedescription = imagedescription;
        OwnerName = ownerName;
        messages = new Vector();
    }

    /**
	 * Constructor
	 * @param imageDate : image Date
	 * @param ownerName : owner Name
	 * @param imagedescription : Image Description
	 * @param prefix: The file prefix 
	 */
    public Description(String imageDate, String ownerName, String imagedescription, String prefix) {
        super();
        ImageDate = imageDate;
        Imagedescription = imagedescription;
        OwnerName = ownerName;
        messages = new Vector();
        this.prefix = prefix;
    }

    /**
	 * @return Returns the imageDate.
	 */
    public String getImageDate() {
        return ImageDate;
    }

    /**
	 * @param imageDate The imageDate to set.
	 */
    public void setImageDate(String imageDate) {
        ImageDate = imageDate;
    }

    /**
	 * @return Returns the imagedescription.
	 */
    public String getImagedescription() {
        return Imagedescription;
    }

    /**
	 * @param imagedescription The imagedescription to set.
	 */
    public void setImagedescription(String imagedescription) {
        Imagedescription = imagedescription;
    }

    /**
	 * @return Returns the messages.
	 */
    public Vector getMessages() {
        return messages;
    }

    /**
	 * @param messages The messages to set.
	 */
    public void setMessages(Vector messages) {
        this.messages = messages;
    }

    /**
	 * @return Returns the owerName.
	 */
    public String getOwerName() {
        return OwnerName;
    }

    /**
	 * @param owerName The owerName to set.
	 */
    public void setOwerName(String owerName) {
        this.OwnerName = owerName;
    }

    /**
	 * @param m
	 */
    public void addMessage(Message m) {
        messages.addElement(m);
    }

    /**
	 * @param index
	 * @return
	 */
    public Message getMessage(int index) {
        return (Message) messages.elementAt(index);
    }

    /**
	 * @return
	 */
    public int numMessage() {
        return messages.size();
    }

    /**
	 * @return Returns the ownerName.
	 */
    public String getOwnerName() {
        return OwnerName;
    }

    /**
	 * @param ownerName The ownerName to set.
	 */
    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    /**
	 * @return Returns the prefix.
	 */
    public String getPrefix() {
        return prefix;
    }

    /**
	 * @param prefix The prefix to set.
	 */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String toString() {
        String result = "Image Data:" + ImageDate + "\n";
        result += "Owner Name:" + OwnerName + "\n";
        result += "Image Description" + Imagedescription + "\n";
        result += "Message: " + messages.toString() + "\n";
        result += "Prefix: " + prefix.toString();
        return result;
    }
}
