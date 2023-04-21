package org.nodal.format.application.octet_stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.nodal.Types;
import org.nodal.filesystem.DocumentFormat;
import org.nodal.model.Node;
import org.nodal.model.NodeFactory;
import org.nodal.model.SequenceNode;
import org.nodal.model.Setter;
import org.nodal.type.NodeType;
import org.nodal.util.ByteSeqUtil;
import org.nodal.util.ConstraintFailure;
import org.nodal.util.IndexBoundsException;
import org.nodal.util.PropertyConstraintFailure;

public class Format extends DocumentFormat.AutoRegister {

    static {
        System.out.println("$$$ Loaded application/octet_stream");
    }

    private static Format singleton = new Format();

    public static DocumentFormat documentFormat() {
        return singleton;
    }

    public Format() {
        System.out.println("new " + this);
    }

    public String mimeType() {
        return "application/octet-stream";
    }

    public NodeType rootType() {
        return Types.OCTET_SEQUENCE;
    }

    private OctetEncoder encoder;

    public Encoder encoder() {
        if (encoder == null) {
            encoder = new OctetEncoder();
        }
        return encoder;
    }

    private static class OctetEncoder implements Encoder {

        public boolean isLossless() {
            return true;
        }

        public OutputStream encode(OutputStream stream, Node root) throws IOException {
            if (!Types.OCTET_SEQUENCE.accepts(root.nodeType())) {
                return null;
            }
            SequenceNode seq = root.asSequenceNode();
            try {
                byte[] bytes = (byte[]) seq.range(0, -1).get();
                stream.write(bytes);
                stream.flush();
            } catch (IndexBoundsException e) {
                throw new RuntimeException("Full range unsuccessful.");
            } catch (PropertyConstraintFailure e) {
                throw new RuntimeException("Unable to access full range.");
            }
            return stream;
        }
    }

    private OctetDecoder decoder;

    public Decoder decoder() {
        if (decoder == null) {
            decoder = new OctetDecoder();
        }
        return decoder;
    }

    private static class OctetDecoder implements Decoder {

        public boolean isLossless() {
            return true;
        }

        public Node decode(InputStream stream, NodeFactory factory) throws IOException {
            SequenceNode.Editor root = factory.createNode(Types.OCTET_SEQUENCE).editSequence();
            try {
                Setter append = root.insertAfter(-1);
                byte[] bytes = new byte[1024];
                int bytesRead;
                while ((bytesRead = stream.read(bytes)) != -1) {
                    append.set(ByteSeqUtil.create(bytes, bytesRead));
                }
            } catch (IndexBoundsException e) {
                throw new RuntimeException("Failed to append.");
            } catch (ConstraintFailure e1) {
                throw new RuntimeException("Failed to append.");
            }
            return root;
        }

        public String setURI(String uri) {
            return uri;
        }

        public List exceptions() {
            return null;
        }
    }
}

;
