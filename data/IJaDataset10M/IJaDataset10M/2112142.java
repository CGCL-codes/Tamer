package cytoprophetServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import cytoprophet.Interaction;
import cytoprophet.Interactor;
import cytoprophet.Pair;

public class TableGenerator {

    public Map<Pair, Interaction> interactions = new TreeMap<Pair, Interaction>();

    public Map<String, Interactor> interactors = new TreeMap<String, Interactor>();

    private char delimiter = ' ';

    public TableGenerator() {
    }

    public boolean ReadFile(String inputFileName) {
        try {
            String str;
            BufferedReader in = new BufferedReader(new FileReader(inputFileName.trim()));
            String leftValue, rightValue;
            int id1, id2, cv = 1;
            double probability;
            while ((str = in.readLine()) != null) {
                try {
                    leftValue = str.substring(0, str.indexOf(delimiter)).trim();
                    str = str.substring(str.indexOf(delimiter) + 1);
                    id1 = cv;
                    if (interactors.containsKey(leftValue)) {
                        id1 = interactors.get(leftValue).id;
                    } else {
                        interactors.put(leftValue, new Interactor(leftValue, id1));
                        ++cv;
                    }
                    rightValue = str.substring(0, str.indexOf(delimiter)).trim();
                    str = str.substring(str.indexOf(delimiter) + 1);
                    id2 = cv;
                    if (interactors.containsKey(rightValue)) {
                        id2 = interactors.get(rightValue).id;
                    } else {
                        interactors.put(rightValue, new Interactor(rightValue, id2));
                        ++cv;
                    }
                    probability = (new Double(str)).doubleValue();
                    Interaction v = interactions.put(new Pair(leftValue, rightValue), new Interaction(leftValue, rightValue, id1, id2, probability));
                    if (v != null) {
                        System.out.println("Duplicate interaction:" + leftValue + ", " + rightValue);
                    } else {
                        interactors.get(leftValue).edges++;
                        interactors.get(rightValue).edges++;
                    }
                } catch (Exception e) {
                }
            }
        } catch (IOException e) {
            System.out.println("" + inputFileName + "failed.");
            return false;
        }
        return true;
    }

    public void InsertInteractors(String sqlTrunk) {
        Connection con = null;
        Iterator<Interactor> iter;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        try {
            String query;
            String id, name, description, source;
            PreparedStatement stmt;
            Interactor holderInteractor;
            String url = "jdbc:postgresql://localhost/testdb";
            con = DriverManager.getConnection(url, "postgres", "Magical87");
            iter = interactors.values().iterator();
            while (iter.hasNext()) {
                holderInteractor = iter.next();
                id = (new Integer(holderInteractor.id)).toString();
                name = holderInteractor.name;
                description = "test";
                source = "test";
                query = sqlTrunk + "VALUES (" + id + ", '" + name + "', '" + description + "', '" + source + "')";
                stmt = con.prepareStatement(query);
                stmt.execute();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
            }
        }
    }

    public void InsertInteractions(int alg_id, String sqlTrunk) {
        Connection con = null;
        Iterator<Interaction> iter;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        try {
            String query;
            String id1, id2, probability;
            PreparedStatement stmt;
            Interaction holderInteraction;
            String url = "jdbc:postgresql://localhost/testdb";
            con = DriverManager.getConnection(url, "postgres", "Magical87");
            iter = interactions.values().iterator();
            ;
            while (iter.hasNext()) {
                holderInteraction = iter.next();
                id1 = (new Integer(holderInteraction.id1)).toString();
                id2 = (new Integer(holderInteraction.id2)).toString();
                probability = (new Double(holderInteraction.probability).toString());
                query = sqlTrunk + "VALUES (" + id1 + ", " + id2 + ", " + alg_id + ", " + probability + ")";
                stmt = con.prepareStatement(query);
                stmt.execute();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
            }
        }
    }
}
