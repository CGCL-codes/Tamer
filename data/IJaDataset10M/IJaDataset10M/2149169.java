package org.nakedobjects.nos.client.xat2.sample.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.DescribedAs;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.annotation.RegEx;
import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.applib.clock.Clock;
import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.applib.util.TitleBuffer;
import org.nakedobjects.nos.client.xat2.sample.services.CustomerRepository;

public class Customer extends AbstractDomainObject {

    /**
     * Defines the title that will be displayed on the user
     * interface in order to identity this object.
     */
    public String title() {
        TitleBuffer t = new TitleBuffer();
        t.append(getFirstName()).append(getLastName());
        return t.toString();
    }

    private String firstName;

    @DescribedAs("Given or christian name")
    @TypicalLength(20)
    @MaxLength(100)
    @Optional
    public String getFirstName() {
        resolve(firstName);
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        objectChanged();
    }

    public boolean modifyFirstNameCalled = false;

    public void modifyFirstName(String firstName) {
        setFirstName(firstName);
        this.modifyFirstNameCalled = true;
    }

    public boolean clearFirstNameCalled = false;

    public void clearFirstName() {
        setFirstName(null);
        this.clearFirstNameCalled = true;
    }

    public String validateFirstName;

    public String validateFirstNameExpectedArg;

    public String validateFirstName(String firstName) {
        if (validateFirstNameExpectedArg != null && !validateFirstNameExpectedArg.equals(firstName)) {
            return "argument provided by XAT framework was incorrect";
        }
        return validateFirstName;
    }

    public String disableFirstName;

    public String disableFirstName() {
        return this.disableFirstName;
    }

    public boolean hideFirstName;

    public boolean hideFirstName() {
        return this.hideFirstName;
    }

    private Country countryOfBirth;

    @Optional
    public Country getCountryOfBirth() {
        resolve(countryOfBirth);
        return countryOfBirth;
    }

    public void setCountryOfBirth(Country countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
        objectChanged();
    }

    public boolean modifyCountryOfBirthCalled = false;

    public void modifyCountryOfBirth(Country countryOfBirth) {
        setCountryOfBirth(countryOfBirth);
        this.modifyCountryOfBirthCalled = true;
    }

    public boolean clearCountryOfBirthCalled = false;

    public void clearCountryOfBirth() {
        setCountryOfBirth(null);
        this.clearCountryOfBirthCalled = true;
    }

    public String validateCountryOfBirth;

    public String validateCountryOfBirth(Country countryOfBirth) {
        return validateCountryOfBirth;
    }

    public String disableCountryOfBirth;

    public String disableCountryOfBirth() {
        return this.disableCountryOfBirth;
    }

    public boolean hideCountryOfBirth;

    public boolean hideCountryOfBirth() {
        return this.hideCountryOfBirth;
    }

    private List<Country> visitedCountries = new ArrayList<Country>();

    public List<Country> getVisitedCountries() {
        resolve();
        return this.visitedCountries;
    }

    @SuppressWarnings("unused")
    private void setVisitedCountries(List<Country> visitedCountries) {
        this.visitedCountries = visitedCountries;
    }

    public void addToVisitedCountries(Country country) {
        getVisitedCountries().add(country);
        objectChanged();
    }

    public void removeFromVisitedCountries(Country country) {
        getVisitedCountries().remove(country);
        objectChanged();
    }

    public String validateAddToVisitedCountries;

    public String validateAddToVisitedCountries(Country country) {
        return validateAddToVisitedCountries;
    }

    public String validateRemoveFromVisitedCountries;

    public String validateRemoveFromVisitedCountries(Country country) {
        return validateRemoveFromVisitedCountries;
    }

    public String disableVisitedCountries;

    public String disableVisitedCountries() {
        return this.disableVisitedCountries;
    }

    public boolean hideVisitedCountries;

    public boolean hideVisitedCountries() {
        return this.hideVisitedCountries;
    }

    private String alwaysDisabledValue;

    @Disabled(When.ALWAYS)
    public String getAlwaysDisabledValue() {
        resolve(alwaysDisabledValue);
        return this.alwaysDisabledValue;
    }

    public void setAlwaysDisabledValue(String alwaysDisabled) {
        this.alwaysDisabledValue = alwaysDisabled;
        objectChanged();
    }

    private Country alwaysDisabledAssociation;

    @Disabled(When.ALWAYS)
    public Country getAlwaysDisabledAssociation() {
        resolve(alwaysDisabledAssociation);
        return this.alwaysDisabledAssociation;
    }

