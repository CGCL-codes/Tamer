package com.ajah.jsvg;

import com.ajah.jsvg.interfaces.AnimAdditionAttrs;
import com.ajah.jsvg.interfaces.AnimAttributeAttrs;
import com.ajah.jsvg.interfaces.AnimElementAttrs;
import com.ajah.jsvg.interfaces.AnimTimingAttrs;
import com.ajah.jsvg.interfaces.AnimValueAttrs;
import com.ajah.jsvg.interfaces.common.StdAttrs;
import com.ajah.jsvg.interfaces.common.TestAttrs;

/**
 * 
 */
public class Animate implements AnimAdditionAttrs, AnimAttributeAttrs, AnimElementAttrs, AnimTimingAttrs, AnimValueAttrs, StdAttrs, TestAttrs {

    private String accumulate = null;

    private String additive = null;

    private String attributeName = null;

    private String attributeType = null;

    private String xlinkHref = null;

    private String begin = null;

    private String dur = null;

    private String end = null;

    private String fill = null;

    private String max = null;

    private String min = null;

    private String repeatCount = null;

    private String repeatDur = null;

    private String restart = null;

    private String by = null;

    private String calcMode = null;

    private String from = null;

    private String keySplines = null;

    private String keyTimes = null;

    private String to = null;

    private String values = null;

    private String id = null;

    private String xmlBase = null;

    private String requiredFeatures = null;

    private String requiredExtensions = null;

    private String systemLanguage = null;

    private String xlinkActuate = null;

    private String xlinkArcrole = null;

    private String xlinkRole = null;

    private String xlinkShow = null;

    private String xlinkTitle = null;

    private String xlinkType = null;

