package edu.ksu.cis.bnj.ver3.inference.exact.mtls;

import edu.ksu.cis.bnj.ver3.core.CPF;

public class SetLambdaMessage extends Message {

    public MessageCoordinator _MessageCoordinator;

    public CPF _Lambda;

    public SetLambdaMessage(MClique C, MessageCoordinator MC, CPF Lambda) {
        _Clique = C;
        _MessageCoordinator = MC;
        _Lambda = Lambda;
    }

    public void run() {
        _Clique.localSetLambdaMessage(_Lambda, _MessageCoordinator);
    }
}
