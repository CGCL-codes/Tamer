package games.strategy.engine.framework;

import games.strategy.engine.data.*;
import games.strategy.engine.framework.startup.ui.NewGameFileChooser;
import java.io.*;
import javax.swing.JFileChooser;
import org.xml.sax.SAXException;

/**
 * <p>
 * Title: TripleA
 * </p>
 * <p>
 * </p>
 * <p>
 * Copyright (c) 2002
 * </p>
 * <p>
 * </p>
 * 
 * @author Sean Bridges
 * 
 */
public class DefaultGameDataLoader implements IGameDataLoader {

    public GameData loadData() {
        NewGameFileChooser file = NewGameFileChooser.getInstance();
        int chooserRVal = file.showOpenDialog(null);
        if (!(chooserRVal == JFileChooser.APPROVE_OPTION)) System.exit(0);
        File xmlFile = file.getSelectedFile();
        InputStream xmlStream = null;
        try {
            xmlStream = new FileInputStream(xmlFile);
            xmlStream = new BufferedInputStream(xmlStream);
        } catch (IOException e) {
            System.err.println("Cannot open xml file:" + xmlFile.getPath());
            System.exit(0);
        }
        GameData data = null;
        try {
            System.out.print("Parsing XML game data");
            long now = System.currentTimeMillis();
            GameParser parser = new GameParser();
            data = parser.parse(xmlStream);
            System.out.println(" done:" + (((double) System.currentTimeMillis() - now) / 1000.0) + "s");
        } catch (GameParseException gpe) {
            gpe.printStackTrace();
            System.err.println("Error parsing xml:" + gpe.getMessage());
            System.exit(0);
        } catch (SAXException spe) {
            System.err.println("Error in xml file:" + spe.getMessage());
            System.exit(0);
        }
        try {
            if (xmlStream != null) xmlStream.close();
        } catch (IOException e) {
        }
        return data;
    }
}
