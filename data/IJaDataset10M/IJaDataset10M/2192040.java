package de.oststadt.shavengods;

/**
 * representation two mated ShavenGods
 */
public class Couple {

    ShavenGod male;

    ShavenGod female;

    Couple(ShavenGod a, ShavenGod b) {
        if (a.gender.equals(Gender.MALE)) {
            this.male = a;
            this.female = b;
        } else {
            this.male = b;
            this.female = a;
        }
    }

    boolean contains(ShavenGod god) {
        return male.equals(god) || female.equals(god);
    }
}
