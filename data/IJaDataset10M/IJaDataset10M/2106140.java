package org.rhwlab.image;

import gov.noaa.pmel.sgt.LineAttribute;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Light;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Raster;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.rhwlab.acetree.AceTree;
import org.rhwlab.snight.NucleiMgr;
import org.rhwlab.snight.Nucleus;
import org.rhwlab.tree.Cell;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import qdxml.DocHandler;
import qdxml.QDParser;

/**
 * @author biowolp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Image3D2Z extends MouseAdapter implements ActionListener, Runnable, DocHandler {

    private PickCanvas iPickCanvas;

    private AceTree iAceTree;

    private NucleiMgr iNucleiMgr;

    public BranchGroup iBG;

    private Background iBackground;

    private JFrame iFrame;

    private SimpleUniverse iUniverse;

    private Canvas3D iCanvas;

    private String iTitle;

    boolean iNewConstruction;

    Thread iThread;

    boolean iSaveInProcess;

    static boolean iSaveImage;

    boolean fakeit1;

    boolean fakeit2;

    JTextField iAngle;

    JTextField iScale;

    JLabel iPick;

    private int iXA;

    private int iYA;

    private float iZA;

    private Transform3D iRotate;

    private TransformGroup iRotGroup;

    private TransformGroup iTranslateGroup;

    private Matrix4d iMatrix;

    private JPanel iImagePanel;

    private JPanel iControlPanel;

    private JTabbedPane iTabbedPane;

    public SublineageDisplayProperty[] iDispProps2Z;

    public PropertiesTab3 iPT3;

    private int iMinRed;

    private int iMaxRed;

    private boolean iUseExpression;

    JTextField iAngX;

    JTextField iAngXInc;

    JButton iXUp;

    JButton iXDn;

    JTextField iAngY;

    JTextField iAngYInc;

    JButton iYUp;

    JButton iYDn;

    JTextField iAngZ;

    JTextField iAngZInc;

    JButton iZUp;

    JButton iZDn;

    JTextField iPosIncr;

    JTextField iPos;

    JButton iPIn;

    JButton iPOut;

    JButton iRestore;

    JButton iUndoButton;

    Vector iUndo;

    JButton iLoadButton;

    JButton iSaveButton;

    String iCurrentRotDir;

    int iLineageCount;

    JButton iSaveImageButton;

    String iSaveImageAsDir;

    String iLastSaveAsName;

    BranchGroup iBGT;

    Indicator3D iIndicator;

    public class Nuclei3D {

        private boolean iShowIt;

        private boolean iTransparent;

        private Vector iSisterList;

        private Hashtable iNucleiHash;

        public Nuclei3D() {
            println("Nuclei3D, " + System.currentTimeMillis());
            AceTree a = iAceTree;
            iBG = new BranchGroup();
            Color3f eColor = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f sColor = new Color3f(1.0f, 1.0f, 1.0f);
            Material m = new Material(eColor, eColor, sColor, sColor, 100.0f);
            m.setLightingEnable(true);
            Appearance app = new Appearance();
            app.setMaterial(m);
            iSisterList = prepareSortedList();
            addNucleiViaSisterList();
        }

        private Appearance setColor(Color3f color) {
            Color3f eColor = new Color3f(0.0f, 0.0f, 0.0f);
            Color3f sColor = color;
            Material m = new Material(eColor, eColor, sColor, sColor, 100.0f);
            m.setLightingEnable(true);
            Appearance app = new Appearance();
            app.setMaterial(m);
            return app;
        }

        private Appearance getLineageColor(int k) {
            Appearance app = null;
            switch(k) {
                case 0:
                    app = setColor(ColorConstants.red);
                    break;
                case 1:
                    app = setColor(ColorConstants.blue);
                    break;
                case 2:
                    app = setColor(ColorConstants.green);
                    break;
                case 3:
                    app = setColor(ColorConstants.yellow);
                    break;
                case 4:
                    app = setColor(ColorConstants.cyan);
                    break;
                case 5:
                    app = setColor(ColorConstants.magenta);
                    break;
                case 6:
                    app = setColor(ColorConstants.pink);
                    break;
                case 7:
                    app = setColor(ColorConstants.gray);
                    break;
                case 8:
                    app = setColor(ColorConstants.white);
                    break;
                default:
                    app = null;
            }
            return app;
        }

        private Appearance getExpressionColor(Nucleus n) {
            Cell.setMinRed(iMinRed);
            Cell.setMaxRed(iMaxRed);
            int k = Cell.getDiscrete(n.rweight);
            Color color = Cell.getTheColor(k);
            Color3f c3f = new Color3f(color);
            Appearance app = setColor(c3f);
            return app;
        }

        private Vector copyNuclei(Vector nuclei) {
            Vector newNuclei = new Vector();
            Enumeration e = nuclei.elements();
            Nucleus n = null;
            while (e.hasMoreElements()) {
                n = (Nucleus) e.nextElement();
                newNuclei.add(n.copy());
            }
            Collections.sort(newNuclei, n);
            return newNuclei;
        }

        private void showSisters() {
            Vector sorted = prepareSortedList();
            iSisterList = sorted;
            for (int i = 0; i < sorted.size(); i++) {
                if (i == sorted.size() - 1) break;
                String first = (String) sorted.get(i);
                String second = (String) sorted.get(i + 1);
                if (areSisters(first, second)) {
                    println("sisters, " + first + CS + second);
                    i++;
                }
            }
        }

        private Vector prepareSortedList() {
            iNucleiHash = new Hashtable();
            Vector sortedList = new Vector();
            Vector catsAndDogs = new Vector();
            NucleiMgr nucleiMgr = iAceTree.getNucleiMgr();
            int time = iAceTree.getImageTime() + iAceTree.getTimeInc();
            Vector nuclei = (Vector) nucleiMgr.getNucleiRecord().elementAt(time - 1);
            for (int i = 0; i < nuclei.size(); i++) {
                Nucleus n = (Nucleus) nuclei.get(i);
                if (n.status == Nucleus.NILLI) continue;
                iNucleiHash.put(n.identity, n);
                if (inSISTERS(n.identity) >= 0) {
                    catsAndDogs.add(n.identity);
                    continue;
                }
                sortedList.add(n.identity);
            }
            Collections.sort(sortedList);
            addCatsAndDogs(sortedList, catsAndDogs);
            return sortedList;
        }

        private void addCatsAndDogs(Vector sortedList, Vector catsAndDogs) {
            while (catsAndDogs.size() > 0) {
                String name = (String) catsAndDogs.remove(0);
                sortedList.add(name);
                String sister = getSister(name);
                if (sister.length() > 0 && catsAndDogs.contains(sister)) {
                    sortedList.add(sister);
                    catsAndDogs.remove(sister);
                }
            }
        }

        private boolean areSisters(String first, String second) {
            boolean rtn = false;
            boolean[] ans = new boolean[2];
            ans[0] = ans[1] = false;
            specialCaseSisters(first, second, ans);
            if (ans[0]) {
                return ans[1];
            }
            String s = "";
            s = first.substring(0, first.length() - 1);
            int k = second.indexOf(s);
            return (k == 0 && first.length() == second.length());
        }

        private int inSISTERS(String name) {
            for (int i = 0; i < SISTERS.length; i++) {
                if (name.equals(SISTERS[i])) return i;
            }
            return -1;
        }

        private String getSister(String name) {
            String sister = "";
            for (int i = 0; i < SISTERS.length; i++) {
                if (name.equals(SISTERS[i])) {
                    if (i % 2 == 0) {
                        sister = SISTERS[i + 1];
                    } else {
                        sister = SISTERS[i - 1];
                    }
                    break;
                }
            }
            return sister;
        }

        private final String[] SISTERS = { "E", "MS", "AB", "P1", "EMS", "P2", "C", "P3", "D", "P4" };

        private void specialCaseSisters(String first, String second, boolean[] ans) {
            int firstLoc = inSISTERS(first);
            int secondLoc = inSISTERS(second);
            ans[0] = firstLoc >= 0 || secondLoc >= 0;
            if (firstLoc % 2 == 0) {
                ans[1] = (secondLoc == firstLoc + 1);
            } else ans[1] = (secondLoc == firstLoc - 1);
        }

        private void addNucleiViaSisterList() {
            println("addNucleiViaSisterList, " + System.currentTimeMillis());
            iShowIt = true;
            iTransparent = false;
            NucleiMgr nucleiMgr = iAceTree.getNucleiMgr();
            int time = iAceTree.getImageTime() + iAceTree.getTimeInc();
            Vector nuclei = (Vector) nucleiMgr.getNucleiRecord().elementAt(time - 1);
            nuclei = copyNuclei(nuclei);
            getCenter(nuclei);
            int width = ImageWindow.cImageWidth;
            int height = ImageWindow.cImageHeight;
            float scale = width / 2;
            float xoff = iXA;
            float yoff = iYA;
            float zoff = iZA;
            float nx, ny, nz, nr;
            while (iSisterList.size() > 0) {
                String name = (String) iSisterList.remove(0);
                if (iSisterList.size() == 0) {
                    addOne(name, false);
                    break;
                }
                String next = (String) iSisterList.get(0);
                if (areSisters(name, next)) {
                    iSisterList.remove(0);
                    addSisters(name, next);
                } else {
                    addOne(name, false);
                }
            }
        }

        private void addSisters(String first, String second) {
            Nucleus n1 = addOne(first, true);
            boolean showing = iShowIt;
            Nucleus n2 = addOne(second, true);
            iShowIt = iShowIt && showing;
            if (iShowIt && !iTransparent) {
                println("addSisters, " + iShowIt + CS + first + CS + second);
                addConnector(n1, n2);
            }
        }

        private void addConnector(Nucleus n1, Nucleus n2) {
            if (!iShowIt) return;
            LineArray connector = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
            float xf, yf, z, rf;
            int width = ImageWindow.cImageWidth;
            int height = ImageWindow.cImageHeight;
            float scale = width / 2;
            float xoff = iXA;
            float yoff = iYA;
            float zoff = iZA;
            float nx, ny, nz, nr;
            Nucleus n = n1;
            xf = (float) ((n.x - xoff) / scale);
            yf = (float) ((n.y - yoff) / scale);
            yf = -yf;
            z = (float) iNucleiMgr.getZPixRes() * (n.z - zoff) / scale;
            z = -z;
            connector.setCoordinate(0, new Point3f(xf, yf, z));
            n = n2;
            xf = (float) ((n.x - xoff) / scale);
            yf = (float) ((n.y - yoff) / scale);
            yf = -yf;
            z = (float) iNucleiMgr.getZPixRes() * (n.z - zoff) / scale;
            z = -z;
            connector.setCoordinate(1, new Point3f(xf, yf, z));
            LineAttributes la = new LineAttributes();
            la.setLineWidth(5);
            connector.setColor(0, ColorConstants.magenta);
            connector.setColor(1, ColorConstants.magenta);
            Appearance app = new Appearance();
            app.setLineAttributes(la);
            int k = getLineageNumber(n1.identity);
            if (k >= iDispProps2Z.length - 2) {
                connector.setColor(0, ColorConstants.white);
                connector.setColor(1, ColorConstants.white);
            }
            iBG.addChild(new Shape3D(connector, app));
        }

        private Nucleus addOne(String name, boolean sister) {
            Nucleus n = (Nucleus) iNucleiHash.get(name);
            if (n == null) {
                println("addOne, " + name + CS + sister);
            }
            float xf, yf, z, rf;
            int width = ImageWindow.cImageWidth;
            int height = ImageWindow.cImageHeight;
            float scale = width / 2;
            float xoff = iXA;
            float yoff = iYA;
            float zoff = iZA;
            float nx, ny, nz, nr;
            xf = (float) ((n.x - xoff) / scale);
            yf = (float) ((n.y - yoff) / scale);
            yf = -yf;
            z = (float) iNucleiMgr.getZPixRes() * (n.z - zoff) / scale;
            z = -z;
            rf = (float) ((n.size / 2) / scale);
            Appearance app = new Appearance();
            TransparencyAttributes tran = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.0f);
            TransparencyAttributes tran2 = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.5f);
            int k = getLineageNumber(n.identity);
            iShowIt = true;
            iTransparent = false;
            if (k < iDispProps2Z.length - 2) app = getLineageColor(k); else {
                int m = iDispProps2Z[iDispProps2Z.length - 2].iLineageNum;
                switch(m) {
                    case 0:
                        iShowIt = false;
                        break;
                    case 1:
                        iTransparent = true;
                        app.setTransparencyAttributes(tran2);
                        break;
                    default:
                        app = getLineageColor(8);
                }
            }
            if (iShowIt && app != null) {
                if (sister) rf = Math.min(n.size / 2, 10) / scale;
                iBG.addChild(makeNamedSphere(n.identity, xf, yf, z, rf, app));
            }
            return n;
        }

        private void addNuclei() {
            int count = 0;
            int falsePos = 0;
            int falseNeg = 0;
            NucleiMgr nucleiMgr = iAceTree.getNucleiMgr();
            int time = iAceTree.getImageTime() + iAceTree.getTimeInc();
            Vector nuclei = (Vector) nucleiMgr.getNucleiRecord().elementAt(time - 1);
            nuclei = copyNuclei(nuclei);
            getCenter(nuclei);
            Nucleus n = null;
            float xf, yf, z, rf;
            int width = ImageWindow.cImageWidth;
            int height = ImageWindow.cImageHeight;
            float scale = width / 2;
            float xoff = iXA;
            float yoff = iYA;
            float zoff = iZA;
            float nx, ny, nz, nr;
            for (int j = 0; j < nuclei.size(); j++) {
                n = (Nucleus) nuclei.elementAt(j);
                if (n.status < 0) continue;
                xf = (float) ((n.x - xoff) / scale);
                yf = (float) ((n.y - yoff) / scale);
                yf = -yf;
                z = (float) iNucleiMgr.getZPixRes() * (n.z - zoff) / scale;
                z = -z;
                rf = (float) ((n.size / 2) / scale);
                Appearance app = new Appearance();
                TransparencyAttributes tran = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.0f);
                TransparencyAttributes tran2 = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.5f);
                tran.setTransparency(0.8f);
                int k = getLineageNumber(n.identity);
                iShowIt = true;
                if (k < iDispProps2Z.length - 2) app = getLineageColor(k); else {
                    int m = iDispProps2Z[iDispProps2Z.length - 2].iLineageNum;
                    switch(m) {
                        case 0:
                            iShowIt = false;
                            break;
                        case 1:
                            app.setTransparencyAttributes(tran);
                            break;
                        default:
                            app = getLineageColor(8);
                    }
                }
                if (iUseExpression) {
                    app = getExpressionColor(n);
                    if (n.rweight < iMinRed) {
                        app = setColor(ColorConstants.white);
                        app.setTransparencyAttributes(tran);
                        iShowIt = true;
                    }
                } else if (iDispProps2Z[iDispProps2Z.length - 3].iName.indexOf("Special") == 0) {
                    app = special(n);
                }
                if (iShowIt && app != null) {
                    iBG.addChild(makeNamedSphere(n.identity, xf, yf, z, rf, app));
                    if (app.getTransparencyAttributes() != tran) count++;
                }
            }
        }

        private boolean inSCAList(Nucleus n) {
            String[] theList = { "ABaraaappaa", "ABalpaappa", "ABaraaappap", "ABaraaapaaa", "ABaraaappp", "MSaaaaaa", "ABalpaapppa", "ABprpapppp", "ABalpaapppp" };
            for (int i = 0; i < theList.length; i++) {
                if (n.identity.equals(theList[i])) return true;
            }
            return false;
        }

        private Appearance special(Nucleus n) {
            TransparencyAttributes faint = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f);
            TransparencyAttributes invisible = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.f);
            TransparencyAttributes solid = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.f);
            Appearance app = null;
            String name = n.identity;
            Appearance appRest = null;
            app = appRest;
            iShowIt = true;
            if (name.indexOf("E") == 0) {
                app = setColor(ColorConstants.yellow);
                app.setTransparencyAttributes(solid);
            }
            if (name.indexOf("MSaa") == 0) {
                app = setColor(ColorConstants.magenta);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("MSaaaaaa") == 0 || name.indexOf("MSaappp") == 0) {
                    app = appRest;
                }
            }
            if (name.indexOf("MSpa") == 0) {
                app = setColor(ColorConstants.cyan);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("MSpapp") == 0) {
                    app = appRest;
                }
            }
            if (name.indexOf("ABalpaaa") == 0 || name.indexOf("ABalpaapa") == 0 || name.indexOf("ABalpapp") == 0) {
                app = setColor(ColorConstants.pink);
                ;
                app.setTransparencyAttributes(solid);
            }
            if (name.indexOf("ABaraaaa") == 0 || name.indexOf("ABaraaapa") == 0) {
                app = setColor(ColorConstants.blue);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("ABaraaapaaa") == 0) {
                    app = appRest;
                }
            }
            if (name.indexOf("ABaraap") == 0) {
                app = setColor(ColorConstants.blue);
                app.setTransparencyAttributes(solid);
            }
            if (name.indexOf("ABarapa") == 0) {
                app = setColor(ColorConstants.blue);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("ABarapapapa") == 0) {
                    app = appRest;
                }
            }
            if (app == appRest) {
                iShowIt = true;
                app = setColor(ColorConstants.white);
                app.setTransparencyAttributes(faint);
            }
            return app;
        }

        private Appearance special(Nucleus n, boolean bogus) {
            TransparencyAttributes faint = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f);
            TransparencyAttributes invisible = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.f);
            TransparencyAttributes solid = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.f);
            Appearance app = null;
            String name = n.identity;
            Appearance appRest = null;
            app = appRest;
            iShowIt = true;
            if (name.indexOf("E") == 0) {
                app = setColor(ColorConstants.green);
                app.setTransparencyAttributes(solid);
            }
            if (name.indexOf("MSaa") == 0) {
                app = setColor(ColorConstants.magenta);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("MSaaaaaa") == 0 || name.indexOf("MSaappp") == 0) {
                    app = appRest;
                }
            }
            if (name.indexOf("MSpa") == 0) {
                app = setColor(ColorConstants.cyan);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("MSpapp") == 0) {
                    app = appRest;
                }
            }
            if (name.indexOf("ABalpaaa") == 0 || name.indexOf("ABalpaapa") == 0 || name.indexOf("ABalpapp") == 0) {
                app = setColor(ColorConstants.red);
                app.setTransparencyAttributes(solid);
            }
            if (name.indexOf("ABaraaaa") == 0 || name.indexOf("ABaraaapa") == 0) {
                app = setColor(ColorConstants.blue);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("ABaraaapaaa") == 0) {
                    app = appRest;
                }
            }
            if (name.indexOf("ABaraap") == 0) {
                app = setColor(ColorConstants.blue);
                app.setTransparencyAttributes(solid);
            }
            if (name.indexOf("ABarapa") == 0) {
                app = setColor(ColorConstants.blue);
                app.setTransparencyAttributes(solid);
                if (name.indexOf("ABarapapapa") == 0) {
                    app = appRest;
                }
            }
            if (app == appRest) {
                iShowIt = true;
                app = setColor(ColorConstants.white);
                app.setTransparencyAttributes(faint);
            }
            return app;
        }

        private void getCenter(Vector nuclei) {
            iXA = 0;
            iYA = 0;
            iZA = 0.f;
            int count = 0;
            Enumeration e = nuclei.elements();
            while (e.hasMoreElements()) {
                Nucleus n = (Nucleus) e.nextElement();
                if (n.status == Nucleus.NILLI) continue;
                iXA += n.x;
                iYA += n.y;
                iZA += n.z;
                count++;
            }
            iXA /= count;
            iYA /= count;
            iZA /= count;
        }

        private TransformGroup makeNamedSphere(String name, float x, float y, float z, float r, Appearance a) {
            Transform3D translate = new Transform3D();
            translate.set(new Vector3f(x, y, z));
            NamedSphere sph = new NamedSphere(name, r, a);
            TransformGroup tg = new TransformGroup(translate);
            tg.addChild(sph);
            return tg;
        }

        private TransformGroup makeSphere(float x, float y, float z, float r, Appearance a) {
            Transform3D translate = new Transform3D();
            translate.set(new Vector3f(x, y, z));
            Sphere sph = new Sphere(r, a);
            TransformGroup tg = new TransformGroup(translate);
            tg.addChild(sph);
            return tg;
        }

        public BranchGroup getBG() {
            return iBG;
        }
    }

    public Image3D2Z(AceTree aceTree, String title) {
        iAceTree = aceTree;
        iNucleiMgr = iAceTree.getNucleiMgr();
        iFrame = new JFrame(title);
        iTitle = title;
        iNewConstruction = true;
        iMinRed = MINRED;
        iMaxRed = MAXRED;
        iUseExpression = false;
        iFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WinEventMgr wem = new WinEventMgr();
        iFrame.addWindowListener(wem);
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        iCanvas = new Canvas3D(config);
        iCanvas.setSize(ImageWindow.cImageWidth, ImageWindow.cImageHeight);
        iUniverse = new SimpleUniverse(iCanvas);
        iUniverse.getViewingPlatform().setNominalViewingTransform();
        ViewingPlatform viewingPlatform = iUniverse.getViewingPlatform();
        iTranslateGroup = viewingPlatform.getViewPlatformTransform();
        iMatrix = new Matrix4d();
        Transform3D t3d = new Transform3D();
        iTranslateGroup.getTransform(t3d);
        Matrix4d m4d = new Matrix4d();
        buildOutUI();
        iTabbedPane = new JTabbedPane();
        iTabbedPane.addTab("Image", null, iImagePanel, "View 3D image");
        Object dispProps = iAceTree.getDispProps3D2Z();
        if (dispProps == null) iAceTree.setDispProps3D2Z((Object) getDisplayProps());
        iDispProps2Z = (SublineageDisplayProperty[]) iAceTree.getDispProps3D2Z();
        iPT3 = new PropertiesTab3(this);
        iControlPanel = iPT3.getPanel();
        iTabbedPane.addTab("Properties", null, iControlPanel, "Set color scheme");
        iFrame.getContentPane().add(iTabbedPane);
        iCanvas.addMouseListener(this);
        iFrame.pack();
        iFrame.show();
        iUndo = new Vector();
        insertContent(iTitle);
        iSaveImageAsDir = "";
        iLastSaveAsName = "";
    }

    private void buildOutUI() {
        iPick = new JLabel("pick");
        iImagePanel = new JPanel();
        iImagePanel.setLayout(new BorderLayout());
        iImagePanel.add(iCanvas, "Center");
        JPanel secondPanel = new JPanel(new BorderLayout());
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.PAGE_AXIS));
        JPanel rotatePanels = new JPanel();
        rotatePanels.setLayout(new GridLayout(0, 1));
        JPanel rotatePanel = new JPanel();
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        rotatePanel.add(iPick);
        rotatePanels.add(rotatePanel);
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        rotatePanel.add(new JLabel("angX"));
        iAngXInc = new JTextField("30", 5);
        iAngX = new JTextField("0", 10);
        iXUp = new JButton("up");
        iXDn = new JButton("dn");
        rotatePanel.add(iAngXInc);
        rotatePanel.add(iAngX);
        rotatePanel.add(iXUp);
        rotatePanel.add(iXDn);
        iXUp.addActionListener(this);
        iXDn.addActionListener(this);
        rotatePanels.add(rotatePanel);
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        rotatePanel.add(new JLabel("angY"));
        iAngYInc = new JTextField("30", 5);
        iAngY = new JTextField("0", 10);
        iYUp = new JButton("up");
        iYDn = new JButton("dn");
        rotatePanel.add(iAngYInc);
        rotatePanel.add(iAngY);
        rotatePanel.add(iYUp);
        rotatePanel.add(iYDn);
        iYUp.addActionListener(this);
        iYDn.addActionListener(this);
        rotatePanels.add(rotatePanel);
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        rotatePanel.add(new JLabel("angZ"));
        iAngZInc = new JTextField("30", 5);
        iAngZ = new JTextField("0", 10);
        iZUp = new JButton("up");
        iZDn = new JButton("dn");
        rotatePanel.add(iAngZInc);
        rotatePanel.add(iAngZ);
        rotatePanel.add(iZUp);
        rotatePanel.add(iZDn);
        iZUp.addActionListener(this);
        iZDn.addActionListener(this);
        rotatePanels.add(rotatePanel);
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        rotatePanel.add(new JLabel("Pos"));
        Transform3D t3d = new Transform3D();
        iTranslateGroup.getTransform(t3d);
        t3d.get(iMatrix);
        iPosIncr = new JTextField("0.2", 5);
        iPos = new JTextField(fmt1(iMatrix.m23), 10);
        iPIn = new JButton("in");
        iPOut = new JButton("out");
        rotatePanel.add(iPosIncr);
        rotatePanel.add(iPos);
        rotatePanel.add(iPIn);
        rotatePanel.add(iPOut);
        iPIn.addActionListener(this);
        iPOut.addActionListener(this);
        rotatePanels.add(rotatePanel);
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        iRestore = new JButton("restore");
        rotatePanel.add(iRestore);
        iRestore.addActionListener(this);
        iUndoButton = new JButton("undo");
        iUndoButton.addActionListener(this);
        rotatePanel.add(iUndoButton);
        rotatePanels.add(rotatePanel);
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        iLoadButton = new JButton("load from file");
        rotatePanel.add(iLoadButton);
        iLoadButton.addActionListener(this);
        iSaveButton = new JButton("save to file");
        iSaveButton.addActionListener(this);
        rotatePanel.add(iSaveButton);
        rotatePanels.add(rotatePanel);
        iCurrentRotDir = ".";
        rotatePanel = new JPanel();
        rotatePanel.setLayout(new GridLayout(1, 0));
        iSaveImageButton = new JButton("saveImageAs");
        rotatePanel.add(iSaveImageButton);
        iSaveImageButton.addActionListener(this);
        rotatePanel.add(iSaveImageButton);
        rotatePanels.add(rotatePanel);
        newPanel.add(rotatePanels);
        secondPanel.add(newPanel, "West");
        iIndicator = new Indicator3D();
        secondPanel.add(iIndicator, "East");
        iImagePanel.add(secondPanel, "South");
    }

    private void reportDispProps() {
        for (int i = 0; i < iDispProps2Z.length; i++) {
            System.out.println("dispProp: " + i + CS + iDispProps2Z[i].iName + CS + iDispProps2Z[i].iLineageNum);
        }
    }

    public void updateDisplayedTab() {
        iTabbedPane.setSelectedIndex(0);
        insertContent(iTitle);
        System.out.println("updateDisplayedTab called");
    }

    private void applyTrans(double incr, char axis) {
        int angle = 0;
        Transform3D t3d = new Transform3D();
        switch(axis) {
            case 'x':
                t3d.rotX(incr);
                iUndo.add(new Trans(t3d, incr, 'x'));
                angle = Integer.parseInt(iAngX.getText());
                angle += Math.round(Math.toDegrees(incr));
                angle = angle % 360;
                iAngX.setText(String.valueOf(angle));
                break;
            case 'y':
                t3d.rotY(incr);
                iUndo.add(new Trans(t3d, incr, 'y'));
                angle = Integer.parseInt(iAngY.getText());
                angle += Math.round(Math.toDegrees(incr));
                angle = angle % 360;
                iAngY.setText(String.valueOf(angle));
                break;
            case 'z':
                t3d.rotZ(incr);
                iUndo.add(new Trans(t3d, incr, 'z'));
                angle = Integer.parseInt(iAngZ.getText());
                angle += Math.round(Math.toDegrees(incr));
                angle = angle % 360;
                iAngZ.setText(String.valueOf(angle));
                break;
        }
        iRotate.mul(t3d);
        iIndicator.apply(t3d);
        iRotGroup.setTransform(iRotate);
    }

    private void handleRotatePanel(Object o) {
        int angle = 0;
        Transform3D t3d = new Transform3D();
        double incrDeg = 30;
        double incr = Math.toRadians(incrDeg);
        if (o == iXUp || o == iXDn) {
            incrDeg = Integer.parseInt(iAngXInc.getText());
            incr = Math.toRadians(incrDeg);
            if (o == iXDn) {
                incr *= -1;
            }
            applyTrans(incr, 'x');
            return;
        }
        if (o == iYUp || o == iYDn) {
            incrDeg = Integer.parseInt(iAngYInc.getText());
            incr = Math.toRadians(incrDeg);
            if (o == iYDn) {
                incr *= -1;
            }
            applyTrans(incr, 'y');
            return;
        }
        if (o == iZUp || o == iZDn) {
            incrDeg = Integer.parseInt(iAngZInc.getText());
            incr = Math.toRadians(incrDeg);
            if (o == iZDn) {
                incr *= -1;
            }
            applyTrans(incr, 'z');
            return;
        }
        if (o == iPIn || o == iPOut) {
            double pos = Double.parseDouble(iPos.getText());
            double posInc = Double.parseDouble(iPosIncr.getText());
            if (o == iPIn) posInc *= -1;
            pos += posInc;
            iPos.setText(fmt1(pos));
            iTranslateGroup.getTransform(t3d);
            Matrix4d m4d = new Matrix4d();
            t3d.get(m4d);
            Matrix4d mincr = new Matrix4d();
            mincr.m23 = posInc;
            m4d.add(mincr);
            t3d.set(m4d);
            iTranslateGroup.setTransform(t3d);
            t3d.set(mincr);
            iUndo.add(new Trans(new Transform3D(), posInc, 'p'));
        }
        if (o == iRestore) {
            iRotate.mulInverse(iRotate);
            iRotGroup.setTransform(iRotate);
            iUndo.clear();
            iIndicator.restore();
            iAngX.setText("0");
            iAngY.setText("0");
            iAngZ.setText("0");
            t3d.set(iMatrix);
            iTranslateGroup.setTransform(t3d);
            iPos.setText(fmt1(iMatrix.m23));
            return;
        }
        if (o == iUndoButton && iUndo.size() > 0) {
            Trans t = (Trans) iUndo.remove(iUndo.size() - 1);
            Transform3D t3 = t.iT3d;
            if (t3 != null) {
                double angInc = Math.toDegrees(t.iAngInc);
                switch(t.iAxis) {
                    case 'x':
                        angle = Integer.parseInt(iAngX.getText());
                        angle -= angInc;
                        iAngX.setText(String.valueOf(angle));
                        handleRotateUndo(t3);
                        break;
                    case 'y':
                        angle = Integer.parseInt(iAngY.getText());
                        angle -= angInc;
                        iAngY.setText(String.valueOf(angle));
                        handleRotateUndo(t3);
                        break;
                    case 'z':
                        angle = Integer.parseInt(iAngZ.getText());
                        angle -= angInc;
                        iAngZ.setText(String.valueOf(angle));
                        handleRotateUndo(t3);
                        break;
                    case 'p':
                        iTranslateGroup.getTransform(t3d);
                        Matrix4d m4d = new Matrix4d();
                        t3d.get(m4d);
                        m4d.m23 -= t.iAngInc;
                        t3d.set(m4d);
                        iTranslateGroup.setTransform(t3d);
                        double pos = Double.parseDouble(iPos.getText());
                        pos -= t.iAngInc;
                        iPos.setText(fmt1(pos));
                        break;
                }
            }
            iRotGroup.setTransform(iRotate);
        }
    }

    private void handleRotateUndo(Transform3D t3) {
        t3.invert();
        iRotate.mul(t3);
        iIndicator.apply(t3);
    }

    private void saveRotations() {
        File file = null;
        JFileChooser fileChooser = new JFileChooser(iCurrentRotDir);
        int returnVal = fileChooser.showSaveDialog(iAceTree);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            System.out.println("Save command cancelled by user.");
            return;
        }
        iCurrentRotDir = file.getParent();
        PrintWriter pw = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            pw = new PrintWriter(fos, true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        pw.println("<?xml version='1.0' encoding='utf-8'?>");
        pw.println();
        pw.println("<rotations>");
        for (int i = 0; i < iUndo.size(); i++) {
            Trans t = (Trans) iUndo.get(i);
            StringBuffer sb = new StringBuffer();
            sb.append("<rotation ");
            sb.append("radians=\"" + t.iAngInc + "\" ");
            sb.append("axis=\"" + t.iAxis + "\"/>");
            pw.println(sb.toString());
        }
        pw.println("</rotations>");
    }

    private void loadRotations() {
        File file = null;
        JFileChooser fileChooser = new JFileChooser(iCurrentRotDir);
        int returnVal = fileChooser.showOpenDialog(iAceTree);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            System.out.println("Save command cancelled by user.");
            return;
        }
        iCurrentRotDir = file.getParent();
        try {
            FileReader fr = new FileReader(file);
            QDParser.parse(this, fr);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startElement(String tag, Hashtable h) throws Exception {
        if (tag.equals("rotation")) {
            String incrs = (String) h.get("radians");
            String axiss = (String) h.get("axis");
            double incr = Double.parseDouble(incrs);
            char axis = axiss.charAt(0);
            applyTrans(incr, axis);
        } else if (tag.equals("lineage")) {
            String name = (String) h.get("name");
            String color = (String) h.get("color");
            iDispProps2Z[iLineageCount].iName = name;
            iDispProps2Z[iLineageCount].iLineageNum = iPT3.getColorNumber(color);
            iLineageCount++;
        }
    }

    private class Trans {

        public Transform3D iT3d;

        public double iAngInc;

        public char iAxis;

        public Trans(Transform3D t, double a, char axis) {
            iT3d = t;
            iAngInc = a;
            iAxis = axis;
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == iXUp || o == iXDn || o == iYUp || o == iYDn || o == iZUp || o == iZDn || o == iPIn || o == iPOut || o == iRestore || o == iUndoButton) {
            handleRotatePanel(o);
            return;
        }
        if (o == iLoadButton) {
            loadRotations();
        } else if (o == iSaveButton) {
            saveRotations();
        } else if (o == iSaveImageButton) {
            saveImageAs();
        }
    }

    public void insertContent(String title) {
        while (iSaveInProcess) ;
        iTitle = title;
        iFrame.setTitle(iTitle);
        if (iBG != null) iBG.detach();
        iBG = createSceneGraph();
        iUniverse.addBranchGraph(iBG);
        iPickCanvas = new PickCanvas(iCanvas, iBG);
        iPickCanvas.setMode(PickCanvas.BOUNDS);
        if (iSaveImage) {
            iThread = new Thread(this);
            iThread.start();
        }
    }

    public BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_DETACH);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f bgColor = new Color3f(0.3f, 0.3f, 0.3f);
        Color3f lColor1 = new Color3f(1f, 1f, 1f);
        Vector3d lPos1 = new Vector3d(0.0, 0.0, 2.0);
        Vector3f lDirect1 = new Vector3f(lPos1);
        lDirect1.negate();
        Light lgt1 = new DirectionalLight(lColor1, lDirect1);
        lgt1.setInfluencingBounds(bounds);
        root.addChild(lgt1);
        int m = iDispProps2Z[iDispProps2Z.length - 1].iLineageNum;
        switch(m) {
            case 0:
                bgColor = new Color3f(.7f, .7f, .7f);
                break;
            case 1:
                bgColor = new Color3f(.3f, .3f, .3f);
                break;
            default:
                bgColor = new Color3f(.1f, .1f, .1f);
                break;
        }
        iBackground = new Background(bgColor);
        iBackground.setApplicationBounds(bounds);
        root.addChild(iBackground);
        TransformGroup objRotate = new TransformGroup();
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        new Nuclei3D();
        iBG.compile();
        objRotate.addChild(iBG);
        TransformGroup initRotGroup = new TransformGroup();
        Transform3D initRotate = new Transform3D();
        NucleiMgr nucMgr = iAceTree.getNucleiMgr();
        int ap = nucMgr.getParameters().apInit;
        int dv = nucMgr.getParameters().dvInit;
        int lr = nucMgr.getParameters().lrInit;
        if (ap == -1) {
            Transform3D apt = new Transform3D();
            apt.rotZ(Math.PI);
            initRotate.mul(apt);
            ap = -ap;
            dv = -dv;
        }
        if (dv == -1) {
            Transform3D dvt = new Transform3D();
            dvt.rotX(Math.PI);
            initRotate.mul(dvt);
            dv = -dv;
            lr = -lr;
        }
        initRotGroup.setTransform(initRotate);
        initRotGroup.addChild(objRotate);
        if (iRotate == null) iRotate = new Transform3D();
        iRotGroup = new TransformGroup(iRotate);
        iRotGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        iRotGroup.addChild(initRotGroup);
        root.addChild(iRotGroup);
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objRotate);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        root.addChild(myMouseRotate);
        root.compile();
        return root;
    }

    public void mouseClicked(MouseEvent e) {
        iPickCanvas.setShapeLocation(e);
        PickResult[] results = iPickCanvas.pickAll();
        String name = getPickedNucleusNames(results);
        iPick.setText("you picked: " + name);
    }

    private String getPickedNucleusNames(PickResult[] results) {
        String s = "none";
        Vector v = new Vector();
        if (results != null) {
            for (int i = (results.length - 1); i >= 0; i--) {
                Primitive p = (Primitive) results[i].getNode(PickResult.PRIMITIVE);
                if (p != null) {
                    String pname = p.getClass().getName();
                    if (pname.indexOf("NamedSphere") >= 0) {
                        s = ((NamedSphere) p).iName;
                        v.add(0, s);
                    }
                }
            }
        }
        if (v.size() == 0) return "none";
        Enumeration e = v.elements();
        s = "";
        while (e.hasMoreElements()) {
            if (s.length() > 0) s += CS;
            s += (String) e.nextElement();
        }
        return s;
    }

    public class NamedSphere extends Sphere {

        String iName;

        public NamedSphere(String name, float r, Appearance a) {
            super(r, a);
            iName = name;
        }
    }

    public class SublineageDisplayProperty {

        public String iName;

        public int iLineageNum;

        public SublineageDisplayProperty(String name, int lineageNum) {
            iName = name;
            iLineageNum = lineageNum;
        }
    }

    public class PropertiesTab3 implements ActionListener {

        JPanel iPanel;

        SublineageUI[] iSubUI;

        JTextField iMinRedField;

        JTextField iMaxRedField;

        JCheckBox iUseExprBox;

        Image3D2Z iParent;

        public PropertiesTab3(Image3D2Z parent) {
            iParent = parent;
            Border blackline = BorderFactory.createLineBorder(Color.black);
            iSubUI = new SublineageUI[iDispProps2Z.length];
            iPanel = new JPanel();
            iPanel.setLayout(new BorderLayout());
            iPanel.setBorder(blackline);
            JPanel lineagePanel = new JPanel();
            JPanel dummyPanel = new JPanel();
            JPanel topPart = new JPanel();
            topPart.setLayout(new GridLayout(1, 2));
            lineagePanel.setLayout(new GridLayout(0, 1));
            lineagePanel.setBorder(blackline);
            topPart.add(lineagePanel);
            topPart.add(dummyPanel);
            JPanel[] testPanel = new JPanel[iDispProps2Z.length];
            JTextField textField;
            JComboBox cb;
            JPanel labelPanel = new JPanel();
            JLabel sublineage = new JLabel("sublineage");
            JLabel color = new JLabel("color");
            labelPanel.setLayout(new GridLayout(1, 2));
            labelPanel.add(sublineage);
            labelPanel.add(color);
            lineagePanel.add(labelPanel);
            for (int i = 0; i < iDispProps2Z.length; i++) {
                iSubUI[i] = new SublineageUI(i);
                lineagePanel.add(iSubUI[i].iPanel);
            }
            lineagePanel.setMaximumSize(new Dimension(200, 200));
            iPanel.add(topPart, BorderLayout.NORTH);
            JPanel botPart = new JPanel();
            botPart.setLayout(new GridLayout(3, 1));
            iPanel.add(botPart, BorderLayout.CENTER);
            JPanel filePanel = new JPanel();
            filePanel.setLayout(new GridLayout(1, 2));
            JButton load = new JButton("Load from file");
            JButton save = new JButton("Save to file");
            filePanel.add(load);
            filePanel.add(save);
            load.addActionListener(this);
            save.addActionListener(this);
            botPart.add(filePanel);
            JPanel jp = new JPanel(new FlowLayout());
            jp.setBorder(blackline);
            iMinRedField = new JTextField(String.valueOf(iMinRed), 7);
            iMaxRedField = new JTextField(String.valueOf(iMaxRed), 7);
            iUseExprBox = new JCheckBox("Use Expression", iUseExpression);
            jp.add(iUseExprBox);
            jp.add(new JLabel("minRed"));
            jp.add(iMinRedField);
            jp.add(new JLabel("maxRed"));
            jp.add(iMaxRedField);
            botPart.add(jp);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 3));
            JButton reset = new JButton("Reset");
            JButton apply = new JButton("Apply");
            JButton cancel = new JButton("Cancel");
            buttonPanel.add(apply);
            reset.addActionListener(this);
            apply.addActionListener(this);
            cancel.addActionListener(this);
            buttonPanel.add(reset);
            buttonPanel.add(apply);
            buttonPanel.add(cancel);
            botPart.add(buttonPanel);
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Cancel")) {
                updateDisplayedTab();
            } else if (command.equals("Reset")) {
                iDispProps2Z = getDisplayProps();
                for (int i = 0; i < iDispProps2Z.length; i++) {
                    iSubUI[i].iTF.setText(iDispProps2Z[i].iName);
                    iSubUI[i].iCB.setSelectedIndex(iDispProps2Z[i].iLineageNum);
                }
                iMinRed = 25000;
                iMaxRed = 100000;
                iUseExpression = false;
            } else if (command.equals("Apply")) {
                for (int i = 0; i < iDispProps2Z.length; i++) {
                    String name = iSubUI[i].iTF.getText();
                    if (name.length() == 0) name = "-";
                    int num = iSubUI[i].iCB.getSelectedIndex();
                    iDispProps2Z[i].iName = name;
                    iDispProps2Z[i].iLineageNum = num;
                }
                iMinRed = Integer.parseInt(iMinRedField.getText());
                iMaxRed = Integer.parseInt(iMaxRedField.getText());
                iUseExpression = iUseExprBox.isSelected();
                iAceTree.setDispProps3D2Z(iDispProps2Z);
                updateDisplayedTab();
            } else if (command.equals("Load from file")) {
                System.out.println("Load from file");
                loadFromFile();
            } else if (command.equals("Save to file")) {
                System.out.println("Save to file");
                saveToFile();
            }
        }

        private void saveToFile() {
            JFileChooser fileChooser = new JFileChooser(iCurrentRotDir);
            int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal != JFileChooser.APPROVE_OPTION) return;
            File file = fileChooser.getSelectedFile();
            iCurrentRotDir = file.getParent();
            System.out.println("saveToFile: " + file);
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream(fileChooser.getSelectedFile()), true);
                pw.println("<?xml version='1.0' encoding='utf-8'?>");
                pw.println();
                pw.println("<lineages>");
                for (int i = 0; i < iDispProps2Z.length - 2; i++) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("<lineage ");
                    sb.append("name=\"" + iDispProps2Z[i].iName + "\" ");
                    sb.append("color=\"" + COLORS[iDispProps2Z[i].iLineageNum] + "\"/>");
                    pw.println(sb.toString());
                }
                pw.println("</lineages>");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }

        private void loadFromFile() {
            JFileChooser fileChooser = new JFileChooser(iCurrentRotDir);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal != JFileChooser.APPROVE_OPTION) return;
            File file = fileChooser.getSelectedFile();
            iCurrentRotDir = file.getParent();
            iLineageCount = 0;
            try {
                FileReader fr = new FileReader(file);
                QDParser.parse(iParent, fr);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = iLineageCount; i < iDispProps2Z.length - 2; i++) {
                iDispProps2Z[i].iName = "";
                iDispProps2Z[i].iLineageNum = 0;
            }
            update();
        }

        public int getColorNumber(String colorName) {
            int k = 0;
            for (int i = 0; i < COLORS.length; i++) {
                if (colorName.equals(COLORS[i])) return i;
            }
            return k;
        }

        public void update() {
            for (int i = 0; i < iDispProps2Z.length - 2; i++) {
                iSubUI[i].iTF.setText(iDispProps2Z[i].iName);
                iSubUI[i].iCB.setSelectedIndex(iDispProps2Z[i].iLineageNum);
            }
        }

        public class SublineageUI {

            public JPanel iPanel;

            public JTextField iTF;

            public JComboBox iCB;

            public SublineageUI(int i) {
                iPanel = new JPanel();
                iPanel.setLayout(new GridLayout(1, 2));
                iTF = new JTextField(iDispProps2Z[i].iName, WIDTH);
                String[] list;
                list = COLORS;
                if (i == iDispProps2Z.length - 2) list = TRANSPROPS; else if (i == iDispProps2Z.length - 1) list = GRAYDEPTH;
                iCB = new JComboBox(list);
                iCB.setSelectedIndex(iDispProps2Z[i].iLineageNum);
                iPanel.add(iTF);
                iPanel.add(iCB);
                iPanel.setMaximumSize(new Dimension(200, 10));
            }
        }

        public JPanel getPanel() {
            return iPanel;
        }

        private String[] COLORS = { "red", "blue", "green", "yellow", "cyan", "magenta", "pink", "gray", "white", "omit" };

        private String[] TRANSPROPS = { "omit", "transparent", "white" };

        private String[] GRAYDEPTH = { "white", "light gray    ", "dark gray" };

        private static final int WIDTH = 15;
    }

    private SublineageDisplayProperty[] getDisplayProps() {
        SublineageDisplayProperty[] dispProps = { new SublineageDisplayProperty("ABa", 0), new SublineageDisplayProperty("ABp", 1), new SublineageDisplayProperty("C", 5), new SublineageDisplayProperty("D", 6), new SublineageDisplayProperty("E", 2), new SublineageDisplayProperty("MS", 4), new SublineageDisplayProperty("P", 3), new SublineageDisplayProperty("polar", 7), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("", 2), new SublineageDisplayProperty("other", 2), new SublineageDisplayProperty("background", 1) };
        return dispProps;
    }

    private int getLineageNumber(String name) {
        if (name.indexOf("Z") >= 0) name = "P";
        int num = iDispProps2Z.length;
        for (int i = 0; i < iDispProps2Z.length; i++) {
            if (name.indexOf(iDispProps2Z[i].iName) >= 0) {
                num = iDispProps2Z[i].iLineageNum;
                break;
            }
        }
        return num;
    }

    public void run() {
        iSaveInProcess = true;
        int k = 1000;
        if (iNewConstruction) {
            k = 5000;
            iNewConstruction = false;
        }
        try {
            Thread.sleep(k);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        saveImage();
    }

    public static void setSaveImageState(boolean saveIt) {
        iSaveImage = saveIt;
    }

    public void saveImage() {
        String saveDir = iAceTree.iImgWin.getSaveImageDirectory();
        if (saveDir == null) {
            iAceTree.iImgWin.cancelSaveOperations();
            iSaveImage = false;
            return;
        }
        Rectangle screenRect = iFrame.getBounds();
        int topAdjust = 23;
        int y = screenRect.y;
        screenRect.y += topAdjust;
        int height = screenRect.height;
        screenRect.height -= topAdjust;
        String title = saveDir + "/";
        Robot robot = null;
        try {
            robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            title += iTitle + "." + IMAGETYPE;
            ImageIO.write(image, IMAGETYPE, new File(title));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("file: " + title + " written");
        iSaveInProcess = false;
    }

    public void saveImageAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(400, 300));
        fileChooser.setCurrentDirectory(new File(iSaveImageAsDir));
        fileChooser.setSelectedFile(new File(iLastSaveAsName));
        String path = "";
        int returnVal = fileChooser.showSaveDialog(iAceTree);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getPath();
            iSaveImageAsDir = file.getParent();
            iLastSaveAsName = file.getName() + "x";
        } else {
            System.out.println("Save command cancelled by user.");
        }
        Rectangle screenRect = iFrame.getBounds();
        int topAdjust = 23;
        int y = screenRect.y;
        screenRect.y += topAdjust;
        int height = screenRect.height;
        screenRect.height -= topAdjust;
        Robot robot = null;
        try {
            robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            ImageIO.write(image, IMAGETYPE, new File(path + "." + IMAGETYPE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("file: " + path + " written");
        iSaveInProcess = false;
    }

    public BufferedImage getBufferedImage() {
        int width = (int) (iCanvas.getSize().getWidth());
        int height = (int) (iCanvas.getSize().getHeight());
        int cursorType = iCanvas.getCursor().getType();
        iCanvas.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        GraphicsContext3D ctx = iCanvas.getGraphicsContext3D();
        Raster ras = new Raster(new Point3f(-1f, -1f, -1f), Raster.RASTER_COLOR, 0, 0, width, height, new ImageComponent2D(ImageComponent.FORMAT_RGB, new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)), null);
        ctx.readRaster(ras);
        BufferedImage img = ras.getImage().getImage();
        iCanvas.setCursor(new Cursor(cursorType));
        return img;
    }

    private class WinEventMgr extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            iFrame.dispose();
            iAceTree.image3DOff();
        }
    }

    private static final String CS = ", ", IMAGETYPE = "jpeg";

    private Color3f[] BACKGROUNDS = { new Color3f(1.f, 1.f, 1.f), new Color3f(0.3f, 0.3f, 0.3f), new Color3f(0.1f, 0.1f, 0.1f) };

    private static final int MINRED = 25000, MAXRED = 100000, SPECIAL = 1;

    public static void main(String[] args) {
    }

    private static void println(String s) {
        System.out.println(s);
    }

    private static final DecimalFormat DF1 = new DecimalFormat("####.##");

    private static final DecimalFormat DF4 = new DecimalFormat("####.####");

    private static String fmt1(double x) {
        return DF1.format(x);
    }

    public void startDocument() throws Exception {
    }

    public void endDocument() throws Exception {
    }

    public void text(String str) throws Exception {
    }

    public void endElement(String tag) throws Exception {
    }
}
