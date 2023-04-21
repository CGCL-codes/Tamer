package it.infocostitre.iface;

import android.appwidget.AppWidgetManager;
import android.content.Context;

/**
 * @author <a href="mailto:pasquale.paola@gmail.com">Pasquale Paola</a>
 *
 */
public interface DbLisner {

    void updateInfoCostiWidget(Context context, AppWidgetManager appWidgetManager);
}
