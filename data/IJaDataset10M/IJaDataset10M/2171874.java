package com.innovative.util;

import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.Map;
import com.innovative.main.MainException;

/**
 * Defines a set of page format enumerations with associated helper methods.
 *
 * @author Dylon Edwards
 * @since 0.3
 */
public class PaperSize {

    /** Keeps track of all the default PaperSizes */
    private static final Map<String, PaperSize> map = new LinkedHashMap<String, PaperSize>();

    public static final PaperSize LETTER = new PaperSize("Letter", 8.5, 11, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize LEGAL = new PaperSize("Legal", 8.5, 14, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize JUNIOR_LEGAL = new PaperSize("Junior Legal", 8, 5, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize LEDGER = new PaperSize("Ledger", 17, 11, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize TABLOID = new PaperSize("Tabloid", 11, 17, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize EXECUTIVE = new PaperSize("Executive", 7.25, 10.5, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize FOLIO = new PaperSize("Folio", 8.25, 13, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize FOOLSCAP_E = new PaperSize("Foolscap E", 8, 13, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize PHOTO = new PaperSize("Photo", 4, 6, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize QUARTO = new PaperSize("Quarto", 8.5, 10.8125, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize STATEMENT = new PaperSize("Statement", 5.5, 8.5, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ANSI_A = new PaperSize("ANSI A", 8.5, 11, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ANSI_B = new PaperSize("ANSI B", 11, 17, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ANSI_C = new PaperSize("ANSI C", 17, 22, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ANSI_D = new PaperSize("ANSI D", 22, 34, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ANSI_E = new PaperSize("ANSI E", 34, 44, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ANSI_F = new PaperSize("ANSI F", 28, 40, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ARCH_A = new PaperSize("Arch A", 9, 12, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ARCH_B = new PaperSize("Arch B", 12, 18, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ARCH_C = new PaperSize("Arch C", 18, 24, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ARCH_D = new PaperSize("Arch D", 24, 36, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ARCH_E = new PaperSize("Arch E", 36, 48, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ARCH_E1 = new PaperSize("Arch E1", 30, 42, UnitSystem.IMPERIAL, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_4A = new PaperSize("4A", 1682, 2378, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_2A = new PaperSize("2A", 1189, 1682, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A0 = new PaperSize("A0", 841, 1189, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A1 = new PaperSize("A1", 594, 841, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A2 = new PaperSize("A2", 420, 594, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A3 = new PaperSize("A3", 297, 420, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A4 = new PaperSize("A4", 210, 297, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A5 = new PaperSize("A5", 148, 210, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A6 = new PaperSize("A6", 105, 148, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A7 = new PaperSize("A7", 74, 105, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A8 = new PaperSize("A8", 52, 74, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A9 = new PaperSize("A9", 37, 52, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_A10 = new PaperSize("A10", 26, 37, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_4B = new PaperSize("4B", 2000, 2828, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_2B = new PaperSize("2B", 1414, 2000, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B0 = new PaperSize("B0", 1000, 1414, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B1 = new PaperSize("B1", 707, 1000, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B2 = new PaperSize("B2", 500, 707, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B3 = new PaperSize("B3", 353, 500, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B4 = new PaperSize("B4", 250, 353, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B5 = new PaperSize("B5", 176, 250, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B6 = new PaperSize("B6", 125, 176, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B7 = new PaperSize("B7", 88, 125, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B8 = new PaperSize("B8", 62, 88, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B9 = new PaperSize("B9", 44, 62, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_B10 = new PaperSize("B10", 31, 44, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C0 = new PaperSize("C0", 917, 1297, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C1 = new PaperSize("C1", 648, 917, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C2 = new PaperSize("C2", 458, 648, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C3 = new PaperSize("C3", 324, 458, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C4 = new PaperSize("C4", 229, 324, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C5 = new PaperSize("C5", 162, 229, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C6 = new PaperSize("C6", 114, 162, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C7 = new PaperSize("C7", 81, 114, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C8 = new PaperSize("C8", 57, 81, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C9 = new PaperSize("C9", 40, 57, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize ISO_C10 = new PaperSize("C10", 28, 40, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_4A = new PaperSize("JIS 4A", 1682, 2378, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_2A = new PaperSize("JIS 2A", 1189, 1682, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A0 = new PaperSize("JIS A0", 841, 1189, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A1 = new PaperSize("JIS A1", 594, 841, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A2 = new PaperSize("JIS A2", 420, 594, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A3 = new PaperSize("JIS A3", 297, 420, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A4 = new PaperSize("JIS A4", 210, 297, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A5 = new PaperSize("JIS A5", 148, 210, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A6 = new PaperSize("JIS A6", 105, 148, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A7 = new PaperSize("JIS A7", 74, 105, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A8 = new PaperSize("JIS A8", 52, 74, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A9 = new PaperSize("JIS A9", 37, 52, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_A10 = new PaperSize("JIS A10", 26, 37, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B0 = new PaperSize("JIS B0", 1030, 1456, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B1 = new PaperSize("JIS B1", 728, 1030, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B2 = new PaperSize("JIS B2", 515, 728, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B3 = new PaperSize("JIS B3", 364, 515, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B4 = new PaperSize("JIS B4", 257, 364, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B5 = new PaperSize("JIS B5", 182, 257, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B6 = new PaperSize("JIS B6", 128, 182, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B7 = new PaperSize("JIS B7", 91, 128, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B8 = new PaperSize("JIS B8", 64, 91, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B9 = new PaperSize("JIS B9", 45, 64, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize JIS_B10 = new PaperSize("JIS B10", 32, 45, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_4B = new PaperSize("DIN 4B", 2000, 2828, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_2B = new PaperSize("DIN 2B", 1414, 2000, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B0 = new PaperSize("DIN B0", 1000, 1414, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B1 = new PaperSize("DIN B1", 707, 1000, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B2 = new PaperSize("DIN B2", 500, 707, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B3 = new PaperSize("DIN B3", 353, 500, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B4 = new PaperSize("DIN B4", 250, 353, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B5 = new PaperSize("DIN B5", 176, 250, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B6 = new PaperSize("DIN B6", 125, 176, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B7 = new PaperSize("DIN B7", 88, 125, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B8 = new PaperSize("DIN B8", 62, 88, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B9 = new PaperSize("DIN B9", 44, 62, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    public static final PaperSize DIN_B10 = new PaperSize("DIN B10", 31, 44, UnitSystem.METRIC, Orientation.PORTRAIT, true);

    /** Specifies the layout of the given PaperSize should be Portrait */
    public static final Orientation PORTRAIT = Orientation.PORTRAIT;

    /** Specifies the layout of the given PaperSize should be Landscape */
    public static final Orientation LANDSCAPE = Orientation.LANDSCAPE;

    /** Useful for toArray(...) methods */
    private static final PaperSize[] sizes = new PaperSize[0];

    /** Holds the String to return from {@link #toString()} */
    public final String name;

    /** Holds the width of this PaperSize using the Portrait orientation */
    public final double width;

    /** Holds the height of this PaperSize using the Portrait orientation */
    public final double height;

    /** Holds the {@link UnitSystem} Specified by this PaperSize */
    public final UnitSystem unitSystem;

    /** The orientation used when specifying the original dimensions */
    public final Orientation orientation;

    /**
	 * Constructs a default PaperSize and records its existence if requested
	 *
	 * @param name       The String representing this PaperSize
	 * @param width      The width of this PaperSize
	 * @param height     The height of this PaperSize
	 * @param unitSystem The {@link UnitSystem} of this PaperSize
	 * @param record     Whether or not to add this to {@link #sizes}
	 */
    private PaperSize(final String name, final double width, final double height, final UnitSystem unitSystem, final Orientation orientation, final boolean record) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.unitSystem = unitSystem;
        this.orientation = orientation;
        if (record) {
            map.put(name, this);
        }
    }

    /**
	 * Constructs a PaperSize object with the given dimensions and name
	 *
	 * @param name        The String representing this PaperSize
	 * @param width       The width of this PaperSize
	 * @param height      The height of this PaperSize
	 * @param unitSystem  The {@link UnitSystem} of this PaperSize
	 * @param orientation The orientation of the paper to return
	 */
    public PaperSize(final String name, final double width, final double height, final UnitSystem unitSystem, final Orientation orientation) {
        this(name, width, height, unitSystem, orientation, false);
        if (name == null) {
            throw new MainException(null, "name may not be null");
        }
        if (width < 0 || height < 0) {
            throw new MainException(null, "neither width nor height may be less than zero");
        }
    }

    /**
	 * Returns all the PaperSize objects in {@link #sizes}
	 *
	 * @return The values of {@link #sizes}
	 */
    public static PaperSize[] getSizes() {
        return map.values().toArray(sizes);
    }

    /**
	 * Returns the PaperSize corresponding to name
	 *
	 * @param name The {@link #name} of the PaperSize to retrieve
	 * @return The PaperSize corresponding to name
	 */
    public static PaperSize valueOf(final String name) {
        return map.get(name);
    }

    /**
	 * Returns the dimensions of this PaperSize in a {@link java.awt.Rectangle.Double}
	 *
	 * @param orientation The orientation of the paper to return
	 * @return {@link #width} and {@link #height} in a new {@link java.awt.Rectangle.Double}
	 */
    public Rectangle.Double getRect(final Orientation orientation) {
        final double width = getWidth(orientation);
        final double height = getHeight(orientation);
        return new Rectangle.Double(0, 0, width, height);
    }

    /**
	 * Returns the width and height of this PaperSize scaled according to the specified DPI
	 *
	 * @param orientation The orientation of the paper to return
	 * @param dpi         The DPI with which to scale {@link #width} and {@link #height}
	 * @return A new {@link java.awt.Rectangle} with this PaperSize's dimensions
	 */
    public Rectangle getRect(final Orientation orientation, final int dpi) {
        final int width = (int) getWidth(orientation, dpi);
        final int height = (int) getHeight(orientation, dpi);
        return new Rectangle(width, height);
    }

    /**
	 * Returns the width of this PaperSize
	 *
	 * @param orientation The {@link Orientation} specifying the dimension to return
	 * @return {@link #width}
	 */
    @SuppressWarnings("fallthrough")
    public double getWidth(final Orientation orientation) {
        switch(orientation) {
            case PORTRAIT:
                {
                    switch(this.orientation) {
                        case PORTRAIT:
                            return width;
                        case LANDSCAPE:
                            return height;
                    }
                }
            case LANDSCAPE:
                {
                    switch(this.orientation) {
                        case PORTRAIT:
                            return height;
                        case LANDSCAPE:
                            return width;
                    }
                }
        }
        return -1;
    }

    /**
	 * Returns the width of this PaperSize, scaled according to dpi
	 *
	 * @param orientation The orientation of the width to return
	 * @param dpi         The desired DPI with which to scale this width
	 * @return            This width, scaled according to dpi
	 */
    public double getWidth(final Orientation orientation, final int dpi) {
        return unitSystem.getPixels(getWidth(orientation), dpi);
    }

    /**
	 * Returns the current width of this PaperSize
	 *
	 * @return {@link #getWidth(Orientation)}
	 */
    public double getWidth() {
        return getWidth(orientation);
    }

    /**
	 * Returns the width of the PaperSize scaled according to the requested DPI
	 *
	 * @param dpi The DPI with which to scale the width of this PaperSize
	 * @return The width of this PaperSize scaled according to dpi
	 */
    public double getWidth(final int dpi) {
        return getWidth(orientation, dpi);
    }

    /**
	 * Returns the height of this PaperSize
	 *
	 * @param orientation The {@link Orientation} specifying the dimension to return
	 * @return {@link #height}
	 */
    @SuppressWarnings("fallthrough")
    public double getHeight(final Orientation orientation) {
        switch(orientation) {
            case PORTRAIT:
                {
                    switch(this.orientation) {
                        case PORTRAIT:
                            return height;
                        case LANDSCAPE:
                            return width;
                    }
                }
            case LANDSCAPE:
                {
                    switch(this.orientation) {
                        case PORTRAIT:
                            return width;
                        case LANDSCAPE:
                            return height;
                    }
                }
        }
        return -1;
    }

    /**
	 * Returns the height of this PaperSize, scaled according to dpi
	 *
	 * @param orientation The orientation of the height to return
	 * @param dpi         The desired DPI with which to scale this height
	 * @return            This height, scaled according to dpi
	 */
    public double getHeight(final Orientation orientation, final int dpi) {
        return unitSystem.getPixels(getHeight(orientation), dpi);
    }

    /**
	 * Returns the height of this PaperSize
	 *
	 * @return The height of this PaperSize
	 */
    public double getHeight() {
        return getHeight(orientation);
    }

    /**
	 * Returns the height of this PaperSize, scaled according to the requested DPI
	 *
	 * @param dpi The DPI with which to scale the height of this PaperSize
	 * @return The height of this PaperSize, scaled according to dpi
	 */
    public double getHeight(final int dpi) {
        return getHeight(orientation, dpi);
    }

    /**
	 * Returns the {@link UnitSystem} specified by this PaperSize
	 *
	 * @return {@link #unitSystem}
	 */
    public UnitSystem getUnitSystem() {
        return unitSystem;
    }

    /**
	 * Returns the {@link Orientation} used when instantiating this PaperSize
	 *
	 * @return {@link #orientation}
	 */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
	 * Returns the String to display for this PaperSize
	 *
	 * @return {@link #toString}
	 */
    @Override
    public String toString() {
        return name;
    }

    /**
	 * Determines the orientation of the given {@link PaperSize}
	 */
    public enum Orientation {

        PORTRAIT, LANDSCAPE
    }

    /**
	 * Returns the hashcode of this PaperSize
	 *
	 * @return {@link java.lang.Object#hashCode()}
	 */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
	 * Compares this PaperSize with another {@link java.lang.Object}
	 *
	 * @param object The {@link java.lang.Object} to compare with this PaperSize
	 * @return Whether object is equal to this PaperSize
	 */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof PaperSize) {
            final PaperSize papersize = (PaperSize) object;
            final double width = papersize.getWidth(orientation);
            final double height = papersize.getHeight(orientation);
            return Math.abs(this.height - height) < 0.01 && Math.abs(this.width - width) < 0.01;
        }
        return false;
    }
}
