package com.sun.lwuit.uidemo;

import java.awt.Color;
import java.io.IOException;
import com.sun.dtv.lwuit.CheckBox;
import com.sun.dtv.lwuit.Command;
import com.sun.dtv.lwuit.Component;
import com.sun.dtv.lwuit.Container;
import com.sun.dtv.lwuit.Image;
import com.sun.dtv.lwuit.Label;
import com.sun.dtv.lwuit.List;
import com.sun.dtv.lwuit.events.ActionEvent;
import com.sun.dtv.lwuit.events.ActionListener;
import com.sun.dtv.lwuit.layouts.BoxLayout;
import com.sun.dtv.lwuit.list.DefaultListCellRenderer;
import com.sun.dtv.lwuit.list.ListCellRenderer;
import com.sun.dtv.lwuit.util.Resources;

/**
 * A demo showing off the power of renderers both for combo boxes and lists
 *
 * @author Shai Almog
 */
public class RenderingDemo extends Demo implements ActionListener {

    private String[] RENDERED_CONTENT = { "ImageRenderer", "WidgetRenderer", "ColorRenderer", "DefaultListCellRenderer", "FishEyeRenderer" };

    private List list;

    private ListCellRenderer[] renderers;

    public String getName() {
        return "Rendering";
    }

    public void cleanup() {
        list = null;
        renderers = null;
    }

    protected void execute(final Container f) {
        f.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        f.setScrollable(false);
        list = new List(RENDERED_CONTENT);
        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        list.setSmoothScrolling(true);
        list.getStyle().setBgTransparency(0);
        renderers = new ListCellRenderer[RENDERED_CONTENT.length];
        renderers[0] = new AlternateImageRenderer();
        renderers[1] = new WidgetRenderer();
        renderers[2] = new AlternateColorRenderer();
        renderers[3] = new DefaultListCellRenderer();
        renderers[4] = new FishEyeRenderer();
        list.setListCellRenderer(renderers[0]);
        list.addActionListener(this);
        Label l = new Label("Press \"Select\" to update:");
        l.getStyle().setBgTransparency(100);
        f.addComponent(l);
        f.addComponent(list);
        l = new Label("Try Applying Renderer To Menu");
        l.getStyle().setBgTransparency(100);
        f.addComponent(l);
        f.getComponentForm().addCommand(new Command("Set To Menu") {

            public void actionPerformed(ActionEvent ev) {
                f.getComponentForm().setMenuCellRenderer(list.getRenderer());
            }
        });
    }

    /**
     * Demonstrates we can derive from any component and implement a renderer any way
     * we please... Normally it would be best to derive from DefaultListCellRenderer.
     */
    private static class AlternateColorRenderer extends Label implements ListCellRenderer {

        private int[] colors = new int[] { 0xFF0000, 0xFF00, 0xFF };

        private static int currentIndex;

        public AlternateColorRenderer() {
            super("");
        }

        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
            setText(value.toString());
            currentIndex = index % 3;
            if (isSelected) {
                setFocus(true);
                getStyle().setBgTransparency(100);
                getStyle().setFgSelectionColor(new Color(colors[currentIndex]));
            } else {
                setFocus(false);
                getStyle().setBgTransparency(0);
                getStyle().setFgColor(new Color(colors[currentIndex]));
            }
            return this;
        }

        public Component getListFocusComponent(List list) {
            setText("");
            setFocus(true);
            getStyle().setBgTransparency(100);
            return this;
        }
    }

    /**
     * Returns the text that should appear in the help command
     */
    protected String getHelp() {
        return "Demonstrates renderers allowing us to customize the look of data " + "structures returned from the model. By allowing the model to return " + "any data type we can save the need to type convert which allows for far " + "simpler code. We can also customize the widgets to any degree desired.";
    }

    public void actionPerformed(ActionEvent evt) {
        int newSelected = ((List) evt.getSource()).getSelectedIndex();
        list.setListCellRenderer(renderers[newSelected]);
        list.requestFocus();
        list.getParent().revalidate();
    }

    /**
     * Demonstrates implementation of a renderer derived from a label 
     * and the use of icons in the renderer
     */
    private static class AlternateImageRenderer extends Label implements ListCellRenderer {

        private Image[] images;

        /** Creates a new instance of AlternateImageRenderer */
        public AlternateImageRenderer() {
            super("");
            images = new Image[2];
            try {
                images[0] = Image.createImage("/sad.png");
                images[1] = Image.createImage("/smily.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
            setText(value.toString());
            if (isSelected) {
                setFocus(true);
                setIcon(images[1]);
                getStyle().setBgTransparency(100);
            } else {
                setFocus(false);
                setIcon(images[0]);
                getStyle().setBgTransparency(0);
            }
            return this;
        }

        public Component getListFocusComponent(List list) {
            setIcon(images[1]);
            setText("");
            setFocus(true);
            getStyle().setBgTransparency(100);
            return this;
        }
    }

    /**
     * Demonstrates implementation of a renderer derived from a CheckBox 
     */
    private static class WidgetRenderer extends CheckBox implements ListCellRenderer {

        /** Creates a new instance of WidgetsRenderer */
        public WidgetRenderer() {
            super("");
        }

        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
            setText("" + value);
            if (isSelected) {
                setFocus(true);
                getStyle().setBgTransparency(100);
                setSelected(true);
            } else {
                setFocus(false);
                getStyle().setBgTransparency(0);
                setSelected(false);
            }
            return this;
        }

        public Component getListFocusComponent(List list) {
            setText("");
            setFocus(true);
            getStyle().setBgTransparency(100);
            setSelected(true);
            return this;
        }
    }

    /**
     * Demonstrates implementation of a renderer derived from a label 
     * and the use of icons in the renderer
     */
    private static class FishEyeRenderer extends Label implements ListCellRenderer {

        private Label title;

        private Container selected = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        public FishEyeRenderer() {
            super("");
            Image smily = null;
            try {
                Resources imageRes = UIDemo.getResource("images");
                smily = imageRes.getImage("smily.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            title = new Label("");
            title.getStyle().setBgTransparency(0);
            Label description = new Label(smily);
            description.setText("description...");
            description.getStyle().setBgTransparency(0);
            selected.addComponent(title);
            selected.addComponent(description);
            title.setFocus(true);
            description.setFocus(true);
        }

        public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
            if (isSelected) {
                title.setText(value.toString());
                return selected;
            }
            setText(value.toString());
            setFocus(false);
            getStyle().setBgTransparency(0);
            return this;
        }

        public Component getListFocusComponent(List list) {
            setText("");
            setFocus(true);
            getStyle().setBgTransparency(100);
            return this;
        }
    }
}
