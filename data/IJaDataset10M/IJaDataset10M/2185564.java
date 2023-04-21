package org.stellarium.astro;

import org.stellarium.*;
import org.stellarium.astro.planet.*;
import org.stellarium.data.IniFileParser;
import org.stellarium.data.ResourceLocatorUtil;
import org.stellarium.projector.DefaultProjector;
import org.stellarium.ui.render.SFontIfc;
import org.stellarium.ui.render.STexture;
import org.stellarium.ui.render.STextureFactory;
import javax.vecmath.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.cos;
import static javax.media.opengl.GL.*;
import static org.stellarium.StelUtility.*;
import static org.stellarium.ui.SglAccess.*;

/**
 * @author Fred Simon
 * @version 0.8.2
 */
public class SolarSystem {

    protected final Logger logger;

    protected final STextureFactory textureFactory;

    public SolarSystem(Logger parentLogger) throws StellariumException {
        logger = Logger.getLogger(getClass().getName());
        if (parentLogger != null) {
            logger.setParent(parentLogger);
        }
        textureFactory = new STextureFactory(logger);
    }

    public void setFont(SFontIfc planetNameFont) throws StellariumException {
        _planetNameFontIfc = planetNameFont;
        Planet.setFont(_planetNameFontIfc);
    }

    public void setLabelColor(Color c) {
        Planet.setLabelColor(c);
    }

    public Color getLabelColor() {
        return Planet.getLabelColor();
    }

    public void setOrbitColor(Color c) {
        Planet.setOrbitColor(c);
    }

    public Color getOrbitColor() {
        return Planet.getOrbitColor();
    }

    public void setTrailColor(Color c) {
        Planet.setTrailColor(c);
    }

    public Color getTrailColor() {
        return Planet.getTrailColor();
    }

