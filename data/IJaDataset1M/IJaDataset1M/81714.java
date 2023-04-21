package Entities;

import XML.XMLBuilder;

/**
 * A class which is XML-Representationable requires a method to convert to XML,
 * whose signature is given here.
 * @author pontuslp
 */
public interface XMLRepresentation {

    /**
	 * A method which appends the object to the XML-string contained in an
	 * XMLBuilder object.
	 * @param xb the builder whose XML-string this object is to append itself to
	 * @return the same builder
	 */
    XMLBuilder toXML(XMLBuilder xb);
}
