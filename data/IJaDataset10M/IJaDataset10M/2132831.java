package research.domain;

import java.util.HashSet;
import java.util.Set;
import research.entity.Entity;
import research.entity.EntityType;

public final class Sample extends Entity {

    private String name;

    private double mass;

    private Production production;

    private Formula formula;

    private FilmType filmType;

    private Set<Strength> strengths;

    private Set<Viscosity> viscosities;

    private Set<Scratch> scratches;

    private Set<Photo> photos;

    private Set<SampleStabilizer> sampleStabilizers;

    private Set<Test> tests;

    private Set<ParameterValue> parameterValues;

    public Sample() {
        super(EntityType.Sample);
        strengths = new HashSet<Strength>();
        scratches = new HashSet<Scratch>();
        photos = new HashSet<Photo>();
        viscosities = new HashSet<Viscosity>();
        sampleStabilizers = new HashSet<SampleStabilizer>();
        parameterValues = new HashSet<ParameterValue>();
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public Set<Strength> getStrengths() {
        return strengths;
    }

    public void setStrengths(Set<Strength> exp_strengths) {
        this.strengths = exp_strengths;
    }

    public Set<Viscosity> getViscosities() {
        return viscosities;
    }

    public void setViscosities(Set<Viscosity> exp_viscosities) {
        this.viscosities = exp_viscosities;
    }

    public Set<Scratch> getScratches() {
        return scratches;
    }

    public void setScratches(Set<Scratch> exp_scratches) {
        this.scratches = exp_scratches;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> exp_photos) {
        this.photos = exp_photos;
    }

    public Set<SampleStabilizer> getSampleStabilizers() {
        return sampleStabilizers;
    }

    public void setSampleStabilizers(Set<SampleStabilizer> sample_stabilizers) {
        this.sampleStabilizers = sample_stabilizers;
    }

    public Set<ParameterValue> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Set<ParameterValue> parameter_values) {
        this.parameterValues = parameter_values;
    }

    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public static Sample getNew() {
        Sample sam = new Sample();
        sam.setName("");
        sam.setMass(0);
        return sam;
    }

    public FilmType getFilmType() {
        return filmType;
    }

    public void setFilmType(FilmType filmType) {
        this.filmType = filmType;
    }
}
