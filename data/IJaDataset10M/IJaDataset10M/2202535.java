package de.cue4net.eventservice.dao;

import de.cue4net.eventservice.model.I18N.Locale;

/**
 * Generic Interface for localized DAO operations
 * @author Keino Uelze
 * @version $Id: DaoLocalizedGeneral.java,v 1.2 2008-06-05 12:19:09 keino Exp $
 */
public interface DaoLocalizedGeneral<T> extends DaoGeneral<T> {

    public T getLocalizedByID(Long idValue, Locale locale);

    public Boolean createOrUpdateLocalized(T object, Locale locale);
}
