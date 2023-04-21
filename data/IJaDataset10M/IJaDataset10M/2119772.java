package org.rakiura.evm;

/**
 * Constants for EVM implementation.
 * 
 * <br><br>
 * Constants.java
 * Created: 16/07/2004 10:58:14 
 *
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version @version@ $Revision: 1.3 $
 * 
 */
public interface Constants {

    int LIST_SIZE_LIMIT = 5000000;

    int LIST_SIZE_INITIAL = 1024;

    int INT_STACK_SIZE_LIMIT = 100000;

    int INT_STACK_SIZE_INITIAL = 1024;

    int PROG_SIZE_LIMIT = 5000;
}
