package net.sf.frozen.utils.messaging;

/**
 * The Types for the Messages.
 * 
 * @author Inácio Ferrarini (inacioferrarini at users.sourceforge.net)
 * 
 */
public interface MessageTypes {

    /**
	 * The Messages´ Types
	 * 
	 * @author Inácio Ferrarini (inacioferrarini at users.sourceforge.net)
	 * 
	 */
    public enum MessageType {

        WARNING_MESSAGE("warning"), ERROR_MESSAGE("error");

        private String type = "";

        /**
		 * Creates a new MessageType.
		 * 
		 * @param type
		 *            the message´s type
		 */
        private MessageType(String type) {
            this.type = type;
        }

        /**
		 * Returns the String representation of the type
		 */
        public String toString() {
            return type;
        }
    }

    ;
}
