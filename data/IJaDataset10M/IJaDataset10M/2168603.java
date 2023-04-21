package br.com.visualmidia.persistence;

import br.com.visualmidia.exception.CourseDoesNotExistsException;

public class GetCourse extends GDQuery {

    private String id;

    public GetCourse(String id) {
        this.id = id;
    }

    public GetCourse() {
        this.id = null;
    }

    protected Object executeQuery(PrevalentSystem system) {
        if (id == null) return system.courses; else if (system.courses.containsKey(id)) return system.courses.get(id); else throw new CourseDoesNotExistsException(id);
    }
}
