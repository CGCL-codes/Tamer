package fr.itris.glips.extension.jwidget.externaldatatable.runtime;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import javax.swing.table.*;
import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.externaldatatable.edition.*;
import fr.itris.glips.extension.jwidget.externaldatatable.runtime.anim.*;
import fr.itris.glips.rtda.jwidget.*;
import fr.itris.glips.rtda.animaction.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class of the view browser runtime jwidget
 * @author ITRIS, Jordi SUC
 */
public class ExternalDataTableRuntime extends JWidgetRuntime {

    /**
	 * the date format
	 */
    protected static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);

    /**
	 * the date and time format
	 */
    protected static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    /**
	 * the jwidget id
	 */
    public static String jwidgetId = "ExternalDataTableWidget";

    /**
	 * the table widget
	 */
    private ExternalDataTableWidget tableWidget;

    /**
	 * the object used to access and make requests to the db
	 */
    private ExternalDataTableDataBaseAccess dataBaseAccess;

    /**
	 * the map associating a field name to its labels
	 */
    private Map<String, String> fieldNameToLabelMap = new HashMap<String, String>();

    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public ExternalDataTableRuntime(SVGPicture picture, Element element) {
        super(picture, element);
    }

    @Override
    public void initialize() {
        tableWidget = new ExternalDataTableWidget(this, element);
        component = tableWidget;
    }

    @Override
    public void initializeWhenCanvasDisplayed() {
        JWidgetToolkit.resizeColumns(tableWidget.getTable());
        tableWidget.validate();
    }

    @Override
    public JWidgetAnimation createAnimation(Element animationElement) {
        JWidgetAnimation animation = null;
        if (animationElement != null) {
            String tagName = animationElement.getTagName();
            if (tagName.equals("rtda:loadData")) {
                animation = new ExternalDataTableLoadData(this, component, animationElement);
            }
        }
        return animation;
    }

    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass() {
        return ExternalDataTableEdition.class;
    }

    @Override
    public void refreshComponentState() {
    }

    /**
     * sets the db access object
	 * @param dataBaseAccess
	 */
    public void setDataBaseAccess(ExternalDataTableDataBaseAccess dataBaseAccess) {
        this.dataBaseAccess = dataBaseAccess;
        reload();
    }

    /**
	 * sets the map of the labels of the table fields
	 * @param fieldNameToLabelMap the map associating a field name to its labels
	 */
    public void setFieldNameToLabelMap(Map<String, String> fieldNameToLabelMap) {
        this.fieldNameToLabelMap.putAll(fieldNameToLabelMap);
    }

    /**
     * reloads the data from the external data source
     */
    public void reload() {
        dataBaseAccess.retrieveData(dataBaseAccess.getRequest());
    }

    /**
     * method used for receiving data form the db
     * @param data the data found in the db, 
     * the first array is the array of the fields of the table
     */
    public void receiveData(final LinkedList<Object[]> data) {
        if (data.size() > 0) {
            Object[] colNames = data.getFirst();
            data.removeFirst();
            final String[] columnNames = new String[colNames.length];
            for (int i = 0; i < colNames.length; i++) {
                columnNames[i] = (String) colNames[i];
            }
            DefaultTableModel model = new DefaultTableModel() {

                @Override
                public String getColumnName(int col) {
                    String label = fieldNameToLabelMap.get(columnNames[col]);
                    if (label == null || label.equals("")) {
                        label = columnNames[col];
                    }
                    return label;
                }

                @Override
                public int getColumnCount() {
                    return columnNames.length;
                }

                @Override
                public int getRowCount() {
                    return data.size();
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public Object getValueAt(int row, int col) {
                    Object value = " ";
                    try {
                        Object[] dataArray = data.get(row);
                        value = dataArray[col];
                        if (value != null) {
                            if (value instanceof Timestamp) {
                                value = dateTimeFormat.format((Date) value);
                            } else if (value instanceof Date) {
                                value = dateFormat.format((Date) value);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return value;
                }
            };
            tableWidget.setTableModel(model);
        } else {
            tableWidget.setTableModel(new DefaultTableModel());
        }
        tableWidget.initialize();
    }

    @Override
    public void registerListeners() {
    }

    @Override
    public void unregisterListeners() {
    }

    @Override
    public void dispose() {
        tableWidget.dispose();
        super.dispose();
    }
}
