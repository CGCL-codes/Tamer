package test.sockets;

import java.util.Scanner;
import java.util.Set;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.NodeHandleSet;

public class testSocket {

    public static void main(String args[]) {
        Init.loadBindProperties(false);
        Node node = Init.bind();
        socketApp s = new socketApp(node);
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println("1. List neighbours:");
            System.out.println("2. Send to");
            System.out.print("Your choice: ");
            NodeHandleSet neighbours = s.endpoint.neighborSet(5);
            int c = reader.nextInt();
            reader.nextLine();
            if (neighbours.size() == 0) System.out.println("No neighbours available"); else switch(c) {
                case 1:
                    displayNeighbours(neighbours);
                    break;
                case 2:
                    displayNeighbours(neighbours);
                    System.out.print("Request neighbour: ");
                    int n = reader.nextInt() - 1;
                    reader.nextLine();
                    System.out.print("Your message: ");
                    String m = reader.nextLine();
                    StringBuilder m1 = new StringBuilder();
                    for (int i = 0; i < 400000; i++) m1.append(m);
                    s.send(neighbours.getHandle(n), m1.toString());
                    break;
            }
        }
    }

    public static void displayNeighbours(NodeHandleSet neighbours) {
        for (int i = 0; i < neighbours.size(); i++) {
            System.out.println((i + 1) + ": " + neighbours.getHandle(i));
        }
    }
}
