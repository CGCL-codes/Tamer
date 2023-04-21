package de.powerstaff.business.entity;

import java.util.HashSet;
import java.util.Set;
import de.mogwai.common.business.entity.AuditableEntity;
import de.powerstaff.business.dto.SearchRequestSupport;

/**
 * Eine gespeicherte Profilsuche.
 * 
 * @author msertic
 */
public class SavedProfileSearch extends AuditableEntity implements SearchRequestSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8205335843201371353L;

    private User user;

    private String profileContent;

    private String plz;

    private Long stundensatzVon;

    private Long stundensatzBis;

    private String sortierung;

    private Set<String> profilesToIgnore = new HashSet<String>();

    /**
	 * @return the user
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @param user
	 *            the user to set
	 */
    public void setUser(User user) {
        this.user = user;
    }

    /**
	 * @return the profileContent
	 */
    public String getProfileContent() {
        return profileContent;
    }

    /**
	 * @param profileContent
	 *            the profileContent to set
	 */
    public void setProfileContent(String profileContent) {
        this.profileContent = profileContent;
    }

    /**
	 * @return the plz
	 */
    public String getPlz() {
        return plz;
    }

    /**
	 * @param plz
	 *            the plz to set
	 */
    public void setPlz(String plz) {
        this.plz = plz;
    }

    /**
	 * @return the stundensatzVon
	 */
    public Long getStundensatzVon() {
        return stundensatzVon;
    }

    /**
	 * @param stundensatzVon
	 *            the stundensatzVon to set
	 */
    public void setStundensatzVon(Long stundensatzVon) {
        this.stundensatzVon = stundensatzVon;
    }

    /**
	 * @return the stundensatzBis
	 */
    public Long getStundensatzBis() {
        return stundensatzBis;
    }

    /**
	 * @param stundensatzBis
	 *            the stundensatzBis to set
	 */
    public void setStundensatzBis(Long stundensatzBis) {
        this.stundensatzBis = stundensatzBis;
    }

    public String getSortierung() {
        return sortierung;
    }

    public void setSortierung(String sortierung) {
        this.sortierung = sortierung;
    }

    public Set<String> getProfilesToIgnore() {
        return profilesToIgnore;
    }

    public void setProfilesToIgnore(Set<String> profilesToIgnore) {
        this.profilesToIgnore = profilesToIgnore;
    }
}