    public void setAlwaysDisabledAssociation(Country alwaysDisabled) {
        this.alwaysDisabledAssociation = alwaysDisabled;
        objectChanged();
    }

    private List<Country> alwaysDisabledCollection = new ArrayList<Country>();

    @Disabled(When.ALWAYS)
    public List<Country> getAlwaysDisabledCollection() {
        resolve();
        return this.alwaysDisabledCollection;
    }

    @SuppressWarnings("unused")
    private void setAlwaysDisabledCollection(List<Country> alwaysDisabledCollection) {
        this.alwaysDisabledCollection = alwaysDisabledCollection;
    }

    public void addToAlwaysDisabledCollection(Country country) {
        getAlwaysDisabledCollection().add(country);
        objectChanged();
    }

    public void removeFromAlwaysDisabledCollection(Country country) {
        getAlwaysDisabledCollection().remove(country);
        objectChanged();
    }

    @Disabled(When.ALWAYS)
    public void alwaysDisabledAction() {
    }

    private String sessionDisabledValue;

    public String getSessionDisabledValue() {
        resolve(sessionDisabledValue);
        return this.sessionDisabledValue;
    }

    public void setSessionDisabledValue(String sessionDisabled) {
        this.sessionDisabledValue = sessionDisabled;
        objectChanged();
    }

    public static String disableSessionDisabledValue(UserMemento user) {
        return "disabled for this user";
    }

    private Country sessionDisabledAssociation;

    public Country getSessionDisabledAssociation() {
        resolve(sessionDisabledAssociation);
        return this.sessionDisabledAssociation;
    }

    public void setSessionDisabledAssociation(Country sessionDisabled) {
        this.sessionDisabledAssociation = sessionDisabled;
        objectChanged();
    }

    public static String disableSessionDisabledAssociation(UserMemento user) {
        return "disabled for this user";
    }

    private List<Country> sessionDisabledCollection = new ArrayList<Country>();

    public List<Country> getSessionDisabledCollection() {
        resolve();
        return this.sessionDisabledCollection;
    }

    @SuppressWarnings("unused")
    private void setSessionDisabledCollection(List<Country> sessionDisabledCollection) {
        this.sessionDisabledCollection = sessionDisabledCollection;
    }

    public void addToSessionDisabledCollection(Country country) {
        getSessionDisabledCollection().add(country);
        objectChanged();
    }

    public void removeFromSessionDisabledCollection(Country country) {
        getSessionDisabledCollection().remove(country);
        objectChanged();
    }

    public static String disableSessionDisabledCollection(UserMemento user) {
        return "disabled for this user";
    }

    public void sessionDisabledAction() {
    }

    public static String disableSessionDisabledAction(UserMemento user) {
        return "disabled for this user";
    }

    private String alwaysHiddenValue;

    @Hidden(When.ALWAYS)
    public String getAlwaysHiddenValue() {
        resolve(alwaysHiddenValue);
        return this.alwaysHiddenValue;
    }

    public void setAlwaysHiddenValue(String alwaysHidden) {
        this.alwaysHiddenValue = alwaysHidden;
        objectChanged();
    }

    private Country alwaysHiddenAssociation;

    @Hidden(When.ALWAYS)
    public Country getAlwaysHiddenAssociation() {
        resolve(alwaysHiddenAssociation);
        return this.alwaysHiddenAssociation;
    }

    public void setAlwaysHiddenAssociation(Country alwaysHidden) {
        this.alwaysHiddenAssociation = alwaysHidden;
        objectChanged();
    }

    private List<Country> alwaysHiddenCollection = new ArrayList<Country>();

    @Hidden(When.ALWAYS)
    public List<Country> getAlwaysHiddenCollection() {
        resolve();
        return this.alwaysHiddenCollection;
    }

    @SuppressWarnings("unused")
    private void setAlwaysHiddenCollection(List<Country> alwaysHiddenCollection) {
        this.alwaysHiddenCollection = alwaysHiddenCollection;
    }

    public void addToAlwaysHiddenCollection(Country country) {
        getAlwaysHiddenCollection().add(country);
        objectChanged();
    }

    public void removeFromAlwaysHiddenCollection(Country country) {
        getAlwaysHiddenCollection().remove(country);
        objectChanged();
    }

    @Hidden(When.ALWAYS)
    public void alwaysHiddenAction() {
    }

    private String sessionHiddenValue;

    public String getSessionHiddenValue() {
        resolve(sessionHiddenValue);
        return this.sessionHiddenValue;
    }

    public void setSessionHiddenValue(String sessionHidden) {
        this.sessionHiddenValue = sessionHidden;
        objectChanged();
    }

