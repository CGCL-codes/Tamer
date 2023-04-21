package com.objectcode.GanttProjectAPI.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.objectcode.GanttProjectAPI.GanttDiagram;

/**
 * JUnitTest-Class to check all fields
 * 
 * Tests with Demo1.gan
 * 
 * Generates a Log (or console-output) from Xml and check if its all correctly
 *    
 * license: LGPL v3
 * 
 * @author FBI
 *
 */
public class GanttProjectAPIJUnitReadTest {

    String ganttDiagramFile = "/home/objectcode/workspace/GanttProjectAPI/data/Demo1.gan";

    GanttDiagram ganttDiagram = new GanttDiagram(ganttDiagramFile);

    String msg = ganttDiagram.loadGanttDiagram();

    protected static void log(String aLogMsg) {
        System.out.println(aLogMsg);
    }

    /**
	 * Check generaldata in Demo1.gan
	 *@author FBI
	 */
    @Test
    public void testGeneralData() {
        log("\nGanttDiagram loaded: " + ganttDiagram + " (Message=" + msg + ")\n\n");
        assertEquals("Name", "Demo1", ganttDiagram.getName());
        assertEquals("Company", "", ganttDiagram.getCompany());
        assertEquals("webLink", "http://", ganttDiagram.getWeblink());
        assertEquals("view-date", "2008-09-07", ganttDiagram.getViewdate());
        assertEquals("viewindex", "1", ganttDiagram.getViewIndex());
        assertEquals("version", "2.0", ganttDiagram.getVersionNo());
        assertEquals("description", "Test-Description", ganttDiagram.getDescription());
    }

