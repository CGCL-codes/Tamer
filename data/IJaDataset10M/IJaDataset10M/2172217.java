package net.sourceforge.plantuml.salt.factory;

import net.sourceforge.plantuml.salt.DataSource;
import net.sourceforge.plantuml.salt.Dictionary;
import net.sourceforge.plantuml.salt.Positionner;
import net.sourceforge.plantuml.salt.Terminated;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.ElementPyramid;
import net.sourceforge.plantuml.salt.element.TableStrategy;

public class ElementFactoryPyramid extends AbstractElementFactoryComplex {

    public ElementFactoryPyramid(DataSource dataSource, Dictionary dictionary) {
        super(dataSource, dictionary);
    }

    public Terminated<Element> create() {
        if (ready() == false) {
            throw new IllegalStateException();
        }
        final String header = getDataSource().next().getElement();
        assert header.startsWith("{");
        TableStrategy strategy = TableStrategy.DRAW_NONE;
        if (header.length() == 2) {
            strategy = TableStrategy.fromChar(header.charAt(1));
        }
        final Positionner positionner = new Positionner();
        while (getDataSource().peek(0).getElement().equals("}") == false) {
            positionner.add(getNextElement());
        }
        final Terminated<String> next = getDataSource().next();
        return new Terminated<Element>(new ElementPyramid(positionner, strategy), next.getTerminator());
    }

    public boolean ready() {
        final String text = getDataSource().peek(0).getElement();
        if (text.equals("{") || text.equals("{+") || text.equals("{#") || text.equals("{!") || text.equals("{-")) {
            final String text1 = getDataSource().peek(1).getElement();
            if (text1.matches("[NSEW]=")) {
                return false;
            }
            return true;
        }
        return false;
    }
}
