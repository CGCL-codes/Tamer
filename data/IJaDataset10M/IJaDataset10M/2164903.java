package easyaccept.webcontrol.pojo;

import java.util.Date;
import net.sf.hibernate4gwt.pojo.java5.LazyPojo;

/**
 * 
 * @author WebControlGWT.html Silva
 * 
 */
public class ArchiveGwt extends LazyPojo {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4702960083906560402L;

    private int id;

    private String name;

    private ProjectGwt projectGwt;

    private Date dtRegister;

    /** default constructor */
    public ArchiveGwt() {
    }

    /** minimal constructor */
    public ArchiveGwt(String name) {
        super();
        this.name = name;
    }

    /** full constructor */
    public ArchiveGwt(int id, String name, Date dtRegister, ProjectGwt projectGwt) {
        super();
        this.id = id;
        this.name = name;
        this.projectGwt = projectGwt;
        this.dtRegister = dtRegister;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDtRegister() {
        return dtRegister;
    }

    public void setDtRegister(Date dtRegister) {
        this.dtRegister = dtRegister;
    }

    public ProjectGwt getProject() {
        return projectGwt;
    }

    public void setProject(ProjectGwt projectGwt) {
        this.projectGwt = projectGwt;
    }
}
