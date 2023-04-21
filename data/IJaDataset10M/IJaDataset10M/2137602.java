package gnumler.pluglord;

import gme.kernel.Element;
import gme.kernel.Identifier;
import gme.kernel.Model;
import gme.kernel.exceptions.InvalidOperationException;
import gme.model.dblogic.DbColumn;
import gme.model.dblogic.DbForeignKey;
import gme.model.dblogic.DbTable;
import gme.model.hibernate.HibernateFactory;
import gme.model.hibernate.SimpleClassHibPlug;
import gme.model.uml.UmlAttribute;
import gme.model.uml.UmlClass;
import gme.model.uml.UmlFactory;
import java.util.Iterator;
import java.util.Map;

/**
 * PlugLord_SQL is a basic reverse engineering tool. It can build a UML structure from
 * a DbLogic model (probably imported via the SQLParser
 * @author Claudio Criscione
 */
public class PlugLord_SQL implements PlugLord {

    Model workModel;

    private Map<Identifier, Element> tables = null;

    public PlugLord_SQL(Model model) {
        this.workModel = model;
    }

    public void defaultTranslation() throws InvalidOperationException {
        boolean supportTable = false;
        Iterator<Element> itOverTables = null;
        DbTable currentTable = null;
        Map<Identifier, Element> fkeys = null;
        tables = this.workModel.getChildrenByType(DbTable.class);
        itOverTables = tables.values().iterator();
        while (itOverTables.hasNext()) {
            supportTable = false;
            currentTable = (DbTable) itOverTables.next();
            fkeys = currentTable.getChildrenByType(DbForeignKey.class);
            Iterator fkeyIt = fkeys.values().iterator();
            while (fkeyIt.hasNext()) {
                if (currentTable.getTablePrimaryKey().getColumns().containsValue(fkeyIt.next())) supportTable = true;
            }
            if (!supportTable) this.mapTable(currentTable);
        }
    }

    /**
	 * mapTable will create an UmlClass according to the SQL table, and add it to the model
	 * @param tableToMap The table to be mapped
	 * @return UmlClass the newly created UmlClass
	 * @throws IdentificationException 
	 * @throws InvalidOperationException 
	 */
    public UmlClass mapTable(DbTable tableToMap) throws InvalidOperationException {
        UmlClass workingClass = null;
        String UmlAlias = "[C]" + tableToMap.getIdentifier().getAliasName();
        Map<Identifier, Element> columns;
        DbColumn currentColumn = null;
        SimpleClassHibPlug workingPlug = null;
        UmlAttribute workingAttribute = null;
        workingClass = UmlFactory.createUmlClass(workModel, UmlAlias);
        workingPlug = HibernateFactory.createSimpleClassPlug(workModel, "[P]" + workingClass, workingClass, tableToMap);
        columns = tableToMap.getChildrenByType(DbColumn.class);
        Iterator<Element> itColumns = columns.values().iterator();
        while (itColumns.hasNext()) {
            currentColumn = (DbColumn) itColumns.next();
            workingAttribute = UmlFactory.createUmlAttribute(workingClass, currentColumn.getIdentifier().getAliasName());
            HibernateFactory.createAttributePlug(workingPlug, "[P]" + workingAttribute, workingAttribute, currentColumn);
        }
        return workingClass;
    }
}
