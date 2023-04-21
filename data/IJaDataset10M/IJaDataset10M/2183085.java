package main;

public class XAliens {

    public frmMain gameArea;

    public static void main(String[] args) throws Exception {
        new XAliens().init();
    }

    public static void exitApp() {
        System.exit(0);
    }

    public void init() throws Exception {
        gameArea = new frmMain();
        gameArea.setVisible(true);
    }
}
