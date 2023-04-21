package com.liferay.documentlibrary.util;

import com.liferay.documentlibrary.DuplicateDirectoryException;
import com.liferay.documentlibrary.DuplicateFileException;
import com.liferay.documentlibrary.NoSuchDirectoryException;
import com.liferay.documentlibrary.NoSuchFileException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.lucene.LuceneUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

/**
 * <a href="FileSystemHook.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Sten Martinez
 *
 */
public class FileSystemHook extends BaseHook {

    public FileSystemHook() {
        _rootDir = new File(_ROOT_DIR);
        if (!_rootDir.exists()) {
            _rootDir.mkdirs();
        }
    }

    public void addDirectory(long companyId, long repositoryId, String dirName) throws PortalException, SystemException {
        File dirNameDir = getDirNameDir(companyId, repositoryId, dirName);
        if (dirNameDir.exists()) {
            throw new DuplicateDirectoryException();
        }
        dirNameDir.mkdirs();
    }

    public void addFile(long companyId, String portletId, long groupId, long repositoryId, String fileName, String properties, InputStream is) throws PortalException, SystemException {
        try {
            File fileNameVersionFile = getFileNameVersionFile(companyId, repositoryId, fileName, DEFAULT_VERSION);
            if (fileNameVersionFile.exists()) {
                throw new DuplicateFileException();
            }
            FileUtil.write(fileNameVersionFile, is);
            Indexer.addFile(companyId, portletId, groupId, repositoryId, properties, fileName);
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    public void checkRoot(long companyId) throws SystemException {
    }

    public void deleteDirectory(long companyId, String portletId, long repositoryId, String dirName) throws PortalException, SystemException {
        File dirNameDir = getDirNameDir(companyId, repositoryId, dirName);
        if (!dirNameDir.exists()) {
            throw new NoSuchDirectoryException();
        }
        FileUtil.deltree(dirNameDir);
    }

    public void deleteFile(long companyId, String portletId, long repositoryId, String fileName) throws PortalException, SystemException {
        try {
            File fileNameDir = getFileNameDir(companyId, repositoryId, fileName);
            if (!fileNameDir.exists()) {
                throw new NoSuchFileException();
            }
            FileUtil.deltree(fileNameDir);
            Indexer.deleteFile(companyId, portletId, repositoryId, fileName);
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    public void deleteFile(long companyId, String portletId, long repositoryId, String fileName, double versionNumber) throws PortalException, SystemException {
        File fileNameVersionFile = getFileNameVersionFile(companyId, repositoryId, fileName, versionNumber);
        if (!fileNameVersionFile.exists()) {
            throw new NoSuchFileException();
        }
        fileNameVersionFile.delete();
    }

    public InputStream getFileAsStream(long companyId, long repositoryId, String fileName, double versionNumber) throws PortalException, SystemException {
        try {
            if (versionNumber == 0) {
                versionNumber = getHeadVersionNumber(companyId, repositoryId, fileName);
            }
            File fileNameVersionFile = getFileNameVersionFile(companyId, repositoryId, fileName, versionNumber);
            if (!fileNameVersionFile.exists()) {
                throw new NoSuchFileException();
            }
            return new FileInputStream(fileNameVersionFile);
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    public String[] getFileNames(long companyId, long repositoryId, String dirName) throws PortalException, SystemException {
        try {
            File dirNameDir = getDirNameDir(companyId, repositoryId, dirName);
            if (!dirNameDir.exists()) {
                throw new NoSuchDirectoryException();
            }
            String[] fileNames = FileUtil.listDirs(dirNameDir);
            Arrays.sort(fileNames);
            for (int i = 0; i < fileNames.length; i++) {
                fileNames[i] = StringPool.SLASH + dirName + StringPool.SLASH + fileNames[i];
            }
            return fileNames;
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    public long getFileSize(long companyId, long repositoryId, String fileName) throws PortalException, SystemException {
        try {
            double versionNumber = getHeadVersionNumber(companyId, repositoryId, fileName);
            File fileNameVersionFile = getFileNameVersionFile(companyId, repositoryId, fileName, versionNumber);
            if (!fileNameVersionFile.exists()) {
                throw new NoSuchFileException();
            }
            return fileNameVersionFile.length();
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    public boolean hasFile(long companyId, long repositoryId, String fileName, double versionNumber) throws PortalException, SystemException {
        File fileNameVersionFile = getFileNameVersionFile(companyId, repositoryId, fileName, versionNumber);
        if (fileNameVersionFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void move(String srcDir, String destDir) throws SystemException {
    }

    public void reIndex(String[] ids) throws SearchException {
        long companyId = GetterUtil.getLong(ids[0]);
        String portletId = ids[1];
        long groupId = GetterUtil.getLong(ids[2]);
        long repositoryId = GetterUtil.getLong(ids[3]);
        IndexWriter writer = null;
        try {
            writer = LuceneUtil.getWriter(companyId);
            File repistoryDir = getRepositoryDir(companyId, repositoryId);
            String[] fileNames = FileUtil.listDirs(repistoryDir);
            for (int i = 0; i < fileNames.length; i++) {
                String fileName = fileNames[i];
                try {
                    Document doc = IndexerImpl.getAddFileDocument(companyId, portletId, groupId, repositoryId, fileName);
                    writer.addDocument(doc);
                } catch (Exception e) {
                    _log.error("Reindexing " + fileName, e);
                }
            }
        } catch (IOException ioe) {
            throw new SearchException(ioe);
        } finally {
            try {
                if (writer != null) {
                    LuceneUtil.write(companyId);
                }
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }

    public void updateFile(long companyId, String portletId, long groupId, long repositoryId, String fileName, double versionNumber, String sourceFileName, String properties, InputStream is) throws PortalException, SystemException {
        try {
            File fileNameVersionFile = getFileNameVersionFile(companyId, repositoryId, fileName, versionNumber);
            if (fileNameVersionFile.exists()) {
                throw new DuplicateFileException();
            }
            FileUtil.write(fileNameVersionFile, is);
            Indexer.updateFile(companyId, portletId, groupId, repositoryId, properties, fileName);
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    public void updateFile(long companyId, String portletId, long groupId, long repositoryId, long newRepositoryId, String fileName) throws PortalException, SystemException {
        try {
            File fileNameDir = getFileNameDir(companyId, repositoryId, fileName);
            File newFileNameDir = getFileNameDir(companyId, newRepositoryId, fileName);
            FileUtil.copyDirectory(fileNameDir, newFileNameDir);
            FileUtil.deltree(fileNameDir);
            try {
                Indexer.deleteFile(companyId, portletId, repositoryId, fileName);
            } catch (IOException ioe) {
            }
            Indexer.addFile(companyId, portletId, groupId, newRepositoryId, fileName);
        } catch (IOException ioe) {
            throw new SystemException();
        }
    }

    protected File getCompanyDir(long companyId) {
        File companyDir = new File(_rootDir + StringPool.SLASH + companyId);
        if (!companyDir.exists()) {
            companyDir.mkdirs();
        }
        return companyDir;
    }

    protected File getDirNameDir(long companyId, long repositoryId, String dirName) {
        return getFileNameDir(companyId, repositoryId, dirName);
    }

    protected File getRepositoryDir(long companyId, long repositoryId) {
        File companyDir = getCompanyDir(companyId);
        File repositoryDir = new File(companyDir + StringPool.SLASH + repositoryId);
        if (!repositoryDir.exists()) {
            repositoryDir.mkdirs();
        }
        return repositoryDir;
    }

    protected File getFileNameDir(long companyId, long repositoryId, String fileName) {
        File repositoryDir = getRepositoryDir(companyId, repositoryId);
        File fileNameDir = new File(repositoryDir + StringPool.SLASH + fileName);
        return fileNameDir;
    }

    protected File getFileNameVersionFile(long companyId, long repositoryId, String fileName, double version) {
        File fileNameDir = getFileNameDir(companyId, repositoryId, fileName);
        File fileNameVersionFile = new File(fileNameDir + StringPool.SLASH + version);
        return fileNameVersionFile;
    }

    protected double getHeadVersionNumber(long companyId, long repositoryId, String fileName) throws IOException {
        File fileNameDir = getFileNameDir(companyId, repositoryId, fileName);
        if (!fileNameDir.exists()) {
            return DEFAULT_VERSION;
        }
        String[] versionNumbers = FileUtil.listFiles(fileNameDir);
        double headVersionNumber = DEFAULT_VERSION;
        for (int i = 0; i < versionNumbers.length; i++) {
            double versionNumber = GetterUtil.getDouble(versionNumbers[i]);
            if (versionNumber > headVersionNumber) {
                headVersionNumber = versionNumber;
            }
        }
        return headVersionNumber;
    }

    private static final String _ROOT_DIR = PropsUtil.get(PropsUtil.DL_HOOK_FILE_SYSTEM_ROOT_DIR);

    private static Log _log = LogFactory.getLog(FileSystemHook.class);

    private File _rootDir;
}
