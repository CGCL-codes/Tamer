package planning.file;

import java.beans.IntrospectionException;
import java.io.IOException;
import org.xml.sax.SAXException;
import planning.file.props.ExecuterProp;
import planning.plan.Executer;
import simulation.shell.Shell;
import util.ObjectXML;

public class PlannerLoader {

    public ExecuterPropConverter propConverter;

    public PlannerLoader() {
        propConverter = new ExecuterPropConverter();
    }

    public void saveExecuter(String filename, Executer executer) throws IOException, SAXException, IntrospectionException {
        ExecuterProp executerProp = propConverter.generateExecuterProp(executer);
        saveExecuterProp(filename, executerProp);
    }

    public Executer loadExecuter(String filename, Shell shell) throws IntrospectionException, IOException, SAXException, NoSuchFieldException {
        ExecuterProp executerProp = loadExecuterProp(filename);
        Executer result = propConverter.generateExecuter(executerProp, shell);
        return result;
    }

    public ExecuterProp loadExecuterProp(String filename) throws IntrospectionException, IOException, SAXException {
        ExecuterProp result = (ExecuterProp) ObjectXML.loadObject(filename, ExecuterProp.class, getClass().getResource("/data/XMLAlgorithmMapping.xml").openStream());
        return result;
    }

    public void saveExecuterProp(String filename, ExecuterProp executerProp) throws IOException, SAXException, IntrospectionException {
        ObjectXML.saveObject(filename, executerProp, getClass().getResource("/data/XMLAlgorithmMapping.xml").openStream());
    }
}
