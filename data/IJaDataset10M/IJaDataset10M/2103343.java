package org.hfbk.vid;

import java.nio.ByteBuffer;
import net.sf.ffmpeg_java.AVCodecLibrary;
import net.sf.ffmpeg_java.SWScaleLibrary;
import net.sf.ffmpeg_java.AVCodecLibrary.AVCodecContext;
import net.sf.ffmpeg_java.AVCodecLibrary.AVFrame;
import net.sf.ffmpeg_java.AVFormatLibrary.AVPacket;
import org.hfbk.util.Sleeper;
import org.hfbk.vis.Prefs;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/** a video decoding thread.
 *  frames are decoded from the add()'ed AVPackets and rgb data is 
 *  presented by 'out' in BGR24 pixel format.
 *  
 *  the host could poll 'out' at frequent intervalls,
 *  and is allowed to reset it to null after receiving 
 *  one frame to easily detect if another one comes available. 
 *  
 *  if a video frame has more than maxPixels pixel, it is
 *  halved in size until it fits maxPixels. 
 *   
 * */
class AVVideoThread extends AVStreamThread {

    /** maximum bytes of buffered undecoded video data */
    static final int VIDEO_BUFFER = 1000000;

    /** width of video frames */
    int width;

    /** height of video frames */
    int height;

    /** present the decoded frames. */
    ByteBuffer out;

    long currentTime;

    int frameBytes;

    final int pix_fmt = AVCodecLibrary.PIX_FMT_BGR24;

    private AVFrame frame;

    /** resampled framebuffer */
    public AVFrame frameBGR;

    Pointer swscaler;

    /** create a video decoder 
	 * @param ctx
	 * @param maxPixels the maximum amount of pixels the output may have
	 * @param timebase the timebase in nanoseconds 
	 */
    public AVVideoThread(AVCodecContext ctx, int maxPixels, int timebase) {
        super("AVVideoThread", ctx, VIDEO_BUFFER);
        width = ctx.width;
        height = ctx.height;
        this.timebase = timebase;
        while (width * height > maxPixels) {
            width /= 2;
            height /= 2;
        }
        frame = AV.CODEC.avcodec_alloc_frame();
        frameBGR = AV.CODEC.avcodec_alloc_frame();
        if (frame == null || frameBGR == null) throw new RuntimeException("Could not allocate frames");
        frameBytes = AV.CODEC.avpicture_get_size(pix_fmt, width, height);
        final Pointer buffer = AV.UTIL.av_malloc(frameBytes);
        if (buffer == null) throw new RuntimeException("Could not allocate frame buffers");
        AV.CODEC.avpicture_fill(frameBGR, buffer, pix_fmt, width, height);
        synchronized (AV.SWSCALE) {
            swscaler = AV.SWSCALE.sws_getContext(ctx.width, ctx.height, ctx.pix_fmt, width, height, pix_fmt, SWScaleLibrary.SWS_POINT, null, null, null);
        }
        if (swscaler == null) throw new RuntimeException("Could not open swscaler.");
    }

    ByteBuffer decodeVideo(AVPacket packet) {
        final IntByReference frameFinished = new IntByReference();
        AV.CODEC.avcodec_decode_video(ctx, frame, frameFinished, packet.data, packet.size);
        if (frameFinished.getValue() != 0) {
            convertToBGR(frame, frameBGR);
            return frameBGR.data0.getByteBuffer(0, frameBytes);
        }
        return null;
    }

    public void run() {
        AVPacket packet;
        while (running) {
            packet = null;
            while (running && (!playing || (packet = poll()) == null && !finished)) Sleeper.sleep(2);
            if (packet == null) break;
            out = decodeVideo(packet);
            if (packet.dts != AVCodecLibrary.AV_NOPTS_VALUE) currentTime = packet.dts * 1000 / timebase;
            tick(currentTime);
            long d = packet.duration * 1000 / timebase;
            long dt = d + currentTime - time;
            currentTime += d;
            if (Prefs.current.debug) System.out.println("video packet t:" + currentTime + " d:" + d + " time:" + time + " dt:" + dt + " pts valid:" + (packet.pts != AVCodecLibrary.AV_NOPTS_VALUE));
            free(packet);
            if (dt > 0) Sleeper.sleep(dt);
        }
        close();
    }

    protected void close() {
        if (Prefs.current.debug) System.out.println("AVVideo finished.");
        super.close();
        synchronized (AV.UTIL) {
            AV.UTIL.av_free(frame.getPointer());
        }
        synchronized (AV.SWSCALE) {
            AV.SWSCALE.sws_freeContext(swscaler);
        }
        if (Prefs.current.debug) System.out.println("AVVideo down!");
    }

    int convertToBGR(AVFrame src, AVFrame dst) {
        Pointer[] id = src.getPointer().getPointerArray(0, 4);
        Pointer[] od = dst.getPointer().getPointerArray(0, 4);
        int[] src_linesize = src.getPointer().getIntArray(4 * Pointer.SIZE, 4);
        int[] dst_linesize = dst.getPointer().getIntArray(4 * Pointer.SIZE, 4);
        return AV.SWSCALE.sws_scale(swscaler, id, src_linesize, 0, ctx.height, od, dst_linesize);
    }

    public void freeFramebuffer() {
        running = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (frameBGR != null) {
            AV.UTIL.av_free(frameBGR.data0);
            AV.UTIL.av_free(frameBGR.getPointer());
            if (Prefs.current.debug) System.out.println("AVVideo framebuffer freed!");
            frameBGR = null;
        }
    }
}
