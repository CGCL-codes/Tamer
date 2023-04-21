package dk.aau.create.processing;

import processing.core.*;
import processing.video.*;

public class ManipulatingVideoImage {

    Capture video;

    int vidWidth = 320, vidHeight = 240;

    void setup() {
        size(vidWidth, vidHeight);
        video = new Capture(this, vidWidth, vidHeight, 30);
    }

    void draw() {
        tint(mouseX, mouseY, 255);
        image(video, 0, 0);
        fill(0);
        stroke(0);
        ellipse(mouseX, mouseY, 5, 5);
    }

    void captureEvent(Capture video) {
        video.read();
    }
}
