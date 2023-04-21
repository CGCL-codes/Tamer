package chequeredflag.data.track;

import java.io.*;
import java.nio.channels.*;
import java.util.*;
import chequeredflag.data.f1gp.F1GPMath;

/**
 *
 * @author Klaus
 */
public class TrackSegments extends Vector {

    final int const2PI_mul_4k = 25736;

    /** Creates a new instance of TrackSegments */
    public TrackSegments() {
        m_segs = new Seg[2000];
        for (int i = 0; i < m_segs.length; i++) m_segs[i] = new Seg();
    }

    public void load(FileInputStream fis) {
        TrackSegment ts;
        do {
            ts = new TrackSegment();
            ts.load(fis);
            add(ts);
        } while (ts.getType() != 0xFF);
    }

    public int save(FileOutputStream fos) throws IOException {
        int nWritten = 0;
        for (Enumeration e = elements(); e.hasMoreElements(); ) nWritten += ((TrackSegment) e.nextElement()).save(fos);
        try {
            fos.write(0xFF);
            fos.write(0xFF);
            nWritten += 2;
        } catch (IOException ioe) {
        }
        return nWritten;
    }

    protected void InitFences() {
    }

    protected void TCClearBuffers() {
    }

    protected void TCLoadKerbColours(int nKerbColours) {
    }

    protected int ProcessTrackSegments(int nSegStart, boolean bTrackCompilePass2) {
        int nSegsProcessed = 0;
        int nSector = 1;
        for (Enumeration e = elements(); e.hasMoreElements(); ) {
            TrackSegment ts = (TrackSegment) e.nextElement();
            ProcessTrackCommands(ts, nSegStart, bTrackCompilePass2);
            nSegsProcessed = ProcessTrackLayout(ts, nSegStart, bTrackCompilePass2, nSector);
            nSegStart = nSegStart + nSegsProcessed;
            nSector++;
        }
        return nSegStart;
    }

    ;

    /**
        Parameters:
         ts - current track segment (data from track file)
         nSegStart - corresponding index into Segs array.
         bTrackCompilePass2 - true if called through second pass of track data.
    */
    protected void ProcessTrackCommands(TrackSegment ts, int nSegStart, boolean bTrackCompilePass2) {
        int nSegNr = nSegStart;
        Vector commands = ts.getCommands();
        fTC0xa5 = false;
        for (Enumeration e = commands.elements(); e.hasMoreElements(); ) {
            Command cmd = (Command) e.nextElement();
            if (bTrackCompilePass2) {
                nSegNr = nSegNr + cmd.getParam(0);
                if (nSegNr > nTrackSegs) nSegNr = nSegNr - nTrackSegs;
            }
            switch(cmd.getType()) {
                case 0x80:
                    break;
                case 0x81:
                case 0x82:
                    break;
                case 0x83:
                case 0x84:
                    break;
                case 0x85:
                    TCTrackWidthChange((short) cmd.getParam(1), (short) cmd.getParam(2), bTrackCompilePass2);
                    break;
                case 0x86:
                case 0x87:
                    break;
                case 0x88:
                case 0x89:
                    break;
                case 0x8A:
                case 0x8B:
                    break;
                case 0x8C:
                case 0x8D:
                    break;
                case 0x8E:
                case 0x8F:
                    break;
                case 0x90:
                case 0x91:
                    break;
                case 0x92:
                case 0x93:
                    break;
                case 0x94:
                case 0x95:
                    break;
                case 0x96:
                case 0x97:
                    break;
                case 0x98:
                case 0x99:
                    break;
                case 0x9A:
                    break;
                case 0x9B:
                case 0x9C:
                case 0x9D:
                case 0x9E:
                    break;
                case 0x9F:
                case 0xA0:
                    break;
                case 0xA1:
                case 0xA2:
                case 0xA3:
                case 0xA4:
                    break;
                case 0xA5:
                    fTC0xa5 = true;
                    break;
                case 0xA6:
                case 0xA7:
                    break;
                case 0xA8:
                    break;
                case 0xA9:
                    break;
                case 0xAA:
                    break;
                case 0xAB:
                    break;
                case 0xAC:
                    break;
            }
        }
    }

    ;

