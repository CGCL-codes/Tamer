package com.angel.architecture.daos;

import java.util.Collection;
import com.angel.architecture.persistence.beans.Language;
import com.angel.architecture.persistence.ids.ObjectId;
import com.angel.dao.generic.interfaces.GenericDAO;

public interface LanguageDAO extends GenericDAO<Language, ObjectId> {

    public Language findUniqueByName(String name);

    public Language findUniqueByLocale(String locale);

    public Collection<Language> findAllByActive();

    public Collection<Language> findAllByNotActive();
}
