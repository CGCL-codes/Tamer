package com.ideo.sweetdevria.taglib.schedule;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.ideo.sweetdevria.model.RiaComponentModel;
import com.ideo.sweetdevria.proxy.IScheduleConfigurator;
import com.ideo.sweetdevria.proxy.exception.TechnicalException;
import com.ideo.sweetdevria.proxy.model.Agenda;
import com.ideo.sweetdevria.proxy.model.Schedule;
import com.ideo.sweetdevria.taglib.AbstractTagDrawer;
import com.ideo.sweetdevria.taglib.agenda.model.AgendaColumnModel;
import com.ideo.sweetdevria.taglib.schedule.model.IGroupContent;
import com.ideo.sweetdevria.taglib.schedule.model.ScheduleColumnModel;
import com.ideo.sweetdevria.taglib.schedule.model.ScheduleModel;

/**
 * @jsp.tag name = "schedule"
 * display-name = "Schedule Tag"
 * description = "Description : Display a schedule. Nested in tag : any"
 */
public class ScheduleTag extends AbstractTagDrawer {

    private static final long serialVersionUID = -8862304881271639208L;

    private static final String BUILDER_ID = "schedule.builder";

    private String id;

    private Integer idUser;

    private IScheduleConfigurator scheduleConfigurator;

    private int startDayConstraint;

    private int timeDivision;

    private boolean firstPass;

    private int beginHour = 8;

    private int endHour = 20;

    private String startDay = "";

    private boolean activeReticle = false;

    protected ScheduleModel model;

    protected short editorOpeningWidth = 430;

    protected short editorOpeningHeight = 500;

    /****************************************************************************************************************

	Constantes permettant d'indiquer l'echelle de temps de la grille de schedule

	 ****************************************************************************************************************/
    public static final int QUARTER = 0;

    public static final int HALF = 1;

    public static final int HOUR = 2;

    /****************************************************************************************************************

	Constantes de jour de la semaine

	 ****************************************************************************************************************/
    public static final int SUNDAY = 0;

    public static final int MONDAY = 1;

    public static final int TUESDAY = 2;

    public static final int WEDNESDAY = 3;

    public static final int THURSDAY = 4;

    public static final int FRIDAY = 5;

    public static final int SATURDAY = 6;

    public String getBuilderId() {
        return BUILDER_ID;
    }

    public ScheduleTag() {
        super();
        this.setStartDayConstraint(MONDAY);
        this.setTimeDivision(HALF);
        release();
    }

