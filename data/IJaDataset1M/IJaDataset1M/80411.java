package org.gvsig.raster.grid.filter.statistics;

import org.gvsig.raster.dataset.IBuffer;

/**
 * Proceso del filtro de recorte de colas aplicado a im�genes double
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TailTrimDoubleFilter extends TailTrimFilter {

    public TailTrimDoubleFilter() {
    }

    public void pre() {
        super.pre();
        sampleDec = new double[raster.getBandCount()][nSamples];
        result = new double[raster.getBandCount()][2];
    }

    public void process(int col, int line) throws InterruptedException {
        for (int iBand = 0; iBand < raster.getBandCount(); iBand++) sampleDec[iBand][count] = raster.getElemDouble(line, col, iBand);
        count++;
    }

    public int getInRasterDataType() {
        return IBuffer.TYPE_DOUBLE;
    }

    public int getOutRasterDataType() {
        return IBuffer.TYPE_DOUBLE;
    }

    public Object getResult(String name) {
        if (name.equals("raster")) return this.raster;
        return null;
    }

    public void post() {
        super.post();
        for (int i = 0; i < raster.getBandCount(); i++) {
            result[i][0] = sampleDec[i][posInit + tailSize];
            result[i][1] = sampleDec[i][(posInit + nSamples) - tailSize];
        }
        stats.setTailTrimValue(tailPercent, result);
        for (int iValue = 0; iValue < tailSizeList.length; iValue++) {
            double[][] res = new double[raster.getBandCount()][2];
            for (int i = 0; i < raster.getBandCount(); i++) {
                res[i][0] = sampleDec[i][posInit + tailSizeList[iValue]];
                res[i][1] = sampleDec[i][(posInit + nSamples) - tailSizeList[iValue]];
            }
            stats.setTailTrimValue(tailPercentList[iValue], res);
        }
    }
}
