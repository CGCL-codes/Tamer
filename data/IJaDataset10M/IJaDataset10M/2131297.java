package shu.cms.plot;

import java.util.*;
import shu.cms.colorspace.ColorSpace;
import shu.cms.colorspace.depend.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 * �i�Ψӵe�U�ؤ��S�w����m�Ŷ�, �u�n�w�q�n�PRGB�۹����ഫ�᪺��m�Ŷ�,
 *  GamutPlot�N�i�Hø�X
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public abstract class RGB2ColorSpaceTransfer {

    /**
   * �ഫ�X�PRGB��������m�Ŷ�
   * @param rgb RGB
   * @return ColorSpace
   */
    public ColorSpace getColorSpace(RGB rgb) {
        ColorSpace colorSpace = _getColorSpace(rgb);
        return getColorSpaceByTransferFilter(colorSpace);
    }

    public abstract ColorSpace _getColorSpace(RGB rgb);

    public abstract RGB getRGB(double[] colorspaceValues);

    public void addTransferFilter(ColorSpaceTransfer transfer) {
        transferList.add(transfer);
    }

    private ColorSpace getColorSpaceByTransferFilter(ColorSpace colorSpace) {
        ColorSpace result = colorSpace;
        for (ColorSpaceTransfer transfer : transferList) {
            result = transfer.transfer(result);
        }
        return result;
    }

    private List<ColorSpaceTransfer> transferList = new LinkedList<ColorSpaceTransfer>();
}
