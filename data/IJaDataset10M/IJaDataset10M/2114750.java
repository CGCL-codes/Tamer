package ar.com.oddie.persistence;

import java.io.IOException;
import ar.com.oddie.btree.BPlusTree;
import ar.com.oddie.core.helper.OddieProperties;
import ar.com.oddie.persistence.entities.Record;

public class RecordIndexPersistenceStrategy<K extends Comparable<K>, T extends Record<K>> implements PersistenceStrategy<K, T> {

    private BPlusTree<K> tree;

    private Long pointer;

    public RecordIndexPersistenceStrategy(String fileName, Class<K> keyClass, int order) throws IOException {
        this.tree = new BPlusTree<K>(order, this.getAbsoluteIndexFileName(fileName), keyClass, fileName);
    }

    public Long getPointer() {
        return pointer;
    }

    public void setPointer(Long pointer) {
        this.pointer = pointer;
    }

    @Override
    public void postRead(T record) {
    }

    @Override
    public void postWrite(T record) {
    }

    public BPlusTree<K> getTree() {
        return tree;
    }

    public void setTree(BPlusTree<K> tree) {
        this.tree = tree;
    }

    @Override
    public PersistenceData preRead(K key) {
        try {
            PersistenceData persistenceData = new PersistenceData();
            this.pointer = this.tree.get(key);
            persistenceData.setBlockId(this.pointer);
            return persistenceData;
        } catch (IOException e) {
            throw new RuntimeException("Error al intentar leer el indice.", e);
        }
    }

    @Override
    public void preWrite(long blockId, T record) {
        try {
            this.tree.put(record.getId(), blockId);
        } catch (IOException e) {
            throw new RuntimeException("Error al intentar escribir el indice.", e);
        }
    }

    @Override
    public PersistenceData preUpdate(K key) {
        return this.preRead(key);
    }

    @Override
    public void postUpdate(T record) {
    }

    private String getAbsoluteIndexFileName(String relativeFileName) {
        return OddieProperties.getOutputDirectoryPath() + FileNameFactory.getIndexFileNameFor(relativeFileName);
    }

    @Override
    public void flush() {
        try {
            this.tree.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error al intentar escribir el indice.", e);
        }
    }

    @Override
    public void close() {
        this.tree.close();
    }
}
