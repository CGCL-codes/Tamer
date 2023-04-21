package servicos;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import produto.xsd.Produto;
import servicos.xsd.ObjetoLogistica;
import venda.xsd.Venda;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the servicos package. 
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

    private static final QName _GetVendaResponseReturn_QNAME = new QName("http://servicos", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: servicos
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RegistrarVenda }
     * 
     */
    public RegistrarVenda createRegistrarVenda() {
        return new RegistrarVenda();
    }

    /**
     * Create an instance of {@link GetVendaResponse }
     * 
     */
    public GetVendaResponse createGetVendaResponse() {
        return new GetVendaResponse();
    }

    /**
     * Create an instance of {@link GetInfoLogistica }
     * 
     */
    public GetInfoLogistica createGetInfoLogistica() {
        return new GetInfoLogistica();
    }

    /**
     * Create an instance of {@link GetInfoLogisticaResponse }
     * 
     */
    public GetInfoLogisticaResponse createGetInfoLogisticaResponse() {
        return new GetInfoLogisticaResponse();
    }

    /**
     * Create an instance of {@link RegistrarVendaResponse }
     * 
     */
    public RegistrarVendaResponse createRegistrarVendaResponse() {
        return new RegistrarVendaResponse();
    }

    /**
     * Create an instance of {@link GetVenda }
     * 
     */
    public GetVenda createGetVenda() {
        return new GetVenda();
    }

    /**
     * Create an instance of {@link ListarProdutosResponse }
     * 
     */
    public ListarProdutosResponse createListarProdutosResponse() {
        return new ListarProdutosResponse();
    }

    /**
     * Create an instance of {@link GetProd }
     * 
     */
    public GetProd createGetProd() {
        return new GetProd();
    }

    /**
     * Create an instance of {@link GetProdResponse }
     * 
     */
    public GetProdResponse createGetProdResponse() {
        return new GetProdResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Venda }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servicos", name = "return", scope = GetVendaResponse.class)
    public JAXBElement<Venda> createGetVendaResponseReturn(Venda value) {
        return new JAXBElement<Venda>(_GetVendaResponseReturn_QNAME, Venda.class, GetVendaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Produto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servicos", name = "return", scope = GetProdResponse.class)
    public JAXBElement<Produto> createGetProdResponseReturn(Produto value) {
        return new JAXBElement<Produto>(_GetVendaResponseReturn_QNAME, Produto.class, GetProdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjetoLogistica }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servicos", name = "return", scope = GetInfoLogisticaResponse.class)
    public JAXBElement<ObjetoLogistica> createGetInfoLogisticaResponseReturn(ObjetoLogistica value) {
        return new JAXBElement<ObjetoLogistica>(_GetVendaResponseReturn_QNAME, ObjetoLogistica.class, GetInfoLogisticaResponse.class, value);
    }
}
