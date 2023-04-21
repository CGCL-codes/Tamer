package uk.co.simphoney.audio.dft;

import uk.co.simphoney.audio.gui.CyclicSpectrogramDataListener;
import uk.co.simphoney.audio.gui.CyclicSpectrogrumImage;

public interface CyclicSpectrumDataBuilder {

    int getBinCount();

    int getSizeInChunks();

    float[][] getMagnitude();

    void addSizeObserver(CyclicSpectrogramDataListener image);
}
