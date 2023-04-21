package android.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;

/** @hide */
public class FileBackupHelper extends FileBackupHelperBase implements BackupHelper {

    private static final String TAG = "FileBackupHelper";

    private static final boolean DEBUG = false;

    Context mContext;

    File mFilesDir;

    String[] mFiles;

    public FileBackupHelper(Context context, String... files) {
        super(context);
        mContext = context;
        mFilesDir = context.getFilesDir();
        mFiles = files;
    }

    /**
     * Based on oldState, determine which of the files from the application's data directory
     * need to be backed up, write them to the data stream, and fill in newState with the
     * state as it exists now.
     */
    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {
        String[] files = mFiles;
        File base = mContext.getFilesDir();
        final int N = files.length;
        String[] fullPaths = new String[N];
        for (int i = 0; i < N; i++) {
            fullPaths[i] = (new File(base, files[i])).getAbsolutePath();
        }
        performBackup_checked(oldState, data, newState, fullPaths, files);
    }

    public void restoreEntity(BackupDataInputStream data) {
        if (DEBUG) Log.d(TAG, "got entity '" + data.getKey() + "' size=" + data.size());
        String key = data.getKey();
        if (isKeyInList(key, mFiles)) {
            File f = new File(mFilesDir, key);
            writeFile(f, data);
        }
    }
}
