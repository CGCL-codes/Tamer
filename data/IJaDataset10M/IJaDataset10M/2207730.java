package com.ridanlabs.onelist.rpclib;

/**
 * Defines a set of methods for a client to interact with the
 * RPC server. This class is shared between the Android application
 * and the Web application/server. Note, this does not actually implement
 * the RPC methods.
 * 
 * @author Nadir Muzaffar
 *
 */
public final class OneListProtocol {

    public static final String EXCEPTION = "exception";

    public static final class Sync {

        public static final String METHOD = "sync";
    }

    public static final class RegisterUser {

        public static final String METHOD = "registerUser";

        public static final String PARAMS = "params";

        public static final class Params {

            public static final String USERNAME = "username";

            public static final String PASSWORD = "password";
        }
    }

    public static final class DeleteList {

        public static final String METHOD = "deleteList";
    }

    public static final class CreateList {

        public static final String METHOD = "createList";

        public static final String[] PARAMETERS = { "listName", "listCreator" };

        public static final String[] RETURN = { "success" };
    }

    public static final class RemoveListItem {

        public static final String METHOD = "removeListItem";
    }

    public static final class AddListItem {

        public static final String METHOD = "addListItem";

        public static final String[] PARAMETERS = { "itemName", "listName", "userName", "quantity" };

        public static final String[] RETURN = { "success" };
    }

    public static final class AddUserToList {

        public static final String METHOD = "addUserToList";

        public static final String[] PARAMETERS = { "user", "list" };

        public static final String[] RETURN = { "success" };
    }

    public static final class RemoveUserFromList {

        public static final String METHOD = "removeUserFromList";
    }

    public static final class RegisterDeviceWithUser {

        public static final String METHOD = "registerDeviceWithUser";

        public static final String[] PARAMETERS = { "deviceID", "userEmail" };

        public static final String[] RETURN = { "success" };
    }

    public static final class UnregisterDevice {

        public static final String METHOD = "unregisterDevice";
    }

    public static final class Ping {

        public static final String METHOD = "ping";
    }
}
