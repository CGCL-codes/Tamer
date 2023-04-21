package org.qtitools.mathqurate.view;

import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.qtitools.mathqurate.controller.DefaultController;
import org.qtitools.mathqurate.model.AttribValue;
import org.qtitools.mathqurate.model.MQMetadata;
import org.qtitools.mathqurate.model.MQModel;
import org.qtitools.mathqurate.utilities.PrefsHelper;

/**
 * The Class MetadataView. The Mathqurate view of the assessment item attributes and related meta-data
 * 
 * @author James Annesley <j.annesley@kingston.ac.uk>
 */
public class MetadataView extends AbstractApplicationWindow {

    /** The taxon pull-down box. */
    private Combo tax;

    /** The autocomplete field for the taxon */
    private Text taxonSearch;

    /** The meta-data helper. */
    private MQMetadata metadata;

    boolean listeners = true;

    /**
	 * The Class AbstractEditingSupport.
	 */
    protected abstract class AbstractEditingSupport extends EditingSupport {

        /** The editor. */
        private CellEditor editor;

        /**
		 * Instantiates a new abstract editing support.
		 * 
		 * @param viewer the viewer
		 */
        public AbstractEditingSupport(TableViewer viewer) {
            super(viewer);
            this.editor = new TextCellEditor(viewer.getTable());
        }

        /**
		 * Instantiates a new abstract editing support.
		 * 
		 * @param viewer the viewer
		 * @param editor the editor
		 */
        public AbstractEditingSupport(TableViewer viewer, CellEditor editor) {
            super(viewer);
            this.editor = editor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        /**
		 * Do set value.
		 * 
		 * @param element the element
		 * @param value the value
		 */
        protected abstract void doSetValue(Object element, Object value);

        @Override
        protected CellEditor getCellEditor(Object element) {
            return editor;
        }

        @Override
        protected void setValue(Object element, Object value) {
            doSetValue(element, value);
            getViewer().update(element, null);
        }
    }

    /**
	 * The Class AssessmentItemAttributesContentProvider.
	 */
    class AssessmentItemAttributesContentProvider implements IStructuredContentProvider {

        public void dispose() {
        }

        @SuppressWarnings("unchecked")
        public Object[] getElements(Object arg0) {
            ArrayList<AttribValue> array = (ArrayList<AttribValue>) arg0;
            return array.toArray();
        }

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        }
    }

    /** The metadataarray. */
    private ArrayList<AttribValue> metadataarray;

    private TableViewer tableViewer;

    /**
	 * Instantiates a new metadata view.
	 * 
	 * @param parentShell the parent shell
	 */
    public MetadataView(Shell parentShell) {
        super(parentShell);
        metadataarray = new ArrayList<AttribValue>();
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Meta-data");
    }

