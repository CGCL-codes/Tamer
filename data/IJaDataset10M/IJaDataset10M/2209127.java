package net.sf.rcpforms.experimenting.rcp.BACK;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.experimenting.binding2.BasicModelAdapterFactory;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.modeladapter.util.Validate;
import net.sf.rcpforms.tablesupport.Activator;
import net.sf.rcpforms.tablesupport.Messages;
import net.sf.rcpforms.tablesupport.tables.ColumnConfiguration;
import net.sf.rcpforms.tablesupport.tables.ECellEditorType;
import net.sf.rcpforms.tablesupport.tables.ITableSelectable;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;

public class PropertyLabelProviderAndCellModifier extends net.sf.rcpforms.tablesupport.tables.PropertyLabelProviderAndCellModifier {

    protected boolean m_showErrorDialog = false;

    protected static final Logger LOG = Logger.getLogger(PropertyLabelProviderAndCellModifier.class.getName());

    protected static Color DISABLED_RGB_BG_COLOR = null;

    protected static Color DISABLED_RGB_FG_COLOR = null;

    /** rgb bg color for disabled rows, light gray */
    protected static final RGB DISABLED_RGB_BG = new RGB(170, 170, 170);

    protected static final RGB DISABLED_RGB_FG = new RGB(50, 50, 50);

    protected static final String CHECKED_KEY = "net.sourceforge.rcpforms.checked";

    protected static final String UNCHECKED_KEY = "net.sourceforge.rcpforms.unchecked";

    protected static final String CHECKED_KEY_DEACTIVATED = "net.sourceforge.rcpforms.checked_deactivated";

    protected static final String UNCHECKED_KEY_DEACTIVATED = "net.sourceforge.rcpforms.unchecked_deactivated";

    /** converter map key */
    protected static final class ConverterKey {

        private final Object fromType;

        private final Object toType;

        /**
         * Constructor for ConverterKey
         * 
         * @param fromType fromType
         * @param toType toType
         */
        public ConverterKey(final Object fromType, final Object toType) {
            super();
            Validate.notNull(fromType);
            Validate.notNull(toType);
            this.fromType = fromType;
            this.toType = toType;
        }

        @Override
        public boolean equals(final Object obj) {
            final ConverterKey conv = (ConverterKey) obj;
            return fromType.equals(conv.fromType) && toType.equals(conv.toType);
        }

        @Override
        public int hashCode() {
            return fromType.hashCode() + 23 * toType.hashCode();
        }

        @Override
        public String toString() {
            return "(" + fromType.toString() + "->" + toType.toString() + ")";
        }
    }

    /** the column configurations to use */
    protected ColumnConfiguration[] m_columnConfigurations;

    /** cached converters */
    protected Map<ConverterKey, IConverter> m_converterCache = new HashMap<ConverterKey, IConverter>();

    protected Object m_rowElementMetaClass;

    /**
     * flag if the table is readonly, i.e. cannot be modified and private boolean readonly; /**
     */
    protected boolean m_readonly;

    /**
     * create an unitialised PropertyLabelProvider. PAY ATTENTION: if using this constructor you
     * have to make sure that the object is properly initialised calling init(ColumnConfiguration[],
     * Class<?>) on it.
     */
    public PropertyLabelProviderAndCellModifier() {
    }

    /**
     * create a PropertyLabelProvider for the columns defined in the given array of column
     * configurations.
     * 
     * @param columnConfigurations column i will be defined by the columnConfigurations[i]
     * @param rowElementMetaClass row element meta class, a Class for beans, an EClass for EMF
     *            objects
     */
    public PropertyLabelProviderAndCellModifier(final ColumnConfiguration[] columnConfigurations, final Object rowElementMetaClass) {
        init(columnConfigurations, rowElementMetaClass);
        initImages();
    }

    /** set if error dialog should be shown on exception in modify of cell editor */
    @Override
    public void setShowErrorDialog(final boolean newValue) {
        this.m_showErrorDialog = newValue;
    }

