package com.habitsoft.kiyaa.util;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Waits for all of a group of async callbacks to complete and then
 * calls a provided callback with the success/failure status.
 *
 * This is very useful when performing a bunch of independent async 
 * operations that you don't need the specific value from, just the
 * final success/failure result. 
 * 
 * eg:
 <code>

     		AsyncCallbackGroup group = new AsyncCallbackGroup();
    		
			getService().getSiteTitle(getSiteName(), new AsyncCallbackGroupMember<String>(group) {
				@Override
				public void onSuccess(String param) {
					setTitle(param);
					super.onSuccess(param);
				}
			});

    		...
    		
		    group.ready(new AsyncCallbackProxy(callback) {
			@Override
			public void onSuccess(Object result) {
				...
			}
 </code>
  *
  * If you want to keep multiple results around you can also do:
 <code>
        AsyncCallbackGroup g = new AsyncCallbackGroup();

        final AsyncCallbackGroupResultCachingMember<X> x = new AsyncCallbackGroupResultCachingMember<X>(g); 
        final AsyncCallbackGroupResultCachingMember<Y> y = new AsyncCallbackGroupResultCachingMember<Y>(g); 

        controller.getService().getX(controller.getToken(), x.getId(), x);
        controller.getService().getY(controller.getToken(), x.getId(), y);

	    group.ready(new AsyncCallbackProxy(callback) {
		@Override
		public void onSuccess(Object result) {
			x.getResult();
			y.getResult();
		}
 </code>
  */
public class AsyncCallbackGroup implements AsyncCallbackExtensions {

    int pending = 0;

    Throwable error;

    boolean ready = false;

    AsyncCallbackGroupMember<?> sharedMember;

    AsyncCallback<?> callback;

    Object callbackParam;

    Object marker;

    private boolean complete;

    public AsyncCallbackGroup() {
    }

    public AsyncCallbackGroup(Object marker) {
        this.marker = marker;
    }

    void addPending() {
        pending++;
    }

    void addFailure(Throwable caught) {
        if (marker != null && (caught instanceof Error || caught instanceof RuntimeException)) try {
            Log.error("At " + marker.toString(), caught);
        } catch (Exception e) {
        }
        error = caught;
        removePending();
    }

    void addSuccess(Object result) {
        removePending();
    }

    private void removePending() {
        pending--;
        if (ready && pending == 0) {
            done();
        } else {
            resetTimeout(null);
        }
    }

    private void done() {
        onComplete();
    }

    /** Supply a callback to be used when all of the member callbacks have completed. The callback param will be passed back to the callback in onsuccess.*/
    public <T> void ready(AsyncCallback<T> callback, T callbackParam) {
        this.callback = callback;
        this.callbackParam = callbackParam;
        ready();
    }

    /** Supply a callback to be used when all of the member callbacks have completed. */
    public void ready(AsyncCallback<Void> callback) {
        this.callback = callback;
        this.callbackParam = null;
        ready();
    }

    public void ready() {
        ready = true;
        if (pending == 0) {
            DeferredCommand.addCommand(new Command() {

                public void execute() {
                    done();
                }
            });
        } else {
            resetTimeout(null);
        }
    }

    /** 
     * If you don't care what the results are you can use this so that you wait until they arrive.
     * 
     * To get the right kind of callback, use the syntax:
     * 
     * group.<Void>member();
     * group.<MyClass>member();
     * 
     * Etc..
     */
    public <T> AsyncCallback<T> member(Object marker) {
        if (marker != null) {
            return new AsyncCallbackGroupMember<T>(this, marker);
        } else {
            return member();
        }
    }

    /** 
     * If you don't care what the results are you can use this so that you wait until they arrive.
     * 
     * To get the right kind of callback, use the syntax:
     * 
     * group.<Void>member();
     * group.<MyClass>member();
     * 
     * Etc..
     */
    @SuppressWarnings("unchecked")
    public <T> AsyncCallback<T> member() {
        if (sharedMember == null) {
            sharedMember = new AsyncCallbackGroupMember<T>(this);
            return (AsyncCallback<T>) sharedMember;
        } else {
            addPending();
            return (AsyncCallback<T>) sharedMember;
        }
    }

    @SuppressWarnings("unchecked")
    protected void onComplete() {
        try {
            if (complete) Log.error("AsyncCallbackGroup completed twice; at " + marker, new Error());
        } catch (Exception e) {
        }
        complete = true;
        if (callback != null) {
            if (error != null) {
                callback.onFailure((Throwable) error);
            } else try {
                ((AsyncCallback<Object>) callback).onSuccess(callbackParam);
            } catch (Throwable t) {
                callback.onFailure(t);
            }
            callback = null;
        }
    }

    /**
	 * Check whether any group members are still pending.  Returns true if there are.
	 */
    public boolean isPending() {
        return pending > 0;
    }

    @Override
    public void resetTimeout(Integer expectedTimeNeeded) {
        if (callback instanceof AsyncCallbackExtensions) ((AsyncCallbackExtensions) callback).resetTimeout(expectedTimeNeeded);
    }

    @Override
    public boolean isOkayToWaitForCurrentAction() {
        if (callback instanceof AsyncCallbackExtensions) return ((AsyncCallbackExtensions) callback).isOkayToWaitForCurrentAction(); else return false;
    }
}
