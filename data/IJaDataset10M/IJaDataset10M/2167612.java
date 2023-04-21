package tx.phonebook.data.proc;

import android.util.Log;
import com.google.gson.annotations.Expose;

public class PersonInfo {

    @Expose
    private String strId;

    @Expose
    private String strName;

    @Expose
    private String strNumber1;

    @Expose
    private String strNumber1_type;

    @Expose
    private String strNumber2;

    @Expose
    private String strNumber2_type;

    @Expose
    private String strNumber3;

    @Expose
    private String strNumber3_type;

    @Expose
    private String strNumber4;

    @Expose
    private String strNumber4_type;

    @Expose
    private String strEmail1;

    @Expose
    private String strEmail1_type;

    @Expose
    private String strEmail2;

    @Expose
    private String strEmail2_type;

    @Expose
    private String strEmail3;

    @Expose
    private String strEmail3_type;

    @Expose
    private String strEmail4;

    @Expose
    private String strEmail4_type;

    @Expose
    private String strAdd1_Street;

    @Expose
    private String strAdd1_POBox;

    @Expose
    private String strAdd1_Suqare;

    @Expose
    private String strAdd1_City;

    @Expose
    private String strAdd1_State;

    @Expose
    private String strAdd1_PostalCode;

    @Expose
    private String strAdd1_Country;

    @Expose
    private String strAdd1_type;

    @Expose
    private String strAdd2_Street;

    @Expose
    private String strAdd2_POBox;

    @Expose
    private String strAdd2_Suqare;

    @Expose
    private String strAdd2_City;

    @Expose
    private String strAdd2_State;

    @Expose
    private String strAdd2_PostalCode;

    @Expose
    private String strAdd2_Country;

    @Expose
    private String strAdd2_type;

    @Expose
    private String strAdd3_Street;

    @Expose
    private String strAdd3_POBox;

    @Expose
    private String strAdd3_Suqare;

    @Expose
    private String strAdd3_City;

    @Expose
    private String strAdd3_State;

    @Expose
    private String strAdd3_PostalCode;

    @Expose
    private String strAdd3_Country;

    @Expose
    private String strAdd3_type;

    @Expose
    private String strAdd4_Street;

    @Expose
    private String strAdd4_POBox;

    @Expose
    private String strAdd4_Suqare;

    @Expose
    private String strAdd4_City;

    @Expose
    private String strAdd4_State;

    @Expose
    private String strAdd4_PostalCode;

    @Expose
    private String strAdd4_Country;

    @Expose
    private String strAdd4_type;

    @Expose
    private String strOrgCmp;

    @Expose
    private String strOrgPoz;

    @Expose
    private String strState;

    @Expose
    private static int nPersonNum;

