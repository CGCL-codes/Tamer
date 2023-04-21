/* 
   $Header: /cvsroot/likken/likken/src/tests/org/likken/test/parser/ConstantsTest.java,v 1.1.1.1 2000/12/07 00:12:43 sbn Exp $

   Likken - An user-friendly interface for managing a LDAP directory
   Copyright (C) 2000 Stephane Boisson, Foc@l.Net
   
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package org.likken.test.parser;

import junit.framework.*;
import org.likken.util.parser.*;

import org.apache.regexp.*;


/**
 * @author Stephane Boisson <s.boisson@focal-net.com> 
 * @version $Revision: 1.1.1.1 $ $Date: 2000/12/07 00:12:43 $
 */
public class ConstantsTest extends TestCase {

    public ConstantsTest( String aName ) {
	super( aName );
    }

    public void testRegexpDescriptor() {
	try {
	    RE pattern = new RE( Constants.DESCRIPTOR_PATTERN );
	    assert( " SIMPLE ", !pattern.match( " SIMPLE " ) );
	    assert( "SIMPLE", pattern.match( "SIMPLE" ) );
	    assert( "SIMPLE-test", pattern.match( "SIMPLE-test" ) );
	    assert( "another simple test", !pattern.match( "another simple test" ) );
	    assert( "abcdefghijklmnopqrstuvxyz-0123456789;test", pattern.match( "abcdefghijklmnopqrstuvxyz-0123456789;test" ) );
	    assert( "0123456789", !pattern.match( "0123456789" ) );
	    assert( "11.22.33.44", !pattern.match( "11.22.33.44" ) );
	    assert( " 11.22.33.44 ", !pattern.match( " 11.22.33.44 " ) );
	    assert( "01.02.03.04", !pattern.match( "01.02.03.04" ) );
	    assert( "11.22.33.44{55}", !pattern.match( "11.22.33.44{55}" ) );
	    assert( " 11.22.33.44{55} ", !pattern.match( " 11.22.33.44{55} " ) );
	}
	catch( final RESyntaxException ex ) {
	    fail( "Unexpected exception: " + ex.toString() );	    
	}
    }

    public void testRegexpNumericOid() {
	try {
	    RE pattern = new RE( Constants.NUMERIC_OID_PATTERN );
	    assert( " SIMPLE ", !pattern.match( " SIMPLE " ) );
	    assert( "SIMPLE", !pattern.match( "SIMPLE" ) );
	    assert( "SIMPLE-test", !pattern.match( "SIMPLE-test" ) );
	    assert( "another simple test", !pattern.match( "another simple test" ) );
	    assert( "abcdefghijklmnopqrstuvxyz-0123456789;test", !pattern.match( "abcdefghijklmnopqrstuvxyz-0123456789;test" ) );
	    assert( "0123456789", !pattern.match( "0123456789" ) );
	    assert( "11.22.33.44", pattern.match( "11.22.33.44" ) );
	    assert( " 11.22.33.44 ", !pattern.match( " 11.22.33.44 " ) );
	    assert( "01.02.03.04", !pattern.match( "01.02.03.04" ) );
	    assert( "11.22.33.44{55}", !pattern.match( "11.22.33.44{55}" ) );
	    assert( " 11.22.33.44{55} ", !pattern.match( " 11.22.33.44{55} " ) );
	}
	catch( final RESyntaxException ex ) {
	    fail( "Unexpected exception: " + ex.toString() );	    
	}
    }

    public void testRegexpNumericOidLen() {
 	try {
	    RE pattern = new RE( Constants.NUMERIC_OID_LEN_PATTERN );
	    assert( " SIMPLE ", !pattern.match( " SIMPLE " ) );
	    assert( "SIMPLE", !pattern.match( "SIMPLE" ) );
	    assert( "SIMPLE-test", !pattern.match( "SIMPLE-test" ) );
	    assert( "another simple test", !pattern.match( "another simple test" ) );
	    assert( "abcdefghijklmnopqrstuvxyz-0123456789;test", !pattern.match( "abcdefghijklmnopqrstuvxyz-0123456789;test" ) );
	    assert( "0123456789", !pattern.match( "0123456789" ) );
	    assert( "11.22.33.44", !pattern.match( "11.22.33.44" ) );
	    assert( " 11.22.33.44 ", !pattern.match( " 11.22.33.44 " ) );
	    assert( "01.02.03.04", !pattern.match( "01.02.03.04" ) );
	    assert( "11.22.33.44{55}", pattern.match( "11.22.33.44{55}" ) );
	    assert( " 11.22.33.44{55} ", !pattern.match( " 11.22.33.44{55} " ) );
	}
	catch( final RESyntaxException ex ) {
	    fail( "Unexpected exception: " + ex.toString() );	    
	}
   }
    
}