    /**
      2007-04-12 KS: added parameter nSector (number of track segment) to store into Segs.
    */
    protected int ProcessTrackLayout(TrackSegment ts, int nSegStart, boolean bTrackCompilePass2, int nSector) {
        int nSegsProcessed;
        if (bTrackCompilePass2) nSegsProcessed = ProcessTrackLayoutPass2(ts, nSegStart); else {
            int nTCSectorArg_Flags;
            nTCSectorArg_Flags = ts.m_nFlags;
            nTCSectorArg_Flags |= 0xC300;
            nTCSectorArg_Flags ^= 0x3000;
            nTCSectorArg_Flags &= 0xFF37;
            int nTCSectorModifiedFlags_Save = nTCSectorModifiedFlags;
            nTCSectorModifiedFlags = nTCSectorArg_Flags;
            nTCSectorArg_Flags &= nTCSectorModifiedFlags_Save;
            nTCSectorArg_Flags &= 0x0C00;
            nTCSectorArgFlagsKerbsContinued = 0;
            if (nTCSectorArg_Flags != 0) {
                if (((nTCSectorModifiedFlags_Save ^ nTCSectorModifiedFlags) & 0x0004) == 0) {
                    nTCSectorArgFlagsKerbsContinued = nTCSectorArg_Flags;
                }
            }
            bTCSectorArgFlagsBridgedWallCntd = nTCSectorModifiedFlags & nTCSectorModifiedFlags_Save & 0x0030;
            nSegsProcessed = TCProcessTrackSectorPass1(ts, nSegStart, nSector);
        }
        return nSegsProcessed;
    }

    ;

    protected void TCCalcVergeWidth() {
    }

    ;

    protected void TCSelectiveClearBufW1E920() {
    }

    ;

    protected void TCInitSegPos(Seg seg) {
        seg.setPos(dTCAbsPosX, dTCAbsPosY, dTCAbsPosZ);
    }

    protected void TCInitSeg(int nSegIndex, int nSector) {
        Seg seg = m_segs[nSegIndex];
        seg.wAngleZ = (short) wTCAbsAngleZ_2;
        seg.wAngleXChase = (short) wTCAbsAngleX;
        TCInitSegPos(seg);
        seg.wCCLine = 0;
        seg.wCCLineRAngle = 0;
        seg.m_nTrackSector = nSector;
    }

    ;

    /**
        Store angle change to Seg nSeg, multiplied with PI/2.
        Segment number and angle of following segment provided by parameters.
    */
    protected void TCWriteAngleZChangeMulHalfPI(int nSeg, int nAngleZ, int nOldAngleZ) {
        if (nSeg < 0) return;
        Seg seg = m_segs[nSeg];
        int nAngleChange = (short) ((short) nAngleZ - (short) nOldAngleZ);
        int nAngleChangeMulHalfPi = (nAngleChange * const2PI_mul_4k) >> 14;
        seg.wAngleZChangeMulHalfPI = nAngleChangeMulHalfPi;
    }

    /**
        returns number of Segs belonging to this TrackSegment.
        2007-04-12 KS: added parameter nSector (number of track segment) to store into Segs.
    */
    protected int TCProcessTrackSectorPass1(TrackSegment ts, int nSegStart, int nSector) {
        TCCalcVergeWidth();
        TCSelectiveClearBufW1E920();
        short wTCOldAbsAngleZ = 0;
        boolean fIncrementAngle = true;
        if (fTC0xa5) {
            wTCAbsAngleZ_2 = (short) (wTCAbsAngleZ_2 - (ts.m_nCurvature >> 1));
            wTCAbsAngleX = (short) (wTCAbsAngleX - (ts.m_nHeightChange >> 1));
            fTC0xa5 = false;
        } else {
            wTCOldAbsAngleZ = wTCAbsAngleZ;
            wTCAbsAngleZ = wTCAbsAngleZ_2;
            wTCAbsAngleZ_2 = (short) (wTCAbsAngleZ_2 + (ts.m_nCurvature >> 1));
            wTCAbsAngleX = (short) (wTCAbsAngleX + (ts.m_nHeightChange >> 1));
            fIncrementAngle = false;
        }
        for (int i = nSegStart; i < nSegStart + ts.m_nTlu; i++) {
            if (fIncrementAngle) {
                wTCAbsAngleZ_2 += ts.m_nCurvature;
                wTCOldAbsAngleZ = wTCAbsAngleZ;
                wTCAbsAngleZ += ts.m_nCurvature;
                wTCAbsAngleX += ts.m_nHeightChange;
            } else fIncrementAngle = true;
            TCCalcOffsetsByTrk_Width(i);
            TCInitSeg(i, nSector);
            TCWriteAngleZChangeMulHalfPI(i - 1, wTCAbsAngleZ, wTCOldAbsAngleZ);
            int nPosChangeX, nPosChangeY, nPosChangeZ;
            nPosChangeX = F1GPMath.LookupSinRaw(wTCAbsAngleZ_2);
            nPosChangeX = (nPosChangeX * 1024) >> 14;
            nPosChangeY = F1GPMath.LookupCosRaw(wTCAbsAngleZ_2);
            nPosChangeY = (nPosChangeY * 1024) >> 14;
            nPosChangeZ = F1GPMath.LookupSinRaw(wTCAbsAngleX);
            nPosChangeZ = (nPosChangeZ * 1024) >> 14;
            dTCAbsPosX = dTCAbsPosX + nPosChangeX;
            dTCAbsPosY = dTCAbsPosY + nPosChangeY;
            dTCAbsPosZ = dTCAbsPosZ + nPosChangeZ;
            TCIncrCountersCalcVergeTrackWidth();
        }
        wTCAbsAngleZ_2 += ts.m_nCurvature >> 1;
        wTCAbsAngleX += ts.m_nHeightChange >> 1;
        wTCOldVergeWidth = (short) ts.m_nFenceDistL;
        return ts.m_nTlu;
    }

