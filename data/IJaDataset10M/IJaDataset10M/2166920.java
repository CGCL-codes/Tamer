package org.kalypso.nofdpidss.report.map;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.kalypso.contribs.eclipse.core.resources.ResourceUtilities;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.core.KalypsoCorePlugin;
import org.kalypso.ogc.gml.GisTemplateHelper;
import org.kalypso.ogc.gml.GisTemplateMapModell;
import org.kalypso.ogc.gml.IKalypsoTheme;
import org.kalypso.ogc.gml.mapmodel.MapModellHelper;
import org.kalypso.template.gismapview.Gismapview;
import org.kalypsodeegree.KalypsoDeegreePlugin;
import org.kalypsodeegree.model.geometry.GM_Envelope;

/**
 * This class creates an image of a map template.
 *
 * @author Holger Albert
 */
public class MapImageMaker {

    /**
   * This function will calculate the width and height for the image, using the maxX and maxY values. It tries to keep
   * the right relation.
   *
   * @param extend
   *          The extend, with which the relation is calculated.
   * @param maxX
   *          The maximum width the image is allowed to have.
   * @param maxY
   *          The maximum height the image is allowed to have. The height is the value that is adjusted. If it must be
   *          smaller, everthing should be ok. If it must be larger, then it is increased until the maximum is reached
   *          (then it is not guaranteed, that the relation will be ok).
   */
    public static Rectangle calculateRelation(final GM_Envelope extend, final int maxX, final int maxY) {
        final double x1 = extend.getMin().getX();
        final double y1 = extend.getMin().getY();
        final double x2 = extend.getMax().getX();
        final double y2 = extend.getMax().getY();
        double x = x2 - x1;
        double y = y2 - y1;
        if (x < 0) x = -1 * x;
        if (y < 0) y = -1 * y;
        final double relation = y / x;
        final double width = maxX;
        double height = width * relation;
        if (height > maxY) height = maxY;
        return new Rectangle((int) width, (int) height);
    }

    /**
   * This function creates an image out of a map template with the given extend.
   *
   * @param template
   *          The map template, from which the image should be taken.
   * @param extent
   *          The extend of the map.
   * @param destination
   *          The destination file. To it the image will be copied. If it does not exist, it will be created.
   * @param width
   *          The width in pixel, the image should use.
   * @param height
   *          The height in pixel, the image should use.
   */
    public static void createImage(final IFile template, GM_Envelope extent, final File destination, final int width, final int height) throws Exception {
        System.gc();
        FileOutputStream fos = null;
        GisTemplateMapModell newMapModell = null;
        try {
            final Gismapview gisview = GisTemplateHelper.loadGisMapView(template);
            final URL context = ResourceUtilities.createURL(template);
            final IProject project = template.getProject();
            newMapModell = new GisTemplateMapModell(context, KalypsoDeegreePlugin.getDefault().getCoordinateSystem(), project, KalypsoCorePlugin.getDefault().getSelectionManager());
            newMapModell.createFromTemplate(gisview);
            extent = MapModellHelper.adjustBoundingBox(newMapModell, extent, (double) height / (double) width);
            final IKalypsoTheme[] allThemes = newMapModell.getAllThemes();
            int timeout = 0;
            boolean isLoading = true;
            while (isLoading) {
                isLoading = false;
                for (final IKalypsoTheme theme : allThemes) if (theme.isLoaded() == false) isLoading = true;
                if (timeout >= 60000) break;
                if (isLoading) {
                    Thread.sleep(1000);
                    timeout = timeout + 1000;
                }
            }
            final Rectangle bounds = new Rectangle(width, height);
            if (extent == null) extent = GisTemplateHelper.getBoundingBox(gisview);
            final BufferedImage image = MapModellHelper.createImageFromModell(extent, bounds, width, height, newMapModell);
            fos = new FileOutputStream(destination);
            final boolean result = ImageIO.write(image, "png", fos);
            if (!result) throw new CoreException(StatusUtilities.createErrorStatus("Wrong image type ..."));
        } finally {
            IOUtils.closeQuietly(fos);
            newMapModell.dispose();
        }
    }

    /**
   * The constructor.
   */
    private MapImageMaker() {
    }
}
