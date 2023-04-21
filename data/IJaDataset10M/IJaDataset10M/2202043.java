package com.patientis.ejb.scheduling;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.Stateless;
import com.patientis.ejb.common.ChainStore;
import com.patientis.ejb.common.IBeanMethod;
import com.patientis.ejb.common.IChainStore;
import com.patientis.ejb.order.ExistingOrderAdditionException;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordDetailModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.UserModel;
import com.patientis.model.common.ModelReference;
import com.patientis.model.common.ServiceCall;
import com.patientis.data.common.BaseData;
import com.patientis.data.common.ISParameter;
import com.patientis.data.common.ISParameterQuery;
import com.patientis.data.common.ModelCache;
import com.patientis.data.scheduling.SchedulingData;
import com.patientis.model.common.Converter;
import com.patientis.model.patient.ViewPatientModel;
import com.patientis.model.reference.AppointmentTypeReference;
import com.patientis.model.reference.CalendarViewReference;
import com.patientis.model.reference.IdentifierTypeReference;
import com.patientis.model.reference.LocationTypeReference;
import com.patientis.model.reference.MessageChannelReference;
import com.patientis.model.reference.MessageStatusReference;
import com.patientis.model.reference.RecordItemReference;
import com.patientis.model.reference.RefModel;
import com.patientis.model.reference.ReminderStatusReference;
import com.patientis.model.reference.ResourceScheduleTypeReference;
import com.patientis.model.reference.ScheduleFrequencyReference;
import com.patientis.model.reference.ScheduleTemplateReference;
import com.patientis.model.reference.ValueDataTypeReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.scheduling.AppointmentModel;
import com.patientis.model.scheduling.AppointmentStatusModel;
import com.patientis.model.scheduling.MessageModel;
import com.patientis.model.scheduling.MessageRecipientModel;
import com.patientis.model.scheduling.AppointmentInstanceModel;
import com.patientis.model.scheduling.ReminderModel;
import com.patientis.model.scheduling.ResourceModel;
import com.patientis.model.scheduling.FrequencyModel;
import com.patientis.model.scheduling.AppointmentTypeModel;
import com.patientis.model.scheduling.ResourceScheduleModel;
import com.patientis.model.scheduling.ScheduleTemplateModel;
import com.patientis.model.scheduling.ScheduleTemplateTimeModel;
import com.patientis.model.scheduling.UserResourceModel;
import com.patientis.model.scheduling.ViewPatientAppointmentModel;
import com.patientis.model.security.ApplicationViewModel;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.api.services.SecurityServer;
import com.patientis.framework.api.standard.StandardRecordItemReference;
import com.patientis.framework.controls.table.ITableColumn;
import com.patientis.framework.locale.FormatUtil;
import com.patientis.framework.logging.Log;
import com.patientis.business.calendar.IAppointment;
import com.patientis.business.calendar.DefaultAppointment;
import com.patientis.business.calendar.ScheduleSetup;
import com.patientis.business.calendar.SlotTimeInterval;
import com.patientis.business.common.ICustomController;
import com.patientis.business.scheduling.DefaultResourceSchedule;
import com.patientis.business.scheduling.DefaultScheduleView;
import com.patientis.framework.scheduler.IServiceScheduling;

/**
 * LabBean is the facade for the business service layer.
 *
 * Design Patterns: <a href="/functionality/rm/1000063.html">BaseService</a>
 * <br/>
 */
@SuppressWarnings({ "unchecked", "static-access" })
@Stateless
public class SchedulingBean extends SchedulingBaseBean implements ISchedulingRemote, ISchedulingLocal, IServiceScheduling {

    /**
	 * Reference to this bean
	 */
    private SchedulingBean self = this;

    /**
	 * Clear the reference cache
	 * 
	 * @param call
	 * @throws Exception
	 */
    public void clearCache(final ServiceCall call) throws Exception {
    }

    /**
	 * 
	 * @param appointments
	 * @return
	 */
    private List<Long> getPatientIds(List<ViewPatientAppointmentModel> appointments) {
        List<Long> ids = new ArrayList<Long>(appointments.size());
        for (ViewPatientAppointmentModel appt : appointments) {
            ids.add(appt.getPatientId());
        }
        return ids;
    }

