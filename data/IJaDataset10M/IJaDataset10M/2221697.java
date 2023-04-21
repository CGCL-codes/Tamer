package screencompare.browser;

public class BrowserChrome extends Browser {

    public String getName() {
        return "Google Chrome";
    }

    public String getWindowTitle() {
        return "Google Chrome";
    }

    public BrowserModel getModel() {
        return BrowserModel.CHROME;
    }
}
