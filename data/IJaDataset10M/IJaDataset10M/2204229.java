package org.t2framework.confeito.contexts;

import java.io.IOException;
import java.util.List;

/**
 * <#if locale="en">
 * <p>
 * {@link AmfContext} is an context object for processing AMF(Action Message
 * Format) type of request.This request usually comes from Flex/AIR.
 * 
 * </p>
 * <#else>
 * <p>
 * {@link AmfContext}はAMF(Action Message Format)タイプのリクエストを処理するコンテキストオブジェクトです.
 * このコンテキストオブジェクトが受け付けるリクエストはFlex/AIRからを原則想定しています.
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public interface AmfContext {

    /**
	 * <#if locale="en">
	 * <p>
	 * Serialize response as an AMF response.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * このレスポンスをAMFレスポンスにシリアライズします.
	 * </p>
	 * </#if>
	 * 
	 * @param webContext
	 * @throws IOException
	 */
    void serialize(WebContext webContext) throws IOException;

    /**
	 * <#if locale="en">
	 * <p>
	 * Deserialize an AMF request to an object.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * AMFリクエストをオブジェクトに展開します.
	 * </p>
	 * </#if>
	 * 
	 * @param webContext
	 * @throws IOException
	 */
    void deserialize(WebContext webContext) throws IOException;

    /**
	 * <#if locale="en">
	 * <p>
	 * True if there is one or more message body.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * リクエストのメッセージボディが1つ以上ある場合にtrueを返します.
	 * </p>
	 * </#if>
	 * 
	 * @return true if message body exists, otherwise false
	 */
    boolean hasMoreMessageBody();

    /**
	 * <#if locale="en">
	 * <p>
	 * Set to next message body.
	 * </p>
	 * <#else>
	 * <p>
	 * メッセージボディの処理対象を次に移します.AMFリクエストでは、
	 * 複数のリクエストをまとめて送信できるのでその場合このメソッドで複数の対象を処理します.
	 * </p>
	 * </#if>
	 */
    void nextMessageBody();

    /**
	 * <#if locale="en">
	 * <p>
	 * True if this message is client ping.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * AMFリクエストはpingメッセージを送信後、実ボディをPOSTします.このメソッドでは最初のpingリクエストかどうかを判定します.
	 * </p>
	 * </#if>
	 * 
	 * @return true if this message is client ping, otherwise false
	 */
    boolean isClientPingMessage();

    /**
	 * <#if locale="en">
	 * <p>
	 * True if this AMF request is remoting message.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * AMFリクエストはpingメッセージを送信後、実ボディをPOSTします.このメソッドでは実データをPOSTしたリクエストかどうかを判定します.
	 * </p>
	 * </#if>
	 * 
	 * @return true if this message is remoting message, otherwise false
	 */
    boolean isRemotingMessage();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get destination string which is defined by client.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * Flex/AIRなどのAMFリクエストを投げるクライアントが指定したdestination文字列を取得します.
	 * </p>
	 * </#if>
	 * 
	 * @return destination
	 */
    String getDestination();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get operation string which is defined by client.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * Flex/AIRなどのAMFリクエストを投げるクライアントが指定したoperation文字列を取得します.
	 * </p>
	 * </#if>
	 * 
	 * @return operation
	 */
    String getOperation();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get parameters for this AMF request.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * AMFリクエストのリクエストパラメータを取得します.
	 * </p>
	 * </#if>
	 * 
	 * @return parameters
	 */
    List<Object> getParameters();

    /**
	 * <#if locale="en">
	 * <p>
	 * Set invoking action method result for serializing to an AMF response.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param result
	 */
    void setRemotingMessageResult(Object result);

    /**
	 * <#if locale="en">
	 * <p>
	 * Create and set AMF ping result.
	 * </p>
	 * <#else>
	 * <p>
	 * AMFのpingリクエストの結果を生成して返します.
	 * </p>
	 * </#if>
	 */
    void createAndSetClientPingResult();

    /**
	 * <#if locale="en">
	 * <p>
	 * Set error to return as AMF response.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * AMFリクエストを処理する最中に発生した例外を設定して返します.
	 * </p>
	 * </#if>
	 * 
	 * @param t
	 */
    void setError(Throwable t);

    /**
	 * <#if locale="en">
	 * <p>
	 * Convert AMF object to Java object
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * AMFのオブジェクトからJavaオブジェクトに変換します.
	 * </p>
	 * </#if>
	 * 
	 * @param valueType
	 *            Java type
	 * @param value
	 * @return converted value
	 */
    Object convertReadValue(Class<?> valueType, Object value);

    /**
	 * <#if locale="en">
	 * <p>
	 * Convert Java object to AMF object
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * JavaオブジェクトからAMFのオブジェクトに変換します.
	 * </p>
	 * </#if>
	 * 
	 * @param valueType
	 *            Java type
	 * @param value
	 * @return converted value
	 */
    Object convertWriteValue(Class<?> valueType, Object value);
}
