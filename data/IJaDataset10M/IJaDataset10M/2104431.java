package net.sourceforge.ondex.core.soapgraph;

import java.util.HashSet;
import java.util.Set;
import javax.xml.ws.Holder;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.DataSource;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.MetaDataFactory;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXGraphMetaData;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.Unit;
import net.sourceforge.ondex.core.util.BitSetFunctions;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.webservice.client.ONDEXapiWS;
import net.sourceforge.ondex.webservice.client.WSAttributeName;
import net.sourceforge.ondex.webservice.client.WSDataSource;
import net.sourceforge.ondex.webservice.client.WSConceptClass;
import net.sourceforge.ondex.webservice.client.WSEvidenceType;
import net.sourceforge.ondex.webservice.client.WSRelationType;
import net.sourceforge.ondex.webservice.client.WSUnit;
import net.sourceforge.ondex.webservice.client.WebserviceException_Exception;

/**
 * Wrapper to represent SOAP meta data.
 * 
 * @author taubertj
 */
public class SOAPONDEXGraphMetaData implements ONDEXGraphMetaData {

    /**
	 * unique graph id
	 */
    Long graphId = null;

    /**
	 * back reference to parent SOAP graph
	 */
    SOAPONDEXGraph parent = null;

    /**
	 * connected web service graph
	 */
    ONDEXapiWS soapGraph = null;

    /**
	 * Initialises back references to soap graph for meta data.
	 * 
	 * @param parent
	 *            parent SOAPGraph
	 */
    public SOAPONDEXGraphMetaData(SOAPONDEXGraph parent) {
        this.parent = parent;
        soapGraph = parent.soapGraph;
        graphId = parent.graphId;
    }

    @Override
    public void associateGraph(ONDEXGraph g) throws AccessDeniedException {
        if (g instanceof SOAPONDEXGraph) {
            parent = (SOAPONDEXGraph) g;
            soapGraph = parent.soapGraph;
            graphId = parent.graphId;
        }
    }

