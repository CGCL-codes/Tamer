package br.com.arsmachina.accesslogger.hibernate;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import br.com.arsmachina.authentication.entity.User;

/**
 * Persistent {@link br.com.arsmachina.accesslogger.Access} subclass.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@Entity
@Table(name = "access")
public class Access extends br.com.arsmachina.accesslogger.Access {

    private static final int MAXIMUM_IP_LENGTH = 20;

    private static final int MAXIMUM_VARCHAR_LENGTH = 255;

    private static final int MAXIMUM_JSESSIONID_LENGTH = 40;

    private static final int MAXIMUM_REMOTE_HOST_LENGTH = 50;

    private static final int MAXIMUM_LOCALE_LENGTH = 15;

    private Long id;

    /**
	 * Default constructor.
	 */
    public Access() {
    }

    /**
	 * Constructor that receives an {@link br.com.arsmachina.accesslogger.Access} instance. All
	 * property values are copied to the created object.
	 * 
	 * @param access an {@link br.com.arsmachina.accesslogger.Access}. It cannot be null.
	 */
    public Access(br.com.arsmachina.accesslogger.Access access) {
        if (access == null) {
            throw new IllegalArgumentException("Parameter access cannot be null");
        }
        setActivationContext(access.getActivationContext());
        setContextPath(access.getContextPath());
        setIp(access.getIp());
        setLocale(access.getLocale());
        setPage(access.getPage());
        setQueryString(access.getQueryString());
        setReferer(access.getReferer());
        setRemoteHost(access.getRemoteHost());
        setSessionId(access.getSessionId());
        setTimestamp(access.getTimestamp());
        setUrl(access.getUrl());
        setUser(access.getUser());
        setUserAgent(access.getUserAgent());
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getContextPath()
	 */
    @NotNull
    @Length(max = MAXIMUM_VARCHAR_LENGTH)
    @Column(nullable = false, length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getContextPath() {
        return super.getContextPath();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getPage()
	 */
    @Length(max = MAXIMUM_VARCHAR_LENGTH)
    @Column(length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getPage() {
        return super.getPage();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getReferer()
	 */
    @Length(max = MAXIMUM_VARCHAR_LENGTH)
    @Column(length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getReferer() {
        return super.getReferer();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getTimestamp()
	 */
    @NotNull
    @Override
    public Date getTimestamp() {
        return super.getTimestamp();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getUserAgent()
	 */
    @Length(min = 0, max = MAXIMUM_VARCHAR_LENGTH)
    @Column(length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getUserAgent() {
        return super.getUserAgent();
    }

    /**
	 * Returns the value of the <code>id</code> property.
	 * 
	 * @return a {@link Long}.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    /**
	 * Changes the value of the <code>id</code> property.
	 * 
	 * @param id a {@link Long}.
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getActivationContext()
	 */
    @Length(max = MAXIMUM_VARCHAR_LENGTH)
    @Column(length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getActivationContext() {
        return super.getActivationContext();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getIp()
	 */
    @NotNull
    @Length(max = MAXIMUM_IP_LENGTH)
    @Column(nullable = false, length = MAXIMUM_IP_LENGTH)
    @Override
    public String getIp() {
        return super.getIp();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getQueryString()
	 */
    @Length(max = MAXIMUM_VARCHAR_LENGTH)
    @Column(length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getQueryString() {
        return super.getQueryString();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getSessionId()
	 */
    @Length(max = MAXIMUM_JSESSIONID_LENGTH)
    @Column(length = MAXIMUM_JSESSIONID_LENGTH)
    @Override
    public String getSessionId() {
        return super.getSessionId();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getUrl()
	 */
    @NotNull
    @Length(max = MAXIMUM_VARCHAR_LENGTH)
    @Column(nullable = false, length = MAXIMUM_VARCHAR_LENGTH)
    @Override
    public String getUrl() {
        return super.getUrl();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getUser()
	 */
    @ManyToOne
    @Override
    public User getUser() {
        return super.getUser();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getLocale()
	 */
    @Column(length = MAXIMUM_LOCALE_LENGTH)
    @Override
    public String getLocale() {
        return super.getLocale();
    }

    /**
	 * @see br.com.arsmachina.accesslogger.Access#getRemoteHost()
	 */
    @Column(nullable = false, length = MAXIMUM_REMOTE_HOST_LENGTH)
    @Override
    public String getRemoteHost() {
        return super.getRemoteHost();
    }
}
