package org.exist.performance;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.exist.EXistException;
import org.exist.Namespaces;
import org.exist.performance.actions.Action;
import org.xmldb.api.base.XMLDBException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ActionSequence extends AbstractAction {

    protected int repeat = 1;

    protected List<Action> actions = new ArrayList<Action>();

    protected Runner runner;

    protected Map<String, String> namespaces = new HashMap<String, String>();

    public void configure(Runner runner, Action parent, Element config) throws EXistException {
        super.configure(runner, parent, config);
        this.runner = runner;
        if (config.hasAttribute("repeat")) {
            String repeatParam = config.getAttribute("repeat");
            try {
                repeat = Integer.parseInt(repeatParam);
            } catch (NumberFormatException e) {
                throw new EXistException(getClass().getName() + ": value of attribute repeat should be an integer. found: " + repeatParam);
            }
        }
        NodeList nl = config.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                if (elem.getNamespaceURI().equals(Namespaces.EXIST_NS)) {
                    if (elem.getLocalName().equals("map")) {
                        String prefix = elem.getAttribute("prefix");
                        String uri = elem.getAttribute("uri");
                        namespaces.put(prefix, uri);
                    } else {
                        Class<Action> clazz = runner.getClassForAction(elem.getLocalName());
                        if (clazz == null) throw new EXistException("no class defined for action: " + elem.getLocalName());
                        if (!Action.class.isAssignableFrom(clazz)) throw new EXistException("class " + clazz.getName() + " does not implement interface Action");
                        try {
                            Action instance = (Action) clazz.newInstance();
                            instance.configure(runner, this, elem);
                            actions.add(instance);
                        } catch (InstantiationException e) {
                            throw new EXistException("failed to create instance of class " + clazz.getName(), e);
                        } catch (IllegalAccessException e) {
                            throw new EXistException("failed to create instance of class " + clazz.getName(), e);
                        }
                    }
                }
            }
        }
    }

    public void execute(Connection connection) throws XMLDBException, EXistException {
        for (int i = 0; i < repeat; i++) {
            for (Action action : actions) {
                long start = System.currentTimeMillis();
                LOG.debug('[' + Thread.currentThread().getName() + "] " + action.getClass().getName());
                action.execute(connection);
                long elapsed = System.currentTimeMillis() - start;
                LOG.debug('[' + Thread.currentThread().getName() + "] " + action.getClass().getName() + " took " + elapsed + "ms.");
                runner.getResults().report(action, null, elapsed);
                System.gc();
            }
        }
    }

    @Override
    public Map<String, String> getNamespaces() {
        return namespaces;
    }
}
