package mindtct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mindtct.Contour.ContourSearchResult;
import mindtct.Imgutil.SearchInDirectionResult;
import mindtct.Lfs.LFSPARMS;
import mindtct.Lfs.MinutiaType;
import mindtct.Lfs.RotationalScanDirection;
import mindtct.Loop.OnHookResult;
import mindtct.Loop.OnIslandOrLakeResult;
import mindtct.Loop.OnIslandOrLakeResultStatus;
import mindtct.Loop.OnLoopResult;
import mindtct.Util.MinMaxResult;
import mindtct.Util.MinMaxType;

/***********************************************************************
      LIBRARY: LFS - NIST Latent Fingerprint System

      FILE:    REMOVE.C
      AUTHOR:  Michael D. Garris
      DATE:    08/02/1999
      UPDATED: 10/04/1999 Version 2 by MDG
      UPDATED: 03/16/2005 by MDG

      Contains routines responsible for detecting and removing false
      minutiae as part of the NIST Latent Fingerprint System (LFS).

***********************************************************************
               ROUTINES:
                        remove_false_minutia()
                        remove_false_minutia_V2()
                        remove_holes()
                        remove_hooks()
                        remove_hooks_islands_overlaps()
                        remove_islands_and_lakes()
                        remove_malformations()
                        remove_near_invblocks()
                        remove_near_invblocks_V2()
                        remove_pointing_invblock()
                        remove_pointing_invblock_V2()
                        remove_overlaps()
                        remove_pores()
                        remove_pores_V2()
                        remove_or_adjust_side_minutiae()
                        remove_or_adjust_side_minutiae_V2()


 * TODO OPT replace get(i) invocations with stored reference
 * 
 * 
 * @author mchaberski
 *
 */
public class Remove {

    private static final Log log = LogFactory.getLog(Remove.class);

