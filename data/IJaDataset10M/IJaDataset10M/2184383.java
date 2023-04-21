package jasperdesign.ui;

import com.l2fprod.common.model.DefaultBeanInfoResolver;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import jasperdesign.ui.jrtree.JRTreeNode;
import java.awt.BorderLayout;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author manningj
 */
public class PropertiesPanel extends JPanel implements SelectionListener, TreeSelectionListener {

    PropertySheetPanel sheet;

    PropertyChangeListener sheetListener;

    DefaultBeanInfoResolver resolver = new DefaultBeanInfoResolver();

    SelectionModel selection;

    /** Creates a new instance of PropertiesPanel */
    public PropertiesPanel() {
        super(new BorderLayout());
    }

    public PropertySheetPanel getPropertySheet() {
        return sheet;
    }

    public void setSelectionModel(SelectionModel m) {
        selection = m;
        selection.addSelectionListener(this);
    }

    public void setElement(final Object data) {
        System.out.print("PropertiesPanel.setElement ");
        if (data != null) {
            System.out.println(data.getClass().getName());
        } else {
            System.out.println("null");
        }
        if (sheetListener != null) {
            sheet.removePropertyChangeListener(sheetListener);
            sheetListener = null;
        }
        if (sheet != null) {
            remove(sheet);
            sheet = null;
        }
        if (data != null) {
            sheet = new PropertySheetPanel();
            BeanInfo beanInfo = resolver.getBeanInfo(data);
            sheet.setMode(PropertySheet.VIEW_AS_CATEGORIES);
            sheet.setProperties(beanInfo.getPropertyDescriptors());
            sheet.readFromObject(data);
            sheet.setDescriptionVisible(true);
            sheet.setSortingCategories(true);
            sheet.setSortingProperties(true);
            sheetListener = new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    Property prop = (Property) evt.getSource();
                    prop.writeToObject(data);
                    System.out.println("Updated object to " + data);
                }
            };
            sheet.addPropertySheetChangeListener(sheetListener);
            add(sheet, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    public void valueChanged(SelectionEvent event) {
        setElement(selection.getSelectedElement());
    }

    public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
        Object sel = e.getNewLeadSelectionPath().getLastPathComponent();
        System.out.println("tree selection " + sel.getClass().getName());
        if (sel instanceof JRTreeNode) {
            setElement(((JRTreeNode) sel).getElement());
        } else {
            setElement(null);
        }
    }
}
