package org.gvsig.raster.grid.filter.bands;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.util.ColorConversion;

/**
 * <P>
 * Clase base para el filtro de Tono, Saturaci�n y Brillo
 * </P>
 *
 * @version 04/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ToLumSaFilter extends RasterFilter {

    protected IBuffer rasterAlpha = null;

    public static String[] names = new String[] { "tolumsa" };

    protected ColorConversion colorConversion = null;

    protected int out = IBuffer.TYPE_BYTE;

    protected double hue = 0;

    protected double luminosity = 0;

    protected double saturation = 0;

    protected int[] renderBands = null;

    /**
	 * Constructor
	 */
    public ToLumSaFilter() {
        super();
        setName(names[0]);
    }

    public void pre() {
        exec = true;
        raster = (RasterBuffer) params.get("raster");
        int[] rb = (int[]) params.get("renderBands");
        switch(raster.getBandCount()) {
            case 1:
                renderBands = new int[] { 0, 0, 0 };
                break;
            case 2:
                renderBands = rb;
                break;
            case 3:
                renderBands = new int[] { 0, 1, 2 };
                break;
        }
        for (int i = 0; i < renderBands.length; i++) if (rb[i] == -1) renderBands[i] = -1;
        if (raster.getDataType() != IBuffer.TYPE_BYTE) {
            exec = false;
            raster = rasterResult;
            return;
        }
        if (params.get("hue") != null) hue = ((Double) params.get("hue")).doubleValue();
        if (params.get("luminosity") != null) luminosity = ((Double) params.get("luminosity")).doubleValue();
        if (params.get("saturation") != null) saturation = ((Double) params.get("saturation")).doubleValue();
        if (raster != null) {
            height = raster.getHeight();
            width = raster.getWidth();
            rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), 3, true);
        }
        if (colorConversion == null) colorConversion = new ColorConversion();
    }

    public String getGroup() {
        return "colores";
    }

    public String[] getNames() {
        return names;
    }

    public Object getResult(String name) {
        if (name.equals("raster")) return (Object) this.rasterResult;
        return null;
    }

    public Params getUIParams(String nameFilter) {
        Params params = new Params();
        params.setParam("hue", new Double(hue), Params.SLIDER, new String[] { "-180", "180", "0", "10", "50" });
        params.setParam("luminosity", new Double(luminosity), Params.SLIDER, new String[] { "-100", "100", "0", "10", "50" });
        params.setParam("saturation", new Double(saturation), Params.SLIDER, new String[] { "-100", "100", "0", "10", "50" });
        return params;
    }

    public void post() {
    }

    public void process(int x, int y) throws InterruptedException {
    }

    public int getOutRasterDataType() {
        return IBuffer.TYPE_BYTE;
    }

    public boolean isVisible() {
        return true;
    }

    public int getInRasterDataType() {
        return IBuffer.TYPE_BYTE;
    }
}
