package cartagows.wsframework.wsatomictransaction;

import java.io.Serializable;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import cartagows.wsframework.core.IOperationContent;

public class ATCreateActivityResultContent implements IOperationContent, Serializable {

    private static final long serialVersionUID = 1L;

    private String coordEPR;

    private String coordContext;

    public ATCreateActivityResultContent(String coordEPR, OMElement coordContext) {
        this.coordContext = coordContext.toString();
        this.coordEPR = coordEPR;
    }

    public String getCoordEPR() {
        return coordEPR;
    }

    public OMElement getCoordContext() {
        OMElement returnValue = null;
        try {
            returnValue = AXIOMUtil.stringToOM(coordContext);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
