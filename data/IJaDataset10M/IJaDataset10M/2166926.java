package com.cafe.serve.database;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import com.cafe.serve.client.Bestellung;
import com.cafe.serve.server.Drink;
import com.cafe.serve.util.StringEscapes;

/**
 * @author Raptis Asterios Created first on 27.01.2005 in project ServerThreaded
 */
public class InsertStatements {

    private static Logger logger = Logger.getLogger(InsertStatements.class.getName());

    private static Connection connection = null;

    public static Connection getPostgreSQLConnection() {
        if (connection == null) {
            connection = DBConnection.getConnection();
        }
        return connection;
    }

    /**
     * Die Methode intoBestellungFromTischInDB f�gt die zum ersten mal gesendete
     * Bestellung in die DB.
     * 
     * @param bestellung
     *            Die Bestellung die eingef�gt werden soll.
     * @param tisch
     *            Der Tisch von dem die Bestellung stammt.
     */
    public static void intoBestellungFromTischInDB(Bestellung bestellung, String tisch) {
        String methodName = "intoBestellungFromTischInDB()";
        String insert = null;
        int bestell_id;
        Timestamp bestell_datum = bestellung.getBestellDatum();
        long id = SelectStatements.maxIDFrombestellungen();
        insert = "INSERT INTO bestellungen " + "(bestell_id, bestell_tisch_id, bestell_datum, bestell_preis)" + "VALUES " + "(" + id + ", '" + tisch + "', '" + bestell_datum + "', 0);";
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            stmt.execute(insert);
            bestell_id = SelectStatements.selectIDFromBestellungen(bestell_datum, tisch);
            bestellung.setBestell_id(bestell_id);
            InsertStatements.insertIntoBestellPosition(bestellung);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(methodName);
            e.printStackTrace();
        }
    }

    /**
     * Die Methode intoRechnungFromTischInDB
     * 
     * @param bestellung
     *            Die Bestellung die eingef�gt werden soll.
     * @param tisch
     *            Der Tisch von dem die Bestellung stammt
     */
    public static void intoRechnungFromTischInDB(Bestellung bestellung, String tisch) {
        String insert = null;
        int rechnung_id;
        Timestamp bestell_datum = bestellung.getBestellDatum();
        long id = SelectStatements.maxIDFromRechnung();
        insert = "INSERT INTO rechnung " + "(rechnung_id, rechnung_tisch_id, rechnung_datum, rechnung_preis)" + "VALUES " + "(" + id + ", '" + tisch + "', '" + bestell_datum + "', 0);";
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            stmt.execute(insert);
            rechnung_id = SelectStatements.idFromRechnung(bestell_datum, tisch);
            bestellung.setBestell_id(rechnung_id);
            InsertStatements.intoRechnungsposition(bestellung);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Die Methode insertIntoBestellPosition f�gt alle Drinks von der Bestellung
     * in die Tabelle bestellposition.
     * 
     * @param bestellung
     *            Die Bestellung die eingef�gt werden soll.
     */
    public static void insertIntoBestellPosition(Bestellung bestellung) {
        Hashtable allDrinks = bestellung.getAllDrinks();
        int bestell_id = bestellung.getBestell_id();
        Enumeration e = allDrinks.keys();
        String insert = null;
        double totalPrice = 0.0;
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            for (int i = 0; e.hasMoreElements(); i++) {
                Drink drink = (Drink) e.nextElement();
                Integer anzahl = (Integer) allDrinks.get(drink);
                double preis = Double.parseDouble(drink.getPreis());
                double gesamtPreis = anzahl.intValue() * preis;
                totalPrice += gesamtPreis;
                long id = SelectStatements.maxIDFromBestellposition();
                insert = "INSERT INTO bestellposition " + "(bp_id, bp_bestell_id, bp_drink_name, bp_anzahl, bp_gesamtpreis)" + "VALUES " + "(" + id + ", " + bestell_id + ", '" + drink.getName() + "', " + anzahl.intValue() + ", " + gesamtPreis + ");";
                stmt.execute(insert);
            }
            UpdateStatements.bestellungenSetBestellpreis(totalPrice, bestell_id);
            stmt.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void intoRechnungsposition(Bestellung bestellung) {
        Hashtable allDrinks = bestellung.getAllDrinks();
        int rechnung_id = bestellung.getBestell_id();
        Enumeration e = allDrinks.keys();
        String insert = null;
        double totalPrice = 0.0;
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            for (int i = 0; e.hasMoreElements(); i++) {
                Drink drink = (Drink) e.nextElement();
                Integer anzahl = (Integer) allDrinks.get(drink);
                double preis = Double.parseDouble(drink.getPreis());
                double gesamtPreis = anzahl.intValue() * preis;
                totalPrice += gesamtPreis;
                long id = SelectStatements.maxIDFromRechnungsposition();
                insert = "INSERT INTO rechnungsposition " + "(rp_id, rp_rechnung_id, rp_drink_name, rp_anzahl, rp_gesamtpreis)" + "VALUES " + "(" + id + ", " + rechnung_id + ", '" + drink.getName() + "', " + anzahl.intValue() + ", " + gesamtPreis + ");";
                stmt.execute(insert);
            }
            UpdateStatements.rechnungSetRechnungspreis(totalPrice, rechnung_id);
            stmt.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Die Methode insertIntoBestellPosition
     * 
     * @param newDrinks
     * @param bestell_id
     */
    public static double insertIntoBestellPosition(Hashtable newDrinks, int bestell_id) {
        Enumeration e = newDrinks.keys();
        String insert = null;
        double totalPrice = 0.0;
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            for (int i = 0; e.hasMoreElements(); i++) {
                Drink drink = (Drink) e.nextElement();
                Integer anzahl = (Integer) newDrinks.get(drink);
                double preis = Double.parseDouble(drink.getPreis());
                double gesamtPreis = anzahl.intValue() * preis;
                totalPrice += gesamtPreis;
                long id = SelectStatements.maxIDFromBestellposition();
                insert = "INSERT INTO bestellposition " + "(bp_id, bp_bestell_id, bp_drink_name, bp_anzahl, bp_gesamtpreis)" + "VALUES " + "(" + id + ", " + bestell_id + ", '" + drink.getName() + "', " + anzahl.intValue() + ", " + gesamtPreis + ");";
                stmt.executeUpdate(insert);
            }
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return totalPrice;
    }

    /**
     * Diese Methode setzt ein Drink-objekt in die Datenbank.
     * 
     * @param d
     *            Das Drink-objekt das in die Datenbank geschrieben werden soll.
     */
    public static void insertDrinkInDB(Drink d) {
        String abfrage = "";
        int id = d.getId();
        String drink_name = StringEscapes.replaceAll(d.getName(), "'", "\\'");
        String kategorie_name = d.getKategory();
        double inhalt = Double.parseDouble(d.getGroesse());
        float preis = Float.parseFloat(d.getPreis());
        int mwst = d.getMehrwertSteuer();
        try {
            getPostgreSQLConnection();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Statement stmt = connection.createStatement();
            String insert = "INSERT INTO drinks (drink_id, drink_name," + " drink_kategorie_name, drink_inhalt, drink_einzelpreis, drink_mwst, drink_state, drink_date) VALUES (" + id + ", '" + drink_name + "', '" + kategorie_name + "', " + inhalt + ", " + preis + ", " + mwst + ", 1, '" + now + "');";
            System.out.println(insert);
            stmt.execute(insert);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("From setDrinkInDB: " + e);
        }
    }

    /**
     * Die Methode insertKategoryIntoDB
     * 
     * @param k
     */
    public static void insertKategoryIntoDB(String k) {
        String insert = "";
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            long id = SelectStatements.maxIDFromKategorien();
            insert = "INSERT INTO kategorien (kategorie_id, kategorie_name) VALUES (" + id + ", '" + k + "');";
            stmt.execute(insert);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("From setKategoryInDB: " + e);
        }
    }

    /**
     * Die Methode insertTischIntoDB
     * 
     * @param tisch
     */
    public static void insertTischIntoDB(String tisch) {
        String insert = "";
        Statement stmt = null;
        try {
            getPostgreSQLConnection();
            stmt = connection.createStatement();
            long id = SelectStatements.maxIDFromTische();
            insert = "INSERT INTO tische (tisch_id, tisch_standort) VALUES (" + id + ", '" + tisch + "')";
            stmt.execute(insert);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    stmt = null;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Die Methode insertInhaltIntoDB
     * 
     * @param k
     */
    public static void insertInhaltIntoDB(float k) {
        String insert = "";
        try {
            getPostgreSQLConnection();
            Statement stmt = connection.createStatement();
            long id = SelectStatements.maxIDFromInhalte();
            insert = "INSERT INTO inhalte (inhalt_id, inhalt) VALUES (" + id + ", " + k + ");";
            stmt.execute(insert);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("From insertInhaltIntoDB: " + e);
        }
    }
}
