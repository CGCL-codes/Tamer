package org.personalsmartspace.psw3p.ms.impl;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.pm.prefmgr.api.pss3p.IPreferenceManager;
import org.personalsmartspace.pm.prefmodel.api.pss3p.IAction;
import org.personalsmartspace.psm.rs.api.pss3p.IResourceSharingManager;
import org.personalsmartspace.psm.rs.api.pss3p.StrategyDescriptionManager;
import org.personalsmartspace.psw3p.api.ms.IPSWService;
import org.personalsmartspace.psw3p.api.rscomp.IRoomProjectorService;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IPrivacyAware;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IService;

/**
 * 
 * @author Perumal Kuppuudaiyar,email:pkudaiyar@users.sourceforge.net
 *
 */
public class PSWServiceImpl implements IPSWService, IService, IPrivacyAware {

    private IServiceIdentifier myId;

    private String jsonReturnMessage;

    private JSONObject retObject = null;

    private PSWDataBaseService pswdbs;

    private IDigitalPersonalIdentifier servicePuplicDPI;

    private PSWContextManager ctxMgr;

    private Hashtable<String, AttendeeInfo> attenInfoHtbl;

    private Hashtable<String, List<?>> confRoomStatus;

    private Hashtable<String, List<?>> confLiveMeeting;

    private Map<?, ?> confRooms;

    private Map<String, String> confRoomsPref;

    private Map<String, String> imgString;

    private Map<String, String> bizCard;

    private ServiceTracker srtroomProjService;

    private ServiceTracker srtRsService;

    private final String ThisConfRoom = "BoardRoom";

    private String hostName;

    private PSSLog logger = new PSSLog(this);

    private IDigitalPersonalIdentifier controllerDPI;

    @SuppressWarnings("unchecked")
    private List roomEvents;

    private ServiceTracker srtPrefMgr;

    private BundleContext bc;

    public PSWServiceImpl(BundleContext context) {
        bc = context;
        pswdbs = new PSWDataBaseService();
        ctxMgr = new PSWContextManager(this, context);
        attenInfoHtbl = new Hashtable<String, AttendeeInfo>();
        confRoomStatus = new Hashtable<String, List<?>>();
        confLiveMeeting = new Hashtable<String, List<?>>();
        confRooms = new HashMap<Object, Object>();
        confRoomsPref = new Hashtable<String, String>();
        imgString = new Hashtable<String, String>();
        bizCard = new Hashtable<String, String>();
        srtroomProjService = new ServiceTracker(context, IRoomProjectorService.class.getName(), null);
        srtroomProjService.open();
        srtRsService = new ServiceTracker(context, IResourceSharingManager.class.getName(), null);
        srtRsService.open();
        srtPrefMgr = new ServiceTracker(context, IPreferenceManager.class.getName(), null);
        srtPrefMgr.open();
        hostName = getHostName();
        logger.debug(" *** PSWServiceImpl Intantiated...*** ");
        resetConferenceRoom();
        sychroniseCofRooms();
        sychroniseMeetingStatus();
        sychroniseRoomPreference();
        retObject = new JSONObject();
    }

    private void resetConferenceRoom() {
        pswdbs.resetConfRoomDataBase();
    }

