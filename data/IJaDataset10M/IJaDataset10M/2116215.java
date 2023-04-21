package org.pcorp.space.persistance.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import org.pcorp.space.metier.domaine.Coordonne;
import org.pcorp.space.metier.domaine.Station;
import org.pcorp.space.metier.domaine.Vue;
import org.pcorp.space.metier.domaine.infovue.StationVue;
import org.pcorp.space.persistance.dao.StationDAO;
import org.pcorp.space.persistance.exception.PersistanceException;

public class StationDAOjdbc extends GeneriqueDAOjdbc implements StationDAO {

    public Station getResurection(int id_joueur) throws PersistanceException {
        Statement requete;
        String sql;
        Station station = null;
        Connection conn;
        conn = getConnection();
        try {
            requete = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "select * from stations_connues join vaisseaux using (vaiss_id) where stc_resu = 'true' and stations_connues.jou_id = " + id_joueur;
            ResultSet resultat = requete.executeQuery(sql);
            if (resultat.next()) {
                station = new Station();
                station.setId(resultat.getInt("vaiss_id"));
                station.setNom(resultat.getString("vaiss_nom"));
                station.setPosition(new Coordonne(resultat.getInt("vaiss_posx"), resultat.getInt("vaiss_posy"), resultat.getInt("vaiss_posz")));
                station.setDescription("");
            }
        } catch (SQLException sqle) {
            throw new PersistanceException(sqle, "erreur lors de la requete :: getResurection");
        } finally {
            closeConnection();
        }
        return station;
    }

    public List<StationVue> getStationInView(Coordonne coord, Vue vue) throws PersistanceException {
        Statement requete;
        String sql;
        StationVue station = null;
        List<StationVue> liste = new LinkedList<StationVue>();
        Connection conn;
        int xmin = coord.getX() - vue.getVuemax();
        int xmax = coord.getX() + vue.getVuemax();
        int ymin = coord.getY() - vue.getVuemax();
        int ymax = coord.getY() + vue.getVuemax();
        int zmin = coord.getZ() - vue.getVuemax();
        int zmax = coord.getZ() + vue.getVuemax();
        conn = getConnection();
        try {
            requete = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "select * from vaisseaux where ";
            sql += "( vaiss_posx <= " + xmax + " and vaiss_posx >= " + xmin + ") ";
            sql += " and ( vaiss_posy <= " + ymax + " and vaiss_posy >= " + ymin + ") ";
            sql += " and ( vaiss_posz <= " + zmax + " and vaiss_posz >= " + zmin + ") ";
            sql += " and cat_id = 13";
            ResultSet resultat = requete.executeQuery(sql);
            while (resultat.next()) {
                station = new StationVue();
                station.setId(resultat.getInt("vaiss_id"));
                station.setNom(resultat.getString("vaiss_nom"));
                station.setPosition(new Coordonne(resultat.getInt("vaiss_posx"), resultat.getInt("vaiss_posy"), resultat.getInt("vaiss_posz")));
                station.setDescription("");
                liste.add(station);
            }
        } catch (SQLException sqle) {
            throw new PersistanceException(sqle, "erreur lors de la requete :: getStationVue");
        } finally {
            closeConnection();
        }
        return liste;
    }
}
