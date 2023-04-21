package fr.uha.ensisa.ir.walther.milcityblue.core;

public class BuildingsIterator {

    private Buildings buildings;

    public BuildingsIterator(Buildings buildings) {
        this.buildings = buildings;
    }

    public boolean hasNext() {
        return this.buildings.hasNext();
    }

    public Building next() {
        if (this.hasNext()) {
            this.buildings = this.buildings.next();
            return this.buildings.value();
        }
        return null;
    }
}
