package fr.gfi.gfinet.server.report.productionPlanning;

import java.sql.Date;
import java.util.ArrayList;
import fr.gfi.gfinet.server.report.ReportBean;

/**
 * 
 * @author Arnaud VAN CAMPENHOUDT
 *
 */
public class ProductionPlanningBean implements ReportBean {

    String generatedReportDateString = null;

    Date generatedReportDate = null;

    int nbOfMissions = 0;

    ArrayList<Manager> managers = null;

    public Date getGeneratedReportDate() {
        return generatedReportDate;
    }

    public void setGeneratedReportDate(Date generatedReportDate) {
        this.generatedReportDate = generatedReportDate;
    }

    public String getGeneratedReportDateString() {
        return generatedReportDateString;
    }

    public void setGeneratedReportDateString(String generatedReportDateString) {
        this.generatedReportDateString = generatedReportDateString;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public void setManagers(ArrayList<Manager> managers) {
        this.managers = managers;
    }

    public int getNbOfMissions() {
        return nbOfMissions;
    }

    public void setNbOfMissions(int nbOfMissions) {
        this.nbOfMissions = nbOfMissions;
    }
}

class MissionPeriod {

    Date startDate = null;

    Date endDate = null;

    String tjm = null;

    String infoNextMissionPeriod = null;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInfoNextMissionPeriod() {
        return infoNextMissionPeriod;
    }

    public void setInfoNextMissionPeriod(String infoNextMissionPeriod) {
        this.infoNextMissionPeriod = infoNextMissionPeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTjm() {
        return tjm;
    }

    public void setTjm(String tjm) {
        this.tjm = tjm;
    }
}

class Mission {

    ArrayList<MissionPeriod> missionPeriod = null;

    String collaboratorFullName = null;

    public String getCollaboratorFullName() {
        return collaboratorFullName;
    }

    public void setCollaboratorFullName(String collaboratorFullName) {
        this.collaboratorFullName = collaboratorFullName;
    }

    public ArrayList<MissionPeriod> getMissionPeriod() {
        return missionPeriod;
    }

    public void setMissionPeriod(ArrayList<MissionPeriod> missionPeriod) {
        this.missionPeriod = missionPeriod;
    }
}

class Project {

    ArrayList<Mission> missions = null;

    String projectName = null;

    public ArrayList<Mission> getMissions() {
        return missions;
    }

    public void setMissions(ArrayList<Mission> missions) {
        this.missions = missions;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

class Customer {

    ArrayList<Project> projects = null;

    String customerName = null;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}

class Manager {

    ArrayList<Customer> customers = null;

    String managerFullName = null;

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public String getManagerFullName() {
        return managerFullName;
    }

    public void setManagerFullName(String managerFullName) {
        this.managerFullName = managerFullName;
    }
}