    /**
     * Init and load the solar system data
     *
     * @throws StellariumException If the planet data cannot be interpreted
     */
    public void load() {
        final String vsop87Filename = "Vsop87.data";
        try {
            InputStream vsop87resource = Vsop87.class.getResourceAsStream(vsop87Filename);
            if (vsop87resource == null) {
                throw new StellariumException("Could not find VSOP87 solar system resource \"" + vsop87Filename + "\"");
            }
            InputStreamReader vsop87reader = new InputStreamReader(vsop87resource, "UTF-8");
            Vsop87 vsop87 = new Vsop87(vsop87reader, logger);
            URL planetFile = ResourceLocatorUtil.getInstance().getDataFile("ssystem.ini");
            logger.fine("Loading Solar System data from " + planetFile);
            IniFileParser pd = new IniFileParser(getClass(), planetFile);
            String[] sectionNames = pd.getSectionNames();
            for (String secName : sectionNames) {
                String englishName = pd.getStr(secName, IniFileParser.NAME);
                final String strParent = pd.getStr(secName, "parent");
                Planet parent = null;
                if (strParent != null && !strParent.equals("none")) {
                    for (Planet planet : systemPlanets) {
                        if (planet.getEnglishName().equalsIgnoreCase(strParent)) {
                            parent = planet;
                            break;
                        }
                    }
                    if (parent == null) {
                        throw new StellariumException("ERROR : can't find parent " + strParent + " for " + englishName);
                    }
                }
                String funcName = pd.getStr(secName, "coord_func");
                PosFunc posfunc;
                OsculatingFunc osculatingFunc = null;
                EllipticalOrbit orb;
                if ("ell_orbit".equals(funcName)) {
                    double period = pd.getDouble(secName, "orbit_Period");
                    double epoch = pd.getDouble(secName, "orbit_Epoch", JulianDay.J2000);
                    double semiMajorAxis = pd.getDouble(secName, "orbit_SemiMajorAxis") / AU;
                    double eccentricity = pd.getDouble(secName, "orbit_Eccentricity");
                    double inclination = Math.toRadians(pd.getDouble(secName, "orbit_Inclination"));
                    double ascendingNode = Math.toRadians(pd.getDouble(secName, "orbit_AscendingNode"));
                    double longOfPericenter = Math.toRadians(pd.getDouble(secName, "orbit_LongOfPericenter"));
                    double meanLongitude = Math.toRadians(pd.getDouble(secName, "orbit_MeanLongitude"));
                    double argOfPericenter = longOfPericenter - ascendingNode;
                    double anomalyAtEpoch = meanLongitude - (argOfPericenter + ascendingNode);
                    double pericenterDistance = semiMajorAxis * (1.0 - eccentricity);
                    final double parent_rot_obliquity = parent.getParent() != null ? parent.getRotObliquity() : 0.0;
                    final double parent_rot_asc_node = parent.getParent() != null ? parent.getRotAscendingnode() : 0.0;
                    orb = new EllipticalOrbit(pericenterDistance, eccentricity, inclination, ascendingNode, argOfPericenter, anomalyAtEpoch, period, epoch, parent_rot_obliquity, parent_rot_asc_node);
                    ellOrbits.add(orb);
                    posfunc = orb.getEllipticalFunc();
                } else if ("sun_special".equals(funcName)) {
                    posfunc = MiscStellPlanet.createSunHelioFunc();
                } else if ("mercury_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.MERCURY, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.MERCURY, vsop87);
                } else if ("venus_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.VENUS, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.VENUS, vsop87);
                } else if ("earth_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.EMB, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.EMB, vsop87);
                } else if ("lunar_special".equals(funcName)) {
                    posfunc = new Elp82b();
                } else if ("mars_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.MARS, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.MARS, vsop87);
                } else if ("jupiter_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.JUPITER, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.JUPITER, vsop87);
                } else if ("europa_special".equals(funcName)) {
                    posfunc = GalileanMoons.createEuropaGalileanFunc();
                } else if ("calisto_special".equals(funcName)) {
                    posfunc = GalileanMoons.createCallistoGalileanFunc();
                } else if ("io_special".equals(funcName)) {
                    posfunc = GalileanMoons.createIoGalileanFunc();
                } else if ("ganymede_special".equals(funcName)) {
                    posfunc = GalileanMoons.createGanymedeGalileanFunc();
                } else if ("saturn_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.SATURN, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.SATURN, vsop87);
                } else if ("uranus_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.URANUS, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.URANUS, vsop87);
                } else if ("neptune_special".equals(funcName)) {
                    posfunc = new Vsop87CoorFunc(Body.NEPTUNE, vsop87);
                    osculatingFunc = new Vsop87OsculatingFunc(Body.NEPTUNE, vsop87);
                } else if ("pluto_special".equals(funcName)) posfunc = Pluto.createHelioFunc(); else {
                    logger.warning("Can't find posfunc " + funcName + " for " + englishName);
                    continue;
                }
                if (posfunc == null) {
                    logger.warning("can't find posfunc " + funcName + " for " + englishName);
                    continue;
                }
                Planet p = new Planet(parent, englishName, pd.getBoolean(secName, "halo"), pd.getBoolean(secName, "lighting"), pd.getDouble(secName, "radius") / AU, pd.getDouble(secName, "oblateness", 0.0), stringToColor(pd.getStr(secName, "color")), pd.getDouble(secName, "albedo"), pd.getStr(secName, "tex_map"), pd.getStr(secName, "tex_halo"), posfunc, osculatingFunc, pd.getBoolean(secName, "hidden", false), logger);
                if ("earth".equalsIgnoreCase(englishName)) {
                    earth = p;
                } else if ("sun".equalsIgnoreCase(englishName)) {
                    sun = p;
                } else if ("moon".equalsIgnoreCase(englishName)) {
                    moon = p;
                }
                p.setRotationElements(pd.getDouble(secName, "rot_periode", pd.getDouble(secName, "orbit_Period", 24.)) / 24., pd.getDouble(secName, "rot_rotation_offset", 0.), pd.getDouble(secName, "rot_epoch", JulianDay.J2000), Math.toRadians(pd.getDouble(secName, "rot_obliquity", 0.)), Math.toRadians(pd.getDouble(secName, "rot_equator_ascending_node", 0.)), pd.getDouble(secName, "rot_precession_rate", 0.) * PI / (180. * 36525), pd.getDouble(secName, "sidereal_period", 0.));
                if (pd.getBoolean(secName, "rings", false)) {
                    final double r_min = pd.getDouble(secName, "ring_inner_size") / AU;
                    final double r_max = pd.getDouble(secName, "ring_outer_size") / AU;
                    Planet.Ring r = new Planet.Ring(r_min, r_max, pd.getStr(secName, "tex_ring"), logger);
                    p.setRings(r);
                }
                String bighalotexfile = pd.getStr(secName, "tex_big_halo", "");
                if (!isEmpty(bighalotexfile)) {
                    p.setBigHalo(bighalotexfile);
                    p.setHaloSize(pd.getDouble(secName, "big_halo_size", 50.f));
                }
                systemPlanets.add(p);
            }
            texEarthShadow = textureFactory.createTexture("earth-shadow.png", STexture.TEX_LOAD_TYPE_PNG_ALPHA);
            logger.fine("Solar System loaded.");
        } catch (UnsupportedEncodingException e) {
            throw new StellariumException(e);
        }
    }

