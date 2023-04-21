package net.taylor.ldap;

import java.util.HashMap;
import java.util.Map;
import net.taylor.ldap.annotations.FilterDef.SearchScope;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

/**
 * This classes provides metadata to override annotation based mapping data.
 * 
 * In general the annotations should be used for all mapping data. However, for
 * framework classes, such as User and Group, the mappings may need to be
 * overridden.
 * 
 * Create an instance of this component in the components.xml file for each
 * entity that needs to be overridden. The name of the component must match the
 * class name + LdapMetadata.
 * 
 * @author jgilbert01
 */
@Scope(ScopeType.APPLICATION)
@Install(value = false, precedence = Install.FRAMEWORK)
@AutoCreate
@BypassInterceptors
public class Metadata {

    private String[] objectClass = {};

    private String contextDN = null;

    private String dnAttribute = null;

    private Map<String, String> attributes = new HashMap<String, String>();

    private Map<String, Boolean> attributeContainsDN = new HashMap<String, Boolean>();

    private Mapper mapper = new AnnotationMapper(this);

    /**
	 * The set of object classes used when creating a new sub context.
	 */
    public String[] getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String[] objectClass) {
        this.objectClass = objectClass;
    }

    /**
	 * The context or suffix of the distinguished name.
	 */
    public String getContextDN() {
        return contextDN;
    }

    public void setContextDN(String contextDN) {
        this.contextDN = contextDN;
    }

    /**
	 * The attributed used as the prefix of the distinguished name.
	 */
    public String getDnAttribute() {
        return dnAttribute;
    }

    public void setDnAttribute(String dnAttribute) {
        this.dnAttribute = dnAttribute;
    }

    /**
	 * Mapping of pojo property names to ldap attribute names.
	 */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
	 * Name of attributes which contain distinguished names, such as role
	 * membership.
	 */
    public Map<String, Boolean> getAttributeContainsDN() {
        return attributeContainsDN;
    }

    public void setAttributeContainsDN(Map<String, Boolean> attributeContainsDN) {
        this.attributeContainsDN = attributeContainsDN;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    private String filterObjectClass = null;

    private SearchScope searchScope = null;

    public String getFilterObjectClass() {
        return filterObjectClass;
    }

    public void setFilterObjectClass(String filterObjectClass) {
        this.filterObjectClass = filterObjectClass;
    }

    public SearchScope getSearchScope() {
        return searchScope;
    }

    public void setSearchScope(SearchScope searchScope) {
        this.searchScope = searchScope;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
