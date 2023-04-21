package teabag;

import java.util.ArrayList;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropManager {

    private static VariableManager vm;

    private static ActionManager am;

    private static PropManager instance;

    private static ArrayList<Prop> propList;

    private PropManager() {
        propList = new ArrayList<Prop>();
        vm = VariableManager.getInstance();
        am = ActionManager.getInstance();
    }

    protected static PropManager getInstance() {
        if (instance == null) {
            instance = new PropManager();
        }
        return instance;
    }

    public String toString() {
        String text = "[PropManager]\n";
        for (Prop p : propList) text += p + "\n";
        text += "[/PropManager]";
        return text;
    }

    protected Prop getProp(String name) {
        for (Prop p : propList) if (p.containsAdjective(name)) return p;
        return null;
    }

    protected Prop createProp(String name) {
        if (getProp(name) != null) return null;
        Prop p = new Prop(name);
        return (propList.add(p) ? p : null);
    }

    protected void destroyProp(String s) {
        for (Prop px : propList) {
            if (px.name.equals(s)) {
                destroyProp(px);
                break;
            }
        }
    }

    protected void destroyProp(Prop p) {
        p.clearVariables();
        p.clearActions();
        propList.remove(p);
    }

    class Prop {

        private ArrayList<VariableManager.Variable> variableList;

        private ArrayList<ActionManager.Action> actionList;

        private ArrayList<String> adjectiveList;

        private String name;

        public String toString() {
            String text = name + "( ";
            for (String a : adjectiveList) text += a + ", ";
            text += "):\n  Variables:\n";
            for (VariableManager.Variable v : variableList) text += "    " + v + "\n";
            text += "  Actions:\n";
            ;
            for (ActionManager.Action a : actionList) text += "    " + a + "\n";
            return text;
        }

        protected Prop(String iName) {
            name = iName;
            adjectiveList = new ArrayList<String>();
            variableList = new ArrayList<VariableManager.Variable>();
            actionList = new ArrayList<ActionManager.Action>();
        }

        protected void addAdjective(String adjective) {
            adjectiveList.add(adjective);
        }

        protected String getName() {
            return name;
        }

        protected boolean containsAdjective(String adjective) {
            if (name.equals(adjective)) return true;
            for (String a : adjectiveList) if ((name + " " + a).equals(adjective)) return true;
            return false;
        }

        protected void removeAdjective(String name) {
            for (String a : adjectiveList) if (a.equals(name)) adjectiveList.remove(a);
        }

        protected void newVariable(String name, String value) {
            variableList.add(vm.addVariable(name, value));
        }

        protected void clearVariables() {
            for (VariableManager.Variable v : variableList) vm.removeVariable(v);
        }

        protected void addAction(Node actionNode) {
            if (actionNode.getNodeType() == Node.TEXT_NODE) return;
            Element actionElement = (Element) actionNode;
            String actionName = actionElement.getAttribute("name");
            try {
                Node doNode = (Node) ScriptManager.xpath.evaluate("do", actionNode, XPathConstants.NODE);
                ActionManager.Action a = am.addAction(actionName, doNode);
                actionList.add(a);
                NodeList aliases = actionNode.getChildNodes();
                Element alias;
                for (int n = 0; n < aliases.getLength(); n++) {
                    if (aliases.item(n).getNodeType() == Node.TEXT_NODE) continue;
                    alias = (Element) aliases.item(n);
                    if (alias.getNodeName().equals("alias")) a.addAlias(alias.getAttribute("name"));
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }

        protected void clearActions() {
            for (ActionManager.Action a : actionList) am.removeAction(a);
        }
    }
}
