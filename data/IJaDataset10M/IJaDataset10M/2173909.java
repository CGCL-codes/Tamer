package it.polimi.elet.socialemis.ui;

import com.vaadin.addon.colorpicker.ColorPicker;
import com.vaadin.addon.colorpicker.events.ColorChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import it.polimi.elet.socialemis.SocialEMISApp;
import java.awt.Color;
import java.util.Iterator;

/**
 *
 * @author italo
 */
public class Shapes extends Window implements Property.ValueChangeListener {

    private Window shapesWindow;

    public Tree t = new Tree();

    private GridLayout grid;

    private SocialEMISApp app;

    private String shapesList;

    private VerticalLayout f;

    private TextField name;

    private Button newshape;

    private static final String[] types = new String[] { "Latest value", "Latest n-values", "Date range" };

    private Button save;

    private Button cancel;

    private TextField coord;

    private String d;

    private Button delete;

    private Button add;

    private Button reset;

    private Button stop;

    private Button start;

    private ColorPicker cp;

    public void Shapes(SocialEMISApp a, Window p, String s) {
        this.app = a;
        this.shapesWindow = p;
        VerticalLayout layout = (VerticalLayout) shapesWindow.getContent();
        layout.setMargin(true);
        layout.setSpacing(true);
        grid = new GridLayout(2, 2);
        grid.setSpacing(true);
        newshape = new Button("New shape", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                grid.removeComponent(1, 0);
                grid.addComponent(newShape(), 1, 0);
                save.setEnabled(false);
                cancel.setEnabled(false);
            }
        });
        newshape.setDisableOnClick(true);
        VerticalLayout l = new VerticalLayout();
        l.addComponent(newshape);
        l.setSpacing(true);
        l.addComponent(new Label("<b>Select a shape:</b>", Label.CONTENT_XHTML));
        insert_leafs(s);
        t.setDescription("shapes edit");
        t.setImmediate(true);
        t.addListener(this);
        l.addComponent(t);
        grid.addComponent(l, 0, 0);
        grid.setComponentAlignment(l, Alignment.TOP_LEFT);
        save = new Button("Save", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                app.saveShapes(ShapesList());
                (shapesWindow.getParent()).removeWindow(shapesWindow);
            }
        });
        cancel = new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                grid.removeComponent(1, 0);
                (shapesWindow.getParent()).removeWindow(shapesWindow);
            }
        });
        HorizontalLayout buttonsConfirm = new HorizontalLayout();
        buttonsConfirm.setSpacing(true);
        buttonsConfirm.addComponent(save);
        buttonsConfirm.addComponent(cancel);
        grid.addComponent(buttonsConfirm, 0, 1);
        layout.addComponent(grid);
    }

    private void insert_leafs(String s) {
        t.removeAllItems();
        String[] function = split(s, "[;]");
        for (int i = 0; i < function.length; i++) {
            if (!function[i].equals("")) {
                t.addItem(function[i]);
                t.setChildrenAllowed(function[i], false);
            }
        }
    }

    private static String[] split(String str, String delims) {
        String[] tokens = str.split(delims);
        return tokens;
    }

    private VerticalLayout newShape() {
        f = new VerticalLayout();
        f.setMargin(true);
        f.setSpacing(true);
        Label l = new Label("Draw shape on the map");
        start = new Button("Start", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                start.setEnabled(false);
                add.setEnabled(false);
                reset.setEnabled(false);
                app.createShapes();
                stop.setEnabled(true);
            }
        });
        stop = new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                start.setEnabled(true);
                stop.setEnabled(false);
                app.StopCreateShapes();
                coord.setValue("");
            }
        });
        HorizontalLayout buttonsDraw = new HorizontalLayout();
        buttonsDraw.setSpacing(true);
        buttonsDraw.addComponent(l);
        buttonsDraw.setComponentAlignment(l, Alignment.MIDDLE_LEFT);
        buttonsDraw.addComponent(start);
        buttonsDraw.addComponent(stop);
        stop.setEnabled(false);
        f.addComponent(buttonsDraw);
        coord = new TextField("Coordinates");
        coord.setRequired(true);
        coord.setWidth(25, UNITS_EM);
        f.addComponent(coord);
        Label c = new Label("Select a color for the shape");
        f.addComponent(c);
        HorizontalLayout buttonsColor = new HorizontalLayout();
        buttonsColor.setSpacing(true);
        buttonsColor.addComponent(c);
        buttonsColor.setComponentAlignment(c, Alignment.MIDDLE_LEFT);
        buttonsColor.addComponent(ColorWindow());
        f.addComponent(buttonsColor);
        add = new Button("Add", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                if (!coord.getValue().equals("")) {
                    String shapePar = coord.getValue().toString();
                    String rgb = Integer.toHexString(cp.getColor().getRGB());
                    shapePar += "#" + rgb.substring(2, rgb.length());
                    t.addItem(shapePar);
                    t.setChildrenAllowed(shapePar, false);
                    grid.removeComponent(1, 0);
                    newshape.setEnabled(true);
                    save.setEnabled(true);
                    cancel.setEnabled(true);
                }
            }
        });
        reset = new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                grid.removeComponent(1, 0);
                newshape.setEnabled(true);
                save.setEnabled(true);
                cancel.setEnabled(true);
            }
        });
        HorizontalLayout buttonsConfirm = new HorizontalLayout();
        buttonsConfirm.setSpacing(true);
        buttonsConfirm.addComponent(add);
        buttonsConfirm.addComponent(reset);
        f.addComponent(buttonsConfirm);
        return f;
    }

    public ColorPicker ColorWindow() {
        cp = new ColorPicker("Select a color", Color.RED);
        cp.setHSVVisibility(false);
        cp.setSwatchesVisibility(false);
        cp.setHistoryVisibility(true);
        cp.setTextfieldVisibility(true);
        cp.setButtonStyle(ColorPicker.ButtonStyle.BUTTON_AREA);
        return cp;
    }

    public void SetCoord(String c) {
        coord.setValue("");
        coord.setValue(c);
        add.setEnabled(true);
        reset.setEnabled(true);
    }

    public void valueChange(ValueChangeEvent event) {
        if (event.getProperty() instanceof Tree) {
            if (((Tree) event.getProperty()).getDescription().equals("shapes edit") && ((Tree) event.getProperty()).toString() != null) {
                grid.removeComponent(1, 0);
                grid.addComponent(dettagliShape(event.getProperty().toString()), 1, 0);
            }
        }
    }

    private VerticalLayout dettagliShape(String par) {
        d = par;
        f = new VerticalLayout();
        f.setMargin(true);
        f.setSpacing(true);
        Label l = new Label(d);
        f.addComponent(l);
        delete = new Button("Delete", new Button.ClickListener() {

            public void buttonClick(Button.ClickEvent event) {
                grid.removeComponent(1, 0);
                t.removeItem(d);
                f.setVisible(false);
            }
        });
        HorizontalLayout buttonsConfirm = new HorizontalLayout();
        buttonsConfirm.setSpacing(true);
        buttonsConfirm.addComponent(delete);
        f.addComponent(buttonsConfirm);
        return f;
    }

    private String ShapesList() {
        shapesList = "";
        for (Iterator<?> it = t.rootItemIds().iterator(); it.hasNext(); ) {
            shapesList += it.next() + ";";
        }
        return shapesList;
    }
}
