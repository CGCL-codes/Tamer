package sobchak.tools.exception;

import sobchak.ITajik;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.05.11
 * Time: 19:11
 * To change this template use File | Settings | File Templates.
 */
public class TajikAlreadyInRoomException extends RuntimeException {

    private ITajik tajik;

    public TajikAlreadyInRoomException(ITajik tajik) {
        this.tajik = tajik;
    }

    @Override
    public String getMessage() {
        return "Tajik " + tajik.toString() + " has been meet to the room";
    }
}
