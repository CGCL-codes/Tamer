package org.jcryptool.crypto.flexiprovider.operations.ui.views.nodes.io;

public class SignatureIONode extends IONode {

    private InputNode inputNode;

    private SignatureNode signatureNode;

    public SignatureIONode() {
        super("Input/Signature");
        inputNode = new InputNode();
        this.addChild(inputNode);
        signatureNode = new SignatureNode();
        this.addChild(signatureNode);
    }

    public void setInput(String input) {
        inputNode.setInput(input);
    }

    public String getInput() {
        return inputNode.getInput();
    }

    public void setSignature(String signature) {
        signatureNode.setSignature(signature);
    }

    public String getSignature() {
        return signatureNode.getSignature();
    }
}
