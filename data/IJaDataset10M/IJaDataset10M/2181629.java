package com.breaktrycatch.needmorehumans.control.webcam.callback;

import java.util.ArrayList;
import processing.core.PImage;

public interface ICaptureCallback {

    public void execute(ArrayList<PImage> img);
}
