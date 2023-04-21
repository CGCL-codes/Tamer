package fr.ird.database.sample.sql;

import java.sql.Types;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLWarning;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import javax.sql.RowSet;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.Writer;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.geotools.resources.Utilities;
import org.geotools.util.ProgressListener;
import fr.ird.database.CatalogException;
import fr.ird.resources.XArray;
import fr.ird.database.coverage.SeriesTable;
import fr.ird.database.sample.SampleDataBase;
import fr.ird.database.sample.SampleEntry;
import fr.ird.database.sample.ParameterEntry;
import fr.ird.database.sample.OperationEntry;
import fr.ird.database.sample.RelativePositionEntry;
import fr.ird.resources.seagis.Resources;
import fr.ird.resources.seagis.ResourceKeys;

/**
 * Impl�mentation d'une table qui fait le lien entre les �chantillons et les param�tres
 * environnementaux aux positions de cet �chantillon. Cette interrogation pourrait �tre
 * faites dans un logiciel de base de donn�es avec une requ�te SQL classique. Mais cette
 * requ�te est assez longue et tr�s laborieuse � construire � la main. De plus, elle d�passe
 * souvent les capacit�s de Access. Cette classe d�coupera cette requ�te monstre en une s�rie
 * de requ�tes plus petites.
 *
 * @version $Id: EnvironmentTable.java,v 1.6 2004/10/18 13:40:47 remi_eve Exp $
 * @author Martin Desruisseaux
 */
final class EnvironmentTable extends Table implements fr.ird.database.sample.EnvironmentTable {

    /**
     * Instruction SQL pour mettre � jour une donn�e environnementale.
     * Note: La valeur est le premier param�tre, et tous les autres sont d�cal�s de 1.
     */
    static final String SQL_UPDATE = Table.configuration.get(Configuration.KEY_ENVIRONMENTS_UPDATE);

    /**
     * Instruction SQL pour ajouter une donn�e environnementale.
     */
    static final String SQL_INSERT = Table.configuration.get(Configuration.KEY_ENVIRONMENTS_INSERT);

    /** Num�ro d'argument. */
    private static final int ARG_SAMPLE = 1;

    /** Num�ro d'argument. */
    private static final int ARG_POSITION = 2;

    /** Num�ro d'argument. */
    private static final int ARG_PARAMETER = 3;

    /** Num�ro d'argument. */
    private static final int ARG_VALUE = 4;

    /**
     * La table des descripteurs du paysage oc�anique.
     * Ne sera construite que la premi�re fois o� elle sera n�cessaire.
     */
    private transient DescriptorTable descriptors;

    /**
     * Table des �chantillons � joindre avec les param�tres environnementaux retourn�s par
     * {@link #getRowSet}, ou <code>null</code> si aucune. Il ne s'agit pas n�cessairement
     * de la table <code>"Samples"</code>. Il pourrait s'agir d'une requ�te, comme par exemple
     * <code>"Pr�sences par esp�ces"<code>.
     *
     * @see #setSampleTable
     */
    private SampleTableStep sampleTableStep;

    /**
     * Liste des param�tres et des op�rations � prendre en compte. Les cl�s sont des
     * objets  {@link EnvironmentTableStep}  repr�sentant le param�tre ainsi que sa
     * position spatio-temporelle.
     */
    private final Map<EnvironmentTableStep, EnvironmentTableStep> parameters = new LinkedHashMap<EnvironmentTableStep, EnvironmentTableStep>();

    /**
     * Indique si les valeurs nulles sont permises pour chaque colonne de {@link #getRowSet}.
     * Ce tableau est calcul� en m�me temps que les �tiquettes par {@link #getColumnLabels}.
     */
    private transient boolean[] nullIncluded;

    /**
     * Instruction � utiliser pour les mises � jour et les insertions.
     * Ces instructions ne seront construites que la premi�re fois o�
     * elle seront n�cessaires.
     */
    private transient PreparedStatement update, insert;

