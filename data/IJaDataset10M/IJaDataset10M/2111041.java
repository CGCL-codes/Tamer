package cz.cvut.phone.core.data.dao;

import javax.ejb.Local;

/**
 *
 * @author Frantisek Hradil
 */
@Local
public interface BasicEntityDAOLocal {

    public java.lang.Object find(java.lang.Class entity, java.lang.Object id) throws java.lang.Exception;

    public cz.cvut.phone.core.data.dao.SettingsEntityDAOLocal getSettingsEntityDAOBean();

    public cz.cvut.phone.core.data.dao.BillEntityDAOLocal getBillEntityDAOBean();

    public cz.cvut.phone.core.data.dao.PersonPhoneNumberMapEntityDAOLocal getPersonPhoneNumberMapEntityDAOBean();

    public cz.cvut.phone.core.data.dao.ItemEntityDAOLocal getItemEntityDAOBean();

    public cz.cvut.phone.core.data.dao.BankPaymentEntityDAOLocal getBankPaymentEntityDAOBean();

    public void setBankPaymentEntityDAOBean(cz.cvut.phone.core.data.dao.BankPaymentEntityDAOLocal bankpayment);

    public void setBankEntityDAOBean(cz.cvut.phone.core.data.dao.BankEntityDAOLocal bank);

    public cz.cvut.phone.core.data.dao.PaymentEntityDAOLocal getPaymentEntityDAOBean();

    public void setPaymentEntityDAOBean(cz.cvut.phone.core.data.dao.PaymentEntityDAOLocal payment);

    public java.lang.Object merge(java.lang.Object object) throws java.lang.Exception;

    public void persist(java.lang.Object object) throws java.lang.Exception;

    public cz.cvut.phone.core.data.dao.PhoneNumberEntityDAOLocal getPhoneNumberEntityDAOBean();

    public cz.cvut.phone.core.data.dao.PersonEntityDAOLocal getPersonEntityDAOBean();

    public cz.cvut.phone.core.data.dao.PrivateCallEntityDAOLocal getPrivateCallEntityDAOBean();

    public cz.cvut.phone.core.data.dao.DepartmentEntityDAOLocal getDepartmentEntityDAOBean();

    public cz.cvut.phone.core.data.dao.RoleEntityDAOLocal getRoleEntityDAOBean();

    public cz.cvut.phone.core.data.dao.OperationEntityDAOLocal getOperationEntityDAOBean();

    public cz.cvut.phone.core.data.dao.RoleOperationMapEntityDAOLocal getRoleOperationMapEntityDAOBean();

    public void remove(java.lang.Object object) throws java.lang.Exception;

    public cz.cvut.phone.core.data.dao.PersonRoleMapEntityDAOLocal getPersonRoleMapEntityDAOBean();

    public cz.cvut.phone.core.data.dao.PersonSettingEntityDAOLocal getPersonSettingEntityDAOBean();

    public cz.cvut.phone.core.data.dao.BankEntityDAOLocal getBankEntityDAOBean();

    public cz.cvut.phone.core.data.dao.SuperSettingsEntityDAOLocal getSuperSettingsEntityDAOBean();

    public void setSuperSettingsEntityDAOBean(cz.cvut.phone.core.data.dao.SuperSettingsEntityDAOLocal supersetting);

    public cz.cvut.phone.core.data.dao.CompanyEntityDAOLocal getCompanyEntityDAOBean();

    public cz.cvut.phone.core.data.dao.LanguageEntityDAOLocal getLanguageEntityDAOBean();

    public cz.cvut.phone.core.data.dao.SuperAdminEntityDAOLocal getSuperAdminEntityDAOBean();

    public cz.cvut.phone.core.data.dao.NewEventEntityDAOLocal getNewEventEntityDAOBean();

    public void setBillEntityDAOBean(BillEntityDAOLocal billEntityDAOBean);

    public void setPersonEntityDAOBean(PersonEntityDAOLocal personEntityDAOBean);
}
