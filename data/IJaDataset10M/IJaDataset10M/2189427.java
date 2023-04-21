package com.webmotix.event;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Interface para implementa��o de eventos do reposit�rio JCR para o motix.
 * @author wsouza
 *
 */
public interface MotixEventListener {

    /**
	 * Deve auto registrar o evento na se��o.
	 * @param session
	 * @throws RepositoryException
	 */
    public void register(final Session session) throws RepositoryException;

    /**
	 * Retorna o nome do workspace.
	 * @return
	 */
    public String getWorkspace();
}
