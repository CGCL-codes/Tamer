package com.ericdaugherty.mail.server.persistence;

import java.io.IOException;
import java.util.List;
import com.ericdaugherty.mail.server.services.smtp.*;

/**
 * A SMTP persistance processor interface.
 *
 * @author Andreas Kyrmegalos
 */
public interface SMTPMessagePersistenceProccessor {

    public void setMessage(SMTPMessage message);

    public void initializeMessage(String filename, boolean headersOnly) throws IOException;

    public long getSize();

    public void addDataLine(byte[] line);

    public void save(boolean useAmavisSMTPDirectory) throws IOException;

    public boolean saveBegin(boolean useAmavisSMTPDirectory);

    public void saveIncrement(List<byte[]> dataLines, boolean writeHeaders, boolean append) throws IOException;

    public boolean saveFinish();

    public List loadIncrementally(int start) throws IOException;

    public List loadIncrementally(int start, String messageName) throws IOException;

    public void moveToFailedFolder() throws Exception;

    public boolean isNotSavedInAmavis();

    public long getPersistedSize();

    public Object getPersistedID();

    public boolean deleteMessage();

    public void redirectToPostmaster() throws IOException;
}
