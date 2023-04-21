package de.bea.services.vidya.client.datasource.types;

public class ExplicantoWebService_unlockScreenplay_RequestStruct {

    protected de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    protected long long_2;

    public ExplicantoWebService_unlockScreenplay_RequestStruct() {
    }

    public ExplicantoWebService_unlockScreenplay_RequestStruct(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1, long long_2) {
        this.WSAuthentication_1 = WSAuthentication_1;
        this.long_2 = long_2;
    }

    public de.bea.services.vidya.client.datasource.types.WSAuthentication getWSAuthentication_1() {
        return WSAuthentication_1;
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public long getLong_2() {
        return long_2;
    }

    public void setLong_2(long long_2) {
        this.long_2 = long_2;
    }
}
