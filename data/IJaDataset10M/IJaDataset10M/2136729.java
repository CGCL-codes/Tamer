package uk.co.pointofcare.echobase.neoutils.rendering.implementation.containers;

import org.neo4j.graphdb.PropertyContainer;

public class ListContainer<T extends PropertyContainer> extends IteratorContainer<T> {

    @Override
    protected String iteratorDivider() {
        return "";
    }

    @Override
    protected String iteratorFooter() {
        return "</ul>";
    }

    @Override
    protected String iteratorHeader() {
        return "<ul>";
    }

    @Override
    protected String iteratorRowEnd() {
        return "</li>";
    }

    @Override
    protected String iteratorRowStart() {
        return "<li>";
    }
}
