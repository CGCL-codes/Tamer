package ch.trackedbean.server.data.to;

import java.io.*;
import ch.trackedbean.copier.annotations.*;
import ch.trackedbean.server.data.dom.*;
import ch.trackedbean.tracking.*;

@SourceClass(PersonDom.class)
public class PersonShort implements Serializable, TrackedBean, TO {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = -3921097274065191842L;

    /**
	 * Property constant for {@link #getKey()}/{@link #setKey(int)}. Type {@link int}.
	 */
    public static final String ATTR_KEY = "key";

    /**
	 * Property constant for {@link #getFirstName()}/{@link #setFirstName(String)}. Type {@link String}.
	 */
    public static final String ATTR_FIRST_NAME = "firstName";

    /**
	 * Property constant for {@link #getName()}/{@link #setName(String)}. Type {@link String}.
	 */
    public static final String ATTR_NAME = "name";

    /**
	 * Property constant for {@link #getAge()}/{@link #setAge(int)}. Type {@link int}.
	 */
    public static final String ATTR_AGE = "age";

    /**
	 * Property constant for {@link #getAddress()}/{@link #setAddress(AddressTO)}. Type {@link AddressTO}.
	 */
    public static final String ATTR_ADDRESS = "address";

    private Integer key;

    private String name;

    private String firstName;

    private int age;

    @DeepPathMapping(path = PersonDom.ATTR_ADDRESSES + "[0]", lastPathType = AddressDom.class, readOnly = true)
    private AddressTO address;

    /**
	 * @return Returns the key.
	 */
    public Integer getKey() {
        return key;
    }

    /**
	 * @param key The key to set.
	 */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the firstName.
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * @param firstName The firstName to set.
	 */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * @return Returns the age.
	 */
    public int getAge() {
        return age;
    }

    /**
	 * @param age The age to set.
	 */
    public void setAge(int age) {
        this.age = age;
    }

    /**
	 * @return Returns the address.
	 */
    public AddressTO getAddress() {
        return address;
    }

    /**
	 * @param address The address to set.
	 */
    public void setAddress(AddressTO address) {
        this.address = address;
    }
}
