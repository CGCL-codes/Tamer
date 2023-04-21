package dmeduc.reference;

import org.modelibra.DomainModel;
import org.modelibra.IDomain;
import dmeduc.reference.countrylanguage.CountryLanguages;
import dmeduc.reference.countryname.CountryNames;
import dmeduc.reference.securityrole.SecurityRoles;

/**
 * Reference generated model. This class should not be changed manually. Use a
 * subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-11-15
 */
public abstract class GenReference extends DomainModel {

    private static final long serialVersionUID = 1195157999719L;

    private CountryNames countryNames;

    private CountryLanguages countryLanguages;

    private SecurityRoles securityRoles;

    /**
	 * Constructs a domain model within the domain.
	 * 
	 * @param domain
	 *            domain
	 */
    public GenReference(IDomain domain) {
        super(domain);
        countryNames = new CountryNames(this);
        countryLanguages = new CountryLanguages(this);
        securityRoles = new SecurityRoles(this);
    }

    /**
	 * Gets CountryName entities.
	 * 
	 * @return CountryName entities
	 */
    public CountryNames getCountryNames() {
        return countryNames;
    }

    /**
	 * Gets CountryLanguage entities.
	 * 
	 * @return CountryLanguage entities
	 */
    public CountryLanguages getCountryLanguages() {
        return countryLanguages;
    }

    /**
	 * Gets SecurityRole entities.
	 * 
	 * @return SecurityRole entities
	 */
    public SecurityRoles getSecurityRoles() {
        return securityRoles;
    }
}