    protected int ProcessTrackLayoutPass2(TrackSegment ts, int nSegStart) {
        return ts.m_nTlu;
    }

    ;

    protected int TCCreateSegments() {
        nTrackSegs = ProcessTrackSegments(0, false);
        ProcessTrackSegments(0, true);
        return nTrackSegs;
    }

    ;

    protected void TCCalcPosAngleDifference(int nIndexLastSeg) {
        wTCAbsAngleZ_2 = (short) (m_segs[0].wAngleZ - m_segs[nIndexLastSeg].wAngleZ);
        wTCAbsAngleX = (short) (m_segs[0].wAngleXChase - m_segs[nIndexLastSeg].wAngleXChase);
        dTCAbsPosX = m_segs[0].getPosX();
        dTCAbsPosY = m_segs[0].getPosY();
        dTCAbsPosZ = m_segs[0].getPosZ();
        if (bCreatingPitlaneSegments) {
            dTCAbsPosX += dTCOffsetPitLanePosX;
            dTCAbsPosY += dTCOffsetPitLanePosY;
            dTCAbsPosZ += dTCOffsetPitLanePosZ;
        }
        dTCAbsPosX = dTCAbsPosX - m_segs[nIndexLastSeg].getPosX();
        dTCAbsPosY = dTCAbsPosY - m_segs[nIndexLastSeg].getPosY();
        dTCAbsPosZ = dTCAbsPosZ - m_segs[nIndexLastSeg].getPosZ();
    }

    ;

    /**
        Moves all Segs so that the difference between start and end of track is
        distributed evenly among the Segs.
    */
    protected void TCRecalcPosToFit(int nLastSeg, int nDiffX, int nDiffY, int nDiffZ) {
        int nAbsPosX, nAbsPosY, nAbsPosZ;
        int nRestX, nRestY, nRestZ;
        int nOffsetX, nOffsetY, nOffsetZ;
        int nNumSegs;
        nRestX = 0;
        nRestY = 0;
        nRestZ = 0;
        nOffsetX = 0;
        nOffsetY = 0;
        nOffsetZ = 0;
        nNumSegs = nLastSeg + 1;
        for (int i = 1; i <= nLastSeg; i++) {
            nRestX = nRestX + nDiffX;
            nOffsetX = nOffsetX + nRestX / nNumSegs;
            nRestX = nRestX % nNumSegs;
            nRestY = nRestY + nDiffY;
            nOffsetY = nOffsetY + nRestY / nNumSegs;
            nRestY = nRestY % nNumSegs;
            nRestZ = nRestZ + nDiffZ;
            nOffsetZ = nOffsetZ + nRestZ / nNumSegs;
            nRestZ = nRestZ % nNumSegs;
            Seg seg = m_segs[i];
            nAbsPosX = seg.getPosX();
            nAbsPosY = seg.getPosY();
            nAbsPosZ = seg.getPosZ();
            seg.setPos(nAbsPosX + nOffsetX, nAbsPosY + nOffsetY, nAbsPosZ + nOffsetZ);
        }
    }

    ;

