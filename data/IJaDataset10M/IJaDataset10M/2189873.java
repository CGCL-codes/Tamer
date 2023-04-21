package com.homeautomate.ihm.dto;

import java.util.List;
import com.homeautomate.architecture.IFloor;
import com.homeautomate.architecture.IHouse;
import com.homeautomate.architecture.IRoom;
import com.homeautomate.constant.ManagerEnum;
import com.homeautomate.mediator.IMediator;
import com.homeautomate.unit.IUnit;

public class ManagerDto {

    private Integer identity;

    private String beanName;

    private String name;

    private ManagerEnum startingMode;

    private ManagerEnum etat;

    private String commentaire;

    private Long heartBeat;

    private List<IRoom> rooms;

    private List<IFloor> floors;

    private List<IHouse> house;

    private List<IMediator> mediators;

    private List<IUnit> units;

    private String properties;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagerEnum getStartingMode() {
        return startingMode;
    }

    public void setStartingMode(ManagerEnum startingMode) {
        this.startingMode = startingMode;
    }

    public ManagerEnum getEtat() {
        return etat;
    }

    public void setEtat(ManagerEnum etat) {
        this.etat = etat;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(Long heartBeat) {
        this.heartBeat = heartBeat;
    }

    public List<IRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<IRoom> rooms) {
        this.rooms = rooms;
    }

    public List<IFloor> getFloors() {
        return floors;
    }

    public void setFloors(List<IFloor> floors) {
        this.floors = floors;
    }

    public List<IHouse> getHouse() {
        return house;
    }

    public void setHouse(List<IHouse> house) {
        this.house = house;
    }

    public List<IMediator> getMediators() {
        return mediators;
    }

    public void setMediators(List<IMediator> mediators) {
        this.mediators = mediators;
    }

    public List<IUnit> getUnits() {
        return units;
    }

    public void setUnits(List<IUnit> units) {
        this.units = units;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }
}
