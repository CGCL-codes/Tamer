package uk.ac.ncl.cs.instantsoap.esciencetool.cline.requestProcessingModule;

/**
 * Created by IntelliJ IDEA.
 * User: louis
 * Date: 17-Oct-2006
 * Time: 13:40:00
 * To change this backupTemplate use FileData | Settings | FileData Templates.
 */
public interface Token {

    Type getType();

    Token copy();

    String getName();

    Existence getExistence();
}
