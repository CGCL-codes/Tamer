package client.fourmi;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import client.J3DClient;
import com.sun.j3d.utils.applet.MainFrame;
import common.fourmi.CaseFourmi;
import common.fourmi.Fourmi;
import common.fourmi.Ouvriere;
import common.fourmi.Pheromone;
import common.fourmi.PheromoneType;
import common.fourmi.PlaneteFourmi;
import common.fourmi.Reine;
import common.planete.base.Case;
import common.planete.fractal.CaseFractal;
import common.planete.fractal.F_atan_2;
import common.planete.fractal.Fonction;
import common.planete.fractal.PlaneteFractal;

public class J3DClientFourmi extends J3DClient implements KeyListener {

    int modeDefilement = 0;

    protected PlaneteFourmi planeteFourmi;

    ArrayList<GeometryArray> rivieres;

    /** Creates a new instance of J3DClient */
    public J3DClientFourmi(PlaneteFractal planete) {
        super(planete);
        planeteFourmi = (PlaneteFourmi) planete;
        rivieresGeometryArray();
        createDisplay();
        canvas3D.addKeyListener(this);
    }

    protected void endInitView() {
        Shape3D s3r = new Shape3D();
        for (GeometryArray ga : rivieres) {
            s3r.addGeometry(ga);
        }
        s3r.setAppearance(sphereAppear);
        mouseTransform.addChild(s3r);
    }

    public static void main(String[] args) {
        initLoggers();
        Fonction fonction = new F_atan_2();
        PlaneteFourmi planete = new PlaneteFourmi(64);
        planete.setFonction(fonction);
        planete.setGraineZeroAltitude(false);
        planete.setNbSources(1);
        planete.generate();
        J3DClientFourmi display = new J3DClientFourmi(planete);
        display.addKeyListener(display);
        MainFrame mf = new MainFrame(display, 800, 600);
        mf.addKeyListener(display);
    }

    protected void generatePlaneteGeometryArray() {
        casesGeometriesArray = new ArrayList<GeometryArray>();
        for (Case c : planete.getCases()) {
            CaseFourmi cf = (CaseFourmi) c;
            int n = c.getSommets().size() + 1;
            int stripCount[] = { n + 1 };
            Point3f points[] = new Point3f[n + 1];
            Color3f colors[] = new Color3f[n + 1];
            points[0] = c.getCentre();
            for (int i = 0; i < n - 1; i++) {
                points[i + 1] = c.getSommets().get(i);
            }
            points[n] = c.getSommets().get(0);
            GeometryArray ptmp = new TriangleFanArray(n + 1, TriangleFanArray.COORDINATES | TriangleFanArray.COLOR_3, stripCount);
            for (int i = 0; i < points.length; i++) {
                drawCaseFourmi(cf, ptmp);
            }
            ptmp.setCoordinates(0, points);
            casesGeometriesArray.add(ptmp);
        }
    }

    public void rivieresGeometryArray() {
        rivieres = new ArrayList<GeometryArray>(planeteFourmi.getNbSources());
        CaseFractal ctmp;
        int riviereSize;
        int index;
        int offset[] = new int[1];
        LineStripArray rtmp = null;
        ArrayList<CaseFractal> casesRiviere = new ArrayList<CaseFractal>();
        int numRiv = 0;
        for (CaseFractal source : planeteFourmi.getSources()) {
            casesRiviere.clear();
            System.out.println("Riviere " + numRiv);
            ctmp = source;
            while (ctmp != null) {
                casesRiviere.add(ctmp);
                if (!ctmp.isRiviereDraw()) {
                    ctmp.setRiviereDraw(true);
                    ctmp = ctmp.getRiviere();
                } else ctmp = null;
            }
            riviereSize = casesRiviere.size() + 1;
            System.out.println("taille = " + riviereSize);
            offset[0] = riviereSize;
            rtmp = new LineStripArray(riviereSize, LineStripArray.COORDINATES | LineStripArray.COLOR_3, offset);
            ctmp = source;
            index = 0;
            for (Case c : casesRiviere) {
                rtmp.setCoordinate(index, c.getCentre());
                rtmp.setColor(index, new Color3f(0.0f, 0.0f, 0.8f));
                index++;
            }
            rivieres.add(rtmp);
            numRiv++;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_NUMPAD0) {
            System.out.println("stepbystep !");
            planeteFourmi.tour();
        }
        for (CaseFractal caseFract : planeteFourmi.getTerres()) {
            CaseFourmi cf = (CaseFourmi) caseFract;
            if (cf.getFourmi() == null) {
                drawCaseFourmi(cf, casesGeometriesArray.get(cf.getId()));
            } else {
                drawFourmi(cf);
            }
        }
    }

