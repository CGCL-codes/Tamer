package org.jazzteam.bpe.logic;

import org.jazzteam.bpe.dao.process.IProcessDAO;
import org.jazzteam.bpe.logic.executor.ProcessExecutor;
import org.jazzteam.bpe.model.process.BusinessProcess;
import org.jazzteam.bpe.xml.exceptions.UnableCreateNodeActionException;
import org.jazzteam.bpe.xml.exceptions.XMLProcessException;
import org.jazzteam.bpe.xml.parser.IProcessParser;

/**
 * 
 * 
 * @author Konstantin
 * @version $Rev: $
 */
public class ProcessManager {

    private IProcessParser parser;

    private ProcessExecutor processExecutor;

    private IProcessDAO processDAO;

    public void startProcess(String processName) {
        BusinessProcess processObj = null;
        try {
            processObj = parser.parse(processName);
        } catch (XMLProcessException e) {
            e.printStackTrace();
        } catch (UnableCreateNodeActionException e) {
            e.printStackTrace();
        }
        processDAO.save(processObj);
        processExecutor.execute(processObj);
    }

    /**
	 * @return the parser
	 */
    public IProcessParser getParser() {
        return parser;
    }

    /**
	 * @param parser the parser to set
	 */
    public void setParser(IProcessParser parser) {
        this.parser = parser;
    }

    /**
	 * @return the processExecutor
	 */
    public ProcessExecutor getProcessExecutor() {
        return processExecutor;
    }

    /**
	 * @param processExecutor the processExecutor to set
	 */
    public void setProcessExecutor(ProcessExecutor processExecutor) {
        this.processExecutor = processExecutor;
    }
}
