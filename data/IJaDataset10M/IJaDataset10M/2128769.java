package edu.kgi.biobridge.oaadriver;

import edu.kgi.biobridge.gum.*;
import com.sri.oaa2.icl.*;
import com.sri.oaa2.lib.*;

/**
 * The OAAAgent class is the OAA driver's implementation of the Service interface.
 * It provides all the methods of a single OAA agent.
 * 
 * @author Cameron Wellock
 */
public class OAAAgent implements Service {

    private String name;

    private OAAMethods methods;

    private LibOaa oaa;

    private IclTerm address;

    private boolean listParamsProblematic;

    /**
 * Create a new OAAAgent object.
 * @param name Name of the agent.
 * @param address Address of the agent.
 * @param methods Methods of the agent.
 * @param oaa Handle to the OAA object.
 */
    public OAAAgent(IclStr name, IclTerm address, IclList methods, LibOaa oaa, boolean listParamsProblematic) {
        this.oaa = oaa;
        this.name = name.toString();
        this.address = address;
        this.listParamsProblematic = listParamsProblematic;
        this.methods = setMethods(methods);
    }

    /**
 * Get a method option from the options list, if it exists.
 * @param method Method struct to search for an option.
 * @param option Name of the option (i.e. "argtypes") to find.
 * @return Method option if found, <b>null</b> if not found.
 */
    private static IclStruct getMethodOption(IclStruct method, String option) {
        if (method.getFunctor().equals("solvable")) {
            if ((method.getNumChildren() >= 2) && (method.getTerm(1).isList())) {
                IclList options = (IclList) method.getTerm(1);
                for (int i = 0; i < options.getNumChildren(); i++) if (options.getTerm(i).isStruct()) {
                    IclStruct optionItem = (IclStruct) options.getTerm(i);
                    if (optionItem.getFunctor().equals(option)) return optionItem;
                }
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
 * Set up the methods collection.
 * @param methods ICL descriptors for the methods.
 * @return Collection of Methods objects.
 */
    protected OAAMethods setMethods(IclList methods) {
        OAAMethods oaaMethods = new OAAMethods();
        IclStruct iclMethod, iclArgtypes, iclDescription;
        OAAMethod oaaMethod;
        for (int i = 0; i < methods.getNumChildren(); i++) {
            iclMethod = (IclStruct) methods.getTerm(i).getTerm(0);
            iclArgtypes = getMethodOption((IclStruct) methods.getTerm(i), "argtypes");
            iclDescription = getMethodOption((IclStruct) methods.getTerm(i), "description");
            if (iclArgtypes != null) oaaMethod = new OAATypedMethod(iclMethod, iclArgtypes, iclDescription, address, oaa); else oaaMethod = new OAAUntypedMethod(iclMethod, iclDescription, address, oaa, listParamsProblematic);
            oaaMethods.add(oaaMethod);
        }
        return oaaMethods;
    }

    public String getName() {
        return name;
    }

    public Methods getMethods() {
        return methods;
    }
}
