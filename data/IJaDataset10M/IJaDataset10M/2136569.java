package de.beas.explicanto.client.rcp.workspace.views;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.part.ViewPart;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datasource.types.WSAnnotation;
import de.bea.services.vidya.client.datasource.types.WSAnnotationAudience;
import de.bea.services.vidya.client.datastructures.CComponent;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.CLesson;
import de.bea.services.vidya.client.datastructures.CPage;
import de.bea.services.vidya.client.datastructures.CProject;
import de.bea.services.vidya.client.datastructures.CUnit;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.Resources;
import de.beas.explicanto.client.rcp.workspace.dialogs.AnnotationDialog;

/**
 * 
 * AnnotationsView
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class AnnotationsView extends ViewPart implements ISelectionChangedListener {

    public static final String ID = "de.beas.explicanto.client.rcp.workspace.views.AnnotationsView";

    public static final Logger log = Logger.getLogger(AnnotationsView.class);

    private TableViewer tableViewer;

    private ProjectsOutline outline;

    private ViewerFilter filter;

    public void createPartControl(Composite parent) {
        setPartName(I18N.translate("annotation.view.title"));
        IViewReference[] refs = getSite().getPage().getViewReferences();
        outline = null;
        for (int i = 0; i < refs.length; i++) {
            if (refs[i].getId().equals(ProjectsOutline.ID)) outline = (ProjectsOutline) refs[i].getPart(false);
        }
        outline.addNodeSelectionListener(this);
        tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
        tableViewer.setContentProvider(new AnnotationsTableCP());
        tableViewer.setLabelProvider(new AnnotationsViewLP());
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                treeTableDoubleClicked(event);
            }
        });
        Table table = (Table) tableViewer.getControl();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableColumn column = new TableColumn(table, SWT.LEFT, 0);
        column.setText(I18N.translate("annotation.view.column.content"));
        column.setWidth(350);
        column = new TableColumn(table, SWT.RIGHT, 1);
        column.setText(I18N.translate("annotation.view.column.author"));
        column.setWidth(70);
        column = new TableColumn(table, SWT.RIGHT, 2);
        column.setText(I18N.translate("annotation.view.column.audience"));
        column.setWidth(220);
        column = new TableColumn(table, SWT.RIGHT, 3);
        column.setText(I18N.translate("annotation.view.column.status"));
        column.setWidth(70);
        getSite().setSelectionProvider(tableViewer);
        if (outline == null) tableViewer.setInput(new String(" ")); else tableViewer.setInput(outline.getSelectedNode());
    }

    protected void treeTableDoubleClicked(DoubleClickEvent event) {
        IStructuredSelection sel = (IStructuredSelection) tableViewer.getSelection();
        WSAnnotation anno = (WSAnnotation) sel.getFirstElement();
        if (anno == null) return;
        AnnotationDialog nad = new AnnotationDialog(getSite().getShell(), anno);
        nad.open();
        if (nad.isOk()) {
            try {
                VidyaDataTree.getDefault().updateAnnotation(anno);
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFocus() {
    }

    public void refresh() {
        tableViewer.refresh();
    }

    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection iss = (IStructuredSelection) event.getSelection();
        TreeNode node = (TreeNode) iss.getFirstElement();
        tableViewer.setInput(node);
        tableViewer.setSelection(StructuredSelection.EMPTY);
    }

    public TreeNode getSelectedNode() {
        return (TreeNode) tableViewer.getInput();
    }

    public ProjectsOutline getProjectsOutline() {
        return outline;
    }

    public void setFilter(ViewerFilter filter) {
        removeFilter();
        this.filter = filter;
        tableViewer.addFilter(filter);
    }

    public void removeFilter() {
        if (filter != null) tableViewer.removeFilter(filter);
    }
}

class AnnotationsTableCP implements IStructuredContentProvider {

    public static final Logger log = Logger.getLogger(AnnotationsTableCP.class);

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        log.debug("inputChanged");
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof String) return new Object[0];
        log.debug("getElements " + inputElement);
        try {
            List annotations;
            if (inputElement instanceof CCustomer) {
                annotations = VidyaDataTree.getDefault().loadAnnotations();
            } else if (inputElement instanceof CProject) {
                CProject project = (CProject) inputElement;
                annotations = VidyaDataTree.getDefault().loadAnnotations(project.convertToWS());
            } else if (inputElement instanceof CLesson) {
                CLesson lesson = (CLesson) inputElement;
                annotations = VidyaDataTree.getDefault().loadAnnotations(lesson.convertToWS());
            } else if (inputElement instanceof CUnit) {
                CUnit unit = (CUnit) inputElement;
                annotations = VidyaDataTree.getDefault().loadAnnotations(unit.convertToWS());
            } else if (inputElement instanceof CCourse) {
                CCourse course = (CCourse) inputElement;
                annotations = VidyaDataTree.getDefault().loadAnnotations(course.convertToWS());
            } else if (inputElement instanceof CComponent) {
                CComponent comp = (CComponent) inputElement;
                annotations = VidyaDataTree.getDefault().loadAnnotationsForComponent((CPage) comp.getParent().getParent(), comp);
            } else {
                return new Object[0];
            }
            log.debug("received " + annotations.size() + " elements");
            return annotations.toArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0];
        }
    }
}

class AnnotationsViewLP extends LabelProvider implements ITableLabelProvider {

    private Image icon = Resources.getImage("rcp/resources/images/annotation.gif");

    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 0) return icon;
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof WSAnnotation) {
            WSAnnotation annotation = (WSAnnotation) element;
            switch(columnIndex) {
                case 0:
                    return annotation.getContent();
                case 1:
                    return annotation.getAuthor().getUsername();
                case 2:
                    StringBuffer buf = new StringBuffer();
                    Iterator it = annotation.getAudience().iterator();
                    while (it.hasNext()) {
                        WSAnnotationAudience aa = (WSAnnotationAudience) it.next();
                        buf.append(aa.getUser().getUsername()).append(" ");
                    }
                    return buf.toString();
                case 3:
                    return annotation.getStatus().getValue();
                default:
                    return "";
            }
        } else return element.getClass().getName();
    }
}
