package org.ine.telluride.jaer.tell2010;

import ch.unizh.ini.jaer.chip.cochlea.BinauralCochleaEvent;
import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.EventPacket;
import java.util.*;
import com.sun.opengl.util.GLUT;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import net.sf.jaer.Description;
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.graphics.FrameAnnotater;
import net.sf.jaer.graphics.ImageDisplay;

/**
 * Shows live cochlea spectrogram in a separate ImageDisplay window.
 * @author Andrew
 */
@Description("Generate a spectrogram from incoming spikes")
public final class SpectrogramFilter extends EventFilter2D implements Observer, FrameAnnotater {

    private int numChannels = getPrefs().getInt("SpectrogramFilter.numChannels", 64);

    private int binWidth = getPrefs().getInt("SpecdtrogramFilter.binWidth", 16000);

    private int numTimeBins = getPrefs().getInt("SpecdtrogramFilter.numTimeBins", 50);

    private int redundancy = getPrefs().getInt("SpecdtrogramFilter.redundancy", 1);

    private int spikeBufferLength = getPrefs().getInt("SpecotragramFilter.spikeBufferLength", 10000);

    private int colorScale = getPrefs().getInt("SpecdtrogramFilter.colorScale", 50);

    private float[][] spectrogram;

    private int currentTime;

    private int[] spikeBufferTs;

    private int[] spikeBufferChans;

    private boolean[] spikeBufferValid;

    private int spikeBufferIndex;

    private ImageDisplay imageDisplay = null;

    private JFrame imageFrame = null;

    public SpectrogramFilter(AEChip chip) {
        super(chip);
        spectrogram = new float[numChannels][numTimeBins];
        spikeBufferIndex = 0;
        spikeBufferTs = new int[spikeBufferLength];
        spikeBufferChans = new int[spikeBufferLength];
        resetFilter();
    }

    @Override
    public synchronized void setFilterEnabled(boolean yes) {
        super.setFilterEnabled(yes);
        if (yes) {
            checkImageDisplay();
            imageFrame.setVisible(true);
        } else {
            if (imageFrame != null) {
                imageFrame.setVisible(false);
            }
        }
    }

    public void initFilter() {
    }

    @Override
    public synchronized void resetFilter() {
        clearSpectrogram();
        for (int spike = 0; spike < spikeBufferLength; spike++) {
            spikeBufferTs[spike] = -1;
            spikeBufferChans[spike] = -1;
        }
    }

    public void update(Observable o, Object arg) {
    }

    @Override
    public synchronized EventPacket<?> filterPacket(EventPacket<?> in) {
        if (!isFilterEnabled()) {
            return in;
        }
        if (in == null) {
            return in;
        }
        for (Object e : in) {
            BinauralCochleaEvent i = (BinauralCochleaEvent) e;
            spikeBufferChans[spikeBufferIndex] = i.x;
            spikeBufferTs[spikeBufferIndex] = i.timestamp;
            currentTime = i.timestamp;
            if (++spikeBufferIndex >= spikeBufferLength) {
                spikeBufferIndex = 0;
            }
        }
        binSpikes();
        return in;
    }

    private void binSpikes() {
        clearSpectrogram();
        int tbin, chan, spike, spikeT, t, tOff;
        for (spike = 0; spike < spikeBufferLength; spike++) {
            chan = spikeBufferChans[spike];
            spikeT = spikeBufferTs[spike];
            t = currentTime - spikeT;
            tOff = t / binWidth;
            tbin = numTimeBins - 1 - tOff;
            if (tbin > 0 && chan != -1) {
                if (chan < 0 || chan >= numChannels || tbin < 0 || tbin >= numTimeBins) {
                    spectrogram[chan][tbin]++;
                } else {
                    spectrogram[chan][tbin]++;
                }
            }
        }
    }

    private void checkImageDisplay() {
        if (imageFrame == null) {
            imageFrame = new JFrame("Spectrogram");
            imageFrame.setPreferredSize(new Dimension(200, 200));
            imageDisplay = ImageDisplay.createOpenGLCanvas();
            imageFrame.getContentPane().add(imageDisplay, BorderLayout.CENTER);
            imageFrame.pack();
            imageDisplay.setxLabel("time");
            imageDisplay.setyLabel("channel");
        }
        if (numChannels != imageDisplay.getSizeY() || numTimeBins != imageDisplay.getSizeX()) {
            imageDisplay.setSize(numTimeBins, numChannels);
        }
    }

    private void clearSpectrogram() {
        for (int chan = 0; chan < numChannels; chan++) {
            for (int time = 0; time < numTimeBins; time++) {
                spectrogram[chan][time] = 0;
            }
        }
    }

    private GLUT glut = new GLUT();

    private JFrame specFrame = null;

    private GLCanvas glCanvas = null;

    public void annotate(GLAutoDrawable drawable) {
        checkImageDisplay();
        imageDisplay.checkPixmapAllocation();
        float c;
        for (int chan = 0; chan < numChannels; chan++) {
            for (int time = 0; time < numTimeBins; time++) {
                c = (float) spectrogram[chan][time] / colorScale;
                imageDisplay.setPixmapGray(time, chan, c);
            }
        }
        imageDisplay.display();
    }

    /**
     * @return the binWidth
     */
    public int getBinWidth() {
        return binWidth;
    }

    /**
     * @param binWidth the binWidth to set
     */
    public void setBinWidth(int binWidth) {
        this.binWidth = binWidth;
        prefs().putInt("SpectrogramFilter.binWidth", binWidth);
    }

    /**
     * @return the numTimeBins
     */
    public int getNumTimeBins() {
        return numTimeBins;
    }

    /**
     * @param numTimeBins the numTimeBins to set
     */
    public synchronized void setNumTimeBins(int numTimeBins) {
        this.numTimeBins = numTimeBins;
        prefs().putInt("SpectrogramFilter.numTimeBins", numTimeBins);
        spectrogram = new float[numChannels][numTimeBins];
    }

    /**
     * @return the redundancy
     */
    public int getRedundancy() {
        return redundancy;
    }

    /**
     * @param redundancy the redundancy to set
     */
    public synchronized void setRedundancy(int redundancy) {
        this.redundancy = redundancy;
        prefs().putInt("SpectrogramFilter.binWidth", redundancy);
    }

    /**
     * @return the spikeBufferLength
     */
    public int getSpikeBufferLength() {
        return spikeBufferLength;
    }

    /**
     * @param spikeBufferLength the spikeBufferLength to set
     */
    public synchronized void setSpikeBufferLength(int spikeBufferLength) {
        this.spikeBufferLength = spikeBufferLength;
        prefs().putInt("SpectrogramFilter.spikeBufferLength", spikeBufferLength);
        spikeBufferTs = new int[spikeBufferLength];
        spikeBufferChans = new int[spikeBufferLength];
    }

    /**
     * @return the colorScale
     */
    public int getColorScale() {
        return colorScale;
    }

    /**
     * @param colorScale the colorScale to set
     */
    public void setColorScale(int colorScale) {
        this.colorScale = colorScale;
        prefs().putInt("SpectrogramFilter.colorScale", colorScale);
    }
}
