package au.edu.qut.yawl.worklet.exception;

import au.edu.qut.yawl.util.JDOMConversionTools;
import au.edu.qut.yawl.worklist.model.WorkItemRecord;
import au.edu.qut.yawl.worklet.support.*;
import au.edu.qut.yawl.worklet.WorkletService;
import org.jdom.Element;
import org.apache.log4j.Logger;
import java.util.*;

/** The CaseMonitor class manages a dataset of descriptors for each case started in the
 *  engine while the ExceptionService is active. The primary purpose is to minimise calls
 *  across the interface.
 *
 *  HandlerRunner instances for each exception raised by this case are maintained
 *  by this class.
 *
 *  @author Michael Adams
 *  BPM Group, QUT Australia
 *  m3.adams@qut.edu.au
 *  @version 0.8, 04/07/2006
 */
public class CaseMonitor {

    private String _specID = null;

    private String _caseID = null;

    private Element _caseData = null;

    private Element _netLevelData = null;

    private Logger _log;

    private HashMap _itemRunners = null;

    private HandlerRunner _hrPreCase, _hrPostCase;

    private HandlerRunner _hrCaseExternal;

    private ArrayList _liveItems = new ArrayList();

    private boolean _liveCase = false;

    private boolean _preCaseCancelled = false;

    private String _hrPreCaseID = null;

    private String _hrPostCaseID = null;

    private String _hrCaseExID = null;

    private String _itemRunnerIDs = null;

    private String _liveItemIDs = null;

    private String _caseDataStr = null;

    private String _netDataStr = null;

    /**
     * The constructor is called when a pre-case constraint check event occurs,
     * marking the start of a new case
     * @param specID
     * @param caseID
     * @param data - a string representation of the case data
     */
    public CaseMonitor(String specID, String caseID, String data) {
        _log = Logger.getLogger("au.edu.qut.yawl.worklet.exception.CaseMonitor");
        _specID = specID;
        _caseID = caseID;
        _caseData = JDOMConversionTools.stringToElement(data);
        _netLevelData = _caseData;
        _itemRunners = new HashMap();
        _liveCase = true;
        _caseDataStr = data;
        _netDataStr = data;
    }

    public CaseMonitor() {
    }

    public String getCaseID() {
        return _caseID;
    }

    public String getSpecID() {
        return _specID;
    }

    public Element getCaseData() {
        return _caseData;
    }

    public Element getNetLevelData() {
        return _netLevelData;
    }

    public void setCaseData(Element data) {
        _caseData = data;
        persistThis();
    }

    public void setCaseCompleted() {
        _liveCase = false;
        persistThis();
    }

    private String get_hrPreCaseID() {
        return _hrPreCaseID;
    }

    private String get_hrPostCaseID() {
        return _hrPostCaseID;
    }

    private String get_hrCaseExID() {
        return _hrCaseExID;
    }

    private String get_itemRunnerIDs() {
        return _itemRunnerIDs;
    }

    private String get_liveItemIDs() {
        return _liveItemIDs;
    }

    private String get_specID() {
        return _specID;
    }

    private String get_caseID() {
        return _caseID;
    }

    private String get_caseDataStr() {
        return _caseDataStr;
    }

    private String get_netDataStr() {
        return _netDataStr;
    }

    private boolean get_liveCase() {
        return _liveCase;
    }

    private void set_hrPreCaseID(String id) {
        _hrPreCaseID = id;
    }

    private void set_hrPostCaseID(String id) {
        _hrPostCaseID = id;
    }

    private void set_hrCaseExID(String id) {
        _hrCaseExID = id;
    }

    private void set_itemRunnerIDs(String ids) {
        _itemRunnerIDs = ids;
    }

    private void set_liveItemIDs(String ids) {
        _liveItemIDs = ids;
    }

    private void set_specID(String s) {
        _specID = s;
    }

    private void set_caseID(String s) {
        _caseID = s;
    }

    private void set_caseDataStr(String s) {
        _caseDataStr = s;
    }

    private void set_netDataStr(String s) {
        _netDataStr = s;
    }

    private void set_liveCase(boolean b) {
        _liveCase = b;
    }

