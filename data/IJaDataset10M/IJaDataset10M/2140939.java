package org.mariella.sample.app.person;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.mariella.rcp.resources.VResourceChangeEvent;
import org.mariella.rcp.resources.VResourceChangeListener;
import org.mariella.rcp.resources.VResourceRef;
import org.mariella.rcp.resources.VResourcesPlugin;
import org.mariella.sample.core.Person;
import org.mariella.sample.core.SampleCorePlugin;

public class PersonsView extends ViewPart {

    public static final String ID = PersonsView.class.getName();

    /**
 * ViewEntry class that holds a Person object and acts as 
 * a ResourceRefHolder. 
 * 
 * @author maschmid
 *
 */
    class ViewEntry implements PersonResourceRefHolder {

        Person person;

        public ViewEntry(Person person) {
            this.person = person;
        }

        public VResourceRef getRef() {
            PersonResourceManager rm = VResourcesPlugin.getResourceManagerRegistry().getResourceManager(PersonResourceManager.class);
            return rm.getRefForPersistentId(person.getId());
        }
    }

    /**
 * Content provider for table viewer
 * 
 * @author maschmid
 *
 */
    class ViewContentProvider implements IStructuredContentProvider {

        @Override
        public Object[] getElements(Object inputElement) {
            return ((List<ViewEntry>) inputElement).toArray();
        }

        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /**
 * Label provider for table viewer
 * 
 * @author maschmid
 *
 */
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            ViewEntry entry = (ViewEntry) element;
            return entry.person.getLastName();
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

    TableViewer tableViewer;

    List<ViewEntry> currentViewEntries = null;

    VResourceChangeListener resourceChangeListener = new VResourceChangeListener() {

        public void resourceRemoved(VResourceChangeEvent event) {
            if (event.getResource() instanceof PersonResource && event.isPersistentChange()) refreshContent();
        }

        public void resourceLoaded(VResourceChangeEvent event) {
            if (event.getResource() instanceof PersonResource && event.isPersistentChange()) refreshContent();
        }

        public void resourceChanged(VResourceChangeEvent event) {
            if (event.getResource() instanceof PersonResource && event.isPersistentChange()) refreshContent();
        }
    };

    @Override
    public void createPartControl(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
        tableViewer.setContentProvider(new ViewContentProvider());
        tableViewer.setLabelProvider(new ViewLabelProvider());
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                OpenPersonEditorAction openAction = new OpenPersonEditorAction(getSite().getWorkbenchWindow());
                if (openAction.isEnabled()) openAction.run();
            }
        });
        getSite().setSelectionProvider(tableViewer);
        VResourcesPlugin.getResourcePool().addResourceChangeListener(resourceChangeListener);
        refreshContent();
    }

    @Override
    public void dispose() {
        removeReferers();
        VResourcesPlugin.getResourcePool().removeResourceChangeListener(resourceChangeListener);
    }

    void refreshContent() {
        PersonResourceManager rm = VResourcesPlugin.getResourceManagerRegistry().getResourceManager(PersonResourceManager.class);
        removeReferers();
        List<Person> persons = SampleCorePlugin.getCoreService().getAvailablePersons();
        currentViewEntries = new ArrayList<ViewEntry>(persons.size());
        for (Person person : persons) {
            ViewEntry entry = new ViewEntry(person);
            rm.addReferrer(entry.getRef(), entry);
            currentViewEntries.add(entry);
        }
        tableViewer.setInput(currentViewEntries);
    }

    void removeReferers() {
        if (currentViewEntries != null) {
            PersonResourceManager rm = VResourcesPlugin.getResourceManagerRegistry().getResourceManager(PersonResourceManager.class);
            for (ViewEntry entry : currentViewEntries) {
                rm.removeReferrer(entry.getRef(), entry);
            }
        }
    }

    @Override
    public void setFocus() {
        tableViewer.getControl().setFocus();
    }
}
