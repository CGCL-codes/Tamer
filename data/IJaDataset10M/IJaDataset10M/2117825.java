package seedpod.webapp.view.seedpodwidgets;

import java.util.Vector;
import java.io.Writer;
import java.io.IOException;
import seedpod.webapp.view.htmlwidget.HiddenInput;
import seedpod.webapp.view.htmlwidget.Image;
import seedpod.webapp.view.htmlwidget.ImageButton;
import seedpod.webapp.view.htmlwidget.Label;
import seedpod.webapp.view.htmlwidget.Table;
import seedpod.webapp.view.htmlwidget.TableCell;
import seedpod.webapp.view.htmlwidget.TableRow;

public class DefaultSeedpodInstanceRenderer {

    /**
   * renders an EMS object (instance)
   * Content includes a header for the object that includes an icon, object's
   * browser key label, and the object's type,
   * instance has a menu key, and finally, object's data
   */
    protected Label label;

    protected String objectType;

    protected Vector<Object> objComponents;

    protected Vector<Label> componentLabels;

    protected Vector<ImageButton> actionMenu;

    protected int numComponents;

    protected Label rendererLabel;

    protected Table _content;

    protected Vector<HiddenInput> hiddenComponents;

    protected static Image icon = new Image("instance", "image/Instance.gif");

    public DefaultSeedpodInstanceRenderer(String objectType) {
        this.objectType = objectType;
        objComponents = new Vector<Object>();
        componentLabels = new Vector<Label>();
        actionMenu = new Vector<ImageButton>();
        numComponents = 0;
        _content = new Table();
        hiddenComponents = new Vector<HiddenInput>();
    }

    public void setLabel(String l) {
        label = new Label(l);
        label.setCssClass("ObjectLabel");
    }

    /**
   * the component list is a list of "Widget" objects
   * @todo change parameter object w to widget w
   * @param l  should be an label object?
   * @param w should be an EMSWidget
   */
    public void addComponent(Label l, Object w) {
        componentLabels.add(numComponents, l);
        objComponents.add(numComponents, w);
        numComponents++;
    }

    public void addHiddenComponent(HiddenInput w) {
        hiddenComponents.add(w);
    }

    public Object getCompoenent(Label label) {
        int objIndex = -1;
        componentLabels.indexOf(label);
        if (objIndex != -1) return objComponents.get(objIndex); else return null;
    }

    protected String makeHeader() {
        String header = "";
        if (rendererLabel != null) {
            rendererLabel.render();
            header += rendererLabel.render() + ": ";
        }
        if (label != null) {
            header += icon.toString() + " ";
            header += label.render();
        }
        return header;
    }

    public void setRendererLabel(Label header) {
        rendererLabel = header;
        rendererLabel.setCssClass("mainContentHeader");
    }

    public void addActionMenuItem(ImageButton menuItem) {
        actionMenu.add(menuItem);
    }

    private String renderActionMenu() {
        String menu = "";
        ImageButton ib;
        for (int m = 0; m < actionMenu.size(); m++) {
            ib = (ImageButton) actionMenu.get(m);
            menu += ib.render();
            menu += "&nbsp;";
        }
        return menu;
    }

    public String render() {
        Table content = new Table();
        content.setAlternativeRowColor(false);
        content.setTableCellSpacing(1, 3);
        TableRow header = content.addRow();
        header.addCell(makeHeader());
        content.addRow();
        TableCell menu = content.addCell(renderActionMenu());
        menu.setNoWrap(true);
        menu.setCssClass("mainContentMenu");
        menu.setAlignment("right");
        TableCell labelCell;
        Table tblComponent = new Table();
        tblComponent.setTableCellSpacing(1, 3);
        tblComponent.setAlternativeRowColor(true);
        for (int i = 0; i < numComponents; i++) {
            tblComponent.addRow();
            Label componentLabel = componentLabels.get(i);
            String labelStr = (componentLabel != null) ? componentLabel.render() : "";
            labelCell = tblComponent.addCell(labelStr);
            labelCell.setNoWrap(true);
            tblComponent.addCell(labelStr);
        }
        content.addRow();
        content.addCell(tblComponent);
        content.addRow();
        content.addCell(renderHiddenComponents());
        return content.toString();
    }

    private String renderHiddenComponents() {
        String hiddenStr = "";
        for (int h = 0; h < hiddenComponents.size(); h++) {
            hiddenStr += (((HiddenInput) hiddenComponents.get(h)).render());
        }
        return hiddenStr;
    }

    public void render(Writer out) {
        try {
            out.write(render());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
