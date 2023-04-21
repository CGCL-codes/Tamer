package net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.options;

import java.util.Locale;
import javax.xml.bind.JAXBException;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.impl.xml.AbstractXMLFileDAO;
import net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.XMLJAXBHelperDAO;
import net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.options.generated.Options;
import net.sourceforge.mazix.persistence.dao.options.OptionsDAO;
import net.sourceforge.mazix.persistence.dto.options.OptionsDTO;
import org.xml.sax.SAXException;

/**
 * JAXB DAO implementation which defines data access methods for options parsing the XML file
 * described by the {@code options.xsd} file using JAXB.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.7
 * @version 0.7
 */
public class XMLJAXBOptionsDAO extends AbstractXMLFileDAO implements OptionsDAO {

    /** Serial version UID. */
    private static final long serialVersionUID = -1031838641759596069L;

    /** The JAXB helper to manage marshalling and unmarshalling for the current JAXB class. */
    private XMLJAXBHelperDAO jaxbHelper = null;

    /**
     * Full constructor.
     * 
     * @param optionsFilePath
     *            the file path directory where to find the file to perform data access operation,
     *            mustn't be <code>null</code>.
     * @param optionsFileName
     *            the file name to perform data access operation, mustn't be <code>null</code>.
     * @param optionsSchemaFileName
     *            the XSD options schema file pathname to perform data access operation, mustn't be
     *            <code>null</code>.
     * @since 0.7
     */
    public XMLJAXBOptionsDAO(final String optionsFilePath, final String optionsFileName, final String optionsSchemaFileName) {
        super(optionsFilePath, optionsFileName, optionsSchemaFileName);
        jaxbHelper = new XMLJAXBHelperDAO(Options.class);
    }

    /**
     * Gets the value of jaxbHelper.
     * 
     * @return the value of jaxbHelper.
     * @since 0.8
     */
    private XMLJAXBHelperDAO getJAXBHelper() {
        return jaxbHelper;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final OptionsDTO getOptions() throws PersistenceException {
        OptionsDTO options = null;
        try {
            options = getOptions(getFilePath(), getFileName(), getSchemaFileName(), getJAXBHelper());
        } catch (final SAXException e) {
            throw new PersistenceException(e);
        } catch (final JAXBException e) {
            throw new PersistenceException(e);
        }
        return options;
    }

    /**
     * Parses and builds a {@link OptionsDTO} instance from an XML file, throws an exception if any
     * errors has been detected. May throw a {@link NullPointerException} if {@code pathname} or
     * {@code schema} are <code>null</code>.
     * 
     * @param optionsFileName
     *            the file pathname to parse, mustn't be <code>null</code>.
     * @param optionsSchemaFileName
     *            the file schema pathname for validation, mustn't be <code>null</code>.
     * @param helper
     *            the JAXB helper to manage marshalling and unmarshalling for the current JAXB
     *            class, mustn't be <code>null</code>.
     * @return a filled {@link OptionsDTO} instance if the XML file has been correctly parsed,
     *         otherwise this method returns a default {@link OptionsDTO} instance.
     * @throws SAXException
     *             if the XML file can't be parsed.
     * @throws JAXBException
     *             if the XML file can't be parsed.
     * @see #getOptions()
     * @since 0.5
     */
    protected final OptionsDTO getOptions(final String optionsFilePath, final String optionsFileName, final String optionsSchemaFileName, final XMLJAXBHelperDAO helper) throws JAXBException, SAXException {
        assert optionsFilePath != null : "optionsFilePath is null";
        assert optionsFileName != null : "optionsFileName is null";
        assert optionsSchemaFileName != null : "optionsSchemaFileName is null";
        assert helper != null : "helper is null";
        final Options op = (Options) helper.unmarshal(optionsFilePath + optionsFileName, optionsFilePath + optionsSchemaFileName);
        final String defaultProfile = op.getDefaultProfile();
        final Locale language = new Locale(op.getLanguage());
        final int effectsVolume = op.getEffectsVolume();
        final int musicVolume = op.getMusicVolume();
        final int height = op.getHeight();
        final int width = op.getWidth();
        return new OptionsDTO(effectsVolume, musicVolume, defaultProfile, language, height, width);
    }

    /**
     * Writes and saves a {@link org.mazix.kernel.GameOptions} instance in a XML file, throws an
     * exception if any errors has been detected. May throw a {@link java.lang.NullPointerException}
     * if {@code gameOptions}, {@code pathname} or {@code schema} are <code>null</code>.
     * 
     * @param options
     *            the {@link org.mazix.kernel.GameOptions} instance to be saved, can't be
     *            <code>null</code>.
     * @param optionsFilePath
     *            the file path directory where to find the file to perform data access operation,
     *            mustn't be <code>null</code>.
     * @param optionsFileName
     *            the file pathname to write, mustn't be <code>null</code>.
     * @param optionsSchemaFileName
     *            the file schema pathname for validation, mustn't be <code>null</code>.
     * @param helper
     *            the JAXB helper to manage marshalling and unmarshalling for the current JAXB
     *            class, mustn't be <code>null</code>.
     * @throws JAXBException
     *             if the XML file can't be written.
     * @see #updateOptions(OptionsDTO)
     * @since 0.5
     */
    private void updateOptions(final Options options, final String optionsFilePath, final String optionsFileName, final String optionsSchemaFileName, final XMLJAXBHelperDAO helper) throws JAXBException {
        assert options != null : "options is null";
        assert optionsFilePath != null : "optionsFilePath is null";
        assert optionsFileName != null : "optionsFileName is null";
        assert optionsSchemaFileName != null : "optionsSchemaFileName is null";
        assert helper != null : "helper is null";
        helper.marshal(options, optionsSchemaFileName, optionsFilePath + optionsFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final void updateOptions(final OptionsDTO optionsDTO) throws PersistenceException {
        assert optionsDTO != null : "optionsDTO is null";
        try {
            final Options options = new Options();
            options.setDefaultProfile(optionsDTO.getProfileName());
            options.setEffectsVolume(optionsDTO.getEffectsVolume());
            options.setHeight(optionsDTO.getHeight());
            options.setLanguage(optionsDTO.getLanguage().getLanguage());
            options.setMusicVolume(optionsDTO.getMusicVolume());
            options.setWidth(optionsDTO.getWidth());
            updateOptions(options, getFilePath(), getFileName(), getSchemaFileName(), getJAXBHelper());
        } catch (final JAXBException e) {
            throw new PersistenceException(e);
        }
    }
}
