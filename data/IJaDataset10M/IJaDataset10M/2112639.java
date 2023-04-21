package thomas.bier.shared;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Gebruiker implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    private String nickname;

    private String email;

    private String ww;

    private String geheimevraag;

    private String geheimantwoord;

    private boolean blokbeheerder;

    private boolean blokstart;

    private boolean isBeheerder;

    public Gebruiker() {
    }

    public Gebruiker(String nickname, String email, String ww, String geheimevraag, String geheimantwoord) {
        this.nickname = nickname;
        this.email = email;
        this.ww = ww;
        this.geheimevraag = geheimevraag;
        this.geheimantwoord = geheimantwoord;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setWw(String ww) {
        this.ww = ww;
    }

    public String getWw() {
        return ww;
    }

    public void setGeheimevraag(String geheimevraag) {
        this.geheimevraag = geheimevraag;
    }

    public String getGeheimevraag() {
        return geheimevraag;
    }

    public void setGeheimantwoord(String geheimantwoord) {
        this.geheimantwoord = geheimantwoord;
    }

    public String getGeheimantwoord() {
        return geheimantwoord;
    }

    public void setBlokbeheerder(boolean blokbeheerder) {
        this.blokbeheerder = blokbeheerder;
    }

    public boolean isBlokbeheerder() {
        return blokbeheerder;
    }

    public void setBlokstart(boolean blokstart) {
        this.blokstart = blokstart;
    }

    public boolean isBlokstart() {
        return blokstart;
    }

    public void setBeheerder(boolean isBeheerder) {
        this.isBeheerder = isBeheerder;
    }

    public boolean isBeheerder() {
        return isBeheerder;
    }
}
