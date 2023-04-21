package jpatch.control.importer;

import java.io.*;
import java.util.*;
import javax.vecmath.*;
import jpatch.boundary.*;
import jpatch.control.*;
import jpatch.control.edit.AtomicChangePatchMaterial;
import jpatch.entity.*;

public class AnimationMasterImport implements ModelImporter {

    private static final int UNDEF = 0;

    private static final int MESH = 1;

    private static final int PATCHES = 2;

    private static final int GROUP = 3;

    private HashMap mapHashCp = new HashMap();

    private HashMap mapControlPoint = new HashMap();

    private ArrayList lstCandidateFivePointPatch = new ArrayList();

    private ArrayList lstGroup = new ArrayList();

    private ArrayList lstGroupName = new ArrayList();

    private HashMap mapGroupMaterials = new HashMap();

    private int iGroup = 0;

    public final String importModel(OLDModel model, String filename) {
        ArrayList lstHashMesh = new ArrayList();
        BufferedReader brFile;
        String strLine;
        String[] astrPart;
        int iMode = UNDEF;
        try {
            brFile = new BufferedReader(new FileReader(filename));
            while ((strLine = brFile.readLine()) != null) {
                switch(iMode) {
                    case UNDEF:
                        if (strLine.equals("[MESH]")) {
                            iMode = MESH;
                            lstHashMesh = new ArrayList();
                        }
                        if (strLine.equals("[PATCHES]")) {
                            iMode = PATCHES;
                        }
                        if (strLine.equals("[GROUP]")) {
                            iMode = GROUP;
                            iGroup++;
                        }
                        break;
                    case MESH:
                        if (strLine.equals("[ENDMESH]")) {
                            iMode = UNDEF;
                        }
                        if (strLine.startsWith("Version=")) {
                            astrPart = strLine.split("=");
                            if (!astrPart[1].equals("2")) {
                                System.err.println("unknown mesh version");
                            }
                        }
                        if (strLine.startsWith("Splines=")) {
                            astrPart = strLine.split("=");
                            int iSplines = (new Integer(astrPart[1])).intValue();
                            for (int s = 0; s < iSplines; s++) {
                                ArrayList lstSpline = new ArrayList();
                                strLine = brFile.readLine();
                                if (strLine.startsWith("CPs=")) {
                                    astrPart = strLine.split("=");
                                    int iCPs = (new Integer(astrPart[1])).intValue();
                                    for (int c = 0; c < iCPs; c++) {
                                        strLine = brFile.readLine();
                                        astrPart = strLine.split("\\s");
                                        long lFlags = (new Long(astrPart[0])).longValue();
                                        int iWeld = (new Integer(astrPart[1])).intValue();
                                        int iNum = (new Integer(astrPart[2])).intValue();
                                        float fHookPos = 0;
                                        if (HashCp.isHook(lFlags)) {
                                            fHookPos = (new Float(astrPart[3])).floatValue();
                                        }
                                        strLine = brFile.readLine();
                                        int iWeldedTo = -1;
                                        Point3f p3Position = new Point3f();
                                        if (HashCp.isWeld(iWeld)) {
                                            iWeldedTo = (new Integer(strLine)).intValue();
                                        } else {
                                            astrPart = strLine.split("\\s");
                                            p3Position.x = (new Float(astrPart[0])).floatValue();
                                            p3Position.y = (new Float(astrPart[1])).floatValue();
                                            p3Position.z = -(new Float(astrPart[2])).floatValue();
                                        }
                                        strLine = brFile.readLine();
                                        astrPart = strLine.split("\\s");
                                        float fInAlpha;
                                        float fInGamma = 0f;
                                        float fInMagnitude = 1f;
                                        fInAlpha = (new Float(astrPart[0])).floatValue();
                                        if (astrPart.length == 3) {
                                            fInGamma = (new Float(astrPart[1])).floatValue();
                                            fInMagnitude = (new Float(astrPart[2])).floatValue();
                                        }
                                        strLine = brFile.readLine();
                                        astrPart = strLine.split("\\s");
                                        float fOutAlpha;
                                        float fOutGamma = 0f;
                                        float fOutMagnitude = 1f;
                                        fOutAlpha = (new Float(astrPart[0])).floatValue();
                                        if (astrPart.length == 3) {
                                            fOutGamma = (new Float(astrPart[1])).floatValue();
                                            fOutMagnitude = (new Float(astrPart[2])).floatValue();
                                        }
                                        HashCp hashCp = new HashCp(lFlags, iWeld, iNum, fHookPos, p3Position, iWeldedTo, fInAlpha, fInGamma, fInMagnitude, fOutAlpha, fOutGamma, fOutMagnitude);
                                        lstSpline.add(hashCp);
                                        Integer key = new Integer(iNum);
                                        mapHashCp.put(key, hashCp);
                                        mapControlPoint.put(key, new OLDControlPoint(p3Position));
                                    }
                                }
                                lstHashMesh.add(lstSpline);
                            }
                        }
                        break;
                    case (PATCHES):
                        if (strLine.equals("[ENDPATCHES]")) {
                            iMode = UNDEF;
                        }
                        if (strLine.startsWith("Version=")) {
                            astrPart = strLine.split("=");
                            if (!astrPart[1].equals("3")) {
                                System.err.println("unknown patches version!");
                            }
                        }
                        astrPart = strLine.split("\\s");
                        if (astrPart.length == 11) {
                            OLDControlPoint[] acp5pp = new OLDControlPoint[5];
                            for (int cp = 0; cp < 5; cp++) {
                                int hcp = (new Integer(astrPart[cp])).intValue();
                                Integer key = new Integer(getHeadKey(hcp));
                                acp5pp[cp] = (OLDControlPoint) mapControlPoint.get(key);
                            }
                            lstCandidateFivePointPatch.add(acp5pp);
                        }
                        break;
                    case (GROUP):
                        if (strLine.equals("[ENDGROUP]")) {
                            iMode = UNDEF;
                        }
                        if (strLine.startsWith("Name=")) {
                            astrPart = strLine.split("=");
                            lstGroupName.add(astrPart[1]);
                        }
                        if (strLine.startsWith("DiffuseColor=")) {
                            astrPart = strLine.split("=");
                            String[] rgb = astrPart[1].split("\\s");
                            float b = (new Float(rgb[0])).floatValue();
                            float g = (new Float(rgb[1])).floatValue();
                            float r = (new Float(rgb[2])).floatValue();
                            if (r > 1 || g > 1 || b > 1) {
                                float R = r;
                                float G = g;
                                float B = b;
                                r = B / 256;
                                g = G / 256;
                                b = R / 256;
                            }
                            OLDMaterial material = new OLDMaterial();
                            material.setColor(new Color3f(r, g, b));
                            mapGroupMaterials.put(new Integer(iGroup), material);
                            model.addMaterial(material);
                        }
                        if (strLine.startsWith("Count=")) {
                            astrPart = strLine.split("=");
                            int iPoints = (new Integer(astrPart[1])).intValue();
                            int[] aiPoints = new int[iPoints];
                            for (int p = 0; p < iPoints; p++) {
                                strLine = brFile.readLine();
                                int cp = (new Integer(strLine)).intValue();
                                aiPoints[p] = cp;
                            }
                            lstGroup.add(aiPoints);
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error while importing Animation:Master model: " + e.getMessage();
        }
        model.addCandidateFivePointPatchList(lstCandidateFivePointPatch);
        for (int s = 0; s < lstHashMesh.size(); s++) {
            ArrayList lstSpline = (ArrayList) lstHashMesh.get(s);
            OLDControlPoint cpLast = null;
            for (int c = 0; c < lstSpline.size(); c++) {
                HashCp hashCp = (HashCp) lstSpline.get(c);
                Integer key = new Integer(hashCp.getNum());
                OLDControlPoint cp = (OLDControlPoint) mapControlPoint.get(key);
                if (c == 0) {
                    model.addCurve(cp);
                } else {
                    cp.appendTo(cpLast);
                }
                if (hashCp.isWeld() && !hashCp.isHook()) {
                    key = new Integer(getHeadKey(hashCp.getNum()));
                    cp.attachTo((OLDControlPoint) mapControlPoint.get(key));
                }
                if (hashCp.isLoop()) {
                    OLDControlPoint start = cp.getStart();
                    cp.setNext(start);
                    start.setPrev(cp);
                    start.setLoop(true);
                }
                cpLast = cp;
            }
        }
        for (int s = 0; s < lstHashMesh.size(); s++) {
            ArrayList lstSpline = (ArrayList) lstHashMesh.get(s);
            for (int c = 0; c < lstSpline.size(); c++) {
                HashCp hashCp = (HashCp) lstSpline.get(c);
                Integer key = new Integer(hashCp.getNum());
                OLDControlPoint cp = (OLDControlPoint) mapControlPoint.get(key);
                cp.setInMagnitude(hashCp.getInMagnitude());
                cp.setOutMagnitude(hashCp.getOutMagnitude());
                if (hashCp.isHook()) {
                    key = new Integer(getHookEndKey(hashCp.getNum()));
                    OLDControlPoint hook = ((OLDControlPoint) mapControlPoint.get(key)).addHook(hashCp.getHookPos(), model);
                    cp.attachTo(hook);
                }
                if (hashCp.isSmooth()) {
                    cp.setMode(OLDControlPoint.AM_SMOOTH);
                } else {
                    cp.setMode(OLDControlPoint.PEAK);
                }
            }
        }
        model.computePatches();
        for (int group = 0; group < lstGroup.size(); group++) {
            int[] aiPoints = (int[]) lstGroup.get(group);
            ArrayList pointList = new ArrayList();
            for (int p = 0; p < aiPoints.length; p++) {
                Integer key = new Integer(aiPoints[p]);
                OLDControlPoint cp = (OLDControlPoint) mapControlPoint.get(key);
                pointList.add(cp.getHead());
            }
            OLDSelection selection = new OLDSelection(pointList);
            selection.setName((String) lstGroupName.get(group));
            model.addSelection(selection);
            OLDMaterial material = (OLDMaterial) mapGroupMaterials.get(new Integer(group + 1));
            if (material != null) {
                for (Iterator it = model.getPatchSet().iterator(); it.hasNext(); ) {
                    Patch patch = (Patch) it.next();
                    if (patch.isSelected(selection)) patch.setMaterial(material);
                }
                material.setName(selection.getName());
            }
        }
        return "";
    }

    private int getHeadKey(int key) {
        HashCp hcp = (HashCp) mapHashCp.get(new Integer(key));
        if (hcp.isWeld()) {
            return getHeadKey(hcp.getWeldedTo());
        } else return key;
    }

    private int getHookEndKey(int key) {
        HashCp hcp = (HashCp) mapHashCp.get(new Integer(key));
        if (hcp.isWeld() && hcp.isHook()) {
            return getHookEndKey(hcp.getWeldedTo());
        } else return key;
    }
}
