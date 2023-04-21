package frost.threads.maintenance;

import java.io.File;
import frost.*;

public class DeleteWholeDirThread extends Thread {

    private final Core core;

    String delDir;

    public DeleteWholeDirThread(Core core, String dirToDelete) {
        delDir = dirToDelete;
        this.core = core;
    }

    public void run() {
        FileAccess.deleteDir(new File(delDir));
    }
}
