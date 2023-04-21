// ----------------------------------------------------------------------
// QueueManager.java - description
//
//    Copyright ©  2001
//       S. Ushijima
//       D. Greenwood
//       Fujitsu Laboratories of America, Inc.
//
//  $Id: QueueManager.java,v 1.1 2002/04/19 13:45:10 bdehora Exp $
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


package org.jagent.service.transport.http;

import java.util.*;

import org.jagent.util.*;

/**
 * The <CODE>QueueManager</CODE> manages registration and
 * de-registration of queues.
 * @see MessageQueue
 */
class QueueManager {
    private Hashtable qhash;

    /**
     * Creates the <CODE>QueueManager</CODE> instance.
     */
    QueueManager() {
	qhash = new Hashtable();
    }

    /**
     * Creates a new queue and binds it to the specified name.
     * @param name a name to be bound to a new queue
     * @return an instance of <CODE>MessageQueue</CODE>
     * @exception IllegalStateException thrown if the specified name
     * is already in use
     */
    MessageQueue register(String name) throws IllegalStateException {
	synchronized (qhash) {
	    if (qhash.containsKey(name)) {
		throw new IllegalStateException("name already in use");
	    }

	    MessageQueue queue = new MessageQueue();
	    qhash.put(name, queue);
	    return queue;
	}
    }

    static int counter = 0;
    static synchronized String getUniqueString() {
	return "a" + System.currentTimeMillis() + counter++;
    }

    /**
     * Creates a new queue and binds it to an automatically generated name.
     * @return an instance of <CODE>MessageQueue</CODE>
     */
    MessageQueue register() {
	try {
	    return register(getUniqueString());
	} catch (IllegalStateException e) {
	    // should not happen;
	    return null;
	}
    }

    /**
     * Deletes a queue bound to the specified name.
     * @param name the name of a queue to be deleted
     */
    void deregister(String name) {
	qhash.remove(name);
    }

    /**
     * Deletes a specified queue.
     * @param queue an instance of queue
     */
    void deregister(MessageQueue queue) {
	Enumeration enum = qhash.keys();
	while (enum.hasMoreElements()) {
	    String key = (String)enum.nextElement();
	    Object obj = qhash.get(key);
	    if (obj.equals(queue)) {
		qhash.remove(key);
		break;
	    }
	}
    }

    /**
     * Returns a queue bound to the specified name.
     * @param the name of a queue to be returned
     * @return a queue bound to the specified name
     * @exception IllegalStateException thrown if there is not a queue
     * which is bound to the specified name
     */
    MessageQueue get(String name) throws IllegalStateException {
	MessageQueue queue = null;
	if ((queue = (MessageQueue)qhash.get(name)) != null) {
	    return queue;
	}

	throw new IllegalStateException(name);
    }
}

/*
 * $Log: QueueManager.java,v $
 * Revision 1.1  2002/04/19 13:45:10  bdehora
 * big renaming: move ri to the org.jagent packagespace
 *
 * Revision 1.3  2002/01/07 16:10:09  aspydell
 * Fixed the author attributes.
 *
 * Revision 1.2  2002/01/04 23:56:45  aspydell
 * Global touch/modify.
 *
 */
