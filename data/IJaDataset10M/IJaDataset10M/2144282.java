package eu.planets_project.pp.plato.bean;

import java.io.Serializable;
import javax.ejb.Local;

/**
 * @see BooleanCapsule
 * @author cbu
 */
@Local
public interface IUploadBean extends Serializable {

    public void destroy();

    public String getPath();

    public void setPath(String path);
}
