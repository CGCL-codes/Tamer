package com.nullfish.lib.vfs.impl.antzip;

import java.io.File;
import java.io.IOException;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.exception.VFSIOException;
import com.nullfish.lib.vfs.impl.local.LocalFile;

public class TemporaryFileUtil {

    /**
	 * シングルトンインスタンス
	 */
    private static TemporaryFileUtil instance = new TemporaryFileUtil();

    private File tempDir;

    private TemporaryFileUtil() {
        tempDir = new File(System.getProperty("java.io.tmpdir"));
    }

    /**
	 * シングルトンインスタンスを取得する。
	 * 
	 * @return
	 */
    public static TemporaryFileUtil getInstance() {
        return instance;
    }

    /**
	 * リモートファイルのテンポラリローカルコピーを取得する。
	 * @param file
	 * @param manipulation
	 * @return
	 * @throws VFSException
	 */
    public LocalFile getTemporaryFile(VFile file, Manipulation manipulation) throws VFSException {
        try {
            File localFile = createTemporaryFile(file);
            VFile rtn = file.getFileSystem().getVFS().getFile(localFile.getAbsolutePath());
            file.copyTo(rtn, manipulation);
            return (LocalFile) rtn;
        } catch (IOException e) {
            throw new VFSIOException(e);
        }
    }

    private synchronized File createTemporaryFile(VFile file) throws IOException {
        String fileName = file.getName();
        for (int i = 0; true; i++) {
            File rtn = new File(tempDir, "[" + i + "]" + fileName);
            if (!rtn.exists()) {
                rtn.createNewFile();
                return rtn;
            }
        }
    }
}