    /**
	 * 
	 * @param filterForm
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<IAppointment> getAppointmentScheduleByFilter(final FormModel filterForm, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getAppointmentScheduleByFilter(filterForm, call);
            }
        };
        return (List<IAppointment>) call(method, call);
    }

    /**
	 * Adds the medication to the database if medication.isNew()
	 * returning the generated id.<br>
	 * Otherwise updates the medication effectively replacing with 
	 * this medication.  Throws an error if the medication is not found.<br>
	 * Commits transaction immediately.
	 * 
	 * @param appt medication model to be inserted or updated
	 * @param call service call
	 * @return medication id unique identifier for the medication
	 * @throws Exception
	 */
    public Long store(final AppointmentModel appt, final long patientId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    Long apptid = store(appt, patientId, chain, call);
                    chain.execute();
                    return apptid;
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * Adds the medication to the database if medication.isNew()
	 * returning the generated id.<br>
	 * Otherwise updates the medication effectively replacing with 
	 * this medication.  Throws an error if the medication is not found.<br>
	 * Commits transaction immediately.
	 * 
	 * @param appt medication model to be inserted or updated
	 * @param call service call
	 * @return medication id unique identifier for the medication
	 * @throws Exception
	 */
    public Long store(final AppointmentModel appt, final long patientId, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                try {
                    appt.setPatientId(patientId);
                    Long apptId = store(appt, chain, call);
                    return apptId;
                } catch (Exception ex) {
                    Log.exception(ex);
                    throw ex;
                }
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * @see com.patientis.framework.scheduler.IServiceScheduling#getFrequencyByRef(long, com.patientis.model.common.ServiceCall)
	 */
    public FrequencyModel getFrequencyByRef(long scheduleFrequencyRefId, ServiceCall call) throws Exception {
        return BaseData.getOrder().getFrequencyByRef(scheduleFrequencyRefId, call);
    }

    /**
	 * Get the appointment for the specific instance only includes that instance
	 * 
	 * @param appointmentInstanceId
	 * @return
	 * @throws Exception
	 */
    public AppointmentModel getAppointmentForInstance(final long appointmentInstanceId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getAppointmentForInstance(appointmentInstanceId, call);
            }
        };
        return (AppointmentModel) call(method, call);
    }

