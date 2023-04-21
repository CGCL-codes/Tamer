package net.sf.rmoffice.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.Equipment;
import net.sf.rmoffice.core.InfoPage;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.Rank;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.IProgression;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SkillType;
import net.sf.rmoffice.meta.enums.StatEnum;
import net.sf.rmoffice.meta.enums.WeightUnit;
import net.sf.rmoffice.ui.models.LongRunningUIModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 */
public class PDFCreator extends AbstractPDFCreator {

    static final Logger log = LoggerFactory.getLogger(PDFCreator.class);

    static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale");

    public PDFCreator(RMSheet sheet, MetaData data, JFrame parent, LongRunningUIModel longRunningModel) {
        super(sheet, data, parent, longRunningModel);
    }

    @Override
    protected void internalCreate(OutputStream os, final LongRunningUIModel longRunAdapter) throws Exception {
        Document document = createDocument();
        try {
            longRunAdapter.startProgress(7 + sheet.getInfoPages().size());
            PdfWriter writer = PdfWriter.getInstance(document, os);
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            document.open();
            loadFonts();
            longRunAdapter.workDone(1, "pdf.page1.title2");
            PdfContentByte canvas = writer.getDirectContent();
            createPage1(canvas);
            longRunAdapter.workDone(1, "pdf.page2.title2");
            if (!document.newPage()) {
                log.error("Could not create a new page 2");
            }
            PdfContentByte canvas2 = writer.getDirectContent();
            createPage2(canvas2);
            longRunAdapter.workDone(1, "pdf.page3.title2");
            createPage3(document, writer);
            longRunAdapter.workDone(1, "pdf.page4.title2");
            if (!document.newPage()) {
                log.error("Could not create a new page 4 equipment");
            }
            PdfContentByte canvas4 = writer.getDirectContent();
            createPage4(canvas4);
            longRunAdapter.workDone(1, "pdf.page5.title2");
            if (!document.newPage()) {
                log.error("Could not create a new page 5 'all skills'");
            }
            PdfContentByte canvas5 = writer.getDirectContent();
            createPage5(canvas5);
            longRunAdapter.workDone(1, "pdf.page6.title2");
            if (!document.newPage()) {
                log.error("Could not create a new page 6 'combat status'");
            }
            PdfContentByte canvas6 = writer.getDirectContent();
            createPage6CombatStatus(canvas6);
            createPageInfoPages(writer, document, longRunAdapter);
            document.close();
            longRunAdapter.workDone(1, "message.exportpdf.successful");
        } finally {
            if (log.isInfoEnabled()) log.info("pdf created");
        }
    }

    private void createPageInfoPages(PdfWriter writer, Document document, LongRunningUIModel longRunAdapter) throws BadElementException, MalformedURLException, IOException, DocumentException {
        if (log.isDebugEnabled()) log.debug("processing additional pages");
        for (InfoPage infoPage : sheet.getInfoPages()) {
            longRunAdapter.workDone(1, "Page - Additional Info Page");
            if (!document.newPage()) {
                log.error("Could not create a new info page");
            }
            PdfContentByte canvas = writer.getDirectContent();
            headerCustomTitle(canvas, infoPage.getTitle());
            box(canvas, LEFT_X, UPPER_Y, RIGHT_X, BOTTOM_Y);
            ColumnText ct = new ColumnText(canvas);
            float fontSize = 8.25f;
            float leading = 10;
            Phrase phrase = new Phrase(infoPage.getContent(), new Font(fontUser, fontSize));
            int result = 0;
            do {
                ct = new ColumnText(canvas);
                fontSize -= 0.25f;
                leading -= 0.35f;
                phrase = new Phrase(infoPage.getContent(), new Font(fontUser, fontSize));
                ct.setSimpleColumn(phrase, LEFT_X + 5, UPPER_Y, RIGHT_X - 5, BOTTOM_Y + 5, leading, Element.ALIGN_LEFT);
                result = ct.go(true);
                log.debug("result column = " + result + " with font size=" + fontSize + " leading=" + leading);
            } while (fontSize > 2.5 && result == ColumnText.NO_MORE_COLUMN);
            ct = new ColumnText(canvas);
            phrase = new Phrase(infoPage.getContent(), new Font(fontUser, fontSize));
            ct.setSimpleColumn(phrase, LEFT_X + 5, UPPER_Y, RIGHT_X - 5, BOTTOM_Y + 5, leading, Element.ALIGN_LEFT);
            result = ct.go();
            if (result == ColumnText.NO_MORE_COLUMN) {
                showUserText(canvas, 6, LEFT_X + 10, BOTTOM_Y + 2, RESOURCE.getString("pdf.info.error.toomuchtext"));
            }
            log.debug("final result column = " + result + " with font size=" + fontSize + " leading=" + leading);
            footer(canvas);
        }
    }

