package com.increg.game.bean;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.exception.NoDatabaseException;
import com.increg.commun.exception.UnauthorisedUserException;
import com.increg.util.SimpleDateFormatEG;

public class GameSession extends BasicSession implements HttpSessionBindingListener {

    /**
     * Nom du fichier de config par d�faut
     */
    public static final String DEFAULT_CONFIG = "config";

    /**
     * Taille des paquets �chang�s
     */
    public static final int CHUNK_SIZE = 4096;

    /**
     * myIdent Identification de l'utilisateur
     */
    protected JoueurBean myJoueur = null;

    /**
     * Bean de s�curit�
     */
    protected SecurityBean security;

    /**
     * Fichier de config � utiliser
     */
    protected ResourceBundle resConfig;

    /**
     * Adresse de base pour le confirmation d'info
     */
    protected String baseURL;

    /**
     * Adresse de base pour la fin de partie
     */
    protected String finURL;

    /**
     * Contexte de Servlet en cours
     */
    protected ServletContext srvCtxt;

    /**
     * Indicateur du dernier chat vu
     */
    protected long lastChatSeen;

    /**
     * Session valide ?
     */
    protected boolean valide;

    /**
     * Derni�res informations retourn�es par le refresh
     */
    protected StringBuffer lastRefresh;

    /**
     * Indicateur si le dernier refresh devra �tre r�p�t�
     */
    protected boolean lastRefreshRepeat;

    /**
     * Derni�res informations retourn�es par le refresh (sous partie stable)
     */
    protected StringBuffer lastSubRefresh;

    /**
     * Indicateur si le dernier refresh devra �tre r�p�t� (sous partie stable)
     */
    protected boolean lastSubRefreshRepeat;

    /**
     * La derni�re fois qu'une carte a �t� jou�e
     */
    protected long lastTimeCarteJouee;

    /**
     * Session gel�e ?
     */
    protected boolean frozen;

    /**
     * GameSession constructor comment. Constructeur utilis� en cas de perte de
     * session Le constructeur doit exister
     */
    public GameSession() {
        valide = false;
        lastRefresh = new StringBuffer();
        lastRefreshRepeat = false;
        lastSubRefresh = new StringBuffer();
        lastSubRefreshRepeat = false;
        frozen = false;
    }

    /**
     * SalonSession constructor comment.
     * 
     * @param aCxt
     *            Contexte de servlet actif
     * @param configName
     *            Nom du fichier de config � utiliser
     * @param pseudo
     *            Pseudo du joueur
     * @param crc
     *            Code de controle
     * @throws UnauthorisedUserException
     *             Exception en cas de probl�me de cr�ation. Typiquement la
     *             licence n'est pas correcte
     */
    public GameSession(ServletContext aCxt, String configName, String pseudo, String crc, DBSession dbConnect) throws UnauthorisedUserException, NoDatabaseException {
        super();
        srvCtxt = aCxt;
        frozen = false;
        setResConfig(ResourceBundle.getBundle(configName));
        GameEnvironment env = (GameEnvironment) srvCtxt.getAttribute("Env");
        env.loadParamAire(dbConnect);
        try {
            String passPhrase = resConfig.getString("passPhrase");
            if (env.isSecured()) {
                setSecurity(new SecurityBean(pseudo, crc, passPhrase));
            }
            myJoueur = JoueurBean.getJoueurBeanFromPseudo(dbConnect, pseudo);
            if (myJoueur == null) {
                myJoueur = new JoueurBean();
                myJoueur.setPseudo(pseudo);
                myJoueur.setPrivilege(0);
                try {
                    myJoueur.setAvatarFaiblePerf(new URL("file:///images/avatar.gif"));
                    myJoueur.setAvatarHautePerf(new URL("file:///images/avatar.gif"));
                    myJoueur.create(dbConnect);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw (new UnauthorisedUserException(e.toString()));
                }
            }
        } catch (UnauthorisedUserException e) {
            myJoueur = null;
            e.printStackTrace();
            throw (new UnauthorisedUserException("Acc�s refus�"));
        }
        valide = false;
        lastRefresh = new StringBuffer();
        lastRefreshRepeat = false;
        lastSubRefresh = new StringBuffer();
        lastSubRefreshRepeat = false;
    }

    /**
     * Insert the method's description here. Creation date: (15/09/2001
     * 15:10:45)
     * 
     * @return java.lang.String
     * @param tab
     *            java.util.Object[]
     */
    public static String arrayToString(Object[] tab) {
        String list = new String();
        if (tab != null) {
            for (int i = 0; i < tab.length; i++) {
                list += tab[i] + ",";
            }
        }
        return list;
    }

    /**
     * Insert the method's description here. Creation date: (03/10/2001
     * 12:53:55)
     * 
     * @param date
     *            java.sql.Timestamp
     * @return String date convertie en cha�ne
     */
    public static String dateToString(Calendar date) {
        if (date != null) {
            SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
            return formatDate.formatEG(date.getTime());
        } else {
            return "";
        }
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 18:04:58)
     * 
     * @return Joueur connect�
     */
    public JoueurBean getMyJoueur() {
        return myJoueur;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 18:04:58)
     * 
     * @param newMyJoueur
     *            Joueur qui vient de s'identifier
     */
    public void setMyJoueur(JoueurBean newMyJoueur) {
        myJoueur = newMyJoueur;
    }

