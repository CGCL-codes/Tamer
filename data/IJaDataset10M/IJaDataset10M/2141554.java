package prediction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;
import database.Database;
import database.InfoWarehouse;

public class SQLAggregator {

    public static final int maxAtomId = 0;

    private static InfoWarehouse db;

    private static Connection con;

    private static int maxAtt = 604;

    private static int peakRange = 300;

    private static int numAttributesB4MZ = 4;

    private static double density = 1;

    private static final int minParticles = 200;

    /**
	 * @author steinbel
	 * @author dmusican
	 * Gathers and formats the ATOFMS data appropriately.
	 * @param outputFile - name of the output file
	 */
    public static void process(PrintWriter out) throws IOException {
        createTempTable();
        System.out.println("table created");
        try {
            String denseQuery = "SELECT roundedTime, mass, value1, cnt FROM\n" + "(SELECT roundedTime, SUM(1/de) as cnt,\n" + "	SUM(size*size*size*" + density + "*(1/de)) as mass\n" + "   FROM RoundedDense\n";
            if (maxAtomId > 0) denseQuery += " WHERE atomID <= " + maxAtomId + "\n";
            denseQuery += "   GROUP BY roundedTime\n" + "   HAVING COUNT(*) > " + minParticles + ") Masses, AggData\n" + "WHERE Masses.roundedTime = AggData.Timestamp\n" + "AND value1 <> 0\n" + "ORDER BY roundedTime";
            System.out.println(denseQuery);
            Statement denseStmt = con.createStatement();
            ResultSet denseSet = denseStmt.executeQuery(denseQuery);
            String sparseQuery = "SELECT roundedTime, peaklocation,\n" + "	SUM(peakarea/de) as adjustedpeak\n" + "FROM RoundedDense d, ATOFMSAtomInfosparse s\n" + "WHERE d.atomid = s.atomid and relpeakarea > .0001\n";
            if (maxAtomId > 0) sparseQuery += "AND d.atomID <= " + maxAtomId + "\n";
            sparseQuery += "GROUP BY roundedTime, peaklocation\n" + "ORDER BY roundedTime, peaklocation";
            System.out.println(sparseQuery);
            Statement sparseStmt = con.createStatement();
            ResultSet sparseSet = sparseStmt.executeQuery(sparseQuery);
            boolean denseRead = denseSet.next();
            boolean sparseRead = sparseSet.next();
            boolean newMass = true;
            if (!denseRead) throw new RuntimeException("Dense set empty");
            if (!sparseRead) throw new RuntimeException("Sparse set empty");
            while (denseRead) {
                Timestamp denseTime = denseSet.getTimestamp("roundedTime");
                Timestamp sparseTime = sparseSet.getTimestamp("roundedTime");
                while (denseTime.compareTo(sparseTime) > 0) {
                    sparseRead = sparseSet.next();
                    if (sparseRead) {
                        sparseTime = sparseSet.getTimestamp("roundedTime");
                    } else {
                        throw new RuntimeException("No more sparse data");
                    }
                }
                if (!denseTime.equals(sparseTime)) {
                    throw new RuntimeException("Dense time does not equal sparse time");
                }
                float value = denseSet.getFloat("value1");
                float mass = denseSet.getFloat("mass");
                float count = denseSet.getFloat("cnt");
                String wekaTime = (denseTime.toString().split("\\."))[0];
                float massScaleFactor = 1e8f;
                float countScaleFactor = 1e7f;
                out.print("{0 \"" + wekaTime + "\"" + ",1 " + value + ",2 " + mass / massScaleFactor + ",3 " + count / countScaleFactor);
                while (sparseRead && denseTime.equals(sparseTime)) {
                    float peakScaleFactor = 1e8f;
                    int location = sparseSet.getInt("peaklocation");
                    if ((location >= -(peakRange)) && (location <= (peakRange))) {
                        out.print("," + (location + peakRange + numAttributesB4MZ) + " " + sparseSet.getFloat("adjustedpeak") / peakScaleFactor);
                    }
                    sparseRead = sparseSet.next();
                    if (sparseRead) {
                        sparseTime = sparseSet.getTimestamp("roundedTime");
                    }
                }
                out.println("}");
                denseRead = denseSet.next();
            }
            denseSet.close();
            sparseSet.close();
            denseStmt.close();
            sparseStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @author steinbel
	 * Opens the connection to the database.
	 */
    public static void openAndConnect() {
        db = Database.getDatabase();
        db.openConnection();
        con = db.getCon();
    }

    /**
	 * @author steinbel
	 * @author dmusican
	 * 
	 * Create a temp table with atomID, detection efficiency, roundedTime 
	 * (initially populated with the size of the particles) during the
	 * hour-long timebin around the given time.
	 */
    private static void createTempTable() {
        try {
            dropTempTable();
            System.out.println("Creating temp table.");
            String order = "CREATE TABLE RoundedDense (\n" + "	atomid int,\n" + "	roundedTime DATETIME,\n" + "	size REAL,\n" + "	de REAL)\n" + "INSERT INTO RoundedDense (atomid, roundedTime, size, de)\n" + "SELECT atomid,\n" + "	DATEADD(hour, DATEDIFF(hour, '20000101', time), '20000101') as roundedTime,\n" + "	size, NULL\n" + "FROM ATOFMSAtomInfoDense\n" + "WHERE size >= 0.1 AND size <= 2.5\n";
            if (maxAtomId > 0) order += "AND atomID <= " + maxAtomId;
            System.out.println(order);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(order);
            System.out.println("Updates 1,2, and 3");
            stmt.executeUpdate("update RoundedDense " + "set de = " + "case " + "when (size >= .1 and size <= .75) " + "then (select power(size*1000, 2.8574)*exp(-27.16)) " + "when (size > .75 and size < 1.) " + "then (select power(size*1000, -.58272)*exp(-4.803)) " + "when (size >= 1. and size <= 2.5) " + "then (select power(size*1000, -7.52)*exp(42.031)) " + "end;");
            ResultSet rs = stmt.executeQuery("SELECT * FROM RoundedDense WHERE de IS NULL");
            if (rs.next()) throw new RuntimeException("de not calculated for all values");
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @author steinbel
	 * Drops the temporary table RoundedDense from the database SpASMSdb.
	 */
    private static void dropTempTable() {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("IF (OBJECT_ID('RoundedDense') " + "IS NOT NULL)\n" + " DROP TABLE RoundedDense\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @author steinbel
	 * @author dmusican
	 * Writes the .arff header info for this file, which includes naming
	 * the m/z values as attributes so we can find them later. 
	 * @param relationName - the name of the relation
	 * @param predictThis - the attribute to be predicted (e.g., "ec" or "bc")
	 * @return the header for the .arff file in the form of a string
	 */
    public static String assembleAttributes(String relationName, String predictThis) {
        String attributeNames = "@relation " + relationName + "\n" + "@attribute time date \"yyyy-MM-dd HH:mm:ss\" \n" + "@attribute " + predictThis + " numeric \n" + "@attribute mass numeric \n" + "@attribute count numeric \n";
        for (int i = -peakRange; i <= peakRange; i++) {
            attributeNames += "@attribute mz" + (i) + " numeric \n";
        }
        attributeNames += "@data \n";
        return attributeNames;
    }

    /**
	 * @author dmusican
	 * Import the filter data
	 */
    private static void importFilterData(String filename) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("IF (OBJECT_ID('AggData') IS NOT NULL) DROP TABLE AggData");
            int numColumns = 0;
            try {
                Scanner tempScanner = new Scanner(new File(filename));
                String[] tempArray = tempScanner.nextLine().split(",");
                numColumns = tempArray.length - 1;
            } catch (IOException e) {
                System.out.println("Something is wrong with the .csv file.");
            }
            String valueColumns = "";
            for (int i = 0; i < numColumns; i++) {
                valueColumns += ", Value" + (i + 1) + " FLOAT";
            }
            String tableQuery = "CREATE TABLE AggData (TimeStamp DATETIME" + valueColumns + ")";
            stmt.executeUpdate(tableQuery);
            stmt.executeUpdate("BULK INSERT AggData\n" + "FROM '" + filename + "'\n" + "WITH (FIELDTERMINATOR = ',', ROWTERMINATOR = '\n')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Date start = new Date();
        openAndConnect();
        try {
            String location = (new File(".")).getCanonicalPath();
            String arfffilename = location + "/prediction/swissbapadjusted_newrel.arff";
            PrintWriter out = new PrintWriter(arfffilename);
            out.print(assembleAttributes("baprelation", "bap"));
            String csvfilename = location + "/prediction/BapSwissAdjusted.csv";
            importFilterData(csvfilename);
            System.out.println("Data imported");
            process(out);
            out.close();
            db.closeConnection();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date end = new Date();
        System.out.println("time taken = " + (end.getTime() - start.getTime()) + " milliseconds.");
    }
}
