package org.quickfix.fix43;

import org.quickfix.FieldNotFound;
import org.quickfix.field.*;

public class Message extends org.quickfix.Message {

    public Message() {
        super();
        header = new Header();
        trailer = new Trailer();
        getHeader().setField(new BeginString("FIX.4.3"));
    }

    public class Header extends org.quickfix.Message.Header {

        public void set(BeginString value) {
            super.setField(value);
        }

        BeginString get(BeginString value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(BodyLength value) {
            super.setField(value);
        }

        BodyLength get(BodyLength value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(MsgType value) {
            super.setField(value);
        }

        MsgType get(MsgType value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(SenderCompID value) {
            super.setField(value);
        }

        SenderCompID get(SenderCompID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(TargetCompID value) {
            super.setField(value);
        }

        TargetCompID get(TargetCompID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(OnBehalfOfCompID value) {
            super.setField(value);
        }

        OnBehalfOfCompID get(OnBehalfOfCompID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(DeliverToCompID value) {
            super.setField(value);
        }

        DeliverToCompID get(DeliverToCompID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(SecureDataLen value) {
            super.setField(value);
        }

        SecureDataLen get(SecureDataLen value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(SecureData value) {
            super.setField(value);
        }

        SecureData get(SecureData value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(MsgSeqNum value) {
            super.setField(value);
        }

        MsgSeqNum get(MsgSeqNum value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(SenderSubID value) {
            super.setField(value);
        }

        SenderSubID get(SenderSubID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(SenderLocationID value) {
            super.setField(value);
        }

        SenderLocationID get(SenderLocationID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(TargetSubID value) {
            super.setField(value);
        }

        TargetSubID get(TargetSubID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(TargetLocationID value) {
            super.setField(value);
        }

        TargetLocationID get(TargetLocationID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(OnBehalfOfSubID value) {
            super.setField(value);
        }

        OnBehalfOfSubID get(OnBehalfOfSubID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(OnBehalfOfLocationID value) {
            super.setField(value);
        }

        OnBehalfOfLocationID get(OnBehalfOfLocationID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(DeliverToSubID value) {
            super.setField(value);
        }

        DeliverToSubID get(DeliverToSubID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(DeliverToLocationID value) {
            super.setField(value);
        }

        DeliverToLocationID get(DeliverToLocationID value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(PossDupFlag value) {
            super.setField(value);
        }

        PossDupFlag get(PossDupFlag value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(PossResend value) {
            super.setField(value);
        }

        PossResend get(PossResend value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(SendingTime value) {
            super.setField(value);
        }

        SendingTime get(SendingTime value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(OrigSendingTime value) {
            super.setField(value);
        }

        OrigSendingTime get(OrigSendingTime value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(XmlDataLen value) {
            super.setField(value);
        }

        XmlDataLen get(XmlDataLen value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(XmlData value) {
            super.setField(value);
        }

        XmlData get(XmlData value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(MessageEncoding value) {
            super.setField(value);
        }

        MessageEncoding get(MessageEncoding value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(LastMsgSeqNumProcessed value) {
            super.setField(value);
        }

        LastMsgSeqNumProcessed get(LastMsgSeqNumProcessed value) throws FieldNotFound {
            super.getField(value);
            return value;
        }

        public void set(OnBehalfOfSendingTime value) {
            super.setField(value);
        }

        OnBehalfOfSendingTime get(OnBehalfOfSendingTime value) throws FieldNotFound {
            super.getField(value);
            return value;
        }
    }
}