    /*************************************************************************
	**************************************************************************
	#cat: remove_false_minutia_V2 - Takes a list of true and false minutiae and
	#cat:                attempts to detect and remove the false minutiae based
	#cat:                on a series of tests.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      direction_map  - map of image blocks containing directional ridge flow
	      low_flow_map   - map of image blocks flagged as LOW RIDGE FLOW
	      high_curve_map - map of image blocks flagged as HIGH CURVATURE
	      mw        - width in blocks of the maps
	      mh        - height in blocks of the maps
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_false_minutia_V2(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, int[] direction_map, int[] low_flow_map, int[] high_curve_map, final int mw, final int mh, final LFSPARMS lfsparms) {
        MinutiaUtility.sort_minutiae_y_x(minutiae, iw, ih);
        remove_islands_and_lakes(minutiae, bdata, iw, ih, lfsparms);
        remove_holes(minutiae, bdata, iw, ih, lfsparms);
        remove_pointing_invblock_V2(minutiae, direction_map, mw, mh, lfsparms);
        remove_near_invblock_V2(minutiae, direction_map, mw, mh, lfsparms);
        remove_or_adjust_side_minutiae_V2(minutiae, bdata, iw, ih, direction_map, mw, mh, lfsparms);
        remove_hooks(minutiae, bdata, iw, ih, lfsparms);
        remove_overlaps(minutiae, bdata, iw, ih, lfsparms);
        remove_malformations(minutiae, bdata, iw, ih, low_flow_map, mw, mh, lfsparms);
        remove_pores_V2(minutiae, bdata, iw, ih, direction_map, low_flow_map, high_curve_map, mw, mh, lfsparms);
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_holes - Removes minutia points on small loops around valleys.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_holes(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, final LFSPARMS lfsparms) {
        int i = 0;
        while (i < minutiae.getNum()) {
            MINUTIA minutia = minutiae.get(i);
            if (minutia.type == MinutiaType.BIFURCATION) {
                OnLoopResult ret = Loop.on_loop(minutia, lfsparms.small_loop_len, bdata, iw, ih);
                if ((ret == OnLoopResult.LOOP_FOUND) || (ret == OnLoopResult.IGNORE)) {
                    log.debug("removing minutia at index i = " + i);
                    minutiae.remove(i);
                } else if (ret == OnLoopResult.FALSE) {
                    i++;
                } else {
                    throw new LookingForLoopException();
                }
            } else {
                i++;
            }
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_hooks - Takes a list of true and false minutiae and
	#cat:                attempts to detect and remove those false minutiae that
	#cat:                are on a hook (white or black).

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_hooks(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, final LFSPARMS lfsparms) {
        boolean[] to_remove = new boolean[minutiae.getNum()];
        final int full_ndirs = lfsparms.num_directions * 2;
        final int qtr_ndirs = lfsparms.num_directions / 4;
        final int min_deltadir = (3 * qtr_ndirs) - 1;
        int f = 0;
        while (f < minutiae.getNum() - 1) {
            if (to_remove[f] == false) {
                MINUTIA minutia1 = minutiae.get(f);
                int s = f + 1;
                while (s < minutiae.getNum()) {
                    MINUTIA minutia2 = minutiae.get(s);
                    final boolean minutia1PixelValue = bdata[minutia1.y * iw + minutia1.x];
                    if (MinutiaType.fromPixelValue(minutia1PixelValue) != minutia1.type) {
                        break;
                    }
                    final boolean minutia2PixelValue = bdata[minutia2.y * iw + minutia2.x];
                    if (MinutiaType.fromPixelValue(minutia2PixelValue) != minutia2.type) {
                        to_remove[s] = true;
                    }
                    if (!to_remove[s]) {
                        final int delta_y = minutia2.y - minutia1.y;
                        if (delta_y <= lfsparms.max_rmtest_dist) {
                            final double dist = Util.distance(minutia1.x, minutia1.y, minutia2.x, minutia2.y);
                            if (dist <= lfsparms.max_rmtest_dist) {
                                int deltadir;
                                if ((deltadir = Util.closest_dir_dist(minutia1.direction, minutia2.direction, full_ndirs)) == Lfs.INVALID_DIR) {
                                    throw new InvalidDirectionException(MindtctErrorCodes.ERR_INVALID_DIRECTION_IN_HOOK_REMOVAL);
                                }
                                if (deltadir > min_deltadir) {
                                    if (minutia1.type != minutia2.type) {
                                        OnHookResult ret = Loop.on_hook(minutia1, minutia2, lfsparms.max_hook_len, bdata, iw, ih);
                                        if (ret == OnHookResult.HOOK_FOUND) {
                                            to_remove[f] = true;
                                            to_remove[s] = true;
                                        } else if (ret == OnHookResult.IGNORE) {
                                            to_remove[f] = true;
                                            break;
                                        } else if (ret == OnHookResult.FALSE) {
                                        } else {
                                            throw new RuntimeException("unexpected OnHookResult: possibly null; is null? " + String.valueOf(ret == null));
                                        }
                                    } else {
                                    }
                                } else {
                                }
                            } else {
                            }
                        } else {
                            break;
                        }
                    } else {
                    }
                    s++;
                }
            }
            f++;
        }
        for (int i = minutiae.getNum() - 1; i >= 0; i--) {
            if (to_remove[i]) {
                log.debug("removing minutia at index i = " + i);
                minutiae.remove(i);
            }
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_islands_and_lakes - Takes a list of true and false minutiae and
	#cat:                attempts to detect and remove those false minutiae that
	#cat:                are either on a common island (filled with black pixels)
	#cat:                or a lake (filled with white pixels).
	#cat:                Note that this routine edits the binary image by filling
	#cat:                detected lakes or islands.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_islands_and_lakes(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, final LFSPARMS lfsparms) {
        final int dist_thresh = lfsparms.max_rmtest_dist;
        final int half_loop = lfsparms.max_half_loop;
        boolean[] to_remove = new boolean[minutiae.getNum()];
        final int full_ndirs = lfsparms.num_directions << 1;
        final int qtr_ndirs = lfsparms.num_directions >> 2;
        final int min_deltadir = (3 * qtr_ndirs) - 1;
        int f = 0;
        while (f < minutiae.getNum() - 1) {
            if (!to_remove[f]) {
                MINUTIA minutia1 = minutiae.get(f);
                int s = f + 1;
                while (s < minutiae.getNum()) {
                    MINUTIA minutia2 = minutiae.get(s);
                    if (minutia2.type == minutia1.type) {
                        if (MinutiaType.fromPixelValue(bdata[minutia1.y * iw + minutia1.x]) != minutia1.type) {
                            break;
                        }
                        if (MinutiaType.fromPixelValue(bdata[minutia2.y * iw + minutia2.x]) != minutia2.type) {
                            to_remove[s] = true;
                        }
                        if (!to_remove[s]) {
                            final int delta_y = minutia2.y - minutia1.y;
                            if (delta_y <= dist_thresh) {
                                final double dist = Util.distance(minutia1.x, minutia1.y, minutia2.x, minutia2.y);
                                if (dist <= dist_thresh) {
                                    int deltadir;
                                    if ((deltadir = Util.closest_dir_dist(minutia1.direction, minutia2.direction, full_ndirs)) == Lfs.INVALID_DIR) {
                                        throw new InvalidDirectionException(-611);
                                    }
                                    if (deltadir > min_deltadir) {
                                        OnIslandOrLakeResult ret = Loop.on_island_lake(minutia1, minutia2, half_loop, bdata, iw, ih);
                                        final int[] loop_x = ret.contour_x, loop_y = ret.contour_y;
                                        final int[] loop_ex = ret.contour_ex, loop_ey = ret.contour_ey;
                                        final int nloop = ret.ncontour;
                                        if (ret.status == OnIslandOrLakeResultStatus.LOOP_FOUND) {
                                            Loop.fill_loop(loop_x, loop_y, nloop, bdata, iw, ih);
                                            to_remove[f] = true;
                                            to_remove[s] = true;
                                        } else if (ret.status == OnIslandOrLakeResultStatus.IGNORE) {
                                            to_remove[f] = true;
                                            break;
                                        } else {
                                        }
                                    } else {
                                    }
                                } else {
                                }
                            } else {
                                break;
                            }
                        } else {
                        }
                    }
                    s++;
                }
            }
            f++;
        }
        for (int i = minutiae.getNum() - 1; i >= 0; i--) {
            if (to_remove[i]) {
                log.debug("removing minutia at index i = " + i);
                minutiae.remove(i);
            }
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_malformations - Attempts to detect and remove minutia points
	#cat:            that are "irregularly" shaped.  Irregularity is measured
	#cat:            by measuring across the interior of the feature at
	#cat:            two progressive points down the feature's contour.  The
	#cat:            test is triggered if a pixel of opposite color from the
	#cat:            feture's type is found.  The ratio of the distances across
	#cat:            the feature at the two points is computed and if the ratio
	#cat:            is too large then the minutia is determined to be malformed.
	#cat:            A cursory test is conducted prior to the general tests in
	#cat:            the event that the minutia lies in a block with LOW RIDGE
	#cat:            FLOW.  In this case, the distance across the feature at
	#cat:            the second progressive contour point is measured and if
	#cat:            too large, the point is determined to be malformed.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      low_flow_map   - map of image blocks flagged as LOW RIDGE FLOW
	      mw        - width in blocks of the map
	      mh        - height in blocks of the map
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_malformations(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, int[] low_flow_map, final int mw, final int mh, final LFSPARMS lfsparms) {
        for (int i = minutiae.getNum() - 1; i >= 0; i--) {
            MINUTIA minutia = minutiae.get(i);
            ContourSearchResult ret1 = Contour.trace_contour(lfsparms.malformation_steps_2, minutia.x, minutia.y, minutia.x, minutia.y, minutia.ex, minutia.ey, RotationalScanDirection.SCAN_COUNTERCLOCKWISE, bdata, iw, ih);
            if ((ret1.status == ContourSearchResult.Status.IGNORE) || (ret1.status == ContourSearchResult.Status.LOOP_FOUND) || (ret1.ncontour < lfsparms.malformation_steps_2)) {
                if ((ret1.status == ContourSearchResult.Status.LOOP_FOUND) || (ret1.ncontour < lfsparms.malformation_steps_2)) {
                }
                log.debug("removing minutia at index i = " + i);
                minutiae.remove(i);
            } else {
                final int ax1 = ret1.contour_x[lfsparms.malformation_steps_1 - 1];
                final int ay1 = ret1.contour_y[lfsparms.malformation_steps_1 - 1];
                final int bx1 = ret1.contour_x[lfsparms.malformation_steps_2 - 1];
                final int by1 = ret1.contour_y[lfsparms.malformation_steps_2 - 1];
                ContourSearchResult ret2 = Contour.trace_contour(lfsparms.malformation_steps_2, minutia.x, minutia.y, minutia.x, minutia.y, minutia.ex, minutia.ey, RotationalScanDirection.SCAN_CLOCKWISE, bdata, iw, ih);
                if ((ret2.status == ContourSearchResult.Status.IGNORE) || (ret2.status == ContourSearchResult.Status.LOOP_FOUND) || (ret2.ncontour < lfsparms.malformation_steps_2)) {
                    if ((ret2.status == ContourSearchResult.Status.LOOP_FOUND) || (ret2.ncontour < lfsparms.malformation_steps_2)) {
                    }
                    log.debug("removing minutia at index i = " + i);
                    minutiae.remove(i);
                } else {
                    final int ax2 = ret2.contour_x[lfsparms.malformation_steps_1 - 1];
                    final int ay2 = ret2.contour_y[lfsparms.malformation_steps_1 - 1];
                    final int bx2 = ret2.contour_x[lfsparms.malformation_steps_2 - 1];
                    final int by2 = ret2.contour_y[lfsparms.malformation_steps_2 - 1];
                    final double a_dist = Util.distance(ax1, ay1, ax2, ay2);
                    final double b_dist = Util.distance(bx1, by1, bx2, by2);
                    final int blk_x = minutia.x / lfsparms.blocksize;
                    final int blk_y = minutia.y / lfsparms.blocksize;
                    boolean removed = false;
                    if ((a_dist == 0.0) || (b_dist == 0.0)) {
                        log.debug("removing minutia at index i = " + i);
                        minutiae.remove(i);
                        removed = true;
                    }
                    if (!removed) {
                        final int fmapval = low_flow_map[blk_y * mw + blk_x];
                        if (fmapval != 0) {
                            if (b_dist > lfsparms.max_malformation_dist) {
                                log.debug("removing minutia at index i = " + i);
                                minutiae.remove(i);
                                removed = true;
                            }
                        }
                    }
                    if (!removed) {
                        Line.Points points = Line.line_points(bx1, by1, bx2, by2);
                        for (int j = 0; j < points.num; j++) {
                            if (MinutiaType.fromPixelValue(bdata[(points.y_list[j] * iw) + points.x_list[j]]) != minutia.type) {
                                final double ratio = b_dist / a_dist;
                                if (ratio > lfsparms.min_malformation_ratio) {
                                    log.debug("removing minutia at index i = " + i);
                                    minutiae.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static final int startblk[] = { 6, 0, 0, 6, -1, 2, 4, 4, 2 };

    private static final int endblk[] = { 8, 0, 2, 6, -1, 2, 6, 4, 4 };

    private static final int blkdx[] = { 0, 1, 1, 1, 0, -1, -1, -1, 0 };

    private static final int blkdy[] = { -1, -1, 0, 1, 1, 1, 0, -1, -1 };

    /*************************************************************************
	**************************************************************************
	#cat: remove_near_invblocks_V2 - Removes minutia points from the given list
	#cat:                that are sufficiently close to a block with invalid
	#cat:                ridge flow or to the edge of the image.

	   Input:
	      minutiae  - list of true and false minutiae
	      direction_map - map of image blocks containing direction ridge flow
	      mw        - width in blocks of the map
	      mh        - height in blocks of the map
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_near_invblock_V2(MINUTIAE minutiae, int[] direction_map, final int mw, final int mh, final LFSPARMS lfsparms) {
        if (lfsparms.inv_block_margin > (lfsparms.blocksize >> 1)) {
            throw new MarginTooLargeException();
        }
        final int lo_margin = lfsparms.inv_block_margin;
        final int hi_margin = lfsparms.blocksize - lfsparms.inv_block_margin - 1;
        int i = 0;
        while (i < minutiae.getNum()) {
            MINUTIA minutia = minutiae.get(i);
            final int bx = minutia.x / lfsparms.blocksize;
            final int by = minutia.y / lfsparms.blocksize;
            final int px = minutia.x % lfsparms.blocksize;
            final int py = minutia.y % lfsparms.blocksize;
            final int ix;
            if (px < lo_margin) ix = 0; else if (px > hi_margin) ix = 2; else ix = 1;
            final int iy;
            if (py < lo_margin) iy = 0; else if (py > hi_margin) iy = 2; else iy = 1;
            boolean removed = false;
            if ((ix != 1) || (iy != 1)) {
                final int sbi = startblk[iy * 3 + ix];
                final int ebi = endblk[iy * 3 + ix];
                for (int ni = sbi; ni <= ebi; ni++) {
                    final int nbx = bx + blkdx[ni];
                    final int nby = by + blkdy[ni];
                    if ((nbx < 0) || (nbx >= mw) || (nby < 0) || (nby >= mh)) {
                        log.debug("removing minutia at index i = " + i);
                        minutiae.remove(i);
                        removed = false;
                        break;
                    } else if (direction_map[(nby * mw) + nbx] == Lfs.INVALID_DIR) {
                        int nvalid = Maps.num_valid_8nbrs(direction_map, nbx, nby, mw, mh);
                        if (nvalid < lfsparms.rm_valid_nbr_min) {
                            log.debug("removing minutia at index i = " + i);
                            minutiae.remove(i);
                            removed = true;
                            break;
                        }
                    }
                }
            }
            if (!removed) i++;
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_pointing_invblock_V2 - Removes minutia points that are relatively
	#cat:                close in the direction opposite the minutia to a
	#cat:                block with INVALID ridge flow.

	   Input:
	      minutiae  - list of true and false minutiae
	      direction_map - map of image blocks containing directional ridge flow
	      mw        - width in blocks of the map
	      mh        - height in blocks of the map
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_pointing_invblock_V2(MINUTIAE minutiae, int[] direction_map, final int mw, final int mh, final LFSPARMS lfsparms) {
        final double pi_factor = Math.PI / (double) lfsparms.num_directions;
        int i = 0;
        while (i < minutiae.getNum()) {
            MINUTIA minutia = minutiae.get(i);
            final double theta = minutia.direction * pi_factor;
            final double dx = Math.sin(theta) * (double) (lfsparms.trans_dir_pix);
            final double dy = Math.cos(theta) * (double) (lfsparms.trans_dir_pix);
            final int delta_x = (int) Math.round(dx);
            final int delta_y = (int) Math.round(dy);
            final int nx = minutia.x - delta_x;
            final int ny = minutia.y + delta_y;
            int bx = (int) (nx / lfsparms.blocksize);
            int by = (int) (ny / lfsparms.blocksize);
            bx = Math.max(0, bx);
            bx = Math.min(mw - 1, bx);
            by = Math.max(0, by);
            by = Math.min(mh - 1, by);
            final int dmapval = direction_map[(by * mw) + bx];
            if (dmapval == Lfs.INVALID_DIR) {
                log.debug("removing minutia at index i = " + i);
                minutiae.remove(i);
            } else {
                i++;
            }
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_overlaps - Takes a list of true and false minutiae and
	#cat:                attempts to detect and remove those false minutiae that
	#cat:                are on opposite sides of an overlap.  Note that this
	#cat:                routine does NOT edit the binary image when overlaps
	#cat:                are removed.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_overlaps(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, final LFSPARMS lfsparms) {
        boolean[] to_remove = new boolean[minutiae.getNum()];
        final int full_ndirs = lfsparms.num_directions << 1;
        final int qtr_ndirs = lfsparms.num_directions >> 2;
        final int half_ndirs = lfsparms.num_directions >> 1;
        final int min_deltadir = (3 * qtr_ndirs) - 1;
        int f = 0;
        while (f < minutiae.getNum() - 1) {
            if (!to_remove[f]) {
                MINUTIA minutia1 = minutiae.get(f);
                int s = f + 1;
                while (s < minutiae.getNum()) {
                    MINUTIA minutia2 = minutiae.get(s);
                    if (MinutiaType.fromPixelValue(bdata[minutia1.y * iw + minutia1.x]) != minutia1.type) {
                        break;
                    }
                    if (MinutiaType.fromPixelValue(bdata[(minutia2.y * iw) + minutia2.x]) != minutia2.type) {
                        to_remove[s] = true;
                    }
                    if (!to_remove[s]) {
                        final int delta_y = minutia2.y - minutia1.y;
                        if (delta_y <= lfsparms.max_overlap_dist) {
                            final double dist = Util.distance(minutia1.x, minutia1.y, minutia2.x, minutia2.y);
                            if (dist <= lfsparms.max_overlap_dist) {
                                final int deltadir;
                                if ((deltadir = Util.closest_dir_dist(minutia1.direction, minutia2.direction, full_ndirs)) == Lfs.INVALID_DIR) {
                                    throw new InvalidDirectionException(-651);
                                }
                                if (deltadir > min_deltadir) {
                                    if (minutia1.type == minutia2.type) {
                                        int joindir = Util.line2direction(minutia1.x, minutia1.y, minutia2.x, minutia2.y, lfsparms.num_directions);
                                        final int opp1dir = (minutia1.direction + lfsparms.num_directions) % full_ndirs;
                                        joindir = Math.abs(opp1dir - joindir);
                                        joindir = Math.min(joindir, full_ndirs - joindir);
                                        if (((joindir <= half_ndirs) || (dist <= lfsparms.max_overlap_join_dist)) && Imgutil.free_path(minutia1.x, minutia1.y, minutia2.x, minutia2.y, bdata, iw, ih, lfsparms)) {
                                            to_remove[f] = true;
                                            to_remove[s] = true;
                                        } else {
                                        }
                                    } else {
                                    }
                                } else {
                                }
                            } else {
                            }
                        } else {
                            break;
                        }
                    } else {
                    }
                    s++;
                }
            }
            f++;
        }
        for (int i = minutiae.getNum() - 1; i >= 0; i--) {
            if (to_remove[i]) {
                log.debug("removing minutia at index i = " + i);
                minutiae.remove(i);
            }
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_pores_V2 - Attempts to detect and remove minutia points located on
	#cat:                   pore-shaped valleys and/or ridges.  Detection for
	#cat:                   these features are only performed in blocks with
	#cat:                   LOW RIDGE FLOW or HIGH CURVATURE.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      direction_map  - map of image blocks containing directional ridge flow
	      low_flow_map   - map of image blocks flagged as LOW RIDGE FLOW
	      high_curve_map - map of image blocks flagged as HIGH CURVATURE
	      mw        - width in blocks of the maps
	      mh        - height in blocks of the maps
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_pores_V2(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, int[] direction_map, int[] low_flow_map, int[] high_curve_map, final int mw, final int mh, final LFSPARMS lfsparms) {
        final double pi_factor = Math.PI / (double) lfsparms.num_directions;
        int i = 0;
        while (i < minutiae.getNum()) {
            MINUTIA minutia = minutiae.get(i);
            boolean removed = false;
            final int blk_x = minutia.x / lfsparms.blocksize;
            final int blk_y = minutia.y / lfsparms.blocksize;
            if ((low_flow_map[(blk_y * mw) + blk_x] != 0 || high_curve_map[(blk_y * mw) + blk_x] != 0) && (direction_map[(blk_y * mw) + blk_x] >= 0)) {
                final double theta = (double) minutia.direction * pi_factor;
                final double sin_theta = Math.sin(theta);
                final double cos_theta = Math.cos(theta);
                final double drx = (double) minutia.x - (sin_theta * (double) lfsparms.pores_trans_r);
                final double dry = (double) minutia.y + (cos_theta * (double) lfsparms.pores_trans_r);
                final int rx = (int) Math.round(drx);
                final int ry = (int) Math.round(dry);
                if (MinutiaType.fromPixelValue(bdata[(ry * iw) + rx]) != minutia.type) {
                    SearchInDirectionResult searchResult = Imgutil.search_in_direction(minutia.type.toPixelValue(), rx, ry, -cos_theta, -sin_theta, lfsparms.pores_perp_steps, bdata, iw, ih);
                    if (searchResult.status == SearchInDirectionResult.PIXEL_FOUND) {
                        final int px = searchResult.x, py = searchResult.y, pex = searchResult.ex, pey = searchResult.ey;
                        ContourSearchResult traceResult1 = Contour.trace_contour(lfsparms.pores_steps_fwd, px, py, px, py, pex, pey, RotationalScanDirection.SCAN_COUNTERCLOCKWISE, bdata, iw, ih);
                        if ((traceResult1.status == ContourSearchResult.Status.IGNORE) || (traceResult1.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult1.ncontour < lfsparms.pores_steps_fwd)) {
                            if ((traceResult1.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult1.ncontour < lfsparms.pores_steps_fwd)) {
                            }
                            log.debug("removing minutia at index i = " + i);
                            minutiae.remove(i);
                            removed = true;
                        } else {
                            final int bx = traceResult1.contour_x[traceResult1.ncontour - 1];
                            final int by = traceResult1.contour_y[traceResult1.ncontour - 1];
                            ContourSearchResult traceResult2 = Contour.trace_contour(lfsparms.pores_steps_bwd, px, py, px, py, pex, pey, RotationalScanDirection.SCAN_CLOCKWISE, bdata, iw, ih);
                            if ((traceResult2.status == ContourSearchResult.Status.IGNORE) || (traceResult2.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult2.ncontour < lfsparms.pores_steps_bwd)) {
                                if ((traceResult2.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult2.ncontour < lfsparms.pores_steps_bwd)) {
                                }
                                log.debug("removing minutia at index i = " + i);
                                minutiae.remove(i);
                                removed = true;
                            } else {
                                final int dx = traceResult2.contour_x[traceResult2.ncontour - 1];
                                final int dy = traceResult2.contour_y[traceResult2.ncontour - 1];
                                SearchInDirectionResult qSearchResult = Imgutil.search_in_direction(minutia.type.toPixelValue(), rx, ry, cos_theta, sin_theta, lfsparms.pores_perp_steps, bdata, iw, ih);
                                if (qSearchResult.status == SearchInDirectionResult.PIXEL_FOUND) {
                                    ContourSearchResult traceResult3 = Contour.trace_contour(lfsparms.pores_steps_fwd, qSearchResult.x, qSearchResult.y, qSearchResult.x, qSearchResult.y, qSearchResult.ex, qSearchResult.ey, RotationalScanDirection.SCAN_CLOCKWISE, bdata, iw, ih);
                                    if ((traceResult3.status == ContourSearchResult.Status.IGNORE) || (traceResult3.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult3.ncontour < lfsparms.pores_steps_fwd)) {
                                        if ((traceResult3.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult3.ncontour < lfsparms.pores_steps_fwd)) {
                                        }
                                        log.debug("removing minutia at index i = " + i);
                                        minutiae.remove(i);
                                        removed = true;
                                    } else {
                                        final int ax = traceResult3.contour_x[traceResult3.ncontour - 1];
                                        final int ay = traceResult3.contour_y[traceResult3.ncontour - 1];
                                        ContourSearchResult traceResult4 = Contour.trace_contour(lfsparms.pores_steps_bwd, qSearchResult.x, qSearchResult.y, qSearchResult.x, qSearchResult.y, qSearchResult.ex, qSearchResult.ey, RotationalScanDirection.SCAN_COUNTERCLOCKWISE, bdata, iw, ih);
                                        if ((traceResult4.status == ContourSearchResult.Status.IGNORE) || (traceResult4.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult4.ncontour < lfsparms.pores_steps_bwd)) {
                                            if ((traceResult4.status == ContourSearchResult.Status.LOOP_FOUND) || (traceResult4.ncontour < lfsparms.pores_steps_bwd)) {
                                            }
                                            log.debug("removing minutia at index i = " + i);
                                            minutiae.remove(i);
                                            removed = true;
                                        } else {
                                            final int cx = traceResult4.contour_x[traceResult4.ncontour - 1];
                                            final int cy = traceResult4.contour_y[traceResult4.ncontour - 1];
                                            final double ab2 = Util.squared_distance(ax, ay, bx, by);
                                            final double cd2 = Util.squared_distance(cx, cy, dx, dy);
                                            if (cd2 > lfsparms.pores_min_dist2) {
                                                final double ratio = ab2 / cd2;
                                                if (ratio <= lfsparms.pores_max_ratio) {
                                                    log.debug("removing minutia at index i = " + i);
                                                    minutiae.remove(i);
                                                    removed = true;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    log.debug("removing minutia at index i = " + i);
                                    minutiae.remove(i);
                                    removed = true;
                                }
                            }
                        }
                    } else {
                        log.debug("removing minutia at index i = " + i);
                        minutiae.remove(i);
                        removed = true;
                    }
                }
            }
            if (!removed) i++;
        }
    }

    /*************************************************************************
	**************************************************************************
	#cat: remove_or_adjust_side_minutiae_V2 - Removes loops or minutia points that
	#cat:              are not on complete contours of specified length. If the
	#cat:              contour is complete, then the minutia is adjusted based
	#cat:              on a minmax analysis of the rotated y-coords of the contour.

	   Input:
	      minutiae  - list of true and false minutiae
	      bdata     - binary image data (0==while & 1==black)
	      iw        - width (in pixels) of image
	      ih        - height (in pixels) of image
	      direction_map - map of image blocks containing directional ridge flow
	      mw        - width (in blocks) of the map
	      mh        - height (in blocks) of the map
	      lfsparms  - parameters and thresholds for controlling LFS
	   Output:
	      minutiae  - list of pruned minutiae
	   Return Code:
	      Zero     - successful completion
	      Negative - system error
	**************************************************************************/
    public static void remove_or_adjust_side_minutiae_V2(MINUTIAE minutiae, boolean[] bdata, final int iw, final int ih, int[] direction_map, final int mw, final int mh, final LFSPARMS lfsparms) {
        int[] rot_y = new int[lfsparms.side_half_contour * 2 + 1];
        final double pi_factor = Math.PI / (double) lfsparms.num_directions;
        int i = 0;
        while (i < minutiae.getNum()) {
            MINUTIA minutia = minutiae.get(i);
            ContourSearchResult contourSearchResult = Contour.get_centered_contour(lfsparms.side_half_contour, minutia.x, minutia.y, minutia.ex, minutia.ey, bdata, iw, ih);
            ContourSearchResult.Status ret = contourSearchResult.status;
            if ((ret == ContourSearchResult.Status.LOOP_FOUND) || (ret == ContourSearchResult.Status.IGNORE) || (ret == ContourSearchResult.Status.INCOMPLETE)) {
                log.debug("removing minutia at index i = " + i);
                minutiae.remove(i);
            } else {
                final double theta = (double) minutia.direction * pi_factor;
                final double sin_theta = Math.sin(theta);
                final double cos_theta = Math.cos(theta);
                final int[] contour_x = contourSearchResult.contour_x, contour_ex = contourSearchResult.contour_ex;
                final int[] contour_y = contourSearchResult.contour_y, contour_ey = contourSearchResult.contour_ey;
                final int ncontour = contourSearchResult.ncontour;
                for (int j = 0; j < ncontour; j++) {
                    final double drot_y = ((double) contour_x[j] * sin_theta) - ((double) contour_y[j] * cos_theta);
                    rot_y[j] = (int) Math.round(drot_y);
                }
                MinMaxResult minmax = Util.minmaxs(rot_y, ncontour);
                if ((minmax.minmax_num == 1) && (minmax.minmax_type[0] == MinMaxType.MINIMA)) {
                    final int tmpx = contour_x[minmax.minmax_i[0]];
                    final int tmpy = contour_y[minmax.minmax_i[0]];
                    final int tmpex = contour_ex[minmax.minmax_i[0]];
                    final int tmpey = contour_ey[minmax.minmax_i[0]];
                    MINUTIA tmpMinutia = new MINUTIA(tmpx, tmpy, tmpex, tmpey, minutia.direction, minutia.reliability, minutia.type, minutia.appearing, minutia.feature_id);
                    minutiae.set(i, tmpMinutia);
                    minutia = minutiae.get(i);
                    final int bx = minutia.x / lfsparms.blocksize;
                    final int by = minutia.y / lfsparms.blocksize;
                    if (direction_map[(by * mw) + bx] == Lfs.INVALID_DIR) {
                        log.debug("removing minutia at index i = " + i);
                        minutiae.remove(i);
                    } else {
                        i++;
                    }
                } else if ((minmax.minmax_num == 3) && (minmax.minmax_type[0] == MinMaxType.MINIMA)) {
                    final int minloc;
                    if (minmax.minmax_val[0] < minmax.minmax_val[2]) minloc = minmax.minmax_i[0]; else minloc = minmax.minmax_i[2];
                    final int tmpx = contour_x[minloc];
                    final int tmpy = contour_y[minloc];
                    final int tmpex = contour_ex[minloc];
                    final int tmpey = contour_ey[minloc];
                    MINUTIA tmpMinutia = new MINUTIA(tmpx, tmpy, tmpex, tmpey, minutia.direction, minutia.reliability, minutia.type, minutia.appearing, minutia.feature_id);
                    minutiae.set(i, tmpMinutia);
                    minutia = minutiae.get(i);
                    final int bx = minutia.x / lfsparms.blocksize;
                    final int by = minutia.y / lfsparms.blocksize;
                    if (direction_map[(by * mw) + bx] == Lfs.INVALID_DIR) {
                        log.debug("removing minutia at index i = " + i);
                        minutiae.remove(i);
                    } else {
                        i++;
                    }
                } else {
                    log.debug("removing minutia at index i = " + i);
                    minutiae.remove(i);
                }
            }
        }
    }
}
