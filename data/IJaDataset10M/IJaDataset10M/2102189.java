package shu.cms.lcd.calibrate.parameter;

import java.util.*;
import shu.cms.colorspace.depend.*;

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
public class ColorProofParameter implements Parameter {

    /**
   * gamma�վ㪺�Ͷ�
   */
    public Gamma gamma = Gamma.Custom;

    /**
   * �ۭqgamma��gamma�ƭ�
   */
    public double customGamma = 2.2;

    /**
   * �ۭqGamma���u
   */
    public double[] customCurve;

    /**
   * �ե����̤p���
   */
    public RGBBase.MaxValue calibrateBits = RGBBase.MaxValue.Int10Bit;

    /**
   * ic��bits��
   */
    public RGBBase.MaxValue icBits = RGBBase.MaxValue.Int10Bit;

    /**
   * ��X��cpcode��T��
   */
    public RGBBase.MaxValue outputBits = RGBBase.MaxValue.Double255;

    public static enum Format {

        VastView, AUO
    }

    /**
   * ��X��cpcode�ɮ׮榡
   */
    public Format outputFileFormat = Format.AUO;

    /**
   * ����I, ����I����~�}�l���(�N�O����I-1�}�l���), �Ӥ��O����I�����N�}�l���.
   */
    public int turnCode = 50;

    /**
   * ��������骺pattern��code���j
   */
    public int grayInterval = 8;

    /**
   * ����CCT���u���覡
   */
    public CCTCalibrate cctCalibrate = CCTCalibrate.IPT;

    /**
   * CCT���u�A���_�I
   */
    public int cctAdaptiveStart = 3;

    public double getTolerance() {
        return calibrateBits.getStepIn255() / 4.;
    }

    public GammaBy gammaBy = GammaBy.W;

    public static enum GammaBy {

        G, W
    }

    public static enum Gamma {

        /**
     * �̷ӭ�l��gamma����
     */
        Native, /**
     * �̷ӭ�l��gamma���ʶȰ��@�u�ʤ�Ҫ��վ�
     */
        Scale, /**
     * �q��l��Gamma����X�̱���gamma���
     */
        Smooth, /**
     * �ۭqgamma���
     */
        Custom, /**
     * �̷�sRGB gamma�ե�
     */
        sRGB, /**
     * DICOM GSDF
     */
        GSDF, /**
     * �ۭqGammaCurve
     */
        CustomCurve, /**
     * ��wG code, calibrate ByG�~�i�ϥ�
     */
        GCode
    }

    /**
   *
   * <p>Title: Colour Management System</p>
   *
   * <p>Description: a Colour Management System by Java</p>
   * ����CCT���覡
   *
   * <p>Copyright: Copyright (c) 2008</p>
   *
   * <p>Company: skygroup</p>
   *
   * @author skyforce
   * @version 1.0
   */
    public static enum CCTCalibrate {

        /**
     * �q��Ŧ��u����¦�A�@�վ�Ӳ���
     */
        Corrected, /**
     * �bu'v'�Ŷ��HCIEDE00�����ܤƦӲ���
     */
        uvpByDE00, /**
     * �bu'v'�Ŷ��HIPT�����ܤƦӲ���
     */
        uvpByIPT, /**
     * �bIPT�Ŷ������ܤƦӲ���
     */
        IPT, /**
     * �bCIECAM02�Ŷ������ܤƦӲ���
     */
        CIECAM02
    }

    /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        switch(gamma) {
            case Custom:
                buf.append("Gamma: Custom(" + customGamma + ")");
                break;
            case CustomCurve:
                buf.append("Gamma: CustomCurve" + Arrays.toString(customCurve));
                break;
            default:
                buf.append("Gamma: " + gamma);
        }
        buf.append(" (GammaBy: " + gammaBy + ")\n");
        buf.append("\nCalibrateBits: " + calibrateBits);
        buf.append("\nicBits: " + icBits);
        buf.append("\nturnCode: " + turnCode);
        buf.append("\ngrayInterval: " + grayInterval);
        buf.append("\nCCTCalibrate: " + cctCalibrate);
        buf.append("\ncctAdaptiveStart: " + cctAdaptiveStart);
        buf.append("\nTolerance: " + this.getTolerance());
        buf.append("\nkeepBlackPoint: " + keepBlackPoint);
        buf.append("\nrunCount: " + runCount);
        return buf.toString();
    }

    /**
   * �̷t�I�O����
   */
    public boolean keepBlackPoint = true;

    /**
   * �ե�������
   */
    public int runCount = 1;

    public RGB[] gCodeArray;

    /**
   * �`�gCPCode, �ΨӴ��N��model���X�����G
   */
    public RGB[] injectedCPCodeArray;
}
