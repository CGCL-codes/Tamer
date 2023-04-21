package siseor.diagram.views;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import siseor.diagram.job.CompileJob;
import siseor.diagram.messages.Messages;
import siseor.diagram.part.SiseorPaletteFactory;
import api.client.genClient.GClient;
import api.server.MetadatenFassade;
import api.server.jUDDI.Suche;
import api.server.service.Message;
import api.server.service.Service;

public class Browser extends ViewPart {

    private final ImageRegistry registry = JFaceResources.getImageRegistry();

    private Action actionClearAll;

    private Action actionClearStubs;

    private Tree treeResults;

    private Action actionClear;

    private Combo comboSearchType;

    private Text textInput;

    private static Vector<Service> resultVector;

    public static Vector<Service> allSearchResults = new Vector<Service>();

    private Service selectedService;

    private String selectedOperation;

    private String selectedPart;

    private int ri;

    public void createPartControl(Composite parent) {
        createStructureAndListeners(parent);
        createActions();
        createToolbarAndMenue();
        createKeyListener();
        this.setPartName(Messages.BrowserTitle);
    }

    private void createKeyListener() {
        treeResults.addKeyListener(new KeyAdapter() {

            public void keyPressed(final KeyEvent event) {
                if (event.keyCode == SWT.DEL) {
                    contextRemovePressed();
                    treeResults.setFocus();
                }
                if (event.keyCode == 13 || event.keyCode == 16777296) {
                    treeResults.setFocus();
                }
            }
        });
        textInput.addKeyListener(new KeyAdapter() {

            public void keyReleased(final KeyEvent event) {
                buttonSearchPressed(null);
                if (event.keyCode == 13 || event.keyCode == 16777296) {
                    buttonSearchPressed(null);
                    treeResults.setFocus();
                }
            }
        });
    }

