package shu.cms.colorformat.adapter;

import java.util.*;
import shu.cms.*;
import shu.cms.colorformat.adapter.TargetAdapter.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.devicemodel.lcd.*;
import shu.cms.lcd.*;
import shu.cms.lcd.LCDTargetBase.Number;
import shu.cms.colorformat.adapter.xls.AUOMeasureXLSAdapter;
import jxl.read.biff.*;
import java.io.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 * ��Jrgb��LCDModel, ���ͥXXYZ
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class LCDModelAdapter extends TargetAdapter {

    protected LCDModel model;

    protected LCDTargetBase.Number number;

    private boolean fromNumber;

    public LCDModelAdapter(LCDModel model, LCDTargetBase.Number number) {
        this.model = model;
        this.number = number;
        fromNumber = true;
        parsing();
    }

    public LCDModelAdapter(LCDModel model, List<RGB> rgbList) {
        this.model = model;
        this.rgbList = rgbList;
        fromNumber = false;
        parsing();
    }

    /**
   *
   * @return boolean
   * @todo M probeParsable
   */
    public boolean probeParsable() {
        return false;
    }

    protected List<RGB> rgbList;

    protected List<String> nameList;

    protected List<CIEXYZ> XYZList;

    protected void parsing() {
        if (fromNumber) {
            LCDTarget rgbTarget = LCDTarget.Instance.get(number);
            nameList = rgbTarget.filter.nameList();
            rgbList = rgbTarget.filter.rgbList();
        }
        int size = rgbList.size();
        XYZList = new ArrayList<CIEXYZ>(size);
        for (RGB rgb : rgbList) {
            XYZList.add(model.getXYZ(rgb, false));
        }
    }

    public static void main(String[] args) {
        ProfileColorSpaceModel pcsm = new ProfileColorSpaceModel(RGB.ColorSpace.sRGB);
        pcsm.produceFactor();
        try {
            AUOMeasureXLSAdapter measureAdapter = new AUOMeasureXLSAdapter("ColorList_all(871).xls");
            List<RGB> measureRGBList = measureAdapter.getRGBList();
            LCDModelAdapter modelAdapter = new LCDModelAdapter(pcsm, measureRGBList);
            LCDTarget target = LCDTarget.Instance.get(modelAdapter);
            for (Patch p : target.getPatchList()) {
                System.out.println(p);
            }
        } catch (BiffException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
   * getStyle
   *
   * @return Style
   */
    public Style getStyle() {
        return Style.RGBXYZ;
    }

    /**
   * getRGBList
   *
   * @return List
   */
    public List getRGBList() {
        return rgbList;
    }

    /**
   * getXYZList
   *
   * @return List
   */
    public List getXYZList() {
        return XYZList;
    }

    /**
   * getSpectraList
   *
   * @return List
   */
    public List getSpectraList() {
        throw new UnsupportedOperationException();
    }

    public List<Spectra> getReflectSpectraList() {
        throw new UnsupportedOperationException();
    }

    /**
   * getPatchNameList
   *
   * @return List
   */
    public List getPatchNameList() {
        return nameList;
    }

    /**
   * getFilename
   *
   * @return String
   */
    public String getFilename() {
        return model.getDescription();
    }

    /**
   * getAbsolutePath
   *
   * @return String
   */
    public String getAbsolutePath() {
        return model.getDescription();
    }

    /**
   * getFileNameExtension
   *
   * @return String
   */
    public String getFileNameExtension() {
        return null;
    }

    /**
   * getFileDescription
   *
   * @return String
   */
    public String getFileDescription() {
        return model.getDescription();
    }

    /**
   * estimateNumber
   *
   * @return Number
   */
    public Number estimateLCDTargetNumber() {
        return number;
    }

    public final boolean isInverseModeMeasure() {
        return false;
    }
}
