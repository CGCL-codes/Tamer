package cytoprophetServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import cytoprophet.Interaction;
import cytoprophet.Interactor;
import cytoprophet.Pair;

public class ProteinTableBuilder {

    private char delimiter = ' ';

    private String dbAccount = "postgres";

    private String dbPassword = "Magical87";

    private Connection con = null;

    public static void main(String[] argv) throws Exception {
        ProteinTableBuilder proteinBuilder = new ProteinTableBuilder();
        proteinBuilder.ReadFile("C:\\Documents and Settings\\Charles Lamanna\\Desktop\\data\\all_sg_cytoprophet_mp.ppi", 3);
    }

    public ProteinTableBuilder() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        try {
            String url = "jdbc:postgresql://localhost/testdb";
            con = DriverManager.getConnection(url, dbAccount, dbPassword);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public boolean ReadFile(String inputFileName, int alg_id) {
        try {
            String str;
            BufferedReader in = new BufferedReader(new FileReader(inputFileName.trim()));
            String leftValue, rightValue;
            int id1, id2, qcv = 1;
            double probability;
            while ((str = in.readLine()) != null) {
                try {
                    leftValue = str.substring(0, str.indexOf(delimiter)).trim();
                    str = str.substring(str.indexOf(delimiter) + 1);
                    rightValue = str.substring(0, str.indexOf(delimiter)).trim();
                    str = str.substring(str.indexOf(delimiter) + 1);
                    id1 = LookupProteinId(leftValue);
                    id2 = LookupProteinId(rightValue);
                    probability = (new Double(str)).doubleValue();
                    String query;
                    if (-1 == id1 || -2 == id2) {
                        System.out.println("Can not be found. Try aliasing.");
                    } else {
                        try {
                            query = "INSERT INTO protein_interaction (p_id_1, p_id_2, inter_alg_id, probability) " + "VALUES (" + id1 + ", " + id2 + ", " + alg_id + ", " + probability + ")";
                            con.prepareStatement(query).execute();
                        } catch (Exception ex) {
                            System.out.println("Exception: " + ex.getMessage());
                        }
                    }
                    ++qcv;
                } catch (Exception e) {
                    System.out.println("Exception");
                }
            }
            System.out.println(qcv);
            con.close();
            in.close();
        } catch (Exception e) {
            System.out.println("" + inputFileName + "failed.");
            return false;
        }
        return true;
    }

    public int LookupProteinId(String protein) {
        int returnValue;
        returnValue = -1;
        try {
            ResultSet rs;
            String query;
            query = "SELECT * FROM protein WHERE default_name = '" + protein + "'";
            rs = con.prepareStatement(query).executeQuery();
            while (rs.next()) {
                returnValue = (new Integer(rs.getString("p_id"))).intValue();
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
        }
        return returnValue;
    }
}
