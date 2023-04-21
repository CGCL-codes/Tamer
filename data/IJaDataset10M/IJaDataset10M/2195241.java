package bouttime.report.bracketsheet;

import bouttime.dao.Dao;
import bouttime.model.Bout;
import bouttime.model.Group;
import bouttime.model.Wrestler;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import java.io.IOException;

/**
 * A class to draw a 16-man bracket in a pdf document.
 */
public class Bracket16BracketSheetReport extends CommonBracketSheet {

    public static final float matBoxStartX = 35;

    public static final float matBoxStartY = 760;

    public static final float matBoxWidth = 50;

    public static final float matBoxHeight = 60;

    public static final float matBoxTopPad = 15;

    public static final float matBoxBottomPad = 10;

    @Override
    protected boolean drawBracket(PdfContentByte cb, BaseFont bf, Dao dao, Group group, boolean doBoutNumbers) throws DocumentException, IOException {
        float leftMargin = 65;
        if (doBoutNumbers) {
            BracketSheetUtil.drawMatBox(cb, bf, matBoxStartX, matBoxStartY, matBoxWidth, matBoxHeight, matBoxTopPad, matBoxBottomPad, 1, 0, (group != null) ? group.getMat() : "", 90);
        }
        BracketSheetUtil.drawTournamentHeader(cb, bf, matBoxStartX, 35, dao, 90);
        if (group != null) {
            RoundRobinBracketSheetUtil.drawTitle(cb, bf, 35, cb.getPdfDocument().getPageSize().getHeight() / 2, group, 90);
        }
        Wrestler w1 = null;
        Wrestler w2 = null;
        Wrestler w3 = null;
        Wrestler w4 = null;
        Wrestler w5 = null;
        Wrestler w6 = null;
        Wrestler w7 = null;
        Wrestler w8 = null;
        Wrestler w9 = null;
        Wrestler w10 = null;
        Wrestler w11 = null;
        Wrestler w12 = null;
        Wrestler w13 = null;
        Wrestler w14 = null;
        Wrestler w15 = null;
        Wrestler w16 = null;
        Wrestler wa = null;
        if (group != null) {
            w1 = group.getWrestlerAtSeed(1);
            w2 = group.getWrestlerAtSeed(2);
            w3 = group.getWrestlerAtSeed(3);
            w4 = group.getWrestlerAtSeed(4);
            w5 = group.getWrestlerAtSeed(5);
            w6 = group.getWrestlerAtSeed(6);
            w7 = group.getWrestlerAtSeed(7);
            w8 = group.getWrestlerAtSeed(8);
            w9 = group.getWrestlerAtSeed(9);
            w10 = group.getWrestlerAtSeed(10);
            w11 = group.getWrestlerAtSeed(11);
            w12 = group.getWrestlerAtSeed(12);
            w13 = group.getWrestlerAtSeed(13);
            w14 = group.getWrestlerAtSeed(14);
            w15 = group.getWrestlerAtSeed(15);
            w16 = group.getWrestlerAtSeed(16);
        }
        float xStart = leftMargin + 30;
        float x = xStart;
        float yStart = 395;
        float y = yStart;
        float length = 150;
        float height = 30;
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w1 != null) ? w1.getString4Bracket() : "", 90, (w1 != null) ? w1.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w16 != null) ? w16.getString4Bracket() : "", 90, (w16 != null) ? w16.isScratched() : false);
        Bout b = (group != null) ? group.getBout(Bout.ROUND_1, 1) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w9 != null) ? w9.getString4Bracket() : "", 90, (w9 != null) ? w9.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w8 != null) ? w8.getString4Bracket() : "", 90, (w8 != null) ? w8.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 2) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w5 != null) ? w5.getString4Bracket() : "", 90, (w5 != null) ? w5.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w12 != null) ? w12.getString4Bracket() : "", 90, (w12 != null) ? w12.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 3) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w13 != null) ? w13.getString4Bracket() : "", 90, (w13 != null) ? w13.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w4 != null) ? w4.getString4Bracket() : "", 90, (w4 != null) ? w4.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 4) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w3 != null) ? w3.getString4Bracket() : "", 90, (w3 != null) ? w3.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w14 != null) ? w14.getString4Bracket() : "", 90, (w14 != null) ? w14.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 5) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w11 != null) ? w11.getString4Bracket() : "", 90, (w11 != null) ? w11.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w6 != null) ? w6.getString4Bracket() : "", 90, (w6 != null) ? w6.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 6) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w7 != null) ? w7.getString4Bracket() : "", 90, (w7 != null) ? w7.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w10 != null) ? w10.getString4Bracket() : "", 90, (w10 != null) ? w10.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 7) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = x + (2 * height);
        BracketSheetUtil.drawString(cb, bf, x - 2, y + 2, 10, (w15 != null) ? w15.getString4Bracket() : "", 90, (w15 != null) ? w15.isScratched() : false);
        BracketSheetUtil.drawString(cb, bf, x + height + 9, y + 2, 10, (w2 != null) ? w2.getString4Bracket() : "", 90, (w2 != null) ? w2.isScratched() : false);
        b = (group != null) ? group.getBout(Bout.ROUND_1, 8) : null;
        if ((b != null) && !b.isBye() && doBoutNumbers) {
            BracketSheetUtil.drawBoutNum(cb, bf, x + (height / 2) - 10, y + (length / 2), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
        }
        BracketSheetUtil.drawFishTailUp(cb, x, y, length, height, 1, 0);
        BracketSheetUtil.drawHorizontalLine(cb, x, y, height, 1, 0);
        x = xStart + (height / 2);
        y = yStart + length;
        float round2height = height * 2;
        float round2length = (length / 5) * 2;
        BracketSheetUtil.drawFishTailUp(cb, x, y, round2length, round2height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (round2height / 2) - 15, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "D", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 1) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2) + 5), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round2height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = x + (2 * round2height);
        BracketSheetUtil.drawFishTailUp(cb, x, y, round2length, round2height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (round2height / 2) - 15, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "E", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 2) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2) + 5), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round2height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = x + (2 * round2height);
        BracketSheetUtil.drawFishTailUp(cb, x, y, round2length, round2height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (round2height / 2) - 15, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "F", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 3) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2) + 5), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round2height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = x + (2 * round2height);
        BracketSheetUtil.drawFishTailUp(cb, x, y, round2length, round2height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (round2height / 2) - 15, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "G", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 4) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2) + 5), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round2height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = xStart + (height / 2) + (round2height / 2);
        y = yStart + length + round2length;
        float round4height = round2height * 2;
        float round4length = round2length;
        BracketSheetUtil.drawFishTailUp(cb, x, y, round4length, round4height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (round4height / 2) - 10, y + (round4length / 2), 10, 5, 2, 12, 1, 0, "B", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_4, 1) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round4height / 2) + 10), y + ((round4length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round4height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = x + (2 * round4height);
        BracketSheetUtil.drawFishTailUp(cb, x, y, round4length, round4height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (round4height / 2) - 10, y + (round4length / 2), 10, 5, 2, 12, 1, 0, "C", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_4, 2) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round4height / 2) + 10), y + ((round4length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round4height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = xStart + (height / 2) + (round2height / 2) + (round4height / 2);
        y = yStart + length + round2length + round4length;
        float round6height = round4height * 2;
        float round6length = round4length;
        b = (group != null) ? group.getBout(Bout.ROUND_6, 1) : null;
        BracketSheetUtil.drawFishTailUp(cb, x, y, round6length, round6height, 1, 0);
        if (b != null) {
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 3, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x + round6height + 10, y + 8, 10, wa.getLastName(), 90, wa.isScratched());
            }
        }
        x = x + (round6height / 2);
        y = y + round6length;
        float finalLength = (round6length / 3) * 3;
        BracketSheetUtil.drawVerticalLine(cb, x, y, finalLength, 1, 0);
        float fontsize = 8;
        float mid = y + (finalLength / 2);
        x += 15;
        BracketSheetUtil.drawStringCentered(cb, bf, x, mid, fontsize, "Champion", 90);
        y -= 50;
        BracketSheetUtil.drawBoutLabel(cb, bf, x - 50, y + 10, 10, 5, 2, 12, 1, 0, "A", 90);
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x, y, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getWinner();
            if (wa != null) {
                BracketSheetUtil.drawString(cb, bf, x - 18, y + 58, 10, wa.getLastName(), 90);
            }
        }
        float consYStart = yStart - round2length;
        float consXStart = xStart + (height / 2);
        x = consXStart;
        y = consYStart;
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, round2height, 1, 0);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 5) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2)), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + round2height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + (2 * round2height);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, round2height, 1, 0);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 6) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2)), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + round2height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + (2 * round2height);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, round2height, 1, 0);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 7) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2)), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + round2height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + (2 * round2height);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, round2height, 1, 0);
        b = (group != null) ? group.getBout(Bout.ROUND_2, 8) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((round2height / 2)), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + round2height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        float consRound3height = (round2height / 5) * 4;
        fontsize = 8;
        x = consXStart + (round2height / 2);
        y = consYStart - round2length;
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound3height, 1, 0);
        BracketSheetUtil.drawString(cb, bf, x + consRound3height + 3, y + round2length + 3, fontsize, "Loser G", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_3, 1) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                if (b != null) {
                    BracketSheetUtil.drawBoutNum(cb, bf, x + (consRound3height / 3), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
                }
                wa = b.getRed();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
                }
                wa = b.getGreen();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound3height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
                }
            }
        }
        x = x + round2height + (round2height / 5);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound3height, 1, 0);
        BracketSheetUtil.drawString(cb, bf, x + 3, y + round2length + 3, fontsize, "Loser F", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_3, 2) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + (consRound3height / 3), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound3height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + (round2height * 2) + ((round2height / 5) * 4);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound3height, 1, 0);
        BracketSheetUtil.drawString(cb, bf, x + consRound3height + 3, y + round2length + 3, fontsize, "Loser E", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_3, 3) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + (consRound3height / 3), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound3height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + round2height + (round2height / 5);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound3height, 1, 0);
        BracketSheetUtil.drawString(cb, bf, x + 3, y + round2length + 3, fontsize, "Loser D", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_3, 4) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + (consRound3height / 3), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound3height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        float consRound4height = (consRound3height * 2) - ((round2height / 5) * 2);
        x = consXStart + (round2height / 2) + (consRound3height / 2);
        y = consYStart - round2length - round2length;
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound4height, 1, 0);
        b = (group != null) ? group.getBout(Bout.ROUND_4, 3) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + (consRound4height / 2), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound4height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + (consRound3height * 5);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound4height, 1, 0);
        b = (group != null) ? group.getBout(Bout.ROUND_4, 4) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + (consRound4height / 2), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound4height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        float consRound5height = (consRound4height * 2) - ((consRound4height / 5) * 3);
        x = consXStart + (round2height / 2) + (consRound3height / 2) + (consRound4height / 2);
        y = consYStart - round2length - round2length - round2length;
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound5height, 1, 0);
        BracketSheetUtil.drawString(cb, bf, x + consRound5height + 3, y + round2length + 3, fontsize, "Loser B", 90);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (consRound5height / 2) - 12, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "X", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_5, 1) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((consRound5height / 2) + 12), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound5height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + consRound5height + ((consRound5height / 5) * 2);
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound5height, 1, 0);
        BracketSheetUtil.drawString(cb, bf, x + 3, y + round2length + 3, fontsize, "Loser C", 90);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (consRound5height / 2) - 12, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "Y", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_5, 2) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((consRound5height / 2) + 12), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound5height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        float consRound6height = (consRound5height * 2) - ((consRound5height / 5) * 3);
        x = consXStart + (round2height / 2) + (consRound3height / 2) + (consRound4height / 2) + (consRound5height / 2);
        y = y - round2length;
        BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, consRound6height, 1, 0);
        BracketSheetUtil.drawBoutLabel(cb, bf, x + (consRound6height / 2) - 12, y + (round2length / 2), 10, 5, 2, 12, 1, 0, "Z", 90);
        b = (group != null) ? group.getBout(Bout.ROUND_6, 2) : null;
        if (b != null) {
            if (!b.isBye() && doBoutNumbers) {
                BracketSheetUtil.drawBoutNum(cb, bf, x + ((consRound6height / 2) + 12), y + ((round2length / 2)) - 10, 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
            }
            wa = b.getRed();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 12, 10, wa.getLastName(), 90);
            }
            wa = b.getGreen();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + consRound6height + 10, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        x = x + (consRound6height / 2);
        y = y - round2length;
        BracketSheetUtil.drawVerticalLine(cb, x, y, finalLength, 1, 0);
        x += 15;
        mid = y + (finalLength / 2);
        fontsize = 8;
        BracketSheetUtil.drawStringCentered(cb, bf, x, mid, fontsize, "3rd Place", 90);
        if (b != null) {
            wa = b.getWinner();
            if (wa != null) {
                BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 18, y + round2length - 12, 10, wa.getLastName(), 90);
            }
        }
        if (dao.isSecondPlaceChallengeEnabled()) {
            float challengeX = 530;
            float challengeY = 670;
            x = challengeX;
            y = challengeY;
            float height2 = height;
            b = (group != null) ? group.getBout(Bout.ROUND_7, 1) : null;
            if (b != null) {
                if (!b.isBye() && doBoutNumbers) {
                    BracketSheetUtil.drawBoutNum(cb, bf, x + (height2 / 6), y + (length / 8), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
                }
                wa = b.getRed();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_LEFT, x - 3, y + 8, 10, wa.getLastName(), 90);
                }
                wa = b.getGreen();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_LEFT, x + height2 + 10, y + 8, 10, wa.getLastName(), 90);
                }
            }
            BracketSheetUtil.drawFishTailUp(cb, x, y, round2length, height2, 1, 0);
            fontsize = 8;
            BracketSheetUtil.drawString(cb, bf, x, y - 30, fontsize, "Loser A", 90);
            BracketSheetUtil.drawString(cb, bf, x + height2, y - 37, fontsize, "Winner Z", 90);
            x += (height2 / 2);
            y = y + round2length;
            BracketSheetUtil.drawVerticalLine(cb, x, y, finalLength, 1, 0);
            if (b != null) {
                wa = b.getWinner();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_LEFT, x - 3, y + 5, 10, wa.getLastName(), 90);
                }
            }
            x += 15;
            mid = y + (finalLength / 2);
            fontsize = 8;
            BracketSheetUtil.drawStringCentered(cb, bf, x, mid, fontsize, "2nd Challenge", 90);
        }
        if (dao.isFifthPlaceEnabled()) {
            float fifthX = 530;
            float fifthY = 150;
            x = fifthX;
            y = fifthY;
            float height2 = height;
            float height5th = (height / 3) * 2;
            b = (group != null) ? group.getBout(Bout.ROUND_6, 3) : null;
            if (b != null) {
                if (!b.isBye() && doBoutNumbers) {
                    BracketSheetUtil.drawBoutNum(cb, bf, x + (height2 / 6), y + (length / 8), 20, 20, 0, 6, 12, 1, 0, b.getBoutNum(), 90);
                }
                wa = b.getRed();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + round2length - 6, 10, wa.getLastName(), 90);
                }
                wa = b.getGreen();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x + height2 + 10, y + round2length - 6, 10, wa.getLastName(), 90);
                }
            }
            BracketSheetUtil.drawFishTailDown(cb, x, y, round2length, height2, 1, 0);
            fontsize = 8;
            BracketSheetUtil.drawString(cb, bf, x, y + round2length + 3, fontsize, "Loser X", 90);
            BracketSheetUtil.drawString(cb, bf, x + height2, y + round2length + 3, fontsize, "Loser Y", 90);
            x += (height2 / 2);
            y = y - round2length;
            BracketSheetUtil.drawVerticalLine(cb, x, y, finalLength, 1, 0);
            if (b != null) {
                wa = b.getWinner();
                if (wa != null) {
                    BracketSheetUtil.drawStringAligned(cb, bf, PdfContentByte.ALIGN_RIGHT, x - 3, y + finalLength - 12, 10, wa.getLastName(), 90);
                }
            }
            x += 15;
            mid = y + (finalLength / 2);
            fontsize = 8;
            BracketSheetUtil.drawStringCentered(cb, bf, x, mid, fontsize, "5th Place", 90);
        }
        return true;
    }
}
