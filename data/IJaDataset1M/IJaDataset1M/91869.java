package vncviewer;

import java.awt.*;

class AboutDialog extends vncviewer.Dialog {

    public AboutDialog() {
        super(false);
        setTitle("About VeNCrypt Viewer");
        Panel p1 = new Panel();
        p1.setLayout(new GridLayout(0, 1));
        p1.add(new Label(VNCViewer.about1));
        p1.add(new Label(VNCViewer.about2));
        p1.add(new Label(VNCViewer.about2_2));
        p1.add(new Label(VNCViewer.about2_5));
        p1.add(new Label(VNCViewer.about3));
        p1.add(new Label(VNCViewer.about4));
        add("Center", p1);
        Panel p2 = new Panel();
        okButton = new Button("OK");
        p2.add(okButton);
        add("South", p2);
        pack();
    }

    public boolean action(Event event, Object arg) {
        if (event.target == okButton) {
            ok = true;
            endDialog();
        }
        return true;
    }

    Button okButton;
}
