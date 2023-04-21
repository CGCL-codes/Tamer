package test;

import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.model.SimpleTextAreaModel;

/**
 *
 * @author Matthias Mann
 */
public class TextAreaDemoDialog2 extends FadeFrame {

    public TextAreaDemoDialog2() {
        SimpleTextAreaModel tam = new SimpleTextAreaModel();
        tam.setText("This is a small test message. It's not too long.\n" + "\tThis is a small test message. It's not too long.\n" + "This\tis a small test message. It's not too long.\n" + "This is\ta small test message. It's not too long.\n" + "This is a\tsmall test message. It's not too long.\n" + "This is a small\ttest message. It's not too long.\n" + "This is a small test\tmessage. It's not too long.\n" + "This is a small test message.\tIt's not too long.\n" + "This is a small test message. It's\tnot too long.\n" + "This is a small test message. It's not\ttoo long.\n" + "This is a small test message. It's not too\tlong.");
        TextArea scrolledWidget2 = new TextArea(tam);
        scrolledWidget2.setTheme("textarea");
        ScrollPane scrollPane2 = new ScrollPane(scrolledWidget2);
        scrollPane2.setTheme("scrollpane");
        scrollPane2.setFixed(ScrollPane.Fixed.HORIZONTAL);
        setTheme("textAreaTestFrame");
        setTitle("TextArea tab test");
        add(scrollPane2);
    }
}
