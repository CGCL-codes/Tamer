package de.bea.services.vidya.client.datasource.types;

public class ExplicantoWebService_getScreenplayList_RequestStruct {

    protected de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    public ExplicantoWebService_getScreenplayList_RequestStruct() {
    }

    public ExplicantoWebService_getScreenplayList_RequestStruct(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public de.bea.services.vidya.client.datasource.types.WSAuthentication getWSAuthentication_1() {
        return WSAuthentication_1;
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }
}
