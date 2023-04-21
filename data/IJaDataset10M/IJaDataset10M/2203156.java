package org.designerator.media.image.editor.filter;

import org.designerator.common.data.ProcessorData;
import org.designerator.common.data.SegmentImageData;
import org.designerator.common.interfaces.IImageEditor;
import org.designerator.media.image.filter.Processor;
import org.designerator.media.image.filter.util.FilterUtils;
import org.designerator.media.image.filter.util.Kernels;
import org.designerator.media.image.filter.util.OffsetKernel;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.ImageData;

public class Sobel extends Processor {

    public static final String Name = "Sobel";

    public static final String StrengthID = "Strength";

    public static boolean runSobel(SegmentImageData sourceData, final byte[] dest, final short tmp[], boolean fillEdged, float[] lutV, float[] lutH, int[] offset, float strength, IProgressMonitor monitor) {
        final int bpp = sourceData.bpp;
        int scanLineEnd = sourceData.scanLineEnd;
        final int klength = offset.length;
        final byte[] source = sourceData.data;
        final int bs = sourceData.blueShift;
        final int gs = sourceData.greenShift;
        final int rs = sourceData.redShift;
        final int length = sourceData.length;
        int r = 0, g = 0, b = 0;
        for (int i = sourceData.start; i < length; i += bpp) {
            if (fillEdged) {
                float rsum = 0, gsum = 0, bsum = 0;
                float rsum2 = 0, gsum2 = 0, bsum2 = 0;
                for (int k = 0; k < klength; k++) {
                    b = source[i + bs + offset[k]] & 0xff;
                    g = source[i + gs + offset[k]] & 0xff;
                    r = source[i + rs + offset[k]] & 0xff;
                    rsum += (lutV[k] * r);
                    gsum += (lutV[k] * g);
                    bsum += (lutV[k] * b);
                    rsum2 += (lutH[k] * r);
                    gsum2 += (lutH[k] * g);
                    bsum2 += (lutH[k] * b);
                }
                r = (int) (Math.sqrt(rsum * rsum + rsum2 * rsum2));
                g = (int) (Math.sqrt(gsum * gsum + gsum2 * gsum2));
                b = (int) (Math.sqrt(bsum * bsum + bsum2 * bsum2));
                tmp[i + bs] = (short) (b);
                tmp[i + gs] = (short) (g);
                tmp[i + rs] = (short) (r);
            }
            r = (int) (tmp[i + rs] / strength);
            g = (int) (tmp[i + gs] / strength);
            b = (int) (tmp[i + bs] / strength);
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }
            dest[i + bs] = (byte) (b);
            dest[i + gs] = (byte) (g);
            dest[i + rs] = (byte) (r);
            if (i == scanLineEnd) {
                scanLineEnd += sourceData.bytesPerLine;
                i += sourceData.kpad;
                if (monitor != null) {
                    monitor.worked(1);
                }
            }
        }
        return true;
    }

    private IImageEditor editor;

    private ImageData iData;

    private byte[] dest;

    private short[] tmp;

    private int[] offset;

    private float[] lutH;

    private float[] lutV;

    private float strength = 1.7f;

    protected boolean fillEdged = true;

    public Sobel() {
        super();
        setNumThreads(1);
    }

    @Override
    public void dispose() {
        if (!isRunning()) {
            iData = null;
            dest = null;
            tmp = null;
            super.dispose();
        }
    }

    public ProcessorData[] getCurrentProcessorData() {
        ProcessorData[] params = new ProcessorData[1];
        ProcessorData sData = new ProcessorData();
        sData.name = StrengthID;
        sData.max = 400;
        sData.min = 0;
        sData.increment = 200;
        sData.index = 0;
        sData.delay = true;
        sData.selection = (int) (strength * 100 + 0.5f);
        params[0] = sData;
        return params;
    }

    public ImageData getOrignalImageData() {
        return iData;
    }

    @Override
    public byte[] getResult() {
        return dest;
    }

    public String getName() {
        return Name;
    }

    public void init(IImageEditor editor, boolean previewData, boolean deleteCache) {
        if (editor == null) {
            return;
        }
        this.editor = editor;
        iData = editor.getImageData(previewData, deleteCache);
        try {
            dest = new byte[iData.data.length];
            tmp = new short[iData.data.length];
        } catch (java.lang.OutOfMemoryError e) {
            e.printStackTrace();
            return;
        }
        System.arraycopy(iData.data, 0, dest, 0, dest.length);
        fillEdged = true;
        setTimer(true);
        setKernelWidth(3);
        super.init(editor, previewData, deleteCache);
        offset = OffsetKernel.getSquareCenteredKernel(9, 3, iData.width, getBitsPerPixel(), getImageDataPad());
        lutV = Kernels.getSobelV();
        lutH = Kernels.getSobelH();
    }

    @Override
    public void processDefault(IProgressMonitor monitor, boolean preview) {
        super.processDefault(monitor, preview);
        ProcessorData[] params = new ProcessorData[1];
        params[0] = new ProcessorData();
        params[0].name = StrengthID;
        params[0].selection = (int) (strength * 100 + 0.5f);
        if (!preview) {
            init(editor, false, false);
        }
        updatePixels(params);
        if (getMonitor() != null) {
            getMonitor().beginTask(getName(), getOrignalImageData().height);
        }
        process();
    }

    @Override
    public byte[] processInThread() {
        SegmentImageData simpleIData = getSimpleIData(iData);
        runSobel(simpleIData, dest, tmp, fillEdged, lutV, lutH, offset, 4.0f - strength, getMonitor());
        return dest;
    }

    protected void processFinnished() {
        fillEdged = false;
        editor.setBusy(false);
    }

    public void run(SegmentImageData segementData) {
        runSobel(segementData, dest, tmp, fillEdged, lutV, lutH, offset, 4.0f - strength, getMonitor());
    }

    public boolean updatePixels(ProcessorData[] params) {
        if (isRunning() || params == null) {
            return false;
        }
        for (ProcessorData param : params) {
            if (param.name.equals(StrengthID)) {
                strength = (param.selection) * 0.01f;
            }
        }
        if (fillEdged) {
            editor.setBusy(true);
        }
        return true;
    }
}
