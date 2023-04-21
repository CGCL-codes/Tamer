package com.ballroomregistrar.compinabox.online.web.action.competitions.edit;

import java.util.List;
import java.util.Map;
import com.ballroomregistrar.compinabox.online.data.Address;
import com.ballroomregistrar.compinabox.online.data.Competition;
import com.ballroomregistrar.compinabox.online.data.CompetitionRepository;
import com.ballroomregistrar.compinabox.online.web.action.AccessAllowed;
import com.ballroomregistrar.compinabox.online.web.action.AccessRealm;
import com.ballroomregistrar.compinabox.online.web.support.Breadcrumb;
import com.ballroomregistrar.compinabox.util.StateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

@Action(results = { @Result(name = "success", location = "/competitions/home?competition=${competition.id}&edit=true", type = "redirect"), @Result(name = "denied", location = "/competitions/home?competition=${competition.id}", type = "redirect") })
@SuppressWarnings("serial")
@AccessAllowed(realm = AccessRealm.COMP, keyParam = "competition")
public class Venue extends ActionSupport {

    private Competition competition;

    private CompetitionRepository competitionRepository;

    private String roomName;

    private String buildingName;

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String zip;

    private String parking;

    private String directions;

    public void setCompetitionRepository(final CompetitionRepository repository) {
        this.competitionRepository = repository;
    }

    public List<Breadcrumb> getBreadcrumbs() {
        List<Breadcrumb> breadcrumbs = Lists.newArrayList();
        breadcrumbs.add(new Breadcrumb("Home", "index", "/"));
        breadcrumbs.add(new Breadcrumb("Competition", "upcoming", "/competitions"));
        Map<String, String> params = Maps.newHashMap();
        params.put("competition", getCompetition().getId().toString());
        breadcrumbs.add(new Breadcrumb(getCompetition().getName(), "home", "/competitions", params));
        breadcrumbs.add(new Breadcrumb("Venue &amp; Location Details", "", null));
        return breadcrumbs;
    }

    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "line1", message = "Please enter the first line of the competition's venue's address."), @RequiredStringValidator(fieldName = "city", message = "Please enter thecity of the competition's venue's address."), @RequiredStringValidator(fieldName = "state", message = "Please enter the state of the competition's venue's address."), @RequiredStringValidator(fieldName = "zip", message = "Please enter the zip code of the competition's venue's address.") })
    public String execute() {
        Address address = new Address();
        address.setRoomName(getRoomName());
        address.setBuildingName(getBuildingName());
        address.setLine1(getLine1());
        address.setLine2(getLine2());
        address.setCity(getCity());
        address.setState(getState());
        address.setZip(getZip());
        competition.setVenue(address);
        competition.setParkingText(getParking());
        competition.setDirectionsText(getDirections());
        competitionRepository.save(competition);
        return SUCCESS;
    }

    public List<String> getStates() {
        return StateUtil.getAllStates();
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine2() {
        return line2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(final String parking) {
        this.parking = parking;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getDirections() {
        return directions;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Competition getCompetition() {
        return competition;
    }
}
