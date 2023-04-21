package net.sourceforge.ondex.core.soapgraph;

import javax.xml.bind.JAXBElement;
import net.sourceforge.ondex.core.Hierarchy;
import net.sourceforge.ondex.core.MetaData;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.webservice.client.ONDEXapiWS;
import net.sourceforge.ondex.webservice.client.WebserviceException_Exception;

/**
 * Wrapper to represent SOAP RelationType.
 * 
 * @author taubertj
 * 
 */
public class SOAPRelationType implements RelationType {

    /**
	 * unique graph id
	 */
    Long graphId = null;

    /**
	 * RelationType id as key
	 */
    String id = null;

    /**
	 * back reference to parent SOAP graph
	 */
    SOAPONDEXGraph parent;

    /**
	 * connected web service graph
	 */
    ONDEXapiWS soapGraph = null;

    /**
	 * Initialises back references to soap graph for RelationType.
	 * 
	 * @param parent
	 *            parent SOAPGraph
	 * @param rtid
	 *            RelationType id
	 */
    public SOAPRelationType(SOAPONDEXGraph parent, String rtid) {
        this.parent = parent;
        soapGraph = parent.soapGraph;
        graphId = parent.graphId;
        id = rtid;
    }

    @Override
    public int compareTo(MetaData o) {
        if (o instanceof RelationType) return id.compareTo(o.getId());
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SOAPRelationType) {
            return id.equals(((SOAPRelationType) obj).id);
        }
        return false;
    }

    @Override
    public String getDescription() {
        try {
            return soapGraph.getRelationType(graphId, id).getDescription().getValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFullname() {
        try {
            return soapGraph.getRelationType(graphId, id).getFullname().getValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getInverseName() {
        try {
            return soapGraph.getRelationType(graphId, id).getInverseName().getValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getSID() {
        return graphId;
    }

    @Override
    public RelationType getSpecialisationOf() {
        String rtid;
        try {
            rtid = soapGraph.getRelationType(graphId, id).getSpecialisationOf().getValue();
            if (rtid != null && rtid.length() > 0) return new SOAPRelationType(parent, rtid);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean isAntisymmetric() {
        try {
            return soapGraph.getRelationType(graphId, id).isAntisymmetric().booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isAssignableFrom(RelationType possibleDescendant) {
        return Hierarchy.Helper.transitiveParent(this, possibleDescendant);
    }

    @Override
    public boolean isAssignableTo(RelationType possibleAncestor) {
        return Hierarchy.Helper.transitiveParent(possibleAncestor, this);
    }

    @Override
    public boolean isReflexive() {
        try {
            return soapGraph.getRelationType(graphId, id).isReflexive().booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isSymmetric() {
        try {
            return soapGraph.getRelationType(graphId, id).isSymmetric().booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isTransitiv() {
        try {
            return soapGraph.getRelationType(graphId, id).isTransitiv().booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setAntisymmetric(boolean isAntisymmetric) {
        try {
            soapGraph.getRelationType(graphId, id).setAntisymmetric(isAntisymmetric);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setDescription(String description) {
        try {
            JAXBElement<String> old = soapGraph.getRelationType(graphId, id).getDescription();
            old.setValue(description);
            soapGraph.getRelationType(graphId, id).setDescription(old);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setFullname(String fullname) {
        try {
            JAXBElement<String> old = soapGraph.getRelationType(graphId, id).getFullname();
            old.setValue(fullname);
            soapGraph.getRelationType(graphId, id).setFullname(old);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setInverseName(String inverseName) {
        try {
            JAXBElement<String> old = soapGraph.getRelationType(graphId, id).getInverseName();
            old.setValue(inverseName);
            soapGraph.getRelationType(graphId, id).setInverseName(old);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setReflexive(boolean isReflexive) {
        try {
            soapGraph.getRelationType(graphId, id).setReflexive(isReflexive);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setSpecialisationOf(RelationType specialisationOf) {
        try {
            JAXBElement<String> old = soapGraph.getRelationType(graphId, id).getSpecialisationOf();
            old.setValue(specialisationOf.getId());
            soapGraph.getRelationType(graphId, id).setSpecialisationOf(old);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setSymmetric(boolean isSymmetric) {
        try {
            soapGraph.getRelationType(graphId, id).setSymmetric(isSymmetric);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setTransitiv(boolean isTransitiv) {
        try {
            soapGraph.getRelationType(graphId, id).setTransitiv(isTransitiv);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return id;
    }
}
