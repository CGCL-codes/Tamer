package org.mcisb.sbml.bnet;

import java.sql.*;
import java.util.*;
import org.sbml.jsbml.*;
import org.mcisb.db.sql.*;
import org.mcisb.ontology.*;
import org.mcisb.sbml.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class BnetUtils {

    /**
	 * 
	 */
    private final Map<String, Integer> tableNameToId = new TreeMap<String, Integer>();

    /**
	 * 
	 */
    private final BnetStatementExecutor statementExecutor;

    /**
	 * 
	 */
    private Map<Integer, SBase> compartments;

    /**
	 * 
	 */
    private Map<Integer, SBase> species;

    /**
	 * 
	 * @param connection
	 * @throws SQLException
	 */
    public BnetUtils(final Connection connection) throws SQLException {
        statementExecutor = new BnetStatementExecutor(connection);
    }

    /**
	 * 
	 * @param tableName
	 * @return int
	 * @throws SQLException
	 */
    public int getTable(final String tableName) throws SQLException {
        Object id = tableNameToId.get(tableName);
        if (id != null) {
            return ((Integer) id).intValue();
        }
        final String TABLE_NAME = "ref_kw_table";
        final String ID_FIELD = ".kw_table_id";
        final String SEARCH_FIELD = ".kw_table";
        int existingIndex = statementExecutor.getExistingIndex(TABLE_NAME + ID_FIELD, TABLE_NAME + SEARCH_FIELD, tableName.toUpperCase(Locale.getDefault()));
        tableNameToId.put(tableName, Integer.valueOf(existingIndex));
        return existingIndex;
    }

    /**
	 * 
	 *
	 * @return SBMLDocument
	 * @throws Exception
	 */
    public SBMLDocument getDocument() throws Exception {
        final SBMLDocument document = new SBMLDocument();
        final Model model = document.createModel(StringUtils.getUniqueId());
        for (Iterator<SBase> iterator = getCompartments().values().iterator(); iterator.hasNext(); ) {
            SbmlUtils.add(model, (Compartment) iterator.next());
        }
        for (Iterator<SBase> iterator = getSpecies().values().iterator(); iterator.hasNext(); ) {
            SbmlUtils.add(model, (Species) iterator.next());
        }
        for (Iterator<Reaction> iterator = getReactions().iterator(); iterator.hasNext(); ) {
            SbmlUtils.add(model, iterator.next());
        }
        return document;
    }

    /**
	 * 
	 * @param tableId
	 * @param recordId
	 * @return Collection
	 * @throws SQLException
	 */
    public Collection<Object> getNames(final int tableId, final int recordId) throws SQLException {
        final int NAME = 0;
        final int IS_PRIMARY = 1;
        final String TABLE_NAME = "allnames";
        final String RECORD_ID_FIELD = ".record_id";
        final String TABLE_ID_FIELD = ".kw_table_id";
        final String NAME_FIELD = ".aname";
        final String PRIMARY_FIELD = ".is_primary";
        final List<Object> names = new ArrayList<Object>();
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + RECORD_ID_FIELD + "=" + SqlUtils.getValueString(Integer.valueOf(recordId)));
        conditions.add(TABLE_NAME + TABLE_ID_FIELD + "=" + SqlUtils.getValueString(Integer.valueOf(tableId)));
        final Collection<List<Object>> values = statementExecutor.getValues(Arrays.asList(TABLE_NAME + NAME_FIELD, TABLE_NAME + PRIMARY_FIELD), conditions);
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            final List<Object> row = iterator.next();
            final Object name = row.get(NAME);
            if (names.size() > 0 && ((String) row.get(IS_PRIMARY)).equals("Y")) {
                names.add(0, name);
            } else {
                names.add(name);
            }
        }
        return names;
    }

    /**
	 * 
	 *
	 * @param eventId
	 * @return Collection
	 * @throws Exception
	 */
    public Collection<String> getReactantIds(final int eventId) throws Exception {
        return getEventComponentIds(eventId, true);
    }

    /**
	 * 
	 *
	 * @param eventId
	 * @return Collection
	 * @throws Exception
	 */
    public Collection<String> getProductIds(final int eventId) throws Exception {
        return getEventComponentIds(eventId, false);
    }

    /**
	 *
	 * @param recordId
	 * @param tableId
	 * @param stoichiometry
	 * @return Collection
	 * @throws SQLException
	 */
    public Collection<Object> getComplexIds(final int recordId, final int tableId, final double stoichiometry) throws SQLException {
        final String TABLE_NAME = "complex_member";
        final String RECORD_ID_FIELD = ".record_id";
        final String TABLE_ID_FIELD = ".kw_table_id";
        final String STOICHIOMETRY_FIELD = ".stoichiometry";
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + RECORD_ID_FIELD + "=" + SqlUtils.getValueString(Integer.valueOf(recordId)));
        conditions.add(TABLE_NAME + TABLE_ID_FIELD + "=" + SqlUtils.getValueString(Integer.valueOf(tableId)));
        conditions.add(TABLE_NAME + STOICHIOMETRY_FIELD + "=" + SqlUtils.getValueString(Double.valueOf(stoichiometry)));
        final Collection<List<Object>> values = statementExecutor.getValues(TABLE_NAME, conditions, new ArrayList<String>());
        final Collection<Object> complexIds = new TreeSet<Object>();
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            final List<Object> row = iterator.next();
            complexIds.add(CollectionUtils.getFirst(row));
        }
        return complexIds;
    }

    /**
	 *
	 * @param eventTypeName
	 * @return int
	 * @throws SQLException
	 */
    public int getEventType(final String eventTypeName) throws SQLException {
        final String TABLE_NAME = "event_type";
        final String SEARCH_FIELD = ".event_type_name";
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + SEARCH_FIELD + "=" + SqlUtils.getValueString(eventTypeName));
        final Collection<List<Object>> values = statementExecutor.getValues(TABLE_NAME, conditions, new ArrayList<String>());
        return ((Integer) CollectionUtils.getFirst(CollectionUtils.getFirst(values))).intValue();
    }

    /**
	 * 
	 * @param recordId
	 * @param tableId
	 * @param isReactant
	 * @param stoichiometry
	 * @return Collection
	 * @throws SQLException
	 */
    Collection<Integer> getEventIds(final int recordId, final int tableId, final boolean isReactant, final double stoichiometry) throws SQLException {
        final String TABLE_NAME = "event_component";
        final Collection<Integer> eventIds = new LinkedHashSet<Integer>();
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + ".record_id=" + SqlUtils.getValueString(Integer.valueOf(recordId)));
        conditions.add(TABLE_NAME + ".kw_table_id=" + SqlUtils.getValueString(Integer.valueOf(tableId)));
        conditions.add(TABLE_NAME + ".is_substrate=" + SqlUtils.getValueString(isReactant ? "Y" : "N"));
        conditions.add(TABLE_NAME + ".stoichiometry=" + SqlUtils.getValueString(Double.valueOf(stoichiometry)));
        final List<List<Object>> values = statementExecutor.getValues(Arrays.asList(TABLE_NAME + ".event_id"), conditions);
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            eventIds.add((Integer) CollectionUtils.getFirst(iterator.next()));
        }
        return eventIds;
    }

    /**
	 *
	 * @param complexId
	 * @return Collection
	 * @throws SQLException
	 */
    Collection<List<Object>> getComplexMembers(final int complexId) throws SQLException {
        final String TABLE_NAME = "complex_member";
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + ".complex_id=" + SqlUtils.getValueString(Integer.valueOf(complexId)));
        return statementExecutor.getValues(Arrays.asList(TABLE_NAME + ".record_id", TABLE_NAME + ".kw_table_id", TABLE_NAME + ".stoichiometry"), conditions);
    }

    /**
	 * 
	 * @return Map
	 * @throws Exception
	 */
    private Map<Integer, SBase> getCompartments() throws Exception {
        if (compartments == null) {
            final String TABLE_NAME = "location";
            compartments = getCollection(TABLE_NAME, false);
        }
        return compartments;
    }

    /**
	 * 
	 *
	 * @return Collection
	 * @throws Exception
	 */
    private Collection<Reaction> getReactions() throws Exception {
        final String TABLE_NAME = "event";
        final String EVENT_TYPE_ID_FIELD = ".event_type_id";
        final String EVENT_ID_FIELD = ".event_id";
        final Collection<Reaction> reactions = new ArrayList<Reaction>();
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + EVENT_TYPE_ID_FIELD + "=" + SqlUtils.getValueString(Integer.valueOf(getEventTypeId(PropertyNames.REACTION))));
        final Collection<List<Object>> values = statementExecutor.getValues(Arrays.asList(TABLE_NAME + EVENT_ID_FIELD), conditions);
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            final List<Object> row = iterator.next();
            final int eventId = ((Integer) CollectionUtils.getFirst(row)).intValue();
            final Reaction reaction = new Reaction();
            reaction.addNamespace(SbmlUtils.getDefaultSBMLNamespace());
            for (Iterator<String> iterator2 = getReactantIds(eventId).iterator(); iterator2.hasNext(); ) {
                final SpeciesReference speciesReference = new SpeciesReference();
                speciesReference.addNamespace(SbmlUtils.getDefaultSBMLNamespace());
                speciesReference.setId(iterator2.next());
                reaction.addReactant(speciesReference);
            }
            for (Iterator<String> iterator2 = getProductIds(eventId).iterator(); iterator2.hasNext(); ) {
                final SpeciesReference speciesReference = new SpeciesReference();
                speciesReference.addNamespace(SbmlUtils.getDefaultSBMLNamespace());
                speciesReference.setId(iterator2.next());
                reaction.addProduct(speciesReference);
            }
            reactions.add(reaction);
        }
        return reactions;
    }

    /**
	 * 
	 * @param tableId
	 * @param recordId
	 * @return String
	 * @throws SQLException
	 */
    private String getName(final int tableId, final int recordId) throws SQLException {
        return (String) CollectionUtils.getFirst(getNames(tableId, recordId));
    }

    /**
	 * 
	 * @return Map
	 * @throws Exception
	 */
    private Map<Integer, SBase> getSpecies() throws Exception {
        if (species == null) {
            final String TABLE_NAME = "compound";
            species = getCollection(TABLE_NAME, true);
        }
        return species;
    }

    /**
	 * 
	 * @param tableName
	 * @param isSpecies
	 * @return Map
	 * @throws Exception
	 */
    private Map<Integer, SBase> getCollection(final String tableName, final boolean isSpecies) throws Exception {
        final int ID = 0;
        final SbmlUtils sbmlUtils = new SbmlUtils();
        final Map<Integer, SBase> collection = new HashMap<Integer, SBase>();
        final int tableId = getTable(tableName);
        final List<List<Object>> values = statementExecutor.getValues(tableName, new ArrayList<String>(), new ArrayList<String>());
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            final List<Object> row = iterator.next();
            final int id = ((Integer) row.get(ID)).intValue();
            final NamedSBase sbase = (isSpecies) ? new Species() : new Compartment();
            sbase.addNamespace(SbmlUtils.getDefaultSBMLNamespace());
            sbase.setId(getId(tableId, id));
            sbase.setName(getName(tableId, id));
            sbmlUtils.addOntologyTerms(sbase, getExternalLinks(tableId, id), CVTerm.Type.BIOLOGICAL_QUALIFIER, CVTerm.Qualifier.BQB_IS);
            collection.put(Integer.valueOf(id), sbase);
        }
        return collection;
    }

    /**
	 * 
	 * @param tableId
	 * @param id
	 * @return String
	 */
    private String getId(final int tableId, final int id) {
        return tableId + "_" + id;
    }

    /**
	 * 
	 * @param tableId
	 * @param recordId
	 * @return Collection
	 * @throws Exception
	 */
    private Collection<OntologyTerm> getExternalLinks(final int tableId, final int recordId) throws Exception {
        final int EXTERNAL_DATABASE_ID = 0;
        final int ONTOLOGY_TERM_ID = 1;
        final String EXTERNAL_LINKS_TABLE_NAME = "external_links";
        final String EXTERNAL_DATABASE_TABLE_NAME = "external_database";
        final String EXTERNAL_DATABASE_ID_FIELD = ".external_database_id";
        final String EXTERNAL_DATABASE_NAME_FIELD = ".database_name";
        final String EXTERNAL_ID_FIELD = ".external_id";
        final String KW_TABLE_ID_FIELD = ".kw_table_id";
        final String RECORD_ID_FIELD = ".record_id";
        final Collection<OntologyTerm> externalLinks = new ArrayList<OntologyTerm>();
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(EXTERNAL_LINKS_TABLE_NAME + KW_TABLE_ID_FIELD + "=" + tableId);
        conditions.add(EXTERNAL_LINKS_TABLE_NAME + RECORD_ID_FIELD + "=" + recordId);
        final Collection<List<Object>> values = statementExecutor.getValues(Arrays.asList(EXTERNAL_LINKS_TABLE_NAME + EXTERNAL_DATABASE_ID_FIELD, EXTERNAL_LINKS_TABLE_NAME + EXTERNAL_ID_FIELD), conditions);
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            final List<Object> row = iterator.next();
            final int externalDatabaseId = ((Integer) row.get(EXTERNAL_DATABASE_ID)).intValue();
            final String ontologyTermId = (String) row.get(ONTOLOGY_TERM_ID);
            conditions.clear();
            conditions.add(EXTERNAL_DATABASE_TABLE_NAME + EXTERNAL_DATABASE_ID_FIELD + "=" + externalDatabaseId);
            final String ontologyName = (String) CollectionUtils.getFirst(CollectionUtils.getFirst(statementExecutor.getValues(Arrays.asList(EXTERNAL_DATABASE_TABLE_NAME + EXTERNAL_DATABASE_NAME_FIELD), conditions)));
            externalLinks.add(OntologyUtils.getInstance().getOntologyTerm(ontologyName, ontologyTermId));
        }
        return externalLinks;
    }

    /**
	 *
	 * @param eventId
	 * @param reactant
	 * @return Collection
	 * @throws Exception
	 */
    private Collection<String> getEventComponentIds(final int eventId, final boolean reactant) throws Exception {
        final String TABLE_NAME = "event_component";
        final String EVENT_ID_FIELD = ".event_id";
        final String SUBSTRATE_ID_FIELD = ".is_substrate";
        final String RECORD_ID_FIELD = ".record_id";
        final String LOCATION_ID_FIELD = ".location_id";
        final String STOICHIOMETRY_ID_FIELD = ".stoichiometry";
        final String TABLE_ID_FIELD = ".kw_table_id";
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + EVENT_ID_FIELD + "=" + SqlUtils.getValueString(Integer.valueOf(eventId)));
        conditions.add(TABLE_NAME + SUBSTRATE_ID_FIELD + "=" + SqlUtils.getValueString(reactant ? "Y" : "N"));
        final Collection<List<Object>> values = statementExecutor.getValues(Arrays.asList(TABLE_NAME + TABLE_ID_FIELD, TABLE_NAME + RECORD_ID_FIELD, TABLE_NAME + LOCATION_ID_FIELD, TABLE_NAME + STOICHIOMETRY_ID_FIELD), conditions);
        final Set<String> eventComponentIds = new TreeSet<String>();
        for (Iterator<List<Object>> iterator = values.iterator(); iterator.hasNext(); ) {
            final List<Object> row = iterator.next();
            eventComponentIds.add(Arrays.toString(row.toArray()));
        }
        return eventComponentIds;
    }

    /**
	 * 
	 * @param eventTypeName
	 * @return int
	 * @throws SQLException
	 */
    private int getEventTypeId(final String eventTypeName) throws SQLException {
        final String TABLE_NAME = "event_type";
        final String NAME_FIELD = ".event_type_name";
        final String ID_FIELD = ".event_type_id";
        final Collection<String> conditions = new ArrayList<String>();
        conditions.add(TABLE_NAME + NAME_FIELD + "=" + SqlUtils.getValueString(eventTypeName));
        final Collection<List<Object>> values = statementExecutor.getValues(Arrays.asList(TABLE_NAME + ID_FIELD), conditions);
        return ((Integer) CollectionUtils.getFirst(CollectionUtils.getFirst(values))).intValue();
    }
}
