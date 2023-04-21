package com.elibera.m.events;

import java.io.IOException;
import java.io.OutputStream;
import com.elibera.m.display.ProgressCanvas;

/**
 * interface for recording element, file-browser, or similar
 * @author meisi
 *
 */
public interface RecordingElement {

    public String getContentType();

    public byte[] getData(RootThread thread);

    public long getDataSize();

    public void setData(byte[] data);

    public void setContentType(String contentType);

    public void recordingFinished();

    public void errorEoccured(String error);

    public int getRecordingSizeLimit();

    /**
	 * checks if the data is hold in memory or is cached as a file stream
	 * @return
	 */
    public boolean isStreamData();

    /**
	 * writes the content of the file to the output stream, does nothing if this is not
	 * a File-Stream
	 * @param out
	 * @param bc
	 * @throws Exception
	 */
    public void writeDataToStream(OutputStream out, ProgressCanvas bc) throws IOException;

    /**
	 * stores the data with an fileIOUrl
	 * @param fileIOUrl
	 * @param size
	 */
    public void setData(String fileIOUrl, long size);
}
