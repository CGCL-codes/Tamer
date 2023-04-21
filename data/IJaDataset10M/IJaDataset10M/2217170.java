package bioevent.datapreparation.attributecalculator.edge;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import bioevent.core.AttributeValuePair;
import bioevent.core.AttributeValuePair.AttributeType;
import bioevent.core.Configuration;
import bioevent.core.EdgeExample;
import bioevent.core.Util;
import bioevent.datapreparation.attributecalculator.IEdgeAttributeCalculator;
import bioevent.db.entities.NellTable;
import bioevent.loader.i2b2.ClinicalConcept;

/**
 * @author ehsan
 * 
 */
public class EdgeType implements IEdgeAttributeCalculator {

    @Override
    public int addAttributeValues(EdgeExample edgeToProcess) throws IOException, SQLException, Exception {
        int addedAttributes = 0;
        int edge_type = 0;
        String mode = Configuration.getValue("RelationMode");
        if (mode.equals("BioNLP")) {
        } else if (mode.equals("I2B2")) {
            ClinicalConcept.ConceptTypes concept_type1 = ((ClinicalConcept) edgeToProcess.conceptRelation.getFirstConcept()).conceptType;
            ClinicalConcept.ConceptTypes concept_type2 = ((ClinicalConcept) edgeToProcess.conceptRelation.getSecondConcept()).conceptType;
            edge_type = concept_type1.ordinal() * 100 + concept_type2.ordinal();
        }
        AttributeValuePair value_pair = new AttributeValuePair(AttributeType.EdgeType, edge_type);
        if (edgeToProcess.addOrUpdateFeature(value_pair)) {
            addedAttributes++;
        }
        return addedAttributes;
    }
}
