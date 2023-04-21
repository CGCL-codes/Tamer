package uk.ac.roslin.ensembl.dao.database;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import uk.ac.roslin.ensembl.datasourceaware.core.DAChromosome;
import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.database.Database;
import uk.ac.roslin.ensembl.model.database.DatabaseType;
import uk.ac.roslin.ensembl.model.core.CollectionSpecies;
import uk.ac.roslin.ensembl.config.EnsemblDBType;
import uk.ac.roslin.ensembl.exception.ConfigurationException;
import uk.ac.roslin.ensembl.model.database.CollectionCoreDatabase;

public class DBCollectionSpecies extends DBSpecies implements CollectionSpecies {

    private DBCollectionCoreDatabase mostRecentCoreDatabase = null;

    private TreeSet<DBCollectionDatabase> databases = new TreeSet<DBCollectionDatabase>();

    private TreeMap<String, Integer> versionIDmap = new TreeMap<String, Integer>();

    private TreeSet<String> allChromosomesRetrieved = new TreeSet<String>();

    public DBCollectionSpecies() {
        dbType = EnsemblDBType.collection_core;
    }

    public DBCollectionSpecies(DBCollectionCoreDatabase database) throws ConfigurationException {
        this();
        this.databases.add(database);
        this.registry = database.getRegistry();
        this.mostRecentEnsemblSchemaVersion = registry.getMostRecentEnsemblVersion();
        if (database.getIntDBVersion() > this.highestDBRelease) {
            this.highestDBRelease = database.getIntDBVersion();
            this.mostRecentCoreDatabase = database;
        }
    }

    public void setIDForVersion(Integer ID, String version) {
        this.versionIDmap.put(version, ID);
    }

    @Override
    public void setProperty(HashMap row) {
        boolean oldStyleKeys = true;
        String key = (String) row.get("meta_key");
        String value = (String) row.get("meta_value");
        Integer version = Integer.parseInt((String) row.get("schemaVersion"));
        if (version != null && version >= 58) {
            oldStyleKeys = false;
        }
        if (oldStyleKeys) {
            if (key.equals("species.db_name")) {
                this.speciesBinomial = value;
                aliases.add(value);
            } else if (key.equals("species.compara_name")) {
                this.databaseStyleSpeciesName = value;
                this.comparaName = value;
            }
        } else {
            if (key.equals("species.production_name")) {
                this.databaseStyleSpeciesName = value;
                this.comparaName = value;
                aliases.add(value);
            } else if (key.equals("species.scientific_name")) {
                this.speciesBinomial = value;
                aliases.add(value);
            } else if (key.equals("species.short_name")) {
                this.shortName = value;
                aliases.add(value);
            } else if (key.equals("species.division")) {
                this.setComparaDivision(value);
            }
        }
        if (key.equals("species.stable_id_prefix")) {
            this.ensemblStablePrefix = value;
        } else if (key.equals("species.common_name") || key.equals("species.ensembl_common_name")) {
            aliases.add(value);
            this.commonName = value;
        } else if (key.equals("species.taxonomy_id")) {
            aliases.add(value);
            this.taxonomyID = value;
        } else if (key.equals("species.alias") || key.equals("species.file_name") || key.equals("species.compara_name") || key.equals("species.sql_name") || key.equals("species.ensembl_alias_name")) {
            aliases.add(value);
        }
    }

    @Override
    public Integer getDBSpeciesID(String version) {
        return this.versionIDmap.get(version);
    }

