package eu.irreality.age;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import eu.irreality.age.filemanagement.Paths;
import eu.irreality.age.filemanagement.URLUtils;
import eu.irreality.age.i18n.UIMessages;

public class GameInfo implements Serializable {

    private String[] theInfo;

    private String f = null;

    static Vector allInstances = new Vector();

    public boolean equals(Object obj) {
        if (!(obj instanceof GameInfo)) return false; else {
            return ((f == null ? ((GameInfo) obj).f == null : f.equals(((GameInfo) obj).f)) && ((theInfo == null) ? (((GameInfo) obj).theInfo == null) : Arrays.equals(theInfo, ((GameInfo) obj).theInfo)));
        }
    }

    public static GameInfo getGameInfoFromFile(String f) {
        for (int i = 0; i < allInstances.size(); i++) {
            if (((GameInfo) allInstances.elementAt(i)).getFile() != null && ((GameInfo) allInstances.elementAt(i)).getFile().equals(f)) {
                return ((GameInfo) allInstances.elementAt(i));
            }
        }
        try {
            return GameInfo.getGameInfo(f);
        } catch (Exception e) {
            return null;
        }
    }

    public GameInfo(String[] info, String f) {
        theInfo = info;
        this.f = f;
        allInstances.add(this);
    }

    public GameInfo() {
        theInfo = new String[5];
        for (int i = 0; i < 5; i++) theInfo[i] = "";
        allInstances.add(this);
    }

    public String getFile() {
        return f;
    }

    public boolean isValid() {
        return (f != null);
    }

    public String getName() {
        return theInfo[0];
    }

    public String getAuthor() {
        return theInfo[1];
    }

    public String getDate() {
        return theInfo[3];
    }

    public String getVersion() {
        return theInfo[2];
    }

    public String getAGEVersion() {
        return theInfo[4];
    }

    public String toString() {
        return theInfo[0] + " " + theInfo[2];
    }

    public String toLongString() {
        return UIMessages.getInstance().getMessage("gameinfo.name") + " " + getName() + "\n" + UIMessages.getInstance().getMessage("gameinfo.author") + " " + getAuthor() + "\n" + UIMessages.getInstance().getMessage("gameinfo.date") + " " + getDate() + "\n" + UIMessages.getInstance().getMessage("gameinfo.version") + " " + getVersion() + "\n" + UIMessages.getInstance().getMessage("gameinfo.required") + " " + getAGEVersion() + "\n" + UIMessages.getInstance().getMessage("gameinfo.file") + " " + getFile();
    }

