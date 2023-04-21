package net.sourceforge.mgl.naming;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

/**
 * Adapter class implementing the DirContext interface.
 *
 * <p><b>Usage:</b><br>
 * <code>N/A</code><br>
 *
 * <p><b>Version Info:</b><br>
 * <font size="-1"><pre>
 * $Date: 2002/10/09 11:58:01 $
 * $Revision: 1.3 $
 * $Author: mgl $
 * </pre></font>
 *
 * <p><b>Change Log:</b><br>
 * <font size="-1"><pre xml:space="preserve">
 * $Log: DirContextAdapter.java,v $
 * Revision 1.3  2002/10/09 11:58:01  mgl
 * bugfix: all methods should throw NamingException
 *
 * Revision 1.2  2002/10/09 11:39:27  mgl
 * changed javadoc format; the new format includes the CVS changelog information
 *
 * <b>Note: please, don't modify the change log!</b>
 * </pre></font>
 *
 * @author <a href="mgl@users.sourceforge.net">Marcel Schepers</a>
 * @since october 09, 2002
 */
public class DirContextAdapter extends ContextAdapter implements DirContext {

    /**
	 * Binds a name to an object, along with associated attributes.  
	 */
    public void bind(Name name, Object obj, Attributes attrs) throws NamingException {
    }

    /**
	 * Binds a name to an object, along with associated attributes.
	 */
    public void bind(String name, Object obj, Attributes attrs) throws NamingException {
    }

    /** 
	 * Creates and binds a new context, along with associated attributes.
	 */
    public DirContext createSubcontext(Name name, Attributes attrs) throws NamingException {
        return null;
    }

    /** 
	 * Creates and binds a new context, along with associated attributes.  
	 */
    public DirContext createSubcontext(String name, Attributes attrs) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves all of the attributes associated with a named object.
	 */
    public Attributes getAttributes(Name name) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves selected attributes associated with a named object.  
	 */
    public Attributes getAttributes(Name name, String[] attrIds) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves all of the attributes associated with a named object.  
	 */
    public Attributes getAttributes(String name) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves selected attributes associated with a named object.  
	 */
    public Attributes getAttributes(String name, String[] attrIds) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves the schema associated with the named object.  
	 */
    public DirContext getSchema(Name name) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves the schema associated with the named object.  
	 */
    public DirContext getSchema(String name) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves a context containing the schema objects of the named object's class definitions.  
	 */
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        return null;
    }

    /** 
	 * Retrieves a context containing the schema objects of the named object's class definitions.  
	 */
    public DirContext getSchemaClassDefinition(String name) throws NamingException {
        return null;
    }

    /** 
	 * Modifies the attributes associated with a named object.  
	 */
    public void modifyAttributes(Name name, int mod_op, Attributes attrs) throws NamingException {
    }

    /** 
	 * Modifies the attributes associated with a named object using an ordered list of modifications.  
	 */
    public void modifyAttributes(Name name, ModificationItem[] mods) throws NamingException {
    }

    /** 
	 * Modifies the attributes associated with a named object.  
	 */
    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException {
    }

    /** 
	 * Modifies the attributes associated with a named object using an ordered list of modifications.  
	 */
    public void modifyAttributes(String name, ModificationItem[] mods) throws NamingException {
    }

    /** 
	 * Binds a name to an object, along with associated attributes, overwriting any existing binding.  
	 */
    public void rebind(Name name, Object obj, Attributes attrs) throws NamingException {
    }

    /** 
	 * Binds a name to an object, along with associated attributes, overwriting any existing binding.  
	 */
    public void rebind(String name, Object obj, Attributes attrs) throws NamingException {
    }

    /** 
	 * Searches in a single context for objects that contain a specified
	 * set of attributes.
	 */
    public NamingEnumeration search(Name name, Attributes matchingAttributes) throws NamingException {
        return null;
    }

    /** 
	 * Searches in a single context for objects that contain a specified
	 * set of attributes, and retrieves selected attributes.
	 */
    public NamingEnumeration search(Name name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException {
        return null;
    }

    /** 
	 * Searches in the named context or object for entries that satisfy
	 * the given search filter.
	 */
    public NamingEnumeration search(Name name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return null;
    }

    /** 
	 * Searches in the named context or object for entries that satisfy
	 * the given search filter.
	 */
    public NamingEnumeration search(Name name, String filter, SearchControls cons) throws NamingException {
        return null;
    }

    /** 
	 * Searches in a single context for objects that contain a specified
	 * set of attributes.
	 */
    public NamingEnumeration search(String name, Attributes matchingAttributes) throws NamingException {
        return null;
    }

    /** 
	 * Searches in a single context for objects that contain a specified
	 * set of attributes, and retrieves selected attributes.
	 */
    public NamingEnumeration search(String name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException {
        return null;
    }

    /** 
	 * Searches in the named context or object for entries that satisfy
	 * the given search filter.
	 */
    public NamingEnumeration search(String name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return null;
    }

    /** 
	 * Searches in the named context or object for entries that satisfy
	 * the given search filter.
	 */
    public NamingEnumeration search(String name, String filter, SearchControls cons) throws NamingException {
        return null;
    }
}
