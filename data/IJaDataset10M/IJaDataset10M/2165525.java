package org.nmc.pachyderm.authoring;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.*;
import ca.ucalgary.apollo.core.*;
import ca.ucalgary.apollo.app.*;
import org.nmc.pachyderm.foundation.*;
import java.net.*;

public class InlineLinkSelector extends InlineBindingEditor {

    public InlineLinkSelector(WOContext context) {
        super(context);
    }

    public void appendToResponse(WOResponse response, WOContext context) {
        if (valueTypeIsScreen()) {
            MCPage page = (MCPage) context().page();
            PXScreen screen = (PXScreen) evaluatedValue();
            NSArray screens = ((PXPresentationDocument) page.document()).screenModel().screens();
            if (!(screens.containsObject(screen))) {
                setEvaluatedValue(null);
            }
        }
        super.appendToResponse(response, context);
    }

    public WOComponent selectScreen() {
        NSDictionary additionalBindings = new NSDictionary(new NSArray("screenEdit"), new NSArray("taskContext"));
        FindScreen page = (FindScreen) MC.mcfactory().pageForTaskTypeTarget("query", "pachyderm.screen", "web", context(), additionalBindings);
        page.setSource(component());
        page.setKey(bindingKey());
        page.setNextPage(context().page());
        return page;
    }

    public WOComponent unlinkAction() {
        setEvaluatedValue(null);
        return context().page();
    }

    public String previewURL() {
        if (valueTypeIsScreen()) {
            PXScreen screen = (PXScreen) evaluatedValue();
            if (screen != null) {
                String rootDescIdent = screen.rootComponent().componentDescription().identifier();
                String filename = NSPathUtilities.stringByAppendingPathExtension(rootDescIdent.replace('.', '-'), "gif");
                return new String("/pachyderm/images/screen-thumbnails/" + filename);
            }
        }
        return "/pachyderm/images/LinkScreenSlug.gif";
    }

    public boolean valueTypeIsScreen() {
        return (evaluatedValue() instanceof PXScreen);
    }

    public boolean valueTypeIsURL() {
        return (evaluatedValue() instanceof String);
    }

    public String urlFieldValue() {
        return valueTypeIsURL() ? (String) evaluatedValue() : null;
    }

    public void setUrlFieldValue(String value) {
        value = _validatedURLValue(value);
        setEvaluatedValue(value);
    }

    private String _validatedURLValue(String value) {
        if (value == null) {
            return null;
        }
        try {
            new URL(value);
        } catch (MalformedURLException murle) {
            if (value.indexOf(":/") == -1) {
                value = "http://" + value;
            }
        }
        return value;
    }
}
