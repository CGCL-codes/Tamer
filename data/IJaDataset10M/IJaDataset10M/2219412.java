package org.qcmylyn.ui.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.ITask.PriorityLevel;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.qcmylyn.core.attribute.priority.MappedPriority;
import org.qcmylyn.core.attribute.priority.MylynUtils;

/**
 * A table to view and edit {@link MappedPriority} in a list.
 *
 * @author Andreas H�hmann
 * @since 0.2.8
 */
public class MappedPriorityTableViewer {

    private static final String COLUMN_PROPERTY_PRIORITYLEVEL = "priority_level";

    private static final String COLUMN_PROPERTY_PROJECTPRIORITY = "priority";

    private static final String[] TABLE_COLUMN_PROPERTIES = new String[] { COLUMN_PROPERTY_PROJECTPRIORITY, COLUMN_PROPERTY_PRIORITYLEVEL };

    private final List<MappedPriority> mappedPriorites = new ArrayList<MappedPriority>();

    private TableViewer tableViewer;

    /**
     * Create the TableViewer.
     *
     * @param parent The parent.
     * @return The composite created.
     */
    public Composite createTableViewer(final Composite parent) {
        final Composite lComp = new Composite(parent, SWT.NONE);
        this.tableViewer = new TableViewer(lComp, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        this.tableViewer.getTable().setLinesVisible(true);
        this.tableViewer.getTable().setHeaderVisible(true);
        this.tableViewer.setUseHashlookup(true);
        this.tableViewer.setColumnProperties(TABLE_COLUMN_PROPERTIES);
        final TableColumnLayout lLayout = new TableColumnLayout();
        lComp.setLayout(lLayout);
        final TableColumn lProjectCol = new TableColumn(this.tableViewer.getTable(), SWT.LEFT, 0);
        lProjectCol.setText("Project Priority");
        lLayout.setColumnData(lProjectCol, new ColumnWeightData(50));
        final TableColumn lPrioCol = new TableColumn(this.tableViewer.getTable(), SWT.LEFT, 1);
        lPrioCol.setText("Mylyn Priority");
        lLayout.setColumnData(lPrioCol, new ColumnWeightData(50));
        final CellEditor[] editors = new CellEditor[TABLE_COLUMN_PROPERTIES.length];
        editors[0] = null;
        final ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(this.tableViewer.getTable(), SWT.READ_ONLY);
        cellEditor.setLabelProvider(new PriorityLevelLabelProvider());
        cellEditor.setContenProvider(new ArrayContentProvider());
        cellEditor.setInput(MappedPriority.getPriorityLevels());
        cellEditor.setActivationStyle(ComboBoxViewerCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION | ComboBoxViewerCellEditor.DROP_DOWN_ON_TRAVERSE_ACTIVATION);
        editors[1] = cellEditor;
        this.tableViewer.setCellEditors(editors);
        this.tableViewer.setCellModifier(new MappedPriorityTableCellModifier(this.tableViewer));
        this.tableViewer.setContentProvider(new ArrayContentProvider());
        this.tableViewer.setLabelProvider(new MappedPriorityTableLabelProvider());
        return lComp;
    }

    public void loadProjectPriorities(final List<MappedPriority> pPriorityList) {
        setInput(pPriorityList);
    }

    /**
     * Set the input for the table viewer from the given {@link TaskRepository}.
     *
     * @param pPriorityList must not be <code>null</code>.
     * @param pRepository to use for load the available qc-priorities and the priority-mapping, if <code>null</code> do nothing
     */
    public void loadProjectPriorities(final String[] pPriorityList, final TaskRepository pRepository) {
        setInput(MylynUtils.readPriorities(pPriorityList, pRepository));
    }

    /**
     * Persist the list of {@link MappedPriority} to the properties of the given {@link TaskRepository}.
     * <p>
     * Save {@link PriorityLevel} for each {@link MappedPriority} to the given {@link TaskRepository}.
     * </p>
     *
     * @param pRepository to use for store the priority-mapping, if <code>null</code> do nothing
     */
    public void saveProjectPriorities(final TaskRepository pRepository) {
        if (pRepository != null) {
            synchronized (this.mappedPriorites) {
                MylynUtils.storePriorities(pRepository, this.mappedPriorites);
            }
        }
    }

    /**
     * Thread safe. Clear internal list of {@link MappedPriority}, add all entries of the given list and refresh the internal
     * {@link TableViewer}.
     *
     * @param theMappedPriorityList to set
     */
    private void setInput(final List<MappedPriority> theMappedPriorityList) {
        synchronized (this.mappedPriorites) {
            this.mappedPriorites.clear();
            this.mappedPriorites.addAll(theMappedPriorityList);
        }
        this.tableViewer.setInput(this.mappedPriorites);
    }

    private class MappedPriorityTableCellModifier implements ICellModifier {

        private final TableViewer viewer;

        /**
         * Constructor.
         *
         * @param tableViewer The table viewer
         */
        public MappedPriorityTableCellModifier(final TableViewer tableViewer) {
            this.viewer = tableViewer;
        }

        /**
         * Overrides canModify. {@inheritDoc}
         *
         * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
         */
        public boolean canModify(final Object element, final String property) {
            return COLUMN_PROPERTY_PRIORITYLEVEL.equals(property);
        }

        /**
         * Overrides getValue. {@inheritDoc}
         *
         * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
         */
        public Object getValue(final Object element, final String property) {
            MappedPriority mappedPriority = null;
            if (element instanceof TableItem) {
                final TableItem ti = (TableItem) element;
                mappedPriority = (MappedPriority) ti.getData();
            } else if (element instanceof MappedPriority) {
                mappedPriority = (MappedPriority) element;
            }
            Object result = null;
            if (mappedPriority != null) {
                if (COLUMN_PROPERTY_PROJECTPRIORITY.equals(property)) {
                    result = mappedPriority.getProjectPriority();
                } else if (COLUMN_PROPERTY_PRIORITYLEVEL.equals(property)) {
                    final PriorityLevel priorityLevel = mappedPriority.getPriorityLevel();
                    if (priorityLevel != null) {
                        final int index = MappedPriority.getPriorityLevels().indexOf(mappedPriority.getPriorityLevel());
                        if (index != -1) {
                            result = index;
                        }
                    }
                }
            }
            return result;
        }

        /**
         * Overrides modify. {@inheritDoc}
         *
         * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
         */
        public void modify(final Object element, final String property, final Object value) {
            MappedPriority mappedPriority = null;
            if (element instanceof TableItem) {
                final TableItem ti = (TableItem) element;
                mappedPriority = (MappedPriority) ti.getData();
            }
            if (element instanceof MappedPriority) {
                mappedPriority = (MappedPriority) element;
            }
            if (mappedPriority != null) {
                if (COLUMN_PROPERTY_PRIORITYLEVEL.equals(property)) {
                    if (value != null) {
                        mappedPriority.setPriorityLevel((PriorityLevel) value);
                        this.viewer.refresh();
                    }
                }
            }
        }
    }

    private class MappedPriorityTableLabelProvider extends PriorityLevelLabelProvider implements ITableLabelProvider {

        /**
         * {@inheritDoc}
         */
        public Image getColumnImage(final Object pElement, final int pColumnIndex) {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        public String getColumnText(final Object element, final int columnIndex) {
            String result = "";
            final MappedPriority mappedPriority = (MappedPriority) element;
            switch(columnIndex) {
                case 0:
                    result = mappedPriority.getProjectPriority();
                    break;
                case 1:
                    final PriorityLevel priorityLevel = mappedPriority.getPriorityLevel();
                    result = getText(priorityLevel);
                    break;
                default:
                    break;
            }
            return result;
        }
    }

    /**
     * Provide labels for the combo of {@link PriorityLevelLabelProvider}'s.
     *
     * @author andreas
     * @since 0.2.8
     */
    private class PriorityLevelLabelProvider extends LabelProvider {

        @Override
        public Image getImage(final Object pElement) {
            if (pElement instanceof PriorityLevel) {
                return TasksUiImages.getImageForPriority((PriorityLevel) pElement);
            } else {
                return null;
            }
        }

        @Override
        public String getText(final Object pElement) {
            if (pElement instanceof PriorityLevel) {
                final PriorityLevel priorityLevel = (PriorityLevel) pElement;
                return String.format("%s (%s)", priorityLevel.toString(), priorityLevel.getDescription());
            } else {
                return null;
            }
        }
    }
}
