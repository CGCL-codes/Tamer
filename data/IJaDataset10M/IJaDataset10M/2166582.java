package dsb.demo.javaee5.jpa.dao;

import java.util.List;
import javax.ejb.Local;
import dsb.demo.javaee5.jpa.model.Bon;

@Local
public interface BonDAO {

    public List<Bon> findByDebiteurId(int id);

    public Bon findById(int id);
}
