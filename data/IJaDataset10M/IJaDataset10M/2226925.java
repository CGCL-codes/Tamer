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
 * @version $Revision: 1.13 $
 * $Date: 2003/07/29 05:46:32 $
 */
public class ClassesPanel extends d20CharPanel {

    ClassesModel classModel;

    JTable classTable;

    /**
	 * Constructor declaration
	 * 
	 * 
	 * @param e
	 * 
	 */
    public ClassesPanel(d20char_handler c) {
        super(c);
        classModel = new ClassesModel(c);
        classTable = new JTable(classModel);
        add(new JScrollPane(classTable));
        JToolBar tb = new JToolBar();
        tb.add(new AddClassAction());
        tb.add(new NewClassAction());
        tb.addSeparator();
        tb.add(new MoveClassUpAction());
        tb.add(new MoveClassDownAction());
        tb.addSeparator();
        tb.add(new RemoveClassAction());
        add(tb, BorderLayout.NORTH);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param url
	 *
	 */
    public void addClass(java.net.URL url) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(url);
            ArrayList al = new ArrayList();
            List l = doc.getRootElement().getChildren("class");
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
            int result = JOptionPane.showInternalConfirmDialog(this, new JScrollPane(list), "Add existing class", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int index = list.getSelectedIndex();
                Element e = (Element) doc.getRootElement().getChildren("class").get(index);
                handler.addClass((Element) e.clone());
                classModel.fireTableChanged(new TableModelEvent(classModel, handler.getClassList().size(), handler.getClassList().size(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
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
    public void doAddClass() {
        addClass(getResource("d20classes.xml"));
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doNewClass() {
        handler.addClass();
        classModel.fireTableChanged(new TableModelEvent(classModel, handler.getClassList().size(), handler.getClassList().size(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doRemoveClass() {
        int index = classTable.getSelectedRow();
        if (index > -1) {
            handler.removeClass(index);
        }
        classModel.fireTableChanged(new TableModelEvent(classModel, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doMoveClassUp() {
        int index = classTable.getSelectedRow();
        if (index > 0) {
            handler.moveClassUp(index);
            classModel.fireTableChanged(new TableModelEvent(classModel, index - 1, index));
            classTable.clearSelection();
            classTable.addRowSelectionInterval(index - 1, index - 1);
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 */
    public void doMoveClassDown() {
        int index = classTable.getSelectedRow();
        if (index < handler.getClassList().size() - 1) {
            handler.moveClassDown(index);
            classModel.fireTableChanged(new TableModelEvent(classModel, index, index + 1));
            classTable.clearSelection();
            classTable.addRowSelectionInterval(index + 1, index + 1);
        }
    }

    /**
	 * Class declaration
	 * 
	 * 
	 * @author $Author: tedberg $
	 * @version $Revision: 1.13 $
	 */
    class ClassesModel extends d20TableModel {

        /**
		 * Constructor declaration
		 * 
		 * 
		 * @param c
		 * 
		 */
        public ClassesModel(d20char_handler c) {
            super(c);
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param index
		 * 
		 * 
		 */
        void fireCellChanged(int index) {
            TableModelEvent tme = new TableModelEvent(this, index, 1, 1);
            fireTableChanged(tme);
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param columnIndex
		 * 
		 * 
		 * 
		 */
        public Class getColumnClass(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * 
		 * 
		 */
        public int getColumnCount() {
            return 3;
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param columnIndex
		 * 
		 * 
		 * 
		 */
        public String getColumnName(int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return "Class";
                case 1:
                    return "Hit Dice";
                case 2:
                    return "Level";
                default:
                    return "err";
            }
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * 
		 * 
		 */
        public int getRowCount() {
            try {
                return handler.getClassList().size();
            } catch (NullPointerException npe) {
                return 0;
            }
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param rowIndex
		 * @param columnIndex
		 * 
		 * 
		 * 
		 */
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return handler.getClassName(rowIndex);
                case 1:
                    return handler.getClassHd(rowIndex);
                case 2:
                    return new Integer(handler.getClassLevel(rowIndex));
                default:
                    return "err";
            }
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param rowIndex
		 * @param columnIndex
		 * 
		 * 
		 * 
		 */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        /**
		 * Method declaration
		 * 
		 * 
		 * @param aValue
		 * @param rowIndex
		 * @param columnIndex
		 * 
		 * 
		 */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    handler.setClassName(rowIndex, aValue.toString());
                    break;
                case 1:
                    handler.setClassHd(rowIndex, aValue.toString());
                    break;
                case 2:
                    handler.setClassLevel(rowIndex, ((Integer) aValue).intValue());
                    break;
                default:
                    return;
            }
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.13 $
	 */
    public class NewClassAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public NewClassAction() {
            initProperties("d20.new.class.action");
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
            doNewClass();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.13 $
	 */
    public class AddClassAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public AddClassAction() {
            initProperties("d20.add.class.action");
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
            doAddClass();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.13 $
	 */
    public class MoveClassUpAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public MoveClassUpAction() {
            initProperties("d20.move.class.up.action");
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
            doMoveClassUp();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.13 $
	 */
    public class MoveClassDownAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public MoveClassDownAction() {
            initProperties("d20.move.class.down.action");
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
            doMoveClassDown();
        }
    }

    /**
	 * Class declaration
	 *
	 *
	 * @author $Author: tedberg $
	 * @version $Revision: 1.13 $
	 */
    public class RemoveClassAction extends DefaultAction {

        /**
		 * Constructor declaration
		 *
		 *
		 */
        public RemoveClassAction() {
            initProperties("d20.remove.class.action");
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
            doRemoveClass();
        }
    }
}
