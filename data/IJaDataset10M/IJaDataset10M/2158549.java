package org.pixory.pxmodel;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixory.pxfoundation.PXFilePath;
import org.pixory.pxfoundation.PXStringUtility;

public class PXFileFilters extends Object {

    private static final Log LOG = LogFactory.getLog(PXFileFilters.class);

    /**
	 * the recognized image format names, which also are the file extensions
	 */
    public static final String[] IMAGE_FILE_FORMATS = { "jpg", "jpeg", "gif", "png" };

    /**
	 * the recognized archive format names, which also are the file extensions
	 */
    public static final String[] IMAGE_ARCHIVE_FORMATS = { "zip" };

    private static FileFilter _pxNodeFileFilter;

    private static FileFilter _pxDirectoryFileFilter;

    private static FileFilter _pxAlbumFileFilter;

    private static FileFilter _pxImageFileFilter;

    private static FileFilter _pxArchiveFileFilter;

    private static FileFilter _pxNoneFileFilter;

    private static String[] _ignoreDirNames;

    private static Set _acceptedImageFileExtensions;

    private static Set _acceptedArchiveFileExtensions;

    private PXFileFilters() {
    }

    public static void setIgnoreDirNames(String[] ignoreDirNames) {
        if (ignoreDirNames != null) {
            StringBuffer aBuffer = new StringBuffer();
            aBuffer.append("(");
            for (int i = 0; i < ignoreDirNames.length; i++) {
                aBuffer.append(ignoreDirNames[i]);
                if (i < (ignoreDirNames.length - 1)) {
                    aBuffer.append(",");
                }
            }
            aBuffer.append(")");
            LOG.debug(aBuffer);
        }
        _ignoreDirNames = ignoreDirNames;
        _pxNodeFileFilter = null;
    }

    /**
	 * @return shared instance
	 */
    public static FileFilter getPXNodeFileFilter() {
        if (_pxNodeFileFilter == null) {
            _pxNodeFileFilter = new PXNodeFileFilter(_ignoreDirNames);
        }
        return _pxNodeFileFilter;
    }

    /**
	 * @return shared instance
	 */
    public static FileFilter getPXDirectoryFileFilter() {
        if (_pxDirectoryFileFilter == null) {
            _pxDirectoryFileFilter = new PXDirectoryFileFilter();
        }
        return _pxDirectoryFileFilter;
    }

    /**
	 * @return shared instance
	 */
    public static FileFilter getPXAlbumFileFilter() {
        if (_pxAlbumFileFilter == null) {
            _pxAlbumFileFilter = new PXAlbumFileFilter();
        }
        return _pxAlbumFileFilter;
    }

    public static FileFilter getPXImageFileFilter() {
        if (_pxImageFileFilter == null) {
            _pxImageFileFilter = new PXImageFileFilter();
        }
        return _pxImageFileFilter;
    }

    public static FileFilter getPXArchiveFileFilter() {
        if (_pxArchiveFileFilter == null) {
            _pxArchiveFileFilter = new PXArchiveFileFilter();
        }
        return _pxArchiveFileFilter;
    }

    public static FileFilter getPXNoneFileFilter() {
        if (_pxNoneFileFilter == null) {
            _pxNoneFileFilter = new PXNoneFileFilter();
        }
        return _pxNoneFileFilter;
    }

    public static boolean isAlbum(PXFilePath filePath) {
        boolean isAlbum = false;
        if (filePath != null) {
            FileFilter anAlbumFileFilter = getPXAlbumFileFilter();
            if (filePath.isAcceptedBy(anAlbumFileFilter)) {
                isAlbum = true;
            }
        }
        return isAlbum;
    }

    public static boolean isAlbum(File file_) {
        return getPXAlbumFileFilter().accept(file_);
    }

    public static boolean isNode(File file_) {
        return getPXNodeFileFilter().accept(file_);
    }

    public static boolean isDirectory(File file_) {
        return getPXDirectoryFileFilter().accept(file_);
    }

    /**
	 * a 'node' is a directory that is not hidden and does not start with ".". A
	 * branch node is a "Directory", i.e. can be traversed by an application to
	 * eventually reach an album. A leaf node is an "Album".
	 */
    private static class PXNodeFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(PXNodeFileFilter.class);

        private final String[] _ignoreDirectoryNames;

