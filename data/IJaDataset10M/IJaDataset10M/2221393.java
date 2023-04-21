package org.tolven.gen.util;

import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Logger;
import org.tolven.gen.entity.VirtualPerson;

/**
 * Set the date of birth, date of death and age at death.
 * @author John Churin
 */
public class LifeSpan extends GeneratorBase {

    private int totalPopulation = 0;

    private Logger log = Logger.getLogger("org.tolven.gen");

    private static final int[] ageDistribution = new int[] { 4033719, 15575428 / 5, 15575428 / 5, 15575428 / 5, 15575428 / 5, 15575428 / 5, 19900837 / 5, 19900837 / 5, 19900837 / 5, 19900837 / 5, 19900837 / 5, 21136449 / 5, 21136449 / 5, 21136449 / 5, 21136449 / 5, 21136449 / 5, 20376151 / 5, 20376151 / 5, 20376151 / 5, 20376151 / 5, 20376151 / 5, 20213632 / 5, 20213632 / 5, 20213632 / 5, 20213632 / 5, 20213632 / 5, 18971892 / 5, 18971892 / 5, 18971892 / 5, 18971892 / 5, 18971892 / 5, 20956412 / 5, 20956412 / 5, 20956412 / 5, 20956412 / 5, 20956412 / 5, 21914882 / 5, 21914882 / 5, 21914882 / 5, 21914882 / 5, 21914882 / 5, 23001724 / 5, 23001724 / 5, 23001724 / 5, 23001724 / 5, 23001724 / 5, 21302064 / 5, 21302064 / 5, 21302064 / 5, 21302064 / 5, 21302064 / 5, 18781873 / 5, 18781873 / 5, 18781873 / 5, 18781873 / 5, 18781873 / 5, 14990542 / 5, 14990542 / 5, 14990542 / 5, 14990542 / 5, 14990542 / 5, 11611184 / 5, 11611184 / 5, 11611184 / 5, 11611184 / 5, 11611184 / 5, 9580927 / 5, 9580927 / 5, 9580927 / 5, 9580927 / 5, 9580927 / 5, 8693288 / 5, 8693288 / 5, 8693288 / 5, 8693288 / 5, 8693288 / 5, 7420394 / 5, 7420394 / 5, 7420394 / 5, 7420394 / 5, 7420394 / 5, 5314239 / 5, 5314239 / 5, 5314239 / 5, 5314239 / 5, 5314239 / 5, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15, 4593069 / 15 };

    /** Creates a new instance of LifeSpan */
    public LifeSpan() {
        rng = new Random();
        totalPopulation = 0;
        for (int c : ageDistribution) {
            totalPopulation = totalPopulation + c;
        }
        log.fine("LifeSpan: Total population base is " + totalPopulation);
    }

    /**
     * Set the life span of a person.
     * Determined how old the person is when they die. Then set
     * their date of birth and death.
     * Start with an age distribution consistent with the US population.
     * But we need people who have already died or may not be born yet so
     * we adjust the age by a random (and thus linear) number of years.
     * We select a date of death from about 10 years before now
     * to about 100 years after now.
     * That yields some people that have died in the past, a few that
     * have yet to be born, and mostly people that are currently living.
     * In any case, we know when they will die and the cause. 
     */
    public void setLifeSpan(VirtualPerson person) {
        int ageAtDeath = person.getAgeAtDeath();
        GregorianCalendar dod = new GregorianCalendar();
        dod.add(GregorianCalendar.YEAR, +rng.nextInt(110) - 10);
        dod.add(GregorianCalendar.HOUR, -(24 * rng.nextInt(365)));
        dod.set(GregorianCalendar.HOUR_OF_DAY, 0);
        dod.set(GregorianCalendar.MINUTE, 0);
        dod.set(GregorianCalendar.SECOND, 0);
        dod.set(GregorianCalendar.MILLISECOND, 0);
        person.setDod(dod.getTime());
        GregorianCalendar dob = new GregorianCalendar();
        dob.setTime(dod.getTime());
        dob.add(GregorianCalendar.YEAR, -ageAtDeath);
        dob.set(GregorianCalendar.HOUR, -(24 * rng.nextInt(365)));
        dob.set(GregorianCalendar.HOUR_OF_DAY, 0);
        dob.set(GregorianCalendar.MINUTE, 0);
        dob.set(GregorianCalendar.SECOND, 0);
        dob.set(GregorianCalendar.MILLISECOND, 0);
        person.setDob(dob.getTime());
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    /**
     * Pick an age of a person based on the population of the US.
     */
    public int pickAnAge() {
        int p = rng.nextInt(totalPopulation);
        int age = 0;
        int cumulativePopulation = 0;
        for (int c : ageDistribution) {
            cumulativePopulation = cumulativePopulation + c;
            age++;
            if (p < cumulativePopulation) break;
        }
        return age;
    }
}
