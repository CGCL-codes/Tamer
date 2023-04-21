package com.plausiblelabs.mdb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.healthmarketscience.jackcess.Database;

public class Main {

    /**
     * Writes the XML serialization of the given SF DPH database
     * to standard out.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        if (args.length != 2) {
            System.out.println(String.format("Usage: %s <access file> <sqlite file>", Main.class.getName()));
            System.exit(1);
        }
        Class.forName("org.sqlite.JDBC");
        final AccessExporter exporter = new AccessExporter(Database.open(new File(args[0]), true));
        final Connection jdbc = DriverManager.getConnection("jdbc:sqlite:" + args[1]);
        exporter.export(jdbc);
    }
}
