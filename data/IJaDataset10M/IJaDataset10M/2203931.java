package com.hp.hpl.jena.rdql;

import java.util.*;
import com.hp.hpl.jena.rdf.model.Model;

/** rdql2.ResultBinding
 * 
 * @author Andy Seaborne
 * @version $Id: ResultBinding.java,v 1.16 2006/03/22 13:53:13 andy_seaborne Exp $
 */
public interface ResultBinding {

    /** Return an iterator of the items in this result binding. 
     *  This iterator has operations to access the current variable name and variable value.
     *  
     * @return ResultBindingIterator
     */
    public ResultBindingIterator iterator();

    /** Return the value of the named variable in this binding.  Object will be
        an RDFNode.
     */
    public Object get(String varName);

    /** Iterator over the names in this binding */
    public Iterator names();

    public Model mergeTriples(Model model);

    public Set getTriples();
}