    /**
	 * Check taskpropertydata
	 * @author FBI
	 */
    @Test
    public void testTaskPropertiesData() {
        assertEquals("taskproperty Id", "tpd0", ganttDiagram.getTaskPropertyId("type"));
        assertEquals("taskproperty name", "type", ganttDiagram.getTaskPropertyName("tpd0"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd0"));
        assertEquals("taskproperty valueType", "icon", ganttDiagram.getTaskPropertyValueType("tpd0"));
        assertEquals("taskproperty Id", "tpd1", ganttDiagram.getTaskPropertyId("priority"));
        assertEquals("taskproperty name", "priority", ganttDiagram.getTaskPropertyName("tpd1"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd1"));
        assertEquals("taskproperty valueType", "icon", ganttDiagram.getTaskPropertyValueType("tpd1"));
        assertEquals("taskproperty Id", "tpd2", ganttDiagram.getTaskPropertyId("info"));
        assertEquals("taskproperty name", "info", ganttDiagram.getTaskPropertyName("tpd2"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd2"));
        assertEquals("taskproperty valueType", "icon", ganttDiagram.getTaskPropertyValueType("tpd2"));
        assertEquals("taskproperty Id", "tpd3", ganttDiagram.getTaskPropertyId("name"));
        assertEquals("taskproperty name", "name", ganttDiagram.getTaskPropertyName("tpd3"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd3"));
        assertEquals("taskproperty valueType", "text", ganttDiagram.getTaskPropertyValueType("tpd3"));
        assertEquals("taskproperty Id", "tpd4", ganttDiagram.getTaskPropertyId("begindate"));
        assertEquals("taskproperty name", "begindate", ganttDiagram.getTaskPropertyName("tpd4"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd4"));
        assertEquals("taskproperty valueType", "date", ganttDiagram.getTaskPropertyValueType("tpd4"));
        assertEquals("taskproperty Id", "tpd5", ganttDiagram.getTaskPropertyId("enddate"));
        assertEquals("taskproperty name", "enddate", ganttDiagram.getTaskPropertyName("tpd5"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd5"));
        assertEquals("taskproperty valueType", "date", ganttDiagram.getTaskPropertyValueType("tpd5"));
        assertEquals("taskproperty Id", "tpd6", ganttDiagram.getTaskPropertyId("duration"));
        assertEquals("taskproperty name", "duration", ganttDiagram.getTaskPropertyName("tpd6"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd6"));
        assertEquals("taskproperty valueType", "int", ganttDiagram.getTaskPropertyValueType("tpd6"));
        assertEquals("taskproperty Id", "tpd7", ganttDiagram.getTaskPropertyId("completion"));
        assertEquals("taskproperty name", "completion", ganttDiagram.getTaskPropertyName("tpd7"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd7"));
        assertEquals("taskproperty valueType", "int", ganttDiagram.getTaskPropertyValueType("tpd7"));
        assertEquals("taskproperty Id", "tpd8", ganttDiagram.getTaskPropertyId("coordinator"));
        assertEquals("taskproperty name", "coordinator", ganttDiagram.getTaskPropertyName("tpd8"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd8"));
        assertEquals("taskproperty valueType", "text", ganttDiagram.getTaskPropertyValueType("tpd8"));
        assertEquals("taskproperty Id", "tpd9", ganttDiagram.getTaskPropertyId("predecessorsr"));
        assertEquals("taskproperty name", "predecessorsr", ganttDiagram.getTaskPropertyName("tpd9"));
        assertEquals("taskproperty type", "default", ganttDiagram.getTaskPropertyType("tpd9"));
        assertEquals("taskproperty valueType", "text", ganttDiagram.getTaskPropertyValueType("tpd9"));
    }

    /**
	 * Check taskdata
	 * @author FBI
	 */
    @Test
    public void testTaskData() {
        assertEquals("task id", "0", ganttDiagram.getTaskId("Phase Idea (1000630)"));
        assertEquals("task name", "Phase Idea (1000630)", ganttDiagram.getTaskName("0"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("0"));
        assertEquals("task start", "2008-09-09", ganttDiagram.getTaskStartDate("0"));
        assertEquals("task duration", 11, ganttDiagram.getTaskDuration("0"));
        assertEquals("task complete", 50, ganttDiagram.getTaskCompleteLevel("0"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("0"));
        assertEquals("task id", "1", ganttDiagram.getTaskId("Innovation Radar"));
        assertEquals("task name", "Innovation Radar", ganttDiagram.getTaskName("1"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("1"));
        assertEquals("task start", "2008-09-09", ganttDiagram.getTaskStartDate("1"));
        assertEquals("task duration", 3, ganttDiagram.getTaskDuration("1"));
        assertEquals("task complete", 100, ganttDiagram.getTaskCompleteLevel("1"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("1"));
        assertEquals("task id", "2", ganttDiagram.getTaskId("IP Protection"));
        assertEquals("task name", "IP Protection", ganttDiagram.getTaskName("2"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("2"));
        assertEquals("task start", "2008-09-12", ganttDiagram.getTaskStartDate("2"));
        assertEquals("task duration", 7, ganttDiagram.getTaskDuration("2"));
        assertEquals("task complete", 50, ganttDiagram.getTaskCompleteLevel("2"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("2"));
        assertEquals("task id", "3", ganttDiagram.getTaskId("Phase Spec Sheet"));
        assertEquals("task name", "Phase Spec Sheet", ganttDiagram.getTaskName("3"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("3"));
        assertEquals("task start", "2008-10-01", ganttDiagram.getTaskStartDate("3"));
        assertEquals("task duration", 11, ganttDiagram.getTaskDuration("3"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("3"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("3"));
        assertEquals("task id", "4", ganttDiagram.getTaskId("Version Tree"));
        assertEquals("task name", "Version Tree", ganttDiagram.getTaskName("4"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("4"));
        assertEquals("task start", "2008-10-01", ganttDiagram.getTaskStartDate("4"));
        assertEquals("task duration", 5, ganttDiagram.getTaskDuration("4"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("4"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("4"));
        assertEquals("task id", "5", ganttDiagram.getTaskId("Business View"));
        assertEquals("task name", "Business View", ganttDiagram.getTaskName("5"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("5"));
        assertEquals("task start", "2008-10-08", ganttDiagram.getTaskStartDate("5"));
        assertEquals("task duration", 5, ganttDiagram.getTaskDuration("5"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("5"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("5"));
        assertEquals("task id", "6", ganttDiagram.getTaskId("Spec Sheet"));
        assertEquals("task name", "Spec Sheet", ganttDiagram.getTaskName("6"));
        assertEquals("task meeting", "true", ganttDiagram.getTaskMeeting("6"));
        assertEquals("task start", "2008-10-15", ganttDiagram.getTaskStartDate("6"));
        assertEquals("task duration", 1, ganttDiagram.getTaskDuration("6"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("6"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("6"));
        assertEquals("task id", "7", ganttDiagram.getTaskId("Phase Lab Sample"));
        assertEquals("task name", "Phase Lab Sample", ganttDiagram.getTaskName("7"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("7"));
        assertEquals("task start", "2008-10-23", ganttDiagram.getTaskStartDate("7"));
        assertEquals("task duration", 3, ganttDiagram.getTaskDuration("7"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("7"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("7"));
        assertEquals("task id", "8", ganttDiagram.getTaskId("Tests"));
        assertEquals("task name", "Tests", ganttDiagram.getTaskName("8"));
        assertEquals("task meeting", "false", ganttDiagram.getTaskMeeting("8"));
        assertEquals("task start", "2008-10-23", ganttDiagram.getTaskStartDate("8"));
        assertEquals("task duration", 3, ganttDiagram.getTaskDuration("8"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("8"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("8"));
        assertEquals("task id", "9", ganttDiagram.getTaskId("Lab Sample"));
        assertEquals("task name", "Lab Sample", ganttDiagram.getTaskName("9"));
        assertEquals("task meeting", "true", ganttDiagram.getTaskMeeting("9"));
        assertEquals("task start", "2008-10-28", ganttDiagram.getTaskStartDate("9"));
        assertEquals("task duration", 1, ganttDiagram.getTaskDuration("9"));
        assertEquals("task complete", 0, ganttDiagram.getTaskCompleteLevel("9"));
        assertEquals("task priority", "1", ganttDiagram.getTaskPriority("9"));
    }

    /**
	 * Check Resourcedata
	 * @author FBI
	 */
    @Test
    public void testResourceData() {
        assertEquals("resource id", "0", ganttDiagram.getResourceId("JPA"));
        assertEquals("resource name", "JPA", ganttDiagram.getResourceName("0"));
        assertEquals("resource function", "Default:1", ganttDiagram.getResourceFunctionCode("0"));
        assertEquals("resource contacts", "test", ganttDiagram.getResourceContacts("0"));
        assertEquals("resource phone", "", ganttDiagram.getResourcePhone("0"));
        assertEquals("resource id", "1", ganttDiagram.getResourceId("IMO"));
        assertEquals("resource name", "IMO", ganttDiagram.getResourceName("1"));
        assertEquals("resource function", "Default:0", ganttDiagram.getResourceFunctionCode("1"));
        assertEquals("resource contacts", "", ganttDiagram.getResourceContacts("1"));
        assertEquals("resource phone", "", ganttDiagram.getResourcePhone("1"));
        assertEquals("resource name", "CLI", ganttDiagram.getResourceName("2"));
        assertEquals("resource function", "1", ganttDiagram.getResourceFunctionCode("2"));
        assertEquals("resource contacts", "", ganttDiagram.getResourceContacts("2"));
        assertEquals("resource phone", "", ganttDiagram.getResourcePhone("2"));
        assertEquals("resource name", "CLI", ganttDiagram.getResourceName("3"));
        assertEquals("resource function", "0", ganttDiagram.getResourceFunctionCode("3"));
        assertEquals("resource contacts", "", ganttDiagram.getResourceContacts("3"));
        assertEquals("resource phone", "", ganttDiagram.getResourcePhone("3"));
    }

    /**
	 * Check Allocationdata
	 * @author FBI
	 */
    @Test
    public void testAllocationData() {
        assertEquals("allocation task-id", "[5]", ganttDiagram.getResourceAllocationTaskIds("3").toString());
        assertEquals("allocation task-id", "[8]", ganttDiagram.getResourceAllocationTaskIds("2").toString());
    }

    /**
	 * Check Roledata
	 * @author FBI
	 */
    @Test
    public void testRoleData() {
        assertEquals("role name", "Coach", ganttDiagram.getRoleNameForFunction("0"));
        assertEquals("role name", "Driver", ganttDiagram.getRoleNameForFunction("1"));
    }

    /**
	 * Check Resourcedata
	 * @author FBI
	 */
    @Test
    public void testNotesData() {
        assertEquals("Notes", "Moin auch 12.02.09 - 00:00:00 SOURCE!", ganttDiagram.getTaskNote("9"));
    }
}
