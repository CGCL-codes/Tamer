package er.extensions.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
 * Useful for picking a String from a list.<br />
 * 
 */
public class ERXStringListPicker extends WOComponent {

    public ERXStringListPicker(WOContext aContext) {
        super(aContext);
    }

    public Object item, _selection;

    public String explanationComponentName;

    public NSDictionary choices;

    public WOComponent cancelPage, nextPage;

    private NSArray _list;

    public NSArray list() {
        if (_list == null) {
            _list = EOSortOrdering.sortedArrayUsingKeyOrderArray(choices.allKeys(), new NSArray(EOSortOrdering.sortOrderingWithKey("toString", EOSortOrdering.CompareAscending)));
        }
        return _list;
    }

    public Object selection() {
        if (_selection == null && list().count() > 0) _selection = (String) list().objectAtIndex(0);
        return _selection;
    }

    public String entityNameForNewInstances() {
        return (String) choices.objectForKey(_selection);
    }

    public WOComponent next() {
        return nextPage;
    }

    public WOComponent cancel() {
        return cancelPage;
    }
}
