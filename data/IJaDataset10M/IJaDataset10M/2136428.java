package jp.go.aist.six.oval.model.independent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jp.go.aist.six.oval.model.Component;
import jp.go.aist.six.oval.model.Family;
import jp.go.aist.six.oval.model.sc.EntityItemAnySimpleType;
import jp.go.aist.six.oval.model.sc.EntityItemStringType;
import jp.go.aist.six.oval.model.sc.ItemType;

/**
 * This element holds information about specific entries in the LDAP directory.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: LdapItem.java 2276 2012-04-02 08:07:18Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class LdapItem extends ItemType {

    private EntityItemStringType suffix;

    private EntityItemStringType relative_dn;

    private EntityItemStringType attribute;

    private EntityItemStringType object_class;

    private EntityItemLdaptypeType ldaptype;

    private final Collection<EntityItemAnySimpleType> value = new ArrayList<EntityItemAnySimpleType>();

    /**
     * Constructor.
     */
    public LdapItem() {
        this(0);
    }

    public LdapItem(final int id) {
        super(id);
        _oval_family = Family.INDEPENDENT;
        _oval_component = Component.LDAP;
    }

    /**
     */
    public void setSuffix(final EntityItemStringType suffix) {
        this.suffix = suffix;
    }

    public EntityItemStringType getSuffix() {
        return suffix;
    }

    /**
     */
    public void setRelativeDn(final EntityItemStringType relative_dn) {
        this.relative_dn = relative_dn;
    }

    public EntityItemStringType getRelativeDn() {
        return relative_dn;
    }

    /**
     */
    public void setAttribute(final EntityItemStringType attribute) {
        this.attribute = attribute;
    }

    public EntityItemStringType getAttribute() {
        return attribute;
    }

    /**
     */
    public void setLdaptype(final EntityItemLdaptypeType ldaptype) {
        this.ldaptype = ldaptype;
    }

    public EntityItemLdaptypeType getLdaptype() {
        return ldaptype;
    }

    /**
     */
    public void setObjectClass(final EntityItemStringType object_class) {
        this.object_class = object_class;
    }

    public EntityItemStringType getObjectClass() {
        return object_class;
    }

    /**
     */
    public void setValue(final Collection<? extends EntityItemAnySimpleType> values) {
        value.clear();
        if (values != null && values.size() > 0) {
            for (EntityItemAnySimpleType value : values) {
                addValue(value);
            }
        }
    }

    public boolean addValue(final EntityItemAnySimpleType value) {
        if (value == null) {
            throw new IllegalArgumentException("empty value");
        }
        return this.value.add(value);
    }

    public Collection<EntityItemAnySimpleType> getValue() {
        return value;
    }

    public Iterator<EntityItemAnySimpleType> iterateValue() {
        return value.iterator();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof LdapItem)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ldap_item[" + super.toString() + ", suffix=" + getSuffix() + ", relative_dn=" + getRelativeDn() + ", attribute=" + getAttribute() + ", ldaptype=" + getLdaptype() + ", object_class=" + getObjectClass() + ", value=" + getValue() + "]";
    }
}
