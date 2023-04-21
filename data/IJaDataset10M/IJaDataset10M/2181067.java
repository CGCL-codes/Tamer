package com.google.code.ckJsfEditor.component;

import com.sun.faces.facelets.tag.MethodRule;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

/**
 * User: billreh
 * Date: 9/23/11
 * Time: 4:27 AM
 */
public class EditorHandler extends ComponentHandler {

    public EditorHandler(ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(new MethodRule("saveMethod", null, new Class[] { SaveEvent.class }));
        metaRuleset.addRule(new MethodRule("changeListener", null, new Class[] { ValueChangeEvent.class }));
        return metaRuleset;
    }
}
