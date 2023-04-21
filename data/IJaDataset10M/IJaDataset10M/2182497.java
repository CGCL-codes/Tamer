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


public class IOI_Replace extends FIXMLAggregate implements Tag {

    public static final String TAG = "28";
    public static final int DATA_TYPE = FIXDataConverter.CHAR;
    public static final String VALUE = "R";
    
	
	private String _Value;
    
	public String getValue() {
	    return VALUE;
	}
	
	public void setValue(String s) throws ModelException {
	    if ( s == null || !s.equals(VALUE) )
	        throw new ModelException("Invalid value for field IOI_Replace, must be (R).");
        _Value = VALUE;	        
	}
	
	public void initValue(Object s) throws ModelException {
	    if ( _Value != null ) 
	        throw new ModelException("Value has already been initialized for field IOI_Replace.");
	    setValue((String)s);
	}
	
	public String getTagValue() {
	    return VALUE;
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
        String[] enum = new String[1];
        enum[0] = VALUE;
        return enum;
    }

    private IOI_RefID _IOI_RefID;
        
    public IOI_RefID getIOI_RefID() {
        return _IOI_RefID;
    }

    public void setIOI_RefID(IOI_RefID obj) {
        _IOI_RefID = obj;
    }
    
    public void initIOI_RefID(Object obj) throws ModelException {
	    if ( _IOI_RefID != null ) 
	        throw new ModelException("Value has already been initialized for IOI_RefID.");
        setIOI_RefID((IOI_RefID)obj); 
    }
    
    
    public String[] getProperties() {
        
        String[] properties = {
		"IOI_RefID", 
        };
        return properties;
    }    
    
    public String[] getRequiredProperties() {
        
        String[] properties = {
        };
        return properties;
    }
	
	public String toFIXMessage() {
	    StringBuffer sb = new StringBuffer("");

            if ( _Value != null )
		sb.append(TAG + ES + VALUE + SOH);
        if ( _IOI_RefID != null )
            sb.append(_IOI_RefID.toFIXMessage());

        return sb.toString();
    }        
	
	public String toFIXML(String ident) {
	    StringBuffer sb = new StringBuffer("");

        sb.append(ident + "<IOI_Replace FIXTag=\"28\" DataType=\"char\" Value=\"" 
                    + VALUE + "\">\n");

        if ( _IOI_RefID != null )
        	sb.append(_IOI_RefID.toFIXML(ident + "\t") + "\n");

	    sb.append(ident + "</IOI_Replace>");
	    return sb.toString();
	}        
}