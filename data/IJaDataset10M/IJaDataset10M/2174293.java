package org.myrobotlab.control;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.myrobotlab.service.interfaces.GUI;

public class AudioCaptureGUI extends ServiceGUI {

    static final long serialVersionUID = 1L;

    final JButton captureBtn = new JButton("Capture");

    final JButton stopBtn = new JButton("Stop");

    final JButton playBtn = new JButton("Playback");

    public AudioCaptureGUI(final String boundServiceName, final GUI myService) {
        super(boundServiceName, myService);
    }

    public void init() {
        captureBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                captureBtn.setEnabled(false);
                stopBtn.setEnabled(true);
                playBtn.setEnabled(false);
                myService.send(boundServiceName, "captureAudio");
            }
        });
        display.add(captureBtn);
        stopBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                captureBtn.setEnabled(true);
                stopBtn.setEnabled(false);
                playBtn.setEnabled(true);
                myService.send(boundServiceName, "stopAudioCapture");
            }
        });
        display.add(stopBtn);
        playBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                myService.send(boundServiceName, "playAudio");
            }
        });
        display.add(playBtn);
        display.setLayout(new FlowLayout());
    }

    @Override
    public void attachGUI() {
    }

    @Override
    public void detachGUI() {
    }
}
