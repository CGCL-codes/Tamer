package converter.gui.filefilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Christian
 */
public class PcapFileFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory() == true) return true;
        if (f.getName().endsWith(".pcap")) return true;
        return false;
    }

    public String getDescription() {
        return "*.pcap";
    }
}
