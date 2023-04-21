package de.morknet.mrw.rcc;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import de.morknet.mrw.Route;
import de.morknet.mrw.base.Abschnitt;
import de.morknet.mrw.util.LogUtil;

/**
 * Diese Klasse repr�sentiert eine View, in der die aktiven Fahrstra�en und die ausgew�hlten Gleisabschnitte
 * angezeigt werden.
 * @author sm
 *
 */
public class InfoView extends RccViewPart implements InfoViewId, SelectionListener, ISelectionChangedListener {

    private final Controller controller = Controller.getController();

    private static final Log log = LogFactory.getLog(InfoView.class);

    private TableViewer viewer;

    private Button remove_button;

    private Button remove_all_button;

    /**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
    class ViewContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            List<Abschnitt> auswahl = controller.getSegmentSelection();
            List<Route> routen = Route.getRoutes();
            Object[] items = new Object[auswahl.size() + routen.size()];
            int i = 0;
            for (Abschnitt a : auswahl) {
                items[i++] = a;
            }
            for (Route r : routen) {
                items[i++] = r;
            }
            return items;
        }
    }

    static class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            String result;
            if (obj instanceof Route) {
                Route route = (Route) obj;
                result = route.toString();
            } else if (obj instanceof Abschnitt) {
                Abschnitt a = (Abschnitt) obj;
                result = a.getName();
            } else {
                result = getText(obj);
            }
            log.debug(result);
            return result;
        }

        public Image getColumnImage(Object obj, int index) {
            return getImage(obj);
        }

        public Image getImage(Object obj) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
        }
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        FormLayout layout = new FormLayout();
        parent.setLayout(layout);
        FormData data1 = new FormData();
        data1.left = new FormAttachment(0, 0);
        data1.right = new FormAttachment(100, 0);
        data1.top = new FormAttachment(0, 0);
        data1.bottom = new FormAttachment(100, -50);
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setInput(getViewSite());
        viewer.getControl().setLayoutData(data1);
        viewer.addSelectionChangedListener(this);
        FormData data2 = new FormData();
        data2.left = new FormAttachment(0, 0);
        data2.right = new FormAttachment(50, 0);
        data2.top = new FormAttachment(100, -50);
        data2.bottom = new FormAttachment(100, 0);
        remove_button = new Button(parent, SWT.PUSH);
        remove_button.setText("L�schen");
        remove_button.addSelectionListener(this);
        remove_button.setLayoutData(data2);
        FormData data3 = new FormData();
        data3.left = new FormAttachment(50, 0);
        data3.right = new FormAttachment(100, 0);
        data3.top = new FormAttachment(100, -50);
        data3.bottom = new FormAttachment(100, 0);
        remove_all_button = new Button(parent, SWT.PUSH);
        remove_all_button.setText("Alle l�schen");
        remove_all_button.addSelectionListener(this);
        remove_all_button.setLayoutData(data3);
        controller.register(this);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    /**
	 * Diese Methode aktualisiert diese Ansicht.
	 */
    public void update() {
        Display display = PlatformUI.getWorkbench().getDisplay();
        if (!display.isDisposed()) {
            display.asyncExec(new Runnable() {

                public void run() {
                    if (!viewer.getControl().isDisposed()) {
                        viewer.refresh();
                        remove_button.setEnabled(Route.hasRoutes());
                        remove_all_button.setEnabled(Route.hasRoutes());
                    }
                }
            });
        }
    }

    /**
	 * Diese Methode gibt die in der InfoView ausgew�hlte Fahrstra�e zur�ck.
	 * @return Die ausgew�hlte Fahrstra�e.
	 */
    public Route getSelectedRoute() {
        StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        for (Object o : selection.toList()) {
            if (o instanceof Route) {
                return (Route) o;
            }
        }
        return null;
    }

    /**
	 * Diese Methode w�hlt ein Element der ListBox aus.
	 * @param object Das auszuw�hlende Objekt. Es kann ein Gleisabschnitt oder eine Fahrstra�e sein.
	 * @see Abschnitt
	 * @see Route
	 */
    public void select(final Object object) {
        log.debug(LogUtil.printf(">select(%s)", object));
        Display display = PlatformUI.getWorkbench().getDisplay();
        if (!display.isDisposed()) {
            display.syncExec(new Runnable() {

                public void run() {
                    if (!viewer.getControl().isDisposed()) {
                        Table table = viewer.getTable();
                        for (int i = 0; i < table.getItemCount(); i++) {
                            if (table.getItem(i).getData() == object) {
                                table.select(i);
                            } else {
                                table.deselect(i);
                            }
                        }
                        setFocus();
                    }
                }
            });
        }
        log.debug(LogUtil.printf("<select(%s)", object));
    }

    private void removeSelection(SelectionEvent event) {
        if (event.getSource() == remove_all_button) {
            setMessage("Alle Fahrstra�en werden aufgel�st...");
            controller.removeAllRoutes();
            setMessage("Alle Fahrstra�en wurden aufgel�st...");
        } else {
            final Route route = getSelectedRoute();
            if (route != null) {
                setMessage("Fahrstra�e wird aufgel�st...");
                controller.deactivateAction(route);
                controller.removeRoute(route);
                setMessage("Fahrstra�e ist aufgel�st...");
            }
        }
        update();
    }

    public void widgetDefaultSelected(SelectionEvent e) {
        removeSelection(e);
    }

    public void widgetSelected(SelectionEvent e) {
        removeSelection(e);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        controller.updateButtons();
    }

    @Override
    public String getId() {
        return ID;
    }
}
