package com.visitrend.ndvis.parameters.api;

import com.visitrend.ndvis.app.NDVis;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import com.visitrend.ndvis.event.api.DataInfoChangedEvent;
import com.visitrend.ndvis.event.api.NDVisListener;
import com.visitrend.ndvis.event.api.NDVisListenerAdapter;
import com.visitrend.ndvis.gui.spi.DataVisualization;
import com.visitrend.ndvis.model.DataInfoINF;
import java.awt.Container;
import javax.swing.JComponent;
import org.openide.util.lookup.ServiceProvider;

/**
 * <p>
 * This class provides utilities for parsing ints to parameter values, vice
 * versa, and more. It is a way of isolating the "mixed radix" (Knuth's term,
 * not mine) math that's done on parameters, points and so forth.
 * </p>
 * 
 * <p>
 * This is not necessarily threadsafe as 2 threads could access a single method
 * at the same time and "warp" eachothers short[] values. To make it threadsafe,
 * just add a synchronized clause to each method. I haven't done that here
 * because it will slow things down (even with only 1 thread, not because of
 * bottle necking but because of the synchronization process in java). Also, if
 * methods are introduced that deal with local variables, you might need to add
 * a synchronized(this) in those methdods.
 * </p>
 * 
 * <p>
 * One general pattern in this class is that methods take an order, bases, and
 * parameterValues or int argument. The bases argument is a short[] of the bases
 * of parameters, IN DEFAULT ORDER. These are typically retrieved from the
 * DataInfo class which keep the bases in default order, thus the methods in
 * here always assume the bases argument has the bases in default order. The
 * parameterValues however are considered to be in the order of the short[]
 * order parameter. This is very important. That said, given a particular order,
 * in order to create an in from parameter values, or vice versa, one must
 * consider the bases in that particular order, and the parameter values in that
 * particular order. Thus, the methods in here (since they consider bases to
 * always be in default order) will typically reorder bases, perform
 * computation, and then return the result. Sometimes, if the result is
 * parameter values, the methods in here will reorder them to be in default
 * order, like the bases when passed in.
 * </p>
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 * 
 */
@ServiceProvider(service = ParametersUtils.class)
public class ParametersUtils extends NDVisListenerAdapter implements NDVisListener {

    short[] tmp1, tmp2, xTmp1, xTmp2, yTmp1, yTmp2, bases;

    /**
     * Only instantiate this class (i.e. use this constructor) if you are
     * planning on using any of the non-static methods).
     * 
     */
    public ParametersUtils() {
        DataInfoINF di = NDVis.getDefault().getDataInfo();
        updateLocalVariables(di);
        NDVis.getDefault().addNDVisListener(this);
    }

    /**
     * @param order
     *            any ordering, usually the current user ordering of parameters
     * @param shorts
     *            assumed in default order, 0, 1, 2, etc. - we'll reorder to the
     *            order in "order"
     * @param answerHolder
     *            a short[] for holding the answer
     * @return the shorts parameter, reordered according to the order order
     */
    public static short[] reOrderShorts(short[] order, short[] shorts, short[] answerHolder) {
        for (int k = 0; k < answerHolder.length; k++) {
            answerHolder[k] = shorts[order[k]];
        }
        return answerHolder;
    }

    /**
     * This calculates the number of possible values, including 0, that the
     * bases parameter (considered as a number of bases.length digits where each
     * digit has its base represented in bases) can represent. For example, if
     * the short[] bases parameter was {2, 2, 2, 2, 2, 2, 2, 2} that would
     * represent a traditional short, or a binary number with 8 bits, which has
     * 256 possible values including 0. I'm differentiating that from the max
     * value, which would be 255.
     * 
     * @param bases
     * @return the number of possible values, including 0, that the bases
     *         parameter (considered as a number of bases.length digits where
     *         each digit has its base represented in bases) can represent
     */
    public int getNumberOfPossibleValuesForBases(short[] bases) {
        int i = bases[0];
        for (int k = 1; k < bases.length; k++) {
            i = i * bases[k];
        }
        return i;
    }

