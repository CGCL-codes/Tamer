package fr.ign.cogit.geoxygene.example;

import java.math.BigDecimal;
import java.util.List;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.feature.IFeatureCollection;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.datatools.Geodatabase;
import fr.ign.cogit.geoxygene.datatools.Metadata;
import fr.ign.cogit.geoxygene.datatools.ojb.GeodatabaseOjbFactory;
import fr.ign.cogit.geoxygene.feature.FT_Feature;

/**
 * Exemple et test d'utilisation de l'interface Geodatabase. On suppose qu'il
 * existe une classe persistante "donnees.defaut.Troncon_route". (sinon changer
 * le nom de la classe dans le code). Si la classe a charger contient beaucoup
 * d'objet, lancer le programme avec l'option '-Xmx512M' (java -Xmx512M
 * exemple.FirstExample) .
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.1
 * 
 */
@SuppressWarnings({ "unchecked", "unqualified-field-access" })
public class TestGeodatabase {

    private Geodatabase db;

    private Class<? extends FT_Feature> tronconClass;

    private String nomClasse = "donnees.defaut.Bdc38_troncon_route";

    @SuppressWarnings("unchecked")
    public TestGeodatabase() {
        this.db = GeodatabaseOjbFactory.newInstance();
        try {
            this.tronconClass = (Class<? extends FT_Feature>) Class.forName(this.nomClasse);
        } catch (ClassNotFoundException e) {
            System.out.println(this.nomClasse + " : non trouvee");
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        TestGeodatabase test = new TestGeodatabase();
        test.testJDO();
        test.testMetadata();
        test.testSpatial();
        test.testSQL();
    }

    public void testJDO() {
        Integer gid = new Integer(this.db.maxId(this.tronconClass));
        int seuil = 100;
        this.db.begin();
        System.out.println("debut transaction");
        System.out.println("transaction ouverte ? : " + this.db.isOpen());
        IFeature feature = this.db.load(this.tronconClass, new Integer(gid));
        if (feature != null) {
            System.out.println("objet charge : " + feature.getClass() + " - id : " + feature.getId());
        }
        IFeatureCollection<?> featList = this.db.loadAllFeatures(this.tronconClass);
        System.out.println("nombre de feature charges : " + featList.size());
        feature = featList.get(0);
        IGeometry geom = feature.getGeom();
        System.out.println(geom);
        Resultat result = new Resultat();
        result.setGeom(geom);
        this.db.makePersistent(result);
        System.out.println("objet resultat cree - id : " + result.getId());
        this.db.checkpoint();
        System.out.println("checkpoint");
        featList = this.db.loadAllFeatures(this.tronconClass);
        System.out.println("nombre de feature charges : " + featList.size());
        featList = this.db.loadAllFeatures(this.tronconClass, geom);
        System.out.println("nombre de feature charges : " + featList.size());
        featList = this.db.loadAllFeatures(this.tronconClass, geom, seuil);
        System.out.println("nombre de feature charges : " + featList.size());
        String query = "select x from " + Resultat.class.getName() + " where int1 = $0";
        System.out.println(query);
        List<?> list = this.db.loadOQL(query, new Integer(0));
        System.out.println("nombre d'objets trouves par la requete : " + list.size());
        this.db.deletePersistent(result);
        System.out.println("objet result detruit - id : " + result.getId());
        this.db.commit();
        System.out.println("fin transaction");
        System.out.println("transaction ouverte ? : " + this.db.isOpen());
    }

    public void testMetadata() {
        List<Metadata> metadataList = this.db.getMetadata();
        for (Metadata metadata : metadataList) {
            if (metadata.getClassName() != null) {
                System.out.println(metadata.getClassName());
            }
            if (metadata.getJavaClass() != null) {
                System.out.println(metadata.getJavaClass());
            }
            if (metadata.getTableName() != null) {
                System.out.println(metadata.getTableName());
            }
            if (metadata.getGeomColumnName() != null) {
                System.out.println(metadata.getGeomColumnName());
            }
            if (metadata.getIdColumnName() != null) {
                System.out.println(metadata.getIdColumnName());
            }
            if (metadata.getSRID() != 0) {
                System.out.println(metadata.getSRID());
            }
            if (metadata.getEnvelope() != null) {
                System.out.println(metadata.getEnvelope());
            }
            if (metadata.getTolerance() != null) {
                System.out.println(metadata.getTolerance(0));
            }
            if (metadata.getDimension() != 0) {
                System.out.println(metadata.getDimension());
            }
            System.out.println("");
        }
        String tableName = this.db.getMetadata(this.tronconClass).getTableName();
        System.out.println(tableName);
        System.out.println("");
        System.out.println(this.db.getMetadata(tableName).getJavaClass().getName());
        System.out.println("");
    }

    public void testSpatial() {
        this.db.mbr(this.tronconClass);
        System.out.println("emprise ok");
        this.db.spatialIndex(this.tronconClass);
        System.out.println("index spatial ok");
    }

    public void testSQL() {
        List<?> list = this.db.exeSQLQuery("SELECT COGITID FROM RESULTAT");
        for (Object obj : list) {
            int featureId = ((BigDecimal) ((Object[]) obj)[0]).intValue();
            System.out.println("feature : " + featureId);
        }
        System.out.println("Requete directe SQL ok");
        this.db.exeSQL("DELETE FROM RESULTAT");
        System.out.println("delete ok");
        System.out.println("nombre d'objets : " + this.db.countObjects(this.tronconClass));
        System.out.println("min id : " + this.db.minId(this.tronconClass));
        System.out.println("max id : " + this.db.maxId(this.tronconClass));
    }
}
