package beantable;

import java.beans.*;
import java.io.Serializable;
import java.util.*;
import javax.swing.JTable;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.TableFormat;
import ellington.LenientComparator;

public class CompositeTableFormat implements Serializable, WritableTableFormat, AdvancedTableFormat, TableCellPropertyDescriptorProvider, PropertyEditorProvider {

    private static final LenientComparator lenientComparator = new LenientComparator();

    private final TableFormat[] formats;

    public CompositeTableFormat(final TableFormat format1) throws IntrospectionException {
        this(new TableFormat[] { format1 });
    }

    public CompositeTableFormat(final TableFormat format1, final TableFormat format2) throws IntrospectionException {
        this(new TableFormat[] { format1, format2 });
    }

    public CompositeTableFormat(final TableFormat[] formats) throws IntrospectionException {
        super();
        if (formats == null) {
            this.formats = new TableFormat[0];
        } else {
            this.formats = (TableFormat[]) formats.clone();
        }
        assert this.formats != null;
    }

    public boolean isEditable(final Object bean, final int column) {
        boolean returnValue = false;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof WritableTableFormat) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((WritableTableFormat) format).isEditable(bean, adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public Class getColumnClass(final int column) {
        Class returnValue = Object.class;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof AdvancedTableFormat) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((AdvancedTableFormat) format).getColumnClass(adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public Comparator getColumnComparator(final int column) {
        Comparator returnValue = lenientComparator;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof AdvancedTableFormat) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((AdvancedTableFormat) format).getColumnComparator(adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public int getColumnCount() {
        int returnValue = 0;
        for (int i = 0; i < this.formats.length; i++) {
            final TableFormat format = this.formats[i];
            if (format != null) {
                returnValue += Math.max(0, format.getColumnCount());
            }
        }
        return returnValue;
    }

    public String getColumnName(final int column) {
        String returnValue = null;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    final int adjustedColumnIndex = column - columnOffset;
                    if (adjustedColumnIndex < formatColumnCount) {
                        returnValue = format.getColumnName(adjustedColumnIndex);
                        break;
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public Object getColumnValue(final Object baseObject, final int column) {
        Object returnValue = baseObject;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    final int adjustedColumnIndex = column - columnOffset;
                    if (adjustedColumnIndex < formatColumnCount) {
                        returnValue = format.getColumnValue(baseObject, adjustedColumnIndex);
                        break;
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public Object setColumnValue(final Object baseObject, final Object newValue, final int column) {
        Object returnValue = null;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof WritableTableFormat) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((WritableTableFormat) format).setColumnValue(baseObject, newValue, adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public final FeatureDescriptor getFeatureDescriptor(final JTable table, final int row, final int column) {
        FeatureDescriptor returnValue = null;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof TableCellFeatureDescriptorProvider) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((TableCellFeatureDescriptorProvider) format).getFeatureDescriptor(table, row, adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public final PropertyDescriptor getPropertyDescriptor(final JTable table, final int row, final int column) {
        PropertyDescriptor returnValue = null;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof TableCellPropertyDescriptorProvider) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((TableCellPropertyDescriptorProvider) format).getPropertyDescriptor(table, row, adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }

    public final PropertyEditor getPropertyEditor(final PropertyDescriptor pd) {
        if (pd != null) {
            Class editorClass = pd.getPropertyEditorClass();
            if (editorClass == null) {
                editorClass = pd.getPropertyType();
                if (editorClass != null) {
                    return PropertyEditorManager.findEditor(editorClass);
                }
            } else {
                Object newInstance = null;
                try {
                    newInstance = editorClass.newInstance();
                } catch (final IllegalAccessException kaboom) {
                    newInstance = null;
                } catch (final InstantiationException kaboom) {
                    newInstance = null;
                }
                if (newInstance instanceof PropertyEditor) {
                    return (PropertyEditor) newInstance;
                }
            }
        }
        return null;
    }

    public final PropertyEditor getPropertyEditor(final int row, final int column) {
        PropertyEditor returnValue = null;
        if (column >= 0) {
            int columnOffset = 0;
            for (int i = 0; i < this.formats.length; i++) {
                final TableFormat format = this.formats[i];
                if (format != null) {
                    final int formatColumnCount = format.getColumnCount();
                    if (format instanceof PropertyEditorProvider) {
                        final int adjustedColumnIndex = column - columnOffset;
                        if (adjustedColumnIndex < formatColumnCount) {
                            returnValue = ((PropertyEditorProvider) format).getPropertyEditor(row, adjustedColumnIndex);
                            break;
                        }
                    }
                    columnOffset += formatColumnCount;
                }
            }
        }
        return returnValue;
    }
}
