package subget.threads.upload.addfiles;

import java.awt.AWTEvent;
import subget.VSpair;

/**
 *
 * @author povder
 */
public class UAddFilesItemAddEvent extends AWTEvent {

    public static final int ID = AWTEvent.RESERVED_ID_MAX + 19;

    private VSpair vspair;

    public UAddFilesItemAddEvent(UAddFilesThread source, VSpair vspair) {
        super(source, ID);
        this.vspair = vspair;
    }

    public VSpair getVspair() {
        return vspair;
    }
}
