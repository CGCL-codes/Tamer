package scrumtaculous.domain;

/** InnoDB free: 41984 kB */
public class Project extends DataTransferObject {

    Integer id;

    String name;

    Integer iterationLengthWeeks;

    java.util.Date kickoff;

    Integer version;

    /**  */
    @Override
    public Integer getId() {
        return id;
    }

    /** , returns this instance so you can chain sets */
    @Override
    public void setId(Integer idIn) {
        id = idIn;
    }

    /**  */
    public String getName() {
        return name;
    }

    /** , returns this instance so you can chain sets */
    public void setName(String nameIn) {
        nameIn = cleanStringInput(nameIn);
        name = nameIn;
    }

    /**  */
    public Integer getIterationLengthWeeks() {
        return iterationLengthWeeks;
    }

    /** , returns this instance so you can chain sets */
    public void setIterationLengthWeeks(Integer iterationLengthWeeksIn) {
        iterationLengthWeeks = iterationLengthWeeksIn;
    }

    /**  */
    public java.util.Date getKickoff() {
        return kickoff;
    }

    /** , returns this instance so you can chain sets */
    public void setKickoff(java.util.Date kickoffIn) {
        kickoff = kickoffIn;
    }

    /**  */
    @Override
    public Integer getVersion() {
        return version;
    }

    /** , returns this instance so you can chain sets */
    @Override
    public void setVersion(Integer versionIn) {
        version = versionIn;
    }
}
