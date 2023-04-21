package testingapplication.duallist;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;

public class DualListManagedBean implements Serializable {

    private List firstListItems;

    private List secondListItems;

    private List westListItems;

    private List centerListItems;

    private List eastListItems;

    public List getFirstListItems() {
        if (firstListItems == null) {
            firstListItems = new LinkedList();
            firstListItems.add(new SelectItem("itemFirst1", "Item 1 of list first list", "itemFirst1"));
            firstListItems.add(new SelectItem("itemFirst2", "Item 2 of list first list", "itemFirst2"));
            firstListItems.add(new SelectItem("itemFirst3", "Item 3 of list first list", "itemFirst3"));
            firstListItems.add(new SelectItem("itemFirst4", "Item 4 of list first list", "itemFirst4"));
            firstListItems.add(new SelectItem("itemFirst5", "Item 5 of list first list", "itemFirst5"));
            firstListItems.add(new SelectItem("itemFirst6", "Item 6 of list first list", "itemFirst6"));
        }
        return firstListItems;
    }

    public void setFirstListItems(List firstListItems) {
        this.firstListItems = firstListItems;
    }

    public List getSecondListItems() {
        if (secondListItems == null) {
            secondListItems = new LinkedList();
            secondListItems.add(new SelectItem("itemSecond1", "Item 1 of list second list", "itemSecond1"));
            secondListItems.add(new SelectItem("itemSecond2", "Item 2 of list second list", "itemSecond1"));
            secondListItems.add(new SelectItem("itemSecond3", "Item 3 of list second list", "itemSecond1"));
            secondListItems.add(new SelectItem("itemSecond4", "Item 4 of list second list", "itemSecond1"));
        }
        return secondListItems;
    }

    public void setSecondListItems(List secondListItems) {
        this.secondListItems = secondListItems;
    }

    public List getWestListItems() {
        if (westListItems == null) {
            westListItems = new LinkedList();
            westListItems.add(new SelectItem("itemWest1", "Item 1 of list west list", "itemWest1"));
            westListItems.add(new SelectItem("itemWest2", "Item 2 of list west list", "itemWest2"));
            westListItems.add(new SelectItem("itemWest3", "Item 3 of list west list", "itemWest3"));
            westListItems.add(new SelectItem("itemWest4", "Item 4 of list west list", "itemWest4"));
            westListItems.add(new SelectItem("itemWest5", "Item 5 of list west list", "itemWest5"));
            westListItems.add(new SelectItem("itemWest6", "Item 6 of list west list", "itemWest6"));
        }
        return westListItems;
    }

    public void setWestListItems(List westListItems) {
        this.westListItems = westListItems;
    }

    public List getCenterListItems() {
        if (centerListItems == null) {
            centerListItems = new LinkedList();
            centerListItems.add(new SelectItem("itemCenter1", "Item 1 of list center list", "itemCenter1"));
            centerListItems.add(new SelectItem("itemCenter2", "Item 2 of list center list", "itemCenter2"));
            centerListItems.add(new SelectItem("itemCenter3", "Item 3 of list center list", "itemCenter3"));
            centerListItems.add(new SelectItem("itemCenter4", "Item 4 of list center list", "itemCenter4"));
        }
        return centerListItems;
    }

    public void setCenterListItems(List centerListItems) {
        this.centerListItems = centerListItems;
    }

    public List getEastListItems() {
        if (eastListItems == null) {
            eastListItems = new LinkedList();
            eastListItems.add(new SelectItem("itemEast1", "Item 1 of list east list", "itemEast1"));
            eastListItems.add(new SelectItem("itemEast2", "Item 2 of list east list", "itemEast2"));
            eastListItems.add(new SelectItem("itemEast3", "Item 3 of list east list", "itemEast3"));
        }
        return eastListItems;
    }

    public void setEastListItems(List eastListItems) {
        this.eastListItems = eastListItems;
    }
}
