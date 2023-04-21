package us.wthr.jdem846.render.render3;

import java.util.Calendar;
import java.util.TimeZone;
import us.wthr.jdem846.DemConstants;
import us.wthr.jdem846.ModelContext;
import us.wthr.jdem846.ModelOptionNamesEnum;
import us.wthr.jdem846.Perspectives;
import us.wthr.jdem846.color.ColorAdjustments;
import us.wthr.jdem846.exception.RayTracingException;
import us.wthr.jdem846.exception.RenderEngineException;
import us.wthr.jdem846.gis.Coordinate;
import us.wthr.jdem846.gis.CoordinateTypeEnum;
import us.wthr.jdem846.gis.datetime.EarthDateTime;
import us.wthr.jdem846.gis.datetime.SolarCalculator;
import us.wthr.jdem846.gis.datetime.SolarPosition;
import us.wthr.jdem846.gis.planets.Planet;
import us.wthr.jdem846.gis.planets.PlanetsRegistry;
import us.wthr.jdem846.lighting.LightSourceSpecifyTypeEnum;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.math.MathExt;
import us.wthr.jdem846.math.Vectors;
import us.wthr.jdem846.rasterdata.RasterDataContext;
import us.wthr.jdem846.render.RayTracing;
import us.wthr.jdem846.render.RayTracing.RasterDataFetchHandler;

public class GridHillshadeProcessor extends AbstractGridProcessor implements GridProcessor, ModelPointHandler {

    private static Log log = Logging.getLog(GridHillshadeProcessor.class);

    protected double relativeLightIntensity;

    protected double relativeDarkIntensity;

    protected int spotExponent;

    private double latitudeResolution;

    private double longitudeResolution;

    private double[] normalBufferA = new double[3];

    private double[] normalBufferB = new double[3];

    protected double backLeftPoints[] = new double[3];

    protected double backRightPoints[] = new double[3];

    protected double frontLeftPoints[] = new double[3];

    protected double frontRightPoints[] = new double[3];

    protected double sunsource[] = new double[3];

    protected double solarElevation;

    protected double solarAzimuth;

    protected double solarZenith;

    protected int[] rgbaBuffer = new int[4];

    protected Perspectives perspectives = new Perspectives();

    private double lightingMultiple = 1.0;

    private Planet planet;

    double north;

    double south;

    double east;

    double west;

    protected double lightZenith;

    protected double darkZenith;

    protected LightSourceSpecifyTypeEnum lightSourceType;

    protected long lightOnDate;

    protected boolean recalcLightOnEachPoint;

    protected SolarCalculator solarCalculator;

    protected SolarPosition position;

    protected EarthDateTime datetime;

    protected Coordinate latitudeCoordinate;

    protected Coordinate longitudeCoordinate;

    protected boolean sunIsUp = false;

    protected RayTracing lightSourceRayTracer;

    protected boolean rayTraceShadows;

    protected double shadowIntensity;

    public GridHillshadeProcessor(ModelContext modelContext, ModelGrid modelGrid) {
        super(modelContext, modelGrid);
    }

