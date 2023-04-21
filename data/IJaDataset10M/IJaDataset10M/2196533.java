package de.bea.services.vidya.client.datasource.types;

public class ExplicantoWebService_getMediaPreview_RequestStruct {

    protected de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    protected de.bea.services.vidya.client.datasource.types.WSMediaElem WSMediaElem_2;

    public ExplicantoWebService_getMediaPreview_RequestStruct() {
    }

    public ExplicantoWebService_getMediaPreview_RequestStruct(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1, de.bea.services.vidya.client.datasource.types.WSMediaElem WSMediaElem_2) {
        this.WSAuthentication_1 = WSAuthentication_1;
        this.WSMediaElem_2 = WSMediaElem_2;
    }

    public de.bea.services.vidya.client.datasource.types.WSAuthentication getWSAuthentication_1() {
        return WSAuthentication_1;
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public de.bea.services.vidya.client.datasource.types.WSMediaElem getWSMediaElem_2() {
        return WSMediaElem_2;
    }

    public void setWSMediaElem_2(de.bea.services.vidya.client.datasource.types.WSMediaElem WSMediaElem_2) {
        this.WSMediaElem_2 = WSMediaElem_2;
    }
}
