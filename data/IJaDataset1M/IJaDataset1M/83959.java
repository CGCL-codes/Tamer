package org.gvsig.raster.dataset;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.hierarchy.IHistogramable;

/**
 * Interfaz que deben implementar cualquier fuente de datos raster. Estas pueden estar
 * compuestas por N datasets. B�sicamente hay dos fuentes que deben implementar este interfaz, 
 * MultiRasterDataset y CompositeDataset. La primera es un dataset compuesto por varios ficheros
 * con el mismo Extent de N bandas cada uno. MultiRasterDataset proporciona una encapsulaci�n al acceso
 * a datos de todos ellos. CompositeDataset es un dataset compuesto de N MultiRasterDatasets cuya extensi�n
 * es continua formando un Grid de datasets con continuidad espacial. IRasterDataSource proporciona
 * una visi�n de acceso a datos com�n para ambos.
 * 
 * @version 27/08/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IRasterDataSource extends IHistogramable {

    /**
	 * Obtiene el tipo de dato por banda
	 * @return tipo de dato por banda
	 */
    public int[] getDataType();

    /**
	 * Obtiene el valor NoData asociado al raster.
	 * @return
	 */
    public double getNoDataValue();

    /**
	 * Vuelve a poner el valor noData como estaba inicialmente
	 */
    public void resetNoDataValue();

    /**
	 * Define el valor NoData asociado al raster.
	 * @return
	 */
    public void setNoDataValue(double value);

    /**
	 * Obtiene si esta activo el valor NoData asociado al raster.
	 * @return
	 */
    public boolean isNoDataEnabled();

    /**
	 * Define si se activa el valor NoData asociado al raster.
	 * @return
	 */
    public void setNoDataEnabled(boolean enabled);

    /**
	 * Obtiene la paleta correspondiente al nombre del fichero pasado por par�metro. 
	 * @param fileName Nombre del fichero
	 * @return Paleta o null si no la tiene
	 */
    public ColorTable getColorTable(String fileName);

    /**
	 * Obtiene el n�mero de bandas del raster
	 * @return N�mero de bandas
	 */
    public int getBandCount();

    /**
	 * Obtiene la lista de bandas.
	 * @return BandList
	 */
    public BandList getBands();

    /**
	 * Obtiene el n�mero de datasets con el extent completo del IRasterDataSource. El valor 
	 * devuelto por esta llamada coincidir�a con el valor devuelto por getDatasetCount() de un
	 * MultiRasterDataset que lo compone.
	 * @return integer.
	 */
    public int getDatasetCount();

    /**
	 * Obtiene el extent de la �ltima selecci�n hecha con alguna de las llamadas
	 * setAreaOfInterest. Este extent es devuelto en coordenadas reales con las transformaciones
	 * que se hayan aplicado sobre el/los dataset.
	 * @return Extent Coordenadas reales que representan el �ltimo �rea de datos
	 * solicitada.
	 */
    public Extent getLastSelectedView();

    /**
	 * Obtiene el dataset de la posici�n i. En un CompositeDataset devolver� la lista de 
	 * todos los datasets en esa posici�n 
	 * @param i Posici�n del dataset a obtener.
	 * @return RasterDataset[] Lista de datasets de esa posici�n. En un MultiRastserDataset ser� uno
	 * solo pero en un Composite habr� una lista de ellos.
	 */
    public RasterDataset[] getDataset(int i);

    /**
	 * Obtiene fichero de nombre fileName.
	 * @param i Posici�n del dataset a obtener.
	 * @return RasterDataset.
	 */
    public RasterDataset getDataset(String fileName);

    /**
	 * Elimina un fichero a la lista a partir de su nombre
	 * @param fileName	Nombre del fichero a eliminar.
	 */
    public void removeDataset(String fileName);

    /**
	 * Elimina un fichero a la lista
	 * @param file Fichero a eliminar
	 */
    public void removeDataset(RasterDataset file);

    /**
	 * A�ade un fichero a la lista de datasets a partir de su nombre. Si se trata de un multiRasterDataset
	 * solo se necesita un fichero pero si es un CompositeDataset necesitaremos una lista de ellos. Tantos
	 * como tenga el CompositeDataset 
	 * @param f fichero a a�adir.
	 * @throws RasterDriverException 
	 */
    public void addDataset(RasterDataset[] f) throws FileNotFoundInListException;

    /**
	 * A�ade un fichero a la lista de datasets a partir de su nombre. Si se trata de un multiRasterDataset
	 * solo se necesita un fichero pero si es un CompositeDataset necesitaremos una lista de ellos. Tantos
	 * como tenga el CompositeDataset 
	 * @param f fichero a a�adir.
	 * @throws RasterDriverException 
	 */
    public void addDataset(String[] fileName) throws FileNotFoundInListException, NotSupportedExtensionException, RasterDriverException;

    /**
	 * Obtiene la altura del raster en p�xeles.
	 * @return altura
	 */
    public double getHeight();

    /**
	 * Obtiene la anchura del raster en p�xeles.
	 * @return anchura
	 */
    public double getWidth();

    /**
	 * Obtiene el tama�o de celda del raster
	 * @return Valor del tama�o de celda
	 */
    public double getCellSize();

    /**
	 * Returns a pixel correction factor for affinely 
	 * rotated raster data.
	 */
    public int getXCorrection();

    /** 
	 * Returns a pixel correction factor for affinely 
	 * rotated raster data.
	 */
    public int getYCorrection();

    /**
	 * Cierra los raster asociados.
	 */
    public void close();

    /**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tama�o a los
	 * pixeles de disco. 
	 * @param x Posici�n X superior izquierda
	 * @param y Posici�n Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
    public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry) throws InvalidSetViewException, InterruptedException, RasterDriverException;

    /**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tama�o a los
	 * pixeles de disco. 
	 * @param x Posici�n X superior izquierda
	 * @param y Posici�n Y superior izquierda
	 * @param w Ancho en coordenadas reales
	 * @param h Alto en coordenadas reales
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
    public IBuffer getWindowRaster(double ulx, double uly, double w, double h, boolean adjustToExtent) throws InvalidSetViewException, InterruptedException, RasterDriverException;

    /**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * Aplica supersampleo o subsampleo en funci�n del tama�o del buffer. Esta operaci�n la gestiona
	 * el driver.
	 * @param minX Valor m�nimo de la X en coordenadas reales
	 * @param minY Valor m�nimo de la Y en coordenadas reales
	 * @param maxX Valor m�ximo de la X en coordenadas reales
	 * @param maxY Valor m�ximo de la Y en coordenadas reales
	 * @param bufWidth ancho del buffer lde datos
	 * @param bufHeight alto del buffer de datos
	 * @param adjustToExtent Flag que dice si el extent solicitado debe ajustarse al extent del raster o no.
	 * @param bandList
	 * @return Buffer de datos
	 */
    public IBuffer getWindowRaster(double ulx, double uly, double lrx, double lry, int bufWidth, int bufHeight, boolean adjustToExtent) throws InvalidSetViewException, InterruptedException, RasterDriverException;

    /**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * No aplica supersampleo ni subsampleo sino que devuelve una matriz de igual tama�o a los
	 * pixeles de disco. 
	 * @param x Posici�n X superior izquierda
	 * @param y Posici�n Y superior izquierda
	 * @param w Ancho en coordenadas pixel
	 * @param h Alto en coordenadas pixel
	 * @param bandList
	 * @return Buffer de datos
	 */
    public IBuffer getWindowRaster(int x, int y, int w, int h) throws InvalidSetViewException, InterruptedException, RasterDriverException;

    /**
	 * Obtiene una ventana de datos de la imagen a partir de coordenadas reales. 
	 * Aplica supersampleo o subsampleo en funci�n del tama�o del buffer
	 * @param x Posici�n X superior izquierda en pixels
	 * @param y Posici�n Y superior izquierda en pixels
	 * @param w Ancho en pixels
	 * @param h Alto en pixels
	 * @param bufWidth ancho del buffer de datos
	 * @param bufHeight alto del buffer de datos
	 * @param bandList
	 * @return Buffer de datos
	 */
    public IBuffer getWindowRaster(int x, int y, int w, int h, int bufWidth, int bufHeight) throws InvalidSetViewException, InterruptedException, RasterDriverException;

    /**
	 * Obtiene el extent del raster.
	 * @return Extent
	 */
    public Extent getExtent();

    /**
	 * Convierte un punto desde coordenadas pixel a coordenadas del mundo.
	 * @param pt Punto a transformar
	 * @return punto transformado en coordenadas del mundo
	 */
    public Point2D rasterToWorld(Point2D pt);

    /**
	 * Convierte un punto desde del mundo a coordenadas pixel.
	 * @param pt Punto a transformar
	 * @return punto transformado en coordenadas pixel
	 */
    public Point2D worldToRaster(Point2D pt);

    /**
	 * Obtiene el objeto con las estadisticas
	 * @return MultiFileStatistics
	 */
    public DatasetListStatistics getStatistics();

    /**
	 * Obtiene el flag que dice si el raster est� o no georreferenciado
	 * @return true si est� georreferenciado y false si no lo est�.
	 */
    public boolean isGeoreferenced();

    /**
	 * Obtiene el valor del raster en la coordenada que se le pasa.
	 * El valor ser� Double, Int, Byte, etc. dependiendo del tipo de
	 * raster.
	 * @param x	coordenada X
	 * @param y coordenada Y
	 * @return
	 * @throws InterruptedException 
	 */
    public Object getData(int x, int y, int band) throws InvalidSetViewException, FileNotOpenException, RasterDriverException, InterruptedException;

    /**
	 * Obtiene el objeto que contiene que contiene la interpretaci�n de color por
	 * banda
	 * @return DatasetColorInterpretation
	 */
    public DatasetColorInterpretation getColorInterpretation();

    /**
	 * Obtiene la proyecci�n asociada al raster. Como todos los dataset del 
	 * multiDataset deben tener la misma proyecci�n obtenemos esta del primer
	 * dataset.
	 * @return Proyecci�n en formato cadena
	 * @throws RasterDriverException
	 */
    public String getWktProjection() throws RasterDriverException;

    /**
	 * Metodo que obtiene si un punto cae dentro de los l�mites de la extensi�n de la fuente de 
	 * datos raster o fuera de ellos.
	 * @param p Punto a calcular
	 * @return true si est� dentro de los l�mites y false si est� fuera
	 */
    public boolean isInside(Point2D p);

    /**
	 * Recupera del raster la matriz de transformaci�n que ten�a en la carga.
	 * @return AffineTransform
	 */
    public AffineTransform getOwnAffineTransform();

    /**
	 * Recupera del raster la matriz de transformaci�n que lo situa en cualquier parte de la vista
	 * @param band
	 * @return AffineTransform
	 */
    public AffineTransform getAffineTransform(int band);

    /**
	 * Asigna al raster la matriz de transformaci�n para situarlo en cualquier parte de la vista
	 * @param transf
	 */
    public void setAffineTransform(AffineTransform transf);

    /**
	 * Consulta de si un raster tiene rotaci�n o no.
	 * @return true si tiene rotaci�n y false si no la tiene.
	 */
    public boolean isRotated();

    /**
	 * Crea un un nuevo dataset que referencia al mismo fichero en disco
	 * @return IRasterDataSource
	 */
    public IRasterDataSource newDataset();

    /**
	 * Dado unas coordenadas reales, un tama�o de buffer y un tama�o de raster. 
	 * Si el buffer es de mayor tama�o que el raster (supersampleo) quiere decir que 
	 * por cada pixel de buffer se repiten varios del raster. Esta funci�n calcula el 
	 * n�mero de pixels de desplazamiento en X e Y que corresponden al primer pixel del
	 * buffer en la esquina superior izquierda. Esto es necesario porque la coordenada
	 * solicitada es real y puede no caer sobre un pixel completo. Este calculo es
	 * util cuando un cliente quiere supersamplear sobre un buffer y que no se lo haga
	 * el driver autom�ticamente.
	 * @param dWorldTLX Coordenada real X superior izquierda
	 * @param dWorldTLY Coordenada real Y superior izquierda
	 * @param nWidth Ancho del raster
	 * @param nHeight Alto del raster
	 * @param bufWidth Ancho del buffer
	 * @param bufHeight Alto del buffer
	 * @return Array de cuatro. Los dos primeros elementos son el desplazamiento en X e Y y los dos segundos
	 * el tama�o en pixels de buffer de un pixel de la imagen en ancho y alto.  
	 */
    public double[] calcSteps(double dWorldTLX, double dWorldTLY, double dWorldBRX, double dWorldBRY, double nWidth, double nHeight, int bufWidth, int bufHeight);

    /**
	 * Obtiene en un array de String la lista de nombres de ficheros. 
	 * @param i Columna para un CompositeDataset 
	 * @param j Fila para un CompositeDataset
	 * @return lista de nombres de los ficheros del GeoRasterMultiFile
	 */
    public String[] getNameDatasetStringList(int i, int j);

    /**
	 * Obtiene el estado de transparencia a partir de los estados de transparencia de todos
	 * los ficheros que lo componen. Si varios de los ficheros que lo componen tienen banda de 
	 * transparencia estas tendr�n que ser mezcladas sobre una banda de transparencia �nica.
	 * @return Objeto FileTransparency con el estado de transparencia
	 */
    public Transparency getTransparencyFilesStatus();

    /**
	 * Obtiene la lista de tablas de color correspondiente a todos los ficheros que forman el raster
	 * @return Paleta asociada a este o null si no tiene. Una posici�n null en el array tambi�n indica que
	 * para ese fichero no hay paletas asociadas.
	 */
    public ColorTable[] getColorTables();

    /**
	 * Calcula el tama�o en byte de los raster.
	 * @return tama�o en bytes de todos los ficheros de la lista
	 */
    public long getFileSize();

    /**
	 * Asigna las bandas dibujables cuando se solicita una acci�n de lectura de datos
	 * @param db int[]
	 */
    public void setDrawableBands(int[] db);

    /**
	 * Asigna las bandas dibujables cuando se solicita una acci�n de lectura de datos
	 * @param db int[]
	 */
    public void clearDrawableBands();

    /**
	 * Para este dataset asigna que bandas se pintaran
	 * sobre el RasterBuf cuando se haga un update. Especificamos a 
	 * trav�s de los par�metros para que posici�n del RasterBuf ir� 
	 * dibujada con que banda del fichero de imagen.
	 * @param posRasterBuf	Posici�n del RasterBuf que queremos pintar.
	 * @param imageBand	Banda de la imagen que se pintar�
	 */
    public void addDrawableBand(int posRasterBuf, int imageBand);

    /**
	 * Consulta si el buffer siguiente a pedir es de solo lectura o lectura y escritura.
	 * La asignaci�n del flag de solo lectura a true debe hacerse para cada consulta.
	 * @return true si la siguiente carga de buffer se hace de solo lectura y false si es de lectura/escritura 
	 */
    public boolean isReadOnly();

    /**
	 * Asigna el flag que dice si la carga del siguiente buffer es de solo lectura o lectura/escritura.
	 * El poner este flag a true hace que se ponga a false el de memory autom�ticamente.
	 * La asignaci�n del flag de solo lectura a true debe hacerse para cada consulta.
	 * @param readOnly true si la siguiente carga de buffer se hace de solo lectura y false si es de lectura/escritura
	 */
    public void setReadOnly(boolean readOnly);

    /**
	 * Consulta si el buffer siguiente a pedir es se est� forzando a que sea cargado en memoria. Si no se fuerza
	 * puede cargarse en memoria o cacheado, seg�n decida el dataset.
	 * @return true si la siguiente carga se forzar� a memoria y false si no se fuerza. 
	 */
    public boolean isMemoryBuffer();

    /**
	 * Asigna el flag que dice si la carga del siguiente buffer es en memoria. El poner este flag a true hace que se
	 * ponga a false el de readOnly autom�ticamente.
	 * @param memory true si la siguiente carga de buffer se hace en memoria y false se deja decidir al dataset 
	 * el tipo de buffer
	 */
    public void setMemoryBuffer(boolean readOnly);

    /**
	 * Obtiene el n�mero de overviews de una banda
	 * @return N�mero de overviews del raster.
	 */
    public int getOverviewCount(int band) throws BandAccessException, RasterDriverException;

    /**
	 * Informa de si el dataset soporta overviews o no.
	 * @return true si soporta overviews y false si no las soporta.
	 */
    public boolean overviewsSupport();

    /**
	 * Guarda en el RMF el objecto actual en caso de que exista un serializador para el.
	 * El tipo del objeto se especifica en el parametro class1. Se usa el fichero pasado
	 * por parametro
	 * Esto nos puede permitir poder poner a null un valor y encontrar su serializador.
	 * @param value
	 * @throws RmfSerializerException 
	 */
    public void saveObjectToRmf(int file, Class class1, Object value) throws RmfSerializerException;

    /**
	 * Carga un objecto desde un serializador usando el tipo del mismo objeto pasado por parametro.
	 * Usa value para iniciar dicho serializador
	 * @param value
	 * @return
	 * @throws RmfSerializerException 
	 */
    public Object loadObjectFromRmf(Class class1, Object value) throws RmfSerializerException;
}
