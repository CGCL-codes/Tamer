package net.pleso.framework.client.bl;

import net.pleso.framework.client.bl.auth.IAuth;
import net.pleso.framework.client.dal.IDataSource;

/**
 * Represents data source with authorization check availability. This interface
 * is used by business  logic interfaces to make all data retrieve actions
 * authorization rights dependent.
 */
public interface IAuthDataSource extends IDataSource, IAuth {
}
