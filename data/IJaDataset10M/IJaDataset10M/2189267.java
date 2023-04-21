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
import org.juddi.datatype.UploadRegister;
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
import org.juddi.datatype.request.AuthInfo;
import org.juddi.datatype.request.SaveBusiness;
import org.juddi.datatype.service.BusinessService;
import org.juddi.datatype.service.BusinessServices;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author Steve Viens (sviens@users.sourceforge.net)
 * @author Anou Mana (anou_mana@users.sourceforge.net)
 */
public class SaveBusinessHandler extends AbstractHandler {

    public static final String TAG_NAME = "save_business";

    private HandlerMaker maker = null;

    protected SaveBusinessHandler(HandlerMaker maker) {
        this.maker = maker;
    }

    public RegistryObject unmarshal(Element element) {
        SaveBusiness obj = new SaveBusiness();
        Vector nodeList = null;
        AbstractHandler handler = null;
        String generic = element.getAttribute("generic");
        if ((generic != null && (generic.trim().length() > 0))) obj.setGeneric(generic);
        nodeList = XMLUtils.getChildElementsByTagName(element, AuthInfoHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(AuthInfoHandler.TAG_NAME);
            obj.setAuthInfo((AuthInfo) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, BusinessEntityHandler.TAG_NAME);
        for (int i = 0; i < nodeList.size(); i++) {
            handler = maker.lookup(BusinessEntityHandler.TAG_NAME);
            obj.addBusinessEntity((BusinessEntity) handler.unmarshal((Element) nodeList.elementAt(i)));
        }
        nodeList = XMLUtils.getChildElementsByTagName(element, UploadRegisterHandler.TAG_NAME);
        for (int i = 0; i < nodeList.size(); i++) {
            handler = maker.lookup(UploadRegisterHandler.TAG_NAME);
            obj.addUploadRegister((UploadRegister) handler.unmarshal((Element) nodeList.elementAt(i)));
        }
        return obj;
    }

    public void marshal(RegistryObject object, Element parent) {
        SaveBusiness request = (SaveBusiness) object;
        Element element = parent.getOwnerDocument().createElement(TAG_NAME);
        AbstractHandler handler = null;
        String generic = request.getGeneric();
        if (generic != null) element.setAttribute("generic", generic);
        AuthInfo authInfo = request.getAuthInfo();
        if (authInfo != null) {
            handler = maker.lookup(AuthInfoHandler.TAG_NAME);
            handler.marshal(authInfo, element);
        }
        Vector businessVector = request.getBusinessEntityVector();
        if ((businessVector != null) && (businessVector.size() > 0)) {
            handler = maker.lookup(BusinessEntityHandler.TAG_NAME);
            for (int i = 0; i < businessVector.size(); i++) handler.marshal((BusinessEntity) businessVector.elementAt(i), element);
        }
        Vector urVector = request.getUploadRegisterVector();
        if ((urVector != null) && (urVector.size() > 0)) {
            handler = maker.lookup(UploadRegisterHandler.TAG_NAME);
            for (int i = 0; i < urVector.size(); i++) handler.marshal((UploadRegister) urVector.elementAt(i), element);
        }
        parent.appendChild(element);
    }

    /***************************************************************************/
    public static void main(String args[]) throws Exception {
        HandlerMaker maker = HandlerMaker.getInstance();
        AbstractHandler handler = maker.lookup(SaveBusinessHandler.TAG_NAME);
        Element parent = XMLUtils.newRootElement();
        Element child = null;
        AuthInfo authInfo = new AuthInfo();
        authInfo.setValue("6f157513-844e-4a95-a856-d257e6ba9726");
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
        SaveBusiness request = new SaveBusiness();
        request.setAuthInfo(authInfo);
        request.addBusinessEntity(business);
        request.addBusinessEntity(business);
        request.addUploadRegister(new UploadRegister("http://www.juddi.org/businessEntity.xml"));
        request.addUploadRegister(new UploadRegister("http://www.sourceforge.net/businessService.xml"));
        request.addUploadRegister(new UploadRegister("http://www.uddi.org/bindingTemplate.xml"));
        System.out.println();
        RegistryObject regObject = request;
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
