package com.lewisshell.helpyourself.psa;

import java.io.*;
import java.util.*;
import javax.media.jai.operator.*;
import org.apache.commons.lang.*;
import org.apache.commons.logging.*;
import com.drew.imaging.jpeg.*;
import com.drew.metadata.*;
import com.drew.metadata.exif.*;
import com.lewisshell.helpyourself.*;

/**
 * @author RichardL
 */
public class Image implements Serializable, Comparable {

    private static final Log LOG = LogFactory.getLog(Image.class);

    private int id;

    private String caption;

    private String notes;

    private String mediaFullPath;

    private String editedMediaFullPath;

    private int attributes;

    private Collection<Integer> folderIds;

    private int mediaPixelWidth;

    private int mediaPixelHeight;

    private Date imageDate;

    private Date imageTime;

    private Long size;

    private transient Boolean stacked;

    private transient Boolean stackTop;

    private transient Boolean stackHidden;

    private transient Folder stackFolder;

    private transient boolean stackFolderCached;

    private transient ImportFolder importFolder;

    private transient boolean importFolderCached;

    private transient Integer favoriteRank;

    private Metadata metadata;

    private transient String exifOrientation;

    private transient boolean exifOrientationCalculated;

    private static final int ATTR_IMAGE = 0;

    private static final int ATTR_VIDEO = 1;

    private static final int ATTR_SOUND = 2;

    private static final int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    static Collection<Integer> decodeFolderInfo(byte[] data) {
        Collection<Integer> folders = null;
        if (data == null) {
            return null;
        }
        for (int i = 0; i < data.length; i += 4) {
            int code = unsignedByteToInt(data[i]) | unsignedByteToInt(data[i + 1]) << 8 | unsignedByteToInt(data[i + 2]) << 16 | unsignedByteToInt(data[i + 3]) << 24;
            if (code == 0) break;
            if (folders == null) {
                folders = new HashSet();
            }
            folders.add(code);
        }
        return folders;
    }

    static byte[] encodeFolderInfo(Collection<Integer> folderIds) {
        byte[] bytes = new byte[128];
        int pos = 0;
        for (int folderId : folderIds) {
            bytes[pos++] = (byte) (folderId & 0x000000FFL);
            bytes[pos++] = (byte) ((folderId & 0x0000FF00L) >> 8);
            bytes[pos++] = (byte) ((folderId & 0x00FF0000L) >> 16);
            bytes[pos++] = (byte) ((folderId & 0xFF000000L) >> 24);
            if (pos + 4 >= bytes.length) {
                break;
            }
        }
        return bytes;
    }

