package companies.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the companies.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _GetCompaniesResponse_QNAME = new QName("http://companies.service/companies", "getCompaniesResponse");

    private static final QName _GetCompanies_QNAME = new QName("http://companies.service/companies", "getCompanies");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: companies.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCompaniesResponse }
     * 
     */
    public GetCompaniesResponse createGetCompaniesResponse() {
        return new GetCompaniesResponse();
    }

    /**
     * Create an instance of {@link GetCompaniesResponse.Return }
     * 
     */
    public GetCompaniesResponse.Return createGetCompaniesResponseReturn() {
        return new GetCompaniesResponse.Return();
    }

    /**
     * Create an instance of {@link CompanyLocationType }
     * 
     */
    public CompanyLocationType createCompanyLocationType() {
        return new CompanyLocationType();
    }

    /**
     * Create an instance of {@link LocationType }
     * 
     */
    public LocationType createLocationType() {
        return new LocationType();
    }

    /**
     * Create an instance of {@link ContactInfoType }
     * 
     */
    public ContactInfoType createContactInfoType() {
        return new ContactInfoType();
    }

    /**
     * Create an instance of {@link GetCompaniesResponse.Return.CompanyInfo.Products }
     * 
     */
    public GetCompaniesResponse.Return.CompanyInfo.Products createGetCompaniesResponseReturnCompanyInfoProducts() {
        return new GetCompaniesResponse.Return.CompanyInfo.Products();
    }

    /**
     * Create an instance of {@link GetCompanies }
     * 
     */
    public GetCompanies createGetCompanies() {
        return new GetCompanies();
    }

    /**
     * Create an instance of {@link Companies.CompanyInfo.Products }
     * 
     */
    public Companies.CompanyInfo.Products createCompaniesCompanyInfoProducts() {
        return new Companies.CompanyInfo.Products();
    }

    /**
     * Create an instance of {@link CompanyInfoType }
     * 
     */
    public CompanyInfoType createCompanyInfoType() {
        return new CompanyInfoType();
    }

    /**
     * Create an instance of {@link GetCompaniesResponse.Return.CompanyInfo.Products.Product }
     * 
     */
    public GetCompaniesResponse.Return.CompanyInfo.Products.Product createGetCompaniesResponseReturnCompanyInfoProductsProduct() {
        return new GetCompaniesResponse.Return.CompanyInfo.Products.Product();
    }

    /**
     * Create an instance of {@link Companies.CompanyInfo.Products.Product }
     * 
     */
    public Companies.CompanyInfo.Products.Product createCompaniesCompanyInfoProductsProduct() {
        return new Companies.CompanyInfo.Products.Product();
    }

    /**
     * Create an instance of {@link Companies.CompanyInfo }
     * 
     */
    public Companies.CompanyInfo createCompaniesCompanyInfo() {
        return new Companies.CompanyInfo();
    }

    /**
     * Create an instance of {@link Companies }
     * 
     */
    public Companies createCompanies() {
        return new Companies();
    }

    /**
     * Create an instance of {@link GetCompaniesResponse.Return.CompanyInfo }
     * 
     */
    public GetCompaniesResponse.Return.CompanyInfo createGetCompaniesResponseReturnCompanyInfo() {
        return new GetCompaniesResponse.Return.CompanyInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompaniesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://companies.service/companies", name = "getCompaniesResponse")
    public JAXBElement<GetCompaniesResponse> createGetCompaniesResponse(GetCompaniesResponse value) {
        return new JAXBElement<GetCompaniesResponse>(_GetCompaniesResponse_QNAME, GetCompaniesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompanies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://companies.service/companies", name = "getCompanies")
    public JAXBElement<GetCompanies> createGetCompanies(GetCompanies value) {
        return new JAXBElement<GetCompanies>(_GetCompanies_QNAME, GetCompanies.class, null, value);
    }
}
