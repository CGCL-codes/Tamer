package org.qfab.EBIRDACompA.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.qfab.EBIRDACompA.DBObject;
import org.qfab.EBIRDACompA.Logger;
import org.qfab.util.PropertyLoader;

/**
 * This object represents the ebiCollection MySQL database table. This table
 * contains the information about the collection that will be used to generate RIFCS 
 * compliant Research Data Australia (RDA) collections
 * @author f.newell, p.chaumeil
 *
 */
public class EBICollection {

    private static Logger logger = Logger.getLogger();

    private Long id;

    private Long parentId;

    private String name;

    private String rank;

    private boolean isInNCBI;

    private List<EBICollection> childCollections;

    private List<Synonym> synonyms;

    private boolean isQueryItem = false;

    private boolean hasEbiData = false;

    private boolean isOrphan = false;

    private boolean isComplete = false;

    private Long numberUniprotRecords = new Long(0);

    private Long numberENARecords = new Long(0);

    private String nlaIdentifier;

    private List<EBIRecord> ebiRecords = new ArrayList<EBIRecord>();

    public EBICollection() {
    }

    public EBICollection(String identifier) throws Exception {
        this.name = identifier;
        this.populateByName();
    }

    public EBICollection(Long id) throws Exception {
        this.id = id;
        this.populateById();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public boolean isInNCBI() {
        return isInNCBI;
    }

    public void setInNCBI(boolean isInNCBI) {
        this.isInNCBI = isInNCBI;
    }

    public boolean getIsQueryItem() {
        return isQueryItem;
    }

    public void setIsQueryItem(boolean isQueryItem) {
        this.isQueryItem = isQueryItem;
    }

    public boolean getHasEbiData() {
        return hasEbiData;
    }

    public void setHasEbiData(boolean hasEbiData) {
        this.hasEbiData = hasEbiData;
    }

    public boolean getIsOrphan() {
        return isOrphan;
    }

    public void setIsOrphan(boolean isOrphan) {
        this.isOrphan = isOrphan;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public void setNlaIdentifier(String nlaIdentifier) {
        this.nlaIdentifier = nlaIdentifier;
    }

    public String getNlaIdentifier() {
        return nlaIdentifier;
    }

    public Long getNumberUniprotRecords() {
        return this.numberUniprotRecords;
    }

    public void setNumberUniprotRecords(Long numberUniprotRecords) {
        this.numberUniprotRecords = numberUniprotRecords;
    }

    public Long getNumberENARecords() {
        return this.numberENARecords;
    }

    public void setNumberENARecords(Long numberENARecords) {
        this.numberENARecords = numberENARecords;
    }

    /**
	 * This function finds and returns the synonym marked as preferred
	 * @return
	 * @throws Exception
	 */
    public String getPreferredSynonym() throws Exception {
        String retValue = "";
        for (Synonym synonym : this.getSynonyms()) {
            if (synonym.isPreferred()) {
                retValue = synonym.getSynonym();
            }
        }
        return retValue;
    }

    /**
	 * This function returns a string that contains a list of synonyms.  The synonyms
	 * are separated by the ; character
	 * @return
	 * @throws Exception
	 */
    public String getSynonymList() throws Exception {
        String synonymString = "  ";
        List<Synonym> synonyms = this.getSynonyms();
        for (Synonym s : synonyms) {
            synonymString += s.getSynonym() + "; ";
        }
        return synonymString.substring(0, synonymString.length() - 2);
    }

    /**
	 * Gets the EBIRecords associated records for an EBICollection
	 * @return a list of associated records that contain EBI data
	 * @throws Exception
	 */
    public List<EBIRecord> getEbiRecords() throws Exception {
        getAllEbiRecords("all");
        return this.ebiRecords;
    }

    /**
	 * Gets the EBIRecords associated records for an EBICollection
	 * @param type either "all","uniprot" or "ena" 
	 * @return a list of associated records that contain EBI data
	 * @throws Exception
	 */
    public List<EBIRecord> getEbiRecords(String type) throws Exception {
        getAllEbiRecords(type);
        return this.ebiRecords;
    }

    /**
	 * Retrieves all ebiRecords for the current EBICollection from the database
	 * @throws Exception
	 */
    private void getAllEbiRecords(String type) throws Exception {
        ebiRecords = new ArrayList<EBIRecord>();
        String sql = new String();
        if (type.equals("all")) {
            sql = "SELECT * FROM ebiRecord where ebiCollectionId = " + this.id + " ORDER BY ebiDatabaseId, accessionNumber";
        } else if (type.equals("uniprot")) {
            sql = "SELECT * FROM ebiRecord where ebiCollectionId = " + this.id + " and (ebiDatabaseId=3 or ebiDatabaseId=4) ORDER BY ebiDatabaseId, accessionNumber";
        } else if (type.equals("ena")) {
            sql = "SELECT * FROM ebiRecord where ebiCollectionId = " + this.id + " and (ebiDatabaseId=1 or ebiDatabaseId=2)  ORDER BY ebiDatabaseId, accessionNumber";
        }
        DBObject dbo = new DBObject();
        ResultSet resultSet = dbo.sqlExecuteSelect(sql);
        while (resultSet.next()) {
            EBIRecord record = new EBIRecord();
            record.setId(resultSet.getLong("id"));
            record.setAccessionNumber(resultSet.getString("accessionNumber"));
            record.setSequenceHeader(resultSet.getString("sequenceHeader"));
            record.setEbiDatabaseId(resultSet.getLong("ebiDatabaseId"));
            record.setEbiCollectionId(resultSet.getLong("ebiCollectionId"));
            record.setUrl(resultSet.getString("ebiUrl"));
            ebiRecords.add(record);
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
    }

    /**
	 * Retrieves all the childCollections for the current object
	 * @return the childCollections
	 * @throws Exception 
	 */
    public List<EBICollection> getChildCollections() throws Exception {
        String sql = "SELECT * FROM ebiCollection where parentId = " + this.id + " ORDER BY name";
        this.childCollections = executeSelect(sql);
        return this.childCollections;
    }

    /**
	 * Retrieves all the associated synonyms for the collection and populated Synonym objects with data from 
	 * the database
	 * @return the synonyms
	 * @throws Exception 
	 */
    public List<Synonym> getSynonyms() throws Exception {
        this.synonyms = new ArrayList<Synonym>();
        String sql = "SELECT * FROM synonym where ebiCollectionId = " + this.id;
        DBObject dbo = new DBObject();
        ResultSet resultSet = dbo.sqlExecuteSelect(sql);
        while (resultSet.next()) {
            Synonym syn = new Synonym();
            syn.setId(resultSet.getLong("id"));
            syn.setPreferred(resultSet.getBoolean(5));
            syn.setEbiCollectionId(resultSet.getLong("ebiCollectionId"));
            syn.setSynonym(resultSet.getString("synonym"));
            syn.setType(resultSet.getString("type"));
            synonyms.add(syn);
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
        return this.synonyms;
    }

    public void setSynonyms(ArrayList<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    /**
	 * Changes the name of the collection in the database
	 * @return
	 * @throws Exception
	 */
    public void updateName() throws Exception {
        DBObject dbo = new DBObject();
        String sql = "UPDATE ebiCollection set name = \"" + this.name + "\" where id = " + this.id;
        dbo.sqlUpdateQuery(sql);
        dbo.closeConnection();
    }

    /**
	 * Saves the object to the database
	 * @return the id of the collection
	 * @throws Exception
	 */
    public Long saveToDB() throws Exception {
        DBObject dbo = new DBObject();
        String collectionSql = "";
        String action = "";
        String where = "";
        if (this.exists()) {
            action = " UPDATE ";
            where = " WHERE id = " + this.id;
        } else {
            action = "INSERT ";
        }
        collectionSql += action + " ebiCollection SET name=\"" + this.name + "\", rank=\"" + this.rank + "\",parentId=" + this.parentId + ",isInNCBI=" + this.isInNCBI + ",isQueryItem=" + this.isQueryItem + ",hasEbiData=" + this.hasEbiData + ",isOrphan=" + this.isOrphan + ",isComplete=" + this.isComplete + ",numberUniprotRecords=" + this.numberUniprotRecords + ",numberENARecords=" + this.numberENARecords + ",nlaIdentifier=\"" + this.nlaIdentifier + "\"" + where;
        if (this.id != null) {
            dbo.sqlUpdateQuery(collectionSql);
        } else {
            id = dbo.sqlInsertQuery(collectionSql);
        }
        return this.id;
    }

    /**
	 * Checks if the object already exists in the database by using the name of the 
	 * collection
	 * @return true if the collection exists, false if it doesn't exist
	 * @throws Exception
	 */
    public boolean exists() throws Exception {
        boolean exists = false;
        DBObject dbo = new DBObject();
        String query = "SELECT id FROM ebiCollection WHERE name =\"" + this.name + "\"";
        ResultSet resultSet = dbo.sqlExecuteSelect(query);
        if (resultSet.next()) {
            this.id = resultSet.getLong("id");
            exists = true;
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
        return exists;
    }

    /**
	 * Populates the current EBICollection information with data from the database 
	 * using the name to retrieve the data
	 * @throws Exception
	 */
    public void populateByName() throws Exception {
        DBObject dbo = new DBObject();
        String select = "SELECT * from ebiCollection where name like \"" + this.name + "\"";
        ResultSet resultSet = dbo.sqlExecuteSelect(select);
        if (resultSet.next()) {
            this.setId(resultSet.getLong("id"));
            this.setRank(resultSet.getString("rank"));
            Long parentId = resultSet.getLong("parentId");
            if (parentId != null && parentId == 0) {
                parentId = null;
            }
            this.setParentId(parentId);
            this.setInNCBI(resultSet.getBoolean(5));
            this.setIsQueryItem(resultSet.getBoolean(6));
            this.setHasEbiData(resultSet.getBoolean(7));
            this.setIsOrphan(resultSet.getBoolean(8));
            this.setIsComplete(resultSet.getBoolean(9));
            this.setNlaIdentifier(resultSet.getString("nlaIdentifier"));
            this.setNumberUniprotRecords(resultSet.getLong("numberUniprotRecords"));
            this.setNumberENARecords(resultSet.getLong("numberENARecords"));
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
    }

    /**
	 * Populates the current EBICollection information with data from the database 
	 * using the id to retrieve the data
	 * @throws Exception
	 */
    public void populateById() throws Exception {
        DBObject dbo = new DBObject();
        String select = "select * from ebiCollection where id = " + this.id;
        ResultSet resultSet = dbo.sqlExecuteSelect(select);
        if (resultSet.next()) {
            this.setName(resultSet.getString("name"));
            this.setRank(resultSet.getString("rank"));
            this.setParentId(resultSet.getLong("parentId"));
            Long parentId = resultSet.getLong("parentId");
            if (parentId != null && parentId == 0) {
                parentId = null;
            }
            this.setParentId(parentId);
            this.setInNCBI(resultSet.getBoolean(5));
            this.setIsQueryItem(resultSet.getBoolean(6));
            this.setHasEbiData(resultSet.getBoolean(7));
            this.setIsOrphan(resultSet.getBoolean(8));
            this.setIsComplete(resultSet.getBoolean(9));
            this.setNlaIdentifier(resultSet.getString("nlaIdentifier"));
            this.setNumberUniprotRecords(resultSet.getLong("numberUniprotRecords"));
            this.setNumberENARecords(resultSet.getLong("numberENARecords"));
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
    }

    /**
	 * Retrieves all the childCollections recursively for the current object for all the rank under the EBICollection Rank
	 * @return the childCollections
	 * @throws Exception 
	 */
    public List<EBICollection> getAllChildCollectionsRecursive() throws Exception {
        this.childCollections = new ArrayList<EBICollection>();
        ArrayList<EBICollection> childCollectionsTemp = new ArrayList<EBICollection>();
        String sql = "SELECT * FROM ebiCollection where parentId = " + this.id;
        DBObject dbo = new DBObject();
        ResultSet resultSet = dbo.sqlExecuteSelect(sql);
        while (resultSet.next()) {
            EBICollection collection = new EBICollection();
            collection.populateEBICollection(resultSet);
            childCollections.add(collection);
            childCollectionsTemp.add(collection);
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
        for (EBICollection childcollection : childCollections) {
            Boolean isLeaf = new Boolean(childcollection.isQueryItem);
            if (isLeaf.equals(false)) {
                childCollectionsTemp.addAll(childcollection.getAllChildCollectionsRecursive());
            }
        }
        return childCollectionsTemp;
    }

    /**
	 * Retrieves all the Query Item childCollections recursively for the current object for all the rank under the EBICollection Rank
	 * @return the childCollections
	 * @throws Exception 
	 */
    public List<EBICollection> getAllQueryItemsFromCollection() throws Exception {
        this.childCollections = new ArrayList<EBICollection>();
        ArrayList<EBICollection> childCollectionsList = new ArrayList<EBICollection>();
        String sql = "SELECT * FROM ebiCollection where parentId = " + this.id;
        DBObject dbo = new DBObject();
        ResultSet resultSet = dbo.sqlExecuteSelect(sql);
        while (resultSet.next()) {
            EBICollection collection = new EBICollection();
            collection.populateEBICollection(resultSet);
            childCollections.add(collection);
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
        for (EBICollection childcollection : childCollections) {
            if (childcollection.getIsQueryItem()) {
                childCollectionsList.add(childcollection);
            } else if (!childcollection.getIsQueryItem()) {
                childCollectionsList.addAll(childcollection.getAllQueryItemsFromCollection());
            }
        }
        return childCollectionsList;
    }

    /**
	 * Retrieves all the Query Item childCollections recursively for the current object for all the rank under the EBICollection Rank
	 * @return the childCollections
	 * @throws Exception 
	 */
    public ArrayList<EBICollection> getAllRootCollections(String type) throws Exception {
        String sql = "";
        if (type.equals("species")) {
            sql = "SELECT * FROM ebiCollection WHERE parentId IS NULL AND (rank = 'Kingdom' OR (rank = 'Phylum' && parentId is null))";
        }
        if (type.equals("researcher")) {
            sql = "SELECT * FROM ebiCollection WHERE parentId IS NULL AND (rank = 'country')";
        }
        List<EBICollection> rootCollections = executeSelect(sql);
        return (ArrayList<EBICollection>) rootCollections;
    }

    /**
	 * Retrieves all the Query Item childCollections recursively for the current object for all the rank under the EBICollection Rank
	 * @return the childCollections
	 * @throws Exception 
	 */
    public ArrayList<EBICollection> getAllRootCollections() throws Exception {
        String sql = "SELECT * FROM ebiCollection WHERE parentId IS NULL AND (rank = 'Kingdom' OR (rank = 'Phylum' && parentId is null))";
        List<EBICollection> rootCollections = executeSelect(sql);
        return (ArrayList<EBICollection>) rootCollections;
    }

    /**
	 * This function returns the count of child objects if its a query item it returns the ebi data count
	 * @return
	 * @throws Exception
	 */
    public int getChildCount() throws Exception {
        int retValue = 0;
        if (this.isQueryItem) {
            retValue = this.getEbiRecords().size();
        } else {
            retValue = this.getChildCollections().size();
        }
        return retValue;
    }

    /**
	 * This function returns the count of child query items 
	 * @return
	 * @throws Exception
	 */
    public int getChildQueryItemCount() throws Exception {
        int retValue = 0;
        if (this.isQueryItem) {
            retValue++;
        } else {
            for (EBICollection childCollection : this.getChildCollections()) {
                retValue += childCollection.getChildQueryItemCount();
            }
        }
        return retValue;
    }

    /**
	 * This function counts up all the child objects recursively
	 * @return
	 * @throws Exception
	 */
    public int getTotalChildCount() throws Exception {
        int retValue = 0;
        if (this.isQueryItem) {
            retValue = this.getEbiRecords().size();
        } else {
            retValue = this.getAllQueryItemsFromCollection().size();
        }
        return retValue;
    }

    /**
	 * This function returns an array of collections from the database.  Input string must be SQL (e.g. SELECT * FROM ebiCollection)
	 * @param sql
	 * @return
	 * @throws Exception
	 */
    public List<EBICollection> executeSelect(String sql) throws Exception {
        List<EBICollection> collections = new ArrayList<EBICollection>();
        DBObject dbo = new DBObject();
        ResultSet resultSet = dbo.sqlExecuteSelect(sql);
        while (resultSet.next()) {
            EBICollection collection = new EBICollection();
            collection.populateEBICollection(resultSet);
            collections.add(collection);
        }
        resultSet.getStatement().close();
        resultSet.close();
        dbo.closeConnection();
        return collections;
    }

    /**
	 * Populates an EBICollection object with data retrieved from the database
	 * @return list of ebi collections from the database
	 * @throws Exception
	 */
    public void populateEBICollection(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getLong("id"));
        Long parentId = resultSet.getLong("parentId");
        if (parentId != null && parentId == 0) {
            parentId = null;
        }
        this.setParentId(parentId);
        this.setParentId(resultSet.getLong("parentId"));
        this.setName(resultSet.getString("name"));
        this.setRank(resultSet.getString("rank"));
        this.setInNCBI(resultSet.getBoolean(5));
        this.setIsQueryItem(resultSet.getBoolean(6));
        this.setHasEbiData(resultSet.getBoolean(7));
        this.setIsOrphan(resultSet.getBoolean(8));
        this.setIsComplete(resultSet.getBoolean(9));
        this.setNlaIdentifier(resultSet.getString("nlaIdentifier"));
        this.setNumberUniprotRecords(resultSet.getLong("numberUniprotRecords"));
        this.setNumberENARecords(resultSet.getLong("numberENARecords"));
    }

    /**
	 * This function returns the description of the collection to be used in the RDA record and landing page
	 * @return
	 * @throws Exception
	 */
    public String getFullDescription() throws Exception {
        String fullDescription = "";
        if (this.isResearcherCollection()) {
            fullDescription = "This data collection contains all currently published nucleotide (DNA/RNA) " + "and protein sequences from " + getResearcherPhrase() + ".";
            fullDescription += getDatabaseBlurb();
        } else if (this.isOrganismCollection()) {
            String noRankSwitch = "";
            String isSpeciesSwitch1 = "Australian dwelling organisms in ";
            String isSpeciesSwitch2 = "species in";
            String isSpeciesSwitch3 = "Australian dwelling organisms";
            if (this.getIsQueryItem()) {
                isSpeciesSwitch1 = "the Australian dwelling organism";
                isSpeciesSwitch2 = "the species";
                isSpeciesSwitch3 = "an Australian dwelling organism";
            } else {
                if (!(this.getRank().equalsIgnoreCase("no rank"))) {
                    noRankSwitch = " the " + this.getRank();
                }
            }
            fullDescription = "This data collection contains all currently published nucleotide (DNA/RNA) " + "and protein sequences from " + isSpeciesSwitch1 + " " + noRankSwitch + " " + this.getName();
            if (!this.getPreferredSynonym().equals("")) {
                fullDescription += ", " + "commonly known as " + this.getPreferredSynonym();
            }
            fullDescription += getSynonymDescription();
            fullDescription += getDatabaseBlurb();
            fullDescription += "The identification of " + isSpeciesSwitch2 + " " + this.getName() + " as " + isSpeciesSwitch3 + " has been achieved by accessing the Australian Plant Census (APC) or Australian Faunal Directory (AFD) through the Atlas of Living Australia.";
        }
        return fullDescription;
    }

    /**
	 * This function returns a string that describes the synonyms based on the synonym type
	 * @return
	 * @throws Exception
	 */
    private String getSynonymDescription() throws Exception {
        String description = "";
        for (Synonym s : this.getSynonyms()) {
            if (s.getType().equals("ala description")) {
                description += ". Other information about this group: " + s.getSynonym() + ".\n\n";
            }
        }
        if (description.equals("")) {
            description += ".\n\n";
        }
        return description;
    }

    /**
	 * This function returns a phrase that describes the researcher item based on the rank. 
	 * @return
	 * @throws Exception
	 */
    private String getResearcherPhrase() throws Exception {
        String phrase = "";
        if (this.getRank().equals("country")) {
            phrase = "Australian Research Institutions";
        } else if (this.getRank().equals("state")) {
            phrase = "Australian Research Institutions in " + this.getName() + " (" + this.getPreferredSynonym() + ")";
        } else if (this.getRank().equals("researcher")) {
            phrase = " the Australian research institution " + this.getName();
        }
        return phrase;
    }

    /**
	 * This function returns a phrase used in teh RDA records and landing page to describe the 
	 * Database sources.
	 * @return
	 */
    private String getDatabaseBlurb() {
        String description = "";
        description += "The nucleotide (DNA/RNA) and protein sequences have been sourced through " + "the European Nucleotide Archive (ENA) and Universal Protein Resource (UniProt), " + "databases that contains comprehensive sets of nucleotide (DNA/RNA) " + "and protein sequences from all organisms that have been published by the International " + "Research Community.\n\n";
        return description;
    }

    /**
	 * This function returns true if the collection is categorised as a researcher type (based on the rank
	 * @return true/false
	 */
    public boolean isResearcherCollection() {
        boolean retValue = false;
        if (this.rank.equalsIgnoreCase("state") || this.rank.equalsIgnoreCase("researcher") || this.rank.equalsIgnoreCase("country")) {
            retValue = true;
        }
        return retValue;
    }

    /**
	 * This function returns true if the collection is not a researcher type
	 * @return true/false
	 */
    public boolean isOrganismCollection() {
        boolean retValue = false;
        if (!(this.isResearcherCollection())) {
            retValue = true;
        }
        return retValue;
    }

    /**
	 * This function returns a phrase used in the RDA and landing page about the access rights
	 * @return
	 */
    public String getAccessRights() {
        String accessRights = "This data is freely available to any individual for any purpose. " + "For more information see http:www.ebi.ac.uk/Information/termsofuse.html";
        return accessRights;
    }

    /**
	 * This function returns a string containing the URL of the landing page which is pulled from
	 * the config.properties file
	 * @return
	 */
    public String getLandingPageURL() {
        Properties props = PropertyLoader.loadProperties("config.properties");
        String landingpage = props.getProperty("rifcs.landingpage");
        String landingPageURL = landingpage + this.getId() + "&view=preview";
        return landingPageURL;
    }

    /**
	 * This function returns the string used as the primary name in the RDA and landing page
	 * @return
	 * @throws Exception
	 */
    public String getPrimaryName() throws Exception {
        String primaryName = "Nucleotide (DNA / RNA) and Protein sequences from ";
        if (this.isResearcherCollection()) {
            primaryName += getResearcherPhrase();
        } else if (this.isOrganismCollection()) {
            primaryName += getOrganismPhrase();
        }
        return primaryName;
    }

    /**
	 * This function returns a phrase used in the RDA and landing page to describe the organism
	 * @return
	 * @throws Exception
	 */
    private String getOrganismPhrase() throws Exception {
        String phrase = "";
        String noRankSwitch = "";
        String isSpeciesSwitch = "Australian dwelling organisms in";
        if (this.getIsQueryItem()) {
            isSpeciesSwitch = "the Australian dwelling species ";
        } else {
            if (!(this.getRank().equalsIgnoreCase("no rank"))) {
                noRankSwitch = " the " + this.getRank();
            }
        }
        phrase += isSpeciesSwitch + " " + noRankSwitch + " " + this.getName();
        if (!(this.getPreferredSynonym().equalsIgnoreCase(""))) {
            phrase += " (" + this.getPreferredSynonym() + ")";
        }
        return phrase;
    }

    /**
	 * Used when generating RIFCS to determine if the collection has EBI data.
	 * @return true when one query item has at least one EBIRecord, returns false if no query items have EBIRecords 
	 * @throws Exception 
	 */
    public boolean hasQueryItemData() throws Exception {
        boolean hasQueryItemData = false;
        if (this.isQueryItem) {
            if (this.getEbiRecords().size() > 0) {
                hasQueryItemData = true;
            }
        } else {
            List<EBICollection> queryItems = this.getAllQueryItemsFromCollection();
            for (EBICollection queryItem : queryItems) {
                if (queryItem.hasEbiData) {
                    hasQueryItemData = true;
                    break;
                }
            }
        }
        return hasQueryItemData;
    }

    /**
	 * This function returns a note that is used in the RDA and landing page
	 * @return
	 */
    public String getNote() {
        String note = "The contents of this collection is dynamic and will change over time " + "as more data is deposited into the European Nucleotide Archive (ENA) " + "and Universal Protein Resource (UniProt).\n\n";
        if (this.isQueryItem) {
            note += "The set of nucleotide (DNA/RNA) and protein sequences in this collection " + "is not necessarily comprehensive. It contains only nucleotide (DNA/RNA) and " + "protein sequences that have been published in the European Nucleotide Archive (ENA) " + "and Universal Protein Resource (UniProt). The results have been returned using " + "the exact search term " + this.getName() + ". The current version of the system does " + "not allow for typographical errors or synonyms.";
        }
        return note;
    }

    /**
	 * This function returns the Synonym that is of type "imported name"
	 * @return Synonym
	 * @throws Exception
	 */
    public Synonym getImportedNameSynonym() throws Exception {
        Synonym s = null;
        List<Synonym> syns = this.getSynonyms();
        for (Synonym syn : syns) {
            if (syn.getType().equalsIgnoreCase("imported name")) {
                s = syn;
            }
        }
        return s;
    }

    /**
	 * Check if the name of the collection has been changed to a synonym and returns the imported synonym if found
	 * @return
	 * @throws Exception 
	 */
    public String getNameChange() throws Exception {
        Synonym s = new Synonym();
        String name = s.getImportedSynonym(this);
        return name;
    }

    /**
	 * This function is used in the landing page for writing out the header for the count of records
	 * @return String
	 */
    public String getNumberTitle() {
        String numberTitle = "";
        if (this.getRank().equals("country") || this.getRank().equals("state")) {
            numberTitle = "Number of research institutions";
        } else if (this.getRank().equals("researcher") || this.getRank().equals("species")) {
            numberTitle = "Number of EBI Records";
        } else {
            numberTitle = "Number of Species";
        }
        return numberTitle;
    }

    /**
	 * This function is used in the landing page for creating the expanded search URL
	 * @return
	 * @throws Exception
	 */
    public String getExpandedSearchURL() throws Exception {
        String url = "http://www.ebi.ac.uk/ebisearch/search.ebi?db=allebi&t=\"" + this.getName() + "\"";
        return url;
    }

    /**
	 * This function is used for creating the string that is used on the landing page for writing out the 
	 * preferred synonym
	 * @return
	 * @throws Exception
	 */
    public String getPreferredSynonymDisplay() throws Exception {
        String display = "";
        if (!this.getPreferredSynonym().equals("")) {
            display = "(AKA: " + this.getPreferredSynonym() + ") ";
        }
        return display;
    }

    /**
	 * This function is used in the landing page and writes out HTML based on the given type (ena, uniprot, all)
	 * @param type
	 * @return
	 * @throws Exception
	 */
    public String getRecordInformation(String type) throws Exception {
        String info = "<ul>";
        String uniprot = "";
        String ena = "";
        String displayRank = "";
        String enaUrl = "";
        String uniprotUrl = "";
        if (this.rank.equals("species")) {
            displayRank = "species";
            enaUrl = "http://www.ebi.ac.uk/ebisearch/search.ebi?db=emblrelease_standard&t=organism_species%3A(" + this.name + ")";
            uniprotUrl = "http://www.ebi.ac.uk/ebisearch/search.ebi?db=uniprot&t=organism_species%3A(" + this.name + ")";
        } else if (this.rank.equals("researcher")) {
            displayRank = "research instituion";
            enaUrl = "http://www.ebi.ac.uk/ebisearch/search.ebi?db=emblrelease_standard&t=references%3A(" + this.name + ")";
            uniprotUrl = "http://www.ebi.ac.uk/ebisearch/search.ebi?db=uniprot&t=references%3A(" + this.name + ")";
        }
        int currentRecords = this.getEbiRecords("ena").size();
        if (currentRecords == 100) {
            ena += "<li><span style='font-weight:bold'>DNA Records:</span> ";
            ena += "The first 100 DNA records for this " + displayRank + " are displayed. Other records (total: " + this.numberENARecords + ")";
            ena += " can be viewed at <a href='" + enaUrl + "' target='_blank'>EBI</a>";
            ena += "</li>";
        }
        int currentRecordsUni = this.getEbiRecords("uniprot").size();
        if (currentRecordsUni == 100) {
            uniprot += "<li><span style='font-weight:bold'>Protein Records:</span> ";
            uniprot += "The first 100 protein records for this " + displayRank + " are displayed. Other records (total: " + this.numberUniprotRecords + ")";
            uniprot += " can be viewed at <a href='" + uniprotUrl + "' target='_blank'>EBI</a>";
            uniprot += "</li>";
        }
        if (type.equals("all")) {
            if (currentRecords == 100 && currentRecordsUni < 100) {
                uniprot += "<li>All available protein records are displayed</li>";
            }
            if (currentRecords < 100 && currentRecordsUni == 100) {
                uniprot += "<li>All available DNA records are displayed</li>";
            }
            info += ena + uniprot;
        } else if (type.equals("uniprot")) {
            info += uniprot;
        } else if (type.equals("ena")) {
            info += ena;
        }
        info += "</ul>";
        return info;
    }

    /**
	 * This function is used for creating log messages.
	 * @param message
	 * @return
	 */
    public String logMessage(String message) {
        logger.message(message);
        return "";
    }
}
