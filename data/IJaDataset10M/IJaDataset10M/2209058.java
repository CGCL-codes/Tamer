package org.tigr.cloe.utils;

import org.tigr.cloe.model.facade.datastoreFacade.dao.DAO;
import org.tigr.cloe.model.facade.datastoreFacade.dao.DAOException;
import org.tigr.common.Application;
import org.tigr.seq.tdb.exceptions.*;

public class AssemblyDBRange {

    /**
    * Given a range relative to the ungapped assembly, calculates and returns
    * the corresponding range within the gapped assembly.  This method makes
    * the implicit assumption that the assembly is locked, so that all
    * coordinates are stable.
    *
    * @param   pDBName          The project database name.
    * @param   pAsmblID         The numeric assembly ID.
    * @param   pStartBase       Starting base number (1-based) of the desired
    *                           range within the ungapped assembly..
    * @param   pEndBase         Ending base number (1-based) of the desired
    *                           range within the ungapped assembly.
    * @return  A string containing 2 values separated by the '-' character.
    *          The two fields are the starting and ending indexes (0-based)
    *          of the range within the gapped assembly.  E.g.: 10013-20046.
    *
    * @throws  ClassNotFoundException, SQLException, OutOfRangeException.
    */
    public static Range calculateGappedAssemblyRange(String pDBName, int pAsmblID, int pStartBase, int pEndBase) throws OutOfRangeException, DAOException {
        int startIdx, endIdx, maxIdx, idx, baseCount;
        DAO dao = Application.getDatastore().getDao();
        int wholesize = dao.getWholeGappedSize(pAsmblID, pDBName);
        String globalLseq = dao.getGappedConsensusData(pAsmblID, pDBName, 0, wholesize);
        maxIdx = globalLseq.length() - 1;
        baseCount = 0;
        startIdx = endIdx = -1;
        for (idx = 0; idx <= maxIdx; idx++) {
            if (globalLseq.charAt(idx) != '-') {
                baseCount++;
                if (baseCount == pStartBase) {
                    startIdx = idx;
                }
                if (baseCount == pEndBase) {
                    endIdx = idx;
                    break;
                }
            }
        }
        if (startIdx < 0) {
            throw new OutOfRangeException("of of range for id " + pAsmblID);
        }
        if (endIdx < 0) {
            endIdx = maxIdx;
        }
        return new Range(startIdx, endIdx);
    }

    /**
    * Given a base number (1-based) of a base within the ungapped consensus,
    * calculates and returns the corresponding offset (0-based) of the
    * same base in the gapped consensus.  This method makes
    * the implicit assumption that the assembly is locked, so that all
    * coordinates are stable.
    *
    * @param   pDBName          The project database name.
    * @param   pAsmblID         The numeric assembly ID.
    * @param   pBaseNum         The 1-based ungapped coordinate.
    *
    * @return  An <code>int</code> value, the corresponding 0-based gapped
    *          offset.
     * @throws DAOException 
    *
    * @throws  ClassNotFoundException, SQLException, OutOfRangeException.
    */
    public static int calculateGappedAsmCoord(String pDBName, int pAsmblID, int pBaseNum) throws OutOfRangeException, DAOException {
        int theOffset, maxIdx, idx, baseCount;
        DAO dao = Application.getDatastore().getDao();
        int wholesize = dao.getWholeGappedSize(pAsmblID, pDBName);
        String globalLseq = dao.getGappedConsensusData(pAsmblID, pDBName, 0, wholesize);
        maxIdx = globalLseq.length() - 1;
        baseCount = 0;
        theOffset = -1;
        for (idx = 0; idx <= maxIdx; idx++) {
            if (globalLseq.charAt(idx) != '-') {
                baseCount++;
                if (baseCount == pBaseNum) {
                    theOffset = idx;
                    break;
                }
            }
        }
        if (theOffset < 0) {
            throw new OutOfRangeException("Error " + pAsmblID + " is out of range");
        }
        globalLseq = null;
        return theOffset;
    }

