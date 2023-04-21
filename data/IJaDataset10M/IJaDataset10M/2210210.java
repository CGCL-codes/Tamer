package zhenyu.pan.voronoi.core;

import javax.vecmath.Point2f;

public class EL extends LinkedList<EL.HalfEdge> {

    static class HalfEdge extends LinkedList.LinkedListNode<EL.HalfEdge> {

        final Edge edge;

        final int pm;

        Site vertex;

        float ystar;

        HalfEdge(final Edge edge, final int pm) {
            this.edge = edge;
            this.pm = pm;
        }
    }

    private HalfEdge hash[];

    private float xmin;

    private float deltax;

    boolean initialize(final int size, final float xmin, final float deltax) {
        this.xmin = xmin;
        this.deltax = deltax;
        this.hash = new HalfEdge[size];
        super.initialize(new HalfEdge(null, 0), new HalfEdge(null, 0));
        this.hash[0] = (HalfEdge) this.first();
        this.hash[this.hash.length - 1] = (HalfEdge) this.last();
        return true;
    }

    private HalfEdge getHash(final int b) {
        HalfEdge he;
        if (b < 0 || b >= this.hash.length) {
            return null;
        }
        he = this.hash[b];
        if (he == null || !this.isDeleted(he)) {
            return he;
        }
        this.hash[b] = null;
        return null;
    }

    HalfEdge leftbnd(final Point2f p) {
        int bucket;
        HalfEdge he;
        bucket = (int) ((p.x - this.xmin) / this.deltax * this.hash.length);
        if (bucket < 0) {
            bucket = 0;
        }
        if (bucket >= this.hash.length) {
            bucket = this.hash.length - 1;
        }
        he = this.getHash(bucket);
        if (he == null) {
            for (int i = 1; i < this.hash.length; i += 1) {
                if ((he = this.getHash(bucket - i)) != null) {
                    break;
                }
                if ((he = this.getHash(bucket + i)) != null) {
                    break;
                }
            }
        }
        if (he == this.first() || he != this.last() && EL.isRightOf(he, p)) {
            do {
                he = this.next(he);
            } while (he != this.last() && EL.isRightOf(he, p));
            he = this.prev(he);
        } else {
            do {
                he = this.prev(he);
            } while (he != this.first() && !EL.isRightOf(he, p));
        }
        if (bucket > 0 && bucket < this.hash.length - 1) {
            this.hash[bucket] = he;
        }
        return he;
    }

    private static boolean isRightOf(final HalfEdge halfEdge, final Point2f point) {
        final Edge edge = halfEdge.edge;
        final Site topSite = edge.sites[1];
        final Site bottomSite = edge.sites[0];
        final boolean right_of_site = point.x > topSite.x;
        if (right_of_site && halfEdge.pm == Voronoi.LE) {
            return true;
        }
        if (!right_of_site && halfEdge.pm == Voronoi.RE) {
            return false;
        }
        boolean above;
        if (edge.isYStepBigger()) {
            final float dyp = point.y - topSite.y;
            final float dxp = point.x - topSite.x;
            final boolean fast;
            if (!right_of_site & edge.xStep < 0.0f | right_of_site & edge.xStep >= 0.0f) {
                above = dyp >= edge.xStep * dxp;
                fast = above;
            } else {
                above = point.x + point.y * edge.xStep > edge.offset;
                if (edge.xStep < 0.0f) {
                    above = !above;
                }
                fast = !above;
            }
            if (!fast) {
                final float dSitesX = topSite.x - bottomSite.x;
                above = edge.xStep * (dxp * dxp - dyp * dyp) < dSitesX * dyp * (1.0f + 2.0f * dxp / dSitesX + edge.xStep * edge.xStep);
                if (edge.xStep < 0.0f) {
                    above = !above;
                }
            }
        } else {
            final float yEdge = edge.calcY(point.x);
            final float yDiff = point.y - yEdge;
            final float topSiteDiffX = point.x - topSite.x;
            final float topSiteDiffY = yEdge - topSite.y;
            above = yDiff * yDiff > topSiteDiffX * topSiteDiffX + topSiteDiffY * topSiteDiffY;
        }
        return halfEdge.pm == Voronoi.LE ? above : !above;
    }
}
