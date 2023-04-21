package cn.jsprun.vo.basic;

public class Extcredit {

    private String id = null;

    private String title = null;

    private String unit = null;

    private boolean available = false;

    private boolean allowexchangein = false;

    private boolean allowexchangeout = false;

    private boolean showinthread = false;

    private String lowerlimit = null;

    private String ratio = null;

    private String initcredit = null;

    public String getInitcredit() {
        return initcredit;
    }

    public void setInitcredit(String initcredit) {
        this.initcredit = initcredit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAllowexchangein() {
        return allowexchangein;
    }

    public void setAllowexchangein(boolean allowexchangein) {
        this.allowexchangein = allowexchangein;
    }

    public boolean isAllowexchangeout() {
        return allowexchangeout;
    }

    public void setAllowexchangeout(boolean allowexchangeout) {
        this.allowexchangeout = allowexchangeout;
    }

    public boolean isShowinthread() {
        return showinthread;
    }

    public void setShowinthread(boolean showinthread) {
        this.showinthread = showinthread;
    }

    public String getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(String lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
