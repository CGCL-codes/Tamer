package org.apache.isis.extensions.wicket.ui.util;

import org.apache.wicket.behavior.IBehavior;

public class EvenOrOddCssClassAppenderFactory {

    private enum EvenOrOdd {

        EVEN, ODD;

        private EvenOrOddCssClassAppenderFactory.EvenOrOdd next() {
            return this == EVEN ? ODD : EVEN;
        }

        private String className() {
            return this.name().toLowerCase();
        }
    }

    private EvenOrOddCssClassAppenderFactory.EvenOrOdd eo = EvenOrOdd.EVEN;

    public IBehavior nextClass() {
        final String className = eo.className();
        eo = eo.next();
        return new CssClassAppender(className);
    }
}
