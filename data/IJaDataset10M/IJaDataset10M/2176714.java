package kfschmidt.stackrenderer;

import kfschmidt.data4d.*;
import kfschmidt.geom3d.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.geom.*;

public class StackRenderer {

    RasterPipeline mRasterPipeline;

    VolumeSampler mVolumeSampler;

    public BufferedImage render(VolumeTimeSeries background_vol, VolumeTimeSeries foreground_vol, BinaryVolumeTimeSeries bg_mask_vol, BinaryVolumeTimeSeries fg_mask_vol, BinaryVolumeTimeSeries[] vois, SlicePlan sliceplan, int bgrep, int fgrep, StackRenderOptions options, int mousex, int mousey) {
        System.out.println("StackRenderer.render(...)");
        boolean do_init = true;
        boolean do_render_bg = true;
        boolean do_render_fg = true;
        boolean do_render_decorations = true;
        if (mRasterPipeline == null) {
            mRasterPipeline = new RasterPipeline();
        }
        if (mVolumeSampler == null) {
            mVolumeSampler = new VolumeSampler();
        }
        if (do_init) {
            mRasterPipeline.initImage(options.getWidth(), options.getHeight(), options.getBaseColor(), options.getRows(), options.getCols(), options.getBorderWidth(), 1.0f);
        }
        if (do_render_bg) {
            if (background_vol != null && background_vol instanceof FloatVolumeTimeSeries) {
                mRasterPipeline.renderBG(mVolumeSampler.sampleFloatVolume((FloatVolumeTimeSeries) background_vol, bgrep, sliceplan), options.getBGLut(), options.getShowSlices());
            } else if (background_vol != null && background_vol instanceof RGBVolumeTimeSeries) {
                mRasterPipeline.renderBG(mVolumeSampler.sampleRGBVolume((RGBVolumeTimeSeries) background_vol, bgrep, sliceplan), options.getShowSlices());
            } else if (background_vol != null && background_vol instanceof BinaryVolumeTimeSeries) {
                boolean[][][] booldata = mVolumeSampler.sampleBinaryVolume((BinaryVolumeTimeSeries) background_vol, 1, sliceplan);
                Color voicolor = Color.ORANGE;
                if (vois != null) {
                    for (int a = 0; a < vois.length; a++) {
                        if (background_vol.equals(vois[a])) voicolor = options.getVOIColor(a);
                    }
                }
                mRasterPipeline.renderBG(booldata, voicolor, options.getBGAlpha(), options.getShowSlices());
            } else {
                mRasterPipeline.renderBG(null, null, options.getShowSlices());
                mRasterPipeline.compositeAlpha(options.getBGAlpha(), options.getFGAlpha());
                return mRasterPipeline.getCompositeImage();
            }
            if (bg_mask_vol != null && bg_mask_vol instanceof BinaryVolumeTimeSeries) {
                boolean[][][] booldata = mVolumeSampler.sampleBinaryVolume((BinaryVolumeTimeSeries) bg_mask_vol, 1, sliceplan);
                mRasterPipeline.maskBuffer(booldata, options.getShowSlices(), options.getBGMaskOutside(), false);
            }
        }
        if (do_render_fg) {
            if (foreground_vol != null && foreground_vol instanceof FloatVolumeTimeSeries) {
                double[][][] data = mVolumeSampler.sampleFloatVolume((FloatVolumeTimeSeries) foreground_vol, fgrep, sliceplan);
                mRasterPipeline.renderFG(data, options.getFGLut(), options.getShowSlices());
            } else if (foreground_vol != null && foreground_vol instanceof BinaryVolumeTimeSeries) {
                boolean[][][] booldata = mVolumeSampler.sampleBinaryVolume((BinaryVolumeTimeSeries) foreground_vol, 1, sliceplan);
                Color voicolor = Color.ORANGE;
                if (vois != null) {
                    for (int a = 0; a < vois.length; a++) {
                        if (foreground_vol.equals(vois[a])) voicolor = options.getVOIColor(a);
                    }
                }
                mRasterPipeline.renderFG(booldata, voicolor, options.getFGAlpha(), options.getShowSlices());
            }
            if (fg_mask_vol != null && fg_mask_vol instanceof BinaryVolumeTimeSeries) {
                boolean[][][] booldata = mVolumeSampler.sampleBinaryVolume((BinaryVolumeTimeSeries) fg_mask_vol, 1, sliceplan);
                mRasterPipeline.maskBuffer(booldata, options.getShowSlices(), options.getFGMaskOutside(), false);
            }
        }
        if (do_render_decorations && vois != null && vois.length > 0) {
            mRasterPipeline.blankVOI();
            for (int a = 0; a < vois.length; a++) {
                if (options.getVOIDisplayFlag(a)) {
                    mRasterPipeline.renderVOI(mVolumeSampler.sampleBinaryVolume((BinaryVolumeTimeSeries) vois[a], 1, sliceplan), options.getVOIColor(a), options.getVOIAlpha(a), options.getShowSlices());
                }
            }
        }
        if (options.getBlendRule() == StackRenderOptions.BLEND_ALPHA) {
            mRasterPipeline.compositeAlpha(options.getBGAlpha(), options.getFGAlpha());
        } else if (options.getBlendRule() == StackRenderOptions.BLEND_BURN) {
            mRasterPipeline.compositeBurn(options.getBGAlpha(), options.getFGAlpha());
        }
        return mRasterPipeline.getCompositeImage();
    }

