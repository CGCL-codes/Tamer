// ----------------------------------------------------------------------
// LdapAgentDirectoryService.java - 
//
//    Copyright © 2001
//       S. Ushijima
//       Fujitsu Laboratories of America, Inc.
//
//  $Id: LdapAgentDirectoryService.java,v 1.1 2002/04/19 13:44:51 bdehora Exp $
//
//  This material is a part of a package and your use is subject
//  to the terms and conditions of the Open Specification License.
//  You should have received a copy of the Open Specification License
//  with the package containing this file.  However, a copy may also
//  be obtained from Fujitsu Laboratories of America 595 Lawerence
//  Expressway, Sunnyvale CA 94085.
//
//  This file is distributed for general use, and is provided "as-is"
//  without any warranty either express or implied.  See the Open
//  Specification License for details.
//
//  This material IS provided under the terms of the Common Public
//  License ("agreement").  Any use, reproduction or distribution of
//  the program constitutes recipient's acceptance of this agreement.
//  You should have received a copy of the Common Public License with
//  the package containing this file.  However a copy can also be
//  obtained at the URL: http://oss.software.ibm.com/developerworks/
//  opensource/license-cpl.html
// ----------------------------------------------------------------------

package org.jagent.service.directory.ldap;

import java.io.*;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import javax.agent.*;
import javax.agent.service.*;
import javax.agent.service.directory.*;

import org.jagent.service.directory.*;

/**
 * The <CODE>LdapAgentDirectoryService</CODE> is an implementation of
 * <CODE>AgentDirectoryService</CODE> by using LDAP.
 * This class accesses LDAP server by JNDI interface.
 * In this implementation, <CODE>AgentDescription</CODE> passed to
 * methods as the argument must be <CODE>Serializable</CODE>.
 * @see AgentDirectoryService
 */
public class LdapAgentDirectoryService implements AgentDirectoryService {
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static String encode(Object obj) {
	try {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(baos);
	    oos.writeObject(obj);
	    oos.flush();

	    return encoder.encodeBuffer(baos.toByteArray());
	} catch (Exception e) {
	    return null;
	}
    }

    private static BASE64Decoder decoder = new BASE64Decoder();
    private static Object decode(String encoded) {
	try {
	    byte[] bytes = decoder.decodeBuffer(encoded);
	    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	    ObjectInputStream ois = new ObjectInputStream(bais);

	    return (Object)ois.readObject();
	} catch (Exception e) {
	    return null;
	}
    }

    private static Attributes desc2attrs(AgentDescription desc) {
	BasicAttributes attrs = new BasicAttributes();
	Enumeration enum = desc.keys();
	while (enum.hasMoreElements()) {
	    String key = (String)enum.nextElement();
	    Object obj = desc.get(key);
	    // If Java objects can be stored to attrs by arbitrary keys
	    // the encoding is not needed.
	    attrs.put(new BasicAttribute(key, encode(obj)));
	}

	return attrs;
    }

    private static AgentDescription attrs2desc(Attributes attrs) {
	AgentDescription desc = new BasicAgentDescription();
	NamingEnumeration enum = attrs.getIDs();
	try {
	    while (enum.hasMore()) {
		String key = (String)enum.next();
		Attribute attr = attrs.get(key);
		String encoded = null;
		try {
		    encoded = (String)attr.get();
		} catch (ClassCastException e) {
		    // This is an attribute added by LDAP server
		    continue;
		}
		Object value = decode(encoded);
		if (value != null) {
		    desc.set(key, value);
		}
	    }
	} catch (NamingException e) {
	}

	return desc;
    }
    
    DirContext dctx = null;

    /**
     * Creates <CODE>LdapAgentDirectoryService</CODE> instance.
     * @param dctx an instance of <CODE>DirContext</CODE>
     * which is connected to an LDAP context.
     */
    public LdapAgentDirectoryService(DirContext dctx) {
	this.dctx = dctx;
    }

    private ServiceProperties props = null;

    /**
     * Returns service properties have been stored to
     * this <CODE>LdapAgentDirectoryService</CODE>.
     * @return service properties of this <CODE>LdapAgentDirectoryService</CODE>
     * @exception ServiceException 
     * @exception ServiceFailure
     */
    public ServiceProperties getServiceProperties()
	throws ServiceException, ServiceFailure {
	return props;
    }

    /**
     * Sets service properties to this <CODE>LdapAgentDirectoryService</CODE>.
     * @param props service properties to be set
     */
    public void setServiceProperties(ServiceProperties props)
	throws ServiceException, ServiceFailure {
	this.props = props;
    }

	 public AgentDescription createAgentDescription() {
		 return new org.jagent.service.directory.BasicAgentDescription();
	 }


    /**
     * Registers the <CODE>AgentDescription</CODE> to the directory context.
     * The <CODE>AgentDescription</CODE> is translated to an <CODE>Attributes</CODE>
     * of JNDI and then registered to the directory.
     * @param desc an instance of a class which implements
     * <CODE>AgentDescription</CODE> interface
     * @exception AlreadyRegisteredException thrown if the AgentName
     * of the given AgentDescription is already registered
     * @exception DirectoryFailure if the directory service is not available
     */
    public void register(AgentDescription desc)
	throws AlreadyRegisteredException, DirectoryFailure {
	// Should hashcode() of the AgentName be used?
	AgentName name = desc.getAgentName();
	if (name == null) {
	    throw new DirectoryFailure("AgentName is empty");
	}

	try {
	    Attributes attrs = desc2attrs(desc);

	    dctx.bind("cn=" + name, desc, attrs);
	    //dctx.rebind("cn=" + name, desc, attrs);
	} catch (NameAlreadyBoundException e) {
	    throw new AlreadyRegisteredException(name.toString());
	} catch (Exception e) {
	    throw new DirectoryFailure(name.toString(), e);
	}
    }

