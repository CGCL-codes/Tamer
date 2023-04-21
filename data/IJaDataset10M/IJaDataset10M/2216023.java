package CADI.Proxy.LogicalTarget.JPEG2000;

import java.io.PrintStream;
import java.util.ArrayList;
import CADI.Common.LogicalTarget.JPEG2000.JPCParameters;
import CADI.Common.LogicalTarget.JPEG2000.JPEG2000Util;
import CADI.Common.LogicalTarget.JPEG2000.JPEG2KCodestream;
import CADI.Common.LogicalTarget.JPEG2000.JPEG2KComponent;
import CADI.Common.LogicalTarget.JPEG2000.JPEG2KResolutionLevel;
import CADI.Common.LogicalTarget.JPEG2000.JPEG2KTile;
import CADI.Common.Network.JPIP.ViewWindowField;
import CADI.Common.Util.ArraysUtil;
import CADI.Common.Util.CADIDimension;
import CADI.Common.Util.CADIRectangle;

/**
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.0 2010/11/13
 */
public class ProxyJPEG2KCodestream extends JPEG2KCodestream {

    /**
   * Constructor.
   * 
   * @param identifier
   * @param jpcParameters
   */
    public ProxyJPEG2KCodestream(int identifier, JPCParameters jpcParameters) {
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
            tiles.put(index, new ProxyJPEG2KTile(this, index));
        }
    }

    @Override
    public ProxyJPEG2KTile getTile(int index) {
        return (ProxyJPEG2KTile) tiles.get(index);
    }

    /**
   * 
   * @param woi
   * @param sizParameters
   * @param codParameters
   * @return
   *
   * @deprecated
   */
    public ArrayList<ViewWindowField> getSubWOIs(ViewWindowField woi) {
        if (woi == null) {
            throw new NullPointerException();
        }
        ArrayList<ViewWindowField> subWOIs = new ArrayList<ViewWindowField>();
        int discardLevels = determineNumberOfDiscardLevels(woi.fsiz, woi.roundDirection);
        int[] components = ArraysUtil.rangesToIndexes(woi.comps);
        ArrayList<Integer> relevantTiles = calculateRelevantTiles(woi, discardLevels);
        JPEG2KTile tileObj = null;
        JPEG2KComponent compObj = null;
        JPEG2KResolutionLevel rLevelObj = null;
        int maxRLevel = 0;
        for (int tileIndex : relevantTiles) {
            tileObj = tiles.get(tileIndex);
            for (int comp : components) {
                if (maxRLevel < tileObj.getComponent(comp).getWTLevels()) {
                    maxRLevel = tileObj.getComponent(comp).getWTLevels();
                }
            }
        }
        for (int tileIndex : relevantTiles) {
            tileObj = tiles.get(tileIndex);
            for (int rLevel = 0; rLevel <= maxRLevel; rLevel++) {
                for (int component : components) {
                    compObj = tileObj.getComponent(component);
                    rLevelObj = compObj.getResolutionLevel(rLevel);
                    int maxRLevelsComponent = compObj.getWTLevels();
                    if (rLevel >= discardLevels) {
                        if (rLevel > maxRLevelsComponent - discardLevels) {
                            continue;
                        }
                    } else {
                        if (rLevel != 0) {
                            continue;
                        }
                    }
                    CADIDimension frameSize = JPEG2000Util.calculateFrameSize(getXSize(), getYSize(), getXOSize(), getYOSize(), maxRLevelsComponent - rLevel);
                    CADIRectangle supportRegion = new CADIRectangle(woi.roff[0], woi.roff[1], woi.rsiz[0], woi.rsiz[1]);
                    calculateSupportRegion(tileIndex, component, rLevel, supportRegion, discardLevels);
                    int precinctWidth = rLevelObj.getPrecinctWidth();
                    int precinctHeight = rLevelObj.getPrecinctHeight();
                    if (precinctWidth > frameSize.width) {
                        precinctWidth = frameSize.width;
                    }
                    if (precinctHeight > frameSize.height) {
                        precinctHeight = frameSize.height;
                    }
                    int numPrecinctsWidth = rLevelObj.getNumPrecinctsWide();
                    int numPrecinctsHeight = rLevelObj.getNumPrecinctsHeigh();
                    int startXPrecinct = 0, endXPrecinct = 0;
                    int startYPrecinct = 0, endYPrecinct = 0;
                    startXPrecinct = (int) (Math.floor((double) supportRegion.x / (double) (precinctWidth)));
                    startYPrecinct = (int) (Math.floor((double) supportRegion.y / (double) (precinctHeight)));
                    endXPrecinct = (int) (Math.ceil((double) (supportRegion.x + supportRegion.width) / (double) (precinctWidth)));
                    endYPrecinct = (int) (Math.ceil((double) (supportRegion.y + supportRegion.height) / (double) (precinctHeight)));
                    for (int yPrecinct = startYPrecinct; yPrecinct < endYPrecinct; yPrecinct++) {
                        for (int xPrecinct = startXPrecinct; xPrecinct < endXPrecinct; xPrecinct++) {
                            long inClassIdentifier = rLevelObj.getInClassIdentifier(yPrecinct * numPrecinctsWidth + xPrecinct);
                            ViewWindowField subWOI = new ViewWindowField();
                            subWOI.fsiz[0] = woi.fsiz[0];
                            subWOI.fsiz[1] = woi.fsiz[1];
                            subWOI.roff[0] = xPrecinct * precinctWidth;
                            subWOI.roff[1] = yPrecinct * precinctHeight;
                            subWOI.rsiz[0] = precinctWidth;
                            subWOI.rsiz[1] = precinctHeight;
                            subWOIs.add(subWOI);
                        }
                    }
                }
            }
        }
        return subWOIs;
    }

    /**
   * 
   * @param viewWindow
   * @return
   */
    public ArrayList<Long> ViewWindowToInClassIdentifier(ViewWindowField viewWindow) {
        return this.findRelevantPrecincts(viewWindow);
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
   * Prints this Proxy JPEG2K Codestream out to the specified output stream.
   * This method is useful for debugging.
   * 
   * @param out an output stream.
   */
    @Override
    public void list(PrintStream out) {
        out.println("-- Proxy JPEG2K Codestream --");
        super.list(out);
    }
}
