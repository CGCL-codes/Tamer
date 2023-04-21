package edu.rice.cs.cunit.record;

import com.sun.jdi.ClassType;
import com.sun.jdi.ClassObjectReference;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @author Mathias Ricken
 */
public interface ISyncPointProcessor {

    /**
     * Processes the verbose synchronization points since the last request and then clears the list in the
     * debugged VM.
     * @param force true if processing should be forced, even if debugged VM is in the process of changing the list
     * @param recorderClass class of the recorder
     * @param recorderClassObject class object of the recorder
     * @param methodDatabase method database (key=class index/method index pair in "%08x %08x" format)
     * @return true if update was successful, false if it was delayed
     */
    boolean process(boolean force, ClassType recorderClass, ClassObjectReference recorderClassObject, HashMap<String, String> methodDatabase);

    /**
     * Processes the current synchronization point immediately.
     * @param force
     * @return true if update was successful, false if it was delayed
     * @param recorderClass class of the recorder
     * @param recorderClassObject class object of the recorder
     * @param methodDatabase method database (key=class index/method index pair in "%08x %08x" format)
     */
    boolean processImmediately(boolean force, ClassType recorderClass, ClassObjectReference recorderClassObject, HashMap<String, String> methodDatabase);

    /**
     * Return a map of threads. Key is the unique ID.
     * @return map of threads
     */
    Map<Long, IThreadInfo> getThreads();

    /**
     * Returns list of list with cycles.
     * @return list of list with cycles
     */
    public List<List<IThreadInfo>> getCycles();
}
