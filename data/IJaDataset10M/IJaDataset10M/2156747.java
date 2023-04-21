package net.etherstorm.jopenrpg.swing.nodehandlers.d20tools;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import net.etherstorm.jopenrpg.swing.actions.DefaultAction;
import net.etherstorm.jopenrpg.swing.nodehandlers.d20char_handler;
import net.etherstorm.jopenrpg.util.ExceptionHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Class declaration
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 1.12 $
 * $Date: 2003/07/29 05:46:32 $
 */
public class FeatPanel extends d20CharPanel {

    FeatTableModel featModel;

    JTable featTable;

    /**
	 * Constructor declaration
	 * 
	 * 
	 * @param c
	 * @param e
	 * 
	 */
    public FeatPanel(d20char_handler c) {
        super(c);
        featModel = new FeatTableModel(handler);
        featTable = new JTable(featModel);
        add(new JScrollPane(featTable));
        JToolBar tb = new JToolBar();
        tb.add(new AddFeatAction());
        tb.add(new NewFeatAction());
        tb.addSeparator();
        tb.add(new MoveFeatUpAction());
        tb.add(new MoveFeatDownAction());
        tb.addSeparator();
        tb.add(new RemoveFeatAction());
        add(tb, BorderLayout.NORTH);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param url
	 *
	 */
    public void addFeat(java.net.URL url) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(url);
            ArrayList al = new ArrayList();
            List l = doc.getRootElement().getChildren("feat");
            Iterator iter = l.iterator();
            while (iter.hasNext()) {
                al.add(((Element) iter.next()).getAttributeValue("name"));
            }
            final JList list = new JList(al.toArray());
            list.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent lse) {
                    list.ensureIndexIsVisible(list.getSelectedIndex());
                }
            });
            int result = JOptionPane.showInternalConfirmDialog(this, new JScrollPane(list), "Add existing feat", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int index = list.getSelectedIndex();
                Element e = (Element) doc.getRootElement().getChildren("feat").get(index);
                handler.addFeat((Element) e.clone());
                featModel.fireTableChanged(new TableModelEvent(featModel, handler.getFeatList().size(), handler.getFeatList().size(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
            }
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doAddFeat() {
        addFeat(getResource("d20feats.xml"));
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doNewFeat() {
        handler.addFeat();
        featModel.fireTableChanged(new TableModelEvent(featModel, handler.getFeatList().size(), handler.getFeatList().size(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doRemoveFeat() {
        int index = featTable.getSelectedRow();
        if (index > -1) {
            handler.removeFeat(index);
        }
        featModel.fireTableChanged(new TableModelEvent(featModel, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doMoveFeatUp() {
        int index = featTable.getSelectedRow();
        if (index > 0) {
            handler.moveFeatUp(index);
            featModel.fireTableChanged(new TableModelEvent(featModel, index - 1, index));
            featTable.clearSelection();
            featTable.addRowSelectionInterval(index - 1, index - 1);
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doMoveFeatDown() {
        int index = featTable.getSelectedRow();
        if (index < handler.getFeatList().size() - 1) {
            handler.moveFeatDown(index);
            featModel.fireTableChanged(new TableModelEvent(featModel, index, index + 1));
            featTable.clearSelection();
            featTable.addRowSelectionInterval(index + 1, index + 1);
        }
    }

    /**
	 * Class declaration
	 * 
	 * 
	 * @author $Author: tedberg $
	 * @version $Revision: 1.12 $
	 */
    public class FeatTableModel extends d20TableModel {

        d20char_handler handler;

        /**
		 * Constructor declaration
		 * 
		 * 
		 * @param h
		 * 
		 */
        public FeatTableModel(d20char_handler h) {
            handler = h;
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param col
		 * 
		 * 
		 */
        public Class getColumnClass(int col) {
            return String.class;
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * 
		 */
        public int getColumnCount() {
            return 2;
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param col
		 * 
		 * 
		 */
        public String getColumnName(int col) {
            switch(col) {
                case 0:
                    return "Feat";
                case 1:
                    return "Type";
                default:
                    return "err";
            }
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * 
		 */
        public int getRowCount() {
            return handler.getFeatList().size();
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param row
		 * @param col
		 * 
		 * 
		 */
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                return handler.getFeatName(row);
            } else {
                return handler.getFeatCategory(row);
            }
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param row
		 * @param col
		 * 
		 * 
		 */
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param value
		 * @param row
		 * @param col
		 * 
		 */
        public void setValueAt(Object value, int row, int col) {
            if (col == 0) {
                handler.setFeatName(row, value.toString());
            } else {
                handler.setFeatCategory(row, value.toString());
            }
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.12 $
	 */
    public class NewFeatAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public NewFeatAction() {
            initProperties("d20.new.feat.action");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            super.actionPerformed(evt);
            doNewFeat();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.12 $
	 */
    public class AddFeatAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public AddFeatAction() {
            initProperties("d20.add.feat.action");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            super.actionPerformed(evt);
            doAddFeat();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.12 $
	 */
    public class MoveFeatUpAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public MoveFeatUpAction() {
            initProperties("d20.move.feat.up.action");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            super.actionPerformed(evt);
            doMoveFeatUp();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.12 $
	 */
    public class MoveFeatDownAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public MoveFeatDownAction() {
            initProperties("d20.move.feat.down.action");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            super.actionPerformed(evt);
            doMoveFeatDown();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.12 $
	 */
    public class RemoveFeatAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public RemoveFeatAction() {
            initProperties("d20.remove.feat.action");
        }

        /**
		 * Method declaration
		 *
		 *
		 * @param evt
		 *
		 */
        public void actionPerformed(ActionEvent evt) {
            super.actionPerformed(evt);
            doRemoveFeat();
        }
    }
}
