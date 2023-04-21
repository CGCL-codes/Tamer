package net.sourceforge.arcamplayer.core.commands;

import java.io.File;

/**
 * Guarda la biblioteca de medios en un archivo en disco que se 
 * solicitará al usuario.
 * @author David Arranz Oveja, Pelayo Campa González-Nuevo
 *
 */
public class SaveAsCollectionCommand extends Command {

    /**
	 * Ordena la ejecución del comando.
	 */
    @Override
    public void execute() {
        File file = appcore.getGuiFacade().askForNewMediaLibraryFile();
        appcore.setNewMediaLibraryFile(file);
        appcore.storeMediaLibrary();
    }
}
