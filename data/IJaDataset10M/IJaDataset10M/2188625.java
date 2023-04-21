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


public class FixedIncome extends FIXMLAggregate implements Tag {

    public static final String TAG = "167";
    public static final int DATA_TYPE = FIXDataConverter.STRING;
    public static final String VALUE = "";
    
    public static final String ENUM_BA = "BA";
    public static final String ENUM_CD = "CD";
    public static final String ENUM_CMO = "CMO";
    public static final String ENUM_COPR = "COPR";
    public static final String ENUM_CP = "CP";
    public static final String ENUM_CPP = "CPP";
    public static final String ENUM_FHA = "FHA";
    public static final String ENUM_FHL = "FHL";
    public static final String ENUM_FN = "FN";
    public static final String ENUM_GN = "GN";
    public static final String ENUM_GOVT = "GOVT";
    public static final String ENUM_IET = "IET";
    public static final String ENUM_MPO = "MPO";
    public static final String ENUM_MPP = "MPP";
    public static final String ENUM_MPT = "MPT";
    public static final String ENUM_MUNI = "MUNI";
    public static final String ENUM_RP = "RP";
    public static final String ENUM_RVRP = "RVRP";
    public static final String ENUM_SL = "SL";
    public static final String ENUM_TD = "TD";
    public static final String ENUM_USTB = "USTB";
    public static final String ENUM_ZOO = "ZOO";

    public static final String ENUM = "|BA|CD|CMO|COPR|CP|CPP|FHA|FHL|FN|GN|GOVT|IET|MPO|MPP|MPT|MUNI|RP|RVRP|SL|TD|USTB|ZOO";
    
    private String _Value;

	public String getValue() {
	    return _Value;
	}
	
	public void setValue(String s) throws ModelException {
	    if ( s == null || s.indexOf('|') != -1 || ENUM.indexOf(s) == -1 )
	        throw new ModelException("Invalid tag value. Not in valid list.");
        _Value = s;	        
	}
	
	public void initValue(Object s) throws ModelException {
	    if ( _Value != null )
	        throw new ModelException("Value has already been initialized for field FixedIncome.");
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
            "BA",
            "CD",
            "CMO",
            "COPR",
            "CP",
            "CPP",
            "FHA",
            "FHL",
            "FN",
            "GN",
            "GOVT",
            "IET",
            "MPO",
            "MPP",
            "MPT",
            "MUNI",
            "RP",
            "RVRP",
            "SL",
            "TD",
            "USTB",
            "ZOO",
        };
        return enum;
    }
    
    private ContractMultiplier _ContractMultiplier;
    private CouponRate _CouponRate;
        
    public ContractMultiplier getContractMultiplier() {
        return _ContractMultiplier;
    }

    public void setContractMultiplier(ContractMultiplier obj) {
        _ContractMultiplier = obj;
    }
    
    public void initContractMultiplier(Object obj) throws ModelException {
	    if ( _ContractMultiplier != null ) 
	        throw new ModelException("Value has already been initialized for ContractMultiplier.");
        setContractMultiplier((ContractMultiplier)obj); 
    }
        
    public CouponRate getCouponRate() {
        return _CouponRate;
    }

    public void setCouponRate(CouponRate obj) {
        _CouponRate = obj;
    }
    
    public void initCouponRate(Object obj) throws ModelException {
	    if ( _CouponRate != null ) 
	        throw new ModelException("Value has already been initialized for CouponRate.");
        setCouponRate((CouponRate)obj); 
    }
    
    public String[] getProperties() {
        
        String[] properties = {
	    "Value",	
		"ContractMultiplier", 
		"CouponRate", 
        };
        return properties;
    }    
    
    public String[] getRequiredProperties() {
        
        String[] properties = {
	    "Value",	
        };
        return properties;
    }
	
	public String toFIXMessage() {
	    StringBuffer sb = new StringBuffer("");

        if ( _Value != null )
    		sb.append(TAG + ES + _Value + SOH);
        if ( _ContractMultiplier != null )
            sb.append(_ContractMultiplier.toFIXMessage());
        if ( _CouponRate != null )
            sb.append(_CouponRate.toFIXMessage());

        return sb.toString();
    }        
	
	public String toFIXML(String ident) {
	    StringBuffer sb = new StringBuffer("");

        sb.append(ident + "<FixedIncome FIXTag=\"167\" DataType=\"String\" Value=\"" 
                    + _Value + "\">\n");

        if ( _ContractMultiplier != null )
        	sb.append(_ContractMultiplier.toFIXML(ident + "\t") + "\n");
        if ( _CouponRate != null )
        	sb.append(_CouponRate.toFIXML(ident + "\t") + "\n");

	    sb.append(ident + "</FixedIncome>");
	    return sb.toString();
	}        
}