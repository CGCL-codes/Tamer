package jorgan.exporter.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import jorgan.swing.wizard.Page;

/**
 * An import.
 */
public interface Export {

    public List<Page> getPages();

    public String getName();

    public String getDescription();

    public abstract String getMimeType();

    public abstract void stream(OutputStream output) throws IOException;
}
