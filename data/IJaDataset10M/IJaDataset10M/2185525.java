package uk.ac.lkl.expresser.server.objectify;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.googlecode.objectify.annotation.Unindexed;
import uk.ac.lkl.common.util.XMLUtilities;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.expression.LocatedExpression;
import uk.ac.lkl.common.util.expression.operation.IntegerAdditionOperation;
import uk.ac.lkl.common.util.expression.operation.IntegerMultiplicationOperation;
import uk.ac.lkl.common.util.expression.operation.IntegerSubtractionOperation;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.expresser.server.ServerUtils;
import uk.ac.lkl.expresser.server.XMLDocument;
import uk.ac.lkl.expresser.server.objectify.ServerExpressedObject;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.UnspecifiedTiedNumberExpression;

@Unindexed
public class ServerLocatedExpression extends ServerExpressedObject {

    private String xml;

    public ServerLocatedExpression(int x, int y, String expressionGuid, String xml, String userKey) {
        super(x, y, expressionGuid, userKey);
        this.xml = xml;
    }

    public ServerLocatedExpression() {
    }

    @Override
    public ServerExpressedObject copy(String idOfCopy) {
        return new ServerLocatedExpression(getX(), getY(), idOfCopy, xml, getProjectKey());
    }

    public String getXml() {
        return xml;
    }

    public LocatedExpression<IntegerValue> getLocatedExpression() {
        Document document = new XMLDocument(xml).getDocument();
        if (document == null) {
            return null;
        }
        NodeList childNodes = document.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                String tagName = element.getTagName();
                if (tagName.equals("locatedExpression")) {
                    Expression<IntegerValue> expression = parseExpression(element.getFirstChild());
                    LocatedExpression<IntegerValue> locatedExpression = new LocatedExpression<IntegerValue>(expression, getX(), getY());
                    locatedExpression.setUniqueId(getId());
                    return locatedExpression;
                }
            }
        }
        return null;
    }

    public static Expression<IntegerValue> parseExpression(Node node) {
        if (!(node instanceof Element)) {
            return null;
        }
        Element element = (Element) node;
        String tagName = element.getTagName();
        if (tagName.equals("tiedNumber")) {
            String id = element.getAttribute("id");
            if (id.isEmpty()) {
                ServerUtils.severe("XML of a tied number lacking an id attribute: " + XMLUtilities.nodeToString(node));
            }
            DAO dao = ServerUtils.getDao();
            ServerTiedNumber serverTiedNumber = dao.getTiedNumber(id);
            if (serverTiedNumber == null) {
                ServerUtils.severe("No tied number in data store with id: " + id);
                return new TiedNumberExpression<IntegerValue>(IntegerValue.ZERO);
            } else {
                return serverTiedNumber.getTiedNumber();
            }
        } else if (tagName.equals("compoundExpression")) {
            String operationName = element.getAttribute("operator");
            ArrayList<Expression<IntegerValue>> subExpressions = new ArrayList<Expression<IntegerValue>>();
            NodeList childNodes = element.getChildNodes();
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                Expression<IntegerValue> subExpression = parseExpression(childNodes.item(i));
                if (subExpression != null) {
                    subExpressions.add(subExpression);
                }
            }
            if (operationName.equals("add")) {
                return new IntegerAdditionOperation(subExpressions);
            } else if (operationName.equals("multiply")) {
                return new IntegerMultiplicationOperation(subExpressions);
            } else if (operationName.equals("subtract")) {
                return new IntegerSubtractionOperation(subExpressions);
            } else {
                throw new RuntimeException("Expected operation to be one of add, multiply, or subtract. Not " + operationName);
            }
        } else if (tagName.equals("unspecifiedNumber")) {
            return new UnspecifiedTiedNumberExpression<IntegerValue>();
        } else {
            ServerUtils.severe("Unrecognised expression tag: " + tagName);
            return null;
        }
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
