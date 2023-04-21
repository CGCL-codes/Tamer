package ru.adv.xml.newt.tmanager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CustomizableThreadCreator;
import ru.adv.util.ADVRuntimeException;
import ru.adv.web.app.HttpSessionDestroyedEvent;

/**
 * 
 * Serve import proceses of newt:tmanager
 * 
 * @author vic
 *
 */
public class TManagerImportService implements ApplicationListener {

    private CustomizableThreadCreator threadCreator;

    private Map<String, ImportProcess> processList;

    public TManagerImportService() {
        super();
        threadCreator = new CustomizableThreadCreator("import-process");
        threadCreator.setDaemon(false);
        processList = new HashMap<String, ImportProcess>();
    }

    /**
	 * Start process background 
	 */
    public synchronized void start(ImportProcess process) {
        final ImportProcess importProcess = processList.get(process.getId());
        if (importProcess != null && importProcess.isStarted()) {
            throw new ADVRuntimeException("ImportProcess with id=" + process.getId() + " is running already");
        }
        Thread thread = threadCreator.createThread(process);
        thread.start();
        processList.put(process.getId(), process);
    }

    public synchronized ImportProcess getProcess(String importProcessId) {
        return processList.get(importProcessId);
    }

    /**
	 * Stop started process
	 * @param sessionId
	 */
    public synchronized void stop(String processId) {
        ImportProcess process = getProcess(processId);
        if (process != null) {
            process.interruptIfStarted();
        }
    }

    /**
	 * destory process by id
	 * @param sessionId
	 */
    private synchronized void destroyProcess(String processId) {
        ImportProcess process = getProcess(processId);
        if (process != null) {
            process.interruptIfStarted();
            processList.remove(processId);
            process.destroy();
        }
    }

    /**
	 * Destroy {@link ImportProcess} is it exists on {@link HttpSession} close
	 */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (HttpSessionDestroyedEvent.class == event.getClass()) {
            String sessionId = ((HttpSessionDestroyedEvent) event).getSession().getId();
            destroyProcess(sessionId);
        }
    }

    /**
	 * returns all known processes
	 * @return
	 */
    public Collection<ImportProcess> getProcessList() {
        return Collections.unmodifiableCollection(this.processList.values());
    }

    /**
	 * Stop all running processes and free resources 
	 */
    public synchronized void destroy() {
        for (ImportProcess importProcess : processList.values()) {
            importProcess.interruptIfStarted();
            importProcess.destroy();
        }
        processList.clear();
    }
}