    private String getHostName() {
        try {
            String computername = InetAddress.getLocalHost().getHostName();
            return computername;
        } catch (Exception e) {
            logger.debug(" *** Exception caught while reading host name ***", e);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "deprecation", "static-access" })
    public void getPSWService(String jsonStringMsg, ICallbackListener listener) {
        JSONObject jsonObjInMsg = JSONObject.fromObject(jsonStringMsg);
        int ReqID = Integer.parseInt(jsonObjInMsg.get("ConfServiceRequestID").toString());
        retObject.clear();
        switch((ReqID)) {
            case GET_PSW_INITIALDATA:
                {
                    String strDPI = jsonObjInMsg.get("consumerDPI").toString();
                    logger.debug(" *** Get_PSW_Intialdata request received for " + strDPI + " *** ");
                    String bizCard = jsonObjInMsg.get("BizCard").toString();
                    logger.debug(" *** BizCard " + bizCard + " *** ");
                    this.bizCard.put(strDPI, bizCard);
                    try {
                        updateAgenda(strDPI, bizCard);
                        retObject.put("Result", "Success");
                    } catch (Exception e) {
                        logger.debug(" *** Exception while updating bizcard & agenda *** ", e);
                        retObject.put("Result", "Failed to update user agenda attribute");
                    }
                    break;
                }
            case ADD_NEW_CONFERENCE_ROOM:
                {
                    logger.debug(" *** Add new conference romm requested *** ");
                    String RoomID = jsonObjInMsg.get("RoomID").toString();
                    String MaxSeats = jsonObjInMsg.get("MaxSeats").toString();
                    String Printer = jsonObjInMsg.get("Printer").toString();
                    String Projector = jsonObjInMsg.get("Projector").toString();
                    jsonReturnMessage = pswdbs.AddNewConferenceRoom(RoomID, MaxSeats, Printer, Projector);
                    retObject.put("Result", jsonReturnMessage);
                    break;
                }
            case GET_CONFERENCE_ROOM_LIST:
                {
                    try {
                        logger.debug(" *** Request conference room list *** ");
                        List<String> result = pswdbs.GetConferenceRoomslist();
                        retObject.put("Result", "Success");
                        retObject.put("ConfTableData", result);
                    } catch (Exception e) {
                        logger.debug(" *** Exception while getting conference room list *** ");
                        retObject.put("Result", "Failed " + e.toString());
                    }
                    break;
                }
            case REMOVE_CONFERENCE_ROOMS:
                {
                    logger.debug(" *** Remove conference room requested *** ");
                    JSONArray jsonArrRooms = JSONArray.fromObject(jsonObjInMsg.get("roomids"));
                    List<String> ConfRoomList = jsonArrRooms.toList(jsonArrRooms);
                    jsonReturnMessage = pswdbs.RemoveConferenceRoom(ConfRoomList);
                    retObject.put("Result", jsonReturnMessage);
                    break;
                }
            case SETUP_A_CONFERENCE_MEETING:
                {
                    logger.debug(" *** Setup conference meeting requested *** ");
                    String strStartDtTime = jsonObjInMsg.get("StartDtTime").toString();
                    String strEndDtTime = jsonObjInMsg.get("EndDtTime").toString();
                    String strRoomID = jsonObjInMsg.get("RoomID").toString();
                    String strSeatingType = jsonObjInMsg.get("SeatingType").toString();
                    String strMeetingName = jsonObjInMsg.get("MeetingName").toString();
                    int intMaxAttendees = Integer.parseInt(jsonObjInMsg.get("MaxAttendees").toString());
                    jsonReturnMessage = pswdbs.SetUpNewMeeting(strStartDtTime, strEndDtTime, strRoomID, strSeatingType, strMeetingName, intMaxAttendees);
                    retObject.put("Result", jsonReturnMessage);
                    break;
                }
            case GET_ALL_MEETINGS_LIST:
                {
                    logger.debug(" *** Get all meeting list requested *** ");
                    try {
                        List<?> result = pswdbs.GetConferenceMeetinglist();
                        retObject.put("Result", "Success");
                        retObject.put("MeetingList", result);
                    } catch (Exception e) {
                        retObject.put("Result", "Failed " + e.toString());
                    }
                    break;
                }
            case DELETE_A_MEETINGS_FROM_DB:
                {
                    logger.debug(" *** Delete meeting requested *** ");
                    JSONArray jsonArrMeeting = JSONArray.fromObject(jsonObjInMsg.get("meetingid"));
                    List<String> meetingid = jsonArrMeeting.toList(jsonArrMeeting);
                    jsonReturnMessage = pswdbs.RemoveMeeting(meetingid);
                    retObject.put("Result", jsonReturnMessage);
                    break;
                }
            case UPLOAD_AGENDA_FOR_A_MEETING:
                {
                    logger.debug(" *** Upload agenda request received *** ");
                    int intMeetingid = Integer.parseInt(jsonObjInMsg.get("meetingid").toString());
                    String AgendaXMLstring = jsonObjInMsg.get("agenda").toString();
                    jsonReturnMessage = pswdbs.InsertMeetingAgenda(intMeetingid, AgendaXMLstring);
                    retObject.put("Result", jsonReturnMessage);
                    break;
                }
            case SUBMIT_FEEDBACK:
                {
                    logger.debug(" *** Feedback submit request received *** ");
                    int intMeetingid = Integer.parseInt(jsonObjInMsg.get("meetingid").toString());
                    String peerid = jsonObjInMsg.get("fbpeerid").toString();
                    String XMLFeedbackdata = jsonObjInMsg.get("feedbackxml").toString();
                    jsonReturnMessage = pswdbs.SubmitFeedback(intMeetingid, peerid, XMLFeedbackdata);
                    retObject.put("Result", jsonReturnMessage);
                    break;
                }
            case GET_FEEDBACK_SUMMARY:
                {
                    logger.debug(" *** Get Feedback summary request received *** ");
                    int intMeetingid = Integer.parseInt(jsonObjInMsg.get("meetingid").toString());
                    jsonReturnMessage = pswdbs.GetFeedbackSummary(intMeetingid);
                    if (jsonReturnMessage != null) retObject.put("Result", "Success"); else retObject.put("Result", "Failed");
                    retObject.put("XMLFeedbackData", jsonReturnMessage);
                    break;
                }
            case SYCHRONISE_CONFROOM:
                {
                    logger.debug(" *** Sychronise conference room request received *** ");
                    try {
                        sychroniseCofRooms();
                        sychroniseMeetingStatus();
                        sychroniseRoomPreference();
                        this.updateAlertContext();
                        retObject.put("Result", "Success");
                    } catch (Exception e) {
                        logger.debug(" *** failed to sychronise conference room *** ", e);
                        retObject.put("Result", " Failed to Sychronise conference system");
                    }
                    break;
                }
            case UPDATE_AGENDA_ALL:
                {
                    logger.debug(" *** update agenda for received *** ");
                    try {
                        updateAgendaAll();
                        retObject.put("Result", "Success");
                    } catch (ContextException e) {
                        logger.debug(" *** ContextException occur while updating with agenda *** ", e);
                        retObject.put("Result", " Failed to update agenda for all-ContextException");
                    } catch (Exception e) {
                        logger.debug(" *** Exception occur while updating with agenda *** ", e);
                        retObject.put("Result", " Failed to update agenda for all-Exception");
                    }
                    break;
                }
            case UPDATE_SEAT_STATUSES:
                {
                    logger.debug("*** Requesting PSW Status Updates....*** ");
                    String strDPI = jsonObjInMsg.get("consumerDPI").toString();
                    retObject = this.getPSWUpdateString(strDPI);
                    break;
                }
            case GET_IMAGE_STRING:
                {
                    logger.info("*** Requesting imageString Updates....*** ");
                    String key = jsonObjInMsg.get("ImgKey").toString();
                    logger.debug("***Image requested for key : " + key);
                    if (imgString.containsKey(key)) {
                        retObject.put("Result", "Success");
                        retObject.put("ImgKey", key);
                        retObject.put("ImageString", imgString.get(key));
                    } else {
                        retObject.put("Result", "Failed, Image string unvailable");
                    }
                    break;
                }
            case ADD_IMAGE_STRING:
                {
                    logger.debug("*** Requesting ImagURL Updates....*** ");
                    String key = jsonObjInMsg.get("ImgKey").toString();
                    String imgStr = jsonObjInMsg.get("ImgString").toString();
                    logger.debug("***adding Image String key : " + key);
                    logger.debug("***Image key : " + key + "Image url : " + imgStr + " *** ");
                    imgString.put(key, imgStr);
                    break;
                }
            case GET_BIZCARD:
                {
                    logger.debug("*** Requesting Bizcard Updates....*** ");
                    String strDPI = jsonObjInMsg.get("consumerDPI").toString();
                    String bizcard = this.bizCard.get(strDPI);
                    logger.debug("Bizcard :" + bizcard + " dpi " + strDPI);
                    if (bizcard != null) {
                        retObject.put("Result", "Success");
                        retObject.put("BizCard", bizcard);
                        retObject.put("DPI", strDPI);
                    } else {
                        retObject.put("Result", "Failed");
                    }
                    break;
                }
            default:
                retObject.put("Result", "Failed unknown service id received");
        }
        retObject.put("ConfServiceRequestID", ReqID);
        jsonReturnMessage = retObject.toString();
        retObject.clear();
        logger.debug("Out going Msg from PSW Service " + jsonReturnMessage);
        listener.handleCallbackString(XMLConverter.objectToXml(jsonReturnMessage));
    }

