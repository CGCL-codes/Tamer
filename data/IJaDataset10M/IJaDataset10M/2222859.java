package com.neethi.webapp.action;

import java.util.List;
import com.neethi.model.Lawyer;
import com.neethi.service.LawyerManager;
import com.neethi.service.PracticeAreaManager;
import com.opensymphony.xwork2.Preparable;

public class LawyerAction extends BaseAction implements Preparable {

    private LawyerManager lawyerManager;

    private List lawyers;

    private Lawyer lawyer;

    private Long sid;

    private PracticeAreaManager practiceAreaManager;

    public PracticeAreaManager getPracticeAreaManager() {
        return practiceAreaManager;
    }

    public void setPracticeAreaManager(PracticeAreaManager practiceAreaManager) {
        this.practiceAreaManager = practiceAreaManager;
    }

    public void setLawyerManager(LawyerManager lawyerManager) {
        this.lawyerManager = lawyerManager;
    }

    public List getLawyers() {
        return lawyers;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            String lawyerId = getRequest().getParameter("lawyer.sid");
            if (lawyerId != null && !lawyerId.equals("")) {
                lawyer = lawyerManager.get(new Long(lawyerId));
            }
        }
    }

    public String list() {
        lawyers = lawyerManager.getAll();
        return SUCCESS;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Lawyer getLawyer() {
        return lawyer;
    }

    public void setLawyer(Lawyer lawyer) {
        this.lawyer = lawyer;
    }

    public String delete() {
        lawyerManager.remove(lawyer.getSid());
        saveMessage(getText("lawyer.deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (sid != null) {
            lawyer = lawyerManager.get(sid);
        } else {
            lawyer = new Lawyer();
        }
        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }
        if (delete != null) {
            return delete();
        }
        boolean isNew = (lawyer.getSid() == null);
        lawyer.getPracticeAreas().clear();
        String[] practiceAreas = getRequest().getParameterValues("practiceAreas");
        String[] aPracticeAreas = getRequest().getParameterValues("availablePracticeAreas");
        System.out.println("Display name " + getRequest().getParameter("lawyer.displayName"));
        if (aPracticeAreas != null) {
            System.out.println("Available Practice Areas Length " + aPracticeAreas.length);
        } else {
            System.out.println("Available Practice Areas are NULL ");
        }
        if (practiceAreas != null) {
            System.out.println("Practice Areas Length " + practiceAreas.length);
        } else {
            System.out.println("Practice Areas are NULL ");
        }
        lawyerManager.save(lawyer);
        String key = (isNew) ? "lawyer.added" : "lawyer.updated";
        saveMessage(getText(key));
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}
