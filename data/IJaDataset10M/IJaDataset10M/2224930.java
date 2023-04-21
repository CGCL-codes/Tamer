package org.petsoar.pets;

import org.petsoar.categories.Category;
import org.petsoar.persistence.PersistenceIndexedAware;
import org.petsoar.persistence.PersistenceManager;
import java.util.List;

/**
 * Default PetStore implementation. It uses the specified persistenceManager to store the pets and categories.
 */
public class DefaultPetStore implements PetStore, PersistenceIndexedAware {

    private PersistenceManager persistenceManager;

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void savePet(Pet pet) {
        persistenceManager.save(pet);
    }

    public void removePet(Pet pet) {
        persistenceManager.remove(pet);
    }

    public List getPets() {
        return persistenceManager.findAllSorted(Pet.class, "name");
    }

    public List getUncategorizedPets() {
        return persistenceManager.find("FROM p IN " + Pet.class + " WHERE p.category IS NULL", null, null);
    }

    public Pet getPet(long id) {
        return (Pet) persistenceManager.getById(Pet.class, id);
    }

    public List getRootCategories() {
        String query = "FROM c IN " + Category.class + " WHERE c.parent IS NULL";
        return persistenceManager.find(query, null, null);
    }

    public void addCategory(Category category) {
        persistenceManager.save(category);
    }

    public Category getCategory(long id) {
        return (Category) persistenceManager.getById(Category.class, id);
    }

    public void removeCategory(Category category) {
        persistenceManager.remove(category);
    }
}
