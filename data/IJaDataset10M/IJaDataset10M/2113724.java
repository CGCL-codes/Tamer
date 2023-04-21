package org.osmius.service;

import java.util.List;

public interface OsmTEventTypinstancesManager extends Manager {

    public Boolean isTypeOfInstaceSupported(String typInstance);

    public List getOsmTEventTypinstances(String event);
}
