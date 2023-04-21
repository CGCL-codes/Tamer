package com.objectcode.GanttProjectAPI.test;

import com.objectcode.GanttProjectAPI.GanttDiagram;

/**
 * read data from gantt-xml-file
 *    
 * license: LGPL v3
 * 
 * @author heyduk, FBI
 */
public class Test1 {

    protected static void log(String aLogMsg) {
        System.out.println(aLogMsg);
    }

    /**
   * Only for testing purposes...  (DateUtils-Calendar-usage NOT possible when using Standalone!)
   * 
   * @param args
   */
    public static void main(String[] args) {
        String ganttDiagramFile = "/home/objectcode/workspace/GanttProjectAPI/data/Demo1.gan";
        GanttDiagram ganttDiagram = new GanttDiagram(ganttDiagramFile);
        String msg = ganttDiagram.loadGanttDiagram();
        log("\nGanttDiagram loaded: " + ganttDiagram + " (Message=" + msg + ")\n\n");
        log("\n-----------------General-Data------------------\n");
        log("Project-Name: " + ganttDiagram.getName());
        log("Version: " + ganttDiagram.getVersionNo());
        log("View-Date: " + ganttDiagram.getViewdate());
        log("View-Index: " + ganttDiagram.getViewIndex());
        log("Description: " + ganttDiagram.getDescription());
        log("DateProjectStart (calculated): " + ganttDiagram.getDateProjectStart());
        log("DateProjectEnd (calculated): " + ganttDiagram.getDateProjectEnd());
        log("Web-Link: " + ganttDiagram.getWeblink());
        log("Company: " + ganttDiagram.getCompany());
        log("PlannedResources (calculated): " + ganttDiagram.getPlannedResources());
        log("\n------------------Task-Data---------------------\n");
        log("Task with Id=4: " + ganttDiagram.getTaskById("4").toString());
        log("Task wiht ID=5: " + ganttDiagram.getActivitiesAndMilestones().get("5"));
        log("Task \"Lab Sample\": " + ganttDiagram.getActivitiesAndMilestones().get(ganttDiagram.getTaskId("Lab Sample")));
        log("Task ID from \"Phase Idea (1000630)\"-Task: " + ganttDiagram.getTaskId("Phase Idea (1000630)"));
        log("Name from Task with Id=5: " + ganttDiagram.getTaskName("5"));
        log("Start Date of Task with ID=9: " + ganttDiagram.getTaskStartDate("9"));
        log("End Date of Task with ID=9: " + ganttDiagram.getTaskEndDate("9"));
        log("Duration of Task Business View: " + ganttDiagram.getTaskDuration("Business View"));
        log("Duration of Task with ID=9: " + ganttDiagram.getTaskDuration("9"));
        log("Is Task with ID=0 a Meeting?: " + ganttDiagram.getTaskMeeting("0"));
        log("Type of Task Business View: " + ganttDiagram.getTaskType("Business View"));
        log("Type of Task with ID=9: " + ganttDiagram.getTaskType("9"));
        log("Dependency from Task ID=5: " + ganttDiagram.getDependencies().get("5"));
        log("Dependency with id=5: " + ganttDiagram.getDependencyObjectById("5").toString());
        log("\n----------------Resource-Data--------------------\n");
        log("Resource JPA(ID=0): " + ganttDiagram.getResources().get(ganttDiagram.getResourceId("JPA")));
        log("Resource with ID=3: " + ganttDiagram.getResourceObjectById("3"));
        log("Resource ID of JPA: " + ganttDiagram.getResourceId("JPA"));
        log("Name of Resource with ID=3: " + ganttDiagram.getResourceName("3"));
        log("Funkion of Resource with Id=3: " + ganttDiagram.getResourceFunction("3"));
        log("TaskId from Resource with ID=0: " + ganttDiagram.getResourceAllocationTaskIds("0"));
        log("TaskId from Resource with ID=2: " + ganttDiagram.getResourceAllocationTaskIds("2"));
        log("Phonenumber from Resource with ID=2: " + ganttDiagram.getResourcePhone("2"));
        log("Contacts from Resource with ID=0: " + ganttDiagram.getResourceContacts("0"));
        log("\n------------------Role-Data----------------------\n");
        log("Role ID=0: " + ganttDiagram.getRoles().get("0"));
        log("Role ID=1: " + ganttDiagram.getRoles().get("1"));
        log("\n----------------Property-Data---------------------\n");
        log("Taskproperty with Id=tpd0: " + ganttDiagram.getTaskPropertyObjectById("tpd0"));
        log("Taskproperty Id: " + ganttDiagram.getTaskPropertyId("type"));
        log("Taskproperty tpd0 Name: " + ganttDiagram.getTaskPropertyName("tpd0"));
        log("Taskproperty Type: " + ganttDiagram.getTaskPropertyType("tpd0"));
        log("Taskproperty Valuetype: " + ganttDiagram.getTaskPropertyValueType("tpd0"));
    }
}
