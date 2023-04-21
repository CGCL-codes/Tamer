package com.jkgh.remedium.states;

import java.nio.ByteBuffer;
import com.jkgh.asin.State;
import com.jkgh.remedium.helpers.RemediumReceiveContext;
import com.jkgh.remedium.objects.RemediumBlob;
import com.jkgh.remedium.objects.RemediumByte;
import com.jkgh.remedium.objects.RemediumCallObject;
import com.jkgh.remedium.objects.RemediumDouble;
import com.jkgh.remedium.objects.RemediumInitialData;
import com.jkgh.remedium.objects.RemediumInteger;
import com.jkgh.remedium.objects.RemediumList;
import com.jkgh.remedium.objects.RemediumMap;
import com.jkgh.remedium.objects.RemediumObject;
import com.jkgh.remedium.objects.RemediumReference;
import com.jkgh.remedium.objects.RemediumReturnObject;
import com.jkgh.remedium.objects.RemediumRunObject;
import com.jkgh.remedium.objects.RemediumString;

public abstract class ReceiveRemediumObjectState extends RemediumState {

    public static final byte LIST = 1;

    public static final byte MAP = 2;

    public static final byte REFERENCE = 3;

    public static final byte BYTE = 4;

    public static final byte INTEGER = 5;

    public static final byte DOUBLE = 6;

    public static final byte STRING = 7;

    public static final byte BLOB = 8;

    public static final byte INITIAL_DATA = 9;

    public static final byte CALL = 10;

    public static final byte RUN = 11;

    public static final byte RETURN = 12;

    public static final byte CALL_OBJECT = 13;

    public static final byte RUN_OBJECT = 14;

    public static final byte RETURN_OBJECT = 15;

    public static final byte HISTORY_INDEX = 16;

    public ReceiveRemediumObjectState(RemediumReceiveContext context) {
        super(context);
    }

    @Override
    public void bytesRead(ByteBuffer data) {
        machine.setState(new ReceiveByteState() {

            @Override
            protected State onReceived(byte b) {
                switch(b) {
                    case LIST:
                        return new ReceiveRemediumListState(context) {

                            @Override
                            protected State onReceived(RemediumList ret) {
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case MAP:
                        return new ReceiveRemediumMapState(context) {

                            @Override
                            protected State onReceived(RemediumMap ret) {
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case REFERENCE:
                        return new ReceiveRemediumReferenceState() {

                            @Override
                            protected State onReceived(int host, int referenceID) {
                                RemediumReference ret = context.getClient().getReference(host, referenceID);
                                context.toHistory(ret);
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case BYTE:
                        return new ReceiveByteState() {

                            @Override
                            protected State onReceived(byte b) {
                                RemediumByte ret = new RemediumByte(b);
                                context.toHistory(ret);
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case INTEGER:
                        return new ReceiveIntegerState() {

                            @Override
                            protected State onReceived(int i) {
                                RemediumInteger ret = new RemediumInteger(i);
                                context.toHistory(ret);
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case DOUBLE:
                        return new ReceiveDoubleState() {

                            @Override
                            protected State onReceived(double d) {
                                RemediumDouble ret = new RemediumDouble(d);
                                context.toHistory(ret);
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case STRING:
                        return new ReceiveRemediumStringState() {

                            @Override
                            protected State onReceived(RemediumString ret) {
                                context.toHistory(ret);
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case BLOB:
                        return new ReceiveRemediumBlobState(context) {

                            @Override
                            protected State onReceived(RemediumBlob ret) {
                                context.toHistory(ret);
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case INITIAL_DATA:
                        return new ReceiveRemediumInitialDataState(context) {

                            @Override
                            protected State onReceived(RemediumInitialData ret) {
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case CALL_OBJECT:
                        return new ReceiveRemediumCallObjectState(context) {

                            @Override
                            protected State onReceived(RemediumCallObject ret) {
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case RUN_OBJECT:
                        return new ReceiveRemediumRunObjectState(context) {

                            @Override
                            protected State onReceived(RemediumRunObject ret) {
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case RETURN_OBJECT:
                        return new ReceiveRemediumReturnObjectState(context) {

                            @Override
                            protected State onReceived(RemediumReturnObject ret) {
                                return ReceiveRemediumObjectState.this.onReceived(ret);
                            }
                        };
                    case HISTORY_INDEX:
                        return new ReceiveIntegerState() {

                            @Override
                            protected State onReceived(int received) {
                                return ReceiveRemediumObjectState.this.onReceived(context.fromHistory(received));
                            }
                        };
                    default:
                        machine.disconnect();
                        return State.IGNORE_REST;
                }
            }
        });
    }

    protected abstract State onReceived(RemediumObject ret);
}
