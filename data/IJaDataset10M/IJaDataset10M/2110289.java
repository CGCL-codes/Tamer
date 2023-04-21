package by.oslab.hachathon.easycar.service;

import by.oslab.hachathon.easycar.model.Adw;
import by.oslab.hachathon.easycar.model.User;

/**
 * Service for user accounts
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
public interface IAdwService extends IModelService<Adw> {

    /**
	 * Finds user by it's login
	 * 
	 * @param login
	 * @return
	 * @throws IllegalArgumentException
	 * @version 1
	 */
    User get(String login) throws IllegalArgumentException;
}
