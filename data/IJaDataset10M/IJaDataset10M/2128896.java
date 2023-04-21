package com.frinika.sequencer.gui.partview;

import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.JComponent;
import uk.org.toot.audio.core.AudioProcess;
import com.frinika.project.FrinikaAudioSystem;
import com.frinika.sequencer.gui.ListProvider;
import com.frinika.sequencer.gui.PopupClient;
import com.frinika.sequencer.gui.PopupSelectorButton;
import com.frinika.sequencer.model.AudioLane;

public class AudioLaneView extends LaneView {

    AudioProcess audioIn;

    String name = "null";

    public AudioLaneView(AudioLane lane) {
        super(lane);
        init();
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected void makeButtons() {
        JComponent but = createDeviceSelector();
        add(but, gc);
        gc.weighty = 1.0;
        add(new Box.Filler(new Dimension(0, 0), new Dimension(10000, 10000), new Dimension(10000, 10000)), gc);
    }

    PopupSelectorButton createDeviceSelector() {
        audioIn = ((AudioLane) lane).getAudioInDevice();
        ListProvider resource = new ListProvider() {

            public Object[] getList() {
                List<String> vec = FrinikaAudioSystem.getAudioServer().getAvailableInputNames();
                String list[] = new String[vec.size()];
                list = vec.toArray(list);
                return list;
            }
        };
        PopupClient client = new PopupClient() {

            public void fireSelected(PopupSelectorButton but, Object o, int cnt) {
                AudioProcess in;
                try {
                    in = FrinikaAudioSystem.getAudioServer().openAudioInput((String) o, null);
                    ((AudioLane) lane).setAudioInDevice(in);
                    name = (String) o;
                    if (in != audioIn) init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return new PopupSelectorButton(resource, client, name);
    }
}
