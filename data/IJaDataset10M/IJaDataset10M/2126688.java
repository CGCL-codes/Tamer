package com.ibm.tuningfork.core.streamgui;

import org.eclipse.swt.graphics.Image;
import com.ibm.tuningfork.infra.stream.SnippedSampleStream;
import com.ibm.tuningfork.infra.stream.core.SampleStream;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.stream.core.TimeIntervalStream;

public class SnippedSampleStreamGUI extends SampleStreamGUI {

    private static final Image OPERATOR_ICON = getIconImage("streamopSnip");

    public Class<?> streamClass() {
        return SnippedSampleStream.class;
    }

    public Class<?>[] consumes() {
        return new Class[] { SampleStream.class };
    }

    public Image getOperatorIcon() {
        return OPERATOR_ICON;
    }

    public boolean operatorIconIsDefault() {
        return false;
    }

    public String getOperatorName() {
        return "snipped";
    }

    public SnipGroup createWidgetGroupHandler(IStreamGUIHost host) {
        return new SnipGroup(host);
    }

    private class SnipGroup extends StreamPairWithOptionGroup {

        public SnipGroup(IStreamGUIHost host) {
            super(host, "Snip Time", SnippedSampleStream.class, TimeIntervalStream.class, "Normal", "Inverted", null);
        }

        protected Stream createStream(Stream thatStream) {
            return new SnippedSampleStream((SampleStream) host.getBaseStream(), (TimeIntervalStream) thatStream, !isFirstOptionSelected());
        }
    }
}
