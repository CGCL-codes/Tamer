package net.sf.RecordEditor.re.file;

import java.io.IOException;
import javax.swing.SwingWorker;
import net.sf.JRecord.Common.RecordException;

public class FileWriterBackground extends SwingWorker<Void, Void> {

    FileWriter writer;

    public FileWriterBackground(FileWriter pWriter) throws RecordException {
        writer = pWriter;
    }

    @Override
    protected Void doInBackground() throws IOException {
        doWrite();
        return null;
    }

    public void doWrite() throws IOException {
        writer.doWrite();
        firePropertyChange("Finished", null, null);
    }
}