    public Point3D mapScreenCoordsToRealSpace(int x, int y, SlicePlan plan, int[] slices_to_show) {
        int pane = mRasterPipeline.getPaneForScreenCoords(x, y);
        int slice = slices_to_show[pane];
        int samplesx = plan.getSamplesX();
        int samplesy = plan.getSamplesY();
        int[] samplexy = mRasterPipeline.getSampleSpaceCoords(x, y, pane, samplesx, samplesy);
        Point3D retpt = new Point3D((double) samplexy[0], (double) samplexy[1], (double) slice);
        AffineTransform3D sampletoreal = plan.getSampleSpaceToRealSpaceXForm();
        sampletoreal.transform(retpt, retpt);
        System.out.println("mapScreenCoordsToRealSpace() point is: " + retpt);
        return retpt;
    }

    /**
     *   NOTE this method ASSUMES SlicePlan.NATIVE type
     *
     */
    public int[] mapScreenCoordsToVoxelSpace(int x, int y, FloatVolumeTimeSeries vol, int[] slices_to_show) {
        int pane = mRasterPipeline.getPaneForScreenCoords(x, y);
        int slice = slices_to_show[pane];
        int samplesx = vol.getData()[0][0].length;
        int samplesy = vol.getData()[0][0][0].length;
        int[] samplexy = mRasterPipeline.getSampleSpaceCoords(x, y, pane, samplesx, samplesy);
        System.out.println("mapScreenCoordsToVoxelSpace() samplexy is: " + samplexy[0] + ", " + samplexy[1]);
        int[] ret = new int[3];
        ret[0] = slice;
        ret[1] = samplexy[0];
        ret[2] = samplexy[1];
        return ret;
    }

    public int mapAreaToVOISpace(Area screen_area, BinaryVolumeTimeSeries voi, SlicePlan plan, int[] slices_to_show) {
        int pane = mRasterPipeline.getPaneForScreenArea(screen_area);
        int samplesx = -1;
        int samplesy = -1;
        if (plan.getType() == SlicePlan.NATIVE) {
            samplesx = voi.getBinaryData()[0][0].length;
            samplesy = voi.getBinaryData()[0][0].length;
        } else {
            samplesx = plan.getSamplesX();
            samplesy = plan.getSamplesY();
        }
        mRasterPipeline.transformAreaToVolumeSpace(screen_area, pane, samplesx, samplesy);
        int slice = slices_to_show[pane];
        return slice;
    }

    public BufferedImage pleaseWaitImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2.setColor(Color.RED);
        g2.drawString("Renderering - Please Wait!", image.getWidth() / 2 - 80, image.getHeight() / 2);
        return image;
    }

    public BufferedImage selectOptionsImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2.setColor(Color.RED);
        g2.drawString("2D Renderer - choose options", image.getWidth() / 2 - 80, image.getHeight() / 2);
        return image;
    }
}