    private void updateAgendaAll() throws ContextException {
        Set<String> strDPISet = attenInfoHtbl.keySet();
        this.ctxMgr.updateAgendaAll(strDPISet, this.attenInfoHtbl);
    }

    private void updateAgenda(String strDPI, String bizCard) throws Exception {
        String roomid = this.attenInfoHtbl.get(strDPI).getRoomID();
        String agenda = this.getAgenda(roomid);
        this.ctxMgr.updateAgenda(strDPI, agenda);
    }

    public JSONObject getPSWUpdateString(String strDPI) {
        String roomID = this.attenInfoHtbl.get(strDPI).getRoomID();
        logger.debug(" *** Roomid is for UpdateString *** " + roomID + " consumerDPI " + strDPI);
        JSONObject retObj = new JSONObject();
        try {
            retObj.put("Result", "Success");
            retObj.put("RoomEvents", roomEvents);
            if (roomID == null) {
                retObj.put("RoomID", "xxxxx");
                return retObj;
            } else if (!confRooms.containsKey(roomID)) {
                retObj.put("RoomID", roomID);
                retObj.put("IsRoomValid", "No");
                return retObj;
            } else if (this.confLiveMeeting.get(roomID) != null) {
                retObj.put("MeetingAvail", "Yes");
                retObj.put("MyMeeting", this.confLiveMeeting.get(roomID));
                retObj.put("SeatStatusAvail", "Yes");
                retObj.put("SeatStatusList", this.confRoomStatus.get(roomID));
                retObj.put("RoomID", roomID);
                retObj.put("IsRoomValid", "Yes");
                if (confRoomsPref.containsKey(roomID)) {
                    retObj.put("RoomPref", confRoomsPref.get(roomID));
                    confRoomStatus.clone();
                } else {
                    retObj.put("RoomPref", "18_650");
                }
            } else {
                retObj.put("MeetingAvail", "No");
                retObj.put("RoomID", roomID);
            }
            retObj.put("PSWHostName", hostName);
            retObj.put("MyPSWDataAvail", "Yes");
            retObj.put("MyPSWData", getAttendeeData(strDPI));
        } catch (Exception e) {
            retObj.put("Result", "Failed " + e.toString());
            e.printStackTrace();
        }
        return retObj;
    }

