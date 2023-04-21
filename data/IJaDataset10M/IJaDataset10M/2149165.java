package com.luzan.app.map.tool;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.IOUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.sun.xfile.XFile;
import com.sun.xfile.XFileInputStream;
import com.sun.xfile.XFileOutputStream;
import com.sun.media.jai.codec.FileCacheSeekableStream;
import com.sun.media.jai.codec.JPEGEncodeParam;
import javax.media.jai.RenderedOp;
import javax.media.jai.JAI;
import javax.media.jai.operator.CropDescriptor;

/**
 * MapCorner
 *
 * @author Alexander Bondar
 */
public class MapCorner {

    private static final Logger logger = Logger.getLogger(MapCorner.class);

    protected XFile srcDir;

    protected boolean cntn = true;

    public void setSrcDir(String srcDir) {
        this.srcDir = new XFile(srcDir);
    }

    public void setCntn(String v) {
        cntn = Boolean.parseBoolean(v);
    }

    public void crop(XFile srcFile, XFile subDir) throws IOException {
        JPEGEncodeParam jpegEncPar = new JPEGEncodeParam();
        jpegEncPar.setQuality(0.35f);
        final XFile fileTopLeft = new XFile(subDir, "top-left.jpg");
        final XFile fileTopRight = new XFile(subDir, "top-right.jpg");
        final XFile fileBottomRight = new XFile(subDir, "bottom-left.jpg");
        final XFile fileBottomLeft = new XFile(subDir, "bottom-right.jpg");
        XFileInputStream in = null;
        FileCacheSeekableStream min = null;
        OutputStream out = null;
        if (!cntn || !(fileTopRight.exists() && fileTopRight.length() > 1)) {
            try {
                in = new XFileInputStream(srcFile);
                min = new FileCacheSeekableStream(in);
                final RenderedOp imSrc = new RenderedOp("stream", (new ParameterBlock()).add(min), null);
                final RenderedOp imTopRight = CropDescriptor.create(imSrc, (float) imSrc.getWidth() - 400, 0f, 400f, 400f, null);
                out = new BufferedOutputStream(new XFileOutputStream(fileTopRight), 1024 * 1024);
                ParameterBlock pbEncode = new ParameterBlock();
                pbEncode.add(out).add("JPEG").add(jpegEncPar);
                pbEncode.addSource(imTopRight.getRendering().getAsBufferedImage());
                final RenderedOp encodeOp = JAI.create("encode", pbEncode);
                out.flush();
                encodeOp.dispose();
                imTopRight.dispose();
                imSrc.dispose();
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(min);
                IOUtils.closeQuietly(out);
            }
        }
        if (!cntn || !(fileTopLeft.exists() && fileTopLeft.length() > 1)) {
            try {
                in = new XFileInputStream(srcFile);
                min = new FileCacheSeekableStream(in);
                final RenderedOp imSrc = new RenderedOp("stream", (new ParameterBlock()).add(min), null);
                final RenderedOp imTopLeft = CropDescriptor.create(imSrc, 0f, 0f, 400f, 400f, null);
                out = new BufferedOutputStream(new XFileOutputStream(fileTopLeft), 1024 * 1024);
                ParameterBlock pbEncode = new ParameterBlock();
                pbEncode.add(out).add("JPEG").add(jpegEncPar);
                pbEncode.addSource(imTopLeft.getRendering().getAsBufferedImage());
                final RenderedOp encodeOp = JAI.create("encode", pbEncode);
                out.flush();
                encodeOp.dispose();
                imTopLeft.dispose();
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(min);
                IOUtils.closeQuietly(out);
            }
        }
        if (!cntn || !(fileBottomLeft.exists() && fileBottomLeft.length() > 1)) {
            try {
                in = new XFileInputStream(srcFile);
                min = new FileCacheSeekableStream(in);
                final RenderedOp imSrc = new RenderedOp("stream", (new ParameterBlock()).add(min), null);
                final RenderedOp imBottomLeft = CropDescriptor.create(imSrc, 0f, (float) imSrc.getHeight() - 400, 400f, 400f, null);
                out = new BufferedOutputStream(new XFileOutputStream(fileBottomLeft), 1024 * 1024);
                ParameterBlock pbEncode = new ParameterBlock();
                pbEncode.add(out).add("JPEG").add(jpegEncPar);
                pbEncode.addSource(imBottomLeft.getRendering().getAsBufferedImage());
                final RenderedOp encodeOp = JAI.create("encode", pbEncode);
                out.flush();
                encodeOp.dispose();
                imBottomLeft.dispose();
                imSrc.dispose();
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(min);
                IOUtils.closeQuietly(out);
            }
        }
        if (!cntn || !(fileBottomRight.exists() && fileBottomRight.length() > 1)) {
            try {
                in = new XFileInputStream(srcFile);
                min = new FileCacheSeekableStream(in);
                final RenderedOp imSrc = new RenderedOp("stream", (new ParameterBlock()).add(min), null);
                final RenderedOp imBottomRight = CropDescriptor.create(imSrc, (float) imSrc.getWidth() - 400, (float) imSrc.getHeight() - 400, 400f, 400f, null);
                out = new BufferedOutputStream(new XFileOutputStream(fileBottomRight), 1024 * 1024);
                ParameterBlock pbEncode = new ParameterBlock();
                pbEncode.add(out).add("JPEG").add(jpegEncPar);
                pbEncode.addSource(imBottomRight.getRendering().getAsBufferedImage());
                final RenderedOp encodeOp = JAI.create("encode", pbEncode);
                out.flush();
                encodeOp.dispose();
                imBottomRight.dispose();
                imSrc.dispose();
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(min);
                IOUtils.closeQuietly(out);
            }
        }
    }

    public void doIt() throws Throwable {
        int n = 0;
        if (srcDir.isDirectory() && srcDir.exists()) {
            final String[] subDirs = srcDir.list();
            for (final String subDirPath : subDirs) {
                logger.debug("1. found dir: " + subDirPath);
                final XFile subDir = new XFile(srcDir, subDirPath);
                final XFile mapFile = new XFile(subDir, subDirPath);
                if (subDir.isDirectory() && subDir.exists() && mapFile.exists() && mapFile.isFile()) {
                    try {
                        logger.debug("2. found map file: " + mapFile.getName());
                        crop(mapFile, subDir);
                        logger.debug("3. file processed");
                    } catch (Throwable t) {
                        logger.error("error", t);
                    }
                    if (++n % 1000 == 0) logger.info("processed: " + n);
                }
            }
        }
    }

    public static void main(String args[]) {
        MapCorner proc = new MapCorner();
        String allArgs = StringUtils.join(args, ' ');
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(MapCorner.class, Object.class);
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                Pattern p = Pattern.compile("-" + pd.getName() + "\\s*([\\S]*)", Pattern.CASE_INSENSITIVE);
                final Matcher m = p.matcher(allArgs);
                if (m.find()) {
                    pd.getWriteMethod().invoke(proc, m.group(1));
                }
            }
            proc.doIt();
        } catch (Throwable e) {
            logger.error("error", e);
            System.out.println(e.getMessage());
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(MapCorner.class);
                System.out.println("Options:");
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    System.out.println("-" + pd.getName());
                }
            } catch (Throwable t) {
                System.out.print("Internal error");
            }
        }
    }
}
