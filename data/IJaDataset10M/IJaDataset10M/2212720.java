package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.NavigationListAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * Interface of renderer of DynamicMenu widget
 * 
 * @mock.generate base="WidgetRenderer"
 */
public interface DynamicMenuWidgetRenderer extends WidgetRenderer {

    /**
     * Generate Dynamic Menu markup on opening of nl element  
     */
    public void renderNlOpen(VolantisProtocol protocol, NavigationListAttributes attributes) throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on closing of nl element  
     */
    public void renderNlClose(VolantisProtocol protocol, NavigationListAttributes attributes) throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on opening of label element  
     */
    public boolean renderLabelOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on closing of label element  
     */
    public void renderLabelClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on opening of li element  
     */
    public void renderLiOpen(VolantisProtocol protocol, ListItemAttributes attributes) throws ProtocolException;

    /**
     * Generate Dynamic Menu markup on closing of li element  
     */
    public void renderLiClose(VolantisProtocol protocol, ListItemAttributes attributes) throws ProtocolException;
}
