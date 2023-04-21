package com.objectwave.persist.xml.test;

import com.objectwave.persist.examples.DomainObject;
import java.lang.reflect.Field;
import java.util.Vector;

/**
 * @author  cson
 * @version  $Id: PersonPhone.java,v 1.1 2004/10/28 16:31:53 dave_hoag Exp $
 */
public class PersonPhone extends DomainObject {

    /**
	 */
    public static Field _oid;

    /**
	 */
    public static Field _person;

    /**
	 */
    public static Field _phone;

    long oid;

    Person person;

    Phone phone;

    /**
	 *Constructor for the PersonPhone object
	 *
	 * @exception  Exception
	 */
    public PersonPhone() throws Exception {
        setObjectEditor(initializeObjectEditor("PersonPhone.xml", this));
    }

    /**
	 *Sets the Oid attribute of the PersonPhone object
	 *
	 * @param  aValue The new Oid value
	 */
    public void setOid(long aValue) {
        editor.set(_oid, aValue, oid);
    }

    /**
	 *Sets the Person attribute of the PersonPhone object
	 *
	 * @param  aValue The new Person value
	 */
    public void setPerson(Person aValue) {
        editor.set(_person, aValue, person);
    }

    /**
	 *Sets the Phone attribute of the PersonPhone object
	 *
	 * @param  aValue The new Phone value
	 */
    public void setPhone(Phone aValue) {
        editor.set(_phone, aValue, phone);
    }

    /**
	 *Gets the Oid attribute of the PersonPhone object
	 *
	 * @return  The Oid value
	 */
    public long getOid() {
        return (long) editor.get(_oid, oid);
    }

    /**
	 *Gets the Person attribute of the PersonPhone object
	 *
	 * @return  The Person value
	 */
    public Person getPerson() {
        return (Person) editor.get(_person, person);
    }

    /**
	 *Gets the Phone attribute of the PersonPhone object
	 *
	 * @return  The Phone value
	 */
    public Phone getPhone() {
        return (Phone) editor.get(_phone, phone);
    }

    /**
	 * @param  get
	 * @param  data
	 * @param  fields
	 */
    public void update(boolean get, Object[] data, Field[] fields) {
        for (int i = 0; i < data.length; i++) {
            try {
                if (get) {
                    data[i] = fields[i].get(this);
                } else {
                    fields[i].set(this, data[i]);
                }
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
        }
    }

    static {
        try {
            _oid = PersonPhone.class.getDeclaredField("oid");
            _person = PersonPhone.class.getDeclaredField("person");
            _phone = PersonPhone.class.getDeclaredField("phone");
        } catch (NoSuchFieldException ex) {
            System.out.println(ex);
        }
    }
}
