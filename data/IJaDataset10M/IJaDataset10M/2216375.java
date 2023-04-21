package protocol;

import client.client.Network;
import db.ATException;
import db.SynchroData;
import java.io.ObjectOutputStream;
import server.ClientConnectionThread;
import server.UserAuthorization;

/**
 * Trieda zabezpecuja prihlasovanie sa do systemu
 * 
 * @author marek
 */
public class Login extends ArcticProtocol {

    private String mail;

    private String nick;

    private String password;

    private boolean authorized;

    /**
     * Vytvori novy login
     * @param mail Email uzivatela pre autorizaciu
     * @param password Heslo uzivatela pre autorizaciu
     */
    public Login(String mail, String password) {
        this.mail = mail;
        this.password = password;
        this.authorized = false;
    }

    @Override
    public void process() {
        SynchroData sd = SynchroData.getInstance();
        UserAuthorization ua = new UserAuthorization();
        ClientConnectionThread cct = (ClientConnectionThread) (ClientConnectionThread.currentThread());
        ObjectOutputStream output = cct.getOS();
        sd.addUserOS(mail, output);
        try {
            try {
                ua.authorize(this);
                if (!authorized) {
                } else {
                    sd.registerOnline(mail);
                    nick = db.Database.getInstance().getBuddyName(mail);
                    sd.notifyBuddyStatus(new Contact("server", mail, nick, true));
                }
                output.writeObject(this);
                output.flush();
            } catch (ATException a) {
                a.printStackTrace();
                output.writeObject(new Error(Error.INVALID_USERNAME));
                output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return Mail uzivatela, ktory sa snazi prihlasit
     */
    public String getMail() {
        return mail;
    }

    /**
     * 
     * @return Heslo
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @return Meno užívateľa
     */
    public String getNick() {
        return nick;
    }

    /**
     * 
     * @return Ano/Nie, ci kredity presli autorizaciou
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * 
     * @param authorized Autorizovany Ano/Nie
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    /**
     * Spracovanie u klienta
     * @param net sieť
     */
    @Override
    public void execute(Network net) {
        net.getClientGuiThread().getLoginFrame().process(this);
    }
}
