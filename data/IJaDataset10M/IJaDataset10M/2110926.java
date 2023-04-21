package org.mbari.vars.query.ui;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.mbari.swing.ListListModel;

/**
 * @author Brian Schlining
 * @version $Id: StringValuePanel.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class StringValuePanel extends ValuePanel {

    private static final long serialVersionUID = -8550914763894420669L;

    /**
	 * @uml.property  name="scrollPane"
	 * @uml.associationEnd  
	 */
    private JScrollPane scrollPane = null;

    /**
	 * @uml.property  name="list"
	 * @uml.associationEnd  
	 */
    private JList list = null;

    /**
	 * @uml.property  name="values"
	 */
    private final List values;

    /**
     * This is the default constructor
     *
     * @param name
     * @param values
     */
    public StringValuePanel(String name, List values) {
        super(name);
        this.values = values;
        initialize();
    }

    /**
	 * <p><!-- Method description --></p>
	 * @return
	 * @uml.property  name="list"
	 */
    private JList getList() {
        if (list == null) {
            list = new JList();
            list.setVisibleRowCount(4);
            list.setModel(new ListListModel(values));
            list.setEnabled(false);
        }
        return list;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public String getSQL() {
        StringBuffer sb = new StringBuffer();
        if (getConstrainCheckBox().isSelected()) {
            Object[] obj = getList().getSelectedValues();
            if (obj.length > 0) {
                sb.append(" ").append(getValueName()).append(" IN (");
                for (int i = 0; i < obj.length; i++) {
                    sb.append("'").append(obj[i].toString()).append("'");
                    if (obj.length > 0 && i < obj.length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(")");
            }
        }
        return sb.toString();
    }

    /**
	 * <p><!-- Method description --></p>
	 * @return
	 * @uml.property  name="scrollPane"
	 */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getList());
        }
        return scrollPane;
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        Dimension d = new Dimension(120, 80);
        setPreferredSize(d);
        setMinimumSize(d);
        this.add(getScrollPane(), null);
        getConstrainCheckBox().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                boolean enable = getConstrainCheckBox().isSelected();
                getList().setEnabled(enable);
            }
        });
    }
}
