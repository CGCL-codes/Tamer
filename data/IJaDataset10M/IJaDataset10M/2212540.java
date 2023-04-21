package org.apache.lucene.spatial.tier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;
import org.apache.lucene.search.Filter;
import org.apache.lucene.spatial.geometry.shape.Rectangle;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;

/**
 *
 */
public class CartesianPolyFilter {

    private IProjector projector = new SinusoidalProjector();

    private Logger log = Logger.getLogger(getClass().getName());

    public Shape getBoxShape(double latitude, double longitude, int miles) {
        Rectangle box = DistanceUtils.getInstance().getBoundary(latitude, longitude, miles);
        double latY = box.getMaxPoint().getY();
        double latX = box.getMinPoint().getY();
        double longY = box.getMaxPoint().getX();
        double longX = box.getMinPoint().getX();
        CartesianTierPlotter ctp = new CartesianTierPlotter(2, projector);
        int bestFit = ctp.bestFit(miles);
        log.info("Best Fit is : " + bestFit);
        ctp = new CartesianTierPlotter(bestFit, projector);
        Shape shape = new Shape(ctp.getTierFieldName());
        double beginAt = ctp.getTierBoxId(latX, longX);
        double endAt = ctp.getTierBoxId(latY, longY);
        double tierVert = ctp.getTierVerticalPosDivider();
        log.fine(" | " + beginAt + " | " + endAt);
        double startX = beginAt - (beginAt % 1);
        double startY = beginAt - startX;
        double endX = endAt - (endAt % 1);
        double endY = endAt - endX;
        int scale = (int) Math.log10(tierVert);
        endY = new BigDecimal(endY).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
        startY = new BigDecimal(startY).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
        log.fine("scale " + scale + " startX " + startX + " endX " + endX + " startY " + startY + " endY " + endY + " tierVert " + tierVert);
        double xInc = 1.0d / tierVert;
        xInc = new BigDecimal(xInc).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
        for (; startX <= endX; startX++) {
            double itY = startY;
            while (itY <= endY) {
                double boxId = startX + itY;
                shape.addBox(boxId);
                itY += xInc;
                itY = new BigDecimal(itY).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
            }
        }
        return shape;
    }

    public Filter getBoundingArea(double latitude, double longitude, int miles) {
        Shape shape = getBoxShape(latitude, longitude, miles);
        return new CartesianShapeFilter(shape, shape.getTierId());
    }
}
