package org.orbeon.oxf.processor;

import org.dom4j.Document;
import org.dom4j.Node;
import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.util.Base64;
import org.orbeon.oxf.xml.XMLUtils;
import org.orbeon.oxf.xml.XPathUtils;
import org.orbeon.oxf.xml.dom4j.LocationSAXWriter;
import org.xml.sax.ContentHandler;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class SignatureVerifierProcessor extends ProcessorImpl {

    public static final String SIGNATURE_DATA_URI = "http://www/orbeon.com/oxf/signature";

    public static final String SIGNATURE_PUBLIC_KEY_URI = "http://www/orbeon.com/oxf/signature/public-key";

    public static final String INPUT_PUBLIC_KEY = "public-key";

    public SignatureVerifierProcessor() {
        addInputInfo(new ProcessorInputOutputInfo(INPUT_DATA, SIGNATURE_DATA_URI));
        addInputInfo(new ProcessorInputOutputInfo(INPUT_PUBLIC_KEY, SIGNATURE_PUBLIC_KEY_URI));
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    public ProcessorOutput createOutput(String name) {
        ProcessorOutput output = new ProcessorImpl.ProcessorOutputImpl(getClass(), name) {

            public void readImpl(org.orbeon.oxf.pipeline.api.PipelineContext context, final ContentHandler contentHandler) {
                try {
                    Document pubDoc = readCacheInputAsDOM4J(context, INPUT_PUBLIC_KEY);
                    String pubString = XPathUtils.selectStringValueNormalize(pubDoc, "/public-key");
                    byte[] pubBytes = Base64.decode(pubString);
                    X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubBytes);
                    KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                    PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
                    Signature dsa = Signature.getInstance("SHA1withDSA");
                    dsa.initVerify(pubKey);
                    Document data = readInputAsDOM4J(context, INPUT_DATA);
                    Node sigDataNode = data.selectSingleNode("/signed-data/data/*");
                    String sig = XPathUtils.selectStringValueNormalize(data, "/signed-data/signature");
                    sigDataNode.detach();
                    Document sigData = XMLUtils.createDOM4JDocument();
                    sigData.add(sigDataNode);
                    dsa.update(XMLUtils.domToString(sigData).getBytes());
                    if (!dsa.verify(Base64.decode(sig))) throw new OXFException("Invalid Signature"); else {
                        LocationSAXWriter saw = new LocationSAXWriter();
                        saw.setContentHandler(contentHandler);
                        saw.write(sigData);
                    }
                } catch (Exception e) {
                    throw new OXFException(e);
                }
            }
        };
        addOutput(name, output);
        return output;
    }
}
