package org.logitest;

import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.*;
import org.jdom.*;

/** The VariableReader base class should be extended to provide the logic for reading
	variable values from a page.
	
	@author Clancy Malcolm
*/
public abstract class VariableReader extends ResourceChild {

    /** Read the variables from the resource
		
		@param resource The resource
		@param writeTo The variables object to write the variables to
		@return The variables read as a hashtable with the variable names as String keys and the variable value as String values
		@throws Exception Any exception
	*/
    public abstract void readVariables(Resource resource, Variables writeTo) throws Exception;
}