    /**
     * This method extracts the bases for the parameters on the X axis. It's
     * important to note that the bases parameter is considered to be the bases
     * in default order. If you just wanted to extract the first x-many values
     * of an array, you can use a for loop. This isn't much more, but takes into
     * consideration the current order of parameters. The answerHolder
     * parameter's length is used to determine the number of parameters on the X
     * axis.
     * 
     * @param order
     *            an ordering of all parameters (not just X parameters),
     *            typically the current ordering
     * @param bases
     *            the bases for all parameters (not just X parameters), IN
     *            DEFAULT ORDER (i.e. the order of bases in DataInfo)
     * @param answerHolder
     *            a short[] for holding the answer. answerHolder.length
     *            implicitly tells us how many parameters to assume are on the X
     *            axis.
     * @return answerHolder with values set to the bases of the parameters of
     *         the X axis and rearranged to the order parameter.
     */
    public static short[] extractXbases(short[] order, short[] bases, short[] answerHolder) {
        for (int k = 0; k < answerHolder.length; k++) {
            answerHolder[k] = bases[order[k]];
        }
        return answerHolder;
    }

    /**
     * The order parameter should be the same length as the local bases and
     * related arrays. If its not, errors will be thrown. Also, we have to know
     * the number of parameters on X. If it is different than we think, we'll
     * have to reconstruct local variables. If you're calling this method from
     * code that will repeatedly pass in different values for numParametersOnX,
     * or code that doesn't rely on the currentOrder reflected in the Parameters
     * object, then you should most likely use
     * {@link #extractXbases(short[], short[], short[])} to prevent the
     * repetitive reconstruction of local short[]s.
     * 
     * @param order
     * @return a short[] reflecting the bases on the x axis given the order of
     *         parameters specified in the short[] order parameter
     */
    public short[] extractXbases(short[] order, int numParametersOnX) {
        updateInternalXandYarraysIfNecessary(numParametersOnX);
        return extractXbases(order, bases, new short[numParametersOnX]);
    }

    /**
     * This method extracts the bases for the parameters on the Y axis. It's
     * important to note that the bases parameter is considered to be the bases
     * in default order. If you just wanted to extract the first y-many values
     * of an array, you can use a for loop. This isn't much more, but takes into
     * consideration the current order of parameters. The answerHolder
     * parameter's length is used to determine the number of parameters ont he Y
     * axis.
     * 
     * @param order
     *            an ordering of all parameters (not just Y parameters),
     *            typically the current ordering
     * @param bases
     *            the bases for all parameters (not just Y parameters) IN
     *            DEFAULT ORDER i.e. the order of bases in DataInfo)
     * @param answerHolder
     *            a short[] for holding the answer - the length of answerHolder
     *            should be the number of parameters on the Y axis
     * @return answerHolder with values set to the bases of the parameters of
     *         the Y axis and rearranged to the order parameter.
     */
    public static short[] extractYbases(short[] order, short[] bases, short[] answerHolder) {
        int numParametersOnX = bases.length - answerHolder.length;
        for (int k = 0; k < answerHolder.length; k++) {
            answerHolder[k] = bases[order[numParametersOnX + k]];
        }
        return answerHolder;
    }

    /**
     * The order parameter should be the same length as the local bases and
     * related arrays. If its not, errors will be thrown. Also, we have to know
     * the number of parameters on Y (we infer the number of parameters on X
     * from this value for use in an internal method). If it is different than
     * we think, we'll have to reconstruct local variables. If you're calling
     * this method from code that will repeatedly pass in different values for
     * numParametersOnX, or code that doesn't rely on the currentOrder reflected
     * in the Parameters object, then you should most likely use
     * {@link #extractXbases(short[], short[], short[])} to prevent the
     * repetitive reconstruction of local short[]s.
     * 
     * @param order
     * @param numParametersOnY
     * @return a short[] holding the values for the parameters on the y axis
     *         given the order parameter
     */
    public short[] extractYbases(short[] order, int numParametersOnY) {
        updateInternalXandYarraysIfNecessary(order.length - numParametersOnY);
        return extractYbases(order, bases, new short[numParametersOnY]);
    }

