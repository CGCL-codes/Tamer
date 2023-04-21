package test.runtime;

import java.io.Serializable;

/**
 * @author Shen Li
 */
public abstract class Target implements Foo, Bar, Serializable {

    private String targetState;

    public String target() {
        return "Target.target()" + RuntimeTest.ARROW + foo();
    }

    public String getTargetState() {
        return targetState;
    }

    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }
}
