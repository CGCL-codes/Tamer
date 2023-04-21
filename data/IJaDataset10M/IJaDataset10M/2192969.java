package itGraph.UMLElements;

import java.awt.geom.Point2D;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Element;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;

public class DependencyView extends BasicAssociationView {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4255445207380378350L;

    public DependencyView(Object cell) {
        super(cell);
        float[] pattern = { 10, 10 };
        GraphConstants.setDashPattern(getAttributes(), pattern);
        GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_SIMPLE);
        GraphConstants.setEndSize(getAttributes(), 13);
        Dependency association = (Dependency) cell;
        Element labelsNode = association.getData().getChild("graphics").getChild("labels");
        Element sourceRolename = null;
        Element targetRolename = null;
        Element sourceMultiplicity = null;
        Element targetMultiplicity = null;
        try {
            XPath path = new JDOMXPath("label[@type='rolename' and @identifier='1']/position");
            sourceRolename = (Element) path.selectSingleNode(labelsNode);
            path = new JDOMXPath("label[@type='rolename' and @identifier='2']/position");
            targetRolename = (Element) path.selectSingleNode(labelsNode);
            path = new JDOMXPath("label[@type='multiplicity' and @identifier='1']/position");
            sourceMultiplicity = (Element) path.selectSingleNode(labelsNode);
            path = new JDOMXPath("label[@type='multiplicity' and @identifier='2']/position");
            targetMultiplicity = (Element) path.selectSingleNode(labelsNode);
        } catch (JaxenException e1) {
            e1.printStackTrace();
        }
        if (sourceRolename != null && targetRolename != null && sourceMultiplicity != null && targetMultiplicity != null) {
            String sourceMultiplicityX = sourceMultiplicity.getAttributeValue("x");
            String sourceMultiplicityY = sourceMultiplicity.getAttributeValue("y");
            Point2D sourceMultiplicityPoint = new Point2D.Double();
            if (sourceMultiplicityX.equals("") || sourceMultiplicityY.equals("")) {
                sourceMultiplicityPoint.setLocation(100, -15);
            } else {
                sourceMultiplicityPoint.setLocation(Double.parseDouble(sourceMultiplicityX), Double.parseDouble(sourceMultiplicityY));
            }
            String targetMultiplicityX = targetMultiplicity.getAttributeValue("x");
            String targetMultiplicityY = targetMultiplicity.getAttributeValue("y");
            Point2D targetMultiplicityPoint = new Point2D.Double();
            if (targetMultiplicityX.equals("") || targetMultiplicityY.equals("")) {
                targetMultiplicityPoint.setLocation(GraphConstants.PERMILLE - 100, -15);
            } else {
                targetMultiplicityPoint.setLocation(Double.parseDouble(targetMultiplicityX), Double.parseDouble(targetMultiplicityY));
            }
            String sourceRolenameX = sourceRolename.getAttributeValue("x");
            String sourceRolenameY = sourceRolename.getAttributeValue("y");
            Point2D sourceRolenamePoint = new Point2D.Double();
            if (sourceRolenameX.equals("") || sourceRolenameY.equals("")) {
                sourceRolenamePoint.setLocation(100, 15);
            } else {
                sourceRolenamePoint.setLocation(Double.parseDouble(sourceRolenameX), Double.parseDouble(sourceRolenameY));
            }
            String targetRolenameX = targetRolename.getAttributeValue("x");
            String targetRolenameY = targetRolename.getAttributeValue("y");
            Point2D targetRolenamePoint = new Point2D.Double();
            if (targetRolenameX.equals("") || targetRolenameY.equals("")) {
                targetRolenamePoint.setLocation(GraphConstants.PERMILLE - 100, 15);
            } else {
                targetRolenamePoint.setLocation(Double.parseDouble(targetRolenameX), Double.parseDouble(targetRolenameY));
            }
            AttributeMap attributes = getAttributes();
            Object[] labels = { association.getSourceRolename(), association.getTargetRolename(), association.getSourceMultiplicity(), association.getTargetMultiplicity() };
            Point2D[] labelsPositions = { sourceRolenamePoint, targetRolenamePoint, sourceMultiplicityPoint, targetMultiplicityPoint };
            GraphConstants.setExtraLabelPositions(attributes, labelsPositions);
            GraphConstants.setExtraLabels(attributes, labels);
        }
    }
}
