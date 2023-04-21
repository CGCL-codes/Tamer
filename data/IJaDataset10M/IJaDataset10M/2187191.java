package signature.compare;

import signature.compare.model.IApiDelta;
import signature.model.IApi;

/**
 * {@code IApiComparator} defines the functionality of a signature comparator.
 */
public interface IApiComparator {

    /**
     * Returns a difference model which describes the differences from {@code
     * fromApi} to {@code toApi}.
     * 
     * @param fromApi
     *            differences are computed relative to {@code fromApi}
     * @param toApi
     *            the target signature model
     * @return a difference model which describes the differences from {@code
     *         fromApi} to {@code toApi}
     */
    IApiDelta compare(IApi fromApi, IApi toApi);
}
