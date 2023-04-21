package net.sf.coredd.userinterface.domain;

import net.sf.coredd.internationalization.domain.ILanguage;
import net.sf.coredd.modularization.domain.IModule;
import net.sf.coredd.modularization.domain.IPackage;
import net.sf.coredd.versioncontrol.domain.ICustomization;
import net.sf.coredd.versioncontrol.domain.IRelease;
import net.sf.coredd.versioncontrol.domain.IVersion;

/**
 * @author roberto.reinert
 * 
 * Interface for reports.
 */
public interface IReport extends Cloneable {

    /**
	 * @return
	 */
    public ILanguage getlanguage();

    /**
	 * @param language
	 */
    public void setLanguage(ILanguage language);

    /**
	 * Return the report package
	 * @return
	 */
    public IPackage getPackage();

    /**
	 * @param apackage
	 */
    public void setPackage(IPackage apackage);

    /**
	 * Return the report module
	 * @return
	 */
    public IModule getModule();

    /**
	 * @param module
	 */
    public void setModule(IModule module);

    /**
	 * Return the report code
	 * @return
	 */
    public String getCode();

    /**
	 * @param code
	 */
    public void setCode(String code);

    /**
	 * Return the report description
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
    public int getType();

    /**
	 * @param type
	 */
    public void setType(int type);

    /**
	 * @param message
	 */
    public void setMessage(String message);

    /**
	 * Return the report version
	 * @return
	 */
    public IVersion getVersion();

    /**
	 * @param version
	 */
    public void setVersion(IVersion version);

    /**
	 * Return the report release
	 * @return
	 */
    public IRelease getRelease();

    /**
	 * @param release
	 */
    public void setRelease(IRelease release);

    /**
	 * Return the report customization
	 * @return
	 */
    public ICustomization getCustomization();

    /**
	 * @param customization
	 */
    public void setCustomization(ICustomization customization);
}
