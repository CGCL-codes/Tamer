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


public class AllocRejCode extends FIXMLField {

    public static final String TAG = "88";
    public static final int DATA_TYPE = FIXDataConverter.INT;
    public static final String ENUM_UnknownAcct = "1";
    public static final String ENUM_IncorrectQty = "2";
    public static final String ENUM_IncorrectAvgPrc = "3";
    public static final String ENUM_IncorrectBrkMnc = "4";
    public static final String ENUM_CommDiff = "5";
    public static final String ENUM_UnknownOrdID = "6";
    public static final String ENUM_Other = "7";

    public static final String ENUM = "|1|2|3|4|5|6|7";
    
    private String _Value;

	public String getValue() {
	    return _Value;
	}
	
	public void setValue(String s) throws ModelException {
	    if ( s == null || s.indexOf('|') != -1 || ENUM.indexOf(s) == -1 )
	        throw new ModelException("Invalid value (" + s + ") for field AllocRejCode.");
        _Value = s;	        
	}
	
	// called only by parser
	public void initValue(Object s) throws ModelException {
	    if ( _Value != null ) 
	        throw new ModelException("Value has already been initialized for field AllocRejCode.");
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
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
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

	    return ident + "<AllocRejCode FIXTag=\"88\" DataType=\"int\">"
	            + _Value + "</AllocRejCode>";

    } // end toFIXML
}