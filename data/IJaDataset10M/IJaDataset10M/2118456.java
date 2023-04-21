package org.openremote.modeler.client.widget;

import org.openremote.modeler.client.gxtextends.NestedJsonLoadResultReader;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.ScriptTagProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * The Class RemoteJsonComboBox.
 * @param <D> any ModelData
 */
public class RemoteJsonComboBox<D extends ModelData> extends ComboBox<D> {

    /** The remote json url. */
    private String remoteJsonURL = "";

    /** The model type. */
    private ModelType modelType = null;

    /**
    * Instantiates a new remote json combo box.
    * 
    * @param remoteJsonURL the remote json url
    * @param modelType the model type
    */
    public RemoteJsonComboBox(String remoteJsonURL, ModelType modelType) {
        super();
        this.remoteJsonURL = remoteJsonURL;
        this.modelType = modelType;
        setup();
    }

    /**
    * Reload list store with a different url.
    * 
    * @param url the url
    */
    public void reloadListStoreWithUrl(String url) {
        final RemoteJsonComboBox<D> box = this;
        ScriptTagProxy<ListLoadResult<D>> scriptTagProxy = new ScriptTagProxy<ListLoadResult<D>>(url);
        NestedJsonLoadResultReader<ListLoadResult<D>> reader = new NestedJsonLoadResultReader<ListLoadResult<D>>(modelType);
        final BaseListLoader<ListLoadResult<D>> loader = new BaseListLoader<ListLoadResult<D>>(scriptTagProxy, reader);
        ListStore<D> store = new ListStore<D>(loader);
        store.addListener(ListStore.DataChanged, new StoreListener<D>() {

            @Override
            public void storeDataChanged(StoreEvent<D> se) {
                box.getStore().add(se.getStore().getModels());
            }
        });
        loader.load();
    }

    /**
    * Setup.
    */
    private void setup() {
        ScriptTagProxy<ListLoadResult<D>> scriptTagProxy = new ScriptTagProxy<ListLoadResult<D>>(remoteJsonURL);
        NestedJsonLoadResultReader<ListLoadResult<D>> reader = new NestedJsonLoadResultReader<ListLoadResult<D>>(modelType);
        final BaseListLoader<ListLoadResult<D>> loader = new BaseListLoader<ListLoadResult<D>>(scriptTagProxy, reader);
        ListStore<D> store = new ListStore<D>(loader);
        setStore(store);
        setTypeAhead(true);
        setTriggerAction(TriggerAction.ALL);
        setMinChars(1);
        addListener(Events.BeforeQuery, new Listener<FieldEvent>() {

            public void handleEvent(FieldEvent be) {
                be.setCancelled(true);
                RemoteJsonComboBox<D> box = be.getComponent();
                if (box.getRawValue() != null && box.getRawValue().length() > 0) {
                    box.getStore().filter(getDisplayField(), box.getRawValue());
                } else {
                    box.getStore().clearFilters();
                }
                box.expand();
            }
        });
        loader.load();
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce) {
        final RemoteJsonComboBox<D> box = this;
        box.getStore().clearFilters();
        box.expand();
    }
}
