package fr.ird.database.sample;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.Arrays;
import java.util.Date;
import java.rmi.RemoteException;
import org.opengis.referencing.operation.TransformException;
import org.geotools.cv.Coverage;
import org.geotools.gc.GridCoverage;
import org.geotools.pt.CoordinatePoint;
import org.geotools.cs.CoordinateSystem;
import fr.ird.operator.coverage.Evaluator;
import fr.ird.database.coverage.SeriesEntry;
import fr.ird.database.coverage.CoverageTable;
import fr.ird.database.coverage.GridCoverage3D;

/**
 * Valeurs d'une {@linkplain SeriesEntry s�rie d'images} � des positions d'�chantillons.
 * Cette couverture offre une m�thode {@link #evaluate(SampleEntry,RelativePositionEntry,double[])
 * evaluate(...)} qui est capable d'adapter son calcul en fonction des donn�es de l'�chantillon.
 * Par exemple, le calcul pourrait se faire dans une r�gion g�ographique dont la taille d�pend de
 * la longueur de la palangre.
 *
 * @version $Id: Environment3D.java,v 1.1 2004/11/10 08:41:50 desruisseaux Exp $
 * @author Martin Desruisseaux
 */
public class Environment3D extends GridCoverage3D implements Coverage3D {

    /**
     * Incertitude dans la d�termination de la date des �chantillons, en nombre de millisecondes.
     * Afin de r�duire les interpolations temporelles, les dates des �chantillons sont "cal�es"
     * sur les dates des images sans que la diff�rence soit sup�rieure � ce nombre.
     * La valeur par d�faut est de 24 heures.
     */
    private static final long TIME_UNCERTAINTY = 24 * 60 * 60 * 1000L;

    /**
     * Petite valeur � utiliser lors des v�rifications. Cette valeur devrait �tre pr�s
     * de celle du type <code>float</code> (et non celle du type <code>double</code>).
     */
    private static final double EPS = 1E-5;

    /**
     * La longueur du demi-axe le long de l'axe des <var>x</var>.
     *
     * @task TODO: Rendre ce param�tre configurable dans une version future.
     */
    private static final double semiX = 10.0 / 60;

    /**
     * La longueur du demi-axe le long de l'axe des <var>y</var>.
     *
     * @task TODO: Rendre ce param�tre configurable dans une version future.
     */
    private static final double semiY = 10.0 / 60;

    /**
     * Le dernier avertissement report�, ou <code>null</code> s'il n'y en a pas.
     */
    transient LogRecord lastWarning;

    /**
     * <code>true</code> si cette impl�mentation est l'impl�mentation par d�faut. Dans ce
     * dernier cas, {@link #evaluate(SampleEntry,double[])} pourra profiter de la m�thode
     * {@link #evaluate(Point2D,Date,double[])}, qui applique une interpolation temporelle.
     * Ce bricolage est temporaire et pourra �tre supprim� lorsque {@link #getGridCoverage2D}
     * retournera une image interpol�e.
     */
    private final boolean isDefaultImplementation;

    /**
     * Construit une couverture � partir des donn�es de la table sp�cifi�e.
     *
     * @param  table Table d'o� proviennent les donn�es.
     * @throws RemoteException si l'interrogation du catalogue a �chou�.
     * @throws TransformException si une transformation de coordonn�es �tait n�cessaire et a �chou�.
     */
    public Environment3D(final CoverageTable table) throws RemoteException, TransformException {
        this(table, table.getCoordinateSystem());
    }

    /**
     * Construit une couverture � partir des donn�es de la table sp�cifi�e.
     *
     * @param  table Table d'o� proviennent les donn�es.
     * @param  cs Le syst�me de coordonn�es � utiliser pour cet obet {@link Coverage}.
     *         Ce syst�me de coordonn�es doit obligatoirement comprendre un axe temporel.
     * @throws RemoteException si l'interrogation du catalogue a �chou�.
     * @throws TransformException si une transformation de coordonn�es �tait n�cessaire et a �chou�.
     */
    public Environment3D(final CoverageTable table, final CoordinateSystem cs) throws RemoteException, TransformException {
        super(table, cs);
        isDefaultImplementation = Environment3D.class.equals(getClass());
    }