    /**
     * <p>
     * The bases and answerHolder parameters should be the same length. This
     * parses a single integer num into the short[] answerHolder parameter. The
     * order of the short[] bases parameter is very important here, as that
     * figures into how the int is parsed. The int should have been created from
     * parameters that were in the order of the bases parameter - as this will
     * return those values. The basic algorithm for parsing (in a fashion that
     * allows different parameters to be different bases) is:
     * <ol>
     * <li> start at the least significant parameter p of the parameters in the
     * int num where p is the (n-1)th parameter of num (going left to right) and
     * n is the number of parameters in num. Remember, parameter is not the same
     * as digit, because num is a decimal representation of the parameters that
     * make it up.
     * <li>grab p's base b (the number of values parameter p can take on).
     * <li> store num modulus b as the value for p.
     * <li> divide num by p and pass the answer on as (new)num to the next
     * iteration
     * <li>repeat for n-2, n-3 and so on until you've covered all the
     * parameters in num
     * </ol>
     * This algorithm is coded in this method.
     * </p>
     * <p>
     * I have thought previously that parameters with more than 1 digit might
     * break this scheme, but I'm not actually sure if that's true.
     * </p>
     * 
     * @param num
     *            the int to be parsed into parameter values
     * @param bases
     *            the bases for each parameter - should be the same length as
     *            answerHolder.
     * @param answerHolder
     *            a short array that will be set to the answer, so that a new
     *            short[] is not created for the answer - for callers trying to
     *            conserve memory and garbage collection
     * @return num parsed to parameter values
     */
    public short[] parseIntToParameterValues(int num, short[] bases, short[] answerHolder) {
        int n = bases.length - 1;
        for (int k = n; k >= 0; k--) {
            answerHolder[k] = (short) (num % bases[k]);
            num = num / bases[k];
        }
        return answerHolder;
    }

    /**
     * This method parses paramValues to a single int. The order of the bases is
     * important. Simply, bases should reflect the bases of the parameters for
     * which values are in parameterValues - and they should all be in the same
     * order. The value at each index of parameterValues should be the value for
     * a parameter that has a base equal to the value, at the same index, in the
     * bases parameter.
     * 
     * @param paramValues
     *            values for parameters
     * @param bases
     *            the number of values each parameter can take on, the order of
     *            this must be the same as the order of paramValues if the
     *            returned int is to be accurate
     * @return an int representation of paramValues
     * 
     * 
     */
    public int parseParameterValuesToInt(short[] paramValues, short[] bases) {
        int answer = 0;
        for (int k = 0; k < bases.length; k++) {
            int a = paramValues[k];
            int n = k + 1;
            for (int j = n; j < bases.length; j++) {
                a = a * bases[j];
            }
            answer += a;
        }
        return answer;
    }

    /**
     * This method uses the bases from the local DataInfo (which should be the
     * same as the DataInfo in the main NDVis app) to parse the paramValues.
     * 
     * @param paramValues
     * @return paramValues parsed to an int given the bases in DataInfo
     */
    public int parseParameterValuesToInt(short[] paramValues) {
        return parseParameterValuesToInt(paramValues, bases);
    }

    /**
     * Remember, bases will typically be retrieved from DataInfo which stores
     * them in default order and doesn't change their order. Thus the bases
     * parameter is considered to be in default order, whereas the paramValues
     * parameter is considered to be in the order of the short[] order
     * parameter.
     * 
     * @param paramValues
     *            values for parameters in the order of the short[] order
     * @param order
     *            the order of the parameter values
     * @param bases
     *            the bases for parameters IN DEFAULT ORDER (not in the order of
     *            the short[] order parameter)
     * @param xTmp1
     *            tmp short[] for use in this method
     * @param xTmp2
     *            tmp short[] for use in this method
     * @param yTmp1
     *            tmp short[] for use in this method
     * @param yTmp2
     *            tmp short[] for use in this method
     * @return a point resulting from the paramValues, considered to be in the
     *         order represented by the order parameter, and given their bases
     *         reflected in the bases parameter
     */
    public Point2D parseParameterValuesToPoint(short[] paramValues, short[] order, short[] bases, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2) {
        int x, y;
        for (int k = 0; k < xTmp1.length; k++) {
            xTmp1[k] = paramValues[k];
        }
        x = parseParameterValuesToInt(xTmp1, extractXbases(order, bases, xTmp2));
        for (int k = 0; k < yTmp1.length; k++) {
            yTmp1[k] = paramValues[k + xTmp1.length];
        }
        y = parseParameterValuesToInt(yTmp1, extractYbases(order, bases, yTmp2));
        return new Point(x, y);
    }

