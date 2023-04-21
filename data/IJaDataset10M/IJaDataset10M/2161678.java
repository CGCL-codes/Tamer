package net.jcsi.bdd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import net.jcsi.exception.JcsiException;

public class Admin {

    private java.sql.Connection con;

    private Object IdPeople = null;

    private Object Login = null;

    private Object Nom = null;

    private Object Prenom = null;

    private Object Pass = null;

    private Object Role = null;

    private Object IdMetier = null;

    private int IdMetierInt = 0;

    private Object Metier = null;

    private Object MaxUser = null;

    public Admin(java.sql.Connection connection) {
        con = connection;
        System.out.println("Ok, Admin instancie");
    }

    public Vector getUsers() throws JcsiException {
        Statement s;
        Vector table = new Vector();
        try {
            s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT u.id_people, u.login, u.nom_people, u.prenom_people, m.nom_metier " + "FROM utilisateur u, metier m " + "WHERE u.id_metier = m.id_metier " + "ORDER BY u.id_people");
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getObject("id_people"));
                row.add(rs.getObject("login"));
                row.add(rs.getObject("nom_people"));
                row.add(rs.getObject("prenom_people"));
                row.add(rs.getObject("nom_metier"));
                table.add(row);
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JcsiException("SQL Error during the acquisition of user's informations", e);
        }
        return (table);
    }

    public void getAllInfo(String id) throws JcsiException {
        Statement s;
        try {
            s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT u.*, m.* " + "FROM utilisateur u, metier m " + "WHERE id_people = '" + id + "'");
            while (rs.next()) {
                IdPeople = rs.getObject("id_people");
                Login = rs.getObject("login");
                Nom = rs.getObject("nom_people");
                Prenom = rs.getObject("prenom_people");
                Pass = rs.getObject("password");
                Role = rs.getObject("role");
                IdMetier = rs.getObject("id_metier");
                IdMetierInt = rs.getInt("id_metier");
                Metier = rs.getObject("nom_metier");
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JcsiException("SQL Error during the acquisition of user's informations", e);
        }
        return;
    }

    public Vector getAllMetier() throws JcsiException {
        Statement s;
        Vector table = new Vector();
        try {
            s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT nom_metier FROM metier");
            while (rs.next()) {
                table.add(rs.getObject("nom_metier"));
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JcsiException("SQL error : work layout", e);
        }
        return (table);
    }

    public boolean AjoutUtilisateur(String login, String nom, String prenom, String pass, String role, String id_metier) throws JcsiException {
        String maxid = null;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT max(id_people)+1 AS \"MAX\" FROM utilisateur");
            while (rs.next()) maxid = rs.getObject("MAX").toString();
            s.executeUpdate("INSERT INTO utilisateur(id_people, id_metier, login, password, nom_people, prenom_people, role)" + " VALUES(\"" + maxid + "\", \"" + id_metier + "\", \"" + login + "\"" + ",\"" + pass + "\",\"" + nom + "\",\"" + prenom + "\",\"" + role + "\")");
            s.close();
        } catch (SQLException e) {
            System.out.println("Error: cannot add the user.");
            throw new JcsiException("Error: cannot add the user.", e);
        }
        return true;
    }

    public boolean SupprimeUtilisateur(String id) throws JcsiException {
        try {
            Statement s = con.createStatement();
            System.out.println("DELETE FROM utilisateur WHERE id_people = '" + id + "'");
            s.executeUpdate("DELETE FROM utilisateur WHERE id_people = '" + id + "'");
            s.close();
        } catch (SQLException e) {
            System.out.println("Error: cannot delete the user.");
            throw new JcsiException("Error: cannot delete the user.", e);
        }
        return true;
    }

    public boolean ModifieUtilisateur(String id, String id_metier, String login, String pass, String nom, String prenom, String role) throws JcsiException {
        try {
            Statement s = con.createStatement();
            s.executeUpdate("UPDATE utilisateur SET " + "id_metier = '" + id_metier + "', login = '" + login + "', password = '" + pass + "', nom_people = '" + nom + "'" + ", prenom_people = '" + prenom + "', role = '" + role + "' " + "WHERE id_people ='" + id + "'");
            s.close();
        } catch (SQLException e) {
            System.out.println("Error: cannot edit the user.");
            throw new JcsiException("Error: cannot edit the user.", e);
        }
        return true;
    }

    public void Close() throws JcsiException {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JcsiException("Error: disconnected of database", e);
        }
    }

    public Object getIdUser() {
        return IdPeople;
    }

    public Object getLogin() {
        return Login;
    }

    public Object getNom() {
        return Nom;
    }

    public Object getPrenom() {
        return Prenom;
    }

    public Object getPass() {
        return Pass;
    }

    public Object getRole() {
        return Role;
    }

    public Object getIdMetier() {
        return IdMetier;
    }

    public Object getMetier() {
        return Metier;
    }

    public int getIdMetierInt() {
        return IdMetierInt;
    }

    public Object getMaxUser() throws JcsiException {
        try {
            Statement s;
            s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT max(id_people)+1 AS \"MAX\" FROM utilisateur");
            while (rs.next()) {
                MaxUser = rs.getObject("MAX");
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JcsiException("SQL Error : cannot acquire Id MAx", e);
        }
        return (MaxUser);
    }
}