    @Override
    public int compareTo(CollectionSpecies o) {
        if (this.getDatabaseStyleName() != null && o != null && o.getDatabaseStyleName() != null) {
            return this.getDatabaseStyleName().compareTo(((DBCollectionSpecies) o).getDatabaseStyleName());
        } else {
            if (this.getDatabaseStyleName() != null) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    @Override
    public DBCollectionCoreDatabase getMostRecentCoreDatabase() {
        return (DBCollectionCoreDatabase) this.mostRecentCoreDatabase;
    }

    public void setMostRecentCoreDatabase(DBCollectionCoreDatabase currentCoreDatabase) {
        this.mostRecentCoreDatabase = currentCoreDatabase;
    }

    @Override
    public TreeSet<DBCollectionDatabase> getDatabases() {
        return this.databases;
    }

    @Override
    public void addDatabases(TreeSet<? extends Database> databases) {
        for (Database d : databases) {
            this.addDatabase(d);
        }
    }

    @Override
    public void addDatabase(Database database) {
        try {
            this.databases.add((DBCollectionDatabase) database);
            if (database.getIntDBVersion() > this.highestDBRelease && database.getType().equals(EnsemblDBType.collection_core)) {
                this.highestDBRelease = database.getIntDBVersion();
                this.mostRecentCoreDatabase = (DBCollectionCoreDatabase) database;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public TreeSet<DBCollectionDatabase> getDatabasesByType(DatabaseType type) {
        TreeSet<DBCollectionDatabase> out = new TreeSet<DBCollectionDatabase>();
        for (DBCollectionDatabase d : databases) {
            if (d.getType() == type) {
                out.add(d);
            }
        }
        return out;
    }

    @Override
    public TreeSet<DBCollectionDatabase> getDatabasesByVersion(String version) {
        TreeSet<DBCollectionDatabase> out = new TreeSet<DBCollectionDatabase>();
        for (DBCollectionDatabase d : databases) {
            if (d.getDBVersion().equals(version)) {
                out.add(d);
            }
        }
        return out;
    }

    @Override
    public DBCollectionDatabase getDatabaseByTypeAndVersion(DatabaseType type, String version) {
        for (DBCollectionDatabase d : databases) {
            if (d.getDBVersion().equalsIgnoreCase(version) && d.getType() == type) {
                return d;
            }
        }
        return null;
    }

    @Override
    public DAChromosome getChromosomeByName(String name, String dbVersion) throws DAOException {
        DAChromosome chr = null;
        if (dbVersion == null || dbVersion.isEmpty()) {
            dbVersion = this.highestDBRelease.toString();
        }
        if (!this.versionIDmap.containsKey(dbVersion)) {
            throw new DAOException("No version " + dbVersion + " for " + this.toString());
        }
        if (!chromosomes.containsKey(dbVersion)) {
            chromosomes.put(dbVersion, new TreeMap<String, DAChromosome>());
        }
        if (chromosomes.get(dbVersion).containsKey(name)) {
            return chromosomes.get(dbVersion).get(name);
        } else {
            chr = ((DBCollectionCoreDatabase) this.getDatabaseByTypeAndVersion(EnsemblDBType.collection_core, dbVersion)).getChromosomeByName(this, name);
        }
        if (chr != null) {
            chromosomes.get(dbVersion).put(name, chr);
        }
        return chr;
    }

    @Override
    public TreeMap<String, DAChromosome> getChromosomes(String dbVersion) throws DAOException {
        if (dbVersion == null || dbVersion.isEmpty()) {
            dbVersion = this.highestDBRelease.toString();
        }
        if (!this.versionIDmap.containsKey(dbVersion)) {
            throw new DAOException("No version " + dbVersion + " for " + this.toString());
        }
        if (this.allChromosomesRetrieved.contains(dbVersion)) {
            return this.chromosomes.get(dbVersion);
        }
        List<DAChromosome> chrs = null;
        if (!this.chromosomes.containsKey(dbVersion)) {
            this.chromosomes.put(dbVersion, new TreeMap<String, DAChromosome>());
        }
        chrs = ((DBCollectionCoreDatabase) this.getDatabaseByTypeAndVersion(EnsemblDBType.collection_core, dbVersion)).getChromosomes(this);
        this.allChromosomesRetrieved.add(dbVersion);
        if (chrs != null) {
            for (DAChromosome c : chrs) {
                if (!chromosomes.get(dbVersion).containsKey(c.getChromosomeName())) {
                    chromosomes.get(dbVersion).put(c.getChromosomeName(), c);
                }
            }
        }
        return this.chromosomes.get(dbVersion);
    }

    @Override
    public DAGene getGeneByID(Integer id, String dbVersion) throws DAOException {
        DAGene gene = null;
        if (dbVersion == null || dbVersion.isEmpty()) {
            dbVersion = this.highestDBRelease.toString();
        }
        try {
            gene = ((DBCollectionCoreDatabase) this.getDatabaseByTypeAndVersion(EnsemblDBType.collection_core, dbVersion)).getCoreFactory(this).getGeneDAO().getGeneByID(id);
        } catch (Exception e) {
            if (e instanceof DAOException) {
                throw (DAOException) e;
            } else {
                throw new DAOException(e);
            }
        }
        return gene;
    }

    @Override
    public DAGene getGeneByStableID(String stableID, String dbVersion) throws DAOException {
        DAGene gene = null;
        if (dbVersion == null || dbVersion.isEmpty()) {
            dbVersion = this.highestDBRelease.toString();
        }
        try {
            gene = ((DBCollectionCoreDatabase) this.getDatabaseByTypeAndVersion(EnsemblDBType.collection_core, dbVersion)).getCoreFactory(this).getGeneDAO().getGeneByStableID(stableID.trim());
        } catch (Exception e) {
            if (e instanceof DAOException) {
                throw (DAOException) e;
            } else {
                throw new DAOException(e);
            }
        }
        return gene;
    }

    @Override
    public String getAssembly() {
        return this.getAssembly(null);
    }

    @Override
    public String getAssembly(String dbVersion) {
        if (dbVersion == null || dbVersion.isEmpty()) {
            dbVersion = highestDBRelease.toString();
        }
        if (this.getDatabaseByTypeAndVersion(EnsemblDBType.collection_core, dbVersion) != null) {
            return ((CollectionCoreDatabase) this.getDatabaseByTypeAndVersion(EnsemblDBType.collection_core, dbVersion)).getAssembly(this);
        } else {
            return null;
        }
    }
}
