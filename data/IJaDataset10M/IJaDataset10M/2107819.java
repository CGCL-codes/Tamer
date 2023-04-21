package org.corrib.jonto.tagging.db.access;

import org.corrib.jonto.tagging.S3B_MULTIMEDIATAGGING;
import org.corrib.jonto.tagging.beans.Clipping;
import org.corrib.jonto.tagging.beans.RectangleROI;
import org.corrib.jonto.tagging.db.StatementFinder;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.vocabulary.XSD;

/**
 * Class for accessing RectangleROI specific properties
 * @author Jakub Demczuk
 *
 */
public class RectangleROI_DBAccess extends ROI_DBAccess {

    public RectangleROI_DBAccess() {
    }

    /**
	 * Method loads width and height properties of RectangleROI objects
	 * @see org.corrib.jonto.tagging.db.access.ROI_DBAccess#fillClipping(org.ontoware.rdf2go.model.Model, org.corrib.jonto.tagging.beans.Clipping)
	 */
    @Override
    protected void performGet(Model model, Clipping clipping) {
        RectangleROI rec = (RectangleROI) clipping;
        Resource subject = clipping.getURI();
        URI predicate = S3B_MULTIMEDIATAGGING.HAS_WIDTH.asURI();
        Statement statement = StatementFinder.findStatement(model, subject, predicate, null);
        if (statement != null) {
            rec.setWidth(Integer.parseInt(statement.getObject().asDatatypeLiteral().getValue()));
        }
        predicate = S3B_MULTIMEDIATAGGING.HAS_HEIGHT.asURI();
        statement = StatementFinder.findStatement(model, subject, predicate, null);
        if (statement != null) {
            rec.setHeight(Integer.parseInt(statement.getObject().asDatatypeLiteral().getValue()));
        }
    }

    /**
	 * Method stores width and height properties of the RectangleROIs 
	 * @see org.corrib.jonto.tagging.db.access.ROI_DBAccess#storeClipping(org.ontoware.rdf2go.model.Model, org.corrib.jonto.tagging.beans.Clipping)
	 */
    @Override
    protected void performAdd(Model model, Clipping clipping) {
        RectangleROI rec = (RectangleROI) clipping;
        URI clippingURI = rec.getURI();
        URI predicate = S3B_MULTIMEDIATAGGING.TYPE.asURI();
        model.addStatement(clippingURI, predicate, S3B_MULTIMEDIATAGGING.RECTANGLE_ROI.asURI());
        predicate = S3B_MULTIMEDIATAGGING.HAS_WIDTH.asURI();
        String value = Integer.toString(rec.getWidth());
        model.addStatement(clippingURI, predicate, model.createDatatypeLiteral(value, XSD._int));
        predicate = S3B_MULTIMEDIATAGGING.HAS_HEIGHT.asURI();
        value = Integer.toString(rec.getHeight());
        model.addStatement(clippingURI, predicate, model.createDatatypeLiteral(value, XSD._int));
    }
}
