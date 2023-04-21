package com.lappy.crazybrackets.logic.conditions;

import org.boretti.drools.integration.drools5.annotations.DroolsPreCondition;

public class Customer {

    private String name;

    private String prenom;

    private boolean sexe;

    private int age;

    private boolean chomeur;

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    public Customer(String name, String prenom, boolean sexe, int age, boolean chomeur) {
        super();
        this.name = name;
        this.prenom = prenom;
        this.sexe = sexe;
        this.age = age;
        this.chomeur = chomeur;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the prenom
	 */
    public String getPrenom() {
        return prenom;
    }

    /**
	 * @param prenom the prenom to set
	 */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
	 * @return the sexe
	 */
    public boolean isSexe() {
        return sexe;
    }

    /**
	 * @param sexe the sexe to set
	 */
    public void setSexe(boolean sexe) {
        this.sexe = sexe;
    }

    /**
	 * @return the age
	 */
    public int getAge() {
        return age;
    }

    /**
	 * @param age the age to set
	 */
    public void setAge(int age) {
        this.age = age;
    }

    /**
	 * @return the chomeur
	 */
    public boolean isChomeur() {
        return chomeur;
    }

    /**
	 * @param chomeur the chomeur to set
	 */
    public void setChomeur(boolean chomeur) {
        this.chomeur = chomeur;
    }

    @DroolsPreCondition(resourceName = "/Customer-conditions-pre.cdrl", error = UnAuthorizedError.class)
    public void doCredit(float howMany) {
        System.err.println("Credit granted :" + howMany);
    }
}
