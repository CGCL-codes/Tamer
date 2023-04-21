package org.havi.ui;

/**
 * This {@link org.havi.ui.HMatteLayer HMatteLayer}interface enables the
 * presentation of components, together with an associated {@link
 * org.havi.ui.HMatte HMatte}, for matte compositing.
 * @see HMatte
 */
public interface HMatteLayer {

    /**
	 * Applies an {@link org.havi.ui.HMatte HMatte}to this component, for matte
	 * compositing. Any existing animated matte must be stopped before this
	 * method is called or an HMatteException will be thrown.
	 * @param m The {@link org.havi.ui.HMatte HMatte}to be applied to this
	 *            component -- note that only one matte may be associated with
	 *            the component, thus any previous matte will be replaced. If m
	 *            is null, then any matte associated with the component is
	 *            removed and further calls to getMatte() shall return null. The
	 *            component shall behave as if it had a fully opaque {@link
	 *            org.havi.ui.HFlatMatte HFlatMatte} associated with it (i.e an
	 *            HFlatMatte with the default value of 1.0.)
	 * @exception HMatteException if the {@link org.havi.ui.HMatteHMatte}cannot
	 *                be associated with the component. This can occur:
	 *                <ul>
	 *                <li>if the specific matte type is not supported
	 *                <li>if the platform does not support any matte type
	 *                <li>if the component is associated with an already
	 *                running
	 *                {@link org.havi.ui.HFlatEffectMatte HFlatEffectMatte}or
	 *                {@linkorg.havi.ui.HImageEffectMatte HImageEffectMatte}.
	 *                The exception is thrown even if m is null.
	 *                </ul>
	 * @see HMatte
	 */
    public void setMatte(HMatte m) throws HMatteException;

    /**
	 * Get any {@link org.havi.ui.HMatte HMatte}currently associated with this
	 * component.
	 * @return the {@link org.havi.ui.HMatte HMatte}currently associated with
	 *         this component or null if there is no associated matte.
	 */
    public HMatte getMatte();
}
