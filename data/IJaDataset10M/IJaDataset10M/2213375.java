package net.sf.jjmpeg.codec.video;

import net.sf.jjmpeg.codec.Codec;
import net.sf.jjmpeg.media.video.VideoFrame;

/**
 * A <CODE>VideoCodec</CODE> represents a video codec. Included are methods to
 * encode and decode data, with the <CODE>VideoFrame</CODE> serving to hold
 * decoded picture data.
 * 
 * @author Derek Whitaker & Trenton Pack
 * @version 1.0
 * @see net.sf.jjmpeg.media.video.VideoFrame
 */
public interface VideoCodec extends Codec {

    /**
	 * Decode the encoded byte data and return picture data. If the data does
	 * not contain enough data for at least 1 video frame, the codec will return
	 * <CODE>null</CODE>. Otherwise, a non-null array of frames should be
	 * returned.
	 * <P />
	 * If the data indicates the end of a stream and no frames have been
	 * decoded, then a zero length array will be returned, indicating there is
	 * no more picture data to be decoded in this stream. If, however, at least
	 * 1 frame has been decoded from this data before the end of the stream is
	 * signaled, then the next call should be prepared to return a zero length
	 * array, regardless of what data it is given.
	 * <P />
	 * The data passed to the codec may not end on a frame boundry, the codec
	 * should be prepared to buffer any data necessary in anticipation of
	 * receiving further data to decode.
	 * <P />
	 * Buffer data is not to be modified.
	 * 
	 * @param data the data encoded picture data
	 * @return the decoded picture data
	 */
    public VideoFrame[] decode(byte[] data);

    /**
	 * Decode the encoded byte data and return picture data. If the data does
	 * not contain enough data for at least 1 video frame, the codec will return
	 * <CODE>null</CODE>. Otherwise, a non-null array of frames should be
	 * returned. Decoding will begin with the data at <CODE>offset</CODE>, and
	 * will continue for <CODE>length</CODE> bytes.
	 * <P />
	 * If the data indicates the end of a stream and no frames have been
	 * decoded, then a zero length array will be returned, indicating there is
	 * no more picture data to be decoded in this stream. If, however, at least
	 * 1 frame has been decoded from this data before the end of the stream is
	 * signaled, then the next call should be prepared to return a zero length
	 * array, regardless of what data it is given.
	 * <P />
	 * The data passed to the codec may not end on a frame boundry, the codec
	 * should be prepared to buffer any data necessary in anticipation of
	 * receiving further data to decode.
	 * <P />
	 * Buffer data is not to be modified.
	 * 
	 * @param data the encoded picture data
	 * @param offset the start offset in array <CODE>data</CODE> at which
	 *            encoded picture data begins
	 * @param length the number of bytes containing encoded picture data
	 * @return the decoded picture data
	 */
    public VideoFrame[] decode(byte[] data, int offset, int length);

    /**
	 * Encode the provided picture data. The data is stored in the byte array in
	 * encoded format. Only fully encoded frames will return data. Frames whose
	 * encoded data will not fit are not encoded, and will be left for encoding
	 * later. Both the number of frames encoded and the number of bytes those
	 * encoded frames occupy will be returned. Codecs are allowed to hold some
	 * frames for future processing, but only entirely encoded frames are to be
	 * returned.
	 * <P />
	 * A <CODE>video</CODE> array of length zero is to be interpreted as the end
	 * of a stream. Any final data needing to be encoded into the stream should
	 * be returned.
	 * <P />
	 * <B>Note:</B> If your buffer is not of sufficient size to hold a single
	 * encoded picture, then no frames will ever be encoded and could cause an
	 * infinite loop.
	 * <P />
	 * Unencoded picture data is not to be modified.
	 * 
	 * @param video the video data to be encoded
	 * @param data the buffer into which the pictures are encoded
	 * @return an array containing two integers: the number of frames fully
	 *         encoded into the byte array will be at index 0, and the number of
	 *         bytes those encoded frames occupy will be at index 1
	 */
    public int[] encode(VideoFrame[] video, byte[] data);

    /**
	 * Encode the provided picture data. The data is stored in the byte array in
	 * encoded format. Only fully encoded frames will return data. Frames whose
	 * encoded data will not fit are not encoded, and will be left for encoding
	 * later. Both the number of frames encoded and the number of bytes those
	 * encoded frames occupy will be returned. Codecs are allowed to hold some
	 * frames for future processing, but only entirely encoded frames are to be
	 * returned. Encoded data will be stored beginning with byte
	 * <CODE>offset</CODE> and will continue to a maximum of <CODE>length</CODE>
	 * bytes.
	 * <P />
	 * A <CODE>video</CODE> array of length zero is to be interpreted as the end
	 * of a stream. Any final data needing to be encoded into the stream should
	 * be returned.
	 * <P />
	 * <B>Note:</B> If your buffer is not of sufficient size to hold a single
	 * encoded picture, then no frames will ever be encoded and could cause an
	 * infinite loop.
	 * <P />
	 * Unencoded picture data is not to be modified.
	 * 
	 * @param video the video data to be encoded
	 * @param data the buffer into which the pictures are encoded
	 * @param offset the start offset in array <CODE>data</CODE> at which
	 *            encoded data should be stored
	 * @param length the number of bytes available to store encoded data
	 * @return an array containing two integers: the number of frames fully
	 *         encoded into the byte array will be at index 0, and the number of
	 *         bytes those encoded frames occupy will be at index 1
	 */
    public int[] encode(VideoFrame[] video, byte[] data, int offset, int length);
}
