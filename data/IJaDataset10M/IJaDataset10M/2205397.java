package com.webobjects.woextensions;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

public class WOTabPanel extends WOComponent {

    protected static final String _undefinedMarker = "UNDEFINED";

    public Object currentTab;

    protected Object _selectedTab;

    protected String _submitActionName;

    protected String _tabNameKey;

    protected NSArray _tabs;

    public String bgcolor;

    protected String _nonSelectedBgColor;

    public WOTabPanel(WOContext aContext) {
        super(aContext);
        _selectedTab = null;
        currentTab = null;
        _tabs = null;
        _submitActionName = _undefinedMarker;
        _tabNameKey = null;
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    public Object selectedTab() {
        if (_selectedTab == null) {
            _selectedTab = valueForBinding("selectedTab");
            if (_selectedTab == null) {
                _selectedTab = ((NSArray) valueForBinding("tabs")).objectAtIndex(0);
                setValueForBinding(_selectedTab, "selectedTab");
            }
        }
        return _selectedTab;
    }

    public String tabNameKey() {
        if (_tabNameKey == null) {
            _tabNameKey = (String) _WOJExtensionsUtil.valueForBindingOrNull("tabNameKey", this);
            if (_tabNameKey == null) _tabNameKey = "toString";
        }
        return _tabNameKey;
    }

    public String selectedTabName() {
        return (String) NSKeyValueCodingAdditions.Utility.valueForKeyPath(selectedTab(), tabNameKey());
    }

    public String currentTabName() {
        return (String) NSKeyValueCodingAdditions.Utility.valueForKeyPath(currentTab, tabNameKey());
    }

    public boolean isCellShaded() {
        return !selectedTab().equals(currentTab);
    }

    public NSArray tabs() {
        if (_tabs == null) {
            _tabs = (NSArray) _WOJExtensionsUtil.valueForBindingOrNull("tabs", this);
            if (_tabs == null) {
                _tabs = NSArray.EmptyArray;
            }
        }
        return _tabs;
    }

    public String nonSelectedBgColor() {
        if (null == _nonSelectedBgColor) {
            if (hasBinding("nonSelectedBgColor")) _nonSelectedBgColor = (String) _WOJExtensionsUtil.valueForBindingOrNull("nonSelectedBgColor", this); else _nonSelectedBgColor = "#B5B5B5";
        }
        return _nonSelectedBgColor;
    }

    public String tabBgColor() {
        if (isCellShaded()) return nonSelectedBgColor(); else {
            if (null == bgcolor) {
                if (hasBinding("bgcolor")) bgcolor = (String) _WOJExtensionsUtil.valueForBindingOrNull("bgcolor", this); else bgcolor = "#E0E0E0";
            }
            return bgcolor;
        }
    }

    public void switchTab() {
        _selectedTab = currentTab;
        setValueForBinding(_selectedTab, "selectedTab");
    }

    public String submitActionName() {
        if (_submitActionName == _undefinedMarker) {
            if (hasBinding("submitActionName")) _submitActionName = (String) _WOJExtensionsUtil.valueForBindingOrNull("submitActionName", this); else _submitActionName = null;
        }
        return _submitActionName;
    }

    public boolean hasSubmitAction() {
        if (submitActionName() != null) return true;
        return false;
    }

    public void switchSubmitTab() {
        switchTab();
        if ((submitActionName() != null) && !submitActionName().equals("")) performParentAction(submitActionName());
    }

    public int contentColSpan() {
        return 2 + tabs().count();
    }

    public int rowSpan() {
        if (isCellShaded()) return 1; else return 2;
    }

    public void appendToResponse(WOResponse aResponse, WOContext aContext) {
        _tabs = null;
        currentTab = null;
        _selectedTab = null;
        _submitActionName = _undefinedMarker;
        _tabNameKey = null;
        super.appendToResponse(aResponse, aContext);
    }
}
