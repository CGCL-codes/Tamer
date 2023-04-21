package ch.cpdzone.addressbook.ui;

import java.io.IOException;
import java.util.Observer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.LdapTemplate;
import ch.cpdzone.addressbook.model.AddressBook;
import ch.cpdzone.addressbook.model.ContactPerson;
import ch.cpdzone.addressbook.model.configuration.AddressBookConfiguration;
import ch.cpdzone.addressbook.model.configuration.AddressBookSource;
import ch.cpdzone.addressbook.model.configuration.mapping.ConfigurationMappingLoader;
import ch.cpdzone.addressbook.model.ldap.LdapAddressBook;
import ch.cpdzone.addressbook.model.ldap.LdapContactPersonDAO;
import ch.cpdzone.addressbook.model.ldap.mapping.LdapMapping;
import ch.cpdzone.addressbook.model.ldap.mapping.LdapMappingLoader;
import ch.cpdzone.addressbook.model.ldap.ssl.NoCertSSLLdapContextSource;

public class UIDataAccess {

    private ApplicationContext appContext;

    private AddressBookConfiguration addressBookConfiguration;

    private ContactPerson currentContactPerson;

    private AddressBookSource currentAddressBookSource;

    private AddressBookSource currentAddressBookSourceFromDropDown;

    public UIDataAccess() {
        appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        load();
    }

    public AddressBookConfiguration getAddressBookConfiguration() {
        return addressBookConfiguration;
    }

    public void setAddressBookConfiguration(AddressBookConfiguration addressBookConfiguration) throws IOException {
        this.addressBookConfiguration = addressBookConfiguration;
        storeConfiguration(appContext, addressBookConfiguration);
        load();
    }

    public void load() {
        try {
            if (addressBookConfiguration == null) {
                addressBookConfiguration = loadConfiguration(appContext);
            }
            for (AddressBookSource addressBookSource : addressBookConfiguration.getAddressBookSources()) {
                if (addressBookSource.isActiv()) {
                    addressBookSource.setAddressBook(loadAddressBook(addressBookSource, appContext));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addObserver(Observer observer) {
        for (AddressBookSource addressBookSource : addressBookConfiguration.getAddressBookSources()) {
            if (addressBookSource.isActiv()) {
                addressBookSource.getAddressBook().getObservable().addObserver(observer);
            }
        }
    }

    public void deleteObserver(Observer observer) {
        for (AddressBookSource addressBookSource : addressBookConfiguration.getAddressBookSources()) {
            if (addressBookSource.isActiv()) {
                addressBookSource.getAddressBook().getObservable().deleteObserver(observer);
            }
        }
    }

    private AddressBook loadAddressBook(AddressBookSource addressBookSource, ApplicationContext appContext) throws Exception {
        LdapMappingLoader ldapMappingLoader = (LdapMappingLoader) appContext.getBean("ldapMappingLoader");
        LdapMapping ldapMapping = ldapMappingLoader.loadLdapMapping();
        LdapContactPersonDAO ldapContactPersonDAO = new LdapContactPersonDAO(getLdapTemplate(addressBookSource), ldapMapping);
        ldapContactPersonDAO.setSearchDN(addressBookSource.getAttributes().get("searchDN"));
        return new LdapAddressBook(ldapContactPersonDAO);
    }

    private AddressBookConfiguration loadConfiguration(ApplicationContext appContext) throws IOException {
        ConfigurationMappingLoader configurationMappingLoader = (ConfigurationMappingLoader) appContext.getBean("configurationMappingLoader");
        return configurationMappingLoader.loadConfiguration();
    }

    private void storeConfiguration(ApplicationContext appContext, AddressBookConfiguration addressBookConfiguration) throws IOException {
        ConfigurationMappingLoader configurationMappingLoader = (ConfigurationMappingLoader) appContext.getBean("configurationMappingLoader");
        configurationMappingLoader.saveConfiguration(addressBookConfiguration);
    }

    private static LdapTemplate getLdapTemplate(AddressBookSource addressBookSource) throws Exception {
        NoCertSSLLdapContextSource contextSource = new NoCertSSLLdapContextSource();
        contextSource.setUrl(addressBookSource.getAttributes().get("url"));
        contextSource.setBase(addressBookSource.getAttributes().get("baseDN"));
        contextSource.setUserName(addressBookSource.getAttributes().get("bindDN"));
        contextSource.setPassword(addressBookSource.getAttributes().get("password"));
        contextSource.setPooled(true);
        contextSource.afterPropertiesSet();
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        return ldapTemplate;
    }

    public ContactPerson getCurrentContactPerson() {
        if (currentContactPerson != null && currentAddressBookSourceFromDropDown.getAddressBook().get(currentContactPerson.getKeyName()) != null) {
            return currentContactPerson;
        } else {
            return null;
        }
    }

    public void setCurrentContactPerson(ContactPerson currentContactPerson) {
        this.currentContactPerson = currentContactPerson;
    }

    public AddressBookSource getCurrentAddressBookSource() {
        return currentAddressBookSource;
    }

    public void setCurrentAddressBookSource(AddressBookSource currentAddressBookSource) {
        this.currentAddressBookSource = currentAddressBookSource;
    }

    public AddressBookSource getCurrentAddressBookSourceFromDropDown() {
        return currentAddressBookSourceFromDropDown;
    }

    public void setCurrentAddressBookSourceFromDropDown(AddressBookSource currentAddressBookSourceFromDropDown) {
        this.currentAddressBookSourceFromDropDown = currentAddressBookSourceFromDropDown;
    }
}
