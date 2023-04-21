package com.kanjiportal.portal.model.service;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Nickho
 * Date: Jan 31, 2009
 * Time: 9:50:25 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "knowledge")
public class KnowledgeParam {

    private Calendar firstTimeSuccess;

    private Calendar lastTimeSuccess;

    private float lastTestSuccess;

    private String kanji;

    private String tag;

    @XmlElement
    public Calendar getFirstTimeSuccess() {
        return firstTimeSuccess;
    }

    public void setFirstTimeSuccess(Calendar firstTimeSuccess) {
        this.firstTimeSuccess = firstTimeSuccess;
    }

    @XmlElement
    public Calendar getLastTimeSuccess() {
        return lastTimeSuccess;
    }

    public void setLastTimeSuccess(Calendar lastTimeSuccess) {
        this.lastTimeSuccess = lastTimeSuccess;
    }

    @XmlElement
    public float getLastTestSuccess() {
        return lastTestSuccess;
    }

    public void setLastTestSuccess(float lastTestSuccess) {
        this.lastTestSuccess = lastTestSuccess;
    }

    @XmlID
    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    @XmlElement
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
