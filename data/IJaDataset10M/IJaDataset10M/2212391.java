package domain.structure_new.components.plant;

/**
 * Classes that implement this interface (Seed, ClonalPlant) 
 * have a mother and a father plant,
 * have field variables with their ID 
 * and getters for them.
 * 
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 */
public interface IGenet {

    int getMotherID();

    int getFatherID();
}
