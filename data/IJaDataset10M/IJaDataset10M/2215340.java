package org.ximtec.igesture.tool.view.admin.action;

import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.ximtec.igesture.core.GestureClass;
import org.ximtec.igesture.core.composite.CompositeDescriptor;
import org.ximtec.igesture.core.composite.Constraint;
import org.ximtec.igesture.tool.GestureConstants;
import org.ximtec.igesture.tool.core.Controller;
import org.ximtec.igesture.tool.core.TreePathAction;
import org.ximtec.igesture.util.XMLParser;

/**
 * @author Bjorn Puype, bpuype@gmail.com
 *
 */
public class AddCompositeDescriptorAction extends TreePathAction {

    private static final Logger LOGGER = Logger.getLogger(AddCompositeDescriptorAction.class.getName());

    private Map<String, String> constraintsMapping;

    private Controller controller;

    /**
	 * @param key
	 * @param controller
	 * @param treePath
	 */
    public AddCompositeDescriptorAction(Controller controller, TreePath treePath) {
        super(GestureConstants.COMPOSITE_DESCRIPTOR_ADD, controller, treePath);
        this.controller = controller;
        initConstraintsMapping();
    }

    private void initConstraintsMapping() {
        constraintsMapping = new HashMap<String, String>();
        URL path = getClass().getClassLoader().getResource(GestureConstants.XML_CONFIG);
        XMLParser parser = new XMLParser() {

            @Override
            public void execute(ArrayList<NodeList> nodeLists) {
                String name = ((Node) nodeLists.get(0).item(0)).getNodeValue();
                String clazz = ((Node) nodeLists.get(1).item(0)).getNodeValue();
                constraintsMapping.put(name, clazz);
            }
        };
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add(GestureConstants.XML_NODE_NAME);
        nodes.add(GestureConstants.XML_NODE_CLASS);
        try {
            parser.parse(path.getFile(), GestureConstants.XML_CONFIG_CONSTRAINT_NODE, nodes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not parse configuration file (" + GestureConstants.XML_CONFIG + " - constraints).", e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        GestureClass gestureClass = (GestureClass) getTreePath().getLastPathComponent();
        ConcurrentSkipListSet<String> map = new ConcurrentSkipListSet<String>();
        map.addAll(constraintsMapping.keySet());
        Constraint constraint = null;
        String choice = (String) JOptionPane.showInputDialog(null, "Choose a Constraint Type", "Constraint Chooser", JOptionPane.PLAIN_MESSAGE, null, map.toArray(), "Concurrency");
        if ((choice != null) && (choice.length() > 0)) {
            String clazz = constraintsMapping.get(choice.toString());
            Class<?> cl;
            try {
                cl = Class.forName(clazz);
                Constructor[] ctors = cl.getDeclaredConstructors();
                constraint = (Constraint) ctors[0].newInstance();
                CompositeDescriptor descriptor = new CompositeDescriptor();
                descriptor.setConstraint(constraint);
                gestureClass.addDescriptor(descriptor);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
