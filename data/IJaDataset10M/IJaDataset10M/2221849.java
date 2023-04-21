package org.expasy.jpl.commons.collection.symbol;

import org.expasy.jpl.commons.collection.symbol.DataSymbolRegister;
import org.expasy.jpl.commons.collection.symbol.AbstractSymbol;
import org.expasy.jpl.commons.collection.symbol.AbstractSymbolType;
import org.expasy.jpl.commons.collection.symbol.SymbolTest.StringSymbol;
import org.junit.Before;
import org.junit.Test;

public class SymbolTypeTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testNewSymbolType() {
        System.out.println(NucSymbol.getSymbolType());
    }

    @SuppressWarnings("serial")
    public static class NucSymbol extends AbstractSymbol<String> {

        static final SymbolType<String> TYPE = new AbstractSymbolType<String>("NucType", NucSymbol.class, "N(R(AG)Y(CT))") {

            public DataSymbolRegister<String> getDataSymbolManager() {
                return null;
            }
        };

        public NucSymbol(final char name) {
            super(name, TYPE);
        }

        public static StringSymbol newInstance(char name) {
            return new StringSymbol(name);
        }

        public static SymbolType<String> getSymbolType() {
            return TYPE;
        }
    }
}
