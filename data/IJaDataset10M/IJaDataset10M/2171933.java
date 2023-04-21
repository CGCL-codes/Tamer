package nr.co.megaware.printwithease.Beans;

import nr.co.megaware.AnnotateCode.Author;
import nr.co.megaware.AnnotateCode.Copyright;

/**
 *An interface for the bean that is the only component end user( Java developer) 
 * needs to understand.</ br> Actually there will be a wrapper bean for the 
 * actual bean which only includes the setters developer of this library need.</ br>
 * All setters for other properties which are set by user of the end application 
 * are included in original bean that implements this Interface.
 * @author Arun.K.R
 * @version 1.0
 */
@Author(first = "Arun", initials = "K.R")
@Copyright(license = "The BSD License", entity = "Arun.K.R")
public interface BeanDefaults {
}
