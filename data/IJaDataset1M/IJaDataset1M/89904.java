package de.andreavicentini.magiclabs;

import junit.framework.TestCase;
import org.magiclabs.basix.MagicScope;
import org.magiclabs.basix.MagicScope.Inject;

public class TestMagicScopeBasics extends TestCase {

    public void testSimple() {
        MagicScope scope = new MagicScope();
        scope.register(String.class, "andrea");
        Mamma mamma = scope.create(Mamma.class);
        assertEquals("andrea", mamma.name);
    }

    public void testScopes() {
        MagicScope master = new MagicScope();
        MagicScope slave = new MagicScope(master);
        master.register(Mamma.class, new Mamma("silvana"));
        slave.register(String.class, "andrea");
        Bimbo bimbo = slave.create(Bimbo.class);
        assertEquals("andrea", bimbo.comeTiChiami());
        assertEquals("silvana", bimbo.chiELaMamma().name);
    }

    class Bimbo {

        private final Mamma mamma;

        private final String name;

        @Inject
        public Bimbo(Mamma mamma, String name) {
            this.mamma = mamma;
            this.name = name;
        }

        public Mamma chiELaMamma() {
            return this.mamma;
        }

        public String comeTiChiami() {
            return name;
        }
    }

    class Mamma {

        final String name;

        @Inject
        public Mamma(String name) {
            this.name = name;
        }
    }
}
