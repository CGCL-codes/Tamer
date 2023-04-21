package org.fao.waicent.xmap2D;

public final class WKSGeometry {

    private boolean __initialized;

    private WKSType __discriminator;

    private java.lang.Object __value;

    public WKSGeometry() {
        __initialized = false;
        __value = null;
    }

    public WKSType discriminator() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        return __discriminator;
    }

    public WKSPoint point() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSPointType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSPoint) __value;
    }

    public void point(WKSPoint value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSPointType);
        __value = value;
    }

    public void multi_point(WKSPoint[] value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSMultiPointType);
        __value = value;
    }

    public WKSPoint[] multi_point() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSMultiPointType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSPoint[]) __value;
    }

    public WKSPoint[] line_string() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSLineStringType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSPoint[]) __value;
    }

    public void line_string(WKSPoint[] value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSLineStringType);
        __value = value;
    }

    public WKSPoint[][] multi_line_string() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSMultiLineStringType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSPoint[][]) __value;
    }

    public void multi_line_string(WKSPoint[][] value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSMultiLineStringType);
        __value = value;
    }

    public WKSPoint[] linear_ring() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSLinearRingType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSPoint[]) __value;
    }

    public void linear_ring(WKSPoint[] value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSLinearRingType);
        __value = value;
    }

    public WKSLinearPolygon linear_polygon() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSLinearPolygonType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSLinearPolygon) __value;
    }

    public void linear_polygon(WKSLinearPolygon value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSLinearPolygonType);
        __value = value;
    }

    public WKSLinearPolygon[] multi_linear_polygon() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSMultiLinearPolygonType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSLinearPolygon[]) __value;
    }

    public void multi_linear_polygon(WKSLinearPolygon[] value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSMultiLinearPolygonType);
        __value = value;
    }

    public WKSGeometry[] collection() throws Exception {
        if (!__initialized) {
            throw new Exception("Bad Operation in" + this.toString());
        }
        switch(__discriminator.value()) {
            case WKSType._WKSCollectionType:
                break;
            default:
                throw new Exception("Bad Operation in" + this.toString());
        }
        return (WKSGeometry[]) __value;
    }

    public void collection(WKSGeometry[] value) {
        __initialized = true;
        __discriminator = (WKSType) (WKSType.WKSCollectionType);
        __value = value;
    }

    /**
     * Returns a string representation of the object.
     * This method is not part of the sf corba specs.
     * Add by TF.
     */
    public String toString() {
        return this.getClass().getName() + __value.toString();
    }
}