    public void release() {
        super.release();
        editorOpeningWidth = 430;
        editorOpeningHeight = 500;
        messageKeys.put("labelLabel", "sweetdev-ria.schedule.event.label");
        messageKeys.put("typeLabel", "sweetdev-ria.schedule.event.type");
        messageKeys.put("agendaLabel", "sweetdev-ria.schedule.event.agenda");
        messageKeys.put("fullDayLabel", "sweetdev-ria.schedule.event.fullDay");
        messageKeys.put("startDateLabel", "sweetdev-ria.schedule.event.startDate");
        messageKeys.put("startTimeLabel", "sweetdev-ria.schedule.event.startTime");
        messageKeys.put("endDateLabel", "sweetdev-ria.schedule.event.endDate");
        messageKeys.put("endTimeLabel", "sweetdev-ria.schedule.event.endTime");
        messageKeys.put("descriptionLabel", "sweetdev-ria.schedule.event.description");
        messageKeys.put("privacyLabel", "sweetdev-ria.schedule.event.privacy");
        messageKeys.put("whereLabel", "sweetdev-ria.schedule.event.where");
        messageKeys.put("privacyDefaultLabel", "sweetdev-ria.schedule.event.privacyDefault");
        messageKeys.put("privacyPrivateLabel", "sweetdev-ria.schedule.event.privacyPrivate");
        messageKeys.put("privacyPublicLabel", "sweetdev-ria.schedule.event.privacyPublic");
        messageKeys.put("days", "sweetdev-ria.schedule.agenda.days");
        messageKeys.put("startAt", "sweetdev-ria.schedule.agenda.startAt");
        messageKeys.put("endAt", "sweetdev-ria.schedule.agenda.endAt");
        messageKeys.put("fromDate", "sweetdev-ria.schedule.agenda.fromDate");
        messageKeys.put("toDate", "sweetdev-ria.schedule.agenda.toDate");
        messageKeys.put("allDay", "sweetdev-ria.schedule.agenda.allDay");
        messageKeys.put("where", "sweetdev-ria.schedule.agenda.where");
        messageKeys.put("UnknowenWhere", "sweetdev-ria.schedule.agenda.UnknowenWhere");
        messageKeys.put("moreDetails", "sweetdev-ria.schedule.agenda.moreDetails");
        messageKeys.put("edit", "sweetdev-ria.schedule.agenda.edit");
        messageKeys.put("delete", "sweetdev-ria.schedule.agenda.delete");
        messageKeys.put("close", "sweetdev-ria.schedule.agenda.close");
        messageKeys.put("previousWeek", "sweetdev-ria.schedule.agenda.previousWeek");
        messageKeys.put("nextWeek", "sweetdev-ria.schedule.agenda.nextWeek");
        messageKeys.put("busy", "sweetdev-ria.schedule.agenda.busy");
        messageKeys.put("createEvent", "sweetdev-ria.schedule.agenda.createEvent");
        messageKeys.put("editEvent", "sweetdev-ria.schedule.agenda.editEvent");
        messageKeys.put("deleteEvent", "sweetdev-ria.schedule.agenda.deleteEvent");
        messageKeys.put("copyEvent", "sweetdev-ria.schedule.agenda.copyEvent");
        messageKeys.put("pasteEvent", "sweetdev-ria.schedule.agenda.pasteEvent");
        messageKeys.put("pasteEventOn", "sweetdev-ria.schedule.agenda.pasteEventOn");
        messageKeys.put("agendas", "sweetdev-ria.schedule.agenda.agendas");
        messageKeys.put("starthourendseance", "sweetdev-ria.schedule.error.starthourendseance");
        messageKeys.put("endhourendseance", "sweetdev-ria.schedule.error.endhourendseance");
        messageKeys.put("starthourstartseance", "sweetdev-ria.schedule.error.starthourstartseance");
        messageKeys.put("endhourstartseance", "sweetdev-ria.schedule.error.endhourstartseance");
        messageKeys.put("close", "sweetdev-ria.window.button.alt.close");
        messageKeys.put("minimize", "sweetdev-ria.window.button.alt.minimize");
        messageKeys.put("restore", "sweetdev-ria.window.button.alt.restore");
        messageKeys.put("maximize", "sweetdev-ria.window.button.alt.maximize");
        messageKeys.put("warn.overLap", "sweetdev-ria.schedule.warn.overLap");
        messageKeys.put("warn.overTime", "sweetdev-ria.schedule.warn.overTime");
        messageKeys.put("warn.forbiddenDeletion", "sweetdev-ria.schedule.warn.forbiddenDeletion");
        messageKeys.put("warn.confirmDelete", "sweetdev-ria.schedule.warn.confirmDelete");
        messageKeys.put("error.startdate", "sweetdev-ria.schedule.error.startdate");
        messageKeys.put("error.enddate", "sweetdev-ria.schedule.error.enddate");
        messageKeys.put("error.starttime", "sweetdev-ria.schedule.error.starttime");
        messageKeys.put("error.endtime", "sweetdev-ria.schedule.error.endtime");
        messageKeys.put("error.dateorder", "sweetdev-ria.schedule.error.dateorder");
        messageKeys.put("error.offTime", "sweetdev-ria.schedule.error.offTime");
        messageKeys.put("error.server.agenda.ownerId", "sweetdev-ria.schedule.error.server.agenda.ownerId");
        messageKeys.put("error.server.agenda.privacy", "sweetdev-ria.schedule.error.server.agenda.privacy");
        messageKeys.put("error.server.event.id", "sweetdev-ria.schedule.error.server.event.id");
        messageKeys.put("error.server.event.orederDate", "sweetdev-ria.schedule.error.server.event.orederDate");
        messageKeys.put("error.server.event.agenda", "sweetdev-ria.schedule.error.server.event.agenda");
        messageKeys.put("error.server.event.ownerId", "sweetdev-ria.schedule.error.server.event.ownerId");
        messageKeys.put("error.server.event.data", "sweetdev-ria.schedule.error.server.event.data");
        messageKeys.put("error.server.event.label", "sweetdev-ria.schedule.error.server.event.label");
        messageKeys.put("error.server.event.url", "sweetdev-ria.schedule.error.server.event.url");
        messageKeys.put("error.server.event.clazz", "sweetdev-ria.schedule.error.server.event.clazz");
        messageKeys.put("error.server.user.name", "sweetdev-ria.schedule.error.server.user.name");
        messageKeys.put("error.server.user.login", "sweetdev-ria.schedule.error.server.user.login");
        messageKeys.put("error.server.user.password", "sweetdev-ria.schedule.error.server.user.password");
        messageKeys.put("event.save", "sweetdev-ria.schedule.event.save");
        messageKeys.put("event.cancel", "sweetdev-ria.schedule.event.cancel");
        messageKeys.put("event.saveas", "sweetdev-ria.schedule.event.saveas");
        messageKeys.put("event.delete", "sweetdev-ria.schedule.event.delete");
    }

