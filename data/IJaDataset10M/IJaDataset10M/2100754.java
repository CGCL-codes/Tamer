package com.google.code.gronono.commons.exif;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDescriptor;
import com.drew.metadata.exif.ExifDirectory;
import com.google.code.gronono.commons.i18n.BundleKey;
import com.google.code.gronono.commons.i18n.BundleName;
import com.google.code.gronono.commons.io.file.FileUtils;

/**
 * Classe permettant de lire les métadonnées EXIFs d'un fichier.
 * <br/>Seuls les fichiers JPEG sont supportés.
 *
 * @author Arnaud BRUNET
 */
@BundleName(value = "com.google.code.gronono.commons.exif")
public class ExifDataReader {

    /** Message d'erreur de lecture des données EXIF. */
    @BundleKey("reading.metadata.error")
    private static String READING_METADATA_ERROR;

    /** Message d'erreur de lecture de l'orientation. */
    @BundleKey("unknown.orientation.error")
    private static String UNKNOW_ORIENTATION_ERROR;

    /** Conteneur des métadonnées Exif associées au fichier image. */
    private ExifDirectory exifDirectory;

    /** Métadonnées Exif extraites du conteneur. */
    private ExifData exifData;

    /** Fichier image. */
    private final File file;

    /**
	 * Constructeur.
	 * @param file Le fichier image.
	 */
    public ExifDataReader(final File file) {
        this.file = file;
    }

    /**
	 * Lecture des métadonnées Exif du fichier image {@link #file} fourni au constructeur {@link #ExifDataReader(File)}.
	 *
	 * @return Les métadonnées Exif lues, <code>null</code> si la lecture n'a rien donné (pas de métadonnées, données vides, etc).
	 * @throws IOException En cas de problème de lecture.
	 *
	 * @see #readDate()
	 * @see #readOrientation()
	 * @see #readThumbnail()
	 */
    public ExifData read() throws IOException {
        FileUtils.checkCanReadFile(file);
        this.exifData = new ExifData();
        this.exifDirectory = createExifDirectory();
        this.exifData.setDate(readDate());
        this.exifData.setOrientation(readOrientation());
        this.exifData.setThumbnail(readThumbnail());
        return exifData;
    }

    /**
	 * Initialisation du conteneur de métadonnées Exif pour le fichier {@link #file} référencé.
	 *
	 * @return Le conteneur de métadonnées Exif {@link #exifDirectory} initialisé.
	 * @throws IOException si une <code>JpegProcessingException<code> est levée.
	 *
	 * @see JpegMetadataReader#readMetadata(File)
	 */
    private ExifDirectory createExifDirectory() throws IOException {
        Metadata metadata = null;
        try {
            metadata = JpegMetadataReader.readMetadata(file);
        } catch (final JpegProcessingException e) {
            throw new IOException(MessageFormat.format(READING_METADATA_ERROR, file.getAbsolutePath()));
        }
        return (ExifDirectory) metadata.getDirectory(ExifDirectory.class);
    }

    /**
	 * Lecture de la date de prise de vue à partir des tags Exif.
	 *
	 * @return La date de prise de vue, <code>null</code> si elle n'a pas pu être déterminée.
	 *
	 * @see ExifDirectory#getDate(int)
	 * @see ExifDirectory#TAG_DATETIME
	 * @see ExifDirectory#TAG_DATETIME_ORIGINAL
	 * @see ExifDirectory#TAG_DATETIME_DIGITIZED
	 */
    private Date readDate() {
        Date date = null;
        try {
            date = exifDirectory.getDate(ExifDirectory.TAG_DATETIME);
        } catch (final MetadataException e) {
            try {
                date = exifDirectory.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
            } catch (final MetadataException e1) {
                try {
                    date = exifDirectory.getDate(ExifDirectory.TAG_DATETIME_DIGITIZED);
                } catch (final MetadataException e2) {
                }
            }
        }
        return date;
    }

    /**
	 * Lecture de l'orientation à partir du tag Exif.
	 *
	 * @return L'{@link Orientation}, <code>null</code> si elle n'a pas pu être déterminée.
	 * @throws IllegalStateException si le tag lu ne fait pas partie des valeurs connues.
	 *
	 * @see ExifDescriptor#getOrientationDescription()
	 * @see ExifDirectory#TAG_ORIENTATION
	 */
    private Orientation readOrientation() {
        final Orientation orientation = null;
        try {
            final int orientationValue = (Integer) exifDirectory.getObject(ExifDirectory.TAG_ORIENTATION);
            switch(orientationValue) {
                case 1:
                    return Orientation.ANGLE_0;
                case 2:
                    return Orientation.ANGLE_0_MIRROR;
                case 3:
                    return Orientation.ANGLE_180;
                case 4:
                    return Orientation.ANGLE_180_MIRROR;
                case 6:
                    return Orientation.ANGLE_90;
                case 7:
                    return Orientation.ANGLE_90_MIRROR;
                case 8:
                    return Orientation.ANGLE_270;
                case 5:
                    return Orientation.ANGLE_270_MIRROR;
                default:
                    throw new IllegalStateException(MessageFormat.format(UNKNOW_ORIENTATION_ERROR, orientation, file.getAbsolutePath()));
            }
        } catch (final Exception e) {
        }
        return orientation;
    }

    /**
	 * Lecture du thumbnail (miniature ou imagette) à partir du tag Exif.
	 *
	 * @return La miniature sous la forme d'un tableau d'octets (au format jpeg), <code>null</code> si elle n'a pas pu être déterminé.
	 *
	 * @see ExifDirectory#getThumbnailData()
	 */
    private byte[] readThumbnail() {
        byte[] thumbnailBytes = null;
        try {
            thumbnailBytes = exifDirectory.getThumbnailData();
        } catch (final MetadataException e) {
        }
        if ((thumbnailBytes == null) || (thumbnailBytes.length == 0)) {
            return null;
        }
        return thumbnailBytes;
    }
}
