package db2dot;

import java.io.FileOutputStream;
import java.sql.*;

public class DB2dot {

    public static void main(String[] args) {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            if (args.length < 2) {
                System.err.println("Parameters:  <filename>.accdb  <filename>.dot");
                System.exit(1);
            }
            String filenameDB = args[0];
            System.out.println(filenameDB);
            String filenameDot = args[1];
            System.out.println(filenameDot);
            String databaseConnectionString = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + filenameDB.trim() + ";READONLY=true}";
            DBGraph graph = new DBGraph(databaseConnectionString);
            String dotOutput = graph.generateDotFile();
            System.out.println(dotOutput);
            FileOutputStream fos = new FileOutputStream(filenameDot);
            fos.write(dotOutput.getBytes());
            fos.close();
            String lista_email = DBUtils.selectEmails(databaseConnectionString);
            System.out.println("Lista Email");
            System.out.println(lista_email);
            String lista_Web = DBUtils.selectWebsites(databaseConnectionString);
            System.out.println("Lista Web");
            System.out.println(lista_Web);
            String ghilimele = DBUtils.ghilimele(databaseConnectionString);
            System.out.println("ID-urile institutiilor cu numar impar de ghilimele");
            System.out.println(ghilimele);
            String spatii = DBUtils.spaces(databaseConnectionString);
            System.out.println("ID-urile institutiilor cu spatii in exces");
            System.out.println(spatii);
            DBUtils.toUpperCase(databaseConnectionString);
            String csvInstitutionsOutput = DBInstitutionsGenerator.generateAbstractInstitutions(databaseConnectionString);
            System.out.println(csvInstitutionsOutput);
        } catch (Exception err) {
            System.out.println("Error: " + err);
        }
    }
}
