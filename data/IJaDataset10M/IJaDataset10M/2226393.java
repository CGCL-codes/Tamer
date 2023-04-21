package pl.edu.zut.wi.vsl.app.modules;

import java.io.Serializable;
import pl.edu.zut.wi.vsl.app.VslException;
import pl.edu.zut.wi.vsl.commons.StegoImage;
import pl.edu.zut.wi.vsl.commons.StegoPackage;
import pl.edu.zut.wi.vsl.commons.distortions.DistortionException;
import pl.edu.zut.wi.vsl.commons.distortions.DistortionTechnique;

/**
 * Module for distortions.
 *
 * @author Michal Wegrzyn
 */
public class DistortionModule extends VslModule implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -7101691301386906735L;

    public DistortionModule(String name, String description, String clazz) {
        super(name, description, clazz, ModuleType.Distortion);
    }

    public DistortionModule(DistortionModule v) {
        super(v);
    }

    @Override
    public StegoPackage execute(StegoPackage cp) throws VslException {
        int paramIndex = getLoopNumber();
        try {
            DistortionTechnique d = (DistortionTechnique) (Class.forName(getModuleClass()).newInstance());
            StegoImage img = d.distort(cp.getImage(), getParameterMap(paramIndex));
            cp.setImage(img);
        } catch (DistortionException e) {
            throw new VslException("Exception during execution", e);
        } catch (ClassNotFoundException e) {
            throw new VslException("Exception during execution", e);
        } catch (InstantiationException e) {
            throw new VslException("Exception during execution", e);
        } catch (IllegalAccessException e) {
            throw new VslException("Exception during execution", e);
        }
        return cp;
    }
}
