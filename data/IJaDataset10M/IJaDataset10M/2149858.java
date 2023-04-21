package net.sourceforge.filebot.archive;

import java.io.*;
import java.util.*;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.*;

class ArchiveOpenVolumeCallback implements IArchiveOpenVolumeCallback, IArchiveOpenCallback, Closeable {

    /**
	 * Cache for opened file streams
	 */
    private Map<String, RandomAccessFile> openedRandomAccessFileList = new HashMap<String, RandomAccessFile>();

    /**
	 * Name of the last volume returned by {@link #getStream(String)}
	 */
    private String name;

    /**
	 * This method should at least provide the name of the last
	 * opened volume (propID=PropID.NAME).
	 * 
	 * @see IArchiveOpenVolumeCallback#getProperty(PropID)
	 */
    public Object getProperty(PropID propID) throws SevenZipException {
        switch(propID) {
            case NAME:
                return name;
        }
        return null;
    }

    /**
	 * The name of the required volume will be calculated out of the
	 * name of the first volume and a volume index. In case of RAR file,
	 * the substring ".partNN." in the name of the volume file will
	 * indicate a volume with id NN. For example:
	 * <ul>
	 * <li>test.rar - single part archive or multi-part archive with a single volume</li>
	 * <li>test.part23.rar - 23-th part of a multi-part archive</li>
	 * <li>test.part001.rar - first part of a multi-part archive. "00" indicates, that at least 100 volumes must exist.</li>
	 * </ul>
	 */
    public IInStream getStream(String filename) throws SevenZipException {
        try {
            RandomAccessFile randomAccessFile = openedRandomAccessFileList.get(filename);
            if (randomAccessFile != null) {
                randomAccessFile.seek(0);
                name = filename;
                return new RandomAccessFileInStream(randomAccessFile);
            }
            randomAccessFile = new RandomAccessFile(filename, "r");
            openedRandomAccessFileList.put(filename, randomAccessFile);
            name = filename;
            return new RandomAccessFileInStream(randomAccessFile);
        } catch (FileNotFoundException fileNotFoundException) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Close all opened streams
	 */
    public void close() throws IOException {
        for (RandomAccessFile file : openedRandomAccessFileList.values()) {
            file.close();
        }
    }

    @Override
    public void setCompleted(Long files, Long bytes) throws SevenZipException {
    }

    @Override
    public void setTotal(Long files, Long bytes) throws SevenZipException {
    }
}
