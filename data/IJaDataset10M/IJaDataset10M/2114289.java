package br.org.sd.dao;

import org.hibernate.Session;
import br.org.sd.model.University;

public class UniversityDAO extends GenericDAO<University> {

    public UniversityDAO(Session session) {
        super(session);
    }

    public University load(Long id) {
        return load(University.class, id);
    }
}
