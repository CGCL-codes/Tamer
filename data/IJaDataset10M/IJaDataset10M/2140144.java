package net.sourceforge.neonzip.plugins;

import java.io.File;
import java.util.Vector;
import javax.swing.JPanel;
import net.sourceforge.neonzip.*;

public interface PluginInterface {

    boolean createNew(File aNewFile);

    Vector open(File aArchiveFile) throws OpenException;

    boolean close() throws CloseException;

    boolean addFile(File[] aFileList) throws AddException;

    boolean delete(int[] aItems) throws DeleteException;

    boolean extract(int[] aItems, File aDestinationDir) throws ExtractException;

    boolean test();

    File getFile();

    JPanel getExtractOptions();

    JPanel getAddOptions();

    void init(Main aMain);

    boolean supportCreateNew();

    boolean supportOpen();

    boolean supportClose();

    boolean supportAddFile();

    boolean supportDelete();

    boolean supportExtract();

    boolean supportTest();
}