    private float page4Jewelry(PdfContentByte canvas, float y) {
        float lineHeight = 11;
        y -= lineHeight;
        float leftX = LEFT_X + ((RIGHT_X - LEFT_X) / 2);
        float centerX = leftX + ((RIGHT_X - LEFT_X) / 4);
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.money.header"), centerX, y, 0);
        canvas.endText();
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.mithril") + ":", sheet.getCoins().getMithril(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.jewelry") + ":", "", centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.platinum") + ":", sheet.getCoins().getPlatinum(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(0), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.gold") + ":", sheet.getCoins().getGold(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(1), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.silver") + ":", sheet.getCoins().getSilver(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(2), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.bronze") + ":", sheet.getCoins().getBronze(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(3), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.copper") + ":", sheet.getCoins().getCopper(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(4), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.tin") + ":", sheet.getCoins().getTin(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(5), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.money.iron") + ":", sheet.getCoins().getIron(), leftX + 4, y, centerX - 3, fontRegular, 8);
        labeledUserText(canvas, "", sheet.getCoins().getJuwelry().get(6), centerX + 2, y, RIGHT_X - 3, fontRegular, 8);
        y -= lineHeight;
        hline(canvas, leftX, y, RIGHT_X);
        y -= lineHeight;
        return y;
    }

    private void page4Equipment(PdfContentByte canvas) {
        float lineHeight = 10.5f;
        float y = UPPER_Y - lineHeight;
        float centerX = LEFT_X + (RIGHT_X - LEFT_X) / 4;
        float[] x = new float[] { LEFT_X + 4, 238, 280 };
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment"), centerX, y, 0);
        canvas.endText();
        y -= lineHeight;
        canvas.beginText();
        canvas.setFontAndSize(fontBold, 8);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("common.equipment.itemdesc"), x[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment.location"), x[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("common.equipment.weight"), x[2], y, 0);
        canvas.endText();
        y -= lineHeight;
        float yLine = y;
        canvas.beginText();
        canvas.setFontAndSize(fontRegular, 7);
        int maxEquipmentLines = 0;
        while (yLine > (BOTTOM_Y + 2 * lineHeight)) {
            maxEquipmentLines++;
            canvas.showTextAligned(Element.ALIGN_LEFT, "___________________________________________", x[0], yLine, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, "__________", x[1], yLine, 0);
            canvas.showTextAligned(Element.ALIGN_CENTER, "__________", x[2], yLine, 0);
            yLine -= lineHeight;
        }
        canvas.endText();
        List<Equipment> list = sheet.getEquipments();
        WeightUnit wu = sheet.getWeightUnit();
        int weightSumCustomUnit = 0;
        int weightSumCarriedCustUnit = 0;
        for (int i = 0; i < list.size() && i < maxEquipmentLines; i++) {
            showUserText(canvas, 7, x[0], y, list.get(i).getDescription());
            showUserText(canvas, 7, x[1], y, list.get(i).getPlace(), Element.ALIGN_CENTER);
            showUserText(canvas, 7, x[2], y, wu.getFormattedString(list.get(i).getWeight()) + (list.get(i).isCarried() ? "*" : ""), Element.ALIGN_CENTER);
            weightSumCustomUnit += list.get(i).getWeight();
            if (list.get(i).isCarried()) {
                weightSumCarriedCustUnit += list.get(i).getWeight();
            }
            y -= lineHeight;
        }
        labeledUserText(canvas, RESOURCE.getString("common.equipment.location.carried") + ":", wu.getFormattedString(weightSumCarriedCustUnit), x[0], yLine, centerX - 10, fontBold, 7);
        float pageCenterX = LEFT_X + (RIGHT_X - LEFT_X) / 2;
        labeledUserText(canvas, RESOURCE.getString("common.equipment.weight.total") + ":", wu.getFormattedString(weightSumCustomUnit), centerX + 10, yLine, pageCenterX - 4, fontBold, 7);
        yLine -= lineHeight;
        float encumbr = sheet.getEncumbranceBase();
        int factor = (int) Math.ceil(weightSumCarriedCustUnit / encumbr);
        int start = factor - 2;
        if (start < 1) {
            start = 1;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < (start + 4); i++) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            if (factor == i) {
                sb.append("*");
            }
            float f1 = Math.round(10f * ((float) i - 1) * encumbr) / 10f;
            float f2 = Math.round(10f * i * encumbr) / 10f;
            sb.append(wu.getFormattedString(f1, false)).append(" - ").append(wu.getFormattedString(f2)).append(": ").append("" + ((i - 1) * -8));
        }
        canvas.beginText();
        canvas.setFontAndSize(fontRegular, 7);
        canvas.showTextAligned(Element.ALIGN_CENTER, sb.toString(), centerX, yLine, 0);
        canvas.endText();
    }

    private float page4CharImageAndSize(PdfContentByte canvas) throws MalformedURLException, IOException, DocumentException {
        if (sheet.getCharacteristics().getCharImage() != null) {
            float leftX = LEFT_X + ((RIGHT_X - LEFT_X) / 2);
            float centerX = leftX + ((RIGHT_X - LEFT_X) / 4);
            Image charImage = Image.getInstance(sheet.getCharacteristics().getCharImage());
            float maxWidth = RIGHT_X - leftX - 10;
            float maxHeight = 200;
            float height = charImage.getHeight();
            float width = charImage.getWidth();
            if (log.isDebugEnabled()) log.debug("character image size w x h = " + width + " x " + height);
            if (height > maxHeight) {
                float f = height / maxHeight;
                height = maxHeight;
                width = width / f;
                if (log.isDebugEnabled()) log.debug("character image scaled (height) to w x h = " + width + " x " + height);
            }
            if (width > maxWidth) {
                float f = width / maxWidth;
                width = maxWidth;
                height = height / f;
                if (log.isDebugEnabled()) log.debug("character image scaled (width) to w x h = " + width + " x " + height);
            }
            float x = centerX - (width / 2);
            float y = BOTTOM_Y + 5;
            charImage.setAbsolutePosition(x, y);
            charImage.scaleAbsolute(width, height);
            canvas.addImage(charImage, true);
            canvas.beginText();
            canvas.setFontAndSize(fontHeadline, 8);
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page4.characterimage.header"), centerX, BOTTOM_Y + 10 + height, 0);
            canvas.endText();
            y = BOTTOM_Y + 21 + height;
            hline(canvas, leftX, y, RIGHT_X);
            return y;
        }
        return BOTTOM_Y;
    }

    private float page1Characteristics(PdfContentByte canvas, float y) {
        if (sheet.getCharacteristics() == null) return y;
        float x1 = LEFT_X + 4;
        float x2 = PAGE1_LEFTBOX_RIGHTX - 3;
        float centerX = x1 + (x2 - x1) / 2;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        float center = LEFT_X + (PAGE1_LEFTBOX_RIGHTX - LEFT_X) / 2;
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.characteristics.header"), center, y, 0);
        canvas.endText();
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        Characteristics ch = sheet.getCharacteristics();
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.demeanor") + ":", ch.getDemeanor(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.appearance") + ":", "" + ch.getAppearance(), x1, y, centerX - 15, fontRegular, 8);
        String ageLabel = "";
        String ageText = "";
        if (StringUtils.trimToNull(ch.getApparentlyAge()) != null) {
            ageText = "(" + ch.getApparentlyAge() + ") ";
            ageLabel = "(" + RESOURCE.getString("rolemaster.characteristics.appage") + ") ";
        }
        ageLabel += RESOURCE.getString("rolemaster.characteristics.age") + ":";
        ageText += ch.getAge();
        labeledUserText(canvas, ageLabel, ageText, centerX - 13, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.gender") + ":", ch.isFemale() ? RESOURCE.getString("gender.female") : RESOURCE.getString("gender.male"), x1, y, centerX - 1, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.skin") + ":", ch.getSkin(), centerX + 1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.height") + ":", sheet.getLengthUnit().getFormattedString(ch.getHeight()), x1, y, centerX - 1, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.weight") + ":", sheet.getWeightUnit().getFormattedString(ch.getWeight()), centerX + 1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.hairColor") + ":", ch.getHairColor(), x1, y, centerX - 1, fontRegular, 8);
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.eyeColor") + ":", ch.getEyeColor(), centerX + 1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        if (!StringUtils.isEmpty(sheet.getCharacteristics().getClothSize())) {
            labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.clothSize") + ":", sheet.getCharacteristics().getClothSize(), x1, y, x2, fontRegular, 8);
            y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        if (!StringUtils.isEmpty(sheet.getCharacteristics().getHatSize()) || !StringUtils.isEmpty(sheet.getCharacteristics().getShoeSize())) {
            labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.hatSize") + ":", sheet.getCharacteristics().getHatSize(), x1, y, centerX - 1, fontRegular, 8);
            labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.shoeSize") + ":", sheet.getCharacteristics().getShoeSize(), centerX + 1, y, x2, fontRegular, 8);
            y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.personality") + ":", ch.getPersonality(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.motivation") + ":", ch.getMotivation(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.alignment") + ":", ch.getAlignment(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        hline(canvas, LEFT_X, y, PAGE1_LEFTBOX_RIGHTX);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.background.header"), center, y, 0);
        canvas.endText();
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.nationality") + ":", ch.getNationality(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.homeTown") + ":", ch.getHomeTown(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.deity") + ":", ch.getDeity(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.lord") + ":", ch.getLord(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.parent") + ":", ch.getParent(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.spouse") + ":", ch.getSpouse(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.siblings") + ":", ch.getSiblings(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.children") + ":", ch.getChildren(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.misc") + ":", ch.getMisc1(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, "", ch.getMisc2(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, "", ch.getMisc3(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, "", ch.getMisc4(), x1, y, x2, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        return y;
    }

    private float page1RaceAttributes(PdfContentByte canvas, float y) {
        float x = LEFT_X + 4;
        float x1 = PAGE1_LEFTBOX_RIGHTX - 4;
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        float center = LEFT_X + (PAGE1_LEFTBOX_RIGHTX - LEFT_X) / 2;
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.raceinfo.header"), center, y, 0);
        canvas.endText();
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("pdf.page1.raceinfo.souldeparture") + ":", "" + sheet.getRace().getSoulDeparture(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        labeledUserText(canvas, RESOURCE.getString("pdf.page1.raceinfo.recoverymult") + ":", "" + sheet.getRace().getRecoveryMultiplier(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        IProgression progK = sheet.getProgressionBody();
        labeledUserText(canvas, RESOURCE.getString("ui.basic.progressionBody") + ":", progK.getFormattedString(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        IProgression progM = sheet.getProgressionPower();
        labeledUserText(canvas, RESOURCE.getString("ui.basic.progressionPower") + ":", progM.getFormattedString(), x, y, x1, fontRegular, 8);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        hline(canvas, LEFT_X, y, PAGE1_LEFTBOX_RIGHTX);
        y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        return y;
    }

    private void drawHitsPPExhaust(PdfContentByte canvas, final float initialY) {
        hline(canvas, PAGE1_RIGHTBOX_LEFTX, initialY, RIGHT_X);
        float lineHeight = 11;
        float y = initialY - lineHeight;
        float[] xVal = new float[] { PAGE1_RIGHTBOX_LEFTX + 4, PAGE1_RIGHTBOX_LEFTX + 100, PAGE1_RIGHTBOX_LEFTX + 160, PAGE1_RIGHTBOX_LEFTX + 220 };
        canvas.beginText();
        canvas.setFontAndSize(fontBold, 7);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.hitsmax"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.exhaustionpoints"), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.powerpoints"), xVal[3], y, 0);
        y -= lineHeight;
        String hits = "" + sheet.getHitPoints();
        String pp = "" + sheet.getPowerPoints();
        canvas.showTextAligned(Element.ALIGN_CENTER, hits, xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "" + sheet.getExhaustionPoints(), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, pp, xVal[3], y, 0);
        y -= lineHeight;
        canvas.setFontAndSize(fontRegular, 7);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("rolemaster.recover.active"), xVal[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "1 / 3 " + RESOURCE.getString("common.hour.short"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "1 / 3 " + RESOURCE.getString("common.minute.short"), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "1 / 3 " + RESOURCE.getString("common.hour.short"), xVal[3], y, 0);
        y -= lineHeight;
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("rolemaster.recover.resting"), xVal[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "" + sheet.getRecoverHits(false) + " / 1 " + RESOURCE.getString("common.hour.short"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "" + sheet.getRecoverExhaust() + " / 1 " + RESOURCE.getString("common.minute.short"), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "" + sheet.getRecoverPP(false) + " / 1 " + RESOURCE.getString("common.hour.short"), xVal[3], y, 0);
        y -= lineHeight;
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("rolemaster.recover.sleeping"), xVal[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "" + sheet.getRecoverHits(true) + " / 3 " + RESOURCE.getString("common.hour.short"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.recover.exhaust.all"), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, "" + sheet.getRecoverPP(true) + " / 3 " + RESOURCE.getString("common.hour.short"), xVal[3], y, 0);
        canvas.endText();
    }

    private void page3SkillsPage(PdfContentByte canvas, List<ISkill> skills, Map<ISkill, Rank> ranks) {
        float[] xVal = new float[] { 61, 260, 282, 317, 352, 387, 422, 457, RIGHT_X - (RIGHT_X - 475) / 2 };
        float lineHeight = 10.5f;
        float y = 0;
        for (int col = 0; col < xVal.length; col++) {
            boolean drawingSpelllists = false;
            canvas.beginText();
            canvas.setFontAndSize(fontUser, 8);
            y = 738.8f;
            for (ISkill skill : skills) {
                int align = Element.ALIGN_CENTER;
                float x = xVal[col];
                if (!drawingSpelllists && skill.isSpelllist()) {
                    drawingSpelllists = true;
                    y -= lineHeight;
                    if (col == 0) {
                        hline(canvas, LEFT_X, y + 8f + lineHeight, RIGHT_X);
                        canvas.setFontAndSize(fontBold, 8);
                        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.header.spelllists"), xVal[col], y, 0);
                        hline(canvas, LEFT_X, y + 8f, RIGHT_X);
                    }
                    y -= lineHeight;
                }
                canvas.setFontAndSize(fontUser, 8);
                String str = null;
                Rank rank = ranks.get(skill);
                switch(col) {
                    case 0:
                        str = skill.getName();
                        align = Element.ALIGN_LEFT;
                        break;
                    case 1:
                        str = sheet.getSkillcategory(skill).getName();
                        align = Element.ALIGN_RIGHT;
                        canvas.setFontAndSize(fontUser, 5.5f);
                        break;
                    case 2:
                        str = "" + rank.getRank();
                        break;
                    case 3:
                        canvas.setFontAndSize(fontWidget, 8);
                        str = getWidgetNewRanksSymbol(sheet.getSkillcategory(skill), skill);
                        break;
                    case 4:
                        str = format(sheet.getSkillRankBonus(skill), true);
                        break;
                    case 5:
                        str = format(sheet.getSkillcategoryTotalBonus(sheet.getSkillcategory(skill)), true);
                        break;
                    case 6:
                        if (rank.getSpecialBonus() != null) {
                            str = format(rank.getSpecialBonus().intValue(), true);
                        }
                        break;
                    case 7:
                        str = format(sheet.getSkillTotalBonus(skill), false);
                        break;
                    case 8:
                        if (sheet.getSkillItemBonus(skill) != null) {
                            align = Element.ALIGN_LEFT;
                            x = 476;
                            canvas.setFontAndSize(fontUserPlain, 4.5f);
                            str = sheet.getSkillItemBonus(skill);
                        }
                        break;
                }
                if (col == 0) {
                    hline(canvas, LEFT_X, y + 8f, RIGHT_X);
                }
                if (str != null && str.length() > 0) {
                    canvas.showTextAligned(align, str, x, y, 0);
                }
                y -= lineHeight;
            }
            canvas.endText();
        }
        float y0 = UPPER_Y;
        float y1 = y + 8f;
        box(canvas, LEFT_X, y0, RIGHT_X, y1);
        vline(canvas, 265, y0, y1);
        vline(canvas, 300, y0, y1);
        vline(canvas, 335, y0, y1);
        vline(canvas, 370, y0, y1);
        vline(canvas, 405, y0, y1);
        vline(canvas, 440, y0, y1);
        vline(canvas, 475, y0, y1);
        canvas.setFontAndSize(fontBold, 8);
        canvas.beginText();
        y0 -= 8.5;
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page3.skill"), xVal[0], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.ranks"), xVal[2], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.newranks"), xVal[3], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.bonus.rank"), xVal[4], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.bonus.group"), xVal[5], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.bonus.special"), xVal[6], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.bonus.total"), xVal[7], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page3.bonus.item"), xVal[8], y0, 0);
        canvas.endText();
    }

    /**
	 * skill categories
	 */
    private void drawSkillcategories(PdfContentByte canvas) {
        float[] xVal = new float[] { 60, 215, 261, 299, 331, 363, 406, 454, 498, 538 };
        float y = 0;
        for (int col = 0; col < xVal.length; col++) {
            canvas.beginText();
            canvas.setFontAndSize(fontUser, 8);
            y = 738.5f;
            boolean spellsStarted = false;
            for (SkillCategory sg : data.getSkillCategories()) {
                int align = Element.ALIGN_CENTER;
                int insets = 0;
                String str = null;
                switch(col) {
                    case 0:
                        align = Element.ALIGN_LEFT;
                        canvas.setFontAndSize(fontRegular, 8);
                        str = sg.getName();
                        if (sg.getRankType().isMagical() && !sg.getRankType().isProgressionMagic()) {
                            insets = 6;
                        }
                        break;
                    case 1:
                        canvas.setFontAndSize(fontRegular, 8);
                        StringBuffer sb = new StringBuffer();
                        for (StatEnum stat : sheet.getSkillcategoryStats(sg)) {
                            if (sb.length() > 0) {
                                sb.append("/");
                            }
                            sb.append(RESOURCE.getString("StatEnum." + stat.name() + ".short"));
                        }
                        str = sb.toString();
                        break;
                    case 2:
                        str = sheet.getSkillcost(sg).toString();
                        break;
                    case 3:
                        if (sg.getRankType().isGroupRankEditable()) {
                            BigDecimal rank = sheet.getSkillcategoryRank(sg).getRank();
                            if (rank.compareTo(BigDecimal.valueOf(0)) == 0) {
                                str = "";
                            } else {
                                str = "" + sheet.getSkillcategoryRank(sg).getRank();
                            }
                        } else {
                            str = RESOURCE.getString("pdf.rank.notavailable.short");
                        }
                        break;
                    case 4:
                        canvas.setFontAndSize(fontWidget, 8);
                        str = getWidgetNewRanksSymbol(sg, null);
                        break;
                    case 5:
                        if (sg.getRankType().isGroupRankEditable()) {
                            str = format(sheet.getSkillcategoryRankBonus(sg), true);
                        } else {
                            str = "";
                        }
                        break;
                    case 6:
                        str = format(sheet.getSkillcategoryStatBonus(sg), false);
                        break;
                    case 7:
                        str = format(sheet.getProfession().getSkillgroupBonus(sg.getId().intValue()), true);
                        break;
                    case 8:
                        Integer specialBonus = sheet.getSkillcategoryRank(sg).getSpecialBonus();
                        int specialBonusInt = 0;
                        if (specialBonus != null) {
                            specialBonusInt += specialBonus.intValue();
                        }
                        specialBonusInt += sheet.getSkillcategorySpecial1Bonus(sg);
                        str = format(specialBonusInt, true);
                        break;
                    case 9:
                        str = format(sheet.getSkillcategoryTotalBonus(sg), true);
                        break;
                    default:
                        str = "";
                }
                if (!spellsStarted && sg.getRankType().isMagical() && !sg.getRankType().isProgressionMagic()) {
                    spellsStarted = true;
                    if (col == 0) {
                        hline(canvas, LEFT_X, y + 8f, RIGHT_X);
                        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("rolemaster.spells"), xVal[col], y, 0);
                    }
                    y -= 10.68f;
                }
                if (str != null && str.length() > 0) {
                    canvas.showTextAligned(align, str, xVal[col] + insets, y, 0);
                }
                if (col == 0) {
                    hline(canvas, LEFT_X, y + 8f, RIGHT_X);
                }
                y -= 10.68f;
            }
            canvas.endText();
        }
        float y0 = UPPER_Y;
        float y1 = y + 8f;
        box(canvas, LEFT_X, y0, RIGHT_X, y1);
        vline(canvas, 190, y0, y1);
        vline(canvas, 240, y0, y1);
        vline(canvas, 282, y0, y1);
        vline(canvas, 316, y0, y1);
        vline(canvas, 345, y0, y1);
        vline(canvas, 385, y0, y1);
        vline(canvas, 431, y0, y1);
        vline(canvas, 477, y0, y1);
        vline(canvas, 523, y0, y1);
        canvas.setFontAndSize(fontBold, 8);
        canvas.beginText();
        y0 -= 8.5;
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page2.skillgroup"), xVal[0], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.stats"), xVal[1], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.dpcosts"), xVal[2], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.ranks"), xVal[3], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.newranks"), xVal[4], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.bonus.rank"), xVal[5], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.bonus.stat"), xVal[6], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.bonus.profession"), xVal[7], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.bonus.special"), xVal[8], y0, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page2.bonus.total"), xVal[9], y0, 0);
        canvas.endText();
    }

    /**
	 * 
	 * @param sg skillgroup, not {@code null}
	 * @param skill the skill or {@code null}
	 * @return
	 */
    private String getWidgetNewRanksSymbol(SkillCategory skillgroup, ISkill skill) {
        String str;
        if (skillgroup.getRankType().isCombined()) {
            str = "V";
        } else if (skillgroup.getRankType().isLimited()) {
            str = ":";
        } else if (skillgroup.getRankType().isProgressionBody() || skillgroup.getRankType().isProgressionMagic()) {
            str = "m";
        } else {
            SkillType type = null;
            if (skill != null) {
                type = sheet.getSkillType(skill);
            } else {
                type = sheet.getSkillgroupType(skillgroup);
            }
            if (type != null) {
                switch(type) {
                    case OCCUPATIONAL:
                        str = "nnn";
                        break;
                    case EVERYMAN:
                        str = "nno";
                        break;
                    case RESTRICTED:
                        str = "woo";
                        break;
                    default:
                        str = "noo";
                }
            } else {
                str = "ooo";
            }
        }
        return str;
    }

    private void drawNamesPage1(PdfContentByte canvas) {
        box(canvas, LEFT_X, 780, RIGHT_X, 750);
        vline(canvas, 165, 780, 750);
        vline(canvas, 272, 780, 750);
        box(canvas, LEFT_X, 740, PAGE1_LEFTBOX_RIGHTX, BOTTOM_Y);
        box(canvas, PAGE1_RIGHTBOX_LEFTX, 740, RIGHT_X, BOTTOM_Y);
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.exppoints"), 110, 771, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("ui.basic.level") + ":", 220, 771, 0);
        canvas.endText();
        labeledUserText(canvas, RESOURCE.getString("pdf.page1.charactername"), sheet.getCharacterName(), 276, 769, 0, fontHeadline, 8);
        labeledUserText(canvas, RESOURCE.getString("pdf.page1.playername"), sheet.getPlayerName(), 276, 755, 0, fontHeadline, 8);
        showUserText(canvas, 18, 110, 754, "" + sheet.getEp(), Element.ALIGN_CENTER);
        showUserText(canvas, 18, 220, 754, "" + sheet.getLevel(), Element.ALIGN_CENTER);
    }

    private float page1Stats(PdfContentByte canvas) {
        float LINE_HEIGHT = 14.5f;
        float y = 728f;
        float[] xVal = new float[] { PAGE1_RIGHTBOX_LEFTX + 4, 337, 358, 382, 408, 442, 482 };
        canvas.beginText();
        canvas.setFontAndSize(fontBold, 8);
        String[] basicBonus = StringUtils.split(RESOURCE.getString("pdf.page1.stat.bonus.basic"));
        int basicBonusIdx = 0;
        String[] racialBonus = StringUtils.split(RESOURCE.getString("pdf.page1.stat.bonus.race"));
        int racialBonusIdx = 0;
        String[] specialBonus = StringUtils.split(RESOURCE.getString("pdf.page1.stat.bonus.special"));
        int specialBonusIdx = 0;
        String[] totalBonus = StringUtils.split(RESOURCE.getString("pdf.page1.stat.bonus.total"));
        int totalBonusIdx = 0;
        if (basicBonus.length > 1) {
            canvas.showTextAligned(Element.ALIGN_CENTER, basicBonus[basicBonusIdx++], xVal[3], y, 0);
        }
        if (racialBonus.length > 1) {
            canvas.showTextAligned(Element.ALIGN_CENTER, racialBonus[racialBonusIdx++], xVal[4], y, 0);
        }
        if (specialBonus.length > 1) {
            canvas.showTextAligned(Element.ALIGN_CENTER, specialBonus[specialBonusIdx++], xVal[5], y, 0);
        }
        if (totalBonus.length > 1) {
            canvas.showTextAligned(Element.ALIGN_CENTER, totalBonus[totalBonusIdx++], xVal[6], y, 0);
        }
        y -= 10;
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page1.stat.header"), xVal[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.stat.temp"), xVal[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.stat.potential"), xVal[2], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, basicBonus[basicBonusIdx], xVal[3], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, racialBonus[racialBonusIdx], xVal[4], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, specialBonus[specialBonusIdx], xVal[5], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, totalBonus[totalBonusIdx], xVal[6], y, 0);
        canvas.endText();
        y -= LINE_HEIGHT;
        for (StatEnum stat : StatEnum.values()) {
            for (int col = 0; col < xVal.length; col++) {
                String str = null;
                switch(col) {
                    case 0:
                        canvas.beginText();
                        canvas.setFontAndSize(fontRegular, 8);
                        canvas.showTextAligned(Element.ALIGN_LEFT, stat.getFullI18N(), xVal[col], y, 0);
                        canvas.endText();
                        break;
                    case 1:
                        str = "" + sheet.getStatTemp(stat);
                        break;
                    case 2:
                        str = "" + sheet.getStatPot(stat);
                        break;
                    case 3:
                        str = format(sheet.getStatBonus(stat), false);
                        break;
                    case 4:
                        str = format(sheet.getRace().getStatBonus(stat), false);
                        break;
                    case 5:
                        int miscBonus = sheet.getStatMiscBonus(stat) + sheet.getStatMisc2Bonus(stat);
                        str = format(miscBonus, false);
                        break;
                    case 6:
                        str = format(sheet.getStatBonusTotal(stat), false);
                        box(canvas, xVal[col] - 9, y + 8, xVal[col] + 10, y - 3);
                        break;
                    default:
                        str = "";
                }
                if (col > 0 && col < (xVal.length - 1)) {
                    showUserText(canvas, 8, xVal[col], y, "___", Element.ALIGN_CENTER);
                }
                showUserText(canvas, 8, xVal[col], y, str, Element.ALIGN_CENTER);
            }
            y -= LINE_HEIGHT;
        }
        hline(canvas, PAGE1_RIGHTBOX_LEFTX, y, RIGHT_X);
        y -= LINE_HEIGHT;
        return y;
    }

    private void createPage1(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        URL imageUrl = getClass().getResource("/images/rmlogo.png");
        Image logo = Image.getInstance(imageUrl);
        logo.setAbsolutePosition(328f, 782f);
        logo.scaleToFit(226, 120);
        canvas.addImage(logo, false);
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 14);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page.title"), 92, 818, 0);
        canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page1.title2"), 92, 802, 0);
        canvas.endText();
        if (log.isDebugEnabled()) log.debug("loading image runes");
        Image raceRune = loadImage("/images/runes/race/" + sheet.getCulture().getRune());
        if (raceRune != null) {
            raceRune.setAbsolutePosition(495, 653);
            raceRune.scaleAbsolute(55f, 55f);
            canvas.addImage(raceRune, true);
        }
        Image profRune = loadImage("/images/runes/prof/" + sheet.getProfession().getRune());
        if (profRune != null) {
            profRune.setAbsolutePosition(495, 580);
            profRune.scaleAbsolute(55, 55);
            canvas.addImage(profRune, true);
        }
        if (log.isDebugEnabled()) log.debug("processing text page 1");
        float y = 0;
        drawNamesPage1(canvas);
        y = page1RaceProfArmorDB(canvas, 728f);
        y = page1Resistance(canvas, y, true);
        y = page1RaceAttributes(canvas, y);
        y = page1Characteristics(canvas, y);
        int lineNumb = (int) Math.floor((y - BOTTOM_Y) / PAGE1_LEFTBOX_LINE_HEIGHT);
        for (int i = 0; i < lineNumb; i++) {
            labeledUserText(canvas, "", "", LEFT_X + 4, y, PAGE1_LEFTBOX_RIGHTX - 4, fontRegular, 8);
            y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        y = page1Stats(canvas);
        y = page1Favorites(canvas, y, 120, true);
        if (y > 131) {
            canvas.beginText();
            canvas.setFontAndSize(fontHeadline, 8);
            float centerX = PAGE1_RIGHTBOX_LEFTX + (RIGHT_X - PAGE1_RIGHTBOX_LEFTX) / 2;
            canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.header.notes"), centerX, y, 0);
            canvas.endText();
        }
        drawHitsPPExhaust(canvas, 111);
        footer(canvas);
    }

    private void createPage2(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        header2(canvas, 2);
        drawSkillcategories(canvas);
        footer(canvas);
    }

    private void createPage3(Document document, PdfWriter writer) throws BadElementException, MalformedURLException, IOException, DocumentException {
        List<ISkill> skills = new ArrayList<ISkill>();
        Map<ISkill, Rank> ranks = new HashMap<ISkill, Rank>();
        for (Rank rank : sheet.getSkillRanks()) {
            ISkill skill = sheet.getSkill(rank.getId());
            if (skill != null) {
                skills.add(skill);
                ranks.put(skill, rank);
            }
        }
        Collections.sort(skills, new Comparator<ISkill>() {

            @Override
            public int compare(ISkill sk1, ISkill sk2) {
                if (!sk1.isSpelllist() && sk2.isSpelllist()) {
                    return -1;
                }
                if (sk1.isSpelllist() && !sk2.isSpelllist()) {
                    return 1;
                }
                return sk1.getName().compareTo(sk2.getName());
            }
        });
        final int maxSkillsPerPage = 64;
        int pageCount = (int) Math.ceil((float) skills.size() / (float) maxSkillsPerPage);
        for (int i = 0; i < pageCount; i++) {
            int toIndex = (i + 1) * maxSkillsPerPage;
            if (toIndex >= skills.size()) {
                toIndex = skills.size() - 1;
            }
            List<ISkill> subList = skills.subList(i * maxSkillsPerPage, toIndex);
            if (!document.newPage()) {
                log.error("Could not create a new page 'skills'");
            }
            PdfContentByte canvas = writer.getDirectContent();
            header2(canvas, 3);
            page3SkillsPage(canvas, subList, ranks);
            footer(canvas);
        }
    }

    /**
	 * Coins and riches
	 */
    private void createPage4(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        header2(canvas, 4);
        box(canvas, LEFT_X, UPPER_Y, RIGHT_X, BOTTOM_Y);
        float centerX = LEFT_X + (RIGHT_X - LEFT_X) / 2;
        vline(canvas, centerX, UPPER_Y, BOTTOM_Y);
        page4Equipment(canvas);
        float y = page4Jewelry(canvas, UPPER_Y);
        float yBottom = page4CharImageAndSize(canvas);
        page4MagicItems(canvas, y, yBottom);
        footer(canvas);
    }

    /**
	 * all skills list
	 */
    private void createPage5(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        header2(canvas, 5);
        box(canvas, LEFT_X, UPPER_Y, RIGHT_X, BOTTOM_Y);
        List<ISkill> allSkills = data.getSkills();
        List<String> skills = new ArrayList<String>();
        for (ISkill skill : allSkills) {
            if (!skill.isSpelllist() && (skill.getScope() == null || skill.getScope().equals(sheet.getRace().getScope()))) {
                SkillCategory category = sheet.getSkillcategory(skill);
                if (!category.getRankType().isCostSwitchable()) {
                    skills.add(skill.getName() + " " + format(sheet.getSkillTotalBonus(skill), false));
                }
            }
        }
        Collections.sort(skills);
        float lineHeight = 8;
        float fontSize = 7f;
        int colWidth = (RIGHT_X - LEFT_X) / 5;
        float x = LEFT_X + 2;
        float y = UPPER_Y - 8;
        canvas.beginText();
        canvas.setFontAndSize(fontRegular, fontSize);
        for (String text : skills) {
            float linefontSize = fontSize;
            float maxLength = 0;
            boolean modifiedFontSize = false;
            do {
                if (maxLength > colWidth) {
                    linefontSize -= 0.25f;
                    modifiedFontSize = true;
                }
                maxLength = fontRegular.getWidthPoint(text, linefontSize);
            } while (maxLength > colWidth);
            if (modifiedFontSize) {
                canvas.setFontAndSize(fontRegular, linefontSize);
            }
            canvas.showTextAligned(Element.ALIGN_LEFT, text, x, y, 0);
            if (modifiedFontSize) {
                canvas.setFontAndSize(fontRegular, fontSize);
            }
            y -= lineHeight;
            if (y < (BOTTOM_Y + 2)) {
                x += colWidth;
                y = UPPER_Y - 8;
            }
        }
        canvas.endText();
        footer(canvas);
    }

    private void createPage6CombatStatus(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
        header2(canvas, 6);
        box(canvas, LEFT_X, UPPER_Y, RIGHT_X, BOTTOM_Y);
        final float lineHeight = 11;
        canvas.beginText();
        canvas.setFontAndSize(fontRegular, 8);
        float x1 = 0;
        float x2 = 0;
        float y = 0;
        for (int i = 1; i <= 6; i++) {
            String textLine = RESOURCE.getString("pdf.page6.order" + i);
            if (textLine.contains("{0}")) {
                String quBonus = format(sheet.getStatBonusTotal(StatEnum.QUICKNESS), false);
                if ("0".equals(quBonus)) {
                    quBonus = "+0";
                }
                textLine = MessageFormat.format(textLine, quBonus);
            }
            canvas.showTextAligned(Element.ALIGN_LEFT, textLine, LEFT_X + 5, UPPER_Y + (-1 * lineHeight * i), 0);
            float x0 = fontRegular.getWidthPoint(RESOURCE.getString("pdf.page6.order" + i), 8);
            if (x1 == 0 || x1 < x0) {
                x1 = x0;
            }
        }
        for (int i = 1; i <= 4; i++) {
            canvas.showTextAligned(Element.ALIGN_LEFT, RESOURCE.getString("pdf.page6.exhaus" + i), LEFT_X + 15 + x1, UPPER_Y + (-1 * lineHeight * i), 0);
            float x0 = fontRegular.getWidthPoint(RESOURCE.getString("pdf.page6.exhaus" + i), 8);
            if (x2 == 0 || x2 < x0) {
                x2 = x0;
            }
        }
        canvas.endText();
        float x = LEFT_X + 25 + x1 + x2;
        labeledUserText(canvas, RESOURCE.getString("pdf.page6.notes"), "", x, UPPER_Y - 11, RIGHT_X - 5, fontRegular, 8);
        for (int i = 2; i <= 6; i++) {
            labeledUserText(canvas, "", "", LEFT_X + 25 + x1 + x2, UPPER_Y + (-11 * i), RIGHT_X - 5, fontRegular, 8);
        }
        hline(canvas, LEFT_X, UPPER_Y - 73, RIGHT_X);
        y = UPPER_Y + (-1 * lineHeight * 6) - 8;
        y = page6ConcussionHits(canvas, lineHeight, y);
        y = page6PowerPoints(canvas, lineHeight, y);
        y = page6ExhaustionPointsAndMovement(canvas, lineHeight, y, true);
        y = page6Injuries(canvas, lineHeight, y);
        page6OutlineImage(canvas, y);
        footer(canvas);
    }

    private void page6OutlineImage(PdfContentByte canvas, final float y) {
        try {
            if (sheet.getPrintOutlineImage() && sheet.getRace().getOutline() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("/images/outline/");
                sb.append(sheet.getRace().getOutline().toLowerCase());
                sb.append("_");
                sb.append(sheet.getCharacteristics().isFemale() ? "f" : "m");
                sb.append(".png");
                URL resUrl = PDFCreator.class.getResource(sb.toString());
                Image charImage = Image.getInstance(resUrl);
                float maxHeight = (y - BOTTOM_Y) * 0.75f;
                float height = charImage.getHeight();
                float width = charImage.getWidth();
                if (log.isDebugEnabled()) log.debug("character image size w x h = " + width + " x " + height);
                if (height > maxHeight) {
                    float f = height / maxHeight;
                    height = maxHeight;
                    width = width / f;
                    if (log.isDebugEnabled()) log.debug("character image scaled (height) to w x h = " + width + " x " + height);
                }
                float x = LEFT_X + (RIGHT_X - LEFT_X) / 2 - (width / 2);
                float y1 = y - height - 10;
                charImage.setAbsolutePosition(x, y1);
                charImage.scaleAbsolute(width, height);
                canvas.addImage(charImage, true);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private float page6Injuries(PdfContentByte canvas, final float lineHeight, final float initialY) {
        canvas.beginText();
        canvas.setFontAndSize(fontBold, 8);
        float y = initialY - lineHeight;
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries"), LEFT_X + ((RIGHT_X - LEFT_X) / 2), y, 0);
        y -= lineHeight;
        float col = (RIGHT_X - LEFT_X) / 3;
        float[] xColCenter = new float[] { LEFT_X + col / 2, LEFT_X + 1.5f * col, LEFT_X + 2.5f * col };
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries.light"), xColCenter[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries.medium"), xColCenter[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries.severe"), xColCenter[2], y, 0);
        canvas.setFontAndSize(fontRegular, 6);
        y -= 7;
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries.light.descr"), xColCenter[0], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries.medium.descr"), xColCenter[1], y, 0);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page6.injuries.severe.descr"), xColCenter[2], y, 0);
        canvas.setFontAndSize(fontRegular, 8);
        for (int i = 0; i < 5; i++) {
            y -= lineHeight;
            for (int colIdx = 0; colIdx < 3; colIdx++) {
                canvas.showTextAligned(Element.ALIGN_CENTER, "_______________________________________", xColCenter[colIdx], y, 0);
            }
        }
        hline(canvas, LEFT_X, y - 6, RIGHT_X);
        y -= 8;
        canvas.endText();
        return y;
    }

    private void page4MagicItems(PdfContentByte canvas, float y, float yBottom) {
        float leftX = LEFT_X + ((RIGHT_X - LEFT_X) / 2);
        float centerX = leftX + ((RIGHT_X - LEFT_X) / 4);
        canvas.beginText();
        canvas.setFontAndSize(fontHeadline, 8);
        canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("rolemaster.magicitems.header"), centerX, y, 0);
        canvas.endText();
        List<MagicalItem> magicalitems = sheet.getMagicalitems();
        for (int i = 0; i < magicalitems.size(); i++) {
            y -= 11;
            MagicalItem item = magicalitems.get(i);
            labeledUserText(canvas, (i + 1) + ": ", item.getName(), leftX + 4, y, RIGHT_X - 4, fontUser, 8);
            canvas.setFontAndSize(fontUser, 6);
            for (MagicalFeature feat : item.getFeatures()) {
                String description = feat.getDescription();
                if (feat.getType().isBonusAvailable() && feat.getBonus() != null) {
                    description += " " + format(feat.getBonus().intValue(), false);
                }
                ColumnText ct = new ColumnText(canvas);
                Phrase phrase = new Phrase(description, new Font(fontUser, 7));
                ct.setSimpleColumn(phrase, leftX + 20, y, RIGHT_X - 5, BOTTOM_Y + 5, 8, Element.ALIGN_LEFT);
                try {
                    ct.go();
                    if (log.isDebugEnabled()) log.debug("description item " + i + " lines written: " + ct.getLinesWritten());
                    y -= (8 * ct.getLinesWritten() + 1);
                } catch (DocumentException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        y -= 11;
        while (y > yBottom) {
            labeledUserText(canvas, "", "", leftX + 4, y, RIGHT_X - 4, fontRegular, 8);
            y -= 11;
        }
    }
}
