package com.semp.jadoma.web.bean;

import java.util.Date;
import com.semp.jadoma.core.model.Dog;
import com.semp.jadoma.core.model.FamilyReference;
import com.semp.jadoma.web.utils.webapp.KeyFactory;

public class DogBean {

    private long id;

    private String name;

    private String nickName;

    private String colorKey;

    private String peelKey;

    private String sexKey;

    private double weight;

    private double size;

    private Date birthDay;

    private String fatherName;

    private long fatherId;

    private String motherName;

    private long motherId;

    private String familyName;

    private long familyId;

    public DogBean(Dog iDog) {
        this.birthDay = iDog.getBirthDay();
        this.peelKey = KeyFactory.peelToKey(iDog.getPeel());
        this.colorKey = KeyFactory.colorToKey(iDog.getColor());
        this.sexKey = KeyFactory.sexToKey(iDog.getSex());
        this.name = iDog.getName();
        this.nickName = iDog.getNickName();
        this.weight = iDog.getWeight();
        this.size = iDog.getSize();
        FamilyReference familyReference = iDog.getFamilyReference();
        if (familyReference != null) {
            fatherName = familyReference.getFather().getName();
            fatherId = familyReference.getFather().getId();
            motherName = familyReference.getMother().getName();
            fatherId = familyReference.getMother().getId();
            familyName = familyReference.getBirthDay().toString();
            familyId = familyReference.getId();
        }
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getColorKey() {
        return colorKey;
    }

    public void setColorKey(String colorKey) {
        this.colorKey = colorKey;
    }

    public long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPeelKey() {
        return peelKey;
    }

    public void setPeelKey(String peelKey) {
        this.peelKey = peelKey;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getFatherId() {
        return fatherId;
    }

    public void setFatherId(long fatherId) {
        this.fatherId = fatherId;
    }

    public long getMotherId() {
        return motherId;
    }

    public void setMotherId(long motherId) {
        this.motherId = motherId;
    }

    public String getSexKey() {
        return sexKey;
    }

    public void setSexKey(String sexKey) {
        this.sexKey = sexKey;
    }
}
