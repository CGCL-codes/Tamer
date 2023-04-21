package org.gjt.sp.jedit.buffer;

/**
 * A interface for notification of buffer undo/redo actions.
 *
 * This interface makes it easier for undo-aware plugins to process
 * undo/redo actions in a buffer.
 *
 * Buffer undo listeners are added and removed from a buffer using
 * <code>JEditBuffer.addBufferUndoListener<code> and
 * <code>JEditBuffer.removeBufferUndoListener<code>, respectively.
 *
 * @author Shlomy Reinstein
 * @version $Id: BufferUndoListener.java 16098 2009-08-27 21:59:29Z shlomy $
 * @since jEdit 4.3pre18
 */
public interface BufferUndoListener {

    /**
	 * Called when an undo operation on the buffer begins.
	 * @param buffer The buffer in question
	 */
    void beginUndo(JEditBuffer buffer);

    /**
	 * Called when an undo operation on the buffer ends.
	 * @param buffer The buffer in question
	 */
    void endUndo(JEditBuffer buffer);

    /**
	 * Called when a redo on the buffer begins.
	 * @param buffer The buffer in question
	 */
    void beginRedo(JEditBuffer buffer);

    /**
	 * Called when a redo on the buffer ends.
	 * @param buffer The buffer in question
	 */
    void endRedo(JEditBuffer buffer);
}
