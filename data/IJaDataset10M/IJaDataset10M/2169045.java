package it.webscience.kpeople.service.datatypes.converter;

import it.webscience.kpeople.service.datatypes.PatternState;
import it.webscience.kpeople.service.datatypes.PatternType;
import java.util.ArrayList;
import java.util.List;

public final class PatternTypeConverter {

    /**
     * Costruttore privato.
     */
    private PatternTypeConverter() {
    }

    /**
     * Converte da Service a BE.
     * @param in oggetto PatternType Service
     * @return oggetto PatternType BE
     */
    public static it.webscience.kpeople.be.PatternType toBE(final PatternType in) {
        it.webscience.kpeople.be.PatternType out = new it.webscience.kpeople.be.PatternType();
        out.setIdPatternType(in.getIdPatternType());
        out.setName(in.getName());
        out.setDescription(in.getDescription());
        out.setActive(in.isActive());
        out.setShowInList(in.isShowInList());
        out.setVersion(in.getVersion());
        out.setRelatedForm(in.getRelatedForm());
        out.setPatternTypeCode(in.getPatternTypeCode());
        out.setOrdering(in.getOrdering());
        out.setWaitingActivity(in.isWaitingActivity());
        out.setActivitiProcessDefinitionId(in.getActivitiProcessDefinitionId());
        out.setHpmPatternTypeId(in.getHpmPatternTypeId());
        DataTraceClassConverter.toBE(in, out);
        return out;
    }

    /**
     * Converte da array Service a List BE.
     * @param in array di oggetti PatternType Service
     * @return Lista BE
     */
    public static List<it.webscience.kpeople.be.PatternType> toBE(final PatternType[] in) {
        List<it.webscience.kpeople.be.PatternType> out = new ArrayList<it.webscience.kpeople.be.PatternType>();
        for (int i = 0; i < in.length; i++) {
            out.add(toBE(in[i]));
        }
        return out;
    }

    /**
     * Converte da List PatternType BE a array PatternType Service.
     * @param in List PatternType BE
     * @return array PatternType Service
     */
    public static PatternType[] toService(final List<it.webscience.kpeople.be.PatternType> in) {
        PatternType[] out = new PatternType[in.size()];
        for (int i = 0; i < in.size(); i++) {
            out[i] = toService(in.get(i));
        }
        return out;
    }

    /**
     * Converte oggetto PatternType BE in Service.
     * @param in oggetto PatternType BE
     * @return oggetto PatternType Service
     */
    public static PatternType toService(final it.webscience.kpeople.be.PatternType in) {
        PatternType out = null;
        if (in != null) {
            out = new PatternType();
            out.setIdPatternType(in.getIdPatternType());
            out.setName(in.getName());
            out.setDescription(in.getDescription());
            out.setActive(in.isActive());
            out.setShowInList(in.isShowInList());
            out.setVersion(in.getVersion());
            out.setRelatedForm(in.getRelatedForm());
            out.setPatternTypeCode(in.getPatternTypeCode());
            out.setOrdering(in.getOrdering());
            out.setWaitingActivity(in.isWaitingActivity());
            out.setActivitiProcessDefinitionId(in.getActivitiProcessDefinitionId());
            out.setHpmPatternTypeId(in.getHpmPatternTypeId());
        }
        return out;
    }
}
