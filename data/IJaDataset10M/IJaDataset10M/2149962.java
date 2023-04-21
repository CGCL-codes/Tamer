package org.gjt.sp.jedit.buffer;

import java.io.*;
import java.util.zip.*;
import java.util.List;
import org.gjt.sp.jedit.io.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.*;

/**
 * A buffer save request.
 * @author Slava Pestov
 * @version $Id: BufferSaveRequest.java 5486 2006-06-23 22:31:58Z kpouer $
 */
public class BufferSaveRequest extends BufferIORequest {

    /**
	 * Creates a new buffer I/O request.
	 * @param view The view
	 * @param buffer The buffer
	 * @param session The VFS session
	 * @param vfs The VFS
	 * @param path The path
	 */
    public BufferSaveRequest(View view, Buffer buffer, Object session, VFS vfs, String path) {
        super(view, buffer, session, vfs, path);
    }

    public void run() {
        OutputStream out = null;
        boolean vfsRenameCap = (vfs.getCapabilities() & VFS.RENAME_CAP) != 0;
        boolean overwriteReadOnly = buffer.getBooleanProperty("overwriteReadonly");
        boolean twoStageSave = overwriteReadOnly || (vfsRenameCap && jEdit.getBooleanProperty("twoStageSave"));
        try {
            String[] args = { vfs.getFileName(path) };
            setStatus(jEdit.getProperty("vfs.status.save", args));
            setAbortable(true);
            path = vfs._canonPath(session, path, view);
            if (!MiscUtilities.isURL(path)) path = MiscUtilities.resolveSymlinks(path);
            if (buffer.getProperty(Buffer.BACKED_UP) == null || jEdit.getBooleanProperty("backupEverySave")) {
                vfs._backup(session, path, view);
                buffer.setBooleanProperty(Buffer.BACKED_UP, true);
            }
            String savePath;
            if (twoStageSave) {
                savePath = vfs.getTwoStageSaveName(path);
                if (savePath == null) {
                    twoStageSave = false;
                    savePath = path;
                }
            } else savePath = path;
            out = vfs._createOutputStream(session, savePath, view);
            try {
                buffer.readLock();
                if (out != null) {
                    if (path.endsWith(".gz")) buffer.setBooleanProperty(Buffer.GZIPPED, true); else if (buffer.getName().endsWith(".gz")) {
                        buffer.setBooleanProperty(Buffer.GZIPPED, false);
                    }
                    if (buffer.getBooleanProperty(Buffer.GZIPPED)) out = new GZIPOutputStream(out);
                    write(buffer, out);
                    if (twoStageSave) {
                        if (!vfs._rename(session, savePath, path, view)) throw new IOException("Rename failed: " + savePath);
                    }
                    if ((vfs.getCapabilities() & VFS.DELETE_CAP) != 0) {
                        if (jEdit.getBooleanProperty("persistentMarkers") && !buffer.getMarkers().isEmpty()) {
                            setStatus(jEdit.getProperty("vfs.status.save-markers", args));
                            setValue(0);
                            out = vfs._createOutputStream(session, markersPath, view);
                            if (out != null) writeMarkers(buffer, out);
                        } else vfs._delete(session, markersPath, view);
                    }
                } else buffer.setBooleanProperty(ERROR_OCCURRED, true);
                if (!twoStageSave) VFSManager.sendVFSUpdate(vfs, path, true);
            } finally {
                buffer.readUnlock();
            }
        } catch (IOException io) {
            Log.log(Log.ERROR, this, io);
            String[] pp = { io.toString() };
            VFSManager.error(view, path, "ioerror.write-error", pp);
            buffer.setBooleanProperty(ERROR_OCCURRED, true);
        } catch (WorkThread.Abort a) {
            IOUtilities.closeQuietly(out);
            buffer.setBooleanProperty(ERROR_OCCURRED, true);
        } finally {
            try {
                vfs._saveComplete(session, buffer, path, view);
                if (twoStageSave) {
                    vfs._finishTwoStageSave(session, buffer, path, view);
                }
                vfs._endVFSSession(session, view);
            } catch (IOException io) {
                Log.log(Log.ERROR, this, io);
                String[] pp = { io.toString() };
                VFSManager.error(view, path, "ioerror.write-error", pp);
                buffer.setBooleanProperty(ERROR_OCCURRED, true);
            } catch (WorkThread.Abort a) {
                buffer.setBooleanProperty(ERROR_OCCURRED, true);
            }
        }
    }

    private static void writeMarkers(Buffer buffer, OutputStream out) throws IOException {
        Writer o = new BufferedWriter(new OutputStreamWriter(out));
        try {
            List markers = buffer.getMarkers();
            for (int i = 0; i < markers.size(); i++) {
                Marker marker = (Marker) markers.get(i);
                o.write('!');
                o.write(marker.getShortcut());
                o.write(';');
                String pos = String.valueOf(marker.getPosition());
                o.write(pos);
                o.write(';');
                o.write(pos);
                o.write('\n');
            }
        } finally {
            o.close();
        }
    }
}
