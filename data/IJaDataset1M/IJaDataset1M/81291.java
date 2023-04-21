package at.ac.tuwien.vitalab.hrcrm.test;

import javax.xml.bind.JAXBElement;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import at.ac.tuwien.vitalab.hrcrm.dto.address.AddressType;
import at.ac.tuwien.vitalab.hrcrm.dto.name.PartyNameType;
import at.ac.tuwien.vitalab.hrcrm.dto.party.AddAddressToPartyRequest;
import at.ac.tuwien.vitalab.hrcrm.dto.party.AddAddressToPartyResponse;
import at.ac.tuwien.vitalab.hrcrm.dto.party.AddNameToPartyRequest;
import at.ac.tuwien.vitalab.hrcrm.dto.party.AddNameToPartyResponse;
import at.ac.tuwien.vitalab.hrcrm.dto.party.PartyType;

public class PartyWebServiceTest {

    private ClassPathXmlApplicationContext applicationContext;

    private WebServiceTemplate partyWSTemplate;

    private WebServiceTemplate addressWSTemplate;

    private WebServiceTemplate nameWSTemplate;

    private at.ac.tuwien.vitalab.hrcrm.dto.party.ObjectFactory partyObjectFactory;

    private at.ac.tuwien.vitalab.hrcrm.dto.address.ObjectFactory addressObjectFactory;

    private at.ac.tuwien.vitalab.hrcrm.dto.name.ObjectFactory nameObjectFactory;

    @SuppressWarnings("unchecked")
    @BeforeClass(groups = "Functional-Test")
    public void setUp() throws Exception {
        this.applicationContext = new ClassPathXmlApplicationContext("party-client.xml");
        this.partyWSTemplate = (WebServiceTemplate) this.applicationContext.getBean("partyWSTemplate");
        this.nameWSTemplate = (WebServiceTemplate) this.applicationContext.getBean("nameWSTemplate");
        this.addressWSTemplate = (WebServiceTemplate) this.applicationContext.getBean("addressWSTemplate");
        this.partyObjectFactory = (at.ac.tuwien.vitalab.hrcrm.dto.party.ObjectFactory) this.applicationContext.getBean("partyObjectFactory");
        this.addressObjectFactory = (at.ac.tuwien.vitalab.hrcrm.dto.address.ObjectFactory) this.applicationContext.getBean("addressObjectFactory");
        this.nameObjectFactory = (at.ac.tuwien.vitalab.hrcrm.dto.name.ObjectFactory) this.applicationContext.getBean("nameObjectFactory");
        AddressType addressType = this.createAddressType();
        JAXBElement<AddressType> addressRequest = this.addressObjectFactory.createAddAddressRequest(addressType);
        JAXBElement<String> addressResponse = (JAXBElement<String>) this.addressWSTemplate.marshalSendAndReceive(addressRequest);
        Assert.assertNotNull(addressResponse);
        Assert.assertEquals(addressResponse.getValue(), "OK");
        PartyNameType partyNameType = this.createPartyNameType();
        JAXBElement<PartyNameType> nameRequest = this.nameObjectFactory.createAddPartyNameRequest(partyNameType);
        JAXBElement<String> nameResponse = (JAXBElement<String>) this.nameWSTemplate.marshalSendAndReceive(nameRequest);
        Assert.assertNotNull(nameResponse);
        Assert.assertEquals(nameResponse.getValue(), "OK");
    }

    @SuppressWarnings("unchecked")
    @AfterClass(groups = "Functional-Test")
    public void tearDown() throws Exception {
        JAXBElement<String> addressRequest = this.addressObjectFactory.createDeleteAddressRequest("997799");
        JAXBElement<String> addressResponse = (JAXBElement<String>) this.addressWSTemplate.marshalSendAndReceive(addressRequest);
        Assert.assertNotNull(addressResponse);
        Assert.assertEquals(addressResponse.getValue(), "OK");
        JAXBElement<String> nameRequest = this.nameObjectFactory.createDeletePartyNameRequest("997799");
        JAXBElement<String> nameResponse = (JAXBElement<String>) this.nameWSTemplate.marshalSendAndReceive(nameRequest);
        Assert.assertNotNull(nameResponse);
        Assert.assertEquals(nameResponse.getValue(), "OK");
        this.applicationContext.close();
        this.applicationContext.destroy();
        this.applicationContext = null;
    }

