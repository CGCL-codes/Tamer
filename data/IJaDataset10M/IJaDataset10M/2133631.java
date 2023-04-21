package com.simonstl.moe.factory;

import com.simonstl.moe.*;
import com.simonstl.moe.explicit.*;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.SecurityException;
import java.lang.ClassCastException;
import java.util.StringTokenizer;

/**
* <p>The MutableQFactory class provides a relatively simple mechanism for creating new MOE components, using namespace URI and QName instead of of namspace URI, local name, prefix.  I genuinely detest the use of QNames in many contexts, but seem folks seem to prefer them.</p>
*
* <p>The MutableQFactory class is just a wrapper around MutableFactory.</p>
*
*<p>Version 0.02 changes the Namespace class to NamespaceDecl.  (Class name change only.)
*
* @version 0.02 17 February 2002
* @author Simon St.Laurent
*/
public class MutableQFactory {

    /**
Divides a QName into two pieces.
**/
    public static String[] breakQName(String QName) {
        String[] returnVal = new String[2];
        StringTokenizer parts = new StringTokenizer(QName, ":");
        if (parts.countTokens() == 1) {
            returnVal[0] = null;
            returnVal[1] = QName;
        }
        if (parts.countTokens() == 2) {
            returnVal[0] = parts.nextToken();
            returnVal[1] = parts.nextToken();
        }
        return returnVal;
    }

    /**
Creates an empty element with a namespace URI, local name, and prefix.
**/
    public static MutableComponentI element(String _nsURI, String _QName) throws MOEException {
        String[] breaker = breakQName(_QName);
        String _prefix = breaker[0];
        String _localName = breaker[1];
        MutableComponentI returnVal = MutableFactory.element(_nsURI, _localName, _prefix);
        return returnVal;
    }

    /**
Creates a complete element with a namespace URI, local name, prefix, and a text node.
**/
    public static MutableComponentI element(String _nsURI, String _QName, String _text) throws MOEException {
        MutableComponentI returnVal = element(_nsURI, _QName);
        returnVal.setContent(_text);
        returnVal.setIsComplete(true);
        return returnVal;
    }

    /**
Creates an element with a namespace URI, local name, prefix, and a text node, and allows the caller to specify whether or not the element is complete.
**/
    public static MutableComponentI element(String _nsURI, String _QName, String _text, boolean _complete) throws MOEException {
        MutableComponentI returnVal = element(_nsURI, _QName, _text);
        returnVal.setIsComplete(_complete);
        return returnVal;
    }

    /**
Creates an empty attribute with a namespace URI, local name, and prefix.
**/
    public static MutableComponentI attribute(String _nsURI, String _QName) throws MOEException {
        String[] breaker = breakQName(_QName);
        String _prefix = breaker[0];
        String _localName = breaker[1];
        MutableComponentI returnVal = MutableFactory.attribute(_nsURI, _localName, _prefix);
        return returnVal;
    }

    /**
Creates an attribute with a namespace URI, local name, prefix, and a text node.
**/
    public static MutableComponentI attribute(String _nsURI, String _QName, String _text) throws MOEException {
        MutableComponentI returnVal = attribute(_nsURI, _QName);
        returnVal.setContent(_text);
        returnVal.setIsComplete(true);
        return returnVal;
    }

    /**
Creates an attribute with a namespace URI, local name, prefix, and a text node, and allows the caller to specify whether or not the element is complete.
**/
    public static MutableComponentI attribute(String _nsURI, String _QName, String _text, boolean _complete) throws MOEException {
        MutableComponentI returnVal = attribute(_nsURI, _QName, _text);
        returnVal.setIsComplete(_complete);
        return returnVal;
    }

    /**
Creates a text node with textual content.
**/
    public static MutableComponentI characters(String _text) throws MOEException {
        MutableComponentI returnVal = MutableFactory.characters(_text);
        return returnVal;
    }

    /**
Creates a complete Processing Instruction with target and textual content.
**/
    public MutableComponentI procInst(String _target, String _text) throws MOEException {
        MutableComponentI returnVal = MutableFactory.procInst(_target, _text);
        return returnVal;
    }

    /**
Creates a Processing Instruction with target and textual content, allowing the code to specify whether or not it is complete.
**/
    public MutableComponentI procInst(String _target, String _text, boolean _complete) throws MOEException {
        MutableComponentI returnVal = procInst(_target, _text);
        returnVal.setIsComplete(_complete);
        return returnVal;
    }

    /**
Creates a complete Comment with textual content.
**/
    public static MutableComponentI comment(String _text) throws MOEException {
        MutableComponentI returnVal = MutableFactory.comment(_text);
        return returnVal;
    }

    /**
Creates a comment with textual content, allowing the code to specify whether or not it is complete.
**/
    public static MutableComponentI comment(String _text, boolean _complete) throws MOEException {
        MutableComponentI returnVal = comment(_text);
        returnVal.setIsComplete(_complete);
        return returnVal;
    }