    @Override
    public void init(final ColumnConfiguration[] columnConfigurations, final Object rowElementClass) {
        this.m_columnConfigurations = columnConfigurations;
        this.m_rowElementMetaClass = rowElementClass;
        Validate.noNullElements(columnConfigurations);
        Validate.notNull(rowElementClass);
        validatePropertiesAndFillEnumComboValues();
        if (getDisabledRGBBGColor() == null) {
            DISABLED_RGB_BG_COLOR = new Color(Display.getCurrent(), DISABLED_RGB_BG);
        }
        if (getDisabledRGBFGColor() == null) {
            DISABLED_RGB_FG_COLOR = new Color(Display.getCurrent(), DISABLED_RGB_FG);
        }
    }

    protected static void initImages() {
        if (JFaceResources.getImageRegistry().getDescriptor(CHECKED_KEY) == null) {
            JFaceResources.getImageRegistry().put(UNCHECKED_KEY, makeShot(false, true));
            JFaceResources.getImageRegistry().put(CHECKED_KEY, makeShot(true, true));
            JFaceResources.getImageRegistry().put(UNCHECKED_KEY_DEACTIVATED, makeShot(false, false));
            JFaceResources.getImageRegistry().put(CHECKED_KEY_DEACTIVATED, makeShot(true, false));
        }
    }

    protected static Image makeShot(final boolean type, final boolean activated) {
        final Color greenScreen = new Color(Display.getCurrent(), 222, 223, 224);
        final Shell shell = new Shell(Display.getCurrent(), SWT.NO_TRIM);
        shell.setBackground(greenScreen);
        final Button button = new Button(shell, SWT.CHECK | SWT.FLAT);
        button.setEnabled(activated);
        button.setBackground(greenScreen);
        button.setSelection(type);
        button.setLocation(1, 1);
        final Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
        bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
        button.setSize(bsize);
        shell.setSize(bsize);
        shell.open();
        final GC gc = new GC(shell);
        final Image image = new Image(Display.getCurrent(), bsize.x, bsize.y);
        gc.copyArea(image, 0, 0);
        gc.dispose();
        shell.close();
        image.getImageData().palette.getPixel(greenScreen.getRGB());
        return image;
    }

    /**
     * get a converter for the given columnindex, fromType, toType. Converters are cached and
     * reused.
     * 
     * @return converter to use for the given column and type
     */
    @Override
    protected IConverter getConverter(final ModelAdapter adapter, final Object fromType, final Object toType) {
        final ConverterKey key = new ConverterKey(fromType, toType);
        IConverter converter = m_converterCache.get(key);
        if (converter == null) {
            converter = adapter.getConverterRegistry().getConverter(fromType, toType);
            m_converterCache.put(key, converter);
        }
        return converter;
    }

    /**
     * retrieves the property of element defined by the columnIndex-th columnConfiguration, converts
     * it using the default converter and returns it
     * 
     * @return string to display in table cell row 'element', column 'columnIndex'
     */
    @Override
    public String getColumnText(final Object element, final int columnIndex) {
        String result = "";
        final String property = m_columnConfigurations[columnIndex].getProperty();
        if (property != null && property.length() > 0) {
            final ModelAdapter adapter = getModelAdapter(element);
            final Object value = getProperty(element, property);
            result = convertValueToString(adapter, value, m_columnConfigurations[columnIndex]);
        }
        return result;
    }

