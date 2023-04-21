package com.chromamorph.score;

public class Code1Item extends Item {

    private float p00numParams, p01objectCode, p02staffNumber, p03horizPos, p04vertPos, p05noteStemAcc, p06noteHeadType, p07rhythmicTimeSpaceVal, p08stemLength, p09dotsTails, p10leftRightDisp, p11marks, p12noteOnStaffAboveBelow, p13markHorizDisp, p14markVertDisp, p15noteSizeFactor, p16ledgerLineThickness, p17noteStemSource, p18markSize, p19moveRhythmDotUpDown, p20noteWidth, p21alphaNotesTypeFont, p22alphaNotesHorizPos, p23alphaNotesVertPos, p24alphaNotesLetterSize, p25layerNumber, p26colorGray, p27ledgerLineColor, p28accPixelWidth, p29accSize, p30specialLedgerLineCtrl;

    private int start, byteCount;

    public Code1Item(int[] bytes, float numParams, int start) throws Exception {
        if (numParams > 30) throw new Exception("Number of parameters greater than 30 for Code 1 item (" + numParams + ")");
        this.start = start;
        this.byteCount = (int) numParams * 4 + 4;
        for (int p = 0; p <= numParams; p++) {
            int index = start + p * 4;
            switch(p) {
                case 0:
                    this.p00numParams = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 1:
                    this.p01objectCode = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 2:
                    this.p02staffNumber = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 3:
                    this.p03horizPos = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 4:
                    this.p04vertPos = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 5:
                    this.p05noteStemAcc = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 6:
                    this.p06noteHeadType = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 7:
                    this.p07rhythmicTimeSpaceVal = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 8:
                    this.p08stemLength = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 9:
                    this.p09dotsTails = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 10:
                    this.p10leftRightDisp = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 11:
                    this.p11marks = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 12:
                    this.p12noteOnStaffAboveBelow = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 13:
                    this.p13markHorizDisp = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 14:
                    this.p14markVertDisp = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 15:
                    this.p15noteSizeFactor = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 16:
                    this.p16ledgerLineThickness = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 17:
                    this.p17noteStemSource = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 18:
                    this.p18markSize = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 19:
                    this.p19moveRhythmDotUpDown = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 20:
                    this.p20noteWidth = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 21:
                    this.p21alphaNotesTypeFont = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 22:
                    this.p22alphaNotesHorizPos = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 23:
                    this.p23alphaNotesVertPos = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 24:
                    this.p24alphaNotesLetterSize = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 25:
                    this.p25layerNumber = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 26:
                    this.p26colorGray = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 27:
                    this.p27ledgerLineColor = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 28:
                    this.p28accPixelWidth = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 29:
                    this.p29accSize = ItemFactory.wordToFloat(bytes, index);
                    break;
                case 30:
                    this.p30specialLedgerLineCtrl = ItemFactory.wordToFloat(bytes, index);
                    break;
                default:
                    throw new Exception("Illegal parameter number in Code 1 item.");
            }
        }
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getByteCount() {
        return byteCount;
    }

    @Override
    public String toString() {
        return "Code1Item [p00numParams=" + p00numParams + ", p01objectCode=" + p01objectCode + ", p02staffNumber=" + p02staffNumber + ", p03horizPos=" + p03horizPos + ", p04vertPos=" + p04vertPos + ", p05noteStemAcc=" + p05noteStemAcc + ", p06noteHeadType=" + p06noteHeadType + ", p07rhythmicTimeSpaceVal=" + p07rhythmicTimeSpaceVal + ", p08stemLength=" + p08stemLength + ", p09dotsTails=" + p09dotsTails + ", p10leftRightDisp=" + p10leftRightDisp + ", p11marks=" + p11marks + ", p12noteOnStaffAboveBelow=" + p12noteOnStaffAboveBelow + ", p13markHorizDisp=" + p13markHorizDisp + ", p14markVertDisp=" + p14markVertDisp + ", p15noteSizeFactor=" + p15noteSizeFactor + ", p16ledgerLineThickness=" + p16ledgerLineThickness + ", p17noteStemSource=" + p17noteStemSource + ", p18markSize=" + p18markSize + ", p19moveRhythmDotUpDown=" + p19moveRhythmDotUpDown + ", p20noteWidth=" + p20noteWidth + ", p21alphaNotesTypeFont=" + p21alphaNotesTypeFont + ", p22alphaNotesHorizPos=" + p22alphaNotesHorizPos + ", p23alphaNotesVertPos=" + p23alphaNotesVertPos + ", p24alphaNotesLetterSize=" + p24alphaNotesLetterSize + ", p25layerNumber=" + p25layerNumber + ", p26colorGray=" + p26colorGray + ", p27ledgerLineColor=" + p27ledgerLineColor + ", p28accPixelWidth=" + p28accPixelWidth + ", p29accSize=" + p29accSize + ", p30specialLedgerLineCtrl=" + p30specialLedgerLineCtrl + ", start=" + start + ", byteCount=" + byteCount + "]";
    }
}
