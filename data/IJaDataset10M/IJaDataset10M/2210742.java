package de.bea.services.vidya.client.datasource.types;

public class ExplicantoWebService_loadStatusList_RequestStruct {

    protected de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    protected int int_2;

    public ExplicantoWebService_loadStatusList_RequestStruct() {
    }

    public ExplicantoWebService_loadStatusList_RequestStruct(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1, int int_2) {
        this.WSAuthentication_1 = WSAuthentication_1;
        this.int_2 = int_2;
    }

    public de.bea.services.vidya.client.datasource.types.WSAuthentication getWSAuthentication_1() {
        return WSAuthentication_1;
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public int getInt_2() {
        return int_2;
    }

    public void setInt_2(int int_2) {
        this.int_2 = int_2;
    }
}
