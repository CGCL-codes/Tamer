package org.eoti.gaea.db;

import org.eoti.mimir.MimirException;
import org.eoti.mimir.MimirRegistry;
import org.eoti.mimir.SchemeNotFoundException;
import org.eoti.spec.gaea.nameprefix.v1.NamePrefixType;
import org.eoti.spec.mimirdb.v1.DBMapping;
import java.math.BigInteger;

public class NamePrefixDB extends GaeaDB<NamePrefixType> {

    /**
	 * Create an instance of a database
	 *
	 * @param registry MimirRegistry responsible for maintaining the mappings
	 * @param dbMapping specification for this database
	 *
	 * @throws org.eoti.mimir.SchemeValidationException if the scheme is invalid
	 * @throws org.eoti.mimir.SchemeNotFoundException if the scheme is not found
	 */
    public NamePrefixDB(MimirRegistry registry, DBMapping dbMapping) throws SchemeNotFoundException, MimirException {
        super(registry, dbMapping);
    }

    protected void initializeDB() throws MimirException {
        createNew("Mr");
        createNew("Mrs");
    }

    public NamePrefixType createNew(String value) throws MimirException {
        NamePrefixType prefix = createNew();
        prefix.setValue(value);
        write(prefix);
        return prefix;
    }

    protected BigInteger getIDfromDATA(NamePrefixType namePrefixType) {
        return namePrefixType.getId();
    }

    public NamePrefixType createNew() throws MimirException {
        NamePrefixType prefix = new NamePrefixType();
        prefix.setId(getNextID());
        return prefix;
    }
}
