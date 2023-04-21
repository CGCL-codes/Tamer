package org.authorsite.bib;

public abstract class AbstractHuman extends AbstractBibEntry {

    private String name;

    private String nameQualification;

    public AbstractHuman(long id) {
        super(id);
    }

    public AbstractHuman() {
        super();
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "Unknown";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameQualification() {
        return nameQualification;
    }

    public void setNameQualification(String nameQualification) {
        this.nameQualification = nameQualification;
    }
}
