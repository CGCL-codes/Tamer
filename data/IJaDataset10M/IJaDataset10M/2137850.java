package org.az.muz;

import processing.core.PApplet;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

public class Analyzer extends PApplet {

    FFT fft;

    Minim minim;

    AudioPlayer jingle;

    public void setup() {
        size(400, 400);
        noStroke();
        frameRate(25);
        minim = new Minim(this);
        jingle = minim.loadFile("D:\\media\\Muzic\\track3.mp3", 2048);
        jingle.loop();
        fft = new FFT(jingle.bufferSize(), jingle.sampleRate());
        fft.logAverages(22, 3);
        rectMode(CORNERS);
        colorMode(HSB, 100);
    }

    public void draw() {
        background(0);
        fft.forward(jingle.mix);
        int w = width / fft.avgSize();
        for (int i = 0; i < fft.avgSize(); i++) {
            float amp = sqrt(sqrt(fft.getAvg(i))) * 150;
            float h = i * 100 / fft.avgSize();
            h -= 10;
            h = 100 - h;
            float s = 70;
            float b = amp / 3 * 100;
            float a = 100;
            fill(color(h, s, b, a));
            rect(i * w, height - 5, i * w + w, height - 5 - fft.getAvg(i));
        }
    }

    public void stop() {
        jingle.close();
        minim.stop();
        super.stop();
    }
}
