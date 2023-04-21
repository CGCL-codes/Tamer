package org.nuplay;

import javax.swing.JProgressBar;
import pitaru.sonia_v2_9.LiveInput;
import pitaru.sonia_v2_9.Sonia;
import com.softsynth.jsyn.AudioDevice;

public class FFT implements Runnable {

    private Thread fft_loop;

    private int fps = 10;

    private boolean is_running = false;

    private int bins = 8;

    private FFTCanvas fft_canvas;

    private NuPlayConnector connector;

    public FFT(int in_bins, FFTCanvas in_canvas) {
        this.bins = in_bins;
        this.fft_canvas = in_canvas;
    }

    public void start(NuPlayConnector in_connector) {
        connector = in_connector;
        System.out.println("start");
        if (fft_loop != null) {
            stop();
        }
        Sonia.start(null, 44100);
        System.out.println(AudioDevice.getNumDevices());
        for (int i = 0; i < AudioDevice.getNumDevices(); i++) {
            System.out.println(AudioDevice.getName(i));
        }
        LiveInput.start(bins);
        fft_loop = new Thread(this);
        is_running = true;
        fft_loop.start();
    }

    public void stop() {
        System.out.println("stop");
        connector = null;
        if (fft_loop != null) {
            is_running = false;
            LiveInput.stop();
            Sonia.stop();
            fft_loop = null;
        }
    }

    public float[] getSpectrum() {
        return LiveInput.getSpectrum(true, 2);
    }

    public void run() {
        while (is_running) {
            try {
                Thread.sleep(1000 / fps);
                float[] spectrum = getSpectrum();
                System.out.println(spectrum.length);
                fft_canvas.setValues(spectrum);
                connector.sendFFT(spectrum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
