package pl.org.minions.stigma.databases.actor.client;

import java.net.URI;
import pl.org.minions.stigma.databases.actor.ProficiencyDB;
import pl.org.minions.stigma.databases.actor.wrappers.ProficiencyWrapper;
import pl.org.minions.stigma.databases.xml.Converter.SimpleConverter;
import pl.org.minions.stigma.databases.xml.client.SimpleXmlAsyncDB;
import pl.org.minions.stigma.game.actor.Proficiency;

/**
 * Proficiency simple asynchronous database.
 * @see ProficiencyDB
 * @see SimpleXmlAsyncDB
 */
public class ProficiencyDBAsync extends SimpleXmlAsyncDB<ProficiencyWrapper, Proficiency, Proficiency> implements ProficiencyDB {

    /**
     * Default constructor.
     * <p>
     * Immediately requests loading resources.
     * @param uri
     *            root URI
     */
    public ProficiencyDBAsync(URI uri) {
        super(uri, ProficiencyWrapper.class, new SimpleConverter<Proficiency>());
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

    /** {@inheritDoc} */
    @Override
    public Proficiency getProficiency(short id) {
        return super.getById(id);
    }
}
