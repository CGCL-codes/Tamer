package com.appspot.bartimebot;

import java.util.Date;
import java.util.logging.Logger;
import com.appspot.bartimebot.temperature.Thermometer;
import com.google.wave.api.*;

@SuppressWarnings("serial")
public class BartimebotServlet extends AbstractRobotServlet {

    private static final int TEMP_INITIAL = 0;

    private static final int TEMP_BASIC_INCREMENT = 10;

    private static final int TEMP_THRESHOLD_WARN = 80;

    private static final int TEMP_THRESHOLD_ACTION = 100;

    private static final String TEXT_WARN = "WARNING: The heat is on.";

    private static final String TEXT_ACTION = "It's time to put this conversation on ice. Where do u wanna hang out?";

    private static final String GADGET_ACTION = "http://betagunit.com/joe/bartime_gadget.xml";

    private static final int INTERVAL_TEMP_REDUCE = 30;

    private static final int TEMP_REDUCE_AMOUNT = 2;

    static final Logger LOG = Logger.getLogger(BartimebotServlet.class.getName());

    @Override
    public void processEvents(RobotMessageBundle bundle) {
        Wavelet wavelet = bundle.getWavelet();
        if (bundle.wasSelfAdded()) {
            wavelet.setDataDocument("temperature", String.valueOf(TEMP_INITIAL));
            wavelet.setDataDocument("lastReductionTime", String.valueOf((new Date()).getTime()));
        }
        for (Event event : bundle.getBlipSubmittedEvents()) {
            Blip blip = event.getBlip();
            String blipText = blip.getDocument().getText();
            final int initialTemperature = Integer.parseInt(wavelet.getDataDocument("temperature"));
            int temperature = initialTemperature;
            long lastReductionTimestamp = Long.parseLong(wavelet.getDataDocument("lastReductionTime"));
            long currentTimestamp = (new Date()).getTime();
            int timeDifference = (int) ((currentTimestamp - lastReductionTimestamp) / 1000);
            while (timeDifference >= INTERVAL_TEMP_REDUCE) {
                temperature -= TEMP_REDUCE_AMOUNT;
                timeDifference -= INTERVAL_TEMP_REDUCE;
                lastReductionTimestamp += (INTERVAL_TEMP_REDUCE * 1000);
                wavelet.setDataDocument("lastReductionTime", String.valueOf(lastReductionTimestamp));
                LOG.info("Temperature reduced by " + TEMP_REDUCE_AMOUNT);
                LOG.info("Calculated time difference (seconds): " + timeDifference);
            }
            temperature = Math.max(temperature, 0);
            temperature += Thermometer.getTemperatureDifference(blipText, TEMP_BASIC_INCREMENT);
            wavelet.setDataDocument("temperature", "" + temperature);
            temperature = Math.min(temperature, initialTemperature + (TEMP_THRESHOLD_ACTION / 4));
            LOG.warning("Conversation temperature: " + temperature);
            wavelet.setDataDocument("temperature", "" + temperature);
            if (temperature >= TEMP_THRESHOLD_WARN && !wavelet.hasDataDocument("warned")) {
                Blip warnBlip = wavelet.appendBlip();
                warnBlip.getDocument().append(TEXT_WARN);
                wavelet.setDataDocument("warned", "1");
            } else if (temperature >= TEMP_THRESHOLD_ACTION && !wavelet.hasDataDocument("beertime")) {
                Blip actionTextBlip = wavelet.appendBlip();
                actionTextBlip.getDocument().append(TEXT_ACTION);
                Blip gadgetBlip = wavelet.appendBlip();
                Gadget actionGadget = new Gadget(GADGET_ACTION);
                gadgetBlip.getDocument().getGadgetView().append(actionGadget);
                wavelet.setDataDocument("beertime", "1");
            }
            if (blipText.startsWith("/temp")) {
                Blip replyBlip = blip.createChild();
                replyBlip.getDocument().append("Conversation Temperature: " + wavelet.getDataDocument("temperature"));
            }
            if (blipText.startsWith("/reset")) {
                wavelet.setDataDocument("temperature", "0");
                Blip replyBlip = blip.createChild();
                replyBlip.getDocument().append("Conversation has been iced.");
                LOG.warning("Conversation temperature has been reset.");
            }
        }
    }
}
