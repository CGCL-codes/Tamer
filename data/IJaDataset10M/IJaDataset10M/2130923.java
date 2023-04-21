package org.eclipse.emf.compare.diff.merge.api;

/**
 * An abstract implementation of a listener to receive merge events. All methods in this class are empty,
 * clients should override the method corresponding to whichever event they are interested in handling
 * notifications for.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractMergeListener implements IMergeListener {

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMergeListener#mergeDiffEnd(org.eclipse.emf.compare.diff.merge.api.MergeEvent)
	 */
    public void mergeDiffEnd(MergeEvent event) {
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMergeListener#mergeDiffStart(org.eclipse.emf.compare.diff.merge.api.MergeEvent)
	 */
    public void mergeDiffStart(MergeEvent event) {
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMergeListener#mergeOperationEnd(org.eclipse.emf.compare.diff.merge.api.MergeEvent)
	 */
    public void mergeOperationEnd(MergeEvent event) {
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMergeListener#mergeOperationStart(org.eclipse.emf.compare.diff.merge.api.MergeEvent)
	 */
    public void mergeOperationStart(MergeEvent event) {
    }
}
