package org.datamining.guha.model.literal;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.logging.*;
import org.datamining.guha.model.Formatter;

/**
 * Literal factory provides access to Literals.
 * It is responsible for creation and maintenance
 * of all Literals in whole model.
 * 
 * If Literal is requested then it creates one (if it hasen't been created yet),
 * stores it in its internal array and returns reference.
 * <p>
 * See <a href="http://www.javaworld.com/javaworld/jw-07-2003/jw-0725-designpatterns.html?page=2">
 * Flyweigth Factory pattern</a> or <a href="http://en.wikipedia.org/wiki/Flyweight_pattern">
 * Flyweight pattern (Wikipedia)</a> for more information.
 *  
 * @author Lukas Vlcek
 */
public class LiteralFactory {

    public static int _A = 65;

    private static final LiteralFactory INSTANCE = new LiteralFactory();

    private static final List<Literal> literalList = new ArrayList<Literal>();

    private static final Log log = LogFactory.getLog(LiteralFactory.class);

    static {
        INSTANCE.setFormatter(new LiteralFormatter());
    }

    private Formatter<Literal> formatter;

    /** Constructor of LiteralFactory is not public. */
    private LiteralFactory() {
    }

    /** Factory method */
    public static LiteralFactory getInstance() {
        return INSTANCE;
    }

    /**
     * @return Literal
     */
    public Literal getLiteral(int symbolValue, boolean negation) {
        for (Literal l : literalList) {
            if (l.isNegative() == negation && l.getSymbol() == symbolValue) {
                if (formatter != null) {
                    l.setFormatter(formatter);
                    log.info("Literal found in cache: " + l);
                }
                return l;
            }
        }
        return addNewLiteral(symbolValue, negation);
    }

    private Literal addNewLiteral(int symbolValue, boolean negation) {
        Literal l = new LiteralImpl(symbolValue, negation);
        literalList.add(l);
        if (formatter != null) {
            l.setFormatter(formatter);
            log.info("New Literal stored in cache: " + l);
        }
        return l;
    }

    /**
     * @return Literal which is negated version of given Literal.
     */
    public Literal getNegatedLitearl(Literal lit) {
        return getLiteral(lit.getSymbol(), !lit.isNegative());
    }

    public void setFormatter(Formatter<Literal> f) {
        formatter = f;
    }
}
