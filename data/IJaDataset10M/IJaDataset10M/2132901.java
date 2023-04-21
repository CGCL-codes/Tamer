package org.juddi.handler;

import java.util.Vector;
import org.juddi.datatype.Address;
import org.juddi.datatype.AddressLine;
import org.juddi.datatype.CategoryBag;
import org.juddi.datatype.Description;
import org.juddi.datatype.DiscoveryURL;
import org.juddi.datatype.DiscoveryURLs;
import org.juddi.datatype.Email;
import org.juddi.datatype.IdentifierBag;
import org.juddi.datatype.KeyedReference;
import org.juddi.datatype.Name;
import org.juddi.datatype.OverviewDoc;
import org.juddi.datatype.Phone;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.binding.AccessPoint;
import org.juddi.datatype.binding.BindingTemplate;
import org.juddi.datatype.binding.BindingTemplates;
import org.juddi.datatype.binding.HostingRedirector;
import org.juddi.datatype.binding.InstanceDetails;
import org.juddi.datatype.binding.TModelInstanceDetails;
import org.juddi.datatype.binding.TModelInstanceInfo;
import org.juddi.datatype.business.BusinessEntity;
import org.juddi.datatype.business.Contact;
import org.juddi.datatype.business.Contacts;
import org.juddi.datatype.service.BusinessService;
import org.juddi.datatype.service.BusinessServices;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * "Knows about the creation and populating of BusinessEntity objects.
 * Returns BusinessEntity."
 *
 * @author Steve Viens (sviens@users.sourceforge.net)
 * @author Anou Mana (anou_mana@users.sourceforge.net)
 */
public class BusinessEntityHandler extends AbstractHandler {

    public static final String TAG_NAME = "businessEntity";

    private HandlerMaker maker = null;

    protected BusinessEntityHandler(HandlerMaker maker) {
        this.maker = maker;
    }

