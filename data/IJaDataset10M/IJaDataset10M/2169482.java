package org.aoplib4j.uml;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Writer that will generate the UML diagram as a PNG file. The writer use under
 * the hood the {@link SEQUENCEWriter} writer to generate a .seq diagram and 
 * then use SEQUENCE with the <code>--headless</code> option
 * which is used when you want to generate a .png image from an input file 
 * without displaying the SEQUENCE screen. 
 *  
 * The writer will create 2 files; the first one having as name the value passed
 * as parameter to the {@link Aoplib4jSequenceDiagram#diagramDepth()} (which is
 * a seq file) and the second file which is the png file.
 * 
 *  @see SEQUENCEWriter
 *  
 * @author Adrian Citu
 *
 */
public final class PngWriter extends DFSDiagramWriter {

    /**
     * the logger to use.
     */
    private static final Logger LOGGER = Logger.getLogger(PngWriter.class.getName());

    /**
     * the writer that will generate the .seq diagram.
     */
    private SEQUENCEWriter delegate = null;

    /**
     * The SEQUENCE main class name. 
     */
    private static final String SEQUENCE_MAIN_CLASS = "com.zanthan.sequence.Main";

    /**
     *  {@inheritDoc}
     *  
     * Delegate the writing of the footer to the attached delegate and
     * generate the png file. 
     */
    @Override
    public void writeFooter(final SequenceMethod meth) throws IOException {
        this.delegate.writeFooter(meth);
        this.generateThePngFile(this.getDiagramFullPath());
    }

    /**
     * Generate the png file. Verify if SEQUENCE jar is in the classpath
     * and then invoke by reflection the {@link #SEQUENCE_MAIN_CLASS} main
     * method with <code>--headless diagramFullPath</code> parameters.
     *  
     * @param diagramFullPath the diagram full path.
     */
    private void generateThePngFile(final String diagramFullPath) {
        Class<?> sequenceMainClass = PngWriter.loadClass(SEQUENCE_MAIN_CLASS);
        Method mainMethod = null;
        if (sequenceMainClass != null) {
            try {
                mainMethod = sequenceMainClass.getDeclaredMethod("main", new Class[] { String[].class });
                if (mainMethod != null) {
                    try {
                        mainMethod.invoke(null, new Object[] { new String[] { "--headless", diagramFullPath } });
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Exception invoking the main method of " + SEQUENCE_MAIN_CLASS + ": " + e.getMessage());
                    } catch (InvocationTargetException e) {
                        LOGGER.warning("Exception invoking the main method of " + SEQUENCE_MAIN_CLASS + ": " + e.getMessage());
                    }
                }
            } catch (SecurityException e) {
                LOGGER.warning("Exception retrieving by reflexion the main " + " method of " + SEQUENCE_MAIN_CLASS + ": " + e.getMessage());
            } catch (NoSuchMethodException e) {
                LOGGER.warning("Exception retrieving by reflexion the main " + " method of " + SEQUENCE_MAIN_CLASS + ": " + e.getMessage());
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Exception retrieving by reflexion the main " + " method of " + SEQUENCE_MAIN_CLASS + ": " + e.getMessage());
            }
        }
    }

    /**
     * Load (and return) from the classpath the class pass as parameter.
     * The method is package protected only for testing purposes.
     * 
     * @param classToLoad the full name of the class to load.
     * @return The loaded class or null if the class in not in classpath.
     * 
     * @see ClassLoader#loadClass(String)
     */
    static Class<?> loadClass(final String classToLoad) {
        if (classToLoad != null) {
            try {
                return PngWriter.class.getClassLoader().loadClass(classToLoad);
            } catch (ClassNotFoundException e) {
                LOGGER.warning(" Cannot find the class " + classToLoad + "in classpath !");
            }
        }
        return null;
    }

    /**
     * Creates the delegate writer (since this is the first method that is 
     * called) and write the header to the attached delegate.
     * {@inheritDoc}
     */
    @Override
    public void writeHeader(final SequenceMethod meth) throws IOException {
        this.delegate = new SEQUENCEWriter(this.getDiagramFullPath());
        this.delegate.writeHeader(meth);
    }

    /**
     * Delegate the writing to the attached delegate.
     * {@inheritDoc}
     */
    @Override
    public void writeMethodAfterChildren(final SequenceMethod meth) throws IOException {
        this.delegate.writeMethodAfterChildren(meth);
    }

    /**
     * Delegate the writing to the attached delegate.
     * {@inheritDoc}
     */
    @Override
    public void writeMethodBeforeChildren(final SequenceMethod meth) throws IOException {
        this.delegate.writeMethodBeforeChildren(meth);
    }
}
