package com.filetransfer.filedownloader.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.filetransfer.domain.ChunkData;
import com.filetransfer.domain.ChunkStatus;
import com.filetransfer.domain.FileInfo;

public class FileCache extends FileInfo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2519562690352404582L;

    /**
	 * 
	 * @param name
	 * @param size
	 * @param numChunks
	 * @param chunkSize
	 */
    public FileCache(String name, long size, int numChunks, int chunkSize) {
        super(name, size);
    }

    /**
	 * Array of ChunkData objects with their download status
	 */
    private List<ChunkData> chunkDataList = new ArrayList<ChunkData>();

    private Date lastDownloadedDate;

    private FileCacheStatus status = FileCacheStatus.NOT_DOWNLOADED;

    public Date getLastDownloadedDate() {
        return lastDownloadedDate;
    }

    public void setLastDownloadedDate(Date lastDownloadedDate) {
        this.lastDownloadedDate = lastDownloadedDate;
    }

    public FileCacheStatus getStatus() {
        return status;
    }

    public void setStatus(FileCacheStatus status) {
        this.status = status;
    }

    /**
	 * 
	 * @return
	 */
    public int getPercentageDownloaded() {
        int percentage = 0;
        if (status == FileCacheStatus.NOT_DOWNLOADED) {
            return percentage;
        } else if (status == FileCacheStatus.COMPLETED) {
            percentage = 100;
            return percentage;
        } else if (status == FileCacheStatus.IN_PROGRESS) {
            synchronized (this) {
                for (ChunkData chunk : chunkDataList) {
                    if (chunk.getStatus() == ChunkStatus.COMPLETED) {
                        percentage += 100 / numChunks;
                    }
                }
            }
        }
        return percentage;
    }

    public ChunkData getFirstUnProcessedChunk() {
        ChunkData data = null;
        synchronized (this) {
            for (ChunkData chunk : chunkDataList) {
                if (chunk != null && chunk.getStatus() == ChunkStatus.NOT_DOWNLOADED) {
                    data = chunk;
                }
            }
            return data;
        }
    }

    /**
	 * 
	 * @param chunkNum
	 * @return
	 */
    public ChunkData getChunk(int chunkNum) {
        ChunkData chunk = null;
        synchronized (this) {
            if (chunkNum >= 1 || chunkNum <= chunkDataList.size()) {
                chunk = chunkDataList.get(chunkNum);
            }
        }
        return chunk;
    }

    /**
	 * 
	 * @param chunk
	 */
    public void setChunk(ChunkData chunk) {
        synchronized (this) {
            if (chunk != null && chunk.getChunkNumber() >= 1 || chunk.getChunkNumber() <= chunkDataList.size()) {
                chunkDataList.set(chunk.getChunkNumber(), chunk);
            }
        }
    }
}
