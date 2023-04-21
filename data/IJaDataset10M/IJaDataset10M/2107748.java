package com.aelitis.azureus.core.diskmanager.file.impl;

import java.io.File;
import org.gudy.azureus2.core3.torrent.*;
import org.gudy.azureus2.core3.util.AEDiagnostics;
import org.gudy.azureus2.core3.util.DirectByteBuffer;
import com.aelitis.azureus.core.diskmanager.file.*;

/**
 * @author parg
 *
 */
public class FMFileTestImpl extends FMFileUnlimited {

    protected long file_offset_in_torrent;

    protected FMFileTestImpl(FMFileOwner _owner, FMFileManagerImpl _manager, File _file, int _type) throws FMFileManagerException {
        super(_owner, _manager, _file, _type);
        TOTorrentFile torrent_file = getOwner().getTorrentFile();
        TOTorrent torrent = torrent_file.getTorrent();
        for (int i = 0; i < torrent.getFiles().length; i++) {
            TOTorrentFile f = torrent.getFiles()[i];
            if (f == torrent_file) {
                break;
            }
            file_offset_in_torrent += f.getLength();
        }
    }

    protected void readSupport(DirectByteBuffer buffer, long offset) throws FMFileManagerException {
        if (AEDiagnostics.CHECK_DUMMY_FILE_DATA) {
            offset += file_offset_in_torrent;
            while (buffer.hasRemaining(DirectByteBuffer.SS_FILE)) {
                buffer.put(DirectByteBuffer.SS_FILE, (byte) offset++);
            }
        } else {
            buffer.position(DirectByteBuffer.SS_FILE, buffer.limit(DirectByteBuffer.SS_FILE));
        }
    }

    protected void writeSupport(DirectByteBuffer[] buffers, long offset) throws FMFileManagerException {
        offset += file_offset_in_torrent;
        for (int i = 0; i < buffers.length; i++) {
            DirectByteBuffer buffer = buffers[i];
            if (AEDiagnostics.CHECK_DUMMY_FILE_DATA) {
                while (buffer.hasRemaining(DirectByteBuffer.SS_FILE)) {
                    byte v = buffer.get(DirectByteBuffer.SS_FILE);
                    if ((byte) offset != v) {
                        System.out.println("FMFileTest: write is bad at " + offset + ": expected = " + (byte) offset + ", actual = " + v);
                        offset += buffer.remaining(DirectByteBuffer.SS_FILE) + 1;
                        break;
                    }
                    offset++;
                }
            }
            buffer.position(DirectByteBuffer.SS_FILE, buffer.limit(DirectByteBuffer.SS_FILE));
        }
    }
}
