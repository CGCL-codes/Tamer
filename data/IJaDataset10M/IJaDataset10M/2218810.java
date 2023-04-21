package com.google.gwt.core.client.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.impl.AsyncFragmentLoader.HttpDownloadFailure;

/**
 * A download strategy that uses a JSONP style script tag mechanism. and is
 * therefore cross site compatible. Note that if this strategy is used, the
 * deferred fragments must be wrapped in a callback called runAsyncCallbackX()
 * where X is the fragment number.
 * 
 * This is the default strategy for the CrossSiteIframeLinker.
 * 
 * TODO(unnurg): Try to use the ScriptInjector here
 */
public class ScriptTagLoadingStrategy extends LoadingStrategyBase {

    /**
   * Uses a JSONP style script tag mechanism to download the code.
   */
    protected static class ScriptTagDownloadStrategy implements DownloadStrategy {

        @Override
        public void tryDownload(final RequestData request) {
            int fragment = request.getFragment();
            JavaScriptObject scriptTag = createScriptTag(request.getUrl());
            setOnSuccess(fragment, onSuccess(fragment, scriptTag, request));
            setOnFailure(scriptTag, onFailure(fragment, scriptTag, request));
            installScriptTag(scriptTag);
        }
    }

    protected static void callOnLoadError(RequestData request) {
        request.onLoadError(new HttpDownloadFailure(request.getUrl(), 404, "Script Tag Failure - no status available"), true);
    }

    private static native boolean clearCallbacksAndRemoveTag(int fragment, JavaScriptObject scriptTag);

    private static native void clearOnFailure(JavaScriptObject scriptTag);

    private static native void clearOnSuccess(int fragment);

    private static native JavaScriptObject createScriptTag(String url);

    private static native void installScriptTag(JavaScriptObject scriptTag);

    private static native JavaScriptObject onFailure(int fragment, JavaScriptObject scriptTag, RequestData request);

    private static native JavaScriptObject onSuccess(int fragment, JavaScriptObject scriptTag, RequestData request);

    private static native void setOnFailure(JavaScriptObject script, JavaScriptObject callback);

    private static native void setOnSuccess(int fragment, JavaScriptObject callback);

    public ScriptTagLoadingStrategy() {
        super(new ScriptTagDownloadStrategy());
    }
}