    public IServiceIdentifier getID() {
        return myId;
    }

    public void setID(IServiceIdentifier id) {
        myId = id;
        IResourceSharingManager rsMgr = getRSMgr();
        logger.debug(" *** Registering with resource sharing *** ");
        rsMgr.registerSharableResource(id, this, StrategyDescriptionManager.getPriorityStrategy("task", "Presenting"));
        logger.debug("PSW Service : my id is:" + myId);
    }

    private IResourceSharingManager getRSMgr() {
        return (IResourceSharingManager) srtRsService.getService();
    }

    public boolean startUserSession(String sessionId, IDigitalPersonalIdentifier consumerDPI, IDigitalPersonalIdentifier publicDPI) {
        logger.debug(" Session started ID :" + sessionId + "Puplic DPI :" + publicDPI.toUriString() + "consumerDPI:" + consumerDPI.toUriString());
        this.servicePuplicDPI = publicDPI;
        String attendeename = ctxMgr.getStringAttFromCtx(consumerDPI, "name");
        try {
            pswdbs.registerUserData(this.ThisConfRoom, consumerDPI.toUriString(), "started", attendeename);
            sychroniseCofRooms();
            sychroniseMeetingStatus();
            this.updateUserPref(consumerDPI);
            this.updateAlertContext();
        } catch (Exception e) {
            logger.debug("*** Error while registering user into PSW Service  ***", e);
            e.printStackTrace();
        }
        return true;
    }

