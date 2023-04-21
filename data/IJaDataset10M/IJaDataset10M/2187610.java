package mainPackage;

import javax.swing.JOptionPane;

public class BrowserControl {

    private static final String errMsg = "Error attempting to launch web browser";

    public static void displayURL(String url) {
        String osName = System.getProperty("os.name");
        url = getAbsolutePath(url);
        CostantiDavide.msgInfo("url:" + url + "\nos:" + osName);
        try {
            if (osName.startsWith("Mac OS")) {
                Runtime.getRuntime().exec(new String[] { "open", url });
            } else if (osName.startsWith("Windows")) {
                url = tranform(url);
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[] { browser, url });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, errMsg + ":\n" + e.getLocalizedMessage());
        }
    }

    public static void displayCalcolatrice() {
        String url = null;
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                url = "/Applications/Calculator.app/Contents/MacOS/Calculator";
                Runtime.getRuntime().exec(url);
            } else if (osName.startsWith("Windows")) {
                url = "calc.exe";
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = { "gcalctool" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Non ho trovato alcuna calcolatrice");
                } else {
                    Runtime.getRuntime().exec(new String[] { browser });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, errMsg + ":\n" + e.getLocalizedMessage());
        }
    }

    private static String getAbsolutePath(String url) {
        String curDir = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        boolean absolute = false;
        if (url != null) {
            if (url.length() > 0) {
                if ((url.charAt(0) == '\\') || (url.charAt(0) == '/')) {
                    absolute = true;
                }
            }
            if (url.length() > 1) {
                if (url.indexOf(':') != -1) {
                    absolute = true;
                }
            }
        }
        if (!absolute) {
            return curDir + separator + url;
        }
        return url;
    }

    /**
     * serve nel caso in cui da windows ci si collega ad una risorsa condivisa
     * su Mac o Linux della forma /Volumes/dir
     * Siccome in Windows non è possibile montare risorse remote in una directory
     * dobbiamo trasformare artificiosamente il percorso sostituendo la stringa
     * contenuta nel file di configurazione con chiave "pref_old" con quella
     * con chiave "pref_new"
     * @param url
     * @return
     */
    private static String tranform(String url) {
        String retVal = url;
        String oldStr = CostantiDavide.confPers.getProperty("pref_old");
        String newStr = CostantiDavide.confPers.getProperty("pref_new");
        if (oldStr != null && newStr != null && url.indexOf(oldStr) == 0) {
            String tmp = url.replaceAll(oldStr, newStr);
            retVal = tmp.replace('/', '\\');
        }
        System.out.println("url modificato in: " + retVal);
        return retVal;
    }
}
