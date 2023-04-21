package com.trsvax.gwthello.components.gwtui;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import com.trsvax.gwthello.client.GWTUI;
import com.trsvax.gwthello.client.gwtui.services.NullService;
import com.trsvax.gwthello.components.GWTComponent;

@IncludeJavaScriptLibrary(value = { "context:/gwtUI/gwtUI.nocache.js" })
public class DisclosurePanel extends GWTComponent<GWTUI, NullService> implements NullService {

    public Class<GWTUI> getGWTModuleClass() {
        return GWTUI.class;
    }
}
