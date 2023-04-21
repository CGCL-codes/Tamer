package vv.cms.lcd.calibrate.measured;

import java.util.*;
import shu.cms.*;
import shu.cms.colorformat.adapter.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.lcd.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class LCDTargetUtils {

    /**
   * �qlogoFilename��J��LCDTarget
   * @param logoFilename String
   * @return LCDTarget
   */
    public static final LCDTarget getLogoLCDTarget(String logoFilename) {
        return LCDTarget.Instance.getFromLogo(logoFilename);
    }

    /**
   * �qlogoFilename��J��LCDTarget, �B��RGB������number����Linear RGB
   * @param lcdTarget LCDTarget
   * @param number Number
   * @return LCDTarget
   */
    public static final LCDTarget getLCDTargetWithLinearRGB(LCDTarget lcdTarget, LCDTargetBase.Number number) {
        List<CIEXYZ> XYZList = lcdTarget.filter.XYZList();
        return getLCDTarget(XYZList, number);
    }

    /**
   * �NlogoFilename�����eŪ�X, ��O�H���`��0~255 rgb����.
   * �O���F�ҥ�w�g��JLUT���e����ܾ�.
   * @param logoFilename String
   * @return LCDTarget
   */
    public static final LCDTarget getLogoLCDTargetWithLinearRGB(String logoFilename) {
        LogoFileAdapter logo = new LogoFileAdapter(logoFilename);
        List<CIEXYZ> XYZList = logo.getXYZList();
        if (XYZList.size() != 256) {
            throw new IllegalArgumentException("logoFilename(" + logoFilename + ")'s XYZList.size() != 256");
        }
        return getLCDTarget(XYZList, LCDTargetBase.Number.Ramp256W);
    }

    /**
   * �Hnumber��rgb��XYZList���ͥXLCDTarget
   * @param XYZList List
   * @param number Number
   * @return LCDTarget
   */
    protected static final LCDTarget getLCDTarget(List<CIEXYZ> XYZList, LCDTarget.Number number) {
        List<RGB> rgbList = LCDTarget.Instance.getRGBList(number);
        List<Patch> patchList = Patch.Produce.XYZRGBPatches(XYZList, rgbList);
        LCDTarget target = LCDTarget.Instance.get(patchList, number, false);
        return target;
    }

    /**
   * �Hnumber��RGB����lcdTarget����RGB
   * @param lcdTarget LCDTarget
   * @param number Number
   * @return LCDTarget
   */
    protected static final LCDTarget getReplacedLCDTarget(LCDTarget lcdTarget, LCDTarget.Number number) {
        return getLCDTarget(lcdTarget.filter.XYZList(), number);
    }

    /**
   * �NrgbArray���N��LCDTarget�̪�Patch, �����@�ӷs��Patch List
   * @param target LCDTarget
   * @param rgbArray RGB[]
   * @return List
   */
    protected static final List<Patch> getReplacedPatchList(LCDTarget target, RGB[] rgbArray) {
        List<Patch> patchList = Patch.Produce.copyOf(target.getPatchList());
        int size = patchList.size();
        for (int x = 0; x < size; x++) {
            Patch p = patchList.get(x);
            RGB rgb = rgbArray[x];
            Patch.Operator.setRGB(p, rgb);
        }
        return patchList;
    }

    /**
   * �Ntarget�̭���RGB������number�̪�RGB
   * @param target LCDTarget
   * @param number Number
   * @return List
   */
    protected static final List<Patch> getReplacedPatchList(LCDTarget target, LCDTarget.Number number) {
        List<RGB> rgbList = LCDTarget.Instance.getRGBList(number);
        RGB[] rgbArray = new RGB[rgbList.size()];
        return getReplacedPatchList(target, rgbArray);
    }
}