    /**
     * reconstitutes objects persisted as string representations
     */
    public void initNonPersistedItems() {
        _caseData = JDOMConversionTools.stringToElement(_caseDataStr);
        _netLevelData = JDOMConversionTools.stringToElement(_netDataStr);
        _liveItems = (ArrayList) RdrConversionTools.StringToStringList(_liveItemIDs);
        _log = Logger.getLogger("au.edu.qut.yawl.worklet.exception.CaseMonitor");
    }

    /**
     * 'reattaches' HandlerRunners belonging to this CaseMonitor after a restore
     * @param runnerMap - a set of all the HandlerRunners restored from persistence
     * @return the list of all runners 'claimed' by this CaseMonitor
     */
    public List restoreRunners(HashMap runnerMap) {
        ArrayList restored = new ArrayList();
        _itemRunners = new HashMap();
        _hrPreCase = restoreRunner(_hrPreCaseID, runnerMap);
        _hrPostCase = restoreRunner(_hrPostCaseID, runnerMap);
        _hrCaseExternal = restoreRunner(_hrCaseExID, runnerMap);
        if (_hrPreCase != null) restored.add(_hrPreCase);
        if (_hrPostCase != null) restored.add(_hrPostCase);
        if (_hrCaseExternal != null) restored.add(_hrCaseExternal);
        HandlerRunner runner;
        String id;
        List runnerIDs = RdrConversionTools.StringToStringList(_itemRunnerIDs);
        if (runnerIDs != null) {
            Iterator itr = runnerIDs.iterator();
            while (itr.hasNext()) {
                id = (String) itr.next();
                runner = restoreRunner(id, runnerMap);
                restored.add(runner);
                _itemRunners.put(runner.getItemId(), runner);
            }
        }
        if (restored.isEmpty()) restored = null;
        return restored;
    }

    /**
     * Seek and retrieve the HandlerRunner specified by the id passed
     * @param id - the persisted id of a HandlerRunner
     * @param runnerMap - the set of restored runners
     * @return the runner that 'owns' the id specified, or null if there is no
     *         runner with that id or the id is null
     */
    private HandlerRunner restoreRunner(String id, HashMap runnerMap) {
        HandlerRunner result = null;
        if (id != null) {
            result = (HandlerRunner) runnerMap.get(id);
            if (result != null) {
                result.setOwnerCaseMonitor(this);
                String taskID = null;
                if (result.getItem() != null) taskID = Library.getTaskNameFromId(result.getItem().getTaskID());
                result.rebuildSearchPair(_specID, taskID);
            }
        }
        return result;
    }

    /** updates the persisted object after changes (if persisting) */
    private void persistThis() {
        DBManager dbMgr = DBManager.getInstance(false);
        if ((dbMgr != null) && dbMgr.isPersisting()) dbMgr.persist(this, DBManager.DB_UPDATE);
    }

    public void updateData(String sData) {
        Element eData = JDOMConversionTools.stringToElement(sData);
        Element wiParam, caseParam, newParam;
        Iterator itr = (eData.getChildren()).iterator();
        while (itr.hasNext()) {
            wiParam = (Element) itr.next();
            caseParam = _caseData.getChild(wiParam.getName());
            if (caseParam != null) caseParam.setText(wiParam.getText()); else {
                newParam = new Element(wiParam.getName());
                newParam.addContent(wiParam.getText());
                _caseData.addContent(newParam);
            }
        }
        updateCaseDataStr();
    }

    /**
     *  Adds an external exception trigger to the case data so that the correct
     *  RDR can be found for it
     * @param triggerValue - the string value of the external trigger
     */
    public void addTrigger(String triggerValue) {
        if (!triggerValue.startsWith("\"")) triggerValue = "\"" + triggerValue + "\"";
        Element eTrigger = new Element("trigger");
        eTrigger.addContent(triggerValue);
        _caseData.addContent(eTrigger);
        updateCaseDataStr();
    }

    public void removeTrigger() {
        _caseData.removeChild("trigger");
        updateCaseDataStr();
    }

    /**
     * Adds the contents of the workitem record to the case data for this case - thus
     * providing information about the workitem to the ruleset
     * @param wir - the wir being tested for an exception
     */
    public void addProcessInfo(WorkItemRecord wir) {
        Element eWir = (Element) JDOMConversionTools.stringToElement(wir.toXML()).detach();
        Element eInfo = new Element("process_info");
        eInfo.addContent(eWir);
        _caseData.addContent(eInfo);
        updateCaseDataStr();
        persistThis();
    }

