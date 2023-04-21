package fr.x9c.cadmium.primitives.stdlib;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import fr.x9c.cadmium.kernel.Block;
import fr.x9c.cadmium.kernel.Channel;
import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Context;
import fr.x9c.cadmium.kernel.Custom;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.FalseExit;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;
import fr.x9c.cadmium.util.IO;
import fr.x9c.cadmium.util.MemoryInputStream;

/**
 * Implements all primitives from 'io.c'.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Io {

    /** New line character. */
    private static final int NEW_LINE = 0x0A;

    /** Carriage return character. */
    private static final int CARRIAGE_RETURN = 0x0D;

    /**
     * No instance of this class.
     */
    private Io() {
    }

    /**
     * Creates an input channel from a file descriptor.
     * @param ctxt context
     * @param fd file descriptor
     * @return input channel for <tt>fd</tt>
     */
    @Primitive
    public static Value caml_ml_open_descriptor_in(final CodeRunner ctxt, final Value fd) {
        final Block c = Block.createCustom(Custom.CHANNEL_SIZE, Custom.CHANNEL_OPS);
        c.setCustom(ctxt.getContext().getChannel(fd.asLong()));
        return Value.createFromBlock(c);
    }

    /**
     * Creates an output channel from a file descriptor.
     * @param ctxt context
     * @param fd file descriptor
     * @return output channel for <tt>fd</tt>
     */
    @Primitive
    public static Value caml_ml_open_descriptor_out(final CodeRunner ctxt, final Value fd) {
        final Block c = Block.createCustom(Custom.CHANNEL_SIZE, Custom.CHANNEL_OPS);
        c.setCustom(ctxt.getContext().getChannel(fd.asLong()));
        return Value.createFromBlock(c);
    }

    /**
     * Returns a list containing all currently opened output channels.
     * @param ctxt context
     * @param unit ignored
     * @return a list containing all currently opened output channels
     */
    @Primitive
    public static Value caml_ml_out_channels_list(final CodeRunner ctxt, final Value unit) {
        return ctxt.getContext().makeOutChannelsList();
    }

    /**
     * Returns the file descriptor of a channel.
     * @param ctxt context
     * @param channel channel
     * @return the file descriptor of <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_channel_descriptor(final CodeRunner ctxt, final Value channel) throws Fail.Exception {
        final int res = ((Channel) channel.asBlock().asCustom()).getFD();
        if (res != -1) {
            return Value.createFromLong(res);
        } else {
            Sys.sysError(null, "invalid file descriptor");
            return Value.UNIT;
        }
    }

    /**
     * Closes a channel.
     * @param ctxt context
     * @param channel channel to close
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_close_channel(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final int fd = ((Channel) channel.asBlock().asCustom()).getFD();
            if (fd != -1) {
                ctxt.getContext().closeChannel(fd);
            }
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Returns the size of a channel, as a long.
     * @param ctxt context
     * @param channel channel to get size from
     * @return the size of <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_channel_size(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final long l = ((Channel) channel.asBlock().asCustom()).size();
            if (l > Value.MAX_LONG) {
                Sys.sysError(null, "overflow");
            }
            return Value.createFromLong((int) l);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, "unable to determine channel size");
            return Value.UNIT;
        }
    }

    /**
     * Returns the size of a channel, as a long.
     * @param ctxt context
     * @param channel channel to get size from
     * @return the size of <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_channel_size_64(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final long l = ((Channel) channel.asBlock().asCustom()).size();
            final Block b = Block.createCustom(Custom.INT_64_SIZE, Custom.INT_64_OPS);
            b.setInt64(l);
            return Value.createFromBlock(b);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, "unable to determine channel size");
            return Value.UNIT;
        }
    }

    /**
     * Does nothing.
     * @param ctxt context
     * @param channel ignored
     * @param mode ignored
     * @return <i>unit</i>
     */
    @Primitive
    public static Value caml_ml_set_binary_mode(final CodeRunner ctxt, final Value channel, final Value mode) {
        return Value.UNIT;
    }

    /**
     * Flushes a channel. <br/>
     * Exact synonym of {@link #caml_ml_flush(CodeRunner, Value)}.
     * @param ctxt context
     * @param channel output channel to flush
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_flush_partial(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        caml_ml_flush(ctxt, channel);
        return Value.TRUE;
    }

    /**
     * Flushes a channel.
     * @param ctxt context
     * @param channel output channel to flush
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_flush(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            ((Channel) channel.asBlock().asCustom()).flush();
            context.leaveBlockingSection();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Writes a character onto a channel.
     * @param ctxt context
     * @param channel output channel
     * @param ch character value to write
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_output_char(final CodeRunner ctxt, final Value channel, final Value ch) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            IO.write8u(((Channel) channel.asBlock().asCustom()).asDataOutput(), ch.asLong());
            context.leaveBlockingSection();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Writes a long value onto a channel.
     * @param ctxt context
     * @param channel output channel
     * @param v long value to write
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_output_int(final CodeRunner ctxt, final Value channel, final Value v) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            IO.write32s(((Channel) channel.asBlock().asCustom()).asDataOutput(), v.asLong());
            context.leaveBlockingSection();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Writes bytes from a string/buffer onto a channel. <br/>
     * Exact synonym of {@link #caml_ml_output(CodeRunner, Value, Value, Value, Value)}.
     * @param ctxt context
     * @param buff string/buffer
     * @param start source index of bytes to write
     * @param length number of bytes to write
     * @return <i>unit</i>
     */
    @Primitive
    public static Value caml_ml_output_partial(final CodeRunner ctxt, final Value channel, final Value buff, final Value start, final Value length) throws Fail.Exception, FalseExit {
        caml_ml_output(ctxt, channel, buff, start, length);
        return length;
    }

    /**
     * Writes bytes from a string/buffer onto a channel.
     * @param ctxt context
     * @param buff string/buffer
     * @param start source index of bytes to write
     * @param len number of bytes to write
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_output(final CodeRunner ctxt, final Value channel, final Value buff, final Value start, final Value len) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            ((Channel) channel.asBlock().asCustom()).asDataOutput().write(buff.asBlock().getBytes(), start.asLong(), len.asLong());
            context.leaveBlockingSection();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Changes the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel output channel
     * @param pos new position of file pointer, as a long
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_seek_out(final CodeRunner ctxt, final Value channel, final Value pos) throws Fail.Exception, FalseExit {
        try {
            ((Channel) channel.asBlock().asCustom()).seek(pos.asLong(), Channel.SEEK_SET);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Changes the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel output channel
     * @param pos new position of file pointer, as an int64
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_seek_out_64(final CodeRunner ctxt, final Value channel, final Value pos) throws Fail.Exception, FalseExit {
        try {
            ((Channel) channel.asBlock().asCustom()).seek(pos.asBlock().asInt64(), Channel.SEEK_SET);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Returns the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel outut channel
     * @return position of the file pointer for <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_pos_out(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final long pos = ((Channel) channel.asBlock().asCustom()).pos();
            if (pos > Value.MAX_LONG) {
                Sys.sysError(null, "overflow");
            }
            return Value.createFromLong((int) pos);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, "unable to determine channel position");
            return Value.UNIT;
        }
    }

    /**
     * Returns the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel output channel
     * @return position of the file pointer for <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_pos_out_64(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final long pos = ((Channel) channel.asBlock().asCustom()).pos();
            final Block res = Block.createCustom(Custom.INT_64_SIZE, Custom.INT_64_OPS);
            res.setInt64(pos);
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, "unable to determine channel position");
            return Value.UNIT;
        }
    }

    /**
     * Reads a character from a channel.
     * @param ctxt context
     * @param channel input channel
     * @return character read
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_input_char(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            final int res = IO.read8u(((Channel) channel.asBlock().asCustom()).asDataInput());
            context.leaveBlockingSection();
            return Value.createFromLong(res);
        } catch (final EOFException eof) {
            context.leaveBlockingSection();
            Fail.raiseEndOfFile();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Reads an long from a channel.
     * @param ctxt context
     * @param channel input channel
     * @return long read
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_input_int(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            final int res = IO.read32s(((Channel) channel.asBlock().asCustom()).asDataInput());
            context.leaveBlockingSection();
            return Value.createFromLong(res);
        } catch (final EOFException eof) {
            context.leaveBlockingSection();
            Fail.raiseEndOfFile();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Reads bytes from a channel into a string/buffer.
     * @param ctxt context
     * @param channel input channel
     * @param buff string/buffer
     * @param start destination offset of read bytes
     * @param len number of bytes to read
     * @return actual number of bytes read
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_input(final CodeRunner ctxt, final Value channel, final Value buff, final Value start, final Value len) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        try {
            context.enterBlockingSection();
            final int res = ((Channel) channel.asBlock().asCustom()).read(buff.asBlock().getBytes(), start.asLong(), len.asLong());
            context.leaveBlockingSection();
            return Value.createFromLong(Math.max(res, 0));
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Changes the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel input channel
     * @param pos new position of file pointer, as a long
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_seek_in(final CodeRunner ctxt, final Value channel, final Value pos) throws Fail.Exception, FalseExit {
        try {
            ((Channel) channel.asBlock().asCustom()).seek(pos.asLong(), Channel.SEEK_SET);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Changes the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel input channel
     * @param pos new position of file pointer, as an int64
     * @return <i>unit</i>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_seek_in_64(final CodeRunner ctxt, final Value channel, final Value pos) throws Fail.Exception, FalseExit {
        try {
            ((Channel) channel.asBlock().asCustom()).seek(pos.asBlock().asInt64(), Channel.SEEK_SET);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, ioe.toString());
        }
        return Value.UNIT;
    }

    /**
     * Returns the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel input channel
     * @return position of the file pointer for <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_pos_in(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final long pos = ((Channel) channel.asBlock().asCustom()).pos();
            if (pos > Value.MAX_LONG) {
                Sys.sysError(null, "overflow");
            }
            return Value.createFromLong((int) pos);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, "unable to determine channel position");
            return Value.UNIT;
        }
    }

    /**
     * Returns the position of the file pointer for a given channel.
     * @param ctxt context
     * @param channel input channel
     * @return position of the file pointer for <tt>channel</tt>
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_pos_in_64(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        try {
            final long pos = ((Channel) channel.asBlock().asCustom()).pos();
            final Block res = Block.createCustom(Custom.INT_64_SIZE, Custom.INT_64_OPS);
            res.setInt64(pos);
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Sys.sysError(null, "unable to determine channel position");
            return Value.UNIT;
        }
    }

    /**
     * Reads data from a channel until a end-of-line character is read. <br/>
     * Input channel position is not modified (read bytes are "pushed back").
     * @param ctxt context
     * @param channel input channel
     * @return number of character read
     * @throws Fail.Exception if an i/o error occurs
     */
    @Primitive
    public static Value caml_ml_input_scan_line(final CodeRunner ctxt, final Value channel) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final Channel ch = (Channel) channel.asBlock().asCustom();
        final RandomAccessFile r = ch.asStream();
        final MemoryInputStream m = ch.asMemStream();
        try {
            context.enterBlockingSection();
            if (r != null) {
                final long begin = r.getFilePointer();
                int b = r.read();
                while ((b != Io.NEW_LINE) && (b != Io.CARRIAGE_RETURN) && (b != -1)) {
                    b = r.read();
                }
                final long end = r.getFilePointer();
                r.seek(begin);
                context.leaveBlockingSection();
                return Value.createFromLong((int) (end - begin));
            } else if (m != null) {
                final long begin = m.getPosition();
                int b = m.read();
                while ((b != Io.NEW_LINE) && (b != Io.CARRIAGE_RETURN) && (b != -1)) {
                    b = m.read();
                }
                final long end = m.getPosition();
                m.setPosition((int) begin);
                context.leaveBlockingSection();
                return Value.createFromLong((int) (end - begin));
            } else {
                final PushbackInputStream pis = ch.asInputStream();
                final byte[] buffer = new byte[Channel.BUFFER_SIZE];
                int ptr = 0;
                int b = pis.read();
                buffer[ptr++] = (byte) (b & 0xFF);
                while ((b != Io.NEW_LINE) && (b != Io.CARRIAGE_RETURN) && (b != -1)) {
                    b = pis.read();
                    buffer[ptr++] = (byte) (b & 0xFF);
                }
                pis.unread(buffer, 0, ptr);
                context.leaveBlockingSection();
                return Value.createFromLong(ptr);
            }
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Sys.sysError(null, ioe.toString());
            return Value.UNIT;
        }
    }
}
