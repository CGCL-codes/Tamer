package com.bluebrim.xml.shared;

import java.util.*;
import com.bluebrim.xml.impl.shared.*;

/**
 * COModelParser parses models in order to generate XML from trees of models.
 * The context for the parser consists of
 * <ul>
 * <li> A model which to parse
 * <li> A CoXmlBuilder which is responsible for building nodes in the XML representation
 * <li> A HashMap to keep track of objects visited. Question: Is there a general way of handling circular references?
 * <li> Information about which attributes to extract for different classes. Will be hardcoded initially
 * <ul/>
 */
public abstract class CoModelParser implements CoModelParserIF {

    protected Object m_model;

    protected com.bluebrim.xml.shared.CoXmlBuilderIF m_builder;

    protected HashMap m_visited;

    /**
 * CoModelParser constructor comment.
 */
    public CoModelParser() {
        super();
    }

    /**
 * Insert the method's description here.
 * Creation date: (1999-09-01 15:54:49)
 * @param model java.lang.Object
 */
    public CoModelParser(Object model) {
        m_model = model;
    }

    /**
 * Insert the method's description here.
 * Creation date: (1999-09-01 15:54:49)
 * @param builder com.bluebrim.xml.shared.CoXmlBuilderIF
 */
    public CoModelParser(com.bluebrim.xml.shared.CoXmlBuilderIF builder) {
        m_builder = builder;
    }
}
