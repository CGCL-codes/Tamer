package pl.radical.open.gg.packet;

import pl.radical.open.gg.utils.GGUtils;
import lombok.Getter;
import lombok.ToString;

/**
 * Gadu-Gadu packet header.
 * 
 * @author <a href="mailto:mnaglik@gazeta.pl">Marcin Naglik</a>
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 */
@ToString
public class GGHeader {

    /**
	 * Gadu-Gadu packet type
	 */
    @Getter
    private int type = -1;

    /**
	 * Gadu-Gadu packet length
	 */
    @Getter
    private int length = -1;

    /**
	 * Constructor for Header.
	 * 
	 * @param data
	 *            dane naglowka
	 */
    public GGHeader(final byte[] data) {
        type = GGUtils.byteToInt(data, 0);
        length = GGUtils.byteToInt(data, 4);
    }
}