    protected void TCInitData(int nStartWidth, int nStartAngle, int nPosX, int nPosY) {
        nSegNumber = 0;
        wTCSectorArgModifiedFlags = 0;
        bTC0x9a = 0x5A;
        wKerbsLeftBegin = 1;
        wKerbsLeftLength = 16;
        wKerbsRightBegin = 1;
        wKerbsRightLength = 16;
        wTrk_CCCoachingLeft = 16;
        wTrk_CCCoachingRight = 16;
        word_1ECE4 = 16;
        word_1ECEC = 16;
        word_1ED10 = 16;
        word_1ED18 = 16;
        InitFences();
        fTC0xa5 = false;
        wTC0xaa_arg1 = 0x17;
        wTC0xaa_arg2 = 0x7;
        wTC0xaa_arg3 = 0x1A00;
        nSeg_1E77E = -1;
        wTCAbsAngleZ_2 = (short) nStartAngle;
        wTCAbsAngleX = 0;
        dTCAbsPosX = nPosX;
        dTCAbsPosZ = 0;
        dTCAbsPosY = nPosY;
        wTrk_Width = (short) nStartWidth;
        wTrk_WidthPlus0x50 = (short) (wTrk_Width + 0x50);
        TCClearBuffers();
        wPoleWidth = 100;
        wTCTrackDataFlags = 0;
        wTCOldVergeWidth = 0;
        wTCNumKerbColours = 0;
        TCLoadKerbColours(wTCNumKerbColours);
        bCreatingPitlaneSegments = false;
        bDefaultTextureFlagsPlus1 = 3;
        byte_1E8AF = (byte) 0xaa;
        byte_1E8AE = 0x3d;
        bDefaultKerbColourIndex = (byte) 0x95;
        byte_1E8AC = (byte) 0x88;
        wTCAbsAngleZ = wTCAbsAngleZ_2;
        nTCSectorModifiedFlags = 0;
        nTCSectorArgFlagsKerbsContinued = 0;
        bTCSectorArgFlagsBridgedWallCntd = 0;
    }

    /**
        11.04.07 KS added fLayoutMode to prevent Seg moving.
    */
    protected void TCCompileTrack(int nStartWidth, int nStartAngle, int nPosX, int nPosY, boolean fLayoutMode) {
        TCInitData(nStartWidth, nStartAngle, nPosX, nPosY);
        nSegNumber = TCCreateSegments();
        int wMaxTrackSegIndex = nSegNumber - 1;
        int wMaxTrackSegIndexDiv32 = wMaxTrackSegIndex / 32;
        if (wMaxTrackSegIndexDiv32 > 50) {
        }
        int nLastTrackSeg = wMaxTrackSegIndex;
        int nSegTmp2 = wMaxTrackSegIndex;
        int nLastSegTrackOrPitLane = wMaxTrackSegIndex;
        int nFirstSegTrackOrPitLane = 0;
        int nSecondButLastSeg = wMaxTrackSegIndex - 1;
        int nSegTmp1 = 0;
        if (!fLayoutMode) {
            TCCalcPosAngleDifference(nLastTrackSeg);
            TCRecalcPosToFit(nLastTrackSeg, dTCAbsPosX, dTCAbsPosY, dTCAbsPosZ);
            TCCalcPosAngleDifference(nLastTrackSeg);
        }
    }

    /**
      Calculate all coordinates and angles of segments.
      11.04.07 KS: new parameter fLayoutMode prevents Seg moving.
    */
    public void calculateTrackLayout(int nStartWidth, int nStartAngle, int nPosX, int nPosY, boolean fLayoutMode) {
        int nWidthLength, nWidthEnd;
        double dANGLE_SCALE;
        nWidthLength = 0;
        nWidthEnd = 0;
        dANGLE_SCALE = Math.PI * 2.0 / 65535.0;
        TCCompileTrack(nStartWidth, nStartAngle, nPosX, nPosY, fLayoutMode);
        double dPosX, dPosY;
        dPosX = nPosX;
        dPosY = nPosY;
        for (Enumeration e = elements(); e.hasMoreElements(); ) {
            TrackSegment ts = (TrackSegment) e.nextElement();
            ts.calculateLayout(dPosX, dPosY, nStartWidth, nStartAngle, nWidthLength, nWidthEnd);
            dPosX = ts.getPosXEnd();
            dPosY = ts.getPosYEnd();
            nStartWidth = ts.getWidthEnd();
            nStartAngle = ts.getAngleEnd();
            nWidthLength = ts.getWidthChangeLength();
            nWidthEnd = ts.getWidthChangeEnd();
        }
    }

