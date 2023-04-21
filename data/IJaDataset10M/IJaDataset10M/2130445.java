package othello.server.meta;

/** */
public final class WaitingEntryMeta extends org.slim3.datastore.ModelMeta<othello.shared.model.WaitingEntry> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<othello.shared.model.WaitingEntry, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<othello.shared.model.WaitingEntry, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<othello.shared.model.WaitingEntry, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<othello.shared.model.WaitingEntry, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final WaitingEntryMeta slim3_singleton = new WaitingEntryMeta();

    /**
     * @return the singleton
     */
    public static WaitingEntryMeta get() {
        return slim3_singleton;
    }

    /** */
    public WaitingEntryMeta() {
        super("WaitingEntry", othello.shared.model.WaitingEntry.class);
    }

    @Override
    public othello.shared.model.WaitingEntry entityToModel(com.google.appengine.api.datastore.Entity entity) {
        othello.shared.model.WaitingEntry model = new othello.shared.model.WaitingEntry();
        model.setKey(entity.getKey());
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        othello.shared.model.WaitingEntry m = (othello.shared.model.WaitingEntry) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        othello.shared.model.WaitingEntry m = (othello.shared.model.WaitingEntry) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        othello.shared.model.WaitingEntry m = (othello.shared.model.WaitingEntry) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        othello.shared.model.WaitingEntry m = (othello.shared.model.WaitingEntry) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        othello.shared.model.WaitingEntry m = (othello.shared.model.WaitingEntry) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    public String getSchemaVersionName() {
        return "slim3.schemaVersion";
    }

    @Override
    public String getClassHierarchyListName() {
        return "slim3.classHierarchyList";
    }
}
