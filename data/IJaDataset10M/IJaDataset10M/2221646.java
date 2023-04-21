package br.guj.chat.dao;

import java.util.ArrayList;
import br.guj.chat.model.language.Language;
import br.guj.chat.model.server.ChatServer;

/**
 * A language dao
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.7 $, $Date: 2003/04/08 13:37:03 $
 */
public interface LanguageDAO {

    /**
	 * Removes a language
	 * @param l	the language
	 * @throws ChatIOException
	 */
    public void remove(ChatServer instance, Language l) throws DAOException;

    /**
	 * Loads a language from the data system
	 * @param id	the language id
	 * @return		the language object
	 */
    public Language getLanguage(ChatServer instance, String id) throws DAOException;

    /**
	 * Saves a language in the data system
	 * @param l	the language object
	 * @throws DAOException
	 */
    public void saveLanguage(ChatServer instance, Language l) throws DAOException;

    /**
	 * Returns the list of available languages
	 * @return ArrayList	list of languages
	 * @throws DAOException
	 */
    public ArrayList getLanguages(ChatServer instance) throws DAOException;

    /**
	 * Saves all languages for this server back in the system
	 * @param server
	 */
    public void saveLanguages(ChatServer server) throws DAOException;
}
