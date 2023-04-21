package org.gjt.universe;

import java.util.*;

/**
 * @version $Id: DisplaySystem.java,v 1.6 2001/06/07 14:47:07 sstarkey Exp $
 */
public class DisplaySystem {

    public static VectorDisplayReturn get(CivID civ) {
        DisplaySystemFilter filter = new DisplaySystemFilter(DisplaySystemFilter.All, DisplaySystemFilter.Alphabetical);
        return get(civ, filter);
    }

    public static VectorDisplayReturn get(CivID AID, DisplaySystemFilter filter) {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        GalaxyBase Gal = GalaxyList.get(new GalaxyID(1));
        Civ civ = CivList.get(AID);
        switch(filter.getFilterType()) {
            case DisplaySystemFilter.All:
                {
                    VectorSystemID syslist = Gal.getGalaxySystems();
                    for (int iter = 0; iter < syslist.size(); iter++) {
                        SystemID SID = syslist.get(iter);
                        SystemBase system = SystemList.get(SID);
                        String tmpstr = DisplayUtil.SystemLine(system, AID, false);
                        tmpstr = Util.fillBlanks(tmpstr, 20);
                        DisplayReturn DR = new DisplayReturn(tmpstr, SID);
                        getTableInfo(AID, DR);
                        returnList.add(DR);
                    }
                    break;
                }
            case DisplaySystemFilter.Explored:
                {
                    VectorSystemID syslist = civ.getExploredSystems();
                    for (int iter = 0; iter < syslist.size(); iter++) {
                        SystemID SID = syslist.get(iter);
                        SystemBase system = SystemList.get(SID);
                        String tmpstr = DisplayUtil.SystemLine(system, AID, false);
                        tmpstr = Util.fillBlanks(tmpstr, 20);
                        DisplayReturn DR = new DisplayReturn(tmpstr, SID);
                        getTableInfo(AID, DR);
                        returnList.add(DR);
                    }
                    break;
                }
            case DisplaySystemFilter.Occupied:
                {
                    VectorSystemID syslist = civ.getOccupiedSystems();
                    for (int iter = 0; iter < syslist.size(); iter++) {
                        SystemID SID = syslist.get(iter);
                        SystemBase system = SystemList.get(SID);
                        String tmpstr = DisplayUtil.SystemLine(system, AID, false);
                        tmpstr = Util.fillBlanks(tmpstr, 20);
                        DisplayReturn DR = new DisplayReturn(tmpstr, SID);
                        getTableInfo(AID, DR);
                        returnList.add(DR);
                    }
                    break;
                }
        }
        switch(filter.getSortType()) {
            case DisplaySystemFilter.Alphabetical:
                Vector items = new Vector();
                int size = returnList.size();
                for (int cnt = 0; cnt < size; cnt++) {
                    SystemID SID = returnList.get(cnt).getSystem();
                    SystemBase system = SystemList.get(SID);
                    items.addElement(system.getName());
                }
                DisplayUtil.sort(returnList, items);
                break;
        }
        return returnList;
    }

    public static VectorDisplayReturn getSpecific(CivID AID, SystemID SID) {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        Civ civ = CivList.get(AID);
        SystemBase system = SystemList.get(SID);
        String tmpstr = "Name: ";
        tmpstr += system.getName();
        returnList.add(new DisplayReturn(tmpstr));
        tmpstr = "Coordinates: ";
        tmpstr += system.getCoords();
        returnList.add(new DisplayReturn(tmpstr));
        SystemBase homesystem = SystemList.get(civ.getHomeSystem());
        tmpstr = "Distance from home system: ";
        tmpstr += system.getCoords().distance(homesystem.getCoords());
        returnList.add(new DisplayReturn(tmpstr));
        return returnList;
    }

    public static VectorDisplayReturn getSpecific(CivID civ, String systemname) {
        return getSpecific(civ, SystemBase.getID(systemname));
    }

    private static void getTableInfo(CivID AID, DisplayReturn DR) {
        Civ civ = CivList.get(AID);
        SystemID SID = DR.getSystem();
        SystemBase system = SystemList.get(SID);
        DR.setNumColumns(4);
        DR.setColumn(0, system.getName());
        DR.setColumnHeader(0, "System");
        DR.setColumn(1, system.getCoords());
        DR.setColumnHeader(1, "Coords");
        KnowledgeSystemMap KSM = civ.getKnowledgeSystemMap();
        KnowledgeID KID = KSM.get(SID);
        String tmpstr = "?";
        if (KID.get() != 0) {
            VectorPlanetID VPID = system.getSystemPlanets();
            tmpstr = "" + VPID.size();
        }
        DR.setColumn(2, tmpstr);
        DR.setColumnHeader(2, "Planets");
        DR.setColumn(3, GalaxyList.get(system.getGalaxy()).getName());
        DR.setColumnHeader(3, "Galaxy");
    }
}
