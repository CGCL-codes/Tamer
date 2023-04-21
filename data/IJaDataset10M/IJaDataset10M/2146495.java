package org.pagger.data.picture;

import org.pagger.data.BaseMetadata;
import org.pagger.data.BaseProperty;
import org.pagger.data.Property;

/**
 *
 * @author gerd
 */
public class ContactMetadata extends BaseMetadata {

    public static final Property<String> NAME = new BaseProperty<String>(String.class, "name", "org.pagger.metadata.contact.name.NAME", "org.pagger.metadata.contact.name.DESCRIPTION");

    public static final Property<String> JOBTITLE = new BaseProperty<String>(String.class, "jobtitle", "org.pagger.metadata.contact.jobtitle.NAME", "org.pagger.metadata.contact.jobtitle.DESCRIPTION");

    public static final Property<String> ADDRESS = new BaseProperty<String>(String.class, "address", "org.pagger.metadata.contact.address.NAME", "org.pagger.metadata.contact.address.DESCRIPTION");

    public static final Property<String> CITY = new BaseProperty<String>(String.class, "city", "org.pagger.metadata.contact.city.NAME", "org.pagger.metadata.contact.city.DESCRIPTION");

    public static final Property<String> POSTCODE = new BaseProperty<String>(String.class, "postcode", "org.pagger.metadata.contact.postcode.NAME", "org.pagger.metadata.contact.postcode.DESCRIPTION");

    public static final Property<String> PROVINCE = new BaseProperty<String>(String.class, "province", "org.pagger.metadata.contact.province.NAME", "org.pagger.metadata.contact.province.DESCRIPTION");

    public static final Property<String> COUNTRY = new BaseProperty<String>(String.class, "country", "org.pagger.metadata.contact.country.NAME", "org.pagger.metadata.contact.country.DESCRIPTION");

    public static final Property<String> TELEPHONE = new BaseProperty<String>(String.class, "telephone", "org.pagger.metadata.contact.telephone.NAME", "org.pagger.metadata.contact.telephone.DESCRIPTION");

    public static final Property<String> EMAIL = new BaseProperty<String>(String.class, "email", "org.pagger.metadata.contact.email.NAME", "org.pagger.metadata.contact.email.DESCRIPTION");

    public static final Property<String> WEBSITE = new BaseProperty<String>(String.class, "website", "org.pagger.metadata.contact.website.NAME", "org.pagger.metadata.contact.website.DESCRIPTION");

    public ContactMetadata() {
        super(ContactMetadata.class.getName(), "org.pagger.metadata.contact.NAME", "org.pagger.metadata.contact.DESCRIPTION");
    }
}
