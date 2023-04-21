package cn.mmbook.platform.model.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.*;
import javacommon.base.*;
import javacommon.util.*;
import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;
import cn.mmbook.platform.model.data.*;
import cn.mmbook.platform.dao.data.*;
import cn.mmbook.platform.service.data.impl.*;
import cn.mmbook.platform.service.data.*;

public class TaxiData extends BaseEntity {

    public static final String FORMAT_ASSESSMENT_DATE_ = DATE_TIME_FORMAT;

    private java.math.BigDecimal id;

    private java.lang.String questionnaireNumber;

    private java.lang.String xiCompany;

    private java.lang.String numberPlates;

    private java.lang.String customerName;

    private java.sql.Date assessmentDate;

    /**测评时间*/
    private java.lang.String evaluationTime;

    private java.lang.String pickUpLocation;

    private java.lang.String dropOffPoint;

    private java.lang.String unitNo;

    /**车票时间*/
    private java.lang.String ticketTime;

    private java.lang.String deduction1;

    private java.lang.String subject1;

    private java.lang.String deduction2;

    private java.lang.String subject2;

    private java.lang.String deduction3;

    private java.lang.String subject3;

    private java.lang.String deduction4;

    private java.lang.String subject4;

    private java.lang.String deduction5;

    private java.lang.String subject5;

    private java.lang.String deduction6;

    private java.lang.String subject6;

    private java.lang.String deduction7;

    private java.lang.String subject7;

    private java.lang.String deduction8;

    private java.lang.String subject8;

    private java.lang.String deduction9;

    private java.lang.String subject9;

    private java.lang.String deduction10;

    private java.lang.String subject10;

    private java.lang.String deduction11;

    private java.lang.String subject11;

    private java.lang.String deduction12;

    private java.lang.String subject12;

    private java.lang.String deduction13;

    private java.lang.String subject13;

    private java.lang.String deduction14;

    private java.lang.String subject14;

    private java.lang.String deduction15;

    private java.lang.String subject15;

    private java.lang.String deduction16;

    private java.lang.String subject16;

    private java.lang.String deduction17;

    private java.lang.String subject17;

    private java.lang.String deduction18;

    private java.lang.String subject18;

    private java.lang.String deduction19;

    private java.lang.String subject19;

    private java.lang.String deduction20;

    private java.lang.String subject20;

    private java.lang.String deduction21;

    private java.lang.String subject21;

    private java.lang.String deduction22;

    private java.lang.String subject22;

    private java.lang.String deduction23;

    private java.lang.String subject23;

    private java.lang.String deduction24;

    private java.lang.String subject24;

    private java.lang.String deduction25;

    private java.lang.String subject25;

    private java.sql.Date beginTime;

    private java.sql.Date endTime;

    public String setEndTimeString() {
        return date2String(getEndTime(), FORMAT_ASSESSMENT_DATE_);
    }

    public void setEndTimeString(String value) {
        setEndTime(string2Date(value, FORMAT_ASSESSMENT_DATE_, java.sql.Date.class));
    }

    public String setBeginTimeString() {
        return date2String(getBeginTime(), FORMAT_ASSESSMENT_DATE_);
    }

    public void setBeginTimeString(String value) {
        setBeginTime(string2Date(value, FORMAT_ASSESSMENT_DATE_, java.sql.Date.class));
    }

    public java.sql.Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(java.sql.Date beginTime) {
        this.beginTime = beginTime;
    }

