package de.carne.fs.provider.png;

import de.carne.fs.core.transfer.IntFormatter;

class PNGChunkTypeFormatter implements IntFormatter {

    private static char mapTypeCode(int code) {
        char c;
        if ('A' <= code && code <= 'Z') {
            c = (char) code;
        } else if ('a' <= code && code <= 'z') {
            c = (char) code;
        } else if (65 <= code && code <= 90) {
            c = (char) code;
        } else if (97 <= code && code <= 122) {
            c = (char) code;
        } else {
            c = '.';
        }
        return c;
    }

    /**
	 * Format PNG Chunk Type.
	 * 
	 * @param buffer The <code>StringBuilder</code> to format into.
	 * @param type The type value to format.
	 * @return The updated <code>StringBuilder</code>.
	 */
    public static StringBuilder formatChunkType(StringBuilder buffer, int type) {
        buffer.append(mapTypeCode(type >>> 24));
        buffer.append(mapTypeCode((type >>> 16) & 0xff));
        buffer.append(mapTypeCode((type >>> 8) & 0xff));
        buffer.append(mapTypeCode(type & 0xff));
        return buffer;
    }

    /**
	 * Format PNG Chunk Type.
	 * 
	 * @param type The type value to format.
	 * @return The formatted type.
	 */
    public static String formatChunkType(int type) {
        return formatChunkType(new StringBuilder(), type).toString();
    }

    /**
	 * Format the flag values encoded in the Chunk Type.
	 * 
	 * @param buffer The <code>StringBuilder</code> to format into.
	 * @param type The type value to format.
	 * @return The update <code>StringBuilder</code>.
	 */
    public static StringBuilder formatChunkTypeFlags(StringBuilder buffer, int type) {
        buffer.append((type & 0x20000000) == 0x20000000 ? "ancillary + " : "critical + ");
        buffer.append((type & 0x00200000) == 0x00200000 ? "private + " : "public + ");
        if ((type & 0x00002000) == 0x00002000) {
            buffer.append("? + ");
        }
        buffer.append((type & 0x00000020) == 0x00000020 ? "safe to copy" : "unsafe to copy");
        return buffer;
    }

    @Override
    public StringBuilder formatInt(StringBuilder buffer, int i, boolean signed, int radix) {
        formatChunkType(buffer, i);
        buffer.append(" (");
        formatChunkTypeFlags(buffer, i);
        buffer.append(")");
        return buffer;
    }
}
