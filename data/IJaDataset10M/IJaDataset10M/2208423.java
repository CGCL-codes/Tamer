package de.ifgi.argoomap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import de.ifgi.argoomap.beans.MarkPlacemarkRelationJSONBean;
import de.ifgi.argoomap.beans.MessageDataSetJSONBean;
import de.ifgi.argoomap.beans.MessageHeaderJSONBean;
import de.ifgi.argoomap.beans.MessageJSONBean;
import de.ifgi.argoomap.beans.MessageThreadParametersJSONBean;
import de.ifgi.argoomap.beans.PlacemarkJSONBean;
import de.ifgi.argoomap.db.DB;
import de.ifgi.argoomap.properties.ArgoomapProperties;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.bean.BeanMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * Servlet implementation class for Servlet: Main
 * 
 */
public class Main extends javax.servlet.http.HttpServlet {

    static final long serialVersionUID = 1L;

    DB db;

    OnDemandGeocoder gc;

    PrintWriter writer;

    ArgoomapProperties props;

    String exceptionString = "";

    StackTraceElement[] stackTrace = null;

    public Main() {
        super();
        try {
            this.props = new ArgoomapProperties("settings.properties");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            DriverManager.setLoginTimeout(0);
            Connection conn = DriverManager.getConnection(this.props.getMySQLConnectionString());
            db = new DB(conn);
            gc = new OnDemandGeocoder(db, this.props.getCRFTaggerDir());
            System.out.println("Argoomap started.");
        } catch (SQLException e) {
            this.exceptionString = e.toString();
            this.stackTrace = e.getStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Could not connect to the MySQL server. Make sure the server is running and you are using the correct username and password.");
        } catch (Exception e) {
            this.exceptionString = e.getMessage();
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("googleMapsKey", this.props.getGoogleMapsKey());
        RequestDispatcher disp = request.getRequestDispatcher("WEB-INF/jsp/index.jsp");
        disp.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        this.writer = response.getWriter();
        String action = (String) request.getParameter("action");
        if (action != null) {
            if (action.equals("savePlacemarks")) {
                String param = request.getParameter("placemarks");
                if (param != null) savePlacemarks(param);
            } else if (action.equals("saveMessage")) {
                String param = request.getParameter("message");
                if (param != null) saveMessage(param);
            } else if (action.equals("saveMarkPlacemarkRelations")) {
                String param = request.getParameter("markPlacemarkRelations");
                if (param != null) saveMarkPlacemarkRelations(param);
            } else if (action.equals("saveThreadParameters")) {
                String param = request.getParameter("threadParameters");
                if (param != null) saveThreadParameters(param);
            } else if (action.equals("getMessages")) {
                String param = request.getParameter("childOf");
                if (param != null) {
                    try {
                        int iParam = Integer.parseInt(param);
                        if (iParam >= 0) getMessages(iParam);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("getMessageHeaders")) {
                String param = request.getParameter("childOf");
                if (param != null) {
                    try {
                        int iParam = Integer.parseInt(param);
                        if (iParam >= 0) getMessageHeaders(iParam);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("getMessageHeadersInBounds")) {
                String param = request.getParameter("bbox");
                if (param != null) {
                    getMessageHeaders(param);
                }
            } else if (action.equals("getMessageThread")) {
                String param = request.getParameter("startMessageId");
                if (param != null) {
                    try {
                        int iParam = Integer.parseInt(param);
                        if (iParam >= 0) getMessageThread(iParam);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("getThreadPlacemarks")) {
                String param = request.getParameter("startMessageId");
                if (param != null) {
                    try {
                        int iParam = Integer.parseInt(param);
                        if (iParam >= 0) getThreadPlacemarks(iParam);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("getMessagePlacemarkRelations")) {
                String param = request.getParameter("startMessageId");
                if (param != null) {
                    try {
                        int iParam = Integer.parseInt(param);
                        if (iParam >= 0) getMessagePlacemarkRelations(iParam);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("getThreadParameters")) {
                String param = request.getParameter("startMessageId");
                if (param != null) {
                    try {
                        int iParam = Integer.parseInt(param);
                        if (iParam >= 0) getThreadParameters(iParam);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("geocode")) {
                String param = request.getParameter("params_json");
                if (param != null) {
                }
            } else if (action.equals("onDemandGeocoding")) {
                String param1 = request.getParameter("markPlacemarkRelations");
                String param2 = request.getParameter("message");
                String param3 = request.getParameter("placemarks");
                String param4 = request.getParameter("threadParams");
                if (param1 != null && param2 != null && param3 != null && param4 != null) {
                    onDemandGeocode(param1, param2, param3, param4);
                }
            }
        }
    }

    private void savePlacemarks(String placemarks_json) {
        placemarks_json = placemarks_json.replace("\\", "");
        Map<String, Class<Placemark>> hashMap = new HashMap<String, Class<Placemark>>();
        hashMap.put("placemarks", Placemark.class);
        JsonConfig config = new JsonConfig();
        config.setClassMap(hashMap);
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(placemarks_json, config);
        List<Placemark> placemarks = new ArrayList<Placemark>();
        Morpher dynaMorpher = new BeanMorpher(Placemark.class, JSONUtils.getMorpherRegistry());
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        morpherRegistry.registerMorpher(dynaMorpher);
        for (Iterator iter = JSONArray.toCollection(jsonArray).iterator(); iter.hasNext(); ) {
            Placemark pm = (Placemark) morpherRegistry.morph(Placemark.class, iter.next());
            placemarks.add(pm);
        }
        try {
            placemarks = db.savePlacemarks(placemarks);
            writer.write(JSONArray.fromObject(placemarks).toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveMessage(String message_json) {
        JSONObject jsonObject = JSONObject.fromObject(message_json);
        Message message = (Message) JSONObject.toBean(jsonObject, Message.class);
        try {
            db.saveMessage(message);
            writer.write(JSONObject.fromObject(message).toString());
            if (!message.getChildOf().equals("0")) {
                Properties props = new Properties();
                props.put("mail.from", "argoomap@googlemail.com");
                props.put("mail.user", "Marius");
                props.put("mail.smtp.user", "argoomap@googlemail.com");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", 465);
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.port", 465);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                Authenticator auth = new SMTPAuthenticator();
                javax.mail.Session session = javax.mail.Session.getInstance(props, auth);
                Message replyMessage = db.getMessage(message.getChildOf());
                if (replyMessage.getEmail() != "") {
                    try {
                        MimeMessage msg = new MimeMessage(session);
                        msg.setFrom();
                        msg.setRecipients(javax.mail.Message.RecipientType.TO, replyMessage.getEmail());
                        msg.setSubject("You received a reply for your post on ArgooMap.");
                        msg.setSentDate(new Date());
                        msg.setText("Dear " + replyMessage.getAuthor() + "!\nYou received a reply for your post \"" + replyMessage.getTitle() + "\".\nPlease go to http://austerschulte.dyndns.org:8080/argoomap/Main to see it.\n\nCheers\nMarius");
                        Transport.send(msg);
                    } catch (MessagingException mex) {
                        System.out.println("send failed, exception: " + mex);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveMarkPlacemarkRelations(String markPlacemarkRelations_json) {
        markPlacemarkRelations_json = markPlacemarkRelations_json.replace("\\", "");
        JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(markPlacemarkRelations_json);
        List<MarkPlacemarkRelation> mprlist = new ArrayList<MarkPlacemarkRelation>();
        Morpher dynaMorpher = new BeanMorpher(MarkPlacemarkRelation.class, JSONUtils.getMorpherRegistry());
        MorpherRegistry morpherRegistry = JSONUtils.getMorpherRegistry();
        morpherRegistry.registerMorpher(dynaMorpher);
        for (Iterator iter = JSONArray.toCollection(jsonArray).iterator(); iter.hasNext(); ) {
            MarkPlacemarkRelation pm = (MarkPlacemarkRelation) morpherRegistry.morph(MarkPlacemarkRelation.class, iter.next());
            mprlist.add(pm);
        }
        try {
            db.saveMarkPlacemarkRelations(mprlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveThreadParameters(String threadParameters_json) {
        JSONObject jsonObject = JSONObject.fromObject(threadParameters_json);
        MessageThreadParameters mtp = new MessageThreadParameters();
        JSONArray featureClasses = (JSONArray) jsonObject.get("featureClasses");
        mtp.setThread_id(jsonObject.getString("threadId"));
        mtp.setMinX(jsonObject.getString("minX"));
        mtp.setMinY(jsonObject.getString("minY"));
        mtp.setMaxX(jsonObject.getString("maxX"));
        mtp.setMaxY(jsonObject.getString("maxY"));
        mtp.setMapCenterX(jsonObject.getString("mapCenterX"));
        mtp.setMapCenterY(jsonObject.getString("mapCenterY"));
        mtp.setMapZoomLevel(jsonObject.getString("mapZoomLevel"));
        List<String> featureClassesList = new ArrayList<String>();
        if (featureClasses != null) {
            for (Iterator iter = featureClasses.iterator(); iter.hasNext(); ) {
                featureClassesList.add((String) iter.next());
            }
            mtp.setFeatureClasses(featureClassesList);
        }
        try {
            db.saveMessageThreadParameters(mtp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getMessages(int childOf) {
        try {
            List<MessageJSONBean> messages = db.getMessages(((Integer) childOf).toString());
            writer.write(JSONArray.fromObject(messages).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getMessageHeaders(int childOf) {
        try {
            List<MessageHeaderJSONBean> msgHeaders = db.getMessageHeaders(((Integer) childOf).toString());
            writer.write(JSONArray.fromObject(msgHeaders).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMessageHeaders(String bbox_json) {
        try {
            JSONObject jsonObject_bbox = JSONObject.fromObject(bbox_json);
            BoundingBox bbox = new BoundingBox(jsonObject_bbox.getDouble("minX"), jsonObject_bbox.getDouble("minY"), jsonObject_bbox.getDouble("maxX"), jsonObject_bbox.getDouble("maxY"));
            List<MessageHeaderJSONBean> msgHeaders = db.getMessageHeaders(bbox);
            writer.write(JSONArray.fromObject(msgHeaders).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMessageThread(int startMessageId) {
        try {
            List<MessageJSONBean> messages = db.getMessageThread(((Integer) startMessageId).toString());
            writer.write(JSONArray.fromObject(messages).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getThreadPlacemarks(int startMessageId) {
        try {
            List<PlacemarkJSONBean> placemarks = db.getThreadPlacemarks(((Integer) startMessageId).toString());
            writer.write(JSONArray.fromObject(placemarks).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getMessagePlacemarkRelations(int startMessageId) {
        try {
            List<MarkPlacemarkRelationJSONBean> mpr = db.getMessagePlacemarkRelations(((Integer) startMessageId).toString());
            writer.write(JSONArray.fromObject(mpr).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getThreadParameters(int startMessageId) {
        try {
            List<MessageThreadParametersJSONBean> mtp = db.getThreadParameters(((Integer) startMessageId).toString());
            writer.write(JSONArray.fromObject(mtp).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void onDemandGeocode(String markPlacemarkRelations_json, String message_json, String placemarks_json, String threadParams_json) {
        MessageDataSet mds = new MessageDataSet();
        JSONArray jsonObject_markPlacemarkRelations = JSONArray.fromObject(markPlacemarkRelations_json);
        ArrayList<MarkPlacemarkRelation> markPlacemarkRelations = new ArrayList<MarkPlacemarkRelation>();
        for (Iterator iter = jsonObject_markPlacemarkRelations.iterator(); iter.hasNext(); ) {
            JSONObject jsonObject_markPlacemarkRelation = JSONObject.fromObject(iter.next());
            MarkPlacemarkRelation mpr = new MarkPlacemarkRelation();
            mpr.setMessageID(jsonObject_markPlacemarkRelation.getString("messageID"));
            mpr.setMarkID(jsonObject_markPlacemarkRelation.getString("markID"));
            mpr.setPlacemarkID(jsonObject_markPlacemarkRelation.getString("placemarkID"));
            mpr.setRefString(jsonObject_markPlacemarkRelation.getString("refString"));
            mpr.setIsReferenced(jsonObject_markPlacemarkRelation.getBoolean("isReferenced"));
            markPlacemarkRelations.add(mpr);
        }
        mds.setMarkPlacemarkRelations(markPlacemarkRelations);
        JSONObject jsonObject_message = JSONObject.fromObject(message_json);
        Message message = new Message();
        message.setId(jsonObject_message.getString("id"));
        message.setText(jsonObject_message.getString("text"));
        message.setChildOf(jsonObject_message.getString("childOf"));
        try {
            message.setTitle(jsonObject_message.getString("title"));
            message.setAuthor(jsonObject_message.getString("author"));
        } catch (JSONException e) {
        }
        mds.setMessage(message);
        JSONArray jsonObject_placemarks = JSONArray.fromObject(placemarks_json);
        ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
        for (Iterator iter = jsonObject_placemarks.iterator(); iter.hasNext(); ) {
            JSONObject jsonObject_placemark = JSONObject.fromObject(iter.next());
            Placemark placemark = new Placemark();
            placemark.setPlacemark_id(jsonObject_placemark.getString("placemark_id"));
            placemark.setGeonameid(jsonObject_placemark.getString("geonameid"));
            placemark.setAddress(jsonObject_placemark.getString("address"));
            placemark.setFeatureClass(jsonObject_placemark.getString("featureClass"));
            placemark.setFeatureCode(jsonObject_placemark.getString("featureCode"));
            placemark.setLat(jsonObject_placemark.getDouble("lat"));
            placemark.setLon(jsonObject_placemark.getDouble("lon"));
            placemarks.add(placemark);
        }
        mds.setPlacemarksTable(placemarks);
        JSONObject thrParams = JSONObject.fromObject(threadParams_json);
        MessageThreadParameters mtp = new MessageThreadParameters();
        mtp.setMinX(thrParams.getString("minX"));
        mtp.setMinY(thrParams.getString("minY"));
        mtp.setMaxX(thrParams.getString("maxX"));
        mtp.setMaxY(thrParams.getString("maxY"));
        mtp.setThread_id(thrParams.getString("threadId"));
        mtp.setMapCenterX(thrParams.getString("mapCenterX"));
        mtp.setMapCenterY(thrParams.getString("mapCenterY"));
        mtp.setMapZoomLevel(thrParams.getString("mapZoomLevel"));
        JSONArray featureClasses = (JSONArray) thrParams.get("featureClasses");
        List<String> featureClassesList = new ArrayList<String>();
        if (featureClasses != null) {
            for (Iterator iter = featureClasses.iterator(); iter.hasNext(); ) {
                featureClassesList.add((String) iter.next());
            }
            mtp.setFeatureClasses(featureClassesList);
        }
        mds.setMessageThreadParameters(mtp);
        try {
            MessageDataSetJSONBean result = gc.fastGeocode(mds, true);
            writer.write(JSONObject.fromObject(result).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("argoomap@googlemail.com", "tomcatargoomap");
        }
    }
}
