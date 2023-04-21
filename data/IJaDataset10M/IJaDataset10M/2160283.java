package org.openxava.qamanager.model;

import java.math.*;
import java.rmi.RemoteException;

public interface ISoftwareRelease extends org.openxava.model.IModel {

    String getLimitationsAndRestrictions() throws RemoteException;

    void setLimitationsAndRestrictions(String limitationsAndRestrictions) throws RemoteException;

    String getArchivalFolder() throws RemoteException;

    void setArchivalFolder(String archivalFolder) throws RemoteException;

    boolean isSeverBugsClosed() throws RemoteException;

    void setSeverBugsClosed(boolean severBugsClosed) throws RemoteException;

    String getEngineeringDetails() throws RemoteException;

    void setEngineeringDetails(String engineeringDetails) throws RemoteException;

    String getOutstandingBugs() throws RemoteException;

    void setOutstandingBugs(String outstandingBugs) throws RemoteException;

    String getReleasedDocument() throws RemoteException;

    void setReleasedDocument(String releasedDocument) throws RemoteException;

    String getRemarks() throws RemoteException;

    void setRemarks(String remarks) throws RemoteException;

    boolean isReceivedLatestSourceDocuments() throws RemoteException;

    void setReceivedLatestSourceDocuments(boolean receivedLatestSourceDocuments) throws RemoteException;

    boolean isAllUnitTestsPassed() throws RemoteException;

    void setAllUnitTestsPassed(boolean allUnitTestsPassed) throws RemoteException;

    boolean isFunctionalityFullyImplemented() throws RemoteException;

    void setFunctionalityFullyImplemented(boolean functionalityFullyImplemented) throws RemoteException;

    String getReleasedSoftware() throws RemoteException;

    void setReleasedSoftware(String releasedSoftware) throws RemoteException;

    java.util.Date getActualShipping() throws RemoteException;

    void setActualShipping(java.util.Date actualShipping) throws RemoteException;

    boolean isAcceptanceTestsPassed() throws RemoteException;

    void setAcceptanceTestsPassed(boolean acceptanceTestsPassed) throws RemoteException;

    String getReasonDelay() throws RemoteException;

    void setReasonDelay(String reasonDelay) throws RemoteException;

    boolean isUserManualsVerified() throws RemoteException;

    void setUserManualsVerified(boolean userManualsVerified) throws RemoteException;

    boolean isVirusChecked() throws RemoteException;

    void setVirusChecked(boolean virusChecked) throws RemoteException;

    java.util.Date getRequestedShipping() throws RemoteException;

    void setRequestedShipping(java.util.Date requestedShipping) throws RemoteException;

    boolean isFuntionalityFullyTested() throws RemoteException;

    void setFuntionalityFullyTested(boolean funtionalityFullyTested) throws RemoteException;

    boolean isStressTesting() throws RemoteException;

    void setStressTesting(boolean stressTesting) throws RemoteException;

    boolean isRegressionTesting() throws RemoteException;

    void setRegressionTesting(boolean regressionTesting) throws RemoteException;

    org.openxava.qamanager.model.IResource getRequestedBy() throws RemoteException;

    void setRequestedBy(org.openxava.qamanager.model.IResource newRequestedBy) throws RemoteException;

    org.openxava.qamanager.model.IShipmentMethod getShipmentMethod() throws RemoteException;

    void setShipmentMethod(org.openxava.qamanager.model.IShipmentMethod newShipmentMethod) throws RemoteException;

    org.openxava.qamanager.model.IResource getReleasebyQA() throws RemoteException;

    void setReleasebyQA(org.openxava.qamanager.model.IResource newReleasebyQA) throws RemoteException;

    org.openxava.qamanager.model.ICustomer getCustomer() throws RemoteException;

    void setCustomer(org.openxava.qamanager.model.ICustomer newCustomer) throws RemoteException;

    org.openxava.qamanager.model.ITestCycle getTestCycle() throws RemoteException;

    void setTestCycle(org.openxava.qamanager.model.ITestCycle newTestCycle) throws RemoteException;

    org.openxava.qamanager.model.IReleaseType getReleaseType() throws RemoteException;

    void setReleaseType(org.openxava.qamanager.model.IReleaseType newReleaseType) throws RemoteException;

    org.openxava.qamanager.model.IResource getReleasebyPM() throws RemoteException;

    void setReleasebyPM(org.openxava.qamanager.model.IResource newReleasebyPM) throws RemoteException;

    org.openxava.qamanager.model.IResource getReleasebyEngineering() throws RemoteException;

    void setReleasebyEngineering(org.openxava.qamanager.model.IResource newReleasebyEngineering) throws RemoteException;
}
