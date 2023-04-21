package ch.bfh.egov.internetapps.persistence.modul;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import ch.bfh.egov.internetapps.tos.Branchensicht;
import ch.bfh.egov.internetapps.tos.Modul;
import java.util.List;

/**
 * Implementierende Data Access Object-Klasse f�r Branchensichten.
 * 
 * @author Kompetenzzentrum E-Business, Simon Bergamin
 */
public class ModulIbatisDao extends SqlMapClientTemplate implements ModulDao {

    Log logger = LogFactory.getLog(this.getClass());

    /**
   * Holt ein Modul anhand seiner Id.
   * 
   * @param m           Modul-Objekt mit ben�tigten Daten
   * @return            das Modul
   */
    public Modul getById(Modul m) {
        return ((Modul) queryForObject("Modul.getById", m));
    }

    /**
   * Holt ein Modul anhand seines Namens.
   * 
   * @param m           Modul-Objekt mit ben�tigten Daten
   * @return            das Modul
   */
    public Modul getByName(Modul m) {
        return ((Modul) queryForObject("Modul.getByName", m));
    }

    /**
   * Holt alle Module eines Mandanten.
   * 
   * @param mandantId   die Id des Mandanten
   * @return            Liste aller Module
   */
    public List<Modul> getAll(Integer mandantId) {
        return queryForList("Modul.getAll", mandantId);
    }

    /**
   * Holt die zugeorndeten Module einer Branchensicht f�r den angegebenen Planungsstand.
   * 
   * @param mandantId   die Id des Mandanten
   * @return            Liste aller Module
   */
    public List<Modul> getAssigned(Branchensicht b, Integer planned) {
        Modul m = new Modul();
        m.setMandantId(b.getMandantId());
        m.setBranchensichtId(b.getBranchensichtId());
        m.setGeplant(planned);
        return queryForList("Modul.getAssigned", m);
    }

    /**
   * Holt die zugeorndeten Module einer Branchensicht.
   *
   * @param b           die Branchensicht
   * @return            Liste aller Module
   */
    public List<Modul> getAllAssigned(Branchensicht b) {
        return queryForList("Modul.getAllAssigned", b);
    }

    /**
   * Holt die Anzahl gespeicherter St�nde iener Branchensicht.
   *
   * @param b           die Branchensicht
   * @return            Liste aller Module
   */
    public Integer getRecordCount(Branchensicht b) {
        return ((Integer) queryForObject("Modul.getRecordCount", b)) + 1;
    }

    /**
   * Speichert ein neues Modul.
   * 
   * @param m     Modul-Objekt mit ben�tigten Daten
   * @return      die Id des Moduls
   */
    public Integer insert(Modul m) {
        return (Integer) insert("Modul.insert", m);
    }

    /**
   * Speichert die Zuordnung eines Moduls zu einer Branchensicht.
   * 
   * @param m     Modul-Objekt mit ben�tigten Daten
   */
    public void insertAssignment(Modul m) {
        insert("Modul.insertAssignment", m);
    }

    /**
   * �ndert ein bestehendes Modul.
   * 
   * @param m    Modul-Objekt mit ben�tigten Daten
   */
    public void update(Modul m) {
        update("Modul.update", m);
    }

    /**
   * L�scht ein bestehendes Modul.
   * 
   * @param m    Modul-Objekt mit ben�tigten Daten
   */
    public void delete(Modul m) {
        delete("Modul.deleteRelated", m);
        delete("Modul.delete", m);
    }

    /**
   * L�scht die Zuordnungen aller Module zu einer Branchensicht.
   *
   * @param b        Branchensicht-Objekt mit ben�tigten Daten
   * @param planned  Gibt den Planungsstand an, der gel�scht werden soll (0 = aktueller Stand)
   */
    public void deleteAssignedModules(Integer branchensichtId, Integer planned) {
        Modul m = new Modul();
        m.setBranchensichtId(branchensichtId);
        m.setGeplant(planned);
        delete("Modul.deleteAssignedModules", m);
    }

    /**
   * L�scht einen bestimmten Planungsstand.
   * 
   * @param m    Modul-Objekt mit ben�tigten Daten
   */
    public void deleteAssignments(Modul m) {
        delete("Modul.deleteAssignedModules", m);
        update("Modul.resetPlanned", m);
    }
}
