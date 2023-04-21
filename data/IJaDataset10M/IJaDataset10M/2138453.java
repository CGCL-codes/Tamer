package Tot_PSE_Com;

import java.io.*;
import java.net.*;
import java.util.*;

public class Ftpkliens {

    private PrintWriter w = null;

    private BufferedReader r = null;

    private Socket sock = null;

    private boolean bin = false;

    private InetAddress cim = null;

    private String cimarg = new String();

    private CommanderMainFrame cmd = null;

    public void kapcsInit(String user, String pass, String celcim, String port) {
        try {
            cimarg = celcim;
            InetAddress cim = InetAddress.getByName(celcim);
            sock = new Socket(cim, Integer.parseInt(port));
            w = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            r = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String sor = new String();
            sor = r.readLine();
            w.println("USER " + user);
            w.flush();
            sor = r.readLine();
            w.println("PASS " + pass);
            w.flush();
            sor = r.readLine();
            w.println("STAT");
            w.flush();
            sor = r.readLine();
            sor = " ";
            while (!sor.startsWith("211")) {
                sor = r.readLine();
            }
        } catch (IOException e) {
            System.err.println(e);
            kapcsBont();
            System.exit(1);
        }
    }

    public void kapcsBont() {
        try {
            w.println("QUIT");
            w.flush();
            r.close();
            w.close();
            sock.close();
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public void ujMappa(String nev) {
        try {
            String sor;
            w.println("MKD " + nev);
            w.flush();
            sor = r.readLine();
            if (!sor.startsWith("2")) System.err.println("hiba a mappa létrehozásakor");
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public void mappaTorol(String nev) {
        try {
            String sor = new String();
            w.println("RMD " + nev);
            w.flush();
            sor = r.readLine();
            if (!sor.startsWith("2")) System.err.println("hiba a mappa törlésekor");
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    private Socket passive() {
        String sor;
        try {
            w.println("PASV ");
            w.flush();
            sor = r.readLine();
            StringTokenizer st = new StringTokenizer(sor);
            String[] result = sor.split(",");
            String[] spec = result[0].split("\\(");
            int[] x = new int[6];
            x[0] = Integer.parseInt(spec[1]);
            for (int i = 1; i < 5; i++) {
                x[i] = Integer.parseInt(result[i]);
            }
            spec = result[5].split("\\)");
            x[5] = Integer.parseInt(spec[0]);
            String szovegcim = new String(new Integer(x[0]).toString());
            for (int i = 1; i < 4; i++) {
                szovegcim = new String("." + new Integer(x[i]).toString());
            }
            InetAddress cim2 = InetAddress.getByName(cimarg);
            int port = x[4] * 256 + x[5];
            Socket sock2 = new Socket(cim2, port);
            result = null;
            spec = null;
            return sock2;
        } catch (Exception e) {
            System.err.print("passiv sz�tsz�llt");
            System.err.print(e);
            kapcsBont();
            System.exit(1);
            return (new Socket());
        }
    }

    public void cd(String mappa) {
        try {
            String sor;
            w.println("CWD " + mappa);
            w.flush();
            sor = r.readLine();
            list();
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public void cdup() {
        try {
            String sor;
            w.println("CDUP");
            w.flush();
            sor = r.readLine();
            list();
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    public String[] list() {
        String sor;
        try {
            if (bin) {
                type('A');
                bin = false;
            }
            Socket ujsock = passive();
            BufferedReader uj = new BufferedReader(new InputStreamReader(ujsock.getInputStream()));
            w.println("LIST ");
            w.flush();
            sor = r.readLine();
            ArrayList<String> listemp = new ArrayList<String>();
            String[] splitres = null;
            listemp.add("..");
            while ((sor = uj.readLine()) != null) {
                splitres = sor.split("\\s");
                if (splitres[0].startsWith("d")) listemp.add("[dir]    " + splitres[splitres.length - 1]); else listemp.add(splitres[splitres.length - 1]);
            }
            ujsock.close();
            sor = r.readLine();
            listemp.trimToSize();
            int meret = listemp.size();
            String[] lista = new String[meret];
            Iterator a = listemp.iterator();
            int i = 0;
            while (a.hasNext()) {
                lista[i] = (String) a.next();
                i++;
            }
            return lista;
        } catch (Exception ukhe) {
            System.err.println(ukhe);
            return (new String[0]);
        }
    }

    public void letolt(String file, String hova) {
        try {
            if (!(bin)) {
                type('I');
                bin = true;
            }
            Socket ujsock = passive();
            InputStream uj = ujsock.getInputStream();
            FileOutputStream cucc = new FileOutputStream(new File(hova + "\\" + file));
            String sor;
            w.println("RETR " + file);
            w.flush();
            sor = r.readLine();
            byte b[] = new byte[4096];
            int ln = 0;
            while ((ln = uj.read(b)) > -1) {
                cucc.write(b, 0, ln);
                cucc.flush();
            }
            cucc.close();
            ujsock.close();
            sor = r.readLine();
        } catch (Exception e) {
            System.err.print(e);
            System.exit(1);
        }
    }

    private void type(char tipus) {
        try {
            String sor;
            w.println("TYPE " + tipus);
            w.flush();
            sor = r.readLine();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void store(String honnan, String mit) {
        try {
            if (!(bin)) {
                type('I');
                bin = true;
            }
            Socket ujsock = passive();
            String keszfile = new String(honnan + mit);
            keszfile = keszfile.replace('\\', '/');
            FileInputStream cucc = new FileInputStream(new File("D:/Thumbs.db", "r"));
            byte[] b = new byte[4096];
            int ln = 0;
            ln = cucc.read(b);
            String sor;
            w.println("STOR " + mit);
            w.flush();
            sor = r.readLine();
            while ((ln = cucc.read(b)) > -1) {
                ujsock.getOutputStream().write(b, 0, ln);
            }
            ujsock.getOutputStream().flush();
            ujsock.getOutputStream().close();
            ujsock.close();
            sor = r.readLine();
        } catch (Exception e) {
            System.err.print(e);
            System.exit(1);
        }
    }

    public void del(String file) {
        String sor;
        try {
            w.println("DELE " + file);
            w.flush();
            sor = r.readLine();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void abort() {
        String sor;
        try {
            w.println("ABOR");
            w.flush();
            sor = r.readLine();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
