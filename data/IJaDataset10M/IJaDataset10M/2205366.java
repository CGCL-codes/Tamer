/*
 * Copyright (C) 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Author: Daniel Sun (sunh11373@hotmail.com).
 */
package com.ark.fix.model.fixml;

import com.ark.fix.model.*;


public class CustomerFirm extends FIXMLField {

    public static final String TAG = "204";
    public static final int DATA_TYPE = FIXDataConverter.STRING;
    public static final String ENUM_Customer = "0";
    public static final String ENUM_Firm = "1";

    public static final String ENUM = "|0|1";
    
    private String _Value;

	public String getValue() {
	    return _Value;
	}
	
	public void setValue(String s) throws ModelException {
	    if ( s == null || s.indexOf('|') != -1 || ENUM.indexOf(s) == -1 )
	        throw new ModelException("Invalid value (" + s + ") for field CustomerFirm.");
        _Value = s;	        
	}
	
	// called only by parser
	public void initValue(Object s) throws ModelException {
	    if ( _Value != null ) 
	        throw new ModelException("Value has already been initialized for field CustomerFirm.");
        setValue((String)s);	        
	}
	
	public String getTagValue() {
	    return _Value;
	}
	
	public void setTagValue(String s) throws ModelException {
        setValue(s);	        
	}
	

    public String getTag() {
        return TAG;
    }

    public int getTagDataType() {
        return DATA_TYPE;
    }
    
    public String[] getTagValueEnum() {
        String[] enum = {
            "0",
            "1",
        };
        return enum;
    }
	
	public String toFIXMessage() {
	    if ( _Value == null || _Value.length() == 0 )
	        return EMPTY;
	    
        return TAG + ES + _Value + SOH;
	}
	
	public String toFIXML(String ident) {
	    
	    if ( _Value == null || _Value.length() == 0 )
	        return "";

	    return ident + "<CustomerFirm FIXTag=\"204\" DataType=\"String\">"
	            + _Value + "</CustomerFirm>";

    } // end toFIXML
}