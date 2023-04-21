package sk.tuke.ess.editor.base;

import sk.tuke.ess.editor.base.commongui.GUI;
import sk.tuke.ess.editor.base.components.logger.Logger;
import sk.tuke.ess.editor.base.document.DocumentManager;
import sk.tuke.ess.editor.base.helpers.FileHelper;
import sk.tuke.ess.editor.base.helpers.PathHelper;
import sk.tuke.ess.editor.base.module.ModuleManager;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Hlavná trieda aplikácie. Zastrešuje všetky ostatné objekty
 * logiky aplikácie a používateľského rozhrania.
 *
 * @author Ján Čabala
 */
public class App {

    /**
     * Vytvorý novú inštanicu aplikácie. Inicializuje {@link Logger},
     * {@link sk.tuke.ess.editor.base.document.DocumentManager} a {@link sk.tuke.ess.editor.base.commongui.GUI}.
     */
    public App(String[] arguments) {
        handleFirstStart();
        if (Arrays.asList(arguments).contains("-debug")) {
            Logger.getLogger().setDebugMode(true);
        }
        ModuleManager moduleManager = new ModuleManager();
        DocumentManager documentManager = new DocumentManager();
        new GUI(moduleManager, documentManager);
    }

    private void handleFirstStart() {
        File dataDir = new File(PathHelper.getDataPath());
        if (dataDir.exists()) return;
        try {
            FileHelper.unzipToDir(new File(new File(PathHelper.getPathFromWorkingDir("data")), "default.zip"), dataDir);
        } catch (IOException e) {
            Logger.getLogger().addError("Nepodarilo sa nakopírovať dáta do priečinku  <b>%s</b>", dataDir.getPath());
            Logger.getLogger().addException(e, "Chyba pri unzipe init dat");
        }
    }
}
