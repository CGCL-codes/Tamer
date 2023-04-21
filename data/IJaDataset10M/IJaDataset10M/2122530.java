package ndsromrenamer.file.tasks.handler;

import java.util.Vector;
import ndsromrenamer.data.entities.DatMetaDataDB;
import ndsromrenamer.data.entities.GameDB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author wuse
 */
public class AdvansceXMLHandler extends DefaultHandler {

    private static final Log log = LogFactory.getLog(AdvansceXMLHandler.class);

    private String tmpLine;

    private GameDB tmpGame;

    private DatMetaDataDB datMetaData;

    private Vector<GameDB> games = new Vector<GameDB>();

    private boolean sceneversion;

    private boolean escaped;

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) {
        if (qName.equals("game")) {
            this.tmpGame = new GameDB();
        } else if (qName.equalsIgnoreCase("romCRC")) {
            if (tmpGame != null) {
                tmpGame.setFileType(attributes.getValue(0));
            }
        } else if (qName.equals("configuration")) {
            this.datMetaData = new DatMetaDataDB();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String tmp = new String(ch, start, length);
        if (tmp.equals("&")) {
            tmpLine = tmpLine + "&";
            this.escaped = true;
        } else {
            if (this.escaped) {
                escaped = false;
                tmpLine = tmpLine + tmp;
            } else {
                tmpLine = new String(ch, start, length);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (this.tmpGame != null) {
            if (qName.equals("game")) {
                this.games.add(tmpGame);
                this.tmpGame = null;
            } else if (qName.equalsIgnoreCase("imageNumber")) {
                this.tmpGame.setImageNumber(Integer.parseInt(tmpLine));
            } else if (qName.equalsIgnoreCase("releaseNumber")) {
                this.tmpGame.setDatNumber(Integer.parseInt(tmpLine));
                if (sceneversion) {
                    this.tmpGame.setReleaseNumber(tmpLine);
                }
            } else if (qName.equalsIgnoreCase("title")) {
                this.tmpGame.setTitle(tmpLine);
            } else if (qName.equalsIgnoreCase("saveType")) {
                this.tmpGame.setSaveType(tmpLine);
            } else if (qName.equalsIgnoreCase("romSize")) {
                this.tmpGame.setRomsize(Double.parseDouble(tmpLine));
            } else if (qName.equalsIgnoreCase("publisher")) {
                this.tmpGame.setPublisher(tmpLine);
            } else if (qName.equalsIgnoreCase("location")) {
                this.tmpGame.setRegionCode(Integer.parseInt(tmpLine));
            } else if (qName.equalsIgnoreCase("language")) {
                this.tmpGame.setLanguageCode(Integer.parseInt(tmpLine));
            } else if (qName.equalsIgnoreCase("romCRC")) {
                this.tmpGame.setCrc(tmpLine);
            } else if (qName.equalsIgnoreCase("icoCRC")) {
                this.tmpGame.setIcocrc(tmpLine);
            } else if (qName.equalsIgnoreCase("nfoCRC")) {
                this.tmpGame.setInfocrc(tmpLine);
            } else if (qName.equalsIgnoreCase("dumpdate")) {
                this.tmpGame.setReleaseDate(tmpLine);
            } else if (qName.equalsIgnoreCase("wifi")) {
                if (tmpLine.equals("No")) {
                    this.tmpGame.setWifi(false);
                } else {
                    this.tmpGame.setWifi(true);
                }
            } else if (qName.equalsIgnoreCase("genre")) {
                this.tmpGame.setGenre(tmpLine);
            } else if (qName.equalsIgnoreCase("comment")) {
                if (!sceneversion) {
                    this.tmpGame.setReleaseNumber(tmpLine);
                }
            } else if (qName.equalsIgnoreCase("duplicateID")) {
                this.tmpGame.setDuplicateId(Integer.parseInt(tmpLine));
            } else if (qName.equalsIgnoreCase("sourceRom")) {
                this.tmpGame.setReleaseGroup(tmpLine);
            } else if (qName.equalsIgnoreCase("im1CRC")) {
                this.tmpGame.setIm1crc(tmpLine);
            } else if (qName.equalsIgnoreCase("im2CRC")) {
                this.tmpGame.setIm2crc(tmpLine);
            }
        } else if (datMetaData != null) {
            if (qName.equals("datName")) {
                if (tmpLine.contains("Scene")) {
                    sceneversion = true;
                }
                this.datMetaData.setName(tmpLine);
            } else if (qName.equals("datVersion")) {
                this.datMetaData.setDatVersion(tmpLine);
            } else if (qName.equals("system")) {
                this.datMetaData.setSystem(tmpLine);
            } else if (qName.equals("datVersionURL")) {
                this.datMetaData.setDatVersionURL(tmpLine);
            } else if (qName.equals("datURL")) {
                this.datMetaData.setDatURL(tmpLine);
            } else if (qName.equals("imURL")) {
                this.datMetaData.setImURL(tmpLine);
            }
        }
    }

    public DatMetaDataDB getDatMetaData() {
        return datMetaData;
    }

    public Vector<GameDB> getGames() {
        return games;
    }
}
