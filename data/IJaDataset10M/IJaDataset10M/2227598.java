package SequenceJuxtaposer;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author hilde
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FastaFilter extends FileFilter {

    public static final String fasta_file_description = "fasta file ( .fa, .fasta )";

    public static final String fasta_file_extension = "fa";

    public static final String fasta_file_extension_long = "fasta";

    public FastaFilter() {
    }

    public String getDescription() {
        return fasta_file_description;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String fileName = f.getName();
        if (fileName != null && (fileName.endsWith(fasta_file_extension) || fileName.endsWith(fasta_file_extension_long))) return true;
        return false;
    }
}
