package org.mockftpserver.core.command;

/**
 * Interface for an object that can retrieve and clear the history of InvocationRecords 
 * for a command handler.
 * 
 * @version $Revision: 8 $ - $Date: 2007-12-18 22:42:32 -0500 (Tue, 18 Dec 2007) $
 *
 * @author Chris Mair
 */
public interface InvocationHistory {

    /**
     * @return the number of invocation records stored for this command handler instance
     */
    public int numberOfInvocations();

    /**
     * Return the InvocationRecord representing the command invoction data for the nth invocation
     * for this command handler instance. One InvocationRecord should be stored for each invocation
     * of the CommandHandler.
     * 
     * @param index - the index of the invocation record to return. The first record is at index zero.
     * @return the InvocationRecord for the specified index
     * 
     * @throws AssertFailedException - if there is no invocation record corresponding to the specified index     */
    public InvocationRecord getInvocation(int index);

    /**
     * Clear out the invocation history for this CommandHandler. After invoking this method, the
     * <code>numberOfInvocations()</code> method will return zero.
     */
    public void clearInvocations();
}
