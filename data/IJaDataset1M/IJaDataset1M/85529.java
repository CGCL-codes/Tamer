package net.ad.adsp.entity;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.envers.Audited;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "USER_PROPERTIES")
@Audited
public class UserProperties implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Integer userPropertiesId;

    private User user;

    private Integer rowsPage;

    private String skinName;

    public UserProperties() {
    }

    public UserProperties(User user, Integer rowsPage) {
        this.user = user;
        this.rowsPage = rowsPage;
        this.skinName = "classic";
    }

    public UserProperties(Integer userPropertiesId, User user, Integer rowsPage) {
        this.userPropertiesId = userPropertiesId;
        this.user = user;
        this.rowsPage = rowsPage;
        this.skinName = "classic";
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "USER_PROPERTIES_ID", unique = true, nullable = false)
    public Integer getUserPropertiesId() {
        return userPropertiesId;
    }

    public void setUserPropertiesId(Integer userPropertiesId) {
        this.userPropertiesId = userPropertiesId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "ROWS_PAGE")
    public Integer getRowsPage() {
        return rowsPage;
    }

    public void setRowsPage(Integer rowsPage) {
        this.rowsPage = rowsPage;
    }

    @Column(name = "SKIN_NAME", length = 50)
    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }
}
