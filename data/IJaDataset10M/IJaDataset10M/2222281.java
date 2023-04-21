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


public class UndrOption extends FIXMLAggregate implements Tag {

    public static final String TAG = "310";
    public static final int DATA_TYPE = FIXDataConverter.CHAR;
    public static final String VALUE = "OPT";
    
	
	private String _Value;
    
	public String getValue() {
	    return VALUE;
	}
	
	public void setValue(String s) throws ModelException {
	    if ( s == null || !s.equals(VALUE) )
	        throw new ModelException("Invalid value for field UndrOption, must be (OPT).");
        _Value = VALUE;	        
	}
	
	public void initValue(Object s) throws ModelException {
	    if ( _Value != null ) 
	        throw new ModelException("Value has already been initialized for field UndrOption.");
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

    private UndrMaturity _UndrMaturity;
    private UndrPutCall _UndrPutCall;
    private UndrStrikePx _UndrStrikePx;
    private UndrOptAttribute _UndrOptAttribute;
        
    public UndrMaturity getUndrMaturity() {
        return _UndrMaturity;
    }

    public void setUndrMaturity(UndrMaturity obj) {
        _UndrMaturity = obj;
    }
    
    public void initUndrMaturity(Object obj) throws ModelException {
	    if ( _UndrMaturity != null ) 
	        throw new ModelException("Value has already been initialized for UndrMaturity.");
        setUndrMaturity((UndrMaturity)obj); 
    }
    
        
    public UndrPutCall getUndrPutCall() {
        return _UndrPutCall;
    }

    public void setUndrPutCall(UndrPutCall obj) {
        _UndrPutCall = obj;
    }
    
    public void initUndrPutCall(Object obj) throws ModelException {
	    if ( _UndrPutCall != null ) 
	        throw new ModelException("Value has already been initialized for UndrPutCall.");
        setUndrPutCall((UndrPutCall)obj); 
    }
    
        
    public UndrStrikePx getUndrStrikePx() {
        return _UndrStrikePx;
    }

    public void setUndrStrikePx(UndrStrikePx obj) {
        _UndrStrikePx = obj;
    }
    
    public void initUndrStrikePx(Object obj) throws ModelException {
	    if ( _UndrStrikePx != null ) 
	        throw new ModelException("Value has already been initialized for UndrStrikePx.");
        setUndrStrikePx((UndrStrikePx)obj); 
    }
    
        
    public UndrOptAttribute getUndrOptAttribute() {
        return _UndrOptAttribute;
    }

    public void setUndrOptAttribute(UndrOptAttribute obj) {
        _UndrOptAttribute = obj;
    }
    
    public void initUndrOptAttribute(Object obj) throws ModelException {
	    if ( _UndrOptAttribute != null ) 
	        throw new ModelException("Value has already been initialized for UndrOptAttribute.");
        setUndrOptAttribute((UndrOptAttribute)obj); 
    }
    
    
    public String[] getProperties() {
        
        String[] properties = {
		"UndrMaturity", 
		"UndrPutCall", 
		"UndrStrikePx", 
		"UndrOptAttribute", 
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
        if ( _UndrMaturity != null )
            sb.append(_UndrMaturity.toFIXMessage());
        if ( _UndrPutCall != null )
            sb.append(_UndrPutCall.toFIXMessage());
        if ( _UndrStrikePx != null )
            sb.append(_UndrStrikePx.toFIXMessage());
        if ( _UndrOptAttribute != null )
            sb.append(_UndrOptAttribute.toFIXMessage());

        return sb.toString();
    }        
	
	public String toFIXML(String ident) {
	    StringBuffer sb = new StringBuffer("");

        sb.append(ident + "<UndrOption FIXTag=\"310\" DataType=\"char\" Value=\"" 
                    + VALUE + "\">\n");

        if ( _UndrMaturity != null )
        	sb.append(_UndrMaturity.toFIXML(ident + "\t") + "\n");
        if ( _UndrPutCall != null )
        	sb.append(_UndrPutCall.toFIXML(ident + "\t") + "\n");
        if ( _UndrStrikePx != null )
        	sb.append(_UndrStrikePx.toFIXML(ident + "\t") + "\n");
        if ( _UndrOptAttribute != null )
        	sb.append(_UndrOptAttribute.toFIXML(ident + "\t") + "\n");

	    sb.append(ident + "</UndrOption>");
	    return sb.toString();
	}        
}