package ua.org.hatu.daos.engine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TODO: javadocs 
 * 
 * @author zeus (alex.pogrebnyuk@gmail.com)
 * @author dmytro (pogrebniuk@gmail.com)
 *
 */
@Entity
@Table(name = "SESSIONS")
public class UserSession implements DomainObject {

    private static final long serialVersionUID = -2656141655705611871L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TOKEN", unique = true, nullable = false, length = 40)
    private String token;

    @Column(name = "EXPIRED", unique = false, nullable = false)
    private Long expired;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the token
	 */
    public String getToken() {
        return token;
    }

    /**
	 * @param token the token to set
	 */
    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }
}
