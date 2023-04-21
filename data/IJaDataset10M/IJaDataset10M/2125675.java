package jSimMacs.fileIO.writer;

import jSimMacs.display.GroTab;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author sr
 *
 */
public class TabWriter implements IFileWriter {

    private GroTab tab;

    public TabWriter(GroTab tab) {
        this.tab = tab;
    }

    public void write() throws IOException {
        if (tab != null) {
            FileWriter writer = null;
            BufferedWriter bfWriter = null;
            try {
                writer = new FileWriter(tab.getFile());
                bfWriter = new BufferedWriter(writer);
                tab.getEditorPane().write(bfWriter);
            } catch (IOException e) {
                throw e;
            } finally {
                try {
                    if (writer != null) writer.close();
                    if (bfWriter != null) bfWriter.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
}
