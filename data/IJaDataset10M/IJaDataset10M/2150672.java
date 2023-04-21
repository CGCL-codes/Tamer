package de.tmtools.ui.dialog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.index.IndexManagerException;
import de.tmtools.ui.AssociationTypeColor;

public class ConfigAssociationTypeColorDialog extends Dialog {

    private HashMap<TableItem, AssociationTypeColor> mapTableItem2Color = new HashMap<TableItem, AssociationTypeColor>();

    private HashMap<Topic, Color> mapTopic2Color;

    private Table colorTable;

    ColorDialog colorDialog;

    private TopicMap topicMap = null;

    public ConfigAssociationTypeColorDialog(Shell parentShell, String title) {
        super(parentShell);
    }

    @Override
    protected Control createContents(Composite parent) {
        parent.getShell().setText("Association Type Colors");
        return super.createContents(parent);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        Form form = toolkit.createForm(parent);
        form.setText("Configure Colors");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(gd);
        Label label = toolkit.createSeparator(form.getBody(), SWT.HORIZONTAL);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);
        colorTable = toolkit.createTable(form.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
        colorTable.setLinesVisible(true);
        colorTable.setHeaderVisible(true);
        TableColumn col1 = new TableColumn(colorTable, SWT.NONE);
        col1.setText("Association Type");
        TableColumn col2 = new TableColumn(colorTable, SWT.NONE);
        col2.setText("Color");
        col1.pack();
        col2.pack();
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 2;
        colorTable.setLayoutData(gd);
        colorTable.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                Point pt = new Point(event.x, event.y);
                TableItem item = colorTable.getItem(pt);
                if (item != null && item.getBounds(1).contains(pt)) {
                    RGB rgb = colorDialog.open();
                    if (rgb != null) {
                        Color color = new Color(getParentShell().getDisplay(), rgb);
                        item.setBackground(1, color);
                        AssociationTypeColor as = mapTableItem2Color.get(item);
                        as.setColor(color);
                    }
                    colorTable.select(new int[0]);
                }
            }
        });
        updateTable();
        colorDialog = new ColorDialog(parent.getShell());
        return form.getBody();
    }

    @Override
    protected Point getInitialSize() {
        return new Point(250, 400);
    }

    public void setModel(TopicMap topicmap) {
        this.topicMap = topicmap;
    }

    protected void updateTable() {
        if (topicMap == null) return;
        mapTableItem2Color.clear();
        try {
            Iterator iterator = topicMap.getIndexManager().getAssociationTypesIndex().getAssociationTypes().iterator();
            while (iterator.hasNext()) {
                Topic topic = (Topic) iterator.next();
                TableItem row = new TableItem(colorTable, 0);
                row.setText(0, topic.getID());
                Color color = this.getParentShell().getDisplay().getSystemColor(SWT.COLOR_BLUE);
                row.setBackground(1, color);
                AssociationTypeColor a = new AssociationTypeColor(topic, color);
                mapTableItem2Color.put(row, a);
            }
        } catch (IndexManagerException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected void okPressed() {
        updateTopicColorHashMap();
        super.okPressed();
    }

    protected void updateTopicColorHashMap() {
        mapTopic2Color = new HashMap<Topic, Color>();
        Collection<AssociationTypeColor> values = mapTableItem2Color.values();
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            AssociationTypeColor entry = (AssociationTypeColor) iter.next();
            mapTopic2Color.put(entry.getTopic(), entry.getColor());
        }
    }

    public HashMap<Topic, Color> getTopicColorHashMap() {
        return mapTopic2Color;
    }
}
