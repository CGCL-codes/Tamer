package org.plazmaforge.bsolution.contact.common.beans;

import java.util.List;
import java.util.ArrayList;
import org.plazmaforge.framework.config.object.IEntityConfig;
import org.plazmaforge.framework.core.data.SimpleBean;
import org.plazmaforge.framework.core.data.ISimpleBean;

/**
 * @author Oleh Hapon Date: 23.02.2004 Time: 20:26:25 $Id: ContactableElement.java,v 1.3 2010/12/05 07:56:01 ohapon Exp $
 * 
 * This is implementation <code>IContactable</code>
 * 
 */
public class ContactableElement extends SimpleBean implements IContactable {

    /**
     * This is owner for contactable Example: partner, employee, person and etc.
     * 
     */
    private IContactableOwner owner;

    private Address address;

    private Email email;

    private Phone phone;

    private Web web;

    private Integer addressId;

    private Integer emailId;

    private Integer phoneId;

    private Integer webId;

    private List addressList;

    private List emailList;

    private List phoneList;

    private List webList;

    private String icq;

    private String msn;

    private IEntityConfig entity;

    public ContactableElement() {
    }

    public ContactableElement(IContactableOwner owner) {
        this.owner = owner;
    }

    public ContactableElement(IContactableOwner owner, IEntityConfig entity) {
        this.owner = owner;
        this.entity = entity;
    }

    public ContactableElement(IEntityConfig entity) {
        this.entity = entity;
    }

    public Integer getOwnerId() {
        return owner == null ? null : owner.getId();
    }

    public void setOwnerId(Integer id) {
    }

    public void setEntity(IEntityConfig entity) {
        this.entity = entity;
    }

    protected IEntityConfig doGetEntity() {
        return entity == null ? owner.getEntity() : entity;
    }

    public IEntityConfig getEntity() {
        return doGetEntity();
    }

    public String getEntityId() {
        return doGetEntity().getId();
    }

    public void setEntityId(String id) {
    }

    public String getEntityConfigId() {
        return doGetEntity().getConfigId();
    }

    public String getFullName() {
        return owner.getFullName();
    }

    public IContactableOwner getOwner() {
        return owner;
    }

    public void setOwner(IContactableOwner owner) {
        this.owner = owner;
    }

    public Integer getContactableId() {
        return getId();
    }

    public List getAddressList() {
        if (addressList == null) {
            addressList = createList();
        }
        return addressList;
    }

    public void setAddressList(List addressList) {
        this.addressList = addressList;
    }

    public List getEmailList() {
        if (emailList == null) {
            emailList = createList();
        }
        return emailList;
    }

    public void setEmailList(List emailList) {
        this.emailList = emailList;
    }

    public List getPhoneList() {
        if (phoneList == null) {
            phoneList = createList();
        }
        return phoneList;
    }

    public void setPhoneList(List phoneList) {
        this.phoneList = phoneList;
    }

    public List getWebList() {
        if (webList == null) {
            webList = createList();
        }
        return webList;
    }

    public void setWebList(List webList) {
        this.webList = webList;
    }

    public void addAddress(Address item) {
        item.setContactableElement(this);
        getAddressList().add(item);
        if (getAddressList().size() == 1) {
            setAddress(item);
        }
    }

    public void addEmail(Email item) {
        item.setContactableElement(this);
        getEmailList().add(item);
        if (getEmailList().size() == 1) {
            setEmail(item);
        }
    }

    public void addPhone(Phone item) {
        item.setContactableElement(this);
        getPhoneList().add(item);
        if (getPhoneList().size() == 1) {
            setPhone(item);
        }
    }

    public void addWeb(Web item) {
        item.setContactableElement(this);
        getWebList().add(item);
        if (getWebList().size() == 1) {
            setWeb(item);
        }
    }

    public void removeAddress(Address item) {
        if (item == null) {
            return;
        }
        resetAddress(item);
        item.setContactableElement(null);
        getAddressList().remove(item);
    }

    public void removeEmail(Email item) {
        if (item == null) {
            return;
        }
        resetEmail(item);
        item.setContactableElement(null);
        getEmailList().remove(item);
    }

    public void removePhone(Phone item) {
        if (item == null) {
            return;
        }
        resetPhone(item);
        item.setContactableElement(null);
        getPhoneList().remove(item);
    }

