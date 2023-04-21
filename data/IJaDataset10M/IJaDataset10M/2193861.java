package edu.uiuc.ncsa.security.rdf.gui;

import edu.uiuc.ncsa.security.rdf.MyThing;

/**
 * Interface for panels that display RDF information. This contains a {@link MyThing}
 * whose properties are displayed. The options are <br>
 * <ul>
 *     <li>UriRef to another object is display as a nested panel</li>
 *     <li>A property of type xsd:string is displayed as a text box</li>
 *     <li>Any other property is displayed as a line of text</li>
 * </ul>
 * This gives a serviceable but very crude view of RDF, suitable for most purposes.
 * <p>Created by Jeff Gaynor<br>
 * on 10/31/11 at  5:15 PM
 */
public interface RDFDisplayPanel {

    MyThing getThing();
}
