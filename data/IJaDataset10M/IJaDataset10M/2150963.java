package org.xaware.ide.xadev.gui;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.TextHandler;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XASystemProps;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * The AdditionalAttributeControlledComponentComposite is used to display Name
 * and Values from XASystemConfig.xml.
 *
 * @author Venkat Rao
 * @version 1.0
 */
public class AdditionalAttributeControlledComponentComposite extends ControlledComponentComposite implements SelectionListener, FocusListener, ModifyListener {

    /** Logger for AttributeControlledComponentComposite */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(AdditionalAttributeControlledComponentComposite.class.getName());

    /** Namespace instance */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** Translator instance to get names from message bundle */
    public static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** components holds the controls */
    private final Vector components = new Vector();

    /** myType specifies from where it is called */
    private int myType;

    /** Name label */
    private Label nameLabel;

    /** attributes list contains the values to populated in the Name Combo */
    private final ArrayList attributes = new ArrayList();

    /** Control which holds Name Combo or TextField */
    private Control nameComponent;

    /** Control which holds Value Combo or TextField */
    protected Control valComponent;

    /** componentByName contains Control and label as pair */
    protected HashMap componentByName = new HashMap();

    /** DNDTreeHandler */
    private DNDTreeHandler myTreehandler;

    /** StackLayout */
    private StackLayout stackLayout;

    /** valueComp control */
    protected Composite valueComp;

    /** paramComp control */
    private Composite paramComp;

    /** Holds the focus flag. */
    private boolean focusFlag;

    /** Holds comp. */
    private Control comp;

    /** Modify flag. */
    private boolean modifyFlag;

    /** Label for the Element Text field. */
    private Label elemLabel;

    /** Element path chooser */
    private XMLPathChooser elemPathChooser;

    /**
     * Creates a new AttributeControlledComponentComposite object.
     *
     * @param rootComp composite
     * @param treeHandler holds tree handler of the BizView file
     */
    public AdditionalAttributeControlledComponentComposite(final Composite rootComp, final DNDTreeHandler treeHandler) {
        this(rootComp, 0, null, treeHandler);
        elemLabel = new Label(paramComp, SWT.NONE);
        elemLabel.setText(translator.getString("Element"));
        XMLTreeNode selNode = (XMLTreeNode) myTreehandler.getSelectedNode();
        if (selNode == null) {
            selNode = (XMLTreeNode) myTreehandler.getRoot();
        }
        final Element selElem = XMLTreeNode.getElementForTreeNode(selNode);
        elemPathChooser = new XMLPathChooser(paramComp, SWT.NONE, true, selElem);
        elemPathChooser.setTreeHandler(myTreehandler);
        paramComp.layout();
    }

