package org.matsim.core.basic.v01;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.population.BasicPerson;
import org.matsim.api.basic.v01.population.BasicPlan;
import org.matsim.population.Desires;

public class BasicPersonImpl<T extends BasicPlan> implements BasicPerson<T> {

    private static final Logger log = Logger.getLogger(BasicPersonImpl.class);

    protected List<T> plans = new ArrayList<T>(6);

    protected Id id;

    private String sex;

    private int age = Integer.MIN_VALUE;

    private String hasLicense;

    private String carAvail;

    private String isEmployed;

    private TreeSet<String> travelcards = null;

    protected BasicKnowledge knowledge = null;

    private Desires desires = null;

    public BasicPersonImpl(final Id id) {
        this.id = id;
    }

    public void addPlan(final T plan) {
        this.plans.add(plan);
    }

    public List<T> getPlans() {
        return this.plans;
    }

    public Id getId() {
        return this.id;
    }

    public void setId(final Id id) {
        this.id = id;
    }

    public void setId(final String idstring) {
        this.id = new IdImpl(idstring);
    }

    public final String getSex() {
        return this.sex;
    }

    public final int getAge() {
        return this.age;
    }

    public final String getLicense() {
        return this.hasLicense;
    }

    public final boolean hasLicense() {
        return ("yes".equals(this.hasLicense)) || ("true".equals(this.hasLicense));
    }

    public final String getCarAvail() {
        return this.carAvail;
    }

    public final Boolean isEmployed() {
        if (this.isEmployed == null) {
            return null;
        }
        return ("yes".equals(this.isEmployed)) || ("true".equals(this.isEmployed));
    }

    public void setAge(final int age) {
        if ((age < 0) && (age != Integer.MIN_VALUE)) {
            throw new NumberFormatException("A person's age has to be an integer >= 0.");
        }
        this.age = age;
    }

    public final void setSex(final String sex) {
        this.sex = (sex == null) ? null : sex.intern();
    }

    public final void setLicence(final String licence) {
        this.hasLicense = (licence == null) ? null : licence.intern();
    }

    public final void setCarAvail(final String carAvail) {
        this.carAvail = (carAvail == null) ? null : carAvail.intern();
    }

    public final void setEmployed(final String employed) {
        this.isEmployed = (employed == null) ? null : employed.intern();
    }

    public final Desires createDesires(final String desc) {
        if (this.desires == null) {
            this.desires = new Desires(desc);
        }
        return this.desires;
    }

    public final void addTravelcard(final String type) {
        if (this.travelcards == null) {
            this.travelcards = new TreeSet<String>();
        }
        if (this.travelcards.contains(type)) {
            log.info(this + "[type=" + type + " already exists]");
        } else {
            this.travelcards.add(type.intern());
        }
    }

    public final TreeSet<String> getTravelcards() {
        return this.travelcards;
    }

    public final BasicKnowledge getKnowledge() {
        return this.knowledge;
    }

    public final Desires getDesires() {
        return this.desires;
    }

    public void setKnowledge(final BasicKnowledge knowledge) {
        this.knowledge = knowledge;
    }
}
