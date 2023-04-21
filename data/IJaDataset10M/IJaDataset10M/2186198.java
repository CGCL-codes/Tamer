package com.xenoage.zong.core.format;

import static com.xenoage.util.NullUtils.notNull;
import static com.xenoage.util.NullUtils.throwNullArg;
import static com.xenoage.zong.core.format.Defaults.defaultFont;
import com.xenoage.pdlib.PVector;
import com.xenoage.util.annotations.MaybeNull;
import com.xenoage.util.annotations.NeverNull;
import com.xenoage.util.font.FontInfo;

/**
 * Default formats for a score.
 *
 * @author Andreas Wenger
 */
public final class ScoreFormat {

    /** The default interline space in mm */
    public final float interlineSpace;

    /** The default distance between the first line of the top system to the top page margin in mm */
    public final float topSystemDistance;

    /** The default layout information for systems */
    @NeverNull
    public final SystemLayout systemLayout;

    @MaybeNull
    public final PVector<StaffLayout> staffLayouts;

    /** The default layout information for staves which have no own default layout */
    @NeverNull
    public final StaffLayout staffLayoutOther;

    /** The default font used for lyrics */
    @NeverNull
    public final FontInfo lyricFont;

    /** The style of measure numbering */
    @NeverNull
    public final MeasureNumbering measureNumbering;

    public static final ScoreFormat defaultValue;

    static {
        defaultValue = new ScoreFormat(1.6f, 15, SystemLayout.defaultValue, null, StaffLayout.defaultValue, defaultFont, MeasureNumbering.System);
    }

    /**
   * Creates a new {@link ScoreFormat}.
   * @param interlineSpace     space between two staff lines ("Rastralgröße") in mm
   * @param topSystemDistance  default distance between the first line of the top system
   *                           to the top page margin in mm
   * @param systemLayout       default system layout information
   * @param staffLayouts       default staff layout information (may also be or contain null)
   * @param staffLayoutOther   staff layout for staves not found in staffLayouts
   * @param lyricFont          default lyric font
   * @param measureNumbering   measure numbering style
   */
    public ScoreFormat(float interlineSpace, float topSystemDistance, SystemLayout systemLayout, PVector<StaffLayout> staffLayouts, StaffLayout staffLayoutOther, FontInfo lyricFont, MeasureNumbering measureNumbering) {
        this.interlineSpace = interlineSpace;
        this.topSystemDistance = topSystemDistance;
        this.systemLayout = systemLayout;
        this.staffLayouts = staffLayouts;
        this.staffLayoutOther = staffLayoutOther;
        this.lyricFont = lyricFont;
        this.measureNumbering = measureNumbering;
    }

    /**
   * Sets the default interline space in mm.
   */
    public ScoreFormat withInterlineSpace(float interlineSpace) {
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }

    public ScoreFormat withTopSystemDistance(float topSystemDistance) {
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }

    /**
   * Sets the default layout information for systems.
   */
    public ScoreFormat withSystemLayout(SystemLayout systemLayout) {
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }

    /**
   * Gets the default layout information for the given staff, or null if undefined.
   */
    public StaffLayout getStaffLayout(int staff) {
        if (staffLayouts != null && staff < staffLayouts.size()) {
            return staffLayouts.get(staff);
        } else {
            return null;
        }
    }

    /**
   * Sets the default layout information for the given staff, or null if undefined.
   */
    public ScoreFormat withStaffLayout(int staff, StaffLayout staffLayout) {
        PVector<StaffLayout> staffLayouts = this.staffLayouts.with(staff, staffLayout);
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }

    /**
   * Sets the default layout information for staves
	 * which have no own default layout.
   */
    public ScoreFormat withStaffLayoutOther(StaffLayout staffLayoutOther) {
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }

    /**
   * Gets the default layout information for the given staff, or
   * the default information for all other staves if unknown.
   * Thus, null is never returned.
   */
    public StaffLayout getStaffLayoutNotNull(int staff) {
        return notNull(getStaffLayout(staff), staffLayoutOther);
    }

    /**
   * Sets the default font used for lyrics.
	 * Must not be null.
   */
    public ScoreFormat withLyricFont(FontInfo lyricFont) {
        throwNullArg(lyricFont);
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }

    /**
   * Sets the style of measure numbering.
   */
    public ScoreFormat withMeasureNumbering(MeasureNumbering measureNumbering) {
        throwNullArg(lyricFont);
        return new ScoreFormat(interlineSpace, topSystemDistance, systemLayout, staffLayouts, staffLayoutOther, lyricFont, measureNumbering);
    }
}
