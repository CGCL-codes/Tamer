package gnu.saw.server.filesystem;

import gnu.saw.server.session.SAWServerSession;
import java.io.File;
import java.io.IOException;

public class SAWServerFileSystemRootsResolver implements Runnable {

    private volatile boolean finished;

    private StringBuilder message;

    private SAWServerSession session;

    public SAWServerFileSystemRootsResolver(SAWServerSession session) {
        this.session = session;
        this.message = new StringBuilder();
        this.finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void run() {
        try {
            message.setLength(0);
            File[] roots = File.listRoots();
            message.append("\nSAW>SAWFILESYSTEMROOTSLIST:File system roots on server file system:\nSAW>");
            for (File root : roots) {
                message.append("\nSAW>Canonical path: '" + root.getCanonicalPath() + '\'');
            }
            message.append("\nSAW>\nSAW>SAWFILESYSTEMROOTSLIST:End of file system roots list\nSAW>");
            synchronized (this) {
                session.getConnection().getResultWriter().write(message.toString());
                session.getConnection().getResultWriter().flush();
                finished = true;
            }
        } catch (SecurityException e) {
            synchronized (this) {
                try {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILESYSTEMROOTSLIST:Security error detected!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                } catch (IOException e1) {
                }
                finished = true;
            }
        } catch (Exception e) {
        }
        finished = true;
    }
}
