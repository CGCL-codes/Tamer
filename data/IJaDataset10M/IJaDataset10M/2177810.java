package org.spnt.recognition.dtw.ui;

import java.awt.BorderLayout;

public class RecognitionUIActionListenerImpl implements RecognitionUIActionListener {

    RecognitionAppFrame recognitionAppFrame;

    public RecognitionUIActionListenerImpl(RecognitionAppFrame recognitionAppFrame) {
        super();
        this.recognitionAppFrame = recognitionAppFrame;
    }

    public void start() {
        recognitionAppFrame.getMainContentPane().add(recognitionAppFrame.getRecognitionPlot(), BorderLayout.CENTER);
        recognitionAppFrame.getRecognitionPlot().startRecognition();
    }

    public void stop() {
        recognitionAppFrame.getRecognitionPlot().stopRecognition();
        recognitionAppFrame.getMainContentPane().remove(recognitionAppFrame.getRecognitionPlot());
        recognitionAppFrame.setRecognitionPlot(null);
    }

    public void changeLearningStatus(boolean status) {
        recognitionAppFrame.setLearnMode(status);
    }
}
