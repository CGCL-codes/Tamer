package com.akop.spark;

import java.io.IOException;
import android.content.Context;

public interface ISupportsMessaging extends IAccount {

    void updateMessages(Context context) throws AuthenticationException, IOException, ParserException;

    void updateMessage(Context context, Object messageId) throws AuthenticationException, IOException, ParserException;

    void sendMessage(Context context, String[] recipients, String body) throws AuthenticationException, IOException, ParserException;

    void deleteMessage(Context context, Object messageId) throws AuthenticationException, IOException, ParserException;
}