    private void createStructureAndListeners(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        Group groupSearch = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        GridData groupSearchLData = new GridData(SWT.FILL, SWT.NONE, true, false);
        groupSearch.setLayoutData(groupSearchLData);
        groupSearch.setLayout(layout);
        Label searchLabel = new Label(groupSearch, SWT.NONE);
        searchLabel.setText(Messages.BrowsersearchLabel);
        textInput = new Text(groupSearch, SWT.BORDER);
        textInput.setText("...");
        textInput.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        textInput.setToolTipText(Messages.BrowsertextInput);
        textInput.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                if (textInput.getText().equals("...")) textInput.setText("");
            }
        });
        textInput.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        Label searchTypeLabel = new Label(groupSearch, SWT.NONE);
        searchTypeLabel.setText(Messages.BrowsersearchTypeLabel);
        comboSearchType = new Combo(groupSearch, SWT.READ_ONLY);
        comboSearchType.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        comboSearchType.add("Name");
        comboSearchType.add("Tag");
        comboSearchType.add("Autor");
        comboSearchType.add("Date");
        comboSearchType.select(0);
        Group groupResults = new Group(parent, SWT.NONE);
        GridLayout group3Layout = new GridLayout();
        groupResults.setLayout(group3Layout);
        GridData resultLData = new GridData(SWT.FILL, SWT.FILL, false, true);
        groupResults.setLayoutData(resultLData);
        groupResults.setText(Messages.BrowsergroupResults);
        treeResults = new Tree(groupResults, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        treeResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        treeResults.setLinesVisible(false);
        treeResults.addTreeListener(new TreeAdapter() {

            public void treeExpanded(TreeEvent evt) {
                alternateTreeBackgrounds();
            }

            public void treeCollapsed(TreeEvent evt) {
                alternateTreeBackgrounds();
            }
        });
        treeResults.addListener(SWT.MouseDoubleClick, new Listener() {

            public void handleEvent(Event event) {
                mouseDoubleclicked(event);
            }
        });
        treeResults.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event event) {
                contextPropertiesPressed();
            }
        });
        treeResults.addListener(SWT.MENU, new Listener() {

            public void handleEvent(Event event) {
            }
        });
        treeResults.addListener(SWT.DragDetect, new Listener() {

            public void handleEvent(Event event) {
            }
        });
        treeResults.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                TreeItem[] mySelection = treeResults.getSelection();
                TreeItem[] items = new TreeItem[3];
                int level = -1;
                if (mySelection.length > 0) {
                    level = 0;
                    items[0] = mySelection[0];
                    while (items[level].getParentItem() != null) {
                        items[++level] = items[level - 1].getParentItem();
                    }
                }
                if (level >= 0) {
                    int index = treeResults.indexOf(items[level]);
                    selectedService = resultVector.get(index);
                    if (level > 0) selectedOperation = items[level - 1].getText(); else selectedOperation = "";
                    if (level > 1) selectedPart = items[level - 2].getText(); else selectedPart = "";
                } else {
                    selectedService = null;
                }
            }
        });
    }

    private void createActions() {
        actionClearAll = new Action() {

            public void run() {
                buttonClearAllPressed();
            }
        };
        actionClearAll.setText("Alles Zurücksetzen");
        actionClearAll.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        actionClearAll.setToolTipText("Alles Zurücksetzen");
        actionClearAll.setEnabled(false);
        actionClear = new Action() {

            public void run() {
                buttonClearPressed();
            }
        };
        actionClear.setText("Suchebegriff löschen");
        actionClear.setToolTipText("Suchebegriff löschen");
        actionClear.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
        actionClearStubs = new Action() {

            public void run() {
                buttonClearStubsPressed();
            }
        };
        actionClearStubs.setText("Temporären Ordner aufräumen");
        actionClearStubs.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
        actionClearStubs.setToolTipText("Temporären Ordner aufräumen");
    }

    @Override
    public void setFocus() {
    }

    private void createToolbarAndMenue() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(actionClearAll);
        manager.add(actionClear);
        manager.add(actionClearStubs);
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(actionClearAll);
        manager.add(actionClear);
        manager.add(actionClearStubs);
    }

    private void contextPropertiesPressed() {
        int n = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences().length - 1;
        for (; n >= 0; --n) {
            String partId = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences()[n].getId();
            if (partId.equals("siseor.diagram.views.infoview")) break;
        }
        Infoview info = (Infoview) PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences()[n].getView(true);
        info.showServiceDetails(selectedService, selectedOperation, selectedPart);
        PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].activate(info);
    }

    private void contextAddPressed() {
        if (selectedService != null) {
            CompileJob comp = new CompileJob(selectedService.getWsdlURL());
            comp.setUser(true);
            comp.schedule();
            if (!selectedOperation.isEmpty()) addToPalette(selectedOperation, selectedService.getWsdlURL(), selectedService);
        }
        alternateTreeBackgrounds();
    }

    private void contextRemovePressed() {
        alternateTreeBackgrounds();
    }

    private void mouseDoubleclicked(Event evt) {
        contextAddPressed();
    }

    /**
	 * @see 
	 *      <url>http://dev.eclipse.org/newslists/news.eclipse.platform.swt/msg23672
	 *      .html</url>
	 */
    private void alternateTreeBackgrounds() {
        ri = 0;
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                traverseAndColor(treeResults.getItems());
            }
        });
    }

    /**
	 * @see 
	 *      <url>http://dev.eclipse.org/newslists/news.eclipse.platform.swt/msg23672
	 *      .html</url>
	 */
    private void traverseAndColor(TreeItem[] items) {
        for (int i = 0; i < items.length; i++) {
            TreeItem item = items[i];
            Color color;
            if (ri % 2 == 0) {
                color = new Color(null, new RGB(255, 255, 255));
            } else {
                color = new Color(null, new RGB(250, 250, 250));
            }
            item.setBackground(color);
            setControlsBackground(item, color);
            ri++;
            if (item.getExpanded()) traverseAndColor(item.getItems());
        }
    }

    /**
	 * @see 
	 *      <url>http://dev.eclipse.org/newslists/news.eclipse.platform.swt/msg23672
	 *      .html</url>
	 */
    private void setControlsBackground(TreeItem item, Color color) {
        if (item.getData() != null && item.getData() instanceof Control[]) {
            Control[] controls = (Control[]) item.getData();
            for (Control control : controls) if (control != null) {
                control.setBackground(color);
            }
        }
    }

    private void buttonClearAllPressed() {
        textInput.setText("");
        comboSearchType.select(0);
        treeResults.removeAll();
        actionClearAll.setEnabled(false);
    }

    private void buttonClearPressed() {
        textInput.setText("");
    }

    private void buttonClearStubsPressed() {
        if (GClient.cleanSer()) System.out.println("[BROWSER] SER Datei geloescht"); else System.out.println("[BROWSER] Keine SER Datei gefunden");
        actionClearStubs.setEnabled(false);
    }

    private void buttonSearchPressed(MouseEvent evt) {
        String searchText = textInput.getText();
        selectedService = null;
        selectedOperation = "";
        selectedPart = "";
        Hashtable<String, String> namesOfServices = new Hashtable<String, String>();
        int n = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences().length - 1;
        for (; n >= 0; --n) {
            String partId = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences()[n].getId();
            if (partId.equals("siseor.diagram.views.infoview")) break;
        }
        Infoview info = (Infoview) PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences()[n].getView(true);
        info.showServiceDetails(selectedService, selectedOperation, selectedPart);
        Suche s = new Suche();
        resultVector = null;
        treeResults.removeAll();
        TreeItem itemEmpty = new TreeItem(treeResults, SWT.NONE);
        itemEmpty.setText("Kein Ergebniss gefunden!");
        if (searchText.isEmpty()) {
        } else {
            int selectionIndex = comboSearchType.getSelectionIndex();
            String value = "", serviceName = "", copy_value = "";
            switch(selectionIndex) {
                case 0:
                    resultVector = s.sucheName(searchText);
                    break;
                case 1:
                    resultVector = new Vector<Service>();
                    namesOfServices = s.loadNamesOfServices("TagsOfAllServices");
                    String keys_values[];
                    boolean bExist = false;
                    for (Enumeration<String> e = namesOfServices.elements(); e.hasMoreElements(); ) {
                        value = (String) e.nextElement();
                        copy_value = value.toLowerCase();
                        searchText = searchText.toLowerCase();
                        if (copy_value.startsWith(searchText)) {
                            keys_values = value.split("\\|");
                            serviceName = keys_values[1];
                            bExist = false;
                            for (Service service : resultVector) if (service.getName().equalsIgnoreCase(serviceName)) {
                                bExist = true;
                                break;
                            }
                            if (!bExist) resultVector.addAll((Collection<Service>) s.sucheTags(keys_values[0]));
                        }
                    }
                    break;
                case 2:
                    resultVector = new Vector<Service>();
                    namesOfServices = s.loadNamesOfServices("AuthorsOfAllServices");
                    for (Enumeration<String> e = namesOfServices.elements(); e.hasMoreElements(); ) {
                        value = (String) e.nextElement();
                        copy_value = value.toLowerCase();
                        searchText = searchText.toLowerCase();
                        if (copy_value.startsWith(searchText)) resultVector.addAll((Collection<Service>) s.sucheAuthor(value));
                    }
                    break;
                case 3:
                    resultVector = new Vector<Service>();
                    namesOfServices = s.loadNamesOfServices("DatesOfAllServices");
                    for (Enumeration<String> e = namesOfServices.elements(); e.hasMoreElements(); ) {
                        value = (String) e.nextElement();
                        copy_value = value.toLowerCase();
                        searchText = searchText.toLowerCase();
                        if (copy_value.startsWith(searchText)) resultVector.addAll((Collection<Service>) s.sucheDate(value));
                    }
                    break;
            }
            String name;
            Service service;
            Vector<String> serviceNames = new Vector<String>();
            int vectorSize = resultVector.size();
            treeResults.removeAll();
            if (vectorSize == 0) {
                TreeItem itemTemp = new TreeItem(treeResults, SWT.NONE);
                itemTemp.setText("Kein Ergebniss gefunden!");
            }
            for (int i = 0; i < vectorSize; i++) {
                service = resultVector.get(i);
                name = service.getName();
                if (serviceNames.contains((String) name)) {
                    continue;
                }
                serviceNames.add((String) name);
                try {
                    service = MetadatenFassade.getInstance().loadService(service.getUDDIServiceKey(), service.getWsdlURL());
                } catch (Exception e) {
                    System.out.println("Some error loading service");
                }
                resultVector.setElementAt(service, i);
                name = resultVector.get(i).getName();
                TreeItem itemServ = new TreeItem(treeResults, SWT.NONE);
                itemServ.setText(name);
                itemServ.setImage(registry.get("ws.gif"));
                addContextmenuToItem(itemServ);
                for (Enumeration<String> e = service.getOperations(); e.hasMoreElements(); ) {
                    String operation = (String) e.nextElement();
                    String inputMsg = service.getOperationMessage(operation, true);
                    String outputMsg = service.getOperationMessage(operation, false);
                    TreeItem itemOp = new TreeItem(itemServ, SWT.NONE);
                    itemOp.setText(operation);
                    itemOp.setText(1, service.getOperationDoc(operation));
                    itemOp.setImage(registry.get("operation.gif"));
                    Message iMsg = service.getMessage(inputMsg);
                    TreeItem itemInput = new TreeItem(itemOp, SWT.None);
                    itemInput.setText("Eingänge");
                    itemInput.setImage(registry.get("inport.gif"));
                    for (Iterator<String> it = (Iterator<String>) iMsg.getParts().iterator(); it.hasNext(); ) {
                        String part = it.next();
                        String partExt = iMsg.getPartDescription(part)[0];
                        TreeItem itemPart = new TreeItem(itemInput, SWT.None);
                        itemPart.setText(part + " (" + partExt + ")");
                        Image img = registry.get("DEFAULT_input.gif");
                        Image imgtmp = registry.get(partExt.toUpperCase() + "_input.gif");
                        if (imgtmp != null) img = imgtmp;
                        itemPart.setImage(img);
                    }
                    Message oMsg = service.getMessage(outputMsg);
                    TreeItem itemOutput = new TreeItem(itemOp, SWT.None);
                    itemOutput.setText("Ausgänge");
                    itemOutput.setImage(registry.get("outport.gif"));
                    for (Iterator<String> it = (Iterator<String>) oMsg.getParts().iterator(); it.hasNext(); ) {
                        String part = it.next();
                        String partExt = oMsg.getPartDescription(part)[0];
                        TreeItem itemPart = new TreeItem(itemOutput, SWT.None);
                        itemPart.setText(part + " (" + partExt + ")");
                        Image img = registry.get("DEFAULT_output.gif");
                        Image imgtmp = registry.get(partExt.toUpperCase() + "_output.gif");
                        if (imgtmp != null) img = imgtmp;
                        itemPart.setImage(img);
                    }
                }
            }
            for (int i = 0; i < resultVector.size(); i++) {
                Service serv = resultVector.elementAt(i);
                if (!allSearchResults.contains(serv)) allSearchResults.add(serv);
            }
        }
        actionClearAll.setEnabled(true);
        actionClearStubs.setEnabled(true);
        alternateTreeBackgrounds();
    }

    private void addContextmenuToItem(TreeItem treeitem) {
        Menu menu = new Menu(treeResults.getShell(), SWT.POP_UP);
        MenuItem menuItemAdd = new MenuItem(menu, SWT.PUSH);
        menuItemAdd.setText("Properties");
        menuItemAdd.setAccelerator(SWT.MOD1 + 'H');
        menuItemAdd.setImage(registry.get("br-con-add.gif"));
        menuItemAdd.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                contextPropertiesPressed();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                contextPropertiesPressed();
            }
        });
        treeResults.setMenu(menu);
    }

    private void addToPalette(String operation, String url, Service service) {
        int n = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences().length - 1;
        for (; n >= 0; --n) {
            String partId = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0].getViewReferences()[n].getId();
            if (partId.equals("org.eclipse.gef.ui.palette_view")) break;
        }
        for (int i = 0; i < SiseorPaletteFactory.paletteRoot.getChildren().size(); i++) {
            if ("siseor.diagram.palette.searcheddrawer" == ((PaletteContainer) SiseorPaletteFactory.paletteRoot.getChildren().get(i)).getId()) {
                PaletteContainer mydrawer = (PaletteContainer) SiseorPaletteFactory.paletteRoot.getChildren().get(i);
                ToolEntry entry = null;
                for (int j = 0; j < mydrawer.getChildren().size(); j++) {
                    entry = (ToolEntry) mydrawer.getChildren().get(j);
                    if (entry.getLabel().equals(operation)) break;
                }
                if (entry == null || !entry.getLabel().equals(operation)) {
                    siseor.diagram.part.SiseorPaletteFactory.addToDrawer(mydrawer, operation, url, service);
                    entry = (ToolEntry) mydrawer.getChildren().get(mydrawer.getChildren().size() - 1);
                }
            }
        }
    }
}
