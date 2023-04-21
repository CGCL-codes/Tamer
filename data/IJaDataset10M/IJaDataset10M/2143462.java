package org.crypthing.signthing.client;

import java.io.Serializable;

/**
 * Interface for action execution.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public interface ActionExecutor extends Serializable {

    /**
   * Sign some-thing
   */
    public static final int SIGN_TASK = 0;

    /**
   * Verify a signed file.
   */
    public static final int VERIFY_TASK = 1;

    /**
   * Gets a key alias.
   */
    public static final int KEY_ALIAS_TASK = 2;

    /**
   * Gets all keys aliases.
   */
    public static final int KEY_ALIASES_TASK = 3;

    /**
   * Gets the current certificate.
   */
    public static final int CERTIFICATE_TASK = 4;

    /**
   * Gets the certificate chain.
   */
    public static final int CHAIN_TASK = 5;

    /**
   * Checks certificate compliance.
   */
    public static final int VERIFY_CERTIFICATE_TASK = 6;

    /**
   * Checks certificate revocation.
   */
    public static final int VERIFY_REVOCATION_TASK = 7;

    /**
   * Executes an action identified by its id.
   * @param id - the action id, just in case of more than one action may be executed.
   * @return - the execution result.
   */
    public ExecutionResult executeAction(int id);

    /**
   * Executes an action identified by its id.
   * @param id - the action id, just in case of more than one action may be executed.
   * @param args - arguments to executor.
   * @return - the execution result.
   */
    public ExecutionResult executeAction(int id, String[] args);
}
