package gdi1sokoban.logic;

import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.Skin;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import gdi1sokoban.logic.Security;

/**
 * This class represents some data manipulations with Player Object
 * @author Stalker
 *
 */
public class PlayerManager extends IdentifierManager {

    private String _playerPath = "res" + File.separator + "player" + File.separator;

    private HashMap<Integer, LevelStatistic> _levelSetStatistic = null;

    private int _levelSetStatId = -1;

    private int _playerStatId = -1;

    private static PlayerManager _instance = null;

    private ArrayList<PlayerIdentifier> _playerIdentifiers = null;

    /**
	 * Get instance of PlayerManager
	 * @return PlayerManager instance
	 */
    public static PlayerManager getInstance() {
        if (_instance == null) _instance = new PlayerManager();
        return _instance;
    }

    /**
	 * Singleton constructor that  sets the path to players.xml
	 */
    private PlayerManager() {
        super("res" + File.separator + "player" + File.separator + "players.xml");
        _playerIdentifiers = new ArrayList<PlayerIdentifier>();
        for (IdentifierRecord i : _identifierRecords) {
            _playerIdentifiers.add(new PlayerIdentifier(i.getName(), i.getId(), i.getUri()));
        }
    }

    /**
	 * Get all player identifiers alphabeticaly sorted 
	 * @return IdentifierRecords of the players
	 */
    public ArrayList<PlayerIdentifier> getPlayerIdentifiers() {
        return _playerIdentifiers;
    }

    /**
	 * Add new player
	 * @param name player name
	 * @return new player id
	 * @throws DuplicateException there is already player with given name
	 */
    public int addPlayer(String name) throws DuplicateException {
        addIdentifierRecord(name, Player.DEFAULT_URI);
        for (int i = 0; i < _playerIdentifiers.size(); i++) {
            if ((name.compareTo(_playerIdentifiers.get(i).getName())) <= 0) {
                _playerIdentifiers.add(i, new PlayerIdentifier(name, this._lastId, Player.DEFAULT_URI));
                return this._lastId;
            }
        }
        _playerIdentifiers.add(new PlayerIdentifier(name, this._lastId, Player.DEFAULT_URI));
        return this._lastId;
    }

    public int addPlayer(int id, Player player) throws DuplicateException {
        addIdentifierRecord(id, player.getName(), Player.DEFAULT_URI);
        for (int i = 0; i < _playerIdentifiers.size(); i++) {
            if ((player.getName().compareTo(_playerIdentifiers.get(i).getName())) <= 0) {
                _playerIdentifiers.add(i, new PlayerIdentifier(player.getName(), this._lastId, player.getUri()));
                return this._lastId;
            }
        }
        _playerIdentifiers.add(new PlayerIdentifier(player.getName(), this._lastId, player.getUri()));
        return this._lastId;
    }

    public int addPlayer(Player player) throws DuplicateException {
        return addPlayer(this._lastId, player);
    }

