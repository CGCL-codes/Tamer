package org.adapit.wctoolkit.infrastructure.configuration.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.configuration.ConfigurableComponent;
import org.adapit.wctoolkit.infrastructure.events.actions.AbstractAction;
import org.adapit.wctoolkit.infrastructure.events.actions.DefaultAbstractApplicationAction;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.models.config.JarClassLoader;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.w3c.dom.Node;

@SuppressWarnings({ "unchecked" })
public class ComponentSpecification implements ConfigurableComponent {

    private String name;

    private Class actionClass;

    private String iconPath;

    private ComponentSpecificationKind componentSpecificationKind;

    private String description;

    private boolean internationalized = false;

    private ResourceMessage messages = SpringResourceMessage.getInstance();

    private Dimension size;

    private IElement element;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class getActionClass() {
        return actionClass;
    }

    public void setActionClass(Class actionClass) {
        this.actionClass = actionClass;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String exportXML(int tab) {
        String str = "";
        str += '\n';
        for (int i = 0; i < tab; i++) str += '\t';
        str += "<ComponentSpecification name=\"" + name + "\"" + " internationalized=\"" + isInternationalized() + "\"" + " actionClass=\"" + actionClass.getName() + "\"" + " iconPath=\"" + iconPath + "\"" + " componentSpecificationKind=\"" + componentSpecificationKind.name() + "\"" + " description=\"" + description + "\"" + " size=\"" + size.width + "," + size.height + "\"";
        str += " >";
        str += '\n';
        for (int i = 0; i < tab; i++) str += '\t';
        str += "</ComponentSpecification>";
        return str;
    }

    @SuppressWarnings("static-access")
    public void importXML(Node element) {
        if (element != null) {
            try {
                name = element.getAttributes().getNamedItem("name").getNodeValue();
            } catch (Exception ex) {
            }
            try {
                internationalized = Boolean.parseBoolean(element.getAttributes().getNamedItem("internationalized").getNodeValue());
            } catch (Exception ex) {
            }
            try {
                String s = element.getAttributes().getNamedItem("actionClass").getNodeValue();
                if (s != null && !s.equals("")) actionClass = getClass().forName(s);
            } catch (Exception ex) {
                try {
                    actionClass = JarClassLoader.getLoadedClasses().get(element.getAttributes().getNamedItem("actionClass").getNodeValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                iconPath = element.getAttributes().getNamedItem("iconPath").getNodeValue();
            } catch (Exception ex) {
            }
            try {
                componentSpecificationKind = ComponentSpecificationKind.valueOf(element.getAttributes().getNamedItem("componentSpecificationKind").getNodeValue());
            } catch (Exception ex) {
                ex.printStackTrace();
                componentSpecificationKind = ComponentSpecificationKind.BUTTON;
            }
            try {
                description = element.getAttributes().getNamedItem("description").getNodeValue();
            } catch (Exception ex) {
            }
            try {
                String s = element.getAttributes().getNamedItem("size").getNodeValue();
                String[] v = s.split(",");
                size = new Dimension(Integer.parseInt(v[0]), Integer.parseInt(v[1]));
            } catch (Exception ex) {
            }
        }
    }

    private AbstractAction abstractAction;

    private DefaultAbstractApplicationAction applicationAction;

    public Component getComponent() {
        Component component = null;
        if (this.componentSpecificationKind == ComponentSpecificationKind.BUTTON) {
            component = new JButton(this.getName());
            if (!internationalized) {
                ((JButton) component).setText(name);
                if (description != null) ((JButton) component).setToolTipText(description);
            } else {
                ((JButton) component).setText(messages.getMessage(name));
                if (description != null) ((JButton) component).setToolTipText(messages.getMessage(description));
            }
            if (iconPath != null) {
                ((JButton) component).setIcon(getIcon());
            }
            if (actionClass != null) {
                try {
                    Object obj = actionClass.newInstance();
                    if (obj instanceof AbstractAction) {
                        abstractAction = (AbstractAction) obj;
                        if (element == null) {
                            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getSelectedElement() != null) abstractAction.setElement(DefaultApplicationFrame.getInstance().getDefaultContentPane().getSelectedElement());
                            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController() != null) abstractAction.setController(DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController());
                        } else {
                            abstractAction.setElement(element);
                            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController() != null) abstractAction.setController(DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController());
                        }
                        ((JButton) component).addActionListener(abstractAction);
                    } else if (obj instanceof DefaultAbstractApplicationAction) {
                        applicationAction = (DefaultAbstractApplicationAction) obj;
                        ((JButton) component).addActionListener(applicationAction);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else if (this.componentSpecificationKind == ComponentSpecificationKind.TOGGLE_BUTTON) {
            component = new JToggleButton(this.getName());
            if (!internationalized) {
                ((JToggleButton) component).setText(name);
                if (description != null) ((JToggleButton) component).setToolTipText(description);
            } else {
                ((JToggleButton) component).setText(messages.getMessage(name));
                if (description != null) ((JToggleButton) component).setToolTipText(messages.getMessage(description));
            }
            if (iconPath != null) {
                ((JToggleButton) component).setIcon(getIcon());
            }
            if (actionClass != null) {
                try {
                    Object obj = actionClass.newInstance();
                    if (obj instanceof AbstractAction) {
                        AbstractAction ac = (AbstractAction) obj;
                        if (element == null) {
                            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getSelectedElement() != null) ac.setElement(DefaultApplicationFrame.getInstance().getDefaultContentPane().getSelectedElement());
                            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController() != null) ac.setController(DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController());
                        } else {
                            ac.setElement(element);
                            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController() != null) ac.setController(DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController());
                        }
                        ((JToggleButton) component).addActionListener(ac);
                    } else if (obj instanceof DefaultAbstractApplicationAction) {
                        applicationAction = (DefaultAbstractApplicationAction) obj;
                        ((JToggleButton) component).addActionListener(applicationAction);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else if (this.componentSpecificationKind == ComponentSpecificationKind.LABEL) {
        }
        return component;
    }

    public ImageIcon getIcon() {
        try {
            URL imURL = getClass().getResource(iconPath);
            if (imURL != null) {
                Image image = new ImageIcon(imURL).getImage();
                if (image != null) {
                    image = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
                    return new ImageIcon(image);
                }
            }
        } catch (java.lang.StackOverflowError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ComponentSpecificationKind getComponentSpecificationKind() {
        return componentSpecificationKind;
    }

    public void setComponentSpecificationKind(ComponentSpecificationKind cKind) {
        this.componentSpecificationKind = cKind;
    }

    public boolean isInternationalized() {
        return internationalized;
    }

    public void setInternationalized(boolean internationalized) {
        this.internationalized = internationalized;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public IElement getElement() {
        return element;
    }

    public void setElement(IElement element) {
        this.element = element;
        if (abstractAction != null) abstractAction.setElement(element);
    }

    public AbstractAction getAbstractAction() {
        return abstractAction;
    }

    public void setAbstractAction(AbstractAction abstractAction) {
        this.abstractAction = abstractAction;
    }
}
