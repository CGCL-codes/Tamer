package br.ufal.tci.nexos.arcolive;

import br.ufal.tci.nexos.arcolive.exception.ArCoLIVECannotAddParticipantException;
import br.ufal.tci.nexos.arcolive.exception.ArCoLIVECannotStartServiceException;
import br.ufal.tci.nexos.arcolive.exception.ArCoLIVECannotStopServiceException;
import br.ufal.tci.nexos.arcolive.participant.ArCoLIVEParticipant;

/**
 * CLASSNAME.java
 *
 * CLASS DESCRIPTION
 *
 * @see CLASSNAME
 *
 * @author <a href="mailto:felipe@labpesquisas.tci.ufal.br">Felipe Barros Pontes</a>.
 * @author <a href="mailto:leandro@labpesquisas.tci.ufal.br">Leandro Melo de Sales</a>.
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
public class ArCoLIVEServiceInstanceAdapter extends ArCoLIVEServiceInstance {

    public ArCoLIVEServiceInstanceAdapter() {
        super();
    }

    @Override
    public void addParticipant(ArCoLIVEParticipant participant) throws ArCoLIVECannotAddParticipantException {
    }

    @Override
    public void removeParticipant(ArCoLIVEParticipant participant) {
    }

    @Override
    public void startService() throws ArCoLIVECannotStartServiceException {
    }

    @Override
    public void stopService() throws ArCoLIVECannotStopServiceException {
    }
}
