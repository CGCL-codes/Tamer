package com.xtech.common.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.LineBorder;
import com.xtech.common.entities.Entity;
import com.xtech.xerp.EntityEditAction;

/**
 * This component contains a group of entities, shows the selected one, 
 * and allows to change the selection via a ComboBox.
 * It uses a DataFieldModel to manage the Entities and the selection.
 * If there is a change in the selection, an ActionEvent will be fired. 
 * To listen for changes in the selection, register as an ActionListener.
 * @author jscruz
 * @since XERP
 */
public class DataField extends JPanel implements MouseListener {

    private JLabel label;

    private JComboBox combo;

    private CardLayout cards;

    private Vector listeners;

    private DataFieldModel model;

    private EntityEditAction editAction;

    private JPanel cardPanel;

    /**
	 * Constructs a new DataField with no entites;.
	 * @author jscruz
	 * @since XERP
	 */
    public DataField() {
        this(new Vector());
    }

    /**
	 * Constructs a new DataField with the entiies stored in the Enumeration.
	 * @author jscruz
	 * @since XERP
	 * @param en
	 */
    public DataField(Enumeration en) {
        this();
        model.addEntities(en);
    }

    /**
	 *  Constructs a new DataField with the entiies stored in the Vector.
	 * @author jscruz
	 * @since XERP
	 * @param v a Vector holding the Entities.
	 */
    public DataField(Vector v) {
        super();
        listeners = new Vector();
        model = new DataFieldModel(this);
        if (v != null) model.addEntities(v);
        cards = new CardLayout(2, 2);
        cardPanel = new JPanel(cards);
        setLayout(new BorderLayout(0, 2));
        label = new JLabel();
        label.setBorder(new LineBorder(Color.black, 1, true));
        label.setOpaque(true);
        label.setBackground(Color.CYAN);
        label.addMouseListener(this);
        label.setHorizontalAlignment(JLabel.CENTER);
        combo = new JComboBox(model);
        combo.setEditable(false);
        cardPanel.add(label, "SHOW");
        cardPanel.add(combo, "EDIT");
        add(cardPanel, BorderLayout.CENTER);
    }

    /**
	 * Sets the width of the combo in columns.
	 * Fills a string with <cols> characters and calls setPrototypeDisplayValue in the combo
	 * @param cols
	 * @author jscruz
	 * @since XERP
	 */
    public void setColumns(int cols) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cols; i++) {
            sb.append("X");
        }
        combo.setPrototypeDisplayValue(sb.toString());
    }

    /**
	 * Adds an ActionListener.
	 * @param l
	 * @author jscruz
	 * @since XERP
	 */
    public void addActionListener(ActionListener l) {
        if (listeners.contains(l)) return;
        listeners.addElement(l);
    }

    /**
	 * Notifies all the listeners that a change in the selection has ocurred.
	 * It fires an ActionEvent with an ID of ActionEvent.ACTION_PERFORMED
	 * and a command of "Item Selected"
	 * @author jscruz
	 * @since XERP
	 */
    public void fireActionPerformed() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Item Selected");
        for (Enumeration en = listeners.elements(); en.hasMoreElements(); ) {
            ActionListener al = (ActionListener) en.nextElement();
            if (al != null) al.actionPerformed(evt);
        }
        if (editAction != null) {
            editAction.getModel().setSelectedEntity(getSelectedID());
        }
    }

    /**
	 * Changes the view tho the JLabel and shows the Entity name into it.
	 * If asked, it will fire an event notifiying the listeners of the change.
	 * @param entity The selected entity
	 * @param notify if true, it will fire an ActionEvent to the listeners.
	 * @author jscruz
	 * @since XERP
	 */
    protected void setSelectedEntity(Entity entity, boolean notify) {
        if (entity == null) {
            label.setText("");
        } else {
            label.setText(entity.getName());
        }
        if (editAction != null) {
            editAction.getModel().setSelectedEntity(entity);
        }
        cards.show(cardPanel, "SHOW");
        if (notify) fireActionPerformed();
    }

    /**
	 * Selects a specific entity in the model. 
	 * @param ID
	 * @author jscruz
	 * @since XERP
	 */
    public void setSelectedID(String ID) {
        model.setSelectedID(ID);
        if (editAction != null) {
            editAction.getModel().setSelectedEntity(ID);
        }
    }

    /**
	 * Obtains the ID of the selected entity.
	 * If there is no selection, returns null.
	 * @return
	 * @author jscruz
	 * @since XERP
	 */
    public String getSelectedID() {
        if (model.getSelectedEntity() == null) return null;
        return model.getSelectedEntity().getID();
    }

    /**
	 * Implementation of MouseListener 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            cards.show(cardPanel, "EDIT");
        }
    }

    /**
	 *  Implementation of MouseListener
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
    public void mouseEntered(MouseEvent e) {
    }

    /**
	 *  Implementation of MouseListener
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
    public void mouseExited(MouseEvent e) {
    }

    /**
	 *  Implementation of MouseListener
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent e) {
    }

    /**
	 * Implementation of MouseListener
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent e) {
    }

    /**
	 * Obtains the model
	 * @return a reference to the model.
	 * @author jscruz
	 * @since XERP
	 */
    public DataFieldModel getModel() {
        return model;
    }

    /**
	 * Sets the model.
	 * @param model
	 * @author jscruz
	 * @since XERP
	 */
    public void setModel(DataFieldModel model) {
        this.model = model;
        if (editAction != null) {
            editAction.getModel().setSelectedEntity(getSelectedID());
        }
    }

    /**
	 * Gets the action associated to the edit button.
	 * @return
	 * @author jscruz
	 * @since XERP
	 */
    public EntityEditAction getEditAction() {
        return editAction;
    }

    /**
	 * Adds a button with the specified action.
	 * The model of the action will be updated when an entity is selected.
	 * @param action
	 * @author jscruz
	 * @since XERP
	 */
    public void setEditAction(EntityEditAction action) {
        editAction = action;
        if (editAction != null) {
            editAction.getModel().refreshData();
            editAction.getModel().setSelectedEntity(getSelectedID());
        }
        JButton b = new JButton(action);
        if (b.getIcon() != null) b.setText(null);
        add(b, BorderLayout.EAST);
    }
}
