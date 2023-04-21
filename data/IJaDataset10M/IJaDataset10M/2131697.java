package org.jazzteam.studenthelper.dao.xml;

import org.jazzteam.studenthelper.model.Identify;

public class TestEntity extends Identify {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        TestEntity test = (TestEntity) obj;
        if ((this.getId().equals(test.getId())) && (this.getName().equals(test.getName()))) {
            return true;
        }
        return false;
    }
}
