package net.dongliu.jalus.service;

import java.util.Collections;
import java.util.List;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import net.dongliu.jalus.dao.FileDAO;
import net.dongliu.jalus.dao.FileIndexDAO;
import net.dongliu.jalus.model.File;
import net.dongliu.jalus.model.FileIndex;
import net.dongliu.jalus.utils.PojoHandler;

public class FileService {

    private FileDAO fileDAO;

    private FileIndexDAO fileIndexDAO;

    private Cache cache;

    private static class Holder {

        private static FileService instance = new FileService();
    }

    private FileService() {
        fileDAO = new FileDAO();
        fileIndexDAO = new FileIndexDAO();
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
            cache = null;
        }
    }

    public static FileService getInstance() {
        return Holder.instance;
    }

    @SuppressWarnings("unchecked")
    public net.dongliu.jalus.pojo.File getFileById(Long id) {
        String key = "getFileById@" + id;
        if (!cache.containsKey(key)) {
            cache.put(key, PojoHandler.po2Vo(fileDAO.getFileById(id)));
        }
        return (net.dongliu.jalus.pojo.File) cache.get(key);
    }

    public Long add(File file) {
        Long fileId = fileDAO.add(file);
        FileIndex fileIndex = new FileIndex(file.getFileName(), file.getMimeType(), file.getDate(), fileId, file.getContent().getBytes().length);
        fileIndexDAO.add(fileIndex);
        return fileId;
    }

    @SuppressWarnings("unchecked")
    public List<net.dongliu.jalus.pojo.File> getAllFileList() {
        List<net.dongliu.jalus.pojo.File> fileList = PojoHandler.po2VoList(fileDAO.getAllFile());
        return fileList;
    }

    public List<FileIndex> getAllFileIndexList() {
        List<FileIndex> fileList = fileIndexDAO.getAllFile();
        return fileList;
    }

    @SuppressWarnings("unchecked")
    public List<net.dongliu.jalus.pojo.FileIndex> getFileListByRange(int startIndex, int endIndex) {
        List<net.dongliu.jalus.pojo.FileIndex> fileList = PojoHandler.po2VoList(fileDAO.getRangeFile(startIndex, endIndex));
        return fileList;
    }

    public List<FileIndex> getFileIndexListByRange(int startIndex, int endIndex) {
        List<FileIndex> fileList = fileIndexDAO.getRangeFile(startIndex, endIndex);
        return fileList;
    }

    public void deleteFile(long fileIndexId) {
        Long fileId = fileIndexDAO.deleteFileById(fileIndexId);
        fileDAO.deleteFileById(fileId);
        cache.clear();
    }

    /**
	 * 返回文件总数
	 * @return
	 */
    public int getFileNum() {
        return getAllFileIndexList().size();
    }

    /**
	 * 重新创建文件索引表
	 */
    public void buildFileIndex() {
        List<File> fileList = fileDAO.getAllFile();
        for (File file : fileList) {
            FileIndex fileIndex = new FileIndex(file.getFileName(), file.getMimeType(), file.getDate(), file.getId(), file.getContent().getBytes().length);
            fileIndexDAO.add(fileIndex);
        }
    }

    public void clearCache() {
        cache.clear();
    }
}
