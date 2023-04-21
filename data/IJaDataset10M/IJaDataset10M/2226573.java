package org.nodal.format.text.plain;

import org.nodal.filesystem.DocumentFormat;
import org.nodal.model.Node;
import org.nodal.model.NodeFactory;
import org.nodal.type.NodeType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.nodal.Types;
import org.nodal.type.SequenceType;
import org.nodal.type.Schema;
import org.nodal.type.Type;
import org.nodal.model.Getter;
import org.nodal.model.NodeContent;
import org.nodal.model.SequenceNode;
import org.nodal.model.Setter;
import org.nodal.util.ConstraintFailure;
import org.nodal.util.IndexBoundsException;
import org.nodal.util.PropertyConstraintFailure;

/**
 * <p>
 * This is the basic implementation of the text/plain schema encoder and
 * decoder.
 * 
 * <p>
 * The decoder takes in a stream and converts it into a Node sequence of
 * strings, while stripping the end of line character from each line. Using the
 * node factory passed to the decode function to create the root node, each
 * subsequent line of text is read in and stored as new Node appended to the
 * sequence.
 * 
 * <p>
 * e.g.
 * <p>
 * root Node --|
 * <p>- Node.content -> String -> "Line 1"
 * <p>- Node.content -> String -> "Line 2"
 * <p>- Node.content -> String -> "Line 3"
 * 
 * <p>
 * The encoder takes in the root Node of the Nodal representation of the text
 * document and then traverses the node tree, extracting the string content from
 * each node. It then writes the strings to an output stream, adding a line
 * seperator after each one. After all the nodes in the tree have been examined,
 * it flushes and returns the output stream.
 * 
 * <p>
 * e.g.
 * <p>
 * Assuming the above example as the input to the encoder, the output stream
 * will look like this:
 * <p>
 * Line1
 * <p>
 * Line2
 * <p>
 * Line3
 * 
 *  *
 * @author Maryam Razavi
 * @author Nelson Siu <nsiu@ece.ubc.ca>
 */
public class Format extends DocumentFormat.AutoRegister {

    static {
        System.out.println("$$$ Loaded text/plain");
    }

    private static Format singleton = new Format();

    public static DocumentFormat documentFormat() {
        return singleton;
    }

    private NodeType rootType;

    private Format() {
        System.out.println("new " + this);
    }

    public String mimeType() {
        return "text/plain";
    }

    public NodeType rootType() {
        if (rootType == null) {
            try {
                Schema.Editor typeDoc = Types.createSchema("memory://transient/testme.nls").edit();
                rootType = typeDoc.createSequenceType(Types.STRING);
            } catch (Type.CreationFailure e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        return rootType;
    }

    private SchemaEncoder encoder;

    public Encoder encoder() {
        if (encoder == null) {
            encoder = new SchemaEncoder();
        }
        return encoder;
    }

    private class SchemaEncoder implements DocumentFormat.Encoder {

        protected SequenceNode sSeq;

        protected Getter sSeqAll;

        public boolean isLossless() {
            return true;
        }

        /**
     * Encodes the Nodal representation of a text/plain document into an output
     * stream.
     * 
     * @var stream The output stream to write the result to.
     * @var root The root Node of the Nodal representation of the document.
     */
        public OutputStream encode(OutputStream stream, Node root) {
            Getter mySeqGetter = null;
            String myString = null;
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(stream));
            SequenceNode mySeq = root.asSequenceNode();
            int mySeqSize = mySeq.size();
            for (int i = 0; i < mySeqSize; i++) {
                try {
                    mySeqGetter = mySeq.item(i);
                } catch (IndexBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    Node myNode = (Node) mySeqGetter.get();
                    myString = myNode.asSequenceNode().range(0, -1).getString();
                } catch (PropertyConstraintFailure e) {
                    e.printStackTrace();
                }
                try {
                    br.write(myString);
                    br.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                br.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stream;
        }
    }

    private SchemaDecoder decoder;

    public Decoder decoder() {
        if (decoder == null) {
            decoder = new SchemaDecoder();
        }
        return decoder;
    }

    private class SchemaDecoder implements DocumentFormat.Decoder {

        protected Schema.Editor typeDoc;

        protected SequenceType stringSeqType;

        protected SequenceNode.Editor sSeqEd;

        protected NodeContent.Editor nodeEd;

        protected String lineBuffer;

        public boolean isLossless() {
            return true;
        }

        public String setURI(String uri) {
            return null;
        }

        /**
     * Decodes a text input stream and converts the file to a Nodal
     * representation.
     * 
     * @var stream The NodeType that will be assigned to the new Node.
     * @var root The NodeFactory from which the new nodal document will be
     *      created
     */
        public Node decode(InputStream stream, NodeFactory root) {
            nodeEd = root.createNode(rootType());
            sSeqEd = nodeEd.editSequence();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                Setter setter = sSeqEd.insertAfter(-1);
                while ((lineBuffer = br.readLine()) != null) {
                    SequenceNode.Editor str = sSeqEd.createNode(Types.STRING).editSequence();
                    str.insertAfter(-1).set(lineBuffer);
                    setter.set(str.bareNode());
                    System.out.println(str.bareNode());
                }
                nodeEd.commit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConstraintFailure e) {
                e.printStackTrace();
            }
            sSeqEd.commit();
            return sSeqEd.bareNode();
        }

        public List exceptions() {
            return null;
        }
    }
}

;
