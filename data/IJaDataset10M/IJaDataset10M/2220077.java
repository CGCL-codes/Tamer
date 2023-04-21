package com.thesett.common.util;

/**
 * A SizeableQueue is a Queue that can calculate its data size.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th>Responsibilities
 * <tr>
 * <td>Report the estimated byte size of a queue.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public interface SizeableQueue<E> extends Queue<E>, Sizeable {
}
