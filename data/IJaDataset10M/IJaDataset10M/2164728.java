package jflac;

/**
 *  libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2000,2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */
public class Constants {

    /** The maximum number of audio channels. */
    public static final int MAX_CHANNELS = 8;

    /** The maximum frame block size. */
    public static final int MAX_BLOCK_SIZE = 65535;

    /** The maximum Rice partition order permitted by the format. */
    public static final int MAX_RICE_PARTITION_ORDER = 15;

    /** independent channels. */
    public static final int CHANNEL_ASSIGNMENT_INDEPENDENT = 0;

    /** left+side stereo. */
    public static final int CHANNEL_ASSIGNMENT_LEFT_SIDE = 1;

    /** right+side stereo. */
    public static final int CHANNEL_ASSIGNMENT_RIGHT_SIDE = 2;

    /** mid+side stereo. */
    public static final int CHANNEL_ASSIGNMENT_MID_SIDE = 3;

    /** FLAC Stream Sync string. */
    public static final byte[] STREAM_SYNC_STRING = new byte[] { (byte) 'f', (byte) 'L', (byte) 'a', (byte) 'C' };
}
