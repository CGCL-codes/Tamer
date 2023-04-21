package com.bccapi.api;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.bccapi.core.BitUtils;
import com.bccapi.core.CompactInt;

/**
 * A bitcoin transaction.
 */
public class Tx {

    private long _version;

    private List<TxInput> _inputs;

    private List<TxOutput> _outputs;

    private long _lockTime;

    /**
    * Construct a transaction that has no inputs or outputs.
    */
    public Tx() {
        _version = 1;
        _inputs = new ArrayList<TxInput>();
        _outputs = new ArrayList<TxOutput>();
    }

    /**
    * Construct a transaction with a number of inputs and outputs.
    * 
    * @param version
    *           The version to use
    * @param inputs
    *           The inputs
    * @param outputs
    *           The outputs
    * @param lockTime
    *           The lock time
    */
    public Tx(long version, List<TxInput> inputs, List<TxOutput> outputs, long lockTime) {
        _version = version;
        _inputs = inputs;
        _outputs = outputs;
        _lockTime = lockTime;
    }

    /**
    * Copy constructor for transaction
    * 
    * @param other
    *           The Tx to copy
    */
    public Tx(Tx other) {
        _version = other._version;
        _inputs = new ArrayList<TxInput>();
        for (TxInput input : other._inputs) {
            _inputs.add(new TxInput(input));
        }
        _outputs = new ArrayList<TxOutput>();
        for (TxOutput output : other._outputs) {
            _outputs.add(new TxOutput(output));
        }
        _lockTime = other._lockTime;
    }

    /**
    * Get the bitcoin version of this transaction.
    * 
    * @return The bitcoin version of this transaction.
    */
    public long getVersion() {
        return _version;
    }

    /**
    * Get the inputs of this transaction.
    * 
    * @return The inputs of this transaction.
    */
    public List<TxInput> getInputs() {
        return _inputs;
    }

    /**
    * Get the outputs of this transaction.
    * 
    * @return The outputs of this transaction.
    */
    public List<TxOutput> getOutputs() {
        return _outputs;
    }

    /**
    * Get the lock time of this transaction.
    * 
    * @return The lock time of this transaction.
    */
    public long getLockTime() {
        return _lockTime;
    }

    /**
    * Deserialize a {@link Tx} from a {@link DataInputStream}.
    * 
    * @param stream
    *           The {@link DataInputStream} to deserialize from.
    * @return A {@link Tx}.
    * @throws IOException
    */
    public static Tx fromStream(DataInputStream stream) throws IOException {
        long version = BitUtils.uint32FromStream(stream);
        long numInputs = CompactInt.fromStream(stream);
        List<TxInput> inputs = new ArrayList<TxInput>();
        for (int i = 0; i < numInputs; i++) {
            inputs.add(TxInput.fromStream(stream));
        }
        long numOutputs = CompactInt.fromStream(stream);
        List<TxOutput> outputs = new ArrayList<TxOutput>();
        for (int i = 0; i < numOutputs; i++) {
            outputs.add(TxOutput.fromStream(stream));
        }
        long lockTime = BitUtils.uint32FromStream(stream);
        return new Tx(version, inputs, outputs, lockTime);
    }

    /**
    * Serialize the transaction to a stream.
    * 
    * @param stream
    *           the {@link OutputStream} serialize to.
    * @throws IOException
    */
    public void toStream(OutputStream stream) throws IOException {
        BitUtils.uint32ToStream(_version, stream);
        CompactInt.toStream(_inputs.size(), stream);
        for (TxInput input : _inputs) {
            input.toStream(stream);
        }
        CompactInt.toStream(_outputs.size(), stream);
        for (TxOutput output : _outputs) {
            output.toStream(stream);
        }
        BitUtils.uint32ToStream(_lockTime, stream);
    }

    /**
    * Serialize the transaction to an array of bytes
    * 
    * @return The transaction serialized to an array of bytes
    */
    public byte[] toByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            toStream(stream);
        } catch (IOException e) {
        }
        return stream.toByteArray();
    }
}
