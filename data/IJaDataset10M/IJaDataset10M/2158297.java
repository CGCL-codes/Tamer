package org.jdmp.sigmen.editeur;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdmp.sigmen.editeur.data.Exportable;

/**
 * La classe abstraite permettant l'importation et l'exportation depuis une base de données.
 * @author Ilod
 *
 */
public abstract class AbstractExportable implements Exportable {

    /**
	 * Initialise l'objet à partir des données contenues dans la BDD indiquée par {@code c}.
	 * @param c la connexion à la BDD à partir de laquelle importer les données.
	 */
    public abstract void importer(Connection c) throws SQLException;

    /**
	 * Exporte l'objet dans la BDD indiquée par {@code c}
	 * @param c le connexion à la BDD dans laquelle exporter les données.
	 */
    public abstract void exporter(Connection c) throws SQLException;

    /**
	 * Initialise l'objet à partir des données contenues dans {@code rs}, ou de sa connexion associée.
	 * @param rs le {@code ResultSet} à partir duquel importer les données.
	 */
    public void importer(ResultSet rs) throws SQLException {
        importer(rs.getStatement().getConnection());
    }
}
