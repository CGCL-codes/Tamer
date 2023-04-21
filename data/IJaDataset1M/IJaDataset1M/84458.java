package org.arpenteur.photogrammetry.orientation;

import org.arpenteur.common.manager.PointManager;
import org.arpenteur.photogrammetry.image.CameraManager;
import org.arpenteur.photogrammetry.image.PhotoManager;
import org.arpenteur.photogrammetry.orientation.constraint.ConstraintManager;

/**
 * PhotogrammetricModel is the interface defining a data model for
 * Photogrammetry
 * 
 * @author piotr
 */
public interface IModel {

    /**
	 * Returns the name of this model
	 * 
	 */
    public String getName();

    /**
	 * set the name of this model
	 * 
	 */
    public void setName(String modelName);

    /**
	 * Assign a description for this Model Units are described in the Unit class
	 * and sub-classes
	 * 
	 * @param p_User
	 *            indicates the name of the user or a path in the user space
	 * @param p_Version
	 *            indicates the version of this model
	 * @param p_CreationDate
	 *            indicates the date of creation of this model.
	 * @param p_LastUpdate
	 *            indicates the date of last update of this model.
	 * @param p_Author
	 *            is the name of the author and should be significant at
	 *            application level only
	 * @param p_AngleUnits
	 *            is a String identifying the angular units used in the model
	 * @param p_GroundUnits
	 *            is a String identifying the length units used in the model
	 *            (this should also be the length of the vector of lenght 1.0)
	 * @param p_ImageUnits
	 *            is a String identifying the units used for describing
	 *            measurements on images
	 * @see net.arpenteur.common.geometry.Unit
	 * @see net.arpenteur.common.geometry.Length
	 * @see net.arpenteur.common.geometry.Angular
	 */
    public void setDescription(String p_User, String p_Version, java.util.Date p_CreationDate, java.util.Date p_LastUpdate, String p_Author, String p_AngleUnits, String p_GroundUnits, String p_ImageUnits);

    /**
	 * Camera manager
	 */
    public CameraManager getCameras();

    /**
	 * Photograph manager
	 */
    public PhotoManager getPhotographs();

    /**
	 * Constraint manager
	 */
    public ConstraintManager getConstraints();

    /**
	 * Point manager: ie measure and observation This PointManager contains
	 * Point_3D with for each a set of ImagePoint
	 */
    public PointManager get3DPoints();
}
