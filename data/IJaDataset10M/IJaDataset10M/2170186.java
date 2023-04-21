package org.databene.commons.visitor;

import java.util.List;
import org.databene.commons.CollectionUtil;
import org.databene.commons.Visitor;

/**
 * Groups multiple visitors into the interface of a single one.<br/><br/>
 * Created: 06.03.2011 14:39:23
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class MultiVisitor<E> implements Visitor<E> {

    protected List<Visitor<E>> realVisitors;

    public MultiVisitor(Visitor<E>... realVisitors) {
        this.realVisitors = CollectionUtil.toList(realVisitors);
    }

    public void visit(E element) {
        for (Visitor<E> realVisitor : realVisitors) realVisitor.visit(element);
    }
}
