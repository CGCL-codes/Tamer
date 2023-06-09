package hu.openig.screens;

import hu.openig.core.Act;
import hu.openig.core.ResourceLocator.ResourcePlace;
import hu.openig.core.ResourceType;
import hu.openig.render.RenderTools;
import hu.openig.ui.UIGenericButton;
import hu.openig.ui.UIImageButton;
import hu.openig.ui.UIMouse;
import hu.openig.ui.UIMouse.Type;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The video listing screen.
 * @author akarnokd, 2010.01.15.
 */
public class VideoScreen extends ScreenBase {

    /** The video entry. */
    class VideoEntry {

        /** The video name. */
        String name;

        /** The video path. */
        String path;

        /** The full video path and name. */
        String fullName;
    }

    /** The screen origin. */
    Rectangle origin;

    /** The listing rectangle. */
    final Rectangle listRect = new Rectangle();

    /** Scroll up button. */
    UIImageButton scrollUpButton;

    /** Scroll down button. */
    UIImageButton scrollDownButton;

    /** Statistics label. */
    UIGenericButton playLabel;

    /** Achievements label. */
    UIGenericButton backLabel;

    /** The top index. */
    int videoIndex;

    /** The view count. */
    int videoCount;

    /** The list of videos. */
    final List<VideoEntry> videos = new ArrayList<VideoEntry>();

    /** The selected video. */
    VideoEntry selectedVideo;

    @Override
    public void onResize() {
        RenderTools.centerScreen(origin, getInnerWidth(), getInnerHeight(), true);
        listRect.setBounds(origin.x + 10, origin.y + 20, origin.width - 50, 360);
        videoCount = listRect.height / 20;
        scrollUpButton.x = origin.x + listRect.width + 12;
        scrollUpButton.y = origin.y + 10 + (listRect.height / 2 - scrollUpButton.height) / 2;
        scrollDownButton.x = scrollUpButton.x;
        scrollDownButton.y = origin.y + 10 + listRect.height / 2 + (listRect.height / 2 - scrollDownButton.height) / 2;
        int w = origin.width / 2;
        backLabel.x = origin.x + (w - backLabel.width) / 2;
        backLabel.y = origin.y + origin.height - backLabel.height - 5;
        playLabel.x = origin.x + w + (w - playLabel.width) / 2;
        playLabel.y = origin.y + origin.height - playLabel.height - 5;
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onInitialize() {
        origin = new Rectangle(0, 0, commons.common().infoEmpty.getWidth(), commons.common().infoEmpty.getHeight());
        scrollUpButton = new UIImageButton(commons.database().arrowUp);
        scrollUpButton.setHoldDelay(100);
        scrollUpButton.onClick = new Act() {

            @Override
            public void act() {
                doScrollUp(1);
            }
        };
        scrollDownButton = new UIImageButton(commons.database().arrowDown);
        scrollDownButton.setHoldDelay(100);
        scrollDownButton.onClick = new Act() {

            @Override
            public void act() {
                doScrollDown(1);
            }
        };
        playLabel = new UIGenericButton(get("videos.play"), fontMetrics(16), commons.common().mediumButton, commons.common().mediumButtonPressed);
        playLabel.onClick = new Act() {

            @Override
            public void act() {
                if (selectedVideo != null) {
                    playVideos(selectedVideo.fullName);
                }
            }
        };
        backLabel = new UIGenericButton(get("videos.back"), fontMetrics(16), commons.common().mediumButton, commons.common().mediumButtonPressed);
        backLabel.onClick = new Act() {

            @Override
            public void act() {
                hideSecondary();
            }
        };
        addThis();
    }

    @Override
    public boolean keyboard(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            hideSecondary();
        }
        return false;
    }

