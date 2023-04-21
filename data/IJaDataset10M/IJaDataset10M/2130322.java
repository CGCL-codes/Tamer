package net.sf.mogbox.area;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.mogbox.Perspective;
import net.sf.mogbox.pol.ffxi.DataFile;
import net.sf.mogbox.pol.ffxi.FFXI;
import net.sf.mogbox.pol.ffxi.loader.Loader;
import net.sf.mogbox.pol.ffxi.loader.StringLoader;
import net.sf.mogbox.preferences.Preferences;
import net.sf.mogbox.renderer.engine.scene.Node;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class AreaPerspective implements Perspective {

    private static final int DEFAULT_NAME_LIST_ID = 0xD8A9;

    private static Logger log = Logger.getLogger(AreaPerspective.class.getName());

    private Area area;

    private Preferences preferences;

    private Map<Integer, Image> expansionIcons = null;

    private Pattern unknown;

    public AreaPerspective() {
        area = new Area();
        preferences = Preferences.getInstance(this);
        unknown = Pattern.compile(Strings.getString("area.unknown.regex"));
    }

    @Override
    public Node getNode() {
        return area;
    }

    @Override
    public String getName() {
        return "mogbox.area";
    }

    @Override
    public String getDisplayName() {
        return Strings.getString("perspective.name");
    }

    @Override
    public URL getIconLocation() {
        return null;
    }

    @Override
    public void createPanel(Composite parent) {
        Display display = parent.getDisplay();
        if (expansionIcons == null) {
            expansionIcons = new HashMap<Integer, Image>();
            InputStream in;
            in = getClass().getResourceAsStream("icon-RotZ.png");
            expansionIcons.put(FFXI.EXP_RISE_OF_THE_ZILART, new Image(display, in));
            in = getClass().getResourceAsStream("icon-CoP.png");
            expansionIcons.put(FFXI.EXP_CHAINS_OF_PROMATHIA, new Image(display, in));
            in = getClass().getResourceAsStream("icon-ToAU.png");
            expansionIcons.put(FFXI.EXP_TREASURES_OF_AHT_URHGAN, new Image(display, in));
            in = getClass().getResourceAsStream("icon-WotG.png");
            expansionIcons.put(FFXI.EXP_WINGS_OF_THE_GODDESS, new Image(display, in));
            in = getClass().getResourceAsStream("icon-ACP.png");
            expansionIcons.put(FFXI.EXP_A_CRYSTALLINE_PROPHECY, new Image(display, in));
            in = getClass().getResourceAsStream("icon-AMKD.png");
            expansionIcons.put(FFXI.EXP_A_MOOGLE_KUPO_DETAT, new Image(display, in));
            in = getClass().getResourceAsStream("icon-ASA.png");
            expansionIcons.put(FFXI.EXP_A_SHANTOTTO_ASCENSION, new Image(display, in));
        }
        Composite composite = new Composite(parent, SWT.NONE);
        Table table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(false);
        table.setLinesVisible(false);
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setResizable(false);
        column.setMoveable(false);
        int list = DEFAULT_NAME_LIST_ID;
        try {
            list = Integer.decode(Strings.getString("area.namelist"));
        } catch (NumberFormatException e) {
        }
        StringLoader loader = null;
        try {
            DataFile dat = new DataFile(list);
            FileChannel c = dat.getFileChannel();
            Loader<?> l = Loader.newInstance(c);
            if (l instanceof StringLoader) loader = (StringLoader) l;
        } catch (IOException e) {
            log.log(Level.SEVERE, null, e);
        }
        int selection = preferences.getInt("area.selection", 0);
        int max = Area.getMax();
        for (int i = 0; i <= max; i++) {
            String[] name = null;
            if (loader != null && loader.hasNext()) name = loader.next();
            if (Area.isUsed(i)) {
                TableItem item = new TableItem(table, SWT.NONE);
                item.setImage(expansionIcons.get(Area.getExpansion(i)));
                if (name != null && name.length > 0 && !name[0].isEmpty() && !unknown.matcher(name[0]).matches()) {
                    item.setText(name[0]);
                } else {
                    item.setText(String.format("%s [%d]", Strings.getString("unknown"), i));
                }
                item.setData("ID", i);
                if (selection == i) table.setSelection(item);
            }
        }
        if (table.getSelectionCount() < 1) table.setSelection(0);
        FormData data;
        composite.setLayout(new FormLayout());
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(1, 1, -3);
        data.bottom = new FormAttachment(1, 1, -3);
        table.setLayoutData(data);
        Listener listener = new EventListener();
        table.addListener(SWT.Resize, listener);
        table.addListener(SWT.Selection, listener);
        table.notifyListeners(SWT.Selection, new Event());
    }

    private class EventListener implements Listener {

        @Override
        public void handleEvent(Event e) {
            Table table = (Table) e.widget;
            switch(e.type) {
                case SWT.Selection:
                    TableItem[] selection = table.getSelection();
                    if (selection.length < 1) return;
                    Object o = selection[0].getData("ID");
                    if (o != null && o instanceof Integer) {
                        int id = (Integer) o;
                        preferences.setInt("area.selection", id);
                        area.set((Integer) id);
                    }
                    break;
                case SWT.Resize:
                    table.getColumns()[0].setWidth(table.getClientArea().width);
                    break;
            }
        }
    }
}
