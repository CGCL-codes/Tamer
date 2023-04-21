package net.sourceforge.arcamplayer.core.commands;

/**
 * <p>Pausa la reproducción.</p>
 * @author David Arranz Oveja, Pelayo Campa González-Nuevo
 */
public class PauseCommand extends Command {

    /**
	 * Ordena la ejecución del comando.
	 */
    @Override
    public void execute() {
        appcore.getPlayerFacade().pause();
    }
}
