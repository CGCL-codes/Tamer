package net.sf.coredd.databasemanagement.domain;

import net.sf.coredd.internationalization.domain.IEnumerated;
import net.sf.coredd.modularization.domain.IPackage;
import net.sf.coredd.versioncontrol.domain.ICustomization;
import net.sf.coredd.versioncontrol.domain.IRelease;
import net.sf.coredd.versioncontrol.domain.IVersion;

/**
 * @author roberto.reinert
 * 
 * Interface for domains.
 */
public interface IDomain extends Cloneable {

    /**
	 * @return
	 */
    public IPackage getPackage();

    /**
	 * @param apackage
	 */
    public void setPackage(IPackage apackage);

    /**
	 * @return
	 */
    public String getCode();

    /**
	 * @param code
	 */
    public void setCode(String code);

    /**
	 * @return
	 */
    public String getDescription();

    /**
	 * @param description
	 */
    public void setDescription(String description);

    /**
	 * @return
	 */
    public int getDataType();

    /**
	 * @param dataType
	 */
    public void setDataType(int dataType);

    /**
	 * @return
	 */
    public String getDisplayFormat();

    /**
	 * @param displayFormat
	 */
    public void setDisplayFormat(String displayFormat);

    /**
	 * @return
	 */
    public String getLegalCharacters();

    /**
	 * @param legalCharacters
	 */
    public void setLegalCharacters(String legalCharacters);

    /**
	 * @return
	 */
    public String getIlegalCharacters();

    /**
	 * @param ilegalCharacters
	 */
    public void setIlegalCharacters(String ilegalCharacters);

    /**
	 * @return
	 */
    public String getRange();

    /**
	 * @param range
	 */
    public void setRange(String range);

    /**
	 * @return
	 */
    public int getLenght();

    /**
	 * @param lenght
	 */
    public void setLenght(int lenght);

    /**
	 * @return
	 */
    public int getAlignment();

    /**
	 * @param alignment
	 */
    public void setAlignment(int alignment);

    /**
	 * @return
	 */
    public int getDigitsBeforeSign();

    /**
	 * @param digitsBeforeSign
	 */
    public void setDigitsBeforeSign(int digitsBeforeSign);

    /**
	 * @return
	 */
    public int getDigitsAfterSign();

    /**
	 * @param digitsAfterSign
	 */
    public void setDigitsAfterSign(int digitsAfterSign);

    /**
	 * @return
	 */
    public int getRoundMethod();

    /**
	 * @param roundMethod
	 */
    public void setRoundMethod(int roundMethod);

    /**
	 * @return
	 */
    public IEnumerated getEnumerated();

    /**
	 * @param enumerated
	 */
    public void setEnumerated(IEnumerated enumerated);

    /**
	 * Return the domain version
	 * @return
	 */
    public IVersion getVersion();

    /**
	 * @param version
	 */
    public void setVersion(IVersion version);

    /**
	 * Return the domain release
	 * @return
	 */
    public IRelease getRelease();

    /**
	 * @param release
	 */
    public void setRelease(IRelease release);

    /**
	 * Return the domain customization
	 * @return
	 */
    public ICustomization getCustomization();

    /**
	 * @param customization
	 */
    public void setCustomization(ICustomization customization);
}