    public void removeProcessInfo() {
        _caseData.removeChild("process_info");
        updateCaseDataStr();
    }

    /** Stringifies the case data for persistence purposes */
    private void updateCaseDataStr() {
        _caseDataStr = JDOMConversionTools.elementToString(_caseData);
        persistThis();
    }

    public void addPreCaseHandlerRunner(HandlerRunner hr) {
        if (_hrPreCase == null) {
            _hrPreCase = hr;
            _hrPreCaseID = String.valueOf(hr.get_id());
            persistThis();
        } else _log.error("Cannot add a pre-case exception manager when one already exists");
    }

    public void addPostCaseHandlerRunner(HandlerRunner hr) {
        if (_hrPostCase == null) {
            _hrPostCase = hr;
            _hrPostCaseID = String.valueOf(hr.get_id());
            persistThis();
        } else _log.error("Cannot add a post-case exception manager when one already exists");
    }

    public void addCaseExternalHandlerRunner(HandlerRunner hr) {
        if (_hrCaseExternal == null) {
            _hrCaseExternal = hr;
            _hrCaseExID = String.valueOf(hr.get_id());
            persistThis();
        } else _log.error("Cannot add a case-level external exception manager when one already exists");
    }

    public void addHandlerRunner(HandlerRunner hr, String itemID) {
        if (itemID.equals("pre")) addPreCaseHandlerRunner(hr); else if (itemID.equals("post")) addPostCaseHandlerRunner(hr); else if (itemID.equals("external")) addCaseExternalHandlerRunner(hr); else {
            if (!_itemRunners.containsKey(itemID)) {
                _itemRunners.put(itemID, hr);
                updateRunnerIDs();
                persistThis();
            } else _log.error("Exception Manager for itemID already exists: " + itemID);
        }
    }

    /** Stringifies the list of item runner ids (required for persistence) */
    private void updateRunnerIDs() {
        ArrayList ids = new ArrayList();
        Iterator itr = new ArrayList(_itemRunners.values()).iterator();
        while (itr.hasNext()) {
            HandlerRunner runner = (HandlerRunner) itr.next();
            ids.add(String.valueOf(runner.get_id()));
        }
        _itemRunnerIDs = RdrConversionTools.StringListToString(ids);
    }

    public HandlerRunner getPreCaseHandlerRunner() {
        return _hrPreCase;
    }

    public HandlerRunner getPostCaseHandlerRunner() {
        return _hrPostCase;
    }

    public HandlerRunner getCaseExternalHandlerRunner() {
        return _hrCaseExternal;
    }

    /** retrieves an item-level runner */
    public HandlerRunner getHandlerRunnerForItem(String itemID) {
        return (HandlerRunner) _itemRunners.get(itemID);
    }

    /** returns the runner for the specified type (if any) */
    public HandlerRunner getRunnerForType(int xType, String itemID) {
        HandlerRunner result;
        switch(xType) {
            case WorkletService.XTYPE_CASE_PRE_CONSTRAINTS:
                result = getPreCaseHandlerRunner();
                break;
            case WorkletService.XTYPE_CASE_POST_CONSTRAINTS:
                result = getPostCaseHandlerRunner();
                break;
            case WorkletService.XTYPE_CASE_EXTERNAL_TRIGGER:
                result = getCaseExternalHandlerRunner();
                break;
            default:
                result = getHandlerRunnerForItem(itemID);
        }
        return result;
    }

    public ArrayList getHandlerRunners() {
        ArrayList list = new ArrayList();
        list.addAll(_itemRunners.values());
        if (_hrPreCase != null) list.add(_hrPreCase);
        if (_hrPostCase != null) list.add(_hrPreCase);
        if (_hrCaseExternal != null) list.add(_hrCaseExternal);
        return list;
    }

    public void removePreCaseHandlerRunner() {
        _hrPreCase = null;
        _hrPreCaseID = null;
        persistThis();
    }

    public void removePostCaseHandlerRunner() {
        _hrPostCase = null;
        _hrPostCaseID = null;
        persistThis();
    }

    public void removeCaseExternalHandlerRunner() {
        _hrCaseExternal = null;
        _hrCaseExID = null;
        persistThis();
    }

