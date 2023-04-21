package org.magnesia.client.gui.tasks;

import java.util.Collection;
import org.magnesia.OPCodes;
import org.magnesia.client.gui.ClientConnection.ThumbnailListener;

public class ThumbnailLoader extends LongTermTask {

    private int i = 0;

    private Collection<String> images;

    private ThumbnailListener tl;

    public ThumbnailLoader(LongTermManager ltm, Collection<String> images, ThumbnailListener tl) {
        super(ltm);
        this.images = images;
        this.tl = tl;
    }

    public int getStatus() {
        return i;
    }

    @Override
    public int getLength() {
        return images.size();
    }

    @Override
    public String getActiveText() {
        return "Loading thumbnail " + i + " of " + images.size();
    }

    @Override
    public String getDescription() {
        return "Loading thumbnails";
    }

    public void run() {
        try {
            ltm.lockConnection();
            ltm.getStream().writeOpCode(OPCodes.GET_THUMBNAILS);
            ltm.getStream().writeInt(images.size());
            for (String s : images) {
                ltm.getStream().writeString(s);
            }
            ltm.getStream().readString();
            for (; i < images.size() && running; i++) {
                String imgHash = ltm.getStream().readString();
                String name = ltm.getStream().readString();
                long filesize = ltm.getStream().readLong();
                byte buf[] = ltm.getStream().readDataFully();
                if (running) {
                    running = tl.received(imgHash, name, buf, filesize);
                }
                ltm.getStream().writeBoolean(running);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ltm.unlockConnection();
            running = false;
            finished = true;
        }
    }
}
