package it.webscience.kpeople.service.datatypes.converter;

import it.webscience.kpeople.service.datatypes.ProcessMetadataSet;

/**
 * @author depascalis Classe di conversione tra la classe ProcessMetadataSet BE
 *         e ProcessMetadataSet service.
 */
public final class ProcessMetadataSetConverter {

    /**
     * Costruttore privato della classe.
     */
    private ProcessMetadataSetConverter() {
    }

    /**
     * @param in ProcessMetadataSet Service
     * @return ProcessMetadataset be
     */
    public static it.webscience.kpeople.be.ProcessMetadataSet toBE(final ProcessMetadataSet in) {
        it.webscience.kpeople.be.ProcessMetadataSet out = new it.webscience.kpeople.be.ProcessMetadataSet();
        if (in.getCmpMetadataList() != null) {
            out.setCmpMetadataList(ProcessCompoundMetadataConverter.toBE(in.getCmpMetadataList()));
        }
        return out;
    }

    public static it.webscience.kpeople.service.datatypes.ProcessMetadataSet toService(final it.webscience.kpeople.be.ProcessMetadataSet in) {
        it.webscience.kpeople.service.datatypes.ProcessMetadataSet out = new it.webscience.kpeople.service.datatypes.ProcessMetadataSet();
        if (in.getCmpMetadataList() != null) {
            out.setCmpMetadataList(ProcessCompoundMetadataConverter.toService(in.getCmpMetadataList()));
        }
        return out;
    }
}
