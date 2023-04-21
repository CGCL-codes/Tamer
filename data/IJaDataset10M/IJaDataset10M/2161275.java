package test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import javax.management.Query;
import org.comptahome.dao.IChequeDao;
import org.comptahome.dao.ICompteDao;
import org.comptahome.model.Compte;
import org.comptahome.util.Serveur;
import org.comptahome.util.SpringContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EssaiDao {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Serveur.start("base/test");
        ICompteDao banque = SpringContext.getInstance().getCompte();
        Compte tempo = new Compte();
        tempo.setBanque("Essai");
        tempo.setNumCompte("dfqdffldlfv d");
        tempo = banque.create(tempo);
        System.out.println("Avant find ALL");
        banque.delete(tempo);
        System.out.println("Destruction du compte");
        List<Compte> liste = banque.findAll();
        System.out.println("Avant from compte");
        List liste2 = banque.findJPQL("FROM Compte x LEFT JOIN x.chequiers");
        List liste3 = banque.findSQL("select banque,numCompte from Compte");
        Object[] obj = (Object[]) liste3.get(0);
        Object obj2 = obj[0];
        if (obj2 instanceof String) {
            System.out.println((String) obj2);
        }
        System.out.println("Apres find ALL");
        System.out.println("OK");
        Serveur.stop();
    }
}
