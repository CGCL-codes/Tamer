package org.gvsig.raster.grid.render;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterListChangeEvent;
import org.gvsig.raster.grid.filter.FilterListChangeListener;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterUtilities;

/**
 * Esta clase se encarga de la gesti�n del dibujado de datos le�dos desde la capa
 * "dataaccess" sobre objetos java. Para ello necesita una fuente de datos que tipicamente
 * es un buffer (RasterBuffer) y un objeto que realice la funci�n de escritura de datos a
 * partir de un estado inicial.
 * Esta capa del renderizado gestiona Extents, rotaciones, tama�os de vista pero la escritura
 * de datos desde el buffer al objeto image es llevada a cabo por ImageDrawer.
 *
 * Par�metros de control de la visualizaci�n:
 * <UL>
 * <LI>renderBands: Orden de visualizado de las bands.</LI>
 * <LI>replicateBands: Para visualizaci�n de raster de una banda. Dice si se replica sobre las otras dos bandas
 * de visualizaci�n o se ponen a 0.</LI>
 * <LI>enhanced: aplicaci�n de filtro de realce</LI>
 * <LI>removeEnds: Eliminar extremos en el filtro de realce. Uso del segundo m�ximo y m�nimo</LI>
 * <LI>tailTrim: Aplicacion de recorte de colas en el realce. Es un valor decimal que representa el porcentaje del recorte entre 100.
 * Es decir, 0.1 significa que el recorte es de un 10%</LI>
 * </UL>
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Rendering implements PropertyListener, FilterListChangeListener {

    /**
	 * Grid para la gesti�n del buffer
	 */
    private Grid grid = null;

    /**
	 * Fuente de datos para el renderizado
	 */
    private BufferFactory bufferFactory = null;

    /**
	 * N�mero de bandas a renderizar y en el orden que se har�. Esto es asignado
	 * por el usuario de la renderizaci�n.
	 */
    private int[] renderBands = { 0, 1, 2 };

    /**
	 * Tiene el comportamiento cuando se tiene un raster con una. Dice si en las
	 * otras bandas a renderizar se replica la banda existente o se ponen a 0.
	 */
    private boolean replicateBand = false;

    private ImageDrawer drawer = null;

    /**
	 * Ultima transparencia aplicada en la visualizaci�n que es obtenida desde el
	 * grid
	 */
    private GridTransparency lastTransparency = null;

    /**
	 * Lista de filtros aplicada en la renderizaci�n
	 */
    private RasterFilterList filterList = null;

    private IBuffer lastRenderBuffer = null;

    /**
	 * Ancho y alto del objeto Image en una petici�n de dibujado a un raster
	 * raster
	 */
    private double widthImage, heightImage;

    private Point2D ulPxRequest, lrPxRequest;

    /**
	 * Array de listeners que ser�n informados cuando cambia una propiedad en la visualizaci�n
	 */
    private ArrayList visualPropertyListener = new ArrayList();

    /**
	 * Constructor
	 */
    public Rendering() {
        init();
    }

    /**
	 * Constructor
	 * @param grid
	 */
    public Rendering(Grid grid) {
        this.grid = grid;
        init();
    }

    /**
	 * Constructor
	 * @param grid
	 */
    public Rendering(BufferFactory ds) {
        this.bufferFactory = ds;
        init();
    }

    private void init() {
        drawer = new ImageDrawer(this);
        if (bufferFactory == null) {
            setRenderBands(new int[] { 0, 1, 2 });
            return;
        }
        switch(bufferFactory.getDataSource().getBandCount()) {
            case 1:
                setRenderBands(new int[] { 0, 0, 0 });
                break;
            case 2:
                setRenderBands(new int[] { 0, 1, 1 });
                break;
            default:
                setRenderBands(new int[] { 0, 1, 2 });
                break;
        }
        if (bufferFactory.getDataSource().getDatasetCount() == 1) {
            DatasetColorInterpretation colorInterpr = bufferFactory.getDataSource().getColorInterpretation();
            if (colorInterpr != null) {
                if (colorInterpr.getBand(DatasetColorInterpretation.PAL_BAND) == -1) {
                    if (colorInterpr.isUndefined()) return;
                    int[] result = new int[] { -1, -1, -1 };
                    int gray = colorInterpr.getBand(DatasetColorInterpretation.GRAY_BAND);
                    if (gray != -1) {
                        result[0] = result[1] = result[2] = gray;
                    } else {
                        int r = colorInterpr.getBand(DatasetColorInterpretation.RED_BAND);
                        if (r != -1) result[0] = r;
                        int g = colorInterpr.getBand(DatasetColorInterpretation.GREEN_BAND);
                        if (g != -1) result[1] = g;
                        int b = colorInterpr.getBand(DatasetColorInterpretation.BLUE_BAND);
                        if (b != -1) result[2] = b;
                    }
                    setRenderBands(result);
                }
            }
        }
    }

    /**
	 * Asigna un listener a la lista que ser� informado cuando cambie una
	 * propiedad visual en la renderizaci�n. 
	 * @param listener VisualPropertyListener
	 */
    public void addVisualPropertyListener(VisualPropertyListener listener) {
        visualPropertyListener.add(listener);
    }

    /**
	 * M�todo llamado cuando hay un cambio en una propiedad de visualizaci�n
	 */
    private void callVisualPropertyChanged(Object obj) {
        for (int i = 0; i < visualPropertyListener.size(); i++) {
            VisualPropertyEvent ev = new VisualPropertyEvent(obj);
            ((VisualPropertyListener) visualPropertyListener.get(i)).visualPropertyValueChanged(ev);
        }
    }

    /**
	 * Dibuja el raster sobre el Graphics. Para ello debemos de pasar el viewPort que corresponde a la
	 * vista. Este viewPort es ajustado a los tama�os m�ximos y m�nimos de la imagen por la funci�n
	 * calculateNewView. Esta funci�n tambi�n asignar� la vista a los drivers. Posteriormente se calcula
	 * el alto y ancho de la imagen a dibujar (wImg, hImg), as� como el punto donde se va a pintar dentro
	 * del graphics (pt). Finalmente se llama a updateImage del driver para que pinte y una vez dibujado
	 * se pasa a trav�s de la funci�n renderizeRaster que es la encargada de aplicar la pila de filtros
	 * sobre el Image que ha devuelto el driver.
	 *
	 * Para calcular en que coordenada pixel (pt) se empezar� a pintar el BufferedImage con el raster le�do
	 * se aplica sobre la esquina superior izquierda de esta la matriz de transformaci�n del ViewPortData
	 * pasado vp.mat.transform(pt, pt). Si el raster no est� rotado este punto es el resultante de la
	 * funci�n calculateNewView que devuelve la petici�n ajustada al extent de la imagen (sin rotar). Si
	 * el raster est� rotado necesitaremos para la transformaci�n el resultado de la funci�n coordULRotateRaster.
	 * Lo que hace esta �ltima es colocar la petici�n que ha sido puesta en coordenadas de la imagen sin rotar
	 * (para pedir al driver de forma correcta) otra vez en coordenadas de la imagen rotada (para calcular su
	 * posici�n de dibujado).
	 *
	 * Para dibujar sobre el Graphics2D el raster rotado aplicaremos la matriz de transformaci�n con los
	 * par�metros de Shear sobre este Graphics de forma inversa. Como hemos movido el fondo tendremos que
	 * recalcular ahora el punto donde se comienza a dibujar aplicandole la transformaci�n sobre este
	 * at.inverseTransform(pt, pt);. Finalmente volcamos el BufferedImage sobre el Graphics volviendo a dejar
	 * el Graphics en su posici�n original al acabar.
	 *
	 * @param g Graphics sobre el que se pinta
	 * @param vp ViewPort de la extensi�n a dibujar
	 * @throws InvalidSetViewException
	 * @throws ArrayIndexOutOfBoundsException
	 */
    public synchronized Image draw(Graphics2D g, ViewPortData vp) throws RasterDriverException, InvalidSetViewException, InterruptedException {
        Image geoImage = null;
        IRasterDataSource dataset = bufferFactory.getDataSource();
        AffineTransform transf = dataset.getAffineTransform(0);
        if (RasterUtilities.isOutside(vp.getExtent(), dataset.getExtent())) return null;
        Extent adjustedRotedRequest = request(vp, dataset);
        if ((widthImage <= 0) || (heightImage <= 0)) return null;
        double[] step = null;
        if (bufferFactory != null) {
            if (lastTransparency == null) {
                lastTransparency = new GridTransparency(bufferFactory.getDataSource().getTransparencyFilesStatus());
                lastTransparency.addPropertyListener(this);
            }
            if (bufferFactory.getDataSource().getTransparencyFilesStatus().getAlphaBandNumber() != -1) {
                bufferFactory.setSupersamplingLoadingBuffer(false);
                bufferFactory.setDrawableBands(new int[] { lastTransparency.getAlphaBandNumber(), -1, -1 });
                bufferFactory.setAreaOfInterest(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY(), adjustedRotedRequest.getLRX(), adjustedRotedRequest.getLRY(), (int) Math.round(widthImage), (int) Math.round(heightImage));
                bufferFactory.setSupersamplingLoadingBuffer(true);
                lastTransparency.setAlphaBand(bufferFactory.getRasterBuf());
            }
            bufferFactory.setSupersamplingLoadingBuffer(false);
            bufferFactory.setDrawableBands(getRenderBands());
            step = bufferFactory.setAreaOfInterest(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY(), adjustedRotedRequest.getLRX(), adjustedRotedRequest.getLRY(), (int) Math.round(widthImage), (int) Math.round(heightImage));
            bufferFactory.setSupersamplingLoadingBuffer(true);
            if (bufferFactory.getDataSource().getTransparencyFilesStatus().isNoDataActive()) lastTransparency.setDataBuffer(bufferFactory.getRasterBuf()); else lastTransparency.setDataBuffer(null);
            lastTransparency.activeTransparency();
        } else {
            return null;
        }
        grid = new Grid(bufferFactory, true);
        filterList.addEnvParam("Transparency", lastTransparency);
        grid.setFilterList(filterList);
        grid.applyFilters();
        if (grid.getFilterList().getAlphaBand() != null) {
            IBuffer t = grid.getFilterList().getAlphaBand();
            if (lastTransparency.getAlphaBand() != null) t = Transparency.merge(t, lastTransparency.getAlphaBand());
            lastTransparency.setAlphaBand(t);
            lastTransparency.activeTransparency();
        }
        lastRenderBuffer = grid.getRasterBuf();
        drawer.setBuffer(lastRenderBuffer);
        drawer.setStep(step);
        drawer.setBufferSize((int) Math.round(widthImage), (int) Math.round(heightImage));
        geoImage = drawer.drawBufferOverImageObject(replicateBand, getRenderBands());
        lastTransparency.setAlphaBand(null);
        if (transf.getScaleX() > 0 && transf.getScaleY() < 0 && transf.getShearX() == 0 && transf.getShearY() == 0) {
            Point2D lastGraphicOffset = new Point2D.Double(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY());
            vp.mat.transform(lastGraphicOffset, lastGraphicOffset);
            g.drawImage(geoImage, (int) Math.round(lastGraphicOffset.getX()), (int) Math.round(lastGraphicOffset.getY()), null);
            return geoImage;
        }
        double sX = Math.abs(ulPxRequest.getX() - lrPxRequest.getX()) / widthImage;
        double sY = Math.abs(ulPxRequest.getY() - lrPxRequest.getY()) / heightImage;
        AffineTransform escale = new AffineTransform(sX, 0, 0, sY, 0, 0);
        try {
            AffineTransform at = (AffineTransform) escale.clone();
            at.preConcatenate(transf);
            at.preConcatenate(vp.getMat());
            g.transform(at);
            Point2D.Double ptUL = null;
            Point2D.Double ptLR = null;
            int ix;
            int iy;
            int diffx;
            int diffy;
            double xscale;
            double yscale;
            int xcorr;
            int ycorr;
            ptUL = new Point2D.Double(transf.getTranslateX(), transf.getTranslateY());
            ptLR = new Point2D.Double(adjustedRotedRequest.getLRX(), adjustedRotedRequest.getLRY());
            vp.getMat().transform(ptUL, ptUL);
            vp.getMat().transform(ptLR, ptLR);
            at.inverseTransform(ptUL, ptUL);
            at.inverseTransform(ptLR, ptLR);
            ix = (int) Math.round(ptUL.getX());
            iy = (int) Math.round(ptUL.getY());
            diffx = 0;
            diffy = 0;
            if (ptLR.getX() > widthImage) {
                diffx = (int) (Math.round((ptLR.getX()) - widthImage));
            }
            if (ptLR.getY() > heightImage) {
                diffy = (int) (Math.round((ptLR.getY()) - heightImage));
            }
            xscale = bufferFactory.getDataSource().getCellSize() * Math.abs(vp.getMat().getScaleX());
            yscale = bufferFactory.getDataSource().getCellSize() * Math.abs(vp.getMat().getScaleY());
            xcorr = bufferFactory.getDataSource().getXCorrection() * (int) Math.round(xscale);
            ycorr = bufferFactory.getDataSource().getYCorrection() * (int) Math.round(yscale);
            g.drawImage(geoImage, ix + diffx + xcorr, iy + diffy + ycorr, null);
            g.transform(at.createInverse());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return geoImage;
    }

    /**
	 * Calculamos la petici�n en coordenadas del mundo real con la transformaci�n del raster. Esto
	 * permite obtener las coordenadas de la petici�n con la rotaci�n, si la tiene.
	 * @param vp
	 * @param dataset
	 * @return
	 */
    private Extent request(ViewPortData vp, IRasterDataSource dataset) {
        if (dataset.isRotated()) {
            Point2D ul = new Point2D.Double(vp.getExtent().minX(), vp.getExtent().maxY());
            Point2D ur = new Point2D.Double(vp.getExtent().maxX(), vp.getExtent().maxY());
            Point2D ll = new Point2D.Double(vp.getExtent().minX(), vp.getExtent().minY());
            Point2D lr = new Point2D.Double(vp.getExtent().maxX(), vp.getExtent().minY());
            ul = dataset.worldToRaster(ul);
            ur = dataset.worldToRaster(ur);
            ll = dataset.worldToRaster(ll);
            lr = dataset.worldToRaster(lr);
            double pxMaxX = Math.max(Math.max(ul.getX(), ur.getX()), Math.max(ll.getX(), lr.getX()));
            double pxMaxY = Math.max(Math.max(ul.getY(), ur.getY()), Math.max(ll.getY(), lr.getY()));
            double pxMinX = Math.min(Math.min(ul.getX(), ur.getX()), Math.min(ll.getX(), lr.getX()));
            double pxMinY = Math.min(Math.min(ul.getY(), ur.getY()), Math.min(ll.getY(), lr.getY()));
            pxMinX = Math.max(pxMinX, 0);
            pxMinY = Math.max(pxMinY, 0);
            pxMaxX = Math.min(pxMaxX, dataset.getWidth());
            pxMaxY = Math.min(pxMaxY, dataset.getHeight());
            ulPxRequest = new Point2D.Double(pxMinX, pxMinY);
            lrPxRequest = new Point2D.Double(pxMaxX, pxMaxY);
            widthImage = ((Math.abs(lrPxRequest.getX() - ulPxRequest.getX()) * vp.getWidth()) / Math.abs(pxMaxX - pxMinX));
            heightImage = ((Math.abs(lrPxRequest.getY() - ulPxRequest.getY()) * vp.getHeight()) / Math.abs(pxMaxY - pxMinY));
            Point2D ulWC = dataset.rasterToWorld(ulPxRequest);
            Point2D lrWC = dataset.rasterToWorld(lrPxRequest);
            return new Extent(ulWC, lrWC);
        }
        Extent adjustedRotedExtent = RasterUtilities.calculateAdjustedView(vp.getExtent(), dataset.getAffineTransform(0), new Dimension((int) dataset.getWidth(), (int) dataset.getHeight()));
        widthImage = (int) Math.round(Math.abs(adjustedRotedExtent.width() * vp.getMat().getScaleX()));
        heightImage = (int) Math.round(Math.abs(adjustedRotedExtent.height() * vp.getMat().getScaleY()));
        Point2D ul = new Point2D.Double(adjustedRotedExtent.getULX(), adjustedRotedExtent.getULY());
        Point2D lr = new Point2D.Double(adjustedRotedExtent.getLRX(), adjustedRotedExtent.getLRY());
        ul = dataset.worldToRaster(ul);
        lr = dataset.worldToRaster(lr);
        ulPxRequest = new Point2D.Double(ul.getX(), ul.getY());
        lrPxRequest = new Point2D.Double(lr.getX(), lr.getY());
        return adjustedRotedExtent;
    }

    /**
	 * Obtiene el n�mero de bandas y el orden de renderizado. Cada posici�n del
	 * vector es una banda del buffer y el contenido de esa posici�n es la banda
	 * de la imagen que se dibujar� sobre ese buffer. A la hora de renderizar hay
	 * que tener en cuenta que solo se renderizan las tres primeras bandas del
	 * buffer por lo que solo se tienen en cuenta los tres primeros elementos. Por
	 * ejemplo, el array {1, 0, 3} dibujar� sobre el Graphics las bandas 1,0 y 3
	 * de un raster de al menos 4 bandas. La notaci�n con -1 en alguna posici�n
	 * del vector solo tiene sentido en la visualizaci�n pero no se puede as�gnar
	 * una banda del buffer a null. Algunos ejemplos:
	 * <P>
	 * <UL>
	 * <LI> {-1, 0, -1} Dibuja la banda 0 del raster en la G de la visualizaci�n. Si
	 * replicateBand es true R = G = B sino R = B = 0 </LI>
	 * <LI> {1, 0, 3} La R = banda 1 del raster, G = 0 y B = 3 </LI>
	 * <LI> {0} La R = banda 0 del raster. Si replicateBand es true R = G = B 
	 * sino G = B = 0</LI>
	 * </UL>
	 * </P>
	 *
	 * @return bandas y su posici�n
	 */
    public int[] getRenderBands() {
        return renderBands;
    }

    /**
		 * Asigna el n�mero de bandas y el orden de renderizado. Cada posici�n del vector es una banda
	 * del buffer y el contenido de esa posici�n es la banda de la imagen que se dibujar�
	 * sobre ese buffer. A la hora de renderizar hay que tener en cuenta que solo se renderizan las
	 * tres primeras bandas del buffer por lo que solo se tienen en cuenta los tres primeros
	 * elementos. Por ejemplo, el array {1, 0, 3} dibujar� sobre el Graphics las bandas 1,0 y 3 de un
	 * raster que tiene al menos 4 bandas. La notaci�n con -1 en alguna posici�n del vector solo tiene sentido
	 * en la visualizaci�n pero no se puede as�gnar una banda del buffer a null.
	 * Algunos ejemplos:
	 * <P>
	 * {-1, 0, -1} Dibuja la banda 0 del raster en la G de la visualizaci�n.
	 * Si replicateBand es true R = G = B sino R = B = 0
	 * {1, 0, 3} La R = banda 1 del raster, G = 0 y B = 3
	 * {0} La R = banda 0 del raster. Si replicateBand es true R = G = B sino G = B = 0
	 * </P>
	 *
	 *
		 * @param renderBands: bandas y su posici�n
		 */
    public void setRenderBands(int[] renderBands) {
        if (renderBands[0] != this.renderBands[0] || renderBands[1] != this.renderBands[1] || renderBands[2] != this.renderBands[2]) callVisualPropertyChanged(renderBands);
        this.renderBands = renderBands;
        if (filterList != null) {
            for (int i = 0; i < filterList.lenght(); i++) ((RasterFilter) filterList.get(i)).addParam("renderBands", renderBands);
        }
    }

    /**
	 * Dado que la notaci�n de bandas para renderizado admite posiciones con -1 y la notaci�n del
	 * buffer no ya que no tendria sentido. Esta funci�n adapta la primera notaci�n a la segunda
	 * para realizar la petici�n setAreaOfInterest y cargar el buffer.
	 * @param b Array que indica la posici�n de bandas para el renderizado
	 * @return Array que indica la posici�n de bandas para la petici�n
	 */
    public int[] formatArrayRenderBand(int[] b) {
        int cont = 0;
        for (int i = 0; i < b.length; i++) if (b[i] >= 0) cont++;
        if (cont <= 0) return null;
        int[] out = new int[cont];
        int pos = 0;
        for (int i = 0; i < cont; i++) {
            while (b[pos] == -1) pos++;
            out[i] = b[pos];
            pos++;
        }
        return out;
    }

    /**
	 * Obtiene el �ltimo objeto transparencia aplicado en la renderizaci�n
	 * @return GridTransparency
	 */
    public GridTransparency getLastTransparency() {
        return lastTransparency;
    }

    /**
	 * Asigna el �ltimo estado de transparencia de la renderizaci�n.  
	 * @param lastTransparency
	 */
    public void setLastTransparency(GridTransparency lastTransparency) {
        this.lastTransparency = lastTransparency;
        this.lastTransparency.addPropertyListener(this);
        if (getFilterList() != null) getFilterList().addEnvParam("Transparency", lastTransparency);
    }

    /**
	 * Obtiene las lista de filtros aplicados en la renderizaci�n
	 * @return RasterFilterList
	 */
    public RasterFilterList getFilterList() {
        return filterList;
    }

    /**
	 * Obtiene el �ltimo buffer renderizado.
	 * @return IBuffer
	 */
    public IBuffer getLastRenderBuffer() {
        return this.lastRenderBuffer;
    }

    /**
	 * Asigna el �ltimo renderizado.
	 * @param buf
	 */
    public void setLastRenderBuffer(IBuffer buf) {
        this.lastRenderBuffer = buf;
    }

    /**
	 * Asigna la lista de filtros que se usar� en el renderizado
	 * @param RasterFilterList
	 */
    public void setFilterList(RasterFilterList filterList) {
        this.filterList = filterList;
        this.filterList.addFilterListListener(this);
    }

    /**
	 * Informa de si el raster tiene tabla de color asociada o no.
	 * @return true si tiene tabla de color y false si no la tiene.
	 */
    public boolean existColorTable() {
        return (filterList.getFilterByBaseClass(ColorTableFilter.class) != null);
    }

    /**
	 * Obtiene el grid asociado al render
	 * @return
	 */
    public Grid getGrid() {
        return grid;
    }

    /**
	 * Asigna la factoria de buffer del renderizador
	 * @param bf
	 */
    public void setBufferFactory(BufferFactory bf) {
        this.bufferFactory = bf;
    }

    /**
	 * Evento activado cuando cambia una propiedad de transparencia.
	 */
    public void actionValueChanged(PropertyEvent e) {
        callVisualPropertyChanged(new VisualPropertyEvent(e.getSource()));
    }

    /**
	 * Evento activado cuando cambia la lista de filtros.
	 */
    public void filterListChanged(FilterListChangeEvent e) {
        callVisualPropertyChanged(new VisualPropertyEvent(e.getSource()));
    }

    public void free() {
        if (lastTransparency != null) lastTransparency.free();
        if (grid != null) grid.free();
        if (getFilterList() != null) getFilterList().free();
        grid = null;
        bufferFactory = null;
        if (lastRenderBuffer != null) lastRenderBuffer.free();
        lastRenderBuffer = null;
    }
}