        private PXNodeFileFilter(String[] ignoreDirectoryNames) {
            _ignoreDirectoryNames = ignoreDirectoryNames;
        }

        public boolean accept(File file) {
            boolean accept = false;
            if (file != null) {
                if (file.isDirectory() && !file.isHidden()) {
                    String aDirectoryName = file.getName();
                    if ((!aDirectoryName.startsWith(".")) && (!PXStringUtility.contains(_ignoreDirectoryNames, aDirectoryName))) {
                        accept = true;
                    }
                }
            }
            return accept;
        }
    }

    /** 
	 * A Directory is a Node which has Node children 
	 */
    private static class PXDirectoryFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(PXDirectoryFileFilter.class);

        private PXDirectoryFileFilter() {
        }

        public boolean accept(File file) {
            boolean accept = false;
            if (file != null) {
                if (isNode(file)) {
                    File[] someFiles = file.listFiles();
                    if (someFiles != null) {
                        for (int i = 0; i < someFiles.length; i++) {
                            if (isNode(someFiles[i])) {
                                accept = true;
                                break;
                            }
                        }
                    }
                }
            }
            return accept;
        }
    }

    /** 
	 * An Album is a Node which has *no* Node children 
	 */
    private static class PXAlbumFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(PXAlbumFileFilter.class);

        private PXAlbumFileFilter() {
        }

        public boolean accept(File file) {
            boolean accept = false;
            if (file != null) {
                if (isNode(file)) {
                    accept = true;
                    File[] someFiles = file.listFiles();
                    if (someFiles != null) {
                        for (int i = 0; i < someFiles.length; i++) {
                            if (isNode(someFiles[i])) {
                                accept = false;
                                break;
                            }
                        }
                    }
                }
            }
            return accept;
        }
    }

    private static class PXImageFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(PXImageFileFilter.class);

        public boolean accept(File file) {
            boolean accept = false;
            if ((file != null) && (file.isFile())) {
                String aLowerFileName = file.getName().toLowerCase();
                Set theAcceptedExtensions = getAcceptedImageFileExtensions();
                Iterator anExtensionIterator = theAcceptedExtensions.iterator();
                while (anExtensionIterator.hasNext()) {
                    String anExtension = (String) anExtensionIterator.next();
                    if (aLowerFileName.endsWith(anExtension)) {
                        accept = true;
                        break;
                    }
                }
            }
            return accept;
        }
    }

    private static class PXArchiveFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(PXArchiveFileFilter.class);

        public boolean accept(File file) {
            boolean accept = false;
            if (file != null) {
                String aLowerFileName = file.getName().toLowerCase();
                Set theAcceptedExtensions = getAcceptedArchiveFileExtensions();
                Iterator anExtensionIterator = theAcceptedExtensions.iterator();
                while (anExtensionIterator.hasNext()) {
                    String anExtension = (String) anExtensionIterator.next();
                    if (aLowerFileName.endsWith(anExtension)) {
                        accept = true;
                        break;
                    }
                }
            }
            return accept;
        }
    }

    private static class PXNoneFileFilter extends Object implements FileFilter {

        private static final Log LOG = LogFactory.getLog(PXNoneFileFilter.class);

        public boolean accept(File file) {
            return false;
        }
    }

    /**
	 * based on IMAGE_FILE_FORMATS
	 */
    private static Set getAcceptedImageFileExtensions() {
        if (_acceptedImageFileExtensions == null) {
            _acceptedImageFileExtensions = new HashSet();
            for (int i = 0; i < IMAGE_FILE_FORMATS.length; i++) {
                _acceptedImageFileExtensions.add(IMAGE_FILE_FORMATS[i].toLowerCase());
            }
        }
        return _acceptedImageFileExtensions;
    }

    /**
	 * based on IMAGE_ARCHIVE_FORMATS
	 */
    private static Set getAcceptedArchiveFileExtensions() {
        if (_acceptedArchiveFileExtensions == null) {
            _acceptedArchiveFileExtensions = new HashSet();
            for (int i = 0; i < IMAGE_ARCHIVE_FORMATS.length; i++) {
                _acceptedArchiveFileExtensions.add(IMAGE_ARCHIVE_FORMATS[i].toLowerCase());
            }
        }
        return _acceptedArchiveFileExtensions;
    }
}