    /**
     * Creates a new AttributeControlledComponentComposite object.
     *
     * @param rootComp Composite
     * @param inType int
     * @param selectedNode XMLTreeNode
     * @param treeHandler
     */
    public AdditionalAttributeControlledComponentComposite(final Composite rootComp, final int inType, final XMLTreeNode selectedNode, final DNDTreeHandler treeHandler) {
        super(rootComp, false);
        myTreehandler = treeHandler;
        myType = inType;
        String labelStr = null;
        String classStr = null;
        String nameStr = null;
        boolean editable = false;
        Attribute attr;
        final Element def = XASystemProps.getComponentConfigForType(myType);
        final Element componentElement = def.getChild(XASystemProps.COMPONENT);
        this.setLayout(new GridLayout(1, false));
        final GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);
        gridData.widthHint = 300;
        this.setLayoutData(gridData);
        paramComp = new Composite(this, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.verticalSpacing = 10;
        paramComp.setLayout(gridLayout);
        GridData griData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);
        gridData.widthHint = 300;
        gridData.widthHint = 250;
        griData.horizontalIndent = 15;
        paramComp.setLayoutData(griData);
        if (componentElement != null) {
            logger.finest(" componentElement  " + componentElement);
            try {
                attr = componentElement.getAttribute(XASystemProps.LABEL);
                if (attr != null) {
                    labelStr = translator.getString(attr.getValue());
                } else {
                    labelStr = translator.getString("Name:");
                }
                attr = componentElement.getAttribute(XASystemProps.CLASS);
                if (attr != null) {
                    classStr = attr.getValue();
                } else {
                    classStr = "org.eclipse.swt.widgets.Combo";
                }
                attr = componentElement.getAttribute(XASystemProps.EDITABLE);
                if (attr != null) {
                    editable = (Boolean.valueOf(attr.getValue())).booleanValue();
                } else {
                    editable = false;
                }
                griData = new GridData();
                nameLabel = new Label(paramComp, SWT.NONE);
                nameLabel.setText(labelStr);
                nameLabel.setLayoutData(griData);
                if (classStr.equals("org.eclipse.swt.widgets.Combo")) {
                    if (editable) {
                        nameComponent = new Combo(paramComp, SWT.NONE);
                        ((Combo) nameComponent).setVisibleItemCount(8);
                    } else {
                        nameComponent = new Combo(paramComp, SWT.READ_ONLY);
                        ((Combo) nameComponent).setVisibleItemCount(8);
                    }
                    final GridData nameGridData = new GridData();
                    nameGridData.widthHint = 340;
                    nameComponent.setLayoutData(nameGridData);
                    ((Combo) nameComponent).setEnabled(editable);
                    components.add(nameComponent);
                    final Label valueLbl = new Label(paramComp, SWT.NONE);
                    valueLbl.setText(translator.getString("Value:"));
                    final GridData valueLblData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
                    valueLbl.setLayoutData(valueLblData);
                    valueComp = new Composite(paramComp, SWT.NONE);
                    final GridData valueCompData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING);
                    valueCompData.heightHint = 26;
                    valueComp.setLayoutData(valueCompData);
                    valueCompData.widthHint = 300;
                    stackLayout = new StackLayout();
                    valueComp.setLayout(stackLayout);
                    final List nameList = componentElement.getChildren(XASystemProps.ITEM);
                    if (nameList != null) {
                        final Iterator nameIitems = nameList.iterator();
                        while (nameIitems.hasNext()) {
                            final Element elem = (Element) nameIitems.next();
                            attr = elem.getAttribute(XASystemProps.VALUE);
                            if (attr != null) {
                                nameStr = attr.getValue();
                                ((Combo) nameComponent).removeSelectionListener(this);
                                ((Combo) nameComponent).removeSelectionListener(this);
                                attributes.add(nameStr);
                                ((Combo) nameComponent).addSelectionListener(this);
                                ((Combo) nameComponent).addFocusListener(this);
                                ((Combo) nameComponent).addModifyListener(this);
                                final Element childCompElem = elem.getChild(XASystemProps.COMPONENT);
                                if (childCompElem != null) {
                                    attr = childCompElem.getAttribute(XASystemProps.LABEL);
                                    if (attr != null) {
                                        labelStr = translator.getString(attr.getValue());
                                    } else {
                                        labelStr = translator.getString("Value:");
                                    }
                                    attr = childCompElem.getAttribute(XASystemProps.CLASS);
                                    if (attr != null) {
                                        classStr = attr.getValue();
                                    } else {
                                        classStr = "org.eclipse.swt.widgets.Combo";
                                    }
                                    attr = childCompElem.getAttribute(XASystemProps.EDITABLE);
                                    if (attr != null) {
                                        editable = (Boolean.valueOf(attr.getValue())).booleanValue();
                                    } else {
                                        editable = false;
                                    }
                                    if (classStr.equals("org.eclipse.swt.widgets.Combo")) {
                                        valComponent = new Combo(valueComp, SWT.NONE);
                                        ((Combo) valComponent).setVisibleItemCount(8);
                                        ((Combo) valComponent).setEnabled(editable);
                                        populateValues((Combo) valComponent, childCompElem);
                                    } else {
                                        final Class clazz = XA_Designer_Plugin.getClassForName(classStr);
                                        final Constructor constructor = clazz.getConstructor(new Class[] { Composite.class, int.class });
                                        valComponent = (Control) constructor.newInstance(new Object[] { valueComp, new Integer(SWT.BORDER) });
                                        if (valComponent instanceof Text) {
                                            final TextHandler handler = new TextHandler((Text) valComponent);
                                        }
                                        if ((valComponent != null) && valComponent instanceof org.xaware.ide.xadev.gui.XMLPathChooser) {
                                            ((org.xaware.ide.xadev.gui.XMLPathChooser) valComponent).setTreeHandler(myTreehandler);
                                            if (selectedNode != null) {
                                                ((org.xaware.ide.xadev.gui.XMLPathChooser) valComponent).setSelectedPath(selectedNode.getNodePath());
                                            }
                                        }
                                        if ((valComponent != null) && valComponent instanceof org.xaware.ide.xadev.gui.FileChooserWithLabeling) {
                                            if (nameStr.trim().equals("bizcomp")) {
                                                ((org.xaware.ide.xadev.gui.FileChooserWithLabeling) valComponent).xaChooserType = XAFileConstants.BIZ_COMP_TYPE;
                                            } else if (nameStr.trim().equals("bizdriver")) {
                                                ((org.xaware.ide.xadev.gui.FileChooserWithLabeling) valComponent).xaChooserType = XAFileConstants.BIZ_DRIVER_TYPE;
                                            } else if (nameStr.trim().equals("bizdoc")) {
                                                ((org.xaware.ide.xadev.gui.FileChooserWithLabeling) valComponent).xaChooserType = XAFileConstants.BIZ_DOC_TYPE;
                                            }
                                        }
                                    }
                                    componentByName.put(nameStr, valComponent);
                                }
                            }
                        }
                        final Text valueTxt = ControlFactory.createText(valueComp, SWT.BORDER);
                        componentByName.put("", valueTxt);
                        stackLayout.topControl = valueTxt;
                        paramComp.layout();
                    }
                } else {
                    creatTextFieldControl(paramComp, componentElement, editable);
                }
            } catch (final Exception e) {
                logger.severe("Exception occurred parsing Controlled Component: " + e);
                logger.printStackTrace(e);
            }
        }
        sortNameComponent();
    }

    /**
     * Adds elements in the sorting order for Name Combo.
     */
    public void sortNameComponent() {
        if (nameComponent instanceof Combo) {
            final Comparator comp = new Comparator() {

                public int compare(Object o1, Object o2) {
                    return compare((String) o1, (String) o2);
                }

                public int compare(String n1, String n2) {
                    return (n1.compareToIgnoreCase(n2));
                }
            };
            final Object[] itemArray = attributes.toArray();
            Arrays.sort(itemArray, comp);
            for (int i = 0; i < itemArray.length; i++) {
                ((Combo) nameComponent).add((String) itemArray[i]);
            }
        }
    }

    /**
     * Creates blank TextField for Name and Value if values are not found from
     * XASystemConfig.xml file.
     *
     * @param paramComp Parent Composite
     * @param componentElement Element of the XML Tree
     * @param editable boolean value used for editable
     */
    public void creatTextFieldControl(final Composite paramComp, final Element componentElement, boolean editable) {
        Attribute attr;
        String labelStr;
        String classStr;
        nameComponent = ControlFactory.createText(paramComp, SWT.BORDER);
        final GridData nameGridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER);
        nameGridData.widthHint = 340;
        nameComponent.setLayoutData(nameGridData);
        ((Text) nameComponent).setEditable(editable);
        components.add(nameComponent);
        final Element childCompElem = componentElement.getChild(XASystemProps.COMPONENT);
        if (childCompElem != null) {
            attr = childCompElem.getAttribute(XASystemProps.LABEL);
            if (attr != null) {
                labelStr = translator.getString(attr.getValue());
            } else {
                labelStr = translator.getString("Value");
            }
            attr = childCompElem.getAttribute(XASystemProps.CLASS);
            if (attr != null) {
                classStr = translator.getString(attr.getValue());
            } else {
                classStr = "org.eclipse.swt.widgets.Text";
            }
            attr = childCompElem.getAttribute(XASystemProps.EDITABLE);
            if (attr != null) {
                editable = (Boolean.valueOf(attr.getValue())).booleanValue();
            } else {
                editable = false;
            }
            try {
                final Label valueLbl = new Label(paramComp, SWT.NONE);
                valueLbl.setText(labelStr);
                valueComp = new Composite(paramComp, SWT.NONE);
                final GridData valueCompGridData = new GridData(GridData.FILL_HORIZONTAL);
                valueComp.setLayoutData(valueCompGridData);
                valueCompGridData.widthHint = 360;
                stackLayout = new StackLayout();
                valueComp.setLayout(stackLayout);
                final Class clazz = XA_Designer_Plugin.getClassForName(classStr);
                final Constructor constructor = clazz.getConstructor(new Class[] { Composite.class, int.class });
                valComponent = (Control) constructor.newInstance(new Object[] { valueComp, new Integer(SWT.BORDER) });
                if (valComponent instanceof Text) {
                    final TextHandler handler = new TextHandler((Text) valComponent);
                }
                stackLayout.topControl = valComponent;
                paramComp.layout();
            } catch (final Exception ex) {
                logger.finest("Class not found. Creating TextField by default");
                valComponent = ControlFactory.createText(valueComp, SWT.BORDER);
                stackLayout.topControl = valComponent;
                valueComp.layout();
            }
            if (valComponent instanceof Text) {
                ((Text) valComponent).setEditable(editable);
            }
            components.add(valComponent);
            componentByName.put("", valComponent);
        }
    }

    /**
     * Populates value for the Name Combo.
     *
     * @param valCombo Value Combo Control
     * @param childCompElem Element of the XASystemConfig.xml file
     */
    public void populateValues(final Combo valCombo, final Element childCompElem) {
        final List valList = childCompElem.getChildren(XASystemProps.ITEM);
        if (valList != null) {
            final Iterator iter = valList.iterator();
            while (iter.hasNext()) {
                final Element itemElem = (Element) iter.next();
                valCombo.add(itemElem.getText());
            }
        }
    }

    /**
     * Gets value of the Control
     *
     * @param index Control index
     *
     * @return String
     */
    @Override
    public String getValueForIndex(final int index) {
        String retVal = "";
        if (components.size() <= index) {
            return retVal;
        }
        final Object obj = components.elementAt(index);
        if (obj instanceof Text) {
            retVal = ((Text) obj).getText();
        } else if (obj instanceof Combo) {
            retVal = ((Combo) obj).getText();
        } else if (obj instanceof org.xaware.ide.xadev.gui.FileChooserWithLabeling) {
            retVal = ((org.xaware.ide.xadev.gui.FileChooserWithLabeling) obj).getFileString();
        } else if (obj instanceof org.xaware.ide.xadev.gui.XMLPathChooser) {
            retVal = ((org.xaware.ide.xadev.gui.XMLPathChooser) obj).getPathString();
        }
        return retVal;
    }

    /**
     * Sets the value for the Control
     *
     * @param inVal String value
     * @param index Control index
     */
    @Override
    public void setValueForIndex(final String inVal, final int index) {
        if (components.size() <= index) {
            return;
        }
        final Object obj = components.elementAt(index);
        if (obj instanceof Text) {
            ((Text) obj).setText(inVal);
            ((Text) obj).forceFocus();
            ((Text) obj).setSelection(((Text) obj).getText().length());
        } else if (obj instanceof Combo) {
            ((Combo) obj).setText(inVal);
        } else if (obj instanceof org.xaware.ide.xadev.gui.FileChooserWithLabeling) {
            ((org.xaware.ide.xadev.gui.FileChooserWithLabeling) obj).setFileString(inVal);
        } else if (obj instanceof org.xaware.ide.xadev.gui.XMLPathChooser) {
            ((org.xaware.ide.xadev.gui.XMLPathChooser) obj).setPathString(inVal);
        }
    }

    /**
     * Updates the valus based on the name selection.
     */
    public void updateScreen() {
        try {
            String str = null;
            if (nameComponent instanceof Combo) {
                str = ((Combo) nameComponent).getText();
            } else if (nameComponent instanceof Text) {
                str = ((Text) nameComponent).getText();
            }
            if (!componentByName.containsKey(str)) {
                str = "";
            }
            comp = (Control) componentByName.get(str);
            if (comp == null) {
                comp = (Control) componentByName.get("");
            }
            stackLayout.topControl = comp;
            valueComp.layout();
            if (comp instanceof Combo) {
                if (((Combo) comp).getItemCount() > 0) {
                    ((Combo) comp).setText(((Combo) comp).getItem(0));
                    if (focusFlag) {
                        Display.getCurrent().asyncExec(new Runnable() {

                            public void run() {
                                valueComp.forceFocus();
                            }
                        });
                    }
                }
            }
            components.insertElementAt(comp, 1);
            if (components.size() > 2) {
                components.removeElementAt(2);
            }
            components.add(elemPathChooser);
        } catch (final Exception e) {
            logger.printStackTrace(e);
        }
    }

    /**
     * fires when combo box is selected.
     *
     * @param e SelectionEvent
     */
    public void widgetSelected(final SelectionEvent e) {
        updateScreen();
    }

    /**
     * Fires when we pressed the enter key on the name field.
     *
     * @param e SelectionEvent
     */
    public void widgetDefaultSelected(final SelectionEvent e) {
        updateScreen();
    }

    /**
     * Sets DNDTreeHandler
     *
     * @param tree treeHandler
     */
    public void setTree(final DNDTreeHandler tree) {
        myTreehandler = tree;
        if ((valComponent != null) && valComponent instanceof org.xaware.ide.xadev.gui.XMLPathChooser) {
            ((org.xaware.ide.xadev.gui.XMLPathChooser) valComponent).setTreeHandler(myTreehandler);
        }
    }

    /**
     * returns DNDTreeHandler
     *
     * @return returns DNDTreeHandler
     */
    public DNDTreeHandler getTree() {
        return myTreehandler;
    }

    /**
     * called when focus is gained.
     *
     * @param e focus event instance.
     */
    public void focusGained(final FocusEvent e) {
        focusFlag = true;
        modifyFlag = false;
    }

    /**
     * called when focus is lost.
     *
     * @param e focus event instance.
     */
    public void focusLost(final FocusEvent e) {
        if (focusFlag && modifyFlag) {
            updateScreen();
            focusFlag = false;
            modifyFlag = false;
            if (valComponent instanceof Text) {
                ((Text) valComponent).forceFocus();
            }
        }
    }

    /**
     * called when text is modified.
     *
     * @param e focus event instance.
     */
    public void modifyText(final ModifyEvent e) {
        modifyFlag = true;
    }
}