    @Override
    protected Control createContents(Composite parent) {
        createTableViewer(parent);
        Composite taxcombo = new Composite(parent, SWT.NONE);
        {
            Label taxLabel = new Label(taxcombo, SWT.NONE);
            taxLabel.setText(PrefsHelper.getTaxonName());
            tax = new Combo(taxcombo, SWT.FILL | SWT.DROP_DOWN | SWT.READ_ONLY);
            for (String s : MQModel.getTaxonArrayList()) {
                tax.add(s.trim());
            }
            tax.select(0);
            tax.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    if (listeners) {
                        Combo c = (Combo) e.widget;
                        int index = c.getSelectionIndex();
                        metadata.setTaxon(c.getItem(index));
                        listeners = false;
                        taxonSearch.setText("");
                        listeners = true;
                    }
                }
            });
            new Label(taxcombo, SWT.NONE).setText("");
            taxonSearch = new Text(taxcombo, SWT.NONE);
            taxonSearch.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    if ((listeners) && (taxonSearch.getText().length() > 4)) {
                        tax.setEnabled(false);
                        listeners = false;
                        String value = ((Text) e.widget).getText().replaceAll("\\s", "");
                        int i = 0;
                        for (String s : MQModel.getTaxonArrayList()) {
                            String sx = s.replaceAll("\\s", "");
                            if (sx.toUpperCase().contains(value.toUpperCase())) {
                                tax.setEnabled(true);
                                tax.select(i);
                                break;
                            }
                            i++;
                        }
                        listeners = true;
                    }
                    if (taxonSearch.getText().length() < 5) {
                        tax.setEnabled(true);
                    }
                }
            });
            GridLayoutFactory.fillDefaults().numColumns(2).generateLayout(taxcombo);
        }
        Composite buttonBar = new Composite(parent, SWT.NONE);
        {
            Button buttonApply = new Button(buttonBar, SWT.NONE);
            buttonApply.setText("Apply");
            buttonApply.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent arg0) {
                }

                public void mouseDown(MouseEvent arg0) {
                    if (System.getProperty("os.name").contains("Mac")) {
                        tableViewer.editElement(tableViewer.getElementAt(0), 1);
                    }
                    String taxonValueText = taxonSearch.getText();
                    boolean found = false;
                    if (taxonValueText.trim().equals("")) {
                        metadata.setTaxon(tax.getText().trim());
                        found = true;
                    } else {
                        for (String s : MQModel.getTaxonArrayList()) {
                            String sx = s.replaceAll("\\s", "").toUpperCase();
                            String tx = taxonValueText.replaceAll("\\s", "").toUpperCase();
                            if (sx.contains(tx)) {
                                metadata.setTaxon(s.trim());
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        metadata.setTaxon(taxonValueText.trim());
                    }
                    controller.setMQMetadata(metadata);
                }

                public void mouseUp(MouseEvent arg0) {
                }
            });
            Button buttonOK = new Button(buttonBar, SWT.NONE);
            buttonOK.setText("OK");
            buttonOK.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent arg0) {
                }

                public void mouseDown(MouseEvent arg0) {
                    if (System.getProperty("os.name").contains("Mac")) {
                        tableViewer.editElement(tableViewer.getElementAt(0), 1);
                    }
                    String taxonValueText = taxonSearch.getText();
                    boolean found = false;
                    if (taxonValueText.trim().equals("")) {
                        metadata.setTaxon(tax.getText().trim());
                        found = true;
                    } else {
                        for (String s : MQModel.getTaxonArrayList()) {
                            String sx = s.replaceAll("\\s", "").toUpperCase();
                            String tx = taxonValueText.replaceAll("\\s", "").toUpperCase();
                            if (sx.contains(tx)) {
                                metadata.setTaxon(s.trim());
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        metadata.setTaxon(taxonValueText.trim());
                    }
                    controller.setMQMetadata(metadata);
                }

                public void mouseUp(MouseEvent arg0) {
                    close();
                }
            });
            Button buttonCancel = new Button(buttonBar, SWT.NONE);
            buttonCancel.setText("Cancel");
            buttonCancel.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent arg0) {
                }

                public void mouseDown(MouseEvent arg0) {
                }

                public void mouseUp(MouseEvent arg0) {
                    close();
                }
            });
            GridLayoutFactory.fillDefaults().numColumns(3).generateLayout(buttonBar);
        }
        GridDataFactory.fillDefaults().span(3, 1).align(SWT.RIGHT, SWT.BOTTOM).applyTo(buttonBar);
        GridLayoutFactory.fillDefaults().numColumns(1).margins(LayoutConstants.getMargins()).generateLayout(parent);
        return (parent);
    }

    /**
	 * Creates the table viewer.
	 */
    private void createTableViewer(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
        tableViewer.setContentProvider(new AssessmentItemAttributesContentProvider());
        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.FILL);
        column.getColumn().setWidth(250);
        column.getColumn().setText("Attribute");
        column.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                AttribValue attribValue = (AttribValue) element;
                return attribValue.getAttrib();
            }
        });
        column = new TableViewerColumn(tableViewer, SWT.FILL);
        column.getColumn().setWidth(550);
        column.getColumn().setText("Value");
        column.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                AttribValue attribValue = (AttribValue) element;
                return attribValue.getValue();
            }
        });
        column.setEditingSupport(new AbstractEditingSupport(tableViewer) {

            @Override
            protected void doSetValue(Object element, Object value) {
                ((AttribValue) element).setValue(value.toString());
                getViewer().update(element, null);
            }

            @Override
            protected Object getValue(Object element) {
                return ((AttribValue) element).getValue() + "";
            }
        });
        tableViewer.getTable().setLinesVisible(true);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DefaultController.SET_ASSESSMENTITEM_PROPERTY)) {
            controller.getMQMetadata();
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQMETADATA_PROPERTY)) {
            metadata = (MQMetadata) evt.getNewValue();
            metadataarray = metadata.getMetadataArray();
            String taxon = metadata.getTaxon();
            String[] taxa = tax.getItems();
            int taxalen = taxa.length;
            int count = 0;
            boolean found = false;
            if (taxon != null) {
                while (count < taxalen) {
                    String possibleTax = taxa[count].replaceAll("\\s", "").toUpperCase();
                    String ourTax = taxon.replaceAll("\\s", "").toUpperCase();
                    ;
                    if (possibleTax.equals(ourTax)) {
                        found = true;
                        break;
                    } else {
                        count++;
                    }
                }
                if (found) {
                    tax.select(count);
                }
            }
            tableViewer.setInput(metadataarray);
            tableViewer.refresh();
        }
    }

    @Override
    public boolean close() {
        getShell().setVisible(false);
        return false;
    }
}
