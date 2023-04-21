package kursach2;

import java.util.*;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import kursach2.barriers.AArchiBot;
import kursach2.strategy.IStrategy;
import kursach2.strategy.FactoryBehaviors;
import kursach2.tactics.ITactics;

/**
 * ����� �������� ������������ ������� � �� �����
 * 
 * @author Vsevolod
 */
public class AgentFactory {

    private Map<Integer, AArchiBot> humans = new HashMap<Integer, AArchiBot>();

    private int Count = 0;

    /**
	 * ���� �������
	 */
    private Map<String, TType> Types = new HashMap<String, TType>();

    private FactoryBehaviors Behaviors;

    public AgentFactory(Node Doc, FactoryBehaviors behaviors) {
        Behaviors = behaviors;
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        NamedNodeMap attr;
        String name;
        int currank = 1;
        try {
            NodeList nodes = (NodeList) xpath.evaluate("type", Doc, XPathConstants.NODESET), list;
            attr = nodes.item(0).getAttributes();
            name = attr.getNamedItem("name").getNodeValue();
            list = nodes.item(0).getChildNodes();
            TType curtype = new TType(name, Behaviors.getStrategy(list.item(1).getTextContent()), Behaviors.getTactics(list.item(3).getTextContent()));
            list = (NodeList) xpath.evaluate("radius", nodes.item(0), XPathConstants.NODESET);
            curtype.setRadius(Double.parseDouble(list.item(0).getTextContent()));
            list = (NodeList) xpath.evaluate("velocity", nodes.item(0), XPathConstants.NODESET);
            curtype.setVelocity(Double.parseDouble(list.item(0).getTextContent()));
            list = (NodeList) xpath.evaluate("acceleration", nodes.item(0), XPathConstants.NODESET);
            curtype.setAccelerate(Double.parseDouble(list.item(0).getTextContent()));
            curtype.rank(currank);
            currank++;
            Types.put(name, curtype);
            Types.put("default", curtype);
            TType deftype = curtype;
            for (int i = 1; i < nodes.getLength(); i++) {
                attr = nodes.item(i).getAttributes();
                name = attr.getNamedItem("name").getNodeValue();
                list = nodes.item(i).getChildNodes();
                curtype = new TType(name, Behaviors.getStrategy(list.item(1).getTextContent()), Behaviors.getTactics(list.item(3).getTextContent()));
                list = (NodeList) xpath.evaluate("radius", nodes.item(0), XPathConstants.NODESET);
                if (list.getLength() > 0) curtype.setRadius(Double.parseDouble(list.item(0).getTextContent())); else curtype.setRadius(deftype.getRadius());
                list = (NodeList) xpath.evaluate("velocity", nodes.item(0), XPathConstants.NODESET);
                if (list.getLength() > 0) curtype.setVelocity(Double.parseDouble(list.item(0).getTextContent())); else curtype.setVelocity(deftype.getVelocity());
                list = (NodeList) xpath.evaluate("accelerate", nodes.item(0), XPathConstants.NODESET);
                if (list.getLength() > 0) curtype.setAccelerate(Double.parseDouble(list.item(0).getTextContent())); else curtype.setAccelerate(deftype.getAccelerate());
                curtype.rank(currank);
                currank++;
                Types.put(name, curtype);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public AArchiBot newAgent(Integer nprem, IStrategy s, ITactics t, double velocity, double accelerate, double radius) {
        THuman h = new THuman(0, 0, null, velocity, accelerate, radius);
        h.setStrategy(s);
        h.setTactics(t);
        h.setNumber(Count++);
        humans.put(h.getNumber(), h);
        return h;
    }

    public List<AArchiBot> getPeople() {
        return new ArrayList<AArchiBot>(humans.values());
    }

    public AArchiBot getHuman(Integer i) {
        return humans.get(i);
    }

    public int count_agents() {
        return humans.size();
    }

    public void clear() {
        humans.clear();
        Count = 0;
    }

    public TType getType(String name) {
        return Types.get(name);
    }

    public static class TType {

        public TType(String name, IStrategy strategy, ITactics tactics) {
            NameType = name;
            Strategy = strategy;
            Tactics = tactics;
        }

        private String NameType;

        private IStrategy Strategy;

        private ITactics Tactics;

        private double radius, velocity, accelerate;

        private int rank;

        public void rank(int r) {
            rank = r;
        }

        public int rank() {
            return rank;
        }

        public String getNameType() {
            return NameType;
        }

        public void setName(String n) {
            NameType = n;
        }

        public double getAccelerate() {
            return accelerate;
        }

        public void setAccelerate(double accelerate) {
            this.accelerate = accelerate;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double rad) {
            this.radius = rad;
        }

        public double getVelocity() {
            return velocity;
        }

        public void setVelocity(double vel) {
            this.velocity = vel;
        }

        public IStrategy getStrategy() {
            return Strategy;
        }

        public ITactics getTactics() {
            return Tactics;
        }
    }
}
