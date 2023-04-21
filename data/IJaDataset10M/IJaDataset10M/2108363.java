package net.sf.iqser.plugin.msx.operations.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import net.sf.iqser.plugin.msx.MSExchangeContentProvider;
import net.sf.iqser.plugin.msx.api.IExchangeApi;
import net.sf.iqser.plugin.msx.api.model.MsxContact;
import net.sf.iqser.plugin.msx.operations.AbstractJob;
import net.sf.iqser.plugin.msx.operations.JobUtils;
import net.sf.iqser.plugin.msx.utils.URLUtils;
import com.iqser.core.exception.IQserRuntimeException;
import com.iqser.core.model.Content;

/**
 * Exchange job operations for Calendar folder.
 *
 * @author cristian.barbus
 */
public class ContactsJob extends AbstractJob {

    private static final String TYPE = "CONTACT";

    /**
	 * Constructor.
	 *
	 * @param msecp Exchange content provider
	 * @param exchangeApi Exchange API
	 */
    public ContactsJob(MSExchangeContentProvider msecp, IExchangeApi exchangeApi) {
        super(msecp, exchangeApi);
    }

    private Content contact2content(MsxContact contact) {
        Content content = new Content();
        content.setProvider(getMsecp().getId());
        content.setType(TYPE);
        String url = URLUtils.createURL(TYPE, contact.getId());
        content.setContentUrl(url);
        if (contact.getMessageBody() != null) {
            content.setFulltext(contact.getMessageBody());
        }
        if (contact.getLastModifiedTime() != null) {
            content.setModificationDate(contact.getLastModifiedTime().getTime());
        } else {
            content.setModificationDate(new Date().getTime());
        }
        addItemAttributes(content, contact);
        addAttribute(content, "EMAIL_ADDRESSES", contact.getEmailAddresses(), true);
        addAttribute(content, "FIRST_NAME", contact.getGivenName(), true);
        addAttribute(content, "SURNAME", contact.getSurname(), true);
        addAttribute(content, "MIDDLE_NAME", contact.getMiddleName(), true);
        addAttribute(content, "NICK_NAME", contact.getNickName(), true);
        addAttribute(content, "COMPANY_NAME", contact.getCompanyName(), true);
        addAttribute(content, "JOB_TITLE", contact.getJobTitle(), true);
        addAttribute(content, "DEPARTMENT", contact.getDepartment(), true);
        addAttribute(content, "HOME_PAGE", contact.getBusinessHomePage(), true);
        addAttribute(content, "PHYSICAL_ADDRESSES", contact.getPhysicalAddresses(), true);
        addAttribute(content, "PHONE_NUMBERS", contact.getPhoneNumbers(), true);
        addAttribute(content, "ASSISTANT_NAME", contact.getAssistantName(), false);
        addAttribute(content, "BIRTHDAY", contact.getBirthday(), false);
        addAttribute(content, "COMPANIES", contact.getCompanies(), false);
        addAttribute(content, "IM_ADDRESSES", contact.getImAddresses(), false);
        addAttribute(content, "MANAGER", contact.getManager(), false);
        addAttribute(content, "OFFICE_LOCATION", contact.getOfficeLocation(), false);
        addAttribute(content, "PROFESSION", contact.getProfession(), false);
        return content;
    }

    /**
	 * Retrieve content from repository, for a given URI.
	 *
	 * @param uri URI of the corresponding content
	 * @return Content object from repository
	 */
    public Content getContent(String uri) {
        try {
            String id = URLUtils.getIdFromURL(uri);
            MsxContact contact = getExchangeApi().fetchContactById(id);
            if (contact == null) return null;
            return contact2content(contact);
        } catch (Exception e) {
            throw new IQserRuntimeException(e);
        }
    }

    private MsxContact content2Contact(Content content) {
        MsxContact contact = new MsxContact();
        contact.setSubject(JobUtils.toStringValue(content.getAttributeByName("SUBJECT")));
        contact.setCategories(JobUtils.toListValue(content.getAttributeByName("CATEGORIES")));
        contact.setImportance(JobUtils.toStringValue(content.getAttributeByName("IMPORTANCE")));
        contact.setMessageBody(JobUtils.toStringValue(content.getAttributeByName("MESSAGE_BODY")));
        contact.setEmailAddresses(JobUtils.toListValue(content.getAttributeByName("EMAIL_ADDRESSES")));
        contact.setGivenName(JobUtils.toStringValue(content.getAttributeByName("FIRST_NAME")));
        contact.setSurname(JobUtils.toStringValue(content.getAttributeByName("SURNAME")));
        contact.setNickName(JobUtils.toStringValue(content.getAttributeByName("NICK_NAME")));
        contact.setCompanyName(JobUtils.toStringValue(content.getAttributeByName("COMPANY_NAME")));
        contact.setJobTitle(JobUtils.toStringValue(content.getAttributeByName("JOB_TITLE")));
        contact.setDepartment(JobUtils.toStringValue(content.getAttributeByName("DEPARTMENT")));
        contact.setMiddleName(JobUtils.toStringValue(content.getAttributeByName("MIDDLE_NAME")));
        contact.setAssistantName(JobUtils.toStringValue(content.getAttributeByName("ASSISTANT_NAME")));
        contact.setBirthday(JobUtils.toDateValue(content.getAttributeByName("BIRTHDAY")));
        contact.setBusinessHomePage(JobUtils.toStringValue(content.getAttributeByName("HOME_PAGE")));
        contact.setCompanies(JobUtils.toListValue(content.getAttributeByName("COMPANIES")));
        contact.setImAddresses(JobUtils.toListValue(content.getAttributeByName("IM_ADDRESSES")));
        contact.setManager(JobUtils.toStringValue(content.getAttributeByName("MANAGER")));
        contact.setOfficeLocation(JobUtils.toStringValue(content.getAttributeByName("OFFICE_LOCATION")));
        contact.setPhoneNumbers(JobUtils.toListValue(content.getAttributeByName("PHONE_NUMBERS")));
        contact.setPhysicalAddresses(JobUtils.toListValue(content.getAttributeByName("PHYSICAL_ADDRESSES")));
        contact.setProfession(JobUtils.toStringValue(content.getAttributeByName("PROFESSION")));
        return contact;
    }

    /**
	 * Save content object to repository.
	 *
	 * @param content Content object
	 * @throws Exception the exception
	 */
    protected void performSaveOperation(Content content) throws Exception {
        MsxContact contact = content2Contact(content);
        String id = getExchangeApi().saveContact(contact);
        String url = URLUtils.createURL(TYPE, id);
        content.setContentUrl(url);
        getMsecp().addContentForJob(content);
    }

    /**
	 * Fetch list of id's from data source.
	 * @param filter map of filters.
	 * @return list of string id's from server.
	 * @throws Exception if operation fails.
	 */
    @Override
    protected List<String> fetchIds(Map<String, Object> filter) throws Exception {
        return getExchangeApi().fetchIds(filter, WellKnownFolderName.Contacts);
    }

    /**
	 * Returns the type of the content.
	 * @return object type .
	 */
    @Override
    protected String getType() {
        return TYPE;
    }
}
