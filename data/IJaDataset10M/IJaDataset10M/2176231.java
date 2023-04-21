package uk.ac.ed.rapid.jobsubmission.jobmanager.cloud.jcloudcompat;

public class SecurityGroup {

    private String name;

    public SecurityGroup(SecurityGroupClient client) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
