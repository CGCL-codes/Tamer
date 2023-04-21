package org.quickfix.fix40;

import org.quickfix.*;
import org.quickfix.field.*;

public class MessageCracker {

    public void onMessage(org.quickfix.Message message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(Advertisement message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(IndicationofInterest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(News message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(Email message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(QuoteRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(Quote message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(NewOrderSingle message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(ExecutionReport message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(DontKnowTrade message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(OrderCancelReplaceRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(OrderCancelRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(OrderCancelReject message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(OrderStatusRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(Allocation message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(AllocationACK message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(NewOrderList message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(ListStatus message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(ListExecute message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(ListCancelRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void onMessage(ListStatusRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        throw new UnsupportedMessageType();
    }

    public void crack(org.quickfix.Message message, SessionID sessionID) throws UnsupportedMessageType, FieldNotFound, IncorrectTagValue {
        crack40((Message) message, sessionID);
    }

    public void crack40(Message message, SessionID sessionID) throws UnsupportedMessageType, FieldNotFound, IncorrectTagValue {
        MsgType msgType = new MsgType();
        message.getHeader().getField(msgType);
        String msgTypeString = msgType.getValue();
        if (msgTypeString.length() > 1) {
            onMessage(message, sessionID);
            return;
        }
        switch(msgTypeString.charAt(0)) {
            case '0':
                onMessage((Heartbeat) message, sessionID);
                break;
            case 'A':
                onMessage((Logon) message, sessionID);
                break;
            case '1':
                onMessage((TestRequest) message, sessionID);
                break;
            case '2':
                onMessage((ResendRequest) message, sessionID);
                break;
            case '3':
                onMessage((Reject) message, sessionID);
                break;
            case '4':
                onMessage((SequenceReset) message, sessionID);
                break;
            case '5':
                onMessage((Logout) message, sessionID);
                break;
            case '7':
                onMessage((Advertisement) message, sessionID);
                break;
            case '6':
                onMessage((IndicationofInterest) message, sessionID);
                break;
            case 'B':
                onMessage((News) message, sessionID);
                break;
            case 'C':
                onMessage((Email) message, sessionID);
                break;
            case 'R':
                onMessage((QuoteRequest) message, sessionID);
                break;
            case 'S':
                onMessage((Quote) message, sessionID);
                break;
            case 'D':
                onMessage((NewOrderSingle) message, sessionID);
                break;
            case '8':
                onMessage((ExecutionReport) message, sessionID);
                break;
            case 'Q':
                onMessage((DontKnowTrade) message, sessionID);
                break;
            case 'G':
                onMessage((OrderCancelReplaceRequest) message, sessionID);
                break;
            case 'F':
                onMessage((OrderCancelRequest) message, sessionID);
                break;
            case '9':
                onMessage((OrderCancelReject) message, sessionID);
                break;
            case 'H':
                onMessage((OrderStatusRequest) message, sessionID);
                break;
            case 'J':
                onMessage((Allocation) message, sessionID);
                break;
            case 'P':
                onMessage((AllocationACK) message, sessionID);
                break;
            case 'E':
                onMessage((NewOrderList) message, sessionID);
                break;
            case 'N':
                onMessage((ListStatus) message, sessionID);
                break;
            case 'L':
                onMessage((ListExecute) message, sessionID);
                break;
            case 'K':
                onMessage((ListCancelRequest) message, sessionID);
                break;
            case 'M':
                onMessage((ListStatusRequest) message, sessionID);
                break;
            default:
                onMessage(message, sessionID);
        }
    }
}

;
