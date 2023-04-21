package de.excrawler.server.FilesManagement;

import de.excrawler.server.Imagecrawler.ImageTools;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * represents an image file
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class ImageFile extends Thread implements ExCFile {

    private FileManager _filemanager;

    private long _checksum;

    private File _file;

    private long _size;

    private String _formatName;

    private int _format;

    private String _FileNamePath;

    private int _width;

    private int _height;

    private int _type;

    private String _typeName;

    private ImageFile _Thumbnail;

    private BufferedImage _image;

    private int _ID;

    public ImageFile() {
        super();
        this._filemanager = new FileManager();
        this._checksum = 0;
        this._file = null;
        this._size = 0;
        this._formatName = "Unknown";
        this._format = ImageTools.UNKNOWN;
        this._FileNamePath = "";
        this._width = 1;
        this._height = 1;
        this._type = BufferedImage.TYPE_CUSTOM;
        this._typeName = "";
        this._image = null;
        this._ID = 0;
    }

    public ImageFile(File file, String filename) throws Exception {
        super(filename);
        this._filemanager = new FileManager();
        this._FileNamePath = filename;
        this._checksum = this._filemanager.checksum(this._FileNamePath);
        this._file = file;
        this._size = this._file.length();
        this._formatName = "Unknown";
        this._format = ImageTools.UNKNOWN;
        this._width = 0;
        this._height = 0;
        this._type = BufferedImage.TYPE_CUSTOM;
        this._typeName = "";
        this._image = null;
        this._ID = 0;
    }

    public void setImage(BufferedImage value) {
        this._image = value;
    }

    public BufferedImage getImage() {
        return this._image;
    }

    public void setThumbnail(ImageFile value) {
        this._Thumbnail = value;
    }

    public ImageFile getThumbnail() {
        if (this._Thumbnail == null) this._Thumbnail = new ImageFile();
        return this._Thumbnail;
    }

    public void setChecksum(long value) {
        this._checksum = value;
    }

    public long getChecksum() {
        return this._checksum;
    }

    public void setFile(File value) throws Exception {
        this._file = value;
        this._size = this._file.length();
    }

    public File getFile() {
        return this._file;
    }

    public void setType(int value) {
        this._type = value;
    }

    public int getType() {
        return this._type;
    }

    public void setTypeName(String value) {
        this._typeName = value;
    }

    public String getTypeName() {
        return this._typeName;
    }

    public void setWidth(int value) {
        this._width = value;
    }

    public int Width() {
        return this._width;
    }

    public void setHeight(int value) {
        this._height = value;
    }

    public int Height() {
        return this._height;
    }

    public void setSize(long value) {
        this._size = value;
    }

    public long getSize() {
        return this._size;
    }

    public void setFormatName(String value) {
        this._formatName = value;
    }

    public String getFormatName() {
        return this._formatName;
    }

    public void setFormat(int value) {
        this._format = value;
    }

    public int getFormat() {
        return this._format;
    }

    public void setFileName(String value) throws Exception {
        this._FileNamePath = value;
        this._checksum = this._filemanager.checksum(this._FileNamePath);
    }

    public String getFileName() {
        return this._FileNamePath;
    }

    public int getID() {
        return this._ID;
    }

    public void setID(int value) {
        this._ID = value;
    }

    public boolean delete() {
        return this._file.delete();
    }
}
