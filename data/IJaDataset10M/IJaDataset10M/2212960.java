package benchmark.bo;

import org.ujorm.UjoPropertyList;
import org.ujorm.extensions.Property;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.orm.annot.Column;

/**
 *
 * @author Pavel Ponec
 */
public class UjoUser extends OrmTable<UjoUser> {

    @Column(pk = true)
    public static final Property<UjoUser, Long> ID = newProperty("id", Long.class);

    @Column(length = 8)
    public static final Property<UjoUser, String> PERSONAL_ID = newProperty("personalId", String.class);

    public static final Property<UjoUser, String> SURENAME = newProperty("surename", String.class);

    public static final Property<UjoUser, String> LASTNAME = newProperty("lastname", String.class);

    private static UjoPropertyList properties = init(UjoUser.class);

    @Override
    public UjoPropertyList readProperties() {
        return properties;
    }
}
