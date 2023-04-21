package net.sf.evemsp.data.io;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import net.sf.evemsp.SkillPlanner;
import net.sf.evemsp.data.AccRecord;
import net.sf.evemsp.data.ChrRecord;
import net.sf.evemsp.data.ChrSkill;
import net.sf.evemsp.data.EveRecordManager;
import net.sf.evemsp.data.Skill;
import net.sf.evemsp.data.io.api.EveApiCall;
import net.sf.evemsp.data.io.api.GetCharSkills;
import net.sf.evemsp.data.io.api.GetCharTraining;
import net.sf.evemsp.util.Device;
import com.alsutton.xmlparser.objectmodel.Node;

/**
 * Support for processing Character information
 * 
 * @author Jaabaa
 */
public class ChrSupport extends EveApiSupport implements SupportListener {

    private ChrRecord chrRecord;

    private AccRecord accRecord;

    private String type;

    protected void processResult(EveApiCall eveApiCall, Object object) {
        try {
            if (!(object instanceof Node)) {
                getSupportListener().setResult(object);
                return;
            }
            Node root = (Node) object;
            if (!"eveapi".equals(root.getName())) {
                getSupportListener().setResult(new EveApiSupportException("Wrong XML type: " + root.getName()));
                return;
            }
            if (!API_VERSION.equals(root.getAttribute("version"))) {
                getSupportListener().setResult(new EveApiSupportException("Wrong version: " + root.getAttribute("version")));
                return;
            }
            if (root.children != null) {
                for (Enumeration e = root.children.elements(); e.hasMoreElements(); ) {
                    Node node = (Node) e.nextElement();
                    if ("error".equals(node.getName())) {
                        try {
                            throw new EveApiSupportException(node.getAttribute("code") + ": " + node.getText());
                        } catch (EveApiSupportException x) {
                            getSupportListener().setResult(x);
                        }
                        return;
                    }
                }
            }
            if (eveApiCall instanceof GetCharSkills) {
                processSkills(root);
                getCharTraining();
            } else if (eveApiCall instanceof GetCharTraining) {
                processTraining(root);
                getSupportListener().setResult(chrRecord);
            } else {
                dumpNode("", root);
            }
        } catch (Exception x) {
            x.printStackTrace();
            showAlert("Error", x.toString());
            getSupportListener().setResult(null);
        }
    }

    /**
	 * Process the CharacterRecord training information
	 * 
	 * @param chrRecord The ChrRecord
	 * @param root Document root
	 */
    private void processTraining(Node root) {
        Date trainingEndTime = null;
        Date trainingStartTime = null;
        int trainingTypeID = 0;
        int trainingStartSP = 0;
        int trainingDestinationSP = 0;
        int trainingToLevel = 0;
        int skillInTraining = 0;
        Vector results = null;
        for (Enumeration e = root.children.elements(); e.hasMoreElements(); ) {
            Node node = (Node) e.nextElement();
            if ("result".equals(node.getName())) {
                results = node.children;
            }
            if ("cachedUntil".equals(node.getName())) {
                chrRecord.setCachedUntil(parseDate(node.getText()));
            }
        }
        if (results != null) {
            for (Enumeration e = results.elements(); e.hasMoreElements(); ) {
                Node node = (Node) e.nextElement();
                if ("skillInTraining".equals(node.getName())) {
                    skillInTraining = Integer.parseInt(node.getText());
                    if (skillInTraining == 0) {
                        break;
                    }
                } else if ("trainingEndTime".equals(node.getName())) {
                    trainingEndTime = parseTrainingDate(node.getText());
                } else if ("trainingStartTime".equals(node.getName())) {
                    trainingStartTime = parseTrainingDate(node.getText());
                } else if ("trainingTypeID".equals(node.getName())) {
                    trainingTypeID = Integer.parseInt(node.getText());
                } else if ("trainingStartSP".equals(node.getName())) {
                    trainingStartSP = Integer.parseInt(node.getText());
                } else if ("trainingDestinationSP".equals(node.getName())) {
                    trainingDestinationSP = Integer.parseInt(node.getText());
                } else if ("trainingToLevel".equals(node.getName())) {
                    trainingToLevel = Integer.parseInt(node.getText());
                }
            }
        }
        if (skillInTraining > 0) {
            chrRecord.setTrainingSkillId(trainingTypeID);
            chrRecord.setTrainingCompletes(trainingEndTime);
            chrRecord.setTrainingStarted(trainingStartTime);
            chrRecord.setTrainingStartSP(trainingStartSP);
            chrRecord.setTrainingEndSP(trainingDestinationSP);
            chrRecord.setTrainingToLevel(trainingToLevel);
            for (Enumeration e = chrRecord.getSkills().elements(); e.hasMoreElements(); ) {
                ChrSkill skl = (ChrSkill) e.nextElement();
                if (skl.getId() == trainingTypeID) {
                    skl.setSp(trainingStartSP);
                    break;
                }
            }
        } else {
            if (chrRecord.getTrainingSkillId() != 0) {
                chrRecord.setTrainingCompletes(null);
                chrRecord.setTrainingStarted(null);
                chrRecord.setTrainingStartSP(0);
                chrRecord.setTrainingEndSP(0);
                chrRecord.setTrainingToLevel((byte) 0);
                chrRecord.setTrainingSkillId(0);
            }
        }
    }

