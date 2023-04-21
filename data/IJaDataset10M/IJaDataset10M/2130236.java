package fr.ign.cogit.geoxygene.util.loader;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import fr.ign.cogit.geoxygene.datatools.Geodatabase;

/**
 * Genere les identifiant d'une table (colonne COGITID). Dans l'ideal, plusieurs
 * types de generation devraient etre possibles : Simple (i.e. de 1 a N),
 * unicite sur toutes les tables geographiques de la base, empreinte numerique,
 * recopie d'une autre colonne ... Pour l'instant seuls simple, et unicite sur
 * toutes les tables geographiques de la base (par l'algo du max) fonctionnent.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.1
 * 
 */
public class GenerateIds {

    private Geodatabase data;

    private String tableName;

    private int maxID = 0;

    private boolean unique;

    private static final String ORACLE_COLUMN_QUERY = "SELECT TABLE_NAME FROM USER_SDO_GEOM_METADATA";

    private static final String POSTGIS_COLUMN_QUERY = "SELECT F_TABLE_NAME FROM GEOMETRY_COLUMNS";

    public GenerateIds(Geodatabase Data, String TableName, boolean Unique) {
        this.data = Data;
        this.tableName = TableName;
        this.unique = Unique;
    }

    public void genere() {
        this.dropColumnID();
        this.addColumnID();
        if (this.unique) {
            if (this.data.getDBMS() == Geodatabase.ORACLE) {
                this.maxCOGITID(GenerateIds.ORACLE_COLUMN_QUERY);
            } else if (this.data.getDBMS() == Geodatabase.POSTGIS) {
                this.maxCOGITID(GenerateIds.POSTGIS_COLUMN_QUERY);
            }
        }
        if (this.data.getDBMS() == Geodatabase.ORACLE) {
            this.genereIDOracle();
        } else if (this.data.getDBMS() == Geodatabase.POSTGIS) {
            this.genereIDPostgres();
        }
    }

    void addColumnID() {
        try {
            Connection conn = this.data.getConnection();
            conn.commit();
            Statement stm = conn.createStatement();
            String query = "ALTER TABLE " + this.tableName + " ADD COGITID INTEGER";
            stm.executeUpdate(query);
            System.out.println(this.tableName + " : colonne CogitID creee");
            stm.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void dropColumnID() {
        try {
            Connection conn = this.data.getConnection();
            conn.commit();
            Statement stm = conn.createStatement();
            try {
                String query = "ALTER TABLE " + this.tableName + " DROP COLUMN COGITID";
                stm.executeUpdate(query);
                System.out.println(this.tableName + " : colonne CogitID effacee");
            } catch (Exception ee) {
                conn.commit();
            }
            stm.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void genereIDOracle() {
        try {
            Connection conn = this.data.getConnection();
            Statement stm = conn.createStatement();
            String query = "SELECT COUNT(*) FROM " + this.tableName;
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                int nbCount = ((BigDecimal) rs.getObject(1)).intValue();
                System.out.println(nbCount + " objets dans la table " + this.tableName + " ... generation des identifiants ...");
            }
            String proc = "CREATE OR REPLACE PROCEDURE genere_cogitid AS";
            proc = proc + " BEGIN";
            proc = proc + " DECLARE";
            proc = proc + " i integer := " + this.maxID + ";";
            proc = proc + " cursor c is select rowid from " + this.tableName + ";";
            proc = proc + " therowid rowid;";
            proc = proc + " BEGIN";
            proc = proc + " if i is null then i := 0; end if;";
            proc = proc + " open c;";
            proc = proc + " LOOP";
            proc = proc + " fetch c into therowid;";
            proc = proc + " exit when c%notfound;";
            proc = proc + " i := i+1;";
            proc = proc + " update " + this.tableName + " set cogitid=i where rowid=therowid;";
            proc = proc + " END LOOP;";
            proc = proc + " close c;";
            proc = proc + " END;";
            proc = proc + " END genere_cogitid;";
            stm.execute(proc);
            CallableStatement cstm = conn.prepareCall("begin GENERE_COGITID; end;");
            cstm.execute();
            cstm.close();
            try {
                String update = "ALTER TABLE " + this.tableName + " DROP PRIMARY KEY";
                stm.executeUpdate(update);
                System.out.println("cle primaire sur " + this.tableName + " supprimee");
            } catch (Exception e1) {
                System.out.println("aucune cle primaire sur " + this.tableName);
            }
            String update = "ALTER TABLE " + this.tableName + " ADD PRIMARY KEY (COGITID)";
            stm.executeUpdate(update);
            System.out.println("cle primaire sur " + this.tableName + " ajoutee (colonne COGITID)");
            stm.close();
            conn.commit();
        } catch (Exception e) {
            System.out.println(this.tableName);
            e.printStackTrace();
        }
    }

    void genereIDPostgres() {
        try {
            Connection conn = this.data.getConnection();
            conn.commit();
            Statement stm = conn.createStatement();
            String query = "SELECT COUNT(*) FROM " + this.tableName;
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                int nbCount = ((Number) rs.getObject(1)).intValue();
                System.out.println(nbCount + " objets dans la table " + this.tableName + " ... generation des identifiants ...");
            }
            try {
                String update = "create SEQUENCE seq_genere_cogitid";
                stm.executeUpdate(update);
            } catch (Exception ee) {
                conn.commit();
            }
            conn.commit();
            if (this.maxID == 0) {
                this.maxID = 1;
            }
            query = "SELECT setval ('seq_genere_cogitid', " + this.maxID + ")";
            rs = stm.executeQuery(query);
            while (rs.next()) {
            }
            conn.commit();
            String update = "update " + this.tableName + " set cogitid = nextval('seq_genere_cogitid')";
            stm.executeUpdate(update);
            conn.commit();
            query = "select con.conname, con.contype from pg_constraint con, pg_class cl";
            query = query + " where con.conrelid = cl.oid";
            query = query + " and cl.relname='" + this.tableName + "'";
            rs = stm.executeQuery(query);
            String conName = "";
            while (rs.next()) {
                String conType = rs.getString(2);
                if (conType.compareToIgnoreCase("p") == 0) {
                    conName = rs.getString(1);
                }
            }
            if (conName.compareTo("") != 0) {
                update = "ALTER TABLE " + this.tableName + " DROP CONSTRAINT " + conName;
                stm.executeUpdate(update);
                System.out.println("cle primaire sur " + this.tableName + " supprimé : " + conName);
            }
            update = "ALTER TABLE " + this.tableName + " ADD PRIMARY KEY (COGITID)";
            stm.executeUpdate(update);
            System.out.println("cle primaire sur " + this.tableName + " ajoutee (colonne COGITID)");
            stm.close();
            conn.commit();
        } catch (Exception e) {
            System.out.println(this.tableName);
            e.printStackTrace();
        }
    }

    public void maxCOGITID(String query) {
        try {
            Connection conn = this.data.getConnection();
            conn.commit();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            List<String> listOfTables = new ArrayList<String>();
            while (rs.next()) {
                listOfTables.add(rs.getString(1));
            }
            Iterator<String> it = listOfTables.iterator();
            while (it.hasNext()) {
                String aTableName = it.next();
                try {
                    query = "SELECT MAX(COGITID) FROM " + aTableName;
                    rs = stm.executeQuery(query);
                    int max = 0;
                    while (rs.next()) {
                        max = ((Number) rs.getObject(1)).intValue();
                    }
                    if (max > this.maxID) {
                        this.maxID = max;
                    }
                } catch (Exception ee) {
                    conn.commit();
                }
            }
            stm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
