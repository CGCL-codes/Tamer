package com.siemens.ct.exi.core;

import java.io.IOException;
import java.util.Enumeration;
import javax.xml.XMLConstants;
import com.siemens.ct.exi.EXIEncoder;
import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.exceptions.EXIException;
import com.siemens.ct.exi.grammar.event.EventType;
import com.siemens.ct.exi.util.MethodsBag;

/**
 * TODO Description
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20090414
 */
public class EXIEncoderPrefixAware extends EXIEncoderPrefixLess implements EXIEncoder {

    protected String lastSEprefix = null;

    public EXIEncoderPrefixAware(EXIFactory exiFactory) {
        super(exiFactory);
    }

    @Override
    protected void encodeQNamePrefix(String uri, String prefix) throws IOException {
        @SuppressWarnings("unchecked") Enumeration<String> prefixes4GivenURI = this.namespaces.getPrefixes(uri);
        if (uri.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
        } else if (prefixes4GivenURI.hasMoreElements()) {
            int numberOfPrefixes = 0;
            int id = -1;
            do {
                if (prefixes4GivenURI.nextElement().equals(prefix)) {
                    id = numberOfPrefixes;
                }
                numberOfPrefixes++;
            } while (prefixes4GivenURI.hasMoreElements());
            if (numberOfPrefixes > 1) {
                block.writeEventCode(id, MethodsBag.getCodingLength(numberOfPrefixes));
            }
        } else {
        }
    }

    @Override
    public void encodeStartElement(String uri, String localName, String prefix) throws EXIException {
        super.encodeStartElement(uri, localName, prefix);
        lastSEprefix = prefix;
    }

    @Override
    public void encodeNamespaceDeclaration(String uri, String prefix) throws EXIException {
        super.encodeNamespaceDeclaration(uri, prefix);
        assert (fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_PREFIX));
        try {
            int ec2 = currentRule.get2ndLevelEventCode(EventType.NAMESPACE_DECLARATION, fidelityOptions);
            encode2ndLevelEventCode(ec2);
            block.writeUri(uri);
            block.writePrefix(prefix, uri);
            if (prefix.equals(lastSEprefix)) {
                block.writeBoolean(true);
            } else {
                block.writeBoolean(false);
            }
        } catch (IOException e) {
            throw new EXIException(e);
        }
    }
}
