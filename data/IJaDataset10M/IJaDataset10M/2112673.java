package edu.uis.csc478.spring09.threeoxen.persistence.pantry;

import edu.uis.csc478.spring09.threeoxen.persistence.ObjectRepository;

public interface PantryRepository extends ObjectRepository {

    public Pantry getPantryByOwnerName(String pantryOwnerName);

    public void addPantry(Pantry pantry);

    public boolean pantryExists(String pantryOwnerName);
}
