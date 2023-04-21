package com.googlecode.dni.internal.callback;

import java.nio.ByteBuffer;
import com.googlecode.dni.internal.callback.ThunkManager.ThunkManagerDelegate;
import com.googlecode.dni.type.Pointer;

/**
 * Implementation of {@link ThunkManagerDelegate} for X86.
 *
 * @author Matthew Wilson
 */
public final class X86ThunkManagerDelegate extends AbstractThunkManagerDelegate {

    /** Conveniently, both the jump and entry table entries are 16 bytes long. */
    private static final int ENTRY_SIZE = 16;

    private final int thunkAddress;

    /**
     * @param thunkAddress
     *            the generic thunk address
     */
    public X86ThunkManagerDelegate(final Pointer thunkAddress) {
        if (Pointer.isNull(thunkAddress)) {
            throw new IllegalArgumentException();
        }
        this.thunkAddress = (int) thunkAddress.address();
        assert NATIVE_ENTRY_SIZE == ENTRY_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public int getJumpEntrySize() {
        return ENTRY_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public int getInfoEntrySize() {
        return ENTRY_SIZE;
    }

    /** {@inheritDoc} */
    @Override
    public void writeJumpEntry(final ByteBuffer jumpBuffer, final long infoAddress) {
        jumpBuffer.put((byte) 0xbb);
        jumpBuffer.putInt((int) infoAddress);
        jumpBuffer.put((byte) 0xb8);
        jumpBuffer.putInt(this.thunkAddress);
        jumpBuffer.put((byte) 0xff);
        jumpBuffer.put((byte) 0xe0);
        while (jumpBuffer.hasRemaining()) {
            jumpBuffer.put((byte) 0xcc);
        }
    }
}
