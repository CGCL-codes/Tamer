package com.simonstl.moe.namespace;

import com.simonstl.moe.*;
import com.simonstl.moe.explicit.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.Attributes;

/**
* <p>The NamespaceContextI interface is a replacement for the current 
*NamespaceContextI interface.  Once this stabilizes and has a real 
*implementation, it will replace NamespaceContextI.</p>
*
*<p>The NamespaceContextI interface provides an API for managing the 
*namespace stack. It provides methods to access the current set 
*of namespaces as well as to manage the stack.  Most applications 
*should probably use the listen(CoreComponentI component) method 
*of the {@link com.simonstl.moe.ComponentInputI ComponentInputI} 
*interface rather than managing the stack manually, but manual 
*management may be appropriate for applications that need to 
*create an initial context before using it.</p>
*
* <p>Version 0.02 changes from Namespace to NamespaceDecl.</p>
* <p>Version 0.01 is the foundation of the interface.</p>
*
* @version 0.01 17 February 2002
* @author Simon St.Laurent
*/
public interface NamespaceContextI {

    /** Declares a mapping between a prefix and a URI at the current tree level.**/
    public void declarePrefix(String prefix, String URI);

    /** Declares a mapping between a prefix and a URI at the current tree level. **/
    public void declarePrefix(NamespaceDecl namespace);

    /** Given a URI, it returns the first prefix it finds matching it.  Because multiple namespace prefixes may be mapped to a given URI, this may not return the expected prefix.**/
    public String getURI(String prefix);

    /** Given a prefix, it returns the URI to which that prefix maps.  This is a processed set of namespaces applying to a current context, so only one URI will be returned.**/
    public String getPrefix(String URI);

    /** Returns an enumeration of all the prefixes and URIs in the current context, keyed to prefix.  This may return multiple prefixes mapped to the same URI.**/
    public HashMap getNamespacesByPrefixes();

    /** Returns an enumeration of all the prefixes and URIs in the current context, keyed to URI. Each URI will be mapped to only one prefix, the most recent declaration.**/
    public HashMap getNamespacesByURIs();

    /** Returns a full set of namespace declarations for the current context.**/
    public String getFullDeclaration();

    /**Not sure if this goes here or in NamespaceSetI or anywhere **/
    public void setParent(CoreComponentI _parent);

    /** pushLevel() is called when an element starts, and doesn't have much to do.*/
    public void pushLevel();

    /** popLevel() is called when an element ends, and strips out old namespace declarations which no longer apply.*/
    public void popLevel();

    /** getLevel() returns the current position of the stack.  Mostly useful for debugging.*/
    public int getLevel();

    /** collectContext() builds a new namespaceContextI object by walking up the tree from the component used as a parameter.*/
    public NamespaceContextI collectContext(CoreComponentI component);

    /** isDeclared() says whether a particular URI/prefix combination has been declared.*/
    public boolean isDeclared(String prefix, String URI);

    /** reset() is called when the object needs to be emptied for reuse.*/
    public void reset();
}
