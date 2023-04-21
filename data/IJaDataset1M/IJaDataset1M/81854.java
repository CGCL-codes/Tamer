package com.example.ane.speechtext;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;

/**
 * SpeechTextFREExtension
 * @author @tokufxug http://twitter.com/tokufxug
 */
public class SpeechTextFREExtension implements FREExtension {

    /**
	 * FREContext
	 */
    private FREContext _context = null;

    /**
	 * FREFunction
	 */
    private Map<String, FREFunction> _funcs = null;

    @Override
    public FREContext createContext(String arg0) {
        return getContext();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void initialize() {
    }

    private FREContext getContext() {
        if (_context == null) {
            _context = new FREContext() {

                @Override
                public Map<String, FREFunction> getFunctions() {
                    if (_funcs == null) {
                        _funcs = getFuncs();
                    }
                    return _funcs;
                }

                @Override
                public void dispose() {
                }
            };
        }
        return _context;
    }

    private Map<String, FREFunction> getFuncs() {
        Map<String, FREFunction> funcs = new HashMap<String, FREFunction>();
        funcs.put("speech", createSpeechFunction());
        funcs.put("speechData", callSpeechData());
        funcs.put("end", callEnd());
        return funcs;
    }

    private FREFunction createSpeechFunction() {
        FREFunction func = new FREFunction() {

            @Override
            public FREObject call(FREContext arg0, FREObject[] arg1) {
                try {
                    SpeechTextActivity.context = arg0;
                    Activity a = arg0.getActivity();
                    Intent speechIntent = new Intent(a, SpeechTextActivity.class);
                    a.startActivity(speechIntent);
                    return FREObject.newObject("");
                } catch (Exception e) {
                    try {
                        return FREObject.newObject(e.toString());
                    } catch (FREWrongThreadException ex) {
                    }
                }
                return null;
            }
        };
        return func;
    }

    private FREFunction callSpeechData() {
        FREFunction func = new FREFunction() {

            @Override
            public FREObject call(FREContext arg0, FREObject[] arg1) {
                try {
                    return FREObject.newObject(SpeechTextActivity.message);
                } catch (Exception e) {
                    try {
                        return FREObject.newObject(e.toString());
                    } catch (FREWrongThreadException ex) {
                    }
                }
                return null;
            }
        };
        return func;
    }

    private FREFunction callEnd() {
        return new FREFunction() {

            @Override
            public FREObject call(FREContext arg0, FREObject[] arg1) {
                try {
                    NotificationManager nf = (NotificationManager) arg0.getActivity().getSystemService(Activity.NOTIFICATION_SERVICE);
                    Notification n = new Notification(arg0.getResourceId("drawable.icon"), "����ɏI�����܂���", System.currentTimeMillis());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    PendingIntent pi = PendingIntent.getActivity(arg0.getActivity(), 0, intent, 0);
                    n.setLatestEventInfo(arg0.getActivity(), "SpeechText", "�Ō�̂Ԃ₫�y" + arg1[0].getAsString() + "�z�ł���", pi);
                    nf.notify(1, n);
                } catch (Exception e) {
                }
                return null;
            }
        };
    }
}
