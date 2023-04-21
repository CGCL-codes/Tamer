package org.moyoman.framework;

import org.moyoman.log.*;
import org.moyoman.module.*;
import org.moyoman.util.*;
import java.io.Serializable;
import java.util.*;

/** A directed acyclic graph (DAG) is constructed leading from the
  * module which implements the Board interface to the terminating module. 
  * Usually the terminating module implements the MoveGenerator interface,
  * but it may be some other type of module since modules can get their own
  * Scheduler object which contains its own DAG.  For example, a module which
  * implements the Joseki interface and calls getScheduler() would get a
  * Scheduler object which contains a DAG object where the terminating module
  * implements type Joseki. 
  * <p>
  * The DAG can be traversed using the getFirst() and getNext() methods, 
  * while the state of each module is set using the clear() and setComplete() methods.
  * <p>
  * The DAG class does not do consistency checking on the module order, since this
  * consistency checking is done by the ModuleCreator object before the DAG object is created.
  */
class DAG implements Cloneable, Serializable {

    /** Key is ModuleName, value is a DAGNode object.*/
    private HashMap nodes;

    /** The name of the Board module.*/
    private ModuleName first;

    /** The modules which are ready to be started.  Each element is a ModuleName object.*/
    private ArrayList ready;

    /** A count of the number of modules which have not yet completed running.*/
    private int modulesRemaining;

    /** Construct the DAG object.
	  * @param modules a HashMap where the key is the ModuleName, and the
	  * value is the Module.
	  * @throws InternalErrorException Thrown if the operation fails for any reason.
	  */
    protected DAG(HashMap modules) throws InternalErrorException {
        try {
            HashMap types = new HashMap();
            ready = new ArrayList();
            nodes = new HashMap();
            Set set = modules.keySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                ModuleName mn = (ModuleName) it.next();
                DAGNode dn = new DAGNode(mn);
                nodes.put(mn, dn);
                types.put(mn.getModuleType(), mn);
            }
            it = set.iterator();
            while (it.hasNext()) {
                ModuleName mn = (ModuleName) it.next();
                ModuleType boardmt = ModuleType.getModuleType("Board");
                if (mn.getModuleType().equals(boardmt)) first = mn;
                DAGNode dn = (DAGNode) nodes.get(mn);
                Module mod = (Module) modules.get(mn);
                ModuleType[] mt = mod.getRequiredModuleList();
                for (int i = 0; i < mt.length; i++) {
                    ModuleName prevmn = (ModuleName) types.get(mt[i]);
                    DAGNode prevdn = (DAGNode) nodes.get(prevmn);
                    dn.addPrevious(prevdn);
                    prevdn.addNext(dn);
                }
            }
            modulesRemaining = nodes.size();
        } catch (Exception e) {
            InternalErrorException iee = new InternalErrorException(e);
            throw iee;
        }
    }

    /** Set all the modules as not having been executed.
	  * This is done before the start of a move.
	  */
    protected void clear() {
        Set set = nodes.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            ModuleName mn = (ModuleName) it.next();
            DAGNode dn = (DAGNode) nodes.get(mn);
            dn.setCompleted(false);
        }
        modulesRemaining = nodes.size();
        ready.clear();
    }

    /** Return all of the module names.
	  * @return The module names.
	  */
    protected ModuleName[] getAllModuleNames() {
        Set set = nodes.keySet();
        ModuleName[] retval = new ModuleName[set.size()];
        set.toArray(retval);
        return retval;
    }

    /** Get the first module name.
	  * This will have a module type of Board.
	  * @return The module name.
	  */
    protected ModuleName getFirst() {
        return first;
    }

    /** Return the names of the modules to be run next.
	  * Calls alternate between getNext() and setComplete().
	  * Once setComplete() is called, the DAG is analyzed
	  * to determine what modules if any have all of their
	  * dependent modules complete.  An array with the names
	  * of those modules is returned when this method is called.
	  * @return An array of the names of modules to be run next.
	  */
    protected ModuleName[] getNext() {
        ModuleName[] mn = new ModuleName[ready.size()];
        ready.toArray(mn);
        ready.clear();
        return mn;
    }

    /** Mark a module as completed.
	  * This method is called when a module has
	  * completed its work for the current move.
	  * The state of the DAG is updated so that
	  * getNext() can return the names of the modules
	  * which are ready to run.
	  * @param name The name of the module which is completed.
	  */
    protected void setComplete(ModuleName name) {
        try {
            DAGNode dn = (DAGNode) nodes.get(name);
            dn.setCompleted(true);
            DAGNode[] nextNodes = dn.getNext();
            for (int i = 0; i < nextNodes.length; i++) {
                DAGNode currentdn = nextNodes[i];
                if (currentdn.isReady()) {
                    ready.add(currentdn.getModuleName());
                }
            }
            modulesRemaining--;
        } catch (Exception e) {
            SystemLog.error(e);
        }
    }

    /** Override the Object.clone() method.
	  * @return A DAG object which is a clone of this one.
	  */
    public Object clone() {
        try {
            DAG dag = (DAG) super.clone();
            dag.nodes = new HashMap();
            HashMap hm = new HashMap();
            Set s = nodes.keySet();
            Iterator it = s.iterator();
            while (it.hasNext()) {
                ModuleName mn = (ModuleName) it.next();
                DAGNode dn1 = (DAGNode) nodes.get(mn);
                DAGNode dn2 = (DAGNode) dn1.clone();
                hm.put(dn1, dn2);
                dag.nodes.put(mn, dn2);
            }
            it = s.iterator();
            while (it.hasNext()) {
                ModuleName mn = (ModuleName) it.next();
                DAGNode dn1 = (DAGNode) nodes.get(mn);
                DAGNode dn2 = (DAGNode) hm.get(dn1);
                DAGNode[] arr = dn1.getPrevious();
                for (int i = 0; i < arr.length; i++) {
                    DAGNode prevdn = (DAGNode) hm.get(arr[i]);
                    dn2.addPrevious(prevdn);
                }
                arr = dn1.getNext();
                for (int i = 0; i < arr.length; i++) {
                    DAGNode nextdn = (DAGNode) hm.get(arr[i]);
                    dn2.addNext(nextdn);
                }
            }
            dag.ready = (ArrayList) ready.clone();
            return dag;
        } catch (Exception e) {
            SystemLog.error(e);
            return this;
        }
    }
}
