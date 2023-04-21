package Modele;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import vue.VueParametres;

/**
 *
 * @author Mathieu PASSENAUD
 * @version 0.6
 */
public class BaseDeDonnees {

    public static Connection con;

    /**
     * Connecte le prog à la bdd spécifiée dans la configuration
     * puis créé les tables nécessaires
     */
    public static void connectionALaBase() {
        if (Parametres.type == 0) {
            System.out.println("hsql");
            new BaseDeDonneesHSQL();
        }
        if (Parametres.type == 1) {
            System.out.println("mysql");
            new BaseDeDonneesMysql();
        }
        if (Parametres.type == 2) {
            System.out.println("ms sql");
            new BaseDeDonneesMSSQL();
        }
        if (Parametres.type == 3) {
            System.out.println("c-jdbc");
            new BaseDeDonneescjdbc();
        }
        if (Parametres.type == 4) {
            System.out.println("postgre");
            new BaseDeDonneesPostgre();
        }
        if (Parametres.type == 5) {
            System.out.println("sqlite");
            new BaseDeDonneesSqlite();
        }
        if (Parametres.type == 6) {
            System.out.println("javadb");
            new BaseDeDonneesJavaDB();
        }
        if (Parametres.type == 7) {
            System.out.println("oracle");
            new BaseDeDonneesOracle();
        }
        if (Parametres.type == 8) {
            System.out.println("sequoia");
            new BaseDeDonneesSequoia();
        }
        if (con == null) {
            new VueParametres().setVisible(true);
            return;
        }
        String sql = "create table fichier (id_fichier integer, nom_fichier char(255))";
        try {
            con.createStatement().execute(sql);
        } catch (SQLException ex) {
        }
        sql = "create table code (id_code integer, code varbinary(" + Parametres.tailleBlocs * 2 + "), md5 varchar(255))";
        try {
            con.createStatement().execute(sql);
        } catch (SQLException ex) {
        }
        sql = "create table constituer (id_code integer, id_fichier integer, sit integer)";
        try {
            con.createStatement().execute(sql);
        } catch (SQLException ex) {
        }
        sql = "create table emplacement (id_emplacement integer, nom_emplacement varchar(255))";
        try {
            con.createStatement().execute(sql);
        } catch (SQLException ex) {
        }
        sql = "create table chemin (id_emplacement integer, id_fichier integer, parent integer, id_sequence integer)";
        try {
            con.createStatement().execute(sql);
        } catch (SQLException ex) {
        }
        sql = "select id_code, md5 from code";
        try {
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                Donnees.index.add(new Index(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException ex) {
        }
    }

    /**
     * retaure un fichier
     * @param fichier
     * @exception IOException
     * 
     */
    public static void restaurer(File f) {
        FileOutputStream fo = null;
        try {
            int id;
            fo = new FileOutputStream(Parametres.emplacement + File.separator + f.getName());
            String req = "select id_fichier from fichier where CAST(nom_fichier as char(254))='" + f.getName() + "'";
            ResultSet rs = con.createStatement().executeQuery(req);
            rs.next();
            id = rs.getInt(1);
            req = "select co.code from code co join constituer cn on cn.id_code=co.id_code where cn.id_fichier=" + id + " order by cn.sit";
            rs = con.createStatement().executeQuery(req);
            byte[] b = new byte[Parametres.tailleBlocs];
            while (rs.next()) {
                try {
                    InputStream in = rs.getBinaryStream(1);
                    int taille = in.read(b);
                    for (int i = 0; i < taille; i++) {
                        fo.write(b[i]);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fo.close();
        } catch (IOException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fo.close();
            } catch (IOException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @param requete SQL
     * @return ResultSet
     */
    public static ResultSet executerRequete(String requete) {
        ResultSet rs = null;
        try {
            rs = con.createStatement().executeQuery(requete);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    /**
     * Supprime une entrée de la table des fichiers
     * et toutes les références qui le concernent
     * @param fichier
     */
    public static void supprimer(String fichier) {
        int id = 0;
        try {
            ResultSet rs = executerRequete("select id_fichier from fichier where CAST(nom_fichier as char(254))='" + fichier + "'");
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con.createStatement().execute("delete from constituer where id_fichier=" + id + "");
            con.createStatement().execute("delete from fichier where id_fichier=" + id + "");
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return prochain numéro de fichier
     */
    public static int retournerProchainIdFichier() {
        ResultSet rs = null;
        rs = executerRequete("select max(id_fichier) from fichier");
        try {
            rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return rs.getInt(1) + 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @return prochain id de code
     */
    public static int retournerProchainIdCode() {
        ResultSet rs = null;
        try {
            rs = executerRequete("select max(id_code) from code");
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Enregistre dans la table FICHIER
     * @param fichier
     */
    public static int enregistrerNouveauFichier(String nom, int id) {
        nom = nom.replace('\'', '%');
        String req = "insert into fichier (id_fichier, nom_fichier) values (" + id + ", '" + nom + "')";
        try {
            con.createStatement().execute(req);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * @param parent
     * @return Liste des fichiers enregistrés
     */
    public static ArrayList<String> retournerFichiers(String parent) {
        int id = 0;
        String req;
        ResultSet rs = null;
        ArrayList<String> l = new ArrayList<String>();
        if (!parent.equals("")) {
            req = "select id_emplacement from emplacement where nom_emplacement='" + parent + "'";
            req = req.replace("\\", "");
            try {
                rs = con.createStatement().executeQuery(req);
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (rs.next()) {
                    try {
                        id = rs.getInt(1);
                    } catch (SQLException ex) {
                        Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            id = 0;
        }
        int fichier = 0, max = 0;
        req = "select id_fichier from fichier";
        ResultSet rs1, rs2;
        try {
            rs = con.createStatement().executeQuery(req);
            while (rs.next()) {
                fichier = rs.getInt(1);
                req = "select max(id_sequence) from chemin where id_fichier=" + fichier;
                rs1 = con.createStatement().executeQuery(req);
                rs1.next();
                max = rs1.getInt(1);
                req = "select fi.nom_fichier from fichier fi join chemin ch on ch.id_fichier=fi.id_fichier where ch.id_emplacement=" + id + " and ch.id_sequence=" + max + " and fi.id_fichier=" + fichier;
                System.out.println(req);
                try {
                    rs2 = con.createStatement().executeQuery(req);
                    while (rs2.next()) {
                        l.add(rs2.getString(1));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }

    /**
     * Retourne la liste des sous-répertoires
     * @param parent
     * @return
     */
    public static ArrayList<String> retournerRepertoires(String parent) {
        int id = 0;
        String req;
        ResultSet rs = null;
        ArrayList<String> l = new ArrayList<String>();
        if (!parent.equals("")) {
            req = "select id_emplacement from emplacement where nom_emplacement='" + parent + "'";
            req = req.replace("\\", "");
            try {
                rs = con.createStatement().executeQuery(req);
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (rs.next()) {
                    try {
                        id = rs.getInt(1);
                    } catch (SQLException ex) {
                        Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            id = 0;
        }
        req = "select distinct(em.nom_emplacement) from emplacement em join chemin ch on ch.id_emplacement=em.id_emplacement where parent=" + id;
        try {
            rs = con.createStatement().executeQuery(req);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs.next()) {
                l.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }

    /**
     * Découpe le chemin et établi la séquence
     * @param canonicalPath
     * @param fichier
     */
    public static void emplacement(String canonicalPath, int fichier) {
        System.out.println("\\");
        canonicalPath = canonicalPath.replace('\\', ',');
        String[] repertoires = canonicalPath.split(",");
        ResultSet rs = null;
        String sql;
        int parent = 0;
        int id = 0;
        for (int i = 0; i < repertoires.length; i++) {
            try {
                System.out.println(repertoires[i]);
                sql = "select id_emplacement from emplacement where CAST(nom_emplacement as char(254))='" + repertoires[i] + "'";
                rs = con.createStatement().executeQuery(sql);
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (rs.next()) {
                    id = rs.getInt(1);
                } else {
                    sql = "select max(id_emplacement) from emplacement";
                    rs = con.createStatement().executeQuery(sql);
                    rs.next();
                    id = rs.getInt(1) + 1;
                    sql = "insert into emplacement values (" + id + ", '" + repertoires[i] + "')";
                    con.createStatement().execute(sql);
                }
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            sql = "insert into chemin values (" + id + ", " + fichier + ", " + parent + ", " + i + ")";
            parent = id;
            try {
                con.createStatement().execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(BaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
