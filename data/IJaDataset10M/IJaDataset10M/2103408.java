package org.gvsig.raster.grid.filter.bands;

import java.util.ArrayList;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.util.extensionPoints.ExtensionPoint;

/**
 * Gestor del filtro de conversi�n de RGB a HSL.
 *
 * @version 06/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RGBToHSLManager implements IRasterFilterListManager {

    protected RasterFilterList filterList = null;

    /**
	 * Registra RGBToHSLManager en los puntos de extension de RasterFilter
	 */
    public static void register() {
        ExtensionPoint point = ExtensionPoint.getExtensionPoint("RasterFilter");
        point.register("RGBToHSL", RGBToHSLManager.class);
    }

    /**
	 * Constructor.
	 * Asigna la lista de filtros y el managener global.
	 *
	 * @param filterListManager
	 */
    public RGBToHSLManager(RasterFilterListManager filterListManager) {
        this.filterList = filterListManager.getFilterList();
    }

    /**
	 * A�ade un filtro de conversi�n de RGB a HSL a la pila de filtros.
 * @throws FilterTypeException 
	 */
    public void addRGBToHSLFilter(int outputType, int[] renderBands) throws FilterTypeException {
        RasterFilter filter = new RGBToHSLByteFilter();
        if (filter != null) {
            filter.addParam("outputType", new Integer(outputType));
            filter.addParam("renderBands", renderBands);
            filterList.add(filter);
        }
    }

    public ArrayList getRasterFilterList() {
        ArrayList filters = new ArrayList();
        filters.add(RGBToHSLFilter.class);
        return filters;
    }

    public void addFilter(Class classFilter, Params params) throws FilterTypeException {
        if (classFilter.equals(RGBToHSLFilter.class)) {
            int[] renderBands = { 0, 1, 2 };
            int out = IBuffer.TYPE_BYTE;
            for (int i = 0; i < params.getNumParams(); i++) {
                if (params.getParam(i).id.equals("RenderBands") && params.getParam(i).defaultValue instanceof String) {
                    String[] bands = new String((String) params.getParam(i).defaultValue).split(" ");
                    renderBands[0] = new Integer(bands[0]).intValue();
                    renderBands[1] = new Integer(bands[1]).intValue();
                    renderBands[2] = new Integer(bands[2]).intValue();
                    continue;
                }
                if (params.getParam(i).id.equals("outputType")) {
                    int value = ((Integer) params.getParam(i).defaultValue).intValue();
                    if (value == 0) out = IBuffer.TYPE_BYTE;
                    if (value == 1) out = IBuffer.TYPE_DOUBLE;
                }
            }
            addRGBToHSLFilter(out, renderBands);
        }
    }

    public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) {
        return filteri;
    }

    public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
        return filterList;
    }
}
