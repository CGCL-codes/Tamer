package com.xmultra.processor.nitf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.xmultra.processor.news.Characters;
import com.xmultra.processor.news.PatternsBuilderParams;
import com.xmultra.util.InitMapHolder;
import com.xmultra.util.InvalidConfigFileFormatException;

/**
 * Handles the processing of special characters such as accented characters, symbols, and
 * icons. Normalizes characters to standard HTML entities.
 *
 * @author      Wayne W. Weber
 * @version     $Revision: #1 $
 * @since       1.4
 */
class Nxf_Characters extends NitfXformer {

    private Characters characters = null;

    private HashMap characterReplaceMap = null;

    private HashMap characterSearchMap = null;

    private PatternsBuilderParams charSearchPatParams = null;

    private Object[] characterMapKeys = null;

    /**
     * Does a setup be creating and getting references to utilities.
     *
     * @param charactersEl      The Characters configuration element associated with this
     *                          particular Xformer.
     *
     * @param nitfProcessorCfg  Holds the NitfProcessorConfig data & methods.
     *
     * @param imh               Holds references to utility and log objects.
     *
     * @param nitfXformerUtils  Has utility methods shared by Xformers.
     *
     * @return True if initialization is successful.
     */
    boolean init(Element charactersEl, NitfProcessorConfig nitfProcessorCfg, InitMapHolder imh, NitfXformerUtils nitfXformerUtils) {
        super.init(charactersEl, nitfProcessorCfg, imh, nitfXformerUtils);
        this.characters = new Characters(imh);
        this.charSearchPatParams = new PatternsBuilderParams();
        this.characterSearchMap = new HashMap();
        this.characterReplaceMap = new HashMap();
        this.characterMapKeys = new Object[0];
        List characterMapKeysList = new ArrayList();
        boolean useNumericEntities = false;
        String entityType = charactersEl.getAttribute(NitfProcessorConfig.ENTITY_TYPE);
        if (entityType.equals(NitfProcessorConfig.ENTITY_TYPE_NUMERIC)) {
            useNumericEntities = true;
        }
        characterReplaceMap = characters.getCharacterReplaceMap(useNumericEntities);
        try {
            Node batWingsEl = xmlParseUtils.getChildNode(charactersEl, NitfProcessorConfig.BAT_WINGS);
            charSearchPatParams.setPatternListMap(characterSearchMap);
            charSearchPatParams.setNode(batWingsEl);
            characterMapKeysList = characters.buildCharacterSearchPatterns(charSearchPatParams, characterReplaceMap);
            Node isolatinEl = xmlParseUtils.getChildNode(charactersEl, NitfProcessorConfig.ISO_LATIN_1);
            charSearchPatParams.setPatternListMap(characterSearchMap);
            charSearchPatParams.setNode(isolatinEl);
            List tmpList = characters.buildCharacterSearchPatterns(charSearchPatParams, characterReplaceMap);
            characterMapKeysList.addAll(tmpList);
            Node otherCharsEl = xmlParseUtils.getChildNode(charactersEl, NitfProcessorConfig.OTHER_CHARS);
            charSearchPatParams.setPatternListMap(characterSearchMap);
            charSearchPatParams.setNode(otherCharsEl);
            tmpList = characters.buildCharacterSearchPatterns(charSearchPatParams, characterReplaceMap);
            characterMapKeysList.addAll(tmpList);
            characterMapKeys = characterMapKeysList.toArray();
        } catch (InvalidConfigFileFormatException e) {
            msgEntry.setAppContext("init()");
            msgEntry.setDocInfo(nitfProcessorCfg.getNitfProcessorConfigFilename());
            msgEntry.setError(e.getMessage());
            logger.logWarning(msgEntry);
            return false;
        }
        return true;
    }

    /**
     * Applies a transform or process to an Nitf document.
     *
     */
    boolean xform(NitfDoc nitfDoc) {
        String nitfDocStr = parseCharactersFromString(nitfDoc.getDoc());
        nitfDoc.setDoc(nitfDocStr);
        return true;
    }

    /**
     * Performs character replacements specified in the Characters element in the
     * config file on a String passed as a parameter.
     *
     * @param input  A String to process.
     *
     * @return The modified String.
     */
    private String parseCharactersFromString(String input) {
        input = strings.substitute("&([^ ;\\s]*(\\s|$))", NitfDoc.AMPERSAND_ENTITY + "$1", input);
        for (int i = 0; i < this.characterMapKeys.length; i++) {
            List characterSearchList = (List) this.characterSearchMap.get(characterMapKeys[i]);
            String replaceChar = (String) this.characterReplaceMap.get(characterMapKeys[i]);
            input = strings.substituteFromList(characterSearchList, replaceChar, input, "gm");
        }
        return input;
    }
}
