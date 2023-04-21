package com.beanview.base;

import java.util.List;
import com.beanview.BeanView;
import com.beanview.test.PeoplePicker;
import com.beanview.test.Person;
import com.beanview.test.PersonFactory;
import junit.framework.TestCase;

/**
 * Basic verification of the PeoplePicker test object.
 * 
 * @author $Author: wiverson $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/09/19 04:21:41 $
 */
public abstract class PeopleFactoryTestBase extends TestCase {

    PeoplePicker picker;

    BeanView<PeoplePicker> bean;

    @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        super.setUp();
        picker = new PeoplePicker();
        bean = getBean();
        bean.setDataObject(picker);
    }

    public abstract BeanView getBean();

    public void testBeanViewPanelComponents() {
        System.out.println("TEST: testBeanViewPanelComponents");
        FieldCheckUtil.checkFields(bean, picker);
    }

    public void testGetPersonArray() {
        System.out.println("TEST: testGetPersonArray");
        Person[] people = PersonFactory.getPersonArray();
        assertNotNull(people);
        assertEquals(people.length, 5);
    }

    public void testGetPersonList() {
        System.out.println("TEST: testGetPersonList");
        List<Person> people = PersonFactory.getPersonList();
        assertNotNull(people);
        assertEquals(people.size(), 5);
    }
}
