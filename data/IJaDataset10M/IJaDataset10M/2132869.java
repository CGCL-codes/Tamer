package org.vardb.util.morphia;

import org.junit.Ignore;
import org.junit.Test;
import org.vardb.util.CStringHelper;
import org.vardb.util.dao.CMongoHelper;
import org.vardb.util.morphia.admin.CSequenceTableReader;
import org.vardb.util.morphia.admin.CXmlDataReader;
import org.vardb.util.morphia.resources.CResourceServiceImpl;
import org.vardb.util.morphia.resources.IResourceService;
import org.vardb.util.morphia.resources.dao.CPathogen;
import org.vardb.util.morphia.resources.dao.CRef;
import org.vardb.util.morphia.resources.dao.CResourceDaoImpl;
import org.vardb.util.morphia.resources.dao.IResourceDao;
import org.vardb.util.tests.AbstractTest;
import com.google.code.morphia.DAO;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class TestMorphia extends AbstractTest {

    public static final String DATABASE = "mydb";

    private IResourceService getResourceService() {
        Mongo mongo = CMongoHelper.getMongo();
        Morphia morphia = new Morphia();
        IResourceDao dao = new CResourceDaoImpl(mongo, morphia, DATABASE);
        CResourceServiceImpl resourceService = new CResourceServiceImpl();
        resourceService.setDao(dao);
        return resourceService;
    }

    @Test
    public void testLoadSequences() {
        String folder = "c:/Documents and Settings/nhayes/My Documents/My Dropbox/vardb/sequence/";
        IResourceService resourceService = getResourceService();
        DB db = resourceService.getDao().getDB();
        db.dropDatabase();
        CSequenceTableReader reader = new CSequenceTableReader(resourceService);
        reader.loadSequencesFromFolder(folder);
        CMongoHelper.printCollections(db);
    }

    @Test
    @Ignore
    public void testLoadXml() {
        String folder = "c:/projects/vardb-newdev/data/";
        IResourceService resourceService = getResourceService();
        DB db = resourceService.getDao().getDB();
        db.dropDatabase();
        CXmlDataReader reader = new CXmlDataReader(resourceService);
        reader.loadXmlFromFolder(folder);
        Datastore ds = resourceService.getDao().getDatastore();
        for (CPathogen pathogen : ds.find(CPathogen.class, "type =", CPathogen.PathogenType.VIRUS).asList()) {
            System.out.println("pathogen: " + pathogen);
        }
        CMongoHelper.printCollections(db);
    }

    @Test
    @Ignore
    public void testLoadXml2() {
        DB db = CMongoHelper.connectToDatabase(DATABASE);
        db.dropDatabase();
        Morphia morphia = new Morphia();
        Mongo mongo = CMongoHelper.getMongo();
        RefDao dao = new RefDao(morphia, mongo);
        CRef ref = new CRef(20576307);
        ref.setTitle("Common variation of IL28 affects gamma-GTP levels and inflammation of the liver in chronically infected hepatitis C virus patients");
        ref.setAuthors("Abe H, Ochi H, Maekawa T, Hayes CN, Tsuge M, Miki D, Mitsui F, Hiraga N, Imamura M, Takahashi S, Ohishi W, Arihiro K, Kubo M, Nakamura Y, Chayama K.");
        ref.setJournal("J Hepatol");
        ref.setAttribute("numsequences", "20");
        dao.save(ref);
        CMongoHelper.printCollections(db);
        Datastore ds = morphia.createDatastore(DATABASE);
        for (CRef curref : ds.find(CRef.class, "journal =", "J Hepatol").asList()) {
            System.out.println("curref=" + CStringHelper.toString(curref));
        }
    }

    @Test
    @Ignore
    public void testCreateResource() {
        DB db = CMongoHelper.connectToDatabase("mydb", "localhost", 27017);
        System.out.println("collections");
        for (String dbname : db.getCollectionNames()) {
            System.out.println(dbname);
        }
        DBCollection pathogens = db.getCollection("pathogens");
        DBCollection families = db.getCollection("families");
        BasicDBObject pathogen = new BasicDBObject();
        pathogen.put("identifier", "plasmodium.falciparum");
        pathogen.put("name", "Plasmodium falciparum");
        pathogen.put("description", "malaria parasite");
        pathogen.put("families", CStringHelper.splitAsList("plasmodium.falciparum.var,plasmodium.falciparum.rif"));
        pathogens.insert(pathogen);
        BasicDBObject family = new BasicDBObject();
        family.put("identifier", "plasmodium.falciparum.var");
        family.put("name", "var gene family");
        families.insert(family);
        family = new BasicDBObject();
        family.put("identifier", "plasmodium.falciparum.rif");
        family.put("name", "rif gene family");
        families.insert(family);
        DBObject mydoc = pathogens.findOne();
        System.out.println("DBObject: " + mydoc);
        System.out.println("families size: " + families.getCount());
        BasicDBObject query = new BasicDBObject();
        query.put("identifier", "plasmodium.falciparum.rif");
        DBCursor cursor = families.find(query);
        while (cursor.hasNext()) {
            System.out.println("family: " + cursor.next());
        }
    }

    public static class RefDao extends DAO<CRef, String> {

        public RefDao(Morphia morphia, Mongo mongo) {
            super(mongo, morphia, DATABASE);
        }
    }
}
