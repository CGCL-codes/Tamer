package edu.ucsd.ncmir.jinx.segmentation.manual.gui.events;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.jinx.segmentation.manual.gui.JxAbstractTracingSegmenter;

public class JxMSegPlaneNumberChangedEvent extends AsynchronousEvent {

    public JxMSegPlaneNumberChangedEvent(JxAbstractTracingSegmenter manual_seg) {
        super(manual_seg);
    }
}
