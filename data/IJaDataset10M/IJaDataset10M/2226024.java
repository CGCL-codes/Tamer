package jp.locky.locdisp;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LocUGDataLoader {

    static String defDir = "C:/Users/kawaguti/Doc2007/WIDE/WiL/�n���X/loaddata";

    static HashMap<String, Point2D.Double> UGoffset;

    static {
        UGoffset = new HashMap<String, Point2D.Double>();
        UGoffset.put("�G�X�J�n���X", new Point2D.Double(136.879456, 35.171051));
        UGoffset.put("�I�A�V�X�Q�P", new Point2D.Double(136.908630, 35.171978));
        UGoffset.put("�T�J�G�`�J", new Point2D.Double(136.904541, 35.170452));
        UGoffset.put("�T�����[�h�E�C�[�g�X�g���[�g�E�V���t�[�h�n���X", new Point2D.Double(136.883926, 35.170273));
        UGoffset.put("�e���~�i�E���C�`�J�n���X", new Point2D.Double(136.882446, 35.173145));
        UGoffset.put("�Z���g�����p�[�N�n���X", new Point2D.Double(136.907028, 35.174240));
        UGoffset.put("�~���R�n���X", new Point2D.Double(136.885971, 35.168915));
        UGoffset.put("���j���[���n���X", new Point2D.Double(136.883759, 35.172394));
        UGoffset.put("�X�̒n���X", new Point2D.Double(136.907486, 35.171860));
        UGoffset.put("���É������n���ʂ�", new Point2D.Double(136.880646, 35.171280));
    }

    public static int loadFileExtUG(String ugname, File fp, HashMap<String, WiFiLocExt> wf) {
        int ct = 0;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(fp.getAbsolutePath()));
            String ll;
            Point2D.Double offset;
            offset = UGoffset.get(ugname);
            if (offset == null) {
                System.out.println("Can't find offset of [" + ugname + "]!!");
            } else {
                System.out.println("Loading ..:" + ugname + ":" + offset.x + "," + offset.y);
                while ((ll = bf.readLine()) != null) {
                    WiFiLocExt wl = WiFiLocExt.parseLocDataUG(ugname, ll, fp, offset);
                    ct++;
                    if (wf.containsKey(wl.BSSID)) {
                        WiFiLocExt oldwl = wf.get(wl.BSSID);
                        if (oldwl.RSSI < wl.RSSI) {
                            wf.put(wl.BSSID, wl);
                        }
                    } else {
                        wf.put(wl.BSSID, wl);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ct;
    }

    public static void loadUGData(HashMap<String, WiFiLocExt> wf) {
        File fl = new File(defDir);
        File fls[] = fl.listFiles();
        int fc = 0;
        int ct = 0;
        for (int kk = 0; kk < fls.length; kk++) {
            String fnm = fls[kk].getName();
            if (fnm.endsWith(".txt")) {
                fc++;
                String dif[] = fnm.split("\\.");
                ct += loadFileExtUG(dif[0], fls[kk], wf);
            }
        }
        System.out.println("Loaded:File=" + fc + ":BSSID=" + ct);
        System.out.println("WiFiLoc=" + wf.size());
    }

    public static void main(String args[]) {
        HashMap<String, WiFiLocExt> wf = new HashMap<String, WiFiLocExt>();
        loadUGData(wf);
        CreateKML.createKMLFile(wf, "wldbext.kml");
    }
}
