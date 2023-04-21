package openwar.DB;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import openwar.Main;
import openwar.world.WorldDecoration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLDataLoader {

    AssetManager assets;

    Main game;

    private static final Logger logger = Logger.getLogger(XMLDataLoader.class.getName());

    public XMLDataLoader(Main appl) {
        game = appl;
        assets = appl.getAssetManager();
    }

    private boolean loadDecorations(Element root) {
        Spatial entity;
        String refname = null;
        try {
            NodeList nodes = root.getElementsByTagName("decoration");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element l = (Element) nodes.item(i);
                refname = l.getAttribute("refname");
                entity = assets.loadModel("decorations" + File.separator + l.getAttribute("file"));
                Main.DB.decorations.put(refname, entity);
                logger.log(Level.WARNING, "*Decoration loaded: {0} *", refname);
            }
            return true;
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Decoration CANNOT be loaded: {0}", refname);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }

    private boolean loadCulture(Element root) {
        Culture c = null;
        try {
            Element cul = (Element) root.getElementsByTagName("culture").item(0);
            c = new Culture(cul.getAttribute("name"), cul.getAttribute("refname"));
            Main.DB.cultures.put(c.refName, c);
            Element sett = (Element) root.getElementsByTagName("settlement").item(0);
            NodeList nodes = sett.getElementsByTagName("model");
            for (int i = 0; i < nodes.getLength(); i++) {
                int l = Integer.parseInt(((Element) nodes.item(i)).getAttribute("level"));
                c.settlementModels.put(l, ((Element) nodes.item(i)).getAttribute("refname"));
            }
            Element dock = (Element) root.getElementsByTagName("dock").item(0);
            nodes = dock.getElementsByTagName("model");
            for (int i = 0; i < nodes.getLength(); i++) {
                int l = Integer.parseInt(((Element) nodes.item(i)).getAttribute("level"));
                c.dockModels.put(l, ((Element) nodes.item(i)).getAttribute("refname"));
            }
            Element army = (Element) root.getElementsByTagName("army").item(0);
            c.armyModel = army.getAttribute("refname");
            Element fleet = (Element) root.getElementsByTagName("fleet").item(0);
            c.fleetModel = fleet.getAttribute("refname");
            logger.log(Level.WARNING, "*Culture loaded: {0} *", c.refName);
            return true;
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Culture CANNOT be loaded: {0}", c.refName);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }

    private boolean loadSounds(Element root) {
        AudioNode entity = new AudioNode();
        String refname = null;
        String file = null;
        try {
            NodeList nodes = root.getElementsByTagName("sound");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element l = (Element) nodes.item(i);
                refname = l.getAttribute("refname");
                file = l.getAttribute("file");
                entity = new AudioNode(assets, "sounds" + File.separator + file, false);
                Main.DB.soundNodes.put(refname, entity);
                logger.log(Level.WARNING, "*Sound loaded: {0} *", refname);
            }
            return true;
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Sound CANNOT be loaded: {0}", refname);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }

    private boolean loadMusic(Element root) {
        AudioNode entity = new AudioNode();
        String refname = null;
        String file = null;
        String mode = null;
        try {
            NodeList nodes = root.getElementsByTagName("music");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element l = (Element) nodes.item(i);
                refname = l.getAttribute("refname");
                file = l.getAttribute("file");
                mode = l.getAttribute("mode");
                entity = new AudioNode(assets, "music" + File.separator + file, true, true);
                Main.DB.musicNodes.put(refname, entity);
                if ("menu".equals(mode)) {
                    game.audioState.menu.add(refname);
                } else if ("loading".equals(mode)) {
                    game.audioState.loading.add(refname);
                } else if ("idle".equals(mode)) {
                    game.audioState.worldMapIdle.add(refname);
                }
                logger.log(Level.WARNING, "*Music loaded: {0} *", refname);
            }
            return true;
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Music CANNOT be loaded: {0}", refname);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }

    private boolean loadModels(Element root) {
        String refname = null, file;
        try {
            NodeList nodes = root.getElementsByTagName("model");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element l = (Element) nodes.item(i);
                refname = l.getAttribute("refname");
                file = l.getAttribute("file");
                Model m = new Model(refname, file, l.getAttribute("diffuse"));
                Main.DB.models.put(refname, m);
                Scanner s = new Scanner(l.getAttribute("scale"));
                s.useLocale(Locale.ENGLISH);
                m.scale = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                logger.log(Level.WARNING, "*Model loaded: {0} *", refname);
            }
            return true;
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Model CANNOT be loaded: {0}", refname);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }

    private boolean loadData(String folder) {
        try {
            File f = new File(game.locatorRoot + folder);
            if (!f.isDirectory()) {
                logger.log(Level.SEVERE, "Cannot find {0} directory...", folder);
                throw new Exception();
            }
            if ("scripts".equals(folder)) {
                for (File l : f.listFiles()) {
                    if (l.isFile()) {
                        Main.scriptEngine.eval(new FileReader(game.locatorRoot + f.getName() + File.separator + l.getName()));
                        logger.log(Level.WARNING, "*Script loaded: {0} *", l.getName());
                    }
                }
                return true;
            }
            if ("map".equals(folder)) {
                File props = null;
                for (File l : f.listFiles()) {
                    if ("props.xml".equals(l.getName())) {
                        props = l;
                    }
                }
                if (props == null) {
                    logger.log(Level.WARNING, "Cannot find props.xml in {0}", f.getName());
                    return false;
                }
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(props.getCanonicalPath());
                return loadMap(dom.getDocumentElement());
            }
            if ("sounds".equals(folder) || "decorations".equals(folder) || "music".equals(folder) || "models".equals(folder)) {
                File props = null;
                for (File l : f.listFiles()) {
                    if ("props.xml".equals(l.getName())) {
                        props = l;
                    }
                }
                if (props == null) {
                    logger.log(Level.WARNING, "Cannot find props.xml in {0}", f.getName());
                    return false;
                }
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(props.getCanonicalPath());
                if ("sounds".equals(folder)) {
                    return loadSounds(dom.getDocumentElement());
                } else if ("decorations".equals(folder)) {
                    return loadDecorations(dom.getDocumentElement());
                } else if ("models".equals(folder)) {
                    return loadModels(dom.getDocumentElement());
                } else {
                    return loadMusic(dom.getDocumentElement());
                }
            }
            for (File u : f.listFiles()) {
                if (!u.isDirectory()) {
                    logger.log(Level.WARNING, "Entity unloadable in {0}", f.getName());
                    continue;
                }
                if (".svn".equals(u.getName())) {
                    continue;
                }
                File props = null;
                for (File l : u.listFiles()) {
                    if ("props.xml".equals(l.getName())) {
                        props = l;
                    }
                }
                if (props == null) {
                    logger.log(Level.WARNING, "Cannot find props.xml in {0}", f.getName() + "/" + u.getName());
                    continue;
                }
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(props.getCanonicalPath());
                Element root = dom.getDocumentElement();
                boolean result = true;
                if ("units".equals(folder)) {
                    result = loadUnit(root);
                } else if ("buildings".equals(folder)) {
                    result = loadBuilding(root);
                } else if ("factions".equals(folder)) {
                    result = loadFaction(root);
                } else if ("cultures".equals(folder)) {
                    result = loadCulture(root);
                }
                if (!result) {
                    return false;
                }
            }
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Error while reading {0} data...", folder);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
        return true;
    }

    private boolean loadUnit(Element root) {
        GenericUnit entity = new GenericUnit();
        try {
            Element unit = (Element) root.getElementsByTagName("unit").item(0);
            Element d = (Element) root.getElementsByTagName("description").item(0);
            Element stats = (Element) root.getElementsByTagName("stats").item(0);
            entity.name = unit.getAttribute("name");
            entity.refName = unit.getAttribute("refname");
            entity.maxCount = Integer.parseInt(unit.getAttribute("maxcount"));
            entity.maxMovePoints = Integer.parseInt(unit.getAttribute("maxmovepoints"));
            entity.turnsToRecruit = Integer.parseInt(unit.getAttribute("turnstorecruit"));
            entity.cost = Integer.parseInt(unit.getAttribute("cost"));
            entity.upkeep = Integer.parseInt(unit.getAttribute("upkeep"));
            entity.walks = Boolean.parseBoolean(unit.getAttribute("walks"));
            entity.sails = Boolean.parseBoolean(unit.getAttribute("sails"));
            entity.cargo = Boolean.parseBoolean(unit.getAttribute("cargo"));
            Description desc = new Description();
            String image = "units" + File.separator + entity.refName + File.separator + d.getAttribute("card");
            desc.card = (Texture2D) assets.loadTexture(image);
            desc.info = d.getAttribute("card");
            entity.desc = desc;
            Main.DB.genUnits.put(entity.refName, entity);
            logger.log(Level.WARNING, "*Unit loaded: {0} *", entity.refName);
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Unit CANNOT be loaded: {0}", entity.refName);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
        return true;
    }

    private boolean loadBuilding(Element root) {
        GenericBuilding entity = new GenericBuilding();
        try {
            Element building = (Element) root.getElementsByTagName("building").item(0);
            NodeList requires = building.getElementsByTagName("requires");
            for (int i = 0; i < requires.getLength(); i++) {
                Element l = (Element) requires.item(i);
                entity.requires.put(l.getAttribute("name"), l.getAttribute("value"));
            }
            NodeList levels = root.getElementsByTagName("level");
            entity.name = building.getAttribute("name");
            entity.refName = building.getAttribute("refname");
            entity.maxLevel = Integer.parseInt(building.getAttribute("maxlevel"));
            for (int i = 0; i <= entity.maxLevel; i++) {
                Element l = (Element) levels.item(i);
                Element d = (Element) l.getElementsByTagName("description").item(0);
                NodeList req = l.getElementsByTagName("requires");
                NodeList prov = l.getElementsByTagName("provides");
                String s = "buildings" + File.separator + entity.refName + File.separator;
                Description desc = new Description();
                desc.card = (Texture2D) assets.loadTexture(s + d.getAttribute("card"));
                entity.addLevel(Integer.parseInt(l.getAttribute("level")), l.getAttribute("name"), l.getAttribute("refname"), Integer.parseInt(l.getAttribute("cost")), Integer.parseInt(l.getAttribute("upkeep")), Integer.parseInt(l.getAttribute("turns")), desc);
                for (int j = 0; j < req.getLength(); j++) {
                    Element el = (Element) req.item(j);
                    entity.levels.get(i).requires.put(el.getAttribute("name"), el.getAttribute("value"));
                }
                for (int j = 0; j < prov.getLength(); j++) {
                    Element el = (Element) prov.item(j);
                    if (entity.levels.get(i).provides.get(el.getAttribute("name")) == null) {
                        ArrayList<String> list = new ArrayList<String>();
                        entity.levels.get(i).provides.put(el.getAttribute("name"), list);
                    }
                    entity.levels.get(i).provides.get(el.getAttribute("name")).add(el.getAttribute("value"));
                }
            }
            entity.createRecruitmentStats();
            Main.DB.genBuildings.put(entity.refName, entity);
            logger.log(Level.WARNING, "*Building loaded: {0} *", entity.refName);
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Building CANNOT be loaded: {0}", entity.refName);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
        return true;
    }

    private boolean loadFaction(Element root) {
        GenericFaction entity = new GenericFaction();
        try {
            Element faction = (Element) root.getElementsByTagName("faction").item(0);
            Element images = (Element) root.getElementsByTagName("images").item(0);
            Element names = (Element) root.getElementsByTagName("names").item(0);
            entity.name = faction.getAttribute("name");
            entity.refName = faction.getAttribute("refname");
            entity.turnOrder = Integer.parseInt(faction.getAttribute("turnOrder"));
            Scanner s = new Scanner(faction.getAttribute("color"));
            float r = s.nextFloat();
            float g = s.nextFloat();
            float b = s.nextFloat();
            entity.color = new Vector3f(r, g, b);
            entity.banner = assets.loadTexture("factions" + File.separator + entity.refName + File.separator + images.getAttribute("banner"));
            entity.flag = assets.loadTexture("factions" + File.separator + entity.refName + File.separator + images.getAttribute("flag"));
            entity.icon = assets.loadTexture("factions" + File.separator + entity.refName + File.separator + images.getAttribute("icon"));
            NodeList males = names.getElementsByTagName("male");
            NodeList females = names.getElementsByTagName("female");
            for (int i = 0; i < males.getLength(); i++) {
                Element l = (Element) males.item(i);
                entity.namesMale.add(l.getAttribute("name"));
            }
            for (int i = 0; i < females.getLength(); i++) {
                Element l = (Element) females.item(i);
                entity.namesFemale.add(l.getAttribute("name"));
            }
            Main.DB.genFactions.put(entity.refName, entity);
            logger.log(Level.WARNING, "*Faction loaded: {0} *", entity.refName);
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Faction CANNOT be loaded: {0}", entity.refName);
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
        return true;
    }

    private boolean loadMap(Element root) {
        try {
            Element textures = (Element) root.getElementsByTagName("textures").item(0);
            Element terrain = (Element) root.getElementsByTagName("terrain").item(0);
            Element climates = (Element) root.getElementsByTagName("climates").item(0);
            Element regions = (Element) root.getElementsByTagName("regions").item(0);
            Element factions = (Element) root.getElementsByTagName("factions").item(0);
            Element decorations = (Element) root.getElementsByTagName("decorations").item(0);
            Main.DB.tilesTexturesCount = Integer.parseInt(textures.getAttribute("tiletextures"));
            NodeList tiletexs = textures.getElementsByTagName("tile");
            NodeList basetexs = textures.getElementsByTagName("base");
            for (int i = 0; i < tiletexs.getLength(); i++) {
                Element l = (Element) tiletexs.item(i);
                Main.DB.tileTextures.add(Integer.parseInt(l.getAttribute("id")), assets.loadTexture("map" + File.separator + l.getAttribute("texture")));
                Main.DB.tileTextures_scales.add(Float.parseFloat(l.getAttribute("scale")));
            }
            for (int i = 0; i < basetexs.getLength(); i++) {
                Element l = (Element) basetexs.item(i);
                String path = "map" + File.separator + l.getAttribute("texture");
                if ("regions".equals(l.getAttribute("name"))) {
                    Main.DB.regionsTex = assets.loadTexture(new TextureKey(path, Boolean.parseBoolean(l.getAttribute("flipY"))));
                    Main.DB.flipOrderRegions = Boolean.parseBoolean(l.getAttribute("flip_byte_order"));
                } else if ("types".equals(l.getAttribute("name"))) {
                    Main.DB.typesTex = assets.loadTexture(new TextureKey(path, Boolean.parseBoolean(l.getAttribute("flipY"))));
                    Main.DB.flipOrderTypes = Boolean.parseBoolean(l.getAttribute("flip_byte_order"));
                } else if ("climates".equals(l.getAttribute("name"))) {
                    Main.DB.climatesTex = assets.loadTexture(new TextureKey(path, Boolean.parseBoolean(l.getAttribute("flipY"))));
                    Main.DB.flipOrderClimates = Boolean.parseBoolean(l.getAttribute("flip_byte_order"));
                } else if ("heights".equals(l.getAttribute("name"))) {
                    Main.DB.heightmapTex = assets.loadTexture(new TextureKey(path, Boolean.parseBoolean(l.getAttribute("flipY"))));
                }
            }
            Main.DB.regionsTex.getImage().setFormat(Format.RGB8);
            Main.DB.heightmapTex.getImage().setFormat(Format.RGB8);
            Main.DB.typesTex.getImage().setFormat(Format.RGB8);
            Main.DB.climatesTex.getImage().setFormat(Format.RGB8);
            NodeList t = terrain.getElementsByTagName("tile");
            for (int i = 0; i < t.getLength(); i++) {
                GenericTile tile = new GenericTile();
                Element l = (Element) t.item(i);
                tile.name = l.getAttribute("name");
                tile.type = Integer.parseInt(l.getAttribute("type"));
                Scanner s = new Scanner(l.getAttribute("color"));
                tile.color = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                tile.cost = Integer.parseInt(l.getAttribute("cost"));
                tile.walkable = Boolean.parseBoolean(l.getAttribute("walkable"));
                tile.sailable = Boolean.parseBoolean(l.getAttribute("sailable"));
                tile.textureid = Integer.parseInt(l.getAttribute("textureid"));
                Main.DB.genTiles.put(tile.type, tile);
            }
            Element hm = (Element) terrain.getElementsByTagName("heightmap").item(0);
            Main.DB.heightmapParams = new Vector3f();
            Main.DB.heightmapParams.x = Float.parseFloat(hm.getAttribute("factor0"));
            Main.DB.heightmapParams.y = Float.parseFloat(hm.getAttribute("factor1"));
            Main.DB.heightmapParams.z = Float.parseFloat(hm.getAttribute("cutoff"));
            if (terrain.getElementsByTagName("water") != null) {
                Element water = (Element) terrain.getElementsByTagName("water").item(0);
                Main.DB.hasWater = true;
                Main.DB.waterHeight = Float.parseFloat(water.getAttribute("height"));
                Scanner s = new Scanner(water.getAttribute("color"));
                Main.DB.water_color = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
            }
            Element sun = (Element) terrain.getElementsByTagName("sun").item(0);
            Scanner s = new Scanner(sun.getAttribute("color"));
            Main.DB.sun_color = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
            s = new Scanner(sun.getAttribute("direction"));
            s.useLocale(Locale.ENGLISH);
            Main.DB.sun_direction = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat()).normalizeLocal();
            NodeList c = climates.getElementsByTagName("climate");
            for (int i = 0; i < c.getLength(); i++) {
                Element l = (Element) c.item(i);
                Climate cl = new Climate();
                s = new Scanner(l.getAttribute("color"));
                cl.name = l.getAttribute("name");
                cl.refName = l.getAttribute("refname");
                cl.color = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                Main.DB.climates.put(cl.refName, cl);
            }
            c = regions.getElementsByTagName("region");
            for (int i = 0; i < c.getLength(); i++) {
                Element r = (Element) c.item(i);
                Region reg = new Region();
                reg.name = r.getAttribute("name");
                reg.refName = r.getAttribute("refname");
                s = new Scanner(r.getAttribute("color"));
                reg.color = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                reg.owner = r.getAttribute("owner");
                Main.DB.regions.put(reg.refName, reg);
                if (r.getElementsByTagName("settlement").getLength() > 0) {
                    Element sett = (Element) r.getElementsByTagName("settlement").item(0);
                    Settlement se = new Settlement();
                    se.name = sett.getAttribute("name");
                    se.posX = Integer.parseInt(sett.getAttribute("posx"));
                    se.posZ = Integer.parseInt(sett.getAttribute("posz"));
                    se.level = Integer.parseInt(sett.getAttribute("level"));
                    se.stats.population = Integer.parseInt(sett.getAttribute("population"));
                    se.stats.base_growth = Float.parseFloat(sett.getAttribute("base_growth"));
                    se.culture = sett.getAttribute("culture");
                    se.region = reg.refName;
                    reg.settlement = se;
                    Main.DB.settlements.put(reg.refName, se);
                    NodeList units = sett.getElementsByTagName("unit");
                    for (int j = 0; j < units.getLength(); j++) {
                        Element unit = (Element) units.item(j);
                        Unit u = new Unit();
                        u.refName = unit.getAttribute("refname");
                        u.count = Integer.parseInt(unit.getAttribute("count"));
                        u.exp = Integer.parseInt(unit.getAttribute("exp"));
                        u.att = Integer.parseInt(unit.getAttribute("att_bonus"));
                        u.def = Integer.parseInt(unit.getAttribute("def_bonus"));
                        u.resetMovePoints();
                        se.units.add(u);
                    }
                    NodeList buildings = sett.getElementsByTagName("building");
                    for (int j = 0; j < buildings.getLength(); j++) {
                        Element building = (Element) buildings.item(j);
                        Building b = new Building();
                        b.refName = building.getAttribute("refname");
                        b.level = Integer.parseInt(building.getAttribute("level"));
                        se.buildings.put(b.refName, b);
                    }
                    if (r.getElementsByTagName("dock").getLength() > 0) {
                        Element dock = (Element) r.getElementsByTagName("dock").item(0);
                        se.createDockInfo(Integer.parseInt(dock.getAttribute("posx")), Integer.parseInt(dock.getAttribute("posz")), Integer.parseInt(dock.getAttribute("spawnx")), Integer.parseInt(dock.getAttribute("spawnz")));
                    }
                }
            }
            c = factions.getElementsByTagName("faction");
            for (int i = 0; i < c.getLength(); i++) {
                Element r = (Element) c.item(i);
                Faction fac = new Faction();
                fac.refName = r.getAttribute("refname");
                fac.gold = Integer.parseInt(r.getAttribute("gold"));
                fac.capital = r.getAttribute("capital");
                Main.DB.factions.put(fac.refName, fac);
                NodeList armies = r.getElementsByTagName("army");
                for (int j = 0; j < armies.getLength(); j++) {
                    Element army = (Element) armies.item(j);
                    Army a = new Army();
                    a.posX = Integer.parseInt(army.getAttribute("posx"));
                    a.posZ = Integer.parseInt(army.getAttribute("posz"));
                    a.owner = fac.refName;
                    NodeList units = army.getElementsByTagName("unit");
                    for (int k = 0; k < units.getLength(); k++) {
                        Element unit = (Element) units.item(k);
                        Unit u = new Unit();
                        u.refName = unit.getAttribute("refname");
                        u.count = Integer.parseInt(unit.getAttribute("count"));
                        u.exp = Integer.parseInt(unit.getAttribute("exp"));
                        u.att = Integer.parseInt(unit.getAttribute("att_bonus"));
                        u.def = Integer.parseInt(unit.getAttribute("def_bonus"));
                        u.resetMovePoints();
                        a.units.add(u);
                    }
                    a.calculateMovePoints();
                    fac.armies.add(a);
                }
            }
            c = decorations.getElementsByTagName("decoration");
            for (int i = 0; i < c.getLength(); i++) {
                Element r = (Element) c.item(i);
                WorldDecoration dec = new WorldDecoration();
                dec.refName = r.getAttribute("refname");
                s = new Scanner(r.getAttribute("pos"));
                s.useLocale(Locale.ENGLISH);
                dec.pos = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                s = new Scanner(r.getAttribute("rot"));
                s.useLocale(Locale.ENGLISH);
                dec.rot = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                s = new Scanner(r.getAttribute("scale"));
                s.useLocale(Locale.ENGLISH);
                dec.scale = new Vector3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                Main.DB.worldDecorations.add(dec);
            }
            logger.log(Level.WARNING, "*Map loaded*");
            return true;
        } catch (Exception E) {
            logger.log(Level.SEVERE, "Map CANNOT be loaded");
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }

    public boolean loadAll() {
        File f = new File(game.locatorRoot);
        if (!f.isDirectory()) {
            logger.log(Level.SEVERE, "Cannot find module directory ''{0}''...", game.locatorRoot);
            return false;
        }
        try {
            boolean result = true;
            logger.log(Level.WARNING, "Loading factions");
            result &= loadData("factions");
            logger.log(Level.WARNING, "Loading models");
            result &= loadData("models");
            logger.log(Level.WARNING, "Loading cultures");
            result &= loadData("cultures");
            logger.log(Level.WARNING, "Loading units");
            result &= loadData("units");
            logger.log(Level.WARNING, "Loading buildings");
            result &= loadData("buildings");
            logger.log(Level.WARNING, "Loading sounds");
            result &= loadData("sounds");
            logger.log(Level.WARNING, "Loading music");
            result &= loadData("music");
            logger.log(Level.WARNING, "Loading decorations");
            result &= loadData("decorations");
            logger.log(Level.WARNING, "Loading map");
            result &= loadData("map");
            logger.log(Level.WARNING, "Loading scripts");
            result &= loadData("scripts");
            return result;
        } catch (Exception E) {
            logger.log(Level.SEVERE, E.toString());
            return false;
        }
    }
}
