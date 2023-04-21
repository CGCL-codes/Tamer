package org.fultest.test.ui;

import com.opensymphony.xwork2.Action;
import org.fulworx.core.descriptor.URITemplate;
import org.fulworx.core.descriptor.action.Accessor;
import org.fulworx.core.descriptor.action.CreateAction;
import org.fulworx.core.descriptor.action.DeleteAction;
import org.fulworx.core.descriptor.action.ReadAction;

/**
 * @version $Id: $
 */
@URITemplate(uri = "/someResource")
@Accessor("entity")
public class SomeAction implements CreateAction, ReadAction, DeleteAction {

    private SomeVO entity;

    private String readValue;

    private String firstName;

    private String lastName;

    private String street1;

    private String street2;

    private Integer value;

    private static String name;

    public String getReadValue() {
        return readValue;
    }

    public void setReadValue(String readValue) {
        this.readValue = readValue;
    }

    public SomeVO getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = (SomeVO) entity;
    }

    public String execute() throws Exception {
        return Action.NONE;
    }

    public String create() throws Exception {
        SomeVO someVO = getEntity();
        someVO.setValue(1);
        return SUCCESS;
    }

    public String read() throws Exception {
        SomeVO someVO = new SomeVO();
        someVO.setFirstname("read-first");
        someVO.setLastname("read-last");
        if (readValue != null) {
            someVO.setValue(new Integer(readValue));
        }
        setEntity(someVO);
        return SUCCESS;
    }

    public String delete() {
        SomeVO someVO = new SomeVO();
        someVO.setFirstname("isdeleted");
        someVO.setLastname("isdeleted");
        someVO.setValue(value);
        SomeAddrVO addressVO = new SomeAddrVO();
        addressVO.setStreet1(street1);
        addressVO.setStreet2(street2);
        someVO.setAddress(addressVO);
        setEntity(someVO);
        return SUCCESS;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String aname) {
        name = aname;
    }

    public static String getTheName() {
        return name;
    }

    public static void setTheName(String aname) {
        name = aname;
    }
}
