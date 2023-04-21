package de.bea.services.vidya.client.datasource.types;

public class ExplicantoWebService_updateCSDETemplate_RequestStruct {

    protected de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    protected de.bea.services.vidya.client.datasource.types.WSCSDETemplate WSCSDETemplate_2;

    public ExplicantoWebService_updateCSDETemplate_RequestStruct() {
    }

    public ExplicantoWebService_updateCSDETemplate_RequestStruct(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1, de.bea.services.vidya.client.datasource.types.WSCSDETemplate WSCSDETemplate_2) {
        this.WSAuthentication_1 = WSAuthentication_1;
        this.WSCSDETemplate_2 = WSCSDETemplate_2;
    }

    public de.bea.services.vidya.client.datasource.types.WSAuthentication getWSAuthentication_1() {
        return WSAuthentication_1;
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public de.bea.services.vidya.client.datasource.types.WSCSDETemplate getWSCSDETemplate_2() {
        return WSCSDETemplate_2;
    }

    public void setWSCSDETemplate_2(de.bea.services.vidya.client.datasource.types.WSCSDETemplate WSCSDETemplate_2) {
        this.WSCSDETemplate_2 = WSCSDETemplate_2;
    }
}
