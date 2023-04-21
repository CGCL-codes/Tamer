package org.moyoman.framework;

import org.moyoman.module.*;
import org.moyoman.util.*;
import java.util.*;

/** This class creates the modules that are required in order 
  * for a specific Module implementation to be able to run.
  * The algorithm is as follows: the ModuleCreator object is initialized
  * with a module name.  Instantiate the module represented
  * by that name, and get the module types that it requires.
  * Verify that it is only dependent on module types that 
  * preceed it in the ordered list of module types.  For each
  * of those module types, get the module name configured to
  * be used for those module types and implement them.
  * Continue this process until all dependencies have been
  * processed.  It is a runtime exception if either a module
  * is declared to be dependent on a module type which does
  * not proceed it, or if any module is not eventually
  * dependent on Board, because in that case, the module is
  * never reached.
  */
class ModuleCreator {

    /** The name is a ModuleName object, and the value a Module object.*/
    private HashMap modules;

    /** For the given mode, instantiate the MoveGenerator module and all dependent modules.
	  * @param id The id of the game.
	  * @param mode The mode of the game.
	  * @throws InternalErrorException Thrown if any dependencies are not correct.
	  */
    protected ModuleCreator(GameId id, Mode mode) throws InternalErrorException {
        ModuleConfig mc = ModuleConfig.getModuleConfig();
        ModuleType mt = ModuleType.getModuleType("MoveGenerator");
        ModuleName mn = mc.getFirstModuleName(mode, mt);
        processModules(id, mn, mode);
    }

    /** For the given module, instantiate it and all dependent modules.
	  * @param id The id of the game.
	  * @param name The name of the terminating module.
	  * @param mode The mode of the game.
	  * @throws InternalErrorException Thrown if any dependencies are not correct.
	  */
    protected ModuleCreator(GameId id, ModuleName topName, Mode mode) throws InternalErrorException {
        processModules(id, topName, mode);
    }

    /** Determine which modules are used by this game and instantiate them.
	  * @param id The id of the game.
	  * @param name The name of the terminating module.
	  * @param mode The mode of the game.
	  * @throws InternalErrorException Thrown if any dependencies are not correct.
	  */
    private void processModules(GameId id, ModuleName topName, Mode mode) throws InternalErrorException {
        try {
            modules = new HashMap();
            ModuleConfig mc = ModuleConfig.getModuleConfig();
            Module topModule = Module.create(id, topName);
            modules.put(topName, topModule);
            ModuleType[] topmt = topModule.getRequiredModuleList();
            HashSet hs = new HashSet();
            for (int i = 0; i < topmt.length; i++) {
                hs.add(topmt[i]);
            }
            while (hs.size() > 0) {
                HashSet hs2 = new HashSet();
                Iterator it = hs.iterator();
                while (it.hasNext()) {
                    ModuleType later = (ModuleType) it.next();
                    ModuleName name = mc.getFirstModuleName(mode, later);
                    if (name == null) throw new InternalErrorException("no ModuleName for " + mode + " " + later);
                    if (!modules.containsKey(name)) {
                        Module mod = Module.create(id, name);
                        modules.put(name, mod);
                        ModuleType[] types = mod.getRequiredModuleList();
                        if (types.length == 0) {
                            if (!later.getSimpleName().equals("Board")) throw new InternalErrorException("Module is not dependent on any other modules" + " " + later.getSimpleName());
                        }
                        for (int i = 0; i < types.length; i++) {
                            ModuleType earlier = types[i];
                            if (!mc.checkModuleTypeOrder(earlier, later)) throw new InternalErrorException("Module dependencies are out of order" + " " + earlier.getSimpleName() + " " + later.getSimpleName());
                            hs2.add(earlier);
                        }
                    }
                }
                hs = new HashSet(hs2);
            }
        } catch (InternalErrorException iee) {
            throw iee;
        } catch (Exception e) {
            InternalErrorException iee = new InternalErrorException(e);
            throw iee;
        }
    }

    /** Return the modules member variable produced by the constructor.
	  * @return The HashMap with the modules.
	  */
    protected HashMap getModules() {
        return modules;
    }
}
