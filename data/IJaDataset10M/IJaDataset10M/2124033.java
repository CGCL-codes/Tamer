package middlegen;

import java.io.File;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Properties;
import java.lang.reflect.Constructor;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import middlegen.swing.JColumnSettingsPanel;
import middlegen.swing.JRelationSettingsPanel;
import middlegen.swing.JTableSettingsPanel;

/**
 * This is the baseclass for plugins. It can be subclassed to add additional functionality, or it can be used "as-is".
 *
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles�y</a>
 * @created 3. april 2002
 */
public class Plugin {

    /**
    * @todo-javadoc Describe the field
    */
    private Middlegen _middlegen;

    /**
    * @todo-javadoc Describe the column
    */
    private final HashMap _columnDecorators = new HashMap();

    /**
    * Key = RelationshipRole
    * Value = RelationshipRoleDecorator
    */
    private final HashMap _relationshipRoleDecorators = new HashMap();

    /**
    * @todo-javadoc Describe the column
    */
    private final HashMap _tableDecorators = new HashMap();

    /**
    * @todo-javadoc Describe the column
    */
    private final Class[] _columnDecoratorConstructorArgs = new Class[] { Column.class };

    /**
    * @todo-javadoc Describe the column
    */
    private final Class[] _tableDecoratorConstructorArgs = new Class[] { Table.class };

    private final Class[] _relationshipRoleDecoratorConstructorArgs = new Class[] { RelationshipRole.class };

    /**
    * @todo-javadoc Describe the column
    */
    private File _destinationDir;

    /** The name of the plugin */
    private String _name;

    /**
    * @todo-javadoc Describe the field
    */
    private String _mergedir;

    /**
    * @todo-javadoc Describe the field
    */
    private HashMap _fileProducers = new HashMap();

    /**
    * @todo-javadoc Describe the field
    */
    private String _displayName;

    /** Whether or not to use the schema prefix in generated code. */
    private boolean _useSchemaPrefix = false;

    /** Get static reference to Log4J Logger */
    private static org.apache.log4j.Category _log = org.apache.log4j.Category.getInstance(Plugin.class.getName());

    /** Constructor */
    public Plugin() {
    }

    /**
    * Describe what the setUseSchemaPrefix constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    * @todo-javadoc Write javadocs for method parameter
    * @param flag Describe what the parameter does
    */
    public void setUseSchemaPrefix(boolean flag) {
        _useSchemaPrefix = flag;
    }

    /**
    * Sets the Mergedir attribute of the Entity20Plugin object
    *
    * @param md The new Mergedir value
    */
    public void setMergedir(String md) {
        _mergedir = md;
    }

    /**
    * The root folder where the sources will be written. This value overrides the destination attribute specified on the
    * Ant task level.
    *
    * @param dir The new Destination value
    */
    public void setDestination(File dir) {
        _destinationDir = dir;
    }

    /**
    * Sets the logical plugin name. Not intended to be called from Ant, but by PluginFinder
    *
    * @param name The new Name value
    */
    public final void setName(String name) {
        _name = name;
    }

    /**
    * Gets the UseSchemaPrefix attribute of the Plugin object
    *
    * @return The UseSchemaPrefix value
    */
    public boolean isUseSchemaPrefix() {
        return _useSchemaPrefix;
    }

    /**
    * Returns the name to be used in the relations. Can be overridden in subclasses
    *
    * @todo-javadoc Write javadocs for method parameter
    * @param table Describe what the parameter does
    * @return The RelationName value
    */
    public String getRelationName(Table table) {
        return table.getSqlName() + "-" + getName();
    }

    /**
    * Gets the Middlegen attribute of the Plugin object
    *
    * @return The Middlegen value
    */
    public Middlegen getMiddlegen() {
        return _middlegen;
    }

    /**
    * Gets the DestinationDir attribute of the Plugin object
    *
    * @return The DestinationDir value
    */
    public File getDestinationDir() {
        return _destinationDir;
    }

    /**
    * Gets the ColumnSettingsPanel attribute of the ClassType object
    *
    * @return The ColumnSettingsPanel value
    */
    public JColumnSettingsPanel getColumnSettingsPanel() {
        return null;
    }

    /**
    * Gets the TableSettingsPanel attribute of the ClassType object
    *
    * @todo return a TableConfigurator interface instead, to avoid dependence on swing packae
    * @return The TableSettingsPanel value
    */
    public JTableSettingsPanel getTableSettingsPanel() {
        return null;
    }

