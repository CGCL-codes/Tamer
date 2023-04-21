package org.gvsig.raster.grid.filter.convolution;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;

/**
 * Filtros de  Convolucion
 * @author  Diego Guerrero Sevilla  <diego.guerrero@uclm.es>
 * @author	Alejandro Mu�oz S�nchez  < alejandro.munoz@uclm.es>
 * */
public class ConvolutionFilter extends RasterFilter {

    public static String[] names = new String[] { "media", "pasobajo", "sharpen", "gauss", "personalizado" };

    public int ladoVentana = 0;

    protected Kernel kernel = null;

    /** Tipos de filtros de convolucion */
    public static final int TYPE_MEDIA = 0;

    public static final int TYPE_LOWPASS = 1;

    public static final int TYPE_HIGHPASS = 2;

    public static final int TYPE_GAUSS = 3;

    public static final int TYPE_OTHER = 4;

    /**Definicion de los filtros basicos */
    static final double gauss3x3[][] = { { 1, 4, 1 }, { 4, 12, 4 }, { 1, 4, 1 } };

    static final double gauss5x5[][] = { { 1, 2, 3, 2, 1 }, { 2, 7, 11, 7, 2 }, { 3, 11, 17, 11, 3 }, { 2, 7, 11, 7, 2 }, { 1, 2, 3, 2, 1 } };

    static final double gauss7x7[][] = { { 1, 1, 2, 2, 2, 1, 1 }, { 1, 2, 2, 4, 2, 2, 1 }, { 2, 2, 4, 8, 4, 2, 2 }, { 2, 4, 8, 16, 8, 4, 2 }, { 2, 2, 4, 8, 4, 2, 2 }, { 1, 2, 2, 4, 2, 2, 1 }, { 1, 1, 2, 2, 2, 1, 1 } };

    static final double gauss7x7aux[][] = { { 0, 0, 0.0003, 0.0006, 0.0003, 0, 0 }, { 0, 0.0011, 0.0079, 0.0153, 0.0079, 0.0011, 0 }, { 0.0003, 0.0079, 0.0563, 0.1082, 0.0563, 0.0079, 0.0003 }, { 0.0006, 0.0153, 0.1082, 0.2079, 0.1082, 0.0153, 0.0006 }, { 0.0003, 0.0079, 0.0563, 0.1082, 0.0563, 0.0079, 0.0003 }, { 0, 0.0011, 0.0079, 0.0153, 0.0079, 0.0011, 0 }, { 0, 0, 0.0003, 0.0006, 0.0003, 0, 0 } };

    static final double lowpass3x3[][] = { { 0, 1, 0 }, { 1, 6, 1 }, { 0, 1, 0 } };

    static final double lowpass5x5[][] = { { 1, 1, 1, 1, 1 }, { 1, 4, 4, 4, 1 }, { 1, 4, 12, 4, 1 }, { 1, 4, 4, 4, 1 }, { 1, 1, 1, 1, 1 } };

    static final double media3x3[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };

    static final double media5x5[][] = { { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 } };

    static final double media7x7[][] = { { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1 } };

    static final double highpass3x3[][] = { { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } };

    static final Kernel kGauss3x3 = new Kernel(gauss3x3, 34);

    static final Kernel kGauss5x5 = new Kernel(gauss5x5, 121);

    static final Kernel kGauss7x7 = new Kernel(gauss7x7aux);

    static final Kernel kLowpass3x3 = new Kernel(lowpass3x3, 10);

    static final Kernel kLowpass5x5 = new Kernel(lowpass5x5);

    static final Kernel kMedia3x3 = new Kernel(media3x3, 9);

    static final Kernel kMedia5x5 = new Kernel(media5x5, 25);

    static final Kernel kMedia7x7 = new Kernel(media7x7, 49);

    static final Kernel kHighpass3x3 = new Kernel(highpass3x3);

    private int operator = 0;

    private double agudeza = 1;

    /** Filtro de Convolucion*/
    public ConvolutionFilter() {
        super();
        setName(names[0]);
    }

    public String getGroup() {
        return "espaciales";
    }

    public int getInRasterDataType() {
        return 0;
    }

    public String[] getNames() {
        return names;
    }

    public int getOutRasterDataType() {
        return 0;
    }

    public Object getResult(String name) {
        if (name.equals("raster")) {
            if (!exec) return this.raster;
            return this.rasterResult;
        }
        return null;
    }

    public void post() {
        raster = null;
    }

    public void pre() {
        exec = true;
        raster = (RasterBuffer) params.get("raster");
        height = raster.getHeight();
        width = raster.getWidth();
        ladoVentana = ((Integer) params.get("ladoVentana")).intValue();
        if (params.get("Agudeza") != null) agudeza = ((Double) params.get("Agudeza")).doubleValue();
        Kernel userKernel = (Kernel) params.get("kernel");
        String name = ((String) params.get("filterName"));
        operator = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                operator = i;
                setName(names[i]);
                break;
            }
        }
        switch(operator) {
            case TYPE_MEDIA:
                setName(names[0]);
                switch(ladoVentana) {
                    case 2:
                        kernel = kMedia7x7;
                        break;
                    case 1:
                        kernel = kMedia5x5;
                        break;
                    default:
                        kernel = kMedia3x3;
                        break;
                }
                break;
            case TYPE_LOWPASS:
                setName(names[1]);
                switch(ladoVentana) {
                    case 1:
                        kernel = kLowpass5x5;
                        break;
                    default:
                        kernel = kLowpass3x3;
                        break;
                }
                break;
            case TYPE_HIGHPASS:
                setName(names[2]);
                double[][] h = new double[3][3];
                for (int i = 0; i < h.length; i++) {
                    for (int j = 0; j < h[i].length; j++) {
                        h[i][j] = highpass3x3[i][j];
                    }
                }
                h[1][1] = 29 - (agudeza * 20.9) / 100;
                kernel = new Kernel(h);
                break;
            case TYPE_GAUSS:
                setName(names[3]);
                switch(ladoVentana) {
                    case 2:
                        kernel = kGauss7x7;
                        break;
                    case 1:
                        kernel = kGauss5x5;
                        break;
                    default:
                        kernel = kGauss3x3;
                        break;
                }
                break;
            case TYPE_OTHER:
                setName(names[4]);
                kernel = userKernel;
                break;
        }
        if (kernel == null) kernel = kMedia3x3;
    }

    public void process(int x, int y) throws InterruptedException {
    }

    public Params getUIParams(String nameFilter) {
        Params params = new Params();
        if (nameFilter == "personalizado") {
            params.setParam("Panel", new ConvolutionUI(kernel), -1, null);
        } else if (nameFilter != "pasobajo" && nameFilter != "sharpen") {
            params.setParam("LadoVentana", new Integer(ladoVentana), Params.CHOICE, new String[] { "3", "5", "7" });
        } else if (nameFilter == "sharpen") {
            params.setParam("Agudeza", new Double(agudeza), Params.SLIDER, new String[] { "0", "100", "0", "1", "25" });
        } else {
            params.setParam("LadoVentana", new Integer(ladoVentana), Params.CHOICE, new String[] { "3", "5" });
        }
        params.setParam("FilterName", nameFilter, -1, null);
        return params;
    }
}