    /**
     * Construit une couverture utilisant les m�mes param�tres que la couverture sp�cifi�e.
     */
    protected Environment3D(final GridCoverage3D source) {
        super(source);
        isDefaultImplementation = Environment3D.class.equals(getClass());
    }

    /**
     * �value les valeurs du param�tre g�ophysique pour un �chantillon. La r�gion g�ographique
     * ainsi que la date des donn�es environnementales � utiliser sont d�termin�es � partir de
     * <code>position.{@link RelativePositionEntry#getCoordinate getCoordinate}(sample)</code> et
     * <code>position.{@link RelativePositionEntry#getTime getTime}(sample)</code>. La couverture
     * spatiales des donn�es est obtenues par un appel �
     * <code>{@link #getCoverage getCoverage}(sample)<code>.
     * Toutes ces m�thodes peuvent �tre red�finies.
     *
     * @param  sample �chantillon pour lequel on veut les param�tres environnementaux.
     * @param  position Position relative � laquelle �valuer les param�tres environnementaux, ou
     *                  <code>null</code> pour les �valuer � la position exacte de l'�chantillon.
     * @param  dest Tableau de destination, ou <code>null</code>.
     * @return Les param�tres environnementaux pour l'�chantillon sp�cifi�.
     */
    public synchronized double[] evaluate(final SampleEntry sample, final RelativePositionEntry position, double[] dest) {
        final Date time;
        final Point2D coord;
        if (position != null) {
            time = position.getTime(sample);
            coord = position.getCoordinate(sample);
        } else {
            time = sample.getTime();
            coord = sample.getCoordinate();
        }
        adjust(time);
        if (isDefaultImplementation) {
            dest = evaluate(coord, time, dest);
            assert testGridCoverage2D(coord, time, dest);
            return dest;
        }
        final Coverage coverage = getCoverage(sample, time);
        if (coverage == null) {
            final int numBands = getNumSampleDimensions();
            if (dest == null) {
                dest = new double[numBands];
            }
            Arrays.fill(dest, 0, numBands, Double.NaN);
            return dest;
        }
        if (coverage instanceof Evaluator) {
            return ((Evaluator) coverage).evaluate(getShape(sample, coord), dest);
        }
        if (coverage instanceof GridCoverage) {
            return ((GridCoverage) coverage).evaluate(coord, dest);
        }
        return coverage.evaluate(new CoordinatePoint(coord), dest);
    }

    /**
     * Retourne toute la couverture spatiale disponible des donn�es environnementales
     * pour l'�chantillon sp�cifi�. Cette couverture sera utilis�e par la m�thode
     * <code>{@link #evaluate(SampleEntry,RelativePositionEntry,double[]) evaluate}(sample, ...)</code>
     * L'impl�mentation par d�faut retourne simplement
     * <code>{@link #getGridCoverage2D getGridCoverage2D}(time)</code>.
     * Les classes d�riv�es peuvent red�finir cette m�thode pour appliquer une op�ration sur
     * l'image avant de la retourner. Les op�rations de type {@link Evaluator} sont trait�es
     * d'une fa�on sp�ciale afin profiter de leur m�thode {@link Evaluator#evaluate(Shape,double[])}.
     *
     * @param  sample L'�chantillon pour laquelle on veut la couverture des donn�es environnementales.
     * @param  time La date pour laquelle on veut l'image. Cette date n'est pas n�cessairement �gale
     *         � celle de l'�chantillon.
     * @return La couverture de donn�es environnementales pour l'�chantillon sp�cifi�.
     */
    protected Coverage getCoverage(final SampleEntry sample, final Date time) {
        return getGridCoverage2D(time);
    }

