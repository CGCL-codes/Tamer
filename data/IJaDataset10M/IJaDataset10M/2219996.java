package hr.chus.cchat.model.db.jpa;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.ejb.QueryHints;

/**
 * 
 * Class describes ServiceProvider DAO model which will be used for JPA/Hibernate implementation.
 * Class defines queries, table and column names and cache.
 * 
 * Service providers are telcos which we use to send messages. Every user has its service provider.
 * 
 * @author Jan Čustović (jan_custovic@yahoo.com)
 *
 */
@Entity
@Table(name = "service_provider")
@NamedQueries({ @NamedQuery(name = "ServiceProvider.getAll", query = "SELECT sp FROM ServiceProvider sp", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") }), @NamedQuery(name = "ServiceProvider.getByNameAndSc", query = "SELECT sp FROM ServiceProvider sp WHERE sp.sc = :sc AND sp.providerName = :providerName", hints = { @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "true") }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String sc;

    private String providerName;

    private String serviceName;

    private Float billingAmount;

    private String description;

    private Boolean disabled;

    private String sendServiceBeanName;

    private Set<ServiceProviderKeyword> serviceProviderKeywords;

    public ServiceProvider() {
    }

    public ServiceProvider(String sc, String providerName, String serviceName, String description, boolean disabled) {
        this.sc = sc;
        this.providerName = providerName;
        this.serviceName = serviceName;
        this.description = description;
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return String.format("ServiceProvider[ID: %s, ProviderName: %s, SC: %s, ServiceName: %s, Disabled: %s]", new Object[] { id, providerName, sc, serviceName, disabled });
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof ServiceProvider)) return false;
        ServiceProvider serviceProvider = (ServiceProvider) object;
        return (serviceProvider.getId().equals(id));
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "sc", length = 20, nullable = false)
    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    @Column(name = "provider_name", length = 30, nullable = false)
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Column(name = "service_name", length = 30, nullable = false)
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Column(name = "billing_amount")
    public Float getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Float billingAmount) {
        this.billingAmount = billingAmount;
    }

    @Column(name = "description", length = 200, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "disabled", nullable = false)
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Column(name = "send_service_bean", length = 30)
    public String getSendServiceBeanName() {
        return sendServiceBeanName;
    }

    public void setSendServiceBeanName(String sendServiceBeanName) {
        this.sendServiceBeanName = sendServiceBeanName;
    }

    @OneToMany(mappedBy = "serviceProvider", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<ServiceProviderKeyword> getServiceProviderKeywords() {
        return serviceProviderKeywords;
    }

    public void setServiceProviderKeywords(Set<ServiceProviderKeyword> serviceProviderKeywords) {
        this.serviceProviderKeywords = serviceProviderKeywords;
    }
}
