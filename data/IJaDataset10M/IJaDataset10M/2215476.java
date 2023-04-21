package uk.ac.lkl.migen.system.expresser.model.tiednumber;

import uk.ac.lkl.migen.system.server.UserSet;

/**
 * That tied numbers would have handles that contain user sets was a bad idea.
 * 
 * Because there is lots of code that uses this the quickest fix is to make the
 * author null. But this class should be replaced by the idString.
 * 
 * @author Ken Kahn
 *
 */
@Deprecated
public class TiedNumberHandle {

    private String idString;

    public TiedNumberHandle(String idString) {
        this.idString = idString;
    }

    public TiedNumberHandle(String idString, UserSet ignoreUserSet) {
        this.idString = idString;
    }

    public String getIdString() {
        return idString;
    }

    public UserSet getAuthor() {
        return null;
    }
}