    public void copyPersonInfo(PersonInfo person) {
        this.strId = person.strId;
        this.strName = person.strName;
        this.strNumber1 = person.strNumber1;
        this.strNumber1_type = person.strNumber1_type;
        this.strNumber2 = person.strNumber2;
        this.strNumber2_type = person.strNumber2_type;
        this.strNumber3 = person.strNumber3;
        this.strNumber3_type = person.strNumber3_type;
        this.strNumber4 = person.strNumber4;
        this.strNumber4_type = person.strNumber4_type;
        this.strEmail1 = person.strEmail1;
        this.strEmail1_type = person.strEmail1_type;
        this.strEmail2 = person.strEmail2;
        this.strEmail2_type = person.strEmail2_type;
        this.strEmail3 = person.strEmail3;
        this.strEmail3_type = person.strEmail3_type;
        this.strEmail4 = person.strEmail4;
        this.strEmail4_type = person.strEmail4_type;
        this.strAdd1_Street = person.strAdd1_Street;
        this.strAdd1_POBox = person.strAdd1_POBox;
        this.strAdd1_Suqare = person.strAdd1_Suqare;
        this.strAdd1_City = person.strAdd1_City;
        this.strAdd1_State = person.strAdd1_State;
        this.strAdd1_PostalCode = person.strAdd1_PostalCode;
        this.strAdd1_Country = person.strAdd1_Country;
        this.strAdd1_type = person.strAdd1_type;
        this.strAdd2_Street = person.strAdd2_Street;
        this.strAdd2_POBox = person.strAdd2_POBox;
        this.strAdd2_Suqare = person.strAdd2_Suqare;
        this.strAdd2_City = person.strAdd2_City;
        this.strAdd2_State = person.strAdd2_State;
        this.strAdd2_PostalCode = person.strAdd2_PostalCode;
        this.strAdd2_Country = person.strAdd2_Country;
        this.strAdd2_type = person.strAdd2_type;
        this.strAdd3_Street = person.strAdd3_Street;
        this.strAdd3_POBox = person.strAdd3_POBox;
        this.strAdd3_Suqare = person.strAdd3_Suqare;
        this.strAdd3_City = person.strAdd3_City;
        this.strAdd3_State = person.strAdd3_State;
        this.strAdd3_PostalCode = person.strAdd3_PostalCode;
        this.strAdd3_Country = person.strAdd3_Country;
        this.strAdd3_type = person.strAdd3_type;
        this.strAdd4_Street = person.strAdd4_Street;
        this.strAdd4_POBox = person.strAdd4_POBox;
        this.strAdd4_Suqare = person.strAdd4_Suqare;
        this.strAdd4_City = person.strAdd4_City;
        this.strAdd4_State = person.strAdd4_State;
        this.strAdd4_PostalCode = person.strAdd4_PostalCode;
        this.strAdd4_Country = person.strAdd4_Country;
        this.strAdd4_type = person.strAdd4_type;
        this.strOrgCmp = person.strOrgCmp;
        this.strOrgPoz = person.strOrgPoz;
    }