    public static ScheduleTag findScheduleTag(TagSupport tag) throws JspException {
        ScheduleTag scheduleTag = (ScheduleTag) findAncestorWithClass(tag, ScheduleTag.class);
        if (scheduleTag == null) throw new JspException("The tag ria:" + tag.toString() + " must be nested in a schedule tag.");
        return scheduleTag;
    }

    public int doStartTag() throws JspException {
        model = (ScheduleModel) RiaComponentModel.getInstance((HttpServletRequest) this.getPageContext().getRequest(), this.getId(), ScheduleModel.class, true);
        if (model.isEmptyModel()) {
        }
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        try {
            Schedule schedule = getScheduleConfigurator().getScheduleProvider().findByOwnerId(this.getIdUser());
            List agendas = schedule.getAgendas();
            for (Iterator iterator = agendas.iterator(); iterator.hasNext(); ) {
                Agenda agenda = (Agenda) iterator.next();
                this.addAgendaModel(agenda);
            }
            model.setIdschedule(schedule.getId());
        } catch (TechnicalException e) {
            e.printStackTrace();
        }
        model.setScheduleConfigurator(this.getScheduleConfigurator());
        model.setStartDayConstraint(this.getStartDayConstraint());
        model.setTimeDivision(this.getTimeDivision());
        model.getGroupContentModel();
        return super.doEndTag();
    }

    /**
     * @jsp.attribute required="true"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="The id of the Schedule"
     */
    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public boolean isFirstPass() {
        return firstPass;
    }

    public void setFirstPass(boolean firstPass) {
        this.firstPass = firstPass;
    }

    /**
	 * Add a column model to this grid's model
	 * @param colModel the column model to add
	 */
    public void addColumnModel(ScheduleColumnModel colModel) {
        model.addColumnModel(colModel);
    }

    public void addGroupContent(IGroupContent groupContent) {
        model.addGroupContentModel(groupContent);
    }

    /**
     * @jsp.attribute required="true"
     * rtexprvalue="true"
     * type="java.lang.Integer"
     * description="The id of the Schedule's user"
     */
    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="int"
     * description="Start day of schedule. Accepted values : SUNDAY, MONDAY(default), TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY."
     */
    public int getStartDayConstraint() {
        return this.startDayConstraint;
    }