    private void changePlayerIdentifier(PlayerIdentifier ident) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(_playerPath + "players.xml"));
            NodeList identNode = doc.getElementsByTagName("identifier");
            for (int i = 0; i < identNode.getLength(); i++) {
                Node tmp = identNode.item(i);
                NamedNodeMap attr = tmp.getAttributes();
                int currId = Integer.parseInt(attr.getNamedItem("id").getTextContent());
                if (currId == ident.getId()) {
                    attr.getNamedItem("name").setTextContent(ident.getName());
                    attr.getNamedItem("uri").setTextContent(ident.getUri());
                    break;
                }
            }
            OutputFormat format = new OutputFormat(doc);
            XMLSerializer ser = new XMLSerializer(new FileOutputStream(_playerPath + "players.xml"), format);
            ser.serialize(doc.getDocumentElement());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Change some data in existing player
	 * @param player player with new data
	 * @throws DuplicateException 
	 */
    public void setPlayer(Player player) {
        if (player == null) return;
        for (PlayerIdentifier i : _playerIdentifiers) {
            if (i.getId() == player.getId()) {
                if (!((i.getName().equals(player.getName())) && (i.getUri().equals(player.getUri())))) {
                    i.setName(player.getName());
                    i.setUri(player.getUri());
                    changePlayerIdentifier(i);
                    changeIdentifierRecord(player.getId(), player.getName(), player.getUri());
                }
                break;
            }
        }
        try {
            FileWriter writer = new FileWriter(_playerPath + player.getId() + File.separator + "player.xml");
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><player><skin id=\"" + player.getSkin().getId() + "\"/>" + "<currentLevelSet id=\"" + player.getCurrentLevelSetId() + "\"/>" + "<currentLevel id=\"" + player.getCurrentLevelId() + "\"/></player>";
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Gets player by id, if player with given id doesn't exists returns null
	 * @param id
	 * @return player, if player with given id doesn't exists returns null
	 * @throws IllegalFormatException 
	 */
    public Player getPlayer(int id) throws IllegalFormatException {
        PlayerIdentifier out = null;
        boolean noPlayer = true;
        for (PlayerIdentifier i : _playerIdentifiers) {
            if (i.getId() == id) {
                out = i;
                noPlayer = false;
                break;
            }
        }
        if (noPlayer) return null;
        int skinId = -1, level = -1, levelSet = -1;
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(_playerPath + id + File.separator + "player.xml"));
            NodeList skinNode = doc.getElementsByTagName("skin");
            for (int i = 0; i < skinNode.getLength(); i++) {
                Node tmp = skinNode.item(i);
                NamedNodeMap attr = tmp.getAttributes();
                skinId = Integer.parseInt(attr.getNamedItem("id").getTextContent());
                break;
            }
            NodeList currLvlSetNode = doc.getElementsByTagName("currentLevelSet");
            for (int i = 0; i < currLvlSetNode.getLength(); i++) {
                Node tmp = currLvlSetNode.item(i);
                NamedNodeMap attr = tmp.getAttributes();
                levelSet = Integer.parseInt(attr.getNamedItem("id").getTextContent());
                break;
            }
            NodeList currLvlNode = doc.getElementsByTagName("currentLevel");
            for (int i = 0; i < currLvlNode.getLength(); i++) {
                Node tmp = currLvlNode.item(i);
                NamedNodeMap attr = tmp.getAttributes();
                level = Integer.parseInt(attr.getNamedItem("id").getTextContent());
                break;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Skin skin = SkinManager.getInstance().getSkin(SkinManager.getInstance().getSkinIdentifier(skinId));
        return new Player(out, skin, levelSet, level);
    }

    /**
	 * Delete Player  
	 * @param id player id
	 */
    public void delPlayer(int id) {
        delIdentifierRecord(id);
        boolean noPlayer = true;
        for (PlayerIdentifier i : _playerIdentifiers) {
            if (i.getId() == id) {
                _playerIdentifiers.remove(i);
                noPlayer = false;
                break;
            }
        }
        if (noPlayer) return;
        LevelSetManager man = LevelSetManager.getInstance();
        man.removeHighscore(id);
    }

    /**
	 * Creates new player on the hard drive		
	 */
    protected void createIdentifierFile() {
        String dirPass = this._pathToIdentifierFile.substring(0, this._pathToIdentifierFile.indexOf("players.xml"));
        File f = new File(dirPass + this._lastId);
        f.mkdir();
        File player = new File(dirPass + this._lastId + File.separator + "player.xml");
        try {
            player.createNewFile();
            FileWriter writer = new FileWriter(player);
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><player><skin id=\"0\"/>" + "<currentLevelSet id=\"0\"/>" + "<currentLevel id=\"1\"/></player>";
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Deletes player from the hard drive
	 */
    protected void deleteIdentifierFile(IdentifierRecord delRecord) {
        String dirPass = this._pathToIdentifierFile.substring(0, this._pathToIdentifierFile.indexOf("players.xml"));
        File f = new File(dirPass + delRecord.getId());
        deleteDirectory(f);
    }

    /**
	 * Gets statistic for given Level, LevelSet and Player
	 * @param levelSetId id of level set
	 * @param playerId player id
	 * @param levelId level id
	 * @return statistic for level
	 */
    public LevelStatistic getLevelStatistic(int levelSetId, int playerId, int levelId) {
        if ((_levelSetStatistic == null) || ((levelSetId != _levelSetStatId) && (playerId != _playerStatId))) {
            _levelSetStatistic = getLevelSetStatistic(levelSetId, playerId);
        }
        return _levelSetStatistic.get(levelId);
    }

    /**
	 * Gets map of level statistic for given level set and player
	 * @param levelSetId level set id
	 * @param playerId player id
	 * @return list level statistic for level set
	 */
    public HashMap<Integer, LevelStatistic> getLevelSetStatistic(int levelSetId, int playerId) {
        _levelSetStatId = levelSetId;
        _playerStatId = playerId;
        HashMap<Integer, LevelStatistic> levelStatistics = new HashMap<Integer, LevelStatistic>();
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(_playerPath + playerId + File.separator + levelSetId + File.separator + "levelSetInfo.xml"));
            NodeList infoNodeList = doc.getElementsByTagName("level");
            for (int i = 0; i < infoNodeList.getLength(); i++) {
                Node tmp = infoNodeList.item(i);
                NamedNodeMap attr = tmp.getAttributes();
                boolean saved = false;
                if (attr.getNamedItem("isSaved").getTextContent().equals("true")) saved = true;
                boolean won = false;
                if (attr.getNamedItem("isWon").getTextContent().equals("true")) won = true;
                LevelStatistic lvlStat = new LevelStatistic(Integer.parseInt(attr.getNamedItem("score").getTextContent()), Long.parseLong(attr.getNamedItem("time").getTextContent()), saved, won);
                levelStatistics.put(Integer.parseInt(attr.getNamedItem("id").getTextContent()), lvlStat);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return levelStatistics;
        }
        return levelStatistics;
    }

    /**
	 * Get name of the player by given id
	 * @param id player id
	 * @return player name
	 */
    public String getPlayerNameById(int id) {
        for (IdentifierRecord i : _identifierRecords) {
            if (i.getId() == id) {
                return i.getName();
            }
        }
        return null;
    }

    /**
	 * Add level statistic
	 * @param levelStat level statistic
	 * @param levelId level id
	 * @param levelSetId level set id
	 * @param playerId player id
	 */
    public void addLevelStatistic(Player p, LevelStatistic levelStat, int levelId, int levelSetId, int playerId) {
        String dirPath = _playerPath + playerId + File.separator + levelSetId + File.separator;
        String xmlPath = dirPath + "levelSetInfo.xml";
        File exists = new File(xmlPath);
        if (!exists.exists()) createLevelSetInfoFile(dirPath);
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(xmlPath));
            NodeList levelList = doc.getElementsByTagName("level");
            Node first = doc.getFirstChild();
            for (int i = 0; i < levelList.getLength(); i++) {
                Node tmp = levelList.item(i);
                NamedNodeMap attr = tmp.getAttributes();
                int currLvlId = Integer.parseInt(attr.getNamedItem("id").getTextContent());
                if (currLvlId == levelId) {
                    first.removeChild(tmp);
                    break;
                }
            }
            Element setElement = doc.createElement("level");
            setElement.setAttribute("id", Integer.toString(levelId));
            setElement.setAttribute("score", Integer.toString(levelStat.getMoves()));
            setElement.setAttribute("time", Long.toString(levelStat.getTime()));
            setElement.setAttribute("isSaved", Boolean.toString(levelStat.isSaved()));
            setElement.setAttribute("isWon", Boolean.toString(levelStat.isWon()));
            first.appendChild(setElement);
            OutputFormat format = new OutputFormat(doc);
            XMLSerializer ser = new XMLSerializer(new FileOutputStream(xmlPath), format);
            ser.serialize(doc.getDocumentElement());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void createLevelSetInfoFile(String path) {
        try {
            File dir = new File(path);
            dir.mkdirs();
            String xmlFile = path + "levelSetInfo.xml";
            FileWriter writer = new FileWriter(xmlFile);
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><levelsetInfo></levelsetInfo>";
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Get saved game from *.soc file
	 * @param levelSetId level set id
	 * @param levelId level id
	 * @param playerId player id
	 * @return Savegame
	 * @throws IOException
	 */
    public Savegame getSavegame(int levelSetId, int levelId, int playerId) throws IOException {
        String path = _playerPath + playerId + File.separator + levelSetId + File.separator + levelId + ".soc";
        return PlayerManager.getSavegame(path);
    }

    protected void addLastPlayedLevel(Player p, int levelSetId, int playerId, int levelId) {
        p.setCurrentLevelId(levelId);
        p.setCurrentLevelSetId(levelSetId);
        try {
            String path = _playerPath + playerId + File.separator + "player.xml";
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(path));
            NodeList levelSetList = doc.getElementsByTagName("currentLevelSet");
            Node first = doc.getFirstChild();
            for (int i = 0; i < levelSetList.getLength(); i++) {
                Node tmp = levelSetList.item(i);
                first.removeChild(tmp);
                Element setElement = doc.createElement("currentLevelSet");
                setElement.setAttribute("id", Integer.toString(levelSetId));
                first.appendChild(setElement);
                break;
            }
            NodeList levelList = doc.getElementsByTagName("currentLevel");
            for (int i = 0; i < levelList.getLength(); i++) {
                Node tmp = levelList.item(i);
                first.removeChild(tmp);
                Element setElement = doc.createElement("currentLevel");
                setElement.setAttribute("id", Integer.toString(levelId));
                first.appendChild(setElement);
                break;
            }
            OutputFormat format = new OutputFormat(doc);
            XMLSerializer ser = new XMLSerializer(new FileOutputStream(path), format);
            ser.serialize(doc.getDocumentElement());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Savegame
	 * @param levelSetId level set id
	 * @param levelId level id
	 * @param playerId player id
	 * @param saveGame savegame to save
	 * @param won is won
	 */
    public void setSavegame(Player p, int levelSetId, int levelId, int playerId, Savegame saveGame) {
        String dirPath = _playerPath + playerId + File.separator + levelSetId + File.separator;
        String filePath = dirPath + levelId + ".soc";
        File chkDir = new File(dirPath);
        if (!chkDir.exists()) {
            chkDir.mkdirs();
        }
        PlayerManager.setSavegame(filePath, saveGame);
        addLastPlayedLevel(p, levelSetId, playerId, levelId);
    }

    public static void setSavegame(String path, Savegame saveGame) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objstream = new ObjectOutputStream(out);
            objstream.writeObject(saveGame);
            objstream.close();
            FileOutputStream wr = new FileOutputStream(path);
            wr.write(out.toByteArray());
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Savegame getSavegame(String path) throws IOException {
        File file = new File(path);
        byte[] out = null;
        try {
            FileInputStream in = new FileInputStream(file);
            out = new byte[(int) file.length()];
            in.read(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream input = new ByteArrayInputStream(out);
        ObjectInputStream objstream = new ObjectInputStream(input);
        Object object = null;
        try {
            object = objstream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        objstream.close();
        return (Savegame) object;
    }
}
