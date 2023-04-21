package er.extensions;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import org.apache.log4j.Category;

public class ERXListDisplay extends WOComponent {

    public ERXListDisplay(WOContext aContext) {
        super(aContext);
    }

    public static final Category cat = Category.getInstance(ERXListDisplay.class);

    public boolean synchronizesBindingsWithVariables() {
        return false;
    }

    public boolean isStateless() {
        return true;
    }

    public boolean escapeHTML() {
        return ERXUtilities.booleanValueForBindingOnComponentWithDefault("escapeHTML", this, true);
    }

    protected String displayString() {
        return ERXExtensions.friendlyEOArrayDisplayForKey((NSArray) valueForBinding("list"), (String) valueForBinding("attribute"), (String) valueForBinding("nullArrayDisplay"));
    }
}