    @SuppressWarnings("unchecked")
    @Test(groups = "Functional-Test")
    public void testAddParty() {
        PartyType partyType = this.createPartyType();
        JAXBElement<PartyType> request = this.partyObjectFactory.createAddPartyRequest(partyType);
        JAXBElement<String> response = (JAXBElement<String>) this.partyWSTemplate.marshalSendAndReceive(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getValue(), "OK");
    }

    @SuppressWarnings("unchecked")
    @Test(groups = "Functional-Test", dependsOnMethods = "testAddParty")
    public void testGetParty() {
        JAXBElement<String> request = this.partyObjectFactory.createGetPartyRequest("998899");
        JAXBElement<PartyType> response = (JAXBElement<PartyType>) this.partyWSTemplate.marshalSendAndReceive(request);
        Assert.assertNotNull(response);
        PartyType partyType = response.getValue();
        Assert.assertEquals(partyType.getPartyID(), String.valueOf(998899));
        Assert.assertEquals(partyType.getHref(), "http://www.xyz.com");
        Assert.assertEquals(partyType.getStatus(), "public");
    }

    @Test(groups = "Functional-Test", dependsOnMethods = "testGetParty")
    public void testAddAddressToParty() {
        AddAddressToPartyRequest request = this.partyObjectFactory.createAddAddressToPartyRequest();
        request.setAddressId("997799");
        request.setPartyId("998899");
        AddAddressToPartyResponse response = (AddAddressToPartyResponse) this.partyWSTemplate.marshalSendAndReceive(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getAddressId(), "997799");
        Assert.assertEquals(response.getPartyId(), "998899");
        Assert.assertEquals(response.isAdded(), true);
    }

    @Test(groups = "Functional-Test", dependsOnMethods = "testGetParty")
    public void testAddNameToParty() {
        AddNameToPartyRequest request = this.partyObjectFactory.createAddNameToPartyRequest();
        request.setNameId("997799");
        request.setPartyId("998899");
        AddNameToPartyResponse response = (AddNameToPartyResponse) this.partyWSTemplate.marshalSendAndReceive(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getNameId(), "997799");
        Assert.assertEquals(response.getPartyId(), "998899");
        Assert.assertEquals(response.isAdded(), true);
    }

    @SuppressWarnings("unchecked")
    @Test(groups = "Functional-Test", dependsOnMethods = { "testAddAddressToParty", "testAddNameToParty" })
    public void testDeletePartyName() {
        JAXBElement<String> request = this.partyObjectFactory.createDeletePartyRequest("998899");
        JAXBElement<String> response = (JAXBElement<String>) this.partyWSTemplate.marshalSendAndReceive(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getValue(), "OK");
    }

    /**
	 * Write access to private field.
	 * @param webServiceTemplate
	 *            The webServiceTemplate to set.
	 */
    public void setWebServiceTemplate(final WebServiceTemplate webServiceTemplate) {
        this.partyWSTemplate = webServiceTemplate;
    }

    private PartyType createPartyType() {
        PartyType partyType = this.partyObjectFactory.createPartyType();
        partyType.setPartyID("998899");
        partyType.setStatus("public");
        partyType.setHref("http://www.xyz.com");
        return partyType;
    }

    private AddressType createAddressType() {
        AddressType addressType = this.addressObjectFactory.createAddressType();
        addressType.setAddressID("997799");
        return addressType;
    }

    private PartyNameType createPartyNameType() {
        PartyNameType partyNameType = this.nameObjectFactory.createPartyNameType();
        partyNameType.setPartyNameID("997799");
        partyNameType.setHref("Vassil");
        partyNameType.setStatus("Nikolov");
        partyNameType.setUsage("25");
        return partyNameType;
    }
}
