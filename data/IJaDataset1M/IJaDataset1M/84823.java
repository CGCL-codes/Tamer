package jorgan.midimerger;

public class MergeInput {

    private int channel;

    private String device;

    /**
	 * @param channelColonDevice
	 *            channel and device separated by a colon
	 * @see #toString()
	 */
    public MergeInput(String channelColonDevice) {
        int colon = channelColonDevice.indexOf(':');
        if (colon == -1) {
            throw new IllegalArgumentException("channel and device expected in'" + channelColonDevice);
        }
        this.channel = Integer.parseInt(channelColonDevice.substring(0, colon));
        this.device = channelColonDevice.substring(colon + 1);
    }

    public MergeInput(String device, int channel) {
        this.device = device;
        this.channel = channel;
    }

    public String getDevice() {
        return device;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return channel + ":" + device;
    }
}