    /**
     * This method rorders the bases to the order that the parameters are in
     * which is reflected in the passed in order short[], then parses the passed
     * in int to parameter values, and then uses those values to parse it into 2
     * ints and finally a point. This means the int is considered to be computed
     * from the parameter values, in their current order, with the bases in that
     * order as well, even though the bases are not in that order when they are
     * passed into this method. If these seems confusing, its because bases will
     * typically be retrieved from DataInfo when passed into these methods, and
     * those bases are always in default order. Whereas parameter values and
     * order will be retrieved from the Parameters object which will always be
     * in the current order and relative to that.
     * 
     * @param parsee
     *            the int to be parsed to a point
     * @param order
     *            an order of parameters, typically the current order
     * @param bases
     *            the bases of the parameters IN DEFAULT ORDER i.e. the order
     *            they are in when first instanciated in DataInfo
     * @param tmp1
     *            a short[] of the same length as order and bases to be used
     *            during execution of this method
     * @param tmp2
     *            a short[] of the same length as order and bases to be used
     *            during execution of this method
     * @param xTmp1
     *            a short[] whose length is the number of parameters on the X
     *            axis
     * @param xTmp2
     *            a short[] whose length is the number of parameters on the X
     *            axis
     * @param yTmp1
     *            a short[] whose length is the number of parameters on the Y
     *            axis
     * @param yTmp2
     *            a short[] whose length is the number of parameters on the Y
     *            axis
     * @return the passed in int parsee parsed to a point given the passed in
     *         order and bases for parameters
     */
    public Point2D parseIntToPoint(int parsee, short[] order, short[] bases, short[] tmp1, short[] tmp2, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2) {
        Point2D p;
        tmp1 = parseIntToParameterValues(parsee, ParametersUtils.reOrderShorts(order, bases, tmp2), tmp1);
        p = parseParameterValuesToPoint(tmp1, order, bases, xTmp1, xTmp2, yTmp1, yTmp2);
        return p;
    }

    /**
     * This method is not threadsafe as it uses local variables that could be
     * modified by other things. That's probably won't happen so I haven't
     * safeguarded, but if you wanted to safeguard, you'd have to do a
     * synchronize(this) since there are more than one local variables used, and
     * you wouldn't want to create a deadlock (i.e. by having things lock
     * variables in different orders - although I supposed you could try and
     * enforce that everything locks everything else in a very specific
     * order...but that still seems iffy).
     * 
     * Everytime this is called with a new value for numParametersOnX, 4
     * short[]s are created. Thus, if you have something calling this repeatedly
     * with different values for that, then you may want to not use this method
     * but instead manage your own short[]s and use the static method
     * {@link #parseIntToPoint(int, short[], short[], short[], short[], short[], short[], short[], short[]) parseIntToPoint(int, short[], short[], short[], short[], short[], short[], short[], short[])}
     * This method basically justs updates some local variables and returns the
     * passed in int parsed to a point.
     * 
     * @param parsee
     * @param order
     * @param numParametersOnX
     * @return the passed in int parsed to a point given the passed in order of
     *         parameters - the parameters' bases are derived from whatevers in
     *         DataInfo
     */
    public Point2D parseIntToPoint(int parsee, short[] order, int numParametersOnX) {
        this.updateInternalXandYarraysIfNecessary(numParametersOnX);
        return parseIntToPoint(parsee, order, bases, tmp1, tmp2, xTmp1, xTmp2, yTmp1, yTmp2);
    }

