package gphoto.bo;

import java.awt.Dimension;
import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import algutil.date.ConversionDate;

public class PhotoBean implements Comparable<PhotoBean> {

    private static final Logger log = Logger.getLogger(PhotoBean.class);

    public static final String tableName = "photo";

    private File file = null;

    private String name = null;

    public static final String nameColumnName = "name";

    private String path = null;

    public static final String pathColumnName = "path";

    private int id;

    private static final String idColumnName = "id";

    private Calendar snapshotDate;

    private static final String snapshotDateTimeColumnName = "snapshot_dt";

    private String autor;

    private static final String autorColumnName = "autor";

    private Dimension dimension = null;

    private Calendar date = null;

    private String metadataDateTime = null;

    private String metadataMake = null;

    private String metadataModel = null;

    private String metadataComment = null;

    private String metadataImageLength = null;

    public static final String heightColumnName = "height";

    private int height = -1;

    private String metadataImageWidth = null;

    private int width = -1;

    public static final String widthColumnName = "length";

    private JpegImageMetadata metadata = null;

    private transient boolean pasDeMetadata = false;

    public PhotoBean(File file) {
        this.file = file;
    }

    public PhotoBean(ResultSet rs) throws SQLException {
        setId(rs.getInt(idColumnName));
        setName(rs.getString(nameColumnName));
        setPath(rs.getString(pathColumnName));
        setWidth(rs.getInt(widthColumnName));
        setHeight(rs.getInt(heightColumnName));
        setAutor(rs.getString(autorColumnName));
        setSnapshotDate(rs.getDate(snapshotDateTimeColumnName));
    }

    public Dimension getDimension() {
        if (dimension == null) {
            try {
                dimension = new Dimension(Integer.parseInt(getMetadataImageWidth()), Integer.parseInt(getMetadataImageLength()));
            } catch (Exception e) {
            }
        }
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        name = file.getName();
        path = file.getParent();
    }

    public long getTailleEnOctets() {
        return file.length();
    }

    public String getNom() {
        return file.getName();
    }

    public String getPath() {
        return file.getPath();
    }

    public String getParent() {
        return file.getParent();
    }

    private void setDate(String jour, String mois, String annee, String heures, String minutes, String secondes) {
        try {
            date = new GregorianCalendar(Integer.parseInt(annee), Integer.parseInt(mois) - 1, Integer.parseInt(jour), Integer.parseInt(heures), Integer.parseInt(minutes), Integer.parseInt(secondes));
        } catch (Exception e) {
            log.error("setDate ", e);
        }
    }

    public String getMetadataModel() {
        if (metadataModel == null && getMetadata() != null) {
            metadataModel = getEXIFValue(ExifTagConstants.EXIF_TAG_MODEL);
        }
        return metadataModel;
    }

    public String getMetadataMake() {
        if (metadataMake == null && getMetadata() != null) {
            metadataMake = getEXIFValue(ExifTagConstants.EXIF_TAG_MAKE);
        }
        return metadataMake;
    }

    public String getMetadataComment() {
        if (metadataComment == null && getMetadata() != null) {
            metadataComment = getEXIFValue(ExifTagConstants.EXIF_TAG_USER_COMMENT);
        }
        return metadataComment;
    }

    private String getEXIFValue(TagInfo tag) {
        return getMetadata().findEXIFValue(tag).getValueDescription();
    }

    public Calendar getDate() {
        if (date == null && getMetadata() != null) {
            try {
                String datetime = getMetadataDatetime();
                datetime = datetime.replaceAll("'", "");
                String annee = datetime.substring(0, 4);
                String mois = datetime.substring(5, 7);
                String jour = datetime.substring(8, 10);
                String heures = datetime.substring(11, 13);
                String minutes = datetime.substring(14, 16);
                String secondes = datetime.substring(17, 19);
                setDate(jour, mois, annee, heures, minutes, secondes);
            } catch (Exception e) {
                log.info("Impossible de r�cup�rer le date pour la photo " + file.getName());
            }
        }
        return date;
    }

    public String getDateForDisplay() {
        String date4Displ = null;
        if (getDate() == null) {
            date4Displ = ConversionDate.formatYYYYMMDD(file.lastModified());
        } else {
            date4Displ = getAnneeDate() + "-" + getMoisDate() + "-" + getJourDate();
        }
        return date4Displ;
    }

    public String getDateTimeForDisplay() {
        return getDateForDisplay() + " " + getHeureDate() + ":" + getMinuteDate() + ":" + getSecondDate();
    }

    public String getSecondDate() {
        if (getDate() != null) {
            return ConversionDate.getSecondes(getDate());
        } else {
            return ConversionDate.getSecondes(file.lastModified());
        }
    }

    public String getMinuteDate() {
        if (getDate() != null) {
            return ConversionDate.getMinutes(getDate());
        } else {
            return ConversionDate.getMinutes(file.lastModified());
        }
    }

    public String getHeureDate() {
        if (getDate() != null) {
            return ConversionDate.getHeures(getDate());
        } else {
            return ConversionDate.getHeures(file.lastModified());
        }
    }

    public String getJourDate() {
        if (getDate() != null) {
            return ConversionDate.getJour(getDate());
        } else {
            return ConversionDate.getJour(file.lastModified());
        }
    }

