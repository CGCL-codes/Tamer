package cn.csust.net2.manager.client.ux;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import cn.csust.net2.manager.client.module.ByGrade;
import cn.csust.net2.manager.shared.po.PO;
import cn.csust.net2.manager.shared.service.BaseServiceAsync;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ExpandGrid<T extends PO> extends Grid<BeanModel> implements Listener<GridEvent<BeanModel>> {

    public static final int PAGE_SIZE = 30;

    public static final int START_SIZE = 0;

    private BaseServiceAsync service;

    private RpcProxy<PagingLoadResult<T>> proxy;

    private BasePagingLoader<PagingLoadResult<ModelData>> loader = null;

    private PagingToolBar toolBar;

    private int currentRow;

    private PO po;

    public ExpandGrid(final PO po, BaseServiceAsync service) {
        this.setService(service);
        this.po = po;
        initProxy();
        initLoader();
        initStore();
        initCM(po);
        initGrid();
    }

    public void load(Map<String, Object> config) {
        ModelData configs = (ModelData) loader.getLastConfig();
        Set<Entry<String, Object>> set = config.entrySet();
        for (Entry<String, Object> entry : set) {
            configs.set(entry.getKey(), entry.getValue());
        }
        loader.load(configs);
    }

    public boolean hasConfig(String key, Object value) {
        ModelData configs = (ModelData) loader.getLastConfig();
        Object result = configs.get(key);
        if (result != null && result.equals(value)) return true; else return false;
    }

    public void clearConifgs() {
        ModelData configs = (ModelData) loader.getLastConfig();
        configs.remove(ByGrade.GRID_FILTER);
        configs.remove(ByGrade.QUERY);
        configs.remove(ExpandTree.TREE_NODE);
        loader.load(configs);
    }

    private void initProxy() {
        proxy = new RpcProxy<PagingLoadResult<T>>() {

            @Override
            public void load(Object loadConfig, AsyncCallback<PagingLoadResult<T>> callback) {
                ExpandGrid.this.getService().findAll(po.getAssist().getClassName(), (PagingLoadConfig) loadConfig, callback);
            }
        };
    }

    private void initLoader() {
        BeanModelReader beanModelReader = new BeanModelReader();
        beanModelReader.setFactoryForEachBean(true);
        this.loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy, beanModelReader);
        this.loader.setRemoteSort(true);
    }

    private void initStore() {
        this.store = new ListStore<BeanModel>(loader);
    }

    private void initCM(PO po) {
        this.cm = ColumnFactory.createColumn(po, getService());
    }

    private void initGrid() {
        this.toolBar = new PagingToolBar(PAGE_SIZE);
        this.toolBar.bind(this.loader);
        this.view = new GridView();
        this.view.setForceFit(true);
        disabledStyle = null;
        baseStyle = "x-grid-panel";
        setSelectionModel(new GridSelectionModel<BeanModel>());
        disableTextSelection(true);
        this.setLoadMask(true);
        this.setBorders(true);
        this.addListener(Events.Attach, this);
        this.addListener(Events.RowClick, new Listener<GridEvent<BeanModel>>() {

            @Override
            public void handleEvent(GridEvent<BeanModel> be) {
                currentRow = be.getRowIndex();
            }
        });
    }

    @Override
    public void handleEvent(GridEvent<BeanModel> be) {
        loader.load(ExpandGrid.START_SIZE, ExpandGrid.PAGE_SIZE);
    }

    public RpcProxy<PagingLoadResult<T>> getProxy() {
        return proxy;
    }

    public void setProxy(RpcProxy<PagingLoadResult<T>> proxy) {
        this.proxy = proxy;
    }

    public BasePagingLoader<PagingLoadResult<ModelData>> getLoader() {
        return loader;
    }

    public void setLoader(BasePagingLoader<PagingLoadResult<ModelData>> loader) {
        this.loader = loader;
    }

    public PagingToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(PagingToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public PO getPo() {
        return po;
    }

    public void setPo(PO po) {
        this.po = po;
    }

    public void setService(BaseServiceAsync service) {
        this.service = service;
    }

    public BaseServiceAsync getService() {
        return service;
    }
}
