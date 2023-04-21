package cn.sduo.app.po;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "SA_I18N_MSG")
public class SaI18NMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String mkey;

    private String mtype;

    private String locale;

    private String longDesp;

    private String shortDesp;

    public SaI18NMsg() {
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy("ASC")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "MKEY", nullable = false, length = 50)
    public String getMkey() {
        return this.mkey;
    }

    public void setMkey(String key) {
        this.mkey = key;
    }

    @Column(name = "MTYPE", nullable = false, length = 50)
    public String getMtype() {
        return this.mtype;
    }

    public void setMtype(String type) {
        this.mtype = type;
    }

    @Column(nullable = false, length = 50)
    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Column(name = "LONG_DESP", length = 20000)
    public String getLongDesp() {
        return this.longDesp;
    }

    public void setLongDesp(String longDesp) {
        this.longDesp = longDesp;
    }

    @Column(name = "SHORT_DESP", nullable = false, length = 200)
    public String getShortDesp() {
        return this.shortDesp;
    }

    public void setShortDesp(String shortDesp) {
        this.shortDesp = shortDesp;
    }
}
