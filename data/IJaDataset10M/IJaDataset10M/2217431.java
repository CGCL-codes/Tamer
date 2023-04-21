package net.solosky.maplefetion;

import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.client.dialog.ChatDialogProxyFactory;
import net.solosky.maplefetion.client.dialog.DialogFactory;
import net.solosky.maplefetion.net.TransferFactory;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.FetionExecutor;
import net.solosky.maplefetion.util.FetionTimer;
import net.solosky.maplefetion.util.LocaleSetting;

/**
 *
 * 飞信运行上下文 相当于一个导航树，便于子模块可以获得飞信运行环境时的资源
 *
 * @author solosky <solosky772@qq.com>
 */
public interface FetionContext {

    /**
	 * 返回对话框工厂
	 * @return
	 */
    public DialogFactory getDialogFactory();

    /**
	 * 返回聊天对话代理工厂
	 * @return
	 */
    public ChatDialogProxyFactory getChatDialogProxyFactoy();

    /**
	 * 返回传输工厂
	 * @return
	 */
    public TransferFactory getTransferFactory();

    /**
	 * 返回单线程执行器
	 * @return
	 */
    public FetionExecutor getFetionExecutor();

    /**
	 * 返回全局的定时器
	 * @return
	 */
    public FetionTimer getFetionTimer();

    /**
	 * 返回飞信用户
	 * @return
	 */
    public User getFetionUser();

    /**
	 * 返回存储对象
	 */
    public FetionStore getFetionStore();

    /**
	 * 返回通知监听器
	 * @return the notifyListener
	 */
    public NotifyEventListener getNotifyEventListener();

    /**
	 * 设置客户端状态
	 */
    public void updateState(ClientState state);

    /**
	 * 返回客户端状态
	 */
    public ClientState getState();

    /**
	 * 返回区域化配置
	 */
    public LocaleSetting getLocaleSetting();

    /**
	 * 处理不可恢复的异常的回调方法
	 * 通常这个方法是为Client处理不可恢复的异常准备的
	 * @param exception
	 */
    public void handleException(FetionException exception);
}
