package org.unv.mozhi.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the m_lang database table.
 * 
 */
@Entity
@Table(name = "m_lang")
public class MLang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "M_LANG_LANGID_GENERATOR", sequenceName = "MOZHI_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "M_LANG_LANGID_GENERATOR")
    @Column(name = "LANG_ID")
    private int langId;

    @Column(name = "COUNTRY_CD", length = 3)
    private String countryCd;

    @Column(name = "LANG", nullable = false, length = 60)
    private String lang;

    @Column(name = "LOCALE_CD", nullable = false, length = 3)
    private String localeCd;

    @OneToMany(mappedBy = "MLang")
    private Set<MMsgLang> MMsgLangs;

    public MLang() {
    }

    public int getLangId() {
        return this.langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public String getCountryCd() {
        return this.countryCd;
    }

    public void setCountryCd(String countryCd) {
        this.countryCd = countryCd;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLocaleCd() {
        return this.localeCd;
    }

    public void setLocaleCd(String localeCd) {
        this.localeCd = localeCd;
    }

    public Set<MMsgLang> getMMsgLangs() {
        return this.MMsgLangs;
    }

    public void setMMsgLangs(Set<MMsgLang> MMsgLangs) {
        this.MMsgLangs = MMsgLangs;
    }
}