    /**
Creates a complete CDATA section with textual content.
**/
    public static MutableComponentI cdataSect(String _text) throws MOEException {
        MutableComponentI returnVal = MutableFactory.cdataSect(_text);
        return returnVal;
    }

    /**
Creates a CDATA section with textual content, allowing the code to specify whether or not it is complete.
**/
    public static MutableComponentI cdataSect(String _text, boolean _complete) throws MOEException {
        MutableComponentI returnVal = cdataSect(_text);
        returnVal.setIsComplete(_complete);
        return returnVal;
    }

    /**
Creates a whitespace node with content.
**/
    public static MutableComponentI whitespace(String _text) throws MOEException {
        MutableComponentI returnVal = MutableFactory.whitespace(_text);
        return returnVal;
    }

    /**
Creates a namespace.
**/
    public static MutableComponentI namespace(String _nsURI, String _prefix) throws MOEException {
        MutableComponentI returnVal = MutableFactory.namespace(_nsURI, _prefix);
        return returnVal;
    }

    /**
Creates a default.
**/
    public static MutableComponentI namespace(String _nsURI) throws MOEException {
        return namespace(_nsURI, null);
    }

    /**
Creates an end element with a namespace URI, local name, and prefix.
**/
    public static MutableComponentI endElement(String _nsURI, String _QName) throws MOEException {
        String[] breaker = breakQName(_QName);
        String _prefix = breaker[0];
        String _localName = breaker[1];
        MutableComponentI returnVal = MutableFactory.endElement(_nsURI, _localName, _prefix);
        return returnVal;
    }

    /**
Creates an empty attribute with a namespace URI, local name, and prefix.
**/
    public static MutableComponentI endAttribute(String _nsURI, String _QName) throws MOEException {
        String[] breaker = breakQName(_QName);
        String _prefix = breaker[0];
        String _localName = breaker[1];
        MutableComponentI returnVal = MutableFactory.endAttribute(_nsURI, _localName, _prefix);
        return returnVal;
    }

    /**
Creates an end to a namespace.
**/
    public static MutableComponentI endNamespace(String _nsURI, String _prefix) throws MOEException {
        MutableComponentI returnVal = MutableFactory.endNamespace(_nsURI, _prefix);
        return returnVal;
    }

    /**
Creates an end to a CDATA section.
**/
    public static MutableComponentI endCdataSect() throws MOEException {
        MutableComponentI returnVal = MutableFactory.endCdataSect();
        return returnVal;
    }

    public static MutableComponentI endProcInst() throws MOEException {
        MutableComponentI returnVal = MutableFactory.endProcInst();
        return returnVal;
    }

    public static MutableComponentI endComment() throws MOEException {
        MutableComponentI returnVal = MutableFactory.endComment();
        return returnVal;
    }

    public static void main(String[] args) throws Exception {
        ComponentQFactory cf = new ComponentQFactory();
        System.out.println("Incomplete element, no text.");
        Element test1 = (Element) cf.element("http://simonstl.com/ns/test", "prefix:test");
        test1.print();
        System.out.println("Incomplete element, no text.");
        Element test2 = (Element) cf.element("http://simonstl.com/ns/test", "prefix:test", "textual content", false);
        test2.print();
        System.out.println("Complete element, no text.");
        Element test3 = (Element) cf.element("http://simonstl.com/ns/test", "prefix:test", "textual content");
        test3.print();
        System.out.println("Attribute, no text.");
        Attribute test4 = (Attribute) cf.attribute("http://simonstl.com/ns/test", "prefix:testAtt1");
        test4.print();
        System.out.println("Attribute, with text.");
        Attribute test5 = (Attribute) cf.attribute("http://simonstl.com/ns/test", "prefix:testAtt2", "texties");
        test5.print();
        System.out.println("Attributes added to complete element.");
        test3.addUnorderedContent(test4);
        test3.addUnorderedContent(test5);
        test3.print();
        System.out.println("Attributes added repeatedly to complete element.");
        test3.addUnorderedContent(test4);
        test3.addUnorderedContent(test5);
        test3.print();
        System.out.println("CDATA section with text, incomplete.");
        CDATASect test6 = (CDATASect) cf.cdataSect("weird content, ><", false);
        test6.print();
        System.out.println("CDATA section with text, complete.");
        CDATASect test7 = (CDATASect) cf.cdataSect("weird content, ><");
        test7.print();
        System.out.println("Characters with text");
        Characters test8 = (Characters) cf.characters("some content");
        test8.print();
        System.out.println("Namespace with prefix");
        NamespaceDecl test9 = (NamespaceDecl) cf.namespace("http://simonstl.com", "goon");
        test9.print();
        System.out.println("Namespace without prefix");
        NamespaceDecl test10 = (NamespaceDecl) cf.namespace("http://simonstl.com");
        test10.print();
    }
}
