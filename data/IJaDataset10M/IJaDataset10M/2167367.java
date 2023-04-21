package net.frede.gui.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import net.frede.gui.program.Argument;

/**
 * a graphical component that allows the net.frede.gui.user to enter a string
 */
public class ArgumentComponentString extends ArgumentComponent implements ItemListener, CaretListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * the combo box that the end-net.frede.gui.user will use to provide a new
	 * value for the argument
	 */
    private JComboBox input;

    /**
	 * default constructor
	 * 
	 * @param a
	 *            DOCUMENT ME!
	 */
    public ArgumentComponentString(Argument a) {
        super(a);
        build();
    }

    /**
	 * called whenever the net.frede.gui.user has entered a new character
	 * 
	 * @param e
	 *            not used
	 */
    public void caretUpdate(CaretEvent e) {
        String item = (String) input.getEditor().getItem();
        if (item.length() == 0) {
            item = null;
        }
        update(item);
    }

    /**
	 * called whenever the net.frede.gui.user has selected an entry in the
	 * combobox
	 * 
	 * @param e
	 *            not used
	 */
    public void itemStateChanged(ItemEvent e) {
        String item = (String) input.getSelectedItem();
        if (item != null) {
            update(item);
            int index = input.getItemCount();
            boolean found = false;
            for (int i = 0; i < index; i++) {
                if (item.equals(input.getItemAt(i))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                input.addItem(item);
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param values
	 *            DOCUMENT ME!
	 */
    protected void setHistory(List values) {
        Collections.reverse(values);
        Iterator it = values.iterator();
        while (it.hasNext()) {
            List l = (List) it.next();
            if (l.size() > 0) {
                add(l.get(0));
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param values
	 *            DOCUMENT ME!
	 */
    protected void setUserInput(Collection values) {
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param o
	 *            DOCUMENT ME!
	 */
    private void add(Object o) {
        int count = input.getItemCount();
        Object old = input.getItemAt(count - 1);
        if ((count != 0) && old.equals(o)) {
            input.setSelectedItem(old);
        } else {
            input.addItem(o);
        }
    }

    /**
	 * builds this graphical component
	 */
    private void build() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.gridx = 1;
        constraints.gridy = 0;
        input = new JComboBox();
        input.addItemListener(this);
        ((JTextComponent) input.getEditor().getEditorComponent()).addCaretListener(this);
        input.setEditable(true);
        add(input, constraints);
        build(getArgument());
    }

    /**
	 * updates the argument with the new value selected by net.frede.gui.user
	 * 
	 * @param item
	 *            the new value selected by net.frede.gui.user
	 */
    private void update(String item) {
        setValue(item);
    }
}
