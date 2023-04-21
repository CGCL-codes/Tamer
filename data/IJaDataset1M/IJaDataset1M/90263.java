package de.bea.services.vidya.client.datasource.types;

public class ExplicantoWebService_removeLessonUnlock_RequestStruct {

    protected de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1;

    protected de.bea.services.vidya.client.datasource.types.WSLesson WSLesson_2;

    protected boolean boolean_3;

    public ExplicantoWebService_removeLessonUnlock_RequestStruct() {
    }

    public ExplicantoWebService_removeLessonUnlock_RequestStruct(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1, de.bea.services.vidya.client.datasource.types.WSLesson WSLesson_2, boolean boolean_3) {
        this.WSAuthentication_1 = WSAuthentication_1;
        this.WSLesson_2 = WSLesson_2;
        this.boolean_3 = boolean_3;
    }

    public de.bea.services.vidya.client.datasource.types.WSAuthentication getWSAuthentication_1() {
        return WSAuthentication_1;
    }

    public void setWSAuthentication_1(de.bea.services.vidya.client.datasource.types.WSAuthentication WSAuthentication_1) {
        this.WSAuthentication_1 = WSAuthentication_1;
    }

    public de.bea.services.vidya.client.datasource.types.WSLesson getWSLesson_2() {
        return WSLesson_2;
    }

    public void setWSLesson_2(de.bea.services.vidya.client.datasource.types.WSLesson WSLesson_2) {
        this.WSLesson_2 = WSLesson_2;
    }

    public boolean isBoolean_3() {
        return boolean_3;
    }

    public void setBoolean_3(boolean boolean_3) {
        this.boolean_3 = boolean_3;
    }
}
