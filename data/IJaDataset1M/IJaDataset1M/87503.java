package com.gdpu.project.action.achievementManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.gdpu.page.vo.PageView;
import com.gdpu.page.vo.QueryResult;
import com.gdpu.project.service.ChengGuoService;
import com.gdpu.project.service.TeacherService;
import com.gdpu.project.util.DateUtil;
import com.gdpu.project.vo.CgConference;
import com.gdpu.project.vo.Teacher;
import com.opensymphony.xwork2.ActionSupport;

public class ConferenceAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private HttpServletRequest request;

    private HttpSession session;

    private int id;

    private String conferenceId;

    private String teacherId;

    private String teacherName;

    private String conferenceName;

    private String conferenceDate;

    private String conferenceTheme;

    private float keYanFen;

    private String comment;

    private PageView<CgConference> pageView;

    private int currentpage = 1;

    private TeacherService teacherService;

    private ChengGuoService chengguoService;

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public PageView<CgConference> getPageView() {
        return pageView;
    }

    public void setChengguoService(ChengGuoService chengguoService) {
        this.chengguoService = chengguoService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getConferenceDate() {
        return conferenceDate;
    }

    public void setConferenceDate(String conferenceDate) {
        this.conferenceDate = conferenceDate;
    }

    public String getConferenceTheme() {
        return conferenceTheme;
    }

    public void setConferenceTheme(String conferenceTheme) {
        this.conferenceTheme = conferenceTheme;
    }

    public float getKeYanFen() {
        return keYanFen;
    }

    public void setKeYanFen(float keYanFen) {
        this.keYanFen = keYanFen;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    public String addConference() throws Exception {
        request = ServletActionContext.getRequest();
        session = request.getSession();
        CgConference cgConference = new CgConference();
        cgConference.setComment(getComment());
        cgConference.setConferenceDate(DateUtil.stringToDate(getConferenceDate()));
        cgConference.setConferenceId(getConferenceId());
        cgConference.setConferenceName(getConferenceName());
        cgConference.setConferenceTheme(getConferenceTheme());
        cgConference.setKeYanFen(getKeYanFen());
        List<Teacher> teacherList = teacherService.findByName(teacherName);
        if (teacherList.size() > 0) {
            cgConference.setTeacherId(teacherList.get(0).getTeacherId());
        }
        chengguoService.addCgConference(cgConference);
        List<CgConference> cgConferencelist = findAllCgConferenceDepRole();
        request.setAttribute("cgConferencelist", cgConferencelist);
        return SUCCESS;
    }

    public String findAllConference() throws Exception {
        request = ServletActionContext.getRequest();
        session = request.getSession();
        List<CgConference> cgConferencelist = findAllCgConferenceDepRole();
        request.setAttribute("cgConferencelist", cgConferencelist);
        return SUCCESS;
    }

    /**
	 * 根据id查找学术会议
	 * @return
	 * @throws Exception
	 */
    public String findConferenceById() throws Exception {
        request = ServletActionContext.getRequest();
        session = request.getSession();
        CgConference cgConference = chengguoService.findCgConference(getId());
        request.setAttribute("cgConference", cgConference);
        return SUCCESS;
    }

    /**
	 * 根据id删除学术会议
	 * @return
	 * @throws Exception
	 */
    public String delConferenceById() throws Exception {
        request = ServletActionContext.getRequest();
        session = request.getSession();
        chengguoService.deleteCgConferenceById(getId());
        List<CgConference> cgConferencelist = findAllCgConferenceDepRole();
        request.setAttribute("cgConferencelist", cgConferencelist);
        return SUCCESS;
    }

    public String updateConference() throws Exception {
        request = ServletActionContext.getRequest();
        session = request.getSession();
        CgConference cgConference = chengguoService.findCgConference(getId());
        if (cgConference != null) {
            cgConference.setComment(getComment());
            cgConference.setConferenceDate(DateUtil.stringToDate(getConferenceDate()));
            cgConference.setConferenceId(getConferenceId());
            cgConference.setConferenceName(getConferenceName());
            cgConference.setConferenceTheme(getConferenceTheme());
            cgConference.setKeYanFen(getKeYanFen());
            List<Teacher> teacherList = teacherService.findByName(teacherName);
            if (teacherList.size() > 0) {
                cgConference.setTeacherId(teacherList.get(0).getTeacherId());
            }
            chengguoService.updateCgConference(cgConference);
            List<CgConference> cgConferencelist = findAllCgConferenceDepRole();
            request.setAttribute("cgConferencelist", cgConferencelist);
        }
        return SUCCESS;
    }

    /**
	 * 角色判断，管理员返回所有，教职工返回相应的
	 * @return
	 */
    private List<CgConference> findAllCgConferenceDepRole() {
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        pageView = new PageView<CgConference>(10, currentpage);
        QueryResult<CgConference> queryResult = null;
        if (teacher.getRole() == 1) {
            queryResult = chengguoService.findAllCgConference(pageView.getFirstResult(), pageView.getMaxresult());
        } else {
            queryResult = chengguoService.findAllCgConferenceByTeacherId(teacher.getTeacherId(), pageView.getFirstResult(), pageView.getMaxresult());
        }
        pageView.setQueryResult(queryResult);
        request.setAttribute("pageView", pageView);
        return queryResult.getResultlist();
    }
}
