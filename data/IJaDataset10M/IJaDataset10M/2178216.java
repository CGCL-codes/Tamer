package pl.otros.logview.gui.actions;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXTable;
import pl.otros.logview.LogData;
import pl.otros.logview.filter.CallHierarchyLogFilter;
import pl.otros.logview.gui.LogDataTableModel;
import pl.otros.logview.gui.OtrosApplication;
import pl.otros.logview.uml.Message;
import pl.otros.logview.uml.Message.MessageType;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class ShowCallHierarchyAction extends FocusOnThisAbstractAction<CallHierarchyLogFilter> {

    public ShowCallHierarchyAction(CallHierarchyLogFilter filter, JCheckBox filterEnableCheckBox, OtrosApplication otrosApplication) {
        super(filter, filterEnableCheckBox, otrosApplication);
        this.putValue(NAME, "Show call hierarchy");
        this.putValue(SHORT_DESCRIPTION, "Filter events to call hierarchy. Logging of method entry/exit have to be present.");
    }

    @Override
    public void action(ActionEvent e, CallHierarchyLogFilter filter, LogData... selectedLogData) {
        JXTable jTable = getOtrosApplication().getSelectPaneJXTable();
        int selected = jTable.getSelectedRow();
        selected = jTable.convertRowIndexToModel(selected);
        ArrayList<Integer> listOfEvents2 = new ArrayList<Integer>();
        HashSet<Integer> listEntryEvents = new HashSet<Integer>();
        LogDataTableModel dataTableModel = getOtrosApplication().getSelectedPaneLogDataTableModel();
        findCallHierarchyEvents(selected, dataTableModel, listOfEvents2, listEntryEvents);
        filter.setListId(listEntryEvents, listOfEvents2);
        filterEnableCheckBox.setSelected(true);
        filter.setEnable(true);
    }

    protected void findCallHierarchyEvents(int selected, LogDataTableModel model, Collection<Integer> listEntryEvents, Collection<Integer> listOfEvents2) {
        LogData ld = model.getLogData(selected);
        String thread = ld.getThread();
        LinkedList<LogData> stack = new LinkedList<LogData>();
        HashMap<Integer, ArrayList<Integer>> allEventsInCallHierarchyMap = new HashMap<Integer, ArrayList<Integer>>();
        int rowCount = model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            LogData logData = model.getLogData(i);
            if (!logData.getThread().equals(thread)) {
                continue;
            }
            Message m = new Message(logData.getMessage());
            Integer stackSize = Integer.valueOf(stack.size());
            if (!allEventsInCallHierarchyMap.containsKey(stackSize)) {
                allEventsInCallHierarchyMap.put(stackSize, new ArrayList<Integer>());
            }
            ArrayList<Integer> tempListofEvents = allEventsInCallHierarchyMap.get(stackSize);
            if (m.getType().equals(MessageType.TYPE_ENTRY)) {
                stack.addLast(logData);
            } else if (m.getType().equals(MessageType.TYPE_EXIT) && theSameLogMethod(stack.getLast(), logData)) {
                stack.removeLast();
                tempListofEvents.clear();
            } else {
                tempListofEvents.add(Integer.valueOf(logData.getId()));
            }
            if (logData.getId() == ld.getId()) {
                break;
            }
        }
        for (ArrayList<Integer> list : allEventsInCallHierarchyMap.values()) {
            listOfEvents2.addAll(list);
        }
        for (int i = 0; i < stack.size(); i++) {
            listEntryEvents.add(Integer.valueOf(stack.get(i).getId()));
        }
    }

    protected boolean theSameLogMethod(LogData ld1, LogData ld2) {
        return StringUtils.equals(ld1.getClazz(), ld2.getClazz()) && StringUtils.equals(ld1.getMethod(), ld2.getMethod());
    }
}
