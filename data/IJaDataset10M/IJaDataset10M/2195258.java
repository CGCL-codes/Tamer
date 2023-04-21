package org.nakedobjects.example.agent.fixture;

import org.nakedobjects.example.agent.Instrument;
import org.nakedobjects.example.agent.Musician;
import org.nakedobjects.example.agent.Performance;
import org.nakedobjects.nof.reflect.java.fixture.JavaFixture;

public class ContextFixture extends JavaFixture {

    public void install() {
        addExplorationPerspective("Music Agent");
        addResource("repository#" + Musician.class.getName());
    }
}
