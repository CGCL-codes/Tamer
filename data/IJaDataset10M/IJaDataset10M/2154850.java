package com.googlecode.javacv;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.PointerPointer;
import java.io.File;
import static com.googlecode.javacv.cpp.avcodec.*;
import static com.googlecode.javacv.cpp.avformat.*;
import static com.googlecode.javacv.cpp.avutil.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.swscale.*;

/**
 *
 * @author Samuel Audet
 */
public class FFmpegFrameRecorder extends FrameRecorder {

    private static Exception loadingException = null;

    public static void tryLoad() throws Exception {
        if (loadingException != null) {
            throw loadingException;
        } else {
            try {
                Loader.load(com.googlecode.javacv.cpp.avutil.class);
                Loader.load(com.googlecode.javacv.cpp.avcodec.class);
                Loader.load(com.googlecode.javacv.cpp.avformat.class);
                Loader.load(com.googlecode.javacv.cpp.swscale.class);
            } catch (Throwable t) {
                if (t instanceof Exception) {
                    throw loadingException = (Exception) t;
                } else {
                    throw loadingException = new Exception("Failed to load " + FFmpegFrameRecorder.class, t);
                }
            }
        }
    }

    public FFmpegFrameRecorder(File file, int imageWidth, int imageHeight) {
        this(file.getAbsolutePath(), imageWidth, imageHeight);
    }

    public FFmpegFrameRecorder(String filename, int imageWidth, int imageHeight) {
        av_register_all();
        this.filename = filename;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.pixelFormat = PIX_FMT_RGB32;
        this.codecID = CODEC_ID_HUFFYUV;
        this.bitrate = 400000;
        this.frameRate = 30;
        this.pkt = new AVPacket();
        this.tempPicture = new AVPicture();
    }

