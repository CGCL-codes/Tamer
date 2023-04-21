package net.sf.gateway.mef.extract.ty2010;

import java.math.BigInteger;
import net.sf.gateway.mef.utilities.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class FormIN112Extract {

    public static StringBuffer buildFormIN112(Document doc, StringBuffer sb) {
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112Obligations/efil:TotalLocalStateObligations"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112Obligations/efil:VTLocalStateObligations"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112Obligations/efil:NonVTLocalStateObligations"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:QualifiedPlansTax"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:RecaptureFederalInvCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:FedForm4972Tax"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:AdditionsSubTotal"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:VTAdditions"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:RecaptureVTCredits"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeAdditions/efil:TotalAdditionsToVtTax"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:ChildAndDependentCareExp"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:ElderlyDisabledCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:investmentTaxCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:farmIncAveragingCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:VTSubtractionSubtotal"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:SubtractoinsFromVtTax"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:VTBusSolarEnergyCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112IncomeSubtractions/efil:TotalSubtractoinsFromVtTax"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        BigInteger Line1 = new BigInteger("0");
        BigInteger Line2 = new BigInteger("0");
        BigInteger Line3 = new BigInteger("0");
        BigInteger Line4 = new BigInteger("0");
        BigInteger Line5 = new BigInteger("0");
        BigInteger Line6 = new BigInteger("0");
        NodeList n = doc.getElementsByTagName("Sch112OSCR");
        if (n != null && n.getLength() > 0) {
            for (int i = 0; i < n.getLength(); i++) {
                if (n.item(i) != null) {
                    String Line1Str = XMLUtils.getElement(n.item(i), "IncomeTaxableToBoth");
                    if (Line1Str == null || Line1Str.equals("")) {
                        Line1.add(new BigInteger("0"));
                    } else {
                        Line1.add(new BigInteger(Line1Str));
                    }
                    String Line2Str = XMLUtils.getElement(n.item(i), "AdjustedGrossIncome");
                    if (Line2Str == null || Line2Str.equals("")) {
                        Line2.add(new BigInteger("0"));
                    } else {
                        Line2.add(new BigInteger(Line2Str));
                    }
                    String Line3Str = XMLUtils.getElement(n.item(i), "VTIncomeTax");
                    if (Line3Str == null || Line3Str.equals("")) {
                        Line3.add(new BigInteger("0"));
                    } else {
                        Line3.add(new BigInteger(Line3Str));
                    }
                    String Line4Str = XMLUtils.getElement(n.item(i), "ComputedTaxCredit");
                    if (Line4Str == null || Line4Str.equals("")) {
                        Line4.add(new BigInteger("0"));
                    } else {
                        Line4.add(new BigInteger(Line4Str));
                    }
                    String Line5Str = XMLUtils.getElement(n.item(i), "NetTaxPaidToOtherState");
                    if (Line5Str == null || Line5Str.equals("")) {
                        Line5.add(new BigInteger("0"));
                    } else {
                        Line5.add(new BigInteger(Line5Str));
                    }
                    String Line6Str = XMLUtils.getElement(n.item(i), "CreditTaxPaidOtherState");
                    if (Line6Str == null || Line6Str.equals("")) {
                        Line6.add(new BigInteger("0"));
                    } else {
                        Line6.add(new BigInteger(Line5Str));
                    }
                    if (!Line3.toString().equals("0")) {
                        Line1 = (Line6.multiply(Line2)).divide(Line3);
                    }
                }
            }
        }
        sb.append(ExtractUtils.appendFinancialTrailerString(Line1.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(Line2.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(Line3.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(Line4.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(Line5.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(Line6.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:FullYearResident/efil:EICFederalAmt"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:FullYearResident/efil:EarnedIncomeCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:FederalWages"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:VTProtionWages"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:FederalOtherEarnedIncome"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:VermontOtherEarnedIncome"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:FederalTotalEarnedIncome"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:VermontTotalEarnedIncome"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:EICAdjustment"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:EICFederalAmount"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:VTEICCreditSubtotal"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.buildNumberFieldwithFinancialTrailer(doc, "/efil:ReturnState/efil:ReturnDataState/efil:FormIN112/efil:Sch112EIC/efil:PartYearResident/efil:EarnedIncomeCredit"));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        BigInteger sch112DLine1 = new BigInteger("0");
        BigInteger sch112DLine1C = new BigInteger("0");
        BigInteger sch112DLine3A = new BigInteger("0");
        BigInteger sch112DLine3B = new BigInteger("0");
        BigInteger sch112DLine3C = new BigInteger("0");
        BigInteger sch112DLine4A = new BigInteger("0");
        BigInteger sch112DLine4B = new BigInteger("0");
        BigInteger sch112DLine4C = new BigInteger("0");
        BigInteger sch112DLine5A = new BigInteger("0");
        BigInteger sch112DLine5B = new BigInteger("0");
        BigInteger sch112DLine5C = new BigInteger("0");
        BigInteger sch112DLine6 = new BigInteger("0");
        NodeList sch112VtCrdits = doc.getElementsByTagName("Sch112VtCredits");
        if (sch112VtCrdits != null && sch112VtCrdits.getLength() > 0) {
            for (int i = 0; i < sch112VtCrdits.getLength(); i++) {
                if (sch112VtCrdits.item(i) != null) {
                    String sch112DLine1Str = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "VtHighEdInvestment", "Earned");
                    if (sch112DLine1Str == null || sch112DLine1Str.equals("")) {
                        sch112DLine1.add(new BigInteger("0"));
                    } else {
                        sch112DLine1.add(new BigInteger(sch112DLine1Str));
                    }
                    String sch112DLine1CStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "VtHighEdInvestment", "CreditAmount");
                    if (sch112DLine1CStr == null || sch112DLine1CStr.equals("")) {
                        sch112DLine1C.add(new BigInteger("0"));
                    } else {
                        sch112DLine1C.add(new BigInteger(sch112DLine1CStr));
                    }
                    String sch112DLine3AStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "CommercialFilm", "Earned");
                    if (sch112DLine3AStr == null || sch112DLine3AStr.equals("")) {
                        sch112DLine3A.add(new BigInteger("0"));
                    } else {
                        sch112DLine3A.add(new BigInteger(sch112DLine3AStr));
                    }
                    String sch112DLine3BStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "CommercialFilm", "CarryForward");
                    if (sch112DLine3BStr == null || sch112DLine3BStr.equals("")) {
                        sch112DLine3B.add(new BigInteger("0"));
                    } else {
                        sch112DLine3B.add(new BigInteger(sch112DLine3BStr));
                    }
                    String sch112DLine3CStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "CommercialFilm", "CreditAmount");
                    if (sch112DLine3CStr == null || sch112DLine3CStr.equals("")) {
                        sch112DLine3C.add(new BigInteger("0"));
                    } else {
                        sch112DLine3C.add(new BigInteger(sch112DLine3CStr));
                    }
                    String sch112DLine4AStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "CharitableHousing", "Earned");
                    if (sch112DLine4AStr == null || sch112DLine4AStr.equals("")) {
                        sch112DLine4A.add(new BigInteger("0"));
                    } else {
                        sch112DLine4A.add(new BigInteger(sch112DLine4AStr));
                    }
                    String sch112DLine4BStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "CharitableHousing", "CarryForward");
                    if (sch112DLine4BStr == null || sch112DLine4BStr.equals("")) {
                        sch112DLine4B.add(new BigInteger("0"));
                    } else {
                        sch112DLine4B.add(new BigInteger(sch112DLine4BStr));
                    }
                    String sch112DLine4CStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "CharitableHousing", "CreditAmount");
                    if (sch112DLine4CStr == null || sch112DLine4CStr.equals("")) {
                        sch112DLine4C.add(new BigInteger("0"));
                    } else {
                        sch112DLine4C.add(new BigInteger(sch112DLine3CStr));
                    }
                    String sch112DLine5AStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "QualifiedSaleMobileHomePark", "Earned");
                    if (sch112DLine5AStr == null || sch112DLine5AStr.equals("")) {
                        sch112DLine5A.add(new BigInteger("0"));
                    } else {
                        sch112DLine5A.add(new BigInteger(sch112DLine5AStr));
                    }
                    String sch112DLine5BStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "QualifiedSaleMobileHomePark", "CarryForward");
                    if (sch112DLine5BStr == null || sch112DLine5BStr.equals("")) {
                        sch112DLine5B.add(new BigInteger("0"));
                    } else {
                        sch112DLine5B.add(new BigInteger(sch112DLine5BStr));
                    }
                    String sch112DLine5CStr = XMLUtils.getDoubleElement(sch112VtCrdits.item(i), "QualifiedSaleMobileHomePark", "CreditAmount");
                    if (sch112DLine5CStr == null || sch112DLine5CStr.equals("")) {
                        sch112DLine5C.add(new BigInteger("0"));
                    } else {
                        sch112DLine5C.add(new BigInteger(sch112DLine5CStr));
                    }
                    String sch112DLine6Str = XMLUtils.getElement(n.item(i), "CreditTotal");
                    if (sch112DLine6Str == null || sch112DLine6Str.equals("")) {
                        sch112DLine6.add(new BigInteger("0"));
                    } else {
                        sch112DLine6.add(new BigInteger(sch112DLine6Str));
                    }
                }
            }
        }
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine1.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine1C.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine3A.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine3B.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine3C.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine4A.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine4B.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine4C.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine5A.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine5B.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine5C.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        sb.append(ExtractUtils.appendFinancialTrailerString(sch112DLine6.toString()));
        sb.append(VTConstants.SUBMISSION_DETAIL_DELIMITER);
        return sb;
    }
}
