package au.gov.naa.digipres.xena.plugin.archive.tar;

import java.io.InputStream;
import au.gov.naa.digipres.xena.plugin.archive.ArchiveHandler;
import au.gov.naa.digipres.xena.plugin.archive.ArchiveNormaliser;
import com.ice.tar.TarInputStream;

/**
 * Normaliser for .tar files
 * 
 * created 28/03/2007
 * archive
 * Short desc of class:
 */
public class TarNormaliser extends ArchiveNormaliser {

    @Override
    protected ArchiveHandler getArchiveHandler(InputStream archiveStream) {
        TarInputStream tarStream = new TarInputStream(archiveStream);
        return new TarHandler(tarStream);
    }

    @Override
    public String getName() {
        return "Tar";
    }

    @Override
    public boolean isConvertible() {
        return true;
    }

    @Override
    public String getOutputFileExtension() {
        return "tar";
    }
}
