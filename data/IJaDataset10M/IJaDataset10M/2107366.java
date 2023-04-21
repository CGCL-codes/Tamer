package org.nmc.pachyderm.authoring;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import ca.ucalgary.apollo.core.*;
import ca.ucalgary.apollo.app.*;
import org.nmc.pachyderm.foundation.*;

public class NewPresentationPage extends MCPage {

    public NewPresentationPage(WOContext context) {
        super(context);
    }

    public WOComponent screenTemplateSelector() {
        SelectPageInterface page = MC.mcfactory().selectPageForTypeTarget("pachyderm.template.component", "web", session());
        page.setNextPageDelegate(new ScreenTemplateSelectorNPD());
        return (WOComponent) page;
    }
}
