package de.laeubisoft.vaadin.webconsole.bundleview;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.service.useradmin.Authorization;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import de.laeubisoft.vaadin.webconsole.WebConsoleConstants;
import de.laeubisoft.vaadin.webconsole.WebConsoleTab;

/**
 * @author Christoph Läubrich
 */
public class BundleViewTab implements WebConsoleTab {

    private BundleContext bundleContext;

    private Table table;

    @Override
    public String getName() {
        return "Bundle View";
    }

    @Override
    public Component getView(final Application application, Authorization authorization) {
        final boolean isAdmin = authorization.hasRole(WebConsoleConstants.ROLE_CONSOLE_ADMIN);
        table = new Table();
        table.addContainerProperty("Bundle Symbolic Name", String.class, new ThemeResource("icons/bundle.png"));
        table.addContainerProperty("Version", String.class, null);
        table.addContainerProperty("State", String.class, null);
        table.addContainerProperty("Active", isAdmin ? CheckBox.class : String.class, null);
        table.setSortContainerPropertyId("Bundle Symbolic Name");
        table.setSizeFull();
        table.setSortAscending(true);
        table.setImmediate(true);
        refreshTable(isAdmin, application);
        Button refreshButton = new Button("Refresh Table");
        refreshButton.addListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                refreshTable(isAdmin, application);
            }
        });
        bundleContext.addBundleListener(new BundleListener() {

            @Override
            public void bundleChanged(BundleEvent event) {
                refreshTable(isAdmin, application);
            }
        });
        GridLayout grid = new GridLayout(1, 2);
        grid.setSizeFull();
        grid.addComponent(table, 0, 0);
        grid.addComponent(refreshButton, 0, 1);
        grid.setRowExpandRatio(0, 1f);
        grid.setRowExpandRatio(1, 0f);
        return grid;
    }

    private void refreshTable(final boolean isAdmin, final Application application) {
        Bundle[] bundles = bundleContext.getBundles();
        table.removeAllItems();
        int i = 1;
        for (Bundle bundle : bundles) {
            final Bundle selectedBundle = bundle;
            final CheckBox checkBox = new CheckBox();
            checkBox.setImmediate(true);
            checkBox.setValue(bundle.getState() == Bundle.ACTIVE);
            checkBox.addListener(new ValueChangeListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                    if (selectedBundle.getState() == Bundle.ACTIVE) {
                        QuestionCallback callback = new QuestionCallback() {

                            @Override
                            public void handleResult(boolean resultIsYes) {
                                if (resultIsYes) {
                                    try {
                                        selectedBundle.stop();
                                        refreshTable(isAdmin, application);
                                    } catch (final BundleException e1) {
                                        throw new RuntimeException("can't stop bundle", e1);
                                    }
                                }
                            }
                        };
                        application.getMainWindow().addWindow(new QuestionDialog("Confirm bundle deaktivation", "Are you sure you want to stop this bundle?", callback));
                    } else if (selectedBundle.getState() == Bundle.RESOLVED) {
                        try {
                            selectedBundle.start();
                            refreshTable(isAdmin, application);
                        } catch (final BundleException e1) {
                            throw new RuntimeException("can't activate bundle", e1);
                        }
                    }
                }
            });
            table.addItem(new Object[] { bundle.getSymbolicName(), bundle.getVersion(), getStateString(bundle), checkBox }, i++);
        }
        table.sort();
    }

    String getStateString(Bundle bundle) {
        switch(bundle.getState()) {
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public boolean isVisible(Authorization authorization) {
        return authorization.hasRole(WebConsoleConstants.ROLE_CONSOLE_ADMIN) || authorization.hasRole(WebConsoleConstants.ROLE_CONSOLE_USER);
    }

    public void start(BundleContext context) {
        this.bundleContext = context;
    }
}
