package org.gjt.universe;

public class DisplayShipDesign {

    public static VectorDisplayReturn get(CivID civ) {
        ShipDesignFilter filter = new ShipDesignFilter(ShipDesignFilter.All, ShipDesignFilter.Alphabetical);
        return get(civ, filter);
    }

    public static VectorDisplayReturn get(CivID AID, ShipDesignFilter filter) {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        Civ civ = CivList.get(AID);
        switch(filter.getFilterType()) {
            case ShipDesignFilter.All:
                break;
            case ShipDesignFilter.Owned:
                break;
            case ShipDesignFilter.Engineered:
                {
                    int size = ShipDesignList.size();
                    KnowledgeShipDesignMap KDM = civ.getKnowledgeShipDesignMap();
                    for (int cnt = 0; cnt < size; cnt++) {
                        ShipDesignID DID = new ShipDesignID(cnt);
                        KnowledgeID KID = KDM.get(DID);
                        if (KID.get() != 0) {
                            KnowledgeShipDesign knowledge = (KnowledgeShipDesign) KnowledgeList.get(KID);
                            if (knowledge.isEngineered()) {
                                ShipDesignBase shipdesign = ShipDesignList.get(DID);
                                returnList.add(new DisplayReturn(shipdesign.getName(), DID));
                            }
                        }
                    }
                    break;
                }
            case ShipDesignFilter.NotEngineered:
                {
                    int size = ShipDesignList.size();
                    KnowledgeShipDesignMap KDM = civ.getKnowledgeShipDesignMap();
                    for (int cnt = 0; cnt < size; cnt++) {
                        ShipDesignID DID = new ShipDesignID(cnt);
                        KnowledgeID KID = KDM.get(DID);
                        if (KID.get() != 0) {
                            KnowledgeShipDesign knowledge = (KnowledgeShipDesign) KnowledgeList.get(KID);
                            if (!knowledge.isEngineered()) {
                                ShipDesignBase shipdesign = ShipDesignList.get(DID);
                                returnList.add(new DisplayReturn(shipdesign.getName(), DID));
                            }
                        }
                    }
                    break;
                }
        }
        return returnList;
    }
}
