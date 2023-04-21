package com.foobnix.engine;

import android.content.Context;
import android.content.Intent;
import com.foobnix.model.FModel;
import com.foobnix.service.DMService;
import com.foobnix.service.DMService.DM_ACTION;
import com.foobnix.service.FoobnixService;
import com.foobnix.service.FoobnixService.SERVICE_ACTION;

public class FServiceHelper {

    private static FServiceHelper instance = new FServiceHelper();

    public static FServiceHelper getInstance() {
        return instance;
    }

    public void addDownload(Context context, FModel item) {
        Intent service = new Intent(context, DMService.class);
        service.setAction(DM_ACTION.ADD.toString());
        service.putExtra(DM_ACTION.ADD.toString(), item);
        context.startService(service);
    }

    public void waitTimerThraed(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.WAIT.toString());
        context.startService(service);
    }

    public void play(Context context, FModel FModel) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PLAY.toString());
        service.putExtra(SERVICE_ACTION.KEY.toString(), FModel);
        context.startService(service);
    }

    public void playAtPos(Context context, int pos) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PLAY_POSITION.toString());
        service.putExtra(SERVICE_ACTION.KEY.toString(), pos);
        context.startService(service);
    }

    public void pause(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PAUSE.toString());
        context.startService(service);
    }

    public void playPause(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PLAY_PAUSE.toString());
        context.startService(service);
    }

    public void play(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PLAY_STATE.toString());
        context.startService(service);
    }

    public void playFirst(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PLAY_FIRST.toString());
        context.startService(service);
    }

    public void seekTo(Context context, int value) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.SEEK.toString());
        service.putExtra(SERVICE_ACTION.SEEK.toString(), value);
        context.startService(service);
    }

    public void activateShortTimer(Context context, boolean flag) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.IS_ACTIVATE_SHORT_TIMER.name());
        service.putExtra(SERVICE_ACTION.IS_ACTIVATE_SHORT_TIMER.toString(), flag);
        context.startService(service);
    }

    public void start(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.START.toString());
        context.startService(service);
    }

    public void next(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.NEXT.toString());
        context.startService(service);
    }

    public void random(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.RANDOM.toString());
        context.startService(service);
    }

    public void prev(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.PREV.toString());
        context.startService(service);
    }

    public void stop(Context context) {
        Intent service = new Intent(context, FoobnixService.class);
        service.setAction(SERVICE_ACTION.STOP.toString());
        context.startService(service);
    }
}