    public void removeWeb(Web item) {
        if (item == null) {
            return;
        }
        resetWeb(item);
        item.setContactableElement(null);
        getWebList().remove(item);
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getWebId() {
        return webId;
    }

    public void setWebId(Integer webId) {
        this.webId = webId;
    }

    protected List createList() {
        return new ArrayList();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getDefaultAddressId() {
        return address == null || address.isDeleted() ? null : address.getId();
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Integer getDefaultEmailId() {
        return email == null || email.isDeleted() ? null : email.getId();
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Integer getDefaultPhoneId() {
        return phone == null || phone.isDeleted() ? null : phone.getId();
    }

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public Integer getDefaultWebId() {
        return web == null || web.isDeleted() ? null : web.getId();
    }

    public void init() {
        super.init();
        getAddressList().size();
        getEmailList().size();
        getPhoneList().size();
        getWebList().size();
        initDefaultItems();
    }

    /**
     * Initialize default items (address, email, phone, web) by Ids (addressId, emailId, phoneId, webId)
     */
    public void initDefaultItems() {
        setAddress((Address) getItemById(getAddressList(), getAddressId()));
        setEmail((Email) getItemById(getEmailList(), getEmailId()));
        setPhone((Phone) getItemById(getPhoneList(), getPhoneId()));
        setWeb((Web) getItemById(getWebList(), getWebId()));
    }

    /**
     * Initialize default item Ids (addressId, emailId, phoneId, webId) 
     */
    public void initDefaultItemsId() {
        setAddressId(getIdByItem(getAddress()));
        setEmailId(getIdByItem(getEmail()));
        setPhoneId(getIdByItem(getPhone()));
        setWebId(getIdByItem(getWeb()));
    }

    /**
     * Reset (set null) default item Ids (addressId, emailId, phoneId, webId)
     */
    public void resetDefaultItemsId() {
        setAddressId(null);
        setEmailId(null);
        setPhoneId(null);
        setWebId(null);
    }

    private Integer getIdByItem(ISimpleBean item) {
        return item == null ? null : (item.isDeleted() ? null : item.getId());
    }

    private Object getItemById(List items, Integer id) {
        if (id == null || items == null) {
            return null;
        }
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (id.equals(((ISimpleBean) item).getId())) {
                return item;
            }
        }
        return null;
    }

    public void resetDefaultItem(ContactableItem item) {
        if (item == null) {
            return;
        }
        if (item instanceof Address) {
            resetAddress((Address) item);
        } else if (item instanceof Email) {
            resetEmail((Email) item);
        }
        if (item instanceof Phone) {
            resetPhone((Phone) item);
        }
        if (item instanceof Web) {
            resetWeb((Web) item);
        }
    }

    public void setDefaultItem(ContactableItem item) {
        if (item == null) {
            return;
        }
        if (item instanceof Address) {
            setAddress((Address) item);
        } else if (item instanceof Email) {
            setEmail((Email) item);
        }
        if (item instanceof Phone) {
            setPhone((Phone) item);
        }
        if (item instanceof Web) {
            setWeb((Web) item);
        }
    }

    public boolean isDefaultItem(ContactableItem item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Address) {
            return getAddress() == item;
        } else if (item instanceof Email) {
            return getEmail() == item;
        }
        if (item instanceof Phone) {
            return getPhone() == item;
        }
        if (item instanceof Web) {
            return getWeb() == item;
        }
        return false;
    }

    public List getContactListByItem(ContactableItem item) {
        if (item == null) {
            return null;
        }
        if (item instanceof Address) {
            return getAddressList();
        } else if (item instanceof Email) {
            return getEmailList();
        }
        if (item instanceof Phone) {
            return getPhoneList();
        }
        if (item instanceof Web) {
            return getWebList();
        }
        return null;
    }

    public int getContactListSizeByItem(ContactableItem item) {
        List list = getContactListByItem(item);
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public boolean isSimpleContactList(ContactableItem item) {
        if (item == null) {
            return true;
        }
        int size = getContactListSizeByItem(item);
        if (item.getContactableElement() == null && size == 1) {
            return false;
        }
        return size <= 1;
    }

    public void resetAddress(Address item) {
        if (item == null) {
            return;
        }
        if (this.address == item) {
            this.address = null;
            this.addressId = null;
        }
    }

    public void resetPhone(Phone item) {
        if (item == null) {
            return;
        }
        if (this.phone == item) {
            this.phone = null;
            this.phoneId = null;
        }
    }

    public void resetEmail(Email item) {
        if (item == null) {
            return;
        }
        if (this.email == item) {
            this.email = null;
            this.emailId = null;
        }
    }

    public void resetWeb(Web item) {
        if (item == null) {
            return;
        }
        if (this.web == item) {
            this.web = null;
            this.webId = null;
        }
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }
}
