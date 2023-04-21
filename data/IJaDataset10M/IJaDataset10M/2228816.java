package com.foursoft.component.util;

public class ImageFileDescription {

    public static final String OUTPUT_FORMAT_JPEG = "image/jpeg";

    public static final String OUTPUT_FORMAT_GIF = "image/gif";

    public static final String OUTPUT_FORMAT_PNG = "image/png";

    public static final String OUTPUT_FORMAT_EMF = "EMF";

    public static final String OUTPUT_FORMAT_SVG = "image/svg";

    final Integer width;

    final Integer height;

    final String format;

    final Integer quality;

    public ImageFileDescription(String format, Integer width, Integer height, Integer quality) {
        this.width = width;
        this.height = height;
        this.quality = quality;
        String f = OUTPUT_FORMAT_GIF;
        if (format.equals(OUTPUT_FORMAT_EMF) || format.equals(OUTPUT_FORMAT_GIF) || format.equals(OUTPUT_FORMAT_JPEG) || format.equals(OUTPUT_FORMAT_PNG) || format.equals(OUTPUT_FORMAT_SVG)) {
            f = format;
        }
        this.format = f;
    }

    /**
	 * @return the width
	 */
    public final Integer getWidth() {
        return width;
    }

    /**
	 * @return the height
	 */
    public final Integer getHeight() {
        return height;
    }

    /**
	 * @return the format
	 */
    public final String getFormat() {
        return format;
    }

    /**
	 * @return the quality
	 */
    public final Integer getQuality() {
        return quality;
    }

    /**
	 * @return the usual extension for the format (without the dot), e.g. "png"
	 *         for PNG
	 */
    public final String getFormatExtension() {
        if (format.equals(OUTPUT_FORMAT_JPEG)) return "jpg"; else if (format.equals(OUTPUT_FORMAT_GIF)) return "gif"; else if (format.equals(OUTPUT_FORMAT_PNG)) return "png"; else if (format.equals(OUTPUT_FORMAT_EMF)) return "emf"; else if (format.equals(OUTPUT_FORMAT_SVG)) return "svg"; else return "unknown";
    }
}
