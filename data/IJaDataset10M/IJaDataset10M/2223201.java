package iicm.utils3d;

/**
 * PickUtil - collection of simple picking functions
 * Copyright (c) 1997 IICM
 *
 * @author Michael Pichler
 * @version 1.0, changed: 21 Jul 97
 */
public abstract class PickUtil {

    private PickUtil() {
    }

    static final int X = Vec3f.X;

    static final int Y = Vec3f.Y;

    static final int Z = Vec3f.Z;

    /**
   * pick a cube from outside. See below.
   */
    public static boolean rayhitscube(Ray ray, Vec3f min, Vec3f max, Hitpoint hitpoint) {
        return rayhitscube(ray, min.value_, max.value_, hitpoint, false);
    }

    /**
   * pick a cube. test whether ray hits an axis-aligned cube given by (min, max)
   * at a hittime (strict) within near/far ranges of ray. In this case, hitpoint.hittime_
   * (and normal_ if non-null) is/are updated. The ray itself is not changed.
   * @param inside flag whether to pick the inner (on true) or outer side of the cube
   * @return flag whether a hit occured (hitpoint updated in this case)
   */
    public static boolean rayhitscube(Ray ray, float[] min, float[] max, Hitpoint hitpoint, boolean inside) {
        float[] A = ray.start_.value_;
        float[] b = ray.direction_.value_;
        float tnear = ray.near_;
        float tfar = ray.far_;
        float thit, hpx, hpy, hpz;
        if (b[X] != 0.0f) {
            thit = (((b[X] > 0) ^ inside ? min[X] : max[X]) - A[X]) / b[X];
            if (tnear < thit && thit < tfar) {
                hpy = A[Y] + thit * b[Y];
                hpz = A[Z] + thit * b[Z];
                if (min[Y] <= hpy && hpy <= max[Y] && min[Z] <= hpz && hpz <= max[Z]) {
                    hitpoint.hittime_ = thit;
                    if (hitpoint.normal_ != null) hitpoint.normal_.assign(b[X] > 0.0f ? -1.0f : 1.0f, 0.0f, 0.0f);
                    return true;
                }
            }
        }
        if (b[Y] != 0.0f) {
            thit = (((b[Y] > 0) ^ inside ? min[Y] : max[Y]) - A[Y]) / b[Y];
            if (tnear < thit && thit < tfar) {
                hpx = A[X] + thit * b[X];
                hpz = A[Z] + thit * b[Z];
                if (min[X] <= hpx && hpx <= max[X] && min[Z] <= hpz && hpz <= max[Z]) {
                    hitpoint.hittime_ = thit;
                    if (hitpoint.normal_ != null) hitpoint.normal_.assign(0.0f, b[Y] > 0.0f ? -1.0f : 1.0f, 0.0f);
                    return true;
                }
            }
        }
        if (b[Z] != 0.0f) {
            thit = (((b[Z] > 0) ^ inside ? min[Z] : max[Z]) - A[Z]) / b[Z];
            if (tnear < thit && thit < tfar) {
                hpx = A[X] + thit * b[X];
                hpy = A[Y] + thit * b[Y];
                if (min[X] <= hpx && hpx <= max[X] && min[Y] <= hpy && hpy <= max[Y]) {
                    hitpoint.hittime_ = thit;
                    if (hitpoint.normal_ != null) hitpoint.normal_.assign(0.0f, 0.0f, b[Z] > 0.0f ? -1.0f : 1.0f);
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * pick a sphere. test whether ray hits a sphere, centered at origin
   * with given radius; twosided if flag set.
   * @return flag whether a hit occured (hitpoint updated in this case)
   */
    public static boolean rayhitssphere(Ray ray, float radius, boolean twosided, Hitpoint hitpoint) {
        Vec3f start = ray.start_;
        Vec3f direction = ray.direction_;
        float a = Vec3f.dot(direction, direction);
        float b = Vec3f.dot(start, direction);
        float c = Vec3f.dot(start, start) - radius * radius;
        float d = b * b - a * c;
        if (a != 0 && d >= 0) {
            d = (float) Math.sqrt(d) / a;
            a = -b / a;
            float hit = a - d;
            if (ray.near_ < hit && hit < ray.far_) {
                hitpoint.hittime_ = hit;
                if (hitpoint.normal_ != null) hitpoint.normal_.rayat(start.value_, hit, direction.value_);
                return true;
            }
            if (!twosided) return false;
            hit = a + d;
            if (ray.near_ < hit && hit < ray.far_) {
                hitpoint.hittime_ = hit;
                if (hitpoint.normal_ != null) {
                    hitpoint.normal_.rayat(start.value_, hit, direction.value_);
                    hitpoint.normal_.negate();
                }
                return true;
            }
        }
        return false;
    }

    /**
   * pick a disk. test whether ray hits a disk parallel to the xz
   * plane at height y with radius r from top or bottom (according to
   * these flags).
   * if flag sens is true the radius is infinitely large
   * @return flag whether a hit occured (hitpoint updated in this case)
   * @see #rayhitscube
   */
    public static boolean rayhitsdisk(Ray ray, float y, float r, boolean top, boolean bottom, Hitpoint hitpoint, boolean sens) {
        float[] dir = ray.direction_.value_;
        if (dir[Y] == 0.0f) return false;
        if ((dir[Y] < 0 && !top) || (dir[Y] > 0 && !bottom)) return false;
        float[] start = ray.start_.value_;
        float thit = (y - start[Y]) / dir[Y];
        if (ray.near_ < thit && thit < ray.far_) {
            float hpx = start[X] + thit * dir[X];
            float hpz = start[Z] + thit * dir[Z];
            if (sens || (hpx * hpx + hpz * hpz <= r * r)) {
                hitpoint.hittime_ = thit;
                if (hitpoint.normal_ != null) hitpoint.normal_.assign(0.0f, (dir[Y] > 0) ? -1.0f : 1.0f, 0.0f);
                return true;
            }
        }
        return false;
    }

    /**
   * pick a cone's side. test whether ray hits the side wall of a cone
   * (given by height and radius). to pick a complete cone also check
   * bottom disk at y = -&nbsp;height/2.
   * @return flag whether a hit occured (hitpoint updated in this case)
   * @see #rayhitsdisk
   */
    public static boolean rayhitsconeside(Ray ray, float height, float radius, boolean twosided, Hitpoint hitpoint) {
        float[] start = ray.start_.value_;
        float[] dir = ray.direction_.value_;
        float r2 = radius * radius;
        float h2 = height / 2.0f;
        float a = dir[X] * dir[X] + dir[Z] * dir[Z] - r2 * dir[Y] * dir[Y] / (height * height);
        float alpha = radius / 2 - radius * start[Y] / height;
        float b = start[X] * dir[X] + start[Z] * dir[Z] + alpha * radius * dir[Y] / height;
        float c = start[X] * start[X] + start[Z] * start[Z] - alpha * alpha;
        float d = b * b - a * c;
        if (a != 0 && d >= 0) {
            d = (float) Math.sqrt(d) / a;
            a = -b / a;
            float hit = a - d;
            if (ray.near_ < hit && hit < ray.far_) {
                float hit_y = start[Y] + hit * dir[Y];
                if (-h2 < hit_y && hit_y < h2) {
                    hitpoint.hittime_ = hit;
                    if (hitpoint.normal_ != null) {
                        float ny = r2 * (0.5f / height - hit_y / (height * height));
                        if (ny != 0.0f) hitpoint.normal_.assign(start[X] + hit * dir[X], ny, start[Z] + hit * dir[Z]); else hitpoint.normal_.assign(0.0f, 1.0f, 0.0f);
                    }
                    return true;
                }
            }
            if (!twosided) return false;
            hit = a + d;
            if (ray.near_ < hit && hit < ray.far_) {
                float hit_y = start[Y] + hit * dir[Y];
                if (-h2 < hit_y && hit_y < h2) {
                    hitpoint.hittime_ = hit;
                    if (hitpoint.normal_ != null) {
                        float ny = -r2 * (0.5f / height - hit_y / (height * height));
                        if (ny != 0.0f) hitpoint.normal_.assign(-start[X] - hit * dir[X], ny, -start[Z] - hit * dir[Z]); else hitpoint.normal_.assign(0.0f, -1.0f, 0.0f);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * pick a cylinder's side. test whether ray hits a cylinder (given
   * by height and radius). to pick a complete cylinder also check top
   * and bottom disks at y = +/-&nbsp;height/2.
   * @return flag whether a hit occured (hitpoint updated in this case)
   * @see #rayhitsdisk
   */
    public static boolean rayhitscylinderside(Ray ray, float height, float radius, boolean twosided, Hitpoint hitpoint, boolean noheight) {
        float[] start = ray.start_.value_;
        float[] dir = ray.direction_.value_;
        float r2 = radius * radius;
        float h2 = height / 2.0f;
        float a = dir[X] * dir[X] + dir[Z] * dir[Z];
        float b = start[X] * dir[X] + start[Z] * dir[Z];
        float c = start[X] * start[X] + start[Z] * start[Z] - r2;
        float d = b * b - a * c;
        if (a != 0 && d >= 0) {
            d = (float) Math.sqrt(d) / a;
            a = -b / a;
            float hit = a - d;
            if (ray.near_ < hit && hit < ray.far_) {
                float hit_y = start[Y] + hit * dir[Y];
                if (noheight || (-h2 < hit_y && hit_y < h2)) {
                    hitpoint.hittime_ = hit;
                    if (hitpoint.normal_ != null) hitpoint.normal_.assign(start[X] + hit * dir[X], 0.0f, start[Z] + hit * dir[Z]);
                    return true;
                }
            }
            if (!twosided) return false;
            hit = a + d;
            if (ray.near_ < hit && hit < ray.far_) {
                float hit_y = start[Y] + hit * dir[Y];
                if (noheight || (-h2 < hit_y && hit_y < h2)) {
                    hitpoint.hittime_ = hit;
                    if (hitpoint.normal_ != null) hitpoint.normal_.assign(-start[X] - hit * dir[X], 0.0f, -start[Z] - hit * dir[Z]);
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * pick a plane. test whether ray hits the xy plane (z = 0) from
   * either side at a hittime > 0.
   * In this case, hitpoint.hittime_ (and normal_ if non-null) is/are
   * updated. The ray itself is not changed.
   * @return flag whether a hit occured (hitpoint updated in this case)
   */
    public static boolean rayhitsplane(Ray ray, Hitpoint hitpoint) {
        float[] A = ray.start_.value_;
        float[] b = ray.direction_.value_;
        float thit;
        if (b[Z] != 0.0f) {
            thit = -A[Z] / b[Z];
            if (thit > 0) {
                hitpoint.hittime_ = thit;
                if (hitpoint.normal_ != null) hitpoint.normal_.assign(0.0f, 0.0f, (b[Z] > 0.0f ? -1.0f : 1.0f));
                return true;
            }
        }
        return false;
    }
}
