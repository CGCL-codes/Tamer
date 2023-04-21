package org.openimmunizationsoftware.dqa.db.model.received;

import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.received.types.Address;
import org.openimmunizationsoftware.dqa.db.model.received.types.CodedEntity;
import org.openimmunizationsoftware.dqa.db.model.received.types.Name;
import org.openimmunizationsoftware.dqa.db.model.received.types.PhoneNumber;

public class NextOfKin implements Skippable {

    public static final String RELATIONSHIP_BROTHER = "BRO";

    public static final String RELATIONSHIP_CARE_GIVER = "CGV";

    public static final String RELATIONSHIP_FATHER = "FTH";

    public static final String RELATIONSHIP_FOSTER_CHILD = "FCH";

    public static final String RELATIONSHIP_GARNDPARENT = "GRP";

    public static final String RELATIONSHIP_GUARDIAN = "GRD";

    public static final String RELATIONSHIP_MOTHER = "MTH";

    public static final String RELATIONSHIP_OTHER = "OTH";

    public static final String RELATIONSHIP_PARENT = "PAR";

    public static final String RELATIONSHIP_SELF = "SEL";

    public static final String RELATIONSHIP_SIBLING = "SIB";

    public static final String RELATIONSHIP_SISTER = "SIS";

    public static final String RELATIONSHIP_SPOUSE = "SPO";

    public static final String RELATIONSHIP_STEPCHILD = "SCH";

    private Address address = new Address();

    private MessageReceived messageReceived = null;

    private Name name = new Name();

    private long nextOfKinId;

    private PhoneNumber phone = new PhoneNumber();

    private int positionId = 0;

    private long receivedId = 0l;

    private CodedEntity relationship = new CodedEntity(CodeTable.Type.PERSON_RELATIONSHIP);

    private boolean skipped = false;

    public Address getAddress() {
        return address;
    }

    public String getAddressCity() {
        return address.getCity();
    }

    public String getAddressCountryCode() {
        return address.getCountry().getCode();
    }

    public String getAddressCountyParishCode() {
        return address.getCountyParish().getCode();
    }

    public void getAddressState(String addressStreet) {
        address.setStreet(addressStreet);
    }

    public String getAddressStateCode() {
        return address.getState().getCode();
    }

    public String getAddressStreet() {
        return address.getStreet();
    }

    public String getAddressStreet2() {
        return address.getStreet2();
    }

    public String getAddressTypeCode() {
        return address.getType().getCode();
    }

    public String getAddressZip() {
        return address.getZip();
    }

    public MessageReceived getMessageReceived() {
        return messageReceived;
    }

    public Name getName() {
        return name;
    }

    public String getNameFirst() {
        return name.getFirst();
    }

    public String getNameLast() {
        return name.getLast();
    }

    public String getNameMiddle() {
        return name.getMiddle();
    }

    public String getNamePrefix() {
        return name.getPrefix();
    }

    public String getNameSuffix() {
        return name.getSuffix();
    }

    public String getNameTypeCode() {
        return name.getTypeCode();
    }

    public long getNextOfKinId() {
        return nextOfKinId;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public String getPhoneNumber() {
        return phone.getNumber();
    }

    public int getPositionId() {
        return positionId;
    }

    public long getReceivedId() {
        return receivedId;
    }

    public CodedEntity getRelationship() {
        return relationship;
    }

    public String getRelationshipCode() {
        return relationship.getCode();
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setAddressCity(String addressCity) {
        address.setCity(addressCity);
    }

    public void setAddressCountryCode(String addressCountryCode) {
        address.getCountry().setCode(addressCountryCode);
    }

    public void setAddressCountyParishCode(String addressCountyParishCode) {
        address.getCountyParish().setCode(addressCountyParishCode);
    }

    public void setAddressStateCode(String addressStateCode) {
        address.getState().setCode(addressStateCode);
    }

    public void setAddressStreet(String addressStreet) {
        address.setStreet(addressStreet);
    }

    public void setAddressStreet2(String addressStreet2) {
        address.setStreet2(addressStreet2);
    }

    public void setAddressTypeCode(String addressTypeCode) {
        address.getType().setCode(addressTypeCode);
    }

    public void setAddressZip(String addressZip) {
        address.setZip(addressZip);
    }

    public void setMessageReceived(MessageReceived messageReceived) {
        this.messageReceived = messageReceived;
    }

    public void setNameFirst(String nameFirst) {
        name.setFirst(nameFirst);
    }

    public void setNameLast(String nameLast) {
        name.setLast(nameLast);
    }

    public void setNameMiddle(String nameMiddle) {
        name.setMiddle(nameMiddle);
    }

    public void setNamePrefix(String namePrefix) {
        name.setPrefix(namePrefix);
    }

    public void setNameSuffix(String nameSuffix) {
        name.setSuffix(nameSuffix);
    }

    public void setNameTypeCode(String nameTypeCode) {
        name.setTypeCode(nameTypeCode);
    }

    public void setNextOfKinId(long nextOfKinId) {
        this.nextOfKinId = nextOfKinId;
    }

    public void setPhoneNumber(String phoneNumber) {
        phone.setNumber(phoneNumber);
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public void setReceivedId(long receivedId) {
        this.receivedId = receivedId;
    }

    public void setRelationshipCode(String relationshipCode) {
        relationship.setCode(relationshipCode);
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }
}
