package app;

import java.io.*;
import java.util.*;

public class Network {

    Node rootNode;

    Network() {
        rootNode = new Node();
        rootNode.setName("root");
        rootNode.setProbability(0);
    }

    public void read(String fileName) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(new File(fileName)));
            String line = null;
            while ((line = input.readLine()) != null) {
                String[] words = line.split(" ");
                if (words.length == 2) {
                    Node node = new Node();
                    node.setName(words[0]);
                    node.setProbability(Double.parseDouble(words[1]));
                    rootNode.addChildNode(node);
                } else {
                    Node node = new Node();
                    node.setName(words[0]);
                    for (int i = 1; i < words.length; i++) {
                        boolean flagNodeName = false;
                        try {
                            double probability = Double.parseDouble(words[i]);
                            node.addParentProbability(probability);
                        } catch (NumberFormatException nfe) {
                            flagNodeName = true;
                        }
                        if (flagNodeName == true) {
                            Node parentNode = findNode(words[i]);
                            if (parentNode == null) throw new Exception("Parent node not found");
                            node.addParentNode(parentNode);
                            parentNode.addChildNode(node);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node findNode(String nodeName) {
        List<Node> visitedNodes = new ArrayList<Node>();
        Vector<Node> queue = new Vector<Node>();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            Node firstNode = queue.firstElement();
            queue.removeElement(firstNode);
            if (nodeName.equals(firstNode.getName())) return firstNode;
            visitedNodes.add(firstNode);
            for (Node childNode : firstNode.getChildNodes()) if (!visitedNodes.contains(childNode) && !queue.contains(childNode)) queue.addElement(childNode);
        }
        return null;
    }

    public void printNetworkGraph() {
        List<Node> visitedNodes = new ArrayList<Node>();
        Vector<Node> queue = new Vector<Node>();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            Node firstNode = queue.firstElement();
            queue.removeElement(firstNode);
            System.out.println("Node: " + firstNode + " probability: " + firstNode.getProbability());
            visitedNodes.add(firstNode);
            if (firstNode.getChildNodes().size() > 0) {
                System.out.print("Children: ");
                for (Node childNode : firstNode.getChildNodes()) {
                    System.out.print(childNode + " ");
                    if (!visitedNodes.contains(childNode) && !queue.contains(childNode)) queue.addElement(childNode);
                }
                System.out.println();
            }
        }
    }
}
