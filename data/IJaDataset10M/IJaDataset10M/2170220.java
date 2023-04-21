package com.ipolyglot.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ipolyglot.dao.LevelDAO;
import com.ipolyglot.model.Language;
import com.ipolyglot.model.Level;
import com.ipolyglot.service.LevelManager;

/**
 * @author mishag
 */
public class LevelManagerImpl extends BaseManager implements LevelManager {

    private LevelDAO dao;

    private static List<Level> cache = new ArrayList<Level>();

    private static Map<String, Level> mapLevelIdLevel = new HashMap<String, Level>();

    public void setLevelDAO(LevelDAO dao) {
        this.dao = dao;
    }

    public Level getLevel(String id) {
        if (mapLevelIdLevel.size() == 0) {
            getLevels();
        }
        return mapLevelIdLevel.get(id);
    }

    public List<Level> getLevels() {
        if (cache == null || cache.size() == 0) {
            cache = dao.getLevels();
            for (Level level : cache) {
                mapLevelIdLevel.put(level.getId().toString(), level);
            }
        }
        return cache;
    }
}
