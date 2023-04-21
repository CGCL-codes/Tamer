package laborki03_szablon;

import java.rmi.*;
import java.text.*;
import java.util.*;

public class Klient {

    public static void main(String[] s) {
        int registryPort = 8000;
        InterfejsSerwera server = null;
        try {
            System.setSecurityManager(new RMISecurityManager());
            server = (InterfejsSerwera) Naming.lookup("//localhost:" + registryPort + "/Serwer");
            String nazwa = server.wezNazwe();
            System.out.println("Laczenie z serwerem " + nazwa + " przebieglo pomyslnie :}\n");
            long czas = server.wezCzas();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            StringBuffer data = new StringBuffer();
            System.out.println("Czas na serwerze: " + df.format(new Date(czas), data, new FieldPosition(0)) + "\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