    /**
     * In this method, the values in the returned short[] are values for
     * parameters, left in the order of of the passed in short[] order.
     * Remember, bases will typically be retrieved from DataInfo which stores
     * them in default order and doesn't change their order. Thus the passed in
     * bases argument is considered to be in default order, whereas the passed
     * in paramValues are considered to be in the order of the passed in short[]
     * order.
     * 
     * @param x
     *            an int resulting from values for parameters on the X axis (in
     *            a particular order)
     * @param y
     *            an int resulting from values for parameters on the Y axis (in
     *            a particular order)
     * @param order
     *            the current order of parameters, used when creating x and y
     *            from parameter values
     * @param bases
     *            the bases for parameters IN DEFAULT ORDER (i.e. the order of
     *            bases in DataInfo)
     * @param xTmp1
     *            a short[] whose length is the number of parameters on the X
     *            axis
     * @param xTmp2
     *            a short[] whose length is the number of parameters on the X
     *            axis
     * @param yTmp1
     *            a short[] whose length is the number of parameters on the Y
     *            axis
     * @param yTmp2
     *            a short[] whose length is the number of parameters on the Y
     *            axis
     * @param answerHolder
     *            a short[] to hold the answer or return value
     * @return the parameter values, left in the order reflected in the passed
     *         in short[] order
     */
    public short[] parseXandYvalsToUnorderedParameterValues(int x, int y, short[] order, short[] bases, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2, short[] answerHolder) {
        xTmp1 = parseIntToParameterValues(x, extractXbases(order, bases, xTmp2), xTmp1);
        for (int k = 0; k < xTmp1.length; k++) {
            answerHolder[k] = xTmp1[k];
        }
        yTmp1 = parseIntToParameterValues(y, extractYbases(order, bases, yTmp2), yTmp1);
        for (int k = 0; k < yTmp1.length; k++) {
            answerHolder[k + xTmp1.length] = yTmp1[k];
        }
        return answerHolder;
    }

    /**
     * In this method, the values in the returned short[] are values for
     * parameters, left in the order of of the passed in short[] order.
     * Remember, bases will typically be retrieved from DataInfo which stores
     * them in default order and doesn't change their order. Thus the passed in
     * bases argument is considered to be in default order, whereas the passed
     * in paramValues are considered to be in the order of the passed in short[]
     * order.
     * 
     * @param p
     *            a point made up of an X coordinate created from values for
     *            parameters on the X axis and a Y coordinate created from
     *            values for parameters on the Y axis, both constructed relative
     *            to the current order of parameters reflected in the passed in
     *            short[] order
     * @param order
     *            the order of the parameters when constructing the x and y
     *            coordinate which constructed the passed in point p
     * @param bases
     *            the bases for parameters IN DEFAULT ORDER (i.e. the order of
     *            bases in DataInfo)
     * @param xTmp1
     *            a short[] whose length is the number of parameters on the X
     *            axis
     * @param xTmp2
     *            a short[] whose length is the number of parameters on the X
     *            axis
     * @param yTmp1
     *            a short[] whose length is the number of parameters on the Y
     *            axis
     * @param yTmp2
     *            a short[] whose length is the number of parameters on the Y
     *            axis
     * @param answerHolder
     *            a short[] to hold the answer or return value
     * @return the parameter values, left in the order reflected in the passed
     *         in short[] order
     */
    public short[] parsePointToUnorderedParameterValues(Point2D p, short[] order, short[] bases, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2, short[] answerHolder) {
        return parseXandYvalsToUnorderedParameterValues((int) p.getX(), (int) p.getY(), order, bases, xTmp1, xTmp2, yTmp1, yTmp2, answerHolder);
    }

    /**
     * 
     * Bases will typically be retrieved from DataInfo which stores them in
     * default order and doesn't change their order. Thus the passed in bases
     * argument is considered to be in default order. The passed in paramValues
     * are considered to be in the order of the passed in short[] order. Thus to
     * "order" them, we reorder them to default order. This method returns the
     * parameter values used to construct the passed in point, in default order
     * (i.e. the default order of parameterNames and bases in DataInfo).
     * 
     * @param p
     *            the point created from parameter values when they were in the
     *            order of the passed in short[] order (and the bases were
     *            considered to be in that order for point creation as well).
     * @param order
     *            the order of the parameters when used to create p
     * @param bases
     *            the bases of the parameters, IN DEFAULT ORDER (i.e. the order
     *            of bases in DataInfo)
     * @param tmp1
     * @param xTmp1
     * @param xTmp2
     * @param yTmp1
     * @param yTmp2
     * @param answerHolder
     * @return the parameter values used to construct the passed in point, in
     *         default order (i.e. the default order of parameterNames and bases
     *         in DataInfo).
     */
    public short[] parsePointToOrderedParameterValues(Point2D p, short[] order, short[] bases, short[] tmp1, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2, short[] answerHolder) {
        tmp1 = parsePointToUnorderedParameterValues(p, order, bases, xTmp1, xTmp2, yTmp1, yTmp2, tmp1);
        for (int k = 0; k < tmp1.length; k++) {
            answerHolder[order[k]] = tmp1[k];
        }
        return answerHolder;
    }

