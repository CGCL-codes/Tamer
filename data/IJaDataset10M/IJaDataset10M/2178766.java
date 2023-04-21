package net.ad.adsp.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.ad.adsp.entity.Target;

public class Item implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Boolean isEnabled;

    private Target target;

    private Item item;

    private Boolean enabledInRole;

    private List<Item> nodes;

    public Item() {
        this.isEnabled = false;
        this.target = null;
        this.item = null;
        this.enabledInRole = false;
        this.nodes = new ArrayList<Item>();
    }

    public Item(Boolean isEnabled, Target target) {
        this.isEnabled = isEnabled;
        this.target = target;
        this.item = null;
        this.enabledInRole = false;
        this.nodes = new ArrayList<Item>();
    }

    public Item(Boolean isEnabled, Target target, Item item) {
        this.isEnabled = isEnabled;
        this.target = target;
        this.item = item;
        this.enabledInRole = false;
        this.nodes = new ArrayList<Item>();
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getNodes() {
        return nodes;
    }

    public void setNodes(List<Item> nodes) {
        this.nodes = nodes;
    }

    public Boolean getEnabledInRole() {
        return enabledInRole;
    }

    public void setEnabledInRole(Boolean enabledInRole) {
        this.enabledInRole = enabledInRole;
    }
}
