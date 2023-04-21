package equilibrium.commons.report.config.model;

/**
 * @author Mariusz Sieraczkiewicz
 * @author Michał Bartyzel
 * 
 * @since 0.4
 */
public class CoverBean {

    private String frontPageDesignFile = null;

    private String backPageDesignFile = null;

    private String prefix = null;

    public CoverBean(String frontPageDesignFile, String backPageDesignFile) {
        super();
        this.frontPageDesignFile = frontPageDesignFile;
        this.backPageDesignFile = backPageDesignFile;
    }

    public CoverBean(String frontPageDesignFile, String backPageDesignFile, String prefix) {
        super();
        this.frontPageDesignFile = frontPageDesignFile;
        this.backPageDesignFile = backPageDesignFile;
        this.prefix = prefix;
    }

    public CoverBean() {
        super();
    }

    public String getBackPageDesignFile() {
        return backPageDesignFile;
    }

    public void setBackPageDesignFile(String backPageDesignFile) {
        this.backPageDesignFile = backPageDesignFile;
    }

    public String getFrontPageDesignFile() {
        return frontPageDesignFile;
    }

    public void setFrontPageDesignFile(String frontPageDesignFile) {
        this.frontPageDesignFile = frontPageDesignFile;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
