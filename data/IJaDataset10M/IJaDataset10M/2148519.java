package saveCCM;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import saveCCM.Application;
import saveCCM.Assembly;
import saveCCM.AssemblyDesc;
import saveCCM.Attribute;
import saveCCM.Behaviour;
import saveCCM.Bindport;
import saveCCM.Clock;
import saveCCM.Component;
import saveCCM.ComponentDesc;
import saveCCM.ComponentList;
import saveCCM.Condition;
import saveCCM.Connection;
import saveCCM.ConnectionList;
import saveCCM.Delay;
import saveCCM.EntryFunc;
import saveCCM.From;
import saveCCM.IODef;
import saveCCM.Inport;
import saveCCM.Model;
import saveCCM.ObjectFactory;
import saveCCM.Outport;
import saveCCM.Realisation;
import saveCCM.Switch;
import saveCCM.SwitchCondition;
import saveCCM.SwitchDesc;
import saveCCM.To;
import saveCCM.TypeDefs;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the saveCCM package. 
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

    private static final QName _APPLICATION_QNAME = new QName("", "APPLICATION");

    private static final QName _Comment_QNAME = new QName("", "comment");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: saveCCM
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Connection }
     * 
     */
    public Connection createConnection() {
        return new Connection();
    }

    /**
     * Create an instance of {@link ComponentDesc }
     * 
     */
    public ComponentDesc createComponentDesc() {
        return new ComponentDesc();
    }

    /**
     * Create an instance of {@link Delay }
     * 
     */
    public Delay createDelay() {
        return new Delay();
    }

    /**
     * Create an instance of {@link Inport }
     * 
     */
    public Inport createInport() {
        return new Inport();
    }

    /**
     * Create an instance of {@link Behaviour }
     * 
     */
    public Behaviour createBehaviour() {
        return new Behaviour();
    }

    /**
     * Create an instance of {@link Model }
     * 
     */
    public Model createModel() {
        return new Model();
    }

    /**
     * Create an instance of {@link Outport }
     * 
     */
    public Outport createOutport() {
        return new Outport();
    }

    /**
     * Create an instance of {@link AssemblyDesc }
     * 
     */
    public AssemblyDesc createAssemblyDesc() {
        return new AssemblyDesc();
    }

    /**
     * Create an instance of {@link Component }
     * 
     */
    public Component createComponent() {
        return new Component();
    }

    /**
     * Create an instance of {@link IODef }
     * 
     */
    public IODef createIODef() {
        return new IODef();
    }

    /**
     * Create an instance of {@link Application }
     * 
     */
    public Application createApplication() {
        return new Application();
    }

    /**
     * Create an instance of {@link To }
     * 
     */
    public To createTo() {
        return new To();
    }

    /**
     * Create an instance of {@link ConnectionList }
     * 
     */
    public ConnectionList createConnectionList() {
        return new ConnectionList();
    }

    /**
     * Create an instance of {@link Bindport }
     * 
     */
    public Bindport createBindport() {
        return new Bindport();
    }

    /**
     * Create an instance of {@link Realisation }
     * 
     */
    public Realisation createRealisation() {
        return new Realisation();
    }

    /**
     * Create an instance of {@link ComponentList }
     * 
     */
    public ComponentList createComponentList() {
        return new ComponentList();
    }

    /**
     * Create an instance of {@link TypeDefs }
     * 
     */
    public TypeDefs createTypeDefs() {
        return new TypeDefs();
    }

    /**
     * Create an instance of {@link SwitchDesc }
     * 
     */
    public SwitchDesc createSwitchDesc() {
        return new SwitchDesc();
    }

    /**
     * Create an instance of {@link From }
     * 
     */
    public From createFrom() {
        return new From();
    }

    /**
     * Create an instance of {@link Assembly }
     * 
     */
    public Assembly createAssembly() {
        return new Assembly();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link Switch }
     * 
     */
    public Switch createSwitch() {
        return new Switch();
    }

    /**
     * Create an instance of {@link EntryFunc }
     * 
     */
    public EntryFunc createEntryFunc() {
        return new EntryFunc();
    }

    /**
     * Create an instance of {@link SwitchCondition }
     * 
     */
    public SwitchCondition createSwitchCondition() {
        return new SwitchCondition();
    }

    /**
     * Create an instance of {@link Condition }
     * 
     */
    public Condition createCondition() {
        return new Condition();
    }

    /**
     * Create an instance of {@link Clock }
     * 
     */
    public Clock createClock() {
        return new Clock();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Application }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "APPLICATION")
    public JAXBElement<Application> createAPPLICATION(Application value) {
        return new JAXBElement<Application>(_APPLICATION_QNAME, Application.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "comment")
    public JAXBElement<String> createComment(String value) {
        return new JAXBElement<String>(_Comment_QNAME, String.class, null, value);
    }
}
