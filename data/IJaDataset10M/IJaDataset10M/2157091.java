package geometry;

import mechanism.Node;

public class Scaler {

    public Node k = new Node();

    public double lambda = 1;

    public Node toScreen(Node n) {
        return Node.scale(Node.sum(n, k), lambda);
    }

    public Node toMechanism(Node n) {
        return Node.sum(Node.scale(n, 1 / lambda), Node.scale(k, -1.));
    }

    public void zoom(Node screenpoint, double lambdaNew) {
        Node m = toMechanism(screenpoint);
        k = Node.sum(Node.scale(screenpoint, 1 / lambdaNew), Node.scale(m, -1));
        lambda = lambdaNew;
    }
}