    private String xmlnsXlink = null;

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAdditionAttrs#getAccumulate()
	 */
    public String getAccumulate() {
        return accumulate;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAdditionAttrs#getAdditive()
	 */
    public String getAdditive() {
        return additive;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAdditionAttrs#setAccumulate(java.lang.String)
	 */
    public void setAccumulate(String accumulate) {
        this.accumulate = accumulate;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAdditionAttrs#setAdditive(java.lang.String)
	 */
    public void setAdditive(String additive) {
        this.additive = additive;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAttributeAttrs#getAttributeName()
	 */
    public String getAttributeName() {
        return attributeName;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAttributeAttrs#getAttributeType()
	 */
    public String getAttributeType() {
        return attributeType;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAttributeAttrs#setAttributeName(java.lang.String)
	 */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimAttributeAttrs#setAttributeType(java.lang.String)
	 */
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimElementAttrs#getXlinkHref()
	 */
    public String getXlinkHref() {
        return xlinkHref;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimElementAttrs#setXlinkHref(java.lang.String)
	 */
    public void setXlinkHref(String xlinkHref) {
        this.xlinkHref = xlinkHref;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getBegin()
	 */
    public String getBegin() {
        return begin;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getDur()
	 */
    public String getDur() {
        return dur;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getEnd()
	 */
    public String getEnd() {
        return end;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getFill()
	 */
    public String getFill() {
        return fill;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getMax()
	 */
    public String getMax() {
        return max;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getMin()
	 */
    public String getMin() {
        return min;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getRepeatCount()
	 */
    public String getRepeatCount() {
        return repeatCount;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getRepeatDur()
	 */
    public String getRepeatDur() {
        return repeatDur;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#getRestart()
	 */
    public String getRestart() {
        return restart;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setBegin(java.lang.String)
	 */
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setDur(java.lang.String)
	 */
    public void setDur(String dur) {
        this.dur = dur;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setEnd(java.lang.String)
	 */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setFill(java.lang.String)
	 */
    public void setFill(String fill) {
        this.fill = fill;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setMax(java.lang.String)
	 */
    public void setMax(String max) {
        this.max = max;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setMin(java.lang.String)
	 */
    public void setMin(String min) {
        this.min = min;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setRepeatCount(java.lang.String)
	 */
    public void setRepeatCount(String repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setRepeatDur(java.lang.String)
	 */
    public void setRepeatDur(String repeatDur) {
        this.repeatDur = repeatDur;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimTimingAttrs#setRestart(java.lang.String)
	 */
    public void setRestart(String restart) {
        this.restart = restart;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getBy()
	 */
    public String getBy() {
        return by;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getCalcMode()
	 */
    public String getCalcMode() {
        return calcMode;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getFrom()
	 */
    public String getFrom() {
        return from;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getKeySplines()
	 */
    public String getKeySplines() {
        return keySplines;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getKeyTimes()
	 */
    public String getKeyTimes() {
        return keyTimes;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getTo()
	 */
    public String getTo() {
        return to;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#getValues()
	 */
    public String getValues() {
        return values;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setBy(java.lang.String)
	 */
    public void setBy(String by) {
        this.by = by;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setCalcMode(java.lang.String)
	 */
    public void setCalcMode(String calcMode) {
        this.calcMode = calcMode;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setFrom(java.lang.String)
	 */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setKeySplines(java.lang.String)
	 */
    public void setKeySplines(String keySplines) {
        this.keySplines = keySplines;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setKeyTimes(java.lang.String)
	 */
    public void setKeyTimes(String keyTimes) {
        this.keyTimes = keyTimes;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setTo(java.lang.String)
	 */
    public void setTo(String to) {
        this.to = to;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.AnimValueAttrs#setValues(java.lang.String)
	 */
    public void setValues(String values) {
        this.values = values;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.StdAttrs#setId(java.lang.String)
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.StdAttrs#getId()
	 */
    public String getId() {
        return id;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.StdAttrs#setXmlBase(java.lang.String)
	 */
    public void setXmlBase(String xmlBase) {
        this.xmlBase = xmlBase;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.StdAttrs#getXmlBase()
	 */
    public String getXmlBase() {
        return xmlBase;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.TestAttrs#setRequiredFeatures(java.lang.String)
	 */
    public void setRequiredFeatures(String requiredFeatures) {
        this.requiredFeatures = requiredFeatures;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.TestAttrs#getRequiredFeatures()
	 */
    public String getRequiredFeatures() {
        return requiredFeatures;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.TestAttrs#setRequiredExtensions(java.lang.String)
	 */
    public void setRequiredExtensions(String requiredExtensions) {
        this.requiredExtensions = requiredExtensions;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.TestAttrs#getRequiredExtensions()
	 */
    public String getRequiredExtensions() {
        return requiredExtensions;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.TestAttrs#setSystemLanguage(java.lang.String)
	 */
    public void setSystemLanguage(String systemLanguage) {
        this.systemLanguage = systemLanguage;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.TestAttrs#getsystemLanguage()
	 */
    public String getsystemLanguage() {
        return systemLanguage;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXlinkActuate()
	 */
    public String getXlinkActuate() {
        return xlinkActuate;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXlinkArcrole()
	 */
    public String getXlinkArcrole() {
        return xlinkArcrole;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXlinkRole()
	 */
    public String getXlinkRole() {
        return xlinkRole;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXlinkShow()
	 */
    public String getXlinkShow() {
        return xlinkShow;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXlinkTitle()
	 */
    public String getXlinkTitle() {
        return xlinkTitle;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXlinkType()
	 */
    public String getXlinkType() {
        return xlinkType;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#getXmlnsXlink()
	 */
    public String getXmlnsXlink() {
        return xmlnsXlink;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXlinkActuate(java.lang.String)
	 */
    public void setXlinkActuate(String xlinkActuate) {
        this.xlinkActuate = xlinkActuate;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXlinkArcrole(java.lang.String)
	 */
    public void setXlinkArcrole(String xlinkArcrole) {
        this.xlinkArcrole = xlinkArcrole;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXlinkRole(java.lang.String)
	 */
    public void setXlinkRole(String xlinkRole) {
        this.xlinkRole = xlinkRole;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXlinkShow(java.lang.String)
	 */
    public void setXlinkShow(String xlinkShow) {
        this.xlinkShow = xlinkShow;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXlinkTitle(java.lang.String)
	 */
    public void setXlinkTitle(String xlinkTitle) {
        this.xlinkTitle = xlinkTitle;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXlinkType(java.lang.String)
	 */
    public void setXlinkType(String xlinkType) {
        this.xlinkType = xlinkType;
    }

    /**
	 * @see com.ajah.jsvg.interfaces.XlinkRefAttrs#setXmlnsXlink(java.lang.String)
	 */
    public void setXmlnsXlink(String xmlnsXlink) {
        this.xmlnsXlink = xmlnsXlink;
    }
}
