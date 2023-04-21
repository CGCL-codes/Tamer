package ru.ksu.niimm.cll.mocassin.crawl.analyzer.mapping;

import java.util.List;

/**
 * Mapping between document parts and ontology
 * 
 * @author nzhiltsov
 * 
 */
public class Mapping {

    private List<MappingElement> elements;

    public Mapping(List<MappingElement> elements) {
        this.elements = elements;
    }

    public List<MappingElement> getElements() {
        return elements;
    }

    public void setElements(List<MappingElement> elements) {
        this.elements = elements;
    }
}
