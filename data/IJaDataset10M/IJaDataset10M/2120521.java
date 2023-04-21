package nacaLib.bdb;

import jlib.threads.ThreadPoolRequest;

/**
 *
 * @author Pierre-Jean Ditscheid, Consultas SA
 * @version $Id$
 */
public class MonoThreadedSortAddItem extends ThreadPoolRequest {

    private BtreeFile m_btreeFile = null;

    private byte m_tbyData[] = null;

    private int m_nTotalLength;

    private int m_nNbRecordRead;

    private boolean m_bVariableLength;

    MonoThreadedSortAddItem(BtreeFile btreeFile, byte tbyData[], int nTotalLength, int nNbRecordRead, boolean bVariableLength) {
        super(false);
        m_btreeFile = btreeFile;
        m_tbyData = new byte[nTotalLength];
        for (int n = 0; n < nTotalLength; n++) {
            m_tbyData[n] = tbyData[n];
        }
        m_nTotalLength = nTotalLength;
        m_nNbRecordRead = nNbRecordRead;
        m_bVariableLength = bVariableLength;
    }

    public void execute() {
        m_btreeFile.asyncAddItemToSort(m_tbyData, m_nTotalLength, m_nNbRecordRead, m_bVariableLength);
    }
}
