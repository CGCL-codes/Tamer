package net.sourceforge.simplegamenet.framework;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.swing.*;
import net.sourceforge.simplegamenet.framework.model.SimpleGameNetSettings;
import net.sourceforge.simplegamenet.framework.transport.GameIdentification;
import net.sourceforge.simplegamenet.specs.model.GameFactory;

/**
 * A Singleton
 */
public class GameFactoryManager {

    private static GameFactoryManager instance;

    public static GameFactoryManager getInstance() {
        return instance;
    }

    public static void loadGamesStaticly(List gameFactoriesList) {
        ClassLoader gamesClassLoader = GameFactoryManager.class.getClassLoader();
        instance = new GameFactoryManager(gamesClassLoader, gameFactoriesList);
    }

    public static void loadGamesDynamically() {
        try {
            String gamesPath = createGamesPath();
            File pathFile = new File(new URI(gamesPath));
            File[] files = pathFile.listFiles(new JarFileFilter());
            if (files == null) {
                JOptionPane.showMessageDialog(null, pathFile.toString() + " does not exist.", "Error loading games", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            ClassLoader gamesClassLoader = createGamesClassLoader(files, gamesPath);
            List gameFactoriesList = createGameFactoriesList(files, gamesClassLoader, pathFile);
            Collections.sort(gameFactoriesList);
            Thread.currentThread().setContextClassLoader(gamesClassLoader);
            instance = new GameFactoryManager(gamesClassLoader, gameFactoriesList);
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(null, "Error loading games.", "Error loading games", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static String createGamesPath() {
        String gamesPath = GameFactoryManager.class.getResource("/" + GameFactoryManager.class.getName().replaceAll("\\.", "/") + ".class").getPath();
        gamesPath = gamesPath.substring(0, gamesPath.lastIndexOf('/', gamesPath.lastIndexOf('!'))) + "/games/";
        return gamesPath;
    }

    private static ClassLoader createGamesClassLoader(File[] files, String path) {
        URL[] uRLs = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                uRLs[i] = new URL(new URL(path), files[i].getName());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        ClassLoader gamesClassLoader = new URLClassLoader(uRLs);
        return gamesClassLoader;
    }

    private static List createGameFactoriesList(File[] files, ClassLoader gamesClassLoader, File pathFile) {
        List gameFactoriesList;
        gameFactoriesList = new ArrayList();
        for (int i = 0; i < files.length; i++) {
            try {
                JarFile jarFile = new JarFile(files[i]);
                Manifest manifest = jarFile.getManifest();
                if (manifest == null) {
                    throw new Exception("No manifest");
                }
                Class classInstance = gamesClassLoader.loadClass(manifest.getMainAttributes().getValue("Main-Class"));
                GameFactory gameFactory = (GameFactory) classInstance.newInstance();
                if (gameFactoriesList.contains(gameFactory)) {
                    JOptionPane.showMessageDialog(null, files[i].getName() + " has been included twice.", "Error loading a game", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                if (SimpleGameNetSettings.getInstance().getVersion().compareTo(gameFactory.getMinimumSimpleGameNetVersion()) < 0) {
                    JOptionPane.showMessageDialog(null, files[i].getName() + " needs SimpleGameNet version " + gameFactory.getMinimumSimpleGameNetVersion().toString() + ".", "Error loading a game", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                gameFactoriesList.add(gameFactory);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, files[i].getName() + " is not a valid game package.", "Error loading a game", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (gameFactoriesList.size() <= 0) {
            JOptionPane.showMessageDialog(null, pathFile.toString() + " did not contain any valid game packages.", "Error loading games", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return gameFactoriesList;
    }

    private ClassLoader gamesClassLoader;

    private List gameFactoriesList;

    private GameFactoryManager(ClassLoader gamesClassLoader, List gameFactoriesList) {
        this.gamesClassLoader = gamesClassLoader;
        this.gameFactoriesList = gameFactoriesList;
    }

    public ClassLoader getGamesClassLoader() {
        return gamesClassLoader;
    }

    public GameFactory[] getGameFactories() {
        return (GameFactory[]) gameFactoriesList.toArray(new GameFactory[gameFactoriesList.size()]);
    }

    public GameFactory getGameFactory(GameIdentification gameIdentification) {
        for (Iterator it = gameFactoriesList.iterator(); it.hasNext(); ) {
            GameFactory gameFactory = (GameFactory) it.next();
            if (gameFactory.getName().equals(gameIdentification.getName()) && gameFactory.getAuthor().equals(gameIdentification.getAuthor())) {
                return gameFactory;
            }
        }
        return null;
    }

    private static class JarFileFilter implements FileFilter {

        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar");
        }
    }
}
