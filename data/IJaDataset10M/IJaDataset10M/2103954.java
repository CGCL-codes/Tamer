package org.quickfix.fix43;

import org.quickfix.FieldNotFound;
import org.quickfix.Group;
import org.quickfix.field.*;

public class SecurityListRequest extends Message {

    public SecurityListRequest() {
        getHeader().setField(new MsgType("x"));
    }

    public SecurityListRequest(org.quickfix.field.SecurityReqID aSecurityReqID, org.quickfix.field.SecurityListRequestType aSecurityListRequestType) {
        getHeader().setField(new MsgType("x"));
        set(aSecurityReqID);
        set(aSecurityListRequestType);
    }

    public void set(org.quickfix.field.SecurityReqID value) {
        setField(value);
    }

    public org.quickfix.field.SecurityReqID get(org.quickfix.field.SecurityReqID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityReqID getSecurityReqID() throws FieldNotFound {
        org.quickfix.field.SecurityReqID value = new org.quickfix.field.SecurityReqID();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityReqID field) {
        return isSetField(field);
    }

    public boolean isSetSecurityReqID() {
        return isSetField(320);
    }

    public void set(org.quickfix.field.SecurityListRequestType value) {
        setField(value);
    }

    public org.quickfix.field.SecurityListRequestType get(org.quickfix.field.SecurityListRequestType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityListRequestType getSecurityListRequestType() throws FieldNotFound {
        org.quickfix.field.SecurityListRequestType value = new org.quickfix.field.SecurityListRequestType();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityListRequestType field) {
        return isSetField(field);
    }

    public boolean isSetSecurityListRequestType() {
        return isSetField(559);
    }

    public void set(org.quickfix.field.Symbol value) {
        setField(value);
    }

    public org.quickfix.field.Symbol get(org.quickfix.field.Symbol value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.Symbol getSymbol() throws FieldNotFound {
        org.quickfix.field.Symbol value = new org.quickfix.field.Symbol();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.Symbol field) {
        return isSetField(field);
    }

    public boolean isSetSymbol() {
        return isSetField(55);
    }

    public void set(org.quickfix.field.SymbolSfx value) {
        setField(value);
    }

    public org.quickfix.field.SymbolSfx get(org.quickfix.field.SymbolSfx value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SymbolSfx getSymbolSfx() throws FieldNotFound {
        org.quickfix.field.SymbolSfx value = new org.quickfix.field.SymbolSfx();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SymbolSfx field) {
        return isSetField(field);
    }

    public boolean isSetSymbolSfx() {
        return isSetField(65);
    }

    public void set(org.quickfix.field.SecurityID value) {
        setField(value);
    }

    public org.quickfix.field.SecurityID get(org.quickfix.field.SecurityID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityID getSecurityID() throws FieldNotFound {
        org.quickfix.field.SecurityID value = new org.quickfix.field.SecurityID();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityID field) {
        return isSetField(field);
    }

    public boolean isSetSecurityID() {
        return isSetField(48);
    }

    public void set(org.quickfix.field.SecurityIDSource value) {
        setField(value);
    }

    public org.quickfix.field.SecurityIDSource get(org.quickfix.field.SecurityIDSource value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityIDSource getSecurityIDSource() throws FieldNotFound {
        org.quickfix.field.SecurityIDSource value = new org.quickfix.field.SecurityIDSource();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityIDSource field) {
        return isSetField(field);
    }

    public boolean isSetSecurityIDSource() {
        return isSetField(22);
    }

    public void set(org.quickfix.field.Product value) {
        setField(value);
    }

    public org.quickfix.field.Product get(org.quickfix.field.Product value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.Product getProduct() throws FieldNotFound {
        org.quickfix.field.Product value = new org.quickfix.field.Product();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.Product field) {
        return isSetField(field);
    }

    public boolean isSetProduct() {
        return isSetField(460);
    }

    public void set(org.quickfix.field.CFICode value) {
        setField(value);
    }

    public org.quickfix.field.CFICode get(org.quickfix.field.CFICode value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.CFICode getCFICode() throws FieldNotFound {
        org.quickfix.field.CFICode value = new org.quickfix.field.CFICode();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.CFICode field) {
        return isSetField(field);
    }

    public boolean isSetCFICode() {
        return isSetField(461);
    }

    public void set(org.quickfix.field.SecurityType value) {
        setField(value);
    }

    public org.quickfix.field.SecurityType get(org.quickfix.field.SecurityType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityType getSecurityType() throws FieldNotFound {
        org.quickfix.field.SecurityType value = new org.quickfix.field.SecurityType();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityType field) {
        return isSetField(field);
    }

    public boolean isSetSecurityType() {
        return isSetField(167);
    }

    public void set(org.quickfix.field.MaturityMonthYear value) {
        setField(value);
    }

    public org.quickfix.field.MaturityMonthYear get(org.quickfix.field.MaturityMonthYear value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.MaturityMonthYear getMaturityMonthYear() throws FieldNotFound {
        org.quickfix.field.MaturityMonthYear value = new org.quickfix.field.MaturityMonthYear();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.MaturityMonthYear field) {
        return isSetField(field);
    }

    public boolean isSetMaturityMonthYear() {
        return isSetField(200);
    }

    public void set(org.quickfix.field.MaturityDate value) {
        setField(value);
    }

    public org.quickfix.field.MaturityDate get(org.quickfix.field.MaturityDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.MaturityDate getMaturityDate() throws FieldNotFound {
        org.quickfix.field.MaturityDate value = new org.quickfix.field.MaturityDate();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.MaturityDate field) {
        return isSetField(field);
    }

    public boolean isSetMaturityDate() {
        return isSetField(541);
    }

    public void set(org.quickfix.field.CouponPaymentDate value) {
        setField(value);
    }

    public org.quickfix.field.CouponPaymentDate get(org.quickfix.field.CouponPaymentDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.CouponPaymentDate getCouponPaymentDate() throws FieldNotFound {
        org.quickfix.field.CouponPaymentDate value = new org.quickfix.field.CouponPaymentDate();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.CouponPaymentDate field) {
        return isSetField(field);
    }

    public boolean isSetCouponPaymentDate() {
        return isSetField(224);
    }

    public void set(org.quickfix.field.IssueDate value) {
        setField(value);
    }

    public org.quickfix.field.IssueDate get(org.quickfix.field.IssueDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.IssueDate getIssueDate() throws FieldNotFound {
        org.quickfix.field.IssueDate value = new org.quickfix.field.IssueDate();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.IssueDate field) {
        return isSetField(field);
    }

    public boolean isSetIssueDate() {
        return isSetField(225);
    }

    public void set(org.quickfix.field.RepoCollateralSecurityType value) {
        setField(value);
    }

    public org.quickfix.field.RepoCollateralSecurityType get(org.quickfix.field.RepoCollateralSecurityType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.RepoCollateralSecurityType getRepoCollateralSecurityType() throws FieldNotFound {
        org.quickfix.field.RepoCollateralSecurityType value = new org.quickfix.field.RepoCollateralSecurityType();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.RepoCollateralSecurityType field) {
        return isSetField(field);
    }

    public boolean isSetRepoCollateralSecurityType() {
        return isSetField(239);
    }

    public void set(org.quickfix.field.RepurchaseTerm value) {
        setField(value);
    }

    public org.quickfix.field.RepurchaseTerm get(org.quickfix.field.RepurchaseTerm value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.RepurchaseTerm getRepurchaseTerm() throws FieldNotFound {
        org.quickfix.field.RepurchaseTerm value = new org.quickfix.field.RepurchaseTerm();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.RepurchaseTerm field) {
        return isSetField(field);
    }

    public boolean isSetRepurchaseTerm() {
        return isSetField(226);
    }

    public void set(org.quickfix.field.RepurchaseRate value) {
        setField(value);
    }

    public org.quickfix.field.RepurchaseRate get(org.quickfix.field.RepurchaseRate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.RepurchaseRate getRepurchaseRate() throws FieldNotFound {
        org.quickfix.field.RepurchaseRate value = new org.quickfix.field.RepurchaseRate();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.RepurchaseRate field) {
        return isSetField(field);
    }

    public boolean isSetRepurchaseRate() {
        return isSetField(227);
    }

    public void set(org.quickfix.field.Factor value) {
        setField(value);
    }

    public org.quickfix.field.Factor get(org.quickfix.field.Factor value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.Factor getFactor() throws FieldNotFound {
        org.quickfix.field.Factor value = new org.quickfix.field.Factor();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.Factor field) {
        return isSetField(field);
    }

    public boolean isSetFactor() {
        return isSetField(228);
    }

    public void set(org.quickfix.field.CreditRating value) {
        setField(value);
    }

    public org.quickfix.field.CreditRating get(org.quickfix.field.CreditRating value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.CreditRating getCreditRating() throws FieldNotFound {
        org.quickfix.field.CreditRating value = new org.quickfix.field.CreditRating();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.CreditRating field) {
        return isSetField(field);
    }

    public boolean isSetCreditRating() {
        return isSetField(255);
    }

    public void set(org.quickfix.field.InstrRegistry value) {
        setField(value);
    }

    public org.quickfix.field.InstrRegistry get(org.quickfix.field.InstrRegistry value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.InstrRegistry getInstrRegistry() throws FieldNotFound {
        org.quickfix.field.InstrRegistry value = new org.quickfix.field.InstrRegistry();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.InstrRegistry field) {
        return isSetField(field);
    }

    public boolean isSetInstrRegistry() {
        return isSetField(543);
    }

    public void set(org.quickfix.field.CountryOfIssue value) {
        setField(value);
    }

    public org.quickfix.field.CountryOfIssue get(org.quickfix.field.CountryOfIssue value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.CountryOfIssue getCountryOfIssue() throws FieldNotFound {
        org.quickfix.field.CountryOfIssue value = new org.quickfix.field.CountryOfIssue();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.CountryOfIssue field) {
        return isSetField(field);
    }

    public boolean isSetCountryOfIssue() {
        return isSetField(470);
    }

    public void set(org.quickfix.field.StateOrProvinceOfIssue value) {
        setField(value);
    }

    public org.quickfix.field.StateOrProvinceOfIssue get(org.quickfix.field.StateOrProvinceOfIssue value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.StateOrProvinceOfIssue getStateOrProvinceOfIssue() throws FieldNotFound {
        org.quickfix.field.StateOrProvinceOfIssue value = new org.quickfix.field.StateOrProvinceOfIssue();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.StateOrProvinceOfIssue field) {
        return isSetField(field);
    }

    public boolean isSetStateOrProvinceOfIssue() {
        return isSetField(471);
    }

    public void set(org.quickfix.field.LocaleOfIssue value) {
        setField(value);
    }

    public org.quickfix.field.LocaleOfIssue get(org.quickfix.field.LocaleOfIssue value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.LocaleOfIssue getLocaleOfIssue() throws FieldNotFound {
        org.quickfix.field.LocaleOfIssue value = new org.quickfix.field.LocaleOfIssue();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.LocaleOfIssue field) {
        return isSetField(field);
    }

    public boolean isSetLocaleOfIssue() {
        return isSetField(472);
    }

    public void set(org.quickfix.field.RedemptionDate value) {
        setField(value);
    }

    public org.quickfix.field.RedemptionDate get(org.quickfix.field.RedemptionDate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.RedemptionDate getRedemptionDate() throws FieldNotFound {
        org.quickfix.field.RedemptionDate value = new org.quickfix.field.RedemptionDate();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.RedemptionDate field) {
        return isSetField(field);
    }

    public boolean isSetRedemptionDate() {
        return isSetField(240);
    }

    public void set(org.quickfix.field.StrikePrice value) {
        setField(value);
    }

    public org.quickfix.field.StrikePrice get(org.quickfix.field.StrikePrice value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.StrikePrice getStrikePrice() throws FieldNotFound {
        org.quickfix.field.StrikePrice value = new org.quickfix.field.StrikePrice();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.StrikePrice field) {
        return isSetField(field);
    }

    public boolean isSetStrikePrice() {
        return isSetField(202);
    }

    public void set(org.quickfix.field.OptAttribute value) {
        setField(value);
    }

    public org.quickfix.field.OptAttribute get(org.quickfix.field.OptAttribute value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.OptAttribute getOptAttribute() throws FieldNotFound {
        org.quickfix.field.OptAttribute value = new org.quickfix.field.OptAttribute();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.OptAttribute field) {
        return isSetField(field);
    }

    public boolean isSetOptAttribute() {
        return isSetField(206);
    }

    public void set(org.quickfix.field.ContractMultiplier value) {
        setField(value);
    }

    public org.quickfix.field.ContractMultiplier get(org.quickfix.field.ContractMultiplier value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.ContractMultiplier getContractMultiplier() throws FieldNotFound {
        org.quickfix.field.ContractMultiplier value = new org.quickfix.field.ContractMultiplier();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.ContractMultiplier field) {
        return isSetField(field);
    }

    public boolean isSetContractMultiplier() {
        return isSetField(231);
    }

    public void set(org.quickfix.field.CouponRate value) {
        setField(value);
    }

    public org.quickfix.field.CouponRate get(org.quickfix.field.CouponRate value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.CouponRate getCouponRate() throws FieldNotFound {
        org.quickfix.field.CouponRate value = new org.quickfix.field.CouponRate();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.CouponRate field) {
        return isSetField(field);
    }

    public boolean isSetCouponRate() {
        return isSetField(223);
    }

    public void set(org.quickfix.field.SecurityExchange value) {
        setField(value);
    }

    public org.quickfix.field.SecurityExchange get(org.quickfix.field.SecurityExchange value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityExchange getSecurityExchange() throws FieldNotFound {
        org.quickfix.field.SecurityExchange value = new org.quickfix.field.SecurityExchange();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityExchange field) {
        return isSetField(field);
    }

    public boolean isSetSecurityExchange() {
        return isSetField(207);
    }

    public void set(org.quickfix.field.Issuer value) {
        setField(value);
    }

    public org.quickfix.field.Issuer get(org.quickfix.field.Issuer value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.Issuer getIssuer() throws FieldNotFound {
        org.quickfix.field.Issuer value = new org.quickfix.field.Issuer();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.Issuer field) {
        return isSetField(field);
    }

    public boolean isSetIssuer() {
        return isSetField(106);
    }

    public void set(org.quickfix.field.EncodedIssuerLen value) {
        setField(value);
    }

    public org.quickfix.field.EncodedIssuerLen get(org.quickfix.field.EncodedIssuerLen value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.EncodedIssuerLen getEncodedIssuerLen() throws FieldNotFound {
        org.quickfix.field.EncodedIssuerLen value = new org.quickfix.field.EncodedIssuerLen();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.EncodedIssuerLen field) {
        return isSetField(field);
    }

    public boolean isSetEncodedIssuerLen() {
        return isSetField(348);
    }

    public void set(org.quickfix.field.EncodedIssuer value) {
        setField(value);
    }

    public org.quickfix.field.EncodedIssuer get(org.quickfix.field.EncodedIssuer value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.EncodedIssuer getEncodedIssuer() throws FieldNotFound {
        org.quickfix.field.EncodedIssuer value = new org.quickfix.field.EncodedIssuer();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.EncodedIssuer field) {
        return isSetField(field);
    }

    public boolean isSetEncodedIssuer() {
        return isSetField(349);
    }

    public void set(org.quickfix.field.SecurityDesc value) {
        setField(value);
    }

    public org.quickfix.field.SecurityDesc get(org.quickfix.field.SecurityDesc value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SecurityDesc getSecurityDesc() throws FieldNotFound {
        org.quickfix.field.SecurityDesc value = new org.quickfix.field.SecurityDesc();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SecurityDesc field) {
        return isSetField(field);
    }

    public boolean isSetSecurityDesc() {
        return isSetField(107);
    }

    public void set(org.quickfix.field.EncodedSecurityDescLen value) {
        setField(value);
    }

    public org.quickfix.field.EncodedSecurityDescLen get(org.quickfix.field.EncodedSecurityDescLen value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.EncodedSecurityDescLen getEncodedSecurityDescLen() throws FieldNotFound {
        org.quickfix.field.EncodedSecurityDescLen value = new org.quickfix.field.EncodedSecurityDescLen();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.EncodedSecurityDescLen field) {
        return isSetField(field);
    }

    public boolean isSetEncodedSecurityDescLen() {
        return isSetField(350);
    }

    public void set(org.quickfix.field.EncodedSecurityDesc value) {
        setField(value);
    }

    public org.quickfix.field.EncodedSecurityDesc get(org.quickfix.field.EncodedSecurityDesc value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.EncodedSecurityDesc getEncodedSecurityDesc() throws FieldNotFound {
        org.quickfix.field.EncodedSecurityDesc value = new org.quickfix.field.EncodedSecurityDesc();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.EncodedSecurityDesc field) {
        return isSetField(field);
    }

    public boolean isSetEncodedSecurityDesc() {
        return isSetField(351);
    }

    public void set(org.quickfix.field.NoSecurityAltID value) {
        setField(value);
    }

    public org.quickfix.field.NoSecurityAltID get(org.quickfix.field.NoSecurityAltID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.NoSecurityAltID getNoSecurityAltID() throws FieldNotFound {
        org.quickfix.field.NoSecurityAltID value = new org.quickfix.field.NoSecurityAltID();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.NoSecurityAltID field) {
        return isSetField(field);
    }

    public boolean isSetNoSecurityAltID() {
        return isSetField(454);
    }

    public static class NoSecurityAltID extends Group {

        public NoSecurityAltID() {
            super(454, 455, new int[] { 455, 456, 0 });
        }

        public void set(org.quickfix.field.SecurityAltID value) {
            setField(value);
        }

        public org.quickfix.field.SecurityAltID get(org.quickfix.field.SecurityAltID value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public org.quickfix.field.SecurityAltID getSecurityAltID() throws FieldNotFound {
            org.quickfix.field.SecurityAltID value = new org.quickfix.field.SecurityAltID();
            getField(value);
            return value;
        }

        public boolean isSet(org.quickfix.field.SecurityAltID field) {
            return isSetField(field);
        }

        public boolean isSetSecurityAltID() {
            return isSetField(455);
        }

        public void set(org.quickfix.field.SecurityAltIDSource value) {
            setField(value);
        }

        public org.quickfix.field.SecurityAltIDSource get(org.quickfix.field.SecurityAltIDSource value) throws FieldNotFound {
            getField(value);
            return value;
        }

        public org.quickfix.field.SecurityAltIDSource getSecurityAltIDSource() throws FieldNotFound {
            org.quickfix.field.SecurityAltIDSource value = new org.quickfix.field.SecurityAltIDSource();
            getField(value);
            return value;
        }

        public boolean isSet(org.quickfix.field.SecurityAltIDSource field) {
            return isSetField(field);
        }

        public boolean isSetSecurityAltIDSource() {
            return isSetField(456);
        }
    }

    public void set(org.quickfix.field.Currency value) {
        setField(value);
    }

    public org.quickfix.field.Currency get(org.quickfix.field.Currency value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.Currency getCurrency() throws FieldNotFound {
        org.quickfix.field.Currency value = new org.quickfix.field.Currency();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.Currency field) {
        return isSetField(field);
    }

    public boolean isSetCurrency() {
        return isSetField(15);
    }

    public void set(org.quickfix.field.Text value) {
        setField(value);
    }

    public org.quickfix.field.Text get(org.quickfix.field.Text value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.Text getText() throws FieldNotFound {
        org.quickfix.field.Text value = new org.quickfix.field.Text();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.Text field) {
        return isSetField(field);
    }

    public boolean isSetText() {
        return isSetField(58);
    }

    public void set(org.quickfix.field.EncodedTextLen value) {
        setField(value);
    }

    public org.quickfix.field.EncodedTextLen get(org.quickfix.field.EncodedTextLen value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.EncodedTextLen getEncodedTextLen() throws FieldNotFound {
        org.quickfix.field.EncodedTextLen value = new org.quickfix.field.EncodedTextLen();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.EncodedTextLen field) {
        return isSetField(field);
    }

    public boolean isSetEncodedTextLen() {
        return isSetField(354);
    }

    public void set(org.quickfix.field.EncodedText value) {
        setField(value);
    }

    public org.quickfix.field.EncodedText get(org.quickfix.field.EncodedText value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.EncodedText getEncodedText() throws FieldNotFound {
        org.quickfix.field.EncodedText value = new org.quickfix.field.EncodedText();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.EncodedText field) {
        return isSetField(field);
    }

    public boolean isSetEncodedText() {
        return isSetField(355);
    }

    public void set(org.quickfix.field.TradingSessionID value) {
        setField(value);
    }

    public org.quickfix.field.TradingSessionID get(org.quickfix.field.TradingSessionID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.TradingSessionID getTradingSessionID() throws FieldNotFound {
        org.quickfix.field.TradingSessionID value = new org.quickfix.field.TradingSessionID();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.TradingSessionID field) {
        return isSetField(field);
    }

    public boolean isSetTradingSessionID() {
        return isSetField(336);
    }

    public void set(org.quickfix.field.TradingSessionSubID value) {
        setField(value);
    }

    public org.quickfix.field.TradingSessionSubID get(org.quickfix.field.TradingSessionSubID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.TradingSessionSubID getTradingSessionSubID() throws FieldNotFound {
        org.quickfix.field.TradingSessionSubID value = new org.quickfix.field.TradingSessionSubID();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.TradingSessionSubID field) {
        return isSetField(field);
    }

    public boolean isSetTradingSessionSubID() {
        return isSetField(625);
    }

    public void set(org.quickfix.field.SubscriptionRequestType value) {
        setField(value);
    }

    public org.quickfix.field.SubscriptionRequestType get(org.quickfix.field.SubscriptionRequestType value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.SubscriptionRequestType getSubscriptionRequestType() throws FieldNotFound {
        org.quickfix.field.SubscriptionRequestType value = new org.quickfix.field.SubscriptionRequestType();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.SubscriptionRequestType field) {
        return isSetField(field);
    }

    public boolean isSetSubscriptionRequestType() {
        return isSetField(263);
    }
}
