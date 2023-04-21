package com.aelitis.azureus.core.security;

public interface CryptoManagerPasswordHandler {

    /** 
	 * HANDLER_TYPE_UNKNOWN is not for public use 
	 **/
    public static final int HANDLER_TYPE_UNKNOWN = 0;

    public static final int HANDLER_TYPE_USER = 1;

    public static final int HANDLER_TYPE_SYSTEM = 2;

    public static final int HANDLER_TYPE_ALL = 3;

    public static final int ACTION_ENCRYPT = 1;

    public static final int ACTION_DECRYPT = 2;

    public int getHandlerType();

    /**
		 * Gets a password
		 * @param handler_type	from AESecurityManager.HANDLER_x enum
		 * @param action_type	from above ACTION_x enum
		 * @param reason		reason for the password being sought
		 * @return password details or null if no password available
		 */
    public passwordDetails getPassword(int handler_type, int action_type, boolean last_pw_incorrect, String reason);

    public void passwordOK(int handler_type, passwordDetails details);

    public interface passwordDetails {

        public char[] getPassword();

        /**
			 * @return	0 -> don't persist, Integer.MAX_VALUE -> persist forever
			 * < 0 -> current session; other -> seconds to persist
			 */
        public int getPersistForSeconds();
    }
}
