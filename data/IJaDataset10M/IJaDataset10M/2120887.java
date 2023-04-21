package uk.ac.ncl.cs.instantsoap.cline;

import org.w3c.dom.Document;
import uk.ac.ncl.cs.instantsoap.InvalidJobSpecificationException;

/**
 * Date: 09-Aug-2006
 * Time: 11:36:48
 *
 * @author Cheng-Yang(Louis) Tang
 */
public interface CommandLineBuilder {

    void validate(Document contents) throws InvalidJobSpecificationException;

    CommandLine build(Document contents);

    public interface CommandLine {

        String toString();

        Result execute();
    }
}
