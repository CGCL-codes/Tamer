package gnu.saw.server.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.zip.Deflater;
import gnu.saw.filesystem.SAWZipUtils;
import gnu.saw.server.session.SAWServerSession;

public class SAWServerZipFileCreateOperation implements Runnable {

    private volatile boolean finished;

    private String zipFilePath;

    private String[] sourcePaths;

    private byte[] readBuffer;

    private SAWServerSession session;

    public SAWServerZipFileCreateOperation(SAWServerSession session) {
        this.session = session;
        this.finished = true;
        this.readBuffer = new byte[64 * 1024];
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
        File zipFile = new File(zipFilePath);
        if (!zipFile.isAbsolute()) {
            this.zipFilePath = new File(session.getWorkingDirectory(), zipFilePath).getAbsolutePath();
        }
    }

    public void setSourcePaths(String[] sourcePaths) {
        String[] checkedPaths = new String[sourcePaths.length];
        int i = 0;
        for (String sourcePath : sourcePaths) {
            File sourceFile = new File(sourcePath);
            checkedPaths[i] = sourceFile.getAbsolutePath();
            if (!sourceFile.isAbsolute()) {
                checkedPaths[i] = new File(session.getWorkingDirectory(), sourcePath).getAbsolutePath();
            }
            i++;
        }
        this.sourcePaths = checkedPaths;
    }

    public void run() {
        try {
            try {
                if (SAWZipUtils.createZipFile(zipFilePath, Deflater.BEST_SPEED, readBuffer, sourcePaths)) {
                    synchronized (this) {
                        session.getConnection().getResultWriter().write("\nSAW>SAWREMOTEZIPCREATE:Zip file '" + zipFilePath + "' created on server file system!\nSAW>");
                        session.getConnection().getResultWriter().flush();
                        finished = true;
                    }
                } else {
                    synchronized (this) {
                        session.getConnection().getResultWriter().write("\nSAW>SAWREMOTEZIPCREATE:Zip file '" + zipFilePath + "' cannot be created on server file system!\nSAW>");
                        session.getConnection().getResultWriter().flush();
                        finished = true;
                    }
                }
            } catch (ClosedByInterruptException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWREMOTEZIPCREATE:Zip file '" + zipFilePath + "' creation interrupted!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (IOException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWREMOTEZIPCREATE:Zip file '" + zipFilePath + "' cannot be created on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (Exception e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWREMOTEZIPCREATE:Error detected while creating zip file '" + zipFilePath + "' on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            }
        } catch (Exception e) {
        }
        finished = true;
    }
}