    public static GameInfo getGameInfo(String modulefile) throws FileNotFoundException, IOException {
        System.out.println("getGameInfo called on " + modulefile);
        String linea = "";
        String token = "";
        String[] moduleInfo = { "?", "?", "?", "?", "?" };
        boolean useAlternativeFile = false;
        if ((modulefile.toLowerCase().endsWith(".xml") || modulefile.toLowerCase().endsWith(".asf"))) {
            File resFile = null;
            if (new File(modulefile).exists()) resFile = new File(new File(modulefile).getAbsolutePath().substring(0, new File(modulefile).getAbsolutePath().length() - 4) + ".res");
            if (resFile != null && resFile.exists()) {
                System.out.println("RES file exists.\n");
                useAlternativeFile = true;
            } else {
                System.out.println("RES file doesn't exist.\n");
                org.w3c.dom.Document d = null;
                try {
                    InputStream str = URLUtils.openFileOrURL(modulefile);
                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    d = db.parse(str);
                } catch (FileNotFoundException fnfe) {
                    System.out.println("[Exception: The game from which this savefile comes could have been deleted or moved]");
                    fnfe.printStackTrace();
                    throw (fnfe);
                } catch (ParserConfigurationException pce) {
                    System.out.println(pce);
                } catch (SAXException se) {
                    System.out.println(se);
                } catch (IOException ioe) {
                    System.out.println("RES file, nay2.");
                    ioe.printStackTrace();
                    throw (ioe);
                }
                org.w3c.dom.Element n = d.getDocumentElement();
                if (n.hasAttribute("moduleName")) moduleInfo[0] = n.getAttribute("moduleName");
                if (n.hasAttribute("author")) moduleInfo[1] = n.getAttribute("author");
                if (n.hasAttribute("version")) moduleInfo[2] = n.getAttribute("version");
                if (n.hasAttribute("date")) moduleInfo[3] = n.getAttribute("date");
                if (n.hasAttribute("parserVersion")) moduleInfo[4] = n.getAttribute("parserVersion");
                if (resFile != null) {
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(resFile), "UTF-8"));
                    pw.println("comment Fichero resumen de informaci�n de mundo generado por Aetheria Game Engine el " + java.text.DateFormat.getDateTimeInstance().format(new Date()) + " a partir de " + modulefile);
                    pw.println("modulename " + moduleInfo[0]);
                    pw.println("author " + moduleInfo[1]);
                    pw.println("version " + moduleInfo[2]);
                    pw.println("date " + moduleInfo[3]);
                    pw.println("parserversion " + moduleInfo[4]);
                    pw.flush();
                    pw.close();
                    System.out.println("Print Writer closed");
                }
                return new GameInfo(moduleInfo, modulefile);
            }
        }
        FileInputStream fp = null;
        java.io.BufferedReader filein = null;
        if (useAlternativeFile) {
            File resFile = new File(new File(modulefile).getAbsolutePath().substring(0, new File(modulefile).getAbsolutePath().length() - 4) + ".res");
            fp = new FileInputStream(resFile);
            filein = new java.io.BufferedReader(new java.io.InputStreamReader(fp, "UTF-8"));
        } else {
            fp = new FileInputStream(modulefile);
            filein = new java.io.BufferedReader(new java.io.InputStreamReader(fp, "UTF-8"));
        }
        while (true) {
            linea = filein.readLine();
            if (linea == null) break;
            token = StringMethods.getTok(linea, 1, ' ');
            if (token.equalsIgnoreCase("modulename")) moduleInfo[0] = StringMethods.getToks(linea, 2, StringMethods.numToks(linea, ' '), ' '); else if (token.equalsIgnoreCase("author")) moduleInfo[1] = StringMethods.getToks(linea, 2, StringMethods.numToks(linea, ' '), ' '); else if (token.equalsIgnoreCase("version")) moduleInfo[2] = StringMethods.getToks(linea, 2, StringMethods.numToks(linea, ' '), ' '); else if (token.equalsIgnoreCase("date")) moduleInfo[3] = StringMethods.getToks(linea, 2, StringMethods.numToks(linea, ' '), ' '); else if (token.equalsIgnoreCase("parserversion")) moduleInfo[4] = StringMethods.getToks(linea, 2, StringMethods.numToks(linea, ' '), ' '); else if (token.equalsIgnoreCase("begin_eva_code")) {
                boolean terminamos = false;
                while (!terminamos) {
                    linea = filein.readLine();
                    String id_linea = StringMethods.getTok(linea, 1, ' ');
                    if (id_linea.equalsIgnoreCase("end_eva_code")) terminamos = true; else {
                        ;
                    }
                }
            } else if (token.equalsIgnoreCase("begin_bsh_code")) {
                boolean terminamos = false;
                while (!terminamos) {
                    linea = filein.readLine();
                    String id_linea = StringMethods.getTok(linea, 1, ' ');
                    if (id_linea.equalsIgnoreCase("end_bsh_code")) terminamos = true; else {
                        ;
                    }
                }
            }
        }
        return new GameInfo(moduleInfo, modulefile);
    }

    public static GameInfo[] getListOfGames() {
        System.out.println("getListOfGames() called\n");
        File cwd = new File(Paths.getWorkingDirectory());
        File worldsDirectory = new File(cwd.getAbsolutePath() + File.separatorChar + Paths.WORLD_PATH);
        if (!worldsDirectory.exists()) {
            if (worldsDirectory.mkdir()) {
                System.out.println("Worlds directory didn't exist, created at " + Paths.WORLD_PATH);
            } else {
                System.err.println("Could not create worlds directory at " + Paths.WORLD_PATH);
            }
        }
        File[] worldsSubdirectories = worldsDirectory.listFiles();
        Vector result = new Vector();
        for (int i = 0; i < worldsSubdirectories.length; i++) {
            if (worldsSubdirectories[i].isDirectory()) {
                File[] fl2 = worldsSubdirectories[i].listFiles();
                for (int j = 0; j < fl2.length; j++) {
                    if (fl2[j].getName().equalsIgnoreCase("world.dat") || fl2[j].getName().equalsIgnoreCase("world.xml")) {
                        try {
                            result.addElement(getGameInfo(fl2[j].getAbsolutePath()));
                        } catch (IOException ioe) {
                            System.out.println(ioe);
                            ioe.printStackTrace();
                        }
                    }
                }
            }
        }
        Object[] objetos = result.toArray();
        GameInfo[] ficheros = new GameInfo[objetos.length];
        for (int i = 0; i < objetos.length; i++) ficheros[i] = (GameInfo) objetos[i];
        return (ficheros);
    }
}
