package net.sf.cplab.gazer;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.cplab.core.util.ImageTrialTimePool;

/**
 * @author jtse
 *
 */
public class FixationPixelColorFactory {

    private static final Log LOG = LogFactory.getLog(FixationPixelColorFactory.class);

    private static final int OUT_OF_SPACE = -1;

    private static final int OUT_OF_TIME = -2;

    private ImageTrialTimePool factory;

    public FixationPixelColorFactory(ImageTrialTimePool factory) {
        this.factory = factory;
    }

    public List<FixationPixelColor> getInstances(List<Fixation> fixations) {
        ArrayList<FixationPixelColor> list = new ArrayList<FixationPixelColor>(fixations.size());
        for (Fixation fixation : fixations) {
            try {
                BufferedImage image;
                try {
                    image = factory.borrowImage(fixation.trialParameters, fixation.trialTime);
                } catch (NoSuchElementException e) {
                    throw e;
                } catch (Exception e) {
                    LOG.error(e, e);
                    throw new RuntimeException("Could not borrow image " + fixation.trialParameters + " at time " + fixation.trialTime);
                }
                FixationPixelColor fpc = null;
                try {
                    fpc = getInstance(fixation, image);
                } catch (RuntimeException e) {
                    LOG.error(e, e);
                    throw new RuntimeException("Error while getInstance(). See log.");
                }
                try {
                    factory.returnImage(fixation.trialParameters, fixation.trialTime, image);
                } catch (Exception e) {
                    LOG.error(e, e);
                    throw new RuntimeException("Could not return image " + fixation.trialParameters + " at time " + fixation.trialTime);
                }
                list.add(fpc);
            } catch (NoSuchElementException e) {
                LOG.warn(new StringBuilder("While on fixation {").append(fixation).append("}"));
                LOG.warn(e, e);
                FixationPixelColor fpc = new FixationPixelColor();
                try {
                    BeanUtils.copyProperties(fpc, fixation);
                } catch (IllegalAccessException e1) {
                    throw new RuntimeException(e1);
                } catch (InvocationTargetException e1) {
                    throw new RuntimeException(e);
                }
                fpc.setRed(OUT_OF_TIME);
                fpc.setGreen(OUT_OF_TIME);
                fpc.setBlue(OUT_OF_TIME);
                fpc.setAlpha(OUT_OF_TIME);
                list.add(fpc);
            }
        }
        return list;
    }

    public FixationPixelColor getInstance(Fixation fixation, BufferedImage image) {
        int x = (int) Math.round(fixation.x);
        int y = (int) Math.round(fixation.y);
        int[] color = null;
        if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            color = image.getRaster().getPixel(x, y, (int[]) null);
        } else {
            color = new int[4];
            color[0] = OUT_OF_SPACE;
            color[1] = OUT_OF_SPACE;
            color[2] = OUT_OF_SPACE;
            color[3] = OUT_OF_SPACE;
        }
        FixationPixelColor fpc = new FixationPixelColor();
        try {
            BeanUtils.copyProperties(fpc, fixation);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        fpc.red = color[0];
        fpc.green = color[1];
        fpc.blue = color[2];
        if (color.length == 4) {
            fpc.alpha = color[3];
        } else {
            fpc.alpha = 255;
        }
        return fpc;
    }
}
