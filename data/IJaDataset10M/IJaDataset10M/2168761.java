package orxatas.travelme.activity;

import android.app.Activity;
import orxatas.travelme.sync.AsyncNoticeCode;

/**
 * Activididad con posibilidad de manejar peticiones asincronas.
 * */
public interface AsyncActivity {

    /**
	 * Cuando haya una llamada asíncrona se ejecutará.
	 * 
	 * @param code Código de aviso
	 * */
    public void asyncNotice(AsyncNoticeCode code);

    /**
	 * Devuelve la actividad actual.
	 * 
	 * @return Devuelve la actividad actual.
	 * */
    public Activity getActivity();

    /**
	 * Tell to activity that an asynchronous call is in course.
	 * */
    public void syncStarted();

    /**
	 * Tell to activity that an asynchronous call was ended.
	 * */
    public void syncEnded();

    /**
	 * It will be executed when a sync and not parallelized call to Internet is executed.
	 * */
    public void syncAndWaitCallStarted();

    /**
	 * It will be executed when a sync and not parallelized call to Internet is ended.
	 * */
    public void syncAndWaitCallEnded(Object o, int syncAction);
}