    /**
     * Compute the position for every elements of the solar system.
     * The order is not important since the position is computed relatively to the mother body
     *
     * @param date the julian day to compute position
     */
    public void computePositions(double date, final Planet homePlanet) {
        if (lightTravelTime) {
            for (Planet iter : systemPlanets) {
                iter.computePositionWithoutOrbits(date);
            }
            final Point3d home_pos = new Point3d(homePlanet.getHeliocentricEclipticPos());
            for (Planet iter : systemPlanets) {
                final double light_speed_correction = iter.getHeliocentricEclipticPos().distance(home_pos) * (149597870000.0 / (299792458.0 * 86400));
                iter.computePosition(date - light_speed_correction);
            }
        } else {
            for (Planet iter : systemPlanets) {
                iter.computePosition(date);
            }
        }
    }

    /**
     * Compute the transformation matrix for every elements of the solar system.
     * The elements have to be ordered hierarchically, eg. it's important to compute earth before moon.
     *
     * @param date the julian day to compute position
     */
    public void computeTransMatrices(double date, final Planet homePlanet) {
        if (lightTravelTime) {
            final Point3d home_pos = new Point3d(homePlanet.getHeliocentricEclipticPos());
            for (Planet iter : systemPlanets) {
                final double light_speed_correction = iter.getHeliocentricEclipticPos().distance(home_pos) * (149597870000.0 / (299792458.0 * 86400));
                iter.computeTransMatrix(date - light_speed_correction);
            }
        } else {
            for (Planet iter : systemPlanets) {
                iter.computeTransMatrix(date);
            }
        }
    }

