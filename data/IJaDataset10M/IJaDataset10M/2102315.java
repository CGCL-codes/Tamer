package jalview.gui;

import java.awt.*;

public class BrowserFrame extends TextFrame {

    Panel p4;

    Label l;

    TextField tf;

    String url;

    Button back;

    Button forward;

    SimpleBrowser sb;

    public BrowserFrame(SimpleBrowser sb, String title, int nrows, int ncols, String text, String url) {
        super(title, nrows, ncols, text);
        this.url = url;
        this.sb = sb;
        p4 = new Panel();
        l = new Label("URL : ");
        tf = new TextField(url, 60);
        setTextFont(new Font("Courier", Font.PLAIN, 12));
        back = new Button("Back");
        forward = new Button("Forward");
        p4.setLayout(new FlowLayout());
        p4.add(l);
        p4.add(tf);
        p4.add(back);
        p4.add(forward);
        add("North", p4);
    }

    public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY) {
            this.hide();
            this.dispose();
            return sb.handleEvent(evt);
        } else return super.handleEvent(evt);
    }

    public boolean action(Event e, Object args) {
        return sb.action(e, args);
    }
}
