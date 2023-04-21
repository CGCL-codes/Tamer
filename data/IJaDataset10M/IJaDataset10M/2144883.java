package prjfbtypes;

import org.jdom.Element;

public class Identification {

    private String Standard = null;

    private String Classification = null;

    private String ApplicationDomain = null;

    private String Function = null;

    private String Type = null;

    private String Description = null;

    public Identification(String Standard, String Classification, String ApplicationDomain, String Function, String Type, String Description) {
        this.Standard = Standard;
        this.Classification = Classification;
        this.ApplicationDomain = ApplicationDomain;
        this.Function = Function;
        this.Type = Type;
        this.Description = Description;
    }

    public String toString() {
        return "Identification";
    }

    public Element toXML() {
        Element identificationElement = new Element("Identification");
        if (Standard != null) identificationElement.setAttribute("Standard", Standard);
        if (Classification != null) identificationElement.setAttribute("Classification", Classification);
        if (ApplicationDomain != null) identificationElement.setAttribute("ApplicationDomain", ApplicationDomain);
        if (Function != null) identificationElement.setAttribute("Function", Function);
        if (Type != null) identificationElement.setAttribute("Type", Type);
        if (Description != null) identificationElement.setAttribute("Description", Description);
        return identificationElement;
    }

    public void print() {
        System.out.println("Standard: " + Standard);
        System.out.println("Classification: " + Classification);
        System.out.println("ApplicationDomain: " + ApplicationDomain);
        System.out.println("Function: " + Function);
        System.out.println("Type: " + Type);
        System.out.println("Description: " + Description);
    }

    public String getApplicationDomain() {
        return ApplicationDomain;
    }

    public void setApplicationDomain(String applicationDomain) {
        ApplicationDomain = applicationDomain;
    }

    public String getClassification() {
        return Classification;
    }

    public void setClassification(String classification) {
        Classification = classification;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getFunction() {
        return Function;
    }

    public void setFunction(String function) {
        Function = function;
    }

    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
