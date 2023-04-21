package metier.Libraire;

import ejb.entity.Libraire;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Wikola
 */
@Remote
public interface LibraireEjbRemote {

    public List<Libraire> loginLibraire(String login, String pass);

    public List<Libraire> loginUniqueLibraire(String login);

    public void updateLibraire(Libraire Libraire);

    public void addLibraire(Libraire Libraire);
}
