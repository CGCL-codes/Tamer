package ru.ksu.niimm.cll.mocassin.frontend.common.client;

public interface IAsyncCallbackWrapper<T> {

    /**
	 * handle failed RPC call
	 * 
	 * @param caught
	 */
    void handleFailure(Throwable caught);

    /**
	 * handle successful RPC call
	 * 
	 * @param result
	 */
    void handleSuccess(T result);

    /**
	 * Important!!! Should always call this method before RPC call
	 */
    void beforeCall();
}
