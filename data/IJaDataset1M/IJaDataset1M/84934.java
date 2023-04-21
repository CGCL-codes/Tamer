package org.primordion.user.app.climatechange.model04;

import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.IXholon;

/**
 * Surface of the Earth.
 * @author ken
 *
 */
public class Surface extends Xhmodel04 {

    private IXholon atmosphere = null;

    /** Net shortwave flux that will be absorbed or reflected by the Surface SW. */
    private double netShortwaveFlux = VAL_NULL;

    /** Reflected solar radiation from surface. */
    private double reflectedSolarRadiation = VAL_NULL;

    /** Shortwave radiation absorbed by surface. */
    private double absorbedBySurfaceSw = VAL_NULL;

    /** Longwave radiation absorbed by surface. */
    private double absorbedBySurfaceLw = VAL_NULL;

    /**
	 * Sensible heat flux SH.
	 * "SW - netLW - LH - SH = 0" (K&T, p.205)
	 * so SH = SW - netLW - LH
	 * SH = 168 - (390 - 324) - 78
	 */
    private double sensibleHeatFlux = 24.0;

    /**
	 * Latent heat flux LH.
	 * "equal to the global mean rate of precipitation" (K&T, p.204)
	 */
    private double latentHeatFlux = 78.0;

    /**
	 * Surface radiation.
	 * "corresponds to a blackbody emission at 15C" (K&T, p.205)
	 * mean Earth temperature estimates online: 14C 14.6C
	 * average surface temperature of the Earth: 15C 288K (Neelin, p.198)
	 * can use Stefan-Boltzmann constant; my calculator: (390/.0000000567)pow0.25 --> 287.98K (14.83C)
	 */
    private double surfaceRadiation = 390.0;

    /**
	 * Total accumulated longwave infrared heat energy absorbed by the surface, from all sources.
	 * TODO initial value should correspond to the Earth's average annual temperature.
	 */
    private double heat = 0.0;

    /** Albedo of the surface. Based on planetary albedo of 0.31 */
    private double albedo = 0.15;

    /** outgoing proportions (from the surface)
	 * "SW - netLW - LH - SH = 0" (K&T, p.205)
	 */
    private double latentHeatFluxPro = 0.0;

    private double surfaceRadiationPro = 0.0;

    public void postConfigure() {
        double absorbedBySurfaceTotal = 168.0 + 324.0;
        latentHeatFluxPro = latentHeatFlux / absorbedBySurfaceTotal;
        surfaceRadiationPro = surfaceRadiation / absorbedBySurfaceTotal;
        super.postConfigure();
    }

    public void act() {
        if (netShortwaveFlux != VAL_NULL) {
            super.act();
            absorbedBySurfaceSw = netShortwaveFlux - reflectedSolarRadiation;
            double absorbedBySurfaceTotal = absorbedBySurfaceSw + absorbedBySurfaceLw;
            latentHeatFlux = absorbedBySurfaceTotal * latentHeatFluxPro;
            surfaceRadiation = absorbedBySurfaceTotal * surfaceRadiationPro;
            sensibleHeatFlux = absorbedBySurfaceTotal - surfaceRadiation - latentHeatFlux;
            atmosphere.sendMessage(SIG_REFLECTED_SOLAR_RADIATION, new Double(reflectedSolarRadiation), this);
            atmosphere.sendMessage(SIG_LATENT_HEAT_FLUX, new Double(latentHeatFlux), this);
            atmosphere.sendMessage(SIG_SURFACE_RADIATION, new Double(surfaceRadiation), this);
            atmosphere.sendMessage(SIG_SENSIBLE_HEAT_FLUX, new Double(sensibleHeatFlux), this);
        }
    }

    public void processReceivedMessage(IMessage msg) {
        switch(msg.getSignal()) {
            case SIG_NET_SHORTWAVE_FLUX:
                netShortwaveFlux = ((Double) msg.getData()).doubleValue();
                break;
            case SIG_BACK_RADIATION:
                absorbedBySurfaceLw = ((Double) msg.getData()).doubleValue();
                break;
            default:
                break;
        }
    }

    public IXholon getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(IXholon atmosphere) {
        this.atmosphere = atmosphere;
    }

    public double getNetShortwaveFlux() {
        return netShortwaveFlux;
    }

    public void setNetShortwaveFlux(double netShortwaveFlux) {
        this.netShortwaveFlux = netShortwaveFlux;
    }

    public double getReflectedSolarRadiation() {
        return reflectedSolarRadiation;
    }

    public void setReflectedSolarRadiation(double reflectedSolarRadiation) {
        this.reflectedSolarRadiation = reflectedSolarRadiation;
    }

    public double getAbsorbedBySurfaceSw() {
        return absorbedBySurfaceSw;
    }

    public void setAbsorbedBySurfaceSw(double absorbedBySurfaceSW) {
        this.absorbedBySurfaceSw = absorbedBySurfaceSW;
    }

    public double getAbsorbedBySurfaceLw() {
        return absorbedBySurfaceLw;
    }

    public void setAbsorbedBySurfaceLw(double absorbedBySurfaceLW) {
        this.absorbedBySurfaceLw = absorbedBySurfaceLW;
    }

    public double getSensibleHeatFlux() {
        return sensibleHeatFlux;
    }

    public void setSensibleHeatFlux(double sensibleHeatFlux) {
        this.sensibleHeatFlux = sensibleHeatFlux;
    }

    public double getLatentHeatFlux() {
        return latentHeatFlux;
    }

    public void setLatentHeatFlux(double latentHeatFlux) {
        this.latentHeatFlux = latentHeatFlux;
    }

    public double getSurfaceRadiation() {
        return surfaceRadiation;
    }

    public void setSurfaceRadiation(double surfaceRadiation) {
        this.surfaceRadiation = surfaceRadiation;
    }

    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public double getAlbedo() {
        return albedo;
    }

    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }
}
