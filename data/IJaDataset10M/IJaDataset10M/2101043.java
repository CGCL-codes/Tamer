package personal;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DemoFrame extends basis.DemoFrame {

    Dialog dialog;

    FileDialog fileDialog;

    static {
        DemoFrame.className = "personal.DemoFrame";
        DemoFrame.title = "J2ME Personal";
        demos.add("personal.demos.WidgetDemo");
    }

    public static void main(String[] args) throws Exception {
        basis.DemoFrame.parse(args);
        Frame frame = new DemoFrame(demos, size);
        frame.setVisible(true);
    }

    public DemoFrame(ArrayList demos, Dimension size) throws Exception {
        super(demos, size);
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MenuItem mi = (MenuItem) e.getSource();
                builder.showDemo(mi.getLabel());
            }
        };
        if (demos.contains("personal.demos.WidgetDemo")) {
            if (demos.size() > 1) {
                for (int i = 0; i < demos.size(); i++) {
                    String label = (String) demos.get(i);
                    label = label.substring(label.lastIndexOf(".") + 1, label.lastIndexOf("Demo"));
                    MenuItem mi = new MenuItem(label);
                    menu.add(mi);
                    mi.addActionListener(listener);
                }
            }
            MenuItem dialogMenuItem = new MenuItem("Dialog ");
            dialogMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    if (dialog == null) {
                        dialog = new AboutDialog(DemoFrame.this, "Dialog", true);
                        Toolkit toolkit = getToolkit();
                        Dimension screen = toolkit.getScreenSize();
                        Dimension dim = dialog.getSize();
                        dialog.setLocation((screen.width - dim.width) / 2, (screen.height - dim.height) / 3);
                    }
                    dialog.setVisible(true);
                }
            });
            MenuItem fileMenuItem = new MenuItem("File dialog");
            fileMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    if (fileDialog == null) {
                        fileDialog = new FileDialog(DemoFrame.this, "FileDialog");
                    }
                    fileDialog.setVisible(true);
                }
            });
            MenuShortcut ms = new MenuShortcut(KeyEvent.VK_E);
            MenuItem exitMenuItem = new MenuItem("Exit", ms);
            exitMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    System.exit(0);
                }
            });
            menu.addSeparator();
            menu.add(new CheckboxMenuItem("Checkbox  "));
            menu.add(dialogMenuItem);
            menu.add(fileMenuItem);
            menu.add(exitMenuItem);
            menuBar.add(menu);
            setMenuBar(menuBar);
            if (demos.size() > 1) {
                builder.showDemo("Widget");
                builder.showDemo("Graphics");
            }
        }
    }

    public class AboutDialog extends Dialog {

        AboutDialog(Frame owner, String title, boolean modal) {
            super(owner, title, modal);
            Label label = new Label("This is a dialog");
            add("Center", label);
            Button button = new Button("Close");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    AboutDialog.this.setVisible(false);
                }
            });
            add("South", button);
            pack();
        }
    }
}
