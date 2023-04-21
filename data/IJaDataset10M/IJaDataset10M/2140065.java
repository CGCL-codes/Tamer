package dnl.jexem.prcs;

import java.util.List;

public interface ProcessOperations {

    public List<ProcessInfo> getProcesses();

    public List<ProcessInfo> grepProcesses(String grepExpression);

    public ProcessInfo getProcess(int pid);

    public boolean killProcess(int pid);
}
