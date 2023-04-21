package org.aigebi.rbac.to;

/**
 * @author Ligong Xu
 * @version $Id: Seniority.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class Seniority extends BaseTO implements SeniorityTO {

    private String name;

    private Integer seniorityLevel;

    private String description;

    public Seniority() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public Integer getSeniorityLevel() {
        return seniorityLevel;
    }

    public void setSeniorityLevel(Integer pSeniorityLevel) {
        seniorityLevel = pSeniorityLevel;
    }
}