    public static boolean hideSessionHiddenValue(UserMemento user) {
        return true;
    }

    private Country sessionHiddenAssociation;

    public Country getSessionHiddenAssociation() {
        resolve(sessionHiddenAssociation);
        return this.sessionHiddenAssociation;
    }

    public void setSessionHiddenAssociation(Country sessionHidden) {
        this.sessionHiddenAssociation = sessionHidden;
        objectChanged();
    }

    public static boolean hideSessionHiddenAssociation(UserMemento user) {
        return true;
    }

    private List<Country> sessionHiddenCollection = new ArrayList<Country>();

    public List<Country> getSessionHiddenCollection() {
        resolve();
        return this.sessionHiddenCollection;
    }

    @SuppressWarnings("unused")
    private void setSessionHiddenCollection(List<Country> sessionHiddenCollection) {
        this.sessionHiddenCollection = sessionHiddenCollection;
    }

    public void addToSessionHiddenCollection(Country country) {
        getSessionHiddenCollection().add(country);
        objectChanged();
    }

    public void removeFromSessionHiddenCollection(Country country) {
        getSessionHiddenCollection().remove(country);
        objectChanged();
    }

    public static boolean hideSessionHiddenCollection(UserMemento user) {
        return true;
    }

    public void sessionHiddenAction() {
    }

    public static boolean hideSessionHiddenAction(UserMemento user) {
        return true;
    }

    private String mandatoryValue;

    public String getMandatoryValue() {
        resolve(mandatoryValue);
        return this.mandatoryValue;
    }

    public void setMandatoryValue(String mandatory) {
        this.mandatoryValue = mandatory;
        objectChanged();
    }

    private Country mandatoryAssociation;

    public Country getMandatoryAssociation() {
        resolve(mandatoryAssociation);
        return this.mandatoryAssociation;
    }

    public void setMandatoryAssociation(Country mandatory) {
        this.mandatoryAssociation = mandatory;
        objectChanged();
    }

    private String optionalValue;

    @Optional
    public String getOptionalValue() {
        resolve(optionalValue);
        return this.optionalValue;
    }

    public void setOptionalValue(String optional) {
        this.optionalValue = optional;
        objectChanged();
    }

    private Country optionalAssociation;

    @Optional
    public Country getOptionalAssociation() {
        resolve(optionalAssociation);
        return this.optionalAssociation;
    }

    public void setOptionalAssociation(Country optional) {
        this.optionalAssociation = optional;
        objectChanged();
    }

    private List<Country> optionalCollection = new ArrayList<Country>();

    @Optional
    public List<Country> getOptionalCollection() {
        resolve();
        return this.optionalCollection;
    }

    @SuppressWarnings("unused")
    private void setOptionalCollection(List<Country> optionalCollection) {
        this.optionalCollection = optionalCollection;
    }

    public void addToOptionalCollection(Country country) {
        getOptionalCollection().add(country);
        objectChanged();
    }

    public void removeFromOptionalCollection(Country country) {
        getOptionalCollection().remove(country);
        objectChanged();
    }

    private String maxLengthField;

    @MaxLength(10)
    public String getMaxLengthField() {
        resolve(maxLengthField);
        return this.maxLengthField;
    }

    public void setMaxLengthField(String maxLength) {
        this.maxLengthField = maxLength;
        objectChanged();
    }

    private String regExCaseSensitiveField;

    @RegEx(validation = "abc.+", caseSensitive = true)
    public String getRegExCaseSensitiveField() {
        resolve(regExCaseSensitiveField);
        return this.regExCaseSensitiveField;
    }

    public void setRegExCaseSensitiveField(String regEx) {
        this.regExCaseSensitiveField = regEx;
        objectChanged();
    }

    private String regExCaseInsensitiveField;

    @RegEx(validation = "abc.+", caseSensitive = false)
    public String getRegExCaseInsensitiveField() {
        resolve(regExCaseInsensitiveField);
        return this.regExCaseInsensitiveField;
    }

    public void setRegExCaseInsensitiveField(String regExCaseInsensitive) {
        this.regExCaseInsensitiveField = regExCaseInsensitive;
        objectChanged();
    }

    private String lastName;

    @DescribedAs("Family name or surname")
    @MaxLength(100)
    @TypicalLength(30)
    @Named("Surname")
    public String getLastName() {
        resolve(lastName);
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        objectChanged();
    }

    public void modifyLastName(String lastName) {
        this.lastName = lastName;
        objectChanged();
    }

    private Integer customerNumber;