    /**
    * Given an offset (0-based) of a base within the gapped consensus,
    * calculates and returns the corresponding base number (1-based) of the
    * same base in the ungapped consensus.  This method makes
    * the implicit assumption that the assembly is locked, so that all
    * coordinates are stable.
    *
    * @param    pDBName         The project database name.
    * @param    pAsmblID        The numeric assembly ID.
    * @param    pOffset         The offset (0-based) of the base within the
    *                           gapped consensus.
    * @return   An <code>int</code>, the base number (1-based) within the
    *           ungapped consensus of the base.
    *
    * @throws   ClassNotFoundException, SQLException, OutOfRangeException.
     * @throws DAOException 
    */
    public static int calculateUngappedAsmCoord(String pDBName, int pAsmblID, int pOffset) throws OutOfRangeException, DAOException {
        int maxIdx, idx, baseNum;
        DAO dao = Application.getDatastore().getDao();
        int wholesize = dao.getWholeGappedSize(pAsmblID, pDBName);
        String globalLseq = dao.getGappedConsensusData(pAsmblID, pDBName, 0, wholesize);
        maxIdx = globalLseq.length() - 1;
        if (pOffset < 0 || pOffset > maxIdx) {
            throw new OutOfRangeException("Error Assembly " + pAsmblID + " is out of range");
        }
        for (idx = 0, baseNum = 0; idx <= pOffset; idx++) {
            if (globalLseq.charAt(idx) != '-') {
                baseNum++;
            }
        }
        globalLseq = null;
        return baseNum;
    }

    /**
    * Given a range relative to the gapped assembly, calculates and returns
    * the corresponding range within the ungapped assembly.  
    *
    * @param   pDBName          The project database name.
    * @param   pAsmblID         The numeric assembly ID.
    * @param   pStartBase       Starting base number (1-based) of the desired
    *                           range within the gapped assembly..
    * @param   pEndBase         Ending base number (1-based) of the desired
    *                           range within the gapped assembly.
    * @return  A string containing 2 values separated by the '-' character.
    *          The two fields are the starting and ending indexes (0-based)
    *          of the range within the ungapped assembly.  E.g.: 10013-20046.
    *
    * @throws  ClassNotFoundException, SQLException, OutOfRangeException.
     * @throws DAOException 
    */
    public static Range calculateUngappedAssemblyRange(String pDBName, int pAsmblID, int pStartBase, int pEndBase) throws OutOfRangeException, DAOException {
        int startIdx, endIdx, maxIdx, idx;
        DAO dao = Application.getDatastore().getDao();
        int wholesize = dao.getWholeGappedSize(pAsmblID, pDBName);
        String globalLseq = dao.getGappedConsensusData(pAsmblID, pDBName, 0, wholesize);
        maxIdx = globalLseq.length();
        if (pStartBase < 1 || pEndBase < 1 || pStartBase > maxIdx) {
            throw new OutOfRangeException("out of range for id " + pAsmblID);
        }
        if (pEndBase > maxIdx) {
            pEndBase = maxIdx;
        }
        startIdx = endIdx = -1;
        int baseCount = 0;
        for (idx = pStartBase - 1; idx <= pEndBase - 1; idx++) {
            if (globalLseq.charAt(idx) != '-') {
                startIdx = idx;
                break;
            }
        }
        for (idx = 0; idx <= startIdx; idx++) {
            if (globalLseq.charAt(idx) != '-') {
                baseCount++;
            }
        }
        startIdx = baseCount - 1;
        baseCount = 0;
        for (idx = pStartBase - 1; idx <= pEndBase - 1; idx++) {
            if (globalLseq.charAt(idx) != '-') {
                baseCount++;
            }
        }
        endIdx = startIdx + baseCount - 1;
        if (startIdx < 0 || endIdx < 0) {
            throw new OutOfRangeException("out of range for id " + pAsmblID);
        }
        return new Range(startIdx + 1, endIdx + 1);
    }
}
