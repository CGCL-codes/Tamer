package de.byteholder.geoclipse.map.preferences;

import java.util.List;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import de.byteholder.geoclipse.map.Activator;
import de.byteholder.geoclipse.map.tilefactories.ITileFactory;

/**
 * This preference page shows all available tile factories. It will allow the
 * user to enable and disable them in the future.
 * 
 * @author Wolfgang Schramm
 * @author Michael Kanis
 * @author $Author: damumbl $
 * @version $Rev: 493 $
 * @levd.rating RED Rev:
 */
public class PrefPageMapFactories extends PreferencePage implements IWorkbenchPreferencePage {

    /** The {@link TableViewer} that holds the available tile factories, */
    private TableViewer viewer;

    /** Simple content provider for the viewer. */
    private static class MapContentProvider implements IStructuredContentProvider {

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        public Object[] getElements(final Object inputElement) {
            return ((List<ITileFactory>) inputElement).toArray();
        }

        /** {@inheritDoc} */
        public void dispose() {
        }

        /** {@inheritDoc} */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /** Simple label provider for the viewer. */
    private static class TileFactoryLabelProvider extends BaseLabelProvider implements ILabelProvider {

        /** {@inheritDoc} */
        @Override
        public Image getImage(Object element) {
            return null;
        }

        /** {@inheritDoc} */
        @Override
        public String getText(Object element) {
            return ((ITileFactory) element).getName();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Control createContents(final Composite parent) {
        final Composite uiContainer = new Composite(parent, SWT.NONE);
        uiContainer.setLayout(new GridLayout());
        GridDataFactory.fillDefaults().grab(true, true).applyTo(uiContainer);
        Label factoriesLabel = new Label(uiContainer, SWT.NONE);
        factoriesLabel.setText(Messages.pref_map_lbl_avail_map_provider);
        viewer = new TableViewer(uiContainer);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getTable());
        viewer.setLabelProvider(new TileFactoryLabelProvider());
        viewer.setContentProvider(new MapContentProvider());
        List<ITileFactory> factories = Activator.getDefault().getTileFactories();
        viewer.setInput(factories);
        viewer.setSelection(new StructuredSelection(factories.get(0)));
        return uiContainer;
    }

    /** {@inheritDoc} */
    public void init(final IWorkbench workbench) {
    }
}