    /**
	 * Parse the Date string returned by the API
	 * 
	 * @param date The date format in XML
	 * @return The java Date object
	 */
    private Date parseTrainingDate(String date) {
        Date result = null;
        if (date != null && date.length() == 19) {
            int d = 0, m = 0, y = 0;
            int hh = 0, mm = 0, ss = 0;
            y = Integer.parseInt(date.substring(0, 4));
            m = Integer.parseInt(date.substring(5, 7));
            d = Integer.parseInt(date.substring(8, 10));
            hh = Integer.parseInt(date.substring(11, 13));
            mm = Integer.parseInt(date.substring(14, 16));
            ss = Integer.parseInt(date.substring(17, 19));
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            c.set(Calendar.YEAR, y);
            c.set(Calendar.MONTH, m - 1);
            c.set(Calendar.DAY_OF_MONTH, d);
            c.set(Calendar.HOUR_OF_DAY, hh);
            c.set(Calendar.MINUTE, mm);
            c.set(Calendar.SECOND, ss);
            c.set(Calendar.MILLISECOND, 0);
            result = c.getTime();
        }
        return result;
    }

    /**
	 * Converts an XML tree to a ChrRecord
	 * 
	 * @param root The document root
	 * @return The genrated ChrRecord
	 */
    private void processSkills(Node root) {
        for (Enumeration e = root.children.elements(); e.hasMoreElements(); ) {
            Node node = (Node) e.nextElement();
            if ("result".equals(node.getName())) {
                loadCharacter(node.children);
            }
        }
    }

