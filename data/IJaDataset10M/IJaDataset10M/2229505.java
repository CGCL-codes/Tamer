package org.vosao.dao.impl;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FileChunkDao;
import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;
import org.vosao.utils.ArrayUtil;
import com.google.appengine.api.datastore.Query;

public class FileChunkDaoImpl extends BaseDaoImpl<FileChunkEntity> implements FileChunkDao {

    public FileChunkDaoImpl() {
        super(FileChunkEntity.class);
    }

    @Override
    public void removeByFile(final Long fileId) {
        if (fileId == null) {
            return;
        }
        Query q = newQuery();
        q.addFilter("fileId", EQUAL, fileId);
        removeSelected(q);
    }

    @Override
    public void save(FileEntity file, byte[] content) {
        removeByFile(file.getId());
        List<FileChunkEntity> chunks = createChunks(file, content);
        for (FileChunkEntity chunk : chunks) {
            save(chunk);
        }
    }

    public static int ENTITY_SIZE_LIMIT = 1000000;

    @Override
    public List<FileChunkEntity> createChunks(FileEntity file, byte[] content) {
        List<FileChunkEntity> result = new ArrayList<FileChunkEntity>();
        List<byte[]> chunks = ArrayUtil.makeChunks(content, ENTITY_SIZE_LIMIT);
        int i = 0;
        for (byte[] chunk : chunks) {
            result.add(new FileChunkEntity(file.getId(), chunk, i++));
        }
        return result;
    }

    private List<FileChunkEntity> getByFile(Long fileId) {
        Query q = newQuery();
        q.addFilter("fileId", EQUAL, fileId);
        List<FileChunkEntity> result = selectNotCache(q);
        Collections.sort(result, new Comparator<FileChunkEntity>() {

            @Override
            public int compare(FileChunkEntity o1, FileChunkEntity o2) {
                if (o1.getIndex() > o2.getIndex()) {
                    return 1;
                }
                if (o1.getIndex() < o2.getIndex()) {
                    return -1;
                }
                return 0;
            }
        });
        return result;
    }

    @Override
    public byte[] getFileContent(FileEntity file) {
        List<byte[]> chunks = new ArrayList<byte[]>();
        for (FileChunkEntity chunk : getByFile(file.getId())) {
            chunks.add(chunk.getContent());
        }
        return ArrayUtil.packChunks(chunks);
    }
}
