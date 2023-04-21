package com.wuala.server.loader.frontend.impl;

import java.nio.ByteBuffer;
import com.wuala.loader2.copied.client.messages.FinishPing;
import com.wuala.loader2.copied.client.messages.FinishPong;
import com.wuala.loader2.copied.client.messages.GetClassesPing;
import com.wuala.loader2.copied.client.messages.GetClassesPong;
import com.wuala.loader2.copied.client.messages.GetVersionPing;
import com.wuala.loader2.copied.client.messages.GetVersionPong;
import com.wuala.loader2.copied.client.messages.ILoader2MessageTypes;
import com.wuala.loader2.copied.client.messages.InitPing;
import com.wuala.loader2.copied.client.messages.InitPong;
import com.wuala.loader2.copied.client.messages.JumpToPing;
import com.wuala.loader2.copied.client.messages.JumpToPong;
import com.wuala.loader2.copied.client.messages.MarshallableMessage;
import com.wuala.server.common.rmi.mina.IMarshallableFactory;
import com.wuala.server.common.varia.ApplicationRuntimeException;

public class Loader2MessageFactory implements ILoader2MessageTypes, IMarshallableFactory {

    public MarshallableMessage create(ByteBuffer data) {
        return Loader2MessageFactory.createStatic(data);
    }

    public static MarshallableMessage createStatic(ByteBuffer data) {
        byte type = MarshallableMessage.peekType(data);
        switch(type) {
            case GETCLASSESPING:
                return new GetClassesPing(data);
            case GETCLASSESPONG:
                return new GetClassesPong(data);
            case INITPING:
                return new InitPing(data);
            case INITPONG:
                return new InitPong(data);
            case GETVERSIONPING:
                return new GetVersionPing(data);
            case GETVERSIONPONG:
                return new GetVersionPong(data);
            case FINISHPING:
                return new FinishPing(data);
            case FINISHPONG:
                return new FinishPong(data);
            case JUMPTOPING:
                return new JumpToPing(data);
            case JUMPTOPONG:
                return new JumpToPong(data);
            default:
                throw new ApplicationRuntimeException();
        }
    }
}