    public RegistryObject unmarshal(Element element) {
        BusinessEntity obj = new BusinessEntity();
        Vector nodeList = null;
        AbstractHandler handler = null;
        obj.setBusinessKey(element.getAttribute("businessKey"));
        obj.setOperator(element.getAttribute("operator"));
        obj.setAuthorizedName(element.getAttribute("authorizedName"));
        nodeList = XMLUtils.getChildElementsByTagName(element, NameHandler.TAG_NAME);
        for (int i = 0; i < nodeList.size(); i++) {
            handler = maker.lookup(NameHandler.TAG_NAME);
            obj.addName((Name) handler.unmarshal((Element) nodeList.elementAt(i)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, DescriptionHandler.TAG_NAME);
        for (int i = 0; i < nodeList.size(); i++) {
            handler = maker.lookup(DescriptionHandler.TAG_NAME);
            obj.addDescription((Description) handler.unmarshal((Element) nodeList.elementAt(i)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, DiscoveryURLsHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(DiscoveryURLsHandler.TAG_NAME);
            obj.setDiscoveryURLs((DiscoveryURLs) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, ContactsHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(ContactsHandler.TAG_NAME);
            obj.setContacts((Contacts) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, BusinessServicesHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(BusinessServicesHandler.TAG_NAME);
            obj.setBusinessServices((BusinessServices) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, IdentifierBagHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(IdentifierBagHandler.TAG_NAME);
            obj.setIdentifierBag((IdentifierBag) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, CategoryBagHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(CategoryBagHandler.TAG_NAME);
            obj.setCategoryBag((CategoryBag) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        return obj;
    }

    public void marshal(RegistryObject object, Element parent) {
        BusinessEntity business = (BusinessEntity) object;
        Element element = parent.getOwnerDocument().createElement(TAG_NAME);
        AbstractHandler handler = null;
        String businessKey = business.getBusinessKey();
        if (businessKey != null) element.setAttribute("businessKey", businessKey);
        String operator = business.getOperator();
        if (operator != null) element.setAttribute("operator", operator);
        String authName = business.getAuthorizedName();
        if (authName != null) element.setAttribute("authorizedName", authName);
        DiscoveryURLs discURLs = business.getDiscoveryURLs();
        if (discURLs != null) {
            handler = maker.lookup(DiscoveryURLsHandler.TAG_NAME);
            handler.marshal(discURLs, element);
        }
        Vector nameVector = business.getNameVector();
        if ((nameVector != null) && (nameVector.size() > 0)) {
            handler = maker.lookup(NameHandler.TAG_NAME);
            for (int i = 0; i < nameVector.size(); i++) handler.marshal((Name) nameVector.elementAt(i), element);
        }
        Vector descrVector = business.getDescriptionVector();
        if ((descrVector != null) && (descrVector.size() > 0)) {
            handler = maker.lookup(DescriptionHandler.TAG_NAME);
            for (int i = 0; i < descrVector.size(); i++) handler.marshal((Description) descrVector.elementAt(i), element);
        }
        Contacts contacts = business.getContacts();
        if (contacts != null) {
            handler = maker.lookup(ContactsHandler.TAG_NAME);
            handler.marshal(contacts, element);
        }
        BusinessServices services = business.getBusinessServices();
        if (services != null) {
            handler = maker.lookup(BusinessServicesHandler.TAG_NAME);
            handler.marshal(services, element);
        }
        IdentifierBag identifierBag = business.getIdentifierBag();
        if (identifierBag != null) {
            handler = maker.lookup(IdentifierBagHandler.TAG_NAME);
            handler.marshal(identifierBag, element);
        }
        CategoryBag categoryBag = business.getCategoryBag();
        if (categoryBag != null) {
            handler = maker.lookup(CategoryBagHandler.TAG_NAME);
            handler.marshal(categoryBag, element);
        }
        parent.appendChild(element);
    }

    /***************************************************************************/
    public static void main(String args[]) throws Exception {
        HandlerMaker maker = HandlerMaker.getInstance();
        AbstractHandler handler = maker.lookup(BusinessEntityHandler.TAG_NAME);
        Element parent = XMLUtils.newRootElement();
        Element child = null;
        OverviewDoc overDoc = new OverviewDoc();
        overDoc.setOverviewURL("http://www.sviens.com/service.html");
        overDoc.addDescription(new Description("over-doc Descr"));
        overDoc.addDescription(new Description("over-doc Descr Two", "en"));
        InstanceDetails instDetails = new InstanceDetails();
        instDetails.addDescription(new Description("body-isa-wunder"));
        instDetails.addDescription(new Description("sweetchild-o-mine", "it"));
        instDetails.setInstanceParms("inst-det parameters");
        instDetails.setOverviewDoc(overDoc);
        TModelInstanceInfo tModInstInfo = new TModelInstanceInfo();
        tModInstInfo.setTModelKey("uuid:ae0f9fd4-287f-40c9-be91-df47a7c72fd9");
        tModInstInfo.addDescription(new Description("tMod-Inst-Info"));
        tModInstInfo.addDescription(new Description("tMod-Inst-Info too", "es"));
        tModInstInfo.setInstanceDetails(instDetails);
        TModelInstanceDetails tModInstDet = new TModelInstanceDetails();
        tModInstDet.addTModelInstanceInfo(tModInstInfo);
        BindingTemplate binding = new BindingTemplate();
        binding.setBindingKey("c9613c3c-fe55-4f34-a3da-b3167afbca4a");
        binding.setServiceKey("997a55bc-563d-4c04-8a94-781604870d31");
        binding.addDescription(new Description("whatever"));
        binding.addDescription(new Description("whatever too", "fr"));
        binding.setHostingRedirector(new HostingRedirector("92658289-0bd7-443c-8948-0bb4460b44c0"));
        binding.setAccessPoint(new AccessPoint(AccessPoint.HTTP, "http://www.sviens.com/service.wsdl"));
        binding.setTModelInstanceDetails(tModInstDet);
        BindingTemplates bindings = new BindingTemplates();
        bindings.addBindingTemplate(binding);
        CategoryBag catBag = new CategoryBag();
        catBag.addKeyedReference(new KeyedReference("catBagKeyName", "catBagKeyValue"));
        catBag.addKeyedReference(new KeyedReference("uuid:dfddb58c-4853-4a71-865c-f84509017cc7", "catBagKeyName2", "catBagKeyValue2"));
        IdentifierBag idBag = new IdentifierBag();
        idBag.addKeyedReference(new KeyedReference("idBagKeyName", "idBagkeyValue"));
        idBag.addKeyedReference(new KeyedReference("uuid:f78a135a-4769-4e79-8604-54d440314bc0", "idBagKeyName2", "idBagkeyValue2"));
        DiscoveryURLs discURLs = new DiscoveryURLs();
        discURLs.addDiscoveryURL(new DiscoveryURL("http", "http://www.sviens.com/service.wsdl"));
        discURLs.addDiscoveryURL(new DiscoveryURL("https", "https://www.sviens.com/service.wsdl"));
        discURLs.addDiscoveryURL(new DiscoveryURL("smtp", "servicemngr@sviens.com"));
        Address address = new Address();
        address.setUseType("myAddressUseType");
        address.setSortCode("sortThis");
        address.setTModelKey(null);
        address.addAddressLine(new AddressLine("AddressLine1", "keyNameAttr", "keyValueAttr"));
        address.addAddressLine(new AddressLine("AddressLine2"));
        Contact contact = new Contact();
        contact.setUseType("myAddressUseType");
        contact.setPersonNameValue("Bob Whatever");
        contact.addDescription(new Description("Bob is a big fat jerk"));
        contact.addDescription(new Description("obBay sIay a igBay atFay erkJay", "es"));
        contact.addEmail(new Email("bob@whatever.com"));
        contact.addPhone(new Phone("(603)559-1901"));
        contact.addAddress(address);
        Contacts contacts = new Contacts();
        contacts.addContact(contact);
        BusinessService service = new BusinessService();
        service.setServiceKey("fe8af00d-3a2c-4e05-b7ca-95a1259aad4f");
        service.setBusinessKey("b8cc7266-9eed-4675-b621-34697f252a77");
        service.setBindingTemplates(bindings);
        service.setCategoryBag(catBag);
        service.addName(new Name("serviceNm"));
        service.addName(new Name("serviceNm2", "en"));
        service.addDescription(new Description("service whatever"));
        service.addDescription(new Description("service whatever too", "it"));
        BusinessServices services = new BusinessServices();
        services.addBusinessService(service);
        services.addBusinessService(service);
        BusinessEntity business = new BusinessEntity();
        business.setBusinessKey("6c0ac186-d36b-4b81-bd27-066a5fe0fc1f");
        business.setAuthorizedName("Steve Viens");
        business.setOperator("jUDDI");
        business.addName(new Name("businessNm"));
        business.addName(new Name("businessNm2", "en"));
        business.addDescription(new Description("business whatever"));
        business.addDescription(new Description("business whatever too", "fr"));
        business.setDiscoveryURLs(discURLs);
        business.setCategoryBag(catBag);
        business.setIdentifierBag(idBag);
        business.setContacts(contacts);
        business.setBusinessServices(services);
        System.out.println();
        RegistryObject regObject = business;
        handler.marshal(regObject, parent);
        child = (Element) parent.getFirstChild();
        parent.removeChild(child);
        XMLUtils.writeXML(child, System.out);
        System.out.println();
        regObject = handler.unmarshal(child);
        handler.marshal(regObject, parent);
        child = (Element) parent.getFirstChild();
        parent.removeChild(child);
        XMLUtils.writeXML(child, System.out);
    }
}
