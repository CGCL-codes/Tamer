package mu.nu.nullpo.game.net;

import java.io.IOException;

/**
 * 受信したメッセージに応じて何か処理をするクラス用のインターフェース
 */
public interface NetMessageListener {

    /**
	 * メッセージ受信時に呼び出される
	 * @param client クライアント(NetBaseClientとその派生クラス)
	 * @param message 受信したメッセージ(タブ区切り済み)
	 * @throws IOException 何かエラーがあったとき
	 */
    public void netOnMessage(NetBaseClient client, String[] message) throws IOException;

    /**
	 * 切断時に呼び出される
	 * @param client クライアント(NetBaseClientとその派生クラス)
	 * @param ex 切断原因となった例外(不明な場合と正常終了の場合はnull)
	 */
    public void netOnDisconnect(NetBaseClient client, Throwable ex);
}
