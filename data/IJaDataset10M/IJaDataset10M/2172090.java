package deesel.lang.module;

import deesel.lang.module.implementations.DefaultModule;
import deesel.parser.com.nodes.DeeselClass;
import deesel.parser.com.nodes.DeeselPackage;
import deesel.util.logging.Logger;
import java.util.List;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class ModuleLocator {

    private static final Logger log = Logger.getLogger(ModuleLocator.class);

    public static Module locate(DeeselClass clazz) throws ModuleLocationException {
        DeeselPackage thePackage = clazz.getPackage();
        List packages = thePackage.getPackages();
        Module lastFoundModule;
        StringBuffer packageName = new StringBuffer();
        for (int i = 0; i < packages.size(); i++) {
            String s = (String) packages.get(i);
            if (packageName.length() > 0) {
                packageName.append('.');
            }
            packageName.append(s);
            StringBuffer moduleName = new StringBuffer().append(packageName).append(".Module");
            log.debug("Looking for " + moduleName);
            try {
                Class modClass = Class.forName(moduleName.toString());
                Module instance = (Module) modClass.newInstance();
                return instance;
            } catch (ClassNotFoundException e) {
            } catch (IllegalAccessException e) {
                throw new ModuleLocationException(clazz, e);
            } catch (ClassCastException e) {
                throw new ModuleLocationException(clazz, e);
            } catch (InstantiationException e) {
                throw new ModuleLocationException(clazz, e);
            }
        }
        return DefaultModule.getInstance();
    }

    /**
     * This method locates a specific module for the package supplied, this
     * method will not look anywhere else than the package specified.
     *
     * @param deeselPackage
     * @return
     * @throws ModuleLocationException
     */
    public static Module getModuleByPackage(DeeselPackage deeselPackage) throws ModuleLocationException, ModuleNotFoundException {
        try {
            return (Module) Class.forName(deeselPackage.getName() + "." + "Module").newInstance();
        } catch (ClassNotFoundException e) {
            throw new ModuleNotFoundException(deeselPackage, e);
        } catch (IllegalAccessException e) {
            throw new ModuleLocationException(deeselPackage, e);
        } catch (InstantiationException e) {
            throw new ModuleLocationException(deeselPackage, e);
        } catch (ClassCastException cce) {
            throw new ModuleLocationException(deeselPackage, cce);
        }
    }
}
