package nl.utwente.ewi.stream.pasnql.commands;

import java.util.HashMap;
import java.util.Map;
import nl.utwente.ewi.stream.network.AbstractPE;
import nl.utwente.ewi.stream.network.AttributeFactory;
import nl.utwente.ewi.stream.network.QueryNetworkManager;
import nl.utwente.ewi.stream.network.AttributeFactory.AttributeType;
import nl.utwente.ewi.stream.network.attributes.AttributeCreationException;
import nl.utwente.ewi.stream.network.attributes.View;
import nl.utwente.ewi.stream.network.attributes.sql.SQLAnnotationView;
import nl.utwente.ewi.stream.network.dbcp.DBType;
import nl.utwente.ewi.stream.intervals.NumberInterval;
import nl.utwente.ewi.stream.pasnql.ExecuteException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Wraps a data command: data(HourlyAvgDts, ...);. A data command is an assignable command
 * @author rein
 */
public class AddAnnotationCommand extends AnnotationCommand {

    String attribute = "";

    Map conditions = new HashMap();

    public static final Logger logger = Logger.getLogger(AddAnnotationCommand.class.getName());

    public NumberInterval ni;

    public SQLAnnotationView view;

    public AddAnnotationCommand(String pe, String annotation, String attribute, Map conditions) {
        super(pe, annotation);
        this.attribute = attribute;
        this.conditions = conditions;
    }

    @Override
    public byte[] execute() {
        if (this.conditions.containsKey("value")) this.ni = (NumberInterval) this.conditions.get("value");
        String query = makeQuery(ni);
        annotation = annotation.replace("'", "");
        AbstractPE node = QueryNetworkManager.findNode(this.pe);
        view = (SQLAnnotationView) node.view;
        StringBuffer sb = new StringBuffer("Added annotation!");
        if (view.addAnnot(this.pe, annotation, query) != null) {
            return sb.toString().getBytes();
        } else {
            return null;
        }
    }

    @Override
    public boolean validate() {
        super.validate();
        logger.log(Level.INFO, "Validating...");
        AbstractPE node = QueryNetworkManager.findNode(this.pe);
        Map<String, DBType> schema = node.view.getSchema();
        if (this.attribute != null && !this.attribute.isEmpty()) {
            if (!schema.containsKey(this.attribute)) errors.add("Attribute '" + this.attribute + "' not found in source node '" + this.pe + "'");
        }
        for (Object att : conditions.keySet()) if (!schema.containsKey((String) att)) errors.add("Condition on '" + att + "' not valid for source node '" + this.pe + "'; attribute does not exist");
        if (!node.view.attributeType.contains("annotation") && errors.isEmpty()) {
            logger.log(Level.INFO, "View is not an annotation view.");
            try {
                node.view = AttributeFactory.createView(AttributeType.valueOf(node.view.attributeType + "annotation"), pe, node.view.getSchema());
            } catch (AttributeCreationException e) {
                errors.add("problem with creating the annotation view");
                e.printStackTrace();
            }
        }
        return errors.isEmpty();
    }

    /**
     * Transforms a numberinterval into a string query
     * @param ni - the NumberInterval to transform.
     * @return the String as made from the NumberInterval.
     */
    public String makeQuery(NumberInterval ni) {
        Number l = (Number) ni.getLeftBoundary();
        Number r = (Number) ni.getRightBoundary();
        boolean il = ni.isLeftBoundaryIncluded();
        boolean ir = ni.isRightBoundaryIncluded();
        String query = "value ";
        if (il) {
            query += ">= " + l.doubleValue();
        } else {
            query += "> " + l.doubleValue();
        }
        if (ir) {
            query += " and value <= " + r.doubleValue();
        } else {
            query += " and value < " + r.doubleValue();
        }
        return query;
    }

    @Override
    public String getResponseType() {
        return "text/plain";
    }

    @Override
    public String getResponseExtension() {
        return "txt";
    }
}
