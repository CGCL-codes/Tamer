package openfarmmanager.memory.monitor;

import openfarmmanager.beans.IBean;
import openfarmmanager.beans.memory.TCLogMemoryMonitorBean;
import openfarmmanager.manager.ManagerRegister;
import openfarmmanager.memory.memtrashcollector.TCDetails;
import openfarmmanager.util.logging.LogManagerUtil;
import openfarmtools.interpreter.exceptions.OpenFarmException;
import org.apache.log4j.Logger;

public class TCLogMemoryMonitor extends MemoryFilesMonitor {

    private static final Logger log = LogManagerUtil.getLogger(TCLogMemoryMonitor.class);

    private IOpenFarmFileReaderMonitor<TCDetails> fileReaderMonitor;

    public TCLogMemoryMonitor(ManagerRegister register, IOpenFarmFileReaderMonitor<TCDetails> fileReaderMonitor) throws OpenFarmException {
        super(register);
        this.eMemFiles = EMemoryFiles.TCLOG;
        this.fileReaderMonitor = fileReaderMonitor;
    }

    @Override
    public IBean monitorBeanFactory() {
        TCLogMemoryMonitorBean memBean = new TCLogMemoryMonitorBean();
        try {
            memBean.setTcDetails(fileReaderMonitor.getObjectFromFile(EMemoryFiles.TCLOG));
            this.init();
            memBean.setMemType(EMemoryFiles.TCLOG);
            memBean.setFilename(this.filename);
            memBean.setFilesize(this.filesize);
            memBean.setTCActive(register.isTcActive());
        } catch (OpenFarmException e) {
            log.warn(EMemoryFiles.TCLOG.name + " is inexistent");
            memBean.setTcDetails(null);
        }
        return memBean;
    }
}
