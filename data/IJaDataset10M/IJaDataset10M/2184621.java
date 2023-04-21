package ar.com.puntosoft.shared.showcase.client;

import java.util.HashMap;
import java.util.Map;
import org.gwt.mosaic.ui.client.LoadingPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import ar.com.puntosoft.shared.gwt.rpc.client.RPCFactoryListener;
import ar.com.puntosoft.shared.showcase.client.facade.AdminFacade;
import ar.com.puntosoft.shared.showcase.client.facade.AdminFacadeAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * @author Alejandro D. Garin <agarin@gmail.com>
 */
public class Application implements RPCFactoryListener, SelectionHandler<TreeItem>, ValueChangeHandler<String> {

    private static Application instance;

    private ApplicationLayout appLayout;

    private int rpcCount;

    private final AdminFacadeAsync facade;

    private LoadingPanel loadingPanel;

    private final EmployeeWidget employeeWidget = new EmployeeWidget();

    private final CacheWidget fooNoCacheWidget = new CacheWidget(false);

    private final CacheWidget fooCacheWidget = new CacheWidget(true);

    private final Image loadingImage = new Image(GWT.getModuleBaseURL() + "images/loading.gif");

    private final Map<String, TreeItem> itemTokens = new HashMap<String, TreeItem>();

    private final Map<TreeItem, ContentWidget> itemWidgets = new HashMap<TreeItem, ContentWidget>();

    private Application() {
        facade = GWT.create(AdminFacade.class);
        History.addValueChangeHandler(this);
    }

    public static Application get() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public void setLayout(ApplicationLayout appLayout) {
        this.appLayout = appLayout;
        this.appLayout.getMainMenu().addSelectionHandler(this);
    }

    @Override
    public void onError(Throwable caught) {
        MessageBox.error("Error", caught.getMessage());
    }

    @Override
    public void onLoadingFinish() {
        rpcCount--;
        if (rpcCount == 0) {
            loadingPanel.hide();
        }
    }

    @Override
    public void onLoadingStart() {
        this.rpcCount++;
        if (rpcCount == 1) {
            loadingPanel = LoadingPanel.show(appLayout.getContentWrapper(), loadingImage);
        }
    }

    private void displayContentWidget(final ContentWidget content) {
        if (content != null) {
            if (!content.isInitialized()) {
                content.initialize();
            }
            appLayout.setContent(content);
        }
    }

    public void init() {
        setupMainMenu();
        if (History.getToken().length() > 0) {
            History.fireCurrentHistoryState();
        } else {
            TreeItem firstItem = appLayout.getMainMenu().getItem(0).getChild(0);
            appLayout.getMainMenu().setSelectedItem(firstItem, false);
            appLayout.getMainMenu().ensureSelectedItemVisible();
            displayContentWidget(itemWidgets.get(firstItem));
            appLayout.getContentWrapper().layout();
        }
    }

    private void setupMainMenu() {
        Tree mainMenu = appLayout.getMainMenu();
        mainMenu.clear();
        TreeItem examples = mainMenu.addItem("GWT / GAE Examples");
        TreeItem employeeWidgetItem = examples.addItem(employeeWidget.getTitle());
        addTreeItem(employeeWidget, employeeWidgetItem);
        TreeItem fooCacheWidgetItem = examples.addItem(fooCacheWidget.getTitle());
        addTreeItem(fooCacheWidget, fooCacheWidgetItem);
        TreeItem fooNoCacheWidgetItem = examples.addItem(fooNoCacheWidget.getTitle());
        addTreeItem(fooNoCacheWidget, fooNoCacheWidgetItem);
    }

    private void addTreeItem(ContentWidget widget, TreeItem treeItem) {
        itemWidgets.put(treeItem, widget);
        itemTokens.put(getContentWidgetToken(widget), treeItem);
    }

    public AdminFacadeAsync getFacade() {
        return facade;
    }

    public EmployeeWidget getEmployeeWidget() {
        return employeeWidget;
    }

    @Override
    public void onSelection(SelectionEvent<TreeItem> event) {
        TreeItem item = event.getSelectedItem();
        ContentWidget content = itemWidgets.get(item);
        if (content != null && !content.equals(appLayout.getContent())) {
            History.newItem(getContentWidgetToken(content));
        }
    }

    private String getContentWidgetToken(ContentWidget content) {
        String className = content.toString();
        className = className.substring(className.lastIndexOf('.') + 1);
        return className;
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        TreeItem item = itemTokens.get(event.getValue());
        if (item == null) {
            item = appLayout.getMainMenu().getItem(0).getChild(0);
        }
        appLayout.getMainMenu().setSelectedItem(item, false);
        appLayout.getMainMenu().ensureSelectedItemVisible();
        displayContentWidget(itemWidgets.get(item));
        this.appLayout.getContentWrapper().layout();
    }
}
