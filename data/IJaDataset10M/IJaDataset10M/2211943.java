package net.sourceforge.desert;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import static java.lang.Math.*;

/**
 *
 * @author codistmonk (creation 2010-04-17)
 */
public class Utilities {

    /**
     * Private default constructor to ensure that the class isn't instantiated.
     */
    private Utilities() {
    }

    public static final String RESOURCE_BASE = "net/sourceforge/desert/resources/";

    /**
     *
     * @param iterator
     * <br>Should no be null
     * <br>Input-output parameter
     * @return the next available element if there is one, or else null
     * <br>A possibly null value
     * <br>A reference
     */
    public static final <T> T next(final Iterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * TODO move this function into a ParticleUtilities class, or ParticleDefaultImplementation itself
     * @param particle
     * <br>Should not be null
     * @param deltaX
     * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.POSITIVE_INFINITY[</code>
     * @param deltaY
     * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.POSITIVE_INFINITY[</code>
     */
    public static final void move(final ParticleDefaultImplementation particle, final float deltaX, final float deltaY) {
        particle.setX(particle.getX() + deltaX);
        particle.setY(particle.getY() + deltaY);
    }

    /**
     * TODO move this function into a ParticleUtilities class
     * @param particle1
     * <br>Should not be null
     * @param particle2
     * <br>Should not be null
     * @return <code>distance(particle1, particle2) <= particle1.getRadius() + particle2.getRadius()</code>
     * <br>A non-null value
     */
    public static final Boolean collision(final Particle particle1, final Particle particle2) {
        return distance(particle1, particle2) <= particle1.getType().getRadius() + particle2.getType().getRadius();
    }

    /**
     * TODO move this function into a ParticleUtilities class
     * @param particle1
     * <br>Should not be null
     * @param particle2
     * <br>Should not be null
     * @return <code>sqrt(square(particle1.getX() - particle2.getX()) + square(particle1.getY() - particle2.getY()))</code>
     * <br>Range: <code>[0F .. Float.POSITIVE_INFINITY[</code>
     */
    public static final float distance(final Particle particle1, final Particle particle2) {
        return length(particle1.getX() - particle2.getX(), particle1.getY() - particle2.getY());
    }

    /**
     *
     * @param deltaX
     * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.OSITIVE_INFINITY[</code>
     * @param deltaY
     * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.OSITIVE_INFINITY[</code>
     * @return
     * <br>Range: <code>[0F .. Float.POSITIVE_INFINITY[</code>
     */
    public static final float length(final float deltaX, final float deltaY) {
        return LengthAlgorithm.EUCLIDIAN.length(deltaX, deltaY);
    }

    /**
     *
     * @param value
     * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.POSITIVE_INFINITY[</code>
     * @return
     * <br>Range: <code>[0F .. Float.POSITIVE_INFINITY[</code>
     */
    public static final float square(final float value) {
        return value * value;
    }

    /**
     *
     * @param resourcePath
     * <br>Should not be null
     * <br>Eg: "net/sourceforge/desert/resources/images/"
     * @return An unmodifiable list
     * <br>A non-null value
     * @throws IOException
     */
    public static final List<String> listSubresources(final String resourcePath) throws IOException {
        final URL resourceURL = Utilities.class.getClassLoader().getResource(resourcePath);
        if (resourceURL.getProtocol().equals("jar")) {
            final JarURLConnection connection = (JarURLConnection) resourceURL.openConnection();
            return listSubentryNames(connection.getJarFile(), connection.getEntryName());
        } else {
            return listSubfilePaths(resourcePath);
        }
    }

    /**
     * Lists the subfiles of <code>path</code>.
     * @param resourcePath
     * <br>Should not be null
     * @return An unmodifiable list; empty if <code>path</code> is not a directory
     * <br>A non-null value
     * <br>A new value
     * @throws IOException
     */
    public static List<String> listSubfilePaths(final String resourcePath) throws IOException {
        final List<String> result = new ArrayList<String>();
        final File file = new File(Utilities.class.getClassLoader().getResource(resourcePath).getFile());
        if (file.isDirectory()) {
            final File[] subfiles = file.listFiles();
            if (subfiles == null) {
                throw new IOException("Could not list subfiles for " + file);
            }
            for (final File subFile : subfiles) {
                result.add(resourcePath + File.separator + subFile.getName());
            }
        } else {
            result.add(resourcePath);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Lists the subentries of the entry named <code>entryName</code> in <code>jarFile</code>.
     * @param jarFile
     * <br>Should not be null
     * @param entryName
     * <br>Should not be null
     * @return An unmodifiable list; empty if the entry is not a directory
     * <br>A new value
     * <br>A non-null value
     */
    public static final List<String> listSubentryNames(final JarFile jarFile, final String entryName) {
        final List<String> result = new ArrayList<String>();
        for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
            final String otherEntryName = entries.nextElement().getName();
            if (otherEntryName.startsWith(entryName) && !otherEntryName.equals(entryName + "/")) {
                result.add(otherEntryName);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     *
     * @return an unmodifiable list
     * <br>A non-null value
     * <br>A new value
     * @throws RuntimeException if an IO exception occurs
     */
    public static final List<String> listBrushPaths() {
        try {
            final List<String> result = new LinkedList<String>(listSubresources(RESOURCE_BASE + "images/"));
            for (final Iterator<String> iterator = result.iterator(); iterator.hasNext(); ) {
                final String resourcePath = iterator.next();
                if (!getResourceName(resourcePath).startsWith("brush_")) {
                    iterator.remove();
                }
            }
            return Collections.unmodifiableList(result);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     *
     * @param resourcePath
     * <br>Should not be null
     * @return
     * <br>A non-null value
     */
    public static final String getResourceName(final String resourcePath) {
        return resourcePath.substring(resourcePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * A brush resource is an image where non-black pixels represent a particle generation point.
     * @param resourcePath
     * <br>Should not be null
     * <br>Eg: "org/sourceforge/desert/resources/images/brush_dot_1x1.png"
     * @return A boolean representation of the pixels of the resource image,
     * where <code>true</code> indicate non-black pixels
     * <br>A non-null value
     * <br>A new value
     * @throws RuntimeException if the resource couldn't be read
     */
    public static final boolean[][] getBrush(final String resourcePath) {
        try {
            final BufferedImage brush = ImageIO.read(Utilities.class.getClassLoader().getResourceAsStream(resourcePath));
            final boolean[][] result = new boolean[brush.getHeight()][brush.getWidth()];
            for (int y = 0; y < brush.getHeight(); ++y) {
                for (int x = 0; x < brush.getWidth(); ++x) {
                    result[y][x] = (brush.getRGB(x, y) & 0x00FFFFFF) != 0;
                }
            }
            return result;
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Concatenates the source location of the call and the string representations
     * of the parameters separated by spaces.
     * <br>This is method helps to perform console debugging using System.out or System.err.
     * @param stackIndex 1 is the source of this mehod, 2 is the source of the call, 3 is the source of the call's caller, and so forth
     * <br>Range: <code>[O .. Integer.MAX_VALUE]</code>
     * @param objects
     * <br>Should not be null
     * @return
     * <br>A new value
     * <br>A non-null value
     */
    public static final String debug(final int stackIndex, final Object... objects) {
        final StringBuilder builder = new StringBuilder(Thread.currentThread().getStackTrace()[stackIndex].toString());
        for (final Object object : objects) {
            builder.append(" ").append(object);
        }
        return builder.toString();
    }

    /**
     * Prints on the standard output the concatenation of the source location of the call
     * and the string representations of the parameters separated by spaces.
     * @param objects
     * <br>Should not be null
     */
    public static final void debugPrint(final Object... objects) {
        System.out.println(debug(3, objects));
    }

    /**
     *
     * @author codistmonk (2010-04-28)
     */
    public static enum LengthAlgorithm {

        MAXIMUM {

            @Override
            public final float length(final float deltaX, final float deltaY) {
                return max(abs(deltaX), abs(deltaY));
            }
        }
        , EUCLIDIAN {

            @Override
            public final float length(final float deltaX, final float deltaY) {
                return (float) sqrt(square(deltaX) + square(deltaY));
            }
        }
        ;

        /**
         *
         * @param x
         * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.POSITIVE_INFINITY[</code>
         * @param y
         * <br>Range: <code>]Float.NEGATIVE_INFINITY .. Float.POSITIVE_INFINITY[</code>
         * @return
         * <br>Range: <code>[0F .. Float.POSITIVE_INFINITY[</code>
         */
        public abstract float length(float x, float y);
    }

    public static float jitter(final float value) {
        return value + (float) Math.random();
    }
}