    /**
      Find position of pit lane entry in trackSegments list.
      Then, calculate other position data based on that.
      fPitSide is true if pits are on the left side of the track,
      and false if on right side.
    */
    public void calculatePitlaneLayout(TrackSegments trackSegments, boolean fPitSide, boolean fLayoutMode) {
        TrackSegment tsPitlaneEntry = trackSegments.findPitlaneEntry();
        if (tsPitlaneEntry == null) return;
        int nPITWIDTH = 1344;
        double dPitStartX, dPitStartY;
        if (fPitSide) {
            dPitStartX = tsPitlaneEntry.getPosXStart() - Math.cos(tsPitlaneEntry.getAngleStart()) * ((double) ((tsPitlaneEntry.getWidthStart() - nPITWIDTH / 2)) / 1024.0);
            dPitStartY = tsPitlaneEntry.getPosYStart() - Math.sin(tsPitlaneEntry.getAngleStart()) * ((double) ((tsPitlaneEntry.getWidthStart() - nPITWIDTH / 2)) / 1024.0);
        } else {
            dPitStartX = tsPitlaneEntry.getPosXStart() + Math.cos(tsPitlaneEntry.getAngleStart()) * ((double) ((tsPitlaneEntry.getWidthStart() - nPITWIDTH / 2)) / 1024.0);
            dPitStartY = tsPitlaneEntry.getPosYStart() + Math.sin(tsPitlaneEntry.getAngleStart()) * ((double) ((tsPitlaneEntry.getWidthStart() - nPITWIDTH / 2)) / 1024.0);
        }
        int nPitStartX = new Double(dPitStartX).intValue();
        int nPitStartY = new Double(dPitStartY).intValue();
        calculateTrackLayout(nPITWIDTH, tsPitlaneEntry.getAngleStart(), nPitStartX, nPitStartY, fLayoutMode);
    }

    /**
      Find track segments that contains the pit lane entry, if any.
      Returns found track segment, else null.
    */
    public TrackSegment findPitlaneEntry() {
        for (Enumeration e = elements(); e.hasMoreElements(); ) {
            TrackSegment ts = (TrackSegment) e.nextElement();
            if (ts.findCommand(0x86) != null) return ts;
        }
        return null;
    }

    /** gets the Seg object at position i (0-based) */
    public Seg getSegAt(int i) {
        if (i <= nSegNumber) return m_segs[i];
        return null;
    }

    public int getMaxTrackSegIndex() {
        return nSegNumber - 1;
    }

    /** gets the track segment at position i in the vector (1-based) */
    public TrackSegment getAt(int i) {
        if ((i > elementCount) || (i < 1)) return null; else return (TrackSegment) elementAt(i - 1);
    }

    /** insert segment at given position (1-based).
        returns the newly inserted segment. */
    public TrackSegment insertAt(int i) {
        TrackSegment newSeg;
        newSeg = new TrackSegment();
        newSeg.m_nTlu = 1;
        if (i > elementCount) add(newSeg); else {
            try {
                add(i - 1, newSeg);
            } catch (ArrayIndexOutOfBoundsException e) {
                newSeg = null;
            }
        }
        return newSeg;
    }

