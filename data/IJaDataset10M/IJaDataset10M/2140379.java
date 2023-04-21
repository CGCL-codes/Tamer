package mindtct;

/**
 * @author mchaberski
 *
 */
public class InvalidBinaryImageDimensionException extends UnexpectedResultException {

    /**
	 * @param ret
	 */
    public InvalidBinaryImageDimensionException(int iw, int ih, int bw, int bh) {
        super(MindtctErrorCodes.ERR_BINARY_IMAGE_DIM_INVALID, String.format("must have ( (iw=%d == bw=%d) and (ih=%d == bh=%d)", iw, bw, ih, bh));
    }
}
