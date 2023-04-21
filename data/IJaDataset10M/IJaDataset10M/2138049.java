package org.tex4java;

import org.tex4java.tex.environment.Environment;
import org.tex4java.tex.environment.MockEnvironment;

public class MockManager extends Manager {

    public MockManager() {
        env = new MockEnvironment(this);
        init();
    }

    public void init() {
        env.init(null);
    }

    public Environment getEnvironment() {
        return env;
    }
}
