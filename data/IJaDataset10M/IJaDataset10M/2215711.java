package org.gjt.universe;

/** This class represents the Knowledge about a particular item. */
public class KnowledgeModuleDesign extends KnowledgeBase {

    private ModuleDesignID EID;

    public static KnowledgeID newKnowledge(CivID AID, ModuleDesignID EID) {
        KnowledgeModuleDesign newKnowledge = new KnowledgeModuleDesign(EID);
        KnowledgeList.add(newKnowledge);
        KnowledgeID KID = newKnowledge.getID();
        KnowledgeModuleDesignMap KEM = CivList.get(AID).getKnowledgeModuleDesignMap();
        KEM.insert(EID, KID);
        return newKnowledge.getID();
    }

    private KnowledgeModuleDesign(ModuleDesignID EID) {
        this.EID = EID;
    }

    public VectorDisplayReturn specificDisplayDebug() {
        VectorDisplayReturn returnList = new VectorDisplayReturn();
        returnList.add(new DisplayReturn("EID: " + EID, EID));
        returnList.add(new DisplayReturn("Type: ModuleDesign"));
        return returnList;
    }

    ModuleDesignID getModuleDesignID() {
        return EID;
    }

    void createSubKnowledge() {
    }
}
