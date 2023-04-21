package at.ac.tuwien.vitalab.hrcrm.ws.party;

import javax.xml.bind.JAXBElement;

public class DeletePartyEndpointImpl extends AbstractPartyEndpoint {

    @Override
    @SuppressWarnings("unchecked")
    protected Object invokeInternal(Object request) throws Exception {
        return this.partyService.deleteParty(((JAXBElement<String>) request).getValue());
    }
}