    /**
     * Compare les valeurs calcul�es par {@link #getGridCoverage2D} avec le tableau de
     * valeurs sp�cifi�. Cette m�thode sert � v�rifier la coh�rence des interpolations.
     */
    private boolean testGridCoverage2D(final Point2D coord, final Date time, final double[] values) {
        final GridCoverage coverage;
        try {
            coverage = getGridCoverage2D(time);
        } catch (IllegalArgumentException exception) {
            SampleDataBase.LOGGER.warning(exception.getLocalizedMessage());
            return true;
        }
        if (coverage != null) {
            final double[] tests = coverage.evaluate(coord, (double[]) null);
            if (tests.length != values.length) {
                return false;
            }
            for (int i = 0; i < values.length; i++) {
                final double v = values[i];
                final double t = tests[i];
                if (Math.abs(t - v) > EPS * Math.max(Math.abs(t), Math.abs(v))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Ajuste la date de l'�chantillon si elle est proche de la date d'une image.
     * Cette m�thode retourne une date dans les 24 heures avant ou apr�s l'�chantillon,
     * mais dont l'heure a �t� ajust�e de fa�on � coller � celle des donn�es satellitaires
     * disponibles. On �vite ainsi des interpolations si une image est disponible le jour de
     * l'�chantillon. Cette d�marche est justifi�e par le fait que l'heure de la p�che n'est
     * pas bien connue. Dans ces conditions, interpoler entre deux images s�par�es de 24 heures
     * n'a pas beaucoup de sens.
     *
     * @param  time En entr�, date d'un �chantillon. En sortie, une date et heure proche de
     *         celle de l'�chantillon (� moins de 24 heures), mais �ventuellement ajust�e pour
     *         correspondre � celles des images.
     */
    final void adjust(final Date time) {
        if (TIME_UNCERTAINTY != 0) {
            final long sampleTime = time.getTime();
            snap(null, time);
            final long imageTime = time.getTime();
            long dt = sampleTime - imageTime;
            dt = (dt / TIME_UNCERTAINTY) * TIME_UNCERTAINTY;
            time.setTime(imageTime + dt);
            assert Math.abs(time.getTime() - sampleTime) < TIME_UNCERTAINTY : dt;
        }
    }

    /**
     * Retourne la r�gion g�ographique � prendre en compte pour les calculs relatifs �
     * l'�chantillon sp�cifi�e. Cette r�gion g�ographique inclue g�n�ralement les coordonn�es
     * de la p�che, mais pas obligatoirement. Des classes d�riv�es pourraient red�finir cette
     * m�thode pour s'int�resser par exemple � ce qui se passe seulement � l'ouest de la
     * zone de p�che. Cette m�thode est appel�e par
     * <code>{@link #evaluate(SampleEntry,RelativePositionEntry,double[]) evaluate}(sample, ...)</code>
     * lorsque la couverture des donn�es environnementales est un objet {@link Evaluator}.
     *
     * @param  sample Capture pour laquelle �valuer les param�tres environnementaux.
     * @param  coord  Coordonn�e � laquelle �valuer les param�tres environnementaux.
     *                Cette coordonn�e aura �t� obtenue avec {@link RelativePosition#getCoordinate}.
     * @return R�gion g�ographique � prendre en compte pour cet �chantillon.
     */
    protected Shape getShape(final SampleEntry sample, final Point2D coord) {
        return new Ellipse2D.Double(coord.getX() - semiX, coord.getY() - semiY, 2 * semiX, 2 * semiY);
    }

    /**
     * Enregistre un message vers le journal des �v�nements. Cette m�thode red�finie celle de
     * {@link GridCoverage3D} de fa�on � n'enregistrer que le premier et dernier message d'une
     * succession de points en dehors de la r�gion spatio-temporelle.
     */
    protected void log(final LogRecord record) {
        if (lastWarning != null) {
            super.log(lastWarning);
            lastWarning = null;
        }
        super.log(record);
    }
}