    @Override
    public boolean mouse(UIMouse e) {
        boolean rep = false;
        switch(e.type) {
            case DOWN:
            case DOUBLE_CLICK:
                if (listRect.contains(e.x, e.y)) {
                    int idx = (e.y - listRect.y) / 20 + videoIndex;
                    if (idx < videos.size()) {
                        selectedVideo = videos.get(idx);
                        if (e.type == Type.DOUBLE_CLICK) {
                            playVideos(selectedVideo.fullName);
                        }
                        rep = true;
                    }
                } else {
                    rep = super.mouse(e);
                }
                break;
            case WHEEL:
                if (listRect.contains(e.x, e.y)) {
                    if (e.z < 0) {
                        doScrollUp(3);
                    } else {
                        doScrollDown(3);
                    }
                }
                break;
            default:
                rep = super.mouse(e);
        }
        return rep;
    }

    /** 
	 * Scoll the list up. 
	 * @param amount the scroll amount
	 */
    void doScrollUp(int amount) {
        int oldIndex = videoIndex;
        videoIndex = Math.max(0, videoIndex - amount);
        if (oldIndex != videoIndex) {
            adjustScrollButtons();
        }
    }

    /** 
	 * Scroll the list down. 
	 * @param amount the scroll amount
	 */
    void doScrollDown(int amount) {
        if (videos.size() > videoCount) {
            int oldIndex = videoIndex;
            videoIndex = Math.min(videoIndex + amount, videos.size() - videoCount);
            if (oldIndex != videoIndex) {
                adjustScrollButtons();
            }
        }
    }

    /** Adjust the visibility of the scroll buttons. */
    void adjustScrollButtons() {
        scrollUpButton.visible(videoIndex > 0);
        scrollDownButton.visible(videoIndex < videos.size() - videoCount);
        askRepaint();
    }

    @Override
    public void onEnter(Screens mode) {
        videos.clear();
        for (String lang : Arrays.asList("generic", commons.config.language)) {
            for (ResourcePlace rp : rl.resourceMap.get(ResourceType.VIDEO).get(lang).values()) {
                VideoEntry ve = new VideoEntry();
                ve.fullName = rp.getName();
                int idx = rp.getName().lastIndexOf('/');
                ve.name = rp.getName();
                ve.path = "";
                if (idx < 0) {
                    ve.name = rp.getName().substring(idx + 1);
                    ve.path = rp.getName().substring(idx);
                }
                videos.add(ve);
            }
        }
        Collections.sort(videos, new Comparator<VideoEntry>() {

            @Override
            public int compare(VideoEntry o1, VideoEntry o2) {
                return o1.fullName.compareTo(o2.fullName);
            }
        });
        onResize();
        adjustScrollButtons();
    }

    @Override
    public void onLeave() {
    }

    @Override
    public void draw(Graphics2D g2) {
        RenderTools.darkenAround(origin, getInnerWidth(), getInnerHeight(), g2, 0.5f, true);
        g2.drawImage(commons.common().infoEmpty, origin.x, origin.y, null);
        String s = get("videos.all_videos");
        int w = commons.text().getTextWidth(14, s) + 10;
        g2.setColor(Color.BLACK);
        g2.fillRect(origin.x + (origin.width - w) / 2, origin.y - 9, w, 18);
        commons.text().paintTo(g2, origin.x + (origin.width - w) / 2 + 5, origin.y - 7, 14, 0xFFFFCC00, s);
        Shape save0 = g2.getClip();
        g2.clipRect(listRect.x, listRect.y, listRect.width, listRect.height);
        int y = listRect.y;
        int h = 10;
        for (int i = videoIndex; i < videos.size() && i < videoIndex + videoCount; i++) {
            VideoEntry ve = videos.get(i);
            int color = ve == selectedVideo ? 0xFFFFCC00 : 0xFF80FFFF;
            commons.text().paintTo(g2, listRect.x + 10, y + (20 - h) / 2, h, color, ve.fullName);
            y += 20;
        }
        g2.setClip(save0);
        super.draw(g2);
    }

    @Override
    public Screens screen() {
        return Screens.VIDEOS;
    }
}
