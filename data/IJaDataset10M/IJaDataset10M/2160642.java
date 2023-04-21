package jmsngr;

import java.io.IOException;
import ymsg.network.*;

/**
 * Bas� sur le package ymsg
 * et le tutorial http://www.devx.com/Java/Article/22546/0/page/3
 *
 * @author PHELIZOT Yvan
 */
public class YClient {

    Session m_session = null;

    JSessionListener m_sessionListener = null;

    JConnexion m_jConnect = null;

    java.util.Map<String, JDialog> m_jDialogs = null;

    java.util.Map<String, JConference> m_jConferences = null;

    JMain m_jMain = null;

    String m_szStatus = null;

    boolean isConnected = false;

    JOfflineMsg m_offlinemsg = null;

    /** Creates a new instance of YClient */
    public YClient() {
        m_session = new Session();
        m_sessionListener = new JSessionListener(this);
        m_session.addSessionListener(m_sessionListener);
        m_jConnect = new JConnexion(this);
        m_offlinemsg = new JOfflineMsg(this);
    }

    public void sendBuzz(String szUser) {
        if (aUneCommunicationAvec(szUser)) {
            try {
                m_session.sendBuzz(szUser);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void ajouterMessage(String szContact, String szMessage, String szDate) {
        if (!m_offlinemsg.isVisible()) m_offlinemsg.setVisible(true);
        m_offlinemsg.ajouterMessage(szContact, szMessage, szDate);
    }

    /** Connexion � Yahoo!
      * <p>
      * Pre : la connexion n'est pas �tablie
      * @param  usr     login 
      * @param  pwd     mot de passe
      * @return         retourne vrai si la connexion est r�ussie
      */
    public boolean connecter(String usr, String pwd) {
        try {
            if (isConnected) return false;
            m_session.login(usr, pwd);
            isConnected = true;
            m_jMain = new JMain(this);
            m_jDialogs = new java.util.TreeMap<String, JDialog>();
            m_jConferences = new java.util.TreeMap<String, JConference>();
            return true;
        } catch (AccountLockedException ale) {
            System.out.println("Exception AccountLockedException dans connecter - " + ale.getMessage());
        } catch (LoginRefusedException lre) {
            System.out.println("Exception LoginRefuserException dans connecter - " + lre.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("Exception IOException dans connecter - " + ioe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        m_session.reset();
        return false;
    }

    /**
     * Affiche la fenetre qui permet d'entrer le login et le mot de passe
     */
    public void connecter() {
        m_jConnect.setVisible(true);
    }

    /**
     * retourne la valeur de isConnected
     * @return retourne vrai si on est connect�
     */
    public boolean estConnecter() {
        return isConnected;
    }

    /**
     * Connexion � Yahoo!
     * <p>
     * Pre : la connexion est etablie
     * @return retourne vrai si la session est fermee sans probleme, faux sinon
     */
    public boolean deconnecter() {
        try {
            if (!isConnected) return false;
            m_session.logout();
            isConnected = false;
            return true;
        } catch (java.io.IOException ioe) {
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * permet d'obtenir les contacts regroup�s par groupe
     * @return retourne les groupes de contacts
     */
    public YahooGroup[] getYahooGroup() {
        return m_session.getGroups();
    }

    public YahooUser getYahooUser(String contact) {
        return m_session.getUser(contact);
    }

    /**
     * affiche la fenetre principale
     */
    public void afficher() {
        m_jMain.setVisible(true);
        m_jMain.afficher();
    }

    /**
     * permet d'ouvrir une fenetre de dialogue pour communiquer avec un contact
     * @param contact contact avec lequel on parle
     */
    public void ouvrirDialogueAvec(String contact) {
        if (isConnected) if (!m_jDialogs.containsKey(contact)) m_jDialogs.put(contact, new JDialog(this, contact));
    }

    public void retirerContact(String szContact, String szGroupe) {
        if (aUneCommunicationAvec(szContact)) fermerDialogueAvec(szContact);
        try {
            m_session.removeFriend(szContact, szGroupe);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Envoie d'un message 
     * Pre : la connexion est etablie
     * @param msg message � envoyer
     * @param contact contact � qui sera envoy� le message
     */
    public void envoyerMessageA(String contact, String msg) {
        System.out.print("Envoi du message " + msg + " � " + contact + "...");
        try {
            if (isConnected) {
                m_session.sendMessage(contact, msg);
                System.out.println("OK");
            }
        } catch (java.io.IOException ioe) {
            System.out.println("Erreur_E/S");
            ioe.printStackTrace();
        }
    }

    /**
     * Permet d'afficher un message recu d'un contact
     * @param msg Message envoye par un contact
     * @param contact Contact qui a envoye un message
     */
    public void recevoirMessageDe(String msg, String contact) {
        System.out.print("Reception d'un message " + msg + " de " + contact + "...");
        ouvrirDialogueAvec(contact);
        m_jDialogs.get(contact).recevoirMessageDe(msg);
        System.out.println("OK");
        if (isAutoReply()) envoyerMessageA(contact, "Message de r�ponse automatique :  " + getSzAutoReplyMesg());
    }

    /**
     * retourne l'identifiant de connexion de l'utilisateur, <I>m_session.getLoginIdentity().getId()</I>
     * @return Identifiant de connexion de l'utilisateur
     */
    public String getId() {
        return m_session.getLoginIdentity().getId();
    }

    /**
     * permet de determiner si l'utilisateur communique avec le contact fourni en parametre
     * @return retourne vrai si l'utilisateur a une connexion avec le contact
     * @param contact string
     */
    public boolean aUneCommunicationAvec(String contact) {
        return (m_jDialogs != null && m_jDialogs.containsValue(contact) ? (m_jDialogs.get(contact) != null) : false);
    }

    /**
     * 
     */
    public void fermerDialogueAvec(String contact) {
        if (contact == null || m_jDialogs == null || m_jDialogs.containsValue(contact)) {
            m_jDialogs.remove(contact);
        }
    }

    public void ajouterComposantNotifiantEcriture(String contact, java.awt.Component comp) {
        m_session.addTypingNotification(contact, comp);
    }

    /**
     * Definit le status de l'utilisateur et s'il est occup� ou pas.
     * @param szStatus string indiquant la valeur du status (en train de regarder un film, disponible, ...)
     * @param dispo indique si l'utilisateur est occup� ou non (busy)
     */
    public void definirStatus(String szStatus, boolean dispo) {
        try {
            m_session.setStatus(szStatus, dispo);
            m_szStatus = szStatus;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Arrete toute l'application
     */
    public void detruire() {
        if (estConnecter()) try {
            m_session.logout();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        isConnected = false;
        m_jConnect = null;
        m_jDialogs = null;
        m_jMain = null;
        m_jConferences = null;
        System.exit(0);
    }

    public String obtenirStatus() {
        return m_szStatus;
    }

    /**
     * Holds value of property isAutoReply.
     */
    private boolean autoReply;

    /**
     * Getter for property isAutoReply.
     * @return Value of property isAutoReply.
     */
    public boolean isAutoReply() {
        return this.autoReply;
    }

    /**
     * Setter for property isAutoReply.
     * @param isAutoReply New value of property isAutoReply.
     */
    public void setAutoReply(boolean isAutoReply) {
        this.autoReply = isAutoReply;
    }

    /**
     * Holds value of property szAutoReplyMesg.
     */
    private String szAutoReplyMesg;

    /**
     * Getter for property szAutoReplyMesg.
     * @return Value of property szAutoReplyMesg.
     */
    public String getSzAutoReplyMesg() {
        return this.szAutoReplyMesg;
    }

    /**
     * Setter for property szAutoReplyMesg.
     * @param szAutoReplyMesg New value of property szAutoReplyMesg.
     */
    public void setSzAutoReplyMesg(String szAutoReplyMesg) {
        this.szAutoReplyMesg = szAutoReplyMesg;
    }

    /**
     * Envoie d'un message 
     * Pre : la connexion est etablie
     * @param msg message � envoyer
     * @param contact contact � qui sera envoy� le message
     */
    public void envoyerConferenceMessageA(YahooConference id, String msg) {
        System.out.print("Envoi du message " + msg + "...");
        try {
            if (isConnected) {
                m_session.sendConferenceMessage(id, msg);
                System.out.println("OK");
            }
        } catch (java.io.IOException ioe) {
            System.out.println("Erreur_E/S");
            ioe.printStackTrace();
        }
    }

    public void ouvrirConferenceAvec(String[] users, String msg) {
        try {
            YahooConference yc = m_session.createConference(users, msg);
            m_jConferences.put(yc.getName(), new JConference(this, users, yc));
        } catch (IllegalIdentityException ex) {
            ex.printStackTrace();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Permet d'afficher un message recu d'un contact
     * @param msg Message envoye par un contact
     * @param contact Contact qui a envoye un message
     */
    public void recevoirConferenceMessageDe(YahooConference id, String contact, String message) {
        System.out.print("Reception d'un message " + message + " de " + contact + "...");
        ouvrirDialogueAvec(contact);
        m_jConferences.get(id.getName()).recevoirConferenceMessage(id, contact, message);
        System.out.println("OK");
    }

    public void aQuitterConference(YahooConference id, String contact) {
        System.out.println(contact + " a quitte la conference");
        m_jConferences.get(id.getName()).aQuitterConference(id, contact);
    }

    public void quitterConference(YahooConference id) {
        System.out.println("Vous quittez la conference");
        m_jConferences.remove(id.getName());
        try {
            m_session.leaveConference(id);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (NoSuchConferenceException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
