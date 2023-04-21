package org.neodatis.odb.test.school;

import org.neodatis.odb.core.query.nq.SimpleNativeQuery;
import org.neodatis.odb.test.vo.school.Student;

public class SchoolSimpleNativeQueryStudent extends SimpleNativeQuery {

    private String name;

    public SchoolSimpleNativeQueryStudent(String name) {
        this.name = name;
    }

    public boolean match(Student object) {
        return object.getName().equals(name);
    }
}