    /**
	 * Converts an XML tree to a ChrRecord
	 * 
	 * @param chrRecord The record to set up with the XML data
	 * @param details The character information
	 */
    private void loadCharacter(Vector details) {
        for (int i = 0; i < details.size(); i++) {
            Node infoNode = (Node) details.elementAt(i);
            String infoType = infoNode.getName();
            if ("characterID".equals(infoType)) {
                chrRecord.setId(Integer.parseInt(infoNode.getText()));
            } else if ("name".equals(infoType)) {
                chrRecord.setName(infoNode.getText());
            } else if ("race".equals(infoType)) {
                chrRecord.setRace(infoNode.getText());
            } else if ("bloodLine".equals(infoType)) {
                chrRecord.setBloodLine(infoNode.getText());
            } else if ("gender".equals(infoType)) {
                chrRecord.setGender(infoNode.getText());
            } else if ("corporationName".equals(infoType)) {
                chrRecord.setCorpName(infoNode.getText());
            } else if ("corporationID".equals(infoType)) {
                chrRecord.setCorpId(Integer.parseInt(infoNode.getText()));
            } else if ("cloneName".equals(infoType)) {
                chrRecord.setCloneType(infoNode.getText());
            } else if ("cloneSkillPoints".equals(infoType)) {
                chrRecord.setCloneSP(Integer.parseInt(infoNode.getText()));
            } else if ("balance".equals(infoType)) {
                chrRecord.setBalance(Double.parseDouble(infoNode.getText()));
            } else if ("attributes".equals(infoType)) {
                loadAttributes(infoNode);
            } else if ("attributeEnhancers".equals(infoType)) {
                loadImplants(infoNode);
            } else if ("rowset".equals(infoType)) {
                String rowsetName = infoNode.getAttribute("name");
                if ("skills".equals(rowsetName)) {
                    loadSkills(infoNode.children);
                } else if ("corporationRoles".equals(rowsetName)) {
                } else if ("corporationRolesAtHQ".equals(rowsetName)) {
                } else if ("corporationRolesAtBase".equals(rowsetName)) {
                } else if ("corporationRolesAtOther".equals(rowsetName)) {
                } else if ("corporationTitles".equals(rowsetName)) {
                } else if ("certificates".equals(rowsetName)) {
                } else {
                    showAlert("Unhandled rowset: " + rowsetName, infoType);
                }
            } else {
                showAlert("Unhandled node", infoType);
            }
        }
    }

    /**
	 * Sets the attribute information
	 * 
	 * @param chrRecord The record to update
	 * @param attrsNode Attribute information
	 */
    private void loadAttributes(Node attrsNode) {
        Vector attrs = attrsNode.getChildren();
        for (int i = 0; i < attrs.size(); i++) {
            Node attrNode = (Node) attrs.elementAt(i);
            String attrName = attrNode.getName();
            int attrValue = Integer.parseInt(attrNode.getText());
            if ("intelligence".equals(attrName)) {
                chrRecord.setBaseInt(attrValue);
            } else if ("charisma".equals(attrName)) {
                chrRecord.setBaseCha(attrValue);
            } else if ("perception".equals(attrName)) {
                chrRecord.setBasePer(attrValue);
            } else if ("memory".equals(attrName)) {
                chrRecord.setBaseMem(attrValue);
            } else if ("willpower".equals(attrName)) {
                chrRecord.setBaseWil(attrValue);
            }
        }
    }

    /**
	 * Sets the implant information
	 * 
	 * @param chrRecord The record to update
	 * @param implantsNode Implant information
	 */
    private void loadImplants(Node implantsNode) {
        Vector implants = implantsNode.getChildren();
        if (implants == null) {
            chrRecord.setImplInt(0);
            chrRecord.setImplMem(0);
            chrRecord.setImplPer(0);
            chrRecord.setImplWil(0);
            chrRecord.setImplCha(0);
            return;
        }
        for (int i = 0; i < implants.size(); i++) {
            Node impNode = (Node) implants.elementAt(i);
            String impName = impNode.getName();
            Vector impInfos = impNode.getChildren();
            int impValue = 0;
            for (int j = 0; j < impInfos.size(); j++) {
                Node infoNode = (Node) impInfos.elementAt(j);
                String infoName = infoNode.getName();
                if ("augmentatorValue".equals(infoName)) {
                    impValue = Integer.parseInt(infoNode.getText());
                }
            }
            if ("intelligenceBonus".equals(impName)) {
                chrRecord.setImplInt(impValue);
            } else if ("charismaBonus".equals(impName)) {
                chrRecord.setImplCha(impValue);
            } else if ("perceptionBonus".equals(impName)) {
                chrRecord.setImplPer(impValue);
            } else if ("memoryBonus".equals(impName)) {
                chrRecord.setImplMem(impValue);
            } else if ("willpowerBonus".equals(impName)) {
                chrRecord.setImplWil(impValue);
            }
        }
    }