    /**
	 * Get the patient with the appointment id
	 * 
	 * @param appointmentId
	 * @return
	 * @throws Exception
	 */
    public ViewPatientModel getPatientForAppointment(final long appointmentId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getPatientForAppointment(appointmentId, call);
            }
        };
        return (ViewPatientModel) call(method, call);
    }

    /**
	 * Get the patient with the appointment id
	 * 
	 * @param appointmentId
	 * @return
	 * @throws Exception
	 */
    public ViewPatientModel getPatientForAppointment(final long appointmentId, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getPatientForAppointment(appointmentId, chain, call);
            }
        };
        return (ViewPatientModel) call(method, call);
    }

    /**
	 * Get all of the resources
	 * 
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ResourceModel> getAllResources(final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getAllResources(call);
            }
        };
        return (List<ResourceModel>) call(method, call);
    }

    /**
	 * Add or update the appointment type
	 * 
	 * @param appointmentType
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public Long store(final AppointmentTypeModel appointmentType, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    appointmentType.setAppointmentTypeRef(BaseData.getReference().storeOrReturn(appointmentType.getAppointmentTypeRef(), AppointmentTypeReference.groupName(), chain, call));
                    Long appointmentTypeId = SchedulingBean.super.store(appointmentType, chain, call);
                    chain.execute();
                    return appointmentTypeId;
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * Get user list of resources this user views
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
    public List<ResourceModel> getResourcesByUserRefId(final long userRefId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getResourcesForUser(userRefId, call);
            }
        };
        return (List<ResourceModel>) call(method, call);
    }

    /**
	 * Save the MessageModel model creating a new message or updating existing rows.
	 * 
	 * @param message Message
	 * @param beforeMessage message model before changes or null if new message
	 * @param chain chainstore defining the database transaction
	 * @param call service call
	 * @return new or existing message id
	 * @throws Exception
	 */
    @Override
    public Long store(final MessageModel message, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                if (message.getMessageDt().isNull()) {
                    message.setMessageDt(DateTimeModel.getNow());
                }
                for (MessageRecipientModel recipient : message.getRecipients()) {
                    try {
                        if (recipient.getMessageChannelRef().isId(MessageChannelReference.PORTAL.getRefId())) {
                            throw new Exception();
                        } else if (recipient.getMessageChannelRef().isId(MessageChannelReference.EMAIL.getRefId())) {
                            message.removeEmptyOrDuplicateEmails();
                            String errorMessage = sendEmail(message, recipient, call);
                            if (errorMessage != null) {
                                recipient.setMessageStatusRef(new DisplayModel(MessageStatusReference.ERROR.getRefId()));
                                recipient.setRecipientErrorText(errorMessage);
                            }
                        } else {
                        }
                        recipient.setMessageStatusRef(new DisplayModel(MessageStatusReference.PROCESSED.getRefId()));
                    } catch (Exception ex) {
                        recipient.setMessageStatusRef(new DisplayModel(MessageStatusReference.ERROR.getRefId()));
                        recipient.setRecipientErrorText(ex.getMessage());
                    }
                }
                return SchedulingBean.super.store(message, chain, call);
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * Send the portal email
	 * 
	 * @param message
	 * @param recipient
	 * @param chain
	 * @param call
	 * @throws Exception
	 */
    public String sendEmail(final MessageModel message, final MessageRecipientModel recipient, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                if (Converter.isNotEmpty(recipient.getReceivingAddress())) {
                    String errorMessage = null;
                    if (SchedulingData.isDuplicateEmail(recipient.getReceivingAddress(), message.getSubject(), Converter.convertDisplayString(message.getMessageText()).length(), call)) {
                        errorMessage = "duplicate email suppressed";
                    } else {
                        errorMessage = BaseData.getSystem().sendEmail(message.getSubject(), message.getMessageText() + "", Converter.isEmpty(message.getMessageHtml()) ? message.getMessageText() : message.getMessageHtml(), recipient.getReceivingAddress(), call);
                    }
                    return errorMessage;
                } else {
                    return null;
                }
            }
        };
        return (String) call(method, call);
    }

    /**
	 * Save the ReminderModel model creating a new reminder or updating existing rows.
	 * 
	 * @param reminder Reminder
	 * @param beforeReminder reminder model before changes or null if new reminder
	 * @param chain chainstore defining the database transaction
	 * @param call service call
	 * @return new or existing reminder id
	 * @throws Exception
	 */
    @Override
    public Long store(final ReminderModel reminder, final IChainStore chain, final ServiceCall call) throws Exception {
        if (reminder.getReminderStatusRef().isNew()) {
            reminder.setReminderStatusRef(new DisplayModel(ReminderStatusReference.ACTIVE.getRefId()));
        }
        return SchedulingBean.super.store(reminder, chain, call);
    }

    /**
	 * Remove resource schedules
	 * 
	 * @param resourceId
	 * @param startDt
	 * @param endDt
	 * @param scheduleFrequenceRefId
	 * @param scheduleType
	 * @throws Exception
	 */
    public void removeResourceSchedules(final long resourceId, final DateTimeModel startDt, final DateTimeModel endDt, final long scheduleFrequencyRefId, final ResourceScheduleTypeReference scheduleType, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    SchedulingData.removeResourceSchedules(resourceId, startDt, endDt, scheduleFrequencyRefId, scheduleType, chain, call);
                    chain.execute();
                    return null;
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
            }
        };
        call(method, call);
    }

    /**
	 * Return a list of reminders for the valued reminderDueDt.
	 * @return reminders
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ReminderModel> getActiveRemindersByReminderDueDt(final DateTimeModel startDt, final DateTimeModel endDt, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<ReminderModel> reminders = SchedulingData.getRemindersByReminderDueDt(startDt, endDt, call);
                List<ReminderModel> activeReminders = new ArrayList<ReminderModel>(reminders.size());
                for (ReminderModel reminder : reminders) {
                    if (reminder.getReminderStatusRef().isId(ReminderStatusReference.ACTIVE.getRefId())) {
                        if (reminder.getOwnerRef().isNew()) {
                            activeReminders.add(reminder);
                        } else if (reminder.getOwnerRef().isId(call.getUserRefId())) {
                            activeReminders.add(reminder);
                        } else {
                        }
                    }
                }
                if (startDt.getTimeInMillis() == DateTimeModel.getNow().getStartOfDay().getTimeInMillis()) {
                    activeReminders.addAll(getActivePastDueReminders(call));
                }
                return activeReminders;
            }
        };
        return (List<ReminderModel>) call(method, call);
    }

    /**
	 * Return a list of reminders for the valued reminderDueDt.
	 * @return reminders
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ReminderModel> getActivePastDueReminders(final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<ReminderModel> reminders = SchedulingData.getActivePastDueReminders(call);
                List<ReminderModel> activeReminders = new ArrayList<ReminderModel>(reminders.size());
                for (ReminderModel reminder : reminders) {
                    if (reminder.getOwnerRef().isNew()) {
                        activeReminders.add(reminder);
                    } else if (reminder.getOwnerRef().isId(call.getUserRefId())) {
                        activeReminders.add(reminder);
                    } else {
                    }
                }
                return activeReminders;
            }
        };
        return (List<ReminderModel>) call(method, call);
    }

    /**
	 * Get the working or blocked hours for a resource in the timespan
	 * 
	 * @param resourceRefId
	 * @param startDt
	 * @param stopDt
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<SlotTimeInterval> getResourceHours(final long resourceRefId, final DateTimeModel startDt, final DateTimeModel stopDt, final ResourceScheduleTypeReference scheduleType, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<ResourceModel> resources = getResourcesByEntityRefId(resourceRefId, call);
                List<SlotTimeInterval> slotIntervals = new ArrayList<SlotTimeInterval>(64);
                if (resources.size() > 0) {
                    long resourceId = resources.get(0).getId();
                    List<ResourceScheduleModel> schedules = SchedulingData.getResourceSchedulesByResourceDate(resourceId, startDt, stopDt, scheduleType, call);
                    for (ResourceScheduleModel schedule : schedules) {
                        SlotTimeInterval slotInterval = new SlotTimeInterval(schedule.getScheduleStartDt(), schedule);
                        slotInterval.setSlot(schedule.getSlotSequence());
                        slotIntervals.add(slotInterval);
                    }
                }
                return slotIntervals;
            }
        };
        return (List<SlotTimeInterval>) call(method, call);
    }

    /**
	 * Get the working or blocked hours for a resource in the timespan
	 * 
	 * @param resourceRefId
	 * @param startDt
	 * @param stopDt
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<SlotTimeInterval> getResourceHoursForIntervals(final List<Long> resourceRefIds, final DateTimeModel startDt, final DateTimeModel stopDt, final ResourceScheduleTypeReference scheduleType, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<SlotTimeInterval> slotIntervals = new ArrayList<SlotTimeInterval>(64);
                for (Long resourceRefId : resourceRefIds) {
                    List<ResourceModel> resources = getResourcesByEntityRefId(resourceRefId, call);
                    if (resources.size() > 0) {
                        long resourceId = resources.get(0).getId();
                        List<ResourceScheduleModel> schedules = SchedulingData.getResourceSchedulesByResourceDate(resourceId, startDt, stopDt, scheduleType, call);
                        for (ResourceScheduleModel schedule : schedules) {
                            SlotTimeInterval slotInterval = new SlotTimeInterval(schedule.getScheduleStartDt(), schedule);
                            slotInterval.setSlot(schedule.getSlotSequence());
                            boolean inList = false;
                            for (SlotTimeInterval sti : slotIntervals) {
                                if (sti.getIntervalStartDt().getMSIntoDay() == slotInterval.getIntervalStartDt().getMSIntoDay() && sti.getSlot() == slotInterval.getSlot()) {
                                    inList = true;
                                    break;
                                }
                            }
                            if (!inList) {
                                slotIntervals.add(slotInterval);
                            }
                        }
                    }
                }
                Collections.sort(slotIntervals);
                return slotIntervals;
            }
        };
        return (List<SlotTimeInterval>) call(method, call);
    }

    /**
	 * 
	 * @param setup
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public int setupSchedule(final long resourceRefId, final ScheduleSetup setup, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<ResourceModel> resources = getResourcesByEntityRefId(resourceRefId, call);
                int intervals = 0;
                if (resources.size() > 0) {
                    long resourceId = resources.get(0).getId();
                    if (setup.isWork()) {
                        IChainStore chain = new ChainStore();
                        try {
                            intervals = setupWorkSchedule(resourceId, setup, chain, call);
                            chain.execute();
                            return intervals;
                        } catch (Exception ex) {
                            Log.exception(ex);
                            chain.rollback();
                            throw ex;
                        }
                    }
                }
                return intervals;
            }
        };
        return (Integer) call(method, call);
    }

    /**
	 * 
	 * @param resourceId
	 * @param setup
	 * @param chain
	 * @param call
	 * @return
	 * @throws Exception
	 */
    private int setupWorkSchedule(final long resourceId, final ScheduleSetup setup, final IChainStore chain, final ServiceCall call) throws Exception {
        DateTimeModel start = setup.getStartDt();
        int intervals = 0;
        for (int i = 0; i < setup.getRepeatTimes(); i++) {
            intervals += setupWorkScheduleDay(start, resourceId, setup, chain, call);
            if (setup.getEveryInDays() > 0) {
                start = start.getAddDays(setup.getEveryInDays());
            } else {
                break;
            }
        }
        return intervals;
    }

    /**
	 * 
	 * @param day
	 * @param resourceId
	 * @param setup
	 * @param chain
	 * @param call
	 * @return
	 * @throws Exception
	 */
    private int setupWorkScheduleDay(final DateTimeModel day, final long resourceId, final ScheduleSetup setup, final IChainStore chain, final ServiceCall call) throws Exception {
        call.debug("setup " + day + " " + resourceId + " until " + setup.getStopDt());
        long durationMS = setup.getStopDt().getTimeInMillis() - setup.getStartDt().getTimeInMillis();
        if (durationMS <= 0 || (setup.getIntervalMS() + setup.getBufferMS()) == 0) {
            call.debug("no duration");
            return 0;
        }
        int intervals = 0;
        for (long ms = day.getTimeInMillis(); ms < day.getTimeInMillis() + durationMS; ms += (setup.getIntervalMS() + setup.getBufferMS())) {
            ResourceScheduleModel schedule = new ResourceScheduleModel();
            String sql = "select count(resource_schedule_id) from resource_schedules where resource_id = :resourceId" + " and schedule_start_dt = :scheduleStartDt and active_ind = 1";
            Object o = BaseData.sqlFirstRow(sql, ISParameter.createList(new ISParameter("resourceId", resourceId), new ISParameter("scheduleStartDt", new DateTimeModel(ms))), call);
            if (o == null || Converter.convertLong(o) == 0) {
                schedule.setResourceId(resourceId);
                schedule.setScheduleStartDt(new DateTimeModel(ms));
                schedule.setScheduleStopDt(new DateTimeModel(ms + setup.getIntervalMS()));
                schedule.setSlotSequence(setup.getSlot());
                schedule.setLocationRef(new DisplayModel(setup.getLocationRefId()));
                schedule.setScheduleTypeRef(new DisplayModel(ResourceScheduleTypeReference.WORKINGHOURS.getRefId()));
                schedule.setDefaultAppointmentTypeRef(new DisplayModel(setup.getTemplateAppointmentTypeRefId()));
                store(schedule, chain, call);
                intervals++;
            } else {
                call.debug("exists " + new DateTimeModel(ms));
            }
            if (setup.getMaxNbrSlotsAffected() > 0 && intervals == setup.getMaxNbrSlotsAffected()) {
                break;
            }
        }
        return intervals;
    }

    /**
	 * 
	 * @param setup
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public int blockSchedule(final long resourceRefId, final ScheduleSetup setup, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    blockSchedule(resourceRefId, setup, chain, call);
                    return Converter.convertInteger(chain.execute());
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
            }
        };
        return (Integer) call(method, call);
    }

    /**
	 * 
	 * @param setup
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public int blockSchedule(final int resourceRefId, final DateTimeModel startDt, final DateTimeModel endDt, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    ScheduleSetup setup = new ScheduleSetup();
                    setup.setStartDt(startDt);
                    setup.setStopDt(endDt);
                    setup.setWork(false);
                    blockSchedule(resourceRefId, setup, chain, call);
                    return Converter.convertInteger(chain.execute());
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
            }
        };
        return (Integer) call(method, call);
    }

    /**
	 * 
	 * @param setup
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public int blockSchedule(final long resourceRefId, final ScheduleSetup setup, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<ResourceModel> resources = getResourcesByEntityRefId(resourceRefId, call);
                int intervals = 0;
                if (resources.size() > 0) {
                    long resourceId = resources.get(0).getId();
                    if (!setup.isWork()) {
                        intervals = SchedulingData.blockSchedule(resourceId, setup.getStartDt(), setup.getStopDt(), setup.getMaxNbrSlotsAffected(), chain, call);
                    }
                }
                return intervals;
            }
        };
        return (Integer) call(method, call);
    }

    /**
	 * 
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<AppointmentStatusModel> getAppointmentStatuses(final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getAppointmentStatuses(call);
            }
        };
        return (List<AppointmentStatusModel>) call(method, call);
    }

    /**
	 * 
	 * @param scheduleTemplateName
	 * @param resourceId
	 * @param fromDate
	 * @param toDate
	 * @param call
	 * @throws Exception
	 */
    public long createScheduleTemplate(final String scheduleTemplateName, final long resourceId, final DateTimeModel fromDate, final DateTimeModel toDate, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                if (BaseData.getReference().getReferenceNoCache(ScheduleTemplateReference.groupName(), RefModel.getDefaultRefKey(scheduleTemplateName), false, call).isNew()) {
                    List<ResourceScheduleModel> schedules = getResourceSchedulesByDateForResource(fromDate, toDate, resourceId, call);
                    ScheduleTemplateModel template = new ScheduleTemplateModel();
                    template.setScheduleTemplateRef(new DisplayModel(0, scheduleTemplateName, 0));
                    template.setScheduleTypeRef(new DisplayModel(ResourceScheduleTypeReference.WORKINGHOURS.getRefId()));
                    for (ResourceScheduleModel resourceSchedule : schedules) {
                        ScheduleTemplateTimeModel time = new ScheduleTemplateTimeModel();
                        time.setScheduleStartDt(resourceSchedule.getScheduleStartDt());
                        time.setScheduleStopDt(resourceSchedule.getScheduleStopDt());
                        time.setResourceQuantity(time.getResourceQuantity());
                        template.getTimes().add(time);
                    }
                    return store(template, call);
                } else {
                    Log.warn("Schedule template exists: " + scheduleTemplateName);
                }
                return 0L;
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * store schedule change
	 * 
	 * @param scheduleTemplate
	 * @param setup
	 * @param resourceRefId
	 * @param startDt
	 * @param stopDt
	 * @param call
	 * @throws Exception
	 */
    public void applyScheduleTemplate(final ScheduleTemplateModel scheduleTemplate, final ScheduleSetup setup, final long resourceRefId, final DateTimeModel startDt, final DateTimeModel stopDt, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<ResourceModel> resources = getResourcesByEntityRefId(resourceRefId, call);
                IChainStore chain = new ChainStore();
                try {
                    for (ResourceModel resource : resources) {
                        for (DateTimeModel day : DateTimeModel.getDaysBetween(startDt, stopDt)) {
                            if (scheduleTemplate.getScheduleTypeRef().isId(ResourceScheduleTypeReference.WORKINGHOURS.getRefId())) {
                                ScheduleSetup blockSetup = new ScheduleSetup();
                                blockSetup.setWork(false);
                                blockSetup.setStartDt(day);
                                blockSetup.setStopDt(day.getEndOfDay());
                                blockSchedule(resourceRefId, blockSetup, chain, call);
                            }
                            for (ScheduleTemplateTimeModel time : scheduleTemplate.getTimes()) {
                                ResourceScheduleModel resourceSchedule = new ResourceScheduleModel();
                                resourceSchedule.setResourceId(resource.getId());
                                resourceSchedule.setScheduleStartDt(day.getAddMS(time.getScheduleStartDt().getMSIntoDay()));
                                resourceSchedule.setScheduleStopDt(day.getAddMS(time.getScheduleStopDt().getMSIntoDay()));
                                resourceSchedule.setResourceQuantity(time.getResourceQuantity());
                                resourceSchedule.setScheduleTypeRef(scheduleTemplate.getScheduleTemplateRef());
                                store(resourceSchedule, chain, call);
                            }
                        }
                    }
                    chain.execute();
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
                return null;
            }
        };
        call(method, call);
    }

    /**
	 * Store schedules already associated with a resource
	 * 
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public void store(final List<ResourceScheduleModel> schedules, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    for (ResourceScheduleModel schedule : schedules) {
                        SchedulingData.store(schedule, chain, call);
                    }
                    chain.execute();
                    return null;
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
            }
        };
        call(method, call);
    }

    /**
	 * Return a list of resourceSchedules for the valued scheduleStartDt.
	 * 
	 * @return resourceSchedules
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ResourceScheduleModel> getResourceSchedulesByDateForResource(final DateTimeModel startDt, final DateTimeModel endDt, final long resourceId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getResourceSchedulesByDateForResource(startDt, endDt, resourceId, call);
            }
        };
        return (List<ResourceScheduleModel>) call(method, call);
    }

    /**
	 * Save the FrequencyModel model creating a new frequency or updating existing rows.
	 * 
	 * @param frequency Frequency
	 * @param beforeFrequency frequency model before changes or null if new frequency
	 * @param chain chainstore defining the database transaction
	 * @param call service call
	 * @return new or existing frequency id
	 * @throws Exception
	 */
    public Long store(final FrequencyModel frequency, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                frequency.setScheduleFrequencyRef(BaseData.getReference().storeOrReturn(frequency.getScheduleFrequencyRef(), ScheduleFrequencyReference.groupName(), chain, call));
                return SchedulingBean.super.store(frequency, chain, call);
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * 
	 * @param patientId
	 * @param startDt
	 * @param stopDt
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<ViewPatientAppointmentModel> getAppointmentsByPatient(final long patientId, final DateTimeModel startDt, final DateTimeModel stopDt, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getAppointmentsByPatient(patientId, startDt, stopDt, call);
            }
        };
        return (List<ViewPatientAppointmentModel>) call(method, call);
    }

    /**
	 * 
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<RefModel> getResourcesRefs(final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                UserModel user = SecurityServer.getUserByRefId(call.getUserRefId());
                return SchedulingData.getResourceRefsForLocation(user.getDefaultLocationRef().getId(), call);
            }
        };
        return (List<RefModel>) call(method, call);
    }

    /**
	 * 
	 * @param appointmentId
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public long getAppointmentStatusForAppointment(long appointmentId, final ServiceCall call) throws Exception {
        if (appointmentId == 0L) {
            return 0L;
        }
        long beforeApppointmentStatusRefId = 0;
        String sql = "select appointment_status_ref_id from appointments where appointment_id = :appointmentId";
        ISParameterQuery query = new ISParameterQuery();
        query.setSql(sql);
        query.setParameters(ISParameter.createList(new ISParameter("appointmentId", appointmentId)));
        List<Long> ids = BaseData.getReference().queryForIds(query, call);
        if (ids != null && ids.size() > 0) {
            beforeApppointmentStatusRefId = ids.get(0);
        }
        return beforeApppointmentStatusRefId;
    }

    /**
	 * Save the AppointmentModel model creating a new appointment or updating existing rows.
	 * 
	 * @param appointment Appointment
	 * @param beforeAppointment appointment model before changes or null if new appointment
	 * @param chain chainstore defining the database transaction
	 * @param call service call
	 * @return new or existing appointment id
	 * @throws Exception
	 */
    public Long store(final AppointmentModel appointment, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                long id = SchedulingBean.super.store(appointment, chain, call);
                for (AppointmentInstanceModel instance : appointment.getInstances()) {
                    if (instance.isDeleted()) {
                        BaseData.deleteDeletedChildrenAfterStore(instance, instance.getResources(), chain, call);
                    }
                }
                BaseData.deleteDeletedChildrenAfterStore(appointment, appointment.getInstances(), chain, call);
                return id;
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * 
	 * @param appointment
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public Long store(final AppointmentModel appt, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                boolean statusUpdate = appt.isNotNew();
                long appointmentId = 0L;
                IChainStore chain = new ChainStore();
                try {
                    appointmentId = SchedulingBean.super.store(appt, chain, call);
                    chain.execute();
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
                return appointmentId;
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * Save the UserResourceModel model creating a new userResource or updating existing rows.
	 * 
	 * @param userResource UserResource
	 * @param beforeUserResource userResource model before changes or null if new userResource
	 * @param call service call
	 * @return new or existing userResource id
	 * @throws Exception
	 */
    public void storeUserResources(final long userId, final List<UserResourceModel> userResources, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    BaseData.deleteHql("delete from UserResourceModel where userId = :userId", ISParameter.createList(new ISParameter("userId", userId)), call);
                    for (UserResourceModel resource : userResources) {
                        SchedulingBean.super.store(resource, chain, call);
                    }
                    chain.execute();
                } catch (Exception ex) {
                    Log.exception(ex);
                    chain.rollback();
                    throw ex;
                }
                return null;
            }
        };
        call(method, call);
    }

    /**
	 * 
	 * @param appointmentId
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public long getPatientIdForAppointment(final long appointmentId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                ISParameterQuery query = new ISParameterQuery();
                String sql = "select v.patient_id " + " from appointments v " + " where v.appointment_id = :appointmentId";
                query.setSql(sql);
                query.setParameters(ISParameter.createList(new ISParameter("appointmentId", appointmentId)));
                List<Long> ids = BaseData.queryForIds(query, call);
                if (ids.size() == 1) {
                    return ids.get(0);
                } else {
                    return 0L;
                }
            }
        };
        return (Long) call(method, call);
    }

    /**
	 * 
	 * @param patientFilterForm
	 * @return
	 * @throws Exception
	 */
    public List<MessageModel> getMessagesByFilter(final FormModel messageFilterForm, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getMessagesByFilter(messageFilterForm, call);
            }
        };
        return (List<MessageModel>) call(method, call);
    }

    /**
	 * 
	 * @param patientFilterForm
	 * @return
	 * @throws Exception
	 */
    public List<AppointmentModel> getAppointmentsByFilter(final FormModel messageFilterForm, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                return SchedulingData.getAppointmentsByFilter(messageFilterForm, call);
            }
        };
        return (List<AppointmentModel>) call(method, call);
    }

    /**
	 * 
	 * @param resourceRefIds
	 * @param startDt
	 * @param stopDt
	 * @param call
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public DefaultScheduleView getScheduleView(final CalendarViewReference view, final List<Long> resourceRefIds, final DateTimeModel startDt, final DateTimeModel stopDt, final ServiceCall call) throws Exception {
        final long key = Converter.convertKey(resourceRefIds, startDt, stopDt, view.getRefId());
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<IAppointment> appts = getAppointments(resourceRefIds, startDt, stopDt, call);
                List<DefaultResourceSchedule> schedules = SchedulingData.getResourceSchedulesByDateForResourceRefIds(startDt, stopDt, resourceRefIds, call);
                DefaultScheduleView scheduleView = new DefaultScheduleView();
                scheduleView.init(resourceRefIds, schedules, startDt, stopDt, appts);
                return scheduleView;
            }
        };
        return (DefaultScheduleView) call(method, call);
    }

    /**
	 * 
	 * @param startDt
	 * @param endDt
	 * @param resourceRefIds
	 * @param call
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<DefaultResourceSchedule> getResourceSchedulesByDateForResourceRefIds(final DateTimeModel startDt, final DateTimeModel endDt, final List<Long> resourceRefIds, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<DefaultResourceSchedule> schedules = SchedulingData.getResourceSchedulesByDateForResourceRefIds(startDt, endDt, resourceRefIds, call);
                return schedules;
            }
        };
        return (List<DefaultResourceSchedule>) call(method, call);
    }

    /**
	 * 
	 * @param patientId
	 * @param appointments
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<Long> storeAppointments(final long patientId, final List<AppointmentModel> appointments, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                IChainStore chain = new ChainStore();
                try {
                    List<Long> orderIds = storeAppointments(patientId, appointments, chain, call);
                    chain.execute();
                    if (patientId > 0L) {
                        BaseData.getOrder().updateNotification(patientId, 0L, 0, call);
                    }
                    return orderIds;
                } catch (Exception ex) {
                    chain.rollback();
                    throw ex;
                }
            }
        };
        return (List<Long>) call(method, call);
    }

    /**
	 * 
	 * @param patientId
	 * @param appointments
	 * @param chain
	 * @param call
	 * @return
	 * @throws Exception
	 * @throws ExistingAppointmentAdditionException
	 */
    public List<Long> storeAppointments(final long patientId, final List<AppointmentModel> appointments, final IChainStore chain, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<Long> appointmentIds = new ArrayList<Long>(appointments.size());
                for (AppointmentModel appt : appointments) {
                    store(appt, patientId, chain, call);
                }
                return appointmentIds;
            }
        };
        return (List<Long>) call(method, call);
    }

    /**
	 * 
	 * @param patientId
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public AppointmentModel getLastAppointmentForPatient(final long patientId, final ServiceCall call) throws Exception {
        if (patientId == 0L) {
            return new AppointmentModel();
        }
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                long appointmentId = SchedulingData.getLastAppointmentForPatient(patientId, call);
                if (appointmentId > 0L) {
                    return getAppointment(appointmentId, call);
                } else {
                    return new AppointmentModel();
                }
            }
        };
        return (AppointmentModel) call(method, call);
    }

    /**
	 * 
	 * @param resourceRefId
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<Long> getAppointmentTypeRefIds(final long resourceRefId, final boolean includeBlocking, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                ResourceModel resource = getResourceForEntityRefId(resourceRefId, call);
                List<Long> includeAppTypeRefIds = new ArrayList<Long>();
                List<Long> excludeAppTypeRefIds = new ArrayList<Long>();
                if (resource.getSettingsFormId() > 0L) {
                    FormModel settingsForm = BaseData.getClinical().getFormFromCache(resource.getSettingsFormId(), call);
                    long ResourceSettingIncludedAppointmentTypes = 40034965L;
                    List<DisplayModel> included = settingsForm.getRefValuesForRecordItem(ResourceSettingIncludedAppointmentTypes);
                    includeAppTypeRefIds.addAll(DisplayModel.getIdList(included));
                    long ResourceSettingExcludedAppointmentTypes = 40034967L;
                    List<DisplayModel> excluded = settingsForm.getRefValuesForRecordItem(ResourceSettingExcludedAppointmentTypes);
                    excludeAppTypeRefIds.addAll(DisplayModel.getIdList(excluded));
                }
                return SchedulingData.getAppointmentTypeRefIds(includeAppTypeRefIds, excludeAppTypeRefIds, call.getFacilityLocationRefId(), includeBlocking, call);
            }
        };
        return (List<Long>) call(method, call);
    }

    /**
	 * @see com.patientis.ejb.scheduling.ISchedulingRemote#getAppointments(java.util.List, com.patientis.model.common.DateTimeModel, com.patientis.model.common.DateTimeModel, com.patientis.model.common.ServiceCall)
	 */
    @Override
    public List<IAppointment> getAppointments(final List<Long> resourceRefIds, final DateTimeModel startDt, final DateTimeModel stopDt, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<FormRecordModel> records = new ArrayList<FormRecordModel>();
                FormRecordModel resourceRec = new FormRecordModel();
                resourceRec.setRecordItemRef(new DisplayModel(StandardRecordItemReference.StandardScheduleFilterAppointmentResourceRefIds));
                resourceRec.setDataTypeRef(new DisplayModel(ValueDataTypeReference.REFERENCE.getRefId()));
                for (Long resourceRefId : resourceRefIds) {
                    FormRecordDetailModel det = new FormRecordDetailModel();
                    det.setRecordItemRef(new DisplayModel(StandardRecordItemReference.StandardScheduleFilterAppointmentResourceRefIds));
                    det.setDataTypeRef(new DisplayModel(ValueDataTypeReference.REFERENCE.getRefId()));
                    det.setValueRef(new DisplayModel(resourceRefId));
                    resourceRec.getDetails().add(det);
                }
                records.add(resourceRec);
                FormRecordModel startRec = new FormRecordModel();
                startRec.setRecordItemRef(new DisplayModel(StandardRecordItemReference.StandardScheduleFilterAppointmentStartDate));
                startRec.setDataTypeRef(new DisplayModel(ValueDataTypeReference.DATE.getRefId()));
                startRec.setValueDate(startDt);
                records.add(startRec);
                FormRecordModel stopRec = new FormRecordModel();
                stopRec.setRecordItemRef(new DisplayModel(StandardRecordItemReference.StandardScheduleFilterAppointmentStopDate));
                stopRec.setDataTypeRef(new DisplayModel(ValueDataTypeReference.DATE.getRefId()));
                stopRec.setValueDate(stopDt);
                records.add(stopRec);
                FormModel filterModel = new FormModel();
                filterModel.getRecords().addAll(records);
                return getAppointmentScheduleByFilter(filterModel, call);
            }
        };
        return (List<IAppointment>) call(method, call);
    }

    /**
	 * 
	 * @param settingsForm
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<DisplayModel> getSchedulableLocations(final FormModel settingsForm, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                List<DisplayModel> locations = new ArrayList<DisplayModel>();
                String sql = "select location_ref_id from locations where location_type_ref_id = " + LocationTypeReference.CLINIC.getRefId() + " and active_ind = 1";
                List<Long> refIds = BaseData.sqlQueryForIds(sql, call, ISParameter.createList());
                for (long refId : refIds) {
                    locations.add(ReferenceServer.getDisplayModel(refId));
                }
                return locations;
            }
        };
        return (List<DisplayModel>) call(method, call);
    }

    /**
	 * 
	 * @param settingsForm
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public AppointmentModel getAppointmentForVisit(final long visitId, final ServiceCall call) throws Exception {
        IBeanMethod method = new IBeanMethod() {

            public Object execute() throws Exception {
                String sql = "select appointment_id from appointments where visit_id = :visitId and active_ind = 1 order by appointment_start_dt";
                List<Long> appointmentIds = BaseData.sqlQueryForIds(sql, call, ISParameter.createList(new ISParameter("visitId", visitId)));
                AppointmentModel appt = new AppointmentModel();
                for (long appointmentId : appointmentIds) {
                    appt = getAppointment(appointmentId, call);
                    break;
                }
                return appt;
            }
        };
        return (AppointmentModel) call(method, call);
    }
}
