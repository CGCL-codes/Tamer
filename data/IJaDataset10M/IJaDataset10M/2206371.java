package org.adapit.wctoolkit.propertyeditors.form.element.parameter;

import java.awt.Dimension;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.propertyeditors.DefaultElementPropertyEditorPane;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.models.util.TabbedPaneUpdatable;
import org.adapit.wctoolkit.models.util.view.ElementListCellRenderer;
import org.adapit.wctoolkit.swing.ext.LoggerJPanel;
import org.adapit.wctoolkit.uml.classes.kernel.DataType;
import org.adapit.wctoolkit.uml.classes.kernel.Operation;
import org.adapit.wctoolkit.uml.classes.kernel.Parameter;
import org.adapit.wctoolkit.uml.classes.kernel.ParameterDirectionKind;
import org.adapit.wctoolkit.uml.classes.kernel.VisibilityKind;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.ElementImpl;
import org.adapit.wctoolkit.uml.ext.core.Stereotype;
import org.adapit.wctoolkit.uml.usecases.Actor;
import org.adapit.wctoolkit.uml.usecases.UseCase;

@SuppressWarnings({ "unchecked" })
public class ParameterEditorPanel extends LoggerJPanel implements HierarchyBoundsListener, TabbedPaneUpdatable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 43861436814361346L;

    private Parameter element;

    public void setElement(IElement element) {
        this.element = (Parameter) element;
    }

    private ResourceMessage messages = SpringResourceMessage.getInstance();

    protected DefaultElementPropertyEditorPane editorPane;

    /**
	 * Creates new form AttributeEditorPanel
	 * 
	 * @param frame
	 */
    public ParameterEditorPanel(JDialog parentDialog, Parameter element) {
        this.element = element;
        initComponents();
        initialize();
    }

    public ParameterEditorPanel(JFrame parentFrame, Parameter element) {
        this.element = element;
        initComponents();
        initialize();
    }

    public ParameterEditorPanel(DefaultElementPropertyEditorPane pane) {
        this.editorPane = pane;
        this.element = (Parameter) pane.getElement();
        initComponents();
        initialize();
    }

    public void changeType() {
        DataType type = null;
        Parameter par = (Parameter) element;
        if (typeComboBox.getSelectedItem() instanceof IElement) try {
            type = (DataType) typeComboBox.getSelectedItem();
            if (type == null) {
                return;
            } else {
                par.setType(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == null && typeComboBox.getSelectedItem() instanceof String) {
            String value = (String) typeComboBox.getSelectedItem();
            try {
                par.setAsTextType(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyElementChanged();
    }

    private void initComponents() {
        typeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        scopeLabel = new javax.swing.JLabel();
        kindComboBox = new javax.swing.JComboBox();
        visibilityLabel = new javax.swing.JLabel();
        visibilityComboBox = new javax.swing.JComboBox();
        cardinalityLabel = new javax.swing.JLabel();
        cardinalityComboBox = new javax.swing.JComboBox();
        checkBoxDecorationPanel = new javax.swing.JPanel();
        derivedUnionCheckBox = new javax.swing.JCheckBox();
        collectionCheckBox = new javax.swing.JCheckBox();
        changeableCheckBox = new javax.swing.JCheckBox();
        volatileCheckBox = new javax.swing.JCheckBox();
        constantCheckBox = new javax.swing.JCheckBox();
        activeCheckBox = new javax.swing.JCheckBox();
        derivedCheckBox = new javax.swing.JCheckBox();
        setLayout(null);
        this.setSize(new Dimension(259, 191));
        addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                ParameterEditorPanel.this.focusGained(evt);
            }
        });
        typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        typeLabel.setText(messages.getMessage("Type") + ":");
        add(typeLabel);
        typeLabel.setBounds(10, 5, 73, 20);
        typeComboBox.setPreferredSize(new java.awt.Dimension(27, 20));
        typeComboBox.setEditable(true);
        typeComboBox.addItemListener(new java.awt.event.ItemListener() {

            boolean firstTime = true;

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                if (evt.getStateChange() == ItemEvent.SELECTED && !firstTime && !elementChanged) {
                    changeType();
                } else if (evt.getStateChange() == ItemEvent.SELECTED && !elementChanged) {
                    firstTime = false;
                }
            }
        });
        typeComboBox.setRenderer(new ElementListCellRenderer());
        add(typeComboBox);
        typeComboBox.setBounds(88, 5, 162, 20);
        scopeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scopeLabel.setText(messages.getMessage("Kind") + ":");
        add(scopeLabel);
        scopeLabel.setBounds(10, 29, 80, 20);
        kindComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "in", "out", "inout", "return" }));
        kindComboBox.setPreferredSize(new java.awt.Dimension(64, 20));
        kindComboBox.addItemListener(new java.awt.event.ItemListener() {

            boolean firstTime = true;

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                if (evt.getStateChange() == ItemEvent.SELECTED && !firstTime && !elementChanged) {
                    kindComboBoxItemStateChanged(evt);
                } else if (evt.getStateChange() == ItemEvent.SELECTED && !elementChanged) {
                    firstTime = false;
                }
            }
        });
        add(kindComboBox);
        kindComboBox.setBounds(88, 30, 162, 20);
        visibilityLabel.setHorizontalAlignment(SwingConstants.LEFT);
        visibilityLabel.setText(messages.getMessage("Visibility") + ":");
        add(visibilityLabel);
        visibilityLabel.setBounds(10, 55, 73, 20);
        visibilityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { VisibilityKind.PUBLIC.name().toLowerCase(), VisibilityKind.PROTECTED.name().toLowerCase(), VisibilityKind.PRIVATE.name().toLowerCase(), VisibilityKind.DEFAULT.name().toLowerCase() }));
        visibilityComboBox.setPreferredSize(new java.awt.Dimension(71, 20));
        visibilityComboBox.addItemListener(new java.awt.event.ItemListener() {

            boolean firstTime = true;

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                if (evt.getStateChange() == ItemEvent.SELECTED && !firstTime && !elementChanged) {
                    visibilityComboBoxItemStateChanged(evt);
                } else if (evt.getStateChange() == ItemEvent.SELECTED && !elementChanged) {
                    firstTime = false;
                }
            }
        });
        add(visibilityComboBox);
        visibilityComboBox.setBounds(88, 55, 162, 20);
        cardinalityLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cardinalityLabel.setText(messages.getMessage("Cardinality") + ":");
        add(cardinalityLabel);
        cardinalityLabel.setBounds(10, 80, 73, 20);
        cardinalityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "0..1", "0..*", "1..*", "*" }));
        cardinalityComboBox.setEditable(true);
        cardinalityComboBox.addItemListener(new java.awt.event.ItemListener() {

            boolean firstTime = true;

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                if (evt.getStateChange() == ItemEvent.SELECTED && !firstTime && !elementChanged) {
                    cardinalityComboBoxItemStateChanged(evt);
                    DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController().notifyElementChanged();
                } else if (evt.getStateChange() == ItemEvent.SELECTED && !elementChanged) {
                    firstTime = false;
                }
            }
        });
        add(cardinalityComboBox);
        cardinalityComboBox.setBounds(88, 80, 162, 20);
        checkBoxDecorationPanel.setLayout(new java.awt.GridLayout(4, 2));
        derivedUnionCheckBox.setText(messages.getMessage("DerivedUnion"));
        derivedUnionCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                derivedUnionCBItemStateChanged(evt);
            }
        });
        collectionCheckBox.setText(messages.getMessage("Collection"));
        volatileCheckBox.setText(messages.getMessage("Volatile"));
        volatileCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                volatileCBItemStateChanged(evt);
            }
        });
        checkBoxDecorationPanel.add(volatileCheckBox, null);
        constantCheckBox.setText(messages.getMessage("Final"));
        constantCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                constantCBItemStateChanged(evt);
            }
        });
        constantCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (elementChanged) return;
                constantCBActionPerformed(evt);
            }
        });
        checkBoxDecorationPanel.add(constantCheckBox, null);
        activeCheckBox.setText(messages.getMessage("Active"));
        activeCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                activeCheckBoxItemStateChanged(evt);
            }
        });
        checkBoxDecorationPanel.add(activeCheckBox, null);
        derivedCheckBox.setText(messages.getMessage("Derived"));
        derivedCheckBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (elementChanged) return;
                derivedCBItemStateChanged(evt);
            }
        });
        add(checkBoxDecorationPanel);
        checkBoxDecorationPanel.setBounds(10, 105, 240, 80);
        checkBoxDecorationPanel.add(getIsListCheckBox(), null);
        checkBoxDecorationPanel.add(getInsertCheckBox(), null);
        checkBoxDecorationPanel.add(getUpdateCheckBox(), null);
        checkBoxDecorationPanel.add(getDeleteCheckBox(), null);
        checkBoxDecorationPanel.add(getAbstractCheckBox(), null);
        this.addHierarchyBoundsListener(this);
    }

    private void derivedUnionCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Stereotype st = element.getAssignedStereotype("DerivedUnion");
        if (st == null && derivedUnionCheckBox.isSelected()) {
            st = new Stereotype(element);
            st.setName("DerivedUnion");
            element.assignStereotype(st);
        } else if (st != null && (!derivedUnionCheckBox.isSelected())) {
            st = new Stereotype(element);
            st.setName("DerivedUnion");
            try {
                element.remove(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void focusGained(java.awt.event.FocusEvent evt) {
        initialize();
    }

    public void initialize() {
        try {
            this.visibilityComboBox.setSelectedItem(element.getVisibility().name().toLowerCase());
            this.kindComboBox.setSelectedItem(element.getDirection().name().toLowerCase());
            Stereotype st = new Stereotype();
            st.setName("volatile");
            this.volatileCheckBox.setSelected(element.contains(st));
            st.setName("collection");
            this.collectionCheckBox.setSelected(element.contains(st));
        } catch (Exception e) {
        }
    }

    private void volatileCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Stereotype st = element.getAssignedStereotype("voltaile");
        if (st == null && volatileCheckBox.isSelected()) {
            st = new Stereotype(element);
            st.setName("volatile");
            element.assignStereotype(st);
        } else if (st != null && (!volatileCheckBox.isSelected())) {
            st = new Stereotype(element);
            st.setName("volatile");
            try {
                element.remove(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void constantCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Stereotype st = element.getAssignedStereotype("frozen");
        if (st == null && constantCheckBox.isSelected()) {
            st = new Stereotype(element);
            st.setName("frozen");
            element.assignStereotype(st);
        } else if (st != null && (!constantCheckBox.isSelected())) {
            st = new Stereotype(element);
            st.setName("frozen");
            try {
                element.remove(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void activeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        Stereotype st = element.getAssignedStereotype("active");
        if (st == null && activeCheckBox.isSelected()) {
            st = new Stereotype(element);
            st.setName("active");
            element.assignStereotype(st);
        } else if (st != null && (!activeCheckBox.isSelected())) {
            st = new Stereotype(element);
            st.setName("active");
            try {
                element.remove(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void derivedCBItemStateChanged(java.awt.event.ItemEvent evt) {
        Stereotype st = element.getAssignedStereotype("derived");
        if (st == null && derivedCheckBox.isSelected()) {
            st = new Stereotype(element);
            st.setName("derived");
            element.assignStereotype(st);
        } else if (st != null && (!derivedCheckBox.isSelected())) {
            st = new Stereotype(element);
            st.setName("derived");
            try {
                element.remove(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void constantCBActionPerformed(java.awt.event.ActionEvent evt) {
        Stereotype st = element.getAssignedStereotype("frozen");
        if (st == null && constantCheckBox.isSelected()) {
            st = new Stereotype(element);
            st.setName("frozen");
            element.assignStereotype(st);
        } else if (st != null && (!constantCheckBox.isSelected())) {
            st = new Stereotype(element);
            st.setName("frozen");
            try {
                element.remove(st);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cardinalityComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        element.setCardinality((String) cardinalityComboBox.getSelectedItem());
    }

    private void visibilityComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        element.setVisibility(VisibilityKind.valueOf(((String) visibilityComboBox.getSelectedItem()).toUpperCase()));
    }

    private void kindComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        element.setDirection(ParameterDirectionKind.valueOf(kindComboBox.getSelectedItem().toString().toUpperCase()));
        if (element.getDirection().equals("return")) {
            (element.getParentElement()).removeElement(element);
            ((Operation) element.getParentElement()).setReturnType(element);
            DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController().notifyElementChanged();
        }
    }

    private void populateCardinalityComboBox() {
        if (element != null) {
            try {
                Parameter par = (Parameter) element;
                if (par != null) {
                    if (par.getCardinality() != null && !par.getCardinality().trim().equals("")) cardinalityComboBox.setSelectedItem(par.getCardinality()); else {
                        par.setCardinality("0..1");
                        cardinalityComboBox.setSelectedItem(par.getCardinality());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateReturnTypeComboBox() {
        if (element != null) {
            try {
                ArrayList<IElement> arr = element.getRootParent().getElements(DataType.class);
                Iterator<IElement> it = ElementImpl.sortElements(true, arr).iterator();
                if (typeComboBox.getItemCount() > 0) typeComboBox.removeAllItems();
                while (it.hasNext()) {
                    DataType t = (DataType) it.next();
                    try {
                        if (!(t instanceof UseCase) && !(t instanceof Actor) && !(t instanceof Operation) && !(t instanceof Parameter)) typeComboBox.addItem(t);
                    } catch (Exception e) {
                    }
                }
                try {
                    DataType selType = (DataType) element.getType();
                    if (selType != null) typeComboBox.setSelectedItem(selType); else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private javax.swing.JComboBox cardinalityComboBox;

    @SuppressWarnings("unused")
    private javax.swing.JCheckBox changeableCheckBox;

    private javax.swing.JCheckBox collectionCheckBox;

    private javax.swing.JCheckBox constantCheckBox;

    private javax.swing.JCheckBox derivedCheckBox;

    private javax.swing.JCheckBox derivedUnionCheckBox;

    private javax.swing.JLabel scopeLabel;

    private javax.swing.JLabel visibilityLabel;

    private javax.swing.JLabel cardinalityLabel;

    private javax.swing.JPanel checkBoxDecorationPanel;

    private javax.swing.JComboBox kindComboBox;

    private javax.swing.JCheckBox activeCheckBox;

    private javax.swing.JComboBox typeComboBox;

    private javax.swing.JLabel typeLabel;

    private javax.swing.JComboBox visibilityComboBox;

    private javax.swing.JCheckBox volatileCheckBox;

    private JCheckBox isListCheckBox = null;

    private JCheckBox insertCheckBox = null;

    private JCheckBox updateCheckBox = null;

    private JCheckBox deleteCheckBox = null;

    private JCheckBox abstractCheckBox = null;

    private boolean elementChanged = true;

    public void notifyElementChanged() {
        elementChanged = true;
        populateReturnTypeComboBox();
        populateCardinalityComboBox();
        initialize();
        super.updateUI();
        elementChanged = false;
    }

    /**
	 * This method initializes isListCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getIsListCheckBox() {
        if (isListCheckBox == null) {
            isListCheckBox = new JCheckBox();
            isListCheckBox.setText(messages.getMessage("List"));
        }
        return isListCheckBox;
    }

    /**
	 * This method initializes insertCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getInsertCheckBox() {
        if (insertCheckBox == null) {
            insertCheckBox = new JCheckBox();
            insertCheckBox.setText(messages.getMessage("Insert"));
        }
        return insertCheckBox;
    }

    /**
	 * This method initializes updateCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getUpdateCheckBox() {
        if (updateCheckBox == null) {
            updateCheckBox = new JCheckBox();
            updateCheckBox.setText(messages.getMessage("Update"));
        }
        return updateCheckBox;
    }

    /**
	 * This method initializes deleteCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getDeleteCheckBox() {
        if (deleteCheckBox == null) {
            deleteCheckBox = new JCheckBox();
            deleteCheckBox.setText(messages.getMessage("Delete"));
        }
        return deleteCheckBox;
    }

    /**
	 * This method initializes abstractCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getAbstractCheckBox() {
        if (abstractCheckBox == null) {
            abstractCheckBox = new JCheckBox();
            abstractCheckBox.setText(messages.getMessage("Abstract"));
            abstractCheckBox.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    element.setAbstract(abstractCheckBox.isSelected());
                }
            });
        }
        return abstractCheckBox;
    }

    public void ancestorResized(HierarchyEvent evt) {
        if (kindComboBox != null) kindComboBox.setSize((evt.getChanged().getSize().width - 100), kindComboBox.getSize().height);
        if (visibilityComboBox != null) visibilityComboBox.setSize((evt.getChanged().getSize().width - 100), visibilityComboBox.getSize().height);
        if (typeComboBox != null) typeComboBox.setSize((evt.getChanged().getSize().width - 100), typeComboBox.getSize().height);
        if (checkBoxDecorationPanel != null) checkBoxDecorationPanel.setSize((evt.getChanged().getSize().width - 100), checkBoxDecorationPanel.getSize().height);
        if (cardinalityComboBox != null) cardinalityComboBox.setSize((evt.getChanged().getSize().width - 100), cardinalityComboBox.getSize().height);
        updateUI();
    }

    public void ancestorMoved(HierarchyEvent evt) {
    }
}
