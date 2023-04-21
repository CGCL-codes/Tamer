package org.pagger.data.picture.io;

import java.io.IOException;
import org.pagger.data.io.MetadataReader;
import org.pagger.data.io.MetadataWriter;
import org.pagger.data.picture.Tiff;
import org.pagger.data.picture.TiffPictureMetadata;

/**
 * @author Gerd Saurer
 * @author Franz Wilhelmstötter
 */
public class TiffPictureMetadataIO implements MetadataReader<TiffPictureMetadata, Tiff>, MetadataWriter<TiffPictureMetadata, Tiff> {

    @Override
    public Class<Tiff> getDocumentType() {
        return Tiff.class;
    }

    @Override
    public Class<TiffPictureMetadata> getMetadataType() {
        return TiffPictureMetadata.class;
    }

    @Override
    public TiffPictureMetadata read(final Tiff document) throws IOException {
        return new TiffPictureMetadata(document.getExifRawMetadata().getMetadata());
    }

    @Override
    public void write(Tiff document) throws IOException {
        final TiffPictureMetadata metadata = document.getMetadata(getMetadataType());
        metadata.clearCache();
    }
}
