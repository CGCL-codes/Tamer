package net.sf.brightside.xlibrary.metamodel;

import java.io.Serializable;
import java.util.List;

public interface Faculty {

    String getName();

    String getCity();

    String getType();

    void setName(String name);

    void setCity(String city);

    void setType(String type);

    List<Student> getStudents();

    void setStudents(List<Student> students);

    Serializable takeId();
}
