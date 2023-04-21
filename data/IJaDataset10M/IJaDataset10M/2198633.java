package com.bluebrim.base.shared.geom;

import com.bluebrim.base.shared.*;

/**
 * Abstract superclass for two-dimensional shapes (shapes that can be filled)
 * 
 * @author: Dennis Malmstr�m
 */
public abstract class CoBoundingShape extends CoShape implements CoBoundingShapeIF, CoGeometryConstants {

    public CoShapeIF createNewInstanceFrom(CoImmutableShapeIF s) {
        CoBoundingShape S = (CoBoundingShape) createNewInstance();
        S.setX(s.getX());
        S.setY(s.getY());
        S.setWidth(s.getWidth());
        S.setHeight(s.getHeight());
        return S;
    }

    protected CoReshapeHandleIF[] createReshapeHandles() {
        return new CoReshapeHandleIF[] { new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX();
            }

            public final double getY() {
                return CoBoundingShape.this.getY();
            }

            public final int getEdgeMask() {
                return ((getWidth() >= 0) ? LEFT_EDGE_MASK : RIGHT_EDGE_MASK) | ((getHeight() >= 0) ? TOP_EDGE_MASK : BOTTOM_EDGE_MASK);
            }

            public final void move(double dx, double dy) {
                double w = getWidth() - dx;
                double h = getHeight() - dy;
                if (m_keepAspectRatio) {
                    double sw = w / getWidth();
                    double sh = h / getHeight();
                    if ((!Double.isInfinite(sw)) && (!Double.isInfinite(sh))) {
                        if (sw < sh) {
                            w = getWidth() * sh;
                        } else {
                            h = getHeight() * sw;
                        }
                    }
                }
                dx = getWidth() - w;
                dy = getHeight() - h;
                setX(CoBoundingShape.this.getX() + dx);
                setWidth(w);
                setY(CoBoundingShape.this.getY() + dy);
                setHeight(h);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX() + getWidth() / 2;
            }

            public final double getY() {
                return CoBoundingShape.this.getY();
            }

            public final int getEdgeMask() {
                return (getHeight() >= 0) ? TOP_EDGE_MASK : BOTTOM_EDGE_MASK;
            }

            public final void move(double dx, double dy) {
                double w = getWidth();
                double h = getHeight();
                if (m_keepAspectRatio && (w != 0) && (h != 0)) {
                    double dw = (w * ((h - dy) / h)) - w;
                    setX(CoBoundingShape.this.getX() - dw / 2);
                    setWidth(w + dw);
                }
                setY(CoBoundingShape.this.getY() + dy);
                setHeight(h - dy);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX() + getWidth();
            }

            public final double getY() {
                return CoBoundingShape.this.getY();
            }

            public final int getEdgeMask() {
                return ((getWidth() >= 0) ? RIGHT_EDGE_MASK : LEFT_EDGE_MASK) | ((getHeight() >= 0) ? TOP_EDGE_MASK : BOTTOM_EDGE_MASK);
            }

            public final void move(double dx, double dy) {
                double w = getWidth() + dx;
                double h = getHeight() - dy;
                if (m_keepAspectRatio) {
                    double sw = w / getWidth();
                    double sh = h / getHeight();
                    if ((!Double.isInfinite(sw)) && (!Double.isInfinite(sh))) {
                        if (sw < sh) {
                            w = getWidth() * sh;
                        } else {
                            h = getHeight() * sw;
                        }
                    }
                }
                dy = getHeight() - h;
                setWidth(w);
                setY(CoBoundingShape.this.getY() + dy);
                setHeight(h);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX() + getWidth();
            }

            public final double getY() {
                return CoBoundingShape.this.getY() + getHeight() / 2;
            }

            public final int getEdgeMask() {
                return (getWidth() >= 0) ? RIGHT_EDGE_MASK : LEFT_EDGE_MASK;
            }

            public final void move(double dx, double dy) {
                double w = getWidth();
                double h = getHeight();
                if (m_keepAspectRatio && (w != 0) && (h != 0)) {
                    double dh = (h * ((w + dx) / w)) - h;
                    setY(CoBoundingShape.this.getY() - dh / 2);
                    setHeight(h + dh);
                }
                setWidth(w + dx);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX() + getWidth();
            }

            public final double getY() {
                return CoBoundingShape.this.getY() + getHeight();
            }

            public final int getEdgeMask() {
                return ((getWidth() >= 0) ? RIGHT_EDGE_MASK : LEFT_EDGE_MASK) | ((getHeight() >= 0) ? BOTTOM_EDGE_MASK : TOP_EDGE_MASK);
            }

            public final void move(double dx, double dy) {
                double w = getWidth() + dx;
                double h = getHeight() + dy;
                if (m_keepAspectRatio) {
                    double sw = w / getWidth();
                    double sh = h / getHeight();
                    if ((!Double.isInfinite(sw)) && (!Double.isInfinite(sh))) {
                        if (sw < sh) {
                            w = getWidth() * sh;
                        } else {
                            h = getHeight() * sw;
                        }
                    }
                }
                setWidth(w);
                setHeight(h);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX() + getWidth() / 2;
            }

            public final double getY() {
                return CoBoundingShape.this.getY() + getHeight();
            }

            public final int getEdgeMask() {
                return (getHeight() >= 0) ? BOTTOM_EDGE_MASK : TOP_EDGE_MASK;
            }

            public final void move(double dx, double dy) {
                double w = getWidth();
                double h = getHeight();
                if (m_keepAspectRatio && (w != 0) && (h != 0)) {
                    double dw = (w * ((h + dy) / h)) - w;
                    setX(CoBoundingShape.this.getX() - dw / 2);
                    setWidth(w + dw);
                }
                setHeight(h + dy);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX();
            }

            public final double getY() {
                return CoBoundingShape.this.getY() + getHeight();
            }

            public final int getEdgeMask() {
                return ((getWidth() >= 0) ? LEFT_EDGE_MASK : RIGHT_EDGE_MASK) | ((getHeight() >= 0) ? BOTTOM_EDGE_MASK : TOP_EDGE_MASK);
            }

            public final void move(double dx, double dy) {
                double w = getWidth() - dx;
                double h = getHeight() + dy;
                if (m_keepAspectRatio) {
                    double sw = w / getWidth();
                    double sh = h / getHeight();
                    if ((!Double.isInfinite(sw)) && (!Double.isInfinite(sh))) {
                        if (sw < sh) {
                            w = getWidth() * sh;
                        } else {
                            h = getHeight() * sw;
                        }
                    }
                }
                dx = getWidth() - w;
                setX(CoBoundingShape.this.getX() + dx);
                setWidth(w);
                setHeight(h);
            }
        }, new CoReshapeHandleIF() {

            public final double getX() {
                return CoBoundingShape.this.getX();
            }

            public final double getY() {
                return CoBoundingShape.this.getY() + getHeight() / 2;
            }

            public final int getEdgeMask() {
                return (getWidth() >= 0) ? LEFT_EDGE_MASK : RIGHT_EDGE_MASK;
            }

            public final void move(double dx, double dy) {
                double w = getWidth();
                double h = getHeight();
                if (m_keepAspectRatio && (w != 0) && (h != 0)) {
                    double dh = (h * ((w - dx) / w)) - h;
                    setY(CoBoundingShape.this.getY() - dh / 2);
                    setHeight(h + dh);
                }
                setX(CoBoundingShape.this.getX() + dx);
                setWidth(getWidth() - dx);
            }
        } };
    }

    public boolean isClosedShape() {
        return true;
    }

    public static final String XML_HEIGHT = "height";

    public static final String XML_WIDTH = "width";

    public static final String XML_X = "x";

    public static final String XML_Y = "y";
}