    /** delete segment at given position (1-based) */
    public void deleteAt(int i) {
        try {
            remove(i - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
        Calculates verge and track width for next Seg, if necessary.
    */
    protected void TCIncrCountersCalcVergeTrackWidth() {
        nSegNumber++;
        bVergeWidthChangeCount++;
        wTCVergeWidth_left = (short) (wTCVergeWidth_left + wTCVergeWidthChange_left);
        wTCVergeWidth_right = (short) (wTCVergeWidth_right + wTCVergeWidthChange_right);
        if (wTCTrackWidthChangeRemainingTlus > 0) {
            wTCTrackWidthChangeRemainingTlus--;
            wTrk_Width = (short) (wTrk_Width + wTCTrackWidthChange);
            wTrk_WidthPlus0x50 = (short) (wTrk_Width + 0x50);
        }
    }

    /**
        Calculate X- and Y-Offsets for width of track and extra stripe
        between track and run-off area (track border).
        Offsets are stored in members of Seg <nSegNum>.
    */
    protected void TCCalcOffsetsByTrk_Width(int nSegNum) {
        int nOffsetX, nOffsetY;
        Seg seg = m_segs[nSegNum];
        nOffsetX = F1GPMath.LookupCosRaw(wTCAbsAngleZ) * wTrk_Width;
        nOffsetX = nOffsetX >> 17;
        nOffsetX = (nOffsetX << 6) | ((wTrk_Width >> 5) & 0x3F);
        seg.setTrackWidthX((short) nOffsetX);
        nOffsetY = F1GPMath.LookupSinRaw(wTCAbsAngleZ) * wTrk_Width;
        nOffsetY = nOffsetY >> 17;
        nOffsetY = nOffsetY << 6;
        seg.setTrackWidthY((short) nOffsetY);
        nOffsetX = F1GPMath.LookupCosRaw(wTCAbsAngleZ) * wTrk_WidthPlus0x50;
        nOffsetX = nOffsetX >> 17;
        seg.setExtraSideX((byte) (nOffsetX - (seg.getTrackWidthX() >> 6)));
        nOffsetY = F1GPMath.LookupSinRaw(wTCAbsAngleZ) * wTrk_WidthPlus0x50;
        nOffsetY = nOffsetY >> 17;
        seg.setExtraSideY((byte) (nOffsetY - (seg.getTrackWidthY() >> 6)));
    }

    protected void TCTrackWidthChange(short wNumTlus, short wWidth, boolean bTrackCompilePass2) {
        if (bTrackCompilePass2) return;
        wTCTrackWidthChangeRemainingTlus = wNumTlus;
        if (wNumTlus == 0) {
            wTrk_Width = wWidth;
            wTrk_WidthPlus0x50 = (short) (wTrk_Width + 0x50);
            wTCTrackWidthChange = 0;
        } else {
            wTCTrackWidthChange = (short) ((wWidth - wTrk_Width) / wNumTlus);
        }
    }

    Seg m_segs[];

    int nSegNumber = 0;

    short wTCSectorArgModifiedFlags = 0;

    byte bTC0x9a = 0x5A;

    short wKerbsLeftBegin = 1;

    short wKerbsLeftLength = 16;

    short wKerbsRightBegin = 1;

    short wKerbsRightLength = 16;

    short wTrk_CCCoachingLeft = 16;

    short wTrk_CCCoachingRight = 16;

    short word_1ECE4 = 16;

    short word_1ECEC = 16;

    short word_1ED10 = 16;

    short word_1ED18 = 16;

    boolean fTC0xa5 = false;

    short wTC0xaa_arg1 = 0x17;

    short wTC0xaa_arg2 = 0x7;

    short wTC0xaa_arg3 = 0x1A00;

    int nSeg_1E77E = -1;

    short wTCAbsAngleZ_2 = 0;

    short wTCAbsAngleX = 0;

    int dTCAbsPosX = 0;

    int dTCAbsPosZ = 0;

    int dTCAbsPosY = 0;

    int dTCOffsetPitLanePosX = 0;

    int dTCOffsetPitLanePosY = 0;

    int dTCOffsetPitLanePosZ = 0;

    short wTrk_Width = 0;

    short wTrk_WidthPlus0x50 = (short) (wTrk_Width + 0x50);

    short wTCTrackWidthChangeRemainingTlus = 0;

    short wTCTrackWidthChange = 0;

    short wPoleWidth = 100;

    short wTCTrackDataFlags = 0;

    short wTCOldVergeWidth = 0;

    byte bVergeWidthChangeCount = 0;

    short wTCVergeWidth_left = 0;

    short wTCVergeWidth_right = 0;

    short wTCVergeWidthChange_left = 0;

    short wTCVergeWidthChange_right = 0;

    short wTCNumKerbColours = 0;

    boolean bCreatingPitlaneSegments = false;

    byte bDefaultTextureFlagsPlus1 = 3;

    byte byte_1E8AF = (byte) 0xaa;

    byte byte_1E8AE = 0x3d;

    byte bDefaultKerbColourIndex = (byte) 0x95;

    byte byte_1E8AC = (byte) 0x88;

    short wTCAbsAngleZ = wTCAbsAngleZ_2;

    int nTCSectorModifiedFlags = 0;

    int nTCSectorArgFlagsKerbsContinued = 0;

    int bTCSectorArgFlagsBridgedWallCntd = 0;

    int nTrackSegs;
}
