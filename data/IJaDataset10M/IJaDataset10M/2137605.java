package oldmcdata.generators;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import cern.jet.random.Uniform;

/** A class to test implementation of Generators
 */
public class UniformRandomGenerator extends AbstractGenerator {

    private static final String NUMBEROFEXCURSIONS = "NumberOfExcursions", NUMBEROFITERATIONS = "NumberOfIterations", DELIMITER = "Delimiter", DEFAULTDELIMITER = ",", MAXIMUMVALUE = "MaximumValue", MINIMUMVALUE = "MinimumValue";

    private int iterationCount = 0;

    private int numberOfExcursions;

    private int numberOfIterations;

    private List<String> excursions;

    private String delim;

    private Uniform uniform;

    public UniformRandomGenerator() {
        super();
        uniform = new Uniform(0.0, 1.0, 123456);
        numberOfExcursions = 1;
        numberOfIterations = 1;
        delim = DEFAULTDELIMITER;
    }

    public void initialize() {
        System.out.println(this.getClass().getName() + ":initialize()");
        translator.initialize();
        configure();
    }

    private void configure() {
        Element parameters = studyXMLHandler.getGeneratorAlgorithmParameters();
        Element numOfExcursions = parameters.element(NUMBEROFEXCURSIONS);
        Element numOfIterations = parameters.element(NUMBEROFITERATIONS);
        Element delimiter = parameters.element(DELIMITER);
        if (numOfIterations != null) setNumberOfIterations(numOfIterations.getText());
        if (numOfExcursions != null) setNumberOfExcursions(numOfExcursions.getText());
        if (delimiter != null) setDelimiter(delimiter.getText());
    }

    public void setNumberOfExcursions(String noe) {
        numberOfExcursions = Integer.parseInt(noe);
    }

    public void setNumberOfIterations(String noi) {
        numberOfIterations = Integer.parseInt(noi);
    }

    public void setDelimiter(String d) {
        delim = d;
    }

    public boolean generate() {
        System.out.println(this.getClass().getName() + ":generate()");
        if (iterationCount < numberOfIterations) {
            studyXMLHandler.openStudyXMLDocument();
            generateExcursions();
            translator.translate(iterationCount, excursions, studyXMLHandler);
            studyXMLHandler.writeStudyXMLDocument();
            iterationCount++;
            return false;
        } else return true;
    }

    private void generateExcursions() {
        excursions = new ArrayList<String>();
        List<Element> variables = studyXMLHandler.getVariables();
        for (int i = 0; i < numberOfExcursions; i++) {
            excursions.add(generateExcursion(variables));
        }
    }

    private String generateExcursion(List<Element> variables) {
        StringBuffer sb = new StringBuffer();
        for (Element variable : variables) {
            String maximumValue = variable.element(MAXIMUMVALUE).getText();
            String minimumValue = variable.element(MINIMUMVALUE).getText();
            double random = uniform.nextDoubleFromTo(Double.parseDouble(minimumValue), Double.parseDouble(maximumValue));
            sb.append(random).append(delim);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
