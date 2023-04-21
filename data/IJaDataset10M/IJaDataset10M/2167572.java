package org.shestkoff.nimium.backtester.events;

import org.shestkov.timeserie.TimeSerieItem;
import org.shestkoff.nimium.expert.AbstractExpert;

/**
 * NumiumTrade project
 * Time: 14.02.2010 11:05:56
 *
 * @author Vasily Shestkov
 */
public interface ExpertEvent extends TimeSerieItem {

    void notify(AbstractExpert expert);
}
