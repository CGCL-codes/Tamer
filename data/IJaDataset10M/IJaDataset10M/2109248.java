package org.matsim.ptproject.qsim.multimodalsimengine.router.util;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.router.util.PersonalizableTravelTime;

/**
 * This is just a first implementation. It will be replaced with a 
 * more accurate implementation in the near future!
 * 
 * @author cdobler
 */
public class WalkTravelTime implements PersonalizableTravelTime {

    private final double walkSpeed;

    private double ageScaleFactor;

    WalkTravelTime(PlansCalcRouteConfigGroup plansCalcRouteConfigGroup) {
        this.walkSpeed = plansCalcRouteConfigGroup.getWalkSpeed();
    }

    @Override
    public double getLinkTravelTime(Link link, double time) {
        return link.getLength() / (walkSpeed * ageScaleFactor);
    }

    @Override
    public void setPerson(Person person) {
        this.ageScaleFactor = calculateAgeScaleFactor(person);
    }

    private double calculateAgeScaleFactor(Person person) {
        if (person != null && person instanceof PersonImpl) {
            int age = ((PersonImpl) person).getAge();
            if (age <= 6) return 0.75; else if (age <= 10) return 0.85; else if (age <= 15) return 0.95; else if (age <= 50) return 1.00; else if (age <= 55) return 0.95; else if (age <= 60) return 0.90; else if (age <= 65) return 0.85; else if (age <= 70) return 0.80; else if (age <= 75) return 0.75; else if (age <= 80) return 0.70; else if (age <= 85) return 0.65; else if (age <= 90) return 0.60; else if (age <= 95) return 0.55; else return 0.50;
        } else return 1.0;
    }
}
