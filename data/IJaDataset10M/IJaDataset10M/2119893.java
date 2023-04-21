package es.rediris.searchy.engine.map;

import org.apache.log4j.Logger;

/** 
 * @version $Id: MapFactory.java,v 1.2 2005/03/10 15:16:39 dfbarrero Exp $
 * @author David F. Barrero
 */
public class MapFactory {

    static Logger logger = Logger.getLogger(MapFactory.class);

    public Map getMap(String type) {
        Map map = null;
        try {
            if (type.equals("filter")) {
                logger.info("Building filter map");
                return new MapFilter();
            } else if (type.equals("trivial")) {
                logger.info("Building trivial map");
                return new MapTrivial();
            } else {
                logger.info("Building custom map " + type);
                Class temp = Class.forName(type);
                map = (Map) temp.newInstance();
                return map;
            }
        } catch (ClassNotFoundException e) {
            logger.error("Class not found " + e.getMessage());
        } catch (InstantiationException e) {
            logger.error("Clase not instanciated " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccess " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error. " + e.getMessage());
        }
        logger.error("Map type " + type + " could not be created");
        return null;
    }
}
