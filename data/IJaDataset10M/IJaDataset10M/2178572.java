package thinwire.apps.test;

import thinwire.ui.*;
import thinwire.ui.event.*;
import thinwire.ui.layout.*;
import thinwire.ui.style.*;

public class TabFolderTest {

    public static void main(String[] args) {
        Frame f = Application.current().getFrame();
        f.setLayout(new TableLayout(new double[][] { { 100, 100, 100, 100 }, { 300, 25 } }, 10, 5));
        final TabFolder tf = new TabFolder();
        f.getChildren().add(tf.setLimit("0, 0, 4, 1"));
        Color[] COLOR = new Color[] { Color.LIGHTCORAL, Color.LIGHTGRAY, Color.LIGHTSTEELBLUE, Color.LIGHTGREEN, Color.LIGHTYELLOW };
        for (int i = 0; i < 5; i++) {
            TabSheet ts = new TabSheet("Tab Sheet " + i);
            ts.getStyle().getBackground().setColor(COLOR[i]);
            tf.getChildren().add(ts);
        }
        final TextField tab = new TextField();
        f.getChildren().add(tab.setLimit("1, 1"));
        Button b = new Button("Toggle Visibility");
        f.getChildren().add(b.setLimit("2, 1"));
        b.addActionListener("click", new ActionListener() {

            private boolean visible = true;

            public void actionPerformed(ActionEvent ev) {
                if (tab.getText().length() == 0) {
                    visible = !visible;
                    for (TabSheet ts : tf.getChildren()) {
                        if (ts != tf.getChildren().get(tf.getCurrentIndex())) {
                            ts.setVisible(visible);
                        }
                    }
                } else {
                    int value = Integer.parseInt(tab.getText());
                    TabSheet ts = tf.getChildren().get(value);
                    ts.setVisible(!ts.isVisible());
                }
            }
        });
    }
}
