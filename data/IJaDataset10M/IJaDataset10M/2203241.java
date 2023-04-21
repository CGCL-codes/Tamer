package org.gvsig.raster.grid.filter.enhancement;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams.Stretch;

/**
 * Filtro de realce lineal para tipos de datos float. El realce es aplicado por intervalos.
 * Para cada pixel se obtiene en que intervalo se encuentra y se aplica la scala y offset
 * calculados para ese intervalo.
 *
 * @version 11/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class LinearStretchEnhancementFloatFilter extends LinearStretchEnhancementFilter {

    public void process(int col, int line) throws InterruptedException {
        for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
            if (renderBands[iBand] < 0) {
                rasterResult.setElem(line, col, iBand, (byte) 0);
                continue;
            }
            float p = raster.getElemFloat(line, col, iBand);
            if (iBand < scaleOffsetList.length) processValue(p, scaleOffsetList[iBand], col, line, iBand); else rasterResult.setElem(line, col, iBand, (byte) p);
        }
    }

    /**
	 * Procesa un dato del raster aplicandole el factor de escala y desplazamiento que
	 * necesita.
	 * @param p Valor del punto
	 * @param data Estructura de datos con los valores de escala y desplazamiento
	 * @param col Columna del valor dentro del raster
	 * @param line L�nea del valor dentro del raster
	 * @param iBand N�mero de banda del valor dentro del raster
	 * @return true si ha podido ser procesado y false si no lo hace
	 * @throws InterruptedException 
	 */
    private void processValue(float p, Stretch data, int col, int line, int iBand) throws InterruptedException {
        if (data.scale != null) {
            if (p > data.maxValue) p = (float) data.maxValue; else if (p < data.minValue) p = (float) data.minValue;
            for (int i = 0; i < data.scale.length; i++) {
                if ((p > data.stretchIn[i] && p <= data.stretchIn[i + 1]) || (i == 0 && p == data.stretchIn[i])) {
                    p = (float) ((((double) p) - data.stretchIn[i]) * data.scale[i] + data.offset[i]);
                    break;
                }
            }
            rasterResult.setElem(line, col, iBand, (byte) (((byte) p) & 0xff));
        }
    }

    public int getInRasterDataType() {
        return IBuffer.TYPE_FLOAT;
    }
}
