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


public class ListStatus extends FIXmlMessage implements Tag {

    public static final String TAG = "35";
    public static final int DATA_TYPE = FIXDataConverter.STRING;
    public static final String VALUE = "N";
    
	
	private String _Value;
    
	public String getValue() {
	    return VALUE;
	}
	
	public void setValue(String s) throws ModelException {
	    if ( s == null || !s.equals(VALUE) )
	        throw new ModelException("Invalid tag value, must be (N).");
        _Value = VALUE;	        
	}
	
	public void initValue(Object s) throws ModelException {
	    if ( _Value != null ) 
	        throw new ModelException("Value has already been initialized for field ListStatus.");
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

    private ListID _ListID;
    private ListStatusType _ListStatusType;
    private NoRpts _NoRpts;
    private ListOrderStatus _ListOrderStatus;
    private RptSeq _RptSeq;
    private Text _Text;
    private EncodedTextGroup _EncodedTextGroup;
    private TransactTime _TransactTime;
    private TotNoOrders _TotNoOrders;
    private ListStatusList _ListStatusList;
        
    public ListID getListID() {
        return _ListID;
    }

    public void setListID(ListID obj) {
        _ListID = obj;
    }
    
    public void initListID(Object obj) throws ModelException {
	    if ( _ListID != null ) 
	        throw new ModelException("Value has already been initialized for ListID.");
        setListID((ListID)obj); 
    }

        
    public ListStatusType getListStatusType() {
        return _ListStatusType;
    }

    public void setListStatusType(ListStatusType obj) {
        _ListStatusType = obj;
    }
    
    public void initListStatusType(Object obj) throws ModelException {
	    if ( _ListStatusType != null ) 
	        throw new ModelException("Value has already been initialized for ListStatusType.");
        setListStatusType((ListStatusType)obj); 
    }

        
    public NoRpts getNoRpts() {
        return _NoRpts;
    }

    public void setNoRpts(NoRpts obj) {
        _NoRpts = obj;
    }
    
    public void initNoRpts(Object obj) throws ModelException {
	    if ( _NoRpts != null ) 
	        throw new ModelException("Value has already been initialized for NoRpts.");
        setNoRpts((NoRpts)obj); 
    }

        
    public ListOrderStatus getListOrderStatus() {
        return _ListOrderStatus;
    }

    public void setListOrderStatus(ListOrderStatus obj) {
        _ListOrderStatus = obj;
    }
    
    public void initListOrderStatus(Object obj) throws ModelException {
	    if ( _ListOrderStatus != null ) 
	        throw new ModelException("Value has already been initialized for ListOrderStatus.");
        setListOrderStatus((ListOrderStatus)obj); 
    }

        
    public RptSeq getRptSeq() {
        return _RptSeq;
    }

    public void setRptSeq(RptSeq obj) {
        _RptSeq = obj;
    }
    
    public void initRptSeq(Object obj) throws ModelException {
	    if ( _RptSeq != null ) 
	        throw new ModelException("Value has already been initialized for RptSeq.");
        setRptSeq((RptSeq)obj); 
    }

        
    public Text getText() {
        return _Text;
    }

    public void setText(Text obj) {
        _Text = obj;
    }
    
    public void initText(Object obj) throws ModelException {
	    if ( _Text != null ) 
	        throw new ModelException("Value has already been initialized for Text.");
        setText((Text)obj); 
    }

        
    public EncodedTextGroup getEncodedTextGroup() {
        return _EncodedTextGroup;
    }

    public void setEncodedTextGroup(EncodedTextGroup obj) {
        _EncodedTextGroup = obj;
    }
    
    public void initEncodedTextGroup(Object obj) throws ModelException {
	    if ( _EncodedTextGroup != null ) 
	        throw new ModelException("Value has already been initialized for EncodedTextGroup.");
        setEncodedTextGroup((EncodedTextGroup)obj); 
    }

        
    public TransactTime getTransactTime() {
        return _TransactTime;
    }

    public void setTransactTime(TransactTime obj) {
        _TransactTime = obj;
    }
    
    public void initTransactTime(Object obj) throws ModelException {
	    if ( _TransactTime != null ) 
	        throw new ModelException("Value has already been initialized for TransactTime.");
        setTransactTime((TransactTime)obj); 
    }

        
    public TotNoOrders getTotNoOrders() {
        return _TotNoOrders;
    }

    public void setTotNoOrders(TotNoOrders obj) {
        _TotNoOrders = obj;
    }
    
    public void initTotNoOrders(Object obj) throws ModelException {
	    if ( _TotNoOrders != null ) 
	        throw new ModelException("Value has already been initialized for TotNoOrders.");
        setTotNoOrders((TotNoOrders)obj); 
    }

        
    public ListStatusList getListStatusList() {
        return _ListStatusList;
    }

    public void setListStatusList(ListStatusList obj) {
        _ListStatusList = obj;
    }
    
    public void initListStatusList(Object obj) throws ModelException {
	    if ( _ListStatusList != null ) 
	        throw new ModelException("Value has already been initialized for ListStatusList.");
        setListStatusList((ListStatusList)obj); 
    }

    
    public String[] getProperties() {
        
        String[] properties = {
		"ListID", 
		"ListStatusType", 
		"NoRpts", 
		"ListOrderStatus", 
		"RptSeq", 
		"Text", 
		"EncodedTextGroup", 
		"TransactTime", 
		"TotNoOrders", 
		"ListStatusList", 
        };
        return properties;
    }    
    
    public String[] getRequiredProperties() {
        
        String[] properties = {
        };
        return properties;
    }

    public boolean isValid() {
        return true;
    }

    public String getMessageType() {
        return "N";
    }
	
	public String toFIXMessage() {
	    StringBuffer sb = new StringBuffer("");

        if ( _Value != null )
    		sb.append(TAG + ES + VALUE + SOH);
        if ( _ListID != null )
            sb.append(_ListID.toFIXMessage());
        if ( _ListStatusType != null )
            sb.append(_ListStatusType.toFIXMessage());
        if ( _NoRpts != null )
            sb.append(_NoRpts.toFIXMessage());
        if ( _ListOrderStatus != null )
            sb.append(_ListOrderStatus.toFIXMessage());
        if ( _RptSeq != null )
            sb.append(_RptSeq.toFIXMessage());
        if ( _Text != null )
            sb.append(_Text.toFIXMessage());
        if ( _EncodedTextGroup != null )
            sb.append(_EncodedTextGroup.toFIXMessage());
        if ( _TransactTime != null )
            sb.append(_TransactTime.toFIXMessage());
        if ( _TotNoOrders != null )
            sb.append(_TotNoOrders.toFIXMessage());
        if ( _ListStatusList != null )
            sb.append(_ListStatusList.toFIXMessage());

        return sb.toString();
    }        
	
	public String toFIXML(String ident) {
	    StringBuffer sb = new StringBuffer("");

        sb.append(ident + "<ListStatus FIXTag=\"35\" DataType=\"String\" Value=\"" 
                    + VALUE + "\">\n");

        if ( _ListID != null )
        	sb.append(_ListID.toFIXML(ident + "\t") + "\n");
        if ( _ListStatusType != null )
        	sb.append(_ListStatusType.toFIXML(ident + "\t") + "\n");
        if ( _NoRpts != null )
        	sb.append(_NoRpts.toFIXML(ident + "\t") + "\n");
        if ( _ListOrderStatus != null )
        	sb.append(_ListOrderStatus.toFIXML(ident + "\t") + "\n");
        if ( _RptSeq != null )
        	sb.append(_RptSeq.toFIXML(ident + "\t") + "\n");
        if ( _Text != null )
        	sb.append(_Text.toFIXML(ident + "\t") + "\n");
        if ( _EncodedTextGroup != null )
        	sb.append(_EncodedTextGroup.toFIXML(ident + "\t") + "\n");
        if ( _TransactTime != null )
        	sb.append(_TransactTime.toFIXML(ident + "\t") + "\n");
        if ( _TotNoOrders != null )
        	sb.append(_TotNoOrders.toFIXML(ident + "\t") + "\n");
        if ( _ListStatusList != null )
        	sb.append(_ListStatusList.toFIXML(ident + "\t") + "\n");

	    sb.append(ident + "</ListStatus>");
	    return sb.toString();
	}        
}