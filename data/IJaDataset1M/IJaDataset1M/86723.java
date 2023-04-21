package pl.org.minions.stigma.databases.item.client;

import java.net.URI;
import pl.org.minions.stigma.databases.item.ModifierDB;
import pl.org.minions.stigma.databases.item.wrappers.ArmorModifierWrapper;
import pl.org.minions.stigma.databases.xml.Converter.SimpleConverter;
import pl.org.minions.stigma.databases.xml.client.SimpleXmlAsyncDB;
import pl.org.minions.stigma.databases.xml.server.SimpleXmlSyncDB;
import pl.org.minions.stigma.game.item.modifier.ArmorModifier;

/**
 * Armor modifier simple asynchronous database.
 * @see ModifierDB
 * @see SimpleXmlSyncDB
 */
public class ArmorModifierDBAsync extends SimpleXmlAsyncDB<ArmorModifierWrapper, ArmorModifier, ArmorModifier> {

    /**
     * Default constructor.
     * <p>
     * Immediately requests loading resources.
     * @param uri
     *            root URI
     */
    public ArmorModifierDBAsync(URI uri) {
        super(uri, ArmorModifierWrapper.class, new SimpleConverter<ArmorModifier>());
    }

    /** {@inheritDoc} */
    @Override
    public String getDbDir() {
        return ModifierDB.MAIN_DB_DIR + '/' + ModifierDB.MODIFIERS_DB_DIR;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilePrefix() {
        return ModifierDB.ARMOR_MODIFIER_FILE_PREFIX;
    }
}
