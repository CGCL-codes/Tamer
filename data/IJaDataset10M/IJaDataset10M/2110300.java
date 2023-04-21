package org.nakedobjects.nos.client.xat2;

import org.nakedobjects.nof.reflect.java.fixture.JavaFixtureBuilder;
import org.nakedobjects.nos.client.xat2.fixtures.SessionFixture;

public class Xat2FixtureBuilder extends JavaFixtureBuilder {

    public Xat2FixtureBuilder() {
    }

    /**
     * Captures {@link SessionFixture} as it is
     * installed, to be returned by {@link #getSessionFixture()}.
     */
    @Override
    public void installFixture(Object fixture) {
        super.installFixture(fixture);
        if (fixture instanceof SessionFixture) {
            this.sessionFixture = (SessionFixture) fixture;
        }
    }

    private SessionFixture sessionFixture;

    /**
     * The {@link Fixture} (if any) added via {@link #installFixture(Fixture)} 
     * that is an instance of {@link SessionFixture}.
     *  
     * <p>
     * If there is more than one {@link SessionFixture}, then the
     * last one installed is returned.
     * 
     * @return
     */
    public SessionFixture getSessionFixture() {
        return sessionFixture;
    }
}
