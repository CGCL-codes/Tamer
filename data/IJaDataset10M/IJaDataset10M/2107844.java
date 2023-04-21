package org.jcr_blog.backend.wtc.converters;

import javax.faces.convert.FacesConverter;

/**
 * A JSF Converter for Blog entities.
 * 
 * Workarround: It looks like setting value and forClass in the FacesConverter annotation does not work
 * @author  Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
@FacesConverter(value = "blogConverter")
public class NamedBlogConverter extends BlogConverter {
}
