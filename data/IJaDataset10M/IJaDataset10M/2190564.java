package br.guj.chat.servlet;

import br.guj.chat.model.server.ChatServer;
import br.guj.chat.model.server.InstanceException;

/**
 * A session no found exception
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.13 $, $Date: 2003/04/07 15:37:02 $
 */
public class SessionNotFoundException extends InstanceException {

    /**
	 * Constructor SessionNotFoundException.
	 */
    public SessionNotFoundException(ChatServer instance, String string) {
        this(instance, string, string);
    }

    /**
	 * Constructor for SessionNotFoundException
	 */
    public SessionNotFoundException(ChatServer instance, String code, String msg) {
        super(instance, code, msg);
    }

    /**
	 * Constructor for SessionNotFoundException
	 */
    public SessionNotFoundException(ChatServer instance, String code, String msg, Throwable ex) {
        super(instance, code, msg, ex);
    }
}
