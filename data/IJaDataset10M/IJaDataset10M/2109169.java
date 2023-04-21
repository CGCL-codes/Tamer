package edu.ucsd.ncmir.tiff;

import edu.sdsc.grid.io.GeneralFile;
import edu.ucsd.ncmir.spl.io.Accessor;
import edu.ucsd.ncmir.volume.transfer_function.ShortTransferFunction;
import edu.ucsd.ncmir.volume.transfer_function.TransferFunction;
import edu.ucsd.ncmir.volume.transfer_function.TransferFunctionGenerator;
import edu.ucsd.ncmir.volume.volume.NativeType;

/**
 *
 * @author spl
 */
class TIFFImageListShort extends TIFFImageList {

    public TIFFImageListShort(Accessor accessor, GeneralFile[] list, int width, int height) {
        super(accessor, list, width, height);
    }

    @Override
    public NativeType getNativeType() {
        return NativeType.SHORT;
    }

    @Override
    public double getRangeMinimum() {
        return Short.MIN_VALUE;
    }

    @Override
    public double getRangeMaximum() {
        return Short.MAX_VALUE;
    }

    @Override
    public TransferFunction createTransferFunction(TransferFunctionGenerator generator) {
        return new ShortTransferFunction(generator);
    }
}
