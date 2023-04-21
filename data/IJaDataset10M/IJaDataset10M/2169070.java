package org.streets.workflow.model.io;

import java.io.IOException;
import java.io.InputStream;
import org.streets.workflow.model.WFProcess;

/**
 * FPDL解析器，将一个xml格式的fpdl流程定义文件解析成WorkflowProcess对象。
 * @author Chennieyun
 */
public interface BPDLParser extends BPDLConstants {

    /** 
     * Parse the given InputStream into a WorkflowProcess object.<br/>
     * 将输入流解析成为一个WorkflowProcess对象。
     * @param in The InputStream
     * @throws IOException Any I/O Exception
     * @throws BPDLParserException Any parser exception
     */
    public WFProcess parse(InputStream in) throws IOException, BPDLParserException;
}
