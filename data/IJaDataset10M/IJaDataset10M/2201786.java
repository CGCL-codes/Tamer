package org.xiph.speex;

import java.io.StreamCorruptedException;

/**
 * Speex in-band and User in-band controls.
 * 
 * @author Marc Gimpel, Wimba S.A. (mgimpel@horizonwimba.com)
 * @version $Revision: 1.1 $
 */
public class Inband {

    private Stereo stereo;

    /**
   * Constructor.
   * @param stereo
   */
    public Inband(final Stereo stereo) {
        this.stereo = stereo;
    }

    /**
   * Speex in-band request (submode=14).
   * @param bits - Speex bits buffer.
   * @throws StreamCorruptedException If stream seems corrupted.
   */
    public void speexInbandRequest(final Bits bits) throws StreamCorruptedException {
        int code = bits.unpack(4);
        switch(code) {
            case 0:
                bits.advance(1);
                break;
            case 1:
                bits.advance(1);
                break;
            case 2:
                bits.advance(4);
                break;
            case 3:
                bits.advance(4);
                break;
            case 4:
                bits.advance(4);
                break;
            case 5:
                bits.advance(4);
                break;
            case 6:
                bits.advance(4);
                break;
            case 7:
                bits.advance(4);
                break;
            case 8:
                bits.advance(8);
                break;
            case 9:
                stereo.init(bits);
                break;
            case 10:
                bits.advance(16);
                break;
            case 11:
                bits.advance(16);
                break;
            case 12:
                bits.advance(32);
                break;
            case 13:
                bits.advance(32);
                break;
            case 14:
                bits.advance(64);
                break;
            case 15:
                bits.advance(64);
                break;
            default:
        }
    }

    /**
   * User in-band request (submode=13).
   * @param bits - Speex bits buffer.
   * @throws StreamCorruptedException If stream seems corrupted.
   */
    public void userInbandRequest(final Bits bits) throws StreamCorruptedException {
        int req_size = bits.unpack(4);
        bits.advance(5 + 8 * req_size);
    }
}