    public short[] parseDataVisualizationMouseEventToOrderedParameterValues(MouseEvent evt, short[] order, int numParametersOnX, short[] answerHolder) {
        Container parent = (Container) evt.getSource();
        while (!(parent instanceof DataVisualization)) {
            parent = parent.getParent();
        }
        DataVisualization imgpan = (DataVisualization) parent;
        int imageHeight = imgpan.getOffScreenImage().getHeight() - 1;
        AffineTransform atx = imgpan.getTransform();
        Point2D p = evt.getPoint();
        try {
            p = atx.inverseTransform(p, p);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        p.setLocation(p.getX(), imageHeight - p.getY());
        return parsePointToOrderedParameterValues(p, order, numParametersOnX, answerHolder);
    }

    /**
     * This method is not threadsafe as it relies on local variables. If you
     * want to make it threadsafe you'll have to do synchronized(this) (just
     * making the method synchronized won't do it because others hit the local
     * variables too, and trying to synchronize on each local field could create
     * deadlock issues).
     * 
     * Most likely this won't be a problem, so I haven't dealt with it here,
     * however if you think it may be a problem, then simply use the static
     * method
     * {@link #parsePointToOrderedParameterValues(Point2D, short[], short[], short[], short[], short[], short[], short[], short[]) parsePointToOrderedParameterValues()},
     * althouhg that could also have thread issues, you could resolve that one
     * by simply making that method synchronized (adding that modifier to the
     * declaration of the method).
     * 
     * @param p
     * @param order -
     *            this is the order the paramters are currently in (.e.g not the
     *            default order, they will be returned in the default order
     * @param numParametersOnX
     * @param answerHolder
     * @return the passed in point parsed to parameter values given the bases in
     *         DataInfo and the passed in order, then those values are reordered
     *         to be in the default order of parameters when DataInfo is
     *         constructed
     */
    public short[] parsePointToOrderedParameterValues(Point2D p, short[] order, int numParametersOnX, short[] answerHolder) {
        this.updateInternalXandYarraysIfNecessary(numParametersOnX);
        return parsePointToOrderedParameterValues(p, order, bases, tmp1, xTmp1, xTmp2, yTmp1, yTmp2, answerHolder);
    }

    public short[] parseXandYvalsToOrderedParameterValues(int x, int y, short[] order, short[] bases, short[] tmp, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2, short[] answerHolder) {
        tmp = parseXandYvalsToUnorderedParameterValues(x, y, order, bases, xTmp1, xTmp2, yTmp1, yTmp2, answerHolder);
        for (int k = 0; k < tmp.length; k++) {
            answerHolder[order[k]] = tmp[k];
        }
        return answerHolder;
    }

    /**
     * 
     * @param num
     *            the number to be parsed
     * @param order
     *            the order the parameters were in when num was created by them
     * @param bases
     *            the bases of the parameters considered in their defulat order
     * @param tmp1
     *            a tmp [] for use during computation, should be the same length
     *            as answerHolder
     * @param tmp2
     *            a tmp [] for use during computation, should be the same length
     *            as answerHolder
     * @param answerHolder
     *            a short[] for storing the answer, should be the same length as
     *            bases, order, tmp1, and tmp2
     * @return the parameter values used to create num, reordered to be in the
     *         default order of parameters (i.e. the order of parameterNames and
     *         bases in DataInfo).
     */
    public short[] parseIntToOrderedParameterValues(int num, short[] order, short[] bases, short[] tmp1, short[] tmp2, short[] answerHolder) {
        for (int k = 0; k < tmp1.length; k++) {
            tmp1[k] = bases[order[k]];
        }
        tmp2 = parseIntToParameterValues(num, tmp1, tmp2);
        for (int k = 0; k < tmp2.length; k++) {
            answerHolder[k] = tmp2[order[k]];
        }
        return answerHolder;
    }

    /**
     * 
     * @param x
     * @param y
     * @param order
     * @param bases
     * @param tmp1
     * @param tmp2
     * @param xTmp1
     * @param xTmp2
     * @param yTmp1
     * @param yTmp2
     * @return the passed in x and y values parsed to an int given the passed in
     *         order of parameters and bases
     */
    public int parseXandYvalsToInt(int x, int y, short[] order, short[] bases, short[] tmp1, short[] tmp2, short[] xTmp1, short[] xTmp2, short[] yTmp1, short[] yTmp2) {
        tmp1 = parseXandYvalsToOrderedParameterValues(x, y, order, bases, tmp1, xTmp1, xTmp2, yTmp1, yTmp2, tmp2);
        return parseParameterValuesToInt(tmp1, bases);
    }

    /**
     * The passed in short[] order should be the same length as the local bases
     * short[]. If it is not, then you should use
     * {@link #parseXandYvalsToInt(int, int, short[], short[], short[], short[], short[], short[], short[], short[])} -
     * also this will throw and exception related to array indexes if that's the
     * case. Also, this will reconstruct local variables based on
     * numParametersOnX if it needs to, thus if you are calling this several
     * times from code that will pass in different values for that, then you
     * should also use
     * {@link #parseXandYvalsToInt(int, int, short[], short[], short[], short[], short[], short[], short[], short[])}
     * instead to cut down on computation required for that constant
     * reconstruction...unless of course your code calls for that anyway.
     * 
     * 
     * 
     * @param x
     * @param y
     * @param order
     * @return the passed in x and y values parsed to an int given the passed on
     *         order for parameters and the bases specified in
     *         {@link com.visitrend.ndvis.model.DataInfo#bases}.
     */
    public int parseXandYvalsToInt(int x, int y, short[] order, int numParametersOnX) {
        this.updateInternalXandYarraysIfNecessary(numParametersOnX);
        return parseXandYvalsToInt(x, y, order, bases, tmp1, tmp2, xTmp1, xTmp2, yTmp1, yTmp2);
    }

    public static String formatParameterString(short[] paramValues, String[] parameterNames) {
        String str = "";
        for (int k = 0; k < parameterNames.length; k++) {
            str += parameterNames[k] + ": " + paramValues[k] + " , ";
        }
        return str;
    }

    /**
     * <p>
     * If the passed in numParametersOnX is not the length of of xTmp1, then we
     * know we have to update all short[]s reflecting info specific to
     * parameters as they are partitioned on each axis. This method check for
     * that and updates local fields if necessary.
     * </p>
     * <p>
     * Important: this is done relative to bases - and thus to the number of
     * parameters specified in DataInfo. If this method is called by something
     * that "thinks" there is a different number of parameters, then this will
     * lead to errors.
     * </p>
     * 
     * 
     * @param numParametersOnX
     */
    private void updateInternalXandYarraysIfNecessary(int numParametersOnX) {
        if (xTmp1 == null || xTmp1.length != numParametersOnX) {
            xTmp1 = new short[numParametersOnX];
            xTmp2 = new short[numParametersOnX];
            yTmp1 = new short[bases.length - numParametersOnX];
            yTmp2 = new short[bases.length - numParametersOnX];
        } else if ((bases.length - numParametersOnX) != yTmp1.length) {
            yTmp1 = new short[bases.length - numParametersOnX];
            yTmp2 = new short[bases.length - numParametersOnX];
        }
    }

    /**
     * This calss actually doesn't hold a DataInfo object, it merely grabs the
     * short[] bases from the passed in DataInfo and updates some local fields
     * accordingly.
     * 
     * @param di
     *            the dataInfo to set
     */
    public void updateLocalVariables(DataInfoINF di) {
        bases = di.getBases();
        if (tmp1 == null || tmp1.length != bases.length) {
            tmp1 = new short[bases.length];
            tmp2 = new short[bases.length];
        }
    }

    /**
     * 
     * @see com.visitrend.ndvis.event.NDVisListener#dataInfoChanged(com.visitrend.ndvis.event.DataInfoChangedEvent)
     */
    public void dataInfoChanged(DataInfoChangedEvent evt) {
        updateLocalVariables((DataInfoINF) evt.getSource());
    }
}
