package ar.com.fdvs.dgarcia.usercomm.users;

import ar.com.fdvs.dgarcia.comm.handlers.ActionRepertoire;
import ar.com.fdvs.dgarcia.comm.handlers.DefaultActionRepertoire;
import ar.com.fdvs.dgarcia.comm.handlers.MessageHandler;
import ar.com.fdvs.dgarcia.usercomm.msgs.BadException;
import ar.com.fdvs.dgarcia.usercomm.msgs.ErroneousExecution;
import ar.com.fdvs.dgarcia.usercomm.msgs.ErrorType;
import ar.com.fdvs.dgarcia.usercomm.msgs.ProgrammerMessage;

/**
 * /** Esta clase representa al programador default. Todas las comunicaciones con este programador
 * son llevadas a cabo a traves de la salida estandar y de errores, paralizando la ejecucion del
 * programa cuando se produce un error.
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia
 */
public class WatchingOutputProgrammer implements Programmer {

    /**
	 * Repertorio de acciones a realizar al recibir un mensaje
	 */
    private ActionRepertoire repertoire;

    /**
	 * @see ar.com.fdvs.dgarcia.usercomm.users.Programmer#receive(ar.com.fdvs.dgarcia.usercomm.msgs.ProgrammerMessage)
	 */
    public void receive(ProgrammerMessage message) {
        Class<? extends ProgrammerMessage> messageType = message.getClass();
        MessageHandler<ProgrammerMessage> handler = this.getRepertoire().getHandlerFor(messageType);
        if (handler != null) {
            handler.handle(message);
        }
        ErroneousExecution.hasHappened("Message type not expected: " + messageType + " " + message, ErrorType.BAD_CONFIGURATION);
    }

    /**
	 * @return Obtiene el repertorio de esta instancia
	 */
    public ActionRepertoire getRepertoire() {
        if (repertoire == null) {
            repertoire = new DefaultActionRepertoire();
            this.configure(repertoire);
        }
        return repertoire;
    }

    /**
	 * Configura el repertorio de acciones de esta instancia
	 * 
	 * @param createdRepertoire
	 *            Repertorio recien creado
	 */
    private void configure(ActionRepertoire createdRepertoire) {
        createdRepertoire.doFor(BadException.class, new ar.com.fdvs.dgarcia.comm.handlers.MessageHandler<BadException>() {

            public void handle(BadException message) {
                throw new RuntimeException(message.getSituationType() + ": " + message.getContextualMessage(), message.getOccuredException());
            }
        });
        createdRepertoire.doFor(ErroneousExecution.class, new ar.com.fdvs.dgarcia.comm.handlers.MessageHandler<ErroneousExecution>() {

            public void handle(ErroneousExecution message) {
                System.err.println(message.getSituationType());
                throw new RuntimeException(message.getSituationType() + ": " + message.getErrorDescription(), message.getContextStack());
            }
        });
    }

    /**
	 * Establece el repertorio de acciones de este programador
	 * 
	 * @param repertoire
	 *            Acciones a realizar cuando se reciben los mensajes
	 */
    public void setRepertoire(ActionRepertoire repertoire) {
        this.repertoire = repertoire;
    }
}