    public JRelationSettingsPanel getRelationSettingsPanel() {
        return null;
    }

    /**
    * Gets the DisplayName attribute of the ClassType object
    *
    * @return The DisplayName value
    */
    public final String getDisplayName() {
        return _displayName;
    }

    /**
    * Returns the name of the plugin.
    *
    * @return The Name value
    */
    public final String getName() {
        return _name;
    }

    /**
    * Gets the ColumnDecoratorClass attribute of the Plugin object
    *
    * @return The ColumnDecoratorClass value
    */
    public Class getColumnDecoratorClass() {
        return ColumnDecorator.class;
    }

    /**
    * Gets the TableDecoratorClass attribute of the Plugin object
    *
    * @return The TableDecoratorClass value
    */
    public Class getTableDecoratorClass() {
        return TableDecorator.class;
    }

    /**
    * Gets the Relationship role decorator attribute of the Plugin object.
    * 
    * @return The RelationshipRoleDecorator value.
    */
    public Class getRelationshipRoleDecoratorClass() {
        return RelationshipRoleDecorator.class;
    }

    /**
    * Gets the Tables attribute of the Plugin object
    *
    * @return The Tables value
    */
    public final Collection getTables() {
        return _tableDecorators.values();
    }

    /**
    * Gets the Table attribute of the Plugin object
    *
    * @todo-javadoc Write javadocs for method parameter
    * @param sqlName Describe what the parameter does
    * @return The Table value
    */
    public final TableDecorator getTable(String sqlName) {
        return (TableDecorator) _tableDecorators.get(sqlName);
    }

    /**
    * Gets the Mergedir attribute of the Entity20Plugin object
    *
    * @return The Mergedir value
    */
    public String getMergedir() {
        return _mergedir;
    }

    /**
    * Adds a file producer. If the file producer's file name contains the String {0}, Middlegen will assume this is a
    * per-table file producer, and one instance for each table will be created. This method can be called from Ant or
    * from subclasses. <BR>
    *
    *
    * @param fileProducer the FileProducer to add.
    */
    public void addConfiguredFileproducer(FileProducer fileProducer) {
        fileProducer.validate();
        String id = fileProducer.getId();
        if (id == null) {
            fileProducer.setId("__custom_" + _fileProducers.size());
        }
        FileProducer customFileProducer = (FileProducer) _fileProducers.get(id);
        if (customFileProducer != null) {
            customFileProducer.copyPropsFrom(fileProducer);
        } else {
            _fileProducers.put(fileProducer.getId(), fileProducer);
        }
    }

    /**
    * Creates and caches decorators for all Tables and Columns.
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param tables Describe what the parameter does
    */
    public final void decorateAll(Collection tables) {
        Iterator tableIterator = tables.iterator();
        while (tableIterator.hasNext()) {
            DbTable table = (DbTable) tableIterator.next();
            TableDecorator tableDecorator = createDecorator(table);
            tableDecorator.setPlugin(this);
            DbColumn pkColumn = (DbColumn) table.getPkColumn();
            if (pkColumn != null) {
                ColumnDecorator pkColumnDecorator = createDecorator(pkColumn);
                pkColumnDecorator.setTableDecorator(tableDecorator);
                tableDecorator.setPkColumnDecorator(pkColumnDecorator);
                _columnDecorators.put(pkColumn, pkColumnDecorator);
            }
            Collection columnDecorators = new ArrayList(table.getColumns().size());
            Iterator columns = table.getColumns().iterator();
            while (columns.hasNext()) {
                DbColumn column = (DbColumn) columns.next();
                ColumnDecorator columnDecorator = createDecorator(column);
                columnDecorator.setPlugin(this);
                columnDecorator.setTableDecorator(tableDecorator);
                _columnDecorators.put(column, columnDecorator);
                columnDecorators.add(columnDecorator);
            }
            tableDecorator.setColumnDecorators(columnDecorators);
            Collection relationshipRoleDecorators = new ArrayList(table.getRelationshipRoles().size());
            Iterator relationshipRoles = table.getRelationshipRoles().iterator();
            while (relationshipRoles.hasNext()) {
                DbRelationshipRole role = (DbRelationshipRole) relationshipRoles.next();
                RelationshipRoleDecorator roleDecorator = createDecorator(role);
                roleDecorator.setPlugin(this);
                roleDecorator.addOriginTable(this, createDecorator((DbTable) role.getOrigin()));
                roleDecorator.addTargetTable(this, createDecorator((DbTable) role.getTarget()));
                _relationshipRoleDecorators.put(role, roleDecorator);
                relationshipRoleDecorators.add(roleDecorator);
            }
            tableDecorator.setRelationshipRoleDecorators(relationshipRoleDecorators);
        }
        Iterator tableDecorators = _tableDecorators.values().iterator();
        while (tableDecorators.hasNext()) {
            ((TableDecorator) tableDecorators.next()).init();
        }
        Iterator columnDecorators = _columnDecorators.values().iterator();
        while (columnDecorators.hasNext()) {
            ((ColumnDecorator) columnDecorators.next()).init();
        }
        Iterator relationshipRoleDecorators = _relationshipRoleDecorators.values().iterator();
        while (relationshipRoleDecorators.hasNext()) {
            ((RelationshipRoleDecorator) relationshipRoleDecorators.next()).init();
        }
    }

