package com.thesett.common.parsing;

/**
 * SourceCodePosition represents a position in a text based source code file, often during parsing or compilation of
 * program code, but could be used for any kink of text file. It can be used to identify the position of an error in the
 * source code.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th>Responsibilities
 * <tr>
 * <td>Identify a location within a source file.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public interface SourceCodePosition {

    /**
     * Provides the line on which the error begins.
     *
     * @return The line on which the error begins.
     */
    public int getStartLine();

    /**
     * Provides the column on which the error begins.
     *
     * @return The column on which the error begins.
     */
    public int getStartColumn();

    /**
     * Provides the line on which the error ends.
     *
     * @return The line on which the error ends.
     */
    public int getEndLine();

    /**
     * Provides the column on which the error ends.
     *
     * @return The column on which the error ends.
     */
    public int getEndColumn();
}
