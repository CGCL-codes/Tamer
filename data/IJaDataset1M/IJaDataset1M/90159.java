package thinwire.ui.ext;

import thinwire.ui.event.ActionEvent;
import thinwire.ui.event.ActionListener;

public class ErrorDialog extends thinwire.ui.Dialog {

    thinwire.ui.WebBrowser webBrowser1 = null;

    thinwire.ui.Button okButton = null;

    protected void initialize() {
        setModal(true);
        okButton.addActionListener(ACTION_CLICK, new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                setVisible(false);
            }
        });
    }

    public ErrorDialog(String message) {
        this();
        webBrowser1.setContent(message);
    }

    public ErrorDialog() {
        super();
        this.setResizeAllowed(true);
        this.setLayout(new thinwire.ui.layout.ext.AnchorLayout());
        this.setTitle("Error");
        this.setX(0);
        this.setY(0);
        this.setWidth(300);
        this.setHeight(300);
        webBrowser1 = new thinwire.ui.WebBrowser();
        this.getChildren().add(webBrowser1);
        webBrowser1.setLimit(new thinwire.ui.layout.ext.anchorlayout.AnchorLimit(new thinwire.ui.layout.ext.anchorlayout.PositionalAnchor(false, 1), new thinwire.ui.layout.ext.anchorlayout.PositionalAnchor(true, 1), new thinwire.ui.layout.ext.anchorlayout.PositionalAnchor(false, 1), new thinwire.ui.layout.ext.anchorlayout.PositionalAnchor(true, 30)));
        webBrowser1.setX(5);
        webBrowser1.setY(6);
        webBrowser1.setWidth(289);
        webBrowser1.setHeight(225);
        okButton = new thinwire.ui.Button();
        this.getChildren().add(okButton);
        okButton.setText("Ok");
        okButton.setLimit(new thinwire.ui.layout.ext.anchorlayout.AnchorLimit(new thinwire.ui.layout.ext.anchorlayout.PercentageAnchor(30), new thinwire.ui.layout.ext.anchorlayout.PercentageAnchor(70), new thinwire.ui.layout.ext.anchorlayout.PositionalAnchor(true, 28), new thinwire.ui.layout.ext.anchorlayout.PositionalAnchor(true, 1)));
        okButton.setX(94);
        okButton.setY(249);
        okButton.setWidth(120);
        okButton.setHeight(26);
        if (getLayout() != null) {
            getLayout().apply();
        }
        initialize();
    }
}