    public void drawCaseFourmi(CaseFourmi cf, GeometryArray ga) {
        Color3f colors[] = new Color3f[cf.getAdj().size() + 2];
        for (int i = 0; i < colors.length; i++) {
            if (cf.getReserveNourriture() == 0) {
                Pheromone ph = cf.getPheromone(0, PheromoneType.NOURRITURE);
                if (ph != null && ph.getPuissance() > 0) {
                    colors[i] = new Color3f(0.0f, 0.3f, 0.3f + ((float) ph.getPuissance()) / 150.0f);
                } else {
                    ph = cf.getPheromone(0, PheromoneType.REINE);
                    if (ph != null && ph.getPuissance() > 0) {
                        colors[i] = new Color3f(0.3f + ((float) ph.getPuissance()) / 150.0f, 0.3f, 0.0f);
                    } else {
                        if (cf.getAltitude() == CaseFractal.NO_ALT) colors[i] = new Color3f(1.0f, 1.0f, 1.0f); else if (cf.getAltitude() < 0) colors[i] = new Color3f(0.0f, 0.0f, (0.5f - 0.5f * ((float) cf.getAltitude()) / CaseFractal.MIN_ALT)); else colors[i] = new Color3f(0.0f, 0.5f + ((float) cf.getAltitude()) / CaseFractal.MAX_ALT, 0.0f);
                    }
                }
            } else {
                colors[i] = new Color3f(0.3f, 0.3f + ((float) cf.getReserveNourriture()) / 100.0f, 0.8f);
            }
        }
        ga.setColors(0, colors);
    }

    public void drawFourmi(CaseFourmi cf) {
        Fourmi fourmi = cf.getFourmi();
        float bcolor;
        if (fourmi instanceof Ouvriere) {
            if (((Ouvriere) fourmi).getStock() > 0) bcolor = 0.2f; else bcolor = 0.0f;
        } else bcolor = 0.0f;
        GeometryArray ga = casesGeometriesArray.get(fourmi.getCaseActuelle().getId());
        ga.setColor(0, new Color3f(0.0f, 0.0f, bcolor));
        if (fourmi instanceof Reine) {
            for (int i = 0; i < fourmi.getCaseActuelle().getAdj().size() + 2; i++) ga.setColor(i, new Color3f(0.0f, 0.0f, bcolor));
        } else {
            int offset = fourmi.getPosPrec();
            int modulo = fourmi.getCaseActuelle().getAdj().size();
            int blackIndexFan1 = (offset + modulo - 1) % modulo + 1;
            int blackIndexFan2 = (offset) % modulo + 1;
            for (int index = 1; index < 7; index++) {
                if (index == blackIndexFan1 || index == blackIndexFan2) ga.setColor(index, new Color3f(0.0f, 0.0f, bcolor)); else ga.setColor(index, new Color3f(0.5f, 0.5f, 0.5f));
            }
            if (modulo == 5) {
                if (blackIndexFan1 == 6 || blackIndexFan2 == 6 || blackIndexFan1 == 1 || blackIndexFan2 == 1) ga.setColor(6, new Color3f(0.0f, 0.0f, bcolor)); else ga.setColor(6, new Color3f(0.5f, 0.5f, 0.5f));
            } else {
                if (6 == blackIndexFan1 || 6 == blackIndexFan2) ga.setColor(6, new Color3f(0.0f, 0.0f, bcolor)); else ga.setColor(6, new Color3f(0.5f, 0.5f, 0.5f));
                if (blackIndexFan1 == 7 || blackIndexFan2 == 7 || blackIndexFan1 == 1 || blackIndexFan2 == 1) ga.setColor(7, new Color3f(0.0f, 0.0f, bcolor)); else ga.setColor(7, new Color3f(0.5f, 0.5f, 0.5f));
            }
            drawCaseFourmi((CaseFourmi) fourmi.getCasePrecedente(), casesGeometriesArray.get(fourmi.getCasePrecedente().getId()));
        }
    }
}
