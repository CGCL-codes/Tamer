package com.astrium.faceo.client.bean.programming.sps2.request;

import java.io.Serializable;

/** * <B>SITE FACEO</B> <BR> *  * <P> * Acquisition Type informations : * The AcquisitionType class is used to specify if the acquisition is monoscopic or * stereoscopic, as well as contain specific acquisition parameters. * </P> * </P> <BR> *  * sample of Acquisition type for GetFeasibility Request *  *   <eo:AcquisitionType> *       <eo:MonoscopicAcquisition> *            <eo:CoverageType>MULTIPASS</eo:CoverageType> *            <eo:IncidenceAngle> *               <eo:Azimuth> *                  <eo:min>-180.0</eo:min> *                  <eo:max>180.0</eo:max> *               </eo:Azimuth> *               <eo:Elevation> *                  <eo:min>0.0</eo:min> *                  <eo:max>15.0</eo:max> *               </eo:Elevation> *            </eo:IncidenceAngle> *            <eo:AcquisitionParametersOPT> *               <eo:GroundResolution> *                  <eo:min>2.5</eo:min> *                  <eo:max>10.0</eo:max> *               </eo:GroundResolution> *               <eo:InstrumentMode>PANCHROMATIC</eo:InstrumentMode> *               <eo:FusionAccepted>false</eo:FusionAccepted> *            </eo:AcquisitionParametersOPT> *       </eo:MonoscopicAcquisition> *   </eo:AcquisitionType> *  * @author  GR * @version 1.0, le 20/04/2010 */
public class PointingAngleRangeBean implements Serializable {

    /**	 * 	 */
    private static final long serialVersionUID = -5822825154274206887L;

    /** Along Track Range min (deg) */
    private float alongTrackMin = 0;

    /** Along Track Range max (deg) */
    private float alongTrackMax = 0;

    /** Across Track Range min (deg) */
    private float acrossTrackMin = 0;

    /** Across Track Range max (deg) */
    private float acrossTrackMax = 0;

    /**	 * debut des methodes	 	 * 	 * Constructeur par defaut : vide	 */
    public PointingAngleRangeBean() {
    }

    /** 	 * getter on alongTrackMin (deg)	 * 	 * @return float : along Track Min	*/
    public float getAlongTrackMin() {
        return this.alongTrackMin;
    }

    /** 	 * getter on alongTrackMax (deg)	 * 	 * @return float : along Track Max	*/
    public float getAlongTrackMax() {
        return this.alongTrackMax;
    }

    /** 	 * getter on acrossTrackMin (deg)	 * 	 * @return float : across Track Min	*/
    public float getAcrossTrackMin() {
        return this.acrossTrackMin;
    }

    /** 	 * getter on acrossTrackMax (deg)	 * 	 * @return float : across Track Max	*/
    public float getAcrossTrackMax() {
        return this.acrossTrackMax;
    }

    /** 	 * setter on alongTrackMin (deg)	 * 	 * @param _alongTrackMin (float): along Track Min value	*/
    public void setAlongTrackMin(float _alongTrackMin) {
        this.alongTrackMin = _alongTrackMin;
    }

    /** 	 * setter on alongTrackMax (deg)	 * 	 * @param _alongTrackMax (float): along Track Max value	*/
    public void setAlongTrackMax(float _alongTrackMax) {
        this.alongTrackMax = _alongTrackMax;
    }

    /** 	 * setter on acrossTrackMin (deg)	 * 	 * @param _acrossTrackMin (float): across Track Max value	*/
    public void setAcrosTrackMin(float _acrossTrackMin) {
        this.acrossTrackMin = _acrossTrackMin;
    }

    /** 	 * setter on acrossTrackMax (deg)	 * 	 * @param _acrossTrackMax (float): across Track Max value	*/
    public void setAcrosTrackMax(float _acrossTrackMax) {
        this.acrossTrackMax = _acrossTrackMax;
    }
}
