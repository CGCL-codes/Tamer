package com.jchapman.jempire.game;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xml.sax.InputSource;
import org.yasl.arch.errors.YASLApplicationException;
import org.yasl.arch.impl.application.config.ConfigUtils;
import org.yasl.jars.DynamicJarLoader;
import org.yasl.jars.DynamicJarLoaderImpl;
import org.yasl.jars.YASLJarResource;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
public class GameTypeModel {

    private List gameTypes = new ArrayList();

    private final String gameTypesDir;

    private GameType selectedGameType;

    public GameTypeModel(String gameTypesDir) {
        super();
        this.gameTypesDir = gameTypesDir;
    }

    public GameType getSelectedGameType() throws YASLApplicationException, MalformedURLException, IOException {
        if (selectedGameType == null) {
            GameType[] gtypes = getGameTypes();
            selectedGameType = gtypes[0];
        }
        return selectedGameType;
    }

    public GameType[] getGameTypes() throws YASLApplicationException, MalformedURLException, IOException {
        if (gameTypes.isEmpty()) {
            loadGameTypesData();
        }
        return (GameType[]) gameTypes.toArray(new GameType[gameTypes.size()]);
    }

    public Map loadUnitDefInfo(GameType gameType) throws YASLApplicationException {
        UnitDefContentHandler unitDefContentHandler = new UnitDefContentHandler(UnitDefContentHandler.QTYPE_UNITDEFS);
        YASLJarResource jarres = gameType.getConfigFile();
        InputSource inputSource = ConfigUtils.getInputSource(jarres.getResourceName(), jarres.getClassLoader());
        ConfigUtils.initFromConfigFile(unitDefContentHandler, inputSource);
        return Collections.unmodifiableMap(unitDefContentHandler.getUnitDefMap());
    }

    private DynamicJarLoader getDynamicJarLoader() {
        return new DynamicJarLoaderImpl();
    }

    private void loadGameTypesData() throws YASLApplicationException, MalformedURLException, IOException {
        File gameTypesFolder = new File(System.getProperty("user.dir"), gameTypesDir);
        UnitDefContentHandler unitDefContentHandler = new UnitDefContentHandler(UnitDefContentHandler.QTYPE_METADATA);
        DynamicJarLoader jarLoader = getDynamicJarLoader();
        List jarResources = jarLoader.dynamicallyFindJarResources(gameTypesFolder.getPath(), Collections.singleton("xml"));
        Iterator iter = jarResources.iterator();
        while (iter.hasNext()) {
            YASLJarResource jarres = (YASLJarResource) iter.next();
            if (jarres.getResourceName().endsWith("unitDefs.xml")) {
                unitDefContentHandler.setFileName(jarres);
                InputSource inputSource = ConfigUtils.getInputSource(jarres.getResourceName(), jarres.getClassLoader());
                ConfigUtils.initFromConfigFile(unitDefContentHandler, inputSource);
                gameTypes.add(unitDefContentHandler.getGameType());
            }
        }
    }

    private static class ConfigFileFilter implements FileFilter {

        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().endsWith(".xml");
        }
    }
}
