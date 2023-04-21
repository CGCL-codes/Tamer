package model2db;

/**
 * Start up the process.
 * <p/>
 * Project Model2DB <br/>
 * GenerateDB.java created 23 februari 2007, 10:16
 * <p/>
 * Copyright &copy 2007 Jethro Borsje
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filePath = "C:\\Documents and Settings\\Jethro Borsje\\Mijn documenten\\ont\\IFRS-GP\\ifrs-gp-2005-01-15.owl";
        String url = "http://nets.ii.uam.es/bronto/ifrs-gp";
        String name = "IFRS";
        String dbName = "ontologies";
        String user = "jethro";
        String password = "jethro";
        new GenerateDB(filePath, url, name, dbName, user, password);
    }
}
