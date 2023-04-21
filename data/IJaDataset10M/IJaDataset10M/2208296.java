package es.rvp.java.simpletag.core.metadata;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import es.rvp.java.simpletag.core.exceptions.LoadingFileException;
import es.rvp.java.simpletag.core.exceptions.SettingValueTagException;
import es.rvp.java.simpletag.core.exceptions.UnknownExtensionException;
import es.rvp.java.simpletag.core.metadata.MetadataPathFile;
import es.rvp.java.simpletag.core.types.IDMetadataTypes;

public class MetadataPathFileTest extends AbstractMetadataTest {

    public MetadataPathFileTest(final File testFile) throws IOException {
        super(testFile);
    }

    @Test
    public final void testGetValueNotNull() throws LoadingFileException, UnknownExtensionException, SettingValueTagException {
        final MetadataPathFile metadata = (MetadataPathFile) AbstractMetadataTest.musicFile.getMetadataContainer().getMetadataByName(IDMetadataTypes.PathFile.getName());
        Assert.assertNotNull(metadata.getValue());
    }

    @Test
    public final void testImpossibleSetValue() throws LoadingFileException, UnknownExtensionException {
        boolean result = true;
        final MetadataPathFile metadata = (MetadataPathFile) AbstractMetadataTest.musicFile.getMetadataContainer().getMetadataByName(IDMetadataTypes.PathFile.getName());
        try {
            metadata.setValue("");
        } catch (final SettingValueTagException ex) {
            result = false;
        }
        Assert.assertFalse(result);
    }
}
