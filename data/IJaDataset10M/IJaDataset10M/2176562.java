package com.germinus.xpression.cms.model;

import java.io.Serializable;
import com.germinus.xpression.cms.contents.binary.BinaryDataReference;
import com.germinus.xpression.i18n.I18NString;

public class Virus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3007603144199876817L;

    private static final int NUMBEROFINTERESTLINKS = 5;

    private String danger = "0";

    private String diffusion = "0";

    private String disperse = "0";

    private String completeName;

    private String name;

    private String type;

    private String platform;

    private I18NString alias;

    private I18NString solution;

    private InterestLink[] interestLinks = new InterestLink[NUMBEROFINTERESTLINKS];

    private BinaryDataReference file;

    public Virus() {
        for (int i = 0; i < NUMBEROFINTERESTLINKS; i++) {
            interestLinks[i] = new InterestLink();
        }
    }

    /**
     * @return Returns the alias.
     */
    public I18NString getAlias() {
        return alias;
    }

    /**
     * @param alias The alias to set.
     */
    public void setAlias(I18NString alias) {
        this.alias = alias;
    }

    /**
     * @return Returns the completeName.
     */
    public String getCompleteName() {
        return completeName;
    }

    /**
     * @param completeName The completeName to set.
     */
    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    /**
     * @return Returns the danger.
     */
    public String getDanger() {
        return danger;
    }

    /**
     * @param danger The danger to set.
     */
    public void setDanger(String danger) {
        this.danger = danger;
    }

    /**
     * @return Returns the diffusion.
     */
    public String getDiffusion() {
        return diffusion;
    }

    /**
     * @param diffusion The diffusion to set.
     */
    public void setDiffusion(String diffusion) {
        this.diffusion = diffusion;
    }

    /**
     * @return Returns the disperse.
     */
    public String getDisperse() {
        return disperse;
    }

    /**
     * @param disperse The disperse to set.
     */
    public void setDisperse(String disperse) {
        this.disperse = disperse;
    }

    /**
     * @return Returns the file.
     */
    public BinaryDataReference getFile() {
        if (file == null) {
            file = new BinaryDataReference();
        }
        return file;
    }

    /**
     * @param file The file to set.
     */
    public void setFile(BinaryDataReference file) {
        if (file == null) {
            file = new BinaryDataReference();
        }
        this.file = file;
    }

    /**
     * @return Returns the interestLinks.
     */
    public InterestLink[] getInterestLinks() {
        return interestLinks;
    }

    /**
     * @param interestLinks The interestLinks to set.
     */
    public void setInterestLinks(InterestLink[] interestLinks) {
        this.interestLinks = interestLinks;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the platform.
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * @param platform The platform to set.
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * @return Returns the solution.
     */
    public I18NString getSolution() {
        return solution;
    }

    /**
     * @param solution The solution to set.
     */
    public void setSolution(I18NString solution) {
        this.solution = solution;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
}