    public void setStartDayConstraint(int startDayConstraint) {
        this.startDayConstraint = startDayConstraint;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="int"
     * description="Time Division of schedule view. Accepted values : QUARTER, HALF(default), HOUR."
     * 
     */
    public String getTimeDivision() {
        switch(this.timeDivision) {
            case 0:
                return "SweetDevRia.ScheduleConstant.QUARTER";
            case 1:
                return "SweetDevRia.ScheduleConstant.HALF";
            case 2:
                return "SweetDevRia.ScheduleConstant.HOUR";
            default:
                return "SweetDevRia.ScheduleConstant.QUARTER";
        }
    }

    public void setTimeDivision(String timeDivision) {
        if (timeDivision.equals("QUARTER")) {
            this.setTimeDivision(0);
        } else if (timeDivision.equals("HALF")) {
            this.setTimeDivision(1);
        } else if (timeDivision.equals("HOUR")) {
            this.setTimeDivision(2);
        } else {
            try {
                throw new Exception("This startDayConstraint doesn't exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setTimeDivision(int timeDivision) {
        this.timeDivision = timeDivision;
    }

    public void addAgendaModel(AgendaColumnModel colModel) {
        model.addAgendaModel(colModel);
    }

    public void addAgendaModel(Agenda agenda) {
        model.addAgendaModel(agenda);
    }

    /**
	 * @return the model
	 */
    public ScheduleModel getModel() {
        return model;
    }

    /**
	 * @param model the model to set
	 */
    public void setModel(ScheduleModel model) {
        this.model = model;
    }

    /**
	 * @return the scheduleConfigurator
	 */
    public IScheduleConfigurator getScheduleConfigurator() {
        return scheduleConfigurator;
    }

    /**
     * @jsp.attribute required="true"
     * rtexprvalue="true"
     * type="com.ideo.sweetdevria.proxy.IScheduleConfigurator"
     * description="The Schedule Configurator that configure the provider's list"
     */
    public void setScheduleConfigurator(IScheduleConfigurator scheduleConfigurator) {
        this.scheduleConfigurator = scheduleConfigurator;
    }

    /**
	 * @return the beginHour
	 */
    public int getBeginHour() {
        return beginHour;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="int"
     * description="The begin hour of schedule"
     */
    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    /**
	 * @return the endHour
	 */
    public int getEndHour() {
        return endHour;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="int"
     * description="The end hour of schedule"
     */
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    /**
	 * @return the startDay
	 */
    public String getStartDay() {
        return startDay;
    }

    /**
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="The start day of schedule"
     */
    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    /**
	 * @return activeReticle
	 */
    public boolean getActiveReticle() {
        return activeReticle;
    }

    /**
	 * @jsp.attribute required="false"
     * rtexprvalue="true"_end
     * type="boolean"
     * description="Allow the reticle activation by changing the 'activeReticle' value to true"
	 */
    public void setActiveReticle(boolean activeReticle) {
        this.activeReticle = activeReticle;
    }

    public short getEditorOpeningHeight() {
        return editorOpeningHeight;
    }

    /**
	 * @jsp.attribute required="false" rtexprvalue="true" type="short"
	 *                description="Editor window's height in pixel (540 by
	 *                default)"
	 */
    public void setEditorOpeningHeight(short editorOpeningHeight) {
        this.editorOpeningHeight = editorOpeningHeight;
    }

    public short getEditorOpeningWidth() {
        return editorOpeningWidth;
    }

    /**
	 * @jsp.attribute required="false" rtexprvalue="true" type="short"
	 *                description="Editor window's width in pixel (400 by
	 *                default)"
	 */
    public void setEditorOpeningWidth(short editorOpeningWidth) {
        this.editorOpeningWidth = editorOpeningWidth;
    }
}
