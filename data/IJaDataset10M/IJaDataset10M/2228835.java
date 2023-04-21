package org.jcvi.vics.web.gwt.detail.server.bse.orf;

import org.jcvi.vics.model.genomics.AccessionIdentifierUtil;
import org.jcvi.vics.model.genomics.BaseSequenceEntity;
import org.jcvi.vics.model.genomics.ORF;
import org.jcvi.vics.server.access.hibernate.DaoException;
import org.jcvi.vics.shared.genomics.SequenceEntityFactory;
import org.jcvi.vics.web.gwt.detail.server.bse.BSEntityInitializer;
import org.jcvi.vics.web.gwt.detail.server.bse.BSEntityServiceImpl;
import java.util.Map;

/**
 * This class is used to initialize the Base Sequence Entity from the defline
 */
public class ORFInitializer extends BSEntityInitializer {

    public ORFInitializer(BSEntityServiceImpl bseService) {
        super(bseService);
    }

    public boolean checkEntityTypeAccession(BaseSequenceEntity bse) {
        return AccessionIdentifierUtil.isORF(bse.getCameraAcc()) && bse instanceof ORF;
    }

    public boolean recognizeAccessionNo(String accessionNo) {
        return AccessionIdentifierUtil.isORF(accessionNo);
    }

    /**
     * Returns a GWT-consumable BaseSequenceEntity instance given a BaseSequenceEntity accession
     *
     * @param accession the camera accession
     * @return BaseSequenceEntity instance
     */
    public BaseSequenceEntity retrieveBseEntity(String accession) throws DaoException {
        ORF orf = null;
        BaseSequenceEntity bsEntity = super.retrieveBseEntity(accession);
        if (bsEntity != null) {
            if (bsEntity instanceof ORF) {
                orf = (ORF) bsEntity;
            } else {
                orf = (ORF) SequenceEntityFactory.duplicateSequenceEntity(bsEntity);
            }
        }
        return orf;
    }

    private String getORFSourceAccessionFromDefLine(Map<String, String> orfDefLine) {
        String readAccessionNo = orfDefLine.get("read_id");
        if (readAccessionNo != null) {
            return readAccessionNo;
        } else {
            return orfDefLine.get("source_dna_id");
        }
    }

    /**
     * sets ORF alignment attributes
     *
     * @param orf        the entity of interest
     * @param orfDefLine the entity's defLine
     */
    private void setORFAlignmentAttributes(ORF orf, Map<String, String> orfDefLine) {
        String translationStart = orfDefLine.get("translation_start");
        if (translationStart == null || translationStart.length() == 0) {
            translationStart = orfDefLine.get("begin");
        }
        if (translationStart != null && translationStart.length() > 0) {
            orf.setDnaBegin(Integer.valueOf(translationStart));
        }
        String translationEnd = orfDefLine.get("translation_end");
        if (translationEnd == null || translationEnd.length() == 0) {
            translationEnd = orfDefLine.get("end");
        }
        if (translationEnd != null && translationEnd.length() > 0) {
            orf.setDnaEnd(Integer.valueOf(translationEnd));
        }
        String orientation = orfDefLine.get("orientation");
        if (orientation != null) {
            if (orientation.equals("reverse")) {
                orf.setDnaOrientation(new Integer(-1));
            } else if (orientation.equals("forward")) {
                orf.setDnaOrientation(new Integer(1));
            } else {
                orf.setDnaOrientation(Integer.valueOf(orientation));
            }
        }
        orf.setDnaAcc(getORFSourceAccessionFromDefLine(orfDefLine));
    }
}