    @Disabled(When.ONCE_PERSISTED)
    public Integer getCustomerNumber() {
        resolve(customerNumber);
        return this.customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
        objectChanged();
    }

    public String validateCustomerNumber(Integer customerNumber) {
        return null;
    }

    private List<Order> orders = new ArrayList<Order>();

    public List<Order> getOrders() {
        resolve();
        return this.orders;
    }

    @SuppressWarnings("unused")
    private void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addToOrders(Order order) {
        getOrders().add(order);
        objectChanged();
    }

    public void removeFromOrders(Order order) {
        getOrders().remove(order);
        objectChanged();
    }

    private Order lastOrder;

    @Disabled
    public Order getLastOrder() {
        resolve(lastOrder);
        return this.lastOrder;
    }

    public void setLastOrder(Order lastOrder) {
        this.lastOrder = lastOrder;
        objectChanged();
    }

    public void modifyLastOrder(Order lastOrder) {
        setLastOrder(lastOrder);
    }

    public void clearLastOrder() {
        setLastOrder(null);
    }

    public void placeOrder(Product p, @Named("Quantity") Integer quantity) {
        Order order = (Order) getContainer().newTransientInstance(Order.class);
        order.modifyCustomer(this);
        order.modifyProduct(p);
        order.setOrderDate(new Date(Clock.getTime()));
        order.setQuantity(quantity);
        addToOrders(order);
        modifyLastOrder(order);
        order.makePersistent();
    }

    public String validatePlaceOrder;

    public String validatePlaceOrder(Product p, Integer quantity) {
        return validatePlaceOrder;
    }

    public String disablePlaceOrder;

    public String disablePlaceOrder() {
        return disablePlaceOrder;
    }

    public boolean hidePlaceOrder;

    public boolean hidePlaceOrder() {
        return hidePlaceOrder;
    }

    public Object[] defaultPlaceOrder() {
        Product lastProductOrdered = null;
        if (getLastOrder() != null) {
            lastProductOrdered = getLastOrder().getProduct();
        }
        return new Object[] { lastProductOrdered, new Integer(1) };
    }

    private List<Order> moreOrders = new ArrayList<Order>();

    @Disabled
    public List<Order> getMoreOrders() {
        resolve();
        return this.moreOrders;
    }

    @SuppressWarnings("unused")
    private void setMoreOrders(List<Order> moreOrders) {
        this.moreOrders = moreOrders;
    }

    public void addToMoreOrders(Order order) {
        getMoreOrders().add(order);
        objectChanged();
    }

    public void removeFromMoreOrders(Order order) {
        getMoreOrders().remove(order);
        objectChanged();
    }

    public String validate;

    public boolean validateCalled = false;

    public String validate() {
        validateCalled = true;
        return validate;
    }

    public Long actionWithOptionalValueParameterArgument = Long.MAX_VALUE;

    public void actionWithOptionalValueParameter(@Optional @Named("Amount") Long val) {
        actionWithOptionalValueParameterArgument = val;
    }

    public Long actionWithMandatoryValueParameterArgument = Long.MAX_VALUE;

    public void actionWithMandatoryValueParameter(@Named("Amount") Long val) {
        actionWithMandatoryValueParameterArgument = val;
    }

    public Product actionWithMandatoryReferenceParameterArgument = new Product();

    public void actionWithMandatoryReferenceParameter(Product product) {
        actionWithMandatoryReferenceParameterArgument = product;
    }

    public Product actionWithOptionalReferenceParameterArgument = new Product();

    public void actionWithOptionalReferenceParameter(@Optional Product product) {
        actionWithOptionalReferenceParameterArgument = product;
    }

    public String actionWithOptionalStringParameterArgument = "original value";

    public void actionWithOptionalStringParameter(@Optional @Named("Amount") String val) {
        actionWithOptionalStringParameterArgument = val;
    }

    public String actionWithMandatoryStringParameterArgument = "original value";

    public void actionWithMandatoryStringParameter(@Named("Amount") String val) {
        actionWithMandatoryStringParameterArgument = val;
    }

    public String actionWithMaxLengthStringParameterArgument = "1234";

    public void actionWithMaxLengthStringParameter(@Named("Amount") @MaxLength(4) String val) {
        actionWithMaxLengthStringParameterArgument = val;
    }

    public String actionWithRegExStringParameterArgument = "1234";

    public void actionWithRegExStringParameter(@Named("Amount") @RegEx(validation = "[0-9]{4}") String val) {
        actionWithRegExStringParameterArgument = val;
    }

    private CustomerRepository customerRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected CustomerRepository getCustomerRepository() {
        return this.customerRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
