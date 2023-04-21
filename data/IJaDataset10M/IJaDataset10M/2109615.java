package service;

public class Data {

    protected service.NewClientContact newClientContact;

    protected service.NewServerContact newServerContact;

    protected service.SynchRequest synchRequest;

    protected service.UpdatedClientContact updatedClientContact;

    protected service.UpdatedServerContact updatedServerContact;

    protected java.lang.Boolean valid;

    public Data() {
    }

    public Data(service.NewClientContact newClientContact, service.NewServerContact newServerContact, service.SynchRequest synchRequest, service.UpdatedClientContact updatedClientContact, service.UpdatedServerContact updatedServerContact, java.lang.Boolean valid) {
        this.newClientContact = newClientContact;
        this.newServerContact = newServerContact;
        this.synchRequest = synchRequest;
        this.updatedClientContact = updatedClientContact;
        this.updatedServerContact = updatedServerContact;
        this.valid = valid;
    }

    public service.NewClientContact getNewClientContact() {
        return newClientContact;
    }

    public void setNewClientContact(service.NewClientContact newClientContact) {
        this.newClientContact = newClientContact;
    }

    public service.NewServerContact getNewServerContact() {
        return newServerContact;
    }

    public void setNewServerContact(service.NewServerContact newServerContact) {
        this.newServerContact = newServerContact;
    }

    public service.SynchRequest getSynchRequest() {
        return synchRequest;
    }

    public void setSynchRequest(service.SynchRequest synchRequest) {
        this.synchRequest = synchRequest;
    }

    public service.UpdatedClientContact getUpdatedClientContact() {
        return updatedClientContact;
    }

    public void setUpdatedClientContact(service.UpdatedClientContact updatedClientContact) {
        this.updatedClientContact = updatedClientContact;
    }

    public service.UpdatedServerContact getUpdatedServerContact() {
        return updatedServerContact;
    }

    public void setUpdatedServerContact(service.UpdatedServerContact updatedServerContact) {
        this.updatedServerContact = updatedServerContact;
    }

    public java.lang.Boolean getValid() {
        return valid;
    }

    public void setValid(java.lang.Boolean valid) {
        this.valid = valid;
    }
}
