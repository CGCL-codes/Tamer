package net.sourceforge.theba.descriptors;

import net.sourceforge.theba.core.RegionDescriptor;
import net.sourceforge.theba.core.RegionMask;

public class EccentricityDescriptor implements RegionDescriptor {

    public Object measure(RegionMask vmask) {
        short[] mask = new short[vmask.getWidth() * vmask.getHeight()];
        for (int x = 0; x < vmask.getWidth(); x++) for (int y = 0; y < vmask.getHeight(); y++) if (vmask.isSet(x, y, 0)) {
            mask[x + y * vmask.getWidth()] = 1;
        }
        return measure(mask, vmask.getWidth(), vmask.getHeight());
    }

    public double measure(short[] pixels, int width, int height) {
        double m_0_0 = 0, m_1_0 = 0, m_0_1 = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x + y * width;
                if (pixels[index] != 0) {
                    m_0_0 += 1;
                    m_1_0 += x;
                    m_0_1 += y;
                }
            }
        }
        double x_c = m_1_0 / m_0_0;
        double y_c = m_0_1 / m_0_0;
        double m_0_2 = 0, m_2_0 = 0, m_1_1 = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = x + y * width;
                if (pixels[index] != 0) {
                    m_1_1 += (x - x_c) * (y - y_c);
                    m_2_0 += (x - x_c) * (x - x_c);
                    m_0_2 += (y - y_c) * (y - y_c);
                }
            }
        }
        double factor = m_2_0 + m_0_2 + Math.sqrt((m_2_0 - m_0_2) * (m_2_0 - m_0_2) + 4 * m_1_1 * m_1_1);
        double dividend = m_2_0 + m_0_2 - Math.sqrt((m_2_0 - m_0_2) * (m_2_0 - m_0_2) + 4 * m_1_1 * m_1_1);
        return factor / dividend;
    }

    public String getName() {
        return "Eccentricity";
    }

    public String getAbout() {
        return "Returns the eccentricity of a single 2D region";
    }

    public boolean does3D() {
        return false;
    }

    public boolean isNumeric() {
        return true;
    }
}
