package com.dyuproject.protostuff;

import java.io.IOException;

/**
 * Transfers data from an {@link Input} to an {@link Output}.
 * 
 * It is recommended to use pipe only to stream data coming from server-side 
 * services (e.g from your datastore/etc).
 * 
 * Incoming data from the interwebs should not be piped due to 
 * validation/security purposes.
 *
 * @author David Yu
 * @created Oct 6, 2010
 */
public abstract class Pipe {

    protected Input input;

    protected Output output;

    /**
     * Resets this pipe for re-use.
     */
    protected Pipe reset() {
        output = null;
        input = null;
        return this;
    }

    /**
     * Begin preliminary input processing.
     */
    protected abstract Input begin(Pipe.Schema<?> pipeSchema) throws IOException;

    /**
     * End input processing.
     * 
     * If {@code cleanupOnly} is true, the io processing ended prematurely hence the 
     * underlying pipe should cleanup/close all resources that need to be.
     */
    protected abstract void end(Pipe.Schema<?> pipeSchema, Input input, boolean cleanupOnly) throws IOException;

    /**
     * Schema for transferring data from a source ({@link Input}) to a 
     * different sink ({@link Output}).
     */
    public abstract static class Schema<T> implements com.dyuproject.protostuff.Schema<Pipe> {

        public final com.dyuproject.protostuff.Schema<T> wrappedSchema;

        public Schema(com.dyuproject.protostuff.Schema<T> wrappedSchema) {
            this.wrappedSchema = wrappedSchema;
        }

        public String getFieldName(int number) {
            return wrappedSchema.getFieldName(number);
        }

        public int getFieldNumber(String name) {
            return wrappedSchema.getFieldNumber(name);
        }

        /**
         * Always returns true since we're just transferring data.
         */
        public boolean isInitialized(Pipe message) {
            return true;
        }

        public String messageFullName() {
            return wrappedSchema.messageFullName();
        }

        public String messageName() {
            return wrappedSchema.messageName();
        }

        public Pipe newMessage() {
            throw new UnsupportedOperationException();
        }

        public Class<Pipe> typeClass() {
            throw new UnsupportedOperationException();
        }

        public final void writeTo(final Output output, final Pipe pipe) throws IOException {
            if (pipe.output == null) {
                pipe.output = output;
                final Input input = pipe.begin(this);
                if (input == null) {
                    pipe.output = null;
                    pipe.end(this, input, true);
                    return;
                }
                pipe.input = input;
                boolean transferComplete = false;
                try {
                    transfer(pipe, input, output);
                    transferComplete = true;
                } finally {
                    pipe.end(this, input, !transferComplete);
                }
                return;
            }
            pipe.input.mergeObject(pipe, this);
        }

        public final void mergeFrom(final Input input, final Pipe pipe) throws IOException {
            transfer(pipe, input, pipe.output);
        }

        /**
         * Transfer data from the {@link Input} to the {@link Output}.
         */
        protected abstract void transfer(Pipe pipe, Input input, Output output) throws IOException;
    }

    /**
     * This should not be called directly by applications.
     */
    public static <T> void transferDirect(Pipe.Schema<T> pipeSchema, Pipe pipe, Input input, Output output) throws IOException {
        pipeSchema.transfer(pipe, input, output);
    }
}
