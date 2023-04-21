package org.jredway.bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.jredway.model.PMF;
import org.jredway.model.Parcours;
import org.jredway.openscore.OpenScoreServlet;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Classe bean pour la gestion des
 * labels dans la jsp supprimerPartie
 * 
 * @author killian.b
 * @version 1.0.0
 */
public class SupprimerPartieBean {

    private static final Logger log = Logger.getLogger(OpenScoreServlet.class.getName());

    private UserService userService = UserServiceFactory.getUserService();

    private User user = userService.getCurrentUser();

    /**
     * Attributs bean
     * 
     * @since 1.0.0
     */
    private String titre, parcours, bouton;

    /**
     * Constructeur du bean suppression partie
     * label + menu selection partie
     * 
     * @throws IOException
     * @since 1.0.0
     */
    public SupprimerPartieBean() throws IOException {
        Properties myProps = new Properties();
        FileInputStream MyInputStream = null;
        try {
            MyInputStream = new FileInputStream(System.getProperty("app.config"));
            log.info("Load properties: on");
        } catch (FileNotFoundException e) {
            log.info("Load properties: off");
        }
        myProps.load(MyInputStream);
        titre = myProps.getProperty("app.champs.suptitre");
        bouton = myProps.getProperty("app.champs.supbouton");
        parcours = constructionMenu();
        MyInputStream.close();
    }

    /**
     * Méthode privé de construction
     * du string menu (option)
     * 
     * @return menu
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    private String constructionMenu() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Parcours.class);
        query.declareImports("import com.google.appengine.api.users.User;");
        query.setFilter("compte == pCompte");
        query.declareParameters("User pCompte");
        List<Parcours> par = (List<Parcours>) query.execute(user);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String tmpDate;
        StringBuilder menu = new StringBuilder();
        for (Parcours tmp : par) {
            tmpDate = dateFormat.format(tmp.getDateParcours());
            String key = KeyFactory.keyToString(tmp.getKey());
            menu.append("<option value=\"");
            menu.append(key);
            menu.append("\">");
            menu.append(tmp.getNomParcours().toUpperCase());
            menu.append(" -- ");
            menu.append(tmpDate);
            menu.append("</option>");
            menu.append("\n");
        }
        return menu.toString();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getParcours() {
        return parcours;
    }

    public void setParcours(String parcours) {
        this.parcours = parcours;
    }

    public String getBouton() {
        return bouton;
    }

    public void setBouton(String bouton) {
        this.bouton = bouton;
    }
}
