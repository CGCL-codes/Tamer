package com.fh.auge.core.internal.ext;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.Duration;
import com.fh.auge.core.Diagnostic;
import com.fh.auge.core.IntervalGain;
import com.fh.auge.core.PreferenceService;
import com.fh.auge.core.Quote;
import com.fh.auge.core.Stock;
import com.fh.auge.core.TimeUtils;
import com.fh.auge.core.Diagnostic.Severity;
import com.fh.auge.event.LifecycleApplicationEvent;
import com.fh.auge.event.LifecycleApplicationEvent.EventType;

@Component
public class StockPerformanceService implements ApplicationContextAware, ApplicationListener {

    private Duration duration = Duration.days(10);

    private Map<Stock, StockPerformance> items = new HashMap<Stock, StockPerformance>();

    private QuoteService quoteService;

    private PreferenceService preferenceService;

    private ApplicationContext ctx;

    public StockPerformance getStockPerformance(Stock stock) {
        StockPerformance performance = items.get(stock);
        if (performance == null) {
            performance = new StockPerformance(stock);
            items.put(stock, performance);
            update(performance);
        }
        return performance;
    }

    private void update(final StockPerformance performance) {
        CalendarDate now = TimeUtils.getToday();
        Quote quote = quoteService.getQuote(performance.getStock(), now);
        Diagnostic diagnostic = new Diagnostic();
        Map<Duration, IntervalGain> gains = new HashMap<Duration, IntervalGain>();
        if (quote == null) diagnostic = new Diagnostic(Severity.WARNING, "Quotes not loaded."); else {
            CalendarDate d = duration.subtractedFrom(TimeUtils.getToday());
            if (quote.getCalendarDate().isBefore(d)) {
                diagnostic = new Diagnostic(Severity.WARNING, "Quotes are not up to date.");
            }
            for (Duration duration : preferenceService.getGainDurations()) {
                CalendarDate start = duration.subtractedFrom(now);
                Quote startQuote = quoteService.getQuote(performance.getStock(), start);
                if (startQuote != null) {
                    gains.put(duration, IntervalGain.create(startQuote.getClose(), quote.getClose()));
                }
            }
        }
        performance.setValue(quote != null ? quote.getClose() : null);
        performance.setGains(gains);
        performance.setDiagnostic(diagnostic);
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof QuoteEvent) {
            QuoteEvent qe = (QuoteEvent) event;
            StockPerformance performance = items.get(qe.getStock());
            if (performance != null) {
                update(performance);
                ctx.publishEvent(new LifecycleApplicationEvent(EventType.MODIFIED, performance));
            }
        }
    }

    @Autowired
    public void setQuoteService(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Autowired
    public void setPreferenceService(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }
}