    public boolean stopUserSession(String sessionId, IDigitalPersonalIdentifier consumerDPI, IDigitalPersonalIdentifier publicDPI) {
        logger.debug("End session " + sessionId + " for \"" + consumerDPI + "\" at " + publicDPI);
        pswdbs.unRegisterAttendee(consumerDPI.toUriString());
        this.attenInfoHtbl.remove(consumerDPI.toUriString());
        this.imgString.remove(consumerDPI.toUriString());
        this.bizCard.remove(consumerDPI.toUriString());
        this.updateAlertContext();
        return true;
    }

    public void updateRoomIDChange(String strDPI, String newRoomId) {
        logger.debug(" user entered into room RoomID : " + newRoomId);
        String attendeename = attenInfoHtbl.get(strDPI).getAttendName();
        try {
            pswdbs.registerUserData(newRoomId, strDPI, "started", attendeename);
            updateAlertContext();
            String agenda = this.getAgenda(newRoomId);
            this.ctxMgr.updateAgenda(strDPI, agenda);
        } catch (Exception e) {
            logger.debug("*** Failed re-register person new room location " + newRoomId + " ***");
            e.printStackTrace();
        }
    }

    public void updateAlertContext() {
        sychroniseAttendees();
        sychroniseRoomStatus();
        roomEvents = this.pswdbs.getRoomEvents();
        Set<String> dpiSet = attenInfoHtbl.keySet();
        String strDpi = null;
        Iterator<String> itr = dpiSet.iterator();
        while (itr.hasNext()) {
            strDpi = itr.next();
            ctxMgr.updateAlertContext(strDpi, servicePuplicDPI, getPSWUpdateString(strDpi));
        }
    }

    private void sychroniseCofRooms() {
        this.confRooms = pswdbs.getRoomMap();
        logger.debug("*** sychroniseCofRooms Room List" + confRooms.toString());
    }

    public void removeAttendee(String strDPI) {
        attenInfoHtbl.remove(strDPI);
    }

    public JSONObject getAttendeeData(String strDpi) {
        JSONObject attenInfo = new JSONObject();
        attenInfo.put("AttendeeName", this.attenInfoHtbl.get(strDpi).getAttendName());
        attenInfo.put("AttnDPI", this.attenInfoHtbl.get(strDpi).getStrDPI());
        attenInfo.put("MeetingID", this.attenInfoHtbl.get(strDpi).getMeetingId());
        attenInfo.put("RoomID", this.attenInfoHtbl.get(strDpi).getRoomID());
        return attenInfo;
    }

    public void sychroniseRoomStatus() {
        logger.debug("*** Querying confefence room statuses ***");
        String roomid;
        confRoomStatus.clear();
        for (Enumeration<String> e = confLiveMeeting.keys(); e.hasMoreElements(); ) {
            roomid = e.nextElement();
            System.out.println("*** getting confefence room Status for " + roomid + " ***");
            List<?> confRoomStatus = pswdbs.getCofRoomStatus(roomid);
            this.confRoomStatus.put(roomid, confRoomStatus);
        }
    }

    @SuppressWarnings("unchecked")
    public void sychroniseRoomPreference() {
        logger.debug("*** Querying confefence room Preferences ***");
        List<?> confRoomPref = pswdbs.getRoomPreferenceList();
        confRoomsPref.clear();
        Iterator<?> iter = confRoomPref.iterator();
        while (iter.hasNext()) {
            Map<String, String> obj = (Map<String, String>) iter.next();
            String roomPref = obj.get("RoomTemp") + "_" + obj.get("RoomLight");
            String room = obj.get("confroomid");
            confRoomsPref.put(room, roomPref);
        }
    }

