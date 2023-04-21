package com.marcinjunger.utils.hibernate.beanatoms;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import javax.naming.directory.InvalidAttributesException;
import org.hibernate.Query;
import org.hibernate.Session;
import com.marcinjunger.utils.beanatoms.MJComboBox;

/**
 * Standard JComboBox plus: 1)transferring the ESCape key event to component's
 * root pane 2)reading items from database through hibernate in two modes: A)
 * selecting object (then specify valuePropertyName and displayTextPropertyName
 * properties) - for example for sSQLGetItems = "from Dic d" and B) selecting
 * two fields, integer and string where integer is id, and string name, for
 * example sSQLGetItems = "select d.ndic, d.valu from Dic d"
 * 
 * @author Marcin Junger, mjunger@hornet.eu.org
 */
public class MJHibernateComboBox extends MJComboBox {

    /**
   * Serialisation id
   */
    private static final long serialVersionUID = -7450888109397637566L;

    /**
   * Default and only constructor
   */
    public MJHibernateComboBox() {
    }

    /**
   * Hibernate database connection
   */
    public Session hibernateConnection;

    /**
   * Name of property in objects returned by Hibernate that contain the integer
   * (!!!) value to be returned by combo box;
   * 
   * @example "getId"
   */
    public String valuePropertyName;

    /**
   * Name of property in objects returned by Hibernate that contain the value to
   * be displayed by combo box;
   * 
   * @example "getName"
   */
    public String displayTextPropertyName;

    /**
   * This is list of objects built in RefreshList. It stores objects that refer
   * to values in the combo box.
   */
    private List<Object> hibernateObjects;

    /**
   * Refreshes the list; as a current item uses lLastSelectedItemID or default
   * value: -CT_SQL: sets first record as selected item; -CD_DATE: today
   * -CT_NAME_VALUE: inherited behaviour from JComboBox
   * 
   * @throws Exception
   */
    @SuppressWarnings("all")
    public void RefreshList() throws Exception {
        try {
            String lLastSelectedItemID = sLastSelectedItemID;
            if (ComboType == ComboBoxType.CT_SQL) {
                removeAllItems();
                if (sSQLGetItems.equals("")) {
                    throw new InvalidAttributesException("sqlGetItems not set!");
                }
                Query query = hibernateConnection.createQuery(sSQLGetItems);
                int lStartupItem = 0;
                if (displayTextPropertyName != null && !displayTextPropertyName.isEmpty() && valuePropertyName != null && !valuePropertyName.isEmpty()) {
                    hibernateObjects = query.list();
                    for (Object record : hibernateObjects) {
                        Class recordClass = record.getClass();
                        Method getName = recordClass.getMethod(displayTextPropertyName, null);
                        Method getValue = recordClass.getMethod(valuePropertyName, null);
                        if (getName == null || getValue == null) {
                            throw new InvalidClassException("Cannot locate '" + displayTextPropertyName + "' and/or '" + valuePropertyName + "' methods inside '" + recordClass + "' class");
                        }
                        Integer id = (Integer) getValue.invoke(record, null);
                        addItem(new MJSQLComboItem(id, (String) getName.invoke(record, null)));
                        if (id.toString().equals(lLastSelectedItemID)) {
                            lStartupItem = getItemCount() - 1;
                        }
                        if (getItemCount() > 0) {
                            setSelectedIndex(lStartupItem);
                        }
                    }
                } else if ((displayTextPropertyName == null || displayTextPropertyName.isEmpty()) && (valuePropertyName == null || valuePropertyName.isEmpty())) {
                    for (Iterator it = query.iterate(); it.hasNext(); ) {
                        Object[] row = (Object[]) it.next();
                        addItem(new MJSQLComboItem(Integer.parseInt(row[0].toString()), row[1].toString()));
                    }
                } else {
                    throw new IllegalStateException("Invalid state of fields: valuePropertyName && displayTextPropertyName");
                }
            } else {
                super.RefreshList();
            }
        } catch (Exception lEx) {
            removeAllItems();
            throw lEx;
        }
    }

    /**
   * Returns object that refers to currently selected item in combo box
   * 
   * @return Hibernate object or null
   * @throws NoSuchMethodException
   * @throws SecurityException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   */
    @SuppressWarnings("all")
    public Object getHibernateObject() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object result = null;
        for (Object record : hibernateObjects) {
            Class recordClass = record.getClass();
            Method getValue = recordClass.getMethod(valuePropertyName, null);
            Integer id = (Integer) getValue.invoke(record, null);
            if (id.toString().equals(getValue())) {
                result = record;
                break;
            }
        }
        return result;
    }
}
