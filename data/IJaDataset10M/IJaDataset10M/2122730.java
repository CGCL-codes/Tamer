package org.ncsa.foodlog.data.profiles;

import org.apache.log4j.Logger;

/**
 * import java.io.IOException
 */
public class Meal extends Profile {

    private int entres = 0;

    private int shakes = 0;

    private int benefitBars = 0;

    private int fruits = 0;

    private int vegetables = 0;

    private int protein = 0;

    private int grains = 0;

    private int dairy = 0;

    private int fats = 0;

    private int sugar = 0;

    private int prepTimeInMinutes = 0;

    private int fvCalories = 0;

    private int mrCalories = 0;

    private int fauxMRCalories = 0;

    private Logger log = Logger.getLogger(Meal.class);

    public Meal() {
        super(false);
    }

    public int getVegetables() {
        return vegetables;
    }

    public void setVegetables(int vegetables) {
        this.vegetables = vegetables;
    }

    public void setFruits(int fruits) {
        this.fruits = fruits;
    }

    public Meal(String aName, int aCalories) {
        super(aName, aCalories, false);
    }

    public Meal(String aName) {
        super(aName, false);
    }

    public Meal(Meal other) {
        super(other);
        entres = other.entres;
        shakes = other.shakes;
        benefitBars = other.benefitBars;
        fruits = other.fruits;
        vegetables = other.vegetables;
        protein = other.protein;
        grains = other.grains;
        dairy = other.dairy;
        fats = other.fats;
        sugar = other.sugar;
        prepTimeInMinutes = other.prepTimeInMinutes;
        fvCalories = other.fvCalories;
        mrCalories = other.mrCalories;
        fauxMRCalories = other.fauxMRCalories;
    }

    /**
	 * use getClass().getName() to get the specific subclass
	 */
    public void setEntres(int aEntres) {
        entres = aEntres;
    }

    public int getEntres() {
        return entres;
    }

    public void add(Meal other) {
        entres += other.entres;
        shakes += other.shakes;
        benefitBars += other.benefitBars;
        fruits += other.fruits;
        vegetables += other.vegetables;
        protein += other.protein;
        grains += other.grains;
        dairy += other.dairy;
        fats += other.fats;
        sugar += other.sugar;
        fvCalories += other.fvCalories;
        mrCalories += other.mrCalories;
        super.add(other);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public void scale(int multiple) {
        entres *= multiple;
        shakes *= multiple;
        benefitBars *= multiple;
        fruits *= multiple;
        vegetables *= multiple;
        protein *= multiple;
        grains *= multiple;
        dairy *= multiple;
        fats *= multiple;
        sugar *= multiple;
        fvCalories *= multiple;
        fauxMRCalories *= multiple;
        mrCalories *= multiple;
        super.scale(multiple);
    }

    public String formattedString() {
        return (formats.formatTitle("Meal: " + getTag().getName(), "delimited.name") + formats.formatNumberWithSpacer(entres, "delimited.entre") + formats.formatNumberWithSpacer(shakes, "delimited.shakes") + formats.formatNumberWithSpacer(benefitBars, "delimited.benefitBars") + formats.formatNumberWithSpacer(fruits, "delimited.fruits") + formats.formatNumberWithSpacer(vegetables, "delimited.vegetables") + formats.formatNumberWithSpacer(protein, "delimited.protein") + formats.formatNumberWithSpacer(grains, "delimited.grains") + formats.formatNumberWithSpacer(dairy, "delimited.dairy") + formats.formatNumberWithSpacer(fats, "delimited.fats") + formats.formatNumberWithSpacer(sugar, "delimited.sugar") + formats.formatNumberWithSpacer(prepTimeInMinutes, "delimited.prepTimeInMinutes") + formats.formatNumberWithSpacer(fvCalories, "delimited.fvCalories") + formats.formatNumberWithSpacer(mrCalories, "delimited.mrCalories") + formats.formatNumberWithSpacer(fauxMRCalories, "delimited.fauxMRCalories") + formats.formatNumberWithSpacer(getCalories(), "delimited.totalCalories"));
    }

    public void setShakes(int aShakes) {
        shakes = aShakes;
    }

    public int getShakes() {
        return shakes;
    }

    public void setBenefitBars(int aBenefitBars) {
        benefitBars = aBenefitBars;
    }

    public int getBenefitBars() {
        return benefitBars;
    }

    public int getFruits() {
        return fruits;
    }

    /**
	 * @return Returns the dairy.
	 */
    public int getDairy() {
        return dairy;
    }

    /**
	 * @param dairy
	 *            The dairy to set.
	 */
    public void setDairy(int dairy) {
        this.dairy = dairy;
    }

    /**
	 * @return Returns the fats.
	 */
    public int getFats() {
        return fats;
    }

    /**
	 * @param fats
	 *            The fats to set.
	 */
    public void setFats(int fats) {
        this.fats = fats;
    }

    /**
	 * @return Returns the grains.
	 */
    public int getGrains() {
        return grains;
    }

    /**
	 * @param grains
	 *            The grains to set.
	 */
    public void setGrains(int grains) {
        this.grains = grains;
    }

    /**
	 * @return Returns the protein.
	 */
    public int getProtein() {
        return protein;
    }

    /**
	 * @param protein
	 *            The protein to set.
	 */
    public void setProtein(int protein) {
        this.protein = protein;
    }

    /**
	 * @return Returns the prepTimeInMinutes.
	 */
    public int getPrepTimeInMinutes() {
        return prepTimeInMinutes;
    }

    /**
	 * @param prepTimeInMinutes
	 *            The prepTimeInMinutes to set.
	 */
    public void setPrepTimeInMinutes(int prepTimeInMinutes) {
        this.prepTimeInMinutes = prepTimeInMinutes;
    }

    public int compareTo(Object arg0) {
        if (arg0 instanceof Profile) {
            Profile other = (Profile) arg0;
            if (arg0 instanceof PhysicalActivity) return 1;
            int result = getTag().compareTo(other.getTag());
            if (result != 0) {
                return result;
            }
        }
        return 1;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getFvCalories() {
        return fvCalories;
    }

    public void setFvCalories(int fvCalories) {
        this.fvCalories = fvCalories;
    }

    public int getFauxMRCalories() {
        return fauxMRCalories;
    }

    public void setFauxMRCalories(int fauxMRCalories) {
        this.fauxMRCalories = fauxMRCalories;
    }

    public int getMrCalories() {
        return mrCalories;
    }

    public void setMrCalories(int mrCalories) {
        this.mrCalories = mrCalories;
    }

    public Profile cloneAndScale(int multiple) {
        Meal result = new Meal(this);
        result.scale(multiple);
        return result;
    }
}
