package pl.org.minions.stigma.databases.map.server;

import java.net.URI;
import pl.org.minions.stigma.databases.xml.Converter;
import pl.org.minions.stigma.databases.xml.server.XmlSyncDB;
import pl.org.minions.stigma.server.npc.StaticNpcs;

/**
 * Synchronous NPC layer database.
 * @see XmlSyncDB
 */
public class StaticNpcsDB extends XmlSyncDB<StaticNpcs, StaticNpcs> {

    private static final String DB_DIR = "staticnpcs";

    private static final String FILE_PREFIX = DB_DIR;

    /**
     * Constructor. Will try to load all layers from given
     * directory.
     * @param uri
     *            resources root
     */
    public StaticNpcsDB(URI uri) {
        super(uri, StaticNpcs.class, new Converter.SimpleConverter<StaticNpcs>(), true);
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir() {
        return DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix() {
        return FILE_PREFIX;
    }
}
