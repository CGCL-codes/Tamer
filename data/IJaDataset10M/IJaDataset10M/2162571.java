package org.epoline.phoenix.common.shared;

public class ItemUser extends org.epoline.phoenix.common.shared.Item implements Cloneable {

    private java.lang.String userID = "";

    private java.lang.String fullName = "";

    private java.lang.String key = "";

    public ItemUser() {
        super();
    }

    public ItemUser(String userID, String fullname) {
        super();
        if (!isValidUserID(userID)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        try {
            setUserID(userID);
            setFullName(fullname);
        } catch (PhoenixUserException ex) {
            throw new IllegalArgumentException("Invalid parameter" + ex);
        }
        if (isValid() == false) {
            throw new IllegalArgumentException("Invalid parameter");
        }
    }

    public ItemUser(String userID, String fullName, String key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        this.key = key;
        this.userID = userID;
        this.fullName = fullName;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ItemUser inUser = (ItemUser) obj;
        if (!isValid() || !inUser.isValid()) {
            return false;
        }
        return (inUser.getUserID().equals(getUserID()) && inUser.getFullName().equals(getFullName()));
    }

    public java.lang.String getFullName() {
        return fullName;
    }

    /**
	 * Insert the method's description here. Creation date: (6/8/01 11:45:07 AM)
	 * 
	 * @return java.lang.String
	 */
    public java.lang.String getKey() {
        return key;
    }

    /**
	 * Return a simple Comparator to compare two objects of this type.
	 * Comparison is done on base of name
	 */
    public java.util.Comparator getSimpleComparator() {
        return new java.util.Comparator() {

            public int compare(Object o1, Object o2) {
                return ((ItemUser) o1).getUserID().compareTo(((ItemUser) o2).getUserID());
            }
        };
    }

    public TableDescriptor getTableDescriptor() {
        return new AbstractTableDescriptor() {

            private String[] COLUMNS = { "UserID", "FullName" };

            public Object getField(Item item, int column) {
                ItemUser user = (ItemUser) item;
                if (user != null) {
                    switch(column) {
                        case 0:
                            return user.getUserID();
                        case 1:
                            return user.getFullName();
                        default:
                            return null;
                    }
                }
                return null;
            }

            public Class getColumnClass(int column) {
                switch(column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                }
                return null;
            }

            public String[] getColumnNames() {
                return COLUMNS;
            }
        };
    }

    public java.lang.String getUserID() {
        return userID;
    }

    public static String getValidUserID(String ID) throws PhoenixUserException {
        String trimmedID = Util.truncateTrim(ID, 20);
        if (trimmedID.length() == 0) throw new PhoenixUserException("Empty userID");
        return trimmedID;
    }

    public int hashCode() {
        return getUserID().hashCode();
    }

    public boolean isValid() {
        if (!isValidUserID(getUserID()) || !isValidFullName(getFullName())) {
            return false;
        }
        return true;
    }

    public static boolean isValidFullName(String fullName) {
        if (fullName == null) throw new NullPointerException("Invalid parameter");
        if (fullName.trim().length() > 0) return true; else return false;
    }

    public static boolean isValidUserID(String ID) {
        if (ID == null) {
            throw new NullPointerException();
        }
        if (ID.length() > 20) {
            return false;
        }
        if (ID.trim().length() == 0) {
            return false;
        }
        return true;
    }

    /**
	 * The input fullname is stored with leading and trailing spaces removed.
	 */
    public void setFullName(java.lang.String newFullName) {
        if (newFullName == null) {
            throw new NullPointerException("Invalid parameter");
        }
        String oldFullName = fullName;
        fullName = Util.truncateTrim(newFullName, 32);
        firePropertyChange("fullName", oldFullName, fullName);
    }

    public void setUserID(java.lang.String newUserID) throws PhoenixUserException {
        if (newUserID == null) {
            throw new NullPointerException("UserID");
        }
        String oldUserID = userID;
        userID = Util.truncateTrim(newUserID, 20).toUpperCase();
        firePropertyChange("userID", oldUserID, userID);
    }

    public String toString() {
        return getUserID() + ":" + getFullName();
    }
}
