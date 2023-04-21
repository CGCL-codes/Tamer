package br.ufal.tci.nexos.arcolive.command;

import br.ufal.tci.nexos.arcolive.participant.ArCoLIVEParticipant;

/**
 * 
 * ArCoLIVEServerCommand.java
 *
 * CLASS DESCRIPTION
 *
 * @see Jan 4, 2008
 *
 * @author <a href="mailto:txithihausen@gmail.com">Ivo Augusto Andrade R Calado</a>.
 * @author <a href="mailto:thiagobrunoms@gmail.com">Thiago Bruno Melo de Sales</a>.
 * @since 0.1
 * @version 0.1
 *
 * <p><b>Revisions:</b>
 *
 * <p><b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public abstract class ArCoLIVEServerCommand {

    protected ArCoLIVEParticipant participant;

    public ArCoLIVEServerCommand(ArCoLIVEParticipant participant) {
        this.participant = participant;
    }

    public abstract void execute();
}
