package elf.SpriteViewer;

public class SpriteViewer {

    public static enum OSType {

        UNKNOWN, WINDOWS, LINUX, OSX
    }

    ;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (getOS() == OSType.OSX) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Sprite Editor");
        }
        MainWindow mw = MainWindow.getInstance();
        mw.createUI();
    }

    public static OSType getOS() {
        String os_name = System.getProperty("os.name", "");
        if (os_name.startsWith("Window")) return OSType.WINDOWS; else if (os_name.startsWith("Linux")) return OSType.LINUX; else if (os_name.startsWith("Mac OS X")) return OSType.OSX;
        return OSType.UNKNOWN;
    }
}
