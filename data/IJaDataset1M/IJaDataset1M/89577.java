package net.sourceforge.ivi;

/** 
 ********************************************************************/
public class GuiItem {

    /** Constructor for this GUI Item place-holder
     ****************************************************************/
    public GuiItem(String inst_name, Object peer) {
        d_peer = peer;
        d_instName = inst_name;
    }

    /****************************************************************
     * addChild()
     ****************************************************************/
    public void addChild(GuiItemIfc child) {
        GuiItemIfc tmp[] = d_children;
        int new_len;
        if (tmp != null) {
            new_len = tmp.length + 1;
        } else {
            new_len = 1;
        }
        d_children = new GuiItemIfc[new_len];
        if (tmp != null) {
            for (int i = 0; i < tmp.length; i++) {
                d_children[i] = tmp[i];
            }
        }
        d_children[new_len - 1] = child;
    }

    /****************************************************************
     * Private Data
     ****************************************************************/
    private String d_instName;

    private Object d_peer;

    private GuiItemIfc d_children[];
}