    public void removeHandlerRunnerForItem(String itemID) {
        if (itemID.equals("pre")) removePreCaseHandlerRunner(); else if (itemID.equals("post")) removePostCaseHandlerRunner(); else if (itemID.equals("external")) removeCaseExternalHandlerRunner(); else {
            if (_itemRunners.containsKey(itemID)) {
                _itemRunners.remove(itemID);
                _itemRunnerIDs = RdrConversionTools.MapKeySetToString(_itemRunners);
                persistThis();
            } else _log.error("Exception Manager for itemID does not exist: " + itemID);
        }
    }

    public void removeHandlerRunner(HandlerRunner runner) {
        if (runner == _hrPreCase) removePreCaseHandlerRunner(); else if (runner == _hrPostCase) removePostCaseHandlerRunner(); else if (runner == _hrCaseExternal) removeCaseExternalHandlerRunner(); else {
            if (_itemRunners.containsKey(runner.getItemId())) {
                _itemRunners.remove(runner.getItemId());
                _itemRunnerIDs = RdrConversionTools.MapKeySetToString(_itemRunners);
                persistThis();
            }
        }
    }

    public void removeAllRunners() {
        removePreCaseHandlerRunner();
        removePostCaseHandlerRunner();
        removeCaseExternalHandlerRunner();
        _itemRunnerIDs = null;
        _itemRunners.clear();
        persistThis();
    }

    public void addLiveItem(String taskID) {
        _liveItems.add(taskID);
        _liveItemIDs = RdrConversionTools.StringListToString(_liveItems);
        persistThis();
    }

    public void removeLiveItem(String taskID) {
        if (_liveItems.contains(taskID)) {
            _liveItems.remove(taskID);
            _liveItemIDs = RdrConversionTools.StringListToString(_liveItems);
            persistThis();
        }
    }

    public boolean hasHandlerRunnerForItem(String itemID) {
        return (_itemRunners.containsKey(itemID));
    }

    public boolean isItemRunner(HandlerRunner runner) {
        return _itemRunners.containsValue(runner);
    }

    public boolean isCaseRunner(HandlerRunner runner) {
        return (runner == _hrPreCase) || (runner == _hrPostCase) || (runner == _hrCaseExternal);
    }

    public boolean hasLiveHandlerRunners() {
        return !((_hrPreCase == null) && (_hrPostCase == null) && (_hrCaseExternal == null) && _itemRunners.isEmpty());
    }

    public boolean hasLiveItems() {
        return !_liveItems.isEmpty();
    }

    public boolean isDone() {
        return !(_liveCase || hasLiveItems() || hasLiveHandlerRunners());
    }

    public boolean isCaseCompleted() {
        return !_liveCase;
    }

    public void setPreCaseCancellationFlag() {
        _preCaseCancelled = true;
    }

    public boolean isPreCaseCancelled() {
        return _preCaseCancelled;
    }

    /********************************************************************************/
    public String toString() {
        StringBuffer s = new StringBuffer("##### CASEMONITOR RECORD #####");
        s.append(Library.newline);
        String specID = (_specID == null) ? "null" : _specID;
        String caseID = (_caseID == null) ? "null" : _caseID;
        String caseData = (_caseData == null) ? "null" : JDOMConversionTools.elementToString(_caseData);
        String netData = (_netLevelData == null) ? "null" : JDOMConversionTools.elementToString(_netLevelData);
        String preHR = (_hrPreCase == null) ? "null" : _hrPreCase.toString();
        String postHR = (_hrPostCase == null) ? "null" : _hrPostCase.toString();
        String extHR = (_hrCaseExternal == null) ? "null" : _hrCaseExternal.toString();
        String liveCase = String.valueOf(_liveCase);
        Library.appendLine(s, "SPECIFICATION ID", specID);
        Library.appendLine(s, "CASE ID", caseID);
        Library.appendLine(s, "CASE DATA", caseData);
        Library.appendLine(s, "NET LEVEL DATA", netData);
        Library.appendLine(s, "PRE-CASE RUNNER", preHR);
        Library.appendLine(s, "POST-CASE RUNNER", postHR);
        Library.appendLine(s, "EXTERNAL RUNNER", extHR);
        Library.appendLine(s, "ITEM RUNNERS", _itemRunnerIDs);
        Library.appendLine(s, "LIVE ITEMS", _liveItemIDs);
        Library.appendLine(s, "LIVE CASE?", liveCase);
        return s.toString();
    }
}
