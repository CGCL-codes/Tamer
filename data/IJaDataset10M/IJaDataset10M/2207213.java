package com.ark.fix.model.fix;

import com.ark.fix.model.*;
import java.io.*;
import java.util.*;

public class SecurityDef extends FIXMessage {

    public static final int TAG_SecurityReqID = 320;

    public static final String FIELD_SecurityReqID = "SecurityReqID";

    public static final int TAG_SecurityResponseID = 322;

    public static final String FIELD_SecurityResponseID = "SecurityResponseID";

    public static final int TAG_SecurityResponseType = 323;

    public static final String FIELD_SecurityResponseType = "SecurityResponseType";

    public static final int TAG_Symbol = 55;

    public static final String FIELD_Symbol = "Symbol";

    public static final int TAG_SymbolSfx = 65;

    public static final String FIELD_SymbolSfx = "SymbolSfx";

    public static final int TAG_SecurityID = 48;

    public static final String FIELD_SecurityID = "SecurityID";

    public static final int TAG_IDSource = 22;

    public static final String FIELD_IDSource = "IDSource";

    public static final int TAG_SecurityType = 167;

    public static final String FIELD_SecurityType = "SecurityType";

    public static final int TAG_MaturityMonthYear = 200;

    public static final String FIELD_MaturityMonthYear = "MaturityMonthYear";

    public static final int TAG_MaturityDay = 205;

    public static final String FIELD_MaturityDay = "MaturityDay";

    public static final int TAG_PutOrCall = 201;

    public static final String FIELD_PutOrCall = "PutOrCall";

    public static final int TAG_StrikePrice = 202;

    public static final String FIELD_StrikePrice = "StrikePrice";

    public static final int TAG_OptAttribute = 206;

    public static final String FIELD_OptAttribute = "OptAttribute";

    public static final int TAG_ContractMultiplier = 231;

    public static final String FIELD_ContractMultiplier = "ContractMultiplier";

    public static final int TAG_CouponRate = 223;

    public static final String FIELD_CouponRate = "CouponRate";

    public static final int TAG_SecurityExchange = 207;

    public static final String FIELD_SecurityExchange = "SecurityExchange";

    public static final int TAG_Issuer = 106;

    public static final String FIELD_Issuer = "Issuer";

    public static final int TAG_EncodedIssuerLen = 348;

    public static final String FIELD_EncodedIssuerLen = "EncodedIssuerLen";

    public static final int TAG_EncodedIssuer = 349;

    public static final String FIELD_EncodedIssuer = "EncodedIssuer";

    public static final int TAG_SecurityDesc = 107;

    public static final String FIELD_SecurityDesc = "SecurityDesc";

    public static final int TAG_EncodedSecurityDescLen = 350;

    public static final String FIELD_EncodedSecurityDescLen = "EncodedSecurityDescLen";

    public static final int TAG_EncodedSecurityDesc = 351;

    public static final String FIELD_EncodedSecurityDesc = "EncodedSecurityDesc";

    public static final int TAG_Currency = 15;

    public static final String FIELD_Currency = "Currency";

    public static final int TAG_TradingSessionID = 336;

    public static final String FIELD_TradingSessionID = "TradingSessionID";

    public static final int TAG_Text = 58;

    public static final String FIELD_Text = "Text";

    public static final int TAG_EncodedTextLen = 354;

    public static final String FIELD_EncodedTextLen = "EncodedTextLen";

    public static final int TAG_EncodedText = 355;

    public static final String FIELD_EncodedText = "EncodedText";

    public static final int TAG_NoRelatedSym = 146;

    public static final String FIELD_NoRelatedSym = "NoRelatedSym";

    public static final int TAG_RelatedSymSeq = -1;

    public static final String FIELD_RelatedSymSeq = "RelatedSymSeq";

    protected String _SecurityReqID;

    protected String _SecurityResponseID;

    protected String _SecurityResponseType;

    protected String _Symbol;

    protected String _SymbolSfx;

    protected String _SecurityID;

    protected String _IDSource;

    protected String _SecurityType;

    protected String _MaturityMonthYear;

    protected String _MaturityDay;

    protected String _PutOrCall;

    protected String _StrikePrice;

    protected String _OptAttribute;

    protected String _ContractMultiplier;

    protected String _CouponRate;

    protected String _SecurityExchange;

    protected String _Issuer;

    protected String _EncodedIssuerLen;

    protected String _EncodedIssuer;

    protected String _SecurityDesc;

    protected String _EncodedSecurityDescLen;

    protected String _EncodedSecurityDesc;

    protected String _Currency;

    protected String _TradingSessionID;

    protected String _Text;

