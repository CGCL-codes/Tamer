package com.centraview.mail.webimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.mail.MailImport;
import com.centraview.mail.MailImportHome;
import com.centraview.settings.Settings;
import com.centraview.sync.SyncUtils;
import com.centraview.syncfacade.SyncFacadeHome;

public class SaveContactHandler extends org.apache.struts.action.Action {

    private static Logger logger = Logger.getLogger(SaveContactHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception {
        final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        HttpSession session = request.getSession();
        UserObject userObject = (UserObject) session.getAttribute("userobject");
        int individualID = userObject.getIndividualID();
        ActionErrors allErrors = new ActionErrors();
        String forward = "showContactDetails";
        DynaActionForm emailForm = (DynaActionForm) form;
        int newIndividualID = -1;
        try {
            MailImportHome importHome = (MailImportHome) CVUtility.getHomeObject("com.centraview.mail.MailImportHome", "MailImport");
            MailImport mailRemote = (MailImport) importHome.create();
            mailRemote.setDataSource(dataSource);
            ArrayList validFields = mailRemote.getValidFields("Contact");
            HashMap contactForm = new HashMap();
            if (validFields != null && validFields.size() > 0) {
                Iterator iter = validFields.iterator();
                while (iter.hasNext()) {
                    HashMap validFieldMap = (HashMap) iter.next();
                    String name = (String) validFieldMap.get("name");
                    String fieldName = (String) validFieldMap.get("fieldName");
                    String value = request.getParameter(fieldName);
                    if (value == null) {
                        value = "";
                    }
                    contactForm.put(fieldName, value);
                }
            }
            String firstName = (String) contactForm.get("firstName");
            String lastName = (String) contactForm.get("lastName");
            if (firstName == null || firstName.equals("")) {
                if (lastName == null || lastName.equals("")) {
                    forward = "errorOccurred";
                }
            }
            String companyName = (String) contactForm.get("entityName");
            if (companyName == null || companyName.equals("")) {
                companyName = firstName + " " + lastName;
            }
            SyncUtils syncUtils = new SyncUtils();
            IndividualVO individualVO = new IndividualVO();
            individualVO.setFirstName(firstName);
            individualVO.setMiddleName((String) contactForm.get("middleInitial"));
            individualVO.setLastName(lastName);
            individualVO.setTitle((String) contactForm.get("title"));
            individualVO.setContactType(2);
            individualVO.setSourceName((String) contactForm.get("source"));
            individualVO.setExternalID((String) contactForm.get("ID2"));
            String street1 = (String) contactForm.get("street1");
            String street2 = (String) contactForm.get("street2");
            String city = (String) contactForm.get("city");
            String state = (String) contactForm.get("state");
            String zipCode = (String) contactForm.get("zip");
            String country = (String) contactForm.get("country");
            AddressVO primaryAddress = new AddressVO();
            if ((street1 != null && street1.length() > 0) || (street2 != null && street2.length() > 0) || (city != null && city.length() > 0) || (state != null && state.length() > 0) || (zipCode != null && zipCode.length() > 0) || (country != null && country.length() > 0)) {
                primaryAddress.setIsPrimary("YES");
                primaryAddress.setStreet1(street1);
                primaryAddress.setStreet2(street2);
                primaryAddress.setCity(city);
                primaryAddress.setStateName(state);
                primaryAddress.setZip(zipCode);
                primaryAddress.setCountryName(country);
                individualVO.setPrimaryAddress(primaryAddress);
            }
            String email = (String) contactForm.get("email");
            if (email != null && !email.equals("")) {
                MethodOfContactVO emailVO = new MethodOfContactVO();
                emailVO.setContent(email);
                emailVO.setMocType(Constants.MOC_EMAIL);
                emailVO.setIsPrimary("YES");
                individualVO.setMOC(emailVO);
            }
            String workPhone = (String) contactForm.get("workPhone");
            String workPhoneExt = (String) contactForm.get("workPhoneExt");
            if (workPhone != null && !workPhone.equals("")) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(workPhone, workPhoneExt, "Work", Constants.MOC_WORK));
            }
            String homePhone = (String) contactForm.get("homePhone");
            String homePhoneExt = (String) contactForm.get("homePhoneExt");
            if (homePhone != null && !homePhone.equals("")) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(homePhone, homePhoneExt, "Home", Constants.MOC_HOME));
            }
            String faxPhone = (String) contactForm.get("faxPhone");
            String faxPhoneExt = (String) contactForm.get("faxPhoneExt");
            if (faxPhone != null && (!faxPhone.equals(""))) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(faxPhone, faxPhoneExt, "Fax", Constants.MOC_FAX));
            }
            String otherPhone = (String) contactForm.get("otherPhone");
            String otherPhoneExt = (String) contactForm.get("otherPhoneExt");
            if (otherPhone != null && (!otherPhone.equals(""))) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(otherPhone, otherPhoneExt, "Other", Constants.MOC_OTHER));
            }
            String mainPhone = (String) contactForm.get("mainPhone");
            String mainPhoneExt = (String) contactForm.get("mainPhoneExt");
            if (mainPhone != null && (!mainPhone.equals(""))) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(mainPhone, mainPhoneExt, "Main", Constants.MOC_MAIN));
            }
            String pagerPhone = (String) contactForm.get("pagerPhone");
            String pagerPhoneExt = (String) contactForm.get("pagerPhoneExt");
            if (pagerPhone != null && (!pagerPhone.equals(""))) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(pagerPhone, pagerPhoneExt, "Pager", Constants.MOC_PAGER));
            }
            String mobilePhone = (String) contactForm.get("mobilePhone");
            String mobilePhoneExt = (String) contactForm.get("mobilePhoneExt");
            if (mobilePhone != null && (!mobilePhone.equals(""))) {
                individualVO.setMOC(syncUtils.createNewPhoneMoc(mobilePhone, mobilePhoneExt, "Mobile", Constants.MOC_MOBILE));
            }
            ContactFacadeHome cfh = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
            ContactFacade cfremote = (ContactFacade) cfh.create();
            cfremote.setDataSource(dataSource);
            SyncFacadeHome syncHome = (SyncFacadeHome) CVUtility.getHomeObject("com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
            com.centraview.syncfacade.SyncFacade sfremote = (com.centraview.syncfacade.SyncFacade) syncHome.create();
            sfremote.setDataSource(dataSource);
            int finalEntityID = 0;
            boolean createNewEntity = false;
            if (companyName != null && (!companyName.equals(""))) {
                finalEntityID = sfremote.findCompanyNameMatch(companyName, individualID);
                if (finalEntityID == 0) {
                    createNewEntity = true;
                }
            }
            String primaryContact = "Yes";
            if (createNewEntity) {
                EntityVO newEntity = new EntityVO();
                newEntity.setContactType(1);
                newEntity.setName(companyName);
                newEntity.setSourceName((String) contactForm.get("entitySource"));
                newEntity.setExternalID((String) contactForm.get("entityID2"));
                String eStreet1 = (String) contactForm.get("entityStreet1");
                String eStreet2 = (String) contactForm.get("entityStreet2");
                String eCity = (String) contactForm.get("entityCity");
                String eState = (String) contactForm.get("entityState");
                String eZipCode = (String) contactForm.get("entityZip");
                String eCountry = (String) contactForm.get("entityCountry");
                if ((eStreet1 != null && eStreet1.length() > 0) || (eStreet2 != null && eStreet2.length() > 0) || (eCity != null && eCity.length() > 0) || (eState != null && eState.length() > 0) || (eZipCode != null && eZipCode.length() > 0) || (eCountry != null && eCountry.length() > 0)) {
                    AddressVO ePrimaryAddress = new AddressVO();
                    ePrimaryAddress.setIsPrimary("YES");
                    ePrimaryAddress.setStreet1(eStreet1);
                    ePrimaryAddress.setStreet2(eStreet2);
                    ePrimaryAddress.setCity(eCity);
                    ePrimaryAddress.setStateName(eState);
                    ePrimaryAddress.setZip(eZipCode);
                    ePrimaryAddress.setCountryName(eCountry);
                    newEntity.setPrimaryAddress(ePrimaryAddress);
                } else {
                    newEntity.setPrimaryAddress(primaryAddress);
                }
                String eEmail = (String) contactForm.get("entityEmail");
                if (eEmail != null && !eEmail.equals("")) {
                    MethodOfContactVO eEmailVO = new MethodOfContactVO();
                    eEmailVO.setContent(eEmail);
                    eEmailVO.setMocType(1);
                    eEmailVO.setIsPrimary("YES");
                    newEntity.setMOC(eEmailVO);
                }
                String eWorkPhone = (String) contactForm.get("entityWorkPhone");
                if (eWorkPhone != null && !eWorkPhone.equals("")) {
                    String eWorkPhoneExt = (String) contactForm.get("entityWorkPhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(eWorkPhone, eWorkPhoneExt, "Work", Constants.MOC_WORK));
                }
                String eHomePhone = (String) contactForm.get("entityHomePhone");
                if (eHomePhone != null && !eHomePhone.equals("")) {
                    String eHomePhoneExt = (String) contactForm.get("entityHomePhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(eHomePhone, eHomePhoneExt, "Home", Constants.MOC_HOME));
                }
                String eFaxPhone = (String) contactForm.get("entityFaxPhone");
                if (eFaxPhone != null && !eFaxPhone.equals("")) {
                    String eFaxPhoneExt = (String) contactForm.get("entityFaxPhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(eFaxPhone, eFaxPhoneExt, "Fax", Constants.MOC_FAX));
                }
                String eOtherPhone = (String) contactForm.get("entityOtherPhone");
                if (eOtherPhone != null && !eOtherPhone.equals("")) {
                    String eOtherPhoneExt = (String) contactForm.get("entityOtherPhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(eOtherPhone, eOtherPhoneExt, "Other", Constants.MOC_OTHER));
                }
                String eMainPhone = (String) contactForm.get("entityMainPhone");
                if (eMainPhone != null && !eMainPhone.equals("")) {
                    String eMainPhoneExt = (String) contactForm.get("entityMainPhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(eMainPhone, eMainPhoneExt, "Main", Constants.MOC_MAIN));
                }
                String ePagerPhone = (String) contactForm.get("entityPagerPhone");
                if (ePagerPhone != null && !ePagerPhone.equals("")) {
                    String ePagerPhoneExt = (String) contactForm.get("entityPagerPhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(ePagerPhone, ePagerPhoneExt, "Pager", Constants.MOC_PAGER));
                }
                String eMobilePhone = (String) contactForm.get("entityMobilePhone");
                if (eMobilePhone != null && !eMobilePhone.equals("")) {
                    String eMobilePhoneExt = (String) contactForm.get("entityMobilePhoneExt");
                    newEntity.setMOC(syncUtils.createNewPhoneMoc(eMobilePhone, eMobilePhoneExt, "Mobile", Constants.MOC_MOBILE));
                }
                this.addCustomFields(newEntity, mailRemote.getValidFields("Entity"), contactForm);
                int newEntityID = cfremote.createEntity(newEntity, individualID);
                finalEntityID = newEntityID;
            } else {
                primaryContact = "No";
            }
            this.addCustomFields(individualVO, mailRemote.getValidFields("Individual"), contactForm);
            individualVO.setEntityID(finalEntityID);
            individualVO.setIsPrimaryContact(primaryContact);
            newIndividualID = cfremote.createIndividual(individualVO, individualID);
            if (newIndividualID > 0) {
                ListGenerator lg = ListGenerator.getListGenerator(dataSource);
                lg.makeListDirty("Individual");
                lg.makeListDirty("Entity");
            } else {
                forward = "errorOccurred";
            }
        } catch (Exception e) {
            logger.error("[Exception] SaveContactHandler.Execute Handler ", e);
            e.printStackTrace();
        }
        return (new ActionForward(mapping.findForward(forward).getPath() + newIndividualID, true));
    }

    private void addCustomFields(ContactVO contactVO, ArrayList validCustomFields, HashMap contactForm) {
        if (validCustomFields != null && validCustomFields.size() > 0) {
            Iterator cfIter = validCustomFields.iterator();
            while (cfIter.hasNext()) {
                HashMap fieldInfo = (HashMap) cfIter.next();
                Number fieldID = (Number) fieldInfo.get("fieldID");
                String fieldName = (String) fieldInfo.get("fieldName");
                String formValue = (String) contactForm.get(fieldName);
                if (formValue != null && formValue.length() > 0) {
                    CustomFieldVO customFieldVO = new CustomFieldVO();
                    customFieldVO.setFieldID(fieldID.intValue());
                    customFieldVO.setValue(formValue);
                    contactVO.setCustomField(customFieldVO);
                }
            }
        }
    }
}
