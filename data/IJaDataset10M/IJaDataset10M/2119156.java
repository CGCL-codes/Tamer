package iConfWeb.bean.assignation;

import java.util.ArrayList;
import java.util.List;

public class PcMemberBean {

    private String login;

    private long nbPapers;

    private boolean wished;

    private List<String> skills;

    public PcMemberBean() {
        skills = new ArrayList<String>();
    }

    public void addSkill(String skill) {
        this.skills.add(skill);
    }

    /**
	 * @return the skills
	 */
    public List<String> getSkills() {
        return skills;
    }

    /**
	 * @param skills the skills to set
	 */
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    /**
	 * @return the login
	 */
    public String getLogin() {
        return login;
    }

    /**
	 * @param login the login to set
	 */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
	 * @return the nbPapers
	 */
    public long getNbPapers() {
        return nbPapers;
    }

    /**
	 * @param nbPapers the nbPapers to set
	 */
    public void setNbPapers(long nbPapers) {
        this.nbPapers = nbPapers;
    }

    /**
	 * @return the wished
	 */
    public boolean isWished() {
        return wished;
    }

    /**
	 * @param wished the wished to set
	 */
    public void setWished(boolean wished) {
        this.wished = wished;
    }
}