    protected String _EncodedTextLen;

    protected String _EncodedText;

    protected String _NoRelatedSym;

    protected FIXObjSeq _RelatedSymSeq;

    public String getSecurityReqID() {
        return _SecurityReqID;
    }

    public void setSecurityReqID(String s) {
        _SecurityReqID = s;
    }

    public String getSecurityReqIDJ() throws ModelException {
        if (_SecurityReqID == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _SecurityReqID);
    }

    public void setSecurityReqIDJ(String obj) throws ModelException {
        if (obj == null) {
            _SecurityReqID = null;
            return;
        }
        _SecurityReqID = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getSecurityResponseID() {
        return _SecurityResponseID;
    }

    public void setSecurityResponseID(String s) {
        _SecurityResponseID = s;
    }

    public String getSecurityResponseIDJ() throws ModelException {
        if (_SecurityResponseID == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _SecurityResponseID);
    }

    public void setSecurityResponseIDJ(String obj) throws ModelException {
        if (obj == null) {
            _SecurityResponseID = null;
            return;
        }
        _SecurityResponseID = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getSecurityResponseType() {
        return _SecurityResponseType;
    }

    public void setSecurityResponseType(String s) {
        _SecurityResponseType = s;
    }

    public Long getSecurityResponseTypeJ() throws ModelException {
        if (_SecurityResponseType == null) return null;
        return (Long) FIXDataConverter.getNativeJavaData(FIXDataConverter.INT, _SecurityResponseType);
    }

    public void setSecurityResponseTypeJ(Long obj) throws ModelException {
        if (obj == null) {
            _SecurityResponseType = null;
            return;
        }
        _SecurityResponseType = FIXDataConverter.getNativeFIXString(FIXDataConverter.INT, obj);
    }

    public String getSymbol() {
        return _Symbol;
    }

    public void setSymbol(String s) {
        _Symbol = s;
    }

    public String getSymbolJ() throws ModelException {
        if (_Symbol == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _Symbol);
    }

    public void setSymbolJ(String obj) throws ModelException {
        if (obj == null) {
            _Symbol = null;
            return;
        }
        _Symbol = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getSymbolSfx() {
        return _SymbolSfx;
    }

    public void setSymbolSfx(String s) {
        _SymbolSfx = s;
    }

    public String getSymbolSfxJ() throws ModelException {
        if (_SymbolSfx == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _SymbolSfx);
    }

    public void setSymbolSfxJ(String obj) throws ModelException {
        if (obj == null) {
            _SymbolSfx = null;
            return;
        }
        _SymbolSfx = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getSecurityID() {
        return _SecurityID;
    }

    public void setSecurityID(String s) {
        _SecurityID = s;
    }

    public String getSecurityIDJ() throws ModelException {
        if (_SecurityID == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _SecurityID);
    }

    public void setSecurityIDJ(String obj) throws ModelException {
        if (obj == null) {
            _SecurityID = null;
            return;
        }
        _SecurityID = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getIDSource() {
        return _IDSource;
    }

    public void setIDSource(String s) {
        _IDSource = s;
    }

    public String getIDSourceJ() throws ModelException {
        if (_IDSource == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _IDSource);
    }

    public void setIDSourceJ(String obj) throws ModelException {
        if (obj == null) {
            _IDSource = null;
            return;
        }
        _IDSource = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getSecurityType() {
        return _SecurityType;
    }

    public void setSecurityType(String s) {
        _SecurityType = s;
    }

    public String getSecurityTypeJ() throws ModelException {
        if (_SecurityType == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _SecurityType);
    }

    public void setSecurityTypeJ(String obj) throws ModelException {
        if (obj == null) {
            _SecurityType = null;
            return;
        }
        _SecurityType = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getMaturityMonthYear() {
        return _MaturityMonthYear;
    }

    public void setMaturityMonthYear(String s) {
        _MaturityMonthYear = s;
    }

    public Date getMaturityMonthYearJ() throws ModelException {
        if (_MaturityMonthYear == null) return null;
        return (Date) FIXDataConverter.getNativeJavaData(FIXDataConverter.MONTHYEAR, _MaturityMonthYear);
    }

    public void setMaturityMonthYearJ(Date obj) throws ModelException {
        if (obj == null) {
            _MaturityMonthYear = null;
            return;
        }
        _MaturityMonthYear = FIXDataConverter.getNativeFIXString(FIXDataConverter.MONTHYEAR, obj);
    }

    public String getMaturityDay() {
        return _MaturityDay;
    }

    public void setMaturityDay(String s) {
        _MaturityDay = s;
    }

    public Date getMaturityDayJ() throws ModelException {
        if (_MaturityDay == null) return null;
        return (Date) FIXDataConverter.getNativeJavaData(FIXDataConverter.DAYOFMONTH, _MaturityDay);
    }

    public void setMaturityDayJ(Date obj) throws ModelException {
        if (obj == null) {
            _MaturityDay = null;
            return;
        }
        _MaturityDay = FIXDataConverter.getNativeFIXString(FIXDataConverter.DAYOFMONTH, obj);
    }

    public String getPutOrCall() {
        return _PutOrCall;
    }

    public void setPutOrCall(String s) {
        _PutOrCall = s;
    }

    public Long getPutOrCallJ() throws ModelException {
        if (_PutOrCall == null) return null;
        return (Long) FIXDataConverter.getNativeJavaData(FIXDataConverter.INT, _PutOrCall);
    }

    public void setPutOrCallJ(Long obj) throws ModelException {
        if (obj == null) {
            _PutOrCall = null;
            return;
        }
        _PutOrCall = FIXDataConverter.getNativeFIXString(FIXDataConverter.INT, obj);
    }

    public String getStrikePrice() {
        return _StrikePrice;
    }

    public void setStrikePrice(String s) {
        _StrikePrice = s;
    }

    public Double getStrikePriceJ() throws ModelException {
        if (_StrikePrice == null) return null;
        return (Double) FIXDataConverter.getNativeJavaData(FIXDataConverter.PRICE, _StrikePrice);
    }

    public void setStrikePriceJ(Double obj) throws ModelException {
        if (obj == null) {
            _StrikePrice = null;
            return;
        }
        _StrikePrice = FIXDataConverter.getNativeFIXString(FIXDataConverter.PRICE, obj);
    }

    public String getOptAttribute() {
        return _OptAttribute;
    }

    public void setOptAttribute(String s) {
        _OptAttribute = s;
    }

    public Character getOptAttributeJ() throws ModelException {
        if (_OptAttribute == null) return null;
        return (Character) FIXDataConverter.getNativeJavaData(FIXDataConverter.CHAR, _OptAttribute);
    }

    public void setOptAttributeJ(Character obj) throws ModelException {
        if (obj == null) {
            _OptAttribute = null;
            return;
        }
        _OptAttribute = FIXDataConverter.getNativeFIXString(FIXDataConverter.CHAR, obj);
    }

    public String getContractMultiplier() {
        return _ContractMultiplier;
    }

    public void setContractMultiplier(String s) {
        _ContractMultiplier = s;
    }

    public Double getContractMultiplierJ() throws ModelException {
        if (_ContractMultiplier == null) return null;
        return (Double) FIXDataConverter.getNativeJavaData(FIXDataConverter.FLOAT, _ContractMultiplier);
    }

    public void setContractMultiplierJ(Double obj) throws ModelException {
        if (obj == null) {
            _ContractMultiplier = null;
            return;
        }
        _ContractMultiplier = FIXDataConverter.getNativeFIXString(FIXDataConverter.FLOAT, obj);
    }

    public String getCouponRate() {
        return _CouponRate;
    }

    public void setCouponRate(String s) {
        _CouponRate = s;
    }

    public Double getCouponRateJ() throws ModelException {
        if (_CouponRate == null) return null;
        return (Double) FIXDataConverter.getNativeJavaData(FIXDataConverter.FLOAT, _CouponRate);
    }

    public void setCouponRateJ(Double obj) throws ModelException {
        if (obj == null) {
            _CouponRate = null;
            return;
        }
        _CouponRate = FIXDataConverter.getNativeFIXString(FIXDataConverter.FLOAT, obj);
    }

    public String getSecurityExchange() {
        return _SecurityExchange;
    }

    public void setSecurityExchange(String s) {
        _SecurityExchange = s;
    }

    public String getSecurityExchangeJ() throws ModelException {
        if (_SecurityExchange == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.EXCHANGE, _SecurityExchange);
    }

    public void setSecurityExchangeJ(String obj) throws ModelException {
        if (obj == null) {
            _SecurityExchange = null;
            return;
        }
        _SecurityExchange = FIXDataConverter.getNativeFIXString(FIXDataConverter.EXCHANGE, obj);
    }

    public String getIssuer() {
        return _Issuer;
    }

    public void setIssuer(String s) {
        _Issuer = s;
    }

    public String getIssuerJ() throws ModelException {
        if (_Issuer == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _Issuer);
    }

    public void setIssuerJ(String obj) throws ModelException {
        if (obj == null) {
            _Issuer = null;
            return;
        }
        _Issuer = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getEncodedIssuerLen() {
        return _EncodedIssuerLen;
    }

    public void setEncodedIssuerLen(String s) {
        _EncodedIssuerLen = s;
    }

    public Long getEncodedIssuerLenJ() throws ModelException {
        if (_EncodedIssuerLen == null) return null;
        return (Long) FIXDataConverter.getNativeJavaData(FIXDataConverter.INT, _EncodedIssuerLen);
    }

    public void setEncodedIssuerLenJ(Long obj) throws ModelException {
        if (obj == null) {
            _EncodedIssuerLen = null;
            return;
        }
        _EncodedIssuerLen = FIXDataConverter.getNativeFIXString(FIXDataConverter.INT, obj);
    }

    public String getEncodedIssuer() {
        return _EncodedIssuer;
    }

    public void setEncodedIssuer(String s) {
        _EncodedIssuer = s;
    }

    public byte[] getEncodedIssuerJ() throws ModelException {
        if (_EncodedIssuer == null) return null;
        return (byte[]) FIXDataConverter.getNativeJavaData(FIXDataConverter.DATA, _EncodedIssuer);
    }

    public void setEncodedIssuerJ(byte[] obj) throws ModelException {
        if (obj == null) {
            _EncodedIssuer = null;
            return;
        }
        _EncodedIssuer = FIXDataConverter.getNativeFIXString(FIXDataConverter.DATA, obj);
    }

    public String getSecurityDesc() {
        return _SecurityDesc;
    }

    public void setSecurityDesc(String s) {
        _SecurityDesc = s;
    }

    public String getSecurityDescJ() throws ModelException {
        if (_SecurityDesc == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _SecurityDesc);
    }

    public void setSecurityDescJ(String obj) throws ModelException {
        if (obj == null) {
            _SecurityDesc = null;
            return;
        }
        _SecurityDesc = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getEncodedSecurityDescLen() {
        return _EncodedSecurityDescLen;
    }

    public void setEncodedSecurityDescLen(String s) {
        _EncodedSecurityDescLen = s;
    }

    public Long getEncodedSecurityDescLenJ() throws ModelException {
        if (_EncodedSecurityDescLen == null) return null;
        return (Long) FIXDataConverter.getNativeJavaData(FIXDataConverter.INT, _EncodedSecurityDescLen);
    }

    public void setEncodedSecurityDescLenJ(Long obj) throws ModelException {
        if (obj == null) {
            _EncodedSecurityDescLen = null;
            return;
        }
        _EncodedSecurityDescLen = FIXDataConverter.getNativeFIXString(FIXDataConverter.INT, obj);
    }

    public String getEncodedSecurityDesc() {
        return _EncodedSecurityDesc;
    }

    public void setEncodedSecurityDesc(String s) {
        _EncodedSecurityDesc = s;
    }

    public byte[] getEncodedSecurityDescJ() throws ModelException {
        if (_EncodedSecurityDesc == null) return null;
        return (byte[]) FIXDataConverter.getNativeJavaData(FIXDataConverter.DATA, _EncodedSecurityDesc);
    }

    public void setEncodedSecurityDescJ(byte[] obj) throws ModelException {
        if (obj == null) {
            _EncodedSecurityDesc = null;
            return;
        }
        _EncodedSecurityDesc = FIXDataConverter.getNativeFIXString(FIXDataConverter.DATA, obj);
    }

    public String getCurrency() {
        return _Currency;
    }

    public void setCurrency(String s) {
        _Currency = s;
    }

    public String getCurrencyJ() throws ModelException {
        if (_Currency == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.CURRENCY, _Currency);
    }

    public void setCurrencyJ(String obj) throws ModelException {
        if (obj == null) {
            _Currency = null;
            return;
        }
        _Currency = FIXDataConverter.getNativeFIXString(FIXDataConverter.CURRENCY, obj);
    }

    public String getTradingSessionID() {
        return _TradingSessionID;
    }

    public void setTradingSessionID(String s) {
        _TradingSessionID = s;
    }

    public String getTradingSessionIDJ() throws ModelException {
        if (_TradingSessionID == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _TradingSessionID);
    }

    public void setTradingSessionIDJ(String obj) throws ModelException {
        if (obj == null) {
            _TradingSessionID = null;
            return;
        }
        _TradingSessionID = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getText() {
        return _Text;
    }

    public void setText(String s) {
        _Text = s;
    }

    public String getTextJ() throws ModelException {
        if (_Text == null) return null;
        return (String) FIXDataConverter.getNativeJavaData(FIXDataConverter.STRING, _Text);
    }

    public void setTextJ(String obj) throws ModelException {
        if (obj == null) {
            _Text = null;
            return;
        }
        _Text = FIXDataConverter.getNativeFIXString(FIXDataConverter.STRING, obj);
    }

    public String getEncodedTextLen() {
        return _EncodedTextLen;
    }

    public void setEncodedTextLen(String s) {
        _EncodedTextLen = s;
    }

    public Long getEncodedTextLenJ() throws ModelException {
        if (_EncodedTextLen == null) return null;
        return (Long) FIXDataConverter.getNativeJavaData(FIXDataConverter.INT, _EncodedTextLen);
    }

    public void setEncodedTextLenJ(Long obj) throws ModelException {
        if (obj == null) {
            _EncodedTextLen = null;
            return;
        }
        _EncodedTextLen = FIXDataConverter.getNativeFIXString(FIXDataConverter.INT, obj);
    }

    public String getEncodedText() {
        return _EncodedText;
    }

    public void setEncodedText(String s) {
        _EncodedText = s;
    }

    public byte[] getEncodedTextJ() throws ModelException {
        if (_EncodedText == null) return null;
        return (byte[]) FIXDataConverter.getNativeJavaData(FIXDataConverter.DATA, _EncodedText);
    }

    public void setEncodedTextJ(byte[] obj) throws ModelException {
        if (obj == null) {
            _EncodedText = null;
            return;
        }
        _EncodedText = FIXDataConverter.getNativeFIXString(FIXDataConverter.DATA, obj);
    }

    public String getNoRelatedSym() {
        return _NoRelatedSym;
    }

    public void setNoRelatedSym(String s) {
        _NoRelatedSym = s;
    }

    public Long getNoRelatedSymJ() throws ModelException {
        if (_NoRelatedSym == null) return null;
        return (Long) FIXDataConverter.getNativeJavaData(FIXDataConverter.INT, _NoRelatedSym);
    }

    public void setNoRelatedSymJ(Long obj) throws ModelException {
        if (obj == null) {
            _NoRelatedSym = null;
            return;
        }
        _NoRelatedSym = FIXDataConverter.getNativeFIXString(FIXDataConverter.INT, obj);
    }

    public FIXObjSeq getRelatedSymSeq() {
        return _RelatedSymSeq;
    }

    public void setRelatedSymSeq(FIXObjSeq aggregates) {
        _RelatedSymSeq = aggregates;
    }

    public String[] getProperties() {
        String[] properties = { "SecurityReqID", "SecurityResponseID", "SecurityResponseType", "Symbol", "SymbolSfx", "SecurityID", "IDSource", "SecurityType", "MaturityMonthYear", "MaturityDay", "PutOrCall", "StrikePrice", "OptAttribute", "ContractMultiplier", "CouponRate", "SecurityExchange", "Issuer", "EncodedIssuerLen", "EncodedIssuer", "SecurityDesc", "EncodedSecurityDescLen", "EncodedSecurityDesc", "Currency", "TradingSessionID", "Text", "EncodedTextLen", "EncodedText", "NoRelatedSym", "RelatedSymSeq" };
        return properties;
    }

    public String[] getRequiredProperties() {
        String[] properties = { "SecurityReqID", "SecurityResponseID" };
        return properties;
    }

    public String getMessageType() {
        return "d";
    }

    public boolean isValid() {
        if (_SecurityReqID == null) return false;
        if (!FIXDataValidator.isValidSTRING(_SecurityReqID)) return false;
        if (_SecurityResponseID == null) return false;
        if (!FIXDataValidator.isValidSTRING(_SecurityResponseID)) return false;
        if (!FIXDataValidator.isValidINT(_SecurityResponseType)) return false;
        if (_SecurityResponseType.indexOf(";") >= 0 || FIXDataTypeDictionary.ENUM_323.indexOf(_SecurityResponseType) < 0) return false;
        if (!FIXDataValidator.isValidSTRING(_Symbol)) return false;
        if (!FIXDataValidator.isValidSTRING(_SymbolSfx)) return false;
        if (!FIXDataValidator.isValidSTRING(_SecurityID)) return false;
        if (!FIXDataValidator.isValidSTRING(_IDSource)) return false;
        if (_IDSource.indexOf(";") >= 0 || FIXDataTypeDictionary.ENUM_22.indexOf(_IDSource) < 0) return false;
        if (!FIXDataValidator.isValidSTRING(_SecurityType)) return false;
        if (_SecurityType.indexOf(";") >= 0 || FIXDataTypeDictionary.ENUM_167.indexOf(_SecurityType) < 0) return false;
        if (!FIXDataValidator.isValidMONTHYEAR(_MaturityMonthYear)) return false;
        if (!FIXDataValidator.isValidDAYOFMONTH(_MaturityDay)) return false;
        if (_MaturityDay.indexOf(";") >= 0 || FIXDataTypeDictionary.ENUM_205.indexOf(_MaturityDay) < 0) return false;
        if (!FIXDataValidator.isValidINT(_PutOrCall)) return false;
        if (_PutOrCall.indexOf(";") >= 0 || FIXDataTypeDictionary.ENUM_201.indexOf(_PutOrCall) < 0) return false;
        if (!FIXDataValidator.isValidPRICE(_StrikePrice)) return false;
        if (!FIXDataValidator.isValidCHAR(_OptAttribute)) return false;
        if (!FIXDataValidator.isValidFLOAT(_ContractMultiplier)) return false;
        if (!FIXDataValidator.isValidFLOAT(_CouponRate)) return false;
        if (!FIXDataValidator.isValidEXCHANGE(_SecurityExchange)) return false;
        if (!FIXDataValidator.isValidSTRING(_Issuer)) return false;
        if (!FIXDataValidator.isValidINT(_EncodedIssuerLen)) return false;
        if (!FIXDataValidator.isValidDATA(_EncodedIssuer)) return false;
        if (!FIXDataValidator.isValidSTRING(_SecurityDesc)) return false;
        if (!FIXDataValidator.isValidINT(_EncodedSecurityDescLen)) return false;
        if (!FIXDataValidator.isValidDATA(_EncodedSecurityDesc)) return false;
        if (!FIXDataValidator.isValidCURRENCY(_Currency)) return false;
        if (!FIXDataValidator.isValidSTRING(_TradingSessionID)) return false;
        if (!FIXDataValidator.isValidSTRING(_Text)) return false;
        if (!FIXDataValidator.isValidINT(_EncodedTextLen)) return false;
        if (!FIXDataValidator.isValidDATA(_EncodedText)) return false;
        if (!FIXDataValidator.isValidINT(_NoRelatedSym)) return false;
        if (!_RelatedSymSeq.isValid()) return false;
        return true;
    }

    public int setValue(int p_tag, byte[] v_value) throws ModelException {
        if (p_tag == TAG_SecurityReqID) {
            _SecurityReqID = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SecurityResponseID) {
            _SecurityResponseID = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SecurityResponseType) {
            _SecurityResponseType = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_Symbol) {
            _Symbol = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SymbolSfx) {
            _SymbolSfx = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SecurityID) {
            _SecurityID = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_IDSource) {
            _IDSource = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SecurityType) {
            _SecurityType = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_MaturityMonthYear) {
            _MaturityMonthYear = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_MaturityDay) {
            _MaturityDay = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_PutOrCall) {
            _PutOrCall = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_StrikePrice) {
            _StrikePrice = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_OptAttribute) {
            _OptAttribute = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_ContractMultiplier) {
            _ContractMultiplier = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_CouponRate) {
            _CouponRate = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SecurityExchange) {
            _SecurityExchange = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_Issuer) {
            _Issuer = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_EncodedIssuerLen) {
            _EncodedIssuerLen = new String(v_value);
            return DATA_TYPE;
        }
        if (p_tag == TAG_EncodedIssuer) {
            _EncodedIssuer = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_SecurityDesc) {
            _SecurityDesc = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_EncodedSecurityDescLen) {
            _EncodedSecurityDescLen = new String(v_value);
            return DATA_TYPE;
        }
        if (p_tag == TAG_EncodedSecurityDesc) {
            _EncodedSecurityDesc = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_Currency) {
            _Currency = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_TradingSessionID) {
            _TradingSessionID = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_Text) {
            _Text = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_EncodedTextLen) {
            _EncodedTextLen = new String(v_value);
            return DATA_TYPE;
        }
        if (p_tag == TAG_EncodedText) {
            _EncodedText = new String(v_value);
            return NORMAL;
        }
        if (p_tag == TAG_NoRelatedSym) {
            _NoRelatedSym = new String(v_value);
            return START_GROUP;
        }
        return NOT_MEMBER;
    }

    public Stack newGroup(int p_tag, int p_len) {
        if (p_tag == TAG_NoRelatedSym) {
            Stack stk = new Stack();
            _RelatedSymSeq = new FIXObjSeq(SecurityDef_RelatedSym.class);
            for (int i = 0; i < p_len; i++) {
                SecurityDef_RelatedSym child = new SecurityDef_RelatedSym();
                _RelatedSymSeq.add(child);
                stk.push(child);
            }
            return stk;
        }
        return null;
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer();
        if (_SecurityReqID != null) {
            sb.append(String.valueOf(TAG_SecurityReqID) + ES + _SecurityReqID + SOH);
        }
        if (_SecurityResponseID != null) {
            sb.append(String.valueOf(TAG_SecurityResponseID) + ES + _SecurityResponseID + SOH);
        }
        if (_SecurityResponseType != null) {
            sb.append(String.valueOf(TAG_SecurityResponseType) + ES + _SecurityResponseType + SOH);
        }
        if (_Symbol != null) {
            sb.append(String.valueOf(TAG_Symbol) + ES + _Symbol + SOH);
        }
        if (_SymbolSfx != null) {
            sb.append(String.valueOf(TAG_SymbolSfx) + ES + _SymbolSfx + SOH);
        }
        if (_SecurityID != null) {
            sb.append(String.valueOf(TAG_SecurityID) + ES + _SecurityID + SOH);
        }
        if (_IDSource != null) {
            sb.append(String.valueOf(TAG_IDSource) + ES + _IDSource + SOH);
        }
        if (_SecurityType != null) {
            sb.append(String.valueOf(TAG_SecurityType) + ES + _SecurityType + SOH);
        }
        if (_MaturityMonthYear != null) {
            sb.append(String.valueOf(TAG_MaturityMonthYear) + ES + _MaturityMonthYear + SOH);
        }
        if (_MaturityDay != null) {
            sb.append(String.valueOf(TAG_MaturityDay) + ES + _MaturityDay + SOH);
        }
        if (_PutOrCall != null) {
            sb.append(String.valueOf(TAG_PutOrCall) + ES + _PutOrCall + SOH);
        }
        if (_StrikePrice != null) {
            sb.append(String.valueOf(TAG_StrikePrice) + ES + _StrikePrice + SOH);
        }
        if (_OptAttribute != null) {
            sb.append(String.valueOf(TAG_OptAttribute) + ES + _OptAttribute + SOH);
        }
        if (_ContractMultiplier != null) {
            sb.append(String.valueOf(TAG_ContractMultiplier) + ES + _ContractMultiplier + SOH);
        }
        if (_CouponRate != null) {
            sb.append(String.valueOf(TAG_CouponRate) + ES + _CouponRate + SOH);
        }
        if (_SecurityExchange != null) {
            sb.append(String.valueOf(TAG_SecurityExchange) + ES + _SecurityExchange + SOH);
        }
        if (_Issuer != null) {
            sb.append(String.valueOf(TAG_Issuer) + ES + _Issuer + SOH);
        }
        if (_EncodedIssuerLen != null) {
            sb.append(String.valueOf(TAG_EncodedIssuerLen) + ES + _EncodedIssuerLen + SOH);
        }
        if (_EncodedIssuer != null) {
            sb.append(String.valueOf(TAG_EncodedIssuer) + ES + _EncodedIssuer + SOH);
        }
        if (_SecurityDesc != null) {
            sb.append(String.valueOf(TAG_SecurityDesc) + ES + _SecurityDesc + SOH);
        }
        if (_EncodedSecurityDescLen != null) {
            sb.append(String.valueOf(TAG_EncodedSecurityDescLen) + ES + _EncodedSecurityDescLen + SOH);
        }
        if (_EncodedSecurityDesc != null) {
            sb.append(String.valueOf(TAG_EncodedSecurityDesc) + ES + _EncodedSecurityDesc + SOH);
        }
        if (_Currency != null) {
            sb.append(String.valueOf(TAG_Currency) + ES + _Currency + SOH);
        }
        if (_TradingSessionID != null) {
            sb.append(String.valueOf(TAG_TradingSessionID) + ES + _TradingSessionID + SOH);
        }
        if (_Text != null) {
            sb.append(String.valueOf(TAG_Text) + ES + _Text + SOH);
        }
        if (_EncodedTextLen != null) {
            sb.append(String.valueOf(TAG_EncodedTextLen) + ES + _EncodedTextLen + SOH);
        }
        if (_EncodedText != null) {
            sb.append(String.valueOf(TAG_EncodedText) + ES + _EncodedText + SOH);
        }
        if (_NoRelatedSym != null) {
            sb.append(String.valueOf(TAG_NoRelatedSym) + ES + _NoRelatedSym + SOH);
        }
        if (_RelatedSymSeq != null) {
            sb.append(_RelatedSymSeq.toFIXMessage());
        }
        return sb.toString();
    }

    public byte[] toFIXBytes() {
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            if (_SecurityReqID != null) {
                bs.write((String.valueOf(TAG_SecurityReqID) + ES + _SecurityReqID + SOH).getBytes());
            }
            if (_SecurityResponseID != null) {
                bs.write((String.valueOf(TAG_SecurityResponseID) + ES + _SecurityResponseID + SOH).getBytes());
            }
            if (_SecurityResponseType != null) {
                bs.write((String.valueOf(TAG_SecurityResponseType) + ES + _SecurityResponseType + SOH).getBytes());
            }
            if (_Symbol != null) {
                bs.write((String.valueOf(TAG_Symbol) + ES + _Symbol + SOH).getBytes());
            }
            if (_SymbolSfx != null) {
                bs.write((String.valueOf(TAG_SymbolSfx) + ES + _SymbolSfx + SOH).getBytes());
            }
            if (_SecurityID != null) {
                bs.write((String.valueOf(TAG_SecurityID) + ES + _SecurityID + SOH).getBytes());
            }
            if (_IDSource != null) {
                bs.write((String.valueOf(TAG_IDSource) + ES + _IDSource + SOH).getBytes());
            }
            if (_SecurityType != null) {
                bs.write((String.valueOf(TAG_SecurityType) + ES + _SecurityType + SOH).getBytes());
            }
            if (_MaturityMonthYear != null) {
                bs.write((String.valueOf(TAG_MaturityMonthYear) + ES + _MaturityMonthYear + SOH).getBytes());
            }
            if (_MaturityDay != null) {
                bs.write((String.valueOf(TAG_MaturityDay) + ES + _MaturityDay + SOH).getBytes());
            }
            if (_PutOrCall != null) {
                bs.write((String.valueOf(TAG_PutOrCall) + ES + _PutOrCall + SOH).getBytes());
            }
            if (_StrikePrice != null) {
                bs.write((String.valueOf(TAG_StrikePrice) + ES + _StrikePrice + SOH).getBytes());
            }
            if (_OptAttribute != null) {
                bs.write((String.valueOf(TAG_OptAttribute) + ES + _OptAttribute + SOH).getBytes());
            }
            if (_ContractMultiplier != null) {
                bs.write((String.valueOf(TAG_ContractMultiplier) + ES + _ContractMultiplier + SOH).getBytes());
            }
            if (_CouponRate != null) {
                bs.write((String.valueOf(TAG_CouponRate) + ES + _CouponRate + SOH).getBytes());
            }
            if (_SecurityExchange != null) {
                bs.write((String.valueOf(TAG_SecurityExchange) + ES + _SecurityExchange + SOH).getBytes());
            }
            if (_Issuer != null) {
                bs.write((String.valueOf(TAG_Issuer) + ES + _Issuer + SOH).getBytes());
            }
            if (_EncodedIssuerLen != null) {
                bs.write((String.valueOf(TAG_EncodedIssuerLen) + ES + _EncodedIssuerLen + SOH).getBytes());
            }
            if (_EncodedIssuer != null) {
                bs.write((String.valueOf(TAG_EncodedIssuer) + ES + _EncodedIssuer + SOH).getBytes());
            }
            if (_SecurityDesc != null) {
                bs.write((String.valueOf(TAG_SecurityDesc) + ES + _SecurityDesc + SOH).getBytes());
            }
            if (_EncodedSecurityDescLen != null) {
                bs.write((String.valueOf(TAG_EncodedSecurityDescLen) + ES + _EncodedSecurityDescLen + SOH).getBytes());
            }
            if (_EncodedSecurityDesc != null) {
                bs.write((String.valueOf(TAG_EncodedSecurityDesc) + ES + _EncodedSecurityDesc + SOH).getBytes());
            }
            if (_Currency != null) {
                bs.write((String.valueOf(TAG_Currency) + ES + _Currency + SOH).getBytes());
            }
            if (_TradingSessionID != null) {
                bs.write((String.valueOf(TAG_TradingSessionID) + ES + _TradingSessionID + SOH).getBytes());
            }
            if (_Text != null) {
                bs.write((String.valueOf(TAG_Text) + ES + _Text + SOH).getBytes());
            }
            if (_EncodedTextLen != null) {
                bs.write((String.valueOf(TAG_EncodedTextLen) + ES + _EncodedTextLen + SOH).getBytes());
            }
            if (_EncodedText != null) {
                bs.write((String.valueOf(TAG_EncodedText) + ES + _EncodedText + SOH).getBytes());
            }
            if (_NoRelatedSym != null) {
                bs.write((String.valueOf(TAG_NoRelatedSym) + ES + _NoRelatedSym + SOH).getBytes());
            }
            if (_RelatedSymSeq != null) {
                bs.write(_RelatedSymSeq.toFIXBytes());
            }
            byte[] t = bs.toByteArray();
            bs.close();
            return t;
        } catch (IOException ie) {
            ie.printStackTrace();
            return new byte[0];
        }
    }
}