    static Comparator ASCENDING_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            return o1 == null ? 1 : ((Comparable) o1).compareTo(o2);
        }
    };

    static Comparator DESCENDING_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            return o2 == null ? 1 : ((Comparable) o2).compareTo(o1);
        }
    };

    public static class PopularityComparator implements Comparator<Image> {

        private HitCounter hitCounter;

        public PopularityComparator(HitCounter hitCounter) {
            this.hitCounter = hitCounter;
        }

        public int compare(Image image1, Image image2) {
            if (this.hitCounter == null) {
                return 0;
            }
            HitCounter.Info hitInfo1 = this.hitCounter.hitInfoForImage(image1);
            HitCounter.Info hitInfo2 = this.hitCounter.hitInfoForImage(image2);
            return hitInfo1 == null ? -1 : -hitInfo1.compareTo(hitInfo2);
        }
    }

    /**
     * images with captions compare less than those without (in ascending order,
     * captionerd images show before those without)
     */
    public static class CaptionComparator implements Comparator<Image> {

        public int compare(Image image1, Image image2) {
            if (image1 == null) return -1;
            if (image2 == null) return 1;
            String caption1 = image1.getCaption();
            String caption2 = image2.getCaption();
            if (caption1 == null && caption2 == null) return 0;
            if (caption1 != null && caption2 != null) return 0;
            return caption1 == null ? 1 : -1;
        }
    }

    public static final CaptionComparator CAPTION_COMPARATOR = new CaptionComparator();

    public static class ImageDateTimeComparator implements Comparator<Image> {

        private Comparator dateComparator;

        private Comparator timeComparator;

        private String name;

        ImageDateTimeComparator(String name, Comparator dateComparator, Comparator timeComparator) {
            if (dateComparator == null && timeComparator == null) {
                throw new IllegalArgumentException("dateComparator or timeComparator required");
            }
            this.dateComparator = dateComparator;
            this.timeComparator = timeComparator;
            this.name = name;
        }

        public int compare(Image image1, Image image2) {
            int dateComparison = this.dateComparator == null ? 0 : this.dateComparator.compare(image1.imageDate, image2.imageDate);
            return dateComparison == 0 && this.timeComparator != null ? this.timeComparator.compare(image1.imageTime, image2.imageTime) : dateComparison;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class FavoriteComparator implements Comparator<Image> {

        private FolderRepository folderRepository;

        public FavoriteComparator(FolderRepository folderRepository) {
            this.folderRepository = folderRepository;
        }

        public int compare(Image image1, Image image2) {
            return image2.getFavoriteRank(this.folderRepository).compareTo(image1.getFavoriteRank(this.folderRepository));
        }
    }

    public static ImageDateTimeComparator DATE_DESCENDING_TIME_ASCENDING = new ImageDateTimeComparator("Date (Newest First)", DESCENDING_COMPARATOR, ASCENDING_COMPARATOR);

    public static ImageDateTimeComparator DATE_AND_TIME_ASCENDING = new ImageDateTimeComparator("Date (Oldest First)", null, ASCENDING_COMPARATOR);

    private static final Map<String, Comparator> IMAGE_SORT_ORDER_MAP = new LinkedHashMap();

    private static void registerImageDateTimeComparator(ImageDateTimeComparator comparator) {
        IMAGE_SORT_ORDER_MAP.put(comparator.getName(), comparator);
    }

    static {
        registerImageDateTimeComparator(DATE_DESCENDING_TIME_ASCENDING);
        registerImageDateTimeComparator(DATE_AND_TIME_ASCENDING);
    }

    public static Collection<String> getImageSortOrderNames() {
        return IMAGE_SORT_ORDER_MAP.keySet();
    }

    public static Comparator imageSortOrder(String name) {
        return IMAGE_SORT_ORDER_MAP.get(name);
    }

    public Image(int id, String caption, String notes, String mediaFullPath, String editedMediaFullPath, int attributes, byte[] folderInfo, int width, int height, Date imageDate, Date imageTime) {
        this.id = id;
        this.caption = caption;
        this.notes = notes;
        this.mediaFullPath = mediaFullPath;
        this.editedMediaFullPath = editedMediaFullPath;
        this.attributes = attributes;
        this.folderIds = decodeFolderInfo(folderInfo);
        this.mediaPixelHeight = height;
        this.mediaPixelWidth = width;
        this.imageDate = imageDate;
        this.imageTime = imageTime;
    }

    public int getId() {
        return this.id;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getMediaFullPath() {
        return StringUtils.isEmpty(this.editedMediaFullPath) ? this.mediaFullPath : this.editedMediaFullPath;
    }

    public synchronized long getSize() {
        if (this.size == null) {
            File file = new File(this.getMediaFullPath());
            this.size = file.exists() ? file.length() : -1;
        }
        return this.size;
    }

    public synchronized Metadata getMetadata() {
        if (this.metadata == null && this.isJpeg()) {
            File jpegFile = new File(this.getMediaFullPath());
            try {
                this.metadata = JpegMetadataReader.readMetadata(jpegFile);
            } catch (JpegProcessingException ex) {
                LOG.error("Cannot load metadata for " + this, ex);
            }
        }
        return this.metadata;
    }

    public Directory getExifDirectory() {
        Metadata metadata = this.getMetadata();
        if (metadata == null) {
            return null;
        }
        return metadata.getDirectory(ExifDirectory.class);
    }

    public TransposeType getTransposeType() {
        String orientation = this.getExifOrientation();
        if (orientation == null) {
            return null;
        }
        orientation = orientation.toLowerCase();
        if (orientation.startsWith("bottom, right side")) {
            return TransposeDescriptor.ROTATE_180;
        } else if (orientation.startsWith("right side, top")) {
            return TransposeDescriptor.ROTATE_90;
        } else if (orientation.startsWith("left side, bottom")) {
            return TransposeDescriptor.ROTATE_270;
        } else {
            return null;
        }
    }

    private String getExifOrientation() {
        if (!this.exifOrientationCalculated) {
            synchronized (this) {
                Directory exifDirectory = this.getExifDirectory();
                if (exifDirectory != null) {
                    try {
                        this.exifOrientation = exifDirectory.getDescription(ExifDirectory.TAG_ORIENTATION);
                    } catch (MetadataException ex) {
                        LOG.warn("Can't determine orientation", ex);
                    }
                    this.exifOrientationCalculated = true;
                }
            }
        }
        return this.exifOrientation;
    }

    public boolean isRotationRequired() {
        String orientation = this.getExifOrientation();
        return orientation != null && this.getTransposeType() != null;
    }

    public int getMediaPixelWidth() {
        return this.mediaPixelWidth;
    }

    public int getMediaPixelHeight() {
        return this.mediaPixelHeight;
    }

    public boolean inFolder(Folder folder) {
        return this.folderIds != null && folderIds.contains(new Integer(folder.getId()));
    }

    public void addFolder(Folder folder) {
        if (folder != null) {
            this.folderIds.add(folder.getId());
        }
    }

    public boolean isSound() {
        return this.attributes == ATTR_SOUND;
    }

    public boolean isImage() {
        return this.attributes == ATTR_IMAGE;
    }

    public boolean isVideo() {
        return this.attributes == ATTR_VIDEO;
    }

    public synchronized Folder findStackFolder(FolderRepository folderRepository) {
        if (!this.stackFolderCached) {
            for (int folderId : this.folderIds) {
                Folder folder = folderRepository.findTagById(folderId);
                if (folder.isStack()) {
                    this.stackFolder = folder;
                    break;
                }
            }
            this.stackFolderCached = true;
        }
        return this.stackFolder;
    }

    public synchronized ImportFolder findImportFolder(FolderRepository folderRepository) {
        if (!this.importFolderCached) {
            for (int folderId : this.folderIds) {
                Folder folder = folderRepository.findTagById(folderId);
                if (folder.isImport() && folder instanceof ImportFolder) {
                    this.importFolder = (ImportFolder) folder;
                    break;
                }
            }
            this.importFolderCached = true;
        }
        return this.importFolder;
    }

    public synchronized boolean isStacked(FolderRepository folderRepository) {
        if (this.stacked == null) {
            Folder stackFolder = this.findStackFolder(folderRepository);
            this.stacked = stackFolder != null && stackFolder.isStack();
        }
        return this.stacked;
    }

    public synchronized boolean isTopOfStack(FolderRepository folderRepository) {
        if (this.stackTop == null) {
            this.stackTop = this.isStacked(folderRepository) && this.findStackFolder(folderRepository).getStackTopImageId().intValue() == this.id;
        }
        return this.stackTop;
    }

    public synchronized boolean isHiddenInStack(FolderRepository folderRepository) {
        if (this.stackHidden == null) {
            this.stackHidden = this.isStacked(folderRepository) && this.findStackFolder(folderRepository).getStackTopImageId().intValue() != this.id;
        }
        return this.stackHidden;
    }

    public synchronized Integer getFavoriteRank(FolderRepository folderRepository) {
        if (this.favoriteRank == null) {
            for (int id : this.getFolderIds()) {
                Folder folder = folderRepository.findTagById(id);
                if (folder == null) {
                    continue;
                }
                if (folder.isFavoriteTag()) {
                    this.favoriteRank = folder.getFavoriteRank();
                    break;
                }
            }
            if (this.favoriteRank == null) {
                this.favoriteRank = 0;
            }
        }
        return this.favoriteRank;
    }

    public boolean equals(Object obj) {
        return obj instanceof Image && ((Image) obj).id == this.id;
    }

    public int hashCode() {
        return this.id;
    }

    public int compareTo(Object image) {
        return this.imageTime.compareTo(((Image) image).imageTime);
    }

    public String getMediaExtension() {
        return getMediaFullPath().substring(getMediaFullPath().lastIndexOf("."));
    }

    public String getMediaFullPathWithoutExtension() {
        return getMediaFullPath().substring(0, getMediaFullPath().lastIndexOf("."));
    }

    public Collection<Integer> getFolderIds() {
        return folderIds;
    }

    public Map<Integer, Set> rootTags(FolderRepository folderRepository) {
        Map<Integer, Set> rootCategoryMap = new TreeMap(Folder.FOLDER_COMPARATOR);
        for (Integer folderId : this.folderIds) {
            Folder folder = folderRepository.findTagById(folderId);
            if (folder.isTagOrCollection() && !folderRepository.isPrivate(folder)) {
                boolean favoriteStar = folder.isFavoriteStarTag();
                int rootId = favoriteStar ? -folder.getStars() : folder.getRootContainerId();
                Set tags = rootCategoryMap.get(rootId);
                if (tags == null) {
                    tags = new TreeSet(Folder.FOLDER_COMPARATOR);
                    rootCategoryMap.put(rootId, tags);
                }
                tags.add(folder.getName());
            }
        }
        return rootCategoryMap;
    }

    public Date getImageDate() {
        return imageDate;
    }

    public Date getImageTime() {
        return imageTime;
    }

    public String getMimeType() {
        return "image/jpeg";
    }

    private boolean isJpeg() {
        return true;
    }

    public String toString() {
        return this.id + ", folderIds: " + this.folderIds;
    }
}