    @Override
    public boolean checkAttributeName(String id) {
        try {
            return soapGraph.checkAttributeName(graphId, id).booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkDataSource(String id) {
        try {
            return soapGraph.checkDataSource(graphId, id).booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkConceptClass(String id) {
        try {
            return soapGraph.checkConceptClass(graphId, id).booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkEvidenceType(String id) {
        try {
            return soapGraph.checkEvidenceType(graphId, id).booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkRelationType(String id) {
        try {
            return soapGraph.checkRelationType(graphId, id).booleanValue();
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkUnit(String id) {
        try {
            return soapGraph.checkUnit(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AttributeName createAttributeName(String id, String fullname, String description, Unit unit, Class<?> datatype, AttributeName specialisationOf) {
        try {
            String specialisationOfId = null;
            if (specialisationOf != null) specialisationOfId = specialisationOf.getId();
            String unitId = null;
            if (unit != null) unitId = unit.getId();
            soapGraph.createAttributeName(graphId, id, fullname, description, unitId, datatype.getName(), specialisationOfId);
            return new SOAPAttributeName(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DataSource createDataSource(String id, String fullname, String description) {
        try {
            soapGraph.createDataSource(graphId, new Holder<String>(id), fullname, description);
            return new SOAPDataSource(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ConceptClass createConceptClass(String id, String fullname, String description, ConceptClass specialisationOf) {
        try {
            String specialisationOfId = null;
            if (specialisationOf != null) specialisationOfId = specialisationOf.getId();
            soapGraph.createConceptClass(graphId, new Holder<String>(id), fullname, description, specialisationOfId);
            return new SOAPConceptClass(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EvidenceType createEvidenceType(String id, String fullname, String description) {
        try {
            soapGraph.createEvidenceType(graphId, new Holder<String>(id), fullname, description);
            return new SOAPEvidenceType(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RelationType createRelationType(String id, String fullname, String description, String inverseName, boolean isAntisymmetric, boolean isReflexive, boolean isSymmetric, boolean isTransitiv, RelationType specialisationOf) {
        try {
            String specialisationOfId = null;
            if (specialisationOf != null) specialisationOfId = specialisationOf.getId();
            soapGraph.createRelationType(graphId, new Holder<String>(id), fullname, description, inverseName, isAntisymmetric, isReflexive, isSymmetric, isTransitiv, specialisationOfId);
            return new SOAPRelationType(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Unit createUnit(String id, String fullname, String description) {
        try {
            soapGraph.createUnit(graphId, new Holder<String>(id), fullname, description);
            return new SOAPUnit(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteAttributeName(String id) {
        try {
            return soapGraph.deleteAttributeName(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDataSource(String id) {
        try {
            return soapGraph.deleteDataSource(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteConceptClass(String id) {
        try {
            return soapGraph.deleteConceptClass(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteEvidenceType(String id) {
        try {
            return soapGraph.deleteEvidenceType(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRelationType(String id) {
        try {
            return soapGraph.deleteRelationType(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUnit(String id) {
        try {
            return soapGraph.deleteUnit(graphId, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AttributeName getAttributeName(String id) {
        try {
            if (soapGraph.getAttributeName(graphId, id) != null) return new SOAPAttributeName(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<AttributeName> getAttributeNames() {
        Set<AttributeName> set = new HashSet<AttributeName>();
        try {
            for (WSAttributeName wsan : soapGraph.getAttributeNames(graphId).getWSAttributeName()) {
                set.add(new SOAPAttributeName(parent, wsan.getId().getValue()));
            }
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return BitSetFunctions.unmodifiableSet(set);
    }

    @Override
    public DataSource getDataSource(String id) {
        try {
            if (soapGraph.getDataSource(graphId, id) != null) return new SOAPDataSource(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<DataSource> getDataSources() {
        Set<DataSource> set = new HashSet<DataSource>();
        try {
            for (WSDataSource wscv : soapGraph.getDataSources(graphId).getWSDataSource()) {
                set.add(new SOAPDataSource(parent, wscv.getId().getValue()));
            }
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return set;
    }

    @Override
    public ConceptClass getConceptClass(String id) {
        try {
            if (soapGraph.getConceptClass(graphId, id) != null) return new SOAPConceptClass(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<ConceptClass> getConceptClasses() {
        Set<ConceptClass> set = new HashSet<ConceptClass>();
        try {
            for (WSConceptClass wscc : soapGraph.getConceptClasses(graphId).getWSConceptClass()) {
                set.add(new SOAPConceptClass(parent, wscc.getId().getValue()));
            }
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return BitSetFunctions.unmodifiableSet(set);
    }

    @Override
    public EvidenceType getEvidenceType(String id) {
        try {
            if (soapGraph.getEvidenceType(graphId, id) != null) return new SOAPEvidenceType(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<EvidenceType> getEvidenceTypes() {
        Set<EvidenceType> set = new HashSet<EvidenceType>();
        try {
            for (WSEvidenceType wset : soapGraph.getEvidenceTypes(graphId).getWSEvidenceType()) {
                set.add(new SOAPEvidenceType(parent, wset.getId().getValue()));
            }
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return BitSetFunctions.unmodifiableSet(set);
    }

    @Override
    public MetaDataFactory getFactory() {
        return new MetaDataFactory(this);
    }

    @Override
    public RelationType getRelationType(String id) {
        try {
            if (soapGraph.getRelationType(graphId, id) != null) return new SOAPRelationType(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<RelationType> getRelationTypes() {
        Set<RelationType> set = new HashSet<RelationType>();
        try {
            for (WSRelationType wsrt : soapGraph.getRelationTypes(graphId).getWSRelationType()) {
                set.add(new SOAPRelationType(parent, wsrt.getId().getValue()));
            }
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return BitSetFunctions.unmodifiableSet(set);
    }

    @Override
    public Unit getUnit(String id) {
        try {
            if (soapGraph.getUnit(graphId, id) != null) return new SOAPUnit(parent, id);
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Unit> getUnits() {
        Set<Unit> set = new HashSet<Unit>();
        try {
            for (WSUnit wsunit : soapGraph.getUnits(graphId).getWSUnit()) {
                set.add(new SOAPUnit(parent, wsunit.getId().getValue()));
            }
        } catch (WebserviceException_Exception e) {
            parent.fireActionEvent(e, e.getMessage());
            e.printStackTrace();
        }
        return BitSetFunctions.unmodifiableSet(set);
    }

    @Override
    public long getSID() {
        return graphId;
    }
}
