package test.dicom4j.network.support;

import java.util.Iterator;
import org.dicom4j.dicom.uniqueidentifiers.SOPClass;
import org.dicom4j.network.NetworkStaticProperties;
import org.dicom4j.network.association.Association;
import org.dicom4j.network.association.associate.AssociateRequest;
import org.dicom4j.network.association.associate.AssociateResponse;
import org.dicom4j.network.association.listeners.defaults.DefaultAssociateRequestHandler;
import org.dicom4j.network.dimse.DimseServiceBroker;
import org.dicom4j.network.dimse.DimseServicesManager;
import org.dicom4j.network.dimse.messages.support.AbstractDimseMessage;
import org.dicom4j.network.dimse.services.DimseService;
import org.dicom4j.network.dimse.services.VerificationSCPService;
import org.dicom4j.network.protocoldataunit.items.PresentationContextItemRQ;
import org.dicom4j.network.protocoldataunit.support.PresentationContextItem;
import org.dolmen.core.lang.thread.WorkerHandler;

/**
 * 
 * 
 * @since Alpha 0.0.3
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class CEchoSCP extends MockServer {

    public class MyAssociateRequestHandler extends DefaultAssociateRequestHandler {

        @Override
        public AssociateResponse requestReceived(Association aAssociation, AssociateRequest aAssociateRequest) {
            AssociateResponse lReponse = createDefaultResponse(aAssociateRequest);
            Iterator<PresentationContextItem> lPres = aAssociateRequest.getPresentationIterator();
            while (lPres.hasNext()) {
                PresentationContextItemRQ lPresRQ = (PresentationContextItemRQ) lPres.next();
                if (lPresRQ.getAbstractSyntax().equals(SOPClass.Verification.getUID())) {
                    lReponse.addPresentationContext(lPresRQ.getID(), NetworkStaticProperties.PresentationContextReasons.ACCEPTANCE, lPresRQ.getTransferSyntax(0));
                } else {
                    lReponse.addPresentationContext(lPresRQ.getID(), NetworkStaticProperties.PresentationContextReasons.USER_REJECTION, lPresRQ.getTransferSyntax(0));
                }
            }
            return lReponse;
        }
    }

    public class MyDimseServicesManager implements DimseServicesManager {

        public DimseService borrowService(Association aAssociation, AbstractDimseMessage aMessage) throws Exception {
            if (aMessage.isCEchoRequest()) {
                return new VerificationSCPService();
            } else {
                return null;
            }
        }

        public void returnService(DimseService aService) {
            aService = null;
        }
    }

    public CEchoSCP(WorkerHandler aWorkerHandler) {
        super(aWorkerHandler);
    }

    @Override
    public void start() throws Exception {
        DimseServiceBroker lBroker = new DimseServiceBroker();
        lBroker.setDimseServicesManager(new MyDimseServicesManager());
        AssociateRequestHandler = new MyAssociateRequestHandler();
        AssociationListener = lBroker;
        super.start();
    }
}
