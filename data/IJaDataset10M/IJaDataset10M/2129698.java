package de.d3web.we.kdom.visitor;

import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.Type;

public interface Visitor {

    public void visit(Section<? extends Type> s);
}
