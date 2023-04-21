package example;

import uk.ac.ncl.cs.instantsoap.stringprocessor.StringProcessor;
import uk.ac.ncl.cs.instantsoap.wsapi.InvalidJobSpecificationException;
import uk.ac.ncl.cs.instantsoap.wsapi.JobExecutionException;
import uk.ac.ncl.cs.instantsoap.wsapi.MetaData;
import static uk.ac.ncl.cs.instantsoap.wsapi.Wsapi.metaData;

public class StringAppender implements StringProcessor {

    private String suffix = "";

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public MetaData describeApplication() {
        return metaData("append" + suffix, "Append the string '" + suffix + "' to the end of the input");
    }

    public MetaData getInput() {
        return metaData("input", "The input string to process");
    }

    public MetaData getOutputs() {
        return metaData("output", "The input string with '" + suffix + "' appended");
    }

    public void validate(String data) throws InvalidJobSpecificationException {
    }

    public String process(String data) throws JobExecutionException {
        return data + suffix;
    }
}