    /**
	 * Set the skill information
	 * 
	 * @param record The record to update
	 * @param skills The skills
	 */
    private void loadSkills(Vector skills) {
        for (Enumeration e = skills.elements(); e.hasMoreElements(); ) {
            Node node = (Node) e.nextElement();
            int typeID = Integer.parseInt(node.getAttribute("typeID"));
            int skillpoints = Integer.parseInt(node.getAttribute("skillpoints"));
            Skill def = Skill.getSkill(typeID);
            if (def != null) {
                ChrSkill skill = new ChrSkill(def);
                skill.setId(typeID);
                skill.setSp(skillpoints);
                chrRecord.addSkill(skill);
            } else {
                showAlert("Skill NOT found", "Skill ID \"" + typeID + "\" not found");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title, message, null, AlertType.WARNING);
        alert.setTimeout(Alert.FOREVER);
        Display disp = Device.getDisplay();
        Displayable d = disp.getCurrent();
        disp.setCurrent(alert, d);
    }

    public synchronized void updateCharacter(ChrRecord chrRecord, AccRecord accRecord) {
        this.chrRecord = chrRecord;
        this.accRecord = accRecord;
        setSupportListener(this);
        getCharSkills();
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Load the character skills
	 * 
	 * @param userID EVE userID
	 * @param apiKey EVE apiKey
	 * @param characterID EVE characterID
	 */
    private void getCharSkills() {
        type = "Get skills ...";
        SkillPlanner.getCanvas().getOverlay().setStatus(chrRecord.getName() + " - " + type);
        GetCharSkills request = new GetCharSkills();
        String key = accRecord.getKeyLtd();
        if (key == null) {
            key = accRecord.getKeyAll();
        }
        request.setUserID(accRecord.getId());
        request.setApiKey(key);
        request.setCharacterID(chrRecord.getId());
        processRequest(request);
    }

    /**
	 * Load the character training infomration
	 * 
	 * @param userID EVE userID
	 * @param apiKey EVE apiKey
	 * @param characterID EVE characterID
	 */
    private void getCharTraining() {
        GetCharTraining request = new GetCharTraining();
        type = "Get training ...";
        SkillPlanner.getCanvas().getOverlay().setStatus(chrRecord.getName() + " - " + type);
        String key = accRecord.getKeyLtd();
        if (key == null) {
            key = accRecord.getKeyAll();
        }
        request.setUserID(accRecord.getId());
        request.setApiKey(key);
        request.setCharacterID(chrRecord.getId());
        processRequest(request);
    }

    public void setEveStatus(String message) {
        SkillPlanner.getCanvas().getOverlay().setStatus(chrRecord.getName() + " - " + type + " " + message);
    }

    public void setEveStatusProgress(String message, int value, int length) {
    }

    public void setEveStatusTransferComplete() {
    }

    public void setEveStatusTransferStarted(int length) {
    }

    public void setResult(Object result) {
        SkillPlanner.getCanvas().getOverlay().setStatus(null);
        if (result instanceof ChrRecord) {
            EveRecordManager.getInstance().store((ChrRecord) result);
        } else {
            if (result instanceof EveApiSupportException) {
                EveApiSupportException x = (EveApiSupportException) result;
                Alert alert = new Alert("Error", x.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                Display disp = Device.getDisplay();
                Displayable d = disp.getCurrent();
                disp.setCurrent(alert, d);
            } else {
                Alert alert = new Alert("Unexpected result", result != null ? result.toString() : "null as result", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                Display disp = Device.getDisplay();
                Displayable d = disp.getCurrent();
                disp.setCurrent(alert, d);
            }
        }
        synchronized (this) {
            notifyAll();
        }
    }
}
