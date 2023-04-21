package er.restadaptorexample.components;

import se.caboo.beast.model.Forum;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import er.extensions.eof.ERXEC;

public class Main extends WOComponent {

    private static final long serialVersionUID = 1L;

    private EOEditingContext _editingContext;

    public Forum _repetitionForum;

    public Main(WOContext context) {
        super(context);
        _editingContext = ERXEC.newEditingContext();
    }

    public NSArray<Forum> forums() {
        return Forum.fetchAllForums(_editingContext);
    }
}
