package sk.fiit.mitandao.tests.core.parameterreader;

import org.apache.log4j.Logger;
import sk.fiit.mitandao.core.parameterreader.annotations.Parameter;
import sk.fiit.mitandao.core.parameterreader.annotations.SpecialParameter;
import sk.fiit.mitandao.core.parameterreader.enumerations.SpecialParameterType;
import sk.fiit.mitandao.modules.interfaces.InputModule;
import edu.uci.ics.jung.graph.Graph;

public class TestModule implements InputModule {

    private Logger log = Logger.getLogger(TestModule.class);

    @Parameter(displayName = "this is a display name of the string parameter")
    private String stringParamer = "renamed";

    @Parameter
    private Integer integerParameter = new Integer(10);

    @Parameter
    private Double doubleParameter = new Double(2.4);

    @SpecialParameter(SpecialParameterType.FILE_OPEN_DIALOG)
    private String fileChooserParameter = "/tmp/asd";

    @Parameter
    private String filePath = "the file path";

    @Override
    public Graph apply(Graph graph) throws Exception {
        log.info("string parameter: " + stringParamer);
        log.info("integer parameter: " + integerParameter);
        log.info("double parameter: " + doubleParameter);
        log.info("fileChooserParameter parameter: " + fileChooserParameter);
        log.info("file path parameter: " + filePath);
        return null;
    }

    public String getStringParamer() {
        return stringParamer;
    }

    public void setStringParamer(String stringParamer) {
        this.stringParamer = stringParamer;
    }

    public Integer getIntegerParameter() {
        return integerParameter;
    }

    public void setIntegerParameter(Integer integerParameter) {
        this.integerParameter = integerParameter;
    }

    public Double getDoubleParameter() {
        return doubleParameter;
    }

    public void setDoubleParameter(Double doubleParameter) {
        this.doubleParameter = doubleParameter;
    }

    public String getFileChooserParameter() {
        return fileChooserParameter;
    }

    public void setFileChooserParameter(String fileChooserParameter) {
        this.fileChooserParameter = fileChooserParameter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return ("Example Module");
    }
}
