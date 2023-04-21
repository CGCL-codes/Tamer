package magoffin.matt.meta.audio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import org.farng.mp3.MP3MetadataContainer;
import org.farng.mp3.MP3MetadataItem;
import org.farng.mp3.MP3Tag;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v2_3;
import magoffin.matt.meta.MetadataImage;
import magoffin.matt.meta.MetadataResource;
import magoffin.matt.meta.support.BasicMetadataImage;

/**
 * {@link MetadataResource} implementation for ID3v2.3 resources.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 1.2 $ $Date: 2007/01/20 07:07:29 $
 */
public class ID3v2_3MetadataResource extends ID3v2_2MetadataResource {

    /**
	 * Construct with a RandomAccessFile.
	 * 
	 * @param file the file
	 * @throws IOException if an error occurs
	 */
    public ID3v2_3MetadataResource(RandomAccessFile file) throws IOException {
        super(file);
    }

    /**
	 * Get a {@link MP3Tag} implementation for a file.
	 * 
	 * @param file the file
	 * @throws IOException if an IO error occurs
	 * @return the MP3Tag implementation
	 */
    @Override
    protected MP3Tag getMP3Tag(RandomAccessFile file) throws IOException {
        MP3Tag id3 = null;
        try {
            id3 = new ID3v2_3(file);
        } catch (TagException e) {
            throw new RuntimeException(e);
        }
        return id3;
    }

    /**
	 * Extract a {@link MetadataImage} for the {@link AudioMetadataType#ALBUM_COVER}.
	 * 
	 * <p>This implementation looks for a {@link MP3MetadataItem} with 
	 * the ID <code>APIC</code> and extracts the picture data from there.</p>
	 * 
	 * @param id3 the ID3 tag data to extract from
	 */
    @Override
    protected void handleAlbumCover(MP3Tag id3) {
        Iterator<MP3MetadataItem> iterator = id3.iterator();
        while (iterator.hasNext()) {
            MP3MetadataItem meta = iterator.next();
            String id = meta.getIdentifier();
            if (id == null || !id.startsWith("APIC")) {
                continue;
            }
            MP3MetadataContainer body = meta.getBody();
            String mime = (String) body.getObject("MIME Type");
            byte[] pic = (byte[]) body.getObject("Picture Data");
            if (mime != null && pic != null && pic.length > 0) {
                setValue(AudioMetadataType.ALBUM_COVER, new BasicMetadataImage(mime, pic));
                return;
            }
        }
    }
}
