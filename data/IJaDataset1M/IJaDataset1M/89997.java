package org.processmining.framework.models.recommendation.net;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;
import org.processmining.lib.mxml.LogException;
import org.processmining.lib.mxml.writing.persistency.LogPersistency;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 *
 */
public class StringLogPersistency extends LogPersistency {

    protected ByteArrayOutputStream outStream = null;

    @Override
    protected void closeFile() throws LogException {
    }

    @Override
    protected OutputStream createFile(String arg0) throws LogException {
        outStream = new ByteArrayOutputStream();
        return outStream;
    }

    @Override
    public void finish() throws LogException {
    }

    public String getOutputString() {
        return outStream.toString();
    }
}
