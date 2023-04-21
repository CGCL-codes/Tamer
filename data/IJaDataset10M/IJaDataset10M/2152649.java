package org.davic.mpeg.dvb;

/**
 * This class represents one service within a transport stream as used in DVB.
 */
public class DvbService extends org.davic.mpeg.Service {

    protected DvbService() {
    }

    /**
	 * Get the elementary stream corresponding to a particular component tag.
	 * @param componentTag the value of the component tag that is associated
	 *            with the elementary stream.
	 * @return a reference to the DvbElementaryStream object that represents the
	 *         Elementary Stream that is associated with the provided component
	 *         tag. Null is returned if the specified component tag is not
	 *         present within this service or if the required information is not
	 *         available.
	 */
    public DvbElementaryStream retrieveDvbElementaryStream(int componentTag) {
        return null;
    }
}
