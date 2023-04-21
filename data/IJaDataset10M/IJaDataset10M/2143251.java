package org.plazmaforge.studio.core.dialogs;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class TableElementController implements ITableElementController {

    private ITableElementHolder holder;

    private List<ITableElementProvider> tableElementProviders;

    public TableElementController(ITableElementHolder holder) {
        super();
        this.holder = holder;
    }

    protected List<ITableElementProvider> doGetTableElementProviders() {
        if (tableElementProviders == null) {
            tableElementProviders = new ArrayList<ITableElementProvider>();
        }
        return tableElementProviders;
    }

    public void setTableElementProvider(ITableElementProvider provider) {
        List<ITableElementProvider> providers = doGetTableElementProviders();
        if (providers.isEmpty()) {
            providers.add(provider);
        } else {
            providers.set(0, provider);
        }
    }

    public ITableElementProvider getTableElementProvider() {
        List<ITableElementProvider> providers = doGetTableElementProviders();
        return (providers == null || providers.isEmpty()) ? null : providers.get(0);
    }

    public void updateModel() {
        if (holder == null) {
            return;
        }
        holder.updateModel();
    }

    public Shell getShell() {
        if (holder == null) {
            return null;
        }
        return holder.getShell();
    }

    public Composite createContents(Composite parent) {
        return createTablePanel(parent);
    }

    public Composite createTablePanel(Composite parent) {
        return createTableByProvider(parent, getTableElementProvider());
    }

    protected Composite createTableByProvider(Composite parent, ITableElementProvider provider) {
        return provider == null ? null : provider.createTablePanel(parent);
    }
}
