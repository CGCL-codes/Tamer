package game.assets;

import java.util.Observable;

/**
 * Asset is the topmost superclass of all graphical and sound based assets.
 * <P>
 * The Asset class simply holds the name of the asset and defines two abstract
 * methods which can be used to retrieve copies of the asset (namely, shallowClone
 * and deepClone). The shallClone method is intended to return a new object copy
 * that <B>shares</B> the actual asset contained within the source object (i.e.
 * sharing the image for image based assets, or the clip for sound based assets).
 * The deepClone method is intended to return a deep clone of the asset object (i.e.
 * generating and returning a copy of the stored asset).
 * <P>
 * All instances of this class are observable, although a particular subclass need not
 * necessarily have observable behaviour (i.e. any notifications are dependent upon
 * the type of asset).
 * <P>
 * Note: Each Asset object is identified by a String based assetName. Whilst not
 * a necessary condition, other classes (e.g. those from the GameEngine package)
 * can operate under an implicit assume that each asset has a unique name.
 *
 * @author <A HREF="mailto:P.Hanna@qub.ac.uk">Philip Hanna</A>
 * @version $Revision: 1 $ $Date: 2006/08 $
 */
public abstract class Asset extends Observable {

    /**
     * String based asset name.
     */
    protected String assetName;

    /**
     * Constructs a new Asset instance, setting the asset name to that specified
     * within the constructor call.
     *
     * @param   assetName the name to be assigned to this asset
     */
    public Asset(String assetName) {
        setAssetName(assetName);
    }

    /**
     * Retrieve the current name of this asset
     *
     * @return  the asset anem
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * Set the asset name to that specified within the method call
     *
     * @param   assetName the new name to be assigned to this asset
     *
     * @exception   IllegalArgumentException if an null  
     *              asset name is specified
     */
    public final void setAssetName(String assetName) {
        if (assetName == null) throw new IllegalArgumentException("Asset.setAssetName: Invalid asset name.");
        this.assetName = assetName;
    }

    /**
     * Asset update - by default does not do anything (i.e. any asset that requires
     * specific actions should be specified within an extending superclass). An
     * example is the ImageAssetSequence class whose update will update the current
     * animation frame.
     */
    public void update() {
    }

    /**
     * Return a shallow clone of this Asset instance (any image or sound based
     * assets contained within this Asset will be shared with the cloned Asset).
     *
     * @return  new Asset instance containing a shallow clone of this Asset instance
     * @see AssetManager
     */
    public abstract Asset shallowClone();

    /**
     * Return a deep clone of this Asset instance (any image or sound based
     * assets contained within this Asset will be themselves cloned and return
     * within the clone Asset).
     *
     * @return  new Asset instance containing a deep clone of this Asset instance
     * @see AssetManager
     */
    public abstract Asset deepClone();
}
