package it.polito.semreview.enrichment.namedentityextraction;

import java.io.Serializable;
import java.net.URI;

public interface NamedEntity extends Serializable {

    String name();

    String type();

    URI uri();

    Double similarityScore();

    Integer support();
}
