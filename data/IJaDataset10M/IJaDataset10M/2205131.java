package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author NomaD
 * Обозвал EUser от слова Entity, иба слово User не хорошо пользовать
 * по-первых такой класс в секурити есть ужо, во-вторых зарезервированное слово в SQL
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "EUser.findAll", query = "SELECT u FROM EUser u"), @NamedQuery(name = "EUser.findById", query = "SELECT u FROM EUser u WHERE u.id = :id"), @NamedQuery(name = "EUser.findByNickname", query = "SELECT u FROM EUser u WHERE u.nickname = :nickname"), @NamedQuery(name = "EUser.findByUsername", query = "SELECT u FROM EUser u WHERE u.username = :username"), @NamedQuery(name = "EUser.findByIcq", query = "SELECT u FROM EUser u WHERE u.icq = :icq"), @NamedQuery(name = "EUser.findByPhone", query = "SELECT u FROM EUser u WHERE u.phone = :phone"), @NamedQuery(name = "EUser.findByEmail", query = "SELECT u FROM EUser u WHERE u.email = :email"), @NamedQuery(name = "EUser.findByPassword", query = "SELECT u FROM EUser u WHERE u.password = :password") })
public class EUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    private String nickname;

    @Basic(optional = false)
    private String username;

    private String icq;

    private String phone;

    @Basic(optional = false)
    private String email;

    @Basic(optional = false)
    private String password;

    @Lob
    private byte[] avatar;

    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private List<Ord> ordersList;

    public EUser() {
    }

    public EUser(Integer id) {
        this.id = id;
    }

    public EUser(String name, String email, String password, boolean enabled) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @XmlTransient
    public List<Ord> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Ord> ordersList) {
        this.ordersList = ordersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EUser)) {
            return false;
        }
        EUser other = (EUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EUser[ id=" + id + " ]";
    }
}
