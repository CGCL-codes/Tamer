package com.netstoke.core.asset.dao;

import com.netstoke.core.asset.IAssetService;
import com.netstoke.persistence.IDataAccessObject;

/**
 * <p>The <code>IAssetDao</code> is responsible for the persistence operations of the {@link IAssetService}.</p>
 * @author kmckee &lt;<a href="mailto:kevin.mckee@netstoke.com">kevin.mckee@netstoke.com</a>&gt;
 * @version 1.0
 * @since 1.0
 */
public interface IAssetDao extends IAssetService, IDataAccessObject {
}