    @Override
    public void prepare() throws RenderEngineException {
        relativeLightIntensity = modelContext.getLightingContext().getRelativeLightIntensity();
        relativeDarkIntensity = modelContext.getLightingContext().getRelativeDarkIntensity();
        spotExponent = modelContext.getLightingContext().getSpotExponent();
        latitudeResolution = modelContext.getModelDimensions().getOutputLatitudeResolution();
        longitudeResolution = modelContext.getModelDimensions().getOutputLongitudeResolution();
        north = modelContext.getNorth();
        south = modelContext.getSouth();
        east = modelContext.getEast();
        west = modelContext.getWest();
        planet = PlanetsRegistry.getPlanet(modelContext.getModelOptions().getOption(ModelOptionNamesEnum.PLANET));
        solarAzimuth = modelContext.getLightingContext().getLightingAzimuth();
        solarElevation = modelContext.getLightingContext().getLightingElevation();
        lightSourceType = modelContext.getLightingContext().getLightSourceSpecifyType();
        lightOnDate = modelContext.getLightingContext().getLightingOnDate();
        recalcLightOnEachPoint = modelContext.getLightingContext().getRecalcLightOnEachPoint();
        lightZenith = modelContext.getLightingContext().getLightZenith();
        darkZenith = modelContext.getLightingContext().getDarkZenith();
        if (lightSourceType == LightSourceSpecifyTypeEnum.BY_AZIMUTH_AND_ELEVATION) {
            sunIsUp = true;
            setUpLightSource(0, 0, modelContext.getLightingContext().getLightingElevation(), modelContext.getLightingContext().getLightingAzimuth(), true);
        }
        if (lightSourceType == LightSourceSpecifyTypeEnum.BY_DATE_AND_TIME) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            cal.setTimeInMillis(lightOnDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            log.info("Setting initial date/time to " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
            position = new SolarPosition();
            datetime = new EarthDateTime(year, month, day, hour, minute, second, 0, false);
            latitudeCoordinate = new Coordinate(0.0, CoordinateTypeEnum.LATITUDE);
            longitudeCoordinate = new Coordinate(0.0, CoordinateTypeEnum.LONGITUDE);
            solarCalculator = new SolarCalculator();
            solarCalculator.setDatetime(datetime);
            if (!recalcLightOnEachPoint) {
                double latitude = (north + south) / 2.0;
                double longitude = (east + west) / 2.0;
                setUpLightSource(latitude, longitude, 0, 0, true);
            }
        }
        lightingMultiple = modelContext.getLightingContext().getLightingMultiple();
        rayTraceShadows = modelContext.getLightingContext().getRayTraceShadows();
        shadowIntensity = modelContext.getLightingContext().getShadowIntensity();
        if (rayTraceShadows) {
            lightSourceRayTracer = new RayTracing(modelContext, new RasterDataFetchHandler() {

                public double getRasterData(double latitude, double longitude) throws Exception {
                    return getElevationAtPoint(latitude, longitude);
                }
            });
        } else {
            lightSourceRayTracer = null;
        }
    }

    @Override
    public void process() throws RenderEngineException {
        super.process();
    }

    @Override
    public void onCycleStart() throws RenderEngineException {
    }

    @Override
    public void onModelLatitudeStart(double latitude) throws RenderEngineException {
    }

    @Override
    public void onModelPoint(double latitude, double longitude) throws RenderEngineException {
        resetBuffers(latitude, longitude);
        setUpLightSource(latitude, longitude, 0, 0, recalcLightOnEachPoint);
        ModelPoint modelPoint = modelGrid.get(latitude, longitude);
        calculateNormal(modelPoint, latitude, longitude);
        calculateDotProduct(modelPoint, latitude, longitude);
        processPointColor(modelPoint, latitude, longitude);
    }

    @Override
    public void onModelLatitudeEnd(double latitude) throws RenderEngineException {
    }

    @Override
    public void onCycleEnd() throws RenderEngineException {
    }

    protected void calculateNormal(ModelPoint midPoint, double latitude, double longitude) {
        double eLat = latitude;
        double eLon = longitude + longitudeResolution;
        double sLat = latitude - latitudeResolution;
        double sLon = longitude;
        double wLat = latitude;
        double wLon = longitude - longitudeResolution;
        double nLat = latitude + latitudeResolution;
        double nLon = longitude;
        ModelPoint ePoint = modelGrid.get(eLat, eLon);
        ModelPoint sPoint = modelGrid.get(sLat, sLon);
        ModelPoint wPoint = modelGrid.get(wLat, wLon);
        ModelPoint nPoint = modelGrid.get(nLat, nLon);
        double midElev = midPoint.getElevation();
        double eElev = (ePoint != null) ? ePoint.getElevation() : midPoint.getElevation();
        double sElev = (sPoint != null) ? sPoint.getElevation() : midPoint.getElevation();
        double wElev = (wPoint != null) ? wPoint.getElevation() : midPoint.getElevation();
        double nElev = (nPoint != null) ? nPoint.getElevation() : midPoint.getElevation();
        calculateNormal(0.0, wElev, midElev, nElev, CornerEnum.SOUTHEAST, normalBufferA);
        normalBufferB[0] = normalBufferA[0];
        normalBufferB[1] = normalBufferA[1];
        normalBufferB[2] = normalBufferA[2];
        calculateNormal(wElev, 0.0, sElev, midElev, CornerEnum.NORTHEAST, normalBufferA);
        normalBufferB[0] += normalBufferA[0];
        normalBufferB[1] += normalBufferA[1];
        normalBufferB[2] += normalBufferA[2];
        calculateNormal(midElev, sElev, 0.0, eElev, CornerEnum.NORTHWEST, normalBufferA);
        normalBufferB[0] += normalBufferA[0];
        normalBufferB[1] += normalBufferA[1];
        normalBufferB[2] += normalBufferA[2];
        calculateNormal(nElev, midElev, eElev, 0.0, CornerEnum.SOUTHWEST, normalBufferA);
        normalBufferB[0] += normalBufferA[0];
        normalBufferB[1] += normalBufferA[1];
        normalBufferB[2] += normalBufferA[2];
        normalBufferB[0] = normalBufferB[0] / 4.0;
        normalBufferB[1] = normalBufferB[1] / 4.0;
        normalBufferB[2] = normalBufferB[2] / 4.0;
        midPoint.setNormal(normalBufferB);
    }

    protected void calculateNormal(double nw, double sw, double se, double ne, CornerEnum corner, double[] normal) {
        backLeftPoints[1] = nw * lightingMultiple;
        backRightPoints[1] = ne * lightingMultiple;
        frontLeftPoints[1] = sw * lightingMultiple;
        frontRightPoints[1] = se * lightingMultiple;
        if (corner == CornerEnum.NORTHWEST) {
            perspectives.calcNormal(backLeftPoints, frontLeftPoints, backRightPoints, normal);
        } else if (corner == CornerEnum.SOUTHWEST) {
            perspectives.calcNormal(backLeftPoints, frontLeftPoints, frontRightPoints, normal);
        } else if (corner == CornerEnum.SOUTHEAST) {
            perspectives.calcNormal(frontLeftPoints, frontRightPoints, backRightPoints, normal);
        } else if (corner == CornerEnum.NORTHEAST) {
            perspectives.calcNormal(backLeftPoints, frontRightPoints, backRightPoints, normal);
        }
    }

    protected void calculateDotProduct(ModelPoint modelPoint, double latitude, double longitude) throws RenderEngineException {
        double dot = perspectives.dotProduct(modelPoint.getNormal(), sunsource);
        double lower = lightZenith;
        double upper = darkZenith;
        if (solarZenith > lower && solarZenith <= upper) {
            double range = (solarZenith - lower) / (upper - lower);
            dot = dot - (2 * range);
        } else if (solarZenith > upper) {
            dot = dot - (2 * 1.0);
        }
        if (dot < -1.0) {
            dot = -1.0;
        }
        try {
            double blockAmt = calculateRayTracedShadow(modelPoint, latitude, longitude);
            dot = dot - (2 * shadowIntensity * blockAmt);
            if (dot < -1.0) {
                dot = -1.0;
            }
        } catch (RayTracingException ex) {
            throw new RenderEngineException("Error ray tracing shadows: " + ex.getMessage(), ex);
        }
        if (dot > 0) {
            dot *= relativeLightIntensity;
        } else if (dot < 0) {
            dot *= relativeDarkIntensity;
        }
        if (spotExponent != 1) {
            dot = MathExt.pow(dot, spotExponent);
        }
        modelPoint.setDotProduct(dot);
    }

    protected double calculateRayTracedShadow(ModelPoint modelPoint, double latitude, double longitude) throws RayTracingException {
        if (this.rayTraceShadows) {
            double blockAmt = lightSourceRayTracer.isRayBlocked(this.solarElevation, this.solarAzimuth, latitude, longitude, modelPoint.getElevation());
            return blockAmt;
        } else {
            return 0.0;
        }
    }

    protected void processPointColor(ModelPoint modelPoint, double latitude, double longitude) throws RenderEngineException {
        double dot = modelPoint.getDotProduct();
        modelPoint.getRgba(rgbaBuffer, false);
        ColorAdjustments.adjustBrightness(rgbaBuffer, dot);
        modelPoint.setRgba(rgbaBuffer, true);
    }

    protected double getElevationAtPoint(double latitude, double longitude) {
        ModelPoint modelPoint = modelGrid.get(latitude, longitude);
        if (modelPoint != null) {
            return modelPoint.getElevation();
        } else {
            return DemConstants.ELEV_NO_DATA;
        }
    }

    protected void setUpLightSource(double latitude, double longitude, double solarElevation, double solarAzimuth, boolean force) {
        if (force && lightSourceType == LightSourceSpecifyTypeEnum.BY_AZIMUTH_AND_ELEVATION) {
            setUpLightSourceBasic(solarElevation, solarAzimuth);
        } else if (lightSourceType == LightSourceSpecifyTypeEnum.BY_DATE_AND_TIME) {
            setUpLightSourceOnDate(latitude, longitude);
        }
    }

    protected void setUpLightSourceOnDate(double latitude, double longitude) {
        latitudeCoordinate.fromDecimal(latitude);
        longitudeCoordinate.fromDecimal(longitude);
        solarCalculator.update();
        solarCalculator.setLatitude(latitudeCoordinate);
        solarCalculator.setLongitude(longitudeCoordinate);
        solarAzimuth = solarCalculator.solarAzimuthAngle();
        solarElevation = solarCalculator.solarElevationAngle();
        solarZenith = solarCalculator.solarZenithAngle();
        if (solarZenith > darkZenith) {
            sunIsUp = false;
        } else {
            sunIsUp = true;
        }
        setUpLightSourceBasic(solarElevation, solarAzimuth);
    }

    protected void setUpLightSourceBasic(double solarElevation, double solarAzimuth) {
        sunsource[0] = 0.0;
        sunsource[1] = 0.0;
        sunsource[2] = -149598000000.0;
        Vectors.rotate(solarElevation, -solarAzimuth, 0, sunsource);
    }

    protected void resetBuffers(double latitude, double longitude) {
        double meanRadius = DemConstants.EARTH_MEAN_RADIUS;
        if (planet != null) {
            meanRadius = planet.getMeanRadius();
        }
        double resolutionMeters = RasterDataContext.getMetersResolution(meanRadius, latitude, longitude, latitudeResolution, longitudeResolution);
        double xzRes = (resolutionMeters / 2.0);
        backLeftPoints[0] = -xzRes;
        backLeftPoints[1] = 0.0;
        backLeftPoints[2] = -xzRes;
        backRightPoints[0] = xzRes;
        backRightPoints[1] = 0.0;
        backRightPoints[2] = -xzRes;
        frontLeftPoints[0] = -xzRes;
        frontLeftPoints[1] = 0.0;
        frontLeftPoints[2] = xzRes;
        frontRightPoints[0] = xzRes;
        frontRightPoints[1] = 0.0;
        frontRightPoints[2] = xzRes;
    }

    public boolean rayTraceShadows() {
        return rayTraceShadows;
    }

    public void setRayTraceShadows(boolean rayTraceShadows) {
        this.rayTraceShadows = rayTraceShadows;
    }

    public boolean recalcLightOnEachPoint() {
        return recalcLightOnEachPoint;
    }

    public void setRecalcLightOnEachPoint(boolean recalcLightOnEachPoint) {
        this.recalcLightOnEachPoint = recalcLightOnEachPoint;
    }
}
