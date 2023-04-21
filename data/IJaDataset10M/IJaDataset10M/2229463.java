package org.koossery.adempiere.core.contract.itf.ad;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IAD_LanguageDTO extends ITransferableObject {

    public String getAd_Language();

    public void setAd_Language(String ad_Language);

    public int getAd_Language_ID();

    public void setAd_Language_ID(int ad_Language_ID);

    public String getCountryCode();

    public void setCountryCode(String countryCode);

    public String getDatePattern();

    public void setDatePattern(String datePattern);

    public String getLanguageISO();

    public void setLanguageISO(String languageISO);

    public String getName();

    public void setName(String name);

    public String getTimePattern();

    public void setTimePattern(String timePattern);

    public String getIsBaseLanguage();

    public void setIsBaseLanguage(String isBaseLanguage);

    public String getIsDecimalPoint();

    public void setIsDecimalPoint(String isDecimalPoint);

    public String getIsProcessing();

    public void setIsProcessing(String isProcessing);

    public String getIsSystemLanguage();

    public void setIsSystemLanguage(String isSystemLanguage);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
