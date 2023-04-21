package net.sf.imca.web.backingbeans;

import java.util.Currency;
import java.util.Date;
import javax.faces.model.SelectItem;
import net.sf.imca.model.AssociationBO;
import net.sf.imca.model.EventBO;
import net.sf.imca.model.MembershipTypeBO;
import net.sf.imca.model.NewsItemBO;
import net.sf.imca.model.entities.EventEntity;
import net.sf.imca.model.entities.MembershipEntity;
import net.sf.imca.model.entities.NewsItemEntity;
import net.sf.imca.services.CommitteeService;

/**
 * Backing bean for Committee member administration of Membership.
 *
 * @author dougculnane
 */
public class CommitteeBean {

    /**
     * Reference date.
     */
    private Date refDate = new Date();

    private EventBO event = null;

    private NewsItemBO newsItem = null;

    /**
     * Start date.
     */
    private Date startDate = new Date();

    private String email = "";

    /**
     * End date.
     */
    private Date endDate = new Date();

    /**
     * Membership type text.
     */
    private String membershipType = "";

    /**
     * Id for the membership type entity.
     */
    private long membershipTypeId;

    /**
     * User feedback message.
     */
    private String userFeedbackMessage = "";

    private long associationId;

    private MembershipEntity[] memberList;

    private String currencyCode;

    private String paymentExplanation;

    private String url;

    private String lastName;

    private String firstName;

    private String countryCode;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    MembershipTableModel dataModel = null;

    private double fee = 0;

    CommitteeService service = new CommitteeService();

    public String actionListMembership() {
        memberList = service.getMembershipList(Utils.getWebUser().getPerson());
        return Utils.ACTION_SUCCESS;
    }

    public String actionSelectAssociation() {
        AssociationBO association = service.getAssociation(this.associationId);
        this.paymentExplanation = association.getEntity().getPaymentExplanation();
        this.url = association.getEntity().getUrl();
        return Utils.ACTION_SUCCESS;
    }

    public String actionSelectMembershipType() {
        MembershipTypeBO membershipType = service.getMembershipType(this.membershipTypeId);
        this.membershipType = membershipType.getEntity().getName();
        this.startDate = membershipType.getEntity().getValidFrom();
        this.endDate = membershipType.getEntity().getValidTo();
        this.fee = membershipType.getEntity().getCurrentFee().getAmount();
        this.currencyCode = membershipType.getEntity().getCurrentFee().getCurrency();
        this.userFeedbackMessage = "";
        return Utils.ACTION_SUCCESS;
    }

    public String actionAddMembershipType() {
        service.saveNewMembershipType(associationId, membershipType, currencyCode, fee, startDate, endDate);
        userFeedbackMessage = "Data Saved.";
        return Utils.ACTION_SUCCESS;
    }

    public String actionSaveMembershipType() {
        service.saveMembershipType(membershipTypeId, membershipType, currencyCode, fee, startDate, endDate);
        userFeedbackMessage = "Data Saved.";
        return Utils.ACTION_SUCCESS;
    }

    public String actionConfirmPaidMembershipRequests() {
        dataModel.actionConfirmPaidMembers();
        getMembershipRequestsList();
        userFeedbackMessage = Utils.getMessage("ConfirmPaidMarkedMembershipRequests");
        return Utils.ACTION_SUCCESS;
    }

    public String actionDeleteMembershipRequests() {
        dataModel.actionDeleteMembers();
        getMembershipRequestsList();
        userFeedbackMessage = Utils.getMessage("DeletedMarkedMembershipRequests");
        return Utils.ACTION_SUCCESS;
    }

    public String actionDeleteMembershipType() {
        if (getMembershipTypeCanBeDeleted()) {
            service.deleteMembershipTypeEntity(this.membershipTypeId);
            membershipTypeId = -1;
        }
        return Utils.ACTION_SUCCESS;
    }

    public boolean getMembershipTypeCanBeDeleted() {
        return !service.getHasLinkedMemberships(this.membershipTypeId);
    }

    public String actionSaveAssociationData() {
        service.saveAssociationData(this.associationId, paymentExplanation, url);
        return actionSelectAssociation();
    }

    public String actionAddPerson() {
        this.userFeedbackMessage = "";
        if (service.addPerson(this.email, this.firstName, this.lastName, this.countryCode)) {
            userFeedbackMessage = Utils.getMessage("addedPerson") + " " + this.firstName + " " + this.lastName;
            this.countryCode = Utils.getWebUser().getPerson().getCountry();
            this.lastName = "";
            this.firstName = "";
            this.email = "";
        } else {
            userFeedbackMessage = Utils.getMessage("failedtoAddPerson");
        }
        return Utils.ACTION_SUCCESS;
    }