    public boolean comparePersonInfo(PersonInfo person) {
        if (("" + this.strName).equals("" + person.strName) && ("" + this.strName).equals("" + person.strName) && ("" + this.strNumber1).equals("" + person.strNumber1) && ("" + this.strNumber1_type).equals("" + person.strNumber1_type) && ("" + this.strNumber2).equals("" + "" + person.strNumber2) && ("" + this.strNumber2_type).equals("" + person.strNumber2_type) && ("" + this.strNumber3).equals("" + person.strNumber3) && ("" + this.strNumber3_type).equals("" + person.strNumber3_type) && ("" + this.strNumber4).equals("" + person.strNumber4) && ("" + this.strNumber4_type).equals("" + person.strNumber4_type) && ("" + this.strEmail1).equals("" + person.strEmail1) && ("" + this.strEmail1_type).equals("" + person.strEmail1_type) && ("" + this.strEmail2).equals("" + person.strEmail2) && ("" + this.strEmail2_type).equals("" + person.strEmail2_type) && ("" + this.strEmail3).equals("" + person.strEmail3) && ("" + this.strEmail3_type).equals("" + person.strEmail3_type) && ("" + this.strEmail4).equals("" + person.strEmail4) && ("" + this.strEmail4_type).equals("" + person.strEmail4_type) && ("" + this.strAdd1_Street).equals("" + person.strAdd1_Street) && ("" + this.strAdd1_POBox).equals("" + person.strAdd1_POBox) && ("" + this.strAdd1_Suqare).equals("" + person.strAdd1_Suqare) && ("" + this.strAdd1_City).equals("" + person.strAdd1_City) && ("" + this.strAdd1_State).equals("" + person.strAdd1_State) && ("" + this.strAdd1_PostalCode).equals("" + person.strAdd1_PostalCode) && ("" + this.strAdd1_Country).equals("" + person.strAdd1_Country) && ("" + this.strAdd1_type).equals("" + person.strAdd1_type) && ("" + this.strAdd2_Street).equals("" + person.strAdd2_Street) && ("" + this.strAdd2_POBox).equals("" + person.strAdd2_POBox) && ("" + this.strAdd2_Suqare).equals("" + person.strAdd2_Suqare) && ("" + this.strAdd2_City).equals("" + person.strAdd2_City) && ("" + this.strAdd2_State).equals("" + person.strAdd2_State) && ("" + this.strAdd2_PostalCode).equals("" + person.strAdd2_PostalCode) && ("" + this.strAdd2_Country).equals("" + person.strAdd2_Country) && ("" + this.strAdd2_type).equals("" + person.strAdd2_type) && ("" + this.strAdd3_Street).equals("" + person.strAdd3_Street) && ("" + this.strAdd3_POBox).equals("" + person.strAdd3_POBox) && ("" + this.strAdd3_Suqare).equals("" + person.strAdd3_Suqare) && ("" + this.strAdd3_City).equals("" + person.strAdd3_City) && ("" + this.strAdd3_State).equals("" + person.strAdd3_State) && ("" + this.strAdd3_PostalCode).equals("" + person.strAdd3_PostalCode) && ("" + this.strAdd3_Country).equals("" + person.strAdd3_Country) && ("" + this.strAdd3_type).equals("" + person.strAdd3_type) && ("" + this.strAdd4_Street).equals("" + person.strAdd4_Street) && ("" + this.strAdd4_POBox).equals("" + person.strAdd4_POBox) && ("" + this.strAdd4_Suqare).equals("" + person.strAdd4_Suqare) && ("" + this.strAdd4_City).equals("" + person.strAdd4_City) && ("" + this.strAdd4_State).equals("" + person.strAdd4_State) && ("" + this.strAdd4_PostalCode).equals("" + person.strAdd4_PostalCode) && ("" + this.strAdd4_Country).equals("" + person.strAdd4_Country) && ("" + this.strAdd4_type).equals("" + person.strAdd4_type) && ("" + this.strOrgCmp).equals("" + person.strOrgCmp) && ("" + this.strOrgPoz).equals("" + person.strOrgPoz)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean dataCompare(PersonInfo personContact, PersonInfo personSd) {
        return false;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrNumber1() {
        return strNumber1;
    }

    public void setStrNumber1(String strNumber1) {
        this.strNumber1 = strNumber1;
    }

    public String getStrNumber1_type() {
        return strNumber1_type;
    }

    public void setStrNumber1_type(String strNumber1Type) {
        strNumber1_type = strNumber1Type;
    }

    public String getStrNumber2() {
        return strNumber2;
    }

    public void setStrNumber2(String strNumber2) {
        this.strNumber2 = strNumber2;
    }

    public String getStrNumber2_type() {
        return strNumber2_type;
    }

    public void setStrNumber2_type(String strNumber2Type) {
        strNumber2_type = strNumber2Type;
    }

    public String getStrNumber3() {
        return strNumber3;
    }

    public void setStrNumber3(String strNumber3) {
        this.strNumber3 = strNumber3;
    }

    public String getStrNumber3_type() {
        return strNumber3_type;
    }

    public void setStrNumber3_type(String strNumber3Type) {
        strNumber3_type = strNumber3Type;
    }

    public String getStrNumber4() {
        return strNumber4;
    }

    public void setStrNumber4(String strNumber4) {
        this.strNumber4 = strNumber4;
    }

    public String getStrNumber4_type() {
        return strNumber4_type;
    }

    public void setStrNumber4_type(String strNumber4Type) {
        strNumber4_type = strNumber4Type;
    }

    public String getStrEmail1() {
        return strEmail1;
    }

    public void setStrEmail1(String strEmail1) {
        this.strEmail1 = strEmail1;
    }

    public String getStrEmail1_type() {
        return strEmail1_type;
    }

    public void setStrEmail1_type(String strEmail1Type) {
        strEmail1_type = strEmail1Type;
    }

    public String getStrEmail2() {
        return strEmail2;
    }

    public void setStrEmail2(String strEmail2) {
        this.strEmail2 = strEmail2;
    }

    public String getStrEmail2_type() {
        return strEmail2_type;
    }

    public void setStrEmail2_type(String strEmail2Type) {
        strEmail2_type = strEmail2Type;
    }

    public String getStrEmail3() {
        return strEmail3;
    }

    public void setStrEmail3(String strEmail3) {
        this.strEmail3 = strEmail3;
    }

    public String getStrEmail3_type() {
        return strEmail3_type;
    }

    public void setStrEmail3_type(String strEmail3Type) {
        strEmail3_type = strEmail3Type;
    }

    public String getStrEmail4() {
        return strEmail4;
    }

    public void setStrEmail4(String strEmail4) {
        this.strEmail4 = strEmail4;
    }

    public String getStrEmail4_type() {
        return strEmail4_type;
    }

    public void setStrEmail4_type(String strEmail4Type) {
        strEmail4_type = strEmail4Type;
    }

    public String getStrAdd1_Street() {
        return strAdd1_Street;
    }

    public void setStrAdd1_Street(String strAdd1Street) {
        strAdd1_Street = strAdd1Street;
    }

    public String getStrAdd1_POBox() {
        return strAdd1_POBox;
    }

    public void setStrAdd1_POBox(String strAdd1POBox) {
        strAdd1_POBox = strAdd1POBox;
    }

    public String getStrAdd1_Suqare() {
        return strAdd1_Suqare;
    }

    public void setStrAdd1_Suqare(String strAdd1Suqare) {
        strAdd1_Suqare = strAdd1Suqare;
    }

    public String getStrAdd1_City() {
        return strAdd1_City;
    }

    public void setStrAdd1_City(String strAdd1City) {
        strAdd1_City = strAdd1City;
    }

    public String getStrAdd1_State() {
        return strAdd1_State;
    }

    public void setStrAdd1_State(String strAdd1State) {
        strAdd1_State = strAdd1State;
    }

    public String getStrAdd1_PostalCode() {
        return strAdd1_PostalCode;
    }

    public void setStrAdd1_PostalCode(String strAdd1PostalCode) {
        strAdd1_PostalCode = strAdd1PostalCode;
    }

    public String getStrAdd1_Country() {
        return strAdd1_Country;
    }

    public void setStrAdd1_Country(String strAdd1Country) {
        strAdd1_Country = strAdd1Country;
    }

    public String getStrAdd1_type() {
        return strAdd1_type;
    }

    public void setStrAdd1_type(String strAdd1Type) {
        strAdd1_type = strAdd1Type;
    }

    public String getStrAdd2_Street() {
        return strAdd2_Street;
    }

    public void setStrAdd2_Street(String strAdd2Street) {
        strAdd2_Street = strAdd2Street;
    }

    public String getStrAdd2_POBox() {
        return strAdd2_POBox;
    }

    public void setStrAdd2_POBox(String strAdd2POBox) {
        strAdd2_POBox = strAdd2POBox;
    }

    public String getStrAdd2_Suqare() {
        return strAdd2_Suqare;
    }

    public void setStrAdd2_Suqare(String strAdd2Suqare) {
        strAdd2_Suqare = strAdd2Suqare;
    }

    public String getStrAdd2_City() {
        return strAdd2_City;
    }

    public void setStrAdd2_City(String strAdd2City) {
        strAdd2_City = strAdd2City;
    }

    public String getStrAdd2_State() {
        return strAdd2_State;
    }

    public void setStrAdd2_State(String strAdd2State) {
        strAdd2_State = strAdd2State;
    }

    public String getStrAdd2_PostalCode() {
        return strAdd2_PostalCode;
    }

    public void setStrAdd2_PostalCode(String strAdd2PostalCode) {
        strAdd2_PostalCode = strAdd2PostalCode;
    }

    public String getStrAdd2_Country() {
        return strAdd2_Country;
    }

    public void setStrAdd2_Country(String strAdd2Country) {
        strAdd2_Country = strAdd2Country;
    }

    public String getStrAdd2_type() {
        return strAdd2_type;
    }

    public void setStrAdd2_type(String strAdd2Type) {
        strAdd2_type = strAdd2Type;
    }

    public String getStrAdd3_Street() {
        return strAdd3_Street;
    }

    public void setStrAdd3_Street(String strAdd3Street) {
        strAdd3_Street = strAdd3Street;
    }

    public String getStrAdd3_POBox() {
        return strAdd3_POBox;
    }

    public void setStrAdd3_POBox(String strAdd3POBox) {
        strAdd3_POBox = strAdd3POBox;
    }

    public String getStrAdd3_Suqare() {
        return strAdd3_Suqare;
    }

    public void setStrAdd3_Suqare(String strAdd3Suqare) {
        strAdd3_Suqare = strAdd3Suqare;
    }

    public String getStrAdd3_City() {
        return strAdd3_City;
    }

    public void setStrAdd3_City(String strAdd3City) {
        strAdd3_City = strAdd3City;
    }

    public String getStrAdd3_State() {
        return strAdd3_State;
    }

    public void setStrAdd3_State(String strAdd3State) {
        strAdd3_State = strAdd3State;
    }

    public String getStrAdd3_PostalCode() {
        return strAdd3_PostalCode;
    }

    public void setStrAdd3_PostalCode(String strAdd3PostalCode) {
        strAdd3_PostalCode = strAdd3PostalCode;
    }

    public String getStrAdd3_Country() {
        return strAdd3_Country;
    }

    public void setStrAdd3_Country(String strAdd3Country) {
        strAdd3_Country = strAdd3Country;
    }

    public String getStrAdd3_type() {
        return strAdd3_type;
    }

    public void setStrAdd3_type(String strAdd3Type) {
        strAdd3_type = strAdd3Type;
    }

    public String getStrAdd4_Street() {
        return strAdd4_Street;
    }

    public void setStrAdd4_Street(String strAdd4Street) {
        strAdd4_Street = strAdd4Street;
    }

    public String getStrAdd4_POBox() {
        return strAdd4_POBox;
    }

    public void setStrAdd4_POBox(String strAdd4POBox) {
        strAdd4_POBox = strAdd4POBox;
    }

    public String getStrAdd4_Suqare() {
        return strAdd4_Suqare;
    }

    public void setStrAdd4_Suqare(String strAdd4Suqare) {
        strAdd4_Suqare = strAdd4Suqare;
    }

    public String getStrAdd4_City() {
        return strAdd4_City;
    }

    public void setStrAdd4_City(String strAdd4City) {
        strAdd4_City = strAdd4City;
    }

    public String getStrAdd4_State() {
        return strAdd4_State;
    }

    public void setStrAdd4_State(String strAdd4State) {
        strAdd4_State = strAdd4State;
    }

    public String getStrAdd4_PostalCode() {
        return strAdd4_PostalCode;
    }

    public void setStrAdd4_PostalCode(String strAdd4PostalCode) {
        strAdd4_PostalCode = strAdd4PostalCode;
    }

    public String getStrAdd4_Country() {
        return strAdd4_Country;
    }

    public void setStrAdd4_Country(String strAdd4Country) {
        strAdd4_Country = strAdd4Country;
    }

    public String getStrAdd4_type() {
        return strAdd4_type;
    }

    public void setStrAdd4_type(String strAdd4Type) {
        strAdd4_type = strAdd4Type;
    }

    public String getStrOrgCmp() {
        return strOrgCmp;
    }

    public void setStrOrgCmp(String strOrgCmp) {
        this.strOrgCmp = strOrgCmp;
    }

    public String getStrOrgPoz() {
        return strOrgPoz;
    }

    public void setStrOrgPoz(String strOrgPoz) {
        this.strOrgPoz = strOrgPoz;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }

    public static int getnPersonNum() {
        return nPersonNum;
    }

    public static void setnPersonNum(int nPersonNum) {
        PersonInfo.nPersonNum = nPersonNum;
    }
}
