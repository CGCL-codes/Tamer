package nacaLib.bdb;

/**
 *
 * @author Pierre-Jean Ditscheid, Consultas SA
 * @version $Id$
 */
public class SegmentKeyTypeFactoryComp3 extends BtreeSegmentKeyTypeFactory {

    BtreeKeySegment make(int nKeyPositionInData, int nKeyPositionInKey, int nBufferLength, boolean bAscending) {
        return new BtreeKeySegmentComp3(nKeyPositionInData, nKeyPositionInKey, nBufferLength, bAscending);
    }
}
