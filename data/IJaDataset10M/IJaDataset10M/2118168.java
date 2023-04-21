package com.astrium.faceo.client.bean.programming.sps2.request;

import java.io.Serializable;

/** * <B>SITE FACEO</B> <BR> *  * <P> * Temporal series : *  * This class can be used to specify a temporal series of acquisitions.  * A temporal series contains a SurveyPeriod element and parameters that define the number of * occurrences, the periodicity and the latest start date of the series. * </P> *  * @author  GR * @version 1.0, le 20/04/2010 */
public class TemporalSeriesBean implements Serializable {

    /**	 * 	 */
    private static final long serialVersionUID = -96297644701715632L;

    /** survey Period 	 * Overall period during which the whole series should be     * acquired, while the latest start, if supported, specifies the     * date before which the first acquisition of the series must     * be completed.     */
    private SurveyPeriodBean surveyPeriod = new SurveyPeriodBean();

    /** Occurences : Number of times the region should be fully covered. */
    private int Occurences = 0;

    /** Periodicity Range Min */
    private float periodicityMin = 0;

    /** Periodicity Range Max */
    private float periodicityMax = 0;

    /** Latest start : Date */
    private String latestStart = null;

    /**	 * debut des methodes	 	 * 	 * Constructeur par defaut : vide	 */
    public TemporalSeriesBean() {
    }

    /** 	 * getter on surveyPeriod	 * 	 * @return SurveyPeriodBean : survey Period	*/
    public SurveyPeriodBean getSurveyPeriod() {
        return this.surveyPeriod;
    }

    /** 	 * getter on Occurences	 * 	 * @return int : Occurences	*/
    public int getOccurences() {
        return this.Occurences;
    }

    /** 	 * getter on periodicityMin	 * 	 * @return int : periodicity Min	*/
    public float getPeriodicityMin() {
        return this.periodicityMin;
    }

    /** 	 * getter on periodicityMax	 * 	 * @return int : periodicity Max	*/
    public float getPeriodicityMax() {
        return this.periodicityMax;
    }

    /** 	 * getter on latestStart	 * 	 * @return String : latest Start	*/
    public String getLatestStart() {
        return (this.latestStart != null) ? this.latestStart : "";
    }

    /** 	 * setter on _surveyPeriod	 * 	 * @param _surveyPeriod (SurveyPeriodBean): survey Period value	*/
    public void setSurveyPeriod(SurveyPeriodBean _surveyPeriod) {
        this.surveyPeriod = _surveyPeriod;
    }

    /** 	 * setter on Occurences	 * 	 * @param _occurences (int): Occurences value	*/
    public void setOccurences(int _occurences) {
        this.Occurences = _occurences;
    }

    /** 	 * setter on periodicityMin	 * 	 * @param _periodicityMin (float): periodicity Min value	*/
    public void setPeriodicityMin(float _periodicityMin) {
        this.periodicityMin = _periodicityMin;
    }

    /** 	 * setter on periodicityMax	 * 	 * @param _periodicityMax (float): periodicity Max value	*/
    public void setPeriodicityMax(float _periodicityMax) {
        this.periodicityMax = _periodicityMax;
    }

    /** 	 * setter on latestStart	 * 	 * @param _latestStart (String): latest Start value	*/
    public void setLatestStart(String _latestStart) {
        this.latestStart = _latestStart;
    }
}
