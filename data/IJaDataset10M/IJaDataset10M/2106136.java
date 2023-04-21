package net.sourceforge.theba.descriptors;

import net.sourceforge.theba.core.RegionDescriptor;
import net.sourceforge.theba.core.RegionMask;

public class CircularityDescriptor implements RegionDescriptor {

    public Object measure(RegionMask vmask) {
        short[] mask = new short[vmask.getWidth() * vmask.getHeight()];
        for (int x = 0; x < vmask.getWidth(); x++) for (int y = 0; y < vmask.getHeight(); y++) if (vmask.isSet(x, y, 0)) {
            mask[x + y * vmask.getWidth()] = 1;
        }
        return measure(mask, vmask.getWidth(), vmask.getHeight());
    }

    public double measure(short[] mask, int w, int h) {
        int count = 0;
        int area = 0;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int index = x + y * w;
                if (mask[index] != 0) {
                    area++;
                    if (x < w - 1 && mask[index + 1] == 0) {
                        count++;
                    } else if (x > 1 && mask[index - 1] == 0) {
                        count++;
                    } else if (y < h - 1 && mask[index + w] == 0) {
                        count++;
                    } else if (y > 0 && mask[index - w] == 0) {
                        count++;
                    }
                }
            }
        }
        if (area <= 0) {
            System.err.println("zero area: " + count + " " + area);
            return 0;
        }
        return (count * count) / (4 * Math.PI * area);
    }

    public String getName() {
        return "Circularity";
    }

    public String getAbout() {
        return null;
    }

    public boolean does3D() {
        return false;
    }

    public boolean isNumeric() {
        return true;
    }
}
