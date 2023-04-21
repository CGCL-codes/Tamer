package org.frameworker.model;

import java.io.Serializable;

/**
 * @author Marcel Mauricio (marcel.wiskecidu@gmail.com)
 * @since 19/06/2007
 * 
 */
public interface Entity {

    /**
	 * Pega o identificador de uma entidade
	 * @author Marcel Mauricio (marcel.wiskecidu@gmail.com)
	 * @since 19/06/2007
	 *
	 * @return o objeto identificador de uma entidade
	 */
    Serializable getId();
}
