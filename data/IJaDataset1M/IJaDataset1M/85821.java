package org.wtc.eclipse.platform.helpers;

import com.windowtester.runtime.IUIContext;
import org.eclipse.core.runtime.IPath;

/**
 * Helper for interacting with the views in the debug perspective and for verifying
 * debugging operations are executed as expected with a currently running process (In
 * other words, if a test is testing that a breakpoint will be hit in an application
 * deployed to a server, it is the responsibility of the caller to start that server in
 * debug mode.
 * 
 * @since 3.8.0
 */
public interface IJavaDebuggingHelper {

    /**
     * removeAllBreakpoints - Remove all breakpoints in the current workspace.
     *
     * @since 3.8.0
     * @param  ui  - Driver for UI generated input
     */
    public void removeAllBreakpoints(IUIContext ui);

    /**
     * resumeDebugging - Resume debugging a suspended process.
     *
     * @since 3.8.0
     * @param  ui  - Driver for UI generated input
     */
    public void resumeDebugging(IUIContext ui);

    /**
     * setLineBreakpoint - Add a breakpoint to the given file on the given line.
     *
     * @since 3.8.0
     * @param  ui              - Driver for UI generated input
     * @param  filePath        - The full path (project included) of the file in which to
     *                         set a breakpoint.
     * @param  breakpointLine  - The line number (Eclipse files start numbering files with
     *                         line number 1) on which to set a breakpoint. If the given
     *                         line number is greater than the line length of the given
     *                         file, this method will issue a test case failure. Note that
     *                         if the line number is placed on a non-executable line (in a
     *                         Java comment or on a line with a brace ('{') for a
     *                         conditional statement, the breakpoint may not be set or may
     *                         jump to the nearest possible executable line depending on
     *                         internal Eclipse breakpoint processing and this method will
     *                         issue a test case failure
     */
    public void setLineBreakpoint(IUIContext ui, IPath filePath, int breakpointLine);

    /**
     * setMethodBreakpoint - Locate the first occurrence of the method with the given
     * signature search parameters, and toggle a method breakpoint on that method.
     *
     * @since 3.8.0
     * @param  ui                - Driver for UI generated input
     * @param  filePath          - The full path (project included) of the file in which
     *                           to set a breakpoint. Must be a Java file
     * @param  typeName          - The short name of the enclosing type of the method that
     *                           will have a breakpoint set
     * @param  methodName        - The name of the method that is to have a breakpoint set
     * @param  methodReturnType  - The return type (or "void") that appears on the method
     *                           signature whose breakpoint should be set
     */
    public void setMethodBreakpoint(IUIContext ui, IPath filePath, String typeName, String methodName, String methodReturnType);

    /**
     * verifyLineBreakpointExists - Wait for a line breakpoint in the given file and the
     * given line to have the given expected existence.
     *
     * @since 3.8.0
     * @param  ui              - Driver for UI generated input
     * @param  filePath        - Full path (project included) of the file whose
     *                         breakpoints (if any) are to be verified
     * @param  breakpointLine  - The line number of the file to search for a breakpoint
     * @param  boolean         exists - True if the breakpoint exists, false otherwise
     */
    public void verifyLineBreakpointExists(IUIContext ui, IPath filePath, int breakpointLine, boolean exists);

    /**
     * verifyLineBreakpointExists - Wait for a line breakpoint in the given file and the
     * given line to have the given expected existence.
     *
     * @since 3.8.0
     * @param  ui                - Driver for UI generated input
     * @param  filePath          - Full path (project included) of the file whose
     *                           breakpoints (if any) are to be verified
     * @param  typeName          - The short name of the enclosing type of the method that
     *                           to contains the breakpoint to look for
     * @param  methodName        - The name of the to scan for method breakpoints
     * @param  methodReturnType  - The return type (or "void") that appears on the method
     *                           signature to scan for breakpoints
     * @param  boolean           exists - True if the breakpoint exists, false otherwise
     */
    public void verifyMethodBreakpointExists(IUIContext ui, IPath filePath, String typeName, String methodName, String methodReturnType, boolean exists);

    /**
     * verifySuspendedAtLine - Wait until a debugged process exists and is suspended at
     * the given line in the given file.  It is the responsibility of the caller to begin
     * a process in debug mode, and to resume a suspended process.
     *
     * @since 3.8.0
     * @param  ui              - Driver for UI generated input
     * @param  filePath        - Full path (project included) of the file that a debugging
     *                         process is expected to be suspended within
     * @param  breakpointLine  - Line number of the file that a debugging process is to be
     *                         suspended at in the given file.
     */
    public void verifySuspendedAtLine(IUIContext ui, IPath filePath, int breakpointLine);

    /**
     * verifySuspendedAtMethod - Wait until a debugged process exists and is suspended at
     * the given method in the given file.  It is the responsibility of the caller to
     * begin a process in debug mode, and to resume a suspended process.
     *
     * @since 3.8.0
     * @param  ui                - Driver for UI generated input
     * @param  filePath          - Full path (project included) of the file that a
     *                           debugging process is expected to be suspended within
     * @param  typeName          - The short name of the enclosing type of the method that
     *                           will have a breakpoint set
     * @param  methodName        - The name of the method that is to have a breakpoint set
     * @param  methodReturnType  - The return type (or "void") that appears on the method
     *                           signature whose breakpoint should be set
     */
    public void verifySuspendedAtMethod(IUIContext ui, IPath filePath, String typeName, String methodName, String methodReturnType);
}
