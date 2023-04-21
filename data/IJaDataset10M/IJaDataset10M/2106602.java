package com.jkgh.remedium.states;

import java.nio.ByteBuffer;
import com.jkgh.asin.State;
import com.jkgh.remedium.RemediumClient;

public abstract class ReceiveDoubleState extends State {

    private static final byte MISSING_8 = 1;

    private static final byte MISSING_7 = 2;

    private static final byte MISSING_6 = 3;

    private static final byte MISSING_5 = 4;

    private static final byte MISSING_4 = 5;

    private static final byte MISSING_3 = 6;

    private static final byte MISSING_2 = 7;

    private static final byte MISSING_1 = 8;

    private byte state;

    private final byte[] received;

    public ReceiveDoubleState() {
        state = MISSING_4;
        received = new byte[8];
    }

    @Override
    public void bytesRead(ByteBuffer data) {
        if (state == MISSING_8 && data.remaining() > 7) {
            machine.setState(onReceived(data.getDouble()));
        } else {
            byteProcessor: while (data.hasRemaining()) {
                byte read = data.get();
                switch(state) {
                    case MISSING_8:
                        received[0] = read;
                        state = MISSING_7;
                        continue byteProcessor;
                    case MISSING_7:
                        received[1] = read;
                        state = MISSING_6;
                        continue byteProcessor;
                    case MISSING_6:
                        received[2] = read;
                        state = MISSING_5;
                        continue byteProcessor;
                    case MISSING_5:
                        received[3] = read;
                        state = MISSING_4;
                        continue byteProcessor;
                    case MISSING_4:
                        received[4] = read;
                        state = MISSING_3;
                        continue byteProcessor;
                    case MISSING_3:
                        received[5] = read;
                        state = MISSING_2;
                        continue byteProcessor;
                    case MISSING_2:
                        received[6] = read;
                        state = MISSING_1;
                        continue byteProcessor;
                    case MISSING_1:
                        received[7] = read;
                        double d = RemediumClient.byteArrayToDouble(received);
                        machine.setState(onReceived(d));
                        break byteProcessor;
                }
            }
        }
    }

    protected abstract State onReceived(double d);
}
