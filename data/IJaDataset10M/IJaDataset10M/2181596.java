package jmri.managers;

import jmri.*;
import jmri.implementation.DefaultClockControl;
import jmri.jmrit.catalog.DefaultCatalogTreeManager;
import jmri.jmrit.display.layoutEditor.LayoutBlockManager;
import jmri.jmrit.logix.OBlockManager;
import jmri.jmrit.logix.WarrantManager;
import jmri.jmrit.roster.RosterIconFactory;

/**
 * Provide the usual default implementations for
 * the InstanceManager.
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under 
 * the terms of version 2 of the GNU General Public License as published 
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 
 * for more details.
 * <P>
 * @author			Bob Jacobsen Copyright (C) 2001, 2008
 * @version			$Revision: 1.6 $
 * @since           2.9.4
 */
public class DefaultInstanceInitializer implements jmri.InstanceInitializer {

    public <T> Object getDefault(Class<T> type) {
        if (type == SignalHeadManager.class) {
            return new AbstractSignalHeadManager();
        }
        if (type == SignalMastManager.class) {
            InstanceManager.signalHeadManagerInstance();
            return new DefaultSignalMastManager();
        }
        if (type == SignalSystemManager.class) {
            return new DefaultSignalSystemManager();
        }
        if (type == SignalGroupManager.class) {
            InstanceManager.signalMastManagerInstance();
            return new DefaultSignalGroupManager();
        }
        if (type == BlockManager.class) {
            return new BlockManager();
        }
        if (type == OBlockManager.class) {
            return new OBlockManager();
        }
        if (type == WarrantManager.class) {
            return new WarrantManager();
        }
        if (type == SectionManager.class) {
            return new SectionManager();
        }
        if (type == TransitManager.class) {
            return new TransitManager();
        }
        if (type == RouteManager.class) {
            return new DefaultRouteManager();
        }
        if (type == LayoutBlockManager.class) {
            return new LayoutBlockManager();
        }
        if (type == LogixManager.class) {
            return new DefaultLogixManager();
        }
        if (type == ConditionalManager.class) {
            return new DefaultConditionalManager();
        }
        if (type == SignalMastLogicManager.class) {
            return new DefaultSignalMastLogicManager();
        }
        if (type == Timebase.class) {
            Timebase timebase = new jmri.jmrit.simpleclock.SimpleTimebase();
            if (InstanceManager.configureManagerInstance() != null) InstanceManager.configureManagerInstance().registerConfig(timebase, jmri.Manager.TIMEBASE);
            return timebase;
        }
        if (type == ClockControl.class) {
            return new DefaultClockControl();
        }
        if (type == CatalogTreeManager.class) {
            return new DefaultCatalogTreeManager();
        }
        if (type == MemoryManager.class) {
            return new DefaultMemoryManager();
        }
        if (type == RosterIconFactory.class) {
            return RosterIconFactory.instance();
        }
        if (type == IdTagManager.class) {
            return new DefaultIdTagManager();
        }
        throw new IllegalArgumentException("Cannot create object of type " + type);
    }
}