    public java.sql.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.sql.Date endTime) {
        this.endTime = endTime;
    }

    public TaxiData() {
    }

    public TaxiData(java.math.BigDecimal id) {
        this.id = id;
    }

    public void setId(java.math.BigDecimal value) {
        this.id = value;
    }

    public java.math.BigDecimal getId() {
        return this.id;
    }

    public void setQuestionnaireNumber(java.lang.String value) {
        this.questionnaireNumber = value;
    }

    public java.lang.String getQuestionnaireNumber() {
        return this.questionnaireNumber;
    }

    public void setXiCompany(java.lang.String value) {
        this.xiCompany = value;
    }

    public java.lang.String getXiCompany() {
        return this.xiCompany;
    }

    public void setNumberPlates(java.lang.String value) {
        this.numberPlates = value;
    }

    public java.lang.String getNumberPlates() {
        return this.numberPlates;
    }

    public void setCustomerName(java.lang.String value) {
        this.customerName = value;
    }

    public java.lang.String getCustomerName() {
        return this.customerName;
    }

    public String getAssessmentDateString() {
        return date2String(getAssessmentDate(), FORMAT_ASSESSMENT_DATE_);
    }

    public void setAssessmentDateString(String value) {
        setAssessmentDate(string2Date(value, FORMAT_ASSESSMENT_DATE_, java.sql.Date.class));
    }

    public void setAssessmentDate(java.sql.Date value) {
        this.assessmentDate = value;
    }

    public java.sql.Date getAssessmentDate() {
        return this.assessmentDate;
    }

    public void setEvaluationTime(java.lang.String value) {
        this.evaluationTime = value;
    }

    public java.lang.String getEvaluationTime() {
        return this.evaluationTime;
    }

    public void setPickUpLocation(java.lang.String value) {
        this.pickUpLocation = value;
    }

    public java.lang.String getPickUpLocation() {
        return this.pickUpLocation;
    }

    public void setDropOffPoint(java.lang.String value) {
        this.dropOffPoint = value;
    }

    public java.lang.String getDropOffPoint() {
        return this.dropOffPoint;
    }

    public void setUnitNo(java.lang.String value) {
        this.unitNo = value;
    }

    public java.lang.String getUnitNo() {
        return this.unitNo;
    }

    public void setDeduction1(java.lang.String value) {
        this.deduction1 = value;
    }

    public java.lang.String getDeduction1() {
        return this.deduction1;
    }

    public void setSubject1(java.lang.String value) {
        this.subject1 = value;
    }

    public java.lang.String getSubject1() {
        return this.subject1;
    }

    public void setDeduction2(java.lang.String value) {
        this.deduction2 = value;
    }

    public java.lang.String getDeduction2() {
        return this.deduction2;
    }

    public void setSubject2(java.lang.String value) {
        this.subject2 = value;
    }

    public java.lang.String getSubject2() {
        return this.subject2;
    }

    public void setDeduction3(java.lang.String value) {
        this.deduction3 = value;
    }

    public java.lang.String getDeduction3() {
        return this.deduction3;
    }

    public void setSubject3(java.lang.String value) {
        this.subject3 = value;
    }

    public java.lang.String getSubject3() {
        return this.subject3;
    }

    public void setDeduction4(java.lang.String value) {
        this.deduction4 = value;
    }

    public java.lang.String getDeduction4() {
        return this.deduction4;
    }

    public void setSubject4(java.lang.String value) {
        this.subject4 = value;
    }

    public java.lang.String getSubject4() {
        return this.subject4;
    }

    public void setDeduction5(java.lang.String value) {
        this.deduction5 = value;
    }

    public java.lang.String getDeduction5() {
        return this.deduction5;
    }

    public void setSubject5(java.lang.String value) {
        this.subject5 = value;
    }

    public java.lang.String getSubject5() {
        return this.subject5;
    }

    public void setDeduction6(java.lang.String value) {
        this.deduction6 = value;
    }

    public java.lang.String getDeduction6() {
        return this.deduction6;
    }

    public void setSubject6(java.lang.String value) {
        this.subject6 = value;
    }

    public java.lang.String getSubject6() {
        return this.subject6;
    }

    public void setDeduction7(java.lang.String value) {
        this.deduction7 = value;
    }

    public java.lang.String getDeduction7() {
        return this.deduction7;
    }

    public void setSubject7(java.lang.String value) {
        this.subject7 = value;
    }

    public java.lang.String getSubject7() {
        return this.subject7;
    }

    public void setDeduction8(java.lang.String value) {
        this.deduction8 = value;
    }

    public java.lang.String getDeduction8() {
        return this.deduction8;
    }

    public void setSubject8(java.lang.String value) {
        this.subject8 = value;
    }

    public java.lang.String getSubject8() {
        return this.subject8;
    }

    public void setDeduction9(java.lang.String value) {
        this.deduction9 = value;
    }

    public java.lang.String getDeduction9() {
        return this.deduction9;
    }

    public void setSubject9(java.lang.String value) {
        this.subject9 = value;
    }

    public java.lang.String getSubject9() {
        return this.subject9;
    }

    public void setDeduction10(java.lang.String value) {
        this.deduction10 = value;
    }

    public java.lang.String getDeduction10() {
        return this.deduction10;
    }

    public void setSubject10(java.lang.String value) {
        this.subject10 = value;
    }

    public java.lang.String getSubject10() {
        return this.subject10;
    }

    public void setDeduction11(java.lang.String value) {
        this.deduction11 = value;
    }

    public java.lang.String getDeduction11() {
        return this.deduction11;
    }

    public void setSubject11(java.lang.String value) {
        this.subject11 = value;
    }

    public java.lang.String getSubject11() {
        return this.subject11;
    }

    public void setDeduction12(java.lang.String value) {
        this.deduction12 = value;
    }

    public java.lang.String getDeduction12() {
        return this.deduction12;
    }

    public void setSubject12(java.lang.String value) {
        this.subject12 = value;
    }

    public java.lang.String getSubject12() {
        return this.subject12;
    }

    public void setDeduction13(java.lang.String value) {
        this.deduction13 = value;
    }

    public java.lang.String getDeduction13() {
        return this.deduction13;
    }

    public void setSubject13(java.lang.String value) {
        this.subject13 = value;
    }

    public java.lang.String getSubject13() {
        return this.subject13;
    }

    public void setDeduction14(java.lang.String value) {
        this.deduction14 = value;
    }

    public java.lang.String getDeduction14() {
        return this.deduction14;
    }

    public void setSubject14(java.lang.String value) {
        this.subject14 = value;
    }

    public java.lang.String getSubject14() {
        return this.subject14;
    }

    public void setDeduction15(java.lang.String value) {
        this.deduction15 = value;
    }

    public java.lang.String getDeduction15() {
        return this.deduction15;
    }

    public void setSubject15(java.lang.String value) {
        this.subject15 = value;
    }

    public java.lang.String getSubject15() {
        return this.subject15;
    }

    public void setDeduction16(java.lang.String value) {
        this.deduction16 = value;
    }

    public java.lang.String getDeduction16() {
        return this.deduction16;
    }

    public void setSubject16(java.lang.String value) {
        this.subject16 = value;
    }

    public java.lang.String getSubject16() {
        return this.subject16;
    }

    public void setDeduction17(java.lang.String value) {
        this.deduction17 = value;
    }

    public java.lang.String getDeduction17() {
        return this.deduction17;
    }

    public void setSubject17(java.lang.String value) {
        this.subject17 = value;
    }

    public java.lang.String getSubject17() {
        return this.subject17;
    }

    public void setDeduction18(java.lang.String value) {
        this.deduction18 = value;
    }

    public java.lang.String getDeduction18() {
        return this.deduction18;
    }

    public void setSubject18(java.lang.String value) {
        this.subject18 = value;
    }

    public java.lang.String getSubject18() {
        return this.subject18;
    }

    public void setDeduction19(java.lang.String value) {
        this.deduction19 = value;
    }

    public java.lang.String getDeduction19() {
        return this.deduction19;
    }

    public void setSubject19(java.lang.String value) {
        this.subject19 = value;
    }

    public java.lang.String getSubject19() {
        return this.subject19;
    }

    public void setDeduction20(java.lang.String value) {
        this.deduction20 = value;
    }

    public java.lang.String getDeduction20() {
        return this.deduction20;
    }

    public void setSubject20(java.lang.String value) {
        this.subject20 = value;
    }

    public java.lang.String getSubject20() {
        return this.subject20;
    }

    public void setDeduction21(java.lang.String value) {
        this.deduction21 = value;
    }

    public java.lang.String getDeduction21() {
        return this.deduction21;
    }

    public void setSubject21(java.lang.String value) {
        this.subject21 = value;
    }

    public java.lang.String getSubject21() {
        return this.subject21;
    }

    public void setDeduction22(java.lang.String value) {
        this.deduction22 = value;
    }

    public java.lang.String getDeduction22() {
        return this.deduction22;
    }

    public void setSubject22(java.lang.String value) {
        this.subject22 = value;
    }

    public java.lang.String getSubject22() {
        return this.subject22;
    }

    public void setDeduction23(java.lang.String value) {
        this.deduction23 = value;
    }

    public java.lang.String getDeduction23() {
        return this.deduction23;
    }

    public void setSubject23(java.lang.String value) {
        this.subject23 = value;
    }

    public java.lang.String getSubject23() {
        return this.subject23;
    }

    public void setDeduction24(java.lang.String value) {
        this.deduction24 = value;
    }

    public java.lang.String getDeduction24() {
        return this.deduction24;
    }

    public void setSubject24(java.lang.String value) {
        this.subject24 = value;
    }

    public java.lang.String getSubject24() {
        return this.subject24;
    }

    public void setDeduction25(java.lang.String value) {
        this.deduction25 = value;
    }

    public java.lang.String getDeduction25() {
        return this.deduction25;
    }

    public void setSubject25(java.lang.String value) {
        this.subject25 = value;
    }

    public java.lang.String getSubject25() {
        return this.subject25;
    }

    public String toString() {
        return new ToStringBuilder(this).append("Id", getId()).append("QuestionnaireNumber", getQuestionnaireNumber()).append("XiCompany", getXiCompany()).append("NumberPlates", getNumberPlates()).append("CustomerName", getCustomerName()).append("AssessmentDate", getAssessmentDate()).append("EvaluationTime", getEvaluationTime()).append("PickUpLocation", getPickUpLocation()).append("DropOffPoint", getDropOffPoint()).append("UnitNo", getUnitNo()).append("TicketTime", getTicketTime()).append("Deduction1", getDeduction1()).append("Subject1", getSubject1()).append("Deduction2", getDeduction2()).append("Subject2", getSubject2()).append("Deduction3", getDeduction3()).append("Subject3", getSubject3()).append("Deduction4", getDeduction4()).append("Subject4", getSubject4()).append("Deduction5", getDeduction5()).append("Subject5", getSubject5()).append("Deduction6", getDeduction6()).append("Subject6", getSubject6()).append("Deduction7", getDeduction7()).append("Subject7", getSubject7()).append("Deduction8", getDeduction8()).append("Subject8", getSubject8()).append("Deduction9", getDeduction9()).append("Subject9", getSubject9()).append("Deduction10", getDeduction10()).append("Subject10", getSubject10()).append("Deduction11", getDeduction11()).append("Subject11", getSubject11()).append("Deduction12", getDeduction12()).append("Subject12", getSubject12()).append("Deduction13", getDeduction13()).append("Subject13", getSubject13()).append("Deduction14", getDeduction14()).append("Subject14", getSubject14()).append("Deduction15", getDeduction15()).append("Subject15", getSubject15()).append("Deduction16", getDeduction16()).append("Subject16", getSubject16()).append("Deduction17", getDeduction17()).append("Subject17", getSubject17()).append("Deduction18", getDeduction18()).append("Subject18", getSubject18()).append("Deduction19", getDeduction19()).append("Subject19", getSubject19()).append("Deduction20", getDeduction20()).append("Subject20", getSubject20()).append("Deduction21", getDeduction21()).append("Subject21", getSubject21()).append("Deduction22", getDeduction22()).append("Subject22", getSubject22()).append("Deduction23", getDeduction23()).append("Subject23", getSubject23()).append("Deduction24", getDeduction24()).append("Subject24", getSubject24()).append("Deduction25", getDeduction25()).append("Subject25", getSubject25()).append("BeginTime", getBeginTime()).append("EndTime", getEndTime()).toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).append(getQuestionnaireNumber()).append(getXiCompany()).append(getNumberPlates()).append(getCustomerName()).append(getAssessmentDate()).append(getEvaluationTime()).append(getPickUpLocation()).append(getDropOffPoint()).append(getUnitNo()).append(getTicketTime()).append(getDeduction1()).append(getSubject1()).append(getDeduction2()).append(getSubject2()).append(getDeduction3()).append(getSubject3()).append(getDeduction4()).append(getSubject4()).append(getDeduction5()).append(getSubject5()).append(getDeduction6()).append(getSubject6()).append(getDeduction7()).append(getSubject7()).append(getDeduction8()).append(getSubject8()).append(getDeduction9()).append(getSubject9()).append(getDeduction10()).append(getSubject10()).append(getDeduction11()).append(getSubject11()).append(getDeduction12()).append(getSubject12()).append(getDeduction13()).append(getSubject13()).append(getDeduction14()).append(getSubject14()).append(getDeduction15()).append(getSubject15()).append(getDeduction16()).append(getSubject16()).append(getDeduction17()).append(getSubject17()).append(getDeduction18()).append(getSubject18()).append(getDeduction19()).append(getSubject19()).append(getDeduction20()).append(getSubject20()).append(getDeduction21()).append(getSubject21()).append(getDeduction22()).append(getSubject22()).append(getDeduction23()).append(getSubject23()).append(getDeduction24()).append(getSubject24()).append(getDeduction25()).append(getSubject25()).append(getEndTime()).append(getBeginTime()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof TaxiData == false) return false;
        if (this == obj) return true;
        TaxiData other = (TaxiData) obj;
        return new EqualsBuilder().append(getId(), other.getId()).append(getQuestionnaireNumber(), other.getQuestionnaireNumber()).append(getXiCompany(), other.getXiCompany()).append(getNumberPlates(), other.getNumberPlates()).append(getCustomerName(), other.getCustomerName()).append(getAssessmentDate(), other.getAssessmentDate()).append(getEvaluationTime(), other.getEvaluationTime()).append(getPickUpLocation(), other.getPickUpLocation()).append(getDropOffPoint(), other.getDropOffPoint()).append(getUnitNo(), other.getUnitNo()).append(getTicketTime(), other.getTicketTime()).append(getDeduction1(), other.getDeduction1()).append(getSubject1(), other.getSubject1()).append(getDeduction2(), other.getDeduction2()).append(getSubject2(), other.getSubject2()).append(getDeduction3(), other.getDeduction3()).append(getSubject3(), other.getSubject3()).append(getDeduction4(), other.getDeduction4()).append(getSubject4(), other.getSubject4()).append(getDeduction5(), other.getDeduction5()).append(getSubject5(), other.getSubject5()).append(getDeduction6(), other.getDeduction6()).append(getSubject6(), other.getSubject6()).append(getDeduction7(), other.getDeduction7()).append(getSubject7(), other.getSubject7()).append(getDeduction8(), other.getDeduction8()).append(getSubject8(), other.getSubject8()).append(getDeduction9(), other.getDeduction9()).append(getSubject9(), other.getSubject9()).append(getDeduction10(), other.getDeduction10()).append(getSubject10(), other.getSubject10()).append(getDeduction11(), other.getDeduction11()).append(getSubject11(), other.getSubject11()).append(getDeduction12(), other.getDeduction12()).append(getSubject12(), other.getSubject12()).append(getDeduction13(), other.getDeduction13()).append(getSubject13(), other.getSubject13()).append(getDeduction14(), other.getDeduction14()).append(getSubject14(), other.getSubject14()).append(getDeduction15(), other.getDeduction15()).append(getSubject15(), other.getSubject15()).append(getDeduction16(), other.getDeduction16()).append(getSubject16(), other.getSubject16()).append(getDeduction17(), other.getDeduction17()).append(getSubject17(), other.getSubject17()).append(getDeduction18(), other.getDeduction18()).append(getSubject18(), other.getSubject18()).append(getDeduction19(), other.getDeduction19()).append(getSubject19(), other.getSubject19()).append(getDeduction20(), other.getDeduction20()).append(getSubject20(), other.getSubject20()).append(getDeduction21(), other.getDeduction21()).append(getSubject21(), other.getSubject21()).append(getDeduction22(), other.getDeduction22()).append(getSubject22(), other.getSubject22()).append(getDeduction23(), other.getDeduction23()).append(getSubject23(), other.getSubject23()).append(getDeduction24(), other.getDeduction24()).append(getSubject24(), other.getSubject24()).append(getDeduction25(), other.getDeduction25()).append(getSubject25(), other.getSubject25()).append(getBeginTime(), other.getBeginTime()).append(getEndTime(), other.getEndTime()).isEquals();
    }

    public java.lang.String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(java.lang.String ticketTime) {
        this.ticketTime = ticketTime;
    }
}
