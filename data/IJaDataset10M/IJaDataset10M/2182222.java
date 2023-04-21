package br.com.visualmidia.persistence;

import br.com.visualmidia.exception.BusinessException;

public class RemoveCourse extends GDTransaction {

    private static final long serialVersionUID = -5437291379421000317L;

    private String id;

    public RemoveCourse(String id) {
        this.id = id;
    }

    protected void execute(PrevalentSystem system) throws BusinessException {
        if (system.courses.containsKey(id)) system.courses.remove(id);
    }
}