    /**
     * @return Bean de s�curit�
     */
    public SecurityBean getSecurity() {
        return security;
    }

    /**
     * @param bean
     *            Bean de s�curit�
     */
    private void setSecurity(SecurityBean bean) {
        security = bean;
    }

    /**
     * @return Adresse de base pour le confirmation d'info
     */
    public String getBaseURL() {
        if (baseURL == null) {
            baseURL = resConfig.getString("baseURL");
        }
        return baseURL;
    }

    /**
     * @param string
     *            Adresse de base pour le confirmation d'info
     */
    public void setBaseURL(String string) {
        baseURL = string;
    }

    /**
     * @return Adresse de fin de parties
     */
    public String getFinURL() {
        if (finURL == null) {
            finURL = resConfig.getString("finURL");
        }
        return finURL;
    }

    /**
     * @param string
     *            Adresse de fin de partie
     */
    public void setFinURL(String string) {
        finURL = string;
    }

    /**
     * @return Le bundle de configuration
     */
    public ResourceBundle getResConfig() {
        return resConfig;
    }

    /**
     * @param bundle
     *            Le bundle de configuration
     */
    private void setResConfig(ResourceBundle bundle) {
        resConfig = bundle;
    }

    /**
     * @return Compteur de chat indiquant le dernier vu
     */
    public long getLastChatSeen() {
        return lastChatSeen;
    }

    /**
     * @param i
     *            Compteur de chat indiquant le dernier vu
     */
    public void setLastChatSeen(long i) {
        if (!frozen) {
            lastChatSeen = i;
        }
    }

    /**
     * @return La session est valide ?
     */
    public boolean isValide() {
        return valide;
    }

    /**
     * @param b
     *            La session est valide ?
     */
    public void setValide(boolean b) {
        valide = b;
    }

    /**
     * @return Derni�res informations retourn�es par le refresh
     */
    public StringBuffer getLastRefresh() {
        return lastRefresh;
    }

    /**
     * @param string
     *            Derni�res informations retourn�es par le refresh
     */
    public void setLastRefresh(StringBuffer string) {
        if (!frozen) {
            lastRefresh = string;
            lastRefreshRepeat = true;
        }
    }

    /**
     * @return La derni�re fois qu'une carte a �t� jou�e
     */
    public long getLastTimeCarteJouee() {
        return lastTimeCarteJouee;
    }

    /**
     * @param timeInMillis
     *            La derni�re fois qu'une carte a �t� jou�e
     */
    public void setLastTimeCarteJouee(long timeInMillis) {
        lastTimeCarteJouee = timeInMillis;
    }

    /**
     * @return Indicateur si le dernier refresh devra �tre r�p�t�
     */
    public boolean isLastRefreshRepeat() {
        return lastRefreshRepeat;
    }

    /**
     * @param b
     *            Indicateur si le dernier refresh devra �tre r�p�t�
     */
    public void setLastRefreshRepeat(boolean b) {
        lastRefreshRepeat = b;
    }

    /**
     * @return Derni�res informations retourn�es par le refresh
     */
    public StringBuffer getLastSubRefresh() {
        return lastSubRefresh;
    }

    /**
     * @param string
     *            Derni�res informations retourn�es par le refresh
     */
    public void setLastSubRefresh(StringBuffer string) {
        if (!frozen) {
            lastSubRefresh = string;
            lastSubRefreshRepeat = true;
        }
    }

    /**
     * @return Indicateur si le dernier refresh devra �tre r�p�t�
     */
    public boolean isLastSubRefreshRepeat() {
        return lastSubRefreshRepeat;
    }

    /**
     * @param b
     *            Indicateur si le dernier refresh devra �tre r�p�t�
     */
    public void setLastSubRefreshRepeat(boolean b) {
        lastSubRefreshRepeat = b;
    }

    /**
     * @return Session gel�e ?
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * @param b
     *            Session gel�e ?
     */
    public void setFrozen(boolean b) {
        frozen = b;
        if (frozen) {
            lastRefresh = new StringBuffer();
            lastSubRefresh = new StringBuffer();
        }
    }

    /**
     * valueBound method comment.
     * 
     * @param arg1 .
     */
    public void valueBound(HttpSessionBindingEvent arg1) {
        if (myJoueur != null) {
            System.out.println(GameSession.dateToString(Calendar.getInstance()) + " Bound : " + myJoueur.getPseudo());
        }
    }

    /**
     * valueUnbound method comment.
     * 
     * @param arg1 .
     */
    public synchronized void valueUnbound(HttpSessionBindingEvent arg1) {
        if (myJoueur != null) {
            GameEnvironment env = (GameEnvironment) srvCtxt.getAttribute("Env");
            if (isValide() && (env.getLstJoueur().contains(myJoueur))) {
                if (env.getLstJoueurDouble().contains(myJoueur)) {
                    env.removeJoueurDouble(myJoueur);
                } else {
                    env.sortieJoueur(myJoueur);
                }
            } else {
            }
            System.out.println(GameSession.dateToString(Calendar.getInstance()) + " Unbound(" + isValide() + ") : " + myJoueur.getPseudo() + " reste : " + env.getLstJoueur().size());
        }
    }
}
