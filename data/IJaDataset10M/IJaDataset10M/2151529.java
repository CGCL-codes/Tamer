package net.sf.brightside.mobilestock.service.api;

import net.sf.brightside.mobilestock.metamodel.api.Account;
import net.sf.brightside.mobilestock.metamodel.api.Shareholder;

public interface ICreateAccount {

    /**
	 * Creates new account for shareholder.
	 * 
	 * @param shareholder
	 * @return created Account
	 */
    Account createAccount(Shareholder shareholder);
}
