package samples.hibernate;

import java.util.List;
import java.util.Date;
import assays.com.Util;

public final class Sample extends Persistent {

    private Date collectionDate;

    private Person person;

    private SampleSubType subType;

    private String typeMemo;

    private ProcessingProtocol processingMetaData1;

    private String processingMetaData2;

    private String processingMetaData3;

    private List aliquots;

    private StorageCondition storageCondition;

    private SampleLocation currentLocation;

    private TransportCondition transportCondition;

    private ReceptionState receptionState;

    private Partner plannedUser;

    public Date getCollectionDate() {
        return collectionDate;
    }

    public Person getPerson() {
        return person;
    }

    public SampleSubType getSubType() {
        return subType;
    }

    public String getTypeMemo() {
        return typeMemo;
    }

    public ProcessingProtocol getProcessingMetaData1() {
        return processingMetaData1;
    }

    public String getProcessingMetaData2() {
        return processingMetaData2;
    }

    public String getProcessingMetaData3() {
        return processingMetaData3;
    }

    public SampleLocation getCurrentLocation() {
        return currentLocation;
    }

    public StorageCondition getStorageCondition() {
        return storageCondition;
    }

    public TransportCondition getTransportCondition() {
        return transportCondition;
    }

    public ReceptionState getReceptionState() {
        return receptionState;
    }

    public Partner getPlannedUser() {
        return plannedUser;
    }

    public List getAliquots() {
        return aliquots;
    }

    public String[] getValueArray(byte[] indexArray, int viewSize) {
        String[] valueArray = super.getValueArray(indexArray, viewSize);
        if (indexArray[6] > 0) valueArray[indexArray[6] - 1] = Util.truncateDate(collectionDate);
        if (indexArray[7] > 0) valueArray[indexArray[7] - 1] = subType.getSampleType().getVisibleName();
        if (indexArray[8] > 0) valueArray[indexArray[8] - 1] = subType.getVisibleName();
        if (indexArray[9] > 0) valueArray[indexArray[9] - 1] = typeMemo;
        if (indexArray[10] > 0) valueArray[indexArray[10] - 1] = currentLocation.getVisibleName();
        if (indexArray[11] > 0) valueArray[indexArray[11] - 1] = (plannedUser != null) ? plannedUser.getVisibleName() : "&nbsp;";
        if (indexArray[12] > 0) valueArray[indexArray[12] - 1] = processingMetaData1.getVisibleName();
        if (indexArray[13] > 0) valueArray[indexArray[13] - 1] = Util.truncateString(processingMetaData2);
        if (indexArray[14] > 0) valueArray[indexArray[14] - 1] = Util.truncateString(processingMetaData3);
        if (indexArray[15] > 0) valueArray[indexArray[15] - 1] = storageCondition.getVisibleName();
        if (indexArray[16] > 0) valueArray[indexArray[16] - 1] = transportCondition.getVisibleName();
        if (indexArray[17] > 0) valueArray[indexArray[17] - 1] = receptionState.getVisibleName();
        if (indexArray[18] > 0) valueArray[indexArray[18] - 1] = person.getVisibleName();
        return valueArray;
    }

    public String getPropertyValue(String propertyName) {
        return (propertyName.equals("collectionDate")) ? Util.truncateDate(collectionDate) : (propertyName.equals("type")) ? subType.getSampleType().getVisibleName() : (propertyName.equals("subType")) ? subType.getVisibleName() : (propertyName.equals("typeMemo")) ? typeMemo : (propertyName.equals("currentLocation")) ? currentLocation.getVisibleName() : (propertyName.equals("plannedUser")) ? (plannedUser != null) ? plannedUser.getVisibleName() : "" : (propertyName.equals("processingMetaData1")) ? processingMetaData1.getVisibleName() : (propertyName.equals("processingMetaData2")) ? processingMetaData2 : (propertyName.equals("processingMetaData3")) ? processingMetaData3 : (propertyName.equals("storageCondition")) ? storageCondition.getVisibleName() : (propertyName.equals("transportCondition")) ? transportCondition.getVisibleName() : (propertyName.equals("receptionState")) ? receptionState.getVisibleName() : (propertyName.equals("person")) ? person.getVisibleName() : super.getPropertyValue(propertyName);
    }

    public Object getPropertyObject(String propertyName) {
        return (propertyName.equals("collectionDate")) ? (Object) collectionDate : (propertyName.equals("type")) ? (Object) subType.getSampleType() : (propertyName.equals("subType")) ? (Object) subType : (propertyName.equals("typeMemo")) ? (Object) typeMemo : (propertyName.equals("currentLocation")) ? (Object) currentLocation : (propertyName.equals("plannedUser")) ? (Object) plannedUser : (propertyName.equals("processingMetaData1")) ? (Object) processingMetaData1 : (propertyName.equals("processingMetaData2")) ? (Object) processingMetaData2 : (propertyName.equals("processingMetaData3")) ? (Object) processingMetaData3 : (propertyName.equals("storageCondition")) ? (Object) storageCondition : (propertyName.equals("transportCondition")) ? (Object) transportCondition : (propertyName.equals("receptionState")) ? (Object) receptionState : (propertyName.equals("person")) ? (Object) person : super.getPropertyObject(propertyName);
    }

    public void setCollectionDate(Date date1) {
        collectionDate = date1;
    }

    public void setPerson(Person pers) {
        person = pers;
    }

    public void setSubType(SampleSubType tp) {
        subType = tp;
    }

    public void setTypeMemo(String string) {
        typeMemo = string;
    }

    public void setProcessingMetaData1(ProcessingProtocol pm1) {
        processingMetaData1 = pm1;
    }

    public void setProcessingMetaData2(String pm2) {
        processingMetaData2 = pm2;
    }

    public void setProcessingMetaData3(String pm3) {
        processingMetaData3 = pm3;
    }

    public void setCurrentLocation(SampleLocation loc) {
        currentLocation = loc;
    }

    public void setStorageCondition(StorageCondition cond) {
        storageCondition = cond;
    }

    public void setTransportCondition(TransportCondition cond) {
        transportCondition = cond;
    }

    public void setReceptionState(ReceptionState state) {
        receptionState = state;
    }

    public void setPlannedUser(Partner partn) {
        plannedUser = partn;
    }

    public void setAliquots(List list) {
        aliquots = list;
    }
}
