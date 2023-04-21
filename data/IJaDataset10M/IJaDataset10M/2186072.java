package com.beimin.eveapi.character.sheet;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.beimin.eveapi.core.ApiResponse;
import com.beimin.eveapi.shared.character.EveAncestry;
import com.beimin.eveapi.shared.character.EveBloodline;
import com.beimin.eveapi.shared.character.EveRace;

public class CharacterSheetResponse extends ApiResponse {

    private static final long serialVersionUID = 1L;

    private long characterID;

    private String name;

    private EveRace race;

    private Date dateOfBirth;

    private EveBloodline bloodLine;

    private EveAncestry ancestry;

    private String gender;

    private String corporationName;

    private long corporationID;

    private double balance;

    private final Set<ApiAttributeEnhancer> attributeEnhancers = new HashSet<ApiAttributeEnhancer>();

    private int intelligence;

    private int memory;

    private int charisma;

    private int perception;

    private int willpower;

    private final Set<ApiSkill> skills = new HashSet<ApiSkill>();

    public long getCharacterID() {
        return characterID;
    }

    public void setCharacterID(long characterID) {
        this.characterID = characterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EveRace getRace() {
        return race;
    }

    public void setRace(EveRace race) {
        this.race = race;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date doB) {
        dateOfBirth = doB;
    }

    public EveBloodline getBloodLine() {
        return bloodLine;
    }

    public void setBloodLine(EveBloodline bloodLine) {
        this.bloodLine = bloodLine;
    }

    public EveAncestry getAncestry() {
        return ancestry;
    }

    public void setAncestry(EveAncestry ancestry) {
        this.ancestry = ancestry;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public long getCorporationID() {
        return corporationID;
    }

    public void setCorporationID(long corporationID) {
        this.corporationID = corporationID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addAttributeEnhancer(ApiAttributeEnhancer attributeEnhancer) {
        attributeEnhancers.add(attributeEnhancer);
    }

    public Set<ApiAttributeEnhancer> getAttributeEnhancers() {
        return attributeEnhancers;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getPerception() {
        return perception;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public void addSkill(ApiSkill skill) {
        skills.add(skill);
    }

    public Set<ApiSkill> getSkills() {
        return skills;
    }
}