    /**
    * Validates that the plugin is correctly configured
    *
    * @exception MiddlegenException if the state is invalid
    */
    public void validate() throws MiddlegenException {
        if (_destinationDir == null) {
            throw new MiddlegenException("destination must be specified in <" + getName() + ">");
        }
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param mergeFile Describe what the parameter does
    * @return Describe the return value
    */
    public boolean mergeFileExists(String mergeFile) {
        return new File(getMergedir(), mergeFile).exists();
    }

    /**
    * Sets the DisplayName attribute of the Plugin object
    *
    * @param s The new DisplayName value
    */
    protected final void setDisplayName(String s) {
        _displayName = s;
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    */
    protected void registerFileProducers() {
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for exception
    * @exception MiddlegenException Describe the exception
    */
    protected void generate() throws MiddlegenException {
        registerFileProducers();
        VelocityEngine velocityEngine = getEngine();
        doIt(velocityEngine);
    }

    /**
    * Sets the Middlegen attribute of the Plugin object
    *
    * @param middlegen The new Middlegen value
    */
    void setMiddlegen(Middlegen middlegen) {
        _middlegen = middlegen;
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param column Describe what the parameter does
    * @return Describe the return value
    */
    final Column decorate(Column column) {
        if (column == null) {
            throw new IllegalArgumentException("column can't be null!" + Middlegen.BUGREPORT);
        }
        if (column.getClass() != DbColumn.class) {
            throw new IllegalArgumentException("column must be of class " + DbColumn.class.getName() + Middlegen.BUGREPORT);
        }
        ColumnDecorator result = (ColumnDecorator) _columnDecorators.get(column);
        if (result == null) {
            throw new IllegalArgumentException("result can't be null!" + Middlegen.BUGREPORT);
        }
        return result;
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param table Describe what the parameter does
    * @return Describe the return value
    */
    final Table decorate(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("table can't be null!" + Middlegen.BUGREPORT);
        }
        if (!table.getClass().equals(DbTable.class)) {
            throw new IllegalArgumentException("table must be of class " + DbTable.class.getName() + Middlegen.BUGREPORT);
        }
        TableDecorator result = (TableDecorator) _tableDecorators.get(table.getSqlName());
        if (result == null) {
            throw new IllegalArgumentException("result can't be null!" + Middlegen.BUGREPORT);
        }
        return result;
    }

    /**
    * Convert the relationship role to a decorated one.
    */
    final RelationshipRole decorate(RelationshipRole relationshipRole) {
        if (relationshipRole == null) {
            throw new IllegalArgumentException("relationshipRole can't be null!" + Middlegen.BUGREPORT);
        }
        if (!relationshipRole.getClass().equals(DbRelationshipRole.class)) {
            throw new IllegalArgumentException("relationshipRole must be of class " + DbRelationshipRole.class.getName() + Middlegen.BUGREPORT);
        }
        RelationshipRoleDecorator result = (RelationshipRoleDecorator) _relationshipRoleDecorators.get(relationshipRole);
        if (result == null) {
            throw new IllegalArgumentException("result can't be null!" + Middlegen.BUGREPORT);
        }
        return result;
    }

    /**
    * Returns all the tabledecorators' file producers. Override this method if you want different behaviour.
    *
    * @return The FileProducers value
    */
    private final Collection getFileProducers() {
        return _fileProducers.values();
    }

    /**
    * Gets the Engine attribute of the Middlegen object
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for exception
    * @return The Engine value
    * @exception MiddlegenException Describe the exception
    */
    private VelocityEngine getEngine() throws MiddlegenException {
        VelocityEngine velocityEngine = new VelocityEngine();
        Properties props = new Properties();
        props.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
        props.setProperty("file.resource.loader.class", "middlegen.KindFileResourceLoader");
        if (getMergedir() != null) {
            props.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, getMergedir());
        }
        props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "middlegen.DontCloseLog4JLogSystem");
        try {
            velocityEngine.init(props);
            return velocityEngine;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MiddlegenException(e.getMessage());
        }
    }

    /**
    * Adds additional file producers. This method is called right before the generation starts. Depending on the
    * fileName and tableDecorators parameters, several things can happen: <p>
    *
    * If fileName contains {0}, a copy of each of these file producers is created, substituting the {0} with the table
    * name, and the original one is removed.
    *
    * @todo-javadoc Write javadocs for exception
    * @todo-javadoc Write javadocs for method parameter
    * @param engine Describe what the parameter does
    * @exception MiddlegenException Describe the exception
    */
    private void doIt(VelocityEngine engine) throws MiddlegenException {
        for (Iterator fileProducerIterator = getFileProducers().iterator(); fileProducerIterator.hasNext(); ) {
            FileProducer fileProducer = (FileProducer) fileProducerIterator.next();
            if (fileProducer.isGenerationPerTable()) {
                for (Iterator tableDecoratorIterator = getTables().iterator(); tableDecoratorIterator.hasNext(); ) {
                    TableDecorator tableDecorator = (TableDecorator) tableDecoratorIterator.next();
                    if (tableDecorator.getTableElement().isGenerate()) {
                        if (tableDecorator.isGenerate() && fileProducer.accept(tableDecorator)) {
                            fileProducer.getContextMap().put("plugin", this);
                            fileProducer.generateForTable(engine, tableDecorator);
                        }
                    }
                }
            } else {
                ArrayList acceptedTableDecorators = new ArrayList();
                for (Iterator tableDecoratorIterator = getTables().iterator(); tableDecoratorIterator.hasNext(); ) {
                    TableDecorator tableDecorator = (TableDecorator) tableDecoratorIterator.next();
                    if (tableDecorator.getTableElement().isGenerate()) {
                        if (tableDecorator.isGenerate() && fileProducer.accept(tableDecorator)) {
                            acceptedTableDecorators.add(tableDecorator);
                        }
                    }
                }
                fileProducer.getContextMap().put("plugin", this);
                fileProducer.generateForTables(engine, acceptedTableDecorators);
            }
        }
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param column Describe what the parameter does
    * @return Describe the return value
    */
    private final ColumnDecorator createDecorator(DbColumn column) {
        Object decorator = _columnDecorators.get(column);
        if (decorator == null) {
            decorator = createDecorator(column, getColumnDecoratorClass(), _columnDecoratorConstructorArgs);
        }
        return (ColumnDecorator) decorator;
    }

    private final RelationshipRoleDecorator createDecorator(DbRelationshipRole relationshipRole) {
        Object decorator = _relationshipRoleDecorators.get(relationshipRole);
        if (decorator == null) {
            decorator = createDecorator(relationshipRole, getRelationshipRoleDecoratorClass(), _relationshipRoleDecoratorConstructorArgs);
        }
        return (RelationshipRoleDecorator) decorator;
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param table Describe what the parameter does
    * @return Describe the return value
    */
    private final TableDecorator createDecorator(DbTable table) {
        Object decorator = _tableDecorators.get(table.getSqlName());
        if (decorator == null) {
            decorator = createDecorator(table, getTableDecoratorClass(), _tableDecoratorConstructorArgs);
            _tableDecorators.put(table.getSqlName(), decorator);
        }
        return (TableDecorator) decorator;
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @param subject Describe what the parameter does
    * @param decoratorClass Describe what the parameter does
    * @param decoratorConstructorArgs Describe what the parameter does
    * @return Describe the return value
    */
    private final Object createDecorator(Object subject, Class decoratorClass, Class[] decoratorConstructorArgs) {
        Object decorator = null;
        String declaredConstructor = decoratorClass.getName() + "(" + decoratorConstructorArgs[0].getName() + ")";
        String invokedConstructor = decoratorClass.getName() + "(" + subject.getClass().getName() + ")";
        try {
            Constructor constructor = decoratorClass.getConstructor(decoratorConstructorArgs);
            Object[] constructorArgs = new Object[] { subject };
            decorator = constructor.newInstance(constructorArgs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Couldn't invoke constructor " + invokedConstructor);
        }
        return decorator;
    }
}
