package com.sadalbari.validator.test.core.mock;

import com.sadalbari.validator.core.annotation.Required;
import com.sadalbari.validator.core.annotation.ValidateChild;

public class Level1Class {

    private String name;

    private Level2Class child1;

    @ValidateChild
    private Level2Class child2;

    public Level1Class() {
        super();
    }

    @ValidateChild
    @Required
    public Level2Class getChild1() {
        return child1;
    }

    public void setChild1(Level2Class child1) {
        this.child1 = child1;
    }

    @Required
    public Level2Class getChild2() {
        return child2;
    }

    public void setChild2(Level2Class child2) {
        this.child2 = child2;
    }

    @Required
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