    public void sychroniseAttendees() {
        logger.debug("*** Querying attendee Status ***");
        List<?> attendeeList = pswdbs.getAttendeeList();
        attenInfoHtbl.clear();
        Iterator<?> iter = attendeeList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            AttendeeInfo attenInfo = new AttendeeInfo();
            Map<?, ?> atten = ((Map<?, ?>) (iter.next()));
            String strDpi = (String) atten.get("AttenDPI");
            attenInfo.setStrDPI(strDpi);
            attenInfo.setAttendName((String) atten.get("AttendeeName"));
            if ((String) atten.get("MeetingID") != null) {
                attenInfo.setMeetingId(Integer.parseInt((String) atten.get("MeetingID")));
            } else {
                attenInfo.setMeetingId(0);
            }
            attenInfo.setRoomID((String) atten.get("Roomid"));
            attenInfoHtbl.put(strDpi, attenInfo);
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    public void sychroniseMeetingStatus() {
        logger.debug("*** Querying confefence room live meeting ***");
        List<?> meetingList = pswdbs.getLiveMeetingList();
        confLiveMeeting.clear();
        Iterator<?> iter = meetingList.iterator();
        int i = 0;
        while (iter.hasNext()) {
            List<?> newMeetingList = meetingList.subList(i, i + 1);
            String roomid = (String) ((Map) (iter.next())).get("confroomid");
            logger.debug("*** Live meeting room " + roomid + " ***");
            confLiveMeeting.put(roomid, newMeetingList);
            i++;
        }
    }

    public void updateSeatingIDChange(String consumerDPI, Integer seatingId) {
        logger.debug("*** updating seat id " + seatingId + " for " + consumerDPI);
        if (seatingId > 0) {
            pswdbs.ReserveASeat(consumerDPI, seatingId);
            logger.debug("*** seat " + seatingId + " reserved for " + consumerDPI);
            updateAlertContext();
        } else {
            pswdbs.UnReserveASeat(consumerDPI, Math.abs(seatingId));
            logger.debug("*** seat " + seatingId + " unreserved for " + consumerDPI);
            updateAlertContext();
        }
    }

    public List<?> getAttendeeRoomStatusList(String strDPI) {
        String roomID = this.attenInfoHtbl.get(strDPI).getRoomID();
        return confRoomStatus.get(roomID);
    }

    public void updateUserNewStatus(String dpiStr, String newStatus) {
        String result = pswdbs.UpdateAttendeeAction(dpiStr, newStatus);
        if (result != "Failed") updateAlertContext(); else logger.debug("*** Error updating attendee status " + dpiStr);
    }

    @Override
    public void updateLocationCoOrdinates(String strDPI, String CoOrd) {
        ctxMgr.processCoOrdStringToSeatID(CoOrd, strDPI);
    }

    @Override
    public void updateUserPreference(String strDPI, String prefType, String value) {
        System.out.println("*** Preference received for user " + strDPI + " type " + prefType + " value " + value);
        try {
            if (prefType.equalsIgnoreCase("RoomTempPref")) {
                pswdbs.updateAttendeeTempPref(strDPI, Integer.parseInt(value));
            } else if (prefType.equalsIgnoreCase("RoomLightPref")) {
                pswdbs.updateAttendeeLightPref(strDPI, Integer.parseInt(value));
            }
            sychroniseRoomPreference();
            updateAlertContext();
        } catch (Exception e) {
            logger.debug("*** Error while updating preference in database ***");
            e.printStackTrace();
        }
    }

    @Override
    public void setControllerDPI(IDigitalPersonalIdentifier ctrdpi) {
        if (ctrdpi != null) logger.debug("*** New Controller received : " + ctrdpi.toUriString() + " ***"); else logger.debug("*** Controller dpi set to null ***");
        this.controllerDPI = ctrdpi;
    }

    public IDigitalPersonalIdentifier getServiceDPI() {
        return this.servicePuplicDPI;
    }

    @Override
    public void checkProjAccess(String strDPI, ICallbackListener listener) {
        logger.debug("*** received check access from " + strDPI + " ***");
        String statusReply = null;
        if (controllerDPI == null) {
            statusReply = "No one has Proj control, You can access now!";
        } else if (controllerDPI.toUriString().equals(strDPI)) {
            statusReply = "You have Proj control, can share your screen!";
        } else {
            String attName = this.attenInfoHtbl.get(controllerDPI.toUriString()).getAttendName();
            if (attName == null) attName = "Un named Person";
            statusReply = attName + " has control, press share to request access!";
        }
        Dimension dimenstion = Toolkit.getDefaultToolkit().getScreenSize();
        listener.handleCallbackString(XMLConverter.objectToXml(statusReply + "__" + dimenstion.getWidth() + "__" + dimenstion.getHeight()));
    }

    public void sendScreenImage(String strDPI, byte[] img, ICallbackListener listener) {
        logger.debug("*** received screen image for " + strDPI + " ***");
        if (controllerDPI != null) {
            logger.debug("*** controller dpi " + controllerDPI.toUriString() + " ***");
        } else {
            logger.debug("*** controller dpi is null ***");
        }
        String attn = this.attenInfoHtbl.get(strDPI).getAttendName();
        if ((attn == null) || (attn == "")) {
            attn = "un named attendee";
        }
        String statusReply = "Access denied";
        if (controllerDPI == null || strDPI.equals(controllerDPI.toUriString())) {
            logger.debug("*** controller & requester dpi matched ***");
            getRoomProjService().displayScreen(img, attn);
            statusReply = "Screen displayed!";
        } else {
            logger.debug("*** controller & requester dpi not matched, requesting control.. ***");
            String proposal = attn + " requesting to access projector, Do you wish to provide persmission?";
            boolean feedback = this.getRSMgr().queryController(this.myId, proposal);
            if (feedback) {
                getRoomProjService().displayScreen(img, attn);
                statusReply = "Access granted and Screen displayed!";
            }
            listener.handleCallbackString(XMLConverter.objectToXml(statusReply));
        }
    }

    @Override
    public void stopSharing(String strDPI, String msg) {
        logger.debug("*** received stop screen image ***");
        if (controllerDPI == null || strDPI.equals(controllerDPI.toUriString())) {
            getRoomProjService().stopDisplay();
        }
    }

    private void updateUserPref(IDigitalPersonalIdentifier consumerDPI) {
        try {
            IAction action = getPrefMgr().getPreference(servicePuplicDPI, consumerDPI, "org.personalsmartspace.psw3p.api.ms.IPSWService", this.myId, "RoomTemp");
            int roomTemp = Integer.parseInt(action.getvalue());
            pswdbs.updateAttendeeTempPref(consumerDPI.toUriString(), roomTemp);
            action = getPrefMgr().getPreference(servicePuplicDPI, consumerDPI, "org.personalsmartspace.psw3p.api.ms.IPSWService", this.myId, "RoomLight");
            int roomlight = Integer.parseInt(action.getvalue());
            pswdbs.updateAttendeeLightPref(consumerDPI.toUriString(), roomlight);
        } catch (Exception e) {
            logger.debug("Error reading and updating the preference", e);
        }
    }

    public String getAgenda(String roomID) {
        String agenda = this.pswdbs.getAgendaString(roomID);
        return agenda;
    }

    private IRoomProjectorService getRoomProjService() {
        return (IRoomProjectorService) srtroomProjService.getService();
    }

    private IPreferenceManager getPrefMgr() {
        return (IPreferenceManager) srtPrefMgr.getService();
    }

    @SuppressWarnings("unused")
    private PSWContextManager getCtxMgr() {
        return new PSWContextManager(this, bc);
    }

    @SuppressWarnings("unused")
    private PSWDataBaseService getPswdbs() {
        return new PSWDataBaseService();
    }

    private static final int GET_PSW_INITIALDATA = 1111;

    private static final int GET_BIZCARD = 2222;

    private static final int ADD_IMAGE_STRING = 4444;

    private static final int GET_IMAGE_STRING = 3333;

    private static final int ADD_NEW_CONFERENCE_ROOM = 1000;

    private static final int GET_CONFERENCE_ROOM_LIST = 1001;

    private static final int REMOVE_CONFERENCE_ROOMS = 1002;

    private static final int SETUP_A_CONFERENCE_MEETING = 1003;

    private static final int GET_ALL_MEETINGS_LIST = 1004;

    private static final int DELETE_A_MEETINGS_FROM_DB = 1005;

    private static final int UPLOAD_AGENDA_FOR_A_MEETING = 1006;

    private static final int UPDATE_SEAT_STATUSES = 2002;

    private static final int SUBMIT_FEEDBACK = 2004;

    private static final int GET_FEEDBACK_SUMMARY = 2005;

    private static final int SYCHRONISE_CONFROOM = 9999;

    private static final int UPDATE_AGENDA_ALL = 9991;
}
