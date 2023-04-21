package network.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * network.common.HelpingUtility class defines the methods for generating random values with
 * uniform distribution using {@link java.util.Random}.
 * <p/>
 * network.common.HelpingUtility class also reads the input parameters and their associated
 * values from the input configuration file.
 * <p/>
 * Conversion of the clock speed to appropriate speed by a factor is also
 * performed in this class.
 */
public class HelpingUtility {

    /**
     * a member of the java.Util.Random class
     */
    private Random rand = null;

    /**
     * list of input parameters
     */
    private Vector allParamSet = null;

    /**
     * Constructor method. Performs initialization of the class variables.
     */
    public HelpingUtility() {
        rand = new Random(12345);
        allParamSet = new Vector();
    }

    /**
     * Sets the seed for the randomizer
     *
     * @param seed seed value
     */
    public void setRandSeed(int seed) {
        rand = new Random(seed);
    }

    /**
     * Instantiates the rand variable.
     */
    public void setRandomSeed() {
        rand = new Random();
    }

    /**
     * Returns a random number from 0 to 1 using Uniform Distribution.
     *
     * @return random value from 0 to 1
     */
    public double getNextRandomNumber() {
        return rand.nextDouble();
    }

    /**
     * Returns a set of input parameters for a particular network speicied by
     * the arguement of the method.
     *
     * @param index Index of the network.Network to be tested
     * @return a set of input parameters
     */
    public Vector getParamSet(int index) {
        if (index < allParamSet.size()) return (Vector) allParamSet.get(index);
        return null;
    }

    /**
     * A simple method to get file path from class path.
     * @param fileName
     * @return file path from class path
     */
    public String getFilePath(String fileName) {
        return getClass().getClassLoader().getResource(".").getPath() + fileName;
    }

    /**
     * Reads the input configuration file. Each empty line defines a new set of
     * input parameters. Typically a set of input parameters correspond to the
     * simulation for a particular topology or configuration.
     * <p/>
     * A typical line of the file is of the format <parameter name>=<parameter
     * value>. The parameter name and its value is stored in the
     * {@link ParamDTO} object.
     *
     * @param parameterFile the name of the input configuration file
     * @see BufferedReader
     * @see StringTokenizer
     */
    public void readParameterFromFile(String parameterFile) {
        StringTokenizer theTokenizer;
        String parameter, value;
        Vector paramSet = new Vector();
        try {
            BufferedReader paramReader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(parameterFile).getFile()));
            String paramLine = paramReader.readLine();
            while (paramLine != null) {
                if (paramLine.equalsIgnoreCase("")) {
                    allParamSet.add(paramSet);
                    paramSet = new Vector();
                    System.out.println("Added: ");
                } else {
                    theTokenizer = new StringTokenizer(paramLine, " =;,:", false);
                    if (theTokenizer.countTokens() >= 2) {
                        parameter = theTokenizer.nextToken();
                        value = theTokenizer.nextToken();
                        paramSet.add(new ParamDTO(parameter, value));
                    }
                }
                paramLine = paramReader.readLine();
                System.out.println("ReadLine: " + paramLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a converted cycle for the factor specified in the arguement.
     *
     * @param cycle  cycle value of an entity, for example a resource
     * @param factor ratio of the speed between an entity, for example a resource
     *               and the switch
     * @return value the cycle with respect to the switch
     */
    public int getConvertedCycle(int cycle, double factor) {
        return (int) Math.floor((double) (cycle) * factor);
    }
}
