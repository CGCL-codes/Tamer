package de.mogwai.kias.example.bo;

public class FreelancerProfile {

    private String name;

    private String type;

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FreelancerProfile() {
    }

    public FreelancerProfile(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
