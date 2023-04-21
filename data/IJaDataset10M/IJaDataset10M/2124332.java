package dao;

import java.util.Date;
import java.util.List;
import entity.Trajet;

public interface TrajetDao {

    public List<Trajet> getAllTrajets();

    public Trajet getTrajetById(long id);

    public List<Trajet> getTrajets(String lieuDepart, String lieuArrivee, Date allee, Date retour);

    public List<Trajet> getAllTrajetsDepuis(String lieuDepart);
}