    public void release() throws Exception {
        stop();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    private String filename;

    private AVFrame picture;

    private BytePointer video_outbuf;

    private int video_outbuf_size;

    private AVOutputFormat oformat;

    private AVFormatContext oc;

    private AVCodecContext c;

    private AVStream video_st;

    private SwsContext img_convert_ctx;

    private AVPacket pkt;

    private AVPicture tempPicture;

    public static final int DEFAULT_FRAME_RATE_BASE = 1001000;

    public void start() throws Exception {
        String formatName = format == null || format.length() == 0 ? null : format;
        oformat = av_guess_format(formatName, filename, null);
        if (oformat == null) {
            throw new Exception("Could not find suitable output format");
        }
        oc = avformat_alloc_context();
        if (oc == null) {
            throw new Exception("Memory error");
        }
        oc.oformat(oformat);
        oc.filename(filename);
        video_st = av_new_stream(oc, 0);
        if (video_st == null) {
            throw new Exception("Could not alloc stream");
        }
        c = video_st.codec();
        c.codec_id(codecID == CODEC_ID_NONE ? oformat.video_codec() : codecID);
        c.codec_type(CODEC_TYPE_VIDEO);
        c.bit_rate(bitrate);
        c.width(imageWidth);
        c.height(imageHeight);
        c.time_base(av_d2q(1 / frameRate, DEFAULT_FRAME_RATE_BASE));
        c.gop_size(12);
        c.pix_fmt(pixelFormat);
        if (c.codec_id() == CODEC_ID_MPEG2VIDEO) {
            c.max_b_frames(2);
        }
        if (c.codec_id() == CODEC_ID_MPEG1VIDEO) {
            c.mb_decision(2);
        }
        String name = oformat.name().getString();
        if (name.equals("mp4") || name.equals("mov") || name.equals("3gp")) {
            c.flags(c.flags() | CODEC_FLAG_GLOBAL_HEADER);
        }
        if (av_set_parameters(oc, null) < 0) {
            av_freep(video_st);
            video_st = null;
            throw new Exception("Invalid output format parameters");
        }
        dump_format(oc, 0, filename, 1);
        if (video_st != null) {
            AVCodec codec = avcodec_find_encoder(c.codec_id());
            if (codec == null) {
                av_freep(video_st);
                video_st = null;
                throw new Exception("codec not found");
            }
            if (avcodec_open(c, codec) < 0) {
                av_freep(video_st);
                video_st = null;
                throw new Exception("could not open codec");
            }
            picture = avcodec_alloc_frame();
            if (picture != null) {
                int size = avpicture_get_size(c.pix_fmt(), c.width(), c.height());
                BytePointer picture_buf = new BytePointer(av_malloc(size));
                if (picture_buf == null) {
                    av_free(picture);
                    picture = null;
                } else {
                    avpicture_fill(picture, picture_buf, c.pix_fmt(), c.width(), c.height());
                }
            }
            if (picture == null) {
                avcodec_close(c);
                av_freep(video_st);
                video_st = null;
                throw new Exception("Could not allocate picture");
            }
            video_outbuf = null;
            if ((oformat.flags() & AVFMT_RAWPICTURE) == 0) {
                video_outbuf_size = imageWidth * imageHeight * 4;
                video_outbuf = new BytePointer(av_malloc(video_outbuf_size));
            }
        }
        if ((oformat.flags() & AVFMT_NOFILE) == 0) {
            ByteIOContext p = new ByteIOContext(null);
            if (url_fopen(p, filename, URL_WRONLY) < 0) {
                avcodec_close(c);
                av_free(picture.data(0));
                av_free(picture);
                av_free(video_outbuf);
                av_freep(video_st);
                video_st = null;
                throw new Exception("Could not open '" + filename + "'");
            }
            oc.pb(p);
        }
        av_write_header(oc);
    }

    public void stop() throws Exception {
        if (video_st != null) {
            avcodec_close(c);
            av_free(picture.data(0));
            av_free(picture);
            av_free(video_outbuf);
            av_freep(video_st);
            video_st = null;
        }
        if (oc != null) {
            av_write_trailer(oc);
            int nb_streams = oc.nb_streams();
            for (int i = 0; i < nb_streams; i++) {
                av_freep(oc.streams(i));
            }
            if ((oformat.flags() & AVFMT_NOFILE) == 0) {
                url_fclose(oc.pb());
            }
            av_free(oc);
            oc = null;
        }
    }

    public void record(IplImage frame) throws Exception {
        record(frame, PIX_FMT_NONE);
    }

    public void record(IplImage frame, int pixelFormat) throws Exception {
        if (video_st == null) {
            throw new Exception("No video output stream (Has start() been called?)");
        }
        int out_size, ret;
        if (frame == null) {
        } else {
            int width = frame.width();
            int height = frame.height();
            int step = frame.widthStep();
            BytePointer data = frame.imageData();
            if (pixelFormat == PIX_FMT_NONE) {
                int depth = frame.depth();
                int channels = frame.nChannels();
                if ((depth == IPL_DEPTH_8U || depth == IPL_DEPTH_8S) && channels == 3) {
                    pixelFormat = PIX_FMT_BGR24;
                } else if ((depth == IPL_DEPTH_8U || depth == IPL_DEPTH_8S) && channels == 1) {
                    pixelFormat = PIX_FMT_GRAY8;
                } else if ((depth == IPL_DEPTH_16U || depth == IPL_DEPTH_16S) && channels == 1) {
                    pixelFormat = AV_HAVE_BIGENDIAN() ? PIX_FMT_GRAY16BE : PIX_FMT_GRAY16LE;
                } else if ((depth == IPL_DEPTH_8U || depth == IPL_DEPTH_8S) && channels == 4) {
                    pixelFormat = PIX_FMT_RGBA;
                } else if ((depth == IPL_DEPTH_8U || depth == IPL_DEPTH_8S) && channels == 2) {
                    pixelFormat = PIX_FMT_NV21;
                    step = width;
                } else {
                    throw new Exception("Could not guess pixel format of image: depth=" + depth + ", channels=" + channels);
                }
            }
            if (c.pix_fmt() != pixelFormat) {
                if (img_convert_ctx == null) {
                    img_convert_ctx = sws_getContext(width, height, pixelFormat, c.width(), c.height(), c.pix_fmt(), SWS_BILINEAR, null, null, null);
                    if (img_convert_ctx == null) {
                        throw new Exception("Cannot initialize the conversion context");
                    }
                }
                avpicture_fill(tempPicture, data, pixelFormat, width, height);
                tempPicture.linesize(0, step);
                sws_scale(img_convert_ctx, new PointerPointer(tempPicture), tempPicture.linesize(), 0, c.height(), new PointerPointer(picture), picture.linesize());
            } else {
                avpicture_fill(picture, data, c.pix_fmt(), c.width(), c.height());
                picture.linesize(0, step);
            }
        }
        if ((oformat.flags() & AVFMT_RAWPICTURE) != 0) {
            av_init_packet(pkt);
            pkt.flags(pkt.flags() | PKT_FLAG_KEY);
            pkt.stream_index(video_st.index());
            pkt.data(new BytePointer(picture));
            pkt.size(Loader.sizeof(AVPicture.class));
            ret = av_write_frame(oc, pkt);
        } else {
            out_size = avcodec_encode_video(c, video_outbuf, video_outbuf_size, picture);
            if (out_size > 0) {
                av_init_packet(pkt);
                AVFrame coded_frame = c.coded_frame();
                long pts = coded_frame.pts();
                if (coded_frame.pts() != AV_NOPTS_VALUE) pkt.pts(av_rescale_q(pts, c.time_base(), video_st.time_base()));
                if (coded_frame.key_frame() != 0) pkt.flags(pkt.flags() | PKT_FLAG_KEY);
                pkt.stream_index(video_st.index());
                pkt.data(video_outbuf);
                pkt.size(out_size);
                ret = av_write_frame(oc, pkt);
            } else {
                ret = 0;
            }
        }
        if (ret != 0) {
            throw new Exception("Error while writing video frame");
        }
    }
}
