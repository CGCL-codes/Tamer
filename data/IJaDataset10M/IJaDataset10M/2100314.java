package org.shestkoff.nimium.service.data;

import org.shestkoff.nimium.common.security.Security;
import org.shestkoff.nimium.service.ErrorListener;
import org.shestkoff.nimium.common.struct.Bar;
import org.shestkoff.nimium.common.struct.Candle;
import org.shestkov.timeserie.TimeSerie;

/**
 * Project: Maxifier
 * User: Vasily
 * Date: 28.11.2009 12:50:58
 * <p/>
 * Copyright (c) 1999-2006 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author Shestkov Vasily
 */
public interface CandleTimeSerieListener extends ErrorListener {

    void candleTimeSerie(Security security, long timeFrame, TimeSerie<Bar<Candle>> candleTimeSerie);
}
