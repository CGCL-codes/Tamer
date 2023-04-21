package pl.edu.amu.xtr.cors;

import pl.edu.amu.xtr.xml.Variables;
import pl.edu.amu.xtr.xsd.PathElement;

/**
 * Provide mathod to acquire correspondence keys.
 * 
 * @author Jakub Marciniak
 */
public interface CorrespondenceSelector {

    public String getSourceKey(PathElement pathElement);

    public String getCorrespondendValue(PathElement pathElement, Variables variables);
}