    public String actionAddEvent() {
        this.userFeedbackMessage = service.addEvent(event);
        this.event = null;
        return Utils.ACTION_SUCCESS;
    }

    public String actionAddNews() {
        this.userFeedbackMessage = service.addNewsItem(newsItem);
        newsItem = null;
        return Utils.ACTION_SUCCESS;
    }

    public String actionAddMember() {
        this.userFeedbackMessage = "";
        if (service.addMember(this.membershipTypeId, this.email)) {
            userFeedbackMessage = Utils.getMessage("addedMember") + " " + email;
        } else {
            userFeedbackMessage = Utils.getMessage("failedtoAddMember") + " " + email;
        }
        return Utils.ACTION_SUCCESS;
    }

    public String getUserFeedbackMessage() {
        return userFeedbackMessage;
    }

    public void setUserFeedbackMessage(String userFeedbackMessage) {
        this.userFeedbackMessage = userFeedbackMessage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public MembershipEntity[] getMemberList() {
        return memberList;
    }

    public void setMemberList(MembershipEntity[] memberList) {
        this.memberList = memberList;
    }

    public SelectItem[] getOfficalImcaAssociationItems() {
        AssociationBO[] associations = service.getOficialIMCACommitteeMemberships(Utils.getWebUser().getPerson());
        SelectItem[] selectItems = new SelectItem[associations.length];
        for (int i = 0; i < selectItems.length; i++) {
            selectItems[i] = new SelectItem(associations[i].getEntity().getId(), associations[i].getEntity().toString());
        }
        return selectItems;
    }

    public SelectItem[] getMembershipTypeItems() {
        MembershipTypeBO[] membershipTypes = service.getEditableMembershipTypes(Utils.getWebUser().getPerson());
        SelectItem[] selectItems = new SelectItem[membershipTypes.length];
        for (int i = 0; i < selectItems.length; i++) {
            selectItems[i] = new SelectItem(membershipTypes[i].getEntity().getId(), membershipTypes[i].getEntity().toString());
        }
        return selectItems;
    }

    public String getAssociationName() {
        AssociationBO association = service.getAssociation(this.associationId);
        return association.getName();
    }

    public SelectItem[] getAssociationItems() {
        AssociationBO[] associations = service.getCommitteeMemberships(Utils.getWebUser().getPerson());
        SelectItem[] selectItems = new SelectItem[associations.length];
        for (int i = 0; i < selectItems.length; i++) {
            selectItems[i] = new SelectItem(associations[i].getEntity().getId(), associations[i].getEntity().toString());
        }
        return selectItems;
    }

    public long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(long associationId) {
        this.associationId = associationId;
    }

    public String getCurrencyCode() {
        if (currencyCode == null) {
            try {
                currencyCode = Currency.getInstance(Utils.getWebAppLocale()).getCurrencyCode();
            } catch (IllegalArgumentException ilgex) {
                currencyCode = "";
            }
        }
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public MembershipTableModel getMembershipRequestsList() {
        dataModel = new MembershipTableModel();
        dataModel.setMembershipEntities(service.getMembershipRequestList(Utils.getWebUser().getPerson()));
        return dataModel;
    }

    public MembershipTableModel getMembershipList() {
        dataModel = new MembershipTableModel();
        dataModel.setMembershipEntities(service.getMembershipList(Utils.getWebUser().getPerson(), refDate));
        return dataModel;
    }

    public Date getRefDate() {
        return refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }

    public String getPaymentExplanation() {
        return paymentExplanation;
    }

    public void setPaymentExplanation(String paymentExplanation) {
        this.paymentExplanation = paymentExplanation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getMembershipTypeId() {
        return membershipTypeId;
    }

    public void setMembershipTypeId(long membershipTypeId) {
        this.membershipTypeId = membershipTypeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEvent(EventBO event) {
        this.event = event;
    }

    public EventBO getEvent() {
        if (event == null) {
            event = new EventBO(new EventEntity());
        }
        return event;
    }

    public void setNewsItem(NewsItemBO newsItem) {
        this.newsItem = newsItem;
    }

    public NewsItemBO getNewsItem() {
        if (newsItem == null) {
            newsItem = new NewsItemBO(new NewsItemEntity());
            newsItem.getEntity().setDate(new Date());
            newsItem.getEntity().setCountryCode(Utils.getWebUser().getPerson().getEntity().getAddress().getCountryCode());
        }
        return newsItem;
    }
}