    /**
     * Deregisters the <CODE>AgentDescription</CODE> from the directory context.
     * @param desc an instance of a class which implements
     * <CODE>AgentDescription</CODE> interface
     * @exception NotRegisteredException thrown if the AgentName of the
     * given AgentDescription is not registered
     * @exception DirectoryFailure if the directory service is not available
     */
    public void deregister(AgentDescription desc) 
	throws NotRegisteredException, DirectoryFailure {
	// Should hashcode() of the AgentName be used?
	AgentName name = desc.getAgentName();
	if (name == null) {
	    throw new DirectoryFailure("AgentName is empty");
	}

	try {
	    dctx.unbind("cn=" + name);
	} catch (NameNotFoundException e) {
	    throw new NotRegisteredException(name.toString());
	} catch (Exception e) {
	   throw new DirectoryFailure(name.toString(), e);
	}
    }

    /**
     * Modifys an AgentDescription by the specified one.
     * The <CODE>AgentDescription</CODE> is translated to an <CODE>Attributes</CODE>
     * of JNDI and then sent to the directory.
     * @param desc the AgentDescription to overlay
     * @exception NotRegisteredException thrown if the AgentName of the given
     * AgentDescription is not registered
     * @exception DirectoryFailure if the directory service is not available
     */
    public void modify(AgentDescription desc)
	throws NotRegisteredException, DirectoryFailure {
	AgentName name = desc.getAgentName();
	if (name == null) {
	    throw new DirectoryFailure("AgentName is empty");
	}

	try {
	    Attributes attrs = desc2attrs(desc);

	    dctx.modifyAttributes("cn=" + name, DirContext.REPLACE_ATTRIBUTE, attrs);
	} catch (NameNotFoundException e) {
	    throw new NotRegisteredException(name.toString());
	} catch (Exception e) {
	    throw new DirectoryFailure(name.toString(), e);
	}
    }

    /**
     * Searches the collection of AgentDescriptions.
     * The <CODE>AgentDescription</CODE> is translated to an <CODE>Attributes</CODE>
     * of JNDI and then sent to the directory.
     * @param desc the AgentDescription to use as a template for searching
     * @return an array of <CODE>AgentDescription</CODE>
     * @exception SearchException if an exception occurs during searching
     * @exception DirectoryFailure if the directory service is not available
     */
    public AgentDescription[] search(AgentDescription desc)
	throws SearchException, DirectoryFailure {
	try {
	    Attributes attrs = desc2attrs(desc);

	    NamingEnumeration ans = dctx.search("", attrs);
	    Vector vec = new Vector();

	    while (ans.hasMore()) {
		SearchResult sr = (SearchResult)ans.next();
		//printAttrs(sr.getAttributes());

		//Because sr.getObject() returns null, the AgentDescription
		//must be retrieved by Attributes
		AgentDescription desc0 = attrs2desc(sr.getAttributes());

		vec.addElement(desc0);
	    }

	    AgentDescription[] descs = new AgentDescription[vec.size()];
	    vec.copyInto(descs);
	    return descs;
	} catch (NamingException e) {
	    throw new SearchException("");
	} catch (Exception e) {
	    throw new DirectoryFailure("", e);
	}
    }

    /**
     * Searches the collection of AgentDescriptions.
     * The number of returned AgentDescriptions is limited
     * by the second argument.
     * The <CODE>AgentDescription</CODE> is translated to an <CODE>Attributes</CODE>
     * of JNDI and then sent to the directory.
     * @param desc the AgentDescription to use as a template for searching
     * @param maxResults the constraints to limit the search
     * @return an array of <CODE>AgentDescription</CODE>
     * @exception SearchException if an exception occurs during searching
     * @exception DirectoryFailure if the directory service is not available
     */
    public AgentDescription[] search(AgentDescription desc, int maxResults)
	throws SearchException, DirectoryFailure {
	try {
	    Attributes attrs = desc2attrs(desc);

	    NamingEnumeration ans = dctx.search("", attrs);
	    Vector vec = new Vector();

	    for (int i = 0; i < maxResults && ans.hasMore(); i++) {
		SearchResult sr = (SearchResult)ans.next();
		//printAttrs(sr.getAttributes());

		//Because sr.getObject() returns null, the AgentDescription
		//must be retrieved by Attributes
		AgentDescription desc0 = attrs2desc(sr.getAttributes());

		vec.addElement(desc0);
	    }

	    AgentDescription[] descs = new AgentDescription[vec.size()];
	    vec.copyInto(descs);
	    return descs;
	} catch (NamingException e) {
	    throw new SearchException("");
	} catch (Exception e) {
	    throw new DirectoryFailure("", e);
	}
    }

    /**
     * Called by JVM when this <CODE>LdapAgentDirectoryService</CODE> is garbage-collected.
     * @exception Throwable thrown if some exception or error happens in this method
     */
    protected void finalize() throws Throwable {
	dctx.close();
    }
}

/*
 * $Log: LdapAgentDirectoryService.java,v $
 * Revision 1.1  2002/04/19 13:44:51  bdehora
 * big renaming: move ri to the org.jagent packagespace
 *
 * Revision 1.3  2002/01/04 23:56:44  aspydell
 * Global touch/modify.
 *
 */
