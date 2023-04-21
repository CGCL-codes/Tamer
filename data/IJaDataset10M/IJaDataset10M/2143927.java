package playground.sergioo.NetworksMatcher.kernel;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import playground.sergioo.NetworksMatcher.kernel.core.Region;

public class RectangularRegion implements Region {

    private final Coord downLeftCorner;

    private final double width;

    private final double height;

    public RectangularRegion(Coord downLeftCorner, double width, double height) {
        this.downLeftCorner = downLeftCorner;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isInside(Node node) {
        return node.getCoord().getX() > downLeftCorner.getX() && node.getCoord().getX() < downLeftCorner.getX() + width && node.getCoord().getY() > downLeftCorner.getY() && node.getCoord().getY() < downLeftCorner.getY() + height;
    }

    @Override
    public boolean isInside(Link link) {
        return link.getFromNode().getCoord().getX() > downLeftCorner.getX() && link.getFromNode().getCoord().getX() < downLeftCorner.getX() + width && link.getFromNode().getCoord().getY() > downLeftCorner.getY() && link.getFromNode().getCoord().getY() < downLeftCorner.getY() + height && link.getToNode().getCoord().getX() > downLeftCorner.getX() && link.getToNode().getCoord().getX() < downLeftCorner.getX() + width && link.getToNode().getCoord().getY() > downLeftCorner.getY() && link.getToNode().getCoord().getY() < downLeftCorner.getY() + height;
    }
}
