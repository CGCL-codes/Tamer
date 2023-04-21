package com.sksdpt.kioskjui.gui.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import com.sksdpt.kioskjui.bean.CntContentBean;
import com.sksdpt.kioskjui.bean.CntRsrcInfoBean;
import com.sksdpt.kioskjui.bean.MPlayerInfoBean;
import com.sksdpt.kioskjui.control.config.DSess;
import com.sksdpt.kioskjui.control.config.VSess;
import com.sksdpt.kioskjui.gui.util.MPlayerRunnable;

/**
 * @author "Luis Alfonso Vega Garcia" <vegacom@gmail.com>
 */
public class MediaPreviewComposite extends Composite {

    private Point maxSize;

    private Point offset;

    private Composite cpsVideo;

    /**
     * @param parent
     * @param style
     */
    public MediaPreviewComposite(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * @return the offset
     */
    public Point getOffset() {
        return offset;
    }

    public void setContent(CntContentBean content) {
        release();
        CntRsrcInfoBean mpreview = content.getMediaPreview();
        String mediaPreviewPath = content.getAbsMediaPreviewPath();
        if (mpreview.getTypeDotExt().startsWith("video")) {
            Point size = new Point(mpreview.getWidth(), mpreview.getHeight());
            setBounds(resize(size));
            playVideo(mediaPreviewPath);
        } else if (mpreview.getTypeDotExt().startsWith("image")) {
            Point size = new Point(mpreview.getWidth(), mpreview.getHeight());
            setBounds(resize(size));
            showImage(mediaPreviewPath);
        } else if (mpreview.getTypeDotExt().startsWith("audio")) {
            playAudio(mediaPreviewPath);
            String videoPath = DSess.sngltn().getAppPath("var", "db", "bg-motion-videos");
            setBounds(resize(maxSize));
            playVideo(videoPath);
        }
    }

    /**
     * @param maxSize
     *            the maxSize to set
     */
    public void setMaxSize(Point maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * @param offset
     *            the offset to set
     */
    public void setOffset(Point offset) {
        this.offset = offset;
    }

    private void playAudio(String audioPath) {
        MPlayerInfoBean mplayerInfo = new MPlayerInfoBean();
        mplayerInfo.setMediaPath(audioPath);
        MPlayerRunnable audioRunnable = new MPlayerRunnable();
        audioRunnable.setMplayerInfo(mplayerInfo);
        VSess.sngltn().setAudioRunnable(audioRunnable);
        String cnt = VSess.sngltn().getThreadsCounterNext();
        new Thread(audioRunnable, "audio" + cnt).start();
    }

    private void playVideo(String videoPath) {
        cpsVideo = new Composite(this, SWT.EMBEDDED);
        cpsVideo.setSize(getSize());
        cpsVideo.setVisible(false);
        MPlayerInfoBean mplayerInfo = new MPlayerInfoBean();
        mplayerInfo.setMediaPath(videoPath);
        mplayerInfo.setWid(cpsVideo.embeddedHandle);
        mplayerInfo.setSize(getSize());
        MPlayerRunnable videoRunnable = new MPlayerRunnable();
        videoRunnable.setMplayerInfo(mplayerInfo);
        VSess.sngltn().setVideoRunnable(videoRunnable);
        String cnt = VSess.sngltn().getThreadsCounterNext();
        new Thread(videoRunnable, "video" + cnt).start();
        cpsVideo.setVisible(true);
    }

    private void release() {
        VSess.sngltn().stopMPlayerThreads();
        if (cpsVideo != null && !cpsVideo.isDisposed()) cpsVideo.dispose();
    }

    private Rectangle resize(Point size) {
        Rectangle bounds = resize_base(size);
        bounds.x += offset.x;
        bounds.y += offset.y;
        return bounds;
    }

    private Rectangle resize_base(Point size) {
        int x, y;
        int w = size.x;
        int h = size.y;
        if (w <= maxSize.x && h <= maxSize.y) {
            x = (maxSize.x - w) / 2;
            y = (maxSize.y - h) / 2;
            Rectangle bounds = new Rectangle(x, y, w, h);
            return bounds;
        }
        double propx = (double) w / maxSize.x;
        double propy = (double) h / maxSize.y;
        if (propx > propy) {
            w /= propx;
            h /= propx;
            x = 0;
            y = (maxSize.y - h) / 2;
        } else {
            w /= propy;
            h /= propy;
            y = 0;
            x = (maxSize.x - w) / 2;
        }
        return new Rectangle(x, y, w, h);
    }

    private void showImage(String imagePath) {
        Image image = VSess.sngltn().getGuiHelper().createImage(imagePath);
        setBackgroundImage(image);
    }
}
