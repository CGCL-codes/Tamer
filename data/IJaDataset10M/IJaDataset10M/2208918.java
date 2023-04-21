package coopnetclient.utils.gamedatabase;

import coopnetclient.enums.LogTypes;
import coopnetclient.frames.FrameOrganizer;
import coopnetclient.utils.Logger;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class XMLReader {

    public static final int LOAD_GAMEDATA = 1;

    public static final int DETECT_GAMES = 2;

    private String loadFrom;

    public XMLReader() {
        super();
    }

    public void parseGameData(String gamename, String datafilepath, int action) throws ParserConfigurationException, SAXException, IOException {
        loadFrom = datafilepath;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        final SAXParser sp = spf.newSAXParser();
        switch(action) {
            case DETECT_GAMES:
                new Thread() {

                    @Override
                    public void run() {
                        try {
                            Logger.log(LogTypes.LOG, "Detecting games");
                            sp.parse(new File(loadFrom), new XmlHandler_DetectGames());
                            Logger.log(LogTypes.LOG, "Done detecting games");
                            if (FrameOrganizer.getClientFrame() != null) {
                                FrameOrganizer.getClientFrame().refreshInstalledGames();
                            }
                        } catch (Exception e) {
                            Logger.log(LogTypes.ERROR, "Game detection failed!");
                            Logger.log(e);
                        }
                    }
                }.start();
                break;
            case LOAD_GAMEDATA:
                sp.parse(new File(loadFrom), new XmlHandler_LoadGameData(gamename));
                break;
        }
    }
}
