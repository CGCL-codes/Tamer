package com.bccapi.api;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.bccapi.core.CompactInt;
import com.bccapi.core.SendCoinFormValidator;

/**
 * Contains all the information necessary to sign and verify a transaction that
 * sends bitcoins to a bitcoin address. Before signing a transaction the client
 * should validate the amounts being signed and the receivers. This can be done
 * using the {@link SendCoinFormValidator}.
 */
public class SendCoinForm {

    private Tx _tx;

    private List<TxOutput> _txFunding;

    private List<Integer> _keyIndexes;

    public SendCoinForm(Tx tx, List<Integer> keyIndexes, List<TxOutput> txFunding) {
        _tx = tx;
        _txFunding = txFunding;
        _keyIndexes = keyIndexes;
    }

    /**
    * Get the transaction of this form.
    * 
    * @return The transaction of this form.
    */
    public Tx getTransaction() {
        return _tx;
    }

    /**
    * Get the list of private key indexes that should sign the inputs of the
    * transactions.
    * 
    * @return The list of private key indexes that should sign the inputs of the
    *         transactions.
    */
    public List<Integer> getKeyIndexes() {
        return _keyIndexes;
    }

    /**
    * Get the list of transaction outputs that fund the inputs of the
    * transaction. This allows the client to verify how many BTC is being sent.
    * 
    * @return The list of transaction outputs that fund the inputs of the
    *         transaction.
    */
    public List<TxOutput> getFunding() {
        return _txFunding;
    }

    /**
    * Deserialize a {@link SendCoinForm} from a {@link DataInputStream}.
    * 
    * @param reader
    *           The {@link DataInputStream} to deserialize from.
    * @return A {@link SendCoinForm}.
    * @throws IOException
    */
    public static SendCoinForm fromStream(DataInputStream reader) throws IOException {
        Tx tx = Tx.fromStream(reader);
        int indexes = (int) CompactInt.fromStream(reader);
        List<Integer> keyIndexes = new ArrayList<Integer>(indexes);
        for (int i = 0; i < indexes; i++) {
            keyIndexes.add((int) CompactInt.fromStream(reader));
        }
        int fundLength = (int) CompactInt.fromStream(reader);
        List<TxOutput> txFunding = new ArrayList<TxOutput>(fundLength);
        for (int i = 0; i < fundLength; i++) {
            txFunding.add(TxOutput.fromStream(reader));
        }
        return new SendCoinForm(tx, keyIndexes, txFunding);
    }

    /**
    * Serialize a {@link SendCoinForm} instance.
    * 
    * @param stream
    *           the stream to serialize to.
    * @throws IOException
    */
    public void toStream(OutputStream stream) throws IOException {
        _tx.toStream(stream);
        CompactInt.toStream(_keyIndexes.size(), stream);
        for (int index : _keyIndexes) {
            CompactInt.toStream(index, stream);
        }
        CompactInt.toStream(_txFunding.size(), stream);
        for (TxOutput out : _txFunding) {
            out.toStream(stream);
        }
    }
}
