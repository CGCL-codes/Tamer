package CADI.Client.ClientLogicalTarget.JPEG2000;

import java.io.PrintStream;
import CADI.Common.LogicalTarget.JPEG2000.JPCParameters;
import CADI.Common.LogicalTarget.JPEG2000.JPEG2KCodestream;
import CADI.Common.Util.ArraysUtil;
import GiciException.ErrorException;
import java.util.Arrays;

/**
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.0 2010/11/13
 */
public class ClientJPEG2KCodestream extends JPEG2KCodestream {

    /**
   *
   * @param identifier
   * @param jpcParameters
   */
    public ClientJPEG2KCodestream(int identifier, JPCParameters jpcParameters) {
        super(identifier, jpcParameters);
    }

    /**
   *
   * @param identifier
   * @throws IllegalAccessException
   */
    @Override
    public void createTile(int index) {
        if (!tiles.containsKey(index)) {
            tiles.put(index, new ClientJPEG2KTile(this, index));
        }
    }

    @Override
    public ClientJPEG2KTile getTile(int index) {
        return (ClientJPEG2KTile) tiles.get(index);
    }

    /**
   * Calculates which are the necessary components to invert a multiple
   * component transformation.
   *
   * compsRanges the range of components to be decompressed. The first
   * 			index is an array index of the ranges. An the second index
   * 			indicates: 0 is the first component of the range, and 1 is the
   * 			last component of the range.
   *
   * @return an one-dimensional array with required components to inver the
   * 			multiple component transform.
   * @throws ErrorException
   */
    public int[] getRelevantComponents(int[][] comps) throws ErrorException {
        if (comps == null) {
            return null;
        }
        if (!isMultiComponentTransform()) {
            return ArraysUtil.rangesToIndexes(comps);
        }
        int[][] necCompsRanges = null;
        int multiComponentTransform = getMultiComponentTransform();
        try {
            if (multiComponentTransform > 0) {
                CalculateRelevantComponents crc = new CalculateRelevantComponents(multiComponentTransform, getZSize(), cbdParameters, mctParametersList, mccParametersList, mcoParameters, comps);
                necCompsRanges = crc.run();
            } else if ((jpkParameters != null) && (jpkParameters.WT3D != 0)) {
                CalculateRelevantComponents crc = new CalculateRelevantComponents(jpkParameters, getZSize(), comps);
                necCompsRanges = crc.run();
            } else {
                assert (true);
            }
        } catch (ErrorException e) {
            throw new ErrorException("Unsupported multicomponent transform");
        }
        return ArraysUtil.rangesToIndexes(necCompsRanges);
    }

    /**
   * Check if the logical target has a multiple component transformation.
   *
   * @return <code>true</code> if the logical target is spectrally
   * 			transformed. Otherwise, returns <code>false</code>.
   */
    public boolean isMultiComponentTransform() {
        if (getMultiComponentTransform() == 0) {
            if ((getJPKParameters() != null) && (getJPKParameters().WT3D != 0)) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
   * Returns the number of bits per sample for the components in
   * <code>relevantComponents</code>.
   * 
   * @param relevantComponents
   * 
   * @return 
   */
    public int[] getPrecision(int[] relevantComponents) {
        int[] precision = new int[relevantComponents.length];
        for (int zIdx = 0; zIdx < relevantComponents.length; zIdx++) {
            precision[zIdx] = getPrecision(relevantComponents[zIdx]);
        }
        return precision;
    }

    /**
   * 
   * @param relevantComponents
   * @return 
   */
    public float[] getRMMultValues(int[] relevantComponents) {
        float[] RMMultValues = new float[relevantComponents.length];
        if (getJPKParameters() == null) {
            Arrays.fill(RMMultValues, 1F);
        } else {
            for (int zIdx = 0; zIdx < relevantComponents.length; zIdx++) {
                RMMultValues[zIdx] = getJPKParameters().RMMultValues[relevantComponents[zIdx]];
            }
        }
        return RMMultValues;
    }

    /**
   * 
   * @param relevantComponents
   * @return 
   */
    public boolean[] getLSComponents(int[] relevantComponents) {
        boolean[] LSComponents = new boolean[relevantComponents.length];
        if (getJPKParameters() == null) {
            for (int zIdx = 0; zIdx < relevantComponents.length; zIdx++) {
                LSComponents[zIdx] = !isSigned(relevantComponents[zIdx]);
            }
        } else {
            for (int zIdx = 0; zIdx < relevantComponents.length; zIdx++) {
                LSComponents[zIdx] = getJPKParameters().LSComponents[relevantComponents[zIdx]];
            }
        }
        return LSComponents;
    }

    public boolean[] isSigned(int[] relevantComponents) {
        boolean[] signed = new boolean[relevantComponents.length];
        for (int zIdx = 0; zIdx < relevantComponents.length; zIdx++) {
            signed[zIdx] = isSigned(relevantComponents[zIdx]);
        }
        return signed;
    }

    @Override
    public String toString() {
        String str = "";
        str += getClass().getName() + " [";
        super.toString();
        str += "]";
        return str;
    }

    /**
   * Prints this Client JPEG2K Codestream out to the specified output stream.
   * This method is useful for debugging.
   *
   * @param out an output stream.
   */
    @Override
    public void list(PrintStream out) {
        out.println("-- Client JPEG2K Codestream --");
        super.list(out);
    }
}