    /**
     * Returns the value which is set into the cell editor.
     * 
     * @param element data model to retrieve property from
     * @param property property to retrieve from element
     * @return value of the property, converted to cell editor representation
     */
    @Override
    public Object getValue(final Object element, final String property) {
        Object value = null;
        try {
            final ModelAdapter adapter = getModelAdapter(element);
            final ColumnConfiguration conf = getColumnConfiguration(property);
            value = getProperty(element, property);
            if (value != null) {
                final IConverter converter = getConverter(adapter, value.getClass(), conf.getCellEditorType().getValueType());
                value = converter.convert(value);
            }
            switch(conf.getCellEditorType()) {
                case COMBO:
                    int index = -1;
                    for (int i = 0; i < conf.getComboItems().length; i++) {
                        if (value != null && value.equals(conf.getComboItems()[i])) {
                            index = i;
                            break;
                        }
                    }
                    value = new Integer(index);
                    break;
                case CHECK:
                    if (value == null) {
                        value = Boolean.FALSE;
                    }
                    Validate.notNull(value, "Null Value received in " + this.getClass().getSimpleName() + ". Is converter from null to Boolean missing ?");
                    Validate.isTrue(value instanceof Boolean, "Value for CheckboxCellEditor is not of type Boolean. Is converter from null to Boolean missing ?");
                    break;
                case TEXT:
                    if (value == null) {
                        value = "";
                    }
                    break;
                default:
                    if (value == null) {
                        value = "";
                    }
                    break;
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            final String message = "Error in Provider: " + getClass().getName() + " getting cell editor value for property " + property + ": " + ex.getMessage();
            LOG.severe(message);
            throw new RuntimeException(message, ex);
        }
        Validate.notNull(value);
        return value;
    }

    /**
     * if a value is edited by a cell editor, this method is used to set the edited value back to
     * the model. The value is converted from cell editor representation to model representation and
     * then set to the given property of the given element.
     * <p>
     * A {@link LabelProviderChangedEvent} must be fired on modification, otherwise the table will
     * not show the changes.
     * <p>
     * ATTENTION: combo cell editors use an Integer index into the combo list as their value
     */
    @Override
    public void modify(Object element, final String property, final Object value) {
        try {
            final ModelAdapter adapter = getModelAdapter(element);
            if (element instanceof Item) {
                element = ((Item) element).getData();
            }
            final ColumnConfiguration conf = getColumnConfiguration(property);
            Object comboValue = value;
            if (conf.getCellEditorType() == ECellEditorType.COMBO) {
                final Integer in = (Integer) value;
                if (in != -1) {
                    comboValue = conf.getComboItems()[in];
                } else {
                    comboValue = null;
                }
            }
            Object result = null;
            if (comboValue != null) {
                final Object propertyType = getPropertyType(element, property);
                final IConverter converter = getConverter(adapter, conf.getCellEditorType().getValueType(), propertyType);
                result = converter.convert(comboValue);
                if (NullValue.isNullValue(result)) {
                    result = null;
                }
            }
            setProperty(element, property, result);
        } catch (final Exception ex) {
            final String message = MessageFormat.format(Messages.getString("PropertyLabelProviderAndCellModifier.CellModificationError") + ex.getMessage(), getClass().getName(), property);
            LOG.severe(message);
            if (m_showErrorDialog) {
                new ErrorDialog(Display.getCurrent().getActiveShell(), Messages.getString("PropertyLabelProviderAndCellModifier.CellModificationErrorTitle"), MessageFormat.format(Messages.getString("PropertyLabelProviderAndCellModifier.CellModificationErrorDetail"), value, property), new Status(Status.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex), IStatus.ERROR).open();
            } else {
                throw new IllegalArgumentException(message);
            }
        }
    }

    protected Object getPropertyType(final Object modelInstance, final String propertyPath) {
        final ModelAdapter modelAdapter = getModelAdapter(modelInstance);
        final IPropertyChain propertyChain = modelAdapter.getPropertyChain(modelAdapter.getMetaClass(modelInstance), propertyPath);
        final Object result = propertyChain.getType();
        return result;
    }

    protected ModelAdapter getModelAdapter(final Object modelInstance) {
        return ModelAdapter.getAdapterForInstance(modelInstance);
    }

    /**
     * override to support images which depend on the given element. images are only provided for
     * {@link ECellEditorType#CHECK} columns}
     * 
     * @return image to use for the given element in the given column
     */
    @Override
    public Image getColumnImage(final Object element, final int columnIndex) {
        final ColumnConfiguration conf = m_columnConfigurations[columnIndex];
        Image returnImage = null;
        if (columnIndex > 0 && conf.getCellEditorType() == ECellEditorType.CHECK) {
            final Boolean booleanValue = this.getBooleanPropertyValue(element, conf.getProperty());
            if (booleanValue != null) {
                if (element instanceof ITableSelectable && !((ITableSelectable) element).getIsSelectable()) {
                    returnImage = JFaceResources.getImageRegistry().get(booleanValue.booleanValue() ? CHECKED_KEY_DEACTIVATED : UNCHECKED_KEY_DEACTIVATED);
                } else {
                    returnImage = JFaceResources.getImageRegistry().get(booleanValue.booleanValue() ? CHECKED_KEY : UNCHECKED_KEY);
                }
            }
        }
        return returnImage;
    }

    /**
     * @param element data model
     * @param property the property to get boolean value from. not null
     * @return boolean or null if either element or property were not applicable
     */
    protected Boolean getBooleanPropertyValue(final Object element, final String property) {
        Boolean returnValue = null;
        try {
            final PropertyDescriptor pd = new PropertyDescriptor(property, element.getClass());
            final Method getterMethod = pd.getReadMethod();
            if (getterMethod.getReturnType() != Boolean.class && getterMethod.getReturnType() != Boolean.TYPE) throw new IllegalArgumentException("Getter method for property " + property + " on " + element.getClass().toString() + " does not return a boolean!");
            returnValue = (Boolean) getterMethod.invoke(element, new Object[] {});
        } catch (final IntrospectionException e) {
            LOG.severe(e.getMessage());
        } catch (final IllegalArgumentException e) {
            LOG.severe(e.getMessage());
        } catch (final IllegalAccessException e) {
            LOG.severe(e.getMessage());
        } catch (final InvocationTargetException e) {
            LOG.severe(e.getMessage());
        }
        return returnValue;
    }

    /**
     * converts the given model value to string representation
     * 
     * @param value model value, usually a Java type like String, Date, Integer, Boolean
     * @param conf column for which to convert the value
     * @return string representation for the given value, must not be null
     */
    @Override
    public String convertValueToString(final ModelAdapter adapter, final Object value, final ColumnConfiguration conf) {
        String result = "";
        if (value != null) {
            final IConverter converter = getConverter(adapter, value.getClass(), String.class);
            result = (String) converter.convert(value);
        }
        return result;
    }

    /**
     * Returns the property of the model object using the ModelAdapter
     * 
     * @param modelInstance the model for the row to retrieve the property from
     * @param propertyPath The property name of bean, nested properties are supported
     * @return value of the given property
     * @throws IllegalArgumentException if property cannot be read
     */
    @Override
    protected Object getProperty(final Object modelInstance, final String propertyPath) {
        final ModelAdapter modelAdapter = getModelAdapter(modelInstance);
        final Object result = modelAdapter.getValue(modelInstance, propertyPath);
        return result;
    }

    /**
     * Adds a value to a bean property by calling the apropriate setter method by introspection
     * 
     * @param element the object where the value should be set
     * @param property the field where the value should be set
     * @param value the value to be set
     */
    protected void setProperty(final Object modelInstance, final String propertyPath, final Object value) {
        final ModelAdapter modelAdapter = getModelAdapter(modelInstance);
        modelAdapter.setValue(modelInstance, propertyPath, value);
    }

    /**
     * Decides if a certain property of a bean can be modified. A property is represented as column
     * inside a table.
     * 
     * @param element The bean
     * @param property The bean property
     * @return True if property is modifiable
     */
    @Override
    public boolean canModify(final Object element, final String property) {
        boolean result = false;
        if (!isReadonly()) {
            for (final ColumnConfiguration config : m_columnConfigurations) {
                if (property != null && property.equals(config.getProperty()) && config.getCellEditorType() != null) {
                    result = true;
                    if (element instanceof ITableSelectable) {
                        final ITableSelectable dlam = (ITableSelectable) element;
                        if (!dlam.getIsSelectable()) {
                            result = false;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    /**
     * gets the readonly style for the label provider; if it is set to true, no editing is possible.
     * 
     * @param m_readonly
     */
    @Override
    public boolean isReadonly() {
        return m_readonly;
    }

    /**
     * sets the readonly style for the label provider; if it is set to true, no editing is possible.
     * 
     * @param readonly
     */
    @Override
    public void setReadonly(final boolean readonly) {
        this.m_readonly = readonly;
    }

    /**
     * Returns the column configuration associated with the bean property
     * 
     * @param property the bean property
     * @return Column configuration for the column associated with the bean property, null if
     *         property not found
     */
    protected ColumnConfiguration getColumnConfiguration(final String property) {
        ColumnConfiguration result = null;
        for (final ColumnConfiguration conf : m_columnConfigurations) {
            if (property.equals(conf.getProperty())) {
                result = conf;
                break;
            }
        }
        return result;
    }

    /**
     * @return true if label is affected by a change of the given property
     */
    @Override
    public boolean isLabelProperty(final Object element, final String property) {
        return getColumnConfiguration(property) != null;
    }

    /**
     * checks if all column properties exist in the given class and have a write method if they are
     * editable.
     * 
     * @throws IllegalArgumentException if properties are not available
     */
    protected void validatePropertiesAndFillEnumComboValues() {
        final String message = "";
        for (int i = 0; i < m_columnConfigurations.length; i++) {
            final ColumnConfiguration conf = m_columnConfigurations[i];
            final ModelAdapter adapter = BasicModelAdapterFactory.getInstance().getAdapterForType((Class<?>) m_rowElementMetaClass);
            adapter.validatePropertyPath(m_rowElementMetaClass, conf.getProperty(), conf.getCellEditorType() != null);
            if (conf.getCellEditorType() == ECellEditorType.COMBO && conf.getComboItems() == null) {
            }
        }
        if (message.length() > 0) {
            throw new IllegalArgumentException("Properties of class " + m_rowElementMetaClass + " not matching column configuration:\n" + message);
        }
    }

    /**
     * overridden to render disabled rows in gray
     * 
     * @param element model
     * @param columnIndex index of the column to retrieve foreground for
     * @return color or null if no specific color is needed
     */
    @Override
    public Color getForeground(final Object element, final int columnIndex) {
        if (element != null && element instanceof ITableSelectable && !((ITableSelectable) element).getIsSelectable()) {
            return getDisabledRGBFGColor();
        }
        return null;
    }

    /**
     * See {@link ITableColorProvider#getBackground(Object, int)}
     * 
     * @param element
     * @param columnIndex
     * @return
     */
    @Override
    public Color getBackground(final Object element, final int columnIndex) {
        if (element != null && element instanceof ITableSelectable && !((ITableSelectable) element).getIsSelectable()) {
            return getDisabledRGBBGColor();
        }
        return null;
    }

    /**
     * TODO LATER: non-static way to determine color
     * 
     * @return color to use as bg for disabled rows
     */
    protected Color getDisabledRGBBGColor() {
        return DISABLED_RGB_BG_COLOR;
    }

    /**
     * TODO LATER: non-static way to determine color
     * 
     * @return color to use as foreground for disabled rows
     */
    protected Color getDisabledRGBFGColor() {
        return DISABLED_RGB_FG_COLOR;
    }

    @Override
    public ColumnConfiguration[] getColumnConfigurations() {
        return m_columnConfigurations;
    }

    @Override
    public Font getFont(final Object element) {
        return JFaceResources.getDialogFont();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
