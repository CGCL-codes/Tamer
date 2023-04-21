package ro.romanescu.useradmin.rights;

import ro.romanescu.useradmin.IRight;

public class RightsList implements IRight {

    public String getDescription() {
        return "The privilege to access the list of rights";
    }

    public void setDescription(String description) {
    }
}
