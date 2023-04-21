package com.hilaver.dzmis.basicinfo;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

public class BiReference {

    private Integer id;

    private String reference;

    private String front;

    private String back;

    private String sleeves;

    private String bottomThrum;

    private String sleevesThrum;

    private String necklineThrum;

    private Integer needle;

    private String ctMachineType;

    private Float weight;

    private String comment;

    private Blob frontPhoto;

    private String frontPhotoName;

    private Blob backPhoto;

    private String backPhotoName;

    private Blob otherPhoto;

    private String otherPhotoName;

    private Set productIdentifications = new HashSet(0);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getSleeves() {
        return sleeves;
    }

    public void setSleeves(String sleeves) {
        this.sleeves = sleeves;
    }

    public String getBottomThrum() {
        return bottomThrum;
    }

    public void setBottomThrum(String bottomThrum) {
        this.bottomThrum = bottomThrum;
    }

    public String getSleevesThrum() {
        return sleevesThrum;
    }

    public void setSleevesThrum(String sleevesThrum) {
        this.sleevesThrum = sleevesThrum;
    }

    public String getNecklineThrum() {
        return necklineThrum;
    }

    public void setNecklineThrum(String necklineThrum) {
        this.necklineThrum = necklineThrum;
    }

    public Integer getNeedle() {
        return needle;
    }

    public void setNeedle(Integer needle) {
        this.needle = needle;
    }

    public String getCtMachineType() {
        return ctMachineType;
    }

    public void setCtMachineType(String ctMachineType) {
        this.ctMachineType = ctMachineType;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Blob getFrontPhoto() {
        return frontPhoto;
    }

    public void setFrontPhoto(Blob frontPhoto) {
        this.frontPhoto = frontPhoto;
    }

    public String getFrontPhotoName() {
        return frontPhotoName;
    }

    public void setFrontPhotoName(String frontPhotoName) {
        this.frontPhotoName = frontPhotoName;
    }

    public Blob getBackPhoto() {
        return backPhoto;
    }

    public void setBackPhoto(Blob backPhoto) {
        this.backPhoto = backPhoto;
    }

    public String getBackPhotoName() {
        return backPhotoName;
    }

    public void setBackPhotoName(String backPhotoName) {
        this.backPhotoName = backPhotoName;
    }

    public Blob getOtherPhoto() {
        return otherPhoto;
    }

    public void setOtherPhoto(Blob otherPhoto) {
        this.otherPhoto = otherPhoto;
    }

    public String getOtherPhotoName() {
        return otherPhotoName;
    }

    public void setOtherPhotoName(String otherPhotoName) {
        this.otherPhotoName = otherPhotoName;
    }

    public Set getProductIdentifications() {
        return productIdentifications;
    }

    public void setProductIdentifications(Set productIdentifications) {
        this.productIdentifications = productIdentifications;
    }
}
