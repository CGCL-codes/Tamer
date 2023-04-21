package com.liferay.documentlibrary.util;

import com.liferay.documentlibrary.NoSuchFileException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.lucene.LuceneUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.FileUtil;
import com.liferay.util.SystemProperties;
import com.liferay.util.servlet.ServletResponseUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.id.uuid.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

/**
 * <a href="S3Hook.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Sten Martinez
 *
 */
public class S3Hook extends BaseHook {

    public S3Hook() {
        try {
            _s3Service = getS3Service();
            _s3Bucket = getS3Bucket();
        } catch (S3ServiceException s3se) {
            _log.error(s3se.getMessage());
        }
    }

    public void addDirectory(long companyId, long repositoryId, String dirName) throws PortalException, SystemException {
    }

    public void addFile(long companyId, String portletId, long groupId, long repositoryId, String fileName, String properties, InputStream is) throws PortalException, SystemException {
        try {
            S3Object s3Object = new S3Object(_s3Bucket, getKey(companyId, repositoryId, fileName, DEFAULT_VERSION));
            s3Object.setDataInputStream(is);
            _s3Service.putObject(_s3Bucket, s3Object);
            Indexer.addFile(companyId, portletId, groupId, repositoryId, fileName, properties);
        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public void checkRoot(long companyId) throws SystemException {
    }

    public void deleteDirectory(long companyId, String portletId, long repositoryId, String dirName) throws PortalException, SystemException {
        try {
            S3Object[] s3Objects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId, dirName), null);
            for (int i = 0; i < s3Objects.length; i++) {
                S3Object s3Object = s3Objects[i];
                _s3Service.deleteObject(_s3Bucket, s3Object.getKey());
            }
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public void deleteFile(long companyId, String portletId, long repositoryId, String fileName) throws PortalException, SystemException {
        try {
            S3Object[] s3Objects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId, fileName), null);
            for (int i = 0; i < s3Objects.length; i++) {
                S3Object s3Object = s3Objects[i];
                _s3Service.deleteObject(_s3Bucket, s3Object.getKey());
            }
            Indexer.deleteFile(companyId, portletId, repositoryId, fileName);
        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public void deleteFile(long companyId, String portletId, long repositoryId, String fileName, double versionNumber) throws PortalException, SystemException {
        try {
            _s3Service.deleteObject(_s3Bucket, getKey(companyId, repositoryId, fileName, versionNumber));
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public InputStream getFileAsStream(long companyId, long repositoryId, String fileName, double versionNumber) throws PortalException, SystemException {
        try {
            if (versionNumber == 0) {
                versionNumber = getHeadVersionNumber(companyId, repositoryId, fileName);
            }
            S3Object s3Object = _s3Service.getObject(_s3Bucket, getKey(companyId, repositoryId, fileName, versionNumber));
            return s3Object.getDataInputStream();
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public String[] getFileNames(long companyId, long repositoryId, String dirName) throws PortalException, SystemException {
        try {
            List list = new ArrayList();
            S3Object[] s3Objects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId, dirName), null);
            for (int i = 0; i < s3Objects.length; i++) {
                S3Object s3Object = s3Objects[i];
                String key = s3Object.getKey();
                int x = key.indexOf(StringPool.SLASH);
                x = key.indexOf(StringPool.SLASH, x + 1);
                int y = key.lastIndexOf(StringPool.SLASH);
                list.add(key.substring(x, y));
            }
            return (String[]) list.toArray(new String[0]);
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public long getFileSize(long companyId, long repositoryId, String fileName) throws PortalException, SystemException {
        try {
            double versionNumber = getHeadVersionNumber(companyId, repositoryId, fileName);
            S3Object objectDetails = _s3Service.getObjectDetails(_s3Bucket, getKey(companyId, repositoryId, fileName, versionNumber));
            return objectDetails.getContentLength();
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public boolean hasFile(long companyId, long repositoryId, String fileName, double versionNumber) throws PortalException, SystemException {
        try {
            S3Object[] s3Objects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId, fileName, versionNumber), null);
            if (s3Objects.length == 0) {
                return false;
            } else {
                return true;
            }
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
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
            S3Object[] searchObjects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId), null);
            Set fileNameSet = new HashSet();
            for (int i = 0; i < searchObjects.length; i++) {
                S3Object currentObject = searchObjects[i];
                String fileName = (String) getFileName(currentObject.getKey());
                fileNameSet.add(fileName);
            }
            Iterator itr = fileNameSet.iterator();
            while (itr.hasNext()) {
                String fileName = (String) itr.next();
                try {
                    Document doc = IndexerImpl.getAddFileDocument(companyId, portletId, groupId, repositoryId, fileName);
                    writer.addDocument(doc);
                } catch (Exception e) {
                    _log.error("Reindexing " + fileName, e);
                }
            }
        } catch (IOException ioe) {
            throw new SearchException(ioe);
        } catch (S3ServiceException s3se) {
            throw new SearchException(s3se);
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
            S3Object s3Object = new S3Object(_s3Bucket, getKey(companyId, repositoryId, fileName, versionNumber));
            s3Object.setDataInputStream(is);
            _s3Service.putObject(_s3Bucket, s3Object);
            Indexer.updateFile(companyId, portletId, groupId, repositoryId, fileName, properties);
        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    public void updateFile(long companyId, String portletId, long groupId, long repositoryId, long newRepositoryId, String fileName) throws PortalException, SystemException {
        try {
            S3Object[] s3Objects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId, fileName), null);
            for (int i = 0; i < s3Objects.length; i++) {
                S3Object oldS3Object = s3Objects[i];
                String oldKey = oldS3Object.getKey();
                oldS3Object = _s3Service.getObject(_s3Bucket, oldKey);
                File tempFile = new File(SystemProperties.get(SystemProperties.TMP_DIR) + File.separator + UUID.timeUUID());
                InputStream is = null;
                try {
                    is = oldS3Object.getDataInputStream();
                    FileUtil.write(tempFile, is);
                } catch (Exception e) {
                } finally {
                    ServletResponseUtil.cleanUp(is);
                }
                is = new FileInputStream(tempFile);
                String newPrefix = getKey(companyId, newRepositoryId);
                int x = oldKey.indexOf(StringPool.SLASH);
                x = oldKey.indexOf(StringPool.SLASH, x + 1);
                String newKey = newPrefix + oldKey.substring(x + 1, oldKey.length());
                S3Object newS3Object = new S3Object(_s3Bucket, newKey);
                newS3Object.setDataInputStream(is);
                _s3Service.putObject(_s3Bucket, newS3Object);
                _s3Service.deleteObject(_s3Bucket, oldKey);
                FileUtil.delete(tempFile);
            }
            Indexer.deleteFile(companyId, portletId, repositoryId, fileName);
            Indexer.addFile(companyId, portletId, groupId, newRepositoryId, fileName);
        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } catch (S3ServiceException s3se) {
            throw new SystemException(s3se);
        }
    }

    protected AWSCredentials getAWSCredentials() throws S3ServiceException {
        if (Validator.isNull(_ACCESS_KEY) || Validator.isNull(_SECRET_KEY)) {
            throw new S3ServiceException("S3 access and secret keys are not set");
        } else {
            return new AWSCredentials(_ACCESS_KEY, _SECRET_KEY);
        }
    }

    protected Object getFileName(String key) {
        int x = key.indexOf(StringPool.SLASH);
        x = key.indexOf(StringPool.SLASH, x + 1);
        int y = key.lastIndexOf(StringPool.SLASH);
        return key.substring(x + 1, y);
    }

    protected double getHeadVersionNumber(long companyId, long repositoryId, String fileName) throws PortalException, S3ServiceException {
        S3Object[] s3Objects = _s3Service.listObjects(_s3Bucket, getKey(companyId, repositoryId, fileName), null);
        String[] keys = new String[s3Objects.length];
        for (int i = 0; i < s3Objects.length; i++) {
            S3Object s3Object = s3Objects[i];
            keys[i] = s3Object.getKey();
        }
        if (keys.length > 0) {
            Arrays.sort(keys);
            String headKey = keys[keys.length - 1];
            int x = headKey.lastIndexOf(StringPool.SLASH);
            return GetterUtil.getDouble(headKey.substring(x + 1, headKey.length()));
        } else {
            throw new NoSuchFileException(fileName);
        }
    }

    protected String getKey(long companyId, long repositoryId) {
        StringMaker sm = new StringMaker();
        sm.append(companyId);
        sm.append(StringPool.SLASH);
        sm.append(repositoryId);
        sm.append(StringPool.SLASH);
        return sm.toString();
    }

    protected String getKey(long companyId, long repositoryId, String fileName) {
        StringMaker sm = new StringMaker();
        sm.append(companyId);
        sm.append(StringPool.SLASH);
        sm.append(repositoryId);
        sm.append(StringPool.SLASH);
        sm.append(fileName);
        sm.append(StringPool.SLASH);
        return sm.toString();
    }

    protected String getKey(long companyId, long repositoryId, String fileName, double versionNumber) {
        StringMaker sm = new StringMaker();
        sm.append(companyId);
        sm.append(StringPool.SLASH);
        sm.append(repositoryId);
        sm.append(StringPool.SLASH);
        sm.append(fileName);
        sm.append(StringPool.SLASH);
        sm.append(versionNumber);
        return sm.toString();
    }

    protected S3Bucket getS3Bucket() throws S3ServiceException {
        if (Validator.isNull(_BUCKET_NAME)) {
            throw new S3ServiceException("S3 bucket name is not set");
        } else {
            return getS3Service().createBucket(_BUCKET_NAME);
        }
    }

    protected S3Service getS3Service() throws S3ServiceException {
        AWSCredentials credentials = getAWSCredentials();
        return new RestS3Service(credentials);
    }

    private static final String _ACCESS_KEY = PropsUtil.get(PropsUtil.DL_HOOK_S3_ACCESS_KEY);

    private static final String _SECRET_KEY = PropsUtil.get(PropsUtil.DL_HOOK_S3_SECRET_KEY);

    private static final String _BUCKET_NAME = PropsUtil.get(PropsUtil.DL_HOOK_S3_BUCKET_NAME);

    private static Log _log = LogFactory.getLog(S3Hook.class);

    private S3Bucket _s3Bucket;

    private S3Service _s3Service;
}