    /**
     * Nom des colonnes utilis�es lors de la derni�re cr�ation des instructions
     * {@link #update} et {@link #insert}. Sert � �viter de reconstruire de
     * nouvelles instructions lorsque ce n'est pas n�cessaire (c'est-�-dire
     * lorsque le nom n'a pas chang�).
     */
    private transient String columnUpdate, columnInsert;

    /**
     * Construit une table.
     *
     * @param  connection Connection vers une base de donn�es des �chantillons.
     * @param  series La table des s�ries. Cette table ne sera pas ferm�e par {@link #close},
     *         puisqu'elle n'appartient pas � cet objet <code>EnvironmentTable</code>.
     * @throws SQLException si <code>EnvironmentTable</code> n'a pas pu construire sa requ�te SQL.
     */
    protected EnvironmentTable(final Connection connection, final SeriesTable series) throws RemoteException {
        super(null);
        descriptors = new ParameterTable(connection, ParameterTable.BY_ID, series).getLinearModelTable().getDescriptorTable();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSampleTable(final String table) throws RemoteException {
        try {
            if (!Utilities.equals(table, getSampleTable())) {
                if (sampleTableStep != null) {
                    sampleTableStep.close();
                    sampleTableStep = null;
                }
                final LogRecord record = Resources.getResources(null).getLogRecord(Level.CONFIG, ResourceKeys.JOIN_TABLE_$1, (table != null) ? table : "<aucune>");
                record.setSourceClassName("EnvironmentTable");
                record.setSourceMethodName("setSampleTable");
                SampleDataBase.LOGGER.log(record);
                if (table != null) {
                    sampleTableStep = new SampleTableStep(descriptors.getConnection(), table);
                }
            }
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String getSampleTable() throws RemoteException {
        return sampleTableStep != null ? sampleTableStep.table : null;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Set<ParameterEntry> getAvailableParameters() throws RemoteException {
        return descriptors.getParameterTable(SingletonTable.LIST).list();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Set<OperationEntry> getAvailableOperations() throws RemoteException {
        return descriptors.getOperationTable(SingletonTable.LIST).list();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Set<RelativePositionEntry> getAvailablePositions() throws RemoteException {
        return descriptors.getPositionTable(SingletonTable.LIST).list();
    }

    /**
     * {@inheritDoc}
     *
     * @task TODO: Le compilateur prototype 2.0 nous oblige � ajouter les cast.
     *             V�rifier si une version plus r�cente arrive � r�soudre tout seul.
     */
    public synchronized void addParameter(final String parameter, final String operation, final String position, final boolean nullIncluded) throws RemoteException {
        addParameter((ParameterEntry) descriptors.getParameterTable(SingletonTable.BY_NAME).getEntry(parameter), (OperationEntry) descriptors.getOperationTable(SingletonTable.BY_NAME).getEntry(operation), (RelativePositionEntry) descriptors.getPositionTable(SingletonTable.BY_NAME).getEntry(position), nullIncluded);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void addParameter(final ParameterEntry parameter, final OperationEntry operation, final RelativePositionEntry position, final boolean nullIncluded) throws RemoteException {
        this.nullIncluded = null;
        remove(operation, new EnvironmentTableStep(parameter, position, !nullIncluded));
        EnvironmentTableStep search = new EnvironmentTableStep(parameter, position, nullIncluded);
        EnvironmentTableStep step = parameters.get(search);
        if (step == null) {
            step = search;
            parameters.put(step, step);
        }
        step.addColumn(operation);
    }

    /**
     * Retire une op�ration de l'objet <code>step</code> sp�cifi�.
     *
     * @throws SQLException si l'acc�s � la base de donn�es a �chou�.
     */
    private void remove(final OperationEntry operation, EnvironmentTableStep step) throws RemoteException {
        step = parameters.get(step);
        if (step != null) {
            step.removeColumn(operation);
            if (step.isEmpty()) {
                step.close();
                parameters.remove(step);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @task TODO: Le compilateur prototype 2.0 nous oblige � ajouter les cast.
     *             V�rifier si une version plus r�cente arrive � r�soudre tout seul.
     */
    public synchronized void removeParameter(final String parameter, final String operation, final String position) throws RemoteException {
        removeParameter((ParameterEntry) descriptors.getParameterTable(SingletonTable.BY_NAME).getEntry(parameter), (OperationEntry) descriptors.getOperationTable(SingletonTable.BY_NAME).getEntry(operation), (RelativePositionEntry) descriptors.getPositionTable(SingletonTable.BY_NAME).getEntry(position));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void removeParameter(final ParameterEntry parameter, final OperationEntry operation, final RelativePositionEntry position) throws RemoteException {
        this.nullIncluded = null;
        boolean nullIncluded = false;
        do {
            remove(operation, new EnvironmentTableStep(parameter, position, nullIncluded));
        } while ((nullIncluded = !nullIncluded) == true);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getParameterCount(final ParameterEntry parameter, final OperationEntry operation, final RelativePositionEntry position) {
        if (parameter == null && operation == null && position == null) {
            return parameters.size();
        }
        int count = 0;
        for (final EnvironmentTableStep step : parameters.values()) {
            if (parameter != null && !parameter.equals(step.parameter)) {
                continue;
            }
            if (position != null && !position.equals(step.position)) {
                continue;
            }
            if (operation != null && !step.hasColumn(operation)) {
                continue;
            }
            count++;
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized String[] getColumnLabels() throws RemoteException {
        try {
            final List<String> titles = new ArrayList<String>();
            final List<Boolean> hasNul = new ArrayList<Boolean>();
            final StringBuffer buffer = new StringBuffer();
            titles.add("sample");
            hasNul.add(Boolean.FALSE);
            if (sampleTableStep != null) {
                final String[] columns = sampleTableStep.getColumns();
                for (int i = 1; i < columns.length; i++) {
                    titles.add(columns[i]);
                    hasNul.add(Boolean.FALSE);
                }
            }
            for (final EnvironmentTableStep step : parameters.values()) {
                buffer.setLength(0);
                buffer.append(step.parameter.getName());
                buffer.append(step.position.getName());
                int prefixLength = 0;
                final String[] columns = step.getColumns(true);
                for (int i = 0; i < columns.length; i++) {
                    final String prefix = columns[i];
                    buffer.replace(0, prefixLength, prefix);
                    titles.add(buffer.toString());
                    hasNul.add(Boolean.valueOf(step.nullIncluded));
                    prefixLength = prefix.length();
                }
            }
            nullIncluded = new boolean[hasNul.size()];
            for (int i = 0; i < nullIncluded.length; i++) {
                nullIncluded[i] = hasNul.get(i).booleanValue();
            }
            return titles.toArray(new String[titles.size()]);
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized RowSet getRowSet(final ProgressListener progress) throws RemoteException {
        try {
            if (progress != null) {
                progress.setDescription("Initialisation");
                progress.started();
            }
            int i = (sampleTableStep != null) ? 1 : 0;
            final ResultSet[] results = new ResultSet[parameters.size() + i];
            final boolean[] nullIncluded = new boolean[results.length];
            final Connection connection = descriptors.getConnection();
            if (sampleTableStep != null) {
                results[0] = sampleTableStep.getResultSet();
            }
            for (final EnvironmentTableStep step : parameters.values()) {
                results[i] = step.getResultSet(connection);
                nullIncluded[i++] = step.nullIncluded;
                if (progress != null) {
                    progress.progress((100f / results.length) * i);
                }
            }
            assert i == results.length;
            return new EnvironmentRowSet(results, getColumnLabels(), nullIncluded);
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int print(final Writer out, int max) throws RemoteException {
        try {
            final ResultSet result = getRowSet(null);
            final ResultSetMetaData meta = result.getMetaData();
            final String lineSeparator = System.getProperty("line.separator", "\n");
            final int columnCount = meta.getColumnCount();
            final int[] width = new int[columnCount];
            final boolean[] isDate = new boolean[columnCount];
            for (int i = 0; i < columnCount; i++) {
                final String title = meta.getColumnLabel(i + 1);
                out.write(title);
                int length = title.length();
                final int type = meta.getColumnType(i + 1);
                switch(type) {
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        {
                            isDate[i] = true;
                            width[i] = 8;
                            break;
                        }
                    default:
                        {
                            width[i] = Math.max(i == 0 ? 11 : 7, length);
                            break;
                        }
                }
                if (false) {
                    final String code = String.valueOf(type);
                    out.write('(');
                    out.write(code);
                    out.write(')');
                    length += (code.length() + 2);
                }
                out.write(Utilities.spaces(width[i] - length + 1));
            }
            int count = 0;
            out.write(lineSeparator);
            DateFormat dateFormat = null;
            final NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);
            while (--max >= 0 && result.next()) {
                for (int i = 0; i < width.length; i++) {
                    final String value;
                    if (i == 0) {
                        final int x = result.getInt(i + 1);
                        value = result.wasNull() ? "" : String.valueOf(x);
                    } else if (!isDate[i]) {
                        final double x = result.getDouble(i + 1);
                        value = result.wasNull() ? "" : format.format(x);
                    } else {
                        final Date x = result.getDate(i + 1);
                        if (!result.wasNull()) {
                            if (dateFormat == null) {
                                dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
                            }
                            value = dateFormat.format(x);
                        } else {
                            value = "";
                        }
                    }
                    out.write(Utilities.spaces(width[i] - value.length()));
                    out.write(value);
                    out.write(' ');
                }
                out.write(lineSeparator);
                count++;
            }
            result.close();
            out.flush();
            return count;
        } catch (IOException e) {
            throw new CatalogException(e);
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int copyToTable(final Connection connection, final String tableName, final ProgressListener progress) throws RemoteException {
        try {
            final ResultSet source = getRowSet(progress);
            final ResultSetMetaData meta = source.getMetaData();
            final int columnCount = meta.getColumnCount();
            final boolean[] isDate = new boolean[columnCount];
            final Statement creator;
            final ResultSet dest;
            if (true) {
                final StringBuffer buffer = new StringBuffer("CREATE TABLE \"");
                buffer.append(tableName);
                buffer.append("\"(\"");
                for (int i = 0; i < columnCount; i++) {
                    if (i != 0) {
                        buffer.append(", \"");
                    }
                    buffer.append(meta.getColumnName(i + 1));
                    buffer.append("\" ");
                    if (i == 0) {
                        buffer.append("INTEGER");
                    } else {
                        switch(meta.getColumnType(i + 1)) {
                            case Types.DATE:
                            case Types.TIME:
                            case Types.TIMESTAMP:
                                {
                                    isDate[i] = true;
                                    buffer.append("TIMESTAMP");
                                    break;
                                }
                            case Types.TINYINT:
                            case Types.SMALLINT:
                                {
                                    buffer.append("SMALLINT");
                                    break;
                                }
                            default:
                                {
                                    buffer.append("REAL");
                                    break;
                                }
                        }
                    }
                    if (!nullIncluded[i] || meta.isNullable(i + 1) == ResultSetMetaData.columnNoNulls) {
                        buffer.append(" NOT NULL");
                    }
                }
                buffer.append(')');
                final String sqlCreate = buffer.toString();
                creator = (connection != null ? connection : descriptors.getConnection()).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                creator.execute(sqlCreate);
                buffer.setLength(0);
                buffer.append("SELECT * FROM \"");
                buffer.append(tableName);
                buffer.append('"');
                dest = creator.executeQuery(buffer.toString());
                if (true) {
                    final LogRecord record = new LogRecord(SampleDataBase.SQL_UPDATE, sqlCreate);
                    record.setSourceClassName("EnvironmentTable");
                    record.setSourceMethodName("copyToTable");
                    SampleDataBase.LOGGER.log(record);
                }
            }
            final float minID = Integer.MIN_VALUE;
            final float maxID = Integer.MAX_VALUE;
            if (progress != null) {
                progress.setDescription("Copie des donn�es");
                progress.progress(0);
            }
            int count = 0;
            while (source.next()) {
                final int ID = source.getInt(1);
                if (progress != null && (count & 0xFF) == 0) {
                    progress.progress((ID - minID) / ((maxID - minID) / 100));
                }
                dest.moveToInsertRow();
                dest.updateInt(1, ID);
                for (int i = 2; i <= columnCount; i++) {
                    if (isDate[i - 1]) {
                        dest.updateTimestamp(i, source.getTimestamp(i));
                    } else {
                        final float x = source.getFloat(i);
                        if (!source.wasNull()) {
                            dest.updateFloat(i, x);
                        } else {
                            dest.updateNull(i);
                        }
                    }
                }
                dest.insertRow();
                count++;
            }
            dest.close();
            source.close();
            creator.close();
            if (progress != null) {
                progress.complete();
            }
            return count;
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void set(final SampleEntry sample, final RelativePositionEntry filter, final double[] values) throws RemoteException {
        try {
            if (values.length != getParameterCount(null, null, filter)) {
                throw new IllegalArgumentException("Le nombre de valeurs ne correspond pas.");
            }
            int index = 0;
            for (final EnvironmentTableStep step : parameters.values()) {
                if (filter != null && !filter.equals(step.position)) {
                    continue;
                }
                final int parameter = step.parameter.getID();
                final int position = step.position.getID();
                final double value = values[index++];
                if (Double.isNaN(value)) {
                    continue;
                }
                final String[] columns = step.getColumns(false);
                for (int i = 0; i < columns.length; i++) {
                    final String column = columns[i];
                    if (update != null && !column.equals(columnUpdate)) {
                        update.close();
                        update = null;
                    }
                    if (update == null) {
                        update = descriptors.getConnection().prepareStatement(replaceQuestionMark(SQL_UPDATE, column));
                        columnUpdate = column;
                    }
                    update.setInt(1 + ARG_SAMPLE, sample.getID());
                    update.setInt(1 + ARG_PARAMETER, parameter);
                    update.setInt(1 + ARG_POSITION, position);
                    update.setDouble(1, value);
                    int n = update.executeUpdate();
                    if (n == 0) {
                        if (insert != null && !column.equals(columnInsert)) {
                            insert.close();
                            insert = null;
                        }
                        if (insert == null) {
                            insert = descriptors.getConnection().prepareStatement(replaceQuestionMark(SQL_INSERT, column));
                            columnInsert = column;
                        }
                        insert.setInt(ARG_SAMPLE, sample.getID());
                        insert.setInt(ARG_PARAMETER, parameter);
                        insert.setInt(ARG_POSITION, position);
                        insert.setDouble(ARG_VALUE, value);
                        n = insert.executeUpdate();
                    }
                    if (n != 1) {
                        throw new SQLWarning(Resources.format(ResourceKeys.ERROR_UNEXPECTED_UPDATE_$1, new Integer(n)));
                    }
                }
            }
            assert index == values.length;
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void clear() throws RemoteException {
        for (final EnvironmentTableStep step : parameters.values()) {
            step.close();
        }
        parameters.clear();
        nullIncluded = null;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void close() throws RemoteException {
        try {
            if (insert != null) {
                insert.close();
                insert = null;
            }
            if (update != null) {
                update.close();
                update = null;
            }
            if (descriptors != null) {
                descriptors.close();
                descriptors = null;
            }
            if (sampleTableStep != null) {
                sampleTableStep.close();
                sampleTableStep = null;
            }
            clear();
            super.close();
        } catch (SQLException e) {
            throw new CatalogException(e);
        }
    }
}