    /**
	 * Retourne le mois
	 */
    public String getMoisDate() {
        if (getDate() != null) {
            return ConversionDate.getMois(getDate());
        } else {
            return ConversionDate.getMois(file.lastModified());
        }
    }

    /**
	 * Retourne l'annee
	 */
    public String getAnneeDate() {
        if (getDate() != null) {
            return ConversionDate.getAnnee(getDate());
        } else {
            return ConversionDate.getAnnee4(file.lastModified());
        }
    }

    /**
	 * Comparaison par date
	 */
    public int compareTo(PhotoBean arg0) {
        if (getAnneeDate().compareTo(arg0.getAnneeDate()) != 0) {
            return getAnneeDate().compareTo(arg0.getAnneeDate());
        } else if (getMoisDate().compareTo(arg0.getMoisDate()) != 0) {
            return getMoisDate().compareTo(arg0.getMoisDate());
        } else if (getJourDate().compareTo(arg0.getJourDate()) != 0) {
            return getJourDate().compareTo(arg0.getJourDate());
        } else if (getHeureDate().compareTo(arg0.getHeureDate()) != 0) {
            return getHeureDate().compareTo(arg0.getHeureDate());
        } else if (getMinuteDate().compareTo(arg0.getMinuteDate()) != 0) {
            return getMinuteDate().compareTo(arg0.getMinuteDate());
        } else if (getSecondDate().compareTo(arg0.getSecondDate()) != 0) {
            return getSecondDate().compareTo(arg0.getSecondDate());
        } else {
            return getNom().compareTo(arg0.getNom());
        }
    }

    public boolean equals(PhotoBean arg0) {
        if (this.compareTo(arg0) == 0 && this.getTailleEnOctets() == arg0.getTailleEnOctets()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPaysage() {
        if (dimension == null) {
            return true;
        }
        if (dimension.width > dimension.height) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPortrait() {
        return !isPaysage();
    }

    public long getTailleEnKo() {
        return file.length() / 1024;
    }

    public String toString() {
        return file.getName();
    }

    public JpegImageMetadata getMetadata() {
        if (metadata == null && !pasDeMetadata) {
            IImageMetadata metadataTmp = null;
            try {
                metadataTmp = Sanselan.getMetadata(file);
            } catch (Exception e) {
                log.warn("Impossible de recuperer le metadata de la photo " + file.getPath(), e);
                pasDeMetadata = true;
            }
            if (metadataTmp instanceof JpegImageMetadata) {
                metadata = (JpegImageMetadata) metadataTmp;
            } else {
                pasDeMetadata = true;
            }
        }
        return metadata;
    }

    public String getMetadataDatetime() {
        if (metadataDateTime == null && getMetadata() != null) {
            metadataDateTime = getEXIFValue(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
            if (metadataDateTime == null) {
                metadataDateTime = getEXIFValue(TiffConstants.TIFF_TAG_DATE_TIME);
            }
        }
        return metadataDateTime;
    }

    public String getMetadataImageLength() {
        if (metadataImageLength == null && getMetadata() != null) {
            if (getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_LENGTH) != null) metadataImageLength = getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_LENGTH).getValueDescription();
        }
        return metadataImageLength;
    }

    public String getMetadataImageWidth() {
        if (metadataImageWidth == null && getMetadata() != null) {
            if (getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_WIDTH) != null) metadataImageWidth = getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_WIDTH).getValueDescription();
        }
        return metadataImageWidth;
    }

    public void afficherMetadata() {
        if (getMetadata() == null) {
            log.info("Pas de metadata");
        } else {
            if (getMetadata().findEXIFValue(TiffConstants.TIFF_TAG_DATE_TIME) != null) log.info("TIFF_TAG_DATE_TIME          : " + getMetadata().findEXIFValue(TiffConstants.TIFF_TAG_DATE_TIME).getValueDescription());
            if (getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL) != null) log.info("EXIF_TAG_DATE_TIME_ORIGINAL : " + getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL).getValueDescription());
            if (getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_LENGTH) != null) log.info("EXIF_TAG_EXIF_IMAGE_LENGTH  : " + getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_LENGTH).getValueDescription());
            if (getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_WIDTH) != null) log.info("EXIF_TAG_EXIF_IMAGE_WIDTH   : " + getMetadata().findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_WIDTH).getValueDescription());
        }
    }

    public void afficherAllMetadata() {
        if (getMetadata() == null) {
            log.info("Pas de metadata");
        } else {
            ArrayList<Object> l = getMetadata().getItems();
            for (int i = 0; i < l.size(); i++) {
                log.info(l.get(i));
            }
        }
    }

    public void renameTo(String newName) {
        file.renameTo(new File(newName));
    }

    public void renameTo(File newFile) {
        file.renameTo(newFile);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (name == null) {
            if (file != null) {
                name = file.getName();
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (file == null && path != null) {
            file = new File(path + File.separator + name);
        }
    }

    public void setPath(String path) {
        this.path = path;
        if (file == null && name != null) {
            file = new File(path + File.separator + name);
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        if (dimension == null && width != -1) {
            dimension = new Dimension(width, height);
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        if (dimension == null && height != -1) {
            dimension = new Dimension(width, height);
        }
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Calendar getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(Calendar snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public void setSnapshotDate(Date snapshotDate) {
        this.snapshotDate = ConversionDate.utilDate2Calendar(snapshotDate);
    }
}
