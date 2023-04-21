package fi.foyt.hibernate.gae.search;

import java.io.Serializable;
import java.util.List;
import fi.foyt.hibernate.gae.search.persistence.dao.FileDAO;
import fi.foyt.hibernate.gae.search.persistence.dao.FileSegmentDAO;
import fi.foyt.hibernate.gae.search.persistence.domainmodel.File;
import fi.foyt.hibernate.gae.search.persistence.domainmodel.FileSegment;

public class GaeFile implements Serializable {

    public static final int SEGMENT_SIZE = 500;

    private static final long serialVersionUID = 1l;

    public GaeFile(String fileName, GaeDirectory directory) {
        this.fileName = fileName;
        this.directory = directory;
    }

    public synchronized long getLength() {
        return getFile().getDataLength();
    }

    public synchronized long getLastModified() {
        return getFile().getModified();
    }

    protected synchronized void setLastModified(long lastModified) {
        FileDAO fileDAO = new FileDAO();
        File file = getFile();
        fileDAO.updateModified(file, lastModified);
    }

    protected synchronized void resetFile() {
        FileDAO fileDAO = new FileDAO();
        FileSegmentDAO fileSegmentDAO = new FileSegmentDAO();
        File file = getFile();
        if (file != null) {
            List<FileSegment> segments = fileSegmentDAO.listByFile(file);
            for (FileSegment segment : segments) {
                fileSegmentDAO.delete(segment);
            }
            fileDAO.updateDataLength(file, 0l);
        }
    }

    public void updateLength(long length) {
        FileDAO fileDAO = new FileDAO();
        fileDAO.updateDataLength(getFile(), length);
    }

    protected synchronized int getFileSegmentsCount() {
        FileSegmentDAO fileSegmentDAO = new FileSegmentDAO();
        return fileSegmentDAO.countByFile(getFile());
    }

    protected synchronized FileSegment getFileSegment(int index) {
        FileSegmentDAO fileSegmentDAO = new FileSegmentDAO();
        return fileSegmentDAO.findByFileAndSegmentNo(getFile(), index);
    }

    protected synchronized FileSegment getNewSegment() {
        int newIndex = getFileSegmentsCount();
        FileSegmentDAO fileSegmentDAO = new FileSegmentDAO();
        return fileSegmentDAO.create(getFile(), new Long(newIndex), null);
    }

    private synchronized File getFile() {
        FileDAO fileDAO = new FileDAO();
        File file = fileDAO.findByDirectoryAndName(directory.getDirectory(), fileName);
        if (file == null) {
            file = fileDAO.create(directory.getDirectory(), fileName, 0l, System.currentTimeMillis());
        }
        return file;
    }

    private String fileName;

    private GaeDirectory directory;
}
