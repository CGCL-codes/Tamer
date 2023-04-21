package org.jboss.tutorial.singleinheritance.bean;

import java.util.List;

public interface PetDAO {

    void createDog(String name, double weight, int bones);

    void createCat(String name, double weight, int lives);

    List findByWeight(double weight);
}
