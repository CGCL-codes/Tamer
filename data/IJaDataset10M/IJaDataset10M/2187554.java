package net.sf.revolver.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.revolver.core.ErrorMessage;
import net.sf.revolver.core.ErrorMessages;

/**
 * ErrorMessage変換ユーテリティクラス.
 *
 * @author tagui
 *
 */
public class Cast4ErrorMessages {

    /**
     * ユーティリティクラスのためインスタンス作成させない.
     */
    private Cast4ErrorMessages() {
    }

    /**
     * Map<String,List<String>>からErrorMessagesに変換.
     *
     * @param map エラーメッセージマップ
     * @return ErrorMessages
     */
    public static ErrorMessages map2ErrorMessages(Map<String, List<String>> map) {
        ErrorMessages errorMessages = new ErrorMessages();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            List<ErrorMessage> messageList = new ArrayList<ErrorMessage>();
            String key = entry.getKey();
            List<String> errorMessageStrings = entry.getValue();
            for (String element : errorMessageStrings) {
                ErrorMessage message = new ErrorMessage(key, element);
                messageList.add(message);
            }
            errorMessages.add(key, messageList);
        }
        return errorMessages;
    }
}
