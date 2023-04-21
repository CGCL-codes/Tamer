package org.idspace.aau.iwis.datamodel;

import org.idspace.aau.iwis.dataaccess.GenericDAO;

public interface SessionDAO extends GenericDAO<Session, Long> {

    public Session getFromLabel(String label);
}
