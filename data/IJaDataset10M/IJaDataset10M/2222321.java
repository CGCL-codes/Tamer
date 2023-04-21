package org.mcisb.beacon.ui.pedro;

import java.util.*;
import javax.swing.*;
import pedro.view.*;
import org.mcisb.beacon.db.xml.*;
import org.mcisb.beacon.ui.db.xml.*;

/**
 * 
 * @author Neil Swainston
 */
public abstract class DbDataPlugin extends AbstractDbPlugin {

    /**
	 * 
	 */
    protected final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.beacon.ui.pedro.messages");

    /**
	 * 
	 */
    private final String collectionName;

    /**
	 * 
	 */
    private Database db;

    /**
	 * 
	 * @param collectionName
	 * @param recordClassName
	 * @param displayName
	 * @param keyProperties
	 */
    public DbDataPlugin(final String collectionName, final String recordClassName, final String displayName, final String[] keyProperties) {
        this(collectionName, Arrays.asList(recordClassName), displayName, keyProperties);
    }

    /**
	 * 
	 * @param collectionName
	 * @param recordClassNames
	 * @param displayName
	 * @param keyProperties
	 */
    public DbDataPlugin(final String collectionName, final Collection recordClassNames, final String displayName, final String[] keyProperties) {
        super(recordClassNames, displayName, keyProperties);
        this.collectionName = collectionName;
    }

    /**
	 * 
	 * @return Database
	 * @throws Exception
	 */
    protected Database getDb() throws Exception {
        if (db == null) {
            db = DatabaseFactory.getInstance(collectionName);
        }
        return db;
    }

    /**
	 * 
	 * @param node
	 * @return String
	 * @throws Exception
	 */
    protected String getDocId(NavigationTreeNode node) throws Exception {
        return showChoices(node.getRecordNameProvider().getRecordClassName());
    }

    /**
	 * 
	 * @param elementName
	 * @return String
	 * @throws Exception
	 */
    protected String showChoices(String elementName) throws Exception {
        final Map results = getDb().getElementIDs(elementName, keyProperties);
        final String message = resourceBundle.getString("DbDataPlugin.title") + elementName;
        Vector resultVector = new Vector(results.keySet());
        Collections.sort(resultVector);
        final String s = (String) JOptionPane.showInputDialog(null, message, message, JOptionPane.PLAIN_MESSAGE, null, resultVector.toArray(), null);
        if (s != null) {
            return (String) results.get(s);
        }
        return null;
    }
}
