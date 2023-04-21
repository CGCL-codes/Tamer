/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Standardized Content Archive Management (SCAM).
 *
 * The Initial Developer of the Original Code is
 * Swedish National Agency for Education (Skolverket).
 *
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * Jan Danils
 * Jöran Stark
 * Fredrik Paulsson
 * Matthias Palmér
 * Mikael Nilsson
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 * ***** END LICENSE BLOCK ******/
 
package scam.webdav.httpmethod;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import scam.*;
import scam.share.*;
import scam.repository.*;
import scam.webdav.share.*;
import scam.webdav.util.*;

/**
 * PASTE Method. Paste resources stored in session.
 *
 * @author Jan Danils
 * @author Jöran Stark
 * @version $Revision: 1.1.1.1 $
 */
public class PasteMethod extends PropFindMethod {
    // Attributes
    private String destUri = null;
    private boolean overwrite = false;

    // Associations
    
    // Operations
    public PasteMethod(GenericServlet servlet, User usr,
		       HttpServletRequest req, HttpServletResponse resp) {
	super(servlet, usr, req, resp);
    }

    protected void parseRequest() throws WebdavException {
	destUri = URLUtil.URLDecode( req.getParameter(DESTINATION) );
	
	if(destUri == null) {
	    destUri = requestUri;
	}
	
	String overwriteStr = req.getParameter(OVERWRITE);
	if(overwriteStr != null && overwriteStr.equalsIgnoreCase(TRUE))
	    overwrite = true;
    }
    
    /** An operation that does...
     *
     * @param firstParam a description of this parameter
     */
    protected void executeRequest() throws WebdavException, FatalException {
	String message = new String();
	String op = (String) session.getAttribute(SESS_OP);
	Vector sourceV = (Vector)session.getAttribute(SESS_SOURCE);

	if(op == null || sourceV == null) {
	    message = "Clipboard is empty.";
	    DEBUG.println("PasteMethod: Failed: "+message);
	    doPropFind(destUri, 1, message);
	    return;
	}
	
	// Fetch destination resource (parent).
	Resource destRes = null;
	try {
	    destRes = iFactory.getResource(destUri);
	    
	} catch (ResourceException e) {
	    message = getFailedAsString(message, destUri, e);
	    DEBUG.println("PasteMethod: Failed: "+message);
	    doPropFind(destUri, 1, message);
	    return;
	}
	
	// Copy/Move resources.
	Enumeration enum = sourceV.elements();
	while(enum.hasMoreElements()) {
	    String currUri = (String)enum.nextElement();
	    NTreeNode tree = null;

	    try {
		Resource srcRes = iFactory.getResource(currUri);
		
		DEBUG.println("PasteMethod: Pasting '"+currUri+"' ["+
			      (op.equals(CUT)?"MOVE":"COPY")+"].");
		
		if(op.equals(CUT)) {
		    tree = srcRes.move(destRes, overwrite);
		} else if (op.equals(COPY)) {
		    tree = srcRes.copy(destRes, overwrite);
		} else {
		    throw new FatalException("PasteMethod: Unknown operation.");
		}
		
		if(tree.hasFailedNodes()) {
		    message += getFailedAsString(tree);
		}
	    } catch (ResourceException e) {
		message = getFailedAsString(message, currUri, e);
	    }
	}
	
	// Clear session variables!
	session.removeAttribute(SESS_OP);
	session.removeAttribute(SESS_SOURCE);
	
	if(message != null && !message.equals(""))
	    DEBUG.println(DEBUG.MEDIUM, "PasteMethod: Message: "+message);
	
	doPropFind(null, 1, message);
    }
    
    /** An operation that does...
     *
     * @param firstParam a description of this parameter
     */
    public boolean requiresAuthentication() {  
	return true;
    }

} /* end class MoveMethod */
