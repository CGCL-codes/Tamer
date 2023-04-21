package org.scrinch.model;

import java.util.logging.Level;

public class ProjectItemSet extends ItemSet {

    private Project project;

    private boolean archive;

    public ProjectItemSet() {
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isArchive() {
        return this.archive;
    }

    @Override
    protected void affectItem(Item item) {
        super.affectItem(item);
        item.setProjectItemSet(this);
    }

    @Override
    public void removeItem(Item item) {
        itemList.remove(item);
        item.setProjectItemSet(null);
    }

    public org.scrinch.model.castor.ItemSet toCastor() {
        org.scrinch.model.castor.ItemSet cItemSet = new org.scrinch.model.castor.ItemSet();
        for (int i = 0; i < itemList.size(); i++) {
            Item item = (Item) itemList.get(i);
            cItemSet.addItemLink(item.getKey());
        }
        cItemSet.setName(this.getTitle());
        cItemSet.setArchive(this.isArchive());
        return cItemSet;
    }

    public static ProjectItemSet fromCastor(org.scrinch.model.castor.ItemSet cItemSet, ScrinchEnvToolkit toolkit) throws ScrinchException {
        ProjectItemSet itemSet = new ProjectItemSet();
        for (int i = 0; i < cItemSet.getItemLinkCount(); i++) {
            String itemKey = cItemSet.getItemLink(i);
            Item item = ItemToolkit.findItem(toolkit.getItemFactory().getItemList(), itemKey);
            if (item != null) {
                itemSet.addItem(item);
            } else {
                ScrinchEnvToolkit.logger.log(Level.WARNING, "Item #" + itemKey + " does not exist");
            }
        }
        itemSet.setTitle(cItemSet.getName());
        itemSet.setArchive(cItemSet.getArchive());
        return itemSet;
    }
}
