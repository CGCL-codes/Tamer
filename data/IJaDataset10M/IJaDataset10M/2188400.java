package com.kwoksys.action.contacts;

import com.kwoksys.action.base.BaseTemplate;
import com.kwoksys.action.common.template.DetailTableTemplate;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.util.HtmlUtils;

/**
 * CompanyContactTemplate
 */
public class CompanyContactSpecTemplate extends BaseTemplate {

    private Contact contact;

    private DetailTableTemplate detailTableTemplate = new DetailTableTemplate();

    public CompanyContactSpecTemplate(Contact contact) {
        super(CompanyContactSpecTemplate.class);
        this.contact = contact;
        addTemplate(detailTableTemplate);
    }

    public void applyTemplate() throws Exception {
        AccessUser accessUser = requestContext.getUser();
        detailTableTemplate.setNumColumns(2);
        DetailTableTemplate.Td td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_label");
        td.setValue(HtmlUtils.encode(contact.getTitle()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.company_name");
        String companyName = HtmlUtils.encode(contact.getCompanyName());
        if (Access.hasPermission(accessUser, AppPaths.CONTACTS_COMPANY_DETAIL)) {
            td.setValue("<a href=\"" + AppPaths.ROOT + AppPaths.CONTACTS_COMPANY_DETAIL + "?companyId=" + contact.getCompanyId() + "\">" + companyName + "</a>");
        } else {
            td.setValue(companyName);
        }
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_phone");
        td.setValue(HtmlUtils.encode(contact.getPhoneWork()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_fax");
        td.setValue(HtmlUtils.encode(contact.getFax()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_email");
        td.setValue(HtmlUtils.formatMailtoLink(contact.getEmailPrimary()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_main_website");
        td.setValue(HtmlUtils.formatExternalLink(contact.getHomepageUrl()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_street_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressStreetPrimary()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_city_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressCityPrimary()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_state_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressStatePrimary()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_zipcode_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressZipcodePrimary()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_address_country_primary");
        td.setValue(HtmlUtils.encode(contact.getAddressCountryPrimary()));
        detailTableTemplate.addTd(td);
        td = detailTableTemplate.new Td();
        td.setHeaderKey("common.column.contact_description");
        td.setValue(HtmlUtils.formatMultiLineDisplay(contact.getDescription()));
        detailTableTemplate.addTd(td);
    }
}