    /**
     * Draw all the elements of the solar system
     * We are supposed to be in heliocentric coordinate
     */
    public void draw(DefaultProjector prj, Navigator nav, ToneReproductor eye, boolean flagPoint) {
        float zero[] = { 0, 0, 0, 0 };
        float ambient[] = { 0.03f, 0.03f, 0.03f, 0.03f };
        float diffuse[] = { 1, 1, 1, 1 };
        glLightfv(GL_LIGHT0, GL_AMBIENT, ambient, 0);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse, 0);
        glLightfv(GL_LIGHT0, GL_SPECULAR, zero, 0);
        glMaterialfv(GL_FRONT, GL_AMBIENT, ambient, 0);
        glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse, 0);
        glMaterialfv(GL_FRONT, GL_EMISSION, zero, 0);
        glMaterialfv(GL_FRONT, GL_SHININESS, zero, 0);
        glMaterialfv(GL_FRONT, GL_SPECULAR, zero, 0);
        Point3d sunPos = new Point3d(0, 0, 0);
        nav.switchToHeliocentric();
        nav.helioToLocal(sunPos);
        prj.setLightPos(sunPos);
        glLightfv(GL_LIGHT0, GL_POSITION, new float[] { 0.f, 0.f, 0.f, 1.f }, 0);
        glEnable(GL_LIGHT0);
        Point3d obsHelioPos = nav.getObserverHelioPos();
        for (Planet iter : systemPlanets) {
            iter.computeDistance(obsHelioPos);
        }
        Collections.sort(systemPlanets, new BiggerDistanceComparator());
        for (Planet iter : systemPlanets) {
            if (iter == moon && nearLunarEclipse(nav, prj)) {
                glClear(GL_STENCIL_BUFFER_BIT);
                glClearStencil(0x0);
                glStencilFunc(GL_ALWAYS, 0x1, 0x1);
                glStencilOp(GL_ZERO, GL_REPLACE, GL_REPLACE);
                iter.draw(prj, nav, eye, flagPoint, true);
            } else {
                if (iter.getParent() == null || iter.getParent() == sun || iter == moon) iter.draw(prj, nav, eye, flagPoint, false);
            }
        }
        glDisable(GL_LIGHT0);
        if (nav.getHomePlanet().getEnglishName().equals("Earth")) drawEarthShadow(nav, prj);
    }

    static class BiggerDistanceComparator implements Comparator<Planet> {

        public int compare(Planet p1, Planet p2) {
            if (p1.equals(p2)) return 0;
            double diff = p1.getDistance() - p2.getDistance();
            if (diff > 0.0d) {
                return -1;
            }
            return 1;
        }
    }

    public Planet searchByEnglishName(String planetEnglishName) {
        for (Planet iter : systemPlanets) {
            if (iter.getEnglishName().equalsIgnoreCase(planetEnglishName)) return iter;
        }
        return null;
    }

    public Planet searchByNamesI18(String planetNameI18n) {
        for (Planet iter : systemPlanets) {
            if (iter.getNameI18n().equals(planetNameI18n)) return iter;
        }
        return null;
    }

    /**
     * Search if any planet is close to position given in earth equatorial position and return the distance
     *
     * @param pos
     * @param nav
     * @param prj
     * @return
     */
    public Planet search(Vector3d pos, Navigator nav, DefaultProjector prj) {
        pos.normalize();
        Planet closest = null;
        double cosAngleClosest = 0;
        Vector3d equPos;
        for (Planet iter : systemPlanets) {
            equPos = new Vector3d(iter.getEarthEquPos(nav));
            equPos.normalize();
            double cosAngDist = equPos.x * pos.x + equPos.y * pos.y + equPos.z * pos.z;
            if (cosAngDist > cosAngleClosest) {
                closest = iter;
                cosAngleClosest = cosAngDist;
            }
        }
        if (cosAngleClosest > 0.999) return closest; else return null;
    }

    /**
     * Return a stl vector containing the planets located inside the limFOV circle around position v
     *
     * @param limFieldOfView
     * @param p
     * @param nav
     * @param prj
     * @return
     */
    public List<StelObject> searchAround(Tuple3d p, double limFieldOfView, Navigator nav, DefaultProjector prj) {
        List<StelObject> result = new ArrayList<StelObject>();
        Vector3d v = new Vector3d(p);
        v.normalize();
        double cosLimFOV = cos(Math.toRadians(limFieldOfView));
        Vector3d equPos;
        for (Planet iter : systemPlanets) {
            equPos = new Vector3d(iter.getEarthEquPos(nav));
            equPos.normalize();
            if (equPos.x * v.x + equPos.y * v.y + equPos.z * v.z >= cosLimFOV) {
                result.add(iter);
            }
        }
        return result;
    }

    public void translateNames(Translator trans) {
        for (Planet planet : systemPlanets) {
            planet.translateName(trans);
        }
    }

    List<String> getNamesI18() {
        List<String> names = new ArrayList<String>();
        for (Planet planet : systemPlanets) {
            names.add(planet.getNameI18n());
        }
        return names;
    }

    public String getPlanetHashString() {
        StringBuffer oss = new StringBuffer();
        for (Planet planet : systemPlanets) {
            String parentName = null;
            if (planet.getParent() != null) {
                parentName = planet.getParent().getEnglishName();
            }
            if (!parentName.equals("Sun")) {
                oss.append(Translator.getCurrentTranslator().translate(parentName)).append(" : ");
            }
            oss.append(Translator.getCurrentTranslator().translate(planet.getEnglishName())).append("\n");
            oss.append(planet.getEnglishName()).append("\n");
        }
        return oss.toString();
    }

    public void updateTrails(Navigator nav) {
        for (Planet iter : systemPlanets) {
            iter.updateTrail(nav);
        }
    }

    public void startTrails(boolean b) {
        for (Planet iter : systemPlanets) {
            iter.startTrail(b);
        }
    }

    public void setFlagPlanets(boolean b) {
        Planet.setflagShow(b);
    }

    public boolean getFlagPlanets() {
        return Planet.getflagShow();
    }

    public void setFlagTrails(boolean b) {
        for (Planet iter : systemPlanets) {
            iter.setFlagTrail(b);
        }
    }

    public boolean getFlagTrails() {
        for (Planet iter : systemPlanets) {
            if (iter.getFlagTrail()) return true;
        }
        return false;
    }

    public void setFlagHints(boolean b) {
        for (Planet iter : systemPlanets) {
            iter.setFlagHints(b);
        }
    }

    public boolean getFlagHints() {
        for (Planet iter : systemPlanets) {
            if (iter.getFlagHints()) return true;
        }
        return false;
    }

    public void setFlagOrbits(boolean b) {
        flagOrbits = b;
        if (!b || selected != null || selected.equals(sun)) {
            for (Planet iter : systemPlanets) {
                iter.setFlagOrbits(b);
            }
        } else {
            for (Planet iter : systemPlanets) {
                if (selected.equals(iter)) {
                    iter.setFlagOrbits(b);
                } else {
                    iter.setFlagOrbits(false);
                }
            }
        }
    }

    public boolean getFlagOrbits() {
        return flagOrbits;
    }

    public void setFlagLightTravelTime(boolean b) {
        lightTravelTime = b;
    }

    public boolean getFlagLightTravelTime() {
        return lightTravelTime;
    }

    /**
     * Set selected planet by english name or "" to select none
     */
    public void setSelected(String englishName) {
        setSelected(searchByEnglishName(englishName));
    }

    public void setSelected(final StelObject obj) {
        if (obj != null && obj.getType() == StelObject.TYPE.PLANET) selected = obj; else selected = StelObject.getUninitializedObject();
        setFlagHints(getFlagHints());
        setFlagOrbits(getFlagOrbits());
        setFlagTrails(getFlagTrails());
    }

    /**
     * draws earth shadow overlapping the moon using stencil buffer
     * umbra and penumbra are sized separately for accuracy
     *
     * @param nav
     * @param prj
     */
    void drawEarthShadow(Navigator nav, DefaultProjector prj) {
        Vector3d e = new Vector3d(getEarth().getEclipticPos());
        Vector3d m = new Vector3d(getMoon().getEclipticPos());
        Vector3d mh = new Vector3d(getMoon().getHeliocentricEclipticPos());
        double mscale = getMoon().getSphereScale();
        Vector3d shadow = new Vector3d(e);
        shadow.normalize();
        shadow.scale(e.length() + m.length());
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor3f(1, 1, 1);
        double rPenumbra = shadow.length() * 702378.1 / (AU * e.length()) - 696000 / AU;
        double rUmbra = 6378.1 / AU - m.length() * (689621.9 / (AU * e.length()));
        Vector3d rpt = new Vector3d();
        rpt.cross(shadow, new Vector3d(0, 0, 1));
        rpt.normalize();
        Vector3d upt = new Vector3d();
        rpt.scale(rUmbra * mscale * 1.02, upt);
        rpt.scale(rPenumbra * mscale);
        Vector3d mdist = new Vector3d();
        mdist.sub(shadow, mh);
        if (mdist.length() > rPenumbra + 2000 / AU) {
            return;
        }
        mdist.scale(mscale);
        shadow.add(mh, mdist);
        rPenumbra *= mscale;
        nav.switchToHeliocentric();
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
        Matrix4d mat = nav.getHelioToEyeMat();
        glBindTexture(GL_TEXTURE_2D, texEarthShadow.getID());
        Vector3d r = new Vector3d();
        Vector3d s = new Vector3d();
        glBegin(GL_TRIANGLE_FAN);
        glTexCoord2f(0, 0);
        prj.sVertex3(shadow.x, shadow.y, shadow.z, mat);
        Matrix4d rot = new Matrix4d();
        for (int i = 0; i <= 100; i++) {
            rot.setRotation(new AxisAngle4d(shadow, 2 * M_PI * i / 100.));
            rot.transform(upt, r);
            s.add(shadow, r);
            glTexCoord2f(0.6f, 0);
            prj.sVertex3(s.x, s.y, s.z, mat);
        }
        glEnd();
        Vector3d u = new Vector3d();
        Vector3d sp = new Vector3d();
        glBegin(GL_TRIANGLE_STRIP);
        for (int i = 0; i <= 100; i++) {
            rot.setRotation(new AxisAngle4d(shadow, 2 * M_PI * i / 100.));
            rot.transform(rpt, r);
            rot.transform(upt, u);
            s.add(shadow, r);
            sp.add(shadow, u);
            glTexCoord2f(0.6f, 0);
            prj.sVertex3(sp.x, sp.y, sp.z, mat);
            glTexCoord2f(1.f, 0);
            prj.sVertex3(s.x, s.y, s.z, mat);
        }
        glEnd();
        glDisable(GL_STENCIL_TEST);
    }

    public void update(long deltaTime, Navigator nav) {
        for (Planet iter : systemPlanets) {
            iter.updateTrail(nav);
            iter.update(deltaTime);
        }
    }

    /**
     * is a lunar eclipse close at hand?
     */
    boolean nearLunarEclipse(final Navigator nav, DefaultProjector prj) {
        Point3d e = getEarth().getEclipticPos();
        Point3d m = getMoon().getEclipticPos();
        Point3d mh = getMoon().getHeliocentricEclipticPos();
        Vector3d en = new Vector3d(e);
        en.normalize();
        Vector3d shadow = new Vector3d(en);
        shadow.scale(getLength(e) + getLength(m));
        double r_penumbra = shadow.length() * 702378.1 / (AU * getLength(e)) - 696000 / AU;
        Vector3d mdist = new Vector3d();
        mdist.sub(shadow, mh);
        if (mdist.length() > r_penumbra + 2000 / AU) return false;
        return true;
    }

    public List<String> listMatchingObjectsI18n(final String objPrefix, int maxNbItem) {
        List<String> result = new ArrayList<String>();
        if (maxNbItem == 0) return result;
        String objw = objPrefix.toUpperCase();
        for (Planet iter : systemPlanets) {
            String nameI18n = iter.getNameI18n();
            if (nameI18n.toUpperCase().startsWith(objw)) {
                result.add(nameI18n);
                if (result.size() == maxNbItem) return result;
            }
        }
        return result;
    }

    /**
     * Set/Get base planets display scaling factor
     */
    public void setScale(double scale) {
        Planet.setScale(scale);
    }

    /**
     * Set/Get base planets display scaling factor
     */
    public double getScale() {
        return Planet.getScale();
    }

    /**
     * Set/Get if Moon display is scaled
     */
    public void setFlagMoonScale(boolean b) {
        if (!b) getMoon().setSphereScale(1.f); else getMoon().setSphereScale(moonScale);
        flagMoonScale = b;
    }

    public boolean getFlagMoonScale() {
        return flagMoonScale;
    }

    /**
     * Set/Get Moon display scaling factor
     */
    public void setMoonScale(float f) {
        moonScale = f;
        if (flagMoonScale) getMoon().setSphereScale(moonScale);
    }

    public float getMoonScale() {
        return moonScale;
    }

    Planet getSun() {
        return sun;
    }

    public Planet getEarth() {
        return earth;
    }

    public Planet getMoon() {
        return moon;
    }

    public StelObject getSelected() {
        return selected;
    }

    private Planet sun;

    private Planet moon;

    private Planet earth;

    /**
     * he currently selected planet
     */
    private StelObject selected;

    /**
     * should be kept synchronized with star scale...
     */
    float object_scale;

    boolean flagMoonScale;

    /**
     * Moon scale value
     */
    float moonScale = 1.f;

    private SFontIfc _planetNameFontIfc;

    /**
     * Vector containing all the bodies of the system
     */
    private List<Planet> systemPlanets = new ArrayList<Planet>();

    /**
     * Pointers on created elliptical orbits
     */
    private final List<EllipticalOrbit> ellOrbits = new ArrayList<EllipticalOrbit>();

    /**
     * for lunar eclipses
     */
    private STexture texEarthShadow;

    boolean flagOrbits;

    boolean lightTravelTime;
}
