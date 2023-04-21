package com.android.calendar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.io.InputStream;

/**
 * Helper class for async access of images.
 */
public class ContactsAsyncHelper extends Handler {

    private static final boolean DBG = false;

    private static final String LOG_TAG = "ContactsAsyncHelper";

    /**
     * Interface for a WorkerHandler result return.
     */
    public interface OnImageLoadCompleteListener {

        /**
         * Called when the image load is complete.
         *
         * @param imagePresent true if an image was found
         */
        public void onImageLoadComplete(int token, Object cookie, ImageView iView, boolean imagePresent);
    }

    private static final int EVENT_LOAD_IMAGE = 1;

    private static final int DEFAULT_TOKEN = -1;

    private static Handler sThreadHandler;

    private static ContactsAsyncHelper sInstance;

    static {
        sInstance = new ContactsAsyncHelper();
    }

    private static final class WorkerArgs {

        public Context context;

        public ImageView view;

        public Uri uri;

        public int defaultResource;

        public Object result;
    }

    /**
     * Thread worker class that handles the task of opening the stream and loading
     * the images.
     */
    private class WorkerHandler extends Handler {

        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            WorkerArgs args = (WorkerArgs) msg.obj;
            switch(msg.arg1) {
                case EVENT_LOAD_IMAGE:
                    InputStream inputStream = null;
                    try {
                        inputStream = Contacts.openContactPhotoInputStream(args.context.getContentResolver(), args.uri);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error opening photo input stream", e);
                    }
                    if (inputStream != null) {
                        args.result = Drawable.createFromStream(inputStream, args.uri.toString());
                        if (DBG) Log.d(LOG_TAG, "Loading image: " + msg.arg1 + " token: " + msg.what + " image URI: " + args.uri);
                    } else {
                        args.result = null;
                        if (DBG) Log.d(LOG_TAG, "Problem with image: " + msg.arg1 + " token: " + msg.what + " image URI: " + args.uri + ", using default image.");
                    }
                    break;
                default:
            }
            Message reply = ContactsAsyncHelper.this.obtainMessage(msg.what);
            reply.arg1 = msg.arg1;
            reply.obj = msg.obj;
            reply.sendToTarget();
        }
    }

    /**
     * Private constructor for static class
     */
    private ContactsAsyncHelper() {
        HandlerThread thread = new HandlerThread("ContactsAsyncWorker");
        thread.start();
        sThreadHandler = new WorkerHandler(thread.getLooper());
    }

    /**
     * Start an image load, attach the result to the specified CallerInfo object.
     * Note, when the query is started, we make the ImageView INVISIBLE if the
     * placeholderImageResource value is -1.  When we're given a valid (!= -1)
     * placeholderImageResource value, we make sure the image is visible.
     */
    public static final void updateImageViewWithContactPhotoAsync(Context context, ImageView imageView, Uri person, int placeholderImageResource) {
        if (person == null) {
            if (DBG) Log.d(LOG_TAG, "target image is null, just display placeholder.");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(placeholderImageResource);
            return;
        }
        WorkerArgs args = new WorkerArgs();
        args.context = context;
        args.view = imageView;
        args.uri = person;
        args.defaultResource = placeholderImageResource;
        Message msg = sThreadHandler.obtainMessage(DEFAULT_TOKEN);
        msg.arg1 = EVENT_LOAD_IMAGE;
        msg.obj = args;
        if (DBG) Log.d(LOG_TAG, "Begin loading image: " + args.uri + ", displaying default image for now.");
        if (placeholderImageResource != -1) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(placeholderImageResource);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
        sThreadHandler.sendMessage(msg);
    }

    /**
     * Called when loading is done.
     */
    @Override
    public void handleMessage(Message msg) {
        WorkerArgs args = (WorkerArgs) msg.obj;
        switch(msg.arg1) {
            case EVENT_LOAD_IMAGE:
                boolean imagePresent = false;
                if (args.result != null) {
                    args.view.setVisibility(View.VISIBLE);
                    args.view.setImageDrawable((Drawable) args.result);
                    imagePresent = true;
                } else if (args.defaultResource != -1) {
                    args.view.setVisibility(View.VISIBLE);
                    args.view.setImageResource(args.defaultResource);
                }
                break;
            default:
        }
    }
}
