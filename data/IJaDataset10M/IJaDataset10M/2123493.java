package net.tourbook.srtm;

import net.tourbook.util.UI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class RGBVertex implements Comparable<Object> {

    RGB rgb;

    long elev;

    public static void main(final String[] args) {
    }

    public RGBVertex() {
        elev = 0;
        rgb = new RGB(255, 255, 255);
    }

    public RGBVertex(final int red, final int green, final int blue, final long elev) {
        if ((red > 255) || (red < 0) || (green > 255) || (green < 0) || (blue > 255) || (blue < 0)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        this.elev = elev;
        rgb = new RGB(red, green, blue);
    }

    public RGBVertex(final RGB rgb) {
        elev = 0;
        this.rgb = rgb;
    }

    /**
	 * Make a close of the vertexSource
	 * 
	 * @param vertexSource
	 */
    public RGBVertex(final RGBVertex vertexSource) {
        final RGB sourceRGB = vertexSource.getRGB();
        rgb = new RGB(sourceRGB.red, sourceRGB.green, sourceRGB.blue);
        elev = vertexSource.elev;
    }

    public int compareTo(final Object anotherRGBVertex) throws ClassCastException {
        if (!(anotherRGBVertex instanceof RGBVertex)) throw new ClassCastException(Messages.rgv_vertex_class_cast_exception);
        final long anotherElev = ((RGBVertex) anotherRGBVertex).getElevation();
        if (this.elev < anotherElev) return (-1);
        if (this.elev > anotherElev) return 1;
        return 0;
    }

    public long getElevation() {
        return elev;
    }

    public RGB getRGB() {
        return rgb;
    }

    public void setElev(final long l) {
        elev = l;
    }

    public void setRGB(final RGB rgb) {
        this.rgb = rgb;
    }

    @Override
    public String toString() {
        return UI.EMPTY_STRING + elev + "," + rgb.red + "," + rgb.green + "," + rgb.blue + ";";
    }
}
