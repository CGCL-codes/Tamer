package at.ofai.gate.extendedgazetteer;

import gate.creole.metadata.CreoleResource;

/**
 *  See documentation in the wiki:
 * http://code.google.com/p/gateplugin-stringannotation/wiki/ExtendedGazetteer
 *
 *  @author Johann Petrak
 *  @author Valentin Tablan
 *  @author Borislav Popov
 */
@CreoleResource(name = "Extended Gazetteer", comment = "An extended version of the GATE DefaultGazetteer that supports finding prefixes and suffixes, specification of word and non-word characters, reuse of datastructures for multiple identical copies of a gazetteer and more.", icon = "shefGazetteer.gif", helpURL = "http://code.google.com/p/gateplugin-stringannotation/wiki/ExtendedGazetteer")
public class ExtendedGazetteer extends AbstractExtendedGazetteer {

    private static final long serialVersionUID = 1L;
}
