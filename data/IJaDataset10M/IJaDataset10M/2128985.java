package jalview.gui;

import java.awt.*;
import java.awt.event.*;

public class TextFrame extends Frame implements ActionListener {

    TextArea ta;

    Label status;

    Button b;

    Panel p;

    Panel p2;

    Panel p3;

    public TextFrame(String title, int nrows, int ncols, String text) {
        super(title);
        ta = new TextArea(nrows, ncols);
        setText(text);
        frameInit();
    }

    public TextFrame(String title, int nrows, int ncols) {
        super(title);
        ta = new TextArea(nrows, ncols);
        frameInit();
    }

    public void frameInit() {
        ta.setBackground(Color.white);
        p = new Panel();
        p2 = new Panel();
        p3 = new Panel();
        b = new Button("close");
        status = new Label("Status:", Label.LEFT);
        p2.setLayout(new GridLayout(2, 1, 5, 5));
        p2.add(status);
        p.setLayout(new FlowLayout());
        p.add(b);
        p3.setLayout(new GridLayout(2, 1, 0, 0));
        p3.add(p2);
        p3.add(p);
        setLayout(new BorderLayout());
        add("Center", ta);
        add("South", p3);
        b.addActionListener(this);
    }

    public void setTextFont(Font f) {
        ta.setFont(f);
    }

    public void setText(String text) {
        ta.setText(text);
    }

    public String getText() {
        return ta.getText();
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == b) {
            this.hide();
            this.dispose();
        }
        return;
    }

    public static void main(String[] args) {
        TextFrame tf = new TextFrame("Test", 10, 72, "poggy text");
        tf.resize(500, 500);
        tf.show();
    }
}
